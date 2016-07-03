package com.btcounter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.btcounter.bikelogic.MeasurementController;
import com.btcounter.bt.BluetoothController;
import com.btcounter.view.StateLed;

import static com.btcounter.bt.BluetoothController.DATA_COUNTER;
import static com.btcounter.bt.BluetoothController.DATA_CADENCE;

/**
 * Created by daba on 2016-06-01.
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();
    private static final int LOCATION_PERMISSION_RQUEST = 100;

    private ListView listView;
    private Button start;
    private Button stop;
    private StateLed speedStateLed;
    private StateLed cadenceStateLed;
    private TextView speedText;
    private TextView distanceText;
    private TextView cadenceText;

    private BluetoothController bluetoothController;
    private MeasurementController measurementController;
    private ArrayAdapter<String> adapter;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();

        listView.setAdapter(adapter = new ArrayAdapter<>(this, R.layout.adapter_log));
        speedText.setText(getString(R.string.speed_format, 0, 0));

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            start.setEnabled(false);
            stop.setEnabled(false);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_RQUEST);
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            start.setEnabled(false);
            stop.setEnabled(false);
            Toast.makeText(this, "Please enable location", Toast.LENGTH_SHORT).show();
            return;
        }

        keepScreenOnAndDim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothController != null) {
            bluetoothController.stopScan();
            bluetoothController.closeGatt();
            bluetoothController.setListener(null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_RQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start.setEnabled(true);
                stop.setEnabled(true);
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
    }

    private void keepScreenOnAndDim() {
        PowerManager powerManager = (PowerManager)getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
    }

    private void bindViews() {
        listView = (ListView)findViewById(R.id.list);
        start = (Button) findViewById(R.id.button_start);
        stop = (Button)findViewById(R.id.button_stop);
        speedStateLed = (StateLed)findViewById(R.id.speed_state_led);
        cadenceStateLed = (StateLed)findViewById(R.id.cadence_state_led);
        speedText = (TextView) findViewById(R.id.text_speed);
        distanceText = (TextView) findViewById(R.id.distance_text);
        cadenceText = (TextView) findViewById(R.id.cadence_text);
    }

    public void onPrepareClick(View view) {
        prepareMeasurement();
    }

    public void onTickClick(View view) {
        speedStateLed.blink();
        measurementController.notifyWheelRotation();
    }

    public void onCadenceTick(View view) {
        cadenceStateLed.blink();
        measurementController.notifyCrankRotation();
    }

    public void onStartClick(View view) {
        startScan();
    }

    public void onStopClick(View view) {
        bluetoothController.stopScan();
        bluetoothController.closeGatt();
    }

    int counter = 0;

    void addLog(String message) {
        runOnUiThread(() -> {
            adapter.add(message);
            adapter.notifyDataSetChanged();
            listView.setSelection(listView.getCount() - 1);
        });
    }

    private void startScan() {
        bluetoothController = new BluetoothController(this);
        bluetoothController.setListener(new BluetoothController.Listener() {
            @Override
            public void log(final String message) {
                addLog(message);
            }

            @Override
            public void ready() {
                prepareMeasurement();
            }

            @Override
            public void onData(@BluetoothController.DataType int value) {
                switch (value) {
                    case DATA_COUNTER:
                        counter++;
                        runOnUiThread(() ->  cadenceText.setText("Counter: " + counter));
                        runOnUiThread(() -> speedStateLed.blink());
                        if (measurementController != null) {
                            measurementController.notifyWheelRotation();
                        }
                        break;
                    case DATA_CADENCE:
                        runOnUiThread(() -> cadenceStateLed.blink());
                        if (measurementController != null) {
                            measurementController.notifyCrankRotation();
                        }
                        break;
                }
            }
        });
        bluetoothController.startScan();
    }

    private void prepareMeasurement() {
        measurementController = new MeasurementController(2075d);
        measurementController.setListener(new MeasurementController.Listener() {
            @Override
            public void refreshSpeed(double speed, long interval) {
                int speedMod = (int)((speed - (int)speed) * 10);

                runOnUiThread(() -> distanceText.setText("Time: "+interval));

                runOnUiThread(() -> speedText.setText(getString(R.string.speed_format, (int)speed, speedMod)));
            }

            @Override
            public void refreshDistance(double distance) {
                //runOnUiThread(() -> distanceText.setText(getString(R.string.distance_format, distance)));
            }

            @Override
            public void refreshCadence(int cadence) {
                //runOnUiThread(() -> cadenceText.setText(getString(R.string.cadence_format, cadence)));
            }
        });
    }
}
