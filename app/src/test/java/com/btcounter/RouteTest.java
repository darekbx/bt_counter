package com.btcounter;

import com.btcounter.model.Route;
import com.btcounter.model.RoutePoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by daba on 2016-07-29.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RouteTest {

    private static final String ROUTE = "route|point1;1.355;34100|point2;2.91;18901";
    private static final String ROUTE_POINT = "point;7.355;34100";

    @Test
    public void route_point_toString() {

        RoutePoint point = new RoutePoint("point", 7.355f, 34100);

        assertEquals(point.toString(), ROUTE_POINT);
    }

    @Test
    public void route_point_fromString() {

        RoutePoint point = RoutePoint.fromString(ROUTE_POINT);

        assertEquals(point.name, "point");
        assertEquals(point.distance, 7.355, 0.0001);
        assertEquals(point.bestTime, 34100);
    }

    @Test
    public void route_toString() {

        List<RoutePoint> points = new ArrayList<>(2);
        points.add(new RoutePoint("point1", 1.355f, 34100));
        points.add(new RoutePoint("point2", 2.91f, 18901));
        Route route = new Route("route", points);

        assertEquals(route.toString(), ROUTE);
    }

    @Test
    public void route_fromString() {

        Route route = Route.fromString(ROUTE);

        assertEquals(route.name, "route");
        assertNotNull(route.points);
        assertEquals(route.points.size(), 2);
        assertEquals(route.points.get(0).name, "point1");
        assertEquals(route.points.get(0).distance, 1.355, 0.0001);
        assertEquals(route.points.get(0).bestTime, 34100);
        assertEquals(route.points.get(1).name, "point2");
        assertEquals(route.points.get(1).distance, 2.91, 0.0001);
        assertEquals(route.points.get(1).bestTime, 18901);
    }
}