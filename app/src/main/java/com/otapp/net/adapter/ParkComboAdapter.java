package com.otapp.net.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.ThemeParkRideListPojo;

import java.util.List;

public class ParkComboAdapter extends RecyclerView.Adapter<ParkComboAdapter.ViewHolder> {

    private Context mContext;
    List<ThemeParkRideListPojo.Combo> mComboList;
    private OnParkComboClickListener onParkComboClickListener;

    public interface OnParkComboClickListener {
        public void onComboClicked(int position);
    }

    public ParkComboAdapter(Context mContext, List<ThemeParkRideListPojo.Combo> mComboList, OnParkComboClickListener onParkComboClickListener) {
        this.mContext = mContext;
        this.mComboList = mComboList;
        this.onParkComboClickListener = onParkComboClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_park_combo, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {

        final ThemeParkRideListPojo.Combo mCombo = mComboList.get(i);
        if (mCombo != null) {
            holder.tvTitle.setText("" + mCombo.comboName);
            holder.tvDescription.setText("" + mCombo.description);
            if (mCombo.includes != null && mCombo.includes.size() > 0) {

                StringBuilder strIncludBuilder = new StringBuilder();
                strIncludBuilder.append("" + mContext.getString(R.string.includes) + "\n");
                for (int j = 0; j < mCombo.includes.size(); j++) {
                    strIncludBuilder.append("" + mCombo.includes.get(j) + "\n");
                }
                holder.tvInclude.setText("" + strIncludBuilder.toString());
            }


//            if (!TextUtils.isEmpty(mCombo.image)) {
////            holder.aviProgress.setVisibility(View.VISIBLE);
//                Glide.with(mContext).load(mCombo.image).listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
////                        holder.aviProgress.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
////                        holder.aviProgress.setVisibility(View.GONE);
//                        return false;
//                    }
//                }).into(holder.ivPhoto);
//            }

            holder.lnrContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onParkComboClickListener.onComboClicked(i);
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mComboList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDescription, tvInclude;
        ImageView ivPhoto;
        LinearLayout lnrContainer;
//        AVLoadingIndicatorView aviProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvInclude = itemView.findViewById(R.id.tvInclude);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            lnrContainer = itemView.findViewById(R.id.lnrContainer);
//            aviProgress = itemView.findViewById(R.id.aviProgress);
        }
    }
}
