package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTheaterListPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("user_key")
    @Expose
    public String userKey;
    @SerializedName("data")
    @Expose
    public List<Theater> theaters = null;

    public class Theater {

        @SerializedName("screen_id")
        @Expose
        public String screenId;
        @SerializedName("screen_name")
        @Expose
        public String screenName;
        @SerializedName("cinema_address")
        @Expose
        public String cinemaAddress;
        @SerializedName("screen_time")
        @Expose
        public List<ScreenTime> screenTime = null;

    }

    public class ScreenTime {

        @SerializedName("mv_screen")
        @Expose
        public String mvScreen;
        @SerializedName("vdt")
        @Expose
        public String vdt;
        @SerializedName("mtid")
        @Expose
        public String mtid;
        @SerializedName("price")
        @Expose
        public Integer price;
        @SerializedName("currency")
        @Expose
        public String currency;
        @SerializedName("cinema_screen_name")
        @Expose
        public String cinemaScreenName;
        @SerializedName("movie_format")
        @Expose
        public String movie_format;
        @SerializedName("available_seats")
        @Expose
        public int available_seats = 0;
        @SerializedName("seat_stauts")
        @Expose
        public String seat_stauts;
        @SerializedName("show_status")
        @Expose
        public String show_status;
        public boolean isSelected = false;

    }
}
