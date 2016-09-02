package com.btcounter.weather;

import com.btcounter.BuildConfig;
import com.btcounter.model.WeatherConditions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by daba on 2016-09-02.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WeatherConditionsControllerTest {

    private WeatherConditionsController.Listener listener;

    @Test
    public void get_weather_conditions() throws Exception {
        listener = mock(WeatherConditionsController.Listener.class);
        WeatherConditionsControllerMock weatherConditionsController
                = new WeatherConditionsControllerMock(listener);

        weatherConditionsController.schedule();

        Thread.sleep(2010);

        ArgumentCaptor<WeatherConditions> captor = ArgumentCaptor.forClass(WeatherConditions.class);
        verify(listener, times(1)).onData(captor.capture());

        assertNotNull(captor.getValue());
        assertTrue(captor.getValue().temperature > -20);

        weatherConditionsController.stop();
    }

    private class WeatherConditionsControllerMock extends WeatherConditionsController {

        public WeatherConditionsControllerMock(Listener listener) {
            super(listener);
        }

        @Override
        public int getInterval() {
            return 1;
        }
    }
}