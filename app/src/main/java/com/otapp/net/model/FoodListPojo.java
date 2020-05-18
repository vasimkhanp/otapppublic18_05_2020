package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodListPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Data data = null;

    public class Data {

        @SerializedName("advertisements")
        @Expose
        public List<Advertisement> advertisements = null;

        @SerializedName("combos")
        @Expose
        public List<Combo> combos = null;
    }

    public class Advertisement {
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("module")
        @Expose
        public String module;
    }

    public class Combo {

        @SerializedName("mcid")
        @Expose
        public String mcid;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("price")
        @Expose
        public int price;
        @SerializedName("max_quntity")
        @Expose
        public int maxQuntity;
        @SerializedName("currency")
        @Expose
        public String currency;

        public int foodCount;

        public boolean isSelected = false;

    }

}
