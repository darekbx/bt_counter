package com.btcounter;

import android.bluetooth.BluetoothGattCharacteristic;

import com.btcounter.bt.BluetoothController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by daba on 2016-07-22.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BluetoothControllerTest {

    private BluetoothController bluetoothController;
    private BluetoothController.Listener listener;

    @Before
    public void prepare() {
        bluetoothController = new BluetoothController(RuntimeEnvironment.application);
        listener = mock(BluetoothController.Listener.class);
        bluetoothController.setListener(listener);
    }

    @Test
    public void distribute_characteristic() {
        BluetoothGattCharacteristic characteristic = createCharacteristic();

        bluetoothController.setIsDebug(false);
        bluetoothController.distributeCharacteristic(characteristic);

        verify(listener, times(1)).onData(BluetoothController.DATA_CADENCE);
        verify(listener, times(0)).onDebug(any());
    }

    @Test
    public void debug_characteristic() {
        BluetoothGattCharacteristic characteristic = createCharacteristic();

        bluetoothController.setIsDebug(true);
        bluetoothController.distributeCharacteristic(characteristic);

        verify(listener, times(1)).onData(BluetoothController.DATA_CADENCE);
        verify(listener, times(1)).onDebug("FLOAT: 200000.0\nSFLOAT: -704.0\nSINT8: 64\nSINT16: 3392\nSINT32: 200000\nUINT8: 64\nUINT16: 3392\nUINT32: 200000\nBYTE: 64,13,3,0,");
    }

    private BluetoothGattCharacteristic createCharacteristic() {
        BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(
                UUID.randomUUID(),
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);
        characteristic.setValue(BluetoothController.DATA_CADENCE, BluetoothGattCharacteristic.FORMAT_SINT32, 0);
        return characteristic;
    }
}