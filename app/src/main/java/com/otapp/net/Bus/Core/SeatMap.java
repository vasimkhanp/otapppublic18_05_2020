package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SeatMap implements Parcelable {

    @SerializedName("Status")
    int Status;
    @SerializedName("message")
    String sMessage;
    @SerializedName("busid")
    String sBusId;
    @SerializedName("route_id")
    String sRouteId;
    @SerializedName("sub_route_id")
    String sSubRouteId;
    @SerializedName("bus_route_id")
    String sBusRouteId;
    @SerializedName("bus_route_schedule_id")
    String sBusRouteScheduleId;
    @SerializedName("fromID")
    String sFromID;
    @SerializedName("toID")
    String sToId;
    @SerializedName("journeyDate")
    String sJourneyDate;
    @SerializedName("bus_route_seat_id")
    String sBusRouteSeatId;
    @SerializedName("TotalSeats")
    String sTotalSeats;
    @SerializedName("boarding_time")
    String sBoardingTime;
    @SerializedName("Seat_Row_count")
    String sRowCount;
    @SerializedName("seatDetails")
    private ArrayList<Object> seatArrayList;
    private String sAvailableSeats;
    private String sProcessingSeats;

    public SeatMap(int status, String sMessage, String sBusId, String sRouteId, String sSubRouteId,
                   String sBusRouteId, String sBusRouteScheduleId, String sFromID, String sToId,
                   String sJourneyDate, String sBusRouteSeatId, String sTotalSeats, String sBoardingTime,
                   String sRowCount, ArrayList<Object> seatArrayList, String sAvailableSeats, String sProcessingSeats) {
        Status = status;
        this.sMessage = sMessage;
        this.sBusId = sBusId;
        this.sRouteId = sRouteId;
        this.sSubRouteId = sSubRouteId;
        this.sBusRouteId = sBusRouteId;
        this.sBusRouteScheduleId = sBusRouteScheduleId;
        this.sFromID = sFromID;
        this.sToId = sToId;
        this.sJourneyDate = sJourneyDate;
        this.sBusRouteSeatId = sBusRouteSeatId;
        this.sTotalSeats = sTotalSeats;
        this.sBoardingTime = sBoardingTime;
        this.sRowCount = sRowCount;
        this.seatArrayList = seatArrayList;
        this.sAvailableSeats = sAvailableSeats;
        this.sProcessingSeats = sProcessingSeats;
    }

    public SeatMap() {

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

    public String getsBusId() {
        return sBusId;
    }

    public void setsBusId(String sBusId) {
        this.sBusId = sBusId;
    }

    public String getsRouteId() {
        return sRouteId;
    }

    public void setsRouteId(String sRouteId) {
        this.sRouteId = sRouteId;
    }

    public String getsSubRouteId() {
        return sSubRouteId;
    }

    public void setsSubRouteId(String sSubRouteId) {
        this.sSubRouteId = sSubRouteId;
    }

    public String getsBusRouteId() {
        return sBusRouteId;
    }

    public void setsBusRouteId(String sBusRouteId) {
        this.sBusRouteId = sBusRouteId;
    }

    public String getsBusRouteScheduleId() {
        return sBusRouteScheduleId;
    }

    public void setsBusRouteScheduleId(String sBusRouteScheduleId) {
        this.sBusRouteScheduleId = sBusRouteScheduleId;
    }

    public String getsFromID() {
        return sFromID;
    }

    public void setsFromID(String sFromID) {
        this.sFromID = sFromID;
    }

    public String getsToId() {
        return sToId;
    }

    public void setsToId(String sToId) {
        this.sToId = sToId;
    }

    public String getsJourneyDate() {
        return sJourneyDate;
    }

    public void setsJourneyDate(String sJourneyDate) {
        this.sJourneyDate = sJourneyDate;
    }

    public String getsBusRouteSeatId() {
        return sBusRouteSeatId;
    }

    public void setsBusRouteSeatId(String sBusRouteSeatId) {
        this.sBusRouteSeatId = sBusRouteSeatId;
    }

    public String getsTotalSeats() {
        return sTotalSeats;
    }

    public void setsTotalSeats(String sTotalSeats) {
        this.sTotalSeats = sTotalSeats;
    }

    public String getsBoardingTime() {
        return sBoardingTime;
    }

    public void setsBoardingTime(String sBoardingTime) {
        this.sBoardingTime = sBoardingTime;
    }

    public String getsRowCount() {
        return sRowCount;
    }

    public void setsRowCount(String sRowCount) {
        this.sRowCount = sRowCount;
    }

    public ArrayList<Object> getSeatArrayList() {
        return seatArrayList;
    }

    public void setSeatArrayList(ArrayList<Object> seatArrayList) {
        this.seatArrayList = seatArrayList;
    }

    public String getsAvailableSeats() {
        return sAvailableSeats;
    }

    public void setsAvailableSeats(String sAvailableSeats) {
        this.sAvailableSeats = sAvailableSeats;
    }

    public String getsProcessingSeats() {
        return sProcessingSeats;
    }

    public void setsProcessingSeats(String sProcessingSeats) {
        this.sProcessingSeats = sProcessingSeats;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Status);
        dest.writeString(this.sMessage);
        dest.writeString(this.sBusId);
        dest.writeString(this.sRouteId);
        dest.writeString(this.sSubRouteId);
        dest.writeString(this.sBusRouteId);
        dest.writeString(this.sBusRouteScheduleId);
        dest.writeString(this.sFromID);
        dest.writeString(this.sToId);
        dest.writeString(this.sJourneyDate);
        dest.writeString(this.sBusRouteSeatId);
        dest.writeString(this.sTotalSeats);
        dest.writeString(this.sBoardingTime);
        dest.writeString(this.sRowCount);
        dest.writeList(this.seatArrayList);
        dest.writeString(this.sAvailableSeats);
        dest.writeString(this.sProcessingSeats);
    }

    protected SeatMap(Parcel in) {
        this.Status = in.readInt();
        this.sMessage = in.readString();
        this.sBusId = in.readString();
        this.sRouteId = in.readString();
        this.sSubRouteId = in.readString();
        this.sBusRouteId = in.readString();
        this.sBusRouteScheduleId = in.readString();
        this.sFromID = in.readString();
        this.sToId = in.readString();
        this.sJourneyDate = in.readString();
        this.sBusRouteSeatId = in.readString();
        this.sTotalSeats = in.readString();
        this.sBoardingTime = in.readString();
        this.sRowCount = in.readString();
        this.seatArrayList = new ArrayList<Object>();
        in.readList(this.seatArrayList, Object.class.getClassLoader());
        this.sAvailableSeats = in.readString();
        this.sProcessingSeats = in.readString();
    }

    public static final Creator<SeatMap> CREATOR = new Creator<SeatMap>() {
        @Override
        public SeatMap createFromParcel(Parcel source) {
            return new SeatMap(source);
        }

        @Override
        public SeatMap[] newArray(int size) {
            return new SeatMap[size];
        }
    };
}






















