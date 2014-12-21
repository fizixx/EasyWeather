package com.fizix.android.easyweather;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fizix.android.easyweather.adapters.DayEntryAdapter;
import com.fizix.android.easyweather.data.Contract;
import com.fizix.android.easyweather.utils.FetchWeatherTask;

import java.util.Date;

public class WeatherListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = WeatherListFragment.class.getSimpleName();

    private static final String ARG_LOCATION_ID = "location_id";
    private static final String ARG_CITY_NAME = "city_name";
    private static final String ARG_QUERY_PARAM = "query_param";

    // Params
    private long mLocationId;
    private String mCityName;
    private String mQueryParam;

    private ListView mDayEntryList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // The adapter used by the day entry list.
    DayEntryAdapter mDayEntryAdapter;

    // The title for the fragment.
    CharSequence mTitle;

    private static final int DAY_ENTRY_LOADER = 1;

    public WeatherListFragment() {
    }

    public static WeatherListFragment newInstance(Context context, long locationId) {
        WeatherListFragment fragment = new WeatherListFragment();

        Cursor cursor = context.getContentResolver().query(
                Contract.Location.buildLocationUri(locationId),
                new String[]{
                        Contract.Location._ID,
                        Contract.Location.COL_CITY_NAME,
                        Contract.Location.COL_QUERY_PARAM
                },
                null,
                null,
                null
        );

        Bundle args = new Bundle();
        if (cursor != null && cursor.moveToFirst()) {
            CharSequence cityName = cursor.getString(cursor.getColumnIndex(Contract.Location.COL_CITY_NAME));

            args.putLong(ARG_LOCATION_ID, cursor.getLong(cursor.getColumnIndex(Contract.Location._ID)));
            args.putCharSequence(ARG_CITY_NAME, cityName);
            args.putCharSequence(ARG_QUERY_PARAM, cursor.getString(cursor.getColumnIndex(Contract.Location.COL_QUERY_PARAM)));

            // Store the title of the fragment.
            fragment.mTitle = cityName;
        }
        fragment.setArguments(args);

        return fragment;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity we're on that we have actions.
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if (args != null) {
            mLocationId = args.getLong(ARG_LOCATION_ID);
            mCityName = args.getString(ARG_CITY_NAME);
            mQueryParam = args.getString(ARG_QUERY_PARAM);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DAY_ENTRY_LOADER, null, this);
    }

    private void refreshWeatherData() {
        // Make sure we're showing the user that the data is loading.
        mSwipeRefreshLayout.setRefreshing(true);

        // Start an async task to refresh the weather data for the current location.
        FetchWeatherTask task = new FetchWeatherTask(getActivity(), mLocationId, mQueryParam);
        task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_list, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mDayEntryList = (ListView) rootView.findViewById(R.id.day_entry_list);
        mDayEntryAdapter = new DayEntryAdapter(getActivity(), null, 0);
        mDayEntryList.setAdapter(mDayEntryAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        final String[] columns = new String[]{
                Contract.DayEntry._ID,
                Contract.DayEntry.COL_LOCATION_ID,
                Contract.DayEntry.COL_DATE,
                Contract.DayEntry.COL_TEMP_HIGH,
                Contract.DayEntry.COL_TEMP_LOW,
                Contract.DayEntry.COL_ICON,
                Contract.DayEntry.COL_QPF_ALL_DAY,
                Contract.DayEntry.COL_QPF_DAY,
                Contract.DayEntry.COL_QPF_NIGHT,
                Contract.DayEntry.COL_SNOW_ALL_DAY,
                Contract.DayEntry.COL_SNOW_DAY,
                Contract.DayEntry.COL_SNOW_NIGHT,
                Contract.DayEntry.COL_MAX_WIND_SPEED,
                Contract.DayEntry.COL_MAX_WIND_DEGREES,
                Contract.DayEntry.COL_AVG_WIND_SPEED,
                Contract.DayEntry.COL_AVG_WIND_DEGREES,
                Contract.DayEntry.COL_AVG_HUMIDITY,
                Contract.DayEntry.COL_MAX_HUMIDITY,
                Contract.DayEntry.COL_MIN_HUMIDITY
        };

        return new CursorLoader(
                getActivity(),
                Contract.DayEntry.buildDayEntryByLocationUri(mLocationId, new Date()),
                columns,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mDayEntryAdapter.swapCursor(cursor);

        // Stop the spinner from refreshing.
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.i(LOG_TAG, "onLoaderReset");
    }

    @Override
    public void onRefresh() {
        // Called when the swipe to refresh is released and we should start refreshing.
        refreshWeatherData();
    }

}
