package com.archilog.archi_log_demassue_piron.Model.Localisation;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.IBinder;
import androidx.core.app.ActivityCompat;
import com.archilog.archi_log_demassue_piron.Model.House;

import java.io.IOException;
import java.util.List;

public class Localisation extends Service implements LocationListener {
    private final Context mContext;

    private Location location;
    private LocationManager locationManager;
    private double latitude, longitude;

    private static final long MIN_DISTANCE_IN_METERS_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60 * 5;

    public Localisation(Context context) {
        this.mContext = context;
        getLocation();
    }

    private Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                //Aucun service de localisation n'est activé
            } else {
                if(isNetworkEnabled) {
                    checkPermissions(LocationManager.NETWORK_PROVIDER);
                }
                if(isGPSEnabled && location == null) {
                    checkPermissions(LocationManager.GPS_PROVIDER);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private void checkPermissions(String providerType) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        locationManager.requestLocationUpdates(
                providerType,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_IN_METERS_CHANGE_FOR_UPDATES, this);

        if (locationManager != null) {
            location = locationManager
                    .getLastKnownLocation(providerType);

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        } else {
            latitude = Double.NaN;
        }

        return latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        } else {
            longitude = Double.NaN;
        }

        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public double getDistanceInMetersBetweenTwoLatLng(House house) {
        float[] results = new float[1];
        double[] houseCoordinates = getLatitudeAndLongitudeFromHouse(house);
        double startLat = getLatitude();
        double startLong = getLongitude();

        Location.distanceBetween(startLat, startLong, houseCoordinates[0], houseCoordinates[1], results);

        return results[0];
    }

    private double[] getLatitudeAndLongitudeFromHouse(House house) {
        double[] results = new double[2];

        Geocoder geocoder = new Geocoder(mContext);

        try {
            List<Address> addresses;
            addresses = geocoder.getFromLocationName(house.getLongFormattedAddress(), 1);

            if(addresses.size() > 0) {
                results[0] = addresses.get(0).getLatitude();
                results[1] = addresses.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
}
