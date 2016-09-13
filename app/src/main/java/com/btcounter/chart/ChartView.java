package com.btcounter.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by daba on 2016-07-22.
 */
public class ChartView extends View implements ChartDrawer.Listener {

    private ChartDrawer chartDrawer;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        chartDrawer = new ChartDrawer();
        chartDrawer.setPaintSpeed(initializePaintSpeed());
        chartDrawer.setPaintStop(initializePaintStop());
        chartDrawer.setListener(this);
    }

    public void invalidateChart(ArrayList<Float> data) {
        chartDrawer.setData(data);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        chartDrawer.drawChart(canvas);
    }

    private Paint initializePaintSpeed() {
        Paint paintSpeed = new Paint();
        paintSpeed.setAntiAlias(true);
        paintSpeed.setColor(Color.WHITE);
        paintSpeed.setStrokeWidth(2f);
        return paintSpeed;
    }

    private Paint initializePaintStop() {
        Paint paintStop = new Paint();
        paintStop.setAntiAlias(true);
        paintStop.setColor(Color.YELLOW);
        paintStop.setTextSize(20);
        return paintStop;
    }

    @Override
    public float getViewWidth() {
        return (float)getWidth();
    }

    @Override
    public float getViewHeight() {
        return (float)getHeight();
    }
}
