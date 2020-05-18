package com.otapp.net.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class FlightCity implements Parcelable {

    public String from;
    public String to;
    public String fromCity;
    public String toCity;
    public String departDate;
    public String returnDate;
    public long displayDepartDate;
    public long displayReturnDate;
    public String clas;
    public String flightAuthToken;
    public String clasName;
    public String currencyName;
    public String traveller;
    public int adultCount = 0;
    public int childCount = 0;
    public int infantCount = 0;
    public int cnvFixedFee = 0;
    public int cnvPerFee = 0;
    public int type = -1;
    public boolean isInternationFlight = false;
    public List<FlightCity> mFlightCityList = null;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeString(this.fromCity);
        dest.writeString(this.toCity);
        dest.writeString(this.departDate);
        dest.writeString(this.returnDate);
        dest.writeLong(this.displayDepartDate);
        dest.writeLong(this.displayReturnDate);
        dest.writeString(this.clas);
        dest.writeString(this.flightAuthToken);
        dest.writeString(this.clasName);
        dest.writeString(this.currencyName);
        dest.writeString(this.traveller);
        dest.writeInt(this.adultCount);
        dest.writeInt(this.childCount);
        dest.writeInt(this.infantCount);
        dest.writeInt(this.cnvFixedFee);
        dest.writeInt(this.cnvPerFee);
        dest.writeInt(this.type);
        dest.writeByte(this.isInternationFlight ? (byte) 1 : (byte) 0);
        dest.writeList(this.mFlightCityList);
    }

    public FlightCity() {
    }

    protected FlightCity(Parcel in) {
        this.from = in.readString();
        this.to = in.readString();
        this.fromCity = in.readString();
        this.toCity = in.readString();
        this.departDate = in.readString();
        this.returnDate = in.readString();
        this.displayDepartDate = in.readLong();
        this.displayReturnDate = in.readLong();
        this.clas = in.readString();
        this.flightAuthToken = in.readString();
        this.clasName = in.readString();
        this.currencyName = in.readString();
        this.traveller = in.readString();
        this.adultCount = in.readInt();
        this.childCount = in.readInt();
        this.infantCount = in.readInt();
        this.cnvFixedFee = in.readInt();
        this.cnvPerFee = in.readInt();
        this.type = in.readInt();
        this.isInternationFlight = in.readByte() != 0;
        this.mFlightCityList = new ArrayList<FlightCity>();
        in.readList(this.mFlightCityList, FlightCity.class.getClassLoader());
    }

    public static final Parcelable.Creator<FlightCity> CREATOR = new Parcelable.Creator<FlightCity>() {
        @Override
        public FlightCity createFromParcel(Parcel source) {
            return new FlightCity(source);
        }

        @Override
        public FlightCity[] newArray(int size) {
            return new FlightCity[size];
        }
    };
}
