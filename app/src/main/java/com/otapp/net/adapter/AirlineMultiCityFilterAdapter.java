package com.otapp.net.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightMultiCityPojo;

import java.util.ArrayList;
import java.util.List;

public class AirlineMultiCityFilterAdapter extends RecyclerView.Adapter<AirlineMultiCityFilterAdapter.ViewHolder> {

    private Context mContext;
    List<FlightMultiCityPojo.Airlines> mAirlineList = new ArrayList<>();
    private OnAirlineClickListener onAirlineClickListener;

    public interface OnAirlineClickListener {
        public void onAirlineClicked(List<FlightMultiCityPojo.Airlines> mAirlistList);
    }

    public AirlineMultiCityFilterAdapter(Context mContext, OnAirlineClickListener onAirlineClickListener) {
        this.mContext = mContext;
        this.onAirlineClickListener = onAirlineClickListener;
    }

    public void addAll(List<FlightMultiCityPojo.Airlines> mTempAirlineList) {

        if (mAirlineList != null && mAirlineList.size() > 0) {
            mAirlineList.clear();
        }

        if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
            mAirlineList.addAll(mTempAirlineList);
        }

        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_airline, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        holder.tvAirline.setText("" + mAirlineList.get(i).name);

        if (mAirlineList.get(i).isSelected) {
            holder.tvAirline.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bg_dot_white, 0, 0);
        } else {
            holder.tvAirline.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bg_dot_transparent, 0, 0);
        }

        holder.tvAirline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAirlineList.get(i).isSelected = !mAirlineList.get(i).isSelected;

                onAirlineClickListener.onAirlineClicked(mAirlineList);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAirlineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvAirline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAirline = itemView.findViewById(R.id.tvAirline);
        }
    }
}
