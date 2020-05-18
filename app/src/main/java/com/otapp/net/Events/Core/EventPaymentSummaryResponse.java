package com.otapp.net.Events.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EventPaymentSummaryResponse implements Parcelable {

    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("promo_fare")
    @Expose
    public String promo_fare;

    @SerializedName("fare")
    @Expose
    public List<Fare> fareList;

    @SerializedName("pgws")
    @Expose
    public List<Pgws> pgwsList;





    public static class Fare implements Parcelable {
        @SerializedName("label")
        @Expose
        public String label;

        @SerializedName("amount")
        @Expose
        public String amount;
        public String totalAmount;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.label);
            dest.writeString(this.amount);
            dest.writeString(this.totalAmount);
        }

        public Fare() {
        }

        protected Fare(Parcel in) {
            this.label = in.readString();
            this.amount = in.readString();
            this.totalAmount = in.readString();
        }

        public static final Creator<Fare> CREATOR = new Creator<Fare>() {
            @Override
            public Fare createFromParcel(Parcel source) {
                return new Fare(source);
            }

            @Override
            public Fare[] newArray(int size) {
                return new Fare[size];
            }
        };
    }

    public static class Pgws implements Parcelable {
        @SerializedName("pgw_name")
        @Expose
        public String pgw_name;

        @SerializedName("pgw_enabled")
        @Expose
        public String pgw_enabled;

        @SerializedName("pgw_id")
        @Expose
        public String pgw_id;

        @SerializedName("extra_pgw_charges")
        @Expose
        public String extra_pgw_charges;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.pgw_name);
            dest.writeString(this.pgw_enabled);
            dest.writeString(this.pgw_id);
            dest.writeString(this.extra_pgw_charges);
        }

        public Pgws() {
        }

        protected Pgws(Parcel in) {
            this.pgw_name = in.readString();
            this.pgw_enabled = in.readString();
            this.pgw_id = in.readString();
            this.extra_pgw_charges = in.readString();
        }

        public static final Creator<Pgws> CREATOR = new Creator<Pgws>() {
            @Override
            public Pgws createFromParcel(Parcel source) {
                return new Pgws(source);
            }

            @Override
            public Pgws[] newArray(int size) {
                return new Pgws[size];
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
        dest.writeString(this.promo_fare);
        dest.writeList(this.fareList);
        dest.writeList(this.pgwsList);
    }

    public EventPaymentSummaryResponse() {
    }

    protected EventPaymentSummaryResponse(Parcel in) {
        this.status = in.readInt();
        this.message = in.readString();
        this.promo_fare = in.readString();
        this.fareList = new ArrayList<Fare>();
        in.readList(this.fareList, Fare.class.getClassLoader());
        this.pgwsList = new ArrayList<Pgws>();
        in.readList(this.pgwsList, Pgws.class.getClassLoader());
    }

    public static final Creator<EventPaymentSummaryResponse> CREATOR = new Creator<EventPaymentSummaryResponse>() {
        @Override
        public EventPaymentSummaryResponse createFromParcel(Parcel source) {
            return new EventPaymentSummaryResponse(source);
        }

        @Override
        public EventPaymentSummaryResponse[] newArray(int size) {
            return new EventPaymentSummaryResponse[size];
        }
    };
}
