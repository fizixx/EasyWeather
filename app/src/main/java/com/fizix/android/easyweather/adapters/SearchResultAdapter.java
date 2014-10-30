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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.search_result_list_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        SearchResult result = getItem(position);

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.cityName.setText(result.getCityName());
        holder.countryCode.setText(result.getCountryCode());
        holder.coords.setText(result.getCoordsAsString());

        return convertView;
    }

    static class ViewHolder {

        TextView cityName;
        TextView countryCode;
        TextView coords;

        public ViewHolder(View parent) {
            cityName = (TextView) parent.findViewById(R.id.city_name);
            countryCode = (TextView) parent.findViewById(R.id.country_code);
            coords = (TextView) parent.findViewById(R.id.coords);
        }

    }
}
