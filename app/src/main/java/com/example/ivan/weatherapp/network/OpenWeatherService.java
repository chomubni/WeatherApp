package com.example.ivan.weatherapp.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.widget.Toast;

import com.example.ivan.weatherapp.MainActivity;
import com.example.ivan.weatherapp.database.OpenWeaterContract;
import com.example.ivan.weatherapp.database.OpenWeatherHelper;
import com.example.ivan.weatherapp.model.CityWeather;
import com.example.ivan.weatherapp.model.Main;
import com.example.ivan.weatherapp.model.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ivan.weatherapp.MainActivity.WEATHER_ACTION;
import static com.example.ivan.weatherapp.database.OpenWeaterContract.*;

/**
 * Created by ivan on 25.09.17.
 */

public class OpenWeatherService extends IntentService {

    private static final String KEY = "d2a6b21c943e38d9e44edcc03c9912ad";
    private OpenWeatherHelper mOpenWeatherHelper;
    SQLiteDatabase mDatabase;

    String mCityName = null;

    public OpenWeatherService() {
        super("OpenWeatherService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mCityName = intent.getStringExtra(MainActivity.EXTRA_CITY_NAME);
        mOpenWeatherHelper = new OpenWeatherHelper(getApplicationContext());
        mDatabase = mOpenWeatherHelper.getWritableDatabase();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
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

                String name = cityWeather.getName();
                String tempMin = String.valueOf(main.getTempMin());
                String tempMax = String.valueOf(main.getTempMax());
                String iconId = list.get(0).getIcon();

                ContentValues contentValues = new ContentValues();
                contentValues.put(CityEntry.COLUMN_CITY_NAME, name);
                contentValues.put(CityEntry.COLUMN_TEMP_MIN, tempMin);
                contentValues.put(CityEntry.COLUMN_TEMP_MAX, tempMax);
                contentValues.put(CityEntry.COLUMN_ICON_ID, iconId);

                Long id = mDatabase.insert(CityEntry.TABLE_NAME,null,contentValues);
                Intent intent = new Intent(WEATHER_ACTION);
                intent.putExtra("rowId",id);
                sendBroadcast(intent);
                mDatabase.close();
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Smth went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
