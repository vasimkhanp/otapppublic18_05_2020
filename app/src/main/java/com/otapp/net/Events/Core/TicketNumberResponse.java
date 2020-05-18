package com.otapp.net.Events.Core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketNumberResponse {
    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("ticket")
    @Expose
    public String ticket;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("tran_id")
    @Expose
    public String tran_id;

    @SerializedName("amount")
    @Expose
    public String amount;

    @SerializedName("msidn")
    @Expose
    public String msidn;


}
