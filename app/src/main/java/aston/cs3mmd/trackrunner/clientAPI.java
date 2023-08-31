package aston.cs3mmd.trackrunner;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//this class is for the retrofit and for the use of API
public class clientAPI {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/").addConverterFactory(GsonConverterFactory.create()).client(httpClient).build();
        return retrofit;
    }
}
