package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetFareResponse implements Parcelable {
    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("promo_fare")
    @Expose
    public String promo_fare;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("header")
    @Expose
    public String header;

    @SerializedName("fare")
    @Expose
    public ArrayList<Fare> fareArrayList;

    @SerializedName("pgws")
    @Expose
    public ArrayList<Pgws> pgwsArrayList;

    public static class Fare implements Parcelable {
        @SerializedName("label")
        @Expose
        public String label;

        @SerializedName("amount")
        @Expose
        public String amount;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.label);
            dest.writeString(this.amount);
        }

        public Fare() {
        }

        protected Fare(Parcel in) {
            this.label = in.readString();
            this.amount = in.readString();
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
        @SerializedName("pay_code")
        @Expose
        public String pay_code;
        @SerializedName("pgw_enabled")
        @Expose
        public String pgw_enabled;
        @SerializedName("pgw_id")
        @Expose
        public String pgw_id;
        @SerializedName("extra_pgw_charges")
        @Expose
        public String extra_pgw_charges;

        @SerializedName("sub_pgws")
        @Expose
       public ArrayList<SubPgws> subPgwsArrayList;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.pgw_name);
            dest.writeString(this.pay_code);
            dest.writeString(this.pgw_enabled);
            dest.writeString(this.pgw_id);
            dest.writeString(this.extra_pgw_charges);
            dest.writeTypedList(this.subPgwsArrayList);
        }

        public Pgws() {
        }

        protected Pgws(Parcel in) {
            this.pgw_name = in.readString();
            this.pay_code = in.readString();
            this.pgw_enabled = in.readString();
            this.pgw_id = in.readString();
            this.extra_pgw_charges = in.readString();
            this.subPgwsArrayList = in.createTypedArrayList(SubPgws.CREATOR);
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
        public static class SubPgws implements Parcelable {
            @SerializedName("pgw_name")
            @Expose
            public String pgw_name;
            @SerializedName("pay_code")
            @Expose
            public String pay_code;
            @SerializedName("pgw_enabled")
            @Expose
            public String pgw_enabled;
            @SerializedName("pgw_id")
            @Expose
            public String pgw_id;
            @SerializedName("extra_pgw_charges")
            @Expose
            public String extra_pgw_charges;
            @SerializedName("logo_path")
            @Expose
            public String logo_path;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.pgw_name);
                dest.writeString(this.pay_code);
                dest.writeString(this.pgw_enabled);
                dest.writeString(this.pgw_id);
                dest.writeString(this.extra_pgw_charges);
                dest.writeString(this.logo_path);
            }

            public SubPgws() {
            }

            protected SubPgws(Parcel in) {
                this.pgw_name = in.readString();
                this.pay_code = in.readString();
                this.pgw_enabled = in.readString();
                this.pgw_id = in.readString();
                this.extra_pgw_charges = in.readString();
                this.logo_path = in.readString();
            }

            public static final Creator<SubPgws> CREATOR = new Creator<SubPgws>() {
                @Override
                public SubPgws createFromParcel(Parcel source) {
                    return new SubPgws(source);
                }

                @Override
                public SubPgws[] newArray(int size) {
                    return new SubPgws[size];
                }
            };
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.promo_fare);
        dest.writeString(this.message);
        dest.writeString(this.header);
        dest.writeList(this.fareArrayList);
        dest.writeList(this.pgwsArrayList);
    }

    public GetFareResponse() {
    }

    protected GetFareResponse(Parcel in) {
        this.status = in.readInt();
        this.promo_fare = in.readString();
        this.message = in.readString();
        this.header = in.readString();
        this.fareArrayList = new ArrayList<Fare>();
        in.readList(this.fareArrayList, Fare.class.getClassLoader());
        this.pgwsArrayList = new ArrayList<Pgws>();
        in.readList(this.pgwsArrayList, Pgws.class.getClassLoader());
    }

    public static final Parcelable.Creator<GetFareResponse> CREATOR = new Parcelable.Creator<GetFareResponse>() {
        @Override
        public GetFareResponse createFromParcel(Parcel source) {
            return new GetFareResponse(source);
        }

        @Override
        public GetFareResponse[] newArray(int size) {
            return new GetFareResponse[size];
        }
    };
}
