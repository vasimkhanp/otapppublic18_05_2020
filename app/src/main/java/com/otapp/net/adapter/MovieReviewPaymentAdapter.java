package com.otapp.net.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.MovieSuccessPojo;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewPaymentAdapter extends BaseAdapter {

    private Context mContext;
    private String mCurrency;
    private List<MovieSuccessPojo.Fare> mSummaryList = new ArrayList<>();

    public MovieReviewPaymentAdapter(Context mContext, String mCurrency) {
        this.mContext = mContext;
        this.mCurrency = mCurrency;
    }


    public void addAll(List<MovieSuccessPojo.Fare> mTempSummaryList) {

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
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie_payment, null);
            ViewHolder holder = new ViewHolder();
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvPrice = view.findViewById(R.id.tvPrice);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        final MovieSuccessPojo.Fare mSummary = mSummaryList.get(i);

        if (mSummary != null) {

            if (!TextUtils.isEmpty(mSummary.quantity)) {
                holder.tvName.setText(mSummary.lable + "\n" + mSummary.quantity);
            } else {
                holder.tvName.setText(mSummary.lable);
            }

            holder.tvPrice.setText(mCurrency + " " + Utils.setPrice(mSummary.price));

        }

        return view;
    }


    private class ViewHolder {
        TextView tvName, tvPrice;
    }
}
