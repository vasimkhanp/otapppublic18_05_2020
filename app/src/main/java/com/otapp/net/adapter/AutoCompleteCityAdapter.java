package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightCityPojo;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteCityAdapter extends ArrayAdapter<FlightCityPojo.City> implements Filterable {

    Context context;
    private int resource;
    private List<FlightCityPojo.City> mCityList;


    public AutoCompleteCityAdapter(Context context, int resource, List<FlightCityPojo.City> mCityList) {
        super(context, resource, mCityList);

        this.context = context;
        this.resource = resource;
        this.mCityList = new ArrayList<>();
        this.mCityList.addAll(mCityList);
//        tempCities = new ArrayList<FlightCityPojo.City>(mCityList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            ViewHolder holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(resource, parent, false);
            holder.tvAirport = view.findViewById(R.id.tvAirport);
            holder.tvCity = view.findViewById(R.id.tvCity);
            holder.tvCode = view.findViewById(R.id.tvCode);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        FlightCityPojo.City mCity = getItem(position);
        if (mCity != null) {
//            TextView tvAirport = view.findViewById(R.id.tvAirport);
//            TextView tvIndex = view.findViewById(R.id.tvIndex);
//            TextView tvCode = view.findViewById(R.id.tvCode);
//            tvIndex.setText(mCity.cityName);
//            tvAirport.setText(mCity.airport);
//            tvCode.setText(mCity.airportCode);
            holder.tvCity.setText(mCity.cityName);
            holder.tvAirport.setText(mCity.airport);
            holder.tvCode.setText(mCity.airportCode);
        }
        return view;
    }

    public class ViewHolder {
        TextView tvCity, tvAirport, tvCode;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((FlightCityPojo.City) resultValue).cityName;
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null) {
                ArrayList<FlightCityPojo.City> suggestions = new ArrayList<FlightCityPojo.City>();
                for (FlightCityPojo.City mCity : mCityList) {
                    if (mCity.cityName.toLowerCase().startsWith(constraint.toString().toLowerCase()) || mCity.airportCode.toLowerCase().startsWith(constraint.toString().toLowerCase())
                            || mCity.airport.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
                addAll((ArrayList<FlightCityPojo.City>) results.values);
            }
            else {
                // no filter, add entire original list back in
                addAll(mCityList);
            }
            notifyDataSetChanged();

        }
    };


}
