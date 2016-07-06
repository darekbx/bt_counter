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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_settings);

        initListeners();
        setWheelSizeSummary();
        setOdoSummary();
    }

    private void initListeners() {
        getWheelSizePreference().setOnPreferenceChangeListener(this);
        getOdoPreference().setOnPreferenceChangeListener(this);
    }

    private void setWheelSizeSummary() {
        getWheelSizePreference().setSummary(String.valueOf(getWheelSize()));
    }

    private void setOdoSummary() {
        getOdoPreference().setSummary(String.valueOf(getOdo()));
    }

    private FloatEditTextPreference getWheelSizePreference() {
        return (FloatEditTextPreference)
                findPreference(getString(R.string.settings_wheel_size_key));
    }

    private FloatEditTextPreference getOdoPreference() {
        return (FloatEditTextPreference)
                findPreference(getString(R.string.settings_odo_key));
    }

    private float getWheelSize() {
        return new SettingsManager(getActivity()).getWheelSize();
    }

    private float getOdo() {
        return new SettingsManager(getActivity()).getOdo();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(getString(R.string.settings_wheel_size_key))) {
            getWheelSizePreference().setSummary(String.valueOf(newValue));
        } else if (preference.getKey().equals(getString(R.string.settings_odo_key))) {
            getOdoPreference().setSummary(String.valueOf(newValue));
        }
        return true;
    }
}
