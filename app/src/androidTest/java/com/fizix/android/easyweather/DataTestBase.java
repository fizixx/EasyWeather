package com.fizix.android.easyweather;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.fizix.android.easyweather.data.Contract;

import java.util.Map;
import java.util.Set;

public class DataTestBase extends AndroidTestCase {

    static protected void validateCursor(Cursor cursor, ContentValues contentValues) {
        Set<Map.Entry<String, Object>> valueSet = contentValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = cursor.getColumnIndex(columnName);
            assertTrue(index != -1);
            String value = entry.getValue().toString();
            assertEquals(value, cursor.getString(index));
        }
    }

    static protected ContentValues getLocationContentValues() {
        ContentValues values = new ContentValues();

        values.put(Contract.Location.COL_LOCATION, "Cape Town, South Africa");
        values.put(Contract.Location.COL_CITY_NAME, "Cape Town");
        values.put(Contract.Location.COL_COORD_LAT, 100);
        values.put(Contract.Location.COL_COORD_LONG, 100);

        return values;
    }

}
