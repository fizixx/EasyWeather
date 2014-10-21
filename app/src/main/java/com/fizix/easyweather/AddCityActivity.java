package com.fizix.easyweather;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.fizix.easyweather.utils.AutocompleteTask;

public class AddCityActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    private static final String LOG_TAG = AddCityActivity.class.getSimpleName();

    private SearchView mSearchView;
    private ListView mCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        mCityList = (ListView) findViewById(R.id.city_list);

        onSearchRequested();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.add_city, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d(LOG_TAG, "Submitting: " + s);

        AutocompleteTask task = new AutocompleteTask(s, this, mCityList);
        task.execute();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.d(LOG_TAG, "Changed to: " + s);
        return false;
    }

}
