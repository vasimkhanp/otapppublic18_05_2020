package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("ukey")
    @Expose
    public String uKey;
    @SerializedName("data")
    @Expose
    public EventDetails data = null;

    public class EventDetails {

        @SerializedName("event_id")
        @Expose
        public String eventId;
        @SerializedName("event_name")
        @Expose
        public String eventName;
        @SerializedName("event_date")
        @Expose
        public List<String> eventDate = null;
        @SerializedName("event_time")
        @Expose
        public List<String> eventTime;
        @SerializedName("event_addr")
        @Expose
        public String eventAddr;
        @SerializedName("event_city")
        @Expose
        public String eventCity;
        @SerializedName("event_photo")
        @Expose
        public String eventPhoto;
        @SerializedName("event_category")
        @Expose
        public String eventCategory;
        @SerializedName("ticket_type")
        @Expose
        public List<TicketType> ticketType = null;
        @SerializedName("floors")
        @Expose
        public List<Floors> floors = null;
        @SerializedName("ticket_is_active")
        @Expose
        public String ticketIsActive;
        @SerializedName("no_of_tkt")
        @Expose
        public String noOfTkt;
        @SerializedName("event_language")
        @Expose
        public String eventLanguage;
        @SerializedName("event_price")
        @Expose
        public float eventPrice = 0;
        @SerializedName("ticket_currency")
        @Expose
        public String eventCurrency;
        @SerializedName("event_remaining_time")
        @Expose
        public String eventRemainingTime;
        public String eventSelectedDate = "";
        public String eventSelectedTime = "";
        @SerializedName("agent_id")
        @Expose
        public int agentId;
        @SerializedName("show_in")
        @Expose
        public int showIn;
        @SerializedName("IHF")
        @Expose
        public int IHF;
        @SerializedName("Taxes")
        @Expose
        public int Taxes;

        @SerializedName("payment_allowed")
        @Expose
        public ArrayList<PaymentAllowed> paymentAllowed = null;

    }

    public class PaymentAllowed {

        @SerializedName("payment_id")
        @Expose
        public String paymentId;
        @SerializedName("payment_name")
        @Expose
        public String paymentName;

    }


    public class TicketType {

        @SerializedName("ticket_id")
        @Expose
        public String ticketId;
        @SerializedName("ticket_type")
        @Expose
        public String ticketType;
        @SerializedName("ticket_amount")
        @Expose
        public String ticketAmount;
        @SerializedName("ticket_currency")
        @Expose
        public String ticketCurrency;
        @SerializedName("floor_name")
        @Expose
        public String floorName;
        @SerializedName("floor_id")
        @Expose
        public String floorId;
        @SerializedName("ticket_floor_id")
        @Expose
        public String ticketFloorId;
        @SerializedName("ticket_available")
        @Expose
        public int ticketAvailable;

        public int ticketCount;

        public boolean isSelected = false;



    }

    public class Floors {

        @SerializedName("floor_name")
        @Expose
        public String floorName;
        @SerializedName("floor_id")
        @Expose
        public String floorId;
    }
}
