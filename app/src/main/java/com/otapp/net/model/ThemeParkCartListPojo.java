package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThemeParkCartListPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("entrance_fee_status")
    @Expose
    public boolean entranceFeeStatus;
    @SerializedName("total_amount")
    @Expose
    public int totalAmount = 0;
    @SerializedName("data")
    @Expose
    public List<CartItem> mCartList = null;

    public class CartItem {

        @SerializedName("tp_id")
        @Expose
        public String tpId;
        @SerializedName("tp_name")
        @Expose
        public String tpName;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("is_entrance")
        @Expose
        public boolean isEntrance = false;
        @SerializedName("currency")
        @Expose
        public String currency;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("adult_price")
        @Expose
        public int adultPrice = 0;
        @SerializedName("adult_ticket_count")
        @Expose
        public int adultTicketCount = 0;
        @SerializedName("child_price")
        @Expose
        public int childPrice = 0;
        @SerializedName("child_ticket_count")
        @Expose
        public int childTicketCount = 0;
        @SerializedName("stud_price")
        @Expose
        public int studPrice = 0;
        @SerializedName("stud_ticket_count")
        @Expose
        public int studTicketCount = 0;

    }

}
