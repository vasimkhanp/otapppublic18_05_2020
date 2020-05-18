package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTimeListPojo {

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
    public List<Time> data;

    public class Time {

        @SerializedName("mv_screen")
        @Expose
        public String mv_screen;
        @SerializedName("vdt")
        @Expose
        public String vdt;
        @SerializedName("mtid")
        @Expose
        public String mtid;
        @SerializedName("price")
        @Expose
        public int price;
        @SerializedName("currency")
        @Expose
        public String currency;

    }

}
