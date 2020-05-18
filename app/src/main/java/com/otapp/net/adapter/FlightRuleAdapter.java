package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightOneDetailsPojo;

import java.util.ArrayList;
import java.util.List;

public class FlightRuleAdapter extends BaseAdapter {

    private Context mContext;

    List<FlightOneDetailsPojo.Farerule> mEventList = new ArrayList<>();

    public FlightRuleAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(List<FlightOneDetailsPojo.Farerule> mTempEventList) {

        if (mEventList != null && mEventList.size() > 0) {
            mEventList.clear();
        }

        if (mTempEventList != null && mTempEventList.size() > 0) {
            mEventList.addAll(mTempEventList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mEventList.size();
    }

    @Override
    public Object getItem(int i) {
        return mEventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_flight_rule, null);
            ViewHolder holder = new ViewHolder();
            holder.tvTitle = view.findViewById(R.id.tvTitle);
            holder.tvRule = view.findViewById(R.id.tvRule);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final FlightOneDetailsPojo.Farerule mFarerule = mEventList.get(i);

        if (mFarerule != null) {
            holder.tvTitle.setText("" + mFarerule.title);
            holder.tvTitle.setVisibility(View.GONE);

            holder.tvRule.setText((i + 1) + ". " + mFarerule.description.replace("\n", " ").replace("\n ", " ")
                    .replace("  ", " ").replace("   ", " ").replace("    ", " ").replace("     ", " "));
        }

        return view;
    }


    private class ViewHolder {
        TextView tvTitle, tvRule;
    }
}
