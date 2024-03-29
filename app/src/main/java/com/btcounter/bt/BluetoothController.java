package com.btcounter.bt;

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
import android.content.Context;

import java.util.UUID;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by daba on 2016-05-31.
 */
public class BluetoothController {

    public static final int DATA_CADENCE = 200000;

    private static final String BT_DEVICE_NAME = "BlunoV1.8";
    private static final String BT_SERVICE = "0000dfb0-0000-1000-8000-00805f9b34fb";
    private static final String BT_CHARACTERISTICS = "0000dfb1-0000-1000-8000-00805f9b34fb";

    public interface Listener {
        void log(String message);
        void ready();
        void onData(int value);
        void onDebug(String message);
    }

    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner leScanner;
    private BluetoothGatt gatt;
    private Listener listener;
    private boolean isDebug;
    private boolean isScanStartedByUser = false;

    public BluetoothController(Context context) {
        this.context = context;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public void startScanByUser() {
        isScanStartedByUser = true;
        startScan();
    }

    public void startScan() {
        final BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Observable
                    .create((Subscriber<? super Object> subscriber) -> {
                        leScanner = bluetoothAdapter.getBluetoothLeScanner();
                        leScanner.startScan(scanCallback);
                        subscriber.onNext(null);
                        subscriber.onCompleted();
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((Object o) -> log("Start scanning"));
        }
    }

    public void stopScanByUser() {
        isScanStartedByUser = false;
        stopScan();
    }

    public void stopScan() {
        if (leScanner != null) {
            leScanner.stopScan(scanCallback);
            log("Stop scanning");
        }
    }

    public void closeGatt() {
        if (gatt != null) {
            gatt.close();
        }
    }

    private void log(String message) {
        listener.log(message);
    }

    private ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            String message = "";
            switch (errorCode) {
                case SCAN_FAILED_ALREADY_STARTED:
                    message = "already started";
                    break;
                case SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                    message = "application registration failed";
                    break;
                case SCAN_FAILED_FEATURE_UNSUPPORTED:
                    message = "feature unsupported";
                    break;
                case SCAN_FAILED_INTERNAL_ERROR:
                    message = "internal error";
                    break;
            }
            log("Scannig failed: " + message);
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (result != null) {
                BluetoothDevice device = result.getDevice();
                if (device != null && device.getName() != null) {
                    log("Found: " + device.getName() + " (" + result.getRssi() + "dBm)");
                    if (device.getName().equals(BT_DEVICE_NAME)) {
                        log("Connecting GATT to: " + device.getName());
                        gatt = device.connectGatt(context, false, gattCallback);
                        if (gatt != null) {
                            gatt.connect();
                        } else {
                            log("Failed to connect GATT");
                        }
                        stopScan();
                    }
                }
            }
        }
    };

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTING:
                    log("GATT is connecting");
                    break;
                case BluetoothProfile.STATE_CONNECTED:
                    log("GATT is connected");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    log("GATT is disconnecting");
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    if (isScanStartedByUser) {
                        log("GATT is disconnected, retrying...");
                        startScan();
                    } else {
                        log("GATT is disconnected");
                    }
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                log("GATT is discovered");
                BluetoothGattService service = gatt.getService(UUID.fromString(BT_SERVICE));
                if (service != null) {
                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BT_CHARACTERISTICS));
                    if (characteristic != null) {
                        log("GATT characteristic found");
                        if (gatt.setCharacteristicNotification(characteristic, true)) {
                            log("GATT set notification success");
                            listener.ready();
                        } else {
                            log("GATT unable to set notification");
                        }
                    }
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            distributeCharacteristic(characteristic);
        }
    };

    public void distributeCharacteristic(BluetoothGattCharacteristic characteristic) {
        float floatValue = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_FLOAT, 0);
        if ((int)floatValue == DATA_CADENCE) {
            listener.onData(DATA_CADENCE);
        } else {
            listener.onData((int)floatValue);
        }
        if (isDebug) {
            debugCharacteristic(characteristic);
        }
    }

    private void debugCharacteristic(BluetoothGattCharacteristic characteristic) {
        float floatValue = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_FLOAT, 0);
        float sFloatValue = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_SFLOAT, 0);

        int sInt8Value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 0);
        int sInt16Value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16, 0);
        int sInt32Value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT32, 0);

        int uInt8Value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
        int uInt16Value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 0);
        int uInt32Value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0);

        String stringValue = characteristic.getStringValue(0);
        byte[] byteValue = characteristic.getValue();

        StringBuilder byteString = new StringBuilder();
        if (byteValue != null) {
            for (int i = 0, count = byteValue.length; i < count; i++) {
                byteString.append(byteValue[i]).append(',');
            }
        }

        final String newLine = "\n";
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder
                .append("FLOAT: ").append(floatValue).append(newLine)
                .append("SFLOAT: ").append(sFloatValue).append(newLine)

                .append("SINT8: ").append(sInt8Value).append(newLine)
                .append("SINT16: ").append(sInt16Value).append(newLine)
                .append("SINT32: ").append(sInt32Value).append(newLine)

                .append("UINT8: ").append(uInt8Value).append(newLine)
                .append("UINT16: ").append(uInt16Value).append(newLine)
                .append("UINT32: ").append(uInt32Value).append(newLine)

                .append("BYTE: ").append(byteString.toString());

        if (listener != null) {
            listener.onDebug(messageBuilder.toString());
        }
    }
}