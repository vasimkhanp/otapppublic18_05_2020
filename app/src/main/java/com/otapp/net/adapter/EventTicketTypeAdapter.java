package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.EventDetailsPojo;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EventTicketTypeAdapter extends BaseAdapter {

    private Context mContext;
    //    private List<EventDetailsPojo.TicketType> mTicketTypeList;
    private OnEventTicketTypeListener onEventTicketTypeListener;
    private List<EventDetailsPojo.TicketType> mTicketTypeList = new ArrayList<>();

    public EventTicketTypeAdapter(Context mContext) {
        this.mContext = mContext;
    }

//    public EventTicketTypeAdapter(Context mContext, List<EventDetailsPojo.TicketType> mTicketTypeList, OnEventTicketTypeListener onEventTicketTypeListener) {
//        this.mContext = mContext;
//        this.mTicketTypeList = mTicketTypeList;
//        this.onEventTicketTypeListener = onEventTicketTypeListener;
//    }

    public void setListener(OnEventTicketTypeListener onEventTicketTypeListener) {
        this.onEventTicketTypeListener = onEventTicketTypeListener;
    }

    public interface OnEventTicketTypeListener {
        public void onPlusClicked(int position);

        public void onMinusClicked(int position);

        public void onAddClicked(int position);
    }

    public void addAll(List<EventDetailsPojo.TicketType> mTempTicketTypeList) {

        if (mTicketTypeList != null && mTicketTypeList.size() > 0) {
            mTicketTypeList.clear();
        }

        if (mTempTicketTypeList != null && mTempTicketTypeList.size() > 0) {
            mTicketTypeList.addAll(mTempTicketTypeList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTicketTypeList.size();
    }

    @Override
    public Object getItem(int i) {
        return mTicketTypeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_event_ticket_type, null);
            ViewHolder holder = new ViewHolder();
            holder.tvType = view.findViewById(R.id.tvType);
            holder.tvPrice = view.findViewById(R.id.tvPrice);
            holder.tvAdd = view.findViewById(R.id.tvAdd);
            holder.tvMinus = view.findViewById(R.id.tvMinus);
            holder.tvQuantity = view.findViewById(R.id.tvQuantity);
            holder.tvPlus = view.findViewById(R.id.tvPlus);
            holder.lnrQuantity = view.findViewById(R.id.lnrQuantity);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final EventDetailsPojo.TicketType mTicketType = mTicketTypeList.get(i);
        if (mTicketType != null) {

            holder.tvType.setText(mTicketType.ticketType+" "+mTicketType.floorName);
            holder.tvPrice.setText(mTicketType.ticketCurrency + " " + Utils.setPrice(Float.parseFloat(mTicketType.ticketAmount)));
//            holder.tvTicketTitle.setText("" + mTicketType.ticketCount);

            if (mTicketType.isSelected) {
                holder.lnrQuantity.setVisibility(View.VISIBLE);
                holder.tvAdd.setVisibility(View.GONE);
                holder.tvQuantity.setText("" + mTicketType.ticketCount);
            } else {
                holder.lnrQuantity.setVisibility(View.GONE);
                holder.tvAdd.setVisibility(View.VISIBLE);
            }

            holder.tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    holder.lnrQuantity.setVisibility(View.VISIBLE);
//                    holder.tvAdd.setVisibility(View.GONE);
//                    mTicketType.isSelected = true;
//                    notifyDataSetChanged();
//                    holder.tvTicketTitle.setText("" + mTicketType.ticketCount);
                    onEventTicketTypeListener.onAddClicked(i);
                }
            });

            holder.tvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (mTicketType.ticketCount < mTicketType.ticketAvailable) {
//                        mTicketType.ticketCount++;
//                        holder.tvTicketTitle.setText("" + mTicketType.ticketCount);
//                    }
                    onEventTicketTypeListener.onPlusClicked(i);
                }
            });

            holder.tvMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEventTicketTypeListener.onMinusClicked(i);
//                    if (mTicketType.ticketCount > 1) {
//                        mTicketType.ticketCount--;
//                        holder.tvTicketTitle.setText("" + mTicketType.ticketCount);
//                    } else {
//                        mTicketType.ticketCount = 0;
//                        mTicketType.isSelected = false;
//                        notifyDataSetChanged();
//                    }
                }
            });
        }

        return view;
    }

    private class ViewHolder {
        TextView tvType, tvPrice, tvAdd, tvMinus, tvQuantity, tvPlus;
        LinearLayout lnrQuantity;
    }
}
