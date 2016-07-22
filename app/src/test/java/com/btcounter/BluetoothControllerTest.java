package com.btcounter;

import com.btcounter.bt.BluetoothController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by daba on 2016-07-22.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BluetoothControllerTest {

    private BluetoothController bluetoothController;

    @Before
    public void prepare() {
        bluetoothController = new BluetoothController(RuntimeEnvironment.application);
    }

    @Test
    public void start_scan() {
        // TODO
    }
}