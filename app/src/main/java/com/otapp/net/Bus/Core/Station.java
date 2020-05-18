package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Station implements Parcelable {

    @SerializedName("stn_id")
    private String sStationId;
    @SerializedName("stn_long_name")
    private String sName;
    @SerializedName("stn_short_name")
    private String sShortName;
    @SerializedName("snt_country_id")
    private String sCountryId;
    @SerializedName("stn_country_name")
    private String sStationCountry;
//    @SerializedName("BD_Poinst")
    private ArrayList<BDPoints> bdPointsArrayList;

    public Station(String sStationId, String sName, String sShortName, String sStationCountry, ArrayList<BDPoints> bdPointsArrayList) {
        this.sStationId = sStationId;
        this.sName = sName;
        this.sShortName = sShortName;
        this.sStationCountry = sStationCountry;
        this.bdPointsArrayList = bdPointsArrayList;
    }

    public String getsStationId() {
        return sStationId;
    }

    public void setsStationId(String sStationId) {
        this.sStationId = sStationId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsShortName() {
        return sShortName;
    }

    public void setsShortName(String sShortName) {
        this.sShortName = sShortName;
    }

    public String getsStationCountry() {
        return sStationCountry;
    }

    public void setsStationCountry(String sStationCountry) {
        this.sStationCountry = sStationCountry;
    }

    public ArrayList<BDPoints> getBdPointsArrayList() {
        return bdPointsArrayList;
    }

    public void setBdPointsArrayList(ArrayList<BDPoints> bdPointsArrayList) {
        this.bdPointsArrayList = bdPointsArrayList;
    }

    @Override
    public String toString() {
        return sName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sStationId);
        dest.writeString(this.sName);
        dest.writeString(this.sShortName);
        dest.writeString(this.sStationCountry);
        dest.writeTypedList(this.bdPointsArrayList);
    }

    protected Station(Parcel in) {
        this.sStationId = in.readString();
        this.sName = in.readString();
        this.sShortName = in.readString();
        this.sStationCountry = in.readString();
        this.bdPointsArrayList = in.createTypedArrayList(BDPoints.CREATOR);
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel source) {
            return new Station(source);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
}
