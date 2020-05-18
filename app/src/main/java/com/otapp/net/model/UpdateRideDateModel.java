package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateRideDateModel {
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("message")
    @Expose
    public String message;
}
