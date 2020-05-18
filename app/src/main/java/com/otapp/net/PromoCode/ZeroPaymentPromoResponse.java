package com.otapp.net.PromoCode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZeroPaymentPromoResponse {

    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("ticket")
    @Expose
    public String ticket;

    @SerializedName("message")
    @Expose
    public String message;
}
