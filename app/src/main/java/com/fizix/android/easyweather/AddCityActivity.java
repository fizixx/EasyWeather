package com.fizix.android.easyweather;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fizix.android.easyweather.adapters.SearchResultAdapter;
import com.fizix.android.easyweather.models.SearchResult;
import com.fizix.android.easyweather.utils.AutoCompleteTask;

import java.util.List;

public class AddCityActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, AutoCompleteTask.Callbacks {

    private static final String LOG_TAG = AddCityActivity.class.getSimpleName();

    private SearchView mSearchView;

    private ListView mCityList;

    private ProgressBar mProgressCityList;
    private TextView mStartSearch;
    private TextView mNoCitiesFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        mCityList = (ListView) findViewById(R.id.city_list);

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
        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String str) {
        Log.d(LOG_TAG, "Submitting: " + str);

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
    public boolean onQueryTextChange(String s) {
        Log.d(LOG_TAG, "Changed to: " + s);
        return false;
    }

    @Override
    public void onTaskComplete(List<SearchResult> results) {
        // Show the no cities found text view if nothing was found and hide the city list.
        if (results.isEmpty()) {
            mNoCitiesFound.setVisibility(View.VISIBLE);
        }

        // Create a new adapter for the list.
        SearchResultAdapter adapter = new SearchResultAdapter(this, results);

        mCityList.setAdapter(adapter);

        // Show the city list and hide the progress bar.
        mCityList.setVisibility(View.VISIBLE);
        mProgressCityList.setVisibility(View.GONE);
    }
}