package com.otapp.net.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.otapp.net.Events.Activity.EventsActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.HomeAdsAdapter;
import com.otapp.net.adapter.HomePromotionAdapter;
import com.otapp.net.adapter.HomeServicesAdapter;
import com.otapp.net.adapter.SupportAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.HomeAdsResponse;
import com.otapp.net.model.HomeServicePojo;
import com.otapp.net.model.SupportBean;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {

    public static String Tag_HomeFragment = "Tag_" + "HomeFragment";

    View mView;

    int[] mServiceDrawableArray = new int[]{R.drawable.ic_movie, R.drawable.ic_airplane, R.drawable.ic_events, R.drawable.ic_theme_park, R.drawable.ic_bus,/*, R.drawable.ic_hotel, R.drawable.ic_ferry, R.drawable.ic_tours*/};

    @BindView(R.id.gvServices)
    GridView gvServices;
    //    @BindView(R.id.tvAppVersion)
//    TextView tvAppVersion;
//    @BindView(R.id.rvHolidays)
//    RecyclerView rvHolidays;
//    @BindView(R.id.rvCity)
//    RecyclerView rvCity;
    @BindView(R.id.rvOffer)
    RecyclerView rvOffer;
    @BindView(R.id.rvSupport)
    RecyclerView rvSupport;
    @BindView(R.id.lvAds)
    ListView lvAds;

    HomeServicesAdapter mHomeServicesAdapter;
    //    HolidaysAdapter mHolidaysAdapter;
//    CitiesAdapter mCitiesAdapter;

    SupportAdapter mSupportAdapter;

    HomePromotionAdapter mHomePromotionAdapter;
    HomeAdsAdapter mHomeAdsAdapter;
    HomeAdsResponse.Data mHomeAdsResponseData;

    ArrayList<HomeServicePojo> mHomeServicePojoList = new ArrayList<>();
    ArrayList<SupportBean> mSupportBeanList = new ArrayList<>();

    private static HomeFragment fragment;

    public static HomeFragment newInstance() {
        if (fragment == null) {
            fragment = new HomeFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mView);
        MyPref.setPref(getContext(), "PromoCode", "");
        InitializeControls();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        PreferenceManager.getDefaultSharedPreferences(getContext()).
//                edit().clear().apply();
        MyPref.setPref(getContext(), "ENABLED_PAYMENT_METHODS", "0");
        MyPref.setPref(getContext(), "PromoCode", "");
    }

    private void InitializeControls() {

//        tvAppVersion.setText(getString(R.string.app_version) + " v" + BuildConfig.VERSION_NAME);


        mHomeServicesAdapter = new HomeServicesAdapter(getActivity());
        gvServices.setAdapter(mHomeServicesAdapter);

        if (mHomePromotionAdapter == null) {
            mHomePromotionAdapter = new HomePromotionAdapter(getActivity(), new HomePromotionAdapter.OnOfferClickListener() {
                @Override
                public void onOfferClicked(String module) {
                    setModule(module);
                }
            });
            rvOffer.setAdapter(mHomePromotionAdapter);
        } else {
            rvOffer.setAdapter(mHomePromotionAdapter);
        }

        if (mHomeAdsAdapter == null) {
            mHomeAdsAdapter = new HomeAdsAdapter(getActivity(), new HomeAdsAdapter.OnAdsClickListener() {
                @Override
                public void onAdsClicked(String module) {
                    setModule(module);
                }
            });
            lvAds.setAdapter(mHomeAdsAdapter);
        } else {
            lvAds.setAdapter(mHomeAdsAdapter);
        }

//        mHolidaysAdapter = new HolidaysAdapter(getActivity());
//        rvHolidays.setAdapter(mHolidaysAdapter);
//        rvHolidays.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//
//        mCitiesAdapter = new CitiesAdapter(getActivity());
//        rvCity.setAdapter(mCitiesAdapter);
//        rvCity.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));ager.HORIZONTAL, false));

        SupportBean mSupportBean1 = new SupportBean();
        mSupportBean1.drawable = R.drawable.ic_support_24;
        mSupportBean1.title = "24/7 customer Support";
        mSupportBean1.support = "We’re here to help whenever you need us.";
        mSupportBeanList.add(mSupportBean1);

        SupportBean mSupportBean2 = new SupportBean();
        mSupportBean2.drawable = R.drawable.ic_security;
        mSupportBean2.title = "Secure Booking Process";
        mSupportBean2.support = "Your personal information is secured the latest industry standards.";
        mSupportBeanList.add(mSupportBean2);

        mSupportAdapter = new SupportAdapter(getActivity(), mSupportBeanList);
        rvSupport.setAdapter(mSupportAdapter);


        gvServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // || i == 1
                if (i == 0 || i == 1 || i == 2 || i == 3||i==4) {  // || i == 4

                    if (!Utils.isInternetConnected(getActivity())) {
                        Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                        return;
                    }

                    Fragment mFragment = ServiceFragment.newInstance();
                    ((ServiceFragment) mFragment).setPosition(i);
                    switchFragment(mFragment, ServiceFragment.Tag_ServiceFragment);

                } else {
                   /* if(i==2){
                        Intent eventIntent = new Intent(getContext(), EventsActivity.class);
                        startActivity(eventIntent);
                    }else {*/
                        launchSoonDialog();
                  /*  }*/
                }


//                String mUrl = "http://www.managemyticket.net/android/api/test_pay_response.php";
//
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.BNDL_TITLE, "");
//                bundle.putString(Constants.BNDL_URL, mUrl);
////
//                switchFragment(MoviePaymentProcessFragment.newInstance(), MoviePaymentProcessFragment.Tag_MoviePaymentProcessFragment, bundle);

            }
        });

        if (mHomeServicePojoList == null || mHomeServicePojoList.size() == 0) {

            List<String> mServiceList = Arrays.asList(getResources().getStringArray(R.array.service_array));

            HomeServicePojo mHomeServicePojo = null;

            for (int j = 0; j < mServiceList.size(); j++) {
                mHomeServicePojo = new HomeServicePojo(mServiceList.get(j), mServiceDrawableArray[j]);
                mHomeServicePojoList.add(mHomeServicePojo);
            }

        }

        mHomeServicesAdapter.addAll(mHomeServicePojoList);

        if (Otapp.mAdsPojoList != null && Otapp.mAdsPojoList.size() > 0) {
            setHomeAdsList();
        } else {
            getCountryCodeList();
        }


    }

//    private void getHomeAdsList() {
//
//        if (!Utils.isInternetConnected(getActivity())) {
//            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
//            return;
//        }
//
//        Utils.showProgressDialog(getActivity());
//
//        ApiInterface mApiInterface = RestClient.getClient(true);
//        Call<HomeAdsResponse> mCall = mApiInterface.getHomeAds(Otapp.mUniqueID);
//        mCall.enqueue(new Callback<HomeAdsResponse>() {
//            @Override
//            public void onResponse(Call<HomeAdsResponse> call, Response<HomeAdsResponse> response) {
//
//                Utils.closeProgressDialog();
//
//                if (response.isSuccessful()) {
//                    HomeAdsResponse mHomeAdsResponse = response.body();
//                    if (mHomeAdsResponse != null) {
//                        if (mHomeAdsResponse.status.equalsIgnoreCase("200")) {
//
//                            mHomeAdsResponseData = mHomeAdsResponse.data;
//                            setHomeAdsList();
//
//                        } else {
//                            Utils.showToast(getActivity(), "" + mHomeAdsResponse.message);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<HomeAdsResponse> call, Throwable t) {
//                Utils.closeProgressDialog();
//                LogUtils.e("", "onFailure:" + t.getMessage());
//            }
//        });
//
//    }

    public void setHomeAdsList() {

        List<CountryCodePojo.Ad5> mCenterImages = new ArrayList<>();
        List<CountryCodePojo.Ad5> mBottomImages = new ArrayList<>();
        if (Otapp.mAdsPojoList != null && Otapp.mAdsPojoList.size() > 0) {
            for (int i = 0; i < Otapp.mAdsPojoList.size(); i++) {
                if (Otapp.mAdsPojoList.get(i).page.equalsIgnoreCase("Landing Page") && Otapp.mAdsPojoList.get(i).location.startsWith("Center Slide")) {
                    mCenterImages.add(Otapp.mAdsPojoList.get(i));
                }

                if (Otapp.mAdsPojoList.get(i).page.equalsIgnoreCase("Landing Page") && Otapp.mAdsPojoList.get(i).location.startsWith("Bottom Banner")) {
                    mBottomImages.add(Otapp.mAdsPojoList.get(i));
                }
            }
        }


        mHomePromotionAdapter.addAll(mCenterImages);

        mHomeAdsAdapter.addAll(mBottomImages);


    }

    private void getCountryCodeList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("user_token", "" + Otapp.mUniqueID);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<CountryCodePojo> mCall = mApiInterface.getCountryCodeList(jsonParams);
        mCall.enqueue(new Callback<CountryCodePojo>() {
            @Override
            public void onResponse(Call<CountryCodePojo> call, Response<CountryCodePojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    CountryCodePojo mCountryCodePojo = response.body();
                    if (mCountryCodePojo != null) {
                        if (mCountryCodePojo.status.equalsIgnoreCase("200")) {

                            Otapp.mCountryCodePojoList = mCountryCodePojo.data;
                            Otapp.mAdsPojoList = mCountryCodePojo.ad5;
                            Otapp.mServiceList = mCountryCodePojo.servicesList;

                            setHomeAdsList();

                        } else {
                            Utils.showToast(getActivity(), "" + mCountryCodePojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryCodePojo> call, Throwable t) {
                LogUtils.e("", "onFailure:" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });

    }

    private void setModule(String module) {

        if (!TextUtils.isEmpty(module)) {
            int i = 0;
            if (module.equalsIgnoreCase("movie")) {
                i = 0;
            } else if (module.equalsIgnoreCase("flight")) {
                i = 1;
            } else if (module.equalsIgnoreCase("event")) {
                i = 2;
            }else if (module.equalsIgnoreCase("themepark")) {
                i = 3;
            } else if (module.equalsIgnoreCase("bus")) {
                i = 4;
            } /* else if (module.equalsIgnoreCase("hotels")) {
                i = 5;
            } else if (module.equalsIgnoreCase("ferry")) {
                i = 6;
            } else if (module.equalsIgnoreCase("tours")) {
                i = 7;
            }*/

            if (!Utils.isInternetConnected(getActivity())) {
                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                return;
            }

            Fragment mFragment = ServiceFragment.newInstance();
            ((ServiceFragment) mFragment).setPosition(i);
            switchFragment(mFragment, ServiceFragment.Tag_ServiceFragment);

        }

    }

    private void launchSoonDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle("" + getString(R.string.app_name));
        alertDialogBuilder
                .setMessage("" + getString(R.string.alert_launch_soon))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void loadThemepark() {
        Fragment mFragment = ServiceFragment.newInstance();
        ((ServiceFragment) mFragment).setPosition(3);
        switchFragment(mFragment, ServiceFragment.Tag_ServiceFragment);
    }

    public void loadMovie() {
        Fragment mFragment = ServiceFragment.newInstance();
        ((ServiceFragment) mFragment).setPosition(0);
        switchFragment(mFragment, ServiceFragment.Tag_ServiceFragment);
    }
}
