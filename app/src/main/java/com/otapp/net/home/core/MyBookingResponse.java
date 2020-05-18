package com.otapp.net.home.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyBookingResponse {
    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("transactions")
    @Expose
    public List<MyBookingTransactoins> myBookingTransactoinsList;

    @SerializedName("tot_pages")
    @Expose
    public String tot_pages;

    @SerializedName("message")
    @Expose
    public String message;

    public class MyBookingTransactoins{
        @SerializedName("tran_id")
        @Expose
        public  String tran_id;

        @SerializedName("ticket_no")
        @Expose
        public  String ticket_no;

        @SerializedName("tran_amount")
        @Expose
        public  String tran_amount;

        @SerializedName("event_date")
        @Expose
        public  String event_date;

        @SerializedName("tran_status")
        @Expose
        public  String tran_status;

        @SerializedName("pay_type")
        @Expose
        public  String pay_type;

        @SerializedName("event_name")
        @Expose
        public  String event_name;

        @SerializedName("event_addrs")
        @Expose
        public  String event_addrs;

        @SerializedName("event_city")
        @Expose
        public  String event_city;

        @SerializedName("event_thumb")
        @Expose
        public  String event_thumb;

        @SerializedName("show_time")
        @Expose
        public String show_time;

        @SerializedName("movie_name")
        @Expose
        public String movie_name;

        @SerializedName("theater_name")
        @Expose
        public String theater_name;

        @SerializedName("theater_city")
        @Expose
        public String theater_city;

        @SerializedName("movie_thumb")
        @Expose
        public String movie_thumb;

        ///Themepark

        @SerializedName("tp_date")
        @Expose
        public String tp_date;

        @SerializedName("tp_name")
        @Expose
        public String tp_name;

        @SerializedName("tp_addrs")
        @Expose
        public String tp_addrs;
        @SerializedName("tp_city")
        @Expose
        public String tp_city;

        @SerializedName("tp_thumb")
        @Expose
        public String tp_thumb;



        ///////Flight
        @SerializedName("tran_stat")
        @Expose
        public String tran_stat;

        @SerializedName("booking_type")
        @Expose
        public String booking_type;

        @SerializedName("start_city")
        @Expose
        public String start_city;

        @SerializedName("end_city")
        @Expose
        public String end_city;

        @SerializedName("class")
        @Expose
        public String clas;

        @SerializedName("departure_date")
        @Expose
        public String departure_date;
        @SerializedName("return_date")
        @Expose
        public String return_date;
        @SerializedName("airline_pnr")
        @Expose
        public String airline_pnr;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("last_name")
        @Expose
        public String last_name;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("mobile")
        @Expose
        public String mobile;


        @SerializedName("flightDetails")
        @Expose
        public List<FlightDetails> flightDetailsList;

        public class FlightDetails{
            @SerializedName("start_Station")
            @Expose
            public String start_Station;

            @SerializedName("end_Station")
            @Expose
            public String end_Station;

            @SerializedName("start_date_time")
            @Expose
            public String start_date_time;

            @SerializedName("end_date_time")
            @Expose
            public String end_date_time;

            @SerializedName("carrier")
            @Expose
            public String carrier;


            @SerializedName("flight_no")
            @Expose
            public String flight_no;

            @SerializedName("equipment_no")
            @Expose
            public String equipment_no;

            @SerializedName("flight_duration")
            @Expose
            public String flight_duration;

            @SerializedName("boarding_terminal")
            @Expose
            public String boarding_terminal;

            @SerializedName("arrival_terminal")
            @Expose
            public String arrival_terminal;

            @SerializedName("baggage_info")
            @Expose
            public String baggage_info;


            @SerializedName("is_connected")
            @Expose
            public String is_connected;


            @SerializedName("logo")
            @Expose
            public String logo;


            @SerializedName("start_airport")
            @Expose
            public String start_airport;


            @SerializedName("end_airport")
            @Expose
            public String end_airport;

            @SerializedName("lay_over_span")
            @Expose
            public String lay_over_span;


        }


    }
}
