package com.btcounter;

import android.os.SystemClock;

import com.btcounter.bikelogic.MeasurementController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Monika on 2016-07-21.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MeasurementControllerTest {

    private MeasurementControllerMock measurementController;
    private MeasurementController.Listener listener;

    @Before
    public void prepare() {
        measurementController = spy(new MeasurementControllerMock(2000));
        listener = mock(MeasurementController.Listener.class);
        measurementController.setListener(listener);
    }

    @Test
    public void wheel_rotation() {

        measurementController.notifyWheelRotationTime(1000);

        verify(listener, times(1)).refreshSpeed(7.2f);
        verify(listener, times(1)).refreshDistance(0.002f);
        verify(listener, times(1)).refreshAverageSpeed(7.2f);
    }

    @Test
    public void crank_rotation() throws InterruptedException {

        measurementController.notifyCrankRotation();
        measurementController.notifyCrankRotation();

        verify(listener, times(1)).refreshCadence(600);
    }

    private class MeasurementControllerMock extends MeasurementController {

        private long time = 0;

        public MeasurementControllerMock(float wheelSize) {
            super(wheelSize);
        }

        @Override
        protected long getTime() {
            time += 100;
            return time;
        }
    }
}
