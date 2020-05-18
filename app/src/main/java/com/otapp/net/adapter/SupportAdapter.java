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
import com.otapp.net.model.SupportBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.ViewHolder> {

    private Context mContext;
    ArrayList<SupportBean> mSupportBeanList;
//    List<MovieDetailsPojo.MovieDate> mTimeList;
//    private OnMovieDateClickListener onMovieDateClickListener;
//
//    public interface OnMovieDateClickListener {
//        public void onMovieDateClicked(String date);
//    }

    public SupportAdapter(Context mContext, ArrayList<SupportBean> mSupportBeanList) {
        this.mContext = mContext;
        this.mSupportBeanList = mSupportBeanList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_support, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

//        if (i == 0) {
//            Picasso.get().load(R.drawable.bg_parish).into(holder.ivSupport);
//            holder.tvIndex.setText("Paris");
//        } else if (i == 1) {
//            Picasso.get().load(R.drawable.bg_singapor).into(holder.ivSupport);
//            holder.tvIndex.setText("Singapore");
//        } else {
//            Picasso.get().load(R.drawable.bg_maldivs).into(holder.ivSupport);
//            holder.tvIndex.setText("Maldives");
//        }

        Picasso.get().load(mSupportBeanList.get(i).drawable).into(holder.ivSupport);
        holder.tvTitle.setText("" + mSupportBeanList.get(i).title);
        holder.tvSupport.setText("" + mSupportBeanList.get(i).support);


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
        return mSupportBeanList.size();
//        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSupport;
        TextView tvTitle, tvSupport;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSupport = itemView.findViewById(R.id.tvSupport);
            ivSupport = itemView.findViewById(R.id.ivSupport);
        }
    }
}
