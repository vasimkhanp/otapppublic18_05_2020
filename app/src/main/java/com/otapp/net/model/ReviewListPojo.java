package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewListPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Review data;

    public class Review {

        @SerializedName("avg_rating")
        @Expose
        public float avgRating = 0;
        @SerializedName("percent_rating")
        @Expose
        public float percentRating = 0;
        @SerializedName("total_reviews")
        @Expose
        public int totalReviews = 0;
        @SerializedName("user_rating")
        @Expose
        public float userRating = 0;

        @SerializedName("user_reviews")
        @Expose
        public List<UserReview> userReviewsList;

    }

    public class UserReview {

        @SerializedName("name")
        @Expose
        public String userName;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("rating")
        @Expose
        public float rating;
        @SerializedName("review")
        @Expose
        public String userComment;

    }

}
