package com.otapp.net.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightOneAdapter extends BaseAdapter {

    private Context mContext;
    List<FlightOneListPojo.Data> mCitiesList = new ArrayList<>();
    private OnFlightSelectListener onFlightSelectListener;
    long mDurationLayover;

    public FlightOneAdapter(Context mContext, OnFlightSelectListener onFlightSelectListener) {
        this.mContext = mContext;
        this.onFlightSelectListener = onFlightSelectListener;
    }

    public interface OnFlightSelectListener {
        public void onFlightSelected(FlightOneListPojo.Data mFlightData);
    }


    public void addAll(List<FlightOneListPojo.Data> mTempEventList) {

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
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_flight, null);
            ViewHolder holder = new ViewHolder();
            holder.ivAirline = view.findViewById(R.id.ivAirline);
            holder.tvAirline = view.findViewById(R.id.tvAirline);
            holder.tvAirlineNo = view.findViewById(R.id.tvAirlineNo);
            holder.tvDepartureTime = view.findViewById(R.id.tvDepartureTime);
            holder.tvDepartureCity = view.findViewById(R.id.tvDepartureCity);
            holder.tvTime = view.findViewById(R.id.tvTime);
            holder.tvStop = view.findViewById(R.id.tvStop);
            holder.tvDestinationTime = view.findViewById(R.id.tvDestinationTime);
            holder.tvDestinationCity = view.findViewById(R.id.tvDestinationCity);
            holder.tvPrice = view.findViewById(R.id.tvPrice);
            holder.tvMultiStop = view.findViewById(R.id.tvMultiStop);
            holder.tvTag = view.findViewById(R.id.tvTag);
//            holder.aviProgress = view.findViewById(R.id.aviProgress);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final List<FlightOneListPojo.Cities> cities = mCitiesList.get(i).cities;

        if (!TextUtils.isEmpty(mCitiesList.get(i).tag)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText("" + mCitiesList.get(i).tag);
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }

        if (cities != null && cities.size() > 0) {
            try {
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

                StringBuilder strNoBuilder = new StringBuilder();
                long mDuration = 0;
                long inscond=0;
                mDurationLayover=0;
                for (int j = 0; j < cities.size(); j++) {
                    FlightOneListPojo.Cities mCities = cities.get(j);
                    if (strNoBuilder.length() > 0) {
                        strNoBuilder.append(", " + mCities.airline + " " + mCities.flightNumber);
                    } else {
                        strNoBuilder.append(mCities.airline + " " + mCities.flightNumber);
                    }
                    mDuration = mDuration + mCities.duration;
                }
              /*  for (int j = 0; j < cities.size()-1; j++) {
                    FlightOneListPojo.Cities mCities = cities.get(j);
                    Date date1;
                    Date date2;
                    if (cities.size() > 1) {

                        date1 = DateFormate.sdfAirportServerDate.parse(cities.get(j + 1).startDate);
                        date2 = DateFormate.sdfAirportServerDate.parse(cities.get(j).endDate);

                       inscond =(date1.getTime() - date2.getTime())/1000%60;
                         mDurationLayover=mDurationLayover+inscond ;


                    }
                }
                long diffMinutes = mDurationLayover / (60 * 1000) % 60;
                long diffHours = mDurationLayover / (60 * 60 * 1000);*/
                mDuration=mDuration+mDurationLayover;

                FlightOneListPojo.Cities mCities = cities.get(0);

                holder.tvAirline.setText("" + mCities.airlineName);
                holder.tvAirlineNo.setText("| " + strNoBuilder.toString());
                holder.tvDepartureTime.setText("" + Utils.getFlightTime(mCities.startDate));
                holder.tvDepartureCity.setText(mCities.startAirportCity);
                holder.tvDestinationTime.setText("" + Utils.getFlightTime(cities.get(cities.size() - 1).endDate));
                holder.tvDestinationCity.setText(cities.get(cities.size() - 1).endAirportCity);
                holder.tvPrice.setText(mCitiesList.get(0).currency + " " + Utils.setPrice(mCitiesList.get(i).fares.total.grandTotal));


                String mTime = Utils.getTimeDifference(mDuration);
                LogUtils.e("", i + "mDuration::" + mDuration + " mTime::" + mTime);
                if (!TextUtils.isEmpty(mTime)) {
                    holder.tvTime.setText("" + mTime);
                } else {
                    holder.tvTime.setText("");
                }

//            try {
//
//                Date date1 = DateFormate.sdfAirportServerDate.parse(mCities.startDate);
//                Date date2 = DateFormate.sdfAirportServerDate.parse(cities.get(cities.size() - 1).endDate);
//                String mTime = Utils.getTimeDifference(date1, date2);
//                holder.tvTime.setText(mTime);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                holder.tvTime.setText("");
//            }


                if (!TextUtils.isEmpty(mCities.logo)) {
//                holder.aviProgress.setVisibility(View.VISIBLE);

                    Picasso.get().load(mCities.logo).into(holder.ivAirline, new Callback() {
                        @Override
                        public void onSuccess() {
//                        holder.aviProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
//                        holder.aviProgress.setVisibility(View.GONE);
                        }
                    });

                } else {
//                holder.aviProgress.setVisibility(View.GONE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFlightSelectListener.onFlightSelected(mCitiesList.get(i));
            }
        });

        return view;
    }

    private class ViewHolder {
        TextView tvAirline, tvAirlineNo, tvDepartureTime, tvDepartureCity, tvTime, tvStop, tvDestinationTime, tvDestinationCity, tvPrice, tvMultiStop, tvTag;
        ImageView ivAirline;
//        AVLoadingIndicatorView aviProgress;
    }
}
