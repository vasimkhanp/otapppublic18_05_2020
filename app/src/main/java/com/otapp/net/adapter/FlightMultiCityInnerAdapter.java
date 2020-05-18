package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightMultiCityPojo;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightMultiCityInnerAdapter extends BaseAdapter {

    private Context mContext;
    List<List<List<FlightMultiCityPojo.City>>> mCitiesList = new ArrayList<>();

    public FlightMultiCityInnerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(List<List<List<FlightMultiCityPojo.City>>> mTempEventList) {

        if (mCitiesList != null && mCitiesList.size() > 0) {
            mCitiesList.clear();
        }

        if (mTempEventList != null && mTempEventList.size() > 0) {
            mCitiesList.addAll(mTempEventList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCitiesList.size();
    }

    @Override
    public Object getItem(int i) {
        return mCitiesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_flight_multicity_inner, null);
            ViewHolder holder = new ViewHolder();
            holder.tvAirline = view.findViewById(R.id.tvAirline);
            holder.tvDepartureTime = view.findViewById(R.id.tvDepartureTime);
            holder.tvDepartureCity = view.findViewById(R.id.tvDepartureCity);
            holder.tvTime = view.findViewById(R.id.tvTime);
            holder.tvStop = view.findViewById(R.id.tvStop);
            holder.tvDestinationTime = view.findViewById(R.id.tvDestinationTime);
            holder.tvDestinationCity = view.findViewById(R.id.tvDestinationCity);
            holder.tvMultiStop = view.findViewById(R.id.tvMultiStop);

            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final List<FlightMultiCityPojo.City> cities = mCitiesList.get(i).get(0);

        if (cities != null && cities.size() > 0) {

            if (cities.size() > 1) {
                holder.tvMultiStop.setVisibility(View.VISIBLE);
                if (cities.size() > 2) {
                    holder.tvMultiStop.setText("" + mContext.getString(R.string.multiple_layovers));
                } else {
                    holder.tvMultiStop.setText("" + mContext.getString(R.string.layover_at) + " " + cities.get(1).startAirportCity);
                }
                holder.tvStop.setText((cities.size() - 1) + " " + mContext.getString(R.string.stop));
            } else {
                holder.tvMultiStop.setVisibility(View.GONE);
                holder.tvStop.setText("" + mContext.getString(R.string.non_stop));
            }

            FlightMultiCityPojo.City mCities = cities.get(0);

            holder.tvAirline.setText(mContext.getString(R.string.trip) + " " + (i + 1) +", "+Utils.getFlightTripDate(mCities.startDate)+" - "+ mCities.airlineName);
            holder.tvDepartureTime.setText("" + Utils.getFlightTime(mCities.startDate));
            holder.tvDepartureCity.setText(mCities.startAirportCity);
            holder.tvDestinationTime.setText("" + Utils.getFlightTime(cities.get(cities.size() - 1).endDate));
            holder.tvDestinationCity.setText(cities.get(cities.size() - 1).endAirportCity);

            try {

                Date date1 = DateFormate.sdfAirportServerDate.parse(mCities.startDate);
                Date date2 = DateFormate.sdfAirportServerDate.parse(cities.get(cities.size() - 1).endDate);
                String mTime = Utils.getTimeDifference(date1, date2);
                holder.tvTime.setText(mTime);

            } catch (Exception e) {
                e.printStackTrace();
                holder.tvTime.setText("");
            }

        }

        return view;
    }

    private class ViewHolder {
        TextView tvAirline, tvDepartureTime, tvDepartureCity, tvTime, tvStop, tvDestinationTime, tvDestinationCity, tvMultiStop;
    }
}
