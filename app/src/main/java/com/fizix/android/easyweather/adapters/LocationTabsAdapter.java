package com.fizix.android.easyweather.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.fizix.android.easyweather.WeatherListFragment;
import com.fizix.android.easyweather.data.Contract;
import com.fizix.android.easyweather.data.LocationInfo;

import java.util.ArrayList;

public class LocationTabsAdapter extends FragmentPagerAdapter {

    private static final String LOG_TAG = LocationTabsAdapter.class.getSimpleName();

    private final Context mContext;

    private ArrayList<LocationInfo> mLocations = new ArrayList<LocationInfo>();

    public LocationTabsAdapter(ActionBarActivity activity) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
    }

    public void addLocationInfo(LocationInfo locationInfo) {
        mLocations.add(locationInfo);
    }

    @Override
    public Fragment getItem(int position) {
        LocationInfo locationInfo = mLocations.get(position);
        return WeatherListFragment.newInstance(locationInfo.getId(), locationInfo.getLocation());
    }

    @Override
    public int getCount() {
        return mLocations.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        LocationInfo locationInfo = mLocations.get(position);
        return locationInfo.getCityName();
    }

}
