package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThemeParkPojo {


    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("prop_id")
    @Expose
    public String prop_id;
    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {

        @SerializedName("banner_img")
        @Expose
        public String bannerImg;
        @SerializedName("theme_park")
        @Expose
        public List<ThemePark> themePark = null;
        @SerializedName("water_park")
        @Expose
        public List<WaterPark> waterPark = null;

    }

    public class ThemePark {

        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("location")
        @Expose
        public String city;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("availability")
        @Expose
        public String availability;
        @SerializedName("time")
        @Expose
        public String time;

    }

    public class WaterPark {

        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("location")
        @Expose
        public String city;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("availability")
        @Expose
        public String availability;
        @SerializedName("time")
        @Expose
        public String time;

    }

    public class Park {

        public String name;
        public String city;
        public String image;
        public String availability;
        public String time;
        public String prop_id;

    }

}
