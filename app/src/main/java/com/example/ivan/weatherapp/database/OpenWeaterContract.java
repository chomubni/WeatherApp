package com.example.ivan.weatherapp.database;

import android.provider.BaseColumns;

/**
 * Created by ivan on 26.09.17.
 */

public class OpenWeaterContract {

    public OpenWeaterContract(){}

    public static abstract class CityEntry implements BaseColumns{
        public static final String TABLE_NAME = "_table";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_CITY_NAME = "city_name";
        public static final String COLUMN_TEMP_MIN = "temp_min";
        public static final String COLUMN_TEMP_MAX = "temp_max";
        public static final String COLUMN_ICON_ID = "icon_id";
    }
}
