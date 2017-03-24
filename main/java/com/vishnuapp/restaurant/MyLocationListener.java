package com.vishnuapp.restaurant;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;

/**
 * Created by Thilina on 23-Jul-16.
 */

public class MyLocationListener implements LocationListener {

    private double latitude;
    private double longitude;
    private LocationActivity locationActivity;

    public MyLocationListener(LocationActivity locationActivity) {
        this.locationActivity = locationActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
        //t.append("\n " + latitude + " " + longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        locationActivity.startActivity(i);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
