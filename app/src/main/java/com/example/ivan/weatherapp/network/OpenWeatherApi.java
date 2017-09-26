package com.example.ivan.weatherapp.network;

import com.example.ivan.weatherapp.model.CityWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ivan on 25.09.17.
 */

public interface OpenWeatherApi {
    @GET("/data/2.5/weather")
    Call<CityWeather> getCityWeater(@Query("q") String name, @Query("APPID") String key);
}
