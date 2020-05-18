package com.otapp.net.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.MovieSeat;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MovieSeatAdapter extends BaseAdapter {

    private Context mContext;
    private List<MovieSeat> mMovieSeatList = new ArrayList<>();
    private OnMovieSeatClickListener onMovieSeatClickListener;
    int fontSize = 10;

    public MovieSeatAdapter(Context mContext, OnMovieSeatClickListener onMovieSeatClickListener) {
        this.mContext = mContext;
        this.onMovieSeatClickListener = onMovieSeatClickListener;
        fontSize = (int) mContext.getResources().getDimension(R.dimen._10sdp);
    }

    public void setfontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public interface OnMovieSeatClickListener {
        public void onMoieSelected(int position);
    }

    public void addAll(List<MovieSeat> mTempMovieSeatList) {

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
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_movie_seat, null);
            ViewHolder holder = new ViewHolder();
            holder.tvSeat = view.findViewById(R.id.tvSeat);
//            holder.tvSeatImage = view.findViewById(R.id.tvSeatImage);
            holder.lnrContainer = view.findViewById(R.id.lnrContainer);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        final MovieSeat mMovieSeat = mMovieSeatList.get(i);
        if (mMovieSeat != null) {
//            LogUtils.e("", i + " holder.tvSea::" + mMovieSeat.seat + " " + mMovieSeat.status);

            holder.tvSeat.setTextColor(mContext.getResources().getColor(R.color.gray_93));
            holder.tvSeat.setTextSize(TypedValue.COMPLEX_UNIT_PX,fontSize);

            if (!TextUtils.isEmpty(mMovieSeat.status)) {
//                holder.tvSeat.setText("" + mMovieSeat.seat);
                if (mMovieSeat.status.equalsIgnoreCase(Constants.SEAT_NONE)) {
                    holder.tvSeat.setText("" + mMovieSeat.seat);
//                    holder.tvSeat.setBackgroundResource(0);
//                    holder.tvSeatImage.setVisibility(View.GONE);
                    holder.lnrContainer.setBackgroundResource(0);
                    holder.tvSeat.setVisibility(View.VISIBLE);
                }
//                else if (mMovieSeat.status.equalsIgnoreCase(Constants.SEAT_INVISIBLE)) {
////                    holder.tvSeat.setText("" + mMovieSeat.seat);
////                    holder.tvSeat.setBackgroundResource(0);
//                    holder.tvSeatImage.setVisibility(View.GONE);
//                    holder.tvSeat.setVisibility(View.GONE);
//                }
                else if (mMovieSeat.status.equalsIgnoreCase(Constants.SEAT_AVAILALBLE)) {
//                    LogUtils.e("", i + " mMovieSeat.seat::" + mMovieSeat.seat + " " + mMovieSeat.seat.replaceAll("[^\\d]", ""));
                    holder.tvSeat.setText("" + mMovieSeat.seat.replaceAll("[^\\d]", ""));
//                    holder.tvSeatImage.setVisibility(View.VISIBLE);
                    holder.tvSeat.setVisibility(View.VISIBLE);
//                    holder.tvSeatImage.setBackgroundResource(R.drawable.ic_movie_seat_available);
                    holder.lnrContainer.setBackgroundResource(R.drawable.bg_status_available);
                } else if (mMovieSeat.status.equalsIgnoreCase(Constants.SEAT_SELECTED)) {
                    holder.tvSeat.setText("" + mMovieSeat.seat.replaceAll("[^\\d]", ""));
//                    holder.tvSeatImage.setVisibility(View.VISIBLE);
                    holder.tvSeat.setVisibility(View.VISIBLE);
//                    holder.tvSeatImage.setBackgroundResource(R.drawable.ic_movie_seat_selected);
                    holder.tvSeat.setTextColor(mContext.getResources().getColor(R.color.white));
                    holder.lnrContainer.setBackgroundResource(R.drawable.bg_status_selected);
                } else if (mMovieSeat.status.equalsIgnoreCase(Constants.SEAT_PROCESSED)) {
                    holder.tvSeat.setText("" + mMovieSeat.seat.replaceAll("[^\\d]", ""));
//                    holder.tvSeatImage.setVisibility(View.VISIBLE);
                    holder.tvSeat.setVisibility(View.VISIBLE);
//                    holder.tvSeatImage.setBackgroundResource(R.drawable.ic_movie_seat_processed);
                    holder.tvSeat.setTextColor(mContext.getResources().getColor(R.color.white));
                    holder.lnrContainer.setBackgroundResource(R.drawable.bg_status_process);
                } else if (mMovieSeat.status.equalsIgnoreCase(Constants.SEAT_BOOKED)) {
                    holder.tvSeat.setText("" + mMovieSeat.seat.replaceAll("[^\\d]", ""));
//                    holder.tvSeatImage.setVisibility(View.VISIBLE);
                    holder.tvSeat.setVisibility(View.VISIBLE);
//                    holder.tvSeatImage.setBackgroundResource(R.drawable.ic_movie_seat_booked);
                    holder.lnrContainer.setBackgroundResource(R.drawable.bg_status_booked);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(mMovieSeat.status) &&
                                (mMovieSeat.status.equalsIgnoreCase(Constants.SEAT_AVAILALBLE) ||
                                        mMovieSeat.status.equalsIgnoreCase(Constants.SEAT_SELECTED))) {
                            onMovieSeatClickListener.onMoieSelected(i);
                        } else if (!TextUtils.isEmpty(mMovieSeat.status) &&
                                (mMovieSeat.status.equalsIgnoreCase(Constants.SEAT_PROCESSED))) {
                            Utils.showToast(mContext, mContext.getString(R.string.msg_seat_processed));
                        } else if (!TextUtils.isEmpty(mMovieSeat.status) &&
                                (mMovieSeat.status.equalsIgnoreCase(Constants.SEAT_BOOKED))) {
                            Utils.showToast(mContext, mContext.getString(R.string.msg_seat_booked));
                        }
                    }
                });


            }
        }


        return view;
    }

    private class ViewHolder {
        TextView tvSeat;
        //                , tvSeatImage;
        LinearLayout lnrContainer;
    }
}
