package com.btcounter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btcounter.R;
import com.btcounter.bikelogic.Measurement;
import com.btcounter.settings.SettingsManager;

/**
 * Created by daba on 2016-07-06.
 */
public class MainFragment extends Fragment {

    private TextView speedText;
    private TextView distanceText;
    private TextView cadenceText;
    private TextView odoText;
    private TextView averageSpeedText;
    private TextView maxSpeedText;
    private TextView debugText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        bindViews(root);
        resetViews();

        return root;
    }

    public void invalidateSpeed(float speed) {
        int speedMod = Measurement.floatModulo(speed);
        speedText.setText(getString(R.string.speed_format, (int)speed, speedMod));
    }

    public void invalidateDistance(float distance) {
        distanceText.setText(getForrmattedUnitText(getString(R.string.distance_format, distance), 2));
    }

    public void invalidateCadence(int cadence) {
        cadenceText.setText(getForrmattedUnitText(getString(R.string.cadence_format, cadence), 3));
    }

    public void invalidateOdo(int odo) {
        odoText.setText(getForrmattedUnitText(getString(R.string.odo_format, odo), 3));
    }

    public void invalidateAverageSpeed(float averageSpeed) {
        averageSpeedText.setText(getForrmattedUnitText(getString(R.string.unit_km_h, averageSpeed), 4));
    }

    public void invalidateMaxSpeed(float maxSpeed) {
        maxSpeedText.setText(getForrmattedUnitText(getString(R.string.unit_km_h, maxSpeed), 4));
    }

    public void updateDebug(String message) {
        debugText.setText(message);
    }

    private void resetViews() {
        invalidateSpeed(0);
        invalidateDistance(0);
        invalidateCadence(0);
        invalidateOdo((int)new SettingsManager(getActivity()).getOdo());
    }

    private void bindViews(View root) {
        speedText = (TextView) root.findViewById(R.id.text_speed);
        distanceText = (TextView) root.findViewById(R.id.distance_text);
        cadenceText = (TextView) root.findViewById(R.id.cadence_text);
        odoText = (TextView) root.findViewById(R.id.odo_text);
        averageSpeedText = (TextView) root.findViewById(R.id.average_value);
        maxSpeedText = (TextView) root.findViewById(R.id.max_speed_value);
        debugText = (TextView) root.findViewById(R.id.debug);

        if (!new SettingsManager(getActivity()).isDebugMode()) {
            debugText.setVisibility(View.GONE);
        }

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }

    private SpannableString getForrmattedUnitText(String text, int unitLength) {
        final int textLength = text.length();
        SpannableString string = new SpannableString(text);
        string.setSpan(new ForegroundColorSpan(getUnitColor()),
                textLength - unitLength, textLength, 0);
        return  string;
    }

    private int getUnitColor() {
        return getActivity().getColor(R.color.text_unit_color);
    }
}