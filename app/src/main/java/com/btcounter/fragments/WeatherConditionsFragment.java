package com.btcounter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btcounter.R;
import com.btcounter.model.WeatherConditions;

/**
 * Created by daba on 2016-07-22.
 */
public class WeatherConditionsFragment extends Fragment {

    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        textView = (TextView) inflater.inflate(R.layout.fragment_weather_conditions, container, false);
        return textView;
    }

    public void notifyData(WeatherConditions data) {
        if (textView != null) {
            String a = getString(R.string.weather_conditions_format,
                    data.temperature, data.windChill, data.pressure, data.humidity);
            textView.setText(a);
        }
    }
}