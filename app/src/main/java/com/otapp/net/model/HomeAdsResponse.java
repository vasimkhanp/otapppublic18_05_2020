package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeAdsResponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {

        @SerializedName("advertisements")
        @Expose
        public List<Advertisement> advertisements = null;
        @SerializedName("promotions")
        @Expose
        public List<Promotion> promotions = null;

    }

    public class Advertisement {

        @SerializedName("aid")
        @Expose
        public String aid;
        @SerializedName("page")
        @Expose
        public String page;
        @SerializedName("image_path")
        @Expose
        public String image_path;
        @SerializedName("link")
        @Expose
        public String link;

    }

    public class Promotion {

        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("module")
        @Expose
        public String module;

    }

}
