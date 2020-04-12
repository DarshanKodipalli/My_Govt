package com.example.know_your_govt;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.widget.ProgressBar;
import android.widget.Toast;
import static android.content.Context.LOCATION_SERVICE;

public class LocationDetector{

    private MainActivity mainActivity;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public LocationDetector(MainActivity ma) {
        this.mainActivity = ma;
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mainActivity);
        checkPermission();
            try {
                progressDialog.setMessage("Setting Current Location");
                progressDialog.setTitle("Loading Location");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(true);
                progressDialog.show();
                Thread.sleep(2500);
            }catch (Exception e){
                e.printStackTrace();
            }
        progressDialog.dismiss();
        setUpLocationManager();
        determineLocation();
    }

    public void determineLocation() {
        if (!checkPermission())
            return;
        if (locationManager == null)
            setUpLocationManager();
        if (locationManager != null) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                mainActivity.setLocation(loc.getLatitude(), loc.getLongitude());
                Toast.makeText(mainActivity, "Using " + LocationManager.NETWORK_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (locationManager != null) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                mainActivity.setLocation(loc.getLatitude(), loc.getLongitude());
                Toast.makeText(mainActivity, "Using " + LocationManager.GPS_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (locationManager != null) {
            Location loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (loc != null) {
                mainActivity.setLocation(loc.getLatitude(), loc.getLongitude());
                Toast.makeText(mainActivity, "Using " + LocationManager.PASSIVE_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        return;
    }

    private void setUpLocationManager() {
        if (locationManager != null)
            return;

        if (!checkPermission())
            return;
            locationManager = (LocationManager) mainActivity.getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    mainActivity.setLocation(location.getLatitude(), location.getLongitude());
                }
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
                public void onProviderEnabled(String provider) {
                }
                public void onProviderDisabled(String provider) {
                }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    public void shutdown() {
        if(locationListener!=null){
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 50);
            return false;
        }
        return true;
    }
}