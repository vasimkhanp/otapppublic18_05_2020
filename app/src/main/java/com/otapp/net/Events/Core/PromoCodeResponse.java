package com.otapp.net.Events.Core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoCodeResponse {
    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("promo_ukey")
    @Expose
    public String promo_ukey;
    @SerializedName("promo_id")
    @Expose
    public String promo_id;

    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("is_additional_verify_reqd")
    @Expose
    public String is_additional_verify_reqd;
    @SerializedName("promo_text")
    @Expose
    public String promo_text;
    @SerializedName("web_img_path")
    @Expose
    public String web_img_path;
    @SerializedName("mob_img_path")
    @Expose
    public String mob_img_path;
    @SerializedName("applicable_pgws")
    @Expose
    public String applicable_pgws;
    @SerializedName("payable_amount")
    @Expose
    public String payable_amount;
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("tot_excluding_tax")
    @Expose
    public String tot_excluding_tax;
    @SerializedName("conv_fees")
    @Expose
    public String conv_fees;

    @SerializedName("taxable_amount")
    @Expose
    public String taxable_amount;
    @SerializedName("tot_vat")
    @Expose
    public String tot_vat;





}
