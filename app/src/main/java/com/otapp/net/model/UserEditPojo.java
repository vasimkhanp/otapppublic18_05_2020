package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserEditPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {
        @SerializedName("cust_id")
        @Expose
        public String custId;
        @SerializedName("cust_name")
        @Expose
        public String custName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("country_code")
        @Expose
        public String custCountryCode;
        @SerializedName("cust_mob")
        @Expose
        public String custMob;
        @SerializedName("cust_email")
        @Expose
        public String custEmail;
        @SerializedName("profile_image")
        @Expose
        public String custProfile;
        @SerializedName("cust_log_key")
        @Expose
        public String custLogKey;

    }


}
