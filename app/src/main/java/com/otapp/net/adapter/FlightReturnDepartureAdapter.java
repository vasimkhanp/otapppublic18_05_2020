package com.otapp.net.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightReturnListPojo;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FlightReturnDepartureAdapter extends BaseAdapter {

    private Context mContext;
    List<FlightReturnListPojo.Data> mCitiesList = new ArrayList<>();
    private OnFlightSelectListener onFlightSelectListener;

    public FlightReturnDepartureAdapter(Context mContext, OnFlightSelectListener onFlightSelectListener) {
        this.mContext = mContext;
        this.onFlightSelectListener = onFlightSelectListener;
    }

    public interface OnFlightSelectListener {
        public void onFlightSelected(FlightReturnListPojo.Data mFlightData);
    }


    public void addAll(List<FlightReturnListPojo.Data> mTempEventList) {

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
            holder.ivAirline0 = view.findViewById(R.id.ivAirline0);
            holder.ivAirline1 = view.findViewById(R.id.ivAirline1);
            holder.ivAirline2 = view.findViewById(R.id.ivAirline2);
            holder.ivAirline3 = view.findViewById(R.id.ivAirline3);
            holder.ivAirline4 = view.findViewById(R.id.ivAirline4);

            holder.tvFlights = view.findViewById(R.id.tvFlights);
            holder.tvPrice = view.findViewById(R.id.tvPrice);
            holder.tvTag = view.findViewById(R.id.tvTag);
            holder.tvBaggage = view.findViewById(R.id.tvBaggage);

            holder.lvFlights = view.findViewById(R.id.lvFlights);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        if (!TextUtils.isEmpty(mCitiesList.get(i).tag)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText("" + mCitiesList.get(i).tag);
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }


        StringBuilder strBuilder = new StringBuilder();

        for (int j = 0; j < mCitiesList.get(i).airlineTItles.size(); j++) {

            FlightReturnListPojo.AirlineTitles mAirlineTitles = mCitiesList.get(i).airlineTItles.get(j);

            if (strBuilder.length() > 0) {
                strBuilder.append(", " + mAirlineTitles.name);
            } else {
                strBuilder.append(mAirlineTitles.name);
            }

            holder.ivAirline0.setImageDrawable(null);
            holder.ivAirline1.setImageDrawable(null);
            holder.ivAirline2.setImageDrawable(null);
            holder.ivAirline3.setImageDrawable(null);
            holder.ivAirline4.setImageDrawable(null);

            if (!TextUtils.isEmpty(mAirlineTitles.logo)) {
                if (mCitiesList.get(i).airlineTItles.size() == 1) {
                    Picasso.get().load(mAirlineTitles.logo).into(holder.ivAirline0);
                    holder.ivAirline0.setVisibility(View.VISIBLE);
                } else {
                    holder.ivAirline0.setVisibility(View.GONE);
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
        }

        if (strBuilder != null && strBuilder.length() > 0) {
            holder.tvFlights.setText("" + strBuilder.toString());
        }
        holder.tvPrice.setText(mCitiesList.get(0).currency + " " + Utils.setPrice(mCitiesList.get(i).fares.total.grandTotal));

        FlightReturnDepartureInnerAdapter mFlightReturnDepartureInnerAdapter = new FlightReturnDepartureInnerAdapter(mContext);
        holder.lvFlights.setAdapter(mFlightReturnDepartureInnerAdapter);
        mFlightReturnDepartureInnerAdapter.addAll(mCitiesList.get(i).cities);

        if (!TextUtils.isEmpty(mCitiesList.get(i).cities.get(0).get(0).get(0).baggageInfo)) {
            holder.tvBaggage.setText(mContext.getString(R.string.baggage) + " " + mCitiesList.get(i).cities.get(0).get(0).get(0).baggageInfo);
        } else {
            holder.tvBaggage.setText(mContext.getString(R.string.baggage));
        }

        holder.lvFlights.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                onFlightSelectListener.onFlightSelected(mCitiesList.get(i));
            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFlightSelectListener.onFlightSelected(mCitiesList.get(i));
            }
        });

        return view;
    }

    private class ViewHolder {
        TextView tvFlights, tvPrice, tvTag, tvBaggage;
        ImageView ivAirline0, ivAirline1, ivAirline2, ivAirline3, ivAirline4;
        ListView lvFlights;
    }
}
