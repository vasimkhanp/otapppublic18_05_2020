package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThemeParkSuccessPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("booking_id")
    @Expose
    public String bookingId;
    @SerializedName("booking_date")
    @Expose
    public String bookingDate;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("total_amount")
    @Expose
    public int totalAmount = 0;
    @SerializedName("total_ticket")
    @Expose
    public int totalTicket = 0;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("park_name")
    @Expose
    public String parkName;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("timings")
    @Expose
    public String timings;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("data")
    @Expose
    public List<ThemeParkSuccess> data = null;
    @SerializedName("fare")
    @Expose
    public List<Fare> fareList;

    public class Fare {

        @SerializedName("lable")
        @Expose
        public String lable;
        @SerializedName("price")
        @Expose
        public float price;

    }

    public class ThemeParkSuccess {

        @SerializedName("tp_id")
        @Expose
        public String tpId;
        @SerializedName("tp_name")
        @Expose
        public String tpName;
        @SerializedName("currency")
        @Expose
        public String currency;
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
        public int childTicketCount= 0;
        @SerializedName("stud_price")
        @Expose
        public int studPrice = 0;
        @SerializedName("stud_ticket_count")
        @Expose
        public int studTicketCount = 0;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("adult_total")
        @Expose
        public int adultTotal = 0;
        @SerializedName("child_total")
        @Expose
        public int childTotal = 0;

    }

}
