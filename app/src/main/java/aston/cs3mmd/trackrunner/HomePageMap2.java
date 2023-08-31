package aston.cs3mmd.trackrunner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//This class is for the actual map page.
public class HomePageMap2 extends Fragment implements OnMapReadyCallback {
    GoogleMap googleMap;
    String start, end;
    private Timer timer;
    private Timer distanceTimer;

    int time = 0;
    float oldDistance = 0;
    double KCALtotal = 0;
    List<LatLng> routeLine = new ArrayList<>();
    float currentDistance = 0;
    int distanceInterval = 3000;

    //This stores all the distance markers.
    private ArrayList<DistanceMarkerPoint> distanceMarkers = new ArrayList<>();

    //Constructor that sets start/end destination variables.
    public HomePageMap2(String start, String end) {
        this.start = start;
        this.end = end;
    }
    //This will set the timer variables.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timer = new Timer();
        distanceTimer = new Timer();
    }
    //This will set the timer tick method.
    private void runTimerMethod() {((RunActivity) getActivity()).runOnUiThread(Timer_Tick);
    }

    //this function gets the distance from each timer tick.
    private void distanceTimerMethod() {
        ((RunActivity) getActivity()).runOnUiThread(DISTANCE_Timer_Tick);
        Location cl = ((RunActivity)getActivity()).userLocation;
        Log.i("FGH", "New marker recorded: " + cl.getLatitude() + ", " + cl.getLongitude());

        //If there is more then 1 marker in the distance marker, calculate distance.
        if (distanceMarkers.size() >= 1) {
            LatLng lastLoc = distanceMarkers.get(distanceMarkers.size()-1).getLocation();
            if (lastLoc.latitude != cl.getLatitude() && lastLoc.longitude != cl.getLongitude()) {

                //calculates the speed.
                Location p1 = new Location("");
                p1.setLatitude(distanceMarkers.get(distanceMarkers.size()-1).getLocation().latitude);
                p1.setLongitude(distanceMarkers.get(distanceMarkers.size()-1).getLocation().longitude);

                //Setting the text for the speed.
                float distance = p1.distanceTo(cl);
                double distanceTime = ((double) time - distanceMarkers.get(distanceMarkers.size()-1).getTime());
                double speed = distance/(distanceTime);
                TextView speedText = getView().findViewById(R.id.viewSpeed);
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                speedText.setText(String.valueOf(Double.parseDouble(decimalFormat.format(speed))+ " mph"));


                //Adding a point to distance markers.
                distanceMarkers.add(new DistanceMarkerPoint(new LatLng(cl.getLatitude(), cl.getLongitude()), time));
            }
        } else {
            distanceMarkers.add(new DistanceMarkerPoint(new LatLng(cl.getLatitude(), cl.getLongitude()), time));
        }
    }
    //Runs everytime the timer ticks/updates the time.
    private Runnable Timer_Tick = new Runnable() {
        @Override
        public void run() {
            TextView timeText = getView().findViewById(R.id.viewTime);
            String seconds = String.valueOf(time % 60);
            String minutes = String.valueOf(time / 60);
            String hours = String.valueOf((time / 60) / 60);

            seconds = (seconds.length() <= 1) ? "0" + seconds : seconds;
            minutes = (minutes.length() <= 1) ? "0" + minutes : minutes;
            hours = (hours.length() <= 1) ? "0" + hours : hours;

            String timeString = hours + ":" + minutes + ":" + seconds;

            timeText.setText(String.valueOf(timeString));
            time++;
        }
    };
    //This will calculate the distance everytime the timer ticks.
    private Runnable DISTANCE_Timer_Tick = new Runnable() {
        @Override
        public void run() {
            float distance = 0 + oldDistance;
            //Loops through distance markers and calculates the total distance.
            if (distanceMarkers.size() >= 2) {
                for (int i = 1; i < distanceMarkers.size(); i++) {
                    Location p1 = new Location("");
                    p1.setLatitude(distanceMarkers.get(i-1).getLocation().latitude);
                    p1.setLongitude(distanceMarkers.get(i-1).getLocation().longitude);

                    Location p2 = new Location("");
                    p2.setLatitude(distanceMarkers.get(i).getLocation().latitude);
                    p2.setLongitude(distanceMarkers.get(i).getLocation().longitude);

                    distance +=  p1.distanceTo(p2);
                    currentDistance = distance;

                    Location cl = ((RunActivity)getActivity()).userLocation;
                    LatLng goal = routeLine.get(routeLine.size()-1);
                    Location goalLocation = new Location("");
                    goalLocation.setLatitude(goal.latitude);
                    goalLocation.setLongitude(goal.longitude);
                    if (cl.distanceTo(goalLocation) <= 5.0) {
                        endRun(currentDistance);
                    }
                }
            }
            //Formats the distance and displaying it. It also converts the distance to miles.
            Double miles = distance * 0.000621;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            Double milesDistance = Double.parseDouble(decimalFormat.format(miles));
            Log.i("FGH", "DISTANCE MILES: " + milesDistance);
            TextView distanceText = getView().findViewById(R.id.viewDistance);
            distanceText.setText(milesDistance + " miles");

        }
    };

    //When run is ended, the timers will stop and change the page to Home Page Map 3/Results page.
    private void endRun(float distance) {
        timer.cancel();
        distanceTimer.cancel();
        ((RunActivity)getActivity()).changePage(new HomePageMap3(time, distance, KCALtotal, start, end));
    }
    //When the page is closed the timer will stop.
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        distanceTimer.cancel();
    }

    //This will run when the view is created.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.homePageMap);
        //Sets the map fragment.
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        //Stop button will end the run when  clicked.
        Button stopButton = getView().findViewById(R.id.runStopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endRun(currentDistance);
            }
        });
        //This is the pause button which will pause the run/time when clicked.
        Button pauseButton = getView().findViewById(R.id.runPauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This is for when the pause button is clicked and changes the text from pause to resume.
                if ("Pause".equals(pauseButton.getText())) {
                    pauseButton.setText("Resume");
                    timer.cancel();
                    distanceTimer.cancel();
                    ConstraintLayout pausedContainer = getView().findViewById(R.id.pausedContainer);
                    pausedContainer.setVisibility(View.VISIBLE);

                    float distance = 0;
                    //This calculates the distance and saves it.
                    if (distanceMarkers.size() >= 2) {
                        for (int i = 1; i < distanceMarkers.size(); i++) {
                            Location p1 = new Location("");
                            p1.setLatitude(distanceMarkers.get(i-1).getLocation().latitude);
                            p1.setLongitude(distanceMarkers.get(i-1).getLocation().longitude);

                            Location p2 = new Location("");
                            p2.setLatitude(distanceMarkers.get(i).getLocation().latitude);
                            p2.setLongitude(distanceMarkers.get(i).getLocation().longitude);

                            distance +=  p1.distanceTo(p2) ;
                        }
                    }
                    oldDistance = distance;
                    distanceMarkers = new ArrayList<>();
                } else if ("Resume".equals(pauseButton.getText())) {
                    //This is for when you resume the run, everything will carry on from where it was before it was paused.
                    pauseButton.setText("Pause");
                    ConstraintLayout pausedContainer = getView().findViewById(R.id.pausedContainer);
                    pausedContainer.setVisibility(View.GONE);

                    timer = new Timer();
                    distanceTimer = new Timer();

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runTimerMethod();
                        }
                    }, 0, 1000);

                    distanceTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            distanceTimerMethod();
                        }
                    }, 0, distanceInterval);
                }
            }
        });
    }
    //This is for getting the route for the path.
    private void callDirection() {
        String location = start;
        String destination = end;
        String key = getResources().getString(R.string.google_map_api);
        GoogleAPI googleAPI = clientAPI.getClient().create(GoogleAPI.class);

        //This is for requesting API to get route.
        googleAPI.getRoot(location, "WALKING", destination, key).enqueue(new Callback<RouteResults>() {

            @Override
            public void onResponse(Call<RouteResults> call, Response<RouteResults> response) {
                //if the API response is successful
                if (response.isSuccessful()) {
                    //This will check if there are any routes, if there isnt a route it will go back.
                    if ("ZERO_RESULTS".equals(response.body().getStatus()) || "NOT_FOUND".equals(response.body().getStatus())) {
                        ((RunActivity) getActivity()).finish();
                        Toast toast = new Toast(getContext());
                        toast.setText("Route Not Found");
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                    //Gets the route data and puts it on the map, camera will show the entire path from your location to the destination.
                    List<RouteStorage> results = response.body().getResults();
                    List<LatLng> routeList = new ArrayList<>();
                    LatLngBounds latLngBounds = null;
                    for (RouteStorage r : results) {
                        String routeData = r.getLineData().getPoints();
                        routeList = PolyUtil.decode(routeData);
                        LatLng southWestBound = new LatLng(Double.parseDouble(r.getBounds().getSouthwest().getLat()), Double.parseDouble(r.getBounds().getSouthwest().getLng()));
                        LatLng northEastBound = new LatLng(Double.parseDouble(r.getBounds().getNortheast().getLat()), Double.parseDouble(r.getBounds().getNortheast().getLng()));
                        latLngBounds = new LatLngBounds(southWestBound, northEastBound);
                    }
                    routeLine = routeList;
                    googleMap.addPolyline(new PolylineOptions().addAll(routeList).width(10).color(Color.GREEN));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 120));
                    LatLng pos = routeList.get(routeList.size() - 1);
                    googleMap.addMarker(new MarkerOptions().position(pos).title("End Goal"));
                    distanceMarkers = new ArrayList<>();

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runTimerMethod();
                        }
                    }, 0, 1000);

                    distanceTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            distanceTimerMethod();
                        }
                    }, 0, distanceInterval);

                }

            }
            @Override
            public void onFailure(Call<RouteResults> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MapsInitializer.initialize(getContext(), MapsInitializer.Renderer.LATEST, new OnMapsSdkInitializedCallback() {
            @Override
            public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
            }
        });
        return inflater.inflate(R.layout.fragment_home_page_map2, container, false);
    }
    //This function will run when the map is loaded.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        //This will do a permissions check.
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //tHis will show users current location.
        googleMap.setMyLocationEnabled(true);
        //Will get the path but if it says "Current Location", it will use the current location
        //Or if there is no "Current Location set they will use the path the user set themselves.
        if ("Current Location".equals(start)) {
            Location loc = ((RunActivity)getActivity()).userLocation;
            start =  loc.getLatitude() + "," + loc.getLongitude();
            callDirection();
        } else {
            callDirection();
        }
    }
}