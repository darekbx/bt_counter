package com.btcounter.bikelogic;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by daba on 2016-06-07.
 */
public class MeasurementController {

    public interface Listener {
        void refreshSpeed(double speed);
        void refreshCadence(int cadence);
    }

    private static final int INTERVAL = 1;

    private Subscription speedSubscription;
    private Listener listener;
    private int wheelRotationCount = 0;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void subscribeSpeed() {
        speedSubscription = Observable
                .interval(INTERVAL, INTERVAL, TimeUnit.SECONDS, Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> {
                    double speedMs = Measurement.speed(2000, wheelRotationCount);
                    double speedKmH = Measurement.speedToKmH(speedMs);
                    listener.refreshSpeed(speedKmH);
                    wheelRotationCount = 0;
                });
    }

    public void closeSubscriptions() {
        if (speedSubscription != null) {
            speedSubscription.unsubscribe();
        }
    }

    public void notifyWheelRotation() {
        wheelRotationCount++;
    }
}