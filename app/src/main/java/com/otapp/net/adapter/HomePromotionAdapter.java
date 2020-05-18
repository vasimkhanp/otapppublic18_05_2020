package com.otapp.net.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class HomePromotionAdapter extends RecyclerView.Adapter<HomePromotionAdapter.ViewHolder> {

    private Context mContext;
    List<CountryCodePojo.Ad5> mPromotionsList = new ArrayList<>();
    private OnOfferClickListener onOfferClickListener;

    public interface OnOfferClickListener {
        public void onOfferClicked(String module);
    }

    public HomePromotionAdapter(Context mContext, OnOfferClickListener onOfferClickListener) {
        this.mContext = mContext;
        this.onOfferClickListener = onOfferClickListener;
    }

    public void addAll(List<CountryCodePojo.Ad5> mTempPromotionsList) {

        if (mPromotionsList != null && mPromotionsList.size() > 0) {
            mPromotionsList.clear();
        }

        if (mTempPromotionsList != null && mTempPromotionsList.size() > 0) {
            mPromotionsList.addAll(mTempPromotionsList);
        }

        LogUtils.e("", "mPromotionsList::"+mPromotionsList.size());

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_advertise, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.rltMain.getLayoutParams();
        if (i == 0) {
            params.setMarginStart((int) mContext.getResources().getDimension(R.dimen._50sdp));
        } else {
            params.setMarginStart(0);
        }
        holder.rltMain.setLayoutParams(params);

        holder.tvTitle.setText(mPromotionsList.get(i).hdr_txt);
        holder.tvSupport.setText(mPromotionsList.get(i).cntnt_txt);

        holder.tvBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(mPromotionsList.get(i).is_open_in_app)) {

                    if (mPromotionsList.get(i).is_open_in_app.equals("1")) {

                        if (!TextUtils.isEmpty(mPromotionsList.get(i).link)) {
                            onOfferClickListener.onOfferClicked(mPromotionsList.get(i).link);
                        }

                    } else if (mPromotionsList.get(i).is_open_in_app.equals("0")) {

                        if (!TextUtils.isEmpty(mPromotionsList.get(i).link)) {
                            String url = mPromotionsList.get(i).link;
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

    }

    @Override
    public int getItemCount() {
        return mPromotionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //        ImageView ivSupport;
        TextView tvTitle, tvSupport, tvBook;
        RelativeLayout rltMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rltMain = itemView.findViewById(R.id.rltMain);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBook = itemView.findViewById(R.id.tvBook);
            tvSupport = itemView.findViewById(R.id.tvSupport);
//            ivSupport = itemView.findViewById(R.id.ivSupport);
        }
    }
}
