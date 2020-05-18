package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProcessSeat implements Parcelable {

    @SerializedName("status")
    int Status;
    @SerializedName("message")
    String sMessage;
    @SerializedName("fare")
    String sFare;
    @SerializedName("tot_fare")
    String sTotalFare;


    public ProcessSeat(int status, String sMessage, String sFare, String sTotalFare) {
        Status = status;
        this.sMessage = sMessage;
        this.sFare = sFare;
        this.sTotalFare = sTotalFare;
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

    public String getsFare() {
        return sFare;
    }

    public void setsFare(String sFare) {
        this.sFare = sFare;
    }

    public String getsTotalFare() {
        return sTotalFare;
    }

    public void setsTotalFare(String sTotalFare) {
        this.sTotalFare = sTotalFare;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Status);
        dest.writeString(this.sMessage);
        dest.writeString(this.sFare);
        dest.writeString(this.sTotalFare);
    }

    protected ProcessSeat(Parcel in) {
        this.Status = in.readInt();
        this.sMessage = in.readString();
        this.sFare = in.readString();
        this.sTotalFare = in.readString();
    }

    public static final Creator<ProcessSeat> CREATOR = new Creator<ProcessSeat>() {
        @Override
        public ProcessSeat createFromParcel(Parcel source) {
            return new ProcessSeat(source);
        }

        @Override
        public ProcessSeat[] newArray(int size) {
            return new ProcessSeat[size];
        }
    };
}
