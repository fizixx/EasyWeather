package com.fizix.android.easyweather.models;

public class SearchResult {

    private String mCityName;
    private String mCountryCode;
    private double mCoordLat;
    private double mCoordLong;

    public SearchResult() {
        mCityName = "Unknown";
        mCountryCode = "Unknown";
        mCoordLat = 0.0;
        mCoordLong = 0.0;
    }

    public SearchResult(String cityName, String countryCode, double coordLat, double coordLong) {
        mCityName = cityName;
        mCountryCode = countryCode;
        mCoordLat = coordLat;
        mCoordLong = coordLong;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public double getCoordLat() {
        return mCoordLat;
    }

    public void setCoordLat(double coordLat) {
        mCoordLat = coordLat;
    }

    public double getCoordLong() {
        return mCoordLong;
    }

    public void setCoordLong(double coordLong) {
        mCoordLong = coordLong;
    }

    public String getCoordsAsString() {
        return String.format("%.2f - %.2f", mCoordLat, mCoordLong);
    }

    @Override
    public String toString() {
        return "Search result: " + mCityName;
    }

}
