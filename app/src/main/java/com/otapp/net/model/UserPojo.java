package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserPojo {

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
        @SerializedName("cust_log_id")
        @Expose
        public String custId;
        @SerializedName("cust_log_name")
        @Expose
        public String custName;
        @SerializedName("cust_log_last_name")
        @Expose
        public String custLastName;
        @SerializedName("country_code")
        @Expose
        public String custCountryCode;
        @SerializedName("cust_log_mob")
        @Expose
        public String custMob;
        @SerializedName("cust_log_email")
        @Expose
        public String custEmail;
        @SerializedName("cust_log_profile")
        @Expose
        public String custProfile;
        @SerializedName("cust_log_gender")
        @Expose
        public String custGender;
        @SerializedName("cust_log_martial_status")
        @Expose
        public String custMaritalStatus;
        @SerializedName("cust_log_key")
        @Expose
        public String custKey;
        @SerializedName("log_id")
        @Expose
        public String logId;
    }



}
