package com.otapp.net.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.otapp.net.R;
import com.otapp.net.model.ThemeParkRideListPojo;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class ParkRidesAdapter extends RecyclerView.Adapter<ParkRidesAdapter.ViewHolder> {

    private Context mContext;
    List<ThemeParkRideListPojo.Ride> mRideList;
    private OnRideClickListener onRideClickListener;

    public interface OnRideClickListener {
        public void onRideClicked(int position);
    }

    public ParkRidesAdapter(Context mContext, List<ThemeParkRideListPojo.Ride> mRideList, OnRideClickListener onRideClickListener) {
        this.mContext = mContext;
        this.mRideList = mRideList;
        this.onRideClickListener = onRideClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_park_rides, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {

        final ThemeParkRideListPojo.Ride mRide = mRideList.get(i);
        if (mRide != null) {
            holder.tvRides.setText("" + mRide.tpName);
            holder.tvPerson.setText("" + mRide.tpSubName);

            if (!TextUtils.isEmpty(mRide.image)) {
                holder.aviProgress.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(mRide.image).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.aviProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.aviProgress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.ivPhoto);
            }

            holder.lnrContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRideClickListener.onRideClicked(i);
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mRideList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRides, tvPerson;
        ImageView ivPhoto;
        LinearLayout lnrContainer;
        AVLoadingIndicatorView aviProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRides = itemView.findViewById(R.id.tvRides);
            tvPerson = itemView.findViewById(R.id.tvPerson);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            lnrContainer = itemView.findViewById(R.id.lnrContainer);
            aviProgress = itemView.findViewById(R.id.aviProgress);
        }
    }
}
