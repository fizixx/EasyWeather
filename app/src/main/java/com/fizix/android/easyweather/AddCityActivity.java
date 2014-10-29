package com.fizix.android.easyweather;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.fizix.android.easyweather.utils.AutoCompleteTask;

import java.util.ArrayList;

public class AddCityActivity extends Activity implements SearchView.OnQueryTextListener, AutoCompleteTask.Callbacks {

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
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_add_city, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);

        return true;
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
    public void onTaskComplete(ArrayList<String> cities) {
        // Show the no cities found text view if nothing was found and hide the city list.
        if (cities.isEmpty()) {
            mNoCitiesFound.setVisibility(View.VISIBLE);
        }

        // Create a new adapter for the list.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                cities);

        mCityList.setAdapter(adapter);

        // Show the city list and hide the progress bar.
        mCityList.setVisibility(View.VISIBLE);
        mProgressCityList.setVisibility(View.GONE);
    }
}