package com.btcounter.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.btcounter.model.ChartPair;

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
    private static final float CADENCE_MULTIPLER = 2;

    private final ArrayList<ChartPair> data = new ArrayList<>();
    private Paint paintSpeed;
    private Paint paintCadence;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setPaintSpeed(Paint paintSpeed) {
        this.paintSpeed = paintSpeed;
    }

    public void setPaintCadence(Paint paintCadence) {
        this.paintCadence = paintCadence;
    }

    public void setData(ArrayList<ChartPair> chartPairs) {
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
            PointF tempCadence = null;

            for (ChartPair pair : data) {
                if (tempSpeed == null) {
                    tempSpeed = createPoint(left, pair, true);
                    tempCadence = createPoint(left, pair, false);
                    continue;
                }

                PointF positionSpeed = createPoint(left, pair, true);
                PointF positionCadence = createPoint(left, pair, false);
                drawLine(canvas, tempSpeed, positionSpeed, paintSpeed);
                drawLine(canvas, tempCadence, positionCadence, paintCadence);

                tempSpeed = createPoint(left, pair, true);
                tempCadence = createPoint(left, pair, false);

                left += ratio;
            }
        }
    }

    public void drawLine(Canvas canvas, PointF a, PointF b, Paint paint) {
        if (canvas != null) {
            canvas.drawLine(a.x, a.y, b.x, b.y, paint);
        }
    }

    public PointF createPoint(float left, ChartPair pair, boolean isSpeed) {
        return new PointF(left,
                getYPosition(
                        isSpeed ? pair.speed : pair.cadence,
                        isSpeed ? SPEED_MULTIPLER : CADENCE_MULTIPLER));
    }

    public float getRatio() {
        return listener.getViewWidth() / (float)data.size();
    }

    public float getYPosition(float value, float mulipler) {
        return listener.getViewHeight() - (value * mulipler);
    }
}
