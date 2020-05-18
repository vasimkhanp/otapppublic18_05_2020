package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSuccessPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public MovieSuccess data = null;

    public class MovieSuccess {

        @SerializedName("booking_type")
        @Expose
        public String bookingType;
        @SerializedName("booking_id")
        @Expose
        public String bookingId;
        @SerializedName("currency")
        @Expose
        public String currency;
        @SerializedName("total_movie_price")
        @Expose
        public int moviePrice;
        @SerializedName("max_seats")
        @Expose
        public int maxSeats;
        @SerializedName("total_amount")
        @Expose
        public int totalAmount;
        @SerializedName("cust_name")
        @Expose
        public String custName;
        @SerializedName("cust_email")
        @Expose
        public String custEmail;
        @SerializedName("cust_mobile")
        @Expose
        public String custMobile;
        @SerializedName("movie_name")
        @Expose
        public String movieName;
        @SerializedName("movie_restriction")
        @Expose
        public String movieRestriction;
        @SerializedName("movie_lang")
        @Expose
        public String movieLang;
        @SerializedName("show_time")
        @Expose
        public String showTime;
        @SerializedName("theater_screen")
        @Expose
        public String theaterScreen;
        @SerializedName("show_date")
        @Expose
        public String showDate;
        @SerializedName("process_seats")
        @Expose
        public String processSeats;
        @SerializedName("screen_number")
        @Expose
        public String screenNumber;
        @SerializedName("combos_number")
        @Expose
        public String combosNumber;
        @SerializedName("combos_price")
        @Expose
        public int combosPrice;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("movie_format")
        @Expose
        public String movieFormat;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("fare")
        @Expose
        public List<Fare> fareList = null;
    }

    public class Fare {

        @SerializedName("lable")
        @Expose
        public String lable;
        @SerializedName("quantity")
        @Expose
        public String quantity;
        @SerializedName("price")
        @Expose
        public float price;

    }

}
