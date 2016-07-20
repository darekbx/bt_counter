package com.btcounter.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by daba on 2016-07-20.
 */

public class TimeUtils {

    private static final String TIME_FORMAT = " %02d:%02d";

    public static String extractTime(long seconds) {
        long milliseconds = seconds * 1000;
        return String.format(TIME_FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
    }
}
