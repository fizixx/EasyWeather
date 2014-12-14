package com.fizix.android.easyweather.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fizix.android.easyweather.R;
import com.fizix.android.easyweather.adapters.LocationDrawerAdapter;
import com.fizix.android.easyweather.data.Contract;

public class LocationDrawerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = LocationDrawerFragment.class.getSimpleName();

    private static final int LOADER_LOCATION = 1;

    private ListView mLocationList;
    private LocationDrawerAdapter mAdapter;

    private OnDrawerSelectedListener mListener;

    public LocationDrawerFragment() {
        super();
        mListener = null;
    }

    public void setOnDrawerSelectedListener(OnDrawerSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_LOCATION, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_drawer, container, false);

        mLocationList = (ListView) rootView.findViewById(R.id.location_list);
        mAdapter = new LocationDrawerAdapter(getActivity(), null, 0);
        mLocationList.setAdapter(mAdapter);

        mLocationList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onDrawerSelected(id);
                }
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                Contract.Location.buildLocationWithTempUri(),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public static interface OnDrawerSelectedListener {
        public void onDrawerSelected(long locationId);
    }

}
