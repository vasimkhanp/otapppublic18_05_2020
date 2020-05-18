package com.otapp.net.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.ParkCartItemAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.model.ThemeParkCartListPojo;
import com.otapp.net.model.ThemeParkDetailsPojo;
import com.otapp.net.model.ThemeParkPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemeParkMyCartFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_ThemeParkMyCartFragment = "Tag_" + "ThemeParkMyCartFragment";

    View mView;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.tvContinue)
    TextView tvContinue;
    @BindView(R.id.tvMoreRide)
    TextView tvMoreRide;
    @BindView(R.id.tvCartEmpty)
    TextView tvCartEmpty;
    @BindView(R.id.lvCart)
    ListView lvCart;

    ThemeParkDetailsPojo.Details mThemeParkDetails;
    private ThemeParkPojo.Park mPark;
    private ThemeParkCartListPojo mThemeParkCartListPojo;
    private boolean isFromRide = false;

    List<ThemeParkCartListPojo.CartItem> mCartList = null;

    int mTotal = 0;

    ParkCartItemAdapter mParkCartItemAdapter;
    int mCartCount = 0;

    public static ThemeParkMyCartFragment newInstance() {
        ThemeParkMyCartFragment fragment = new ThemeParkMyCartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_park_my_cart, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        mParkCartItemAdapter = new ParkCartItemAdapter(getActivity(), this);
        lvCart.setAdapter(mParkCartItemAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mThemeParkDetails = new Gson().fromJson(bundle.getString(Constants.BNDL_PARK_DETAILS), ThemeParkDetailsPojo.Details.class);
            mPark = new Gson().fromJson(bundle.getString(Constants.BNDL_PARK), ThemeParkPojo.Park.class);
            isFromRide = bundle.getBoolean(Constants.BNDL_IS_PARK_FROM_RIDE, false);
        }

        getMyCartList();
        mCartCount = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_COUNT, mCartCount);
        Log.d("Log", "Count Cart TOMKF : " + mCartCount);
        tvBack.setOnClickListener(this);
        tvContinue.setOnClickListener(this);
        tvMoreRide.setOnClickListener(this);

    }

    private void getMyCartList() {

        LogUtils.e("", "getMyCartList");

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        String mParkCartID = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "");

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ThemeParkCartListPojo> mCall = mApiInterface.getThemeParkCartList(mParkCartID, Otapp.mUniqueID);
        mCall.enqueue(new Callback<ThemeParkCartListPojo>() {
            @Override
            public void onResponse(Call<ThemeParkCartListPojo> call, Response<ThemeParkCartListPojo> response) {

                Utils.closeProgressDialog();

                LogUtils.e("", "response.isSuccessful()::" + response.isSuccessful());

                if (response.isSuccessful()) {

                    mThemeParkCartListPojo = response.body();
                    if (mThemeParkCartListPojo != null) {
                        LogUtils.e("", "mThemeParkCartListPojo.status::" + mThemeParkCartListPojo.status);
                        if (mThemeParkCartListPojo.status.equals("200")) {
                            LogUtils.e("", "mThemeParkCartListPojo.mCartList::" + mThemeParkCartListPojo.mCartList);

                            mCartList = mThemeParkCartListPojo.mCartList;
                            mParkCartItemAdapter.addAll(mCartList);
                            if (mCartList != null && mCartList.size() > 0) {
                                tvCartEmpty.setVisibility(View.GONE);
                            } else {
                                tvCartEmpty.setVisibility(View.VISIBLE);
                                MyPref.setPref(getActivity(), MyPref.PREF_PARK_DATE, 0l);
                            }
                            setTotalPrice();

                        } else {
                            tvCartEmpty.setVisibility(View.VISIBLE);
                            MyPref.setPref(getActivity(), MyPref.PREF_PARK_DATE, 0l);
                            Utils.showToast(getActivity(), mThemeParkCartListPojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ThemeParkCartListPojo> call, Throwable t) {
                LogUtils.e("", "onFailure::" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });


    }

    public void onDeleteClick(final int position, ThemeParkCartListPojo.CartItem mCartItem) {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        String mParkCartID = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "");

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ApiResponse> mCall = mApiInterface.removeRideItem(mCartItem.tpId, mParkCartID, Otapp.mUniqueID);
        mCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null) {
                        if (mApiResponse.status.equals("200")) {

                            Utils.showToast(getActivity(), mApiResponse.message);
                            if (mCartList != null && mCartList.size() > 0) {
                                if (mCartList.get(position).isEntrance) {
                                    mThemeParkCartListPojo.entranceFeeStatus = false;
                                }
                                mCartList.remove(position);
                            }
                            mParkCartItemAdapter.addAll(mCartList);
                            if (mCartList != null && mCartList.size() > 0) {
                                tvCartEmpty.setVisibility(View.GONE);
                            } else {
                                tvCartEmpty.setVisibility(View.VISIBLE);
                                MyPref.setPref(getActivity(), MyPref.PREF_PARK_DATE, 0l);
                                MyPref.setPref(getActivity(), MyPref.PREF_PARK_CART_COUNT, 0);
                            }
                            setTotalPrice();
                            mCartCount = mCartCount - 1;
                            MyPref.setPref(getActivity(), MyPref.PREF_PARK_CART_COUNT, mCartCount);
                            Log.d("Log", "Count Cart = " + mCartCount);
                            if(mCartCount==0){
                                Calendar calendar = Calendar.getInstance();

                                MyPref.setPref(getContext(), "RideDate", DateFormate.sdfParkDisplayDate.format(calendar.getTime()));
                                MyPref.setPref(getActivity(), MyPref.PREF_PARK_DATE, calendar.getTimeInMillis());
                            }
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

    private void setTotalPrice() {
        mTotal = 0;
        String currancy = "";
        if (mCartList != null && mCartList.size() > 0) {
            for (int i = 0; i < mCartList.size(); i++) {
                ThemeParkCartListPojo.CartItem mCartItem = mCartList.get(i);
                if (mCartItem != null) {
                    currancy = mCartItem.currency;

                    if (mCartItem.adultTicketCount > 0) {
                        mTotal = mTotal + (mCartItem.adultTicketCount * mCartItem.adultPrice);
                    }
                    if (mCartItem.childTicketCount > 0) {
                        mTotal = mTotal + (mCartItem.childTicketCount * mCartItem.childPrice);
                    }

                    if (mCartItem.studTicketCount > 0) {
                        mTotal = mTotal + (mCartItem.studTicketCount * mCartItem.studPrice);
                    }
                }
            }
        }
        tvTotalPrice.setText(currancy + " " + Utils.setPrice(mTotal));
    }

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            popBackStack();
          /*  int cartCallFlag= MyPref.getPref(getActivity(),"cartCall",0);
            if(cartCallFlag==1) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_PARK_DETAILS, new Gson().toJson(mThemeParkDetails));
                bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                switchFragment(ThemeParkAddCartFragment.newInstance(), ThemeParkAddCartFragment.Tag_ThemeParkAddCartFragment, bundle);
            }else {
                popBackStack();
            }*/
        } else if (view == tvContinue) {
            if (mTotal > 0) {

                if (mThemeParkCartListPojo != null && mThemeParkCartListPojo.entranceFeeStatus) {

                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.BNDL_PARK_DETAILS, new Gson().toJson(mThemeParkDetails));
                    bundle.putString(Constants.BNDL_PARK_CART_LIST, new Gson().toJson(mCartList));
                    bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                   switchFragment(ThemeParkPaymentFragment.newInstance(), ThemeParkPaymentFragment.Tag_ThemeParkPaymentFragment, bundle);


                } else {
                    showEntranceDialog();
                }

            } else {
                Utils.showToast(getActivity(), getString(R.string.alert_add_ticket));
            }
        } else if (view == tvMoreRide) {
            LogUtils.e("", "tvMoreRide clicked");
            if (isFromRide) {
                popBackStack();
            } else {
                popBackStack(ThemeParkDetailsFragment.Tag_ThemeParkDetailsFragment);
            }
        }
    }

    private void showEntranceDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_park_entrance);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView tvDate = (TextView) dialog.findViewById(R.id.tvDate);
        TextView tvSkip = (TextView) dialog.findViewById(R.id.tvSkip);
        TextView tvContinue = (TextView) dialog.findViewById(R.id.tvContinue);

        Calendar mCalDate = Calendar.getInstance();
        LogUtils.e("", " my cart PREF_PARK_DATE::" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_DATE, 0l));
        if (MyPref.getPref(getActivity(), MyPref.PREF_PARK_DATE, 0l) > 0l) {
            mCalDate.setTimeInMillis(MyPref.getPref(getActivity(), MyPref.PREF_PARK_DATE, 0l));
        }
        LogUtils.e("", "mCalDate::" + mCalDate.getTime() + " " + DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()));

        tvDate.setText("" + DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()));

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_PARK_DETAILS, new Gson().toJson(mThemeParkDetails));
                bundle.putString(Constants.BNDL_PARK_CART_LIST, new Gson().toJson(mCartList));
                bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                switchFragment(ThemeParkPaymentFragment.newInstance(), ThemeParkPaymentFragment.Tag_ThemeParkPaymentFragment, bundle);
                //switchFragment(ThemeParkOrderReviewFragment.newInstance(), ThemeParkOrderReviewFragment.Tag_ThemeParkOrderReviewFragment, bundle);
            }
        });

        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

                popBackStack(ThemeParkDetailsFragment.Tag_ThemeParkDetailsFragment);

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
