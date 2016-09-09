package com.btcounter.bikelogic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * Created by daba on 2016-07-22.
 */
public class ChartController {

    public interface Listener {
        float onCollect();
        void onData(ArrayList<Float> data);
    }

    private static final int TIME_DELAY = 1;

    private Subscription repeatSubscription;
    private Listener listener;

    private ArrayList<Float> data = new ArrayList<>();

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

    public int getStopSummary() {
        int seconds = 0;
        for (int i = 0, count = data.size(); i < count; i++) {
            if (data.get(i) == 0f) {
                seconds++;
            }
        }
        return seconds;
    }

    public int getSummary() {
        return data.size();
    }

    public void setData(ArrayList<Float> data) {
        this.data = data;
    }

    protected void tick() {
        if (listener != null) {
            Float value = listener.onCollect();
            data.add(value);
            listener.onData(data);
        }
    }
}