package com.btcounter;

import android.app.Activity;
import android.os.Bundle;

import com.btcounter.model.Route;

/**
 * Created by daba on 2016-08-02.
 */

public class AddRouteActivity extends Activity {

    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
    }


}