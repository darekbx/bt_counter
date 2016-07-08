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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        bindViews(root);
        resetViews();

        return root;
    }

    public void updateSpeed(float speed) {
        int speedMod = Measurement.floatModulo(speed);
        speedText.setText(getString(R.string.speed_format, (int)speed, speedMod));
    }

    public void updateDistance(float distance) {
        distanceText.setText(getForrmattedUnitText(getString(R.string.distance_format, distance), 2));
    }

    public void updateCadence(int cadence) {
        cadenceText.setText(getForrmattedUnitText(getString(R.string.cadence_format, cadence), 1));
    }

    public void updateOdo(int odo) {
        odoText.setText(getForrmattedUnitText(getString(R.string.odo_format, odo), 3));
    }

    private void resetViews() {
        updateSpeed(0);
        updateDistance(0);
        updateCadence(0);
        updateOdo((int)new SettingsManager(getActivity()).getOdo());
    }

    private void bindViews(View root) {
        speedText = (TextView) root.findViewById(R.id.text_speed);
        distanceText = (TextView) root.findViewById(R.id.distance_text);
        cadenceText = (TextView) root.findViewById(R.id.cadence_text);
        odoText = (TextView) root.findViewById(R.id.odo_text);

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