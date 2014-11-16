package com.fizix.android.easyweather.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fizix.android.easyweather.R;
import com.fizix.android.easyweather.models.SearchResult;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    List<SearchResult> mResults;

    public SearchResultAdapter(List<SearchResult> results) {
        super();
        mResults = results;
    }

    public SearchResult getSearchResult(int position) {
        return mResults.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        SearchResult result = mResults.get(position);

        viewHolder.cityName.setText(result.getLocation());
        viewHolder.countryCode.setText(result.getCountryCode());
        viewHolder.coords.setText(result.getCoordsAsString());
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView cityName;
        TextView countryCode;
        TextView coords;

        public ViewHolder(View itemView) {
            super(itemView);

            cityName = (TextView) itemView.findViewById(R.id.city_name);
            countryCode = (TextView) itemView.findViewById(R.id.country_code);
            coords = (TextView) itemView.findViewById(R.id.coords);
        }

    }

}
