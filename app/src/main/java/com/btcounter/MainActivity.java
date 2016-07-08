package com.btcounter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.btcounter.bikelogic.MeasurementController;
import com.btcounter.bt.BluetoothController;
import com.btcounter.fragments.DrawerFragment;
import com.btcounter.fragments.MainFragment;
import com.btcounter.settings.SettingsManager;
import com.btcounter.utils.PermissionHelper;

import static com.btcounter.bt.BluetoothController.DATA_CADENCE;

/**
 * Created by daba on 2016-06-01.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final int SETTINGS_REQUEST = 1;
    private static final int LOCATION_PERMISSION_REQUEST = 100;

    private SettingsManager settingsManager;
    private BluetoothController bluetoothController;
    private MeasurementController measurementController;
    private PowerManager.WakeLock wakeLock;
    private MainFragment mainFragment;
    private DrawerFragment drawerFragment;

    private float odo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMainFragment();
        addDrawerFragment();
        initializeSettingsManager();
        resetSpeedText();
        checkPermissions();
        loadOdo();
        keepScreenOnAndDim();

        if (!isWheelSizeSettingValid()) {
            Toast.makeText(this, R.string.settings_wheel_size_invalid, Toast.LENGTH_SHORT).show();
        }
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
                .add(R.id.content_frame, mainFragment)
                .commit();
    }

    private void addDrawerFragment() {
        drawerFragment = new DrawerFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_drawer, drawerFragment)
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

    private void initializeSettingsManager() {
        settingsManager = new SettingsManager(this);
    }

    private void setButtonsState(boolean enabled) {
        if (isDrawerFragmentActive()) {
            drawerFragment.setButtonsState(enabled);
        }
    }

    private boolean isMainFragmentActive() {
        return mainFragment != null && mainFragment.isAdded();
    }

    private boolean isDrawerFragmentActive() {
        return drawerFragment != null && drawerFragment.isAdded();
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

    private void keepScreenOnAndDim() {
        PowerManager powerManager = (PowerManager)getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
    }

    private void saveOdo() {
        if (measurementController != null) {
            settingsManager.appendOdoAndSave(measurementController.getDistance());
        }
    }

    private void updateOdo(int odo) {
        if (isMainFragmentActive()) {
            runOnUiThread(() -> mainFragment.updateOdo(odo));
        }
    }

    public void onPrepareClick(View view) {
        prepareMeasurement();
    }

    public void onTickClick(View view) {
        measurementController.notifyWheelRotationTime(180);
    }

    public void onCadenceTick(View view) {
        measurementController.notifyCrankRotation();
    }

    public void onStartClick(View view) {
        startScan();
    }

    public void onStopClick(View view) {
        if (bluetoothController != null) {
            bluetoothController.stopScan();
            bluetoothController.closeGatt();
        }
    }

    private void startScan() {
        bluetoothController = new BluetoothController(this);
        bluetoothController.setListener(new BluetoothController.Listener() {
            @Override
            public void log(final String message) {
                if (isDrawerFragmentActive()) {
                    runOnUiThread(() -> drawerFragment.addLog(message));
                }
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
                appendToOdoAndUpdate(distance);
            }

            @Override
            public void refreshCadence(int cadence) {
                if (isMainFragmentActive()) {
                    runOnUiThread(() -> mainFragment.updateCadence(cadence));
                }
            }
        });
    }

    private void appendToOdoAndUpdate(float distance) {
        updateOdo((int)(odo + distance));
    }

    private float getWheelSize() {
        return settingsManager.getWheelSize();
    }

    private void loadOdo() {
        odo = settingsManager.getOdo();
    }
}
