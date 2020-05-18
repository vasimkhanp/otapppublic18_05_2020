package com.otapp.net.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.fragment.UpcomingMovieTabFragment;
import com.otapp.net.model.MovieListPojo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.MaskTransformation;

public class UpcomingMovieAdapter extends BaseAdapter {

    private Context mContext;
    private UpcomingMovieTabFragment mMovieListFragment;
    private List<MovieListPojo.UpcomingMovie> mUpcomingMoviesList = new ArrayList<>();
    private List<MovieListPojo.UpcomingMovie> mAllMoviesList = new ArrayList<>();
    private Transformation transformation;

    public UpcomingMovieAdapter(Context mContext, UpcomingMovieTabFragment mMovieListFragment) {
        this.mContext = mContext;
        this.mMovieListFragment = mMovieListFragment;
        transformation = new MaskTransformation(mContext, R.drawable.top_rounded_convers_transformation);
    }

    public void addAll(List<MovieListPojo.UpcomingMovie> mTempUpcomingMoviesList) {

        if (mUpcomingMoviesList != null && mUpcomingMoviesList.size() > 0) {
            mUpcomingMoviesList.clear();
            mAllMoviesList.clear();
        }

        if (mTempUpcomingMoviesList != null && mTempUpcomingMoviesList.size() > 0) {
            mUpcomingMoviesList.addAll(mTempUpcomingMoviesList);
            mAllMoviesList.addAll(mTempUpcomingMoviesList);
        }

        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return mUpcomingMoviesList.size();
    }

    @Override
    public Object getItem(int i) {
        return mUpcomingMoviesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_movie, null);
            ViewHolder holder = new ViewHolder();
            holder.ivMovie = view.findViewById(R.id.ivMovie);
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvLanguage = view.findViewById(R.id.tvLanguage);
            holder.tvAction = view.findViewById(R.id.tvAction);
            holder.aviProgress = view.findViewById(R.id.aviProgress);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final MovieListPojo.UpcomingMovie mMovie = mUpcomingMoviesList.get(i);

        if (mMovie != null) {
            holder.tvName.setText("" + Html.fromHtml(mMovie.name));
            holder.tvLanguage.setText(mMovie.language);
            holder.tvAction.setText("" + mMovie.movieGenre + " | " + mMovie.movieDuration);

            if (!TextUtils.isEmpty(mMovie.image)) {
                holder.aviProgress.setVisibility(View.VISIBLE);

//                Glide.with(mContext).load(mMovie.image).listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        holder.aviProgress.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        holder.aviProgress.setVisibility(View.GONE);
//                        return false;
//                    }
//                }).into(holder.ivMovie);

                Picasso.get().load(mMovie.image).transform(transformation).into(holder.ivMovie, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.aviProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.aviProgress.setVisibility(View.GONE);
                    }
                });
            } else {
                holder.aviProgress.setVisibility(View.GONE);
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMovieListFragment.onMovieClicked(mMovie);
            }
        });

        return view;
    }


    private class ViewHolder {
        TextView tvName, tvLanguage, tvAction;
        ImageView ivMovie;
        AVLoadingIndicatorView aviProgress;
    }

    public void filter(String filterText) {
        filterText = filterText.toLowerCase(Locale.getDefault());
        mUpcomingMoviesList.clear();
        if (filterText.length() == 0) {
            mUpcomingMoviesList.addAll(mAllMoviesList);
        } else {
            for (MovieListPojo.UpcomingMovie mMovie : mAllMoviesList) {
                if (Html.fromHtml(mMovie.name).toString().toLowerCase(Locale.getDefault())
                        .contains(filterText) || Html.fromHtml(mMovie.movieCinema).toString().toLowerCase(Locale.getDefault())
                        .contains(filterText)) {
                    mUpcomingMoviesList.add(mMovie);
                }
            }
        }
        notifyDataSetChanged();
    }
}
