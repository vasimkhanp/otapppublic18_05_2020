package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpcomingTripPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("total_count")
    @Expose
    public int total_count = 0;
    @SerializedName("data")
    @Expose
    public List<UpcomingTrip> data;

    public class UpcomingTrip {

        @SerializedName("bookings_date")
        @Expose
        public String bookingsDate;
        @SerializedName("Service")
        @Expose
        public String service;
        @SerializedName("tkt_no")
        @Expose
        public String tktNo;
        @SerializedName("service_name")
        @Expose
        public String serviceName;
        @SerializedName("show_date_time")
        @Expose
        public String showDateTime;
        @SerializedName("prop_name")
        @Expose
        public String propName;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("ticket_type")
        @Expose
        public String ticketType;
        @SerializedName("seats")
        @Expose
        public String seats;
        @SerializedName("tran_currency")
        @Expose
        public String tranCurrency;
        @SerializedName("amt_paid")
        @Expose
        public String amtPaid;
        @SerializedName("pnr")
        @Expose
        public String pnr;
        @SerializedName("depature_date")
        @Expose
        public String depatureDate;
        @SerializedName("depature_arv_date")
        @Expose
        public String depatureArvDate;
        @SerializedName("return_date")
        @Expose
        public String returnDate;
        @SerializedName("return_arvl_date_time")
        @Expose
        public String returnArvlDateTime;
        @SerializedName("flight_type")
        @Expose
        public String flightType;
        @SerializedName("start_city")
        @Expose
        public String startCity;
        @SerializedName("end_city")
        @Expose
        public String endCity;

    }

}
