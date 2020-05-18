package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieScreenListPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Screen> data;

    public class Screen {

        @SerializedName("screen_id")
        @Expose
        public String screenId;
        @SerializedName("screen_name")
        @Expose
        public String screenName;

    }

}
