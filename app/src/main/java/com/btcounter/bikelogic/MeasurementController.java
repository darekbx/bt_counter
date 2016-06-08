package com.btcounter.bikelogic;

import android.os.SystemClock;

/**
 * Created by daba on 2016-06-07.
 */
public class MeasurementController {

    public interface Listener {
        void refreshSpeed(double speed);
        void refreshDistance(double distance);
        void refreshCadence(int cadence);
    }

    public MeasurementController(double wheelSize) {
        this.wheelSize = wheelSize;
    }

    private Listener listener;

    private double wheelSize;
    private double distance = 0;
    private long wheelRotationTime = 0;

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
    }

    private long getTime() {
        return SystemClock.uptimeMillis();
    }
}