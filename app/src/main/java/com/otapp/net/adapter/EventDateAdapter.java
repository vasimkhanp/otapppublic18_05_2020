package com.otapp.net.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.EventDatePojo;

import java.util.ArrayList;
import java.util.List;

public class EventDateAdapter extends RecyclerView.Adapter<EventDateAdapter.ViewHolder> {

    private Context mContext;
    List<EventDatePojo> mEventDateList;
    private OnEventDateClickListener onEventDateClickListener;

    public interface OnEventDateClickListener{
        public void onEventDateClicked(String date);
    }

    public EventDateAdapter(Context mContext, ArrayList<EventDatePojo> mEventDateList, OnEventDateClickListener onEventDateClickListener) {
        this.mContext = mContext;
        this.mEventDateList = mEventDateList;
        this.onEventDateClickListener = onEventDateClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_event_date, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final EventDatePojo mEventDate = mEventDateList.get(i);
        if (mEventDate != null) {
            holder.tvEventDate.setText("" + mEventDate.mDate);
            if (mEventDate.isSelected) {
                holder.tvEventDate.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.tvEventDate.setBackgroundResource(R.drawable.bg_round_rectangle_primary);
            } else {
                holder.tvEventDate.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.tvEventDate.setBackgroundResource(R.drawable.bg_round_border_primary);
            }

            holder.tvEventDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < mEventDateList.size(); j++) {
                        mEventDateList.get(j).setSelected(false);
                    }
                    mEventDate.setSelected(true);
                    onEventDateClickListener.onEventDateClicked(mEventDate.mDate);
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mEventDateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEventDate = itemView.findViewById(R.id.tvEventDate);
        }
    }
}
