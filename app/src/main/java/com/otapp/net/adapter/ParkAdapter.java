package com.otapp.net.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.otapp.net.R;
import com.otapp.net.fragment.ThemeParkTabFragment;
import com.otapp.net.model.ThemeParkPojo;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

//import com.otapp.net.fragment.ThemeParkTab1Fragment;
//import com.otapp.net.fragment.ThemeParkTab2Fragment;
//import com.otapp.net.fragment.ThemeParkTab3Fragment;

public class ParkAdapter extends BaseAdapter {

    private Context mContext;
    private ThemeParkTabFragment mThemeParkTabFragment;
    private List<ThemeParkPojo.Park> mParkList = new ArrayList<>();


    public ParkAdapter(Context mContext, ThemeParkTabFragment mThemeParkTabFragment) {
        this.mContext = mContext;
        this.mThemeParkTabFragment = mThemeParkTabFragment;
    }

    public void addAll(List<ThemeParkPojo.Park> mTempParkList) {

        if (mParkList != null && mParkList.size() > 0) {
            mParkList.clear();
        }

        if (mTempParkList != null && mTempParkList.size() > 0) {
            mParkList.addAll(mTempParkList);
        }

        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return mParkList.size();
    }

    @Override
    public Object getItem(int i) {
        return mParkList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_themepark, null);
            ViewHolder holder = new ViewHolder();
            holder.ivThemePark = view.findViewById(R.id.ivThemePark);
            holder.tvPark = view.findViewById(R.id.tvPark);
            holder.tvPlace = view.findViewById(R.id.tvPlace);
            holder.tvVisit = view.findViewById(R.id.tvVisit);
            holder.tvComingSoon = view.findViewById(R.id.tvComingSoon);
            holder.aviProgress = view.findViewById(R.id.aviProgress);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final ThemeParkPojo.Park mPark = mParkList.get(i);

        if (mPark != null) {

            holder.tvPark.setText("" + mPark.name);
            holder.tvPlace.setText(mPark.city);

            if (!TextUtils.isEmpty(mPark.image)) {
                holder.aviProgress.setVisibility(View.VISIBLE);

                Glide.with(mContext).load(mPark.image).listener(new RequestListener<Drawable>() {
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
                }).into(holder.ivThemePark);

            } else {
                holder.aviProgress.setVisibility(View.GONE);
            }
        }

        if (mPark.availability.equalsIgnoreCase("Coming Soon")) {
            holder.tvComingSoon.setVisibility(View.VISIBLE);
        } else {
            holder.tvComingSoon.setVisibility(View.GONE);
        }

        holder.tvVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mPark.availability.equalsIgnoreCase("Coming Soon")) {
                    mThemeParkTabFragment.onParkClicked(mPark);
//                    if (mThemeParkTabFragment instanceof ThemeParkTab1Fragment) {
//                        ((ThemeParkTab1Fragment) mThemeParkTabFragment).onParkClicked(mPark);
//                    } else if (mThemeParkTabFragment instanceof ThemeParkTab2Fragment) {
//                        ((ThemeParkTab2Fragment) mThemeParkTabFragment).onParkClicked(mPark);
//                    } else if (mThemeParkTabFragment instanceof ThemeParkTab3Fragment) {
//                        ((ThemeParkTab3Fragment) mThemeParkTabFragment).onParkClicked(mPark);
//                    }
                }
            }
        });

        return view;
    }


    private class ViewHolder {
        TextView tvPark, tvPlace, tvVisit, tvComingSoon;
        ImageView ivThemePark;
        AVLoadingIndicatorView aviProgress;
    }
}
