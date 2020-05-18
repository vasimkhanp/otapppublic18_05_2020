package com.otapp.net.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.otapp.net.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class MovieFeaturedAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mCurrentMovies;

    public MovieFeaturedAdapter(Context mContext, List<String> mCurrentMovies) {
        this.mContext = mContext;
        this.mCurrentMovies = mCurrentMovies;
    }

    @Override
    public int getCount() {
        return mCurrentMovies.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.pager_item_featured_movie, null);

        ImageView ivFeaturedMovie = mView.findViewById(R.id.ivFeaturedMovie);
        final AVLoadingIndicatorView aviProgress = mView.findViewById(R.id.aviProgress);

        String mCurrentMovie = mCurrentMovies.get(position);
        if (!TextUtils.isEmpty(mCurrentMovie)) {
            aviProgress.setVisibility(View.VISIBLE);

            Glide.with(mContext).load(mCurrentMovie).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    aviProgress.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    aviProgress.setVisibility(View.GONE);
                    return false;
                }
            }).into(ivFeaturedMovie);

//            Picasso.get().load(mCurrentMovie).into(ivFeaturedMovie, new com.squareup.picasso.Callback() {
//                @Override
//                public void onSuccess() {
//                    aviProgress.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    aviProgress.setVisibility(View.GONE);
//                }
//            });
        }

        container.addView(mView);
        return mView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
