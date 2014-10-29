package com.fizix.android.easyweather;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.fizix.android.easyweather.data.DbHelper;

public class DbHelperTest extends AndroidTestCase {

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(DbHelper.DATABASE_NAME);
        SQLiteDatabase dbHelper = new DbHelper(mContext).getWritableDatabase();
        assertEquals(true, dbHelper.isOpen());
        dbHelper.close();
    }

}
