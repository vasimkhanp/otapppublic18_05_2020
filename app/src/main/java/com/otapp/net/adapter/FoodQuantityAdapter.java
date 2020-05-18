package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FoodListPojo;
import com.otapp.net.utils.Utils;

import java.util.List;

public class FoodQuantityAdapter extends BaseAdapter {

    private Context mContext;
    private List<FoodListPojo.Combo> mComboList;

    public FoodQuantityAdapter(Context mContext, List<FoodListPojo.Combo> mComboList) {
        this.mContext = mContext;
        this.mComboList = mComboList;
    }


//    public void addAll(List<FoodListPojo.Combo> mTempComboList) {
//
//        if (mComboList != null && mComboList.size() > 0) {
//            mComboList.clear();
//        }
//
//        if (mTempComboList != null && mTempComboList.size() > 0) {
//            mComboList.addAll(mTempComboList);
//        }
//
//        notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
        return mComboList.size();
    }

    @Override
    public Object getItem(int i) {
        return mComboList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie_food, null);
            ViewHolder holder = new ViewHolder();
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvPrice = view.findViewById(R.id.tvPrice);
            holder.tvQuantity = view.findViewById(R.id.tvQuantity);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        final FoodListPojo.Combo mFoodCombo = mComboList.get(i);

        if (mFoodCombo != null) {
            holder.tvName.setText("" + mFoodCombo.name);
            holder.tvQuantity.setText("(" + mFoodCombo.foodCount + ")");
            holder.tvPrice.setText(mFoodCombo.currency + " " + Utils.setPrice(mFoodCombo.price * mFoodCombo.foodCount));

        }

        return view;
    }


    private class ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
    }
}
