package com.example.ivan.weatherapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.ivan.weatherapp.database.OpenWeaterContract.*;

/**
 * Created by ivan on 26.09.17.
 */

public class OpenWeatherHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "OpenWeather.db";
    public static int DB_VERSION = 1;

    public OpenWeatherHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createCityTable = "create table "+ CityEntry.TABLE_NAME+"(" +
                CityEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                CityEntry.COLUMN_CITY_NAME + " TEXT, " +
                CityEntry.COLUMN_TEMP_MIN + " TEXT, " +
                CityEntry.COLUMN_TEMP_MAX + " TEXT, " +
                CityEntry.COLUMN_ICON_ID + " TEXT);";
        sqLiteDatabase.execSQL(createCityTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
