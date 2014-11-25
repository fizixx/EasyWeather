package com.fizix.android.easyweather.models;

public class SearchResult {

    private String mQueryParam;
    private String mLocation;
    private String mCountryCode;
    private double mCoordLat;
    private double mCoordLong;

    public SearchResult() {
        mQueryParam = "";
        mLocation = "";
        mCountryCode = "";
        mCoordLat = 0.0;
        mCoordLong = 0.0;
    }

    public SearchResult(String queryParam, String location, String countryCode, double coordLat, double coordLong) {
        mQueryParam = queryParam;
        mLocation = location;
        mCountryCode = countryCode;
        mCoordLat = coordLat;
        mCoordLong = coordLong;
    }

    public String getQueryParam() {
        return mQueryParam;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public double getCoordLat() {
        return mCoordLat;
    }

    public double getCoordLong() {
        return mCoordLong;
    }

    public String getCoordsAsString() {
        return String.format("%.2f - %.2f", mCoordLat, mCoordLong);
    }

    @Override
    public String toString() {
        return "Search result: " + mLocation;
    }

}
