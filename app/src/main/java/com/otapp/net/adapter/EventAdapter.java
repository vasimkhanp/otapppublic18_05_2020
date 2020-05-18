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
import com.otapp.net.fragment.EventListFragment;
import com.otapp.net.model.EventListPojo;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends BaseAdapter {

    private Context mContext;
    private EventListFragment mEventListFragment;

    List<EventListPojo.Events> mEventList = new ArrayList<>();

    public EventAdapter(Context mContext, EventListFragment mEventListFragment) {
        this.mContext = mContext;
        this.mEventListFragment = mEventListFragment;
    }

    public void addAll(List<EventListPojo.Events> mTempEventList) {

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
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_event, null);
            ViewHolder holder = new ViewHolder();
            holder.ivEvent = view.findViewById(R.id.ivEvent);
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvAddress = view.findViewById(R.id.tvAddress);
            holder.tvGenre = view.findViewById(R.id.tvGenre);
            holder.tvPrice = view.findViewById(R.id.tvPrice);
            holder.aviProgress = view.findViewById(R.id.aviProgress);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final EventListPojo.Events mEvents = mEventList.get(i);

        if (mEvents != null) {
            holder.tvName.setText("" + mEvents.eventName);
            holder.tvAddress.setText("" + mEvents.eventAddress + "," + mEvents.eventCity);
            holder.tvGenre.setText("" + mEvents.eventCategory);
            holder.tvPrice.setText(mEvents.ticketCurrency + " " + Utils.setPrice(mEvents.eventPrice)  + " " + mContext.getString(R.string.onwards));

            if (!TextUtils.isEmpty(mEvents.eventPhoto)) {
                holder.aviProgress.setVisibility(View.VISIBLE);

                Picasso.get().load(mEvents.eventPhoto).into(holder.ivEvent, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.aviProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.aviProgress.setVisibility(View.GONE);
                    }
                });

            } else {
                holder.aviProgress.setVisibility(View.GONE);
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mEventListFragment.onEventClicked(mEvents);
            }
        });

        return view;
    }


    private class ViewHolder {
        TextView tvName, tvAddress, tvGenre, tvPrice;
        ImageView ivEvent;
        AVLoadingIndicatorView aviProgress;
    }
}
