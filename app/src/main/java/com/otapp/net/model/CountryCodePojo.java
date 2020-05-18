package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryCodePojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("country_code")
    @Expose
    public List<CountryCode> data;
    @SerializedName("ad5")
    @Expose
    public List<Ad5> ad5;

    @SerializedName("services")
    @Expose
    public List<Services> servicesList;

    public class CountryCode {

        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("mob_length")
        @Expose
        public int mobLength = 9;
    }

    public class Ad5 {

        @SerializedName("page")
        @Expose
        public String page;
        @SerializedName("location")
        @Expose
        public String location;
        @SerializedName("link")
        @Expose
        public String link;
        @SerializedName("image_path")
        @Expose
        public String image_path;
        @SerializedName("hdr_txt")
        @Expose
        public String hdr_txt;
        @SerializedName("cntnt_txt")
        @Expose
        public String cntnt_txt;
        @SerializedName("is_open_in_app")
        @Expose
        public String is_open_in_app;
    }

    public class Services{
        @SerializedName("service_id")
        @Expose
        public String service_id;

        @SerializedName("service_name")
        @Expose
        public String service_name;
    }
}
