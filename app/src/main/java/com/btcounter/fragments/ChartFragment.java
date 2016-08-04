package com.btcounter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btcounter.R;
import com.btcounter.chart.ChartView;

import java.util.ArrayList;

/**
 * Created by daba on 2016-07-22.
 */
public class ChartFragment extends Fragment {

    private ChartView chartView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chartView = (ChartView) inflater.inflate(R.layout.fragment_chart, container, false);
        return chartView;
    }

    public void notifyData(ArrayList<Float> data) {
        if (chartView != null) {
            chartView.invalidateChart(data);
        }
    }
}