package com.otapp.net.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FlightOneDetailsPojo;

import java.lang.reflect.Method;
import java.util.List;

public class AddonsBaggageSpinAdapter extends ArrayAdapter<FlightOneDetailsPojo.Baggage> {

    private Context mContext;
    private String currency;
    int resource;
    Spinner spinBaggage;
    List<FlightOneDetailsPojo.Baggage> mBaggageList = null;
    private OnBaggageSelectListener onBaggageSelectListener;

    public interface OnBaggageSelectListener {
        public void onBaggageSelected(int position);
    }

    public AddonsBaggageSpinAdapter(Context mContext,  int resource, List<FlightOneDetailsPojo.Baggage> mBaggages, String currency,
                                    Spinner spinBaggage,  OnBaggageSelectListener onBaggageSelectListener) {
        super(mContext, resource, 0, mBaggages);

        this.mContext = mContext;
        this.currency = currency;
        this.resource = resource;
        this.spinBaggage = spinBaggage;
        this.mBaggageList = mBaggages;
        this.onBaggageSelectListener = onBaggageSelectListener;

    }

//    public void addAll(List<EventListPojo.Events> mTempEventList) {
//
//        if (mBaggageList != null && mBaggageList.size() > 0) {
//            mBaggageList.clear();
//        }
//
//        if (mTempEventList != null && mTempEventList.size() > 0) {
//            mBaggageList.addAll(mTempEventList);
//        }
//
//        notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
        if (mBaggageList == null) {
            return 0;
        }
        return mBaggageList.size();
    }

    @Override
    public FlightOneDetailsPojo.Baggage getItem(int i) {
        return mBaggageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getDropDownView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(resource, null);
            ViewHolder holder = new ViewHolder();
            holder.tvTitle = view.findViewById(R.id.tvTitle);
            holder.tvWeight = view.findViewById(R.id.tvWeight);
            holder.tvDetails = view.findViewById(R.id.tvDetails);
            holder.tvIndicator = view.findViewById(R.id.tvIndicator);
            holder.cbFlight = view.findViewById(R.id.cbFlight);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final FlightOneDetailsPojo.Baggage mBaggage = mBaggageList.get(i);

        if (mBaggage != null) {

            holder.tvTitle.setText("" + mBaggage.name);
            holder.tvDetails.setText(currency + " " + mBaggage.price);
            holder.tvWeight.setText("" + mBaggage.code);

            if (mBaggage.name.equalsIgnoreCase(mContext.getString(R.string.hint_baggage))){
                holder.tvDetails.setVisibility(View.GONE);
            }else{
                holder.tvDetails.setVisibility(View.VISIBLE);
            }

            if (mBaggage.isSelected) {
                holder.cbFlight.setChecked(true);
            } else {
                holder.cbFlight.setChecked(false);
            }

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int j = 0; j < mBaggageList.size(); j++) {
                    if (j == i) {
                        mBaggageList.get(j).isSelected = true;
                    } else {
                        mBaggageList.get(j).isSelected = false;
                    }
                }
                notifyDataSetChanged();
                onBaggageSelectListener.onBaggageSelected(i);

                try {
                    Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
                    method.setAccessible(true);
                    method.invoke(spinBaggage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = View.inflate(mContext, R.layout.spin_addons_baggage, null);
        TextView tvAddOn = view.findViewById(R.id.tvAddOn);
        tvAddOn.setText("" + mBaggageList.get(position).name);
        return view;
    }


    private class ViewHolder {
        TextView tvTitle, tvWeight, tvDetails, tvIndicator;
        CheckBox cbFlight;
    }
}
