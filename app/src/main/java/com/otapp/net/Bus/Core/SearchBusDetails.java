package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchBusDetails implements Parcelable {

    private String sFrom;
    private String sTo;
    private String sDate;
    private String sFromId;
    private String sToId;
    private boolean isReturn = false;
    private String sReturnDate = "";
    private String sBoarding = "";
    private String sDropping = "";
    private String sReturnBoarding = "";
    private String sReturnDropping = "";
    private String sReturnFrom;
    private String sReturnTo;
    private String sReturnFromId;
    private String sReturnToId;
    private AvailableBuses availableBuses;
    private Station stationFrom;
    private Station stationTo;
    private AvailableBuses availableReturnBuses;

    private ArrayList<Seat> arrayListOnWords;
    private ArrayList<Seat> arrayListReturn;
    private boolean retrunActivityCalled=false;
    private String diffNoOfSeatAllowed;
    private String sContactPersonName;
    private String sContactEmailAdd;
    private String sContactPhone;


    public SearchBusDetails(String sFrom, String sTo, String sDate, String sFromId, String sToId, boolean isReturn, String sReturnDate, String sBoarding, String sDropping, String sReturnBoarding, String sReturnDropping, String sReturnFrom, String sReturnTo, String sReturnFromId, String sReturnToId, AvailableBuses availableBuses, Station stationFrom, Station stationTo) {
        this.sFrom = sFrom;
        this.sTo = sTo;
        this.sDate = sDate;
        this.sFromId = sFromId;
        this.sToId = sToId;
        this.isReturn = isReturn;
        this.sReturnDate = sReturnDate;
        this.sBoarding = sBoarding;
        this.sDropping = sDropping;
        this.sReturnBoarding = sReturnBoarding;
        this.sReturnDropping = sReturnDropping;
        this.sReturnFrom = sReturnFrom;
        this.sReturnTo = sReturnTo;
        this.sReturnFromId = sReturnFromId;
        this.sReturnToId = sReturnToId;
        this.availableBuses = availableBuses;
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
    }


    public ArrayList<Seat> getArrayListOnWords() {
        return arrayListOnWords;
    }

    public void setArrayListOnWords(ArrayList<Seat> arrayListOnWords) {
        this.arrayListOnWords = arrayListOnWords;
    }

    public ArrayList<Seat> getArrayListReturn() {
        return arrayListReturn;
    }

    public void setArrayListReturn(ArrayList<Seat> arrayListReturn) {
        this.arrayListReturn = arrayListReturn;
    }

    public String getsFrom() {
        return sFrom;
    }

    public void setsFrom(String sFrom) {
        this.sFrom = sFrom;
    }

    public String getsTo() {
        return sTo;
    }

    public void setsTo(String sTo) {
        this.sTo = sTo;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String getsFromId() {
        return sFromId;
    }

    public void setsFromId(String sFromId) {
        this.sFromId = sFromId;
    }

    public String getsToId() {
        return sToId;
    }

    public void setsToId(String sToId) {
        this.sToId = sToId;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public String getsReturnDate() {
        return sReturnDate;
    }

    public void setsReturnDate(String sReturnDate) {
        this.sReturnDate = sReturnDate;
    }

    public String getsBoarding() {
        return sBoarding;
    }

    public void setsBoarding(String sBoarding) {
        this.sBoarding = sBoarding;
    }

    public String getsDropping() {
        return sDropping;
    }

    public void setsDropping(String sDropping) {
        this.sDropping = sDropping;
    }

    public String getsReturnBoarding() {
        return sReturnBoarding;
    }

    public void setsReturnBoarding(String sReturnBoarding) {
        this.sReturnBoarding = sReturnBoarding;
    }

    public String getsReturnDropping() {
        return sReturnDropping;
    }

    public void setsReturnDropping(String sReturnDropping) {
        this.sReturnDropping = sReturnDropping;
    }

    public String getsReturnFrom() {
        return sReturnFrom;
    }

    public void setsReturnFrom(String sReturnFrom) {
        this.sReturnFrom = sReturnFrom;
    }

    public String getsReturnTo() {
        return sReturnTo;
    }

    public void setsReturnTo(String sReturnTo) {
        this.sReturnTo = sReturnTo;
    }

    public String getsReturnFromId() {
        return sReturnFromId;
    }

    public void setsReturnFromId(String sReturnFromId) {
        this.sReturnFromId = sReturnFromId;
    }

    public String getsReturnToId() {
        return sReturnToId;
    }

    public void setsReturnToId(String sReturnToId) {
        this.sReturnToId = sReturnToId;
    }

    public AvailableBuses getAvailableBuses() {
        return availableBuses;
    }

    public void setAvailableBuses(AvailableBuses availableBuses) {
        this.availableBuses = availableBuses;
    }

    public Station getStationFrom() {
        return stationFrom;
    }

    public void setStationFrom(Station stationFrom) {
        this.stationFrom = stationFrom;
    }

    public Station getStationTo() {
        return stationTo;
    }

    public void setStationTo(Station stationTo) {
        this.stationTo = stationTo;
    }

    public boolean isRetrunActivityCalled() {
        return retrunActivityCalled;
    }

    public void setRetrunActivityCalled(boolean retrunActivityCalled) {
        this.retrunActivityCalled = retrunActivityCalled;
    }

    public AvailableBuses getAvailableReturnBuses() {
        return availableReturnBuses;
    }

    public void setAvailableReturnBuses(AvailableBuses availableReturnBuses) {
        this.availableReturnBuses = availableReturnBuses;
    }

    public String getDiffNoOfSeatAllowed() {
        return diffNoOfSeatAllowed;
    }

    public void setDiffNoOfSeatAllowed(String diffNoOfSeatAllowed) {
        this.diffNoOfSeatAllowed = diffNoOfSeatAllowed;
    }

    public String getsContactPersonName() {
        return sContactPersonName;
    }

    public void setsContactPersonName(String sContactPersonName) {
        this.sContactPersonName = sContactPersonName;
    }

    public String getsContactEmailAdd() {
        return sContactEmailAdd;
    }

    public void setsContactEmailAdd(String sContactEmailAdd) {
        this.sContactEmailAdd = sContactEmailAdd;
    }

    public String getsContactPhone() {
        return sContactPhone;
    }

    public void setsContactPhone(String sContactPhone) {
        this.sContactPhone = sContactPhone;
    }
    /*public SearchBusDetails(String sFrom, String sTo, String sDate, String sFromId, String sToId,
                            boolean isReturn, String sReturnDate, String sBoarding, String sDropping, String sReturnBoarding, String sReturnDropping) {
        this.sFrom = sFrom;
        this.sTo = sTo;
        this.sDate = sDate;
        this.sFromId = sFromId;
        this.sToId = sToId;
        this.isReturn = isReturn;
        this.sReturnDate = sReturnDate;
        this.sBoarding = sBoarding;
        this.sDropping = sDropping;
        this.sReturnBoarding = sReturnBoarding;
        this.sReturnDropping = sReturnDropping;
    }*/


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sFrom);
        dest.writeString(this.sTo);
        dest.writeString(this.sDate);
        dest.writeString(this.sFromId);
        dest.writeString(this.sToId);
        dest.writeByte(this.isReturn ? (byte) 1 : (byte) 0);
        dest.writeString(this.sReturnDate);
        dest.writeString(this.sBoarding);
        dest.writeString(this.sDropping);
        dest.writeString(this.sReturnBoarding);
        dest.writeString(this.sReturnDropping);
        dest.writeString(this.sReturnFrom);
        dest.writeString(this.sReturnTo);
        dest.writeString(this.sReturnFromId);
        dest.writeString(this.sReturnToId);
        dest.writeParcelable(this.availableBuses, flags);
        dest.writeParcelable(this.stationFrom, flags);
        dest.writeParcelable(this.stationTo, flags);
        dest.writeParcelable(this.availableReturnBuses, flags);
        dest.writeTypedList(this.arrayListOnWords);
        dest.writeTypedList(this.arrayListReturn);
        dest.writeByte(this.retrunActivityCalled ? (byte) 1 : (byte) 0);
        dest.writeString(this.diffNoOfSeatAllowed);
        dest.writeString(this.sContactPersonName);
        dest.writeString(this.sContactEmailAdd);
        dest.writeString(this.sContactPhone);
    }

    protected SearchBusDetails(Parcel in) {
        this.sFrom = in.readString();
        this.sTo = in.readString();
        this.sDate = in.readString();
        this.sFromId = in.readString();
        this.sToId = in.readString();
        this.isReturn = in.readByte() != 0;
        this.sReturnDate = in.readString();
        this.sBoarding = in.readString();
        this.sDropping = in.readString();
        this.sReturnBoarding = in.readString();
        this.sReturnDropping = in.readString();
        this.sReturnFrom = in.readString();
        this.sReturnTo = in.readString();
        this.sReturnFromId = in.readString();
        this.sReturnToId = in.readString();
        this.availableBuses = in.readParcelable(AvailableBuses.class.getClassLoader());
        this.stationFrom = in.readParcelable(Station.class.getClassLoader());
        this.stationTo = in.readParcelable(Station.class.getClassLoader());
        this.availableReturnBuses = in.readParcelable(AvailableBuses.class.getClassLoader());
        this.arrayListOnWords = in.createTypedArrayList(Seat.CREATOR);
        this.arrayListReturn = in.createTypedArrayList(Seat.CREATOR);
        this.retrunActivityCalled = in.readByte() != 0;
        this.diffNoOfSeatAllowed = in.readString();
        this.sContactPersonName = in.readString();
        this.sContactEmailAdd = in.readString();
        this.sContactPhone = in.readString();
    }

    public static final Creator<SearchBusDetails> CREATOR = new Creator<SearchBusDetails>() {
        @Override
        public SearchBusDetails createFromParcel(Parcel source) {
            return new SearchBusDetails(source);
        }

        @Override
        public SearchBusDetails[] newArray(int size) {
            return new SearchBusDetails[size];
        }
    };
}
