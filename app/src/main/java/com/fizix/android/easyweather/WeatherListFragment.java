package com.fizix.android.easyweather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherListFragment extends Fragment {

    public static final String ARG_LOCATION_ID = "location_id";

    private long mLocationId;
    private RecyclerView mRecyclerView;

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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create a adapter for the list.
        WeatherEntryAdapter adapter = new WeatherEntryAdapter(new String[]{"Item 1", "Item 2", "Item 3"});
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

    public static class WeatherEntryAdapter extends RecyclerView.Adapter<WeatherEntryAdapter.ViewHolder> {

        private String[] mDataSet;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;

            public ViewHolder(View parent) {
                super(parent);
                title = (TextView) parent.findViewById(R.id.title);
            }

        }

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
            viewHolder.title.setText(mDataSet[position]);
        }

        @Override
        public int getItemCount() {
            return mDataSet.length;
        }

    }

}
