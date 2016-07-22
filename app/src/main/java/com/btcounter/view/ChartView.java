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

        // TODO extaract methods
        float ratio = getWidth() / data.size();
        float left = 0f;

        PointF temp = null;

        for (ChartPair pair : data) {
            if (temp == null) {
                temp = new PointF(left, getYPosition(pair.speed));
                continue;
            }

            canvas.drawLine(temp.x, temp.y, left,  getYPosition(pair.speed), paintSpeed);

            temp = new PointF(left,  getYPosition(pair.speed));

            left += ratio;
        }
    }

    private float getYPosition(float value) {
        return getHeight() - (value * 5/* TODO Extract static final */);
    }

    private void initializePaintSpeed() {
        paintSpeed = new Paint();
        paintSpeed.setAntiAlias(true);
        paintSpeed.setColor(Color.WHITE);
    }

    private void initializePaintCadence() {
        paintCadence = new Paint();
        paintCadence.setAntiAlias(true);
        paintCadence.setColor(Color.YELLOW);
    }
}
