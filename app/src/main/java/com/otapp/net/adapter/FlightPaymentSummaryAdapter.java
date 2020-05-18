package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightPaymentSummaryPojo;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FlightPaymentSummaryAdapter extends BaseAdapter {

    private Context mContext;
    private String mCurrency;
    private List<FlightPaymentSummaryPojo.Summary> mSummaryList = new ArrayList<>();

    public FlightPaymentSummaryAdapter(Context mContext, String mCurrency) {
        this.mContext = mContext;
        this.mCurrency = mCurrency;
    }


    public void addAll(List<FlightPaymentSummaryPojo.Summary> mTempSummaryList) {

        if (mSummaryList != null && mSummaryList.size() > 0) {
            mSummaryList.clear();
        }

        if (mTempSummaryList != null && mTempSummaryList.size() > 0) {
            mSummaryList.addAll(mTempSummaryList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSummaryList.size();
    }

    @Override
    public Object getItem(int i) {
        return mSummaryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_flight_payment_summary, null);
            ViewHolder holder = new ViewHolder();
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvPrice = view.findViewById(R.id.tvPrice);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        final FlightPaymentSummaryPojo.Summary mSummary = mSummaryList.get(i);

        if (mSummary != null) {

            holder.tvName.setText(mSummary.lable);

            holder.tvPrice.setText(mCurrency + " " + Utils.setPrice(mSummary.price));

        }

        return view;
    }


    private class ViewHolder {
        TextView tvName, tvPrice;
    }
}
