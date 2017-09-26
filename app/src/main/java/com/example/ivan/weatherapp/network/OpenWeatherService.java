package com.example.ivan.weatherapp.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.ivan.weatherapp.MainActivity;
import com.example.ivan.weatherapp.model.CityWeather;
import com.example.ivan.weatherapp.model.Main;
import com.example.ivan.weatherapp.model.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ivan.weatherapp.MainActivity.WEATHER_ACTION;

/**
 * Created by ivan on 25.09.17.
 */

public class OpenWeatherService extends IntentService {

    private static final String KEY = "d2a6b21c943e38d9e44edcc03c9912ad";
    public static final String EXTRA_CITY_NAME = "city_name";
    public static final String EXTRA_CITY_MIN_TEMP = "min_temp";
    public static final String EXTRA_CITY_MAX_TEMP = "max_temp";
    public static final String EXTRA_WEATHER_ICON_ID = "weather_icon_id";

    String mCityName = null;

    public OpenWeatherService() {
        super("OpenWeatherService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mCityName = intent.getStringExtra(MainActivity.EXTRA_CITY_NAME);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //mCityName = intent.getExtras().getString(MainActivity.EXTRA_CITY_NAME);
        makeRequestByCityName(mCityName);
    }

    public void makeRequestByCityName(String cName) {
        RetrofitClient retrofitClient = new RetrofitClient();
        Call<CityWeather> call = retrofitClient.getOpenWeatherApi().getCityWeater(cName, KEY);
        call.enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                CityWeather cityWeather = response.body();
                List<Weather> list = cityWeather.getWeather();
                Main main = cityWeather.getMain();
                Intent intent = new Intent(WEATHER_ACTION);
                String name = cityWeather.getName();
                intent.putExtra(EXTRA_CITY_NAME, name);
                intent.putExtra(EXTRA_CITY_MIN_TEMP, String.valueOf(main.getTempMin()));
                intent.putExtra(EXTRA_CITY_MAX_TEMP, String.valueOf(main.getTempMax()));
                intent.putExtra(EXTRA_WEATHER_ICON_ID, list.get(0).getIcon());
                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Smth went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
