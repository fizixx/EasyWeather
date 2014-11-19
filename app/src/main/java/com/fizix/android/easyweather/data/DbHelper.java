package com.fizix.android.easyweather.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "easy_weather.db";
    private static final String LOG_TAG = DbHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold locations.
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + Contract.Location.TABLE_NAME + " (" +
                Contract.Location._ID + " INTEGER PRIMARY KEY, " +
                Contract.Location.COL_LOCATION + " TEXT UNIQUE NOT NULL, " +
                Contract.Location.COL_CITY_NAME + " TEXT NOT NULL, " +
                Contract.Location.COL_COORD_LAT + " REAL NOT NULL, " +
                Contract.Location.COL_COORD_LONG + " REAL NOT NULL, " +
                "UNIQUE (" + Contract.Location.COL_LOCATION + ") ON CONFLICT REPLACE);";

        Log.i(LOG_TAG, "Creating locations table: " + SQL_CREATE_LOCATION_TABLE);

        db.execSQL(SQL_CREATE_LOCATION_TABLE);

        // Create a table to hold the forecast entry for each day.
        final String SQL_CREATE_DAY_ENTRY_TABLE = "CREATE TABLE " + Contract.DayEntry.TABLE_NAME + " (" +
                Contract.DayEntry._ID + " INTEGER PRIMARY KEY, " +

                Contract.DayEntry.COL_LOCATION_ID + " INTEGER NOT NULL, " +

                Contract.DayEntry.COL_DATE + " TEXT NOT NULL, " +

                Contract.DayEntry.COL_TEMP_HIGH + " REAL, " +
                Contract.DayEntry.COL_TEMP_LOW + " REAL, " +

                Contract.DayEntry.COL_ICON + " TEXT, " +

                Contract.DayEntry.COL_QPF_ALL_DAY + " INTEGER, " +
                Contract.DayEntry.COL_QPF_DAY + " INTEGER, " +
                Contract.DayEntry.COL_QPF_NIGHT + " INTEGER, " +

                Contract.DayEntry.COL_SNOW_ALL_DAY + " REAL, " +
                Contract.DayEntry.COL_SNOW_DAY + " REAL, " +
                Contract.DayEntry.COL_SNOW_NIGHT + " REAL, " +

                Contract.DayEntry.COL_MAX_WIND_SPEED + " INTEGER, " +
                Contract.DayEntry.COL_MAX_WIND_DEGREES + " INTEGER, " +

                Contract.DayEntry.COL_AVG_WIND_SPEED + " INTEGER, " +
                Contract.DayEntry.COL_AVG_WIND_DEGREES + " INTEGER, " +

                Contract.DayEntry.COL_AVG_HUMIDITY + " INTEGER, " +
                Contract.DayEntry.COL_MAX_HUMIDITY + " INTEGER, " +
                Contract.DayEntry.COL_MIN_HUMIDITY + " INTEGER, " +

                // Set up the location_id column as a foreign key to location table.
                "FOREIGN KEY (" + Contract.DayEntry.COL_LOCATION_ID + ") REFERENCES " + Contract.Location.TABLE_NAME + " (" + Contract.Location._ID + "), " +

                // To ensure the application only has one weather entry per day per location, we create a UNIQUE contraint with REPLACE strategy.
                "UNIQUE (" + Contract.DayEntry.COL_DATE + ", " + Contract.DayEntry.COL_LOCATION_ID + ") ON CONFLICT REPLACE" +
                ");";

        Log.i(LOG_TAG, "Creating day_entry table: " + SQL_CREATE_DAY_ENTRY_TABLE);

        db.execSQL(SQL_CREATE_DAY_ENTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP_TABLE_IF EXISTS " + Contract.DayEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Location.TABLE_NAME);
        onCreate(db);
    }
}
