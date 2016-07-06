package com.btcounter;

import android.app.Activity;
import android.os.Bundle;

import com.btcounter.fragments.SettingsFragment;

/**
 * Created by daba on 2016-07-05.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
