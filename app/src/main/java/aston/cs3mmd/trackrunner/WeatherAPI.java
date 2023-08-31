package aston.cs3mmd.trackrunner;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
//This interface is for the Weather API
public interface WeatherAPI {
    @GET("data/2.5/weather")
    Call<StoreWeatherData> getWeatherData(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String appID,
            @Query("units") String units
    );
}
