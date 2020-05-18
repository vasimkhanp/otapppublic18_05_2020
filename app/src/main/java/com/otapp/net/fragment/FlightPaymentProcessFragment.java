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
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightOneDetailsPojo;
import com.otapp.net.model.FlightSuccessPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
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

public class FlightPaymentProcessFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_FlightPaymentProcessFragment = "Tag_" + "FlightPaymentProcessFragment";

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
    @BindView(R.id.lnrFlightPNR)
    LinearLayout lnrFlightPNR;
    @BindView(R.id.wvProcess)
    WebView wvProcess;
    Map<String, String> hashMap;
    String postData = null;
    String cardexp = null;
    String cardno = null;
    String cardcvv = null;
    String cardtype = null;
    String uuid = null;
    String key = null;
    String adults = null;
    String childern = null;
    String infants = null;
    String custemail = null;
    String custmob = null;
    String total = null;
    String currency = null;
    String startCity = null;
    String endCity = null;
    String ticktclass = null;
    String departurDate = null;
    String depatureArvDate = null;
    String returnDate = null;
    String returnArvDate = null;
    String bookinType = null;
    String isBookingFrom = null;
    String dpartureDuration = null;
    String returnDuration = null;
    String connectedFligt = null;
    String flightAuthToken = null;
    String airlineName = null;
    String promoId = null;
    String uKey = null;

    private String title = "", mUrl = "", mPaymentType = "";

    FlightOneDetailsPojo mFlightOneDetailsPojo;
    FlightCity mFlightCity;


    public static FlightPaymentProcessFragment newInstance() {
        FlightPaymentProcessFragment fragment = new FlightPaymentProcessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_flight_payment_process, container, false);
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
        hashMap = new HashMap<>();

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
            hashMap = (Map<String, String>) bundle.getSerializable("postdata");
            mPaymentType = bundle.getString(Constants.BNDL_PAYMENT_TYPE);
            mFlightCity = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT), FlightCity.class);
            mFlightOneDetailsPojo = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT_DETAILS_RESPONSE), FlightOneDetailsPojo.class);
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
                //  wvProcess.loadUrl(mUrl);
                try {
                    if (mPaymentType.equals(Constants.BNDL_PAYMENT_TYPE_DEBIT)) {
                        cardexp = hashMap.get("careexp");
                        cardno = hashMap.get("cardno");
                        cardcvv = hashMap.get("cardcvv");
                        cardtype = hashMap.get("cardtype");
                        uuid = hashMap.get("uuid");
                        key = hashMap.get("key");
                        adults = hashMap.get("adults");
                        childern = hashMap.get("children");
                        infants = hashMap.get("infants");
                        custemail = hashMap.get("cust_email");
                        custmob = hashMap.get("cust_mob");
                        total = hashMap.get("total");
                        currency = hashMap.get("currency");
                        startCity = hashMap.get("start_city");
                        endCity = hashMap.get("end_city");
                        ticktclass = hashMap.get("class");
                        departurDate = hashMap.get("depature_date");
                        depatureArvDate = hashMap.get("depature_arv_date");
                        returnDate = hashMap.get("return_date");
                        returnArvDate = hashMap.get("return_arv_date");
                        bookinType = hashMap.get("booking_type");
                        isBookingFrom = hashMap.get("is_booking_from");
                        dpartureDuration = hashMap.get("depature_duration");
                        returnDuration = hashMap.get("return_duration");
                        connectedFligt = hashMap.get("connected_flights");
                        flightAuthToken = hashMap.get("flight_auth_token");
                        airlineName = hashMap.get("airline_name");
                        promoId = hashMap.get("promo_id");
                        uKey = hashMap.get("ukey");

                        Log.d("Log", "Adults " + adults);
                        Log.d("Log", "Childre" + childern);
                        Log.d("Log", "infant" + infants);

                        postData = "cardexp=" + URLEncoder.encode(cardexp, "UTF-8") + "&cardno=" + URLEncoder.encode(cardno, "UTF-8")
                                + "&cardcvv=" + URLEncoder.encode(cardcvv, "UTF-8") + "&card_type=" + URLEncoder.encode(cardtype, "UTF-8")
                                + "&uuid=" + URLEncoder.encode(uuid, "UTF-8") + "&key=" + URLEncoder.encode(key, "UTF-8")
                                + "&adults=" + adults + "&children=" + childern + "&infants=" + infants + "&cust_email="
                                + URLEncoder.encode(custemail, "UTF-8")
                                + "&cust_mob=" + URLEncoder.encode(custmob, "UTF-8")
                                + "&total=" + URLEncoder.encode(total, "UTF-8")
                                + "&currency=" + currency
                                + "&start_city=" + URLEncoder.encode(startCity, "UTF-8")
                                + "&end_city=" + URLEncoder.encode(endCity, "UTF-8")
                                + "&class=" + URLEncoder.encode(ticktclass, "UTF-8")
                                + "&depature_date=" + URLEncoder.encode(departurDate, "UTF-8")
                                + "&depature_arv_date=" + URLEncoder.encode(depatureArvDate, "UTF-8")
                                + "&return_date=" + URLEncoder.encode(returnDate, "UTF-8")
                                + "&return_arv_date=" + URLEncoder.encode(returnArvDate, "UTF-8")
                                + "&booking_type=" + URLEncoder.encode(bookinType, "UTF-8")
                                + "&is_booking_from=" + URLEncoder.encode(isBookingFrom, "UTF-8")
                                + "&depature_duration=" + URLEncoder.encode(dpartureDuration, "UTF-8")
                                + "&return_duration=" + URLEncoder.encode(returnDuration, "UTF-8")
                                + "&connected_flights=" + URLEncoder.encode(connectedFligt, "UTF-8")
                                + "&flight_auth_token=" + URLEncoder.encode(flightAuthToken, "UTF-8")
                                + "&airline_name=" + URLEncoder.encode(airlineName, "UTF-8")
                                + "&promo_id=" + URLEncoder.encode(promoId, "UTF-8")
                                + "&ukey=" + URLEncoder.encode(uKey, "UTF-8");

//                    postData = postData.replaceAll("null", "");


                        Log.d("Log", "postdata  " + postData);


                        wvProcess.postUrl(mUrl, postData.getBytes());
//                wvProcess.loadUrl("https://www.google.com/");
                    } else {
                        wvProcess.loadUrl(mUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        tvBack.setOnClickListener(this);


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

    public String getPaymentType() {
        return mPaymentType;
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
                    Utils.showFlightProgressDialog(getActivity());
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
        public void mPesaPaymentFailaftertransaction(String transaction_id, String msidn, String amount, String message) {
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
        public void mTigoPaymentFailaftertransaction(String transaction_id, String msidn, String amount, String message) {
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

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                lnrFlightPNR.setVisibility(View.VISIBLE);
            }
        });

        Utils.showFlightProgressDialog(getActivity());

        ApiInterface mApiInterface = RestClient.getClient(true);
        Log.d("Log", "booking with = bookin id=" + mBookingID + "uniq id =" + Otapp.mUniqueID + "  auth token =" + mFlightCity.flightAuthToken);
        Call<FlightSuccessPojo> mCall = mApiInterface.getFlightPaymentSuccess(mBookingID, Otapp.mUniqueID, mFlightCity.flightAuthToken);
        mCall.enqueue(new Callback<FlightSuccessPojo>() {
            @Override
            public void onResponse(Call<FlightSuccessPojo> call, Response<FlightSuccessPojo> response) {

                Utils.closeProgressDialog();
                if (response.isSuccessful()) {



                    FlightSuccessPojo mFlightSuccessPojo = response.body();
                    if (mFlightSuccessPojo != null) {
                        if (mFlightSuccessPojo.status.equalsIgnoreCase("200")) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_FLIGHT_RESPONSE, new Gson().toJson(mFlightSuccessPojo.data));
                            bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
                            bundle.putString(Constants.BNDL_FLIGHT_DETAILS_RESPONSE, new Gson().toJson(mFlightOneDetailsPojo));
                            lnrFlightPNR.setVisibility(View.GONE);
                            switchFragment(FlightReviewOrderFragment.newInstance(), FlightReviewOrderFragment.Tag_FlightReviewOrderFragment, bundle);

                           /* getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    lnrFlightPNR.setVisibility(View.GONE);
                                }
                            });*/

                        } else if (mFlightSuccessPojo.status.equalsIgnoreCase("404") || mFlightSuccessPojo.status.equalsIgnoreCase("400")) {
                            showFlightFailurDialog(mFlightSuccessPojo.message);
                        } else {
                            Utils.showToast(getActivity(), mFlightSuccessPojo.message);
                            popBackStack(ServiceFragment.Tag_ServiceFragment);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<FlightSuccessPojo> call, Throwable t) {
                LogUtils.e("", "onFailure::" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });


    }

    private void showFlightFailurDialog(String message) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_flight_payment_failure);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        final TextView tvOkay = (TextView) dialog.findViewById(R.id.tvOkay);

        tvMessage.setText(message);

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
}