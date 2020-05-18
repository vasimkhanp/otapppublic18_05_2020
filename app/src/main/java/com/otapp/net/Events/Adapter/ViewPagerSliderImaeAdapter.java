package com.otapp.net.Events.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.otapp.net.R;
import com.otapp.net.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class ViewPagerSliderImaeAdapter extends PagerAdapter {
    Context context;
    List<String> sliderImageList;
    LayoutInflater mLayoutInflater;

    public ViewPagerSliderImaeAdapter(Context context, List<String> sliderImageList) {
        this.context = context;
        this.sliderImageList = sliderImageList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sliderImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.layout_slide_pager_image, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.ivEvent);
        AVLoadingIndicatorView avLoadingIndicatorView = itemView.findViewById(R.id.aviProgress);

        Log.d("Log","image list size = "+sliderImageList.size());
       avLoadingIndicatorView.setVisibility(View.VISIBLE);
        Glide.with(context).load(sliderImageList.get(position)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                imageView.setImageResource(R.drawable.bg_no_event_img);
                avLoadingIndicatorView.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                return false;

            }
        }).into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
