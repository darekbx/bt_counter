package com.btcounter;

import com.btcounter.utils.TimeUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * Created by daba on 2016-07-22.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TimeUtilsTest {

    @Test
    public void extract_time() {

        assertEquals(TimeUtils.extractTime(100), " 01:40");
    }
}
