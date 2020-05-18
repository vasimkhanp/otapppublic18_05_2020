package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

public class NormalTicketInfo implements Parcelable {

    private String sPassengerDetails;
    private String sPassengerPhoneNo;
    private String sEmail;
    private String sFromId;
    private String sToId;
    private String sNewJourneyDate;
    private String sBusRoutSubSeatId;
    private String sBusID;
    private String sUKey;
    private String seatCount;
    private String sFare;
    private String sLanguage;
    private String sDate;
    private BookTicketResponse bookTicketResponse;

    public NormalTicketInfo(String sPassengerDetails, String sPassengerPhoneNo, String sEmail,
                            String sFromId, String sToId, String sNewJourneyDate,
                            String sBusRoutSubSeatId, String sBusID, String sUKey, String seatCount,
                            String sFare, String sLanguage, String sDate, BookTicketResponse bookTicketResponse) {
        this.sPassengerDetails = sPassengerDetails;
        this.sPassengerPhoneNo = sPassengerPhoneNo;
        this.sEmail = sEmail;
        this.sFromId = sFromId;
        this.sToId = sToId;
        this.sNewJourneyDate = sNewJourneyDate;
        this.sBusRoutSubSeatId = sBusRoutSubSeatId;
        this.sBusID = sBusID;
        this.sUKey = sUKey;
        this.seatCount = seatCount;
        this.sFare = sFare;
        this.sLanguage = sLanguage;
        this.sDate = sDate;
        this.bookTicketResponse = bookTicketResponse;
    }

    public String getsPassengerDetails() {
        return sPassengerDetails;
    }

    public void setsPassengerDetails(String sPassengerDetails) {
        this.sPassengerDetails = sPassengerDetails;
    }

    public String getsPassengerPhoneNo() {
        return sPassengerPhoneNo;
    }

    public void setsPassengerPhoneNo(String sPassengerPhoneNo) {
        this.sPassengerPhoneNo = sPassengerPhoneNo;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
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

    public String getsNewJourneyDate() {
        return sNewJourneyDate;
    }

    public void setsNewJourneyDate(String sNewJourneyDate) {
        this.sNewJourneyDate = sNewJourneyDate;
    }

    public String getsBusRoutSubSeatId() {
        return sBusRoutSubSeatId;
    }

    public void setsBusRoutSubSeatId(String sBusRoutSubSeatId) {
        this.sBusRoutSubSeatId = sBusRoutSubSeatId;
    }

    public String getsBusID() {
        return sBusID;
    }

    public void setsBusID(String sBusID) {
        this.sBusID = sBusID;
    }

    public String getsUKey() {
        return sUKey;
    }

    public void setsUKey(String sUKey) {
        this.sUKey = sUKey;
    }

    public String getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(String seatCount) {
        this.seatCount = seatCount;
    }

    public String getsFare() {
        return sFare;
    }

    public void setsFare(String sFare) {
        this.sFare = sFare;
    }

    public String getsLanguage() {
        return sLanguage;
    }

    public void setsLanguage(String sLanguage) {
        this.sLanguage = sLanguage;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public BookTicketResponse getBookTicketResponse() {
        return bookTicketResponse;
    }

    public void setBookTicketResponse(BookTicketResponse bookTicketResponse) {
        this.bookTicketResponse = bookTicketResponse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sPassengerDetails);
        dest.writeString(this.sPassengerPhoneNo);
        dest.writeString(this.sEmail);
        dest.writeString(this.sFromId);
        dest.writeString(this.sToId);
        dest.writeString(this.sNewJourneyDate);
        dest.writeString(this.sBusRoutSubSeatId);
        dest.writeString(this.sBusID);
        dest.writeString(this.sUKey);
        dest.writeString(this.seatCount);
        dest.writeString(this.sFare);
        dest.writeString(this.sLanguage);
        dest.writeString(this.sDate);
        dest.writeParcelable(this.bookTicketResponse, flags);
    }

    protected NormalTicketInfo(Parcel in) {
        this.sPassengerDetails = in.readString();
        this.sPassengerPhoneNo = in.readString();
        this.sEmail = in.readString();
        this.sFromId = in.readString();
        this.sToId = in.readString();
        this.sNewJourneyDate = in.readString();
        this.sBusRoutSubSeatId = in.readString();
        this.sBusID = in.readString();
        this.sUKey = in.readString();
        this.seatCount = in.readString();
        this.sFare = in.readString();
        this.sLanguage = in.readString();
        this.sDate = in.readString();
        this.bookTicketResponse = in.readParcelable(BookTicketResponse.class.getClassLoader());
    }

    public static final Creator<NormalTicketInfo> CREATOR = new Creator<NormalTicketInfo>() {
        @Override
        public NormalTicketInfo createFromParcel(Parcel source) {
            return new NormalTicketInfo(source);
        }

        @Override
        public NormalTicketInfo[] newArray(int size) {
            return new NormalTicketInfo[size];
        }
    };
}
