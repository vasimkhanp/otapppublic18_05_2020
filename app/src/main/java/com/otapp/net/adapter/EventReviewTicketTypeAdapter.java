package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.EventSuccessPojo;

import java.util.ArrayList;
import java.util.List;

public class EventReviewTicketTypeAdapter extends BaseAdapter {

    private Context mContext;
    List<EventSuccessPojo.Seats> mEventList = new ArrayList<>();

    public EventReviewTicketTypeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(List<EventSuccessPojo.Seats> mTempEventList) {

        if (mEventList != null && mEventList.size() > 0) {
            mEventList.clear();
        }

        if (mTempEventList != null && mTempEventList.size() > 0) {
            mEventList.addAll(mTempEventList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mEventList.size();
    }

    @Override
    public Object getItem(int i) {
        return mEventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_event_review_ticket_type, null);
            ViewHolder holder = new ViewHolder();
            holder.tvQuantity = view.findViewById(R.id.tvQuantity);
            holder.tvGround = view.findViewById(R.id.tvGround);
            holder.tvSeat = view.findViewById(R.id.tvSeat);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        final EventSuccessPojo.Seats mSeat = mEventList.get(i);

        if (mSeat != null) {
            holder.tvQuantity.setText("" + mSeat.seatscount);
            holder.tvGround.setText("" + mSeat.Floor);
            holder.tvSeat.setText("" + mSeat.TicketType);
        }

        return view;
    }


    private class ViewHolder {
        TextView tvQuantity, tvGround, tvSeat;
    }
}
