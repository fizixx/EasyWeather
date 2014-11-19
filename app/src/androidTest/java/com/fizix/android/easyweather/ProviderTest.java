package com.fizix.android.easyweather;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fizix.android.easyweather.data.Contract;

public class ProviderTest extends TestBase {

    private static final String LOG_TAG = ProviderTest.class.getSimpleName();

    public void testDeleteAllRecords() {
        Log.i(LOG_TAG, Contract.Location.CONTENT_URI.toString());

        // Delete all locations.
        mContext.getContentResolver().delete(Contract.Location.CONTENT_URI, null, null);

        // Check if we can find any records in the table.
        Cursor cursor = mContext.getContentResolver().query(Contract.Location.CONTENT_URI, null, null, null, null);
        assertEquals(cursor.getCount(), 0);
        cursor.close();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testDeleteAllRecords();
    }

    public void testGetType() throws Throwable {
        // content://com.fizix.android.easyweather/location/
        String type = mContext.getContentResolver().getType(Contract.Location.CONTENT_URI);
        assertEquals(Contract.Location.CONTENT_TYPE_DIR, type);

        type = mContext.getContentResolver().getType(Contract.Location.buildLocationUri((101)));
        assertEquals(Contract.Location.CONTENT_ITEM_TYPE, type);
    }

    public void testInsertReader() throws Throwable {
        ContentValues locationValues = getLocationContentValues();

        Uri locationInsertUri = mContext.getContentResolver().insert(Contract.Location.CONTENT_URI, locationValues);
        long locationRowId = ContentUris.parseId(locationInsertUri);

        // Make sure we got a row back.
        assertTrue(locationRowId != -1);

        Cursor locationCursor = mContext.getContentResolver().query(Contract.Location.buildLocationUri(locationRowId), null, null, null, null);
        if (locationCursor.moveToFirst()) {
            validateCursor(locationCursor, locationValues);
        } else {
            fail("No location value returned.");
        }
    }

}
