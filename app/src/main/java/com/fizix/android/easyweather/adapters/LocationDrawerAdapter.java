package com.fizix.android.easyweather.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fizix.android.easyweather.R;
import com.fizix.android.easyweather.data.Contract;

public class LocationDrawerAdapter extends CursorAdapter {

    private static final String LOG_TAG = LocationDrawerAdapter.class.getSimpleName();

    public LocationDrawerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location_drawer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String iconName = cursor.getString(cursor.getColumnIndex(Contract.DayEntry.COL_ICON));
        int iconResource = R.drawable.circle_clear;
        if (iconName.equals("clear")) {
            iconResource = R.drawable.art_clear;
        } else if (iconName.equals("partlycloudy")) {
            iconResource = R.drawable.art_cloudy;
        } else if (iconName.equals("mostlycloudy")) {
            iconResource = R.drawable.art_cloudy;
        } else if (iconName.equals("cloudy")) {
            iconResource = R.drawable.art_overcast;
        } else if (iconName.equals("chancerain")) {
            iconResource = R.drawable.art_rain;
        }

        viewHolder.imageIcon.setImageDrawable(context.getResources().getDrawable(iconResource));

        viewHolder.textLocation.setText(
                cursor.getString(cursor.getColumnIndex(Contract.Location.COL_CITY_NAME))
        );

        viewHolder.textTemp.setText(
                Contract.DayEntry.longToDegrees(
                        cursor.getLong(cursor.getColumnIndex(Contract.DayEntry.COL_TEMP_HIGH))
                )
        );
    }

    private static class ViewHolder {

        ImageView imageIcon;
        TextView textLocation;
        TextView textTemp;

        ViewHolder(View parent) {
            imageIcon = (ImageView) parent.findViewById(R.id.image_icon);
            textLocation = (TextView) parent.findViewById(R.id.text_location);
            textTemp = (TextView) parent.findViewById(R.id.text_degrees);
        }

    }

}
