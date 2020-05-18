package com.otapp.net.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;

import java.util.ArrayList;
import java.util.List;

public class MovieTermsAdapter extends BaseAdapter {

    private Context mContext;

    List<String> mTermsList = new ArrayList<>();

    public MovieTermsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(List<String> mTempEventList) {

        if (mTermsList != null && mTermsList.size() > 0) {
            mTermsList.clear();
        }

        if (mTempEventList != null && mTempEventList.size() > 0) {
            mTermsList.addAll(mTempEventList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTermsList.size();
    }

    @Override
    public Object getItem(int i) {
        return mTermsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie_terms, null);
            ViewHolder holder = new ViewHolder();
            holder.tvIndex = view.findViewById(R.id.tvIndex);
            holder.tvTerms = view.findViewById(R.id.tvTerms);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final String mTerms = mTermsList.get(i);

        if (!TextUtils.isEmpty(mTerms)) {
            holder.tvIndex.setText("" + (i + 1));
            holder.tvTerms.setText("" + mTerms);
        }

        return view;
    }


    private class ViewHolder {
        TextView tvIndex, tvTerms;
    }
}
