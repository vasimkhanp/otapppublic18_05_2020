package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddReviewPojo {

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
        @SerializedName("percentRating")
        @Expose
        public float percentRating = 0;
        @SerializedName("total_reviews")
        @Expose
        public int totalReviews = 0;

    }

}
