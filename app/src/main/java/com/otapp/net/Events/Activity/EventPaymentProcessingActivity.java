package com.otapp.net.Events.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.Async.Interface.OtappApiServices;
import com.otapp.net.Async.Interface.RestClient;
import com.otapp.net.Events.Core.PaymentSuceesResponse;
import com.otapp.net.Events.Core.TicketNumberResponse;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;
import com.otapp.net.helper.SHA;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPaymentProcessingActivity extends AppCompatActivity {

    @BindView(R.id.toolbarTitle)
    TextView pageTitle;
    @BindView(R.id.tvWarning)
    TextView tvWarning;
    @BindView(R.id.tvMobile)
    TextView tvMobile;
    @BindView(R.id.lnrPaymentAlert)
    LinearLayout lnrPaymentAlert;
    @BindView(R.id.wvProcess)
    WebView wvProcess;
    Map<String, String> hashMap;
    String postData="";

    private String title = "", mUrl = "", mPaymentType = "",key="",apiKey="";
    int AuthKey;
    String strAgentId="",strBookFrom="";
    int paymentFlag=0;

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_payment_back_alert);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        TextView btnOk = dialog.findViewById(R.id.tvOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }*/
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent e) {

       if (keyCode == KeyEvent.KEYCODE_BACK) {
           final Dialog dialog = new Dialog(this);
           dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
           dialog.setContentView(R.layout.dailog_payment_back_alert);
           dialog.setCancelable(false);
           dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
           dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


           TextView btnOk = dialog.findViewById(R.id.tvOk);
           TextView tvBack= dialog.findViewById(R.id.tvPaymentMsg);

           if(paymentFlag==2){
               tvBack.setText(R.string.msg_back_mpesa);
           }else if(paymentFlag==3){
               tvBack.setText(R.string.msg_back_tigo);
           }else if(paymentFlag==4) {
               tvBack.setText(R.string.msg_back_airtel);
           }

           btnOk.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialog.dismiss();
               }
           });


           dialog.show();
           return true;
       }

       return super.onKeyDown(keyCode, e);
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_payment_processing);
        ButterKnife.bind(this);
        InitializeControls();

    }
    public void InitializeControls(){
        wvProcess.setWebChromeClient(new MyWebChromeClient());
        wvProcess.setWebViewClient(new MyWebViewClient());
        wvProcess.getSettings().setDomStorageEnabled(true);
        wvProcess.getSettings().setJavaScriptEnabled(true);
        wvProcess.getSettings().setAppCacheEnabled(true);
        wvProcess.getSettings().setLoadsImagesAutomatically(true);
        wvProcess.addJavascriptInterface(new WebAppInterface(getApplicationContext()), "Android");
        hashMap= new HashMap<>();
        strAgentId= AppConstants.agentId;
        strBookFrom=AppConstants.bookFrom;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvProcess.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        String title=getIntent().getExtras().getString(AppConstants.BNDL_TITLE);
        pageTitle.setText(title);
        try {
            hashMap = (Map<String, String>) getIntent().getExtras().getSerializable("postdata");
        }catch (Exception e){
            e.printStackTrace();
        }
        mPaymentType = getIntent().getExtras().getString(AppConstants.BNDL_PAYMENT_TYPE);
        mUrl = getIntent().getExtras().getString(AppConstants.BNDL_URL);

        if (!TextUtils.isEmpty(mPaymentType)) {
            if (mPaymentType.equalsIgnoreCase(AppConstants.BNDL_PAYMENT_TYPE_MPESA)) {
                lnrPaymentAlert.setVisibility(View.VISIBLE);
                tvMobile.setBackgroundResource(R.drawable.ic_mpesa);
                SpannableStringBuilder strMpesa = new SpannableStringBuilder(getString(R.string.msg_payment_mpesa_txt));
                strMpesa.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 29, 35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvWarning.setText(strMpesa);

            } else if (mPaymentType.equalsIgnoreCase(AppConstants.BNDL_PAYMENT_TYPE_TIGO)) {
                lnrPaymentAlert.setVisibility(View.VISIBLE);

                tvMobile.setBackgroundResource(R.drawable.ic_tigo_pesa);
                SpannableStringBuilder strMpesa = new SpannableStringBuilder(getString(R.string.msg_payment_tigo_txt));
                strMpesa.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 29, 39, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvWarning.setText(strMpesa);

            } else if (mPaymentType.equalsIgnoreCase(AppConstants.BNDL_PAYMENT_TYPE_AIRTEL)) {
                lnrPaymentAlert.setVisibility(View.VISIBLE);

                tvMobile.setBackgroundResource(R.drawable.ic_airtel_money);
                SpannableStringBuilder strMpesa = new SpannableStringBuilder(getString(R.string.msg_payment_airtel_txt));
                strMpesa.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 29, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvWarning.setText(strMpesa);

            } else {
                lnrPaymentAlert.setVisibility(View.GONE);
            }
        }





        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }
        AuthKey = Integer.parseInt(String.valueOf(random));


       // wvProcess.loadUrl("https://www.google.com/");
        if(mPaymentType.equals(AppConstants.BNDL_PAYMENT_TYPE_DEBIT)) {
            paymentFlag=1;
            String cardexp = hashMap.get("careexp");
            String cardno = hashMap.get("cardno");
            String cardcvv = hashMap.get("cardcvv");
            String cardtype = hashMap.get("cardtype");
            String seat = hashMap.get("no_tkts");
            String custname = hashMap.get("cust_name");
            String custemail = hashMap.get("cust_email");
            String custmob = hashMap.get("cust_mob").replaceAll("\\+","");
            String ukey = hashMap.get("ukey");
            String evtdatetime = hashMap.get("evt_date_time");
            String evtpid = hashMap.get("evt_pid");
            String custId = hashMap.get("cust_id");
            String promoId = hashMap.get("promo_id");

            key=calculateHash(AuthKey, calculateHash(1, calculateHash(4,strAgentId+strBookFrom+evtpid+evtdatetime+
                    ukey+promoId+seat+custId+custname+custemail+custmob+"1"+cardtype+cardno+cardexp+cardcvv+"AND"+"Bo0k(rD6")));


            apiKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"b0OkcR)b")));


                postData="auth_key="+AuthKey+"&agent_id="+strAgentId+"&book_from="+strBookFrom+"&event_id="+evtpid+"&event_date="+evtdatetime+
                        "&ukey="+ukey+"&promo_id="+promoId+"&no_tkts="+seat+"&cust_id="+custId+"&cust_name="+custname+
                        "&email="+custemail+"&mob="+custmob+"&mg_term="+"1"+"&card_type="+cardtype+"&cardno="+cardno+
                        "&cardexp="+cardexp+"&cardcvv="+cardcvv+"&req_from=AND&key="+key;


            mUrl=mUrl+"key="+apiKey;

           wvProcess.postUrl(mUrl, postData.getBytes());
           // setPaymentSucessful("TT33292221");
        }else if(mPaymentType.equals(AppConstants.BNDL_PAYMENT_TYPE_MPESA)){
            paymentFlag=2;

            String seat = hashMap.get("no_tkts");
            String custname = hashMap.get("cust_name");
            String custemail = hashMap.get("cust_email");
            String custmob = hashMap.get("cust_mob").replaceAll("\\+","");
            String ukey = hashMap.get("ukey");
            String evtdatetime = hashMap.get("evt_date_time");
            String evtpid = hashMap.get("evt_pid");
            String custId = hashMap.get("cust_id");
            String promoId = hashMap.get("promo_id");
            String mPesa=hashMap.get("mPesa").replaceAll("\\+","");

            key=calculateHash(AuthKey, calculateHash(1, calculateHash(4,strAgentId+strBookFrom+evtpid+evtdatetime+
                    ukey+promoId+seat+custId+custname+custemail+custmob+"1"+mPesa+"AND"+"Bo0kMpE5@")));

            apiKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"b0OkmPe52")));

         /*   postData="auth_key="+AuthKey+"&agent_id="+strAgentId+"&book_from="+strBookFrom+"&event_id="+evtpid+"&event_date="+evtdatetime+
                    "&ukey="+ukey+"&promo_id="+promoId+"&no_tkts="+seat+"&cust_id="+custId+"&cust_name="+custname+
                    "&email="+custemail+"&mob="+custmob+"&mpesa_term="+"1"+"&req_from=AND&key="+key+"&mpesa="+mPesa;
            mUrl=mUrl+"key="+apiKey;
            wvProcess.postUrl(mUrl, postData.getBytes());*/


            OtappApiServices otappApiServices = RestClient.getClient(true);
            Call<TicketNumberResponse> mCallTicketNumber = otappApiServices.getMpesaPaymentProceed(apiKey,strAgentId,strBookFrom, String.valueOf(AuthKey),evtpid,ukey,promoId,
                    evtdatetime,seat,custId,custname,custemail,custmob,"1","AND",key,mPesa);
            mCallTicketNumber.enqueue(new Callback<TicketNumberResponse>() {
                @Override
                public void onResponse(Call<TicketNumberResponse> call, Response<TicketNumberResponse> response) {
                    JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response : " + jsonObjectResponse);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(response!=null){
                        if(response.body().status==200){
                            setPaymentSucessful(response.body().ticket);
                        }else if(response.body().status==401){
                            showFailurDialog(response.body().tran_id,response.body().msidn,response.body().amount,response.body().message);
                        }else {
                            Toast.makeText(EventPaymentProcessingActivity.this, ""+response.body().message, Toast.LENGTH_SHORT).show();
                            showFailurDialog(response.body().tran_id,response.body().msidn,response.body().amount,response.body().message);
                        }
                    }


                }

                @Override
                public void onFailure(Call<TicketNumberResponse> call, Throwable t) {
                    Log.d("Log",t.getMessage());

                }
            });



        }else if(mPaymentType.equals(AppConstants.BNDL_PAYMENT_TYPE_TIGO)){
            paymentFlag=3;
            String seat = hashMap.get("no_tkts");
            String custname = hashMap.get("cust_name");
            String custemail = hashMap.get("cust_email");
            String custmob = hashMap.get("cust_mob").replaceAll("\\+","");
            String ukey = hashMap.get("ukey");
            String evtdatetime = hashMap.get("evt_date_time");
            String evtpid = hashMap.get("evt_pid");
            String custId = hashMap.get("cust_id");
            String promoId = hashMap.get("promo_id");
            String tigo=hashMap.get("tigo").replaceAll("\\+","");

            key=calculateHash(AuthKey, calculateHash(1, calculateHash(4,strAgentId+strBookFrom+evtpid+evtdatetime+
                    ukey+promoId+seat+custId+custname+custemail+custmob+"1"+tigo+"AND"+"Bo0kT!G)")));

            apiKey= calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"b0Okt1Go")));


            OtappApiServices otappApiServices = RestClient.getClient(true);
            Call<TicketNumberResponse> mCallTicketNumber = otappApiServices.getTigoPaymentProceed(apiKey,strAgentId,strBookFrom, String.valueOf(AuthKey),evtpid,ukey,promoId,
                    evtdatetime,seat,custId,custname,custemail,custmob,"1","AND",key,tigo);
            mCallTicketNumber.enqueue(new Callback<TicketNumberResponse>() {
                @Override
                public void onResponse(Call<TicketNumberResponse> call, Response<TicketNumberResponse> response) {
                    JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response : " + jsonObjectResponse);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(response!=null){
                        if(response.body().status==200){
                            setPaymentSucessful(response.body().ticket);
                        }else if(response.body().status==401){
                            showFailurDialog(response.body().tran_id,response.body().msidn,response.body().amount,response.body().message);
                        }else {
                            Toast.makeText(EventPaymentProcessingActivity.this, ""+response.body().message, Toast.LENGTH_SHORT).show();
                            showFailurDialog(response.body().tran_id,response.body().msidn,response.body().amount,response.body().message);
                        }
                    }


                }

                @Override
                public void onFailure(Call<TicketNumberResponse> call, Throwable t) {
                    Log.d("Log",t.getMessage());

                }
            });


        }else if(mPaymentType.equals(AppConstants.BNDL_PAYMENT_TYPE_AIRTEL)){
            paymentFlag=4;

            if (!Utils.isProgressDialogShowing()) {
                Utils.showProgressDialog(getApplicationContext());
            }

            String seat = hashMap.get("no_tkts");
            String custname = hashMap.get("cust_name");
            String custemail = hashMap.get("cust_email");
            String custmob = hashMap.get("cust_mob").replaceAll("\\+","");
            String ukey = hashMap.get("ukey");
            String evtdatetime = hashMap.get("evt_date_time");
            String evtpid = hashMap.get("evt_pid");
            String custId = hashMap.get("cust_id");
            String promoId = hashMap.get("promo_id");
            String airtel=hashMap.get("airtel").replaceAll("\\+","");

            key=calculateHash(AuthKey, calculateHash(1, calculateHash(4,strAgentId+strBookFrom+evtpid+evtdatetime+
                    ukey+promoId+seat+custId+custname+custemail+custmob+"1"+airtel+"AND"+"Bo0k@1RTe!")));

            apiKey= calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"b0Ok@!rtE1")));


         OtappApiServices otappApiServices = RestClient.getClient(true);
         Call<TicketNumberResponse> mCallTicketNumber = otappApiServices.getAirtelPaymentProceed(apiKey,strAgentId,strBookFrom, String.valueOf(AuthKey),evtpid,ukey,promoId,
                 evtdatetime,seat,custId,custname,custemail,custmob,"1","AND",key,airtel);
         mCallTicketNumber.enqueue(new Callback<TicketNumberResponse>() {
             @Override
             public void onResponse(Call<TicketNumberResponse> call, Response<TicketNumberResponse> response) {

                 JSONObject jsonObjectResponse = null;
                 try {
                     jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                     Log.d("Log", "Response : " + jsonObjectResponse);

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 if(response!=null){
                     if(response.body().status==200){
                         Utils.closeProgressDialog();
                         setPaymentSucessful(response.body().ticket);
                     }else if(response.body().status==401){
                         Utils.closeProgressDialog();
                         showFailurDialog(response.body().tran_id,response.body().msidn,response.body().amount,response.body().message);
                     }else {
                         Utils.closeProgressDialog();
                         Toast.makeText(EventPaymentProcessingActivity.this, ""+response.body().message, Toast.LENGTH_SHORT).show();
                         showFailurDialog(response.body().tran_id,response.body().msidn,response.body().amount,response.body().message);
                     }
                 }


             }

             @Override
             public void onFailure(Call<TicketNumberResponse> call, Throwable t) {
                 Log.d("Log",t.getMessage());
                 Utils.closeProgressDialog();
             }
         });

        }

    }

    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int progress) {
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        }
    }
    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("Log","sarting url "+url);
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                wvProcess.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
            if (!mPaymentType.equalsIgnoreCase(AppConstants.BNDL_PAYMENT_TYPE_MPESA)) {
                if (!Utils.isProgressDialogShowing()) {
                    Utils.showProgressDialog(getApplicationContext());
                }
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("Log","sarting url "+url);
            Utils.closeProgressDialog();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("Log","sarting url "+url);
            LogUtils.e("", "shouldOverrideUrlLoading url::" + url);
            return super.shouldOverrideUrlLoading(view, url);

        }

        @Override
        public void onLoadResource(WebView view, String url) {
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
        }
    }
    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void dcPaymentSuccessful(String mResponse) {
            LogUtils.e("", "dcPaymentSuccessful mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), getString(R.string.alert_payment_successful));
            if (!TextUtils.isEmpty(mResponse)) {
                setPaymentSucessful(mResponse);
            }
        }

        @JavascriptInterface
        public void dcPaymentCancelled(String mResponse) {
            LogUtils.e("", "dcPaymentCancelled mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), "" + mResponse);
           // popBackStack();
            callHomePage();

        }

        @JavascriptInterface
        public void dcPaymentFail(String mResponse) {
            LogUtils.e("Log1", "dcPaymentFail mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), "" + mResponse);
           // popBackStack(ServiceFragment.Tag_ServiceFragment);
            callHomePage();

        }

        @JavascriptInterface
        public void dcPaymentFailaftertransaction(String transaction_id, String msidn, String amount, String message) {
            LogUtils.e("Log1", "dcPaymentFailaftertransaction mResponse::" + transaction_id + " " + msidn + " " + amount);
            Utils.showToast(getApplicationContext(), getString(R.string.alert_payment_fail));
            showFailurDialog(transaction_id, msidn, amount, message);
        }

        @JavascriptInterface
        public void mPesaPaymentSuccessful(String mResponse) {
            LogUtils.e("", "mPesaPaymentSuccessful mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), getString(R.string.alert_payment_successful));
            if (!TextUtils.isEmpty(mResponse)) {
                setPaymentSucessful(mResponse);
            }
        }


        @JavascriptInterface
        public void mPesaPaymentCancelled(String mResponse) {
            LogUtils.e("", "mPesaPaymentCancelled mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), "" + mResponse);

         //   popBackStack();

        }

        @JavascriptInterface
        public void mPesaPaymentFail(String mResponse) {
            LogUtils.e("Log1", "mPesaPaymentFail mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), "" + mResponse);
         //   popBackStack(ServiceFragment.Tag_ServiceFragment);

        }

        @JavascriptInterface
        public void mPesaPaymentFailaftertransaction(String transaction_id, String msidn, String amount, String message) {
            LogUtils.e("", "mPesaPaymentFailaftertransaction mResponse::" + transaction_id + " " + msidn + " " + amount);
            Utils.showToast(getApplicationContext(), getString(R.string.alert_payment_fail));
            showFailurDialog(transaction_id, msidn, amount, message);
        }

        @JavascriptInterface
        public void mTigoPaymentSuccessful(String mResponse) {
            LogUtils.e("Log1", "mTigoPaymentSuccessful mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), getString(R.string.alert_payment_successful));
            if (!TextUtils.isEmpty(mResponse)) {
                setPaymentSucessful(mResponse);
            }
        }

        @JavascriptInterface
        public void mTigoPaymentCancelled(String mResponse) {
            LogUtils.e("Log1", "mTigoPaymentCancelled mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), "" + mResponse);
       //     popBackStack();

        }

        @JavascriptInterface
        public void mTigoPaymentFail(String mResponse) {
            LogUtils.e("Log", "mTigoPaymentFail mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), "" + mResponse);
    //        popBackStack(ServiceFragment.Tag_ServiceFragment);
        }

        @JavascriptInterface
        public void mTigoPaymentFailaftertransaction(String transaction_id, String msidn, String amount, String message) {
            LogUtils.e("Log1", "mTigoPaymentFail mResponse::" + transaction_id + " " + msidn + " " + amount);
            Utils.showToast(getApplicationContext(), getString(R.string.alert_payment_fail));
            showFailurDialog(transaction_id, msidn, amount, message);
        }

        @JavascriptInterface
        public void airtelPaymentSuccessful(String mResponse) {
            LogUtils.e("Log", "airtelPaymentSuccessful mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), getString(R.string.alert_payment_successful));
            if (!TextUtils.isEmpty(mResponse)) {
                setPaymentSucessful(mResponse);
            }
        }

        @JavascriptInterface
        public void airtelPaymentCancelled(String mResponse) {
            LogUtils.e("Log", "airtelPaymentCancelled mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), "" + mResponse);
   //         popBackStack();

        }

        @JavascriptInterface
        public void airtelPaymentFail(String mResponse) {
            LogUtils.e("Log", "airtelPaymentFail mResponse::" + mResponse);
            Utils.showToast(getApplicationContext(), "" + mResponse);
     //       popBackStack(ServiceFragment.Tag_ServiceFragment);
        }

        @JavascriptInterface
        public void airtelPaymentFailaftertransaction(String transaction_id, String msidn, String amount, String message) {
            LogUtils.e("Log", "airtelPaymentFailaftertransaction mResponse::" + transaction_id + " " + msidn + " " + amount);
            Utils.showToast(getApplicationContext(), getString(R.string.alert_payment_fail));
           showFailurDialog(transaction_id, msidn, amount, message);
        }

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
    private void showFailurDialog(String transaction_id, String msidn, String amount, String message) {
        Utils.closeProgressDialog();
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_payment_failure);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tvTransactionID = (TextView) dialog.findViewById(R.id.tvTransactionID);
        final TextView tvMsidn = (TextView) dialog.findViewById(R.id.tvMsidn);
        final TextView tvAmount = (TextView) dialog.findViewById(R.id.tvAmount);
        final TextView tvOkay = (TextView) dialog.findViewById(R.id.tvOkay);

        tvTransactionID.setText(getString(R.string.transaction_id) + " " + transaction_id);
        tvMsidn.setText(getString(R.string.msidn) + " " + msidn);
        tvAmount.setText(getString(R.string.amount) + " " + amount);
        tvAmount.setText(getString(R.string.message) + " " + message);

        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   popBackStack(ServiceFragment.Tag_ServiceFragment);
                finish();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                    callHomePage();
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

    }
    public void callHomePage(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    private void setPaymentSucessful(String mBookingID) {
        Utils.closeProgressDialog();
//        popBackStack(ServiceFragment.Tag_ServiceFragment);


        if (!Utils.isInternetConnected(getApplicationContext())) {
            Utils.showToast(getApplicationContext(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getApplicationContext());

        String apiKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"tKt!nf0")));

        String key= calculateHash(AuthKey, calculateHash(1, calculateHash(4,strAgentId+strBookFrom+mBookingID+"TkT1nF)")));


        OtappApiServices otappApiServices = RestClient.getClient(true);
        Call<PaymentSuceesResponse> callTicketInfo = otappApiServices.getSuccessTicketInfo(apiKey,strAgentId,strBookFrom, String.valueOf(AuthKey),key,mBookingID);

        callTicketInfo.enqueue(new Callback<PaymentSuceesResponse>() {
            @Override
            public void onResponse(Call<PaymentSuceesResponse> call, Response<PaymentSuceesResponse> response) {
                if (response.isSuccessful()) {
                    Utils.closeProgressDialog();
                    PaymentSuceesResponse mEventSuccessPojo = response.body();
                    if (mEventSuccessPojo != null) {
                        if (mEventSuccessPojo.status.equalsIgnoreCase("200")) {

                            Utils.closeProgressDialog();
                           // switchFragment(EventOrderReviewFragment.newInstance(), EventOrderReviewFragment.Tag_EventOrderReviewFragment, bundle);
                            Intent intent = new Intent(getApplicationContext(), TicketSuccessResponseActivity.class);
                            intent.putExtra(AppConstants.BNDL_MOVIE_RESPONSE, new Gson().toJson(mEventSuccessPojo));
                            startActivity(intent);

                        } else {
                            Utils.showToast(getApplicationContext(), mEventSuccessPojo.message);
                        //    popBackStack(ServiceFragment.Tag_ServiceFragment);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<PaymentSuceesResponse> call, Throwable t) {

            }
        });

/*
        mCall.enqueue(new Callback<EventSuccessPojo>() {
            @Override
            public void onResponse(Call<EventSuccessPojo> call, Response<EventSuccessPojo> response) {

                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    Utils.closeProgressDialog();
                    EventSuccessPojo mEventSuccessPojo = response.body();
                    if (mEventSuccessPojo != null) {
                        if (mEventSuccessPojo.status.equalsIgnoreCase("200")) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_MOVIE_RESPONSE, new Gson().toJson(mEventSuccessPojo.data));
                            Utils.closeProgressDialog();
                            switchFragment(EventOrderReviewFragment.newInstance(), EventOrderReviewFragment.Tag_EventOrderReviewFragment, bundle);
                        } else {
                            Utils.showToast(getActivity(), mEventSuccessPojo.message);
                            popBackStack(ServiceFragment.Tag_ServiceFragment);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<EventSuccessPojo> call, Throwable t) {
                LogUtils.e("", "onFailure::" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });
*/


    }


}
