package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThemeParkRideListPojo {

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

        @SerializedName("banner_img")
        @Expose
        public String bannerImg;
        @SerializedName("rides")
        @Expose
        public List<Ride> rides = null;
        @SerializedName("entrance_fee")
        @Expose
        public EnterasFee entranceFee = null;
        @SerializedName("services_and_events")
        @Expose
        public List<ServicesAndEvent> servicesAndEvents = null;
        @SerializedName("combo")
        @Expose
        public List<Combo> combo = null;

    }

    public class EnterasFee {

        @SerializedName("tp_id")
        @Expose
        public String tpId;
        @SerializedName("tp_name")
        @Expose
        public String tpName;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("tp_sub_name")
        @Expose
        public String tpSubName;
    }


    public class Ride {

        @SerializedName("tp_id")
        @Expose
        public String tpId;
        @SerializedName("tp_name")
        @Expose
        public String tpName;
        @SerializedName("tp_sub_name")
        @Expose
        public String tpSubName;
        @SerializedName("image")
        @Expose
        public String image;

    }

    public class ServicesAndEvent {

        @SerializedName("tp_id")
        @Expose
        public String tpId;
        @SerializedName("tp_name")
        @Expose
        public String tpName;
        @SerializedName("tp_sub_name")
        @Expose
        public String tpSubName;
        @SerializedName("image")
        @Expose
        public String image;

    }

    public class Combo {

        @SerializedName("combo_id")
        @Expose
        public String comboId;
        @SerializedName("combo_name")
        @Expose
        public String comboName;
        @SerializedName("includes")
        @Expose
        public List<String> includes = null;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("image")
        @Expose
        public String image;

    }

}
