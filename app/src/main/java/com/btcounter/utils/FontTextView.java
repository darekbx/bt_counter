package com.btcounter.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by daba on 2016-07-07.
 */
public class FontTextView extends TextView {

    private static final String FONT = "fonts/makisupa.ttf";

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        } else {
            final Typeface customTypeface = Typeface.createFromAsset(context.getAssets(), FONT);
            setTypeface(customTypeface);
        }
    }
}
