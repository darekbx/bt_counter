package com.btcounter.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by daba on 2016-07-26.
 */

public class ChartDrawer {

    public interface Listener {
        float getViewWidth();
        float getViewHeight();
    }

    private static final float SPEED_MULTIPLER = 9;

    private final ArrayList<Float> data = new ArrayList<>();
    private Paint paintSpeed;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setPaintSpeed(Paint paintSpeed) {
        this.paintSpeed = paintSpeed;
    }

    public void setData(ArrayList<Float> chartPairs) {
        synchronized (this.data) {
            this.data.clear();
            this.data.addAll(chartPairs);
        }
    }

    public void drawChart(Canvas canvas) {
        synchronized (this.data) {
            float ratio = getRatio();
            float left = 0f;
            PointF tempSpeed = null;

            for (float value : data) {
                if (tempSpeed == null) {
                    tempSpeed = createPoint(left, value);
                    continue;
                }

                PointF positionSpeed = createPoint(left, value);
                drawLine(canvas, tempSpeed, positionSpeed, paintSpeed);

                tempSpeed = createPoint(left, value);

                left += ratio;
            }
        }
    }

    public void drawLine(Canvas canvas, PointF a, PointF b, Paint paint) {
        if (canvas != null) {
            canvas.drawLine(a.x, a.y, b.x, b.y, paint);
        }
    }

    public PointF createPoint(float left, float value) {
        return new PointF(left, getYPosition(value, SPEED_MULTIPLER));
    }

    public float getRatio() {
        return listener.getViewWidth() / (float)data.size();
    }

    public float getYPosition(float value, float multipler) {
        return listener.getViewHeight() - (value * multipler);
    }
}
