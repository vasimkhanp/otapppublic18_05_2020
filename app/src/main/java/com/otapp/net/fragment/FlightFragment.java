package com.otapp.net.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.ServiceCategoryPagerAdapterWithTitle;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightFragment extends BaseFragment {

    public static String Tag_FlightFragment = "Tag_" + "FlightFragment";


    @BindView(R.id.tlSlidingTabs)
    TabLayout tlSlidingTabs;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private int mFlightPosition;
    FlightCityPojo.Data mFlightCityPojoData;

    ServiceCategoryPagerAdapterWithTitle mServiceCategoryPagerAdapterWithTitle;

    private int mCityHeight = 0, mCityCount = 0;

    private IncomingListener mIncomingListener;

    View mView;

    public static FlightFragment newInstance() {
        FlightFragment fragment = new FlightFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_service_flight, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        mIncomingListener = new IncomingListener();
        getActivity().registerReceiver(mIncomingListener, new IntentFilter(Constants.FILTER_FLIGHT_ADD_CITY));

        mCityHeight = (int) getResources().getDimension(R.dimen._500sdp);

        String[] mFlightArray = getResources().getStringArray(R.array.flight_array);

        if (!isAdded()) return;
        mServiceCategoryPagerAdapterWithTitle = new ServiceCategoryPagerAdapterWithTitle(getChildFragmentManager());

        for (int i = 0; i < mFlightArray.length; i++) {
            if (i == 0 || i == 1) {
                Fragment mFragment = FlightOneWayFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.CITY_TYPE_POSITION, i);
                mFragment.setArguments(bundle);
                mServiceCategoryPagerAdapterWithTitle.addFragment(mFragment, mFlightArray[i]);
            } else {
                mServiceCategoryPagerAdapterWithTitle.addFragment(FlightMultiCityFragment.newInstance(), mFlightArray[i]);
            }

        }


        mViewPager.setAdapter(mServiceCategoryPagerAdapterWithTitle);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(mFlightPosition);
        tlSlidingTabs.setupWithViewPager(mViewPager, true);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
//        if (i == 0 || i == 1) {
            params.height = (int) getResources().getDimension(R.dimen._450sdp);
//        } else {
//            params.height = mCityHeight;
//        }
        mViewPager.setLayoutParams(params);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mFlightPosition = i;
                LogUtils.e("", "onPageSelected::" + mFlightPosition);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
                if (i == 0 || i == 1) {
                    params.height = (int) getResources().getDimension(R.dimen._450sdp);
                } else {
                    params.height = mCityHeight;
                }
                mViewPager.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (getUserVisibleHint()) {
//            if (mFlightCityPojoData == null) {
//
//                LogUtils.e("", "isAirportDetailRunning::" + isAirportDetailRunning);
//                if (!isAirportDetailRunning) {
//                    getFlightCityList();
//                } else {
//                    if (!Utils.isProgressDialogShowing()) {
//                        Utils.showProgressDialog(getActivity());
//                    }
//                }
//            } else {
//                setFlightCityList();
//            }
//        }
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.e(Tag_FlightFragment, "isVisibleToUser::" + getUserVisibleHint());

//        if (getUserVisibleHint()){
            if (mFlightCityPojoData == null) {
                getFlightCityList();
            } else {
                setFlightCityList();
            }
//        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        LogUtils.e(Tag_FlightFragment, "isVisibleToUser::" + isVisibleToUser);
//        if (isVisibleToUser) {
//            if (mFlightCityPojoData == null) {
//                getFlightCityList();
//            } else {
//                setFlightCityList();
//            }
//        }
//    }

    private void getFlightCityList() {


        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            popBackStack();
            return;
        }

        Utils.showFlightProgressDialog(getActivity());


        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<FlightCityPojo> mCall = mApiInterface.getAirportList(Otapp.mUniqueID);
        mCall.enqueue(new Callback<FlightCityPojo>() {
            @Override
            public void onResponse(Call<FlightCityPojo> call, Response<FlightCityPojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    FlightCityPojo mFlightCityPojo = response.body();
                    if (mFlightCityPojo != null) {
                        if (mFlightCityPojo.status.equalsIgnoreCase("200")) {

                            mFlightCityPojoData = mFlightCityPojo.data;
                            JSONObject jsonObjectResponse = null;
                            try {
                                jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                                Log.d("Log", "Response : " + jsonObjectResponse);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setFlightCityList();

                        } else {

                            ServiceFragment mServiceFragment = (ServiceFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ServiceFragment.Tag_ServiceFragment);
                            if (mServiceFragment != null) {
                                mServiceFragment.setFlightError(mFlightCityPojo.message);
                            }

//                            Utils.showToast(getActivity(), "" + mFlightCityPojo.message);
//                            try {
//                                popBackStack();
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
                            mFlightCityPojoData = null;
                        }
                    }
                }



            }


            @Override
            public void onFailure(Call<FlightCityPojo> call, Throwable t) {
                Utils.closeProgressDialog();
//                try {
//                    popBackStack();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }

            }
        });
    }

    private void setFlightCityList() {

        if (getActivity() != null && isAdded()) {

            if (mFlightCityPojoData != null) {
                if (mFlightCityPojoData.cities != null && mFlightCityPojoData.cities.size() > 0) {
                    LogUtils.e("", "mFlightCityList size:" + mFlightCityPojoData.cities.size());
                }

                FlightOneWayFragment mFlightOneWayFragment = (FlightOneWayFragment) mServiceCategoryPagerAdapterWithTitle.getItem(0);
                if (mFlightOneWayFragment != null) {
                    mFlightOneWayFragment.setAirportDetails(mFlightCityPojoData);
                }

                FlightOneWayFragment mFlightOneWayFragment1 = (FlightOneWayFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
                if (mFlightOneWayFragment1 != null) {
                    mFlightOneWayFragment1.setAirportDetails(mFlightCityPojoData);
                }

//                FlightMultiCityFragment mFlightMultiCityFragment = (FlightMultiCityFragment) mServiceCategoryPagerAdapterWithTitle.getItem(2);
//                if (mFlightMultiCityFragment != null) {
//                    mFlightMultiCityFragment.setAirportDetails(mFlightCityPojoData.data);
//                }

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mIncomingListener != null) {
            getActivity().unregisterReceiver(mIncomingListener);
        }
    }

    public void setFromCity(FlightCityPojo.City mFromCity) {
        if (mServiceCategoryPagerAdapterWithTitle != null){
            FlightOneWayFragment mFlightOneWayFragment = (FlightOneWayFragment) mServiceCategoryPagerAdapterWithTitle.getItem(0);
            if (mFlightOneWayFragment != null) {
                mFlightOneWayFragment.setFromCity(mFromCity);
            }

            FlightOneWayFragment mFlightOneWayFragment1 = (FlightOneWayFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
            if (mFlightOneWayFragment1 != null) {
                mFlightOneWayFragment1.setFromCity(mFromCity);
            }
        }
    }

    public void setToCity(FlightCityPojo.City mToCity) {
        if (mServiceCategoryPagerAdapterWithTitle != null){
            FlightOneWayFragment mFlightOneWayFragment = (FlightOneWayFragment) mServiceCategoryPagerAdapterWithTitle.getItem(0);
            if (mFlightOneWayFragment != null) {
                mFlightOneWayFragment.setToCity(mToCity);
            }

            FlightOneWayFragment mFlightOneWayFragment1 = (FlightOneWayFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
            if (mFlightOneWayFragment1 != null) {
                mFlightOneWayFragment1.setToCity(mToCity);
            }
        }
    }

    public void setCurrency(FlightCityPojo.Currency mCurrency) {
        if (mServiceCategoryPagerAdapterWithTitle != null){
            FlightOneWayFragment mFlightOneWayFragment = (FlightOneWayFragment) mServiceCategoryPagerAdapterWithTitle.getItem(0);
            if (mFlightOneWayFragment != null) {
                mFlightOneWayFragment.setCurrency(mCurrency);
            }

            FlightOneWayFragment mFlightOneWayFragment1 = (FlightOneWayFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
            if (mFlightOneWayFragment1 != null) {
                mFlightOneWayFragment1.setCurrency(mCurrency);
            }
        }
    }



    private class IncomingListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null && intent.getAction() == Constants.FILTER_FLIGHT_ADD_CITY) {

//                if (mViewPager.getCurrentItem() == 2) {
////
////
//                    mCityCount = intent.getExtras().getInt(Constants.BNDL_FLIGHT_CITY_COUNT, 1) - 1;
////                    mCityHeight = (int) getResources().getDimension(R.dimen._450sdp) + (mCityCount * (int) getResources().getDimension(R.dimen._215sdp));
//                    mCityHeight = (int) getResources().getDimension(R.dimen._450sdp) + (mCityCount * (int) getResources().getDimension(R.dimen._240sdp));
//
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
//                    params.height = mCityHeight;
//                    mViewPager.setLayoutParams(params);
//                }

            }

        }
    }

}
