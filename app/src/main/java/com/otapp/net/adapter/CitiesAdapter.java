package com.otapp.net.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.otapp.net.R;
import com.squareup.picasso.Picasso;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    private Context mContext;
//    List<MovieDetailsPojo.MovieDate> mTimeList;
//    private OnMovieDateClickListener onMovieDateClickListener;
//
//    public interface OnMovieDateClickListener {
//        public void onMovieDateClicked(String date);
//    }

    public CitiesAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_cities, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        if (i == 0) {
            Picasso.get().load(R.drawable.bg_parish).into(holder.ivCity);
            holder.tvCity.setText("Paris");
        } else if (i == 1) {
            Picasso.get().load(R.drawable.bg_singapor).into(holder.ivCity);
            holder.tvCity.setText("Singapore");
        } else {
            Picasso.get().load(R.drawable.bg_maldivs).into(holder.ivCity);
            holder.tvCity.setText("Maldives");
        }

//        final MovieDetailsPojo.MovieDate mMovieDate = mTimeList.get(i);
//        if (mMovieDate != null) {
//            holder.tvAirline.setText("" + mMovieDate.dt);
//            if (mMovieDate.isSelected) {
//                holder.tvAirline.setBackgroundResource(R.drawable.bg_button_tab_selected);
//            } else {
//                holder.tvAirline.setBackgroundResource(R.drawable.bg_round_border_gray);
//            }
//
//            holder.tvAirline.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    for (int j = 0; j < mTimeList.size(); j++) {
//                        mTimeList.get(j).isSelected = false;
//                    }
//                    mMovieDate.isSelected = true;
//                    onMovieDateClickListener.onMovieDateClicked(mMovieDate.dt);
//                    notifyDataSetChanged();
//                }
//            });
//        }

    }

    @Override
    public int getItemCount() {
//        return mTimeList.size();
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCity;
        TextView tvCity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCity = itemView.findViewById(R.id.tvCity);
            ivCity = itemView.findViewById(R.id.ivCity);
        }
    }
}
