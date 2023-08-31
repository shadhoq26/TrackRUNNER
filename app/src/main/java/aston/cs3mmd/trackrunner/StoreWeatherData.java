package aston.cs3mmd.trackrunner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
//This stores the weather data.
public class StoreWeatherData implements Serializable {
    @SerializedName("weather")
    ArrayList<WeatherInformation> weather = new ArrayList<>();

    @SerializedName("main")
    Temperature temp;

    public ArrayList<WeatherInformation> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherInformation> weather) {
        this.weather = weather;
    }

    public Temperature getTemp() {
        return temp;
    }

    public void setTemp(Temperature temp) {
        this.temp = temp;
    }
}
