package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.HomeServicePojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeServicesAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<HomeServicePojo> mHomeServicesListPojo = new ArrayList<>();
    private int mSize = 0;

    public HomeServicesAdapter(Context mContext) {
        this.mContext = mContext;
        mSize = (int) mContext.getResources().getDimension(R.dimen._20sdp);
    }

    public void addAll(ArrayList<HomeServicePojo> mTempHomeServicesListPojo) {

        if (mHomeServicesListPojo != null && mHomeServicesListPojo.size() > 0) {
            mHomeServicesListPojo.clear();
        }

        if (mTempHomeServicesListPojo != null && mTempHomeServicesListPojo.size() > 0) {
            mHomeServicesListPojo.addAll(mTempHomeServicesListPojo);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mHomeServicesListPojo.size();
    }

    @Override
    public Object getItem(int i) {
        return mHomeServicesListPojo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_home_services, null);
            ViewHolder holder = new ViewHolder();
            holder.tvService = view.findViewById(R.id.tvService);
            holder.ivIcons = view.findViewById(R.id.ivIcons);
            view.setTag(holder);

        }

        ViewHolder holder = (ViewHolder) view.getTag();

        HomeServicePojo mHomeServicePojo = mHomeServicesListPojo.get(i);
        if (mHomeServicePojo != null) {

            holder.tvService.setText("" + mHomeServicePojo.getService());
            Picasso.get()
                    .load(mHomeServicePojo.getId()).resize(mSize, mSize)
                    .into(holder.ivIcons);
        }

        return view;
    }

    private class ViewHolder {

        TextView tvService;
        ImageView ivIcons;

    }
}
