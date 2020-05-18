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
import com.otapp.net.model.MovieDetailsPojo;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.LogUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class MovieDateAdapter extends RecyclerView.Adapter<MovieDateAdapter.ViewHolder> {

    private Context mContext;
    List<MovieDetailsPojo.MovieDate> mMovieDateList;
    private OnMovieDateClickListener onMovieDateClickListener;

    public interface OnMovieDateClickListener {
        public void onMovieDateClicked(String date);
    }

    public MovieDateAdapter(Context mContext, List<MovieDetailsPojo.MovieDate> mMovieDateList, OnMovieDateClickListener onMovieDateClickListener) {
        this.mContext = mContext;
        this.mMovieDateList = mMovieDateList;
        this.onMovieDateClickListener = onMovieDateClickListener;
        LogUtils.e("", "mTimeList::" + mMovieDateList.size() + " " + mMovieDateList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie_date, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final MovieDetailsPojo.MovieDate mMovieDate = mMovieDateList.get(i);
        if (mMovieDate != null) {

            if (!TextUtils.isEmpty(mMovieDate.dt)) {
                try {
                    Date mDateTime = DateFormate.sdfMovieDate.parse(mMovieDate.dt);
                    String mMovieDayFormate = DateFormate.sdfDay.format(mDateTime);
                    String mMovieDateFormate = DateFormate.sdfDate.format(mDateTime);
                    if (!TextUtils.isEmpty(mMovieDateFormate) && !TextUtils.isEmpty(mMovieDayFormate)) {
                        holder.tvMovieDate.setText(mMovieDateFormate);
                        holder.tvMovieDay.setText(mMovieDayFormate);
                    } else {
                        holder.tvMovieDate.setText("" + mMovieDate.dt);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    holder.tvMovieDate.setText("" + mMovieDate.dt);
                }
            }


            if (mMovieDate.isSelected) {
                holder.lnrContainer.setBackgroundResource(R.drawable.bg_round_rectangle_white_half);
                holder.tvMovieDate.setTextColor(mContext.getResources().getColor(R.color.blue_movie));
                holder.tvMovieDay.setTextColor(mContext.getResources().getColor(R.color.blue_movie));
            } else {
                holder.lnrContainer.setBackgroundResource(0);
                holder.tvMovieDate.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.tvMovieDay.setTextColor(mContext.getResources().getColor(R.color.white));
            }

            holder.lnrContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < mMovieDateList.size(); j++) {
                        mMovieDateList.get(j).isSelected = false;
                    }
                    mMovieDate.isSelected = true;
                    onMovieDateClickListener.onMovieDateClicked(mMovieDate.dt);
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mMovieDateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMovieDate, tvMovieDay;
        LinearLayout lnrContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMovieDate = itemView.findViewById(R.id.tvMovieDate);
            tvMovieDay = itemView.findViewById(R.id.tvMovieDay);
            lnrContainer = itemView.findViewById(R.id.lnrContainer);
        }
    }
}
