package com.fizix.android.easyweather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fizix.android.easyweather.utils.FetchWeatherTask;

public class WeatherListFragment extends Fragment {

    private static final String LOG_TAG = WeatherListFragment.class.getSimpleName();

    private static final String ARG_LOCATION = "location";
    private static final String ARG_LOCATION_ID = "location_id";

    private long mLocationId;
    private String mLocation;
    private RecyclerView mRecyclerView;

    public WeatherListFragment() {
    }

    public static WeatherListFragment newInstance(long locationId, String location) {
        WeatherListFragment fragment = new WeatherListFragment();

        Bundle args = new Bundle();
        args.putString(ARG_LOCATION, location);
        args.putLong(ARG_LOCATION_ID, locationId);

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
            mLocation = args.getString(ARG_LOCATION);
        }
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
            Log.i(LOG_TAG, "Refreshing: " + mLocation);

            FetchWeatherTask task = new FetchWeatherTask(getActivity(), mLocationId, mLocation);
            task.execute();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create a adapter for the list.
        WeatherEntryAdapter adapter = new WeatherEntryAdapter(
                new String[]{"Item 1", "Item 2", "Item 3", "Item 4", "Item 5",
                        "Item 6", "Item 7", "Item 8", "Item 9", "Item 10"});
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

    public static class WeatherEntryAdapter extends RecyclerView.Adapter<WeatherEntryAdapter.ViewHolder> {

        private String[] mDataSet;

        public WeatherEntryAdapter(String[] dataSet) {
            mDataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_list_entry, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
        }

        @Override
        public int getItemCount() {
            return mDataSet.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View parent) {
                super(parent);
            }

        }

    }

}
