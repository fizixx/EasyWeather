package com.fizix.android.easyweather.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.fizix.android.easyweather";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Paths
    public static final String PATH_LOCATION = "location";
    public static final String PATH_DAY_ENTRY = "day_entry";

    public static final class Location implements BaseColumns {

        // Content Provider Constants
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();
        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        // Table Name
        public static final String TABLE_NAME = "location";

        // Columns

        // Returned from the autocomplete API for the city you selected.
        public static final String COL_QUERY_PARAM = "query_param";

        // The name of the city as it is returned from the autocomplete API.
        public static final String COL_LOCATION = "location";

        // The shortened name we generate from the full location.
        public static final String COL_CITY_NAME = "city_name";

        // The longitude and latitude for the location.
        public static final String COL_COORD_LAT = "lat";
        public static final String COL_COORD_LONG = "long";

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class DayEntry implements BaseColumns {

        // Content Provider Constants
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DAY_ENTRY).build();
        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_DAY_ENTRY;
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_DAY_ENTRY;

        // Table Name
        public static final String TABLE_NAME = "day_entry";

        // Columns

        // The foreign key to the location table.
        public static final String COL_LOCATION_ID = "location_id";

        // Date as a strin, e.g. 20141024.
        public static final String COL_DATE = "date";

        // Temperature in celcius.
        public static final String COL_TEMP_HIGH = "temp_high";
        public static final String COL_TEMP_LOW = "temp_low";
        // String holding the description of the weather, e.g. partlycloudy, clear, etc.
        public static final String COL_ICON = "icon";
        // QPF in mm.
        public static final String COL_QPF_ALL_DAY = "qpf_all_day";
        public static final String COL_QPF_DAY = "qpf_day";
        public static final String COL_QPF_NIGHT = "qpf_night";
        // Snow in cm.
        public static final String COL_SNOW_ALL_DAY = "snow_all_day";
        public static final String COL_SNOW_DAY = "snow_day";
        public static final String COL_SNOW_NIGHT = "snow_night";
        // Max/avg wind speed in km/h and direction in degrees.
        public static final String COL_MAX_WIND_SPEED = "max_wind_speed";
        public static final String COL_MAX_WIND_DEGREES = "max_wind_degrees";
        public static final String COL_AVG_WIND_SPEED = "avg_wind_speed";
        public static final String COL_AVG_WIND_DEGREES = "avg_wind_degrees";
        // Humidity in %.
        public static final String COL_AVG_HUMIDITY = "avg_humidity";
        public static final String COL_MAX_HUMIDITY = "max_humidity";
        public static final String COL_MIN_HUMIDITY = "min_humidity";

        public static Uri buildDayEntryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // Return the string representation of the given date components.
        public static String createDateString(int year, int month, int day) {
            return String.format("%04d%02d%02d", year, month, day);
        }

        // Create a date string from a Date object.
        public static String createDateString(Date date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            return dateFormat.format(date);
        }

        public static Uri buildDayEntryByLocationUri(long locationId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(locationId)).build();
        }

        public static Uri buildDayEntryByLocationUri(long locationId, Date startDate) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(locationId)).appendPath(createDateString(startDate)).build();
        }

    }

}
