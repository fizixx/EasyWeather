package com.fizix.android.easyweather.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "easy_weather.db";
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Location.TABLE_NAME);
        onCreate(db);
    }
}
