package com.btcounter;

import com.btcounter.utils.PermissionHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;

/**
 * Created by daba on 2016-07-22.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PermissionHelperTest {

    @Test
    public void has_gps_permission() {

        assertFalse(PermissionHelper.hasGpsPermission(RuntimeEnvironment.application));
    }

    @Test
    public void has_location_permission() {

        assertFalse(PermissionHelper.hasLocationPermission(RuntimeEnvironment.application));
    }
}
