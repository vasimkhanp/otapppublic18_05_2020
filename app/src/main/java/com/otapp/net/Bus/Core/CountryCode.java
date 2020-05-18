package com.otapp.net.Bus.Core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CountryCode implements Parcelable {

    @SerializedName("country_code")
    String countryCode;
    @SerializedName("is_default")
    String isDefaultValue;

    public CountryCode(String countryCode, String isDefaultValue) {
        this.countryCode = countryCode;
        this.isDefaultValue = isDefaultValue;
    }

    public CountryCode() {

    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIsDefaultValue() {
        return isDefaultValue;
    }

    public void setIsDefaultValue(String isDefaultValue) {
        this.isDefaultValue = isDefaultValue;
    }

    @Override
    public String toString() {
        return countryCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryCode);
        dest.writeString(this.isDefaultValue);
    }

    protected CountryCode(Parcel in) {
        this.countryCode = in.readString();
        this.isDefaultValue = in.readString();
    }

    public static final Creator<CountryCode> CREATOR = new Creator<CountryCode>() {
        @Override
        public CountryCode createFromParcel(Parcel source) {
            return new CountryCode(source);
        }

        @Override
        public CountryCode[] newArray(int size) {
            return new CountryCode[size];
        }
    };
}
