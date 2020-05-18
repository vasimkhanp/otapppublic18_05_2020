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
import com.otapp.net.fragment.ThemeParkMyCartFragment;
import com.otapp.net.model.ThemeParkCartListPojo;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class ParkCartItemAdapter extends BaseAdapter {

    private Context mContext;
    private ThemeParkMyCartFragment mThemeParkMyCartFragment;

    List<ThemeParkCartListPojo.CartItem> mCartList = new ArrayList<>();

    public ParkCartItemAdapter(Context mContext, ThemeParkMyCartFragment mThemeParkMyCartFragment) {
        this.mContext = mContext;
        this.mThemeParkMyCartFragment = mThemeParkMyCartFragment;
    }

    public void addAll(List<ThemeParkCartListPojo.CartItem> mTempCartList) {

        if (mCartList != null && mCartList.size() > 0) {
            mCartList.clear();
        }

        if (mTempCartList != null && mTempCartList.size() > 0) {
            mCartList.addAll(mTempCartList);
        }
        LogUtils.e("", "mCartList::" + mCartList.size());

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCartList.size();
    }

    @Override
    public Object getItem(int i) {
        return mCartList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_park_cart, null);
            ViewHolder holder = new ViewHolder();

            holder.ivRide = view.findViewById(R.id.ivRide);
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
            holder.tvDate = view.findViewById(R.id.tvDate);
            holder.tvAdult = view.findViewById(R.id.tvAdult);
            holder.tvAdultPrice = view.findViewById(R.id.tvAdultPrice);
            holder.tvChild = view.findViewById(R.id.tvChild);
            holder.tvChildPrice = view.findViewById(R.id.tvChildPrice);
            holder.tvStudent = view.findViewById(R.id.tvStudent);
            holder.tvStudentPrice = view.findViewById(R.id.tvStudentPrice);
            holder.aviProgress = view.findViewById(R.id.aviProgress);
            holder.lnrAdult = view.findViewById(R.id.lnrAdult);
            holder.lnrChild = view.findViewById(R.id.lnrChild);
            holder.lnrStudent = view.findViewById(R.id.lnrStudent);
            holder.lnrDelete = view.findViewById(R.id.lnrDelete);

            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final ThemeParkCartListPojo.CartItem mCartItem = mCartList.get(i);

        if (mCartItem != null) {
            holder.tvName.setText("" + mCartItem.tpName);
            holder.tvDate.setText("" + mCartItem.date);

//            holder.tvDate.setText("" + mCartItem.eventCategory);
            int mTotal = 0;
            if (mCartItem.adultTicketCount > 0) {
                holder.lnrAdult.setVisibility(View.VISIBLE);
                holder.tvAdult.setText(mCartItem.adultTicketCount + " " + mContext.getText(R.string.ticket) + " (" + mContext.getString(R.string.adult) + ") x " + Utils.setPrice(mCartItem.adultPrice));
                holder.tvAdultPrice.setText(mCartItem.currency + " " + Utils.setPrice(mCartItem.adultTicketCount * mCartItem.adultPrice));
                mTotal = mTotal + (mCartItem.adultTicketCount * mCartItem.adultPrice);
            } else {
                holder.lnrAdult.setVisibility(View.GONE);
            }

            if (mCartItem.childTicketCount > 0) {
                holder.lnrChild.setVisibility(View.VISIBLE);
                holder.tvChild.setText(mCartItem.childTicketCount + " " + mContext.getText(R.string.ticket) + " (" + mContext.getString(R.string.child) + ") x " + Utils.setPrice(mCartItem.childPrice));
                holder.tvChildPrice.setText(mCartItem.currency + " " + Utils.setPrice(mCartItem.childTicketCount * mCartItem.childPrice));
                mTotal = mTotal + (mCartItem.childTicketCount * mCartItem.childPrice);
            } else {
                holder.lnrChild.setVisibility(View.GONE);
            }

            if (mCartItem.studTicketCount > 0) {
                holder.lnrStudent.setVisibility(View.VISIBLE);
                holder.tvStudent.setText(mCartItem.studTicketCount + " " + mContext.getText(R.string.ticket) + " (" + mContext.getString(R.string.student) + ") x " + Utils.setPrice(mCartItem.studPrice));
                holder.tvStudentPrice.setText(mCartItem.currency + " " + Utils.setPrice(mCartItem.studTicketCount * mCartItem.studPrice));
                mTotal = mTotal + (mCartItem.studTicketCount * mCartItem.studPrice);
            } else {
                holder.lnrStudent.setVisibility(View.GONE);
            }


            holder.tvTotalPrice.setText(mCartItem.currency + " " + Utils.setPrice(mTotal));


            if (!TextUtils.isEmpty(mCartItem.image)) {
                holder.aviProgress.setVisibility(View.VISIBLE);

                Picasso.get().load(mCartItem.image).into(holder.ivRide, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.aviProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.aviProgress.setVisibility(View.GONE);
                    }
                });

            } else {
                holder.aviProgress.setVisibility(View.GONE);
            }
        }

        holder.lnrDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mThemeParkMyCartFragment.onDeleteClick(i, mCartItem);
            }
        });

        return view;
    }


    private class ViewHolder {
        TextView tvName, tvTotalPrice, tvDate, tvAdult, tvAdultPrice, tvChild, tvChildPrice, tvStudent, tvStudentPrice;
        ImageView ivRide;
        AVLoadingIndicatorView aviProgress;
        LinearLayout lnrAdult, lnrChild, lnrStudent, lnrDelete;
    }
}
