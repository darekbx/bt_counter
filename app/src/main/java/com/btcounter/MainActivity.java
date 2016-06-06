package com.btcounter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.btcounter.bt.BluetoothController;

/**
 * Created by daba on 2016-06-01.
 */
public class MainActivity extends Activity {

    private static final int LOCATION_PERMISSION_RQUEST = 100;

    private ListView listView;
    private Button start;
    private Button stop;

    private BluetoothController bluetoothController;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();

        Runnable runnable = () -> {

        };

        listView.setAdapter(adapter = new ArrayAdapter<>(this, R.layout.adapter_log));

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

        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            start.setEnabled(false);
            stop.setEnabled(false);
            Toast.makeText(this, "Please enable location", Toast.LENGTH_SHORT).show();
            return;
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

    private void bindViews() {
        listView = (ListView)findViewById(R.id.list);
        start = (Button) findViewById(R.id.button_start);
        stop = (Button)findViewById(R.id.button_stop);
    }

    public void onStartClick(View view) {
        startScan();
    }

    public void onStopClick(View view) {
        bluetoothController.stopScan();
        bluetoothController.closeGatt();
    }

    private void startScan() {
        bluetoothController = new BluetoothController(this, new BluetoothController.Listener() {
            @Override
            public void log(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.add(message);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void ready() {
                // TODO
            }

            @Override
            public void onData(int value) {
                // TODO
            }
        });
        bluetoothController.startScan();
    }
}
