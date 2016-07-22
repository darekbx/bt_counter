package com.btcounter;

import com.btcounter.settings.SettingsManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by daba on 2016-07-22.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SettingsManagerTest {

    private SettingsManager settingsManager;

    @Before
    public void prepare() {
        settingsManager = new SettingsManager(RuntimeEnvironment.application);
    }

    @Test
    public void append_and_save_odo() {

        settingsManager.appendOdoAndSave(10);

        assertEquals(10f, settingsManager.getOdo(), 0);
    }

    @Test
    public void get_wheel_size() {

        assertEquals(settingsManager.getWheelSize(), 0, 0);
    }

    @Test
    public void get_max_speed() {

        assertEquals(settingsManager.getMaxSpeed(), 0, 0);
    }

    @Test
    public void get_distance() {

        assertEquals(settingsManager.getDistance(), 0, 0);
    }

    @Test
    public void is_debug_mode() {

        assertEquals(settingsManager.isDebugMode(), false);
    }

    @Test
    public void save_max_speed() {

        settingsManager.saveMaxSpeed(10);

        assertEquals(settingsManager.getMaxSpeed(), 10, 0);
    }

    @Test
    public void save_distance() {

        settingsManager.saveDistance(10);

        assertEquals(settingsManager.getDistance(), 10, 0);
    }

    @Test
    public void save_odo() {

        settingsManager.saveOdo(10);

        assertEquals(settingsManager.getOdo(), 10, 0);
    }
}
