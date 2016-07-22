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

    public void appendOdoAndSave(float distance) {
        float odo = getOdo();
        odo += distance;
        saveOdo(odo);
    }

    public float getOdo() {
        String key = context.getString(R.string.settings_odo_key);
        return getPreferences().getFloat(key, 0);
    }

    public float getWheelSize() {
        String key = context.getString(R.string.settings_wheel_size_key);
        return getPreferences().getFloat(key, 0);
    }

    public float getMaxSpeed() {
        String key = context.getString(R.string.settings_max_speed_key);
        return getPreferences().getFloat(key, 0);
    }

    public float getDistance() {
        String key = context.getString(R.string.settings_distance_key);
        return getPreferences().getFloat(key, 0);
    }

    public boolean isDebugMode() {
        String key = context.getString(R.string.settings_debug_mode_key);
        return getPreferences().getBoolean(key, false);
    }

    public void saveMaxSpeed(float maxSpeed) {
        String key = context.getString(R.string.settings_max_speed_key);
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putFloat(key, maxSpeed);
        editor.apply();
    }

    public void saveDistance(float distance) {
        String key = context.getString(R.string.settings_distance_key);
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putFloat(key, distance);
        editor.apply();
    }

    public void saveOdo(float odo) {
        String key = context.getString(R.string.settings_odo_key);
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putFloat(key, odo);
        editor.apply();
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
