package com.btcounter.bikelogic;

/**
 * Created by daba on 2016-06-06.
 */
public class Measurement {

    /**
     * Calculate speed
     * @param wheelSize Wheel size in mm (WS)
     * @param ticks ticks per second
     * @return Speed in m/s
     */
    public static double speed(int wheelSize, int ticks) {
        return (wheelSize / 1000) * ticks;
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
     * Get cadence ticks per minute
     * @param ticks
     * @return Cadence
     */
    public static int cadence(int ticks, int minutes) {
        return ticks / minutes;
    }
}