package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSeatListPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("screens")
    @Expose
    public Screen screens = null;
    @SerializedName("terms_and_condition")
    @Expose
    public List<String> termsCondition = null;

    public class Screen {

        @SerializedName("screen_id")
        @Expose
        public String screenId;
        @SerializedName("screen_info")
        @Expose
        public List<ScreenInfo> screenInfo = null;
        @SerializedName("max_seats_per_row")
        @Expose
        public String maxSeatsPerRow;

    }

    public class ScreenInfo {

        @SerializedName("screen_row")
        @Expose
        public String screenRow;
        @SerializedName("screen_row_seats")
        @Expose
        public String screenRowSeats;

    }

}
