package com.example.ivan.weatherapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivan.weatherapp.database.OpenWeatherHelper;
import com.example.ivan.weatherapp.network.OpenWeatherService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.ivan.weatherapp.database.OpenWeaterContract.*;

public class MainActivity extends Activity {

    public static final String EXTRA_CITY_NAME = "name";
    public static final String WEATHER_ACTION = "com.example.ivan.weatherapp.action";
    BroadcastReceiver broadcastReceiver;
    private OpenWeatherHelper mOpenWeatherHelper;
    private SQLiteDatabase mDatabase;
    Cursor cursor;

    private String iconId;
    private Long rowId;

    @BindView(R.id.city_name) TextView cityNameTxtv;
    @BindView(R.id.weater_image) ImageView weatherImageImgv;
    @BindView(R.id.get_weather) Button getWeatherBtn;
    @BindView(R.id.weather_info) TextView weatherInfoTxtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mOpenWeatherHelper = new OpenWeatherHelper(this);

        getWeatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor = getCursorLastQuery();
                setMianViews(cursor);
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                rowId = intent.getLongExtra("rowId",0);
            }
        };
        IntentFilter intentFilter = new IntentFilter(WEATHER_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
        startOpenWeatherServiceByCityName("Tokyo");
    }

    private void setMianViews(Cursor c) {
        c.moveToFirst();
        iconId = c.getString(c.getColumnIndex(CityEntry.COLUMN_ICON_ID));
        String iconUrl = "http://openweathermap.org/img/w/" + iconId + ".png";
        Picasso.with(getApplicationContext()).load(iconUrl).resize(250,250).into(weatherImageImgv);
        StringBuilder sb = new StringBuilder();
        sb.append("Temp MIN : " + c.getString(c.getColumnIndex(CityEntry.COLUMN_TEMP_MIN)) + "F \n"
        + "Temp MAX : " + c.getString(c.getColumnIndex(CityEntry.COLUMN_TEMP_MAX)) + "F \n");
        weatherInfoTxtv.setText(sb.toString());
        cityNameTxtv.setText(cursor.getString(cursor.getColumnIndex(CityEntry.COLUMN_CITY_NAME)));
    }

    private Cursor getCursorLastQuery() {
        Cursor tempCursor;
        mDatabase = mOpenWeatherHelper.getReadableDatabase();
        String recordId = String.valueOf(rowId);
        String selection = CityEntry.COLUMN_ID + "=?";
        String[] selectionArgs = new String[] {recordId};
        tempCursor = mDatabase.query(
                CityEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,null,null);
        return tempCursor;
    }

    private void startOpenWeatherServiceByCityName(String cityName) {
        Intent intent = new Intent(this, OpenWeatherService.class);
        intent.putExtra(EXTRA_CITY_NAME, cityName);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        mDatabase.close();
    }
}
