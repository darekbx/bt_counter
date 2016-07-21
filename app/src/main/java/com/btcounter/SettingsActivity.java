package com.btcounter;

import android.app.Activity;
import android.os.Bundle;

import com.btcounter.fragments.SettingsFragment;

/**
 * Created by daba on 2016-07-05.
 */
public class SettingsActivity extends Activity implements SettingsFragment.Listener {

    public static final int TRIP_DISTANCE_RESULT = 1;
    public static final int WHEEL_SIZE_RESULT = 2;
    public static final int ODO_RESULT = 3;
    public static final int MAX_SPEED_RESULT = 4;

    private SettingsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new SettingsFragment();
        fragment.setListener(this);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    @Override
    public void onWheelSizeChanged() {
        setResult(WHEEL_SIZE_RESULT);
    }

    @Override
    public void onTripDistanceChanged() {
        setResult(TRIP_DISTANCE_RESULT);
    }

    @Override
    public void onOdoChanged() {
        setResult(ODO_RESULT);
    }

    @Override
    public void onMaxSpeedChanged() {
        setResult(MAX_SPEED_RESULT);
    }
}
