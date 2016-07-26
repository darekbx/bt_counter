package com.btcounter.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.btcounter.model.ChartPair;

import java.util.ArrayList;

/**
 * Created by daba on 2016-07-22.
 */
public class ChartView extends View {

    private static final float SPEED_MULTIPLER = 9;
    private static final float CADENCE_MULTIPLER = 2;

    private ArrayList<ChartPair> data;
    private Paint paintSpeed;
    private Paint paintCadence;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initializePaintSpeed();
        initializePaintCadence();
    }

    public void invalidateChart(ArrayList<ChartPair> data) {
        this.data = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (data == null) {
            return;
        }
        super.onDraw(canvas);
        drawChart(canvas);
    }

    private void drawChart(Canvas canvas) {
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

            PointF positionSpeed = createPoint(left, pair,  true);
            PointF positionCadence = createPoint(left, pair,  false);
            canvas.drawLine(tempSpeed.x, tempSpeed.y, positionSpeed.x, positionSpeed.y, paintSpeed);
            canvas.drawLine(tempCadence.x, tempCadence.y, positionCadence.x, positionCadence.y, paintCadence);

            tempSpeed = createPoint(left, pair, true);
            tempCadence = createPoint(left, pair, false);

            left += ratio;
        }
    }

    private PointF createPoint(float left, ChartPair pair, boolean isSpeed) {
        return new PointF(left,
                getYPosition(
                    isSpeed ? pair.speed : pair.cadence,
                    isSpeed ? SPEED_MULTIPLER : CADENCE_MULTIPLER));
    }

    private float getRatio() {
        return (float)getWidth() / (float)data.size();
    }

    private float getYPosition(float value, float mulipler) {
        return getHeight() - (value * mulipler);
    }

    private void initializePaintSpeed() {
        paintSpeed = new Paint();
        paintSpeed.setAntiAlias(true);
        paintSpeed.setColor(Color.WHITE);
        paintSpeed.setStrokeWidth(2f);
    }

    private void initializePaintCadence() {
        paintCadence = new Paint();
        paintCadence.setAntiAlias(true);
        paintCadence.setColor(Color.GRAY);
        paintCadence.setStrokeWidth(2f);
    }
}
