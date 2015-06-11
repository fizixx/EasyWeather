package com.fizix.android.easyweather.activities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fizix.android.easyweather.R;
import com.fizix.android.easyweather.adapters.SearchResultAdapter;
import com.fizix.android.easyweather.data.Contract;
import com.fizix.android.easyweather.models.SearchResult;
import com.fizix.android.easyweather.utils.AutoCompleteTask;
import com.fizix.android.easyweather.utils.sync.SyncUtils;

import java.util.List;

public class AddLocationActivity extends ActionBarActivity implements AutoCompleteTask.Callbacks {

    private static final String LOG_TAG = AddLocationActivity.class.getSimpleName();

    private ListView mCityList;
    private SearchResultAdapter mCurrentAdapter;

    private EditText mSearchText;

    private ProgressBar mProgressCityList;
    private TextView mStartSearch;
    private TextView mNoCitiesFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        // Set up the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        // Map the search text.
        mSearchText = (EditText) findViewById(R.id.search_text);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startAutocomplete(String.valueOf(mSearchText.getText()));
                    return true;
                }
                return false;
            }
        });

        mCityList = (ListView) findViewById(R.id.city_list);
        mCityList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentAdapter != null) {
                    addLocation(mCurrentAdapter.getItem(position));
                }
            }
        });

        mProgressCityList = (ProgressBar) findViewById(R.id.progress_city_list);
        mProgressCityList.setVisibility(View.GONE);

        mStartSearch = (TextView) findViewById(R.id.start_search_text);
        mStartSearch.setVisibility(View.VISIBLE);

        mNoCitiesFound = (TextView) findViewById(R.id.no_cities_found);
        mNoCitiesFound.setVisibility(View.GONE);

        onSearchRequested();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_city, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                startAutocomplete(String.valueOf(mSearchText.getText()));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addLocation(SearchResult result) {
        // Get the city name from the location.
        String[] parts = result.getLocation().split(",");
        String cityName = (parts.length > 1) ? parts[0] : result.getLocation();

        ContentValues values = new ContentValues();
        values.put(Contract.Location.COL_QUERY_PARAM, result.getQueryParam());
        values.put(Contract.Location.COL_LOCATION, result.getLocation());
        values.put(Contract.Location.COL_CITY_NAME, cityName);
        values.put(Contract.Location.COL_COORD_LAT, result.getCoordLat());
        values.put(Contract.Location.COL_COORD_LONG, result.getCoordLong());

        long newLocationId = -1;
        try {
            Uri resultUri = getContentResolver().insert(Contract.Location.CONTENT_URI, values);
            // Get the locationId from the uri.
            newLocationId = ContentUris.parseId(resultUri);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not insert location.", e);
            e.printStackTrace();
        }

        // Start off the task to update the weather for the newly added location.
        if (newLocationId >= 0) {
            SyncUtils.syncLocationNow(this, newLocationId);
        }

        finish();
    }

    public boolean startAutocomplete(String str) {
        Log.d(LOG_TAG, "Submitting: " + str);

        // Disable the search input.
        mSearchText.setEnabled(false);

        // Hide the city list.
        mCityList.setVisibility(View.GONE);
        // Show the progress bar.
        mProgressCityList.setVisibility(View.VISIBLE);
        // Hide the start search text view (this will never be shown again).
        mStartSearch.setVisibility(View.GONE);
        // Hide the no cities found text view.
        mNoCitiesFound.setVisibility(View.GONE);

        AutoCompleteTask task = new AutoCompleteTask(this, str);
        task.execute();

        return false;
    }

    @Override
    public void onTaskComplete(List<SearchResult> results) {
        // Enable the search text again.
        mSearchText.setEnabled(true);

        // Show the no cities found text view if nothing was found and hide the city list.
        if (results.isEmpty()) {
            mNoCitiesFound.setVisibility(View.VISIBLE);
        }

        // Create a new adapter for the list.
        mCurrentAdapter = new SearchResultAdapter(this, results);
        mCityList.setAdapter(mCurrentAdapter);

        // Show the city list and hide the progress bar.
        mCityList.setVisibility(View.VISIBLE);
        mProgressCityList.setVisibility(View.GONE);
    }

}