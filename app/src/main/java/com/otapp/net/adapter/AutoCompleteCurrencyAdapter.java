package com.otapp.net.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightCityPojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteCurrencyAdapter extends ArrayAdapter<FlightCityPojo.Currency> implements Filterable {

    Context context;
    private int resource;
    private List<FlightCityPojo.Currency> mCurrencyList;


    public AutoCompleteCurrencyAdapter(Context context, int resource, List<FlightCityPojo.Currency> mCurrencyList) {
        super(context, resource, mCurrencyList);

        this.context = context;
        this.resource = resource;
        this.mCurrencyList = new ArrayList<>();
        this.mCurrencyList.addAll(mCurrencyList);
//        tempCities = new ArrayList<FlightCityPojo.City>(mCurrencyList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            ViewHolder holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(resource, parent, false);
            holder.tvCurrency = view.findViewById(R.id.tvCurrency);
            holder.ivIcon = view.findViewById(R.id.ivIcon);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        FlightCityPojo.Currency mCurrency = getItem(position);
        if (mCurrency != null) {
//            TextView tvAirport = view.findViewById(R.id.tvAirport);
//            TextView tvIndex = view.findViewById(R.id.tvIndex);
//            TextView tvCode = view.findViewById(R.id.tvCode);
//            tvIndex.setText(mCity.cityName);
//            tvAirport.setText(mCity.airport);
//            tvCode.setText(mCity.airportCode);
            holder.tvCurrency.setText(""+mCurrency.currencyName);
            if (!TextUtils.isEmpty(mCurrency.icon)) {
                Picasso.get().load(mCurrency.currencyName).into(holder.ivIcon);
            }

        }
        return view;
    }

    public class ViewHolder {
        TextView tvCurrency;
        ImageView ivIcon;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((FlightCityPojo.Currency) resultValue).currencyName;
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null) {
                ArrayList<FlightCityPojo.Currency> suggestions = new ArrayList<FlightCityPojo.Currency>();
                for (FlightCityPojo.Currency mCity : mCurrencyList) {
                    if (mCity.currencyName.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(mCity);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;

//            if (constraint != null) {
//                suggestions.clear();
//                for (FlightCityPojo.City city : tempCities) {
//                    LogUtils.e("", "city.cityName::" + city.cityName + " constraint::" + constraint.toString().toLowerCase());
//                    if (city.cityName.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
//                        suggestions.add(city);
//                        LogUtils.e("","city add:"+city.cityName);
//                    }
//                }
//                LogUtils.e("", "suggestions::"+suggestions);
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = suggestions;
//                filterResults.count = suggestions.size();
//                return filterResults;
//            } else {
//                return new FilterResults();
//            }

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//            List<FlightCityPojo.City> filterList = (ArrayList<FlightCityPojo.City>) results.values;
//            LogUtils.e("", "filterList::"+filterList);
//            if (results != null && results.count > 0) {
//                clear();
//                for (FlightCityPojo.City city : filterList) {
//                    add(city);
//                    LogUtils.e("", "city name add::"+city.cityName);
//                }
//                notifyDataSetChanged();
//            } else {
//                clear();
//                notifyDataSetChanged();
//            }
            clear();

            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<FlightCityPojo.Currency>) results.values);
            } else {
                // no filter, add entire original list back in
                addAll(mCurrencyList);
            }
            notifyDataSetChanged();

        }
    };


}
