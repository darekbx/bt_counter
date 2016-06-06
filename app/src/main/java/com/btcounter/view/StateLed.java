package com.btcounter.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.btcounter.R;

/**
 * Created by daba on 2016-06-06.
 */
public class StateLed extends View {

    private static final int BORDER = 4;

    private Paint paint;
    private int onColor;
    private int offColor;

    public StateLed(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateLed);
        onColor = a.getColor(R.styleable.StateLed_on_color, Color.BLACK);
        offColor = a.getColor(R.styleable.StateLed_off_color, Color.BLACK);
        a.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int size = getWidth() / 2;
        //paint.setColor(Color.BLACK);
        canvas.drawCircle(size, size, getWidth(), paint);

        paint.setColor(onColor);
        canvas.drawCircle(size, size, (size) - BORDER, paint);
    }
}
