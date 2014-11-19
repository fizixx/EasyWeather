package com.fizix.android.easyweather.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.fizix.android.easyweather.data.Contract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FetchWeatherTask extends AsyncTask<Void, Void, Void> {

    private static final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private Context mContext;
    private long mLocationId;
    private String mLocation;

    public FetchWeatherTask(Context context, long locationId, String location) {
        mContext = context;
        mLocationId = locationId;
        mLocation = location;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.i(LOG_TAG, "doInBackground: " + mLocation);

        HttpURLConnection urlConnection = null;
        BufferedReader reader;
        String jsonString = null;

        try {
            // http://api.wunderground.com/api/f2a4fe2862596f5e/forecast10day/q/CA/San_Francisco.json

            final String BASE_URL = "http://api.wunderground.com/api/";

            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath("f2a4fe2862596f5e")
                    .appendPath("forecast10day")
                    .appendPath("q")
                    .appendPath(mLocation.replace(" ", "_") + ".json")
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
            List<ContentValues> results = WundergroundParser.parseForecast(inputStream);

            if (results != null) {
                for (ContentValues contentValues : results) {
                    // Make sure the contentValues has a location_id set.
                    contentValues.put(Contract.DayEntry.COL_LOCATION_ID, mLocationId);
                    mContext.getContentResolver().insert(Contract.DayEntry.CONTENT_URI, contentValues);
                }
            }

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Unable to parse url.", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not read JSON.", e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

}
