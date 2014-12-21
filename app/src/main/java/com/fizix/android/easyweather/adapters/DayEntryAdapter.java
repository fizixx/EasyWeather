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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayEntryAdapter extends CursorAdapter {

    private static final String LOG_TAG = DayEntryAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private Context mContext;

    public DayEntryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_NORMAL;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final int layoutId =
                (getItemViewType(cursor.getPosition()) == VIEW_TYPE_TODAY)
                        ? R.layout.item_weather_list_today
                        : R.layout.item_weather_list;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int iconResource = R.drawable.ic_launcher;
        int descResource = R.string.desc_unknown;

        String iconName = cursor.getString(cursor.getColumnIndex(Contract.DayEntry.COL_ICON));

        if (iconName.equals("clear")) {
            iconResource = R.drawable.art_clear;
            descResource = R.string.desc_clear;
        } else if (iconName.equals("partlycloudy")) {
            iconResource = R.drawable.art_cloudy;
            descResource = R.string.desc_partly_cloudy;
        } else if (iconName.equals("mostlycloudy")) {
            iconResource = R.drawable.art_cloudy;
            descResource = R.string.desc_mostly_cloudy;
        } else if (iconName.equals("cloudy")) {
            iconResource = R.drawable.art_overcast;
            descResource = R.string.desc_cloudy;
        } else if (iconName.equals("chancerain")) {
            iconResource = R.drawable.art_rain;
            descResource = R.string.desc_chance_rain;
        }

        // Convert the date string to a real date.
        String date = cursor.getString(cursor.getColumnIndex(Contract.DayEntry.COL_DATE));
        try {
            SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyyMMdd");
            Date inputDate = dbDateFormat.parse(date);
            SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM d");
            date = monthDayFormat.format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.artIcon.setImageDrawable(mContext.getResources().getDrawable(iconResource));
        viewHolder.title.setText(date);
        viewHolder.description.setText(mContext.getResources().getString(descResource));
        viewHolder.tempMax.setText(Contract.DayEntry.longToDegrees(cursor.getLong(cursor.getColumnIndex(Contract.DayEntry.COL_TEMP_HIGH))));
        viewHolder.tempMin.setText(Contract.DayEntry.longToDegrees(cursor.getLong(cursor.getColumnIndex(Contract.DayEntry.COL_TEMP_LOW))));
    }

    private static class ViewHolder {
        public ImageView artIcon;
        public TextView title;
        public TextView description;
        public TextView tempMax;
        public TextView tempMin;

        public ViewHolder(View rootView) {
            artIcon = (ImageView) rootView.findViewById(R.id.art_icon);
            title = (TextView) rootView.findViewById(R.id.title);
            description = (TextView) rootView.findViewById(R.id.description);
            tempMax = (TextView) rootView.findViewById(R.id.temp_max);
            tempMin = (TextView) rootView.findViewById(R.id.temp_min);
        }
    }

}
