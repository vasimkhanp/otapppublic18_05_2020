package com.otapp.net.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.FlightCityPojo;

import java.util.ArrayList;
import java.util.List;

public class Otapp extends MultiDexApplication {

    public static String mUniqueID = "";

    public static FlightCityPojo mFlightCityPojo;

    public static List<CountryCodePojo.CountryCode> mCountryCodePojoList = new ArrayList<>();
    public static List<CountryCodePojo.Ad5> mAdsPojoList = new ArrayList<>();
    public static  List<CountryCodePojo.Services> mServiceList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        if (mCountryCodePojoList != null && mCountryCodePojoList.size() > 0) {
            mCountryCodePojoList.clear();
        }

        if (mAdsPojoList != null && mAdsPojoList.size() > 0) {
            mAdsPojoList.clear();
        }
        if(mServiceList!=null && mServiceList.size()>0){
            mServiceList.clear();
        }

//        getFlightCityList();
    }

//    private void getFlightCityList() {
//
//
//        if (!Utils.isInternetConnected(getActivity())) {
//            return;
//        }
//
//
//        ApiInterface mApiInterface = RestClient.getClient(true);
//        Call<FlightCityPojo> mCall = mApiInterface.getAirportList(Otapp.mUniqueID);
//        mCall.enqueue(new Callback<FlightCityPojo>() {
//            @Override
//            public void onResponse(Call<FlightCityPojo> call, Response<FlightCityPojo> response) {
//
//                if (response.isSuccessful()) {
//                    mFlightCityPojo = response.body();
//                    if (mFlightCityPojo != null) {
//                        if (mFlightCityPojo.status.equalsIgnoreCase("200")) {
//
//                        } else {
//
//                            Utils.showToast(getActivity(), "" + mFlightCityPojo.message);
//                            mFlightCityPojo = null;
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<FlightCityPojo> call, Throwable t) {
//            }
//        });
//    }

    private Context getActivity() {
        return getApplicationContext();
    }

}
