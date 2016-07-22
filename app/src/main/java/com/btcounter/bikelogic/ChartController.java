package com.btcounter.bikelogic;

import com.btcounter.model.ChartPair;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * Created by daba on 2016-07-22.
 */
public class ChartController {

    public interface Listener {
        ChartPair onCollect();
        void onData(ArrayList<ChartPair> pairs);
    }

    private static final int TIME_DELAY = 1;

    private Subscription repeatSubscription;
    private Listener listener;

    private ArrayList<ChartPair> data = new ArrayList<>();

    public void startListening() {
        repeatSubscription = Observable
                .just(0)
                .delay(TIME_DELAY, TimeUnit.SECONDS)
                .repeat()
                .subscribe((a) -> {
                    tick();
                });
    }

    public void stopListening() {
        if (repeatSubscription != null) {
            repeatSubscription.unsubscribe();
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    protected void tick() {
        ChartPair pair = listener.onCollect();
        data.add(pair);
        listener.onData(data);
    }
}