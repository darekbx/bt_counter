package com.btcounter;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.UUID;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TestActivity extends Activity {

    public static class TmobileIdReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    @Deprecated
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                addToLog("onReceive " + device.getAddress() + " - " + device.getName());
                if (device.getAddress().equals("7E:BE:3A:04:2E:89")) {
                    addToLog("Connecting to: " + device.getName());
                    gatt = device.connectGatt(TestActivity.this, true, callback);
                }
            }
        }
    };

    @Deprecated
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (device != null) {
                addToLog("onLeScan " + device.getName());
            }
        }
    };

    public BluetoothGattCallback callback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            if (newState == BluetoothProfile.STATE_CONNECTING) {
                addToLog("Gatt connecting...");
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                addToLog("Gatt connected");
                gatt.discoverServices();
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                addToLog("Gatt discovered");

                BluetoothGattService service = gatt.getService(UUID.fromString("0000dfb0-0000-1000-8000-00805f9b34fb"));
                if (service != null) {
                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("0000dfb1-0000-1000-8000-00805f9b34fb"));
                    if (characteristic != null) {
                        addToLog("Gatt characteristic found");
                        gatt.setCharacteristicNotification(characteristic, true);
                        gatt.readCharacteristic(characteristic);
                    }
                }

                /*addToLog("Gatt services: " + gatt.getServices());

                for (BluetoothGattService service : gatt.getServices()) {
                    addToLog("Gatt service: " + service.getUuid());
                    addToLog("Gatt characteristics: " + service.getCharacteristics());
                }*/
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            addToLog("onCharacteristicRead " + status);

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            addToLog("onCharacteristicChanged " + characteristic);
        }
    };

    private ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            addToLog("onScanFailed " + errorCode);
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (result.getDevice() != null && result.getDevice().getName() != null) {
                addToLog("onScanResult device name: " + result.getDevice().getName());
                if (result.getDevice().getName().toLowerCase().contains("pebble")) { // TODO: change name
                    addToLog("onScanResult device: " + result.getDevice().getName());

                    gatt = result.getDevice().connectGatt(TestActivity.this, false, callback);
                    gatt.connect();

                    stopScan();
                }
            }
        }
    };

    //@BindView(R.id.list)
    ListView listView;

    //@BindView(R.id.button_start)
    Button start;

    //@BindView(R.id.button_stop)
    Button stop;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner leScanner;
    private BluetoothGatt gatt;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);
        listView.setAdapter(adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1));


        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            start.setEnabled(false);
            stop.setEnabled(false);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_RQUEST);
        }

        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            start.setEnabled(false);
            stop.setEnabled(false);

            Toast.makeText(this, "Please enable location", Toast.LENGTH_SHORT).show();
        }
    }

    private static final int LOCATION_PERMISSION_RQUEST = 100;

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
    protected void onDestroy() {
        super.onDestroy();
    }

    private enum Type {
        DISCOVERY,
        SCAN,
        LE_SCAN
    }

    private static final Type TYPE = Type.SCAN;

    public void onStartClick(View view) {
        switch (TYPE) {
            case DISCOVERY: startDiscovery(); break;
            case SCAN: startScan(); break;
            case LE_SCAN: startLeScan(); break;
        }
    }

    public void onStopClick(View view) {
        switch (TYPE) {
            case DISCOVERY: cancelDiscovery(); break;
            case SCAN: stopScan(); break;
            case LE_SCAN: stopLeScan(); break;
        }
    }

    /**
     *
     */
    private void startScan() {
        final BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Observable
                    .create(new Observable.OnSubscribe<Object>() {
                        @Override
                        public void call(Subscriber<? super Object> subscriber) {
                            leScanner = bluetoothAdapter.getBluetoothLeScanner();
                            leScanner.startScan(scanCallback);
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            addToLog("Start scan...");
                        }
                    });
        }
    }
    private void stopScan() {
        if (leScanner != null) {
            leScanner.stopScan(scanCallback);
            addToLog("Stop scan");
        }
    }

    /**
     *
      */
    @Deprecated
    private void startDiscovery() {
        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        final BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.startDiscovery();
            addToLog("Start discovery...");
        }
    }
    @Deprecated
    private void cancelDiscovery() {
        unregisterReceiver(receiver);

        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
            addToLog("Stop discovery");
        }
    }

    /**
     *
     */
    @Deprecated
    private void startLeScan() {
        final BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Observable
                    .create(new Observable.OnSubscribe<Object>() {
                        @Override
                        public void call(Subscriber<? super Object> subscriber) {
                            bluetoothAdapter.startLeScan(leScanCallback);
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            addToLog("Start LE scan...");
                        }
                    });
        }
    }
    @Deprecated
    private void stopLeScan() {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.stopLeScan(leScanCallback);
            addToLog("Stop LE scan");
        }
    }

    private void addToLog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(message);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
