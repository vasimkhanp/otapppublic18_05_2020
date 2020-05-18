package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponResponsePojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("promo_ukey")
    @Expose
    public String promo_ukey;
    @SerializedName("is_additional_verify_reqd")
    @Expose
    public String is_additional_verify_reqd;
    @SerializedName("applicable_pgws") // 0 enable all 1 for credit card 2 for tigo and 3 for mpesa
    @Expose
    public String applicable_pgws;
    @SerializedName("promo_text")
    @Expose
    public String promo_text;
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("web_img_path")
    @Expose
    public String web_img_path;
    @SerializedName("mob_img_path")
    @Expose
    public String mob_img_path;
    @SerializedName("promo_id")
    @Expose
    public int promo_id;
    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("payable_amount")
    @Expose
    public String payable_amount;
}
