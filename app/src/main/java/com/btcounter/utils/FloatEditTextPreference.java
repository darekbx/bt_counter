package com.btcounter.utils;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * Created by daba on 2016-07-05.
 */
public class FloatEditTextPreference extends EditTextPreference {

    public FloatEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        return String.valueOf(getPersistedFloat(-1));
    }

    @Override
    protected boolean persistString(String value) {
        return persistFloat(Float.valueOf(value));
    }
}