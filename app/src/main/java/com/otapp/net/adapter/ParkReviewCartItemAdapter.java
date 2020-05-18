package com.otapp.net.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ThemeParkSuccessPojo;
import com.otapp.net.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ParkReviewCartItemAdapter extends BaseAdapter {

    private Context mContext;

    List<ThemeParkSuccessPojo.ThemeParkSuccess> mCartList = new ArrayList<>();

    public ParkReviewCartItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(List<ThemeParkSuccessPojo.ThemeParkSuccess> mTempCartList) {

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
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_park_review_cart, null);
            ViewHolder holder = new ViewHolder();

            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvAdult = view.findViewById(R.id.tvAdult);
            holder.tvAdultPrice = view.findViewById(R.id.tvAdultPrice);
            holder.tvChild = view.findViewById(R.id.tvChild);
            holder.tvChildPrice = view.findViewById(R.id.tvChildPrice);
            holder.tvStudent = view.findViewById(R.id.tvStudent);
            holder.tvStudentPrice = view.findViewById(R.id.tvStudentPrice);
            holder.lnrAdult = view.findViewById(R.id.lnrAdult);
            holder.lnrChild = view.findViewById(R.id.lnrChild);
            holder.lnrStudent = view.findViewById(R.id.lnrStudent);

            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final ThemeParkSuccessPojo.ThemeParkSuccess mCartItem = mCartList.get(i);

        if (mCartItem != null) {
            holder.tvName.setText("" + mCartItem.tpName.substring(0, 1).toUpperCase() +  mCartItem.tpName.substring(1));

            if (mCartItem.adultTicketCount > 0) {
                holder.lnrAdult.setVisibility(View.VISIBLE);
                holder.tvAdult.setText(mCartItem.adultTicketCount + " " + mContext.getText(R.string.ticket) + " (" + mContext.getString(R.string.adult) + ") x " + mCartItem.adultPrice);
                holder.tvAdultPrice.setText(mCartItem.currency + " " + (mCartItem.adultTicketCount * mCartItem.adultPrice));
            } else {
                holder.lnrAdult.setVisibility(View.GONE);
            }

            if (mCartItem.childTicketCount > 0) {
                holder.lnrChild.setVisibility(View.VISIBLE);
                holder.tvChild.setText(mCartItem.childTicketCount + " " + mContext.getText(R.string.ticket) + " (" + mContext.getString(R.string.child) + ") x " + mCartItem.childPrice);
                holder.tvChildPrice.setText(mCartItem.currency + " " + (mCartItem.childTicketCount * mCartItem.childPrice));
            } else {
                holder.lnrChild.setVisibility(View.GONE);
            }

            if (mCartItem.studTicketCount > 0) {
                holder.lnrStudent.setVisibility(View.VISIBLE);
                holder.tvStudent.setText(mCartItem.studTicketCount + " " + mContext.getText(R.string.ticket) + " (" + mContext.getString(R.string.student) + ") x " + mCartItem.studPrice);
                holder.tvStudentPrice.setText(mCartItem.currency + " " + (mCartItem.studTicketCount * mCartItem.studPrice));
            } else {
                holder.lnrStudent.setVisibility(View.GONE);
            }

        }


        return view;
    }


    private class ViewHolder {
        TextView tvName, tvAdult, tvAdultPrice, tvChild, tvChildPrice, tvStudent, tvStudentPrice;
        LinearLayout lnrAdult, lnrChild, lnrStudent;
    }
}
