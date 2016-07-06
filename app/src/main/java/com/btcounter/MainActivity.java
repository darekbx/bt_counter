package com.btcounter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.btcounter.bikelogic.MeasurementController;
import com.btcounter.bt.BluetoothController;
import com.btcounter.fragments.MainFragment;
import com.btcounter.settings.SettingsManager;
import com.btcounter.utils.PermissionHelper;

import static com.btcounter.bt.BluetoothController.DATA_CADENCE;

/**
 * Created by daba on 2016-06-01.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    public static final int LOCATION_PERMISSION_REQUEST = 100;
    private static final int SETTINGS_REQUEST = 1;

    private BluetoothController bluetoothController;
    private MeasurementController measurementController;
    private PowerManager.WakeLock wakeLock;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMainFragment();
        initializeToolbar();
        resetSpeedText();
        checkPermissions();

        if (!isWheelSizeSettingValid()) {
            Toast.makeText(this, R.string.settings_wheel_size_invalid, Toast.LENGTH_SHORT).show();
        }

        keepScreenOnAndDim();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setButtonsState(true);
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothController != null) {
            bluetoothController.stopScan();
            bluetoothController.closeGatt();
            bluetoothController.setListener(null);
        }
        saveOdo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQUEST) {
            // TODO: refresh app when settigs has changed
        }
    }

    private void addMainFragment() {
        mainFragment = new MainFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, mainFragment)
                .commit();
    }

    private void checkPermissions() {
        if (PermissionHelper.hasLocationPermission(this)) {
            if (!PermissionHelper.hasGpsPermission(this)) {
                Toast.makeText(this, R.string.error_permission_gps, Toast.LENGTH_SHORT).show();
                setButtonsState(false);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            setButtonsState(false);
        }
    }

    private void setButtonsState(boolean enabled) {
        if (isMainFragmentActive()) {
            mainFragment.setButtonsState(enabled);
        }
    }

    private boolean isMainFragmentActive() {
        return mainFragment != null && mainFragment.isAdded();
    }

    private void openSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivityForResult(settingsIntent, SETTINGS_REQUEST);
    }

    private void resetSpeedText() {
        if (isMainFragmentActive()) {
            mainFragment.updateSpeed(0);
        }
    }

    private boolean isWheelSizeSettingValid() {
        return getWheelSize() != 0;
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void keepScreenOnAndDim() {
        PowerManager powerManager = (PowerManager)getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
    }

    private void saveOdo() {
        if (measurementController != null) {
            new SettingsManager(this).appendOdoAndSave(measurementController.getDistance());
        }
    }

    public void onPrepareClick(View view) {
        prepareMeasurement();
    }

    public void onTickClick(View view) {
        measurementController.notifyWheelRotationTime(480);
    }

    public void onCadenceTick(View view) {
        measurementController.notifyCrankRotation();
    }

    public void onStartClick(View view) {
        startScan();
    }

    public void onStopClick(View view) {
        bluetoothController.stopScan();
        bluetoothController.closeGatt();
    }

    private void startScan() {
        bluetoothController = new BluetoothController(this);
        bluetoothController.setListener(new BluetoothController.Listener() {
            @Override
            public void log(final String message) {
                runOnUiThread(() -> mainFragment.addLog(message));
            }

            @Override
            public void ready() {
                prepareMeasurement();
            }

            @Override
            public void onData(int value) {
                switch (value) {
                    case DATA_CADENCE:
                        if (measurementController != null) {
                            measurementController.notifyCrankRotation();
                        }
                        break;
                    default:
                        if (measurementController != null) {
                            measurementController.notifyWheelRotationTime(value);
                        }
                        break;
                }
            }
        });
        bluetoothController.startScan();
    }

    private void prepareMeasurement() {
        float wheelSize = getWheelSize();
        measurementController = new MeasurementController(wheelSize);
        measurementController.setListener(new MeasurementController.Listener() {
            @Override
            public void refreshSpeed(float speed) {
                if (isMainFragmentActive()) {
                    runOnUiThread(() -> mainFragment.updateSpeed(speed));
                }
            }

            @Override
            public void refreshDistance(float distance) {
                if (isMainFragmentActive()) {
                    runOnUiThread(() -> mainFragment.updateDistance(distance));
                }
            }

            @Override
            public void refreshCadence(int cadence) {
                if (isMainFragmentActive()) {
                    runOnUiThread(() -> mainFragment.updateCadence(cadence));
                }
            }
        });
    }

    private float getWheelSize() {
        return new SettingsManager(this).getWheelSize();
    }
}
