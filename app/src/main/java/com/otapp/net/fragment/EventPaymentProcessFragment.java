package com.otapp.net.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.EventSuccessPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPaymentProcessFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_EventPaymentProcessFragment = "Tag_" + "EventPaymentProcessFragment";

    View mView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvWarning)
    TextView tvWarning;
    @BindView(R.id.tvMobile)
    TextView tvMobile;
    @BindView(R.id.lnrPaymentAlert)
    LinearLayout lnrPaymentAlert;
    @BindView(R.id.wvProcess)
    WebView wvProcess;
    Map<String,String> hashMap;
    String postData="";

    private String title = "", mUrl = "", mPaymentType = "";


    public static EventPaymentProcessFragment newInstance() {
        EventPaymentProcessFragment fragment = new EventPaymentProcessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_event_payment_process, container, false);
        ButterKnife.bind(this, mView);
        InitializeControls();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return mView;
    }

    private void InitializeControls() {

        wvProcess.setWebChromeClient(new MyWebChromeClient());
        wvProcess.setWebViewClient(new MyWebViewClient());
        wvProcess.getSettings().setDomStorageEnabled(true);
        wvProcess.getSettings().setJavaScriptEnabled(true);
        wvProcess.getSettings().setAppCacheEnabled(true);
        wvProcess.getSettings().setLoadsImagesAutomatically(true);
        wvProcess.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");
        hashMap= new HashMap<>();

//        wvProcess.getSettings().setJavaScriptEnabled(true);
//        CookieManager.getInstance().setAcceptCookie(true);

//        wvProcess.getSettings().setLoadWithOverviewMode(false);
//        wvProcess.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvProcess.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString(Constants.BNDL_TITLE);
            mUrl = bundle.getString(Constants.BNDL_URL);
            hashMap= (Map<String, String>) bundle.getSerializable("postdata");
            mPaymentType = bundle.getString(Constants.BNDL_PAYMENT_TYPE);

            if (!TextUtils.isEmpty(mPaymentType)) {
                if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_MPESA)) {
                    lnrPaymentAlert.setVisibility(View.VISIBLE);

                    tvMobile.setBackgroundResource(R.drawable.ic_mpesa);

                    SpannableStringBuilder strMpesa = new SpannableStringBuilder(getString(R.string.msg_payment_mpesa_txt));
                    strMpesa.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 29, 35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvWarning.setText(strMpesa);

                } else if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_TIGO)) {
                    lnrPaymentAlert.setVisibility(View.VISIBLE);

                    tvMobile.setBackgroundResource(R.drawable.ic_tigo_pesa);
                    SpannableStringBuilder strMpesa = new SpannableStringBuilder(getString(R.string.msg_payment_tigo_txt));
                    strMpesa.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 29, 39, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvWarning.setText(strMpesa);

                } else if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_AIRTEL)) {
                    lnrPaymentAlert.setVisibility(View.VISIBLE);

                    tvMobile.setBackgroundResource(R.drawable.ic_airtel_money);
                    SpannableStringBuilder strMpesa = new SpannableStringBuilder(getString(R.string.msg_payment_airtel_txt));
                    strMpesa.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 29, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvWarning.setText(strMpesa);

                } else {
                    lnrPaymentAlert.setVisibility(View.GONE);
                }
            }

            LogUtils.e("", "mUrl::" + mUrl);

            if (!TextUtils.isEmpty(title)) {
                tvTitle.setText("" + title);
            }

            if (!TextUtils.isEmpty(mUrl)) {

//                if (!Utils.isInternetConnected(getActivity())) {
//                    Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
//                    return;
//                }
//
//
//                ApiInterface mApiInterface = RestClient.getClient(true);
//                Call<String> mCall = mApiInterface.getPaymentApi(mUrl);
//                mCall.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.isSuccessful()) {
//                            String mData = response.body().toString();
//                            LogUtils.e("", "mData::"+mData);
//                            if (!TextUtils.isEmpty(mData)) {
////                                wvProcess.loadData(mData, "text/html", "UTF-8");
//                                wvProcess.loadDataWithBaseURL(mUrl, mData, "text/html", "charset=UTF-8", null);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//
//                    }
//                });
////
          //      wvProcess.loadUrl(mUrl);
                if(mPaymentType.equals(Constants.BNDL_PAYMENT_TYPE_DEBIT)) {
                    String cardexp = hashMap.get("careexp");
                    String cardno = hashMap.get("cardno");
                    String cardcvv = hashMap.get("cardcvv");
                    String cardtype = hashMap.get("cardtype");
                    String seat = hashMap.get("seats");
                    String custname = hashMap.get("cust_name");
                    String custemail = hashMap.get("cust_email");
                    String custmob = hashMap.get("cust_mob");
                    String ukey = hashMap.get("ukey");
                    String evtdatetime = hashMap.get("evt_date_time");
                    String evtpid = hashMap.get("evt_pid");
                    String custId= hashMap.get("cust_id");
                    String promoId= hashMap.get("promo_id");

                    Log.d("Log","cardexp "+cardexp );
                    Log.d("Log","cardno "+cardno );


                    Log.d("Log","cardcvv "+cardcvv );
                    Log.d("Log","card_type "+cardtype );
                    Log.d("Log","seats "+seat );
                    Log.d("Log","cust_name "+custname );
                    Log.d("Log","cust_email "+custemail );
                    Log.d("Log","cust_mob "+custmob );
                    Log.d("Log","ukey "+ukey);
                    Log.d("Log","evt_date_time "+evtdatetime);
                    Log.d("Log","evt_pid "+evtpid);
                    Log.d("Log","cust_id "+custId);
                    Log.d("Log","promo_id "+promoId);


                    try {
                        postData = "cardexp=" + URLEncoder.encode(cardexp, "UTF-8") + "&cardno=" + URLEncoder.encode(cardno, "UTF-8")
                                + "&cardcvv=" + URLEncoder.encode(cardcvv, "UTF-8") + "&card_type=" + URLEncoder.encode(cardtype, "UTF-8")
                                + "&seats=" + URLEncoder.encode(seat, "UTF-8") + "&cust_name=" + URLEncoder.encode(custname, "UTF-8")
                                + "&cust_email=" + URLEncoder.encode(custemail, "UTF-8") + "&cust_mob=" + URLEncoder.encode(custmob, "UTF-8") + "&ukey=" + URLEncoder.encode(ukey, "UTF-8") + "&evt_date_time="
                                + URLEncoder.encode(evtdatetime, "UTF-8") + "&evt_pid=" + URLEncoder.encode(evtpid, "UTF-8")
                                +"&cust_id="+custId+"&promo_id="+promoId;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    Log.d("Log", "postdata  " + postData);


                    wvProcess.postUrl(mUrl, postData.getBytes());
                }else {
                    wvProcess.loadUrl(mUrl);
                }

            }
        }

        tvBack.setOnClickListener(this);


    }

    public String getPaymentType() {
        return mPaymentType;
    }

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_MPESA)) {
                Utils.showToast(getActivity(), getString(R.string.msg_back_mpesa));
            } else if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_TIGO)) {
                Utils.showToast(getActivity(), getString(R.string.msg_back_tigo));
            } else {
                popBackStack();
            }

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
            if (!url.contains("docs.google.com") && url.endsWith(".pdf")) {
                wvProcess.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
            if (!mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_MPESA)) {
                if (!Utils.isProgressDialogShowing()) {
                    Utils.showProgressDialog(getActivity());
                }
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Utils.closeProgressDialog();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.e("", "shouldOverrideUrlLoading url::" + url);
//            if (url.endsWith(".mp4")) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(url), "video/*");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                view.getContext().startActivity(intent);
//                // If we return true, onPageStarted, onPageFinished won't be called.
//                return true;
//            } else if (url.startsWith("tel:") || url.startsWith("sms:") || url.startsWith("smsto:") || url
//                    .startsWith("mms:") || url.startsWith("mmsto:")) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                view.getContext().startActivity(intent);
//                return true; // If we return true, onPageStarted, onPageFinished won't be called.
//            }
//            /*******************************************************
//             * Added in support for mailto:
//             *******************************************************/
//            else if (url.startsWith("mailto:")) {
//
//                MailTo mt = MailTo.parse(url);
//
//                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//
//                emailIntent.setType("text/html");
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mt.getTo()});
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, mt.getSubject());
//                emailIntent.putExtra(Intent.EXTRA_CC, mt.getCc());
//                emailIntent.putExtra(Intent.EXTRA_TEXT, mt.getBody());
//
//                startActivity(emailIntent);
//
//                return true;
//            } else {
            return super.shouldOverrideUrlLoading(view, url);
//            }
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
            Utils.showToast(getActivity(), getString(R.string.alert_payment_successful));
            if (!TextUtils.isEmpty(mResponse)) {
                setPaymentSucessful(mResponse);
            }
        }

        @JavascriptInterface
        public void dcPaymentCancelled(String mResponse) {
            LogUtils.e("", "dcPaymentCancelled mResponse::" + mResponse);
            Utils.showToast(getActivity(), "" + mResponse);
            popBackStack();

        }

        @JavascriptInterface
        public void dcPaymentFail(String mResponse) {
            LogUtils.e("", "dcPaymentFail mResponse::" + mResponse);
            Utils.showToast(getActivity(), "" + mResponse);
            popBackStack(ServiceFragment.Tag_ServiceFragment);

        }

        @JavascriptInterface
        public void dcPaymentFailaftertransaction(String transaction_id, String msidn, String amount, String message) {
            LogUtils.e("", "dcPaymentFailaftertransaction mResponse::" + transaction_id + " " + msidn + " " + amount);
            Utils.showToast(getActivity(), getString(R.string.alert_payment_fail));
            showFailurDialog(transaction_id, msidn, amount, message);
        }

        @JavascriptInterface
        public void mPesaPaymentSuccessful(String mResponse) {
            LogUtils.e("", "mPesaPaymentSuccessful mResponse::" + mResponse);
            Utils.showToast(getActivity(), getString(R.string.alert_payment_successful));
            if (!TextUtils.isEmpty(mResponse)) {
                setPaymentSucessful(mResponse);
            }
        }


        @JavascriptInterface
        public void mPesaPaymentCancelled(String mResponse) {
            LogUtils.e("", "mPesaPaymentCancelled mResponse::" + mResponse);
            Utils.showToast(getActivity(), "" + mResponse);
            popBackStack();

        }

        @JavascriptInterface
        public void mPesaPaymentFail(String mResponse) {
            LogUtils.e("", "mPesaPaymentFail mResponse::" + mResponse);
            Utils.showToast(getActivity(), "" + mResponse);
            popBackStack(ServiceFragment.Tag_ServiceFragment);

        }

        @JavascriptInterface
        public void mPesaPaymentFailaftertransaction(String transaction_id, String msidn, String amount,  String message) {
            LogUtils.e("", "mPesaPaymentFailaftertransaction mResponse::" + transaction_id + " " + msidn + " " + amount);
            Utils.showToast(getActivity(), getString(R.string.alert_payment_fail));
            showFailurDialog(transaction_id, msidn, amount, message);
        }

        @JavascriptInterface
        public void mTigoPaymentSuccessful(String mResponse) {
            LogUtils.e("", "mTigoPaymentSuccessful mResponse::" + mResponse);
            Utils.showToast(getActivity(), getString(R.string.alert_payment_successful));
            if (!TextUtils.isEmpty(mResponse)) {
                setPaymentSucessful(mResponse);
            }
        }

        @JavascriptInterface
        public void mTigoPaymentCancelled(String mResponse) {
            LogUtils.e("", "mTigoPaymentCancelled mResponse::" + mResponse);
            Utils.showToast(getActivity(), "" + mResponse);
            popBackStack();

        }

        @JavascriptInterface
        public void mTigoPaymentFail(String mResponse) {
            LogUtils.e("", "mTigoPaymentFail mResponse::" + mResponse);
            Utils.showToast(getActivity(), "" + mResponse);
            popBackStack(ServiceFragment.Tag_ServiceFragment);
        }

        @JavascriptInterface
        public void mTigoPaymentFailaftertransaction(String transaction_id, String msidn, String amount,  String message) {
            LogUtils.e("", "mTigoPaymentFail mResponse::" + transaction_id + " " + msidn + " " + amount);
            Utils.showToast(getActivity(), getString(R.string.alert_payment_fail));
            showFailurDialog(transaction_id, msidn, amount, message);
        }

        @JavascriptInterface
        public void airtelPaymentSuccessful(String mResponse) {
            LogUtils.e("", "airtelPaymentSuccessful mResponse::" + mResponse);
            Utils.showToast(getActivity(), getString(R.string.alert_payment_successful));
            if (!TextUtils.isEmpty(mResponse)) {
                setPaymentSucessful(mResponse);
            }
        }

        @JavascriptInterface
        public void airtelPaymentCancelled(String mResponse) {
            LogUtils.e("", "airtelPaymentCancelled mResponse::" + mResponse);
            Utils.showToast(getActivity(), "" + mResponse);
            popBackStack();

        }

        @JavascriptInterface
        public void airtelPaymentFail(String mResponse) {
            LogUtils.e("", "airtelPaymentFail mResponse::" + mResponse);
            Utils.showToast(getActivity(), "" + mResponse);
            popBackStack(ServiceFragment.Tag_ServiceFragment);
        }

        @JavascriptInterface
        public void airtelPaymentFailaftertransaction(String transaction_id, String msidn, String amount, String message) {
            LogUtils.e("", "airtelPaymentFailaftertransaction mResponse::" + transaction_id + " " + msidn + " " + amount);
            Utils.showToast(getActivity(), getString(R.string.alert_payment_fail));
            showFailurDialog(transaction_id, msidn, amount, message);
        }

    }

    private void showFailurDialog(String transaction_id, String msidn, String amount, String message) {
        Utils.closeProgressDialog();
        final Dialog dialog = new Dialog(getActivity());
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

                popBackStack(ServiceFragment.Tag_ServiceFragment);

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

    }

    private void setPaymentSucessful(String mBookingID) {
        Utils.closeProgressDialog();
//        popBackStack(ServiceFragment.Tag_ServiceFragment);

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        /*Utils.showProgressDialog(getActivity());*/

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<EventSuccessPojo> mCall = mApiInterface.getEventPaymentSuccess(mBookingID, Otapp.mUniqueID);
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


    }
}