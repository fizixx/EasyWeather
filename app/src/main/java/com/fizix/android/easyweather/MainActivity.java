package com.fizix.android.easyweather;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fizix.android.easyweather.data.Contract;
import com.fizix.android.easyweather.ui.LocationDrawerFragment;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>, LocationDrawerFragment.OnDrawerSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int LOADER_LOCATION = 0;

    private Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private View mFragmentContainerView;
    private LocationDrawerFragment mDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentContainerView = findViewById(R.id.main_fragment_container);

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
                Contract.Location.COL_QUERY_PARAM,
                Contract.Location.COL_CITY_NAME,
        };

        return new CursorLoader(this, Contract.Location.CONTENT_URI, columns, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    @Override
    public void onDrawerSelected(long locationId) {
        setCurrentLocation(locationId);

        // A new drawer item was selected, so we close the drawer.
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setCurrentLocation(long locationId) {
        // Set the fragment on the fragment area.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, WeatherListFragment.newInstance(locationId, ""))
                .commit();
    }

}
