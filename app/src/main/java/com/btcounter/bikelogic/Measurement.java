package com.btcounter.bikelogic;

/**
 * Created by daba on 2016-06-06.
 */
public class Measurement {

    /**
     * Calculate speed
     * @param wheelSize Wheel size in mm (WS)
     * @param interval in milliseconds
     * @return Speed in m/s
     */
    public static double speed(double wheelSize, double interval) {
        double wheelSizeMeters = wheelSize / 1000d;
        double intervalSeconds = interval / 1000d;
        return wheelSizeMeters / intervalSeconds;
    }

    /**
     * Covert speed in m/s to km/h
     * @param speedMs in m/s
     * @return Speed in km/h
     */
    public static double speedToKmH(double speedMs) {
        return speedMs * 60 * 60 / 1000;
    }

    /**
     * Get cadence crank rotations per minute
     * @param interval in ms
     * @return Cadence
     */
    public static int cadence(long interval) {
        final int minute = 60 * 1000;
        return (int)(minute / interval);
    }

    /**
     * Convert distaince in mm to km
     * @param distance in mm
     * @return distance in km
     */
    public static double distanceToKilometers(double distance) {
        return distance / 1000d / 1000d;
    }

    public static int doubleModulo(double value) {
        return (int)((value - (int)value) * 10);
    }
}