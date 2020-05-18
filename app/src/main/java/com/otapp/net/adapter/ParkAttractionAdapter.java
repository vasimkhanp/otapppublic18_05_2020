package com.otapp.net.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.otapp.net.R;
import com.otapp.net.model.ThemeParkDetailsPojo;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class ParkAttractionAdapter extends RecyclerView.Adapter<ParkAttractionAdapter.ViewHolder> {

    private Context mContext;
    private String tpName;
    List<ThemeParkDetailsPojo.MajorAttraction> mAttractionList;


    public ParkAttractionAdapter(Context mContext, List<ThemeParkDetailsPojo.MajorAttraction> mAttractionList, String tpName) {
        this.mContext = mContext;
        this.tpName = tpName;
        this.mAttractionList = mAttractionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_park_attraction, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {

        final ThemeParkDetailsPojo.MajorAttraction mMajorAttraction = mAttractionList.get(i);

        holder.tvName.setText("" + mMajorAttraction.title);

        if (!TextUtils.isEmpty(mMajorAttraction.image)) {
            holder.aviProgress.setVisibility(View.VISIBLE);

            Glide.with(mContext).load(mMajorAttraction.image).listener(new RequestListener<Drawable>() {
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
            }).into(holder.ivAttraction);

        }

        holder.ivAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showParkGalleryDialog(mAttractionList, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAttractionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivAttraction;
        AVLoadingIndicatorView aviProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            ivAttraction = itemView.findViewById(R.id.ivAttraction);
            aviProgress = itemView.findViewById(R.id.aviProgress);
        }
    }

    private void showParkGalleryDialog(List<ThemeParkDetailsPojo.MajorAttraction> mGalleryList, int positon) {

        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_park_gallery);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);


        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        TextView tvBack = (TextView) dialog.findViewById(R.id.tvBack);
        ImageView ivPhoto = (ImageView) dialog.findViewById(R.id.ivPhoto);
        final AVLoadingIndicatorView aviProgress = (AVLoadingIndicatorView) dialog.findViewById(R.id.aviProgress);

//        tvIndex.setText("" + tpName);
        tvTitle.setText("" + mGalleryList.get(positon).title);


        if (!TextUtils.isEmpty(mGalleryList.get(positon).image)) {
            aviProgress.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mGalleryList.get(positon).image).listener(new RequestListener<Drawable>() {
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
            }).into(ivPhoto);

        }

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        dialog.show();


    }
}
