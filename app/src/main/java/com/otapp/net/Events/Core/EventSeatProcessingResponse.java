package com.otapp.net.Events.Core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventSeatProcessingResponse {
    @SerializedName("status")
    @Expose
    public   int status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("tot_fare")
    @Expose
    public String tot_fare;

    @SerializedName("ukey")
    @Expose
    public String ukey;

    @SerializedName("selected_seats")
    @Expose
    public String selected_seats;
}
