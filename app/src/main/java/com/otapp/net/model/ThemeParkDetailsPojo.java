package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ThemeParkDetailsPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Details details;


    public class Details {

        @SerializedName("tp_id")
        @Expose
        public String tpId;
        @SerializedName("tp_name")
        @Expose
        public String tpName;
        @SerializedName("tp_type")
        @Expose
        public String tpType;
        @SerializedName("tp_sub_name")
        @Expose
        public String tpSubName;
        @SerializedName("tp_description")
        @Expose
        public String tpDescription;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("tick_den_id")
        @Expose
        public String tickDenId;
        @SerializedName("currrency")
        @Expose
        public String currrency;
        @SerializedName("adult_price")
        @Expose
        public int adultPrice = 0;
        @SerializedName("child_price")
        @Expose
        public int childPrice = 0;
        @SerializedName("stud_price")
        @Expose
        public int studPrice = 0;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("timings")
        @Expose
        public String timings;
        @SerializedName("booking_date")
        @Expose
        public String bookingDate;
        @SerializedName("cart_count")
        @Expose
        public int cartCount;
        @SerializedName("image_gallery")
        @Expose
        public List<String> imageGallery = null;
        @SerializedName("major_attractions")
        @Expose
        public List<MajorAttraction> majorAttractions = null;
        public int adultQuntity = 0;
        public int childQuntity = 0;
        public int studentQuntity = 0;

        @SerializedName("payment_allowed")
        @Expose
        public ArrayList<PaymentAllowed> paymentAllowed = null;

    }

    public class PaymentAllowed {

        @SerializedName("payment_id")
        @Expose
        public String paymentId;
        @SerializedName("payment_name")
        @Expose
        public String paymentName;

    }


    public class MajorAttraction {

        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("title")
        @Expose
        public String title;
    }

}
