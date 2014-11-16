package com.fizix.android.easyweather.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.fizix.android.easyweather";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Paths
    public static final String PATH_LOCATION = "location";

    public static final class Location implements BaseColumns {

        // Content Provider Constants
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        // Table Name
        public static final String TABLE_NAME = "location";

        // Columns
        public static final String COL_LOCATION = "location";
        public static final String COL_CITY_NAME = "city_name";
        public static final String COL_COORD_LAT = "lat";
        public static final String COL_COORD_LONG = "long";

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
