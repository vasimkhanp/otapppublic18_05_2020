package com.otapp.net.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.model.FlightReturnListPojo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PreferedReturnArlineFilterAdapter extends BaseAdapter {
    private Context mContext;
    List<FlightReturnListPojo.Airlines> mAirlineList = new ArrayList<>();
    private PreferedReturnArlineFilterAdapter.OnAirlineClickListener onAirlineClickListener;

    public interface OnAirlineClickListener {
        public void onAirlineClicked(int position, boolean isSelected);
    }

    public PreferedReturnArlineFilterAdapter(Context mContext, PreferedReturnArlineFilterAdapter.OnAirlineClickListener onAirlineClickListener) {
        this.mContext = mContext;
        this.onAirlineClickListener = onAirlineClickListener;
    }

    public void addAll(List<FlightReturnListPojo.Airlines> mTempAirlineList) {

        if (mAirlineList != null && mAirlineList.size() > 0) {
            mAirlineList.clear();
        }

        if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
            mAirlineList.addAll(mTempAirlineList);
        }

        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return mAirlineList.size();
    }

    @Override
    public Object getItem(int i) {
        return mAirlineList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_airline_prefered, viewGroup, false);
            PreferedReturnArlineFilterAdapter.ViewHolder holder = new PreferedReturnArlineFilterAdapter.ViewHolder();
            holder.tvAirline = view.findViewById(R.id.tvAirline);
            holder.ivAirline = view.findViewById(R.id.ivAirline);
            holder.cbFlights = view.findViewById(R.id.cbFlights);
            holder.lnrFlight = view.findViewById(R.id.lnrFlight);
            view.setTag(holder);
        }

        final PreferedReturnArlineFilterAdapter.ViewHolder holder = (PreferedReturnArlineFilterAdapter.ViewHolder) view.getTag();

        final FlightReturnListPojo.Airlines mAirline = mAirlineList.get(i);

        holder.tvAirline.setText("" + mAirline.name);

        holder.cbFlights.setChecked(mAirline.isSelected);

        if (!TextUtils.isEmpty(mAirline.logo)) {
//                holder.aviProgress.setVisibility(View.VISIBLE);

            Picasso.get().load(mAirline.logo).into(holder.ivAirline, new Callback() {
                @Override
                public void onSuccess() {
//                        holder.aviProgress.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
//                        holder.aviProgress.setVisibility(View.GONE);
                }
            });

        } else {
//                holder.aviProgress.setVisibility(View.GONE);
        }


        holder.lnrFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAirline.isSelected = !mAirline.isSelected;
                holder.cbFlights.setChecked(mAirline.isSelected);
                onAirlineClickListener.onAirlineClicked(i, mAirline.isSelected);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public class ViewHolder {

        TextView tvAirline;
        ImageView ivAirline;
        CheckBox cbFlights;
        LinearLayout lnrFlight;

    }
}
