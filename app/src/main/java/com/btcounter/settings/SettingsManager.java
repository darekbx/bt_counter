package com.btcounter.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.btcounter.R;

/**
 * Created by daba on 2016-07-05.
 */
public class SettingsManager {

    private Context context;

    public SettingsManager(Context context) {
        this.context = context;
    }

    public float getWheelSize() {
        String key = context.getString(R.string.settings_wheel_size_key);
        return getPreferences().getFloat(key, 0);
    }

    public boolean isDebugMode() {
        String key = context.getString(R.string.settings_debug_mode_key);
        return getPreferences().getBoolean(key, false);
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
