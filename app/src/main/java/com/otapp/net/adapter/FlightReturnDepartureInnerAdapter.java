package com.otapp.net.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightReturnListPojo;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightReturnDepartureInnerAdapter extends BaseAdapter {

    private Context mContext;
    List<List<List<FlightReturnListPojo.City>>> mCitiesList = new ArrayList<>();

    public FlightReturnDepartureInnerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(List<List<List<FlightReturnListPojo.City>>> mTempEventList) {

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
            holder.tvOption = view.findViewById(R.id.tvOption);

            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final List<FlightReturnListPojo.City> cities = mCitiesList.get(i).get(0);

        if (mCitiesList.get(i).size() > 1) {
            holder.tvOption.setVisibility(View.VISIBLE);
            holder.tvOption.setText("+" + (mCitiesList.get(i).size() - 1) + " " + mContext.getString(R.string.options));
        } else {
            holder.tvOption.setVisibility(View.GONE);
        }

        if (cities != null && cities.size() > 0) {

            if (cities.size() > 1) {
                holder.tvMultiStop.setVisibility(View.VISIBLE);
                if (cities.size() > 2) {
                    holder.tvMultiStop.setText("(" + mContext.getString(R.string.multiple_layovers) + ")");
                } else {

                    String mTime = "";

                    try {

                        Date date1 = DateFormate.sdfAirportServerDate.parse(cities.get(0).endDate);
                        Date date2 = DateFormate.sdfAirportServerDate.parse(cities.get(cities.size() - 1).startDate);
                        mTime = Utils.getTimeDifference(date1, date2);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String mStart = "(" + mContext.getString(R.string.layover_at) + " " + cities.get(1).startAirportCity;

                    Spannable wordtoSpan = new SpannableString("(" + mContext.getString(R.string.layover_at) + " " + cities.get(1).startAirportCity + " " + mTime + ")");
                    wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), mStart.length(), mStart.length() + 1 + mTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    holder.tvMultiStop.setText(wordtoSpan);

//                    holder.tvMultiStop.setText(mContext.getString(R.string.layover_at) + " " + cities.get(1).startAirportCity + " " + mTime);
                }
                holder.tvStop.setText((cities.size() - 1) + " " + mContext.getString(R.string.stop));
            } else {
                holder.tvMultiStop.setVisibility(View.GONE);
                holder.tvStop.setText("" + mContext.getString(R.string.non_stop));
            }

            FlightReturnListPojo.City mCities = cities.get(0);

            holder.tvAirline.setText((i == 0 ? mContext.getString(R.string.depart) : mContext.getString(R.string.retrn)) + " - " + mCities.airlineName);
            holder.tvDepartureTime.setText("" + Utils.getFlightTime(mCities.startDate));
            holder.tvDepartureCity.setText(mCities.startAirportCity);
            holder.tvDestinationTime.setText("" + Utils.getFlightTime(cities.get(cities.size() - 1).endDate));
            holder.tvDestinationCity.setText(cities.get(cities.size() - 1).endAirportCity);

            long mDuration = 0;
            for (int j = 0; j < cities.size(); j++) {
                mDuration = mDuration + cities.get(j).duration;
            }

            try {

//                Date date1 = DateFormate.sdfAirportServerDate.parse(mCities.startDate);
//                Date date2 = DateFormate.sdfAirportServerDate.parse(cities.get(cities.size() - 1).endDate);
                String mTime = Utils.getTimeDifference(mDuration);
                if (!TextUtils.isEmpty(mTime)) {
                    Spannable wordtoSpan = new SpannableString(mTime);
                    if (mTime.contains("d ")) {
                        int index = mTime.indexOf("d ");
                        wordtoSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.gray_68)), index, index + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (mTime.contains("h ")) {
                        int index = mTime.indexOf("h ");
                        wordtoSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.gray_68)), index, index + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (mTime.contains("m")) {
                        int index = mTime.indexOf("m");
                        wordtoSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.gray_68)), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    holder.tvTime.setText(wordtoSpan);
                }

            } catch (Exception e) {
                e.printStackTrace();
                holder.tvTime.setText("");
            }

        }

        return view;
    }

    private class ViewHolder {
        TextView tvAirline, tvDepartureTime, tvDepartureCity, tvTime, tvStop, tvDestinationTime, tvDestinationCity, tvMultiStop, tvOption;
    }
}
