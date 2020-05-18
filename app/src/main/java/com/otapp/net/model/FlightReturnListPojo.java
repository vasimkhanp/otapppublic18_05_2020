package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlightReturnListPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("airlines")
    @Expose
    public List<Airlines> airlines;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;

    public class Data {

        @SerializedName("uid")
        @Expose
        public String uid;
        @SerializedName("key")
        @Expose
        public String key;
        public String tag = "";
        @SerializedName("cities")
        @Expose
        public List<List<List<City>>> cities = null;

        @SerializedName("airlines")
        @Expose
        public List<AirlineTitles> airlineTItles;
        @SerializedName("fares")
        @Expose
        public Fares fares;
        @SerializedName("currency")
        @Expose
        public String currency;

    }

    public class AirlineTitles {

        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("airlineName")
        @Expose
        public String name;
        @SerializedName("logo")
        @Expose
        public String logo;
    }

    public class Airlines {

        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("logo")
        @Expose
        public String logo;
        public boolean isSelected = false;
    }

    public class Fares {

        @SerializedName("adult")
        @Expose
        public Adult adult;
        @SerializedName("child")
        @Expose
        public Child child;
        @SerializedName("infant")
        @Expose
        public Infant infant;
        @SerializedName("total")
        @Expose
        public Total total;
        @SerializedName("extra")
        @Expose
        public Object extra;

    }

    public class City {

        @SerializedName("uid")
        @Expose
        public String uid;
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
        @SerializedName("operatingCarrier")
        @Expose
        public String operatingCarrier;
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
        public String _class;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("startAirportName")
        @Expose
        public String startAirportName;
        @SerializedName("endAirportName")
        @Expose
        public String endAirportName;
        @SerializedName("airlineName")
        @Expose
        public String airlineName;
        @SerializedName("logo")
        @Expose
        public String logo;
        @SerializedName("startAirportCity")
        @Expose
        public String startAirportCity;
        @SerializedName("endAirportCity")
        @Expose
        public String endAirportCity;
        @SerializedName("flightEupipmentName")
        @Expose
        public String flightEupipmentName;
        public boolean isSelected = false;

    }

    public class Adult {

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

    public class Child {

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

    public class Infant {

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

    public class Total {

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

}
