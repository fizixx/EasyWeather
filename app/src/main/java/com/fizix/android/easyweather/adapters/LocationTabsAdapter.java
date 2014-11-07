package com.fizix.android.easyweather.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;

import com.fizix.android.easyweather.WeatherListFragment;
import com.fizix.android.easyweather.data.Contract;

public class LocationTabsAdapter extends FragmentPagerAdapter {

    private static final String LOG_TAG = LocationTabsAdapter.class.getSimpleName();

    private final Context mContext;

    private long[] mLocationIds;

    public LocationTabsAdapter(ActionBarActivity activity) {
        super(activity.getSupportFragmentManager());
        mContext = activity;

        updateRowIds();
    }

    private void updateRowIds() {
        Cursor cursor = mContext.getContentResolver().query(Contract.Location.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            mLocationIds = new long[cursor.getCount()];
            int i = 0;
            do {
                mLocationIds[i] = cursor.getLong(0);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public Fragment getItem(int i) {
        long locationId = mLocationIds[i];
        return WeatherListFragment.newInstance(locationId);
    }

    @Override
    public int getCount() {
        return mLocationIds.length;
    }

}
