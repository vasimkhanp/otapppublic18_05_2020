package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BDPoints implements Parcelable {

    private String sBDID;

    private String sBDName;
    private boolean isSelected = false;

    public BDPoints(String sBDID, String sBDName) {
        this.sBDID = sBDID;
        this.sBDName = sBDName;
    }

    public String getsBDID() {
        return sBDID;
    }

    public void setsBDID(String sBDID) {
        this.sBDID = sBDID;
    }

    public String getsBDName() {
        return sBDName;
    }

    public void setsBDName(String sBDName) {
        this.sBDName = sBDName;
    }

    @Override
    public String toString() {
        return sBDName;
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
        dest.writeString(this.sBDID);
        dest.writeString(this.sBDName);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected BDPoints(Parcel in) {
        this.sBDID = in.readString();
        this.sBDName = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<BDPoints> CREATOR = new Creator<BDPoints>() {
        @Override
        public BDPoints createFromParcel(Parcel source) {
            return new BDPoints(source);
        }

        @Override
        public BDPoints[] newArray(int size) {
            return new BDPoints[size];
        }
    };
}
