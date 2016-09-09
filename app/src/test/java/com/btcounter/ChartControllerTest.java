package com.btcounter;

import com.btcounter.bikelogic.ChartController;

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

        when(listener.onCollect()).thenReturn(10f);
        chartController.startListening();

        Thread.sleep(1500);

        ArrayList<Float> pairs = new ArrayList<>();
        pairs.add(10f);

        ArgumentCaptor<ArrayList> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(listener, times(1)).onData(captor.capture());

        assertEquals(captor.getValue().size(), 1);
        assertEquals(((Float)captor.getValue().get(0)), 10f, 0);
    }

    @Test
    public void stop_listening() throws InterruptedException {

        chartController.stopListening();

        Thread.sleep(1005);

        verify(listener, never()).onData(any());
    }

    @Test
    public void get_stop_summary() {

        ArrayList<Float> data = initializeData();
        chartController.setData(data);

        assertEquals(chartController.getStopSummary(), 60);
    }

    @Test
    public void get_summary() {

        ArrayList<Float> data = initializeData();
        chartController.setData(data);

        assertEquals(chartController.getSummary(), 260);
    }

    private ArrayList<Float> initializeData() {
        ArrayList<Float> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add((float) (Math.random() * 5 + 20));
        }
        for (int i = 0; i < 50; i++) {
            data.add(0f);
        }
        for (int i = 0; i < 100; i++) {
            data.add((float) (Math.random() * 5 + 20));
        }
        for (int i = 0; i < 10; i++) {
            data.add(0f);
        }
        return data;
    }
}