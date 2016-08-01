package com.btcounter.route;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.btcounter.model.Route;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by daba on 2016-07-29.
 */

public class RouteStorage {

    public static final String ROUTES_KEY = "routes_key";

    private Context context;

    public RouteStorage(Context context) {
        this.context = context;
    }

    public void addRoute(Route route) {
        List<Route> routes = getRoutes();
        routes.add(route);

        saveRoutes(routes);
    }

    public void deleteRoute(Route routeToDelete) {
        List<Route> routes = getRoutes();

        for (Route route : routes) {
            if (route.name.equals(routeToDelete.name)) {
                routes.remove(route);
                break;
            }
        }

        saveRoutes(routes);
    }

    public void updateRoute(Route routeToUpdate) {
        List<Route> routes = getRoutes();

        for (Route route : routes) {
            if (route.name.equals(routeToUpdate.name)) {
                route.points = routeToUpdate.points;
                break;
            }
        }

        saveRoutes(routes);
    }

    public List<Route> routesStringToRoutes(Set<String> routesStrings) {
        List<Route> routes = new ArrayList<>();
        for (String routeString : routesStrings) {
            routes.add(Route.fromString(routeString));
        }
        return routes;
    }

    public Set<String> routesToRoutesString(final List<Route> routes) {
        Set<String> routesString = new HashSet<>();
        for (Route route : routes) {
            routesString.add(route.toString());
        }
        return routesString;
    }

    public void saveRoutes(List<Route> routes) {
        Set<String> routesString = routesToRoutesString(routes);
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putStringSet(ROUTES_KEY, routesString);
        editor.apply();
    }

    public List<Route> getRoutes() {
        Set<String> routesString = getRoutesStrings();
        return routesStringToRoutes(routesString);
    }

    public Set<String> getRoutesStrings() {
        return getPreferences().getStringSet(ROUTES_KEY, new HashSet<>(0));
    }

    public void reset() {
        getPreferences().edit().clear().apply();
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}