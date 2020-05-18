package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlightSuccessPojo {

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

        @SerializedName("pnr")
        @Expose
        public String pnr;
        @SerializedName("transaction_id")
        @Expose
        public Integer transactionId;
        @SerializedName("carrier")
        @Expose
        public String carrier;
        @SerializedName("ticket_number")
        @Expose
        public String ticketNumber;
        @SerializedName("tkt_no")
        @Expose
        public String tkt_no;
        @SerializedName("error_message")
        @Expose
        public String error_message;

    }

}
