package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.TotalSeat;

import java.util.ArrayList;
import java.util.List;

public class TotalSeatAdapter extends BaseAdapter {

    private Context mContext;
    private List<TotalSeat> mMovieSeatList = new ArrayList<>();
    private OnMovieSeatClickListener onMovieSeatClickListener;

    public TotalSeatAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public interface OnMovieSeatClickListener {
        public void onMoieSelected(int position);
    }

    public void setListener(OnMovieSeatClickListener onMovieSeatClickListener) {
        this.onMovieSeatClickListener = onMovieSeatClickListener;
    }

    public void addAll(List<TotalSeat> mTempMovieSeatList) {

        if (mMovieSeatList != null && mMovieSeatList.size() > 0) {
            mMovieSeatList.clear();
        }

        if (mTempMovieSeatList != null && mTempMovieSeatList.size() > 0) {
            mMovieSeatList.addAll(mTempMovieSeatList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMovieSeatList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMovieSeatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_movie_total_seat, null);
            ViewHolder holder = new ViewHolder();
            holder.tvSeat = view.findViewById(R.id.tvSeat);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        holder.tvSeat.setText("" + mMovieSeatList.get(i).seat);
        if (mMovieSeatList.get(i).isSelected) {

            holder.tvSeat.setBackgroundResource(R.drawable.bg_round_rectangle_blue_movie);
            holder.tvSeat.setTextColor(mContext.getResources().getColor(R.color.white));

        } else {

            holder.tvSeat.setBackgroundResource(0);
            holder.tvSeat.setTextColor(mContext.getResources().getColor(R.color.gray_56));

        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMovieSeatClickListener.onMoieSelected(i);
            }
        });

        return view;
    }

    private class ViewHolder {
        TextView tvSeat;
    }
}
