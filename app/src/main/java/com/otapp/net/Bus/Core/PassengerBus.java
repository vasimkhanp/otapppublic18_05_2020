package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PassengerBus implements Parcelable {

    @SerializedName("BusOperators")
    String sBusOperators;
    @SerializedName("BusNumber")
    String sBusNumber;
    @SerializedName("BusID")
    String sBusID;
    @SerializedName("BusType")
    String sBusType;
    @SerializedName("Departure")
    String sDeparture;
    @SerializedName("Arrival")
    String sArrival;
    @SerializedName("JourneyDuration")
    String sJourneyDuration;
    @SerializedName("Fare")
    String sFare;
    @SerializedName("New_Journey_Date")
    String sNewJourneyDate;
    @SerializedName("is_enabled")
    String sIsEnabled;
    @SerializedName("SeatAvailable")
    String sSeatAvailable;


    public PassengerBus(String sBusOperators, String sBusNumber, String sBusID, String sBusType,
                        String sDeparture, String sArrival, String sJourneyDuration, String sFare,
                        String sNewJourneyDate, String sIsEnabled, String sSeatAvailable) {
        this.sBusOperators = sBusOperators;
        this.sBusNumber = sBusNumber;
        this.sBusID = sBusID;
        this.sBusType = sBusType;
        this.sDeparture = sDeparture;
        this.sArrival = sArrival;
        this.sJourneyDuration = sJourneyDuration;
        this.sFare = sFare;
        this.sNewJourneyDate = sNewJourneyDate;
        this.sIsEnabled = sIsEnabled;
        this.sSeatAvailable = sSeatAvailable;
    }

    public String getsBusOperators() {
        return sBusOperators;
    }

    public void setsBusOperators(String sBusOperators) {
        this.sBusOperators = sBusOperators;
    }

    public String getsBusNumber() {
        return sBusNumber;
    }

    public void setsBusNumber(String sBusNumber) {
        this.sBusNumber = sBusNumber;
    }

    public String getsBusID() {
        return sBusID;
    }

    public void setsBusID(String sBusID) {
        this.sBusID = sBusID;
    }

    public String getsBusType() {
        return sBusType;
    }

    public void setsBusType(String sBusType) {
        this.sBusType = sBusType;
    }

    public String getsDeparture() {
        return sDeparture;
    }

    public void setsDeparture(String sDeparture) {
        this.sDeparture = sDeparture;
    }

    public String getsArrival() {
        return sArrival;
    }

    public void setsArrival(String sArrival) {
        this.sArrival = sArrival;
    }

    public String getsJourneyDuration() {
        return sJourneyDuration;
    }

    public void setsJourneyDuration(String sJourneyDuration) {
        this.sJourneyDuration = sJourneyDuration;
    }

    public String getsFare() {
        return sFare;
    }

    public void setsFare(String sFare) {
        this.sFare = sFare;
    }

    public String getsNewJourneyDate() {
        return sNewJourneyDate;
    }

    public void setsNewJourneyDate(String sNewJourneyDate) {
        this.sNewJourneyDate = sNewJourneyDate;
    }

    public String getsIsEnabled() {
        return sIsEnabled;
    }

    public void setsIsEnabled(String sIsEnabled) {
        this.sIsEnabled = sIsEnabled;
    }

    public String getsSeatAvailable() {
        return sSeatAvailable;
    }

    public void setsSeatAvailable(String sSeatAvailable) {
        this.sSeatAvailable = sSeatAvailable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sBusOperators);
        dest.writeString(this.sBusNumber);
        dest.writeString(this.sBusID);
        dest.writeString(this.sBusType);
        dest.writeString(this.sDeparture);
        dest.writeString(this.sArrival);
        dest.writeString(this.sJourneyDuration);
        dest.writeString(this.sFare);
        dest.writeString(this.sNewJourneyDate);
        dest.writeString(this.sIsEnabled);
        dest.writeString(this.sSeatAvailable);
    }

    protected PassengerBus(Parcel in) {
        this.sBusOperators = in.readString();
        this.sBusNumber = in.readString();
        this.sBusID = in.readString();
        this.sBusType = in.readString();
        this.sDeparture = in.readString();
        this.sArrival = in.readString();
        this.sJourneyDuration = in.readString();
        this.sFare = in.readString();
        this.sNewJourneyDate = in.readString();
        this.sIsEnabled = in.readString();
        this.sSeatAvailable = in.readString();
    }

    public static final Creator<PassengerBus> CREATOR = new Creator<PassengerBus>() {
        @Override
        public PassengerBus createFromParcel(Parcel source) {
            return new PassengerBus(source);
        }

        @Override
        public PassengerBus[] newArray(int size) {
            return new PassengerBus[size];
        }
    };
}
