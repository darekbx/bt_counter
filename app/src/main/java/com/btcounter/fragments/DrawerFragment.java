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

import com.btcounter.R;
import com.btcounter.settings.SettingsManager;

/**
 * Created by daba on 2016-07-08.
 */
public class DrawerFragment extends Fragment {

    private ArrayAdapter<String> adapter;

    private ListView listView;
    private Button start;
    private Button stop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drawer, container, false);

        bindViews(root);
        initilizeDebugList();

        return root;
    }

    private void bindViews(View root) {
        listView = (ListView) root.findViewById(R.id.list);
        start = (Button) root.findViewById(R.id.button_start);
        stop = (Button) root.findViewById(R.id.button_stop);

        if (!new SettingsManager(getActivity()).isDebugMode()) {
            root.findViewById(R.id.button_prepare).setVisibility(View.GONE);
            root.findViewById(R.id.button_tick).setVisibility(View.GONE);
            root.findViewById(R.id.button_cadence).setVisibility(View.GONE);
        }
    }

    public void setButtonsState(boolean enabled) {
        start.setEnabled(enabled);
        stop.setEnabled(enabled);
    }

    public void addLog(String message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        listView.setSelection(listView.getCount() - 1);
    }

    private void initilizeDebugList() {
        listView.setAdapter(adapter = new ArrayAdapter<>(getActivity(), R.layout.adapter_log));
    }

}
