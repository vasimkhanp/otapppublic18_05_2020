package com.otapp.net.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.model.ReviewListPojo;

import java.util.ArrayList;
import java.util.List;

public class UserReviewAdapter extends BaseAdapter {

    private Context mContext;

    List<ReviewListPojo.UserReview> mReviewList = new ArrayList<>();

    public UserReviewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(List<ReviewListPojo.UserReview> mTempEventList) {

        if (mReviewList != null && mReviewList.size() > 0) {
            mReviewList.clear();
        }

        if (mTempEventList != null && mTempEventList.size() > 0) {
            mReviewList.addAll(mTempEventList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mReviewList.size();
    }

    @Override
    public Object getItem(int i) {
        return mReviewList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie_review, null);
            ViewHolder holder = new ViewHolder();
            holder.tvCommentTitle = view.findViewById(R.id.tvCommentTitle);
            holder.tvUserComment = view.findViewById(R.id.tvUserComment);
            holder.tvUserName = view.findViewById(R.id.tvUserName);
            holder.tvRate = view.findViewById(R.id.tvRate);
            holder.rbRating = view.findViewById(R.id.rbRating);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final ReviewListPojo.UserReview mUserReview = mReviewList.get(i);

        if (mUserReview != null) {
            holder.tvCommentTitle.setText("" + mUserReview.title);
            holder.tvUserComment.setText("" + Html.fromHtml(mUserReview.userComment));
            holder.tvUserName.setText("" + Html.fromHtml(mUserReview.userName));
            holder.tvRate.setText(""+(int)(mUserReview.rating * 20) +"%");
            holder.rbRating.setRating(mUserReview.rating);


        }

        return view;
    }


    private class ViewHolder {
        TextView tvCommentTitle, tvUserComment, tvUserName, tvRate;
        RatingBar rbRating;
    }
}
