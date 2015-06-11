package com.fizix.android.easyweather.utils.sync;


import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.fizix.android.easyweather.data.Contract;
import com.fizix.android.easyweather.utils.WundergroundParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = SyncAdapter.class.getSimpleName();

    public static final String KEY_LOCATION_ID = "location_id";

    // Content resolver, for performing database operations.
    private final ContentResolver mContentResolver;

    // Obtains handle to content resolver for later use.
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    // Obtains handle to content resolver for later use.
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient, SyncResult syncResult) {
        // If we are passed a -1 or the location id isn't set, then we assume we have to update all
        // locations.
        long locationId = bundle.getLong(KEY_LOCATION_ID, -1);

        Cursor locationCursor = null;
        try {
            final String[] projection = new String[]{
                    Contract.Location._ID,
                    Contract.Location.COL_QUERY_PARAM,
            };

            if (locationId != -1) {
                locationCursor =  contentProviderClient.query(
                        Contract.Location.CONTENT_URI,
                        projection,
                        Contract.Location._ID + " = ?",
                        new String[] {String.valueOf(locationId)},
                        Contract.Location._ID + " ASC"
                );
            } else {
                locationCursor = contentProviderClient.query(
                        Contract.Location.CONTENT_URI,
                        projection,
                        null,
                        null,
                        Contract.Location._ID + " ASC"
                );
            }
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Could not get list of locations from content provider client.", e);
            e.printStackTrace();
        }

        if (locationCursor == null || ! locationCursor.moveToFirst()) {
            Log.e(LOG_TAG, "Could not get list of locations.");
            return;
        }

        do {
            long rowLocationId = locationCursor.getLong(
                    locationCursor.getColumnIndex(Contract.Location._ID));
            String rowQueryParam = locationCursor.getString(
                    locationCursor.getColumnIndex(Contract.Location.COL_QUERY_PARAM));

            updateLocation(contentProviderClient, rowLocationId, rowQueryParam);

        } while (locationCursor.moveToNext());
    }

    private static boolean updateLocation(ContentProviderClient contentProviderClient,
                                          long locationId,
                                          String queryParam) {
        Log.i(LOG_TAG, String.format("Synching location: %d", locationId));

        HttpURLConnection urlConnection = null;

        try {
            final String BASE_URL = "http://api.wunderground.com/api/";

            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath("f2a4fe2862596f5e")
                    .appendPath("forecast10day")
                    .appendPath("q")
                    .appendEncodedPath(queryParam + ".json")
                    .build();

            String uriStr = uri.toString();
            Log.d(LOG_TAG, "Requesting URL: " + uriStr);

            URL url = new URL(uriStr);

            // Create the request.
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a string.
            InputStream inputStream = urlConnection.getInputStream();

            // Parse the json and get a list of the results.
            List<ContentValues> results = WundergroundParser.parseForecast(inputStream, locationId);

            if (results != null) {
                ContentValues[] valuesToInsert = new ContentValues[results.size()];
                results.toArray(valuesToInsert);

                contentProviderClient.bulkInsert(Contract.DayEntry.CONTENT_URI, valuesToInsert);
            }

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Unable to parse url.", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not read JSON.", e);
            e.printStackTrace();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Could not insert data into remote content provider.", e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return true;
    }

}
