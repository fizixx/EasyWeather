package com.fizix.android.easyweather.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class Provider extends ContentProvider {

    private static final String LOG_TAG = Provider.class.getSimpleName();

    private static final int LOCATION = 200;
    private static final int LOCATION_ID = 201;

    private static final int DAY_ENTRY_DIR = 300;
    private static final int DAY_ENTRY_DIR_BY_LOCATION = 301;
    private static final int DAY_ENTRY_DIR_BY_LOCATION_AND_START_DATE = 302;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mDbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;

        matcher.addURI(authority, Contract.PATH_LOCATION, LOCATION);
        matcher.addURI(authority, Contract.PATH_LOCATION + "/#", LOCATION_ID);

        matcher.addURI(authority, Contract.PATH_DAY_ENTRY, DAY_ENTRY_DIR);
        matcher.addURI(authority, Contract.PATH_DAY_ENTRY + "/*", DAY_ENTRY_DIR_BY_LOCATION);
        matcher.addURI(authority, Contract.PATH_DAY_ENTRY + "/*/*", DAY_ENTRY_DIR_BY_LOCATION_AND_START_DATE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;

        switch (sUriMatcher.match(uri)) {

            case LOCATION_ID: {
                retCursor = mDbHelper.getReadableDatabase().query(
                        Contract.Location.TABLE_NAME,
                        projection,
                        Contract.Location._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case LOCATION: {
                retCursor = mDbHelper.getReadableDatabase().query(
                        Contract.Location.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            }

            case DAY_ENTRY_DIR_BY_LOCATION: {
                // Get the location_id from the URI.
                String locationId = uri.getPathSegments().get(1);

                retCursor = mDbHelper.getReadableDatabase().query(
                        Contract.DayEntry.TABLE_NAME,
                        projection,
                        Contract.DayEntry.COL_LOCATION_ID + " = ?",
                        new String[] {locationId},
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case DAY_ENTRY_DIR_BY_LOCATION_AND_START_DATE: {
                // Get the locationId and the startDate from the URI.
                List<String> segments = uri.getPathSegments();
                String locationId = segments.get(1);
                String startDate = segments.get(2);

                Log.i(LOG_TAG, "===========================================");

                retCursor = mDbHelper.getReadableDatabase().query(
                        Contract.DayEntry.TABLE_NAME,
                        projection,
                        Contract.DayEntry.COL_LOCATION_ID + " = ? AND " + Contract.DayEntry.COL_DATE + " >= ?",
                        new String[] {locationId, startDate},
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {

            case LOCATION:
                return Contract.Location.CONTENT_TYPE_DIR;

            case LOCATION_ID:
                return Contract.Location.CONTENT_ITEM_TYPE;

            case DAY_ENTRY_DIR:
                return Contract.DayEntry.CONTENT_TYPE_DIR;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri retUri;

        switch (match) {

            case LOCATION: {
                long id = db.insert(Contract.Location.TABLE_NAME, null, values);
                if (id > 0)
                    retUri = Contract.Location.buildLocationUri(id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }

            case DAY_ENTRY_DIR: {
                long id = db.insert(Contract.DayEntry.TABLE_NAME, null, values);
                if (id > 0)
                    retUri = Contract.DayEntry.buildDayEntryUri(id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify observers that the data changed.
        getContext().getContentResolver().notifyChange(uri, null);

        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;

        switch (match) {

            case LOCATION: {
                rowsDeleted = db.delete(Contract.Location.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case DAY_ENTRY_DIR: {
                rowsDeleted = db.delete(Contract.DayEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {

            case LOCATION: {
                rowsUpdated = db.update(Contract.Location.TABLE_NAME, values, selection, selectionArgs);
                break;
            }

            case DAY_ENTRY_DIR: {
                rowsUpdated = db.update(Contract.DayEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if (selection == null || rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case DAY_ENTRY_DIR: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insert(Contract.DayEntry.TABLE_NAME, null, value);
                        if (id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            default:
                return super.bulkInsert(uri, values);
        }
    }

}
