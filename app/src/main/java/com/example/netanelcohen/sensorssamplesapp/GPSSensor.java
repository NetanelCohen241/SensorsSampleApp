package com.example.netanelcohen.sensorssamplesapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by n on 29/07/2018.
 */

public class GPSSensor extends ASensor {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private String provider;

    public GPSSensor(Context context) {
        super(context);
        location = null;
        provider = LocationManager.GPS_PROVIDER;
        //set GPS attributes
        this.attributes.add("Latitude");
        this.attributes.add("Longitude");

    }

    /**
     *
     * @return GPS sensor parameters : latitude , longtitude
     */
    @Override
    public Data getData() {

        Data ans = new Data();
        if (location != null) {
            ans.addValue(location.getLatitude());
            ans.addValue(location.getLongitude());
        }
        else{
            ans.addValue(0.0);
            ans.addValue(0.0);
        }

        return ans;
    }


    @Override
    public void stopListen() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(locationListener);
            locationListener=null;
        }

    }


    // listen to gps sensor continuously and update location field
    @Override
    public void startListen() {

        locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        if(locationListener == null)
            locationListener =  new LocationListener() {

                @Override
                public void onLocationChanged(Location loc) {
                    location = loc;
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }

            };

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
            Location tmp = locationManager.getLastKnownLocation(provider);
            if (tmp != null) {
                location = tmp;
            }
        }
    }




}






