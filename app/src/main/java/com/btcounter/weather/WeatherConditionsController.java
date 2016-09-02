package com.btcounter.weather;

import com.btcounter.model.WeatherConditions;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
/**
 * Created by daba on 2016-09-02.
 */

public class WeatherConditionsController {

    public static final String IF_ADDRESS = "http://www.if.pw.edu.pl/~meteo/okienkow.php";
    public static final int INTERVAL = 6;

    public interface Listener {
        void onData(WeatherConditions weatherConditions);
    }

    private Listener listener;
    private Subscription subscription;

    public WeatherConditionsController(Listener listener) {
        this.listener = listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void schedule() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = Observable
                .just(0)
                .delay(getInterval(), TimeUnit.SECONDS)
                .repeat()
                .subscribe(a -> {
                    refreshWeatherConditions();
                });
    }

    public void stop() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public int getInterval() {
        return INTERVAL;
    }

    public void refreshWeatherConditions() {
        new ContentDownloader()
                .downloadObservable(IF_ADDRESS)
                .map(s -> new ContentParser().parse(s))
                .subscribe(weatherConditions -> {
                    if (listener != null) {
                        listener.onData(weatherConditions);
                    }
                }, error -> { /* do nothing */ });

    }
}