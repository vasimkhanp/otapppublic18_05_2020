package com.otapp.net.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.otapp.net.BaseActivity;
import com.otapp.net.R;
import com.otapp.net.fragment.MovieDateSelectionFragment;
import com.otapp.net.fragment.ServiceFragment;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class MovieAdvertiseAdapter extends PagerAdapter {

    private Context mContext;
    private MovieDateSelectionFragment movieDateSelectionFragment;
    private List<CountryCodePojo.Ad5> mCurrentMovies;

    public MovieAdvertiseAdapter(Context mContext, List<CountryCodePojo.Ad5> mCurrentMovies, MovieDateSelectionFragment movieDateSelectionFragment) {
        this.mContext = mContext;
        this.mCurrentMovies = mCurrentMovies;
        this.movieDateSelectionFragment = movieDateSelectionFragment;
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

        String mCurrentMovie = mCurrentMovies.get(position).image_path;
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

            ivFeaturedMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!TextUtils.isEmpty(mCurrentMovies.get(position).is_open_in_app)) {


                        if (mCurrentMovies.get(position).is_open_in_app.equals("1")) {

                            if (!TextUtils.isEmpty(mCurrentMovies.get(position).link)) {
                                setModule(mCurrentMovies.get(position).link);
                            }

                        } else if (mCurrentMovies.get(position).is_open_in_app.equals("0")) {

                            if (!TextUtils.isEmpty(mCurrentMovies.get(position).link)) {
                                String url = mCurrentMovies.get(position).link;
                                if (!url.startsWith("http")) {
                                    url = "http://" + url;
                                }
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                mContext.startActivity(intent);
                            }

                        }
                    }

                }
            });

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

    private void setModule(String module) {

        if (!TextUtils.isEmpty(module)) {
            int i = 0;
            if (module.equalsIgnoreCase("movie")) {
                i = 0;
            } else if (module.equalsIgnoreCase("themepark")) {
                i = 1;
            } else if (module.equalsIgnoreCase("event")) {
                i = 2;
            } else if (module.equalsIgnoreCase("bus")) {
                i = 3;
            } else if (module.equalsIgnoreCase("flight")) {
                i = 4;
            } else if (module.equalsIgnoreCase("hotels")) {
                i = 5;
            } else if (module.equalsIgnoreCase("ferry")) {
                i = 6;
            } else if (module.equalsIgnoreCase("tours")) {
                i = 7;
            }

            if (!Utils.isInternetConnected(getActivity())) {
                Utils.showToast(getActivity(), mContext.getString(R.string.msg_no_internet));
                return;
            }

            Fragment mFragment = ServiceFragment.newInstance();
            ((ServiceFragment) mFragment).setPosition(i);
            movieDateSelectionFragment.switchFragment(mFragment, ServiceFragment.Tag_ServiceFragment);
        }

    }

    private Context getActivity() {
        return ((BaseActivity) mContext);
    }
}
