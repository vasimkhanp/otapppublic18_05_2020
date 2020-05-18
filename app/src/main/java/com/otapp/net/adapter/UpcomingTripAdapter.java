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
import com.otapp.net.fragment.UpcomingTripsFragment;
import com.otapp.net.model.UpcomingTripPojo;
import com.otapp.net.utils.DateFormate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpcomingTripAdapter extends BaseAdapter {

    private Context mContext;
    private UpcomingTripsFragment mUpcomingTripsFragment;

    List<UpcomingTripPojo.UpcomingTrip> mUpcomingTrip = new ArrayList<>();

    public UpcomingTripAdapter(Context mContext, UpcomingTripsFragment mUpcomingTripsFragment) {
        this.mContext = mContext;
        this.mUpcomingTripsFragment = mUpcomingTripsFragment;
    }

    public void addAll(List<UpcomingTripPojo.UpcomingTrip> mTempEventList) {

        if (mUpcomingTrip != null && mUpcomingTrip.size() > 0) {
            mUpcomingTrip.clear();
        }

        if (mTempEventList != null && mTempEventList.size() > 0) {
            mUpcomingTrip.addAll(mTempEventList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mUpcomingTrip.size();
    }

    @Override
    public Object getItem(int i) {
        return mUpcomingTrip.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_trip, null);
            ViewHolder holder = new ViewHolder();
            holder.ivPhoto = view.findViewById(R.id.ivPhoto);
            holder.tvTitle = view.findViewById(R.id.tvTitle);
            holder.tvFrom = view.findViewById(R.id.tvFrom);
            holder.tvTo = view.findViewById(R.id.tvTo);
            holder.tvDate = view.findViewById(R.id.tvDate);
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvRoundTrip = view.findViewById(R.id.tvRoundTrip);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final UpcomingTripPojo.UpcomingTrip mUpcomingTrp = mUpcomingTrip.get(i);

        holder.tvTo.setVisibility(View.GONE);
        holder.tvRoundTrip.setVisibility(View.GONE);

        if (mUpcomingTrp != null && !TextUtils.isEmpty(mUpcomingTrp.service)) {

            if (mUpcomingTrp.service.equalsIgnoreCase("Movie")) {
                Picasso.get().load(R.drawable.ic_movie_blue).into(holder.ivPhoto);

                holder.tvTitle.setText("" + mUpcomingTrp.serviceName);
                holder.tvFrom.setText("" + mUpcomingTrp.propName);
                holder.tvName.setText("" + mUpcomingTrp.name);

                try {

                    Date date = DateFormate.sdfMyBookingDate.parse(mUpcomingTrp.showDateTime);
                    String strDate = DateFormate.sdfTripDisplayDate.format(date);
                    if (!TextUtils.isEmpty(strDate)) {
                        holder.tvDate.setText("" + strDate);
                    } else {
                        holder.tvDate.setText("" + mUpcomingTrp.showDateTime);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    holder.tvDate.setText("" + mUpcomingTrp.showDateTime);
                }


                holder.tvTo.setVisibility(View.GONE);
                holder.tvRoundTrip.setVisibility(View.GONE);

            } else if (mUpcomingTrp.service.equalsIgnoreCase("Event")) {

                Picasso.get().load(R.drawable.ic_events_blue).into(holder.ivPhoto);

                holder.tvTitle.setText("" + mUpcomingTrp.serviceName);
                holder.tvFrom.setText("" + mUpcomingTrp.ticketType);
                holder.tvName.setText("" + mUpcomingTrp.name);

                try {

                    Date date = DateFormate.sdfMyBookingDate.parse(mUpcomingTrp.showDateTime);
                    String strDate = DateFormate.sdfTripDisplayDate.format(date);
                    if (!TextUtils.isEmpty(strDate)) {
                        holder.tvDate.setText("" + strDate);
                    } else {
                        holder.tvDate.setText("" + mUpcomingTrp.showDateTime);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    holder.tvDate.setText("" + mUpcomingTrp.showDateTime);
                }


                holder.tvTo.setVisibility(View.GONE);
                holder.tvRoundTrip.setVisibility(View.GONE);

            } else if (mUpcomingTrp.service.equalsIgnoreCase("Flights")) {
                Picasso.get().load(R.drawable.ic_airplane_blue).into(holder.ivPhoto);

                holder.tvTitle.setText("" + mUpcomingTrp.flightType);
                holder.tvFrom.setText("" + mUpcomingTrp.startCity);
                holder.tvTo.setText("" + mUpcomingTrp.endCity);
                holder.tvName.setText("" + mUpcomingTrp.name);

                try {

                    StringBuilder stringBuilder = new StringBuilder();
                    Date date = DateFormate.sdfMyBookingDate.parse(mUpcomingTrp.depatureDate);
                    String strDate = DateFormate.sdfTripDisplayDate.format(date);
                    if (!TextUtils.isEmpty(strDate)) {
                        stringBuilder.append(strDate);
//                        holder.tvDate.setText("" + );
                    } else {
                        stringBuilder.append(mUpcomingTrp.showDateTime);
//                        holder.tvDate.setText("" + mUpcomingTrp.showDateTime);
                    }

                    if (!TextUtils.isEmpty(mUpcomingTrp.returnDate)) {

                        Date date1 = DateFormate.sdfAirportServerDate.parse(mUpcomingTrp.returnDate);
                        String strDate1 = DateFormate.sdfTripDisplayDate.format(date1);
                        if (!TextUtils.isEmpty(strDate1)) {
                            stringBuilder.append(" - " + strDate1);
//                        holder.tvDate.setText("" + );
                        } else {
                            stringBuilder.append(" - " + mUpcomingTrp.returnDate);
//                        holder.tvDate.setText("" + mUpcomingTrp.showDateTime);
                        }

                    }

                    holder.tvDate.setText("" + stringBuilder.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                    holder.tvDate.setText("" + mUpcomingTrp.showDateTime);
                }


                holder.tvTo.setVisibility(View.VISIBLE);
                holder.tvRoundTrip.setVisibility(View.VISIBLE);

            } else if (mUpcomingTrp.service.equalsIgnoreCase("Theme Park")) {
                Picasso.get().load(R.drawable.ic_theme_park_blue).into(holder.ivPhoto);

                holder.tvTitle.setText("" + mUpcomingTrp.serviceName);
                holder.tvFrom.setText("" + mUpcomingTrp.propName);
                holder.tvName.setText("" + mUpcomingTrp.name);

                try {

                    Date date = DateFormate.sdfMyBookingDate.parse(mUpcomingTrp.showDateTime);
                    String strDate = DateFormate.sdfTripDisplayDate.format(date);
                    if (!TextUtils.isEmpty(strDate)) {
                        holder.tvDate.setText("" + strDate);
                    } else {
                        holder.tvDate.setText("" + mUpcomingTrp.showDateTime);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    holder.tvDate.setText("" + mUpcomingTrp.showDateTime);
                }

                holder.tvTo.setVisibility(View.GONE);
                holder.tvRoundTrip.setVisibility(View.GONE);
            }

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mUpcomingTripsFragment.onTripClicked(mUpcomingTrp);
            }
        });

        return view;
    }


    private class ViewHolder {
        TextView tvTitle, tvFrom, tvTo, tvDate, tvName, tvRoundTrip;
        ImageView ivPhoto;
    }
}
