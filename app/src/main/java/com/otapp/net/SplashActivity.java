package com.otapp.net;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.perf.FirebasePerformance;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.UpdatePojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    private String sUpdateLink = "https://play.google.com/store/apps/details?id=com.otapp.net";
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Utils.printHashKey(getActivity());

        FirebasePerformance.getInstance().setPerformanceCollectionEnabled(true);
        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_UNIQUE_ID, ""))) {
            String mID = getString(R.string.app_name) + System.currentTimeMillis();
            Otapp.mUniqueID = mID;
            MyPref.setPref(getActivity(), MyPref.PREF_UNIQUE_ID, "" + mID);
        } else {
            Otapp.mUniqueID = MyPref.getPref(getActivity(), MyPref.PREF_UNIQUE_ID, "");
        }

        LogUtils.e("", "Otapp.mUniqueID::" + Otapp.mUniqueID);
//        LogUtils.e("", "PREF_SELECTED_COUNTRY_CODE::" + MyPref.getPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, MyPref.PREF_DEFAULT_COUNTRY_CODE));

        getCountryCodeList();

        if (BuildConfig.DEBUG) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goFurtherNavigation();
                }
            }, 1000);

        } else {
            checkAppVersion();  //  Check app version
        }


    }

    private void goFurtherNavigation() {

        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false) || MyPref.getPref(getActivity(), MyPref.PREF_IS_SKIPED, false)) {
            IntentHandler.startActivity(getActivity(), HomeActivity.class);
        } else {
            IntentHandler.startActivity(getActivity(), LoginActivity.class);
        }


        finish();
    }

    private Context getActivity() {
        return SplashActivity.this;
    }

    private void checkAppVersion() {
        if (!Utils.isInternetConnected(getActivity())) {
            snackbar = Snackbar
                    .make(linearLayout, getString(R.string.msg_no_internet), Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                            checkAppVersion();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return;
        }

        Map<String, String> jsonParams = new ArrayMap<>();
        jsonParams.put("version", BuildConfig.VERSION_NAME);
        String mRegisterUrl = RestClient.API_PREFIX + "otapp_update.php";
        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<UpdatePojo> mCall = mApiInterface.checkVersion(mRegisterUrl, jsonParams);
        mCall.enqueue(new Callback<UpdatePojo>() {
            @Override
            public void onResponse(Call<UpdatePojo> call, Response<UpdatePojo> response) {
                Utils.closeProgressDialog();
                LogUtils.e("", "response::" + response);
//                try {
//                    JSONObject jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
//                    Log.d("Log", "Response Bus List : " + jsonObjectResponse);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().status.equalsIgnoreCase("200")) {
                            UpdateAvailable(response.body());
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    goFurtherNavigation();
                                }
                            }, 1000);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdatePojo> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });
    }

    private void UpdateAvailable(UpdatePojo updatePojo) {

        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_update);

        if (dialog != null) {


            TextView tvUpdate = (TextView) dialog.findViewById(R.id.tvUpdate);
            TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
            TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
            View mVerticalLine = (View) dialog.findViewById(R.id.mVerticalLine);
            tvMessage.setText(updatePojo.message);

            if (updatePojo.is_force_update.equals("1")) {
                tvCancel.setVisibility(View.GONE);
                mVerticalLine.setVisibility(View.GONE);
                dialog.setCancelable(false);
            }
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    goFurtherNavigation();
                }
            });

            tvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(sUpdateLink));
                    startActivity(intent);
                    finish();
                }
            });

            if (!isFinishing()) {
                dialog.show();
            }
        }
    }

    private void getCountryCodeList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("user_token", "" + Otapp.mUniqueID);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<CountryCodePojo> mCall = mApiInterface.getCountryCodeList(jsonParams);
        mCall.enqueue(new Callback<CountryCodePojo>() {
            @Override
            public void onResponse(Call<CountryCodePojo> call, Response<CountryCodePojo> response) {

                if (response.isSuccessful()) {
                    CountryCodePojo mCountryCodePojo = response.body();
                    if (mCountryCodePojo != null) {
                        if (mCountryCodePojo.status.equalsIgnoreCase("200")) {

                            Otapp.mCountryCodePojoList = mCountryCodePojo.data;
                            Otapp.mAdsPojoList = mCountryCodePojo.ad5;
                            Otapp.mServiceList=mCountryCodePojo.servicesList;
                        } else {
                            Utils.showToast(getActivity(), "" + mCountryCodePojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryCodePojo> call, Throwable t) {
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

    }
}
