package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchBusResponse {
    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("message")
    @Expose
    public int message;


    @SerializedName("available_buses")
    @Expose
   public ArrayList<AvailableBuses> availableBusesArrayList;

    public SearchBusResponse(int status, int message, ArrayList<AvailableBuses> availableBusesArrayList) {
        this.status = status;
        this.message = message;
        this.availableBusesArrayList = availableBusesArrayList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public ArrayList<AvailableBuses> getAvailableBusesArrayList() {
        return availableBusesArrayList;
    }

    public void setAvailableBusesArrayList(ArrayList<AvailableBuses> availableBusesArrayList) {
        this.availableBusesArrayList = availableBusesArrayList;
    }
}
