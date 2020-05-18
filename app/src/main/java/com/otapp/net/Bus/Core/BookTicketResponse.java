package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BookTicketResponse implements Parcelable {

    @SerializedName("status")
    private int Status;
    @SerializedName("message")
    private String sMessage;
    @SerializedName("ticketno")
    String sTicketNo;
    @SerializedName("singleticketfare")
    String sSingleTicketFare;
    @SerializedName("totalfare")
    String stotalFare;
    @SerializedName("departuretime")
    String sDepartureTime;
    @SerializedName("bustype")
    String sBusType;
    @SerializedName("busoperator")
    String sBusOperator;
    @SerializedName("busno")
    String sBusNo;
    @SerializedName("booking_time")
    String sBookingTime;
    @SerializedName("header")
    String sHeader;
    @SerializedName("footer")
    String sFooter;

    public BookTicketResponse(int status, String sMessage, String sTicketNo, String sSingleTicketFare,
                              String stotalFare, String sDepartureTime, String sBusType, String sBusOperator,
                              String sBusNo, String sBookingTime) {
        Status = status;
        this.sMessage = sMessage;
        this.sTicketNo = sTicketNo;
        this.sSingleTicketFare = sSingleTicketFare;
        this.stotalFare = stotalFare;
        this.sDepartureTime = sDepartureTime;
        this.sBusType = sBusType;
        this.sBusOperator = sBusOperator;
        this.sBusNo = sBusNo;
        this.sBookingTime = sBookingTime;
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

    public String getsTicketNo() {
        return sTicketNo;
    }

    public void setsTicketNo(String sTicketNo) {
        this.sTicketNo = sTicketNo;
    }

    public String getsSingleTicketFare() {
        return sSingleTicketFare;
    }

    public void setsSingleTicketFare(String sSingleTicketFare) {
        this.sSingleTicketFare = sSingleTicketFare;
    }

    public String getStotalFare() {
        return stotalFare;
    }

    public void setStotalFare(String stotalFare) {
        this.stotalFare = stotalFare;
    }

    public String getsDepartureTime() {
        return sDepartureTime;
    }

    public void setsDepartureTime(String sDepartureTime) {
        this.sDepartureTime = sDepartureTime;
    }

    public String getsBusType() {
        return sBusType;
    }

    public void setsBusType(String sBusType) {
        this.sBusType = sBusType;
    }

    public String getsBusOperator() {
        return sBusOperator;
    }

    public void setsBusOperator(String sBusOperator) {
        this.sBusOperator = sBusOperator;
    }

    public String getsBusNo() {
        return sBusNo;
    }

    public void setsBusNo(String sBusNo) {
        this.sBusNo = sBusNo;
    }

    public String getsBookingTime() {
        return sBookingTime;
    }

    public void setsBookingTime(String sBookingTime) {
        this.sBookingTime = sBookingTime;
    }

    public String getsHeader() {
        return sHeader;
    }

    public void setsHeader(String sHeader) {
        this.sHeader = sHeader;
    }

    public String getsFooter() {
        return sFooter;
    }

    public void setsFooter(String sFooter) {
        this.sFooter = sFooter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Status);
        dest.writeString(this.sMessage);
        dest.writeString(this.sTicketNo);
        dest.writeString(this.sSingleTicketFare);
        dest.writeString(this.stotalFare);
        dest.writeString(this.sDepartureTime);
        dest.writeString(this.sBusType);
        dest.writeString(this.sBusOperator);
        dest.writeString(this.sBusNo);
        dest.writeString(this.sBookingTime);
        dest.writeString(this.sHeader);
        dest.writeString(this.sFooter);
    }

    protected BookTicketResponse(Parcel in) {
        this.Status = in.readInt();
        this.sMessage = in.readString();
        this.sTicketNo = in.readString();
        this.sSingleTicketFare = in.readString();
        this.stotalFare = in.readString();
        this.sDepartureTime = in.readString();
        this.sBusType = in.readString();
        this.sBusOperator = in.readString();
        this.sBusNo = in.readString();
        this.sBookingTime = in.readString();
        this.sHeader = in.readString();
        this.sFooter = in.readString();
    }

    public static final Creator<BookTicketResponse> CREATOR = new Creator<BookTicketResponse>() {
        @Override
        public BookTicketResponse createFromParcel(Parcel source) {
            return new BookTicketResponse(source);
        }

        @Override
        public BookTicketResponse[] newArray(int size) {
            return new BookTicketResponse[size];
        }
    };
}
