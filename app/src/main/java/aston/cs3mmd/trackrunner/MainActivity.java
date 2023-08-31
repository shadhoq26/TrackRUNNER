package aston.cs3mmd.trackrunner;

import static com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import aston.cs3mmd.trackrunner.databinding.ActivityMainBinding;
//This is a class for the main activity.
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    LocationCallback locationCallBack;
    Location currentLocation;
    ProfileData profileData = new ProfileData();

    //This will run when the app first loads in.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        //Gets any information passed into the main activity.
        Bundle extras = getIntent().getExtras();
        //if the extra information is set, it will go to that page or else it will go to the home page.
        if (extras != null) {
            if (extras.getBoolean("profilePage")) {
                changePage(new ProfilePage());
            } else if (extras.getBoolean("analysis")) {
                changePage(new AnalysisPage());
            } else {
                changePage(new HomePage());
            }
        } else {
            changePage(new HomePage());
        }

        //This is for navigating to the different pages for the bottom navigation buttons.
        binding.bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottomNavHomeButton:
                    changePage(new HomePage());
                    break;
                case R.id.bottomNavProfileButton:
                    changePage(new ProfilePage());
                    break;
                case R.id.bottomNavAnalysisButton:
                    changePage(new AnalysisPage());
                    break;
                case R.id.bottomNavSettingsButton:
                    changePage(new SettingsPage());
                    break;
            }
            return true;
        });
        //This is a permission check for location permissions.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityResultLauncher<String[]> permissionrequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean findLocationgranted = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    findLocationgranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                }
                Boolean courseLocationgranted = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    courseLocationgranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                }
                if (findLocationgranted != null && findLocationgranted) {
                    Log.i("FGH", "Find location is granted");
                    //getLastCurrentLocation();
                } else if (courseLocationgranted != null && courseLocationgranted) {
                    Log.i("FGH", "Course location is granted");
                    //getLastCurrentLocation();
                } else {
                    Log.i("FGH", "no location is granted");
                }
            });
            //This will launch permission requests, ask for the location permission.
            permissionrequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            Log.i("FGH", "Permission is already granted");
            //getLastCurrentLocation();

        }
    }

    //This is for changing the fragment page.
    public void changePage(Fragment page) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, page)
                .commit();
    }
    //This is for calling the last location.(Gets the users last location)
    public void getLastCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                }
            }
        });
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
    //loads in the profile data from the files.
    public void loadProfileData() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput("profileData.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                profileData = new ProfileData();
                profileData.setName(data[0]);
                profileData.setGender(data[1]);
                profileData.setWeight(data[2]);
                profileData.setHeight(data[3]);
                profileData.setWeightGoal(data[4]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //return the profile data.
    public ProfileData getProfileData() {
        return profileData;
    }
}


