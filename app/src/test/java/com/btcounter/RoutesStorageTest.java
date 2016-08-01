package com.btcounter;

import com.btcounter.model.Route;
import com.btcounter.model.RoutePoint;
import com.btcounter.route.RouteStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by daba on 2016-07-29.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RoutesStorageTest {

    private RouteStorage routeStorage;

    @Before
    public void prepare() {
        routeStorage = new RouteStorage(RuntimeEnvironment.application);
    }

    @Test
    public void add_route() {
        resetAndAddRouteToStorage();

        assertEquals(routeStorage.getRoutesStrings().size(), 1);
        assertEquals(routeStorage.getRoutes().size(), 1);
        assertEquals(routeStorage.getRoutes().get(0).name, "route_to_work");
        assertEquals(routeStorage.getRoutes().get(0).points.size(), 3);
        assertEquals(routeStorage.getRoutes().get(0).points.get(0).bestTime, 150);
    }

    @Test
    public void get_routes_strings() {
        resetAndAddRouteToStorage();

        Set<String> routesString = routeStorage.getRoutesStrings();

        assertEquals(routesString.size(), 1);
    }

    @Test
    public void get_routes() {
        resetAndAddRouteToStorage();

        List<Route> routes = routeStorage.getRoutes();

        assertEquals(routes.size(), 1);
        assertEquals(routes.get(0).name, "route_to_work");
    }

    @Test
    public void update_route() {
        resetAndAddRouteToStorage();

        List<Route> routes = routeStorage.getRoutes();
        routes.get(0).points.get(1).bestTime = 200;
        routeStorage.updateRoute(routes.get(0));

        routes = routeStorage.getRoutes();
        assertEquals(routes.get(0).points.size(), 3);
        assertEquals(routes.get(0).points.get(1).name, "czluchowska_powstancow");
        assertEquals(routes.get(0).points.get(1).distance, 2800f, 0f);
        assertEquals(routes.get(0).points.get(1).bestTime, 200);
    }

    @Test
    public void delete_route() {
        resetAndAddRouteToStorage();

        List<Route> routes = routeStorage.getRoutes();
        routeStorage.deleteRoute(routes.get(0));

        routes = routeStorage.getRoutes();
        assertEquals(routes.size(), 0);
    }

    private void resetAndAddRouteToStorage() {
        Route route = createRoute();
        routeStorage.reset();
        routeStorage.addRoute(route);
    }

    private Route createRoute() {
        List<RoutePoint> points = new ArrayList<>();
        points.add(new RoutePoint("batalionow_czluchowska", 1200f, 150));
        points.add(new RoutePoint("czluchowska_powstancow", 2800f, 240));
        points.add(new RoutePoint("przejazd_kolejowy", 3700f, 120));
        Route route = new Route("route_to_work", points);
        return route;
    }
}