package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

public class SeatType implements Parcelable {
    public String sType;
    public String sTypeId;

    public SeatType(String sType, String sTypeId) {
        this.sType = sType;
        this.sTypeId = sTypeId;
    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public String getsTypeId() {
        return sTypeId;
    }

    public void setsTypeId(String sTypeId) {
        this.sTypeId = sTypeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sType);
        dest.writeString(this.sTypeId);
    }

    protected SeatType(Parcel in) {
        this.sType = in.readString();
        this.sTypeId = in.readString();
    }

    public static final Parcelable.Creator<SeatType> CREATOR = new Parcelable.Creator<SeatType>() {
        @Override
        public SeatType createFromParcel(Parcel source) {
            return new SeatType(source);
        }

        @Override
        public SeatType[] newArray(int size) {
            return new SeatType[size];
        }
    };
}
