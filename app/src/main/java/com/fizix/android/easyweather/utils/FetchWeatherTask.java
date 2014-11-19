package com.fizix.android.easyweather.utils;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.fizix.android.easyweather.data.Contract;
import com.fizix.android.easyweather.models.SearchResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchWeatherTask extends AsyncTask<Void, Void, List<ContentValues>> {

    private static final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private String mLocation;

    public FetchWeatherTask(String location) {
        mLocation = location;
    }

    @Override
    protected List<ContentValues> doInBackground(Void... params) {
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

            return results;

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

    @Override
    protected void onPostExecute(List<ContentValues> results) {
        for (ContentValues values : results) {
            Log.i(LOG_TAG, "date: " + values.get(Contract.DayEntry.COL_DATE));
        }
    }
}
