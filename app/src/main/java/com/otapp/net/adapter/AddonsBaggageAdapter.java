package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightOneDetailsPojo;

import java.util.List;

public class AddonsBaggageAdapter extends BaseAdapter {

    private Context mContext;
    private String currency;
    List<FlightOneDetailsPojo.Baggage> mBaggageList = null;

    public AddonsBaggageAdapter(Context mContext, List<FlightOneDetailsPojo.Baggage> mBaggageList, String currency) {
        this.mContext = mContext;
        this.currency = currency;
        this.mBaggageList = mBaggageList;
    }

//    public void addAll(List<EventListPojo.Events> mTempEventList) {
//
//        if (mBaggageList != null && mBaggageList.size() > 0) {
//            mBaggageList.clear();
//        }
//
//        if (mTempEventList != null && mTempEventList.size() > 0) {
//            mBaggageList.addAll(mTempEventList);
//        }
//
//        notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
        if (mBaggageList == null) {
            return 0;
        }
        return mBaggageList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBaggageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_addons, null);
            ViewHolder holder = new ViewHolder();
            holder.tvTitle = view.findViewById(R.id.tvTitle);
            holder.tvWeight = view.findViewById(R.id.tvWeight);
            holder.tvDetails = view.findViewById(R.id.tvDetails);
            holder.tvIndicator = view.findViewById(R.id.tvIndicator);
            holder.cbFlight = view.findViewById(R.id.cbFlight);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final FlightOneDetailsPojo.Baggage mBaggage = mBaggageList.get(i);

        if (mBaggage != null) {

            holder.tvTitle.setText("" + mBaggage.name);
            holder.tvDetails.setText(currency+" " + mBaggage.price);
            holder.tvWeight.setText("" + mBaggage.code);

            if (mBaggage.isSelected) {
                holder.cbFlight.setChecked(true);
            } else {
                holder.cbFlight.setChecked(false);
            }

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int j = 0; j < mBaggageList.size(); j++) {
                    if (j == i) {
                        mBaggageList.get(j).isSelected = true;
                    } else {
                        mBaggageList.get(j).isSelected = false;
                    }
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }


    private class ViewHolder {
        TextView tvTitle, tvWeight, tvDetails, tvIndicator;
        CheckBox cbFlight;
    }
}
