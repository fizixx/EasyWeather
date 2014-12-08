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
import android.widget.ListView;

import com.fizix.android.easyweather.R;
import com.fizix.android.easyweather.adapters.LocationDrawerAdapter;
import com.fizix.android.easyweather.data.Contract;

public class LocationDrawerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = LocationDrawerFragment.class.getSimpleName();

    private static final int LOADER_LOCATION = 1;

    private ListView mLocationList;
    private LocationDrawerAdapter mAdapter;

    public LocationDrawerFragment() {
        super();
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

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String[] columns = new String[]{
                Contract.Location._ID,
                Contract.Location.COL_CITY_NAME,
        };

        return new CursorLoader(
                getActivity(),
                Contract.Location.CONTENT_URI,
                columns,
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
        Log.i(LOG_TAG, "onLoaderReset");
    }
}
