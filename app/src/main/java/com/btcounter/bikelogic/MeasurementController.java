package com.btcounter.bikelogic;

import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * Created by daba on 2016-06-07.
 */
public class MeasurementController {

    public interface Listener {
        void refreshSpeed(double speed);
        void refreshDistance(double distance);
        void refreshCadence(int cadence);
    }

    private static final int CLEAN_DELAY = 2;

    private Listener listener;
    private Subscription subscription;

    private double wheelSize;
    private double distance = 0;
    private long wheelRotationTime = 0;

    public MeasurementController(double wheelSize) {
        this.wheelSize = wheelSize;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void notifyWheelRotation() {
        if (wheelRotationTime == 0) {
            wheelRotationTime = getTime();
        } else {
            long currentTime = getTime();
            long interval = currentTime - wheelRotationTime;
            wheelRotationTime = currentTime;

            double speedMs = Measurement.speed(wheelSize, interval);
            double speedKmH = Measurement.speedToKmH(speedMs);
            listener.refreshSpeed(speedKmH);
        }
        distance += Measurement.distanceToKilometers(wheelSize);
        listener.refreshDistance(distance);

        delayedClean();
    }

    private void delayedClean() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = Observable
                .just(0)
                .delay(CLEAN_DELAY, TimeUnit.SECONDS)
                .subscribe((a) -> {
                    listener.refreshSpeed(0);
                });
    }

    private long getTime() {
        return SystemClock.uptimeMillis();
    }
}