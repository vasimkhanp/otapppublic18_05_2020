package com.otapp.net.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.otapp.net.R;
import com.otapp.net.fragment.MovieDetailsFragment;
import com.otapp.net.fragment.ProfileFragment;
import com.otapp.net.model.RecomendedMoviePojo;
import com.otapp.net.utils.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class RecomendedMovieAdapter extends RecyclerView.Adapter<RecomendedMovieAdapter.ViewHolder> {

    private Context mContext;
    List<RecomendedMoviePojo.RecomendedMovie> mMovieList;
    private OnMovieClickListener onMovieClickListener;

    public interface OnMovieClickListener {
        public void onMovieClicked(int position);
    }

    public RecomendedMovieAdapter(Context mContext, List<RecomendedMoviePojo.RecomendedMovie> mMovieList, OnMovieClickListener onMovieClickListener) {
        this.mContext = mContext;
        this.mMovieList = mMovieList;
        this.onMovieClickListener = onMovieClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_coming_movie, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {

        final RecomendedMoviePojo.RecomendedMovie mMovie = mMovieList.get(i);
        if (mMovie != null) {

            holder.tvName.setText("" + Html.fromHtml(mMovie.name));
            holder.tvLanguage.setText("" + mMovie.language);
            holder.tvAction.setText("" + mMovie.movieGenre + " | " + mMovie.movieDuration);
            holder.tvMoviesID.setText("" + mMovie.movieId);

            if (!TextUtils.isEmpty(mMovie.image)) {
                holder.aviProgress.setVisibility(View.VISIBLE);

                Glide.with(mContext).load(mMovie.image).listener(new RequestListener<Drawable>() {
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
                }).into(holder.ivMovie);

//                Picasso.get().load(mMovie.image).into(holder.ivThemePark, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        holder.aviProgress.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        holder.aviProgress.setVisibility(View.GONE);
//                    }
//                });
            } else {
                holder.aviProgress.setVisibility(View.GONE);
            }

            holder.ivMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onMovieClickListener.onMovieClicked(i);
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvLanguage, tvAction, tvMoviesID;
        ImageView ivMovie;
        AVLoadingIndicatorView aviProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMovie = itemView.findViewById(R.id.ivMovie);
            tvName = itemView.findViewById(R.id.tvName);
            tvLanguage = itemView.findViewById(R.id.tvLanguage);
            tvAction = itemView.findViewById(R.id.tvAction);
            aviProgress = itemView.findViewById(R.id.aviProgress);
            tvMoviesID = itemView.findViewById(R.id.moviesId);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != -1) {
                        onMovieClickListener.onMovieClicked(getAdapterPosition());
                    }
//                    String moviesId=tvMoviesID.getText().toString();
//                    Toast.makeText(mContext, ""+tvMoviesID.getText().toString(), Toast.LENGTH_SHORT).show();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.BNDL_MOVIE_ID,moviesId);
//                    bundle.putString(Constants.BNDL_MOVIE_TYPE, Constants.BNDL_MOVIE_TYPE_CURRENT);
                    //   recommdedmovieId(moviesId,Constants.BNDL_MOVIE_TYPE_CURRENT);
                    /*  mContext.switchFragment(MovieDetailsFragment.newInstance(), MovieDetailsFragment.Tag_MovieDetailsFragment, bundle);*/
                }
            });
        }
    }
}
