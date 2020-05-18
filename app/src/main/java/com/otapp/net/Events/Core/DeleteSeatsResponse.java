package com.otapp.net.Events.Core;

import com.google.gson.annotations.SerializedName;

public class DeleteSeatsResponse {

    @SerializedName("status")
    int Status;
    @SerializedName("message")
    String sMessage;
    @SerializedName("tot_fare")
    String sTotalFare;
    @SerializedName("ukey")
    String sUKey;
    @SerializedName("selected_seats")
    String sSelectedSeats;

    public DeleteSeatsResponse(int status, String sMessage, String sTotalFare, String sUKey, String sSelectedSeats) {
        Status = status;
        this.sMessage = sMessage;
        this.sTotalFare = sTotalFare;
        this.sUKey = sUKey;
        this.sSelectedSeats = sSelectedSeats;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getsMessage() {
        return sMessage;
    }

    public void setsMessage(String sMessage) {
        this.sMessage = sMessage;
    }

    public String getsTotalFare() {
        return sTotalFare;
    }

    public void setsTotalFare(String sTotalFare) {
        this.sTotalFare = sTotalFare;
    }

    public String getsUKey() {
        return sUKey;
    }

    public void setsUKey(String sUKey) {
        this.sUKey = sUKey;
    }

    public String getsSelectedSeats() {
        return sSelectedSeats;
    }

    public void setsSelectedSeats(String sSelectedSeats) {
        this.sSelectedSeats = sSelectedSeats;
    }
}
