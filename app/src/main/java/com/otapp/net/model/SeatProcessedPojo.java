package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeatProcessedPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("bk_seats")
    @Expose
    public String bookedSeats;
    @SerializedName("prcs_seats")
    @Expose
    public String processSeats;

}
