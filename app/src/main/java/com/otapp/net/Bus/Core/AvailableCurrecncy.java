package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableCurrecncy implements Parcelable {
    @SerializedName("cur_id")
    @Expose
    public String cur_id;
    @SerializedName("cur")
    @Expose
    public  String cur;

    public AvailableCurrecncy(String cur_id, String cur) {
        this.cur_id = cur_id;
        this.cur = cur;
    }

    public String getCur_id() {
        return cur_id;
    }

    public void setCur_id(String cur_id) {
        this.cur_id = cur_id;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cur_id);
        dest.writeString(this.cur);
    }

    protected AvailableCurrecncy(Parcel in) {
        this.cur_id = in.readString();
        this.cur = in.readString();
    }

    public static final Parcelable.Creator<AvailableCurrecncy> CREATOR = new Parcelable.Creator<AvailableCurrecncy>() {
        @Override
        public AvailableCurrecncy createFromParcel(Parcel source) {
            return new AvailableCurrecncy(source);
        }

        @Override
        public AvailableCurrecncy[] newArray(int size) {
            return new AvailableCurrecncy[size];
        }
    };
}
