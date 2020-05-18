package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlightOneDetailsPojo {

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

        @SerializedName("uid")
        @Expose
        public String uid;
        @SerializedName("from")
        @Expose
        public String from;
        @SerializedName("fromCity")
        @Expose
        public String fromCity;
        @SerializedName("to")
        @Expose
        public String to;
        @SerializedName("toCity")
        @Expose
        public String toCity;
        @SerializedName("journeyEndDate")
        @Expose
        public String journeyEndDate;
        @SerializedName("journeyStartDate")
        @Expose
        public String journeyStartDate;
        @SerializedName("journeyDurationTotal")
        @Expose
        public String journeyDurationTotal;
        @SerializedName("currency")
        @Expose
        public String currency;
        public String userName;
        public String userEmail;
        public String userPhone;
        @SerializedName("stop_status")
        @Expose
        public String stopStatus;
        @SerializedName("baseFare")
        @Expose
        public float baseFare;
        @SerializedName("fuelSurcharge")
        @Expose
        public float fuelSurcharge;
        @SerializedName("otherCharges")
        @Expose
        public float otherCharges;
        @SerializedName("psf")
        @Expose
        public float psf;
        @SerializedName("kTax")
        @Expose
        public float kTax;
        @SerializedName("tax")
        @Expose
        public float tax;
        @SerializedName("serviceFee")
        @Expose
        public float serviceFee;
        @SerializedName("adultFare")
        @Expose
        public AdultFare adultFare;
        @SerializedName("childFare")
        @Expose
        public ChildFare childFare;
        @SerializedName("infantFare")
        @Expose
        public InfantFare infantFare;
        @SerializedName("grandTotal")
        @Expose
        public float grandTotal;
        @SerializedName("isRefundableFare")
        @Expose
        public boolean isRefundableFare;
        @SerializedName("flightDetails")
        @Expose
        public List<FlightDetail> flightDetails = null;
        @SerializedName("farerules")
        @Expose
        public List<Farerule> farerules = null;
        @SerializedName("meals")
        @Expose
        public List<Meal> meals = null;
        @SerializedName("baggage")
        @Expose
        public List<Baggage> baggages = null;


    }

    public class FlightDetail {

        @SerializedName("uid")
        @Expose
        public String uid;
        @SerializedName("key")
        @Expose
        public String key;
        @SerializedName("duration")
        @Expose
        public Integer duration;
        @SerializedName("startAirport")
        @Expose
        public String startAirport;
        @SerializedName("startTerminal")
        @Expose
        public String startTerminal;
        @SerializedName("startDate")
        @Expose
        public String startDate;
        @SerializedName("endAirport")
        @Expose
        public String endAirport;
        @SerializedName("endTerminal")
        @Expose
        public String endTerminal;
        @SerializedName("endDate")
        @Expose
        public String endDate;
        @SerializedName("airline")
        @Expose
        public String airline;
        @SerializedName("flightNumber")
        @Expose
        public String flightNumber;
        @SerializedName("equipmentNumber")
        @Expose
        public String equipmentNumber;
        @SerializedName("baggageInfo")
        @Expose
        public String baggageInfo;
        @SerializedName("class")
        @Expose
        public String clas;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("startAirportCity")
        @Expose
        public String startAirportCity;
        @SerializedName("endAirportCity")
        @Expose
        public String endAirportCity;
        @SerializedName("startAirportName")
        @Expose
        public String startAirportName;
        @SerializedName("endAirportName")
        @Expose
        public String endAirportName;
        @SerializedName("logo")
        @Expose
        public String logo;
        @SerializedName("flag")
        @Expose
        public String flag;
        public String layOvertime;

    }

    public class AdultFare {

        @SerializedName("baseFare")
        @Expose
        public float baseFare;
        @SerializedName("fuelSurcharge")
        @Expose
        public float fuelSurcharge;
        @SerializedName("otherCharges")
        @Expose
        public float otherCharges;
        @SerializedName("psf")
        @Expose
        public float psf;
        @SerializedName("tax")
        @Expose
        public float tax;
        @SerializedName("kTax")
        @Expose
        public float kTax;
        @SerializedName("grandTotal")
        @Expose
        public float grandTotal;

    }

    public class ChildFare {

        @SerializedName("baseFare")
        @Expose
        public float baseFare;
        @SerializedName("fuelSurcharge")
        @Expose
        public float fuelSurcharge;
        @SerializedName("otherCharges")
        @Expose
        public float otherCharges;
        @SerializedName("psf")
        @Expose
        public float psf;
        @SerializedName("tax")
        @Expose
        public float tax;
        @SerializedName("kTax")
        @Expose
        public float kTax;
        @SerializedName("grandTotal")
        @Expose
        public float grandTotal;

    }

    public class InfantFare {

        @SerializedName("baseFare")
        @Expose
        public float baseFare;
        @SerializedName("fuelSurcharge")
        @Expose
        public float fuelSurcharge;
        @SerializedName("otherCharges")
        @Expose
        public float otherCharges;
        @SerializedName("psf")
        @Expose
        public float psf;
        @SerializedName("tax")
        @Expose
        public float tax;
        @SerializedName("kTax")
        @Expose
        public float kTax;
        @SerializedName("grandTotal")
        @Expose
        public float grandTotal;

    }

    public class Farerule {

        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("description")
        @Expose
        public String description;

    }

    public class Meal {

        public String id = "";
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("price")
        @Expose
        public float price;
        public boolean isSelected = false;

    }

    public class Baggage {

        public String id = "";
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("price")
        @Expose
        public float price;
        public boolean isSelected = false;

    }

}
