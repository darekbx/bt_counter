package com.btcounter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.btcounter.R;
import com.btcounter.bikelogic.Measurement;
import com.btcounter.settings.SettingsManager;

/**
 * Created by daba on 2016-07-06.
 */
public class MainFragment extends Fragment {

    private ArrayAdapter<String> adapter;

    private ListView listView;
    private Button start;
    private Button stop;
    private TextView speedText;
    private TextView distanceText;
    private TextView cadenceText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        bindViews(root);
        initilizeDebugList();

        return root;
    }

    public void setButtonsState(boolean enabled) {
        start.setEnabled(enabled);
        stop.setEnabled(enabled);
    }

    public void updateSpeed(float speed) {
        int speedMod = Measurement.floatModulo(speed);
        speedText.setText(getString(R.string.speed_format, (int)speed, speedMod));
    }

    public void updateDistance(float distance) {
        distanceText.setText(getString(R.string.distance_format, distance));
    }

    public void updateCadence(int cadence) {
        cadenceText.setText(getString(R.string.cadence_format, cadence));
    }

    public void addLog(String message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        listView.setSelection(listView.getCount() - 1);
    }

    private void initilizeDebugList() {
        listView.setAdapter(adapter = new ArrayAdapter<>(getActivity(), R.layout.adapter_log));
    }

    private void bindViews(View root) {
        listView = (ListView) root.findViewById(R.id.list);
        start = (Button) root.findViewById(R.id.button_start);
        stop = (Button) root.findViewById(R.id.button_stop);
        speedText = (TextView) root.findViewById(R.id.text_speed);
        distanceText = (TextView) root.findViewById(R.id.distance_text);
        cadenceText = (TextView) root.findViewById(R.id.cadence_text);

        if (!new SettingsManager(getActivity()).isDebugMode()) {
            listView.setVisibility(View.GONE);
        }
    }
}