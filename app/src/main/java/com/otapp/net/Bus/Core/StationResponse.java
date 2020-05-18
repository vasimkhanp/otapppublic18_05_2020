package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StationResponse implements Parcelable {

    @SerializedName("status")
    private int Status;
    @SerializedName("message")
    private String sMessage;
    @SerializedName("stations")
    private ArrayList<Station> stationArrayList;

    public StationResponse(int status, String sMessage, ArrayList<Station> stationArrayList) {
        this.Status = status;
        this.sMessage = sMessage;
        this.stationArrayList = stationArrayList;
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

    public ArrayList<Station> getStationArrayList() {
        return stationArrayList;
    }

    public void setStationArrayList(ArrayList<Station> stationArrayList) {
        this.stationArrayList = stationArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Status);
        dest.writeString(this.sMessage);
        dest.writeTypedList(this.stationArrayList);
    }

    protected StationResponse(Parcel in) {
        this.Status = in.readInt();
        this.sMessage = in.readString();
        this.stationArrayList = in.createTypedArrayList(Station.CREATOR);
    }

    public static final Creator<StationResponse> CREATOR = new Creator<StationResponse>() {
        @Override
        public StationResponse createFromParcel(Parcel source) {
            return new StationResponse(source);
        }

        @Override
        public StationResponse[] newArray(int size) {
            return new StationResponse[size];
        }
    };
}
