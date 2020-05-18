package com.otapp.net.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.ParkComboAdapter;
import com.otapp.net.adapter.ParkEventsAdapter;
import com.otapp.net.adapter.ParkRidesAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ThemeParkPojo;
import com.otapp.net.model.ThemeParkRideListPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemeParkRidesFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_ThemeParkRidesFragment = "Tag_" + "ThemeParkRidesFragment";

    View mView;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvMyCart)
    TextView tvMyCart;
    @BindView(R.id.tvRides)
    TextView tvRides;
    @BindView(R.id.tvServiceEvents)
    TextView tvServiceEvents;
    @BindView(R.id.tvCombos)
    TextView tvCombos;
    @BindView(R.id.rvRides)
    RecyclerView rvRides;
    @BindView(R.id.rvServiceEvents)
    RecyclerView rvServiceEvents;
    @BindView(R.id.rvCombos)
    RecyclerView rvCombos;
    @BindView(R.id.ivThemePark)
    ImageView ivThemePark;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvEnterasFee)
    TextView tvEnterasFee;
    @BindView(R.id.tvEnterasPerson)
    TextView tvEnterasPerson;
    @BindView(R.id.tvEnterasFeeTitle)
    TextView tvEnterasFeeTitle;
    @BindView(R.id.tvRideRight)
    TextView tvRideRight;
    @BindView(R.id.tvEventRight)
    TextView tvEventRight;
    @BindView(R.id.tvComboRight)
    TextView tvComboRight;
    @BindView(R.id.card_view)
    CardView card_view;
    @BindView(R.id.lnrContainer)
    LinearLayout lnrContainer;
    @BindView(R.id.aviProgress)
    AVLoadingIndicatorView aviProgress;
    @BindView(R.id.aviEnterasProgress)
    AVLoadingIndicatorView aviEnterasProgress;

    ParkRidesAdapter mParkRidesAdapter;
    ParkEventsAdapter mParkEventsAdapter;
    ParkComboAdapter mParkComboAdapter;

    private ThemeParkPojo.Park mPark;

    private ThemeParkRideListPojo mThemeParkRideListPojo;

    public static ThemeParkRidesFragment newInstance() {
        ThemeParkRidesFragment fragment = new ThemeParkRidesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_themepark_rides, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {


        Bundle bundle = getArguments();
        if (bundle != null) {
            mPark = new Gson().fromJson(bundle.getString(Constants.BNDL_PARK), ThemeParkPojo.Park.class);
            if (mPark != null) {
                tvTitle.setText("" + mPark.name);
            }
        }

        if (mThemeParkRideListPojo == null) {
            getRideList();
        } else {
            setRideList();
        }

        tvBack.setOnClickListener(this);
        tvMyCart.setOnClickListener(this);
        tvRideRight.setOnClickListener(this);

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        LogUtils.e(Tag_ThemeParkRidesFragment, "isVisibleToUser::" + isVisibleToUser);
//        if (isVisibleToUser) {
//            if (mThemeParkRideListPojo == null) {
//                getRideList();
//            } else {
//                setRideList();
//            }
//        } else {
//        }
//    }

    private void getRideList() {
        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ThemeParkRideListPojo> mCall = mApiInterface.getThemeParkRideList(Otapp.mUniqueID);
        mCall.enqueue(new Callback<ThemeParkRideListPojo>() {
            @Override
            public void onResponse(Call<ThemeParkRideListPojo> call, Response<ThemeParkRideListPojo> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    mThemeParkRideListPojo = response.body();
                    if (mThemeParkRideListPojo != null) {
                        if (mThemeParkRideListPojo.status.equalsIgnoreCase("200")) {

                            setRideList();

                        } else {

                            Utils.showToast(getActivity(), "" + mThemeParkRideListPojo.message);
                            mThemeParkRideListPojo = null;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ThemeParkRideListPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });
    }

    private void setRideList() {

        if (mThemeParkRideListPojo.data != null) {

            if (!TextUtils.isEmpty(mThemeParkRideListPojo.data.bannerImg)) {
                aviProgress.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(mThemeParkRideListPojo.data.bannerImg).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        aviProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        aviProgress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(ivThemePark);
            }

            final ThemeParkRideListPojo.EnterasFee mEnterasFee = mThemeParkRideListPojo.data.entranceFee;
            if (mEnterasFee != null) {

                tvEnterasFee.setText("" + mEnterasFee.tpName);
                tvEnterasPerson.setText("" + mEnterasFee.tpSubName);

                if (!TextUtils.isEmpty(mEnterasFee.image)) {
                    aviEnterasProgress.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(mEnterasFee.image).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            aviEnterasProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            aviEnterasProgress.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(ivPhoto);
                }
                card_view.setVisibility(View.VISIBLE);
                tvEnterasFeeTitle.setVisibility(View.VISIBLE);

                lnrContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.BNDL_PARK_ID, mEnterasFee.tpId);
                        bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                        switchFragment(ThemeParkDetailsFragment.newInstance(), ThemeParkDetailsFragment.Tag_ThemeParkDetailsFragment, bundle);

                    }
                });

            } else {
                card_view.setVisibility(View.GONE);
                tvEnterasFeeTitle.setVisibility(View.GONE);
            }


            final List<ThemeParkRideListPojo.Ride> mRideList = mThemeParkRideListPojo.data.rides;
            if (mRideList != null && mRideList.size() > 0) {
                mParkRidesAdapter = new ParkRidesAdapter(getActivity(), mRideList, new ParkRidesAdapter.OnRideClickListener() {
                    @Override
                    public void onRideClicked(int position) {

                        ThemeParkRideListPojo.Ride mRide = mRideList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.BNDL_PARK_ID, mRide.tpId);
                        bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                        switchFragment(ThemeParkDetailsFragment.newInstance(), ThemeParkDetailsFragment.Tag_ThemeParkDetailsFragment, bundle);

                    }
                });

                if (mRideList.size() > 2) {
                    tvRideRight.setVisibility(View.GONE);
                }

                rvRides.setAdapter(mParkRidesAdapter);
                rvRides.setVisibility(View.VISIBLE);
                tvRides.setVisibility(View.VISIBLE);
            } else {
                rvRides.setVisibility(View.GONE);
                tvRides.setVisibility(View.GONE);
                tvRideRight.setVisibility(View.GONE);
            }

            final List<ThemeParkRideListPojo.ServicesAndEvent> mParkEventList = mThemeParkRideListPojo.data.servicesAndEvents;
            if (mParkEventList != null && mParkEventList.size() > 0) {
                mParkEventsAdapter = new ParkEventsAdapter(getActivity(), mParkEventList, new ParkEventsAdapter.OnParkEventClickListener() {
                    @Override
                    public void onParkEventClicked(int position) {
                        ThemeParkRideListPojo.ServicesAndEvent mParkEvent = mParkEventList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.BNDL_PARK_ID, mParkEvent.tpId);
                        bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                        switchFragment(ThemeParkDetailsFragment.newInstance(), ThemeParkDetailsFragment.Tag_ThemeParkDetailsFragment, bundle);
                    }
                });

                if (mParkEventList.size() > 2) {
                    tvEventRight.setVisibility(View.GONE);
                }

                rvServiceEvents.setAdapter(mParkEventsAdapter);
                rvServiceEvents.setVisibility(View.VISIBLE);
                tvServiceEvents.setVisibility(View.VISIBLE);
            } else {
                rvServiceEvents.setVisibility(View.GONE);
                tvServiceEvents.setVisibility(View.GONE);
                tvEventRight.setVisibility(View.GONE);
            }

            final List<ThemeParkRideListPojo.Combo> mParkComboList = mThemeParkRideListPojo.data.combo;
            if (mParkComboList != null && mParkComboList.size() > 0) {
                mParkComboAdapter = new ParkComboAdapter(getActivity(), mParkComboList, new ParkComboAdapter.OnParkComboClickListener() {
                    @Override
                    public void onComboClicked(int position) {
                        ThemeParkRideListPojo.Combo mParkCombo = mParkComboList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.BNDL_PARK_ID, mParkCombo.comboId);
                        bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                        switchFragment(ThemeParkDetailsFragment.newInstance(), ThemeParkDetailsFragment.Tag_ThemeParkDetailsFragment, bundle);
                    }
                });

                if (mParkComboList.size() > 2) {
                    tvComboRight.setVisibility(View.GONE);
                }

                rvCombos.setAdapter(mParkComboAdapter);
                rvCombos.setVisibility(View.VISIBLE);
                tvCombos.setVisibility(View.VISIBLE);
            } else {
                rvCombos.setVisibility(View.GONE);
                tvCombos.setVisibility(View.GONE);
                tvComboRight.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            popBackStack();
        } else if (view == tvRideRight) {

        } else if (view == tvMyCart) {
            int mThemeparkCount = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_COUNT, 0);

            if (mThemeparkCount > 0) {

                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_PARK_DETAILS, MyPref.getPref(getActivity(), Constants.BNDL_PARK_DETAILS, ""));
                bundle.putString(Constants.BNDL_PARK, MyPref.getPref(getActivity(), Constants.BNDL_PARK, ""));
                bundle.putBoolean(Constants.BNDL_IS_PARK_FROM_RIDE, true);
                switchFragment(ThemeParkMyCartFragment.newInstance(), ThemeParkMyCartFragment.Tag_ThemeParkMyCartFragment, bundle);

            } else {
                Utils.showToast(getActivity(), getString(R.string.alert_add_ticket));
            }
        }
    }
}
