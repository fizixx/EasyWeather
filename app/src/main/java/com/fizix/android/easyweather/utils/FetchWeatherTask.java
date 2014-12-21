package com.fizix.android.easyweather.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.fizix.android.easyweather.data.Contract;

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
    private String mQueryParam;

    public FetchWeatherTask(Context context, long locationId, String queryParam) {
        mContext = context;
        mLocationId = locationId;
        mQueryParam = queryParam;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;

        try {
            final String BASE_URL = "http://api.wunderground.com/api/";

            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath("f2a4fe2862596f5e")
                    .appendPath("forecast10day")
                    .appendPath("q")
                    .appendEncodedPath(mQueryParam + ".json")
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
            List<ContentValues> results = WundergroundParser.parseForecast(inputStream, mLocationId);

            if (results != null) {
                ContentValues[] valuesToInsert = new ContentValues[results.size()];
                results.toArray(valuesToInsert);

                mContext.getContentResolver().bulkInsert(Contract.DayEntry.CONTENT_URI, valuesToInsert);
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
