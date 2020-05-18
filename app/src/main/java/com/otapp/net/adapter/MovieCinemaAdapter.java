package com.otapp.net.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.MovieTheaterListPojo;

import java.util.ArrayList;
import java.util.List;

public class MovieCinemaAdapter extends BaseAdapter {

    private Context mContext;
    List<MovieTheaterListPojo.Theater> mMovieTheaterList = new ArrayList<>();
    private OnMovieScreenTimeSelected onMovieScreenTimeSelected;

    public MovieCinemaAdapter(Context mContext, OnMovieScreenTimeSelected onMovieScreenTimeSelected) {
        this.mContext = mContext;
        this.onMovieScreenTimeSelected = onMovieScreenTimeSelected;
    }

    public interface OnMovieScreenTimeSelected {
        public void onMovieScreenTimeSelected(int mTheaterPos, int mScreenTimePos, MovieTheaterListPojo.ScreenTime mScreenTime);
    }

    public void addAll(List<MovieTheaterListPojo.Theater> mTempMovieTheaterList) {

        if (this.mMovieTheaterList != null && this.mMovieTheaterList.size() > 0) {
            this.mMovieTheaterList.clear();
        }

        if (mTempMovieTheaterList != null && mTempMovieTheaterList.size() > 0) {
            this.mMovieTheaterList.addAll(mTempMovieTheaterList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMovieTheaterList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMovieTheaterList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie_date_selection, null);
            ViewHolder holder = new ViewHolder();
            holder.tvCinema = view.findViewById(R.id.tvCinema);
//            holder.gvTimeList = view.findViewById(R.id.gvTimeList);
            holder.rvTimeList = view.findViewById(R.id.rvTimeList);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        final MovieTheaterListPojo.Theater mTheater = mMovieTheaterList.get(i);

        if (mTheater != null) {
            holder.tvCinema.setText("" + mTheater.screenName);

//            MovieTimeAdapter mMovieTimeAdapter = new MovieTimeAdapter(mContext, mTheater.screenTime, new MovieTimeAdapter.OnMovieTimeSelected() {
//                @Override
//                public void onMovieTimeSelected(int mScreenTimePos, MovieTheaterListPojo.ScreenTime mScreenTime) {
//                    onMovieScreenTimeSelected.onMovieScreenTimeSelected(i, mScreenTimePos, mScreenTime);
//                }
//            });
//            holder.gvTimeList.setAdapter(mMovieTimeAdapter);

            MovieTimeInnerAdapter mMovieTimeAdapter = new MovieTimeInnerAdapter(mContext, new MovieTimeInnerAdapter.OnMovieTimeSelected() {
                @Override
                public void onMovieTimeSelected(int mScreenTimePos, MovieTheaterListPojo.ScreenTime mScreenTime) {
                    onMovieScreenTimeSelected.onMovieScreenTimeSelected(i, mScreenTimePos, mScreenTime);
                }
            });
            mMovieTimeAdapter.addAll(mTheater.screenTime);
            holder.rvTimeList.setAdapter(mMovieTimeAdapter);

        }

        return view;
    }

    private class ViewHolder {
        TextView tvCinema;
//        GridView gvTimeList;
        RecyclerView rvTimeList;
    }
}
