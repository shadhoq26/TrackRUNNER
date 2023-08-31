package aston.cs3mmd.trackrunner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

//this class is for the activity that holds all the run fragments.
public class RunActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    public Location userLocation = new Location("");
    public String userWeight;
    public String route = "";

    public String getUserWeight() {
        return userWeight;
    }

    //This is for the activity is created,
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        //This gets any information, passed into the activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userWeight = extras.getString("weight");
            if (extras.getString("route") != null) {
                route = extras.getString("route");
            }
        }
        //this creates the location services provider.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //this creates the location callback.
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    userLocation = location;
                }
            }
        };

        startLocationUpdates();

        changePage(new HomePageMap1(route));
    }
    //This is to change the fragment in the activity.
    public void changePage(Fragment page){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.beginTransaction().replace(R.id.frameLayout2,page)
                .commit();
    }

    //this is a function to start location updates.
    private void startLocationUpdates() {
        if (locationRequest == null) {
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
        //Does a permissions check to check if permissions are granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
    //This stops the location updates.
    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    //When you close activity, location updates will stop.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }
}