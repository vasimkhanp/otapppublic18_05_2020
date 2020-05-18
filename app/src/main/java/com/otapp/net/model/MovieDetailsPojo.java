package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Movie movie;

    public class Movie {

        @SerializedName("movie_id")
        @Expose
        public String movieId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("language")
        @Expose
        public String language;
        @SerializedName("movie_genre")
        @Expose
        public String movieGenre;
        @SerializedName("movie_restriction")
        @Expose
        public String movieRestriction;
        @SerializedName("movie_release_date")
        @Expose
        public String movieReleaseDate;
        @SerializedName("movie_duration")
        @Expose
        public String movieDuration;
        @SerializedName("movie_format")
        @Expose
        public String movieFormat;
        @SerializedName("movie_cast")
        @Expose
        public String movieCast;
        @SerializedName("mv_subtitle")
        @Expose
        public String mvSubtitle;
        @SerializedName("movie_sysnops")
        @Expose
        public String movieSysnops;
        @SerializedName("movie_trailer")
        @Expose
        public String movieTrailer;
        @SerializedName("movie_director")
        @Expose
        public String movieDirector;
        @SerializedName("movie_dates")
        @Expose
        public List<MovieDate> movieDates = null;
        @SerializedName("total_user_reviews")
        @Expose
        public int totalUserReviews = 0;
        @SerializedName("total_critics_reviews")
        @Expose
        public int totalCriticsReviews;
        @SerializedName("tax")
        @Expose
        public int tax;
        @SerializedName("ihf")
        @Expose
        public int ihf;
        @SerializedName("is_loggin_user_liked")
        @Expose
        public int isLogginUserLiked = 0;
        @SerializedName("is_user_reviewed")
        @Expose
        public int isUserReviewed = 0;
        @SerializedName("like_count")
        @Expose
        public String likeCount;
        @SerializedName("cnv_fixed_fee")
        @Expose
        public int cnvFixedFee = 0;
        @SerializedName("cnv_per_fee")
        @Expose
        public int cnvPerFee = 0;
        @SerializedName("glass_price")
        @Expose
        public int glassPrice = 0;
        public int glassesQuantity = 0;
        @SerializedName("user_review")
        @Expose
        public UserReview userReview;
        //        @SerializedName("critics_review")
//        @Expose
//        public CriticsReview criticsReview;
        @SerializedName("payment_allowed")
        @Expose
        public ArrayList<PaymentAllowed> paymentAllowed = null;

        public String mUserFirstName = "";
        public String mUserLastName = "";
        public String mUserEmail = "";
        public String mUserCountryCode = "";
        public String mUserPhone = "";
        public boolean isTermsAccepted = false;

    }

//    public class CriticsReview {
//
//        @SerializedName("critics_name")
//        @Expose
//        public String criticsName;
//        @SerializedName("rating")
//        @Expose
//        public float rating;
//        @SerializedName("critics_comment")
//        @Expose
//        public String criticsComment;
//
//    }

    public class PaymentAllowed {

        @SerializedName("payment_id")
        @Expose
        public String paymentId;
        @SerializedName("payment_name")
        @Expose
        public String paymentName;

    }

    public class MovieDate {

        @SerializedName("dt")
        @Expose
        public String dt;
        @SerializedName("vdt")
        @Expose
        public String vdt;
        public boolean isSelected = false;

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
