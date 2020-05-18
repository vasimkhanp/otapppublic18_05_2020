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

public class AddonsMealSpinAdapter extends ArrayAdapter<FlightOneDetailsPojo.Meal> {

    private Context mContext;
    String currency;
    int resource;
    Spinner spinMeal;
    List<FlightOneDetailsPojo.Meal> mMealList = null;
    private OnMealSelectListener onMealSelectListener;

    public interface OnMealSelectListener {
        public void onMealSelected(int position);
    }

    public AddonsMealSpinAdapter(Context mContext, int resource, List<FlightOneDetailsPojo.Meal> mMeals, String currency, Spinner spinMeal, OnMealSelectListener onMealSelectListener) {
        super(mContext, resource, 0, mMeals);

        this.mContext = mContext;
        this.resource = resource;
        this.currency = currency;
        this.mMealList = mMeals;
        this.spinMeal = spinMeal;
        this.onMealSelectListener = onMealSelectListener;

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
        if (mMealList == null) {
            return 0;
        }
        return mMealList.size();
    }

    @Override
    public FlightOneDetailsPojo.Meal getItem(int i) {
        return mMealList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getDropDownView(final int i, View view, final ViewGroup viewGroup) {

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

        final FlightOneDetailsPojo.Meal mMeal = mMealList.get(i);

        if (mMeal != null) {

            holder.tvTitle.setText("" + mMeal.name.replace(mContext.getString(R.string.optional_bracket), ""));
            holder.tvDetails.setText(currency + " " + mMeal.price);

            if (mMeal.name.equalsIgnoreCase(mContext.getString(R.string.hint_meal))){
                holder.tvDetails.setVisibility(View.GONE);
            }else{
                holder.tvDetails.setVisibility(View.VISIBLE);
            }

            if (mMeal.name.contains("non veg") || mMeal.name.contains("nonveg")) {
                holder.tvIndicator.setBackgroundResource(R.drawable.bg_oval_red_nonveg);
            } else {
                holder.tvIndicator.setBackgroundResource(R.drawable.bg_oval_green_veg);
            }

            if (mMeal.isSelected) {
                holder.cbFlight.setChecked(true);
            } else {
                holder.cbFlight.setChecked(false);
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int j = 0; j < mMealList.size(); j++) {
                    if (j == i) {
                        mMealList.get(j).isSelected = true;
                    } else {
                        mMealList.get(j).isSelected = false;
                    }
                }
                notifyDataSetChanged();

                onMealSelectListener.onMealSelected(i);

                try {
                    Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
                    method.setAccessible(true);
                    method.invoke(spinMeal);

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
        view = View.inflate(mContext, R.layout.spin_addons_meal, null);
        TextView tvAddOn = view.findViewById(R.id.tvAddOn);
        tvAddOn.setText("" + mMealList.get(position).name);
        return view;
    }


    private class ViewHolder {
        TextView tvTitle, tvWeight, tvDetails, tvIndicator;
        CheckBox cbFlight;
    }
}
