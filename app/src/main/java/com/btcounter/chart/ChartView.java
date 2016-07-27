package com.btcounter.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.btcounter.model.ChartPair;

import java.util.ArrayList;

/**
 * Created by daba on 2016-07-22.
 */
public class ChartView extends View implements ChartDrawer.Listener {

    private ChartDrawer chartDrawer;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        chartDrawer = new ChartDrawer();
        chartDrawer.setPaintCadence(initializePaintCadence());
        chartDrawer.setPaintSpeed(initializePaintSpeed());
        chartDrawer.setListener(this);
    }

    public void invalidateChart(ArrayList<ChartPair> data) {
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

    private Paint initializePaintCadence() {
        Paint paintCadence = new Paint();
        paintCadence.setAntiAlias(true);
        paintCadence.setColor(Color.GRAY);
        paintCadence.setStrokeWidth(2f);
        return paintCadence;
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
