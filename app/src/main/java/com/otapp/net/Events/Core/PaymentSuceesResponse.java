package com.otapp.net.Events.Core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentSuceesResponse {
    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("tkt_no")
    @Expose
    public String tkt_no;

    @SerializedName("tkt_cur")
    @Expose
    public String tkt_cur;

    @SerializedName("tot_excl_tax")
    @Expose
    public String tot_excl_tax;

    @SerializedName("discount")
    @Expose
    public String discount;

    @SerializedName("conv_fee")
    @Expose
    public String conv_fee;

    @SerializedName("taxable_amount")
    @Expose
    public String taxable_amount;

    @SerializedName("tot_vat")
    @Expose
    public String tot_vat;

    @SerializedName("paid_amount")
    @Expose
    public String paid_amount;

    @SerializedName("event_name")
    @Expose
    public String event_name;

    @SerializedName("event_addrs")
    @Expose
    public String event_addrs;

    @SerializedName("event_city")
    @Expose
    public String event_city;

    @SerializedName("event_date")
    @Expose
    public String event_date;

    @SerializedName("event_time")
    @Expose
    public String event_time;

    @SerializedName("event_img")
    @Expose
    public String event_img;

    @SerializedName("seats")
    @Expose
    public List<Seats> seatsList;

    @SerializedName("pay_type")
    @Expose
    public String pay_type;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("mob")
    @Expose
    public String mob;

    @SerializedName("booked_by")
    @Expose
    public String booked_by;

    @SerializedName("booking_time")
    @Expose
    public String booking_time;

    @SerializedName("resend_email_url")
    @Expose
    public String resend_email_url;




    public class Seats{
        @SerializedName("tkt_type")
        @Expose
        public String tkt_type;

        @SerializedName("floor")
        @Expose
        public String floor;

        @SerializedName("row")
        @Expose
        public String row;

        @SerializedName("column")
        @Expose
        public String column;

        @SerializedName("seats")
        @Expose
        public String seats;

        @SerializedName("tkt_currency")
        @Expose
        public String tkt_currency;

        @SerializedName("tkt_amount")
        @Expose
        public String tkt_amount;

        @SerializedName("no_tkts")
        @Expose
        public String no_tkts;

        @SerializedName("tot_amount")
        @Expose
        public String tot_amount;
    }
}
