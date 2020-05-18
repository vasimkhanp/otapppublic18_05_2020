package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EventPaymentSummaryPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Data data = null;

    public class Data {

        @SerializedName("fare")
        @Expose
        public List<Summary> fare = null;
        @SerializedName("payment_allowed")
        @Expose
        public ArrayList<PaymentAllowed> paymentAllowed = null;
        @SerializedName("promo_fare")
        @Expose
        public  PromoFare promoFare;

    }
    public  class  PromoFare{
        @SerializedName("promo_fare")
        @Expose
        public int promoFare;
    }
    public class Summary {

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

    public class PaymentAllowed {

        @SerializedName("payment_id")
        @Expose
        public String paymentId;
        @SerializedName("payment_name")
        @Expose
        public String paymentName;
        @SerializedName("extra_pgw_charges")
        @Expose
        public String sExtraPGWCharges;


    }

}
