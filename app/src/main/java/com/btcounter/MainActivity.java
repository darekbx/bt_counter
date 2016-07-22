package com.btcounter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.btcounter.bikelogic.ChartController;
import com.btcounter.bikelogic.MeasurementController;
import com.btcounter.bt.BluetoothController;
import com.btcounter.fragments.ChartFragment;
import com.btcounter.fragments.DrawerFragment;
import com.btcounter.fragments.MainFragment;
import com.btcounter.settings.SettingsManager;
import com.btcounter.utils.PermissionHelper;
import com.btcounter.utils.TimeUtils;

import java.util.Random;

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
    private ChartController chartLogic;
    private PowerManager.WakeLock wakeLock;

    private MainFragment mainFragment;
    private DrawerFragment drawerFragment;
    private ChartFragment chartFragment;

    private float odo;
    private float maxSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMainFragment();
        addDrawerFragment();
        addChartFragment();
        initializeSettingsManager();
        initializeChartLogic();
        resetSpeedText();
        checkPermissions();
        loadOdo();
        loadMaxSpeed();
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
        if (measurementController != null) {
            measurementController.unsubscribe();
            measurementController.setListener(null);
        }
        if (chartLogic != null) {
            chartLogic.stopListening();
        }
        saveOdo();
        saveDistance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wakeLock != null) {
            wakeLock.acquire();
        }
        initializeDisplay();
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
            switch (resultCode) {
                case SettingsActivity.TRIP_DISTANCE_RESULT:
                    resetTripDistance();
                    break;
                case SettingsActivity.WHEEL_SIZE_RESULT:
                    updateWheelSize();
                    break;
                case SettingsActivity.ODO_RESULT:
                    resetOdo();
                    break;
                case SettingsActivity.MAX_SPEED_RESULT:
                    updateMaxSpeed();
                    break;
            }
            // TODO: refresh isDebug when settigs has changed
        }
    }

    @Override
    public void onBackPressed() {
        showConfirmExitDialog();
    }

    private void initializeChartLogic() {
        chartLogic = new ChartController();
        chartLogic.startListening();
    }

    private void resetTripDistance() {
        float distance = settingsManager.getDistance();
        invalidateDistance(distance);
        if (measurementController != null) {
            measurementController.setDistance(distance);
        }
    }

    private void resetOdo() {
        odo = settingsManager.getOdo();
        invalidateOdo((int)odo);
    }

    private void updateWheelSize() {
        if (measurementController != null) {
            measurementController.setWheelSize(settingsManager.getWheelSize());
        }
    }

    private void updateMaxSpeed() {
        maxSpeed = settingsManager.getMaxSpeed();
        invalidateMaxSpeed();
    }

    private void initializeDisplay() {
        if (bluetoothController == null) {
            invalidateOdo((int) odo);
            invalidateMaxSpeed();
            invalidateAverageSpeed(0);
            invalidateDistance(settingsManager.getDistance());
        }
    }

    private void showConfirmExitDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_exit_title)
                .setPositiveButton(R.string.yes, (DialogInterface dialog, int which) -> super.onBackPressed())
                .setNegativeButton(R.string.no, null)
                .show();
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

    private void addChartFragment() {
        chartFragment = new ChartFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.chart_frame, chartFragment)
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
            mainFragment.invalidateSpeed(0);
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

    private void saveDistance() {
        if (measurementController != null) {
            settingsManager.saveDistance(measurementController.getDistance());
        }
    }

    private void invalidateOdo(int odo) {
        if (isMainFragmentActive()) {
            runOnUiThread(() -> mainFragment.invalidateOdo(odo));
        }
    }

    private void invalidateSpeed(float speed) {
        if (isMainFragmentActive()) {
            runOnUiThread(() -> mainFragment.invalidateSpeed(speed));
        }
    }

    private void invalidateMaxSpeed() {
        if (isMainFragmentActive()) {
            runOnUiThread(() -> mainFragment.invalidateMaxSpeed(maxSpeed));
        }
    }

    private void invalidateAverageSpeed(float averageSpeed) {
        if (isMainFragmentActive()) {
            runOnUiThread(() -> mainFragment.invalidateAverageSpeed(averageSpeed));
        }
    }

    private void invalidateDistance(float distance) {
        if (isMainFragmentActive()) {
            runOnUiThread(() -> mainFragment.invalidateDistance(distance));
        }
    }

    private void invalidateTime(long ticks) {
        if (isMainFragmentActive()) {
            final String time = TimeUtils.extractTime(ticks);
            runOnUiThread(() -> mainFragment.updateToolbarTitle(getString(R.string.session_time) + time));
        }
    }

    private void updateMaxSpeed(float speed) {
        if (speed > maxSpeed) {
            maxSpeed = speed;
            saveMaxSpeed();
            invalidateMaxSpeed();
        }
    }

    private void saveMaxSpeed() {
        settingsManager.saveMaxSpeed(maxSpeed);
    }

    private void appendToOdoAndUpdate(float distance) {
        invalidateOdo((int)(odo + distance));
    }

    private float getWheelSize() {
        return settingsManager.getWheelSize();
    }

    private void loadOdo() {
        odo = settingsManager.getOdo();
    }

    private void loadMaxSpeed() {
        maxSpeed = settingsManager.getMaxSpeed();
    }

    public void onPrepareClick(View view) {
        prepareMeasurement();
    }

    public void onTickClick(View view) {
        measurementController.notifyWheelRotationTime(new Random().nextInt(800) + 100);
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
        bluetoothController.setIsDebug(settingsManager.isDebugMode());
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

            @Override
            public void onDebug(String message) {
                if (isMainFragmentActive()) {
                    runOnUiThread(() -> mainFragment.updateDebug(message));
                }
            }
        });
        bluetoothController.startScan();
    }

    private void prepareMeasurement() {
        float wheelSize = getWheelSize();
        measurementController = new MeasurementController(wheelSize);
        measurementController.setDistance(settingsManager.getDistance());
        measurementController.setListener(new MeasurementController.Listener() {
            @Override
            public void refreshSpeed(float speed) {
                invalidateSpeed(speed);
                updateMaxSpeed(speed);
            }

            @Override
            public void refreshDistance(float distance) {
                invalidateDistance(distance);
                appendToOdoAndUpdate(distance);
            }

            @Override
            public void refreshAverageSpeed(float averageSpeed) {
                invalidateAverageSpeed(averageSpeed);
            }

            @Override
            public void refreshCadence(int cadence) {
                if (isMainFragmentActive()) {
                    runOnUiThread(() -> mainFragment.invalidateCadence(cadence));
                }
            }

            @Override
            public void refreshTime(long time) {
                invalidateTime(time);
            }
        });
    }
}