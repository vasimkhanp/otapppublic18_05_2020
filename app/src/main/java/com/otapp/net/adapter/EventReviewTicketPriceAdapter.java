package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.EventSuccessPojo;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EventReviewTicketPriceAdapter extends BaseAdapter {

    private Context mContext;
    private String currency = "";
    List<EventSuccessPojo.Fare> mFareList = new ArrayList<>();

    public EventReviewTicketPriceAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(List<EventSuccessPojo.Fare> mTempEventList, String currency) {
        this.currency = currency;

        if (mFareList != null && mFareList.size() > 0) {
            mFareList.clear();
        }

        if (mTempEventList != null && mTempEventList.size() > 0) {
            mFareList.addAll(mTempEventList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFareList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFareList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_event_review_ticket, null);
            ViewHolder holder = new ViewHolder();
            holder.tvTicketTitle = view.findViewById(R.id.tvTicketTitle);
            holder.tvTicketPrice = view.findViewById(R.id.tvTicketPrice);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        final EventSuccessPojo.Fare mSeat = mFareList.get(i);

        if (mSeat != null) {
            holder.tvTicketTitle.setText(mSeat.lable);
            holder.tvTicketPrice.setText(currency + " " + Utils.setPrice(mSeat.price));
        }

        return view;
    }


    private class ViewHolder {
        TextView tvTicketTitle, tvTicketPrice;
    }
}
