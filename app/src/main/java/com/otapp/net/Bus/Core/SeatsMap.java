package com.otapp.net.Bus.Core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SeatsMap {
    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("lower_seat_map")
    @Expose
    public ArrayList<Object> lowerSeatMapArrayList;

    @SerializedName("upper_seat_map")
    @Expose
    public ArrayList<Object> upperSeatMapArrayList;

    @SerializedName("process_seats")
    @Expose
    public  String process_seats;

    @SerializedName("available_seats")
    @Expose
    public  String available_seats;

    @SerializedName("no_of_lower_seat_map_row")
    @Expose
    public String no_of_lower_seat_map_row;

    @SerializedName("no_of_upper_seat_map_row")
    @Expose
    public String no_of_upper_seat_map_row;

    @SerializedName("ukey")
    @Expose
    public  String ukey;

    @SerializedName("max_allowed_seats_per_booking")
    @Expose
    public String max_allowed_seats_per_booking;

    @SerializedName("seat_types")
    @Expose
    ArrayList<SeatType> seatTypesArrayList;

   /* public class SeatTypes{
        @SerializedName("seat_type_id")
        @Expose
        public String seat_type_id;

        @SerializedName("seat_type_name")
        @Expose
        public String seat_type_name;

        @SerializedName("seats")
        @Expose
        public String seats;
    }*/

    public SeatsMap(int status, /*ArrayList<Object> lowerSeatMapArrayList, ArrayList<Object> upperSeatMapArrayList,*/ String process_seats, String available_seats, String no_of_lower_seat_map_row, String no_of_upper_seat_map_row, String ukey, String max_allowed_seats_per_booking, ArrayList<SeatType> seatTypesArrayList) {
        this.status = status;
       /* this.lowerSeatMapArrayList = lowerSeatMapArrayList;
        this.upperSeatMapArrayList = upperSeatMapArrayList;*/
        this.process_seats = process_seats;
        this.available_seats = available_seats;
        this.no_of_lower_seat_map_row = no_of_lower_seat_map_row;
        this.no_of_upper_seat_map_row = no_of_upper_seat_map_row;
        this.ukey = ukey;
        this.max_allowed_seats_per_booking = max_allowed_seats_per_booking;
        this.seatTypesArrayList = seatTypesArrayList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

  /*  public ArrayList<Object> getLowerSeatMapArrayList() {
        return lowerSeatMapArrayList;
    }

    public void setLowerSeatMapArrayList(ArrayList<Object> lowerSeatMapArrayList) {
        this.lowerSeatMapArrayList = lowerSeatMapArrayList;
    }

    public ArrayList<Object> getUpperSeatMapArrayList() {
        return upperSeatMapArrayList;
    }

    public void setUpperSeatMapArrayList(ArrayList<Object> upperSeatMapArrayList) {
        this.upperSeatMapArrayList = upperSeatMapArrayList;
    }*/

    public String getProcess_seats() {
        return process_seats;
    }

    public void setProcess_seats(String process_seats) {
        this.process_seats = process_seats;
    }

    public String getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(String available_seats) {
        this.available_seats = available_seats;
    }

    public String getNo_of_lower_seat_map_row() {
        return no_of_lower_seat_map_row;
    }

    public void setNo_of_lower_seat_map_row(String no_of_lower_seat_map_row) {
        this.no_of_lower_seat_map_row = no_of_lower_seat_map_row;
    }

    public String getNo_of_upper_seat_map_row() {
        return no_of_upper_seat_map_row;
    }

    public void setNo_of_upper_seat_map_row(String no_of_upper_seat_map_row) {
        this.no_of_upper_seat_map_row = no_of_upper_seat_map_row;
    }

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }

    public String getMax_allowed_seats_per_booking() {
        return max_allowed_seats_per_booking;
    }

    public void setMax_allowed_seats_per_booking(String max_allowed_seats_per_booking) {
        this.max_allowed_seats_per_booking = max_allowed_seats_per_booking;
    }

    public ArrayList<SeatType> getSeatTypesArrayList() {
        return seatTypesArrayList;
    }

    public void setSeatTypesArrayList(ArrayList<SeatType> seatTypesArrayList) {
        this.seatTypesArrayList = seatTypesArrayList;
    }

}
