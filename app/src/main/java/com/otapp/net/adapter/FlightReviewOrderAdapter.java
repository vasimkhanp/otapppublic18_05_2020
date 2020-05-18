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
import com.otapp.net.model.FlightOneDetailsPojo;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FlightReviewOrderAdapter extends BaseAdapter {

    private Context mContext;
    String clasName;
    List<FlightOneDetailsPojo.FlightDetail> mFlightList = new ArrayList<>();

    public FlightReviewOrderAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setClass(String clasName) {
        this.clasName = clasName;
    }

    public void addAll(List<FlightOneDetailsPojo.FlightDetail> mTempEventList) {

        if (mFlightList != null && mFlightList.size() > 0) {
            mFlightList.clear();
        }

        if (mTempEventList != null && mTempEventList.size() > 0) {
            mFlightList.addAll(mTempEventList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFlightList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFlightList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_flight_order_review, null);
            ViewHolder holder = new ViewHolder();
            holder.ivAirline = view.findViewById(R.id.ivAirline);
            holder.tvDepartureDate = view.findViewById(R.id.tvDepartureDate);
            holder.tvDestinationDate = view.findViewById(R.id.tvDestinationDate);
            holder.tvAirline = view.findViewById(R.id.tvAirline);
            holder.tvAirlineNo = view.findViewById(R.id.tvAirlineNo);
            holder.tvAirlineClass = view.findViewById(R.id.tvAirlineClass);
            holder.tvDepartureCity = view.findViewById(R.id.tvDepartureCity);
            holder.tvDepartureCode = view.findViewById(R.id.tvDepartureCode);
            holder.tvDestinationCode = view.findViewById(R.id.tvDestinationCode);
            holder.tvDestinationCity = view.findViewById(R.id.tvDestinationCity);
            holder.tvDepartureTime = view.findViewById(R.id.tvDepartureTime);
            holder.tvDestinationTime = view.findViewById(R.id.tvDestinationTime);
            holder.tvDepartureAirport = view.findViewById(R.id.tvDepartureAirport);
            holder.tvDestinationAirport = view.findViewById(R.id.tvDestinationAirport);
            holder.tvBaggage = view.findViewById(R.id.tvBaggage);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final FlightOneDetailsPojo.FlightDetail mFlightDetail = mFlightList.get(i);

        if (mFlightDetail != null) {

            holder.tvAirline.setText(mFlightDetail.airline);
            holder.tvAirlineNo.setText(mFlightDetail.airline + " " + mFlightDetail.flightNumber);

            holder.tvDepartureCity.setText("" + mFlightDetail.startAirportCity);
            holder.tvDepartureCode.setText("" + mFlightDetail.startAirport);
            holder.tvDepartureAirport.setText(mFlightDetail.startAirportName);

            holder.tvDestinationCity.setText("" + mFlightDetail.endAirportCity);
            holder.tvDestinationCode.setText("" + mFlightDetail.endAirport);
            holder.tvDestinationAirport.setText(mFlightDetail.endAirportName);

            holder.tvDepartureTime.setText("" + Utils.getFlight12Time(mFlightDetail.startDate));
            holder.tvDestinationTime.setText("" + Utils.getFlight12Time(mFlightDetail.endDate));

            holder.tvDepartureDate.setText("" + Utils.getFlightDate(mFlightDetail.startDate));
            holder.tvDestinationDate.setText("" + Utils.getFlightDate(mFlightDetail.endDate));
            holder.tvBaggage.setText(" " + mFlightDetail.baggageInfo);
            holder.tvAirlineClass.setText("" + clasName);


            if (!TextUtils.isEmpty(mFlightDetail.logo)) {
                Picasso.get().load(mFlightDetail.logo).into(holder.ivAirline);
            }
        }


        return view;
    }

    private class ViewHolder {
        TextView tvDepartureDate, tvDestinationDate, tvAirline, tvAirlineNo, tvAirlineClass, tvDepartureCity, tvDepartureCode, tvDestinationCode, tvDestinationCity, tvDepartureTime,
                tvDestinationTime, tvDepartureAirport, tvDestinationAirport, tvBaggage;
        ImageView ivAirline;
    }
}
