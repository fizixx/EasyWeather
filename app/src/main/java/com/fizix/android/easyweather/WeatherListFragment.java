package com.fizix.android.easyweather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherListFragment extends Fragment {

    public static final String ARG_LOCATION_ID = "location_id";

    private long mLocationId;

    public static WeatherListFragment newInstance(long locationId) {
        WeatherListFragment fragment = new WeatherListFragment();

        Bundle args = new Bundle();
        args.putLong(ARG_LOCATION_ID, locationId);

        fragment.setArguments(args);

        return fragment;
    }

    public WeatherListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocationId = getArguments().getLong(ARG_LOCATION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_list, container, false);

        TextView text = (TextView) rootView.findViewById(R.id.text);
        text.setText(String.valueOf(mLocationId));

        return rootView;
    }

}
