package com.otapp.net.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.MovieTheaterListPojo;

import java.util.ArrayList;
import java.util.List;

public class MovieTimeInnerAdapter extends RecyclerView.Adapter<MovieTimeInnerAdapter.ViewHolder> {

    private Context mContext;
    List<MovieTheaterListPojo.ScreenTime> mTimeList = new ArrayList<>();
    private OnMovieTimeSelected onMovieTimeSelected;

    public interface OnMovieTimeSelected {
        public void onMovieTimeSelected(int mScreenTimePos, MovieTheaterListPojo.ScreenTime mScreenTime);
    }

    public MovieTimeInnerAdapter(Context mContext, OnMovieTimeSelected onMovieTimeSelected) {
        this.mContext = mContext;
        this.onMovieTimeSelected = onMovieTimeSelected;
    }

    public void addAll(List<MovieTheaterListPojo.ScreenTime> mTempTimeList) {

        if (mTimeList != null && mTimeList.size() > 0) {
            mTimeList.clear();
        }

        if (mTempTimeList != null && mTempTimeList.size() > 0) {
            mTimeList.addAll(mTempTimeList);
        }

        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie_time, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        final MovieTheaterListPojo.ScreenTime mScreenTime = mTimeList.get(i);

        if (!TextUtils.isEmpty(mScreenTime.vdt)) {
//            try {
//                Date mDateTime = DateFormate.sdfEvent24Time.parse(mScreenTime.vdt);
//                String mMovieTimeFormate = DateFormate.sdfEvent12Time.format(mDateTime);
//                if (!TextUtils.isEmpty(mMovieTimeFormate)) {
//                    holder.tvMovieDate.setText(mMovieTimeFormate);
//                } else {
//                    holder.tvMovieDate.setText("" + mScreenTime.vdt);
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
                holder.tvMovieDate.setText("" + mScreenTime.vdt);
//            }
        }

        holder.tvMovieFormate.setText("" + mScreenTime.movie_format);

        if (mScreenTime.show_status.equals("0")) {
            holder.lnrContainer.setBackgroundResource(R.drawable.bg_round_border_gray_c8);
            holder.tvMovieDate.setTextColor(mContext.getResources().getColor(R.color.gray_c9));
            holder.tvMovieFormate.setTextColor(mContext.getResources().getColor(R.color.gray_c9));
        } else {
            if (mScreenTime.isSelected) {
                holder.lnrContainer.setBackgroundResource(R.drawable.bg_round_rectangle_blue_movie);
                holder.tvMovieDate.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.tvMovieFormate.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                holder.lnrContainer.setBackgroundResource(R.drawable.bg_round_border_blue_movie);
                holder.tvMovieDate.setTextColor(mContext.getResources().getColor(R.color.blue_movie));
                holder.tvMovieFormate.setTextColor(mContext.getResources().getColor(R.color.blue_movie));
            }
        }

        holder.lnrContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mScreenTime.show_status.equals("0")) {

                    for (int j = 0; j < mTimeList.size(); j++) {
                        mTimeList.get(j).isSelected = false;
                    }
                    mScreenTime.isSelected = true;
                    onMovieTimeSelected.onMovieTimeSelected(i, mScreenTime);
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTimeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMovieDate, tvMovieFormate;
        LinearLayout lnrContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMovieDate = itemView.findViewById(R.id.tvMovieDate);
            tvMovieFormate = itemView.findViewById(R.id.tvMovieFormate);
            lnrContainer = itemView.findViewById(R.id.lnrContainer);
        }
    }
}
