package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AvailableBuses implements Parcelable {
    @SerializedName("sub_id")
    @Expose
    public  String sub_id;

    @SerializedName("dep_time")
    @Expose
    public   String dep_time;

    @SerializedName("arrvl_time")
    @Expose
    public  String arrvl_time;

    @SerializedName("journey_duration")
    @Expose
    public String journey_duration;

    @SerializedName("journey_kms")
    @Expose
    public  String journey_kms;

    @SerializedName("tdi_id")
    @Expose
    public  String tdi_id;

    @SerializedName("avail_seats")
    @Expose
    public  String avail_seats;

    @SerializedName("bus_type")
    @Expose
    public String bus_type;

    @SerializedName("is_wifi")
    @Expose
    public String is_wifi;

    @SerializedName("is_usb_charging")
    @Expose
    public  String is_usb_charging;

    @SerializedName("is_tv")
    @Expose
    public String is_tv;


    @SerializedName("is_ac")
    @Expose
    public String is_ac;

    @SerializedName("is_survlnc_camera")
    @Expose
    public String is_survlnc_camera;

    @SerializedName("is_sleeper_class")
    @Expose
    public String is_sleeper_class;

    @SerializedName("is_double_decker")
    @Expose
    public  String is_double_decker;

    @SerializedName("bus_name")
    @Expose
    public String bus_name;

    @SerializedName("min_fare")
    @Expose
    public String min_fare;
    @SerializedName("avail_currencies")
    @Expose
    public ArrayList<AvailableCurrecncy> availableCurrenciesArrayList;
    @SerializedName("Boarding_Points")
    @Expose
    public String[] strBoardingPoints;
    @SerializedName("Dropping_Points")
    @Expose
    public String[] strDropingPoints;

    @SerializedName("lb_id")
    @Expose
    public String lb_id;

    @SerializedName("key")
    @Expose
    public String key;

    @SerializedName("pbi_id")
    @Expose
    public String pbi_id;

    @SerializedName("asi_id")
    @Expose
    public String asi_id;

    @SerializedName("arrival_date")
    @Expose
    public String arrival_date;


    @SerializedName("bus_operator")
    @Expose
    public String bus_operator;

    public AvailableBuses(String sub_id, String dep_time, String arrvl_time, String journey_duration, String journey_kms, String tdi_id, String avail_seats, String bus_type, String is_wifi, String is_usb_charging, String is_tv, String is_ac, String is_survlnc_camera, String is_sleeper_class, String is_double_decker, String bus_name, String min_fare, ArrayList<AvailableCurrecncy> availableCurrenciesArrayList, String[] strBoardingPoints, String[] strDropingPoints, String lb_id, String key, String pbi_id, String asi_id, String arrival_date, String bus_operator) {
        this.sub_id = sub_id;
        this.dep_time = dep_time;
        this.arrvl_time = arrvl_time;
        this.journey_duration = journey_duration;
        this.journey_kms = journey_kms;
        this.tdi_id = tdi_id;
        this.avail_seats = avail_seats;
        this.bus_type = bus_type;
        this.is_wifi = is_wifi;
        this.is_usb_charging = is_usb_charging;
        this.is_tv = is_tv;
        this.is_ac = is_ac;
        this.is_survlnc_camera = is_survlnc_camera;
        this.is_sleeper_class = is_sleeper_class;
        this.is_double_decker = is_double_decker;
        this.bus_name = bus_name;
        this.min_fare = min_fare;
        this.availableCurrenciesArrayList = availableCurrenciesArrayList;
        this.strBoardingPoints = strBoardingPoints;
        this.strDropingPoints = strDropingPoints;
        this.lb_id = lb_id;
        this.key = key;
        this.pbi_id = pbi_id;
        this.asi_id = asi_id;
        this.arrival_date = arrival_date;
        this.bus_operator = bus_operator;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getBus_operator() {
        return bus_operator;
    }

    public void setBus_operator(String bus_operator) {
        this.bus_operator = bus_operator;
    }

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getDep_time() {
        return dep_time;
    }

    public void setDep_time(String dep_time) {
        this.dep_time = dep_time;
    }

    public String getArrvl_time() {
        return arrvl_time;
    }

    public void setArrvl_time(String arrvl_time) {
        this.arrvl_time = arrvl_time;
    }

    public String getJourney_duration() {
        return journey_duration;
    }

    public void setJourney_duration(String journey_duration) {
        this.journey_duration = journey_duration;
    }

    public String getJourney_kms() {
        return journey_kms;
    }

    public void setJourney_kms(String journey_kms) {
        this.journey_kms = journey_kms;
    }

    public String getTdi_id() {
        return tdi_id;
    }

    public void setTdi_id(String tdi_id) {
        this.tdi_id = tdi_id;
    }

    public String getAvail_seats() {
        return avail_seats;
    }

    public void setAvail_seats(String avail_seats) {
        this.avail_seats = avail_seats;
    }

    public String getBus_type() {
        return bus_type;
    }

    public void setBus_type(String bus_type) {
        this.bus_type = bus_type;
    }

    public String getIs_wifi() {
        return is_wifi;
    }

    public void setIs_wifi(String is_wifi) {
        this.is_wifi = is_wifi;
    }

    public String getIs_usb_charging() {
        return is_usb_charging;
    }

    public void setIs_usb_charging(String is_usb_charging) {
        this.is_usb_charging = is_usb_charging;
    }

    public String getIs_tv() {
        return is_tv;
    }

    public void setIs_tv(String is_tv) {
        this.is_tv = is_tv;
    }

    public String getIs_ac() {
        return is_ac;
    }

    public void setIs_ac(String is_ac) {
        this.is_ac = is_ac;
    }

    public String getIs_survlnc_camera() {
        return is_survlnc_camera;
    }

    public void setIs_survlnc_camera(String is_survlnc_camera) {
        this.is_survlnc_camera = is_survlnc_camera;
    }

    public String getIs_sleeper_class() {
        return is_sleeper_class;
    }

    public void setIs_sleeper_class(String is_sleeper_class) {
        this.is_sleeper_class = is_sleeper_class;
    }

    public String getIs_double_decker() {
        return is_double_decker;
    }

    public void setIs_double_decker(String is_double_decker) {
        this.is_double_decker = is_double_decker;
    }

    public String getBus_name() {
        return bus_name;
    }

    public void setBus_name(String bus_name) {
        this.bus_name = bus_name;
    }

    public String getMin_fare() {
        return min_fare;
    }

    public void setMin_fare(String min_fare) {
        this.min_fare = min_fare;
    }

    public ArrayList<AvailableCurrecncy> getAvailableCurrenciesArrayList() {
        return availableCurrenciesArrayList;
    }

    public void setAvailableCurrenciesArrayList(ArrayList<AvailableCurrecncy> availableCurrenciesArrayList) {
        this.availableCurrenciesArrayList = availableCurrenciesArrayList;
    }

    public String[] getStrBoardingPoints() {
        return strBoardingPoints;
    }

    public void setStrBoardingPoints(String[] strBoardingPoints) {
        this.strBoardingPoints = strBoardingPoints;
    }

    public String[] getStrDropingPoints() {
        return strDropingPoints;
    }

    public void setStrDropingPoints(String[] strDropingPoints) {
        this.strDropingPoints = strDropingPoints;
    }

    public String getLb_id() {
        return lb_id;
    }

    public void setLb_id(String lb_id) {
        this.lb_id = lb_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPbi_id() {
        return pbi_id;
    }

    public void setPbi_id(String pbi_id) {
        this.pbi_id = pbi_id;
    }

    public String getAsi_id() {
        return asi_id;
    }

    public void setAsi_id(String asi_id) {
        this.asi_id = asi_id;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sub_id);
        dest.writeString(this.dep_time);
        dest.writeString(this.arrvl_time);
        dest.writeString(this.journey_duration);
        dest.writeString(this.journey_kms);
        dest.writeString(this.tdi_id);
        dest.writeString(this.avail_seats);
        dest.writeString(this.bus_type);
        dest.writeString(this.is_wifi);
        dest.writeString(this.is_usb_charging);
        dest.writeString(this.is_tv);
        dest.writeString(this.is_ac);
        dest.writeString(this.is_survlnc_camera);
        dest.writeString(this.is_sleeper_class);
        dest.writeString(this.is_double_decker);
        dest.writeString(this.bus_name);
        dest.writeString(this.min_fare);
        dest.writeTypedList(this.availableCurrenciesArrayList);
        dest.writeStringArray(this.strBoardingPoints);
        dest.writeStringArray(this.strDropingPoints);
        dest.writeString(this.lb_id);
        dest.writeString(this.key);
        dest.writeString(this.pbi_id);
        dest.writeString(this.asi_id);
        dest.writeString(this.arrival_date);
        dest.writeString(this.bus_operator);
    }

    protected AvailableBuses(Parcel in) {
        this.sub_id = in.readString();
        this.dep_time = in.readString();
        this.arrvl_time = in.readString();
        this.journey_duration = in.readString();
        this.journey_kms = in.readString();
        this.tdi_id = in.readString();
        this.avail_seats = in.readString();
        this.bus_type = in.readString();
        this.is_wifi = in.readString();
        this.is_usb_charging = in.readString();
        this.is_tv = in.readString();
        this.is_ac = in.readString();
        this.is_survlnc_camera = in.readString();
        this.is_sleeper_class = in.readString();
        this.is_double_decker = in.readString();
        this.bus_name = in.readString();
        this.min_fare = in.readString();
        this.availableCurrenciesArrayList = in.createTypedArrayList(AvailableCurrecncy.CREATOR);
        this.strBoardingPoints = in.createStringArray();
        this.strDropingPoints = in.createStringArray();
        this.lb_id = in.readString();
        this.key = in.readString();
        this.pbi_id = in.readString();
        this.asi_id = in.readString();
        this.arrival_date = in.readString();
        this.bus_operator = in.readString();
    }

    public static final Creator<AvailableBuses> CREATOR = new Creator<AvailableBuses>() {
        @Override
        public AvailableBuses createFromParcel(Parcel source) {
            return new AvailableBuses(source);
        }

        @Override
        public AvailableBuses[] newArray(int size) {
            return new AvailableBuses[size];
        }
    };
}
