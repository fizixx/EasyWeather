package com.fizix.android.easyweather;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

public class WeatherListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = WeatherListFragment.class.getSimpleName();

    private static final String ARG_QUERY_PARAM = "location";
    private static final String ARG_LOCATION_ID = "location_id";

    private long mLocationId;
    private String mQueryParam;
    private ListView mDayEntryList;

    // The adapter used by the day entry list.
    DayEntryAdapter mDayEntryAdapter;

    private static final int DAY_ENTRY_LOADER = 1;

    public WeatherListFragment() {
    }

    public static WeatherListFragment newInstance(long locationId, String queryParam) {
        WeatherListFragment fragment = new WeatherListFragment();

        Bundle args = new Bundle();
        args.putLong(ARG_LOCATION_ID, locationId);
        args.putString(ARG_QUERY_PARAM, queryParam);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity we're on that we have actions.
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if (args != null) {
            mLocationId = args.getLong(ARG_LOCATION_ID);
            mQueryParam = args.getString(ARG_QUERY_PARAM);

            Log.i(LOG_TAG, "Query param: " + mQueryParam);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DAY_ENTRY_LOADER, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.weather_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            // Start an async task to refresh the weather data for the current location.
            Log.i(LOG_TAG, "Refreshing: " + mQueryParam);

            FetchWeatherTask task = new FetchWeatherTask(getActivity(), mLocationId, mQueryParam);
            task.execute();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_list, container, false);

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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.i(LOG_TAG, "onLoaderReset");
    }
}
