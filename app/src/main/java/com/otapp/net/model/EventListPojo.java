package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventListPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("category")
    @Expose
    public List<EventCategory> categories = null;
    @SerializedName("data")
    @Expose
    public List<Events> events = null;

    public class EventCategory {

        @SerializedName("category_id")
        @Expose
        public String categoryId;
        @SerializedName("category_name")
        @Expose
        public String categoryName;

    }

    public class Events {

        @SerializedName("event_id")
        @Expose
        public String eventId;
        @SerializedName("event_name")
        @Expose
        public String eventName;
        @SerializedName("event_date")
        @Expose
        public List<String> eventDate = null;
        @SerializedName("event_address")
        @Expose
        public String eventAddress;
        @SerializedName("event_city")
        @Expose
        public String eventCity;
        @SerializedName("event_photo")
        @Expose
        public String eventPhoto;
        @SerializedName("event_category")
        @Expose
        public String eventCategory;
        @SerializedName("event_price")
        @Expose
        public float eventPrice;
        @SerializedName("ticket_currency")
        @Expose
        public String ticketCurrency;
        @SerializedName("event_language")
        @Expose
        public String eventLanguage;

    }

}
