package com.fizix.android.easyweather;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fizix.android.easyweather.adapters.LocationTabsAdapter;
import com.fizix.android.easyweather.data.Contract;
import com.fizix.android.easyweather.data.LocationInfo;
import com.fizix.android.easyweather.views.SlidingTabLayout;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_LOCATION = 0;
    private SlidingTabLayout mSlidingTabs;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private Button mAddLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mSlidingTabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mAddLocationButton = (Button) findViewById(R.id.add_location_button);
        mAddLocationButton.setVisibility(View.GONE);
        mAddLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddCityActivity.class));
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //mToolbar.setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        // Set up the content loader.
        getSupportLoaderManager().initLoader(LOADER_LOCATION, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_city:
                startActivity(new Intent(this, AddCityActivity.class));
                return true;

            case R.id.action_clear_all:
                getContentResolver().delete(Contract.Location.CONTENT_URI, null, null);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "onCreateLoader(...)");

        String[] columns = {
                Contract.Location._ID,
                Contract.Location.COL_CITY_NAME,
                Contract.Location.COL_COORD_LAT,
                Contract.Location.COL_COORD_LONG,
        };

        return new CursorLoader(this, Contract.Location.CONTENT_URI, columns, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.i(LOG_TAG, "onLoadFinished(...)");

        LocationTabsAdapter adapter = null;
        if (cursor != null && cursor.moveToFirst()) {
            adapter = new LocationTabsAdapter(this);
            do {
                LocationInfo locationInfo = new LocationInfo(cursor);
                Log.i(LOG_TAG, "LocationInfo: " + locationInfo.getCityName());
                adapter.addLocationInfo(locationInfo);
            } while (cursor.moveToNext());
        }

        mViewPager.setAdapter(adapter);
        mViewPager.setVisibility((adapter == null) ? View.GONE : View.VISIBLE);

        // We have to call this for the sliding tabs to update.
        mSlidingTabs.setViewPager(mViewPager);

        mAddLocationButton.setVisibility((adapter == null) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

}
