package aston.cs3mmd.trackrunner;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//this is the class for the home page fragment.
public class HomePage extends Fragment {

    Timer timer = new Timer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //For when the view is created.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Loads the profile data.
        ((MainActivity)getActivity()).loadProfileData();
        ProfileData profileData = ((MainActivity)getActivity()).getProfileData();

        //For the start run button is pressed.
        Button startRunButton = getView().findViewById(R.id.homepage_RunButton);
        startRunButton.setOnClickListener(new View.OnClickListener() {
            //When the button is pressed, it will open the run activity and send weight data.
            @Override
            public void onClick(View view) {
                //if the weight isn't set in the profile, it will tell the user to set a profile weight before continuing.
                if (profileData.getWeight() == null || profileData.getWeight().isEmpty() || profileData.getWeight() == "0") {
                    Toast toast = new Toast(getContext());
                    toast.setText("Set Profile Weight");
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    //Open the run activity and send the weight data.
                    Intent intent = new Intent(getContext(), RunActivity.class);
                    intent.putExtra("weight", profileData.getWeight());
                    startActivity(intent);
                }
            }
        });
        //Starts the timer for the current time.
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerMethod();
            }
        }, 0, 1000);

        //Will set the welcome text, depending on the name you have on the Profile page.
        TextView welcomeText = getView().findViewById(R.id.welcomeBackText);
        welcomeText.setText("Welcome back " + profileData.getName());

        getWeather();

    }
    //This will get the weather using Weather API.
    public void getWeather() {
        try {
            //Will get the user location.
            Criteria criteria = new Criteria();
            LocationManager locationManager = null;
            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            String provider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);

            //This will request the weather api to get weather using current location.
            String key = getResources().getString(R.string.weather_api);
            WeatherAPI weatherAPI = WeatherAPIclient.getClient().create(WeatherAPI.class);
            weatherAPI.getWeatherData(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), key, "metric").enqueue(new Callback<StoreWeatherData>() {
                @Override
                public void onResponse(Call<StoreWeatherData> call, Response<StoreWeatherData> response) {
                    if (response.isSuccessful()) {
                        ArrayList<WeatherInformation> weatherInfo = response.body().getWeather();
                        Temperature temp = response.body().getTemp();
                        try {
                            TextView tempText = getView().findViewById(R.id.homepage_WeatherText);
                            DecimalFormat decimalFormat = new DecimalFormat("#");
                            Double tempValue = Double.parseDouble(temp.getTemp());
                            tempText.setText(String.valueOf(Math.round(tempValue) + "°C"));
                            ImageView weatherImage = getView().findViewById(R.id.homepage_WeatherIcon);
                            String imageAddress = "http://openweathermap.org/img/wn/" + weatherInfo.get(0).getIcon() + "@2x.png";
                            Picasso.get()
                                .load(imageAddress)
                                .resize(130, 130)
                                .into(weatherImage);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                //if there is an error getting information, it will be printed in logcat.
                @Override
                public void onFailure(Call<StoreWeatherData> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Timer method is for the current time displayed on home page. It will be updated every second.
    public void timerMethod(){
        getActivity().runOnUiThread(timerTask);
    }
    public Runnable timerTask = new Runnable() {
        @Override
        public void run() {
            try {
                String currentTime = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());
                TextView timeText = getView().findViewById(R.id.homepageTimeText);
                timeText.setText(currentTime.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }
    //When you leave the home page, timer will be stopped.
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}