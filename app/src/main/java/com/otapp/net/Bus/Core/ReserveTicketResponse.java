package com.otapp.net.Bus.Core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReserveTicketResponse {
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("tkt_status")
    @Expose
    public String tkt_status;
    @SerializedName("booking_id")
    @Expose
    public String booking_id;
    @SerializedName("pay_mode")
    @Expose
    public String pay_mode;
    @SerializedName("booking_time")
    @Expose
    public String booking_time;


    @SerializedName("booking_info")
    @Expose
    public BookingInfo bookingInfo;
    @SerializedName("return_booking_info")
    @Expose
    public ReturnBookingInfo returnBookingInfo;



    public class BookingInfo{
        @SerializedName("tkt_id")
        @Expose
        public String tkt_id;
        @SerializedName("from_stn_name")
        @Expose
        public String from_stn_name;
        @SerializedName("to_stn_name")
        @Expose
        public String to_stn_name;
        @SerializedName("travel_date_time")
        @Expose
        public String travel_date_time;
        @SerializedName("arrival_date_time")
        @Expose
        public String arrival_date_time;
        @SerializedName("total_fare")
        @Expose
        public String total_fare;
        @SerializedName("journey_duration")
        @Expose
        public String journey_duration;
        @SerializedName("bus_name")
        @Expose
        public String bus_name;
        @SerializedName("plate_no")
        @Expose
        public String plate_no;
        @SerializedName("reporting_time")
        @Expose
        public String reporting_time;
        @SerializedName("boarding_point")
        @Expose
        public String boarding_point;
        @SerializedName("dropping")
        @Expose
        public String dropping;
        @SerializedName("header")
        @Expose
        public String header;
        @SerializedName("footer")
        @Expose
        public String footer;
        @SerializedName("bus_company_name")
        @Expose
        public String bus_company_name;
        @SerializedName("bus_company_phone")
        @Expose
        public String bus_company_phone;
        @SerializedName("bus_owner_address")
        @Expose
        public String bus_owner_address;
        @SerializedName("bus_owner_tin")
        @Expose
        public String bus_owner_tin;
        @SerializedName("passengers")
        @Expose
        public ArrayList<Passengers> passengersArrayList;

        public class Passengers {
            @SerializedName("name")
            @Expose
            public String name;
            @SerializedName("gender")
            @Expose
            public String gender;
            @SerializedName("category")
            @Expose
            public String category;
            @SerializedName("passport")
            @Expose
            public String passport;
            @SerializedName("seat_sno")
            @Expose
            public String seat_sno;
            @SerializedName("seat_id")
            @Expose
            public String seat_id;
            @SerializedName("fare")
            @Expose
            public String fare;
        }

    }
    public class ReturnBookingInfo{
        @SerializedName("tkt_id")
        @Expose
        public String tkt_id;
        @SerializedName("from_stn_name")
        @Expose
        public String from_stn_name;
        @SerializedName("to_stn_name")
        @Expose
        public String to_stn_name;
        @SerializedName("travel_date_time")
        @Expose
        public String travel_date_time;
        @SerializedName("arrival_date_time")
        @Expose
        public String arrival_date_time;
        @SerializedName("total_fare")
        @Expose
        public String total_fare;
        @SerializedName("journey_duration")
        @Expose
        public String journey_duration;
        @SerializedName("bus_name")
        @Expose
        public String bus_name;
        @SerializedName("plate_no")
        @Expose
        public String plate_no;
        @SerializedName("reporting_time")
        @Expose
        public String reporting_time;
        @SerializedName("boarding_point")
        @Expose
        public String boarding_point;
        @SerializedName("dropping")
        @Expose
        public String dropping;
        @SerializedName("header")
        @Expose
        public String header;
        @SerializedName("footer")
        @Expose
        public String footer;
        @SerializedName("bus_company_name")
        @Expose
        public String bus_company_name;
        @SerializedName("bus_company_phone")
        @Expose
        public String bus_company_phone;
        @SerializedName("bus_owner_address")
        @Expose
        public String bus_owner_address;
        @SerializedName("bus_owner_tin")
        @Expose
        public String bus_owner_tin;
        @SerializedName("passengers")
        @Expose
        public ArrayList<Passengers> passengersArrayList;

        public class Passengers {
            @SerializedName("name")
            @Expose
            public String name;
            @SerializedName("gender")
            @Expose
            public String gender;
            @SerializedName("category")
            @Expose
            public String category;
            @SerializedName("passport")
            @Expose
            public String passport;
            @SerializedName("seat_sno")
            @Expose
            public String seat_sno;
            @SerializedName("seat_id")
            @Expose
            public String seat_id;
            @SerializedName("fare")
            @Expose
            public String fare;
        }

    }
}
