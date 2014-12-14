package com.fizix.android.easyweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fizix.android.easyweather.data.Contract;
import com.fizix.android.easyweather.ui.LocationDrawerFragment;


public class MainActivity extends ActionBarActivity implements LocationDrawerFragment.OnDrawerSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private LocationDrawerFragment mDrawerFragment;

    CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // Set up the drawer fragment.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Listen for drawer item changes.
        mDrawerFragment = (LocationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.main_drawer_fragment);
        mDrawerFragment.setOnDrawerSelectedListener(this);

        mDrawerToggle.syncState();

        // Get the current title from the activity.
        mTitle = getTitle();

        // Read the initial values from the preferences.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long selectedLocationId = prefs.getLong("pref_current_location_id", -1);
        Log.i(LOG_TAG, "selectedLocationId: " + selectedLocationId);
        if (selectedLocationId != -1) {
            setCurrentLocation(selectedLocationId);
        }
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
    public void onDrawerSelected(long locationId) {
        // Set the new content area of the activity.
        setCurrentLocation(locationId);

        // A new drawer item was selected, so we close the drawer.
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setCurrentLocation(long locationId) {
        WeatherListFragment fragment = WeatherListFragment.newInstance(this, locationId);

        // Set the fragment on the fragment area.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();

        // Update the title on the toolbar to the name of the city.
        getSupportActionBar().setTitle(fragment.getTitle());

        // Save in the preferences that we have a new "selected" location.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit()
                .putLong("pref_current_location_id", locationId)
                .apply();
    }

}
