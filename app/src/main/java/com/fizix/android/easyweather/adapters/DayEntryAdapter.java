package com.fizix.android.easyweather.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.fizix.android.easyweather.R;
import com.fizix.android.easyweather.data.Contract;

public class DayEntryAdapter extends CursorAdapter {

    public DayEntryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_list_entry, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.title.setText(cursor.getString(cursor.getColumnIndex(Contract.DayEntry.COL_DATE)));
        viewHolder.tempMax.setText(cursor.getString(cursor.getColumnIndex(Contract.DayEntry.COL_TEMP_HIGH)));
        viewHolder.tempMin.setText(cursor.getString(cursor.getColumnIndex(Contract.DayEntry.COL_TEMP_LOW)));
    }

    private static class ViewHolder {

        public TextView title;
        public TextView tempMax;
        public TextView tempMin;

        public ViewHolder(View rootView) {
            title = (TextView) rootView.findViewById(R.id.title);
            tempMax = (TextView) rootView.findViewById(R.id.temp_max);
            tempMin = (TextView) rootView.findViewById(R.id.temp_min);
        }

    }

}
