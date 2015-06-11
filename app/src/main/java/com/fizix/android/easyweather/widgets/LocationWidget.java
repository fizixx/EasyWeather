package com.fizix.android.easyweather.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import com.fizix.android.easyweather.R;
import com.fizix.android.easyweather.activities.LocationWidgetConfigureActivity;
import com.fizix.android.easyweather.data.Contract;


public class LocationWidget extends AppWidgetProvider {

    private static final String LOG_TAG = LocationWidget.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            LocationWidgetConfigureActivity.deleteLocationIdPref(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // If the widget id is 0, then it means its the widget that is being placed and not an active widget.
        if (appWidgetId == 0) {
            return;
        }

        long locationId = LocationWidgetConfigureActivity.loadLocationIdPref(context, appWidgetId);

        Log.i(LOG_TAG, String.format("%d - %d", appWidgetId, locationId));

        // Get the data for the widget.
        Cursor cursor = context.getContentResolver().query(
                Contract.Location.buildLocationWidgetUri(locationId),
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            String location = cursor.getString(cursor.getColumnIndex(Contract.Location.COL_CITY_NAME));
            long temp = cursor.getLong(cursor.getColumnIndex(Contract.DayEntry.COL_TEMP_HIGH));
            String icon = cursor.getString(cursor.getColumnIndex(Contract.DayEntry.COL_ICON));

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.location_widget);
            views.setTextViewText(R.id.appwidget_location, location);
            views.setTextViewText(R.id.appwidget_temp, Contract.DayEntry.longToDegrees(temp));
            views.setImageViewResource(R.id.appwidget_icon, Contract.DayEntry.iconResource(icon));

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}


