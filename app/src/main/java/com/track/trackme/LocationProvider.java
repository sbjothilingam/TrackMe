package com.track.trackme;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Suresh Babu on 8/20/2015.
 */
public class LocationProvider implements LocationListener {
    private static double latitude;
    private static double longitude;
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }
}
