package com.btcounter;

import android.graphics.PointF;

import com.btcounter.chart.ChartDrawer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by daba on 2016-07-27.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChartDrawerTest {

    private ChartDrawer chartDrawer;
    private ChartDrawer.Listener listener;

    @Before
    public void prepare() {
        chartDrawer = spy(new ChartDrawer());
        listener = mock(ChartDrawer.Listener.class);
        chartDrawer.setListener(listener);
        chartDrawer.setData(getMockData());
    }

    @Test
    public void get_ratio() {

        when(listener.getViewWidth()).thenReturn(100f);

        assertEquals(chartDrawer.getRatio(), 50f, 0);
    }

    @Test
    public void get_y_position() {

        when(listener.getViewHeight()).thenReturn(200f);

        assertEquals(chartDrawer.getYPosition(20, 5), 100f, 0);
    }

    @Test
    public void create_point() {

        PointF pointF = chartDrawer.createPoint(10, 20);
        assertEquals(pointF.x, 10f, 0);
        assertEquals(pointF.y, -180f, 0);
    }

    @Test
    public void draw_chart() {

        chartDrawer.drawChart(null);

        when(listener.getViewHeight()).thenReturn(200f);
        when(listener.getViewWidth()).thenReturn(100f);

        verify(chartDrawer, times(1)).drawLine(any(), any(), any(), any());
    }

    private ArrayList<Float> getMockData() {
        ArrayList<Float> data = new ArrayList<>(2);
        data.add(20f);
        data.add(25f);
        return data;
    }
}
