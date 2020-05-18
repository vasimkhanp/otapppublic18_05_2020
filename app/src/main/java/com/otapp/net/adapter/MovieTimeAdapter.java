package com.otapp.net.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.MovieTheaterListPojo;
import com.otapp.net.utils.DateFormate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class MovieTimeAdapter extends BaseAdapter {

    private Context mContext;
    private List<MovieTheaterListPojo.ScreenTime> mTimeList;
    private OnMovieTimeSelected onMovieTimeSelected;

    public MovieTimeAdapter(Context mContext, List<MovieTheaterListPojo.ScreenTime> mTimeList, OnMovieTimeSelected onMovieTimeSelected) {
        this.mContext = mContext;
        this.mTimeList = mTimeList;
        this.onMovieTimeSelected = onMovieTimeSelected;
    }

    public interface OnMovieTimeSelected {
        public void onMovieTimeSelected(int mScreenTimePos, MovieTheaterListPojo.ScreenTime mScreenTime);
    }


//    public void addAll(List<FoodListPojo.Combo> mTempComboList) {
//
//        if (mTimeList != null && mTimeList.size() > 0) {
//            mTimeList.clear();
//        }
//
//        if (mTempComboList != null && mTempComboList.size() > 0) {
//            mTimeList.addAll(mTempComboList);
//        }
//
//        notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
        return mTimeList.size();
    }

    @Override
    public Object getItem(int i) {
        return mTimeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie_time, null);
            ViewHolder holder = new ViewHolder();
            holder.tvMovieDate = view.findViewById(R.id.tvMovieDate);
            holder.tvMovieFormate = view.findViewById(R.id.tvMovieFormate);
            holder.lnrContainer = view.findViewById(R.id.lnrContainer);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        final MovieTheaterListPojo.ScreenTime mScreenTime = mTimeList.get(i);

        if (mScreenTime != null) {
            if (!TextUtils.isEmpty(mScreenTime.vdt)) {
                try {
                    Date mDateTime = DateFormate.sdfEvent24Time.parse(mScreenTime.vdt);
                    String mMovieTimeFormate = DateFormate.sdfEvent12Time.format(mDateTime);
                    if (!TextUtils.isEmpty(mMovieTimeFormate)) {
                        holder.tvMovieDate.setText(mMovieTimeFormate);
                    } else {
                        holder.tvMovieDate.setText("" + mScreenTime.vdt);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    holder.tvMovieDate.setText("" + mScreenTime.vdt);
                }
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


            holder.tvMovieDate.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }


    private class ViewHolder {
        TextView tvMovieDate, tvMovieFormate;
        LinearLayout lnrContainer;
    }
}
