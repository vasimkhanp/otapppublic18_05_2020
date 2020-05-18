package com.otapp.net;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.model.CouponResponsePojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponCodeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvApply)
    TextView tvApply;
    @BindView(R.id.tvRemove)
    TextView tvRemove;
    @BindView(R.id.etCouponCode)
    EditText etCouponCode;
    String strPromoCode = "";
    private String mPropertyId = "", mTicketInfo = "", mTotalFare = "", mUserKey = "", mCurrency = "", mPlatform = "A";
    int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_coupon_code);
        ButterKnife.bind(this);

        InitializeControls();
    }

    private void InitializeControls() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPropertyId = bundle.getString(Constants.BNDL_PROPERTY_ID);
            mTicketInfo = bundle.getString(Constants.BNDL_TICKET_INFO);
            mTotalFare = bundle.getString(Constants.BNDL_TOTAL_FARE);
            mUserKey = bundle.getString(Constants.BNDL_USER_KEY);
            mCurrency = bundle.getString(Constants.BNDL_CURRENCY);
        }
        etCouponCode.setText(MyPref.getPref(getApplicationContext(), "PromoCode", strPromoCode));
        etCouponCode.setSelection(etCouponCode.getText().length());
        strPromoCode = etCouponCode.getText().toString();
        if (!strPromoCode.equals("")) {
            tvApply.setVisibility(View.GONE);
            tvRemove.setVisibility(View.VISIBLE);
        }
        strPromoCode = etCouponCode.getText().toString();
        tvBack.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        tvRemove.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == tvBack) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (view == tvApply) {
            flag = 0;
            if (TextUtils.isEmpty(etCouponCode.getText().toString())) {
                Utils.showToast(getActivity(), getString(R.string.alert_enter_coupon_code));
                return;
            }
            strPromoCode = etCouponCode.getText().toString();
            MyPref.setPref(getApplicationContext(), "PromoCode", strPromoCode);
            applyCouponCode();
        } else if (view == tvRemove) {
            flag = 1;
            etCouponCode.setText("");
            MyPref.setPref(getApplicationContext(), "ENABLED_PAYMENT_METHODS", "0");
            MyPref.setPref(getApplicationContext(), "PromoCode", "");

            applyCouponCode();


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    private void applyCouponCode() {

        String mPromoCode = etCouponCode.getText().toString();
        if (flag == 1) {
            mPromoCode = "";
            mCurrency = "";
        }

//        key=*MD5(SHA512(promo_code+prop_id+tkt_info+tot_fare+ukey+cur+platform+"pr0mOCode"))

        String mKey = Utils.getCouponCodeKey(mPromoCode + mPropertyId + mTicketInfo + mTotalFare + mUserKey + mCurrency + mPlatform + "pr0mOCode");

//        https://www.managemyticket.net/android/api/promo/Validate_Promo.php?promo_code=STANCHART800&prop_id=23&tkt_info={"tkts_info": [{"tkt_id": "4770", "tot_tkt_id_tkts_count": "2", "single_fare": "10000.00", "seats": "A1,A2"}]}&tot_fare=20000.00&ukey=TEST1234&cur=TSH&platform=W&key=b651cdfb275d53e9f39b1f379b26ac68

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }
        Map<String, String> jsonParams = new HashMap<>();

        jsonParams.put("promo_code", "" + mPromoCode);
        jsonParams.put("cur", "" + mCurrency);

        jsonParams.put("key", "" + mKey);
        jsonParams.put("platform", "" + mPlatform);
        jsonParams.put("ukey", "" + mUserKey);
        jsonParams.put("tot_fare", "" + mTotalFare);
        jsonParams.put("prop_id", "" + mPropertyId);
        jsonParams.put("tkt_info", "" + mTicketInfo);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<CouponResponsePojo> mCall = mApiInterface.validateCouponCode(jsonParams);
        mCall.enqueue(new Callback<CouponResponsePojo>() {
            @Override
            public void onResponse(Call<CouponResponsePojo> call, Response<CouponResponsePojo> response) {
                Utils.closeProgressDialog();
                LogUtils.e("", "response::" + response);

                JSONObject jsonObjectResponse = null;
                try {
                    jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                    Log.d("Log", "Verify Code Response : " + jsonObjectResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (response.isSuccessful()) {

                    CouponResponsePojo mCouponResponsePojo = response.body();

                    if (mCouponResponsePojo != null) {
                        if (mCouponResponsePojo.status.equalsIgnoreCase("200")) {

                            MyPref.setPref(getApplicationContext(), "ENABLED_PAYMENT_METHODS", mCouponResponsePojo.applicable_pgws);
//                            MD5(promo_id+promo_ukey+discount+is_additional_verify_reqd+"2O0pr)mO"+applicable_pgws+payable_amount)

                            String mMyKey = Utils.MD5(mCouponResponsePojo.promo_id + mCouponResponsePojo.promo_ukey + mCouponResponsePojo.discount + mCouponResponsePojo.is_additional_verify_reqd + "2O0pr)mO" + mCouponResponsePojo.applicable_pgws + mCouponResponsePojo.payable_amount);

                            LogUtils.e("", "mMyKey::" + mMyKey);


                            if (!TextUtils.isEmpty(mMyKey) && !TextUtils.isEmpty(mCouponResponsePojo.key) && mCouponResponsePojo.key.equals(mMyKey)) {

                                if (!TextUtils.isEmpty(mCouponResponsePojo.is_additional_verify_reqd) && mCouponResponsePojo.is_additional_verify_reqd.equals("1")) {

                                    final Dialog dialog = new Dialog(getActivity());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.dialog_verify_promocode);
                                    dialog.setCancelable(false);
                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                    TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);
                                    TextView tvText = (TextView) dialog.findViewById(R.id.tvText);
                                    TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                                    EditText etCode = (EditText) dialog.findViewById(R.id.etCode);
                                    ImageView ivPhoto = (ImageView) dialog.findViewById(R.id.ivPhoto);
                                    final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);

                                    if (!TextUtils.isEmpty(mCouponResponsePojo.promo_text)) {
                                        tvText.setVisibility(View.VISIBLE);
                                        byte[] data = Base64.decode(mCouponResponsePojo.promo_text, Base64.DEFAULT);
                                        try {
                                            String mText = new String(data, "UTF-8");
                                            if (!TextUtils.isEmpty(mText)) {
                                                tvText.setText("" + mText);
                                            }
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        tvText.setVisibility(View.GONE);
                                    }

                                    if (!TextUtils.isEmpty(mCouponResponsePojo.mob_img_path)) {
                                        ivPhoto.setVisibility(View.VISIBLE);
                                        Picasso.get().load(mCouponResponsePojo.mob_img_path).into(ivPhoto);
                                    } else {
                                        ivPhoto.setVisibility(View.GONE);
                                    }


                                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (TextUtils.isEmpty(etCode.getText().toString())) {
                                                Utils.showToast(getActivity(), getString(R.string.msg_enter_code));
                                                return;
                                            }

                                            if (!Utils.isInternetConnected(getActivity())) {
                                                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                                return;
                                            }

                                            if (!Utils.isProgressDialogShowing()) {
                                                Utils.showFlightProgressDialog(getActivity());
                                            }

                                            String mFullText = mCouponResponsePojo.promo_ukey + etCode.getText().toString() + "@dtnLReQd.";
                                            LogUtils.e("", "mFullText::" + mFullText);
                                            String mInnerKey = Utils.getCouponCodeKey(mFullText);

                                            Map<String, String> jsonParams = new HashMap<>();
                                            jsonParams.put("verify_code", "" + etCode.getText().toString());
                                            jsonParams.put("ukey", "" + mCouponResponsePojo.promo_ukey);
                                            jsonParams.put("key", "" + mInnerKey);

                                            ApiInterface mApiInterface = RestClient.getClient(true);
                                            Call<ApiResponse> mCall = mApiInterface.verifyCouponCode(jsonParams);
                                            mCall.enqueue(new Callback<ApiResponse>() {
                                                @Override
                                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                                    Utils.closeProgressDialog();
                                                    if (response.isSuccessful()) {

                                                        ApiResponse mApiResponse = response.body();

                                                        if (mApiResponse != null) {
                                                            if (mApiResponse.status.equalsIgnoreCase("200")) {

                                                                if (dialog != null && dialog.isShowing()) {
                                                                    dialog.dismiss();
                                                                    dialog.cancel();
                                                                }

                                                                Utils.showToast(getActivity(), mApiResponse.message);

                                                                goBackWithDiscount(mCouponResponsePojo);

                                                            } else {
                                                                Utils.showToast(getActivity(), mApiResponse.message);
                                                              //  Utils.showToast(getActivity(), "Line 285");
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                                    Utils.closeProgressDialog();
                                                }
                                            });


                                        }
                                    });

                                    tvCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (dialog != null && dialog.isShowing()) {
                                                dialog.dismiss();
                                                dialog.cancel();
                                            }
                                        }
                                    });

                                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                        @Override
                                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                            return keyCode == KeyEvent.KEYCODE_BACK;
                                        }
                                    });


                                    dialog.show();

                                } else {
                                    goBackWithDiscount(mCouponResponsePojo);
                                }

                            } else {
                                Utils.showToast(getActivity(), getString(R.string.msg_code_not_apply));
                            }

                        } else {
                            MyPref.setPref(getApplicationContext(), "ENABLED_PAYMENT_METHODS", "0");
                            if (mCouponResponsePojo.message.equals("Invalid currecny or currecny not found!!")) {
                                goBackWithDiscount(mCouponResponsePojo);
                                Utils.showToast(getActivity(), "Promo Code Successfully removed");
                            } else {
                                Utils.showToast(getActivity(), mCouponResponsePojo.message);
                                goBackWithDiscount(mCouponResponsePojo);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CouponResponsePojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });
    }

    private void goBackWithDiscount(CouponResponsePojo mCouponResponsePojo) {

        Intent back = new Intent();
        back.putExtra(Constants.BNDL_COUPON_RESPONSE, new Gson().toJson(mCouponResponsePojo));
        setResult(RESULT_OK, back);
        finish();

    }

    private Context getActivity() {
        return CouponCodeActivity.this;
    }
}
