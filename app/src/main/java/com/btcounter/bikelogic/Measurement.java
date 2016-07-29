package com.btcounter.bikelogic;

/**
 * Created by daba on 2016-06-06.
 */
public class Measurement {

    public static float speed(float wheelSize, float interval) {
        float wheelSizeMeters = wheelSize / 1000f;
        float intervalSeconds = interval / 1000f;
        return wheelSizeMeters / intervalSeconds;
    }

    public static float speedToKmH(float speedMs) {
        return speedMs * 60 * 60 / 1000;
    }

    public static int cadence(long interval) {
        final int minute = 60 * 1000;
        return (int)(minute / interval);
    }

    public static float distanceToKilometers(float distance) {
        return distance / 1000f / 1000f;
    }

    public static int floatModulo(float value) {
        if (value == 0) {
            return 0;
        }
        return (int)((value - (int)value) * 10);
    }
}