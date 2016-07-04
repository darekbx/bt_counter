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
    private long cranksRotationTime = 0;

    public MeasurementController(double wheelSize) {
        this.wheelSize = wheelSize;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void notifyWheelRotationTime(int timeDiff) {
        double speedMs = Measurement.speed(wheelSize, timeDiff);
        double speedKmH = Measurement.speedToKmH(speedMs);
        listener.refreshSpeed(speedKmH);

        distance += Measurement.distanceToKilometers(wheelSize);
        listener.refreshDistance(distance);

        delayedClean();
    }

    public void notifyCrankRotation() {
        if (cranksRotationTime == 0) {
            cranksRotationTime = getTime();
        } else {
            long currentTime = getTime();
            long interval = currentTime - cranksRotationTime;
            cranksRotationTime = currentTime;

            int cadence = Measurement.cadence(interval);
            listener.refreshCadence(cadence);
        }
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
                    listener.refreshCadence(0);
                });
    }

    private long getTime() {
        return SystemClock.uptimeMillis();
    }
}