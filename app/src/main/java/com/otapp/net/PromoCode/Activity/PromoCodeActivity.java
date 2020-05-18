package com.otapp.net.PromoCode.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.loadingview.LoadingView;
import com.google.gson.Gson;
import com.otapp.net.Async.Interface.OtappApiServices;
import com.otapp.net.Async.Interface.RestClient;
import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.Events.Core.PromoCodeResponse;
import com.otapp.net.Events.Core.VerifyPromoResponse;
import com.otapp.net.R;
import com.otapp.net.helper.SHA;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoCodeActivity extends AppCompatActivity {
    @BindView(R.id.toolbarTitle)
    TextView pageTitle;
    @BindView(R.id.editOfferCode)
    EditText editOfferCode;
    @BindView(R.id.btnApply)
    Button btnApply;
    @BindView(R.id.btnRemove)
    Button btnRemove;
    @BindView(R.id.loadingView)
            LoadingView loadingView;
   /* @BindView(R.id.loadingView)
    LoadingView loadingView;*/


    String strAgentId = "", strBookFrom = "", strEventId = "", strEventDate = "", strUkey = "", strPromoCode = "", strNoOfTickets = "", strCustId = "", strCustName = "", strEmail = "", strMob = "", strReqtFrom = "AND";
    String apiKey = "", key = "";
    String strPromoFlag="";
    int AuthKey;
    String strCurrency = "", strPlatForm = "A", strTotalFare = "", strTicketInfo;

    List<EventsListResponse.Events.EventTickets.Tickets> tempArrylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        pageTitle.setText(R.string.offer_voucher_giftCare);

        strPromoFlag= MyPref.getPref(getApplicationContext(), MyPref.PREF_PROMO_FLAG,"0");
        if(strPromoFlag.equals("1")){
            btnRemove.setVisibility(View.VISIBLE);
            btnApply.setVisibility(View.GONE);
            editOfferCode.setText( MyPref.getPref(getApplicationContext(), MyPref.PREF_PROMO_CODE,""));
            editOfferCode.setSelection(editOfferCode.getText().length());
        }


        tempArrylist = bundle.getParcelableArrayList("ticketInfo");
        strTotalFare = bundle.getString("totalFare");

        strAgentId = AppConstants.agentId;
        strBookFrom = AppConstants.bookFrom;
        strEventId = MyPref.getPref(getApplicationContext(), MyPref.PREF_EVENT_ID, "");
        strEventDate = MyPref.getPref(getApplicationContext(), MyPref.PREF_EVENT_DATE, "");
        strUkey = MyPref.getPref(getApplicationContext(), MyPref.PREF_UKEY, "");



        Log.d("Log",strUkey);

        strNoOfTickets = String.valueOf(bundle.getInt("noOfTickets"));
        JSONObject ticketInfo = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject tickt = new JSONObject();
        try {
            for (int i = 0; i < tempArrylist.size(); i++) {
                ticketInfo.put("tkt_id", tempArrylist.get(i).tkt_id);
                ticketInfo.put("tot_tkt_id_tkts_count", tempArrylist.get(i).count);
                ticketInfo.put("single_fare", "0.00");
                ticketInfo.put("seats", "");
                jsonArray.put(ticketInfo);
            }
            tickt.put("tkts_info", jsonArray);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        strTicketInfo = tickt.toString();
        strCurrency = MyPref.getPref(getApplicationContext(), MyPref.PREF_CURRENCY, "");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.btnApply)
    public void applayPromoCode() {
        boolean isConnected = AppConstants.isConnected(getApplicationContext());
        if (isConnected) {
           /* loadingView.setVisibility(View.VISIBLE);
            loadingView.start();*/
           if(!Utils.isProgressDialogShowing()){
               Utils.showProgressDialog(this);
           }
            strPromoCode = editOfferCode.getText().toString();
            MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_CODE,strPromoCode);
            applyOrRemovePromoCode();

        }
    }

   @OnClick(R.id.btnRemove)
   public void onPromoCode(){
      /* if(!Utils.isProgressDialogShowing()){
           Utils.showProgressDialog(this);
       }*/
        editOfferCode.setText("");
        strPromoCode="";
        strCurrency="";
       MyPref.setPref(getApplicationContext(), "ENABLED_PAYMENT_METHODS", "0");
        applyOrRemovePromoCode();
   }
    @OnClick(R.id.back)
    public void onBack(){
        finish();
    }



    private void goBackWithDiscount(PromoCodeResponse mCouponResponsePojo) {
        Intent back = new Intent();
        back.putExtra(AppConstants.BNDL_COUPON_RESPONSE, new Gson().toJson(mCouponResponsePojo));
        setResult(RESULT_OK, back);
        finish();

    }
    private String calculateHash(int authKey, String sHash) {
        switch (authKey) {
            case 1:
                sHash = SHA.MD5(sHash);
                break;
            case 2:
                try {
                    sHash = SHA.SHA1(sHash);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    sHash = SHA.SHA256(sHash);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    sHash = SHA.SHA512(sHash);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
        return sHash;
    }
    public void applyOrRemovePromoCode(){
        try {
            key = calculateHash(1, calculateHash(4, strPromoCode + strEventId + strTicketInfo + strTotalFare + strUkey + strCurrency + strPlatForm + "pr0mOCode"));
            OtappApiServices otappApiServices = RestClient.getClient(true);
            Call<PromoCodeResponse> mCallPromoCode = otappApiServices.getPromoCode(strPromoCode, strEventId, strTicketInfo, strTotalFare, strUkey, strCurrency, strPlatForm, key);
            mCallPromoCode.enqueue(new Callback<PromoCodeResponse>() {
                @Override
                public void onResponse(Call<PromoCodeResponse> call, Response<PromoCodeResponse> response) {
                     Utils.closeProgressDialog();

                    if (response.isSuccessful()) {
                        final PromoCodeResponse mCouponResponsePojo = response.body();
                        if (mCouponResponsePojo != null) {
                            if (response.body().status.equals("200")) {
                                MyPref.setPref(getApplicationContext(), "ENABLED_PAYMENT_METHODS", mCouponResponsePojo.applicable_pgws);
                                String mMyKey = calculateHash(1, mCouponResponsePojo.promo_id + mCouponResponsePojo.promo_ukey + mCouponResponsePojo.discount + mCouponResponsePojo.is_additional_verify_reqd + "2O0pr)mO" + mCouponResponsePojo.applicable_pgws + mCouponResponsePojo.payable_amount);

                                if (!mMyKey.isEmpty() && !mCouponResponsePojo.key.isEmpty() && mCouponResponsePojo.key.equals(mMyKey)) {

                                    if (!mCouponResponsePojo.is_additional_verify_reqd.isEmpty() && mCouponResponsePojo.is_additional_verify_reqd.equals("1")) {

                                        final Dialog dialog = new Dialog(PromoCodeActivity.this);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.dialog_verify_promocode);
                                        dialog.setCancelable(false);
                                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                        TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);
                                        TextView tvText = (TextView) dialog.findViewById(R.id.tvText);
                                        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                                        final EditText etCode = (EditText) dialog.findViewById(R.id.etCode);
                                        ImageView ivPhoto = (ImageView) dialog.findViewById(R.id.ivPhoto);
                                        final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                                    /*    final LoadingView loadingView = dialog.findViewById(R.id.loadingView);*/

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
                                            /*  Picasso.get().load(mCouponResponsePojo.mob_img_path).into(ivPhoto);*/
                                            Glide.with(getApplicationContext()).load(mCouponResponsePojo.mob_img_path).listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    return false;
                                                }
                                            }).into(ivPhoto);
                                        } else {
                                            ivPhoto.setVisibility(View.GONE);
                                        }

                                        tvCancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });

                                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                String fullText = mCouponResponsePojo.promo_ukey + etCode.getText().toString() + "@dtnLReQd.";
                                                String innerKey = calculateHash(1, calculateHash(4, fullText));
                                                String verfyCode = etCode.getText().toString();
                                                String ukey = mCouponResponsePojo.promo_ukey;
                                                MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_CODE, verfyCode);
                                                loadingView.setVisibility(View.VISIBLE);
                                                loadingView.start();

                                        /* if(!Utils.isProgressDialogShowing()){
                                             Utils.showProgressDialog(getApplicationContext());
                                         }*/
                                                OtappApiServices otappApiServices = RestClient.getClient(true);
                                                Call<VerifyPromoResponse> mCallVerifyPromo = otappApiServices.getVerifyPromo(verfyCode, ukey, innerKey);
                                                mCallVerifyPromo.enqueue(new Callback<VerifyPromoResponse>() {
                                                    @Override
                                                    public void onResponse(Call<VerifyPromoResponse> call, Response<VerifyPromoResponse> response) {
                                                        loadingView.stop();
                                                        Utils.closeProgressDialog();
                                                        JSONObject jsonObjectResponse = null;
                                                        try {
                                                            jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                                                            Log.d("Log", "Response : " + jsonObjectResponse);

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (response.body().status.equals("200")) {
                                                            if (dialog != null && dialog.isShowing()) {
                                                                dialog.dismiss();
                                                                dialog.cancel();
                                                            }

                                                            //goBackWithDiscount(mCouponResponsePojo);
                                                            MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_ID, mCouponResponsePojo.promo_id);
                                                            /* MyPref.setPref(getApplicationContext(),MyPref.PREF_PROMO_UKEY,mCouponResponsePojo.promo_ukey);*/
                                                            MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_FLAG, "1");

                                                            Log.d("Log", mCouponResponsePojo.promo_ukey);
                                                          //  Toast.makeText(PromoCodeActivity.this, "" + mCouponResponsePojo.promo_ukey, Toast.LENGTH_SHORT).show();
                                                            finish();

                                                        } else {
                                                            if (dialog != null && dialog.isShowing()) {
                                                                dialog.dismiss();
                                                                dialog.cancel();
                                                            }
                                                            Toast.makeText(PromoCodeActivity.this, "" + response.body().message, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<VerifyPromoResponse> call, Throwable t) {
                                                        //loadingView.stop();
                                                        Utils.closeProgressDialog();
                                                    }
                                                });
                                            }
                                        });


                                        dialog.show();
                                    }else {
                                        MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_ID, mCouponResponsePojo.promo_id);
                                        /* MyPref.setPref(getApplicationContext(),MyPref.PREF_PROMO_UKEY,mCouponResponsePojo.promo_ukey);*/
                                        MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_FLAG, "1");

                                        Log.d("Log", mCouponResponsePojo.promo_ukey);
                                  //      Toast.makeText(PromoCodeActivity.this, "" + mCouponResponsePojo.promo_ukey, Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                }


                            } else {
                                MyPref.setPref(getApplicationContext(), "ENABLED_PAYMENT_METHODS", "0");
                                Toast.makeText(PromoCodeActivity.this, "" + response.body().message, Toast.LENGTH_SHORT).show();
                                MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_FLAG, "0");
                                finish();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<PromoCodeResponse> call, Throwable t) {
                    Utils.closeProgressDialog();
                    Toast.makeText(PromoCodeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
