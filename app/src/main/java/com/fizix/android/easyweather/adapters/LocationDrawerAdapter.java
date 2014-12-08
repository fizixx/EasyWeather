package com.fizix.android.easyweather.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fizix.android.easyweather.R;
import com.fizix.android.easyweather.data.Contract;

public class LocationDrawerAdapter extends CursorAdapter {

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

        viewHolder.textLocation.setText(cursor.getString(cursor.getColumnIndex(Contract.Location.COL_CITY_NAME)));
    }

    private static class ViewHolder {

        ImageView imageIcon;
        TextView textLocation;
        TextView textDegrees;

        ViewHolder(View parent) {
            imageIcon = (ImageView) parent.findViewById(R.id.image_icon);
            textLocation = (TextView) parent.findViewById(R.id.text_location);
            textDegrees = (TextView) parent.findViewById(R.id.text_degrees);
        }

    }

}
