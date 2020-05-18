package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlightCityPojo {

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

        @SerializedName("cities")
        @Expose
        public List<City> cities = null;
        @SerializedName("class")
        @Expose
        public List<Clas> clas = null;
        @SerializedName("currency")
        @Expose
        public List<Currency> currencies = null;
//        @SerializedName("cnv_fix_fee")
//        @Expose
//        public int cnvFixedFee = 0;
//        @SerializedName("cnv_per_fee")
//        @Expose
//        public int cnvPerFee = 0;
        @SerializedName("flight_auth_token")
        @Expose
        public String flightAuthToken = "";

    }

    public class Currency {

        @SerializedName("currency")
        @Expose
        public String currencyName;
        @SerializedName("icon")
        @Expose
        public String icon;
        @SerializedName("cnv_fix_fee")
        @Expose
        public int cnvFixedFee = 0;
        @SerializedName("cnv_per_fee")
        @Expose
        public int cnvPerFee = 0;

    }

    public class Clas {

        @SerializedName("class_name")
        @Expose
        public String className;
        @SerializedName("class_code")
        @Expose
        public String classCode;

    }

    public class City {

        @SerializedName("cityCode")
        @Expose
        public String cityCode;
        @SerializedName("cityName")
        @Expose
        public String cityName;
        @SerializedName("airportCode")
        @Expose
        public String airportCode;
        @SerializedName("countryCode")
        @Expose
        public String countryCode;
        @SerializedName("state")
        @Expose
        public String state;
        @SerializedName("airport")
        @Expose
        public String airport;
        @SerializedName("countryName")
        @Expose
        public String countryName;

    }

}
