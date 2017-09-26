package com.example.ivan.weatherapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ivan on 25.09.17.
 */

public class RetrofitClient {
    private static OpenWeatherApi openWeatherApi;
    String baseUrl = "http://api.openweathermap.org";
    private Retrofit retrofit;

    public OpenWeatherApi getOpenWeatherApi() {
        return openWeatherApi;
    }

    public RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherApi = retrofit.create(OpenWeatherApi.class);
    }


}
