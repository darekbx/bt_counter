package com.btcounter.chart;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathDashPathEffect;
import android.graphics.PointF;
import android.util.Log;

import com.btcounter.utils.TimeUtils;

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
    private static final int STOP_TOP_PADDING = 5;
    private static final int MINIMUM_TIME = 5;
    private static final int STOP_HEIGHT = 60;

    private ArrayList<Float> data = new ArrayList<>();
    private Paint paintSpeed;
    private Paint paintStop;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setPaintSpeed(Paint paintSpeed) {
        this.paintSpeed = paintSpeed;
    }

    public void setPaintStop(Paint paintStop) {
        this.paintStop = paintStop;
    }

    public void setData(ArrayList<Float> chartPairs) {
        synchronized (this.data) {
            this.data = chartPairs;
        }
    }

    public void drawChart(Canvas canvas) {
        synchronized (this.data) {
            float ratio = getRatio();
            float left = 0f;
            PointF tempSpeed = null;

            float stopStart = 0, stopEnd = 0;
            int index = 0, stopTimeStart = 0, stopTimeEnd = 0;
            boolean drawStop = false;

            for (float value : data) {
                if (tempSpeed == null) {
                    tempSpeed = createPoint(left, value);
                    continue;
                }

                if (stopStart > 0f && value > 0f) {
                    stopEnd = left;
                    stopTimeEnd = index;
                    drawStop = true;
                }
                if (value == 0f && stopStart == 0f) {
                    stopStart = left;
                    stopTimeStart = index;
                }

                PointF positionSpeed = createPoint(left, value);
                drawLine(canvas, tempSpeed, positionSpeed, paintSpeed);

                tempSpeed = createPoint(left, value);

                left += ratio;
                index++;

                if (drawStop) {
                    drawStop(canvas, stopStart, stopEnd, stopTimeStart, stopTimeEnd);
                    stopStart = 0f;
                    stopEnd = 0f;
                    drawStop = false;
                }
            }
        }
    }

    public void drawStop(Canvas canvas, float stopStart, float stopEnd, int stopTimeStart, int stopTimeEnd) {
        int diff = stopTimeEnd - stopTimeStart;
        if (diff >= MINIMUM_TIME) {
            float position = calculatePosition(stopStart, stopEnd);
            float linePosition = calculateLinePosition(stopStart, stopEnd);

            if (canvas != null) {
                canvas.save();
                canvas.rotate(90, position, STOP_TOP_PADDING);
                canvas.drawText(TimeUtils.extractTime(diff), position, STOP_TOP_PADDING, paintStop);
                canvas.restore();
            }

            drawLine(canvas,
                    new PointF(linePosition, STOP_HEIGHT + STOP_TOP_PADDING),
                    new PointF(linePosition, listener.getViewHeight()),
                    paintStop);
        }
    }

    public float calculatePosition(float stopStart, float stopEnd) {
        int diff = (int) (stopEnd - stopStart);
        return stopStart + diff / 2 - 6;
    }

    public float calculateLinePosition(float stopStart, float stopEnd) {
        return stopStart + (stopEnd - stopStart) / 2;
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
