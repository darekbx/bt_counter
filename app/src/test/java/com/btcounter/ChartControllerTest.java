package com.btcounter;

import com.btcounter.bikelogic.ChartController;
import com.btcounter.model.ChartPair;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by daba on 2016-07-22.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChartControllerTest {

    private ChartController chartController;
    private ChartController.Listener listener;

    @Before
    public void prepare() {
        chartController = new ChartController();
        listener = mock(ChartController.Listener.class);
        chartController.setListener(listener);
    }

    @After
    public void destroy() {
        chartController.stopListening();
    }

    @Test
    public void start_listening() throws InterruptedException {

        when(listener.onCollect()).thenReturn(new ChartPair(10, 20));
        chartController.startListening();

        Thread.sleep(1500);

        ArrayList<ChartPair> pairs = new ArrayList<>();
        pairs.add(new ChartPair(10, 20));

        ArgumentCaptor<ArrayList> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(listener, times(1)).onData(captor.capture());

        assertEquals(captor.getValue().size(), 1);
        assertEquals(((ChartPair)captor.getValue().get(0)).cadence, 20, 0);
        assertEquals(((ChartPair)captor.getValue().get(0)).speed, 10, 0);
    }

    @Test
    public void stop_listening() throws InterruptedException {

        chartController.stopListening();

        Thread.sleep(1005);

        verify(listener, never()).onData(any());
    }
}