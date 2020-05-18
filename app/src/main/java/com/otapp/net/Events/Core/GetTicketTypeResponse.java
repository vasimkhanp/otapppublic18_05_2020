package com.otapp.net.Events.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTicketTypeResponse {

    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("tickets")
    @Expose
    public List<GetTickets> getTicketsList=null;

    public static class GetTickets implements Parcelable {

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
            dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        }

        public GetTickets() {
        }

        protected GetTickets(Parcel in) {
            this.tkt_id = in.readString();
            this.tkt_name = in.readString();
            this.tkt_currency = in.readString();
            this.tkt_amount = in.readString();
            this.count = in.readInt();
            this.seatAmount = in.readDouble();
            this.isSelected = in.readByte() != 0;
        }

        public static final Parcelable.Creator<GetTickets> CREATOR = new Parcelable.Creator<GetTickets>() {
            @Override
            public GetTickets createFromParcel(Parcel source) {
                return new GetTickets(source);
            }

            @Override
            public GetTickets[] newArray(int size) {
                return new GetTickets[size];
            }
        };
    }
}
