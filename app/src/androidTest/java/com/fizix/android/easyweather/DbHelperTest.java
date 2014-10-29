package com.fizix.android.easyweather;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.fizix.android.easyweather.data.Contract;
import com.fizix.android.easyweather.data.DbHelper;

import java.util.Map;
import java.util.Set;

public class DbHelperTest extends AndroidTestCase {

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(DbHelper.DATABASE_NAME);
        SQLiteDatabase dbHelper = new DbHelper(mContext).getWritableDatabase();
        assertTrue(dbHelper.isOpen());
        dbHelper.close();
    }

    public void testInsertRead() throws Throwable {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues locationValues = new ContentValues();
        locationValues.put(Contract.Location.COL_CITY_NAME, "Cape Town, South Africa");
        locationValues.put(Contract.Location.COL_COORD_LAT, 123.123);
        locationValues.put(Contract.Location.COL_COORD_LONG, 456.456);

        long locationRowId = db.insert(Contract.Location.TABLE_NAME, null, locationValues);

        // Make sure we got a row back.
        assertTrue(locationRowId != -1);

        // Now try to read the value back from the db.
        Cursor locationCursor = db.query(Contract.Location.TABLE_NAME, null, null, null, null, null, null);
        if (locationCursor.moveToFirst()) {
            validateCursor(locationCursor, locationValues);
        } else {
            fail("No location values returned");
        }
    }

    static private void validateCursor(Cursor cursor, ContentValues contentValues) {
        Set<Map.Entry<String, Object>> valueSet = contentValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = cursor.getColumnIndex(columnName);
            assertTrue(index != -1);
            String value = entry.getValue().toString();
            assertEquals(value, cursor.getString(index));
        }
    }

}
