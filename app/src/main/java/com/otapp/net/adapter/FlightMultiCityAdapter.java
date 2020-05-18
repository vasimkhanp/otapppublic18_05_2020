package com.otapp.net.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightMultiCityPojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FlightMultiCityAdapter extends BaseAdapter {

    private Context mContext;
    List<FlightMultiCityPojo.Data> mCitiesList = new ArrayList<>();
    private OnFlightSelectListener onFlightSelectListener;

    public FlightMultiCityAdapter(Context mContext, OnFlightSelectListener onFlightSelectListener) {
        this.mContext = mContext;
        this.onFlightSelectListener = onFlightSelectListener;
    }

    public interface OnFlightSelectListener {
        public void onFlightSelected(FlightMultiCityPojo.Data mFlightData);
    }


    public void addAll(List<FlightMultiCityPojo.Data> mTempEventList) {

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
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_flight_multicity, null);
            ViewHolder holder = new ViewHolder();
            holder.ivAirline1 = view.findViewById(R.id.ivAirline1);
            holder.ivAirline2 = view.findViewById(R.id.ivAirline2);
            holder.ivAirline3 = view.findViewById(R.id.ivAirline3);
            holder.ivAirline4 = view.findViewById(R.id.ivAirline4);

            holder.tvFlights = view.findViewById(R.id.tvFlights);
            holder.tvPrice = view.findViewById(R.id.tvPrice);

            holder.lvFlights = view.findViewById(R.id.lvFlights);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();


        StringBuilder strBuilder = new StringBuilder();

        for (int j = 0; j < mCitiesList.get(i).airlineTItles.size(); j++) {

            FlightMultiCityPojo.AirlineTitles mAirlineTitles = mCitiesList.get(i).airlineTItles.get(j);

            if (strBuilder.length() > 0) {
                strBuilder.append(", " + mAirlineTitles.name);
            } else {
                strBuilder.append(mAirlineTitles.name);
            }

            holder.ivAirline1.setImageDrawable(null);
            holder.ivAirline2.setImageDrawable(null);
            holder.ivAirline3.setImageDrawable(null);
            holder.ivAirline4.setImageDrawable(null);

            if (!TextUtils.isEmpty(mAirlineTitles.logo)) {
                switch (j) {
                    case 0:
                        Picasso.get().load(mAirlineTitles.logo).into(holder.ivAirline1);
                        break;
                    case 1:
                        Picasso.get().load(mAirlineTitles.logo).into(holder.ivAirline2);
                        break;
                    case 2:
                        Picasso.get().load(mAirlineTitles.logo).into(holder.ivAirline3);
                        break;
                    case 3:
                        Picasso.get().load(mAirlineTitles.logo).into(holder.ivAirline4);
                        break;
                }

            }
        }

        if (strBuilder != null && strBuilder.length() > 0) {
            holder.tvFlights.setText("" + strBuilder.toString());
        }
        holder.tvPrice.setText(mCitiesList.get(0).currency + " " + mCitiesList.get(i).fares.total.grandTotal);

        FlightMultiCityInnerAdapter mFlightMultiCityInnerAdapter = new FlightMultiCityInnerAdapter(mContext);
        holder.lvFlights.setAdapter(mFlightMultiCityInnerAdapter);
        mFlightMultiCityInnerAdapter.addAll(mCitiesList.get(i).cities);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFlightSelectListener.onFlightSelected(mCitiesList.get(i));
            }
        });

        return view;
    }

    private class ViewHolder {
        TextView tvFlights, tvPrice;
        ImageView ivAirline1, ivAirline2, ivAirline3, ivAirline4;
        ListView lvFlights;
    }
}
