package com.btcounter.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.btcounter.R;
import com.btcounter.settings.SettingsManager;
import com.btcounter.utils.FloatEditTextPreference;

/**
 * Created by daba on 2016-07-05.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    public interface Listener {
        void onWheelSizeChanged();
        void onTripDistanceChanged();
        void onOdoChanged();
        void onMaxSpeedChanged();
    }

    private Listener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_settings);

        initListeners();
        setWheelSizeSummary();
        setOdoSummary();
        setMaxSpeedSummary();
        setTripDistanceSummary();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void initListeners() {
        getWheelSizePreference().setOnPreferenceChangeListener(this);
        getOdoPreference().setOnPreferenceChangeListener(this);
        getMaxSpeedPreference().setOnPreferenceChangeListener(this);
        getTripDistancePreference().setOnPreferenceChangeListener(this);
    }

    private void setWheelSizeSummary() {
        getWheelSizePreference().setSummary(String.valueOf(getWheelSize()));
    }

    private void setOdoSummary() {
        getOdoPreference().setSummary(String.valueOf(getOdo()));
    }

    private void setMaxSpeedSummary() {
        getMaxSpeedPreference().setSummary(String.valueOf(getMaxSpeed()));
    }

    private void setTripDistanceSummary() {
        getTripDistancePreference().setSummary(String.valueOf(getTripDistance()));
    }

    private FloatEditTextPreference getWheelSizePreference() {
        return (FloatEditTextPreference)
                findPreference(getString(R.string.settings_wheel_size_key));
    }

    private FloatEditTextPreference getOdoPreference() {
        return (FloatEditTextPreference)
                findPreference(getString(R.string.settings_odo_key));
    }

    private FloatEditTextPreference getMaxSpeedPreference() {
        return (FloatEditTextPreference)
                findPreference(getString(R.string.settings_max_speed_key));
    }

    private FloatEditTextPreference getTripDistancePreference() {
        return (FloatEditTextPreference)
                findPreference(getString(R.string.settings_distance_key));
    }

    private float getWheelSize() {
        return new SettingsManager(getActivity()).getWheelSize();
    }

    private float getOdo() {
        return new SettingsManager(getActivity()).getOdo();
    }

    private float getMaxSpeed() {
        return new SettingsManager(getActivity()).getMaxSpeed();
    }

    private float getTripDistance() {
        return new SettingsManager(getActivity()).getDistance();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(getString(R.string.settings_wheel_size_key))) {
            getWheelSizePreference().setSummary(String.valueOf(newValue));
            if (listener != null) {
                listener.onWheelSizeChanged();
            }
        } else if (preference.getKey().equals(getString(R.string.settings_odo_key))) {
            getOdoPreference().setSummary(String.valueOf(newValue));
            if (listener != null) {
                listener.onOdoChanged();
            }
        } else if (preference.getKey().equals(getString(R.string.settings_max_speed_key))) {
            getMaxSpeedPreference().setSummary(String.valueOf(newValue));
            if (listener != null) {
                listener.onMaxSpeedChanged();
            }
        } else if (preference.getKey().equals(getString(R.string.settings_distance_key))) {
            getTripDistancePreference().setSummary(String.valueOf(newValue));
            if (listener != null) {
                listener.onTripDistanceChanged();
            }
        }
        return true;
    }
}
