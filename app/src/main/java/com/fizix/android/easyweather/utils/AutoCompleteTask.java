package com.fizix.android.easyweather.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.util.Vector;

public class AutoCompleteTask extends AsyncTask<Void, Void, Vector<ContentValues>> {

    private static final String LOG_TAG = AutoCompleteTask.class.getSimpleName();

    private String mCity;

    public interface Callbacks {
        void onTaskComplete(ArrayList<String> cities);
    }

    private Callbacks mCallbacks;

    public AutoCompleteTask(Callbacks callbacks, String city) {
        super();

        mCallbacks = callbacks;
        mCity = city;
    }

    @Override
    protected Vector<ContentValues> doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader;
        String jsonString = null;

        try {
            final String AUTOCOMPLETE_BASE_URL = "http://autocomplete.wunderground.com/aq?";
            final String CITY_NAME_PARAM = "query";
            final String FORMAT_PARAM = "format";
            final String HURRICANES_PARAM = "h";

            Uri uri = Uri.parse(AUTOCOMPLETE_BASE_URL).buildUpon()
                    .appendQueryParameter(CITY_NAME_PARAM, mCity)
                    .appendQueryParameter(FORMAT_PARAM, "json")
                    .appendQueryParameter(HURRICANES_PARAM, "0")
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

            // Read the input stream into a buffer.
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            jsonString = buffer.toString();

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

        Vector<ContentValues> citiesVector = null;

        try {
            JSONObject root = new JSONObject(jsonString);

            JSONArray cities = root.getJSONArray("RESULTS");

            citiesVector = new Vector<ContentValues>(cities.length());

            for (int i = 0; i < cities.length(); i++) {
                JSONObject cityObj = cities.getJSONObject(i);

                ContentValues cityValues = new ContentValues();
                cityValues.put("name", cityObj.getString("name"));
                cityValues.put("lat", cityObj.getDouble("lat"));
                cityValues.put("lon", cityObj.getDouble("lon"));

                citiesVector.add(cityValues);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Could not parse JSON.", e);
            e.printStackTrace();
        }

        return citiesVector;
    }

    @Override
    protected void onPostExecute(Vector<ContentValues> cities) {
        super.onPostExecute(cities);

        ArrayList<String> results = new ArrayList<String>();

        if (cities != null) {
            for (ContentValues values : cities) {
                results.add(values.getAsString("name"));
            }
        }

        mCallbacks.onTaskComplete(results);
    }

}