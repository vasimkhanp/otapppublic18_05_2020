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

public class ParkEventsAdapter extends RecyclerView.Adapter<ParkEventsAdapter.ViewHolder> {

    private Context mContext;
    List<ThemeParkRideListPojo.ServicesAndEvent> mParkEventList;
    private OnParkEventClickListener onParkEventClickListener;

    public interface OnParkEventClickListener{
        public void onParkEventClicked(int position);
    }

    public ParkEventsAdapter(Context mContext, List<ThemeParkRideListPojo.ServicesAndEvent> mParkEventList, OnParkEventClickListener onParkEventClickListener) {
        this.mContext = mContext;
        this.mParkEventList = mParkEventList;
        this.onParkEventClickListener = onParkEventClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_park_service, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {

        final ThemeParkRideListPojo.ServicesAndEvent mParkEvent = mParkEventList.get(i);
        if (mParkEvent != null) {
            holder.tvService.setText("" + mParkEvent.tpName);
            holder.tvPerson.setText("" + mParkEvent.tpSubName);

            if (!TextUtils.isEmpty(mParkEvent.image)) {
                holder.aviProgress.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(mParkEvent.image).listener(new RequestListener<Drawable>() {
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
                    onParkEventClickListener.onParkEventClicked(i);
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mParkEventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvService, tvPerson;
        ImageView ivPhoto;
        LinearLayout lnrContainer;
        AVLoadingIndicatorView aviProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvService = itemView.findViewById(R.id.tvService);
            tvPerson = itemView.findViewById(R.id.tvPerson);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            lnrContainer = itemView.findViewById(R.id.lnrContainer);
            aviProgress = itemView.findViewById(R.id.aviProgress);
        }
    }
}
