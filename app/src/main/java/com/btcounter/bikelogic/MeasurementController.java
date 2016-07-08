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
        void refreshSpeed(float speed);
        void refreshDistance(float distance);
        void refreshAverageSpeed(float averageSpeed);
        void refreshCadence(int cadence);
    }

    private static final int CLEAN_DELAY = 2;

    private Listener listener;
    private Subscription subscription;

    private float wheelSize;
    private float distance = 0;
    private float averageSum = 0;
    private float averageCount = 0;
    private long cranksRotationTime = 0;

    public MeasurementController(float wheelSize) {
        this.wheelSize = wheelSize;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void notifyWheelRotationTime(int timeDiff) {
        float speedMs = Measurement.speed(wheelSize, timeDiff);
        float speedKmH = Measurement.speedToKmH(speedMs);
        listener.refreshSpeed(speedKmH);

        distance += Measurement.distanceToKilometers(wheelSize);
        listener.refreshDistance(distance);

        updateAverage(speedKmH);
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

    public float getDistance() {
        return distance;
    }

    private void updateAverage(float speed) {
        averageSum += speed;
        averageCount++;
        float averageSpeed = averageSum / averageCount;
        listener.refreshAverageSpeed(averageSpeed);
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