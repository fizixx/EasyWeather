package com.fizix.android.easyweather.data;

import android.database.Cursor;

public class LocationInfo {

    private long mId;
    private String mQueryParam;
    private String mCityName;

    public LocationInfo(long id, String queryParam, String cityName) {
        mId = id;
        mQueryParam = queryParam;
        mCityName = cityName;
    }

    public LocationInfo(Cursor cursor) {
        loadFromCursor(cursor);
    }

    public long getId() {
        return mId;
    }

    public String getQueryParam() {
        return mQueryParam;
    }

    public String getCityName() {
        return mCityName;
    }

    public void loadFromCursor(Cursor cursor) {
        mId = cursor.getLong(cursor.getColumnIndex(Contract.Location._ID));
        mQueryParam = cursor.getString(cursor.getColumnIndex(Contract.Location.COL_QUERY_PARAM));
        mCityName = cursor.getString(cursor.getColumnIndex(Contract.Location.COL_CITY_NAME));
    }

}
