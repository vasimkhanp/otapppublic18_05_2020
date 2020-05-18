package com.otapp.net.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.otapp.net.R;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.utils.LogUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeAdsAdapter extends BaseAdapter {

    private Context mContext;

    List<CountryCodePojo.Ad5> mAdvertisementsList = new ArrayList<>();

    private OnAdsClickListener onAdsClickListener;

    public interface OnAdsClickListener {
        public void onAdsClicked(String module);
    }

    public HomeAdsAdapter(Context mContext, OnAdsClickListener onAdsClickListener) {
        this.mContext = mContext;
        this.onAdsClickListener = onAdsClickListener;
    }

    public void addAll(List<CountryCodePojo.Ad5> mTempAdvertisementsList) {

        if (mAdvertisementsList != null && mAdvertisementsList.size() > 0) {
            mAdvertisementsList.clear();
        }

        if (mTempAdvertisementsList != null && mTempAdvertisementsList.size() > 0) {
            mAdvertisementsList.addAll(mTempAdvertisementsList);
        }

        LogUtils.e("", "mAdvertisementsList::"+mAdvertisementsList.size());

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAdvertisementsList.size();
    }

    @Override
    public Object getItem(int i) {
        return mAdvertisementsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_ads, null);
            ViewHolder holder = new ViewHolder();
            holder.ivAds = view.findViewById(R.id.ivAds);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final CountryCodePojo.Ad5 mAd5 = mAdvertisementsList.get(i);

        if (mAd5 != null) {

            LogUtils.e("", "mAdvertisement.image_path::"+mAd5.image_path);
            if (!TextUtils.isEmpty(mAd5.image_path)) {

                Picasso.get().load(mAd5.image_path).into(holder.ivAds, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });

            } else {
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(mAd5.is_open_in_app)) {

                    if (mAd5.is_open_in_app.equals("1")) {

                        if (!TextUtils.isEmpty(mAd5.link)) {
                            onAdsClickListener.onAdsClicked(mAd5.link);
                        }

                    } else if (mAd5.is_open_in_app.equals("0")) {

                        if (!TextUtils.isEmpty(mAd5.link)) {
                            String url = mAd5.link;
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

        return view;
    }


    private class ViewHolder {
        ImageView ivAds;
    }
}
