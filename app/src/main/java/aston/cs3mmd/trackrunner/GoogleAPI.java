package aston.cs3mmd.trackrunner;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
//This interface is for the Google API
public interface GoogleAPI {
    @GET("directions/json")
    Call <RouteResults> getRoot(
            @Query("origin") String origin,
            @Query("travelMode") String travelMode,
            @Query("destination") String destination,
            @Query("key") String key

    );
}
