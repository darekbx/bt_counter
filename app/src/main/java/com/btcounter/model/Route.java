package com.btcounter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daba on 2016-07-29.
 */

public class Route {

    private static final String DELIMITER = "|";

    public String name;
    public List<RoutePoint> points;

    public Route(String name, List<RoutePoint> points) {
        this.name = name;
        this.points = points;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        for (RoutePoint point : points) {
            builder.append(DELIMITER);
            builder.append(point.toString());
        }
        return builder.toString();
    }

    public static Route fromString(String string) {
        String[] parts = string.split("\\" + DELIMITER);
        String name = parts[0];
        List<RoutePoint> points = new ArrayList<>();

        for (int i = 1, count = parts.length; i < count; i++) {
            points.add(RoutePoint.fromString(parts[i]));
        }

        return new Route(name, points);
    }
}