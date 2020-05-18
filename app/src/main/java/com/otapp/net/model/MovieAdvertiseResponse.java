package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieAdvertiseResponse {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("advertList")
    @Expose
    public List<Advertise> advertiseList = null;


    public class Advertise {

        @SerializedName("ADVERT_LOC")
        @Expose
        public String ADVERT_LOC;
        @SerializedName("ADVERT_EXT")
        @Expose
        public String ADVERT_EXT;

    }

}
