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
    public double speed(int wheelSize, int ticks) {
        return wheelSize * ticks;
    }

    /**
     * Covert speed in m/s to km/h
     * @param speed in m/s
     * @return Speed in km/h
     */
    public double speedToKmH(double speed) {
        return speed * 60 * 60 / 1000;
    }

    /**
     * Get cadence ticks per minute
     * @param ticks
     * @return Cadence
     */
    public int cadence(int ticks, int minutes) {
        return ticks / minutes;
    }
}