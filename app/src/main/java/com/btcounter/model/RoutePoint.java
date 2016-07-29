package com.btcounter.model;

/**
 * Created by daba on 2016-07-29.
 */

public class RoutePoint {

    private static final String DELIMITER = ";";

    public String name;
    public float distance;
    public long bestTime;

    public RoutePoint(String name, float distance, long bestTime) {
        this.name = name;
        this.distance = distance;
        this.bestTime = bestTime;
    }

    @Override
    public String toString() {
        return name + DELIMITER + distance + DELIMITER + bestTime;
    }

    public static RoutePoint fromString(String string) {
        String[] parts = string.split(DELIMITER);
        String name = parts[0];
        float distance = Float.parseFloat(parts[1]);
        long bestTime = Long.parseLong(parts[2]);
        return new RoutePoint(name, distance, bestTime);
    }
}