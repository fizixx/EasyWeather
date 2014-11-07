package com.fizix.android.easyweather.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;

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

        updateTabInfo();
    }

    private void updateTabInfo() {
        Cursor cursor = mContext.getContentResolver().query(Contract.Location.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            mLocations.clear();
            do {
                mLocations.add(new LocationInfo(cursor));
                mLocations.add(new LocationInfo(2, "This is a longer title", 0.0f, 0.0f));
                mLocations.add(new LocationInfo(3, "Another title", 0.0f, 0.0f));
                mLocations.add(new LocationInfo(4, "One more just for testing purposes", 0.0f, 0.0f));
            } while (cursor.moveToNext());
        }
    }

    @Override
    public Fragment getItem(int position) {
        LocationInfo locationInfo = mLocations.get(position);
        return WeatherListFragment.newInstance(locationInfo.getId());
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
