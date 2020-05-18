package com.otapp.net.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.FoodListPojo;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieFoodAdapter extends BaseAdapter {

    private Context mContext;
    private List<FoodListPojo.Combo> mComboList;
    private OnFoodListener onFoodListener;

    public MovieFoodAdapter(Context mContext, List<FoodListPojo.Combo> mComboList) {
        this.mContext = mContext;
        this.mComboList = mComboList;
    }

    public interface OnFoodListener {

        public void onPlusClicked(int position);

        public void onMinusClicked(int position);

        public void onAddClicked(int position);
    }

    public void setListener(OnFoodListener onFoodListener) {
        this.onFoodListener = onFoodListener;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_movie_food_new, null);
            ViewHolder holder = new ViewHolder();
            holder.ivFood = view.findViewById(R.id.ivFood);
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvPrice = view.findViewById(R.id.tvPrice);
            holder.tvAdd = view.findViewById(R.id.tvAdd);
            holder.tvMinus = view.findViewById(R.id.tvMinus);
            holder.tvQuantity = view.findViewById(R.id.tvQuantity);
            holder.tvPlus = view.findViewById(R.id.tvPlus);
            holder.rltQuantity = view.findViewById(R.id.rltQuantity);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        final FoodListPojo.Combo mFoodCombo = mComboList.get(i);

        if (mFoodCombo != null) {
            holder.tvName.setText("" + mFoodCombo.name);
            holder.tvPrice.setText(mFoodCombo.currency + " " + Utils.setPrice(mFoodCombo.price));

            if (!TextUtils.isEmpty(mFoodCombo.image)) {
                Picasso.get().load(mFoodCombo.image).into(holder.ivFood);
            }

            if (mFoodCombo.isSelected) {
                holder.rltQuantity.setVisibility(View.VISIBLE);
                holder.tvAdd.setVisibility(View.GONE);
                holder.tvQuantity.setText("" + mFoodCombo.foodCount);
            } else {
                holder.rltQuantity.setVisibility(View.GONE);
                holder.tvAdd.setVisibility(View.VISIBLE);
            }

            holder.tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFoodListener.onAddClicked(i);
                }
            });

            holder.tvPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFoodListener.onPlusClicked(i);
                }
            });

            holder.tvMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFoodListener.onMinusClicked(i);
                }
            });
        }

        return view;
    }


    private class ViewHolder {
        TextView tvName, tvPrice, tvAdd, tvMinus, tvQuantity, tvPlus;
        ImageView ivFood;
        LinearLayout rltQuantity;
    }
}
