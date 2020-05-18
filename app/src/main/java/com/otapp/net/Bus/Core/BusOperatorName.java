package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

public class BusOperatorName implements Parcelable {
    public String sBusOperatorName;
    public boolean isSelected;

    public BusOperatorName(String sBusOperatorName, boolean isSelected) {
        this.sBusOperatorName = sBusOperatorName;
        this.isSelected = isSelected;
    }

    public String getsBusOperatorName() {
        return sBusOperatorName;
    }

    public void setsBusOperatorName(String sBusOperatorName) {
        this.sBusOperatorName = sBusOperatorName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sBusOperatorName);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected BusOperatorName(Parcel in) {
        this.sBusOperatorName = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<BusOperatorName> CREATOR = new Parcelable.Creator<BusOperatorName>() {
        @Override
        public BusOperatorName createFromParcel(Parcel source) {
            return new BusOperatorName(source);
        }

        @Override
        public BusOperatorName[] newArray(int size) {
            return new BusOperatorName[size];
        }
    };
}
