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

public class AddonsMealAdapter extends BaseAdapter {

    private Context mContext;
    String currency;
    List<FlightOneDetailsPojo.Meal> mMealList = null;

    public AddonsMealAdapter(Context mContext, List<FlightOneDetailsPojo.Meal> mMealList, String currency) {
        this.mContext = mContext;
        this.currency = currency;
        this.mMealList = mMealList;
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
        if (mMealList == null) {
            return 0;
        }
        return mMealList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMealList.get(i);
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

        final FlightOneDetailsPojo.Meal mMeal = mMealList.get(i);

        if (mMeal != null) {

            holder.tvTitle.setText("" + mMeal.name);
            holder.tvDetails.setText(currency + " " + mMeal.price);
            if (mMeal.name.contains("non veg") || mMeal.name.contains("nonveg")) {
                holder.tvIndicator.setBackgroundResource(R.drawable.bg_oval_red_nonveg);
            } else {
                holder.tvIndicator.setBackgroundResource(R.drawable.bg_oval_green_veg);
            }

            if (mMeal.isSelected) {
                holder.cbFlight.setChecked(true);
            } else {
                holder.cbFlight.setChecked(false);
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int j = 0; j < mMealList.size(); j++) {
                    if (j == i) {
                        mMealList.get(j).isSelected = true;
                    } else {
                        mMealList.get(j).isSelected = false;
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
