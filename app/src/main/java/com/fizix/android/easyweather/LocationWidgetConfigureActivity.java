package com.fizix.android.easyweather;



import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fizix.android.easyweather.adapters.LocationDrawerAdapter;
import com.fizix.android.easyweather.data.Contract;


public class LocationWidgetConfigureActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String PREFS_NAME = "com.fizix.android.easyweather.LocationWidget";
    private static final String PREF_PREFIX_KEY = "widget_location_id_";

    private static final int LOADER_LOCATION_LIST = 0;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    LocationDrawerAdapter mLocationAdapter;

    public LocationWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_location_widget_configure);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        // If we are modifying an existing widget, we have to set the previously selected id.
        /*
        mAppWidgetText.setText(loadTitlePref(LocationWidgetConfigureActivity.this, mAppWidgetId));
        */

        // Set up the location list.
        ListView locationList = (ListView) findViewById(R.id.location_list);
        locationList.setOnItemClickListener(mOnItemClickListener);
        mLocationAdapter = new LocationDrawerAdapter(this, null, 0);
        locationList.setAdapter(mLocationAdapter);

        // Set up the loader that will handle out list of locations.
        getSupportLoaderManager().initLoader(LOADER_LOCATION_LIST, null, this);
    }

    ListView.OnItemClickListener mOnItemClickListener = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Context context = LocationWidgetConfigureActivity.this;

            // When the item is clicked, store the string locally
            saveLocationIdPref(context, mAppWidgetId, id);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            LocationWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    // Write the prefix to the SharedPreferences object for this widget
    static void saveLocationIdPref(Context context, int appWidgetId, long id) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putLong(PREF_PREFIX_KEY + appWidgetId, id);
        prefs.commit();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static long loadLocationIdPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        long locationId = prefs.getLong(PREF_PREFIX_KEY + appWidgetId, 0);
        return locationId;
    }

    static void deleteLocationIdPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                Contract.Location.buildLocationWithTempUri(),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mLocationAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
