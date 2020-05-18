package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

public class Seat implements Parcelable {

    public String sType="";
    public String sTypeId="0";
    public String sFare;
    public String sSeatNo;
    public String sIsSelected;

    public Seat(String sSeatNo, String sIsSelected) {
        this.sSeatNo = sSeatNo;
        this.sIsSelected = sIsSelected;
    }

    public String getsFare() {
        return sFare;
    }

    public void setsFare(String sFare) {
        this.sFare = sFare;
    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public String getsTypeId() {
        return sTypeId;
    }

    public void setsTypeId(String sTypeId) {
        this.sTypeId = sTypeId;
    }

    /*
    private String sSeatNo;
    private String sStatus;
    private String sFare;

    public Seat(String sSeatNo, String sStatus) {
        this.sSeatNo = sSeatNo;
        this.sStatus = sStatus;
    }

    public String getsSeatNo() {
        return sSeatNo;
    }

    public void setsSeatNo(String sSeatNo) {
        this.sSeatNo = sSeatNo;
    }

    public String getsStatus() {
        return sStatus;
    }

    public void setsStatus(String sStatus) {
        this.sStatus = sStatus;
    }

    public String getsFare() {
        return sFare;
    }

    public void setsFare(String sFare) {
        this.sFare = sFare;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sSeatNo);
        dest.writeString(this.sStatus);
        dest.writeString(this.sFare);
    }

    protected Seat(Parcel in) {
        this.sSeatNo = in.readString();
        this.sStatus = in.readString();
        this.sFare = in.readString();
    }

    public static final Creator<Seat> CREATOR = new Creator<Seat>() {
        @Override
        public Seat createFromParcel(Parcel source) {
            return new Seat(source);
        }

        @Override
        public Seat[] newArray(int size) {
            return new Seat[size];
        }
    };*/

    public String getsSeatNo() {
        return sSeatNo;
    }

    public void setsSeatNo(String sSeatNo) {
        this.sSeatNo = sSeatNo;
    }

    public String getsIsSelected() {
        return sIsSelected;
    }

    public void setsIsSelected(String sIsSelected) {
        this.sIsSelected = sIsSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sType);
        dest.writeString(this.sTypeId);
        dest.writeString(this.sFare);
        dest.writeString(this.sSeatNo);
        dest.writeString(this.sIsSelected);
    }

    protected Seat(Parcel in) {
        this.sType = in.readString();
        this.sTypeId = in.readString();
        this.sFare = in.readString();
        this.sSeatNo = in.readString();
        this.sIsSelected = in.readString();
    }

    public static final Creator<Seat> CREATOR = new Creator<Seat>() {
        @Override
        public Seat createFromParcel(Parcel source) {
            return new Seat(source);
        }

        @Override
        public Seat[] newArray(int size) {
            return new Seat[size];
        }
    };
}
