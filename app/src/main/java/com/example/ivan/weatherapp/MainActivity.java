package com.example.ivan.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivan.weatherapp.network.OpenWeatherService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CITY_NAME = "name";
    public static final String WEATHER_ACTION = "com.example.ivan.weatherapp.action";
    BroadcastReceiver broadcastReceiver;

    private String cName;
    private String tempMin;
    private String tempMax;
    private String iconId;

    @BindView(R.id.city_name) TextView cityNameTxtv;
    @BindView(R.id.weater_image) ImageView weatherImageImgv;
    @BindView(R.id.get_weather) Button getWeatherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        getWeatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityNameTxtv.setText(cName);
                String iconUrl = "http://openweathermap.org/img/w/" + iconId + ".png";
                Picasso.with(getApplicationContext()).load(iconUrl).resize(250,250).into(weatherImageImgv);
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                cName = intent.getExtras().getString(OpenWeatherService.EXTRA_CITY_NAME);
                tempMin = intent.getExtras().getString(OpenWeatherService.EXTRA_CITY_MIN_TEMP);
                tempMax = intent.getExtras().getString(OpenWeatherService.EXTRA_CITY_MAX_TEMP);
                iconId = intent.getExtras().getString(OpenWeatherService.EXTRA_WEATHER_ICON_ID);

            }
        };

       // cityNameTxtv.setText("H");
        IntentFilter intentFilter = new IntentFilter(WEATHER_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
        startOpenWeatherServiceByCityName("Lviv");
    }

    private void startOpenWeatherServiceByCityName(String cityName) {
        Intent intent = new Intent(this, OpenWeatherService.class);
        intent.putExtra(EXTRA_CITY_NAME, cityName);
        startService(intent);
    }
}
