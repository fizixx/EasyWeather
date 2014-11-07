package com.fizix.android.easyweather.data;

import android.database.Cursor;

public class LocationInfo {

    private long mId;
    private String mCityName;
    private float mCoordLat;
    private float mCoordLong;

    public LocationInfo(long id, String cityName, float coordLat, float coordLong) {
        mId = id;
        mCityName = cityName;
        mCoordLat = coordLat;
        mCoordLong = coordLong;
    }

    public LocationInfo(Cursor cursor) {
        loadFromCursor(cursor);
    }

    public long getId() {
        return mId;
    }

    public String getCityName() {
        return mCityName;
    }

    public float getCoordLat() {
        return mCoordLat;
    }

    public float getCoordLong() {
        return mCoordLong;
    }

    public void loadFromCursor(Cursor cursor) {
        mId = cursor.getLong(cursor.getColumnIndex(Contract.Location._ID));
        mCityName = cursor.getString(cursor.getColumnIndex(Contract.Location.COL_CITY_NAME));
        mCoordLat = cursor.getFloat(cursor.getColumnIndex(Contract.Location.COL_COORD_LAT));
        mCoordLong = cursor.getFloat(cursor.getColumnIndex(Contract.Location.COL_COORD_LONG));
    }

}
