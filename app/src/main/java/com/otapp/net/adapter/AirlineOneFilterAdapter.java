package com.otapp.net.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class    AirlineOneFilterAdapter extends RecyclerView.Adapter<AirlineOneFilterAdapter.ViewHolder> {

    private Context mContext;
    List<FlightOneListPojo.Airlines> mAirlineList = new ArrayList<>();
    private OnAirlineClickListener onAirlineClickListener;

    public interface OnAirlineClickListener {
        public void onAirlineClicked(List<FlightOneListPojo.Airlines> mAirlistList);
    }

    public AirlineOneFilterAdapter(Context mContext, OnAirlineClickListener onAirlineClickListener) {
        this.mContext = mContext;
        this.onAirlineClickListener = onAirlineClickListener;
    }

    public void addAll(List<FlightOneListPojo.Airlines> mTempAirlineList) {

        if (mAirlineList != null && mAirlineList.size() > 0) {
            mAirlineList.clear();
        }



        if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
            FlightOneListPojo.Airlines mAirlines = new FlightOneListPojo().new Airlines();
          /*  mAirlines.code = "NS0";
            mAirlines.name = mContext.getString(R.string.non_stop);
            mAirlineList.add(0, mAirlines);*/
//            mAirlineList.add()
            mAirlineList.addAll(mTempAirlineList);
        }

        notifyDataSetChanged();

    }

    public void nonStopSelected() {

        if (mAirlineList != null && mAirlineList.size() > 0) {
            for (int i = 0; i < mAirlineList.size(); i++) {
                if (!mAirlineList.get(i).code.equalsIgnoreCase("NS0")) {
                    mAirlineList.get(i).isSelected = false;
                }
            }
            notifyDataSetChanged();
        }
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
        LogUtils.e("", i+" "+mAirlineList.get(i).name+" "+mAirlineList.get(i).isSelected);

        if (mAirlineList.get(i).isSelected) {
//            holder.tvAirline.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bg_dot_white, 0, 0);
            holder.tvLine.setVisibility(View.VISIBLE);
        } else {
//            holder.tvAirline.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bg_dot_transparent, 0, 0);
            holder.tvLine.setVisibility(View.INVISIBLE);
        }

        holder.lnrContainer.setOnClickListener(new View.OnClickListener() {
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

        TextView tvAirline, tvLine;
        LinearLayout lnrContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAirline = itemView.findViewById(R.id.tvAirline);
            tvLine = itemView.findViewById(R.id.tvLine);
            lnrContainer = itemView.findViewById(R.id.lnrContainer);
        }
    }
}
