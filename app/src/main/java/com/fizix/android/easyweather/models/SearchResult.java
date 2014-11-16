package com.fizix.android.easyweather.models;

public class SearchResult {

    private String mLocation;
    private String mCountryCode;
    private double mCoordLat;
    private double mCoordLong;

    public SearchResult() {
        mLocation = "Unknown";
        mCountryCode = "Unknown";
        mCoordLat = 0.0;
        mCoordLong = 0.0;
    }

    public SearchResult(String location, String countryCode, double coordLat, double coordLong) {
        mLocation = location;
        mCountryCode = countryCode;
        mCoordLat = coordLat;
        mCoordLong = coordLong;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
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
        return "Search result: " + mLocation;
    }

}
