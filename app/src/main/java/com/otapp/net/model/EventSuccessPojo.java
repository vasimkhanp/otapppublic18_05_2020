package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventSuccessPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public EventSuccess data;

    public class EventSuccess {

        @SerializedName("currency")
        @Expose
        public String currency;
        @SerializedName("amount")
        @Expose
        public int amount = 0;
        @SerializedName("booking_id")
        @Expose
        public String bookingId;
        @SerializedName("event_name")
        @Expose
        public String eventName;
        @SerializedName("cust_name")
        @Expose
        public String custName;
        @SerializedName("cust_email")
        @Expose
        public String custEmail;
        @SerializedName("cust_mobile")
        @Expose
        public String custMobile;
        @SerializedName("transaction_type")
        @Expose
        public String transactionType;
        @SerializedName("event_date")
        @Expose
        public String eventDate;
        @SerializedName("event_time")
        @Expose
        public String eventTime;
        @SerializedName("event_address")
        @Expose
        public String eventAddress;
        @SerializedName("event_city")
        @Expose
        public String eventCity;
        @SerializedName("event_image")
        @Expose
        public String image;
        @SerializedName("seats")
        @Expose
        public List<Seats> seats;
        @SerializedName("fare")
        @Expose
        public List<Fare> fareList;

    }


    public class Fare {

        @SerializedName("lable")
        @Expose
        public String lable;
        @SerializedName("quantity")
        @Expose
        public String quantity;
        @SerializedName("price")
        @Expose
        public float price;

    }

    public class Seats {

        @SerializedName("ticket_type")
        @Expose
        public String TicketType;
        @SerializedName("floor")
        @Expose
        public String Floor;
        @SerializedName("seat")
        @Expose
        public String Seat;
        @SerializedName("seatscount")
        @Expose
        public int seatscount;
        @SerializedName("price")
        @Expose
        public int price;
        @SerializedName("currency")
        @Expose
        public String currency;

    }
}
