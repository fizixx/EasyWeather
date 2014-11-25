package com.fizix.android.easyweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fizix.android.easyweather.R;
import com.fizix.android.easyweather.models.SearchResult;

import java.util.List;

public class SearchResultAdapter extends BaseAdapter {

    Context mContext;
    List<SearchResult> mResults;

    public SearchResultAdapter(Context context, List<SearchResult> results) {
        super();
        mContext = context;
        mResults = results;
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public SearchResult getItem(int position) {
        return mResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Create the new item if one doesn't exist already.
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_result_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        // Get the search result.
        SearchResult result = getItem(position);

        // Populate the data in the view.
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.cityName.setText(result.getLocation());
        viewHolder.countryCode.setText(result.getCountryCode());
        viewHolder.coords.setText(result.getCoordsAsString());

        return convertView;
    }

    private static class ViewHolder {
        public TextView cityName;
        public TextView countryCode;
        public TextView coords;

        ViewHolder(View parent) {
            cityName = (TextView) parent.findViewById(R.id.city_name);
            countryCode = (TextView) parent.findViewById(R.id.country_code);
            coords = (TextView) parent.findViewById(R.id.coords);
        }
    }

}
