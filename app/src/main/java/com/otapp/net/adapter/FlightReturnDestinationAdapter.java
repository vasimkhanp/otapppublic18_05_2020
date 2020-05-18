//package com.otapp.net.adapter;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.otapp.net.R;
//import com.otapp.net.model.FlightReturnListPojo;
//import com.otapp.net.utils.DateFormate;
//import com.otapp.net.utils.Utils;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class FlightReturnDestinationAdapter extends BaseAdapter {
//
//    private Context mContext;
//    List<FlightReturnListPojo.Return> mCitiesList = new ArrayList<>();
//    private OnFlightSelectListener onFlightSelectListener;
//
//    public FlightReturnDestinationAdapter(Context mContext, OnFlightSelectListener onFlightSelectListener) {
//        this.mContext = mContext;
//        this.onFlightSelectListener = onFlightSelectListener;
//    }
//
//    public interface OnFlightSelectListener {
//        public void onFlightSelected(FlightReturnListPojo.Return mFlightData);
//    }
//
//
//    public void addAll(List<FlightReturnListPojo.Return> mTempEventList) {
//
//        if (mCitiesList != null && mCitiesList.size() > 0) {
//            mCitiesList.clear();
//        }
//
//        if (mTempEventList != null && mTempEventList.size() > 0) {
//            mCitiesList.addAll(mTempEventList);
//        }
//
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getCount() {
//        return mCitiesList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return mCitiesList.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(final int i, View view, ViewGroup viewGroup) {
//
//        if (view == null) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_flight_return, null);
//            ViewHolder holder = new ViewHolder();
//            holder.ivAirline = view.findViewById(R.id.ivAirline);
//            holder.tvAirline = view.findViewById(R.id.tvAirline);
//            holder.tvAirlineNo = view.findViewById(R.id.tvAirlineNo);
//            holder.tvDepartureTime = view.findViewById(R.id.tvDepartureTime);
//            holder.tvDepartureCity = view.findViewById(R.id.tvDepartureCity);
//            holder.tvTime = view.findViewById(R.id.tvTime);
//            holder.tvStop = view.findViewById(R.id.tvStop);
//            holder.tvDestinationTime = view.findViewById(R.id.tvDestinationTime);
//            holder.tvDestinationCity = view.findViewById(R.id.tvDestinationCity);
//            holder.tvRate = view.findViewById(R.id.tvRate);
//            holder.tvMultiStop = view.findViewById(R.id.tvMultiStop);
//            holder.lnrContainer = view.findViewById(R.id.lnrContainer);
////            holder.aviProgress = view.findViewById(R.id.aviProgress);
//            view.setTag(holder);
//        }
//
//        final ViewHolder holder = (ViewHolder) view.getTag();
//
//        final List<FlightReturnListPojo.Cities> cities = mCitiesList.get(i).cities;
//
//        if (mCitiesList.get(i).isSelected) {
//            holder.lnrContainer.setBackgroundResource(R.color.flight_selected);
//        } else {
//            holder.lnrContainer.setBackgroundResource(R.color.white);
//        }
//
//        if (cities != null && cities.size() > 0) {
//
//            if (cities.size() > 1) {
//                holder.tvMultiStop.setVisibility(View.VISIBLE);
//                if (cities.size() > 2) {
//                    holder.tvMultiStop.setText("" + mContext.getString(R.string.multiple_layovers));
//                } else {
//                    holder.tvMultiStop.setText("" + mContext.getString(R.string.layover_at) + " " + cities.get(1).startAirportCity);
//                }
////                holder.tvStop.setText((cities.size() - 1) + " " + mContext.getString(R.string.stop));
//                holder.tvDestinationCity.setText((cities.size() - 1) + " " + mContext.getString(R.string.stop));
//            } else {
//                holder.tvMultiStop.setVisibility(View.GONE);
////                holder.tvStop.setText("" + mContext.getString(R.string.non_stop));
//                holder.tvDestinationCity.setText("" + mContext.getString(R.string.non_stop));
//            }
//
//            StringBuilder strNoBuilder = new StringBuilder();
//            for (int j = 0; j < cities.size(); j++) {
//                FlightReturnListPojo.Cities mCities = cities.get(j);
//                if (strNoBuilder.length() > 0) {
//                    strNoBuilder.append(", " + mCities.airline + " " + mCities.flightNumber);
//                } else {
//                    strNoBuilder.append(mCities.airline + " " + mCities.flightNumber);
//                }
//            }
//
//            FlightReturnListPojo.Cities mCities = cities.get(0);
//
//            holder.tvAirline.setText("" + mCities.airlineName);
//            holder.tvAirlineNo.setText("| " + strNoBuilder.toString());
//            holder.tvDepartureTime.setText("" + Utils.getFlightTime(mCities.startDate));
////            holder.tvDepartureCity.setText(mCities.startAirportCity);
//            holder.tvDestinationTime.setText("" + Utils.getFlightTime(cities.get(cities.size() - 1).endDate));
////            holder.tvDestinationCity.setText(cities.get(cities.size() - 1).endAirportCity);
//            holder.tvRate.setText(mCitiesList.get(0).currency + " " + mCitiesList.get(i).fares.total.grandTotal);
//
//            try {
//
//                Date date1 = DateFormate.sdfAirportServerDate.parse(mCities.startDate);
//                Date date2 = DateFormate.sdfAirportServerDate.parse(cities.get(cities.size() - 1).endDate);
//                String mTime = Utils.getTimeDifference(date1, date2);
////                holder.tvTime.setText(mTime);
//                holder.tvDepartureCity.setText(mTime);
//
//            } catch (Exception e) {
//                e.printStackTrace();
////                holder.tvTime.setText("");
//                holder.tvDepartureCity.setText("");
//            }
//
//
//            if (!TextUtils.isEmpty(mCities.logo)) {
////                holder.aviProgress.setVisibility(View.VISIBLE);
//
//                Picasso.get().load(mCities.logo).into(holder.ivAirline, new Callback() {
//                    @Override
//                    public void onSuccess() {
////                        holder.aviProgress.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
////                        holder.aviProgress.setVisibility(View.GONE);
//                    }
//                });
//
//            } else {
////                holder.aviProgress.setVisibility(View.GONE);
//            }
//        }
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onFlightSelectListener.onFlightSelected(mCitiesList.get(i));
//            }
//        });
//
//        return view;
//    }
//
//    private class ViewHolder {
//        TextView tvAirline, tvAirlineNo, tvDepartureTime, tvDepartureCity, tvTime, tvStop, tvDestinationTime, tvDestinationCity, tvRate, tvMultiStop;
//        ImageView ivAirline;
//        LinearLayout lnrContainer;
////        AVLoadingIndicatorView aviProgress;
//    }
//}
