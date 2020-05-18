package com.otapp.net.Events.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EventsListResponse implements Parcelable {
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("events")
    @Expose
    public List<Events> eventsList;

    @SerializedName("event_genres")
    @Expose
    public List<EventsGenres> eventsGenresList;


    public static class Events implements Parcelable {
        @SerializedName("event_id")
        @Expose
      public String event_id;

        @SerializedName("event_address")
        @Expose
     public String event_address;

        @SerializedName("event_city")
        @Expose
      public String event_city;

        @SerializedName("event_slider")
        @Expose
        public String event_slider;

        @SerializedName("event_genre")
        @Expose
        public String event_genre;

        @SerializedName("event_start_fare")
        @Expose
       public String event_start_fare;

        @SerializedName("event_dates")
        @Expose
        public List<EventDates> eventDatesList;

        public class EventDates implements Parcelable {

            @SerializedName("event_date")
            @Expose
            public String event_date;
            public  boolean isSelected = false;

            @SerializedName("slider")
            @Expose
            public String slider;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.event_date);
                dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
            }

            public EventDates() {
            }

            protected EventDates(Parcel in) {
                this.event_date = in.readString();
                this.isSelected = in.readByte() != 0;
            }

            public final Creator<EventDates> CREATOR = new Creator<EventDates>() {
                @Override
                public EventDates createFromParcel(Parcel source) {
                    return new EventDates(source);
                }

                @Override
                public EventDates[] newArray(int size) {
                    return new EventDates[size];
                }
            };
        }

        @SerializedName("event_tickets")
        @Expose
        public List<EventTickets> eventTicketsList;

        public static class  EventTickets implements Parcelable {

            @SerializedName("floor_id")
            @Expose
            public String floor_id;

            @SerializedName("floor_name")
            @Expose
            public String floor_name;

            @SerializedName("tickets")
            @Expose
            public List<Tickets> ticketsList;

            @SerializedName("Get_Tickets")
            @Expose
            public  String getTickets;

            public boolean isSeleected;

            public static class Tickets implements Parcelable {

                @SerializedName("status")
                @Expose
                public int status;

                @SerializedName("tkt_id")
                @Expose
                public String tkt_id;
                @SerializedName("tkt_name")
                @Expose
                public String tkt_name;

                @SerializedName("tkt_currency")
                @Expose
                public String tkt_currency;

                @SerializedName("tkt_amount")
                @Expose
                public String tkt_amount;

                @SerializedName("tkt_desc")
                @Expose
                public String tkt_desc;

                public int count=0;
                public boolean setEnabled=true;

                public double seatAmount=0.0;

                public boolean isSelected=false;


                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.tkt_id);
                    dest.writeString(this.tkt_name);
                    dest.writeString(this.tkt_currency);
                    dest.writeString(this.tkt_amount);
                    dest.writeInt(this.count);
                    dest.writeDouble(this.seatAmount);
                }

                public Tickets() {
                }

                protected Tickets(Parcel in) {
                    this.tkt_id = in.readString();
                    this.tkt_name = in.readString();
                    this.tkt_currency = in.readString();
                    this.tkt_amount = in.readString();
                    this.count = in.readInt();
                    this.seatAmount = in.readDouble();
                }

                public static final Creator<Tickets> CREATOR = new Creator<Tickets>() {
                    @Override
                    public Tickets createFromParcel(Parcel source) {
                        return new Tickets(source);
                    }

                    @Override
                    public Tickets[] newArray(int size) {
                        return new Tickets[size];
                    }
                };
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.floor_id);
                dest.writeString(this.floor_name);
                dest.writeList(this.ticketsList);
                dest.writeByte(this.isSeleected ? (byte) 1 : (byte) 0);
            }

            public EventTickets() {
            }

            protected EventTickets(Parcel in) {
                this.floor_id = in.readString();
                this.floor_name = in.readString();
                this.ticketsList = new ArrayList<Tickets>();
                in.readList(this.ticketsList, Tickets.class.getClassLoader());
                this.isSeleected = in.readByte() != 0;
            }

            public static final Creator<EventTickets> CREATOR = new Creator<EventTickets>() {
                @Override
                public EventTickets createFromParcel(Parcel source) {
                    return new EventTickets(source);
                }

                @Override
                public EventTickets[] newArray(int size) {
                    return new EventTickets[size];
                }
            };
        }

        @SerializedName("event_short_date_title")
        @Expose
        public String event_short_date_title;

        @SerializedName("event_date_title")
        @Expose
        public String event_date_title;

        @SerializedName("event_slider_1")
        @Expose
        public String event_slider_1;

        @SerializedName("event_slider_2")
        @Expose
        public String event_slider_2;

        @SerializedName("event_name")
        @Expose
       public String event_name;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.event_id);
            dest.writeString(this.event_address);
            dest.writeString(this.event_city);
            dest.writeString(this.event_slider);
            dest.writeString(this.event_genre);
            dest.writeString(this.event_start_fare);
            dest.writeList(this.eventDatesList);
            dest.writeList(this.eventTicketsList);
            dest.writeString(this.event_short_date_title);
            dest.writeString(this.event_date_title);
            dest.writeString(this.event_name);
        }

        public Events() {
        }

        protected Events(Parcel in) {
            this.event_id = in.readString();
            this.event_address = in.readString();
            this.event_city = in.readString();
            this.event_slider = in.readString();
            this.event_genre = in.readString();
            this.event_start_fare = in.readString();
            this.eventDatesList = new ArrayList<EventDates>();
            in.readList(this.eventDatesList, EventDates.class.getClassLoader());
            this.eventTicketsList = new ArrayList<EventTickets>();
            in.readList(this.eventTicketsList, EventTickets.class.getClassLoader());
            this.event_short_date_title = in.readString();
            this.event_date_title = in.readString();
            this.event_name = in.readString();
        }

        public static final Creator<Events> CREATOR = new Creator<Events>() {
            @Override
            public Events createFromParcel(Parcel source) {
                return new Events(source);
            }

            @Override
            public Events[] newArray(int size) {
                return new Events[size];
            }
        };
    }

    public static class EventsGenres implements Parcelable {
        @SerializedName("genre_id")
        @Expose
        public String genre_id;

        @SerializedName("genre_name")
        @Expose
        public String genre_name;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.genre_id);
            dest.writeString(this.genre_name);
        }

        public EventsGenres() {
        }

        protected EventsGenres(Parcel in) {
            this.genre_id = in.readString();
            this.genre_name = in.readString();
        }

        public static final Creator<EventsGenres> CREATOR = new Creator<EventsGenres>() {
            @Override
            public EventsGenres createFromParcel(Parcel source) {
                return new EventsGenres(source);
            }

            @Override
            public EventsGenres[] newArray(int size) {
                return new EventsGenres[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.message);
        dest.writeList(this.eventsList);
        dest.writeList(this.eventsGenresList);
    }

    public EventsListResponse() {
    }

    protected EventsListResponse(Parcel in) {
        this.status = in.readInt();
        this.message = in.readString();
        this.eventsList = new ArrayList<Events>();
        in.readList(this.eventsList, Events.class.getClassLoader());
        this.eventsGenresList = new ArrayList<EventsGenres>();
        in.readList(this.eventsGenresList, EventsGenres.class.getClassLoader());
    }

    public static final Creator<EventsListResponse> CREATOR = new Creator<EventsListResponse>() {
        @Override
        public EventsListResponse createFromParcel(Parcel source) {
            return new EventsListResponse(source);
        }

        @Override
        public EventsListResponse[] newArray(int size) {
            return new EventsListResponse[size];
        }
    };
}
