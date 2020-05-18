package com.otapp.net.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.otapp.net.R;
import com.otapp.net.adapter.ServiceCategoryPagerAdapterWithTitle;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ThemeParkPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemeparkFragment extends BaseFragment {

    public static String Tag_ThemeparkFragment = "Tag_" + "ThemeparkFragment";

    View mView;
    @BindView(R.id.tvAllCity)
    TextView tvAllCity;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.ivThemePark)
    ImageView ivThemePark;
    @BindView(R.id.tlSlidingTabs)
    TabLayout tlSlidingTabs;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.aviProgress)
    AVLoadingIndicatorView aviProgress;

    private ThemeParkPojo mThemeParkPojo;
    ServiceCategoryPagerAdapterWithTitle mServiceCategoryPagerAdapterWithTitle;

    private int mParkPosition;

    public static ThemeparkFragment newInstance() {
        ThemeparkFragment fragment = new ThemeparkFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_service_themepark, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        String[] mParkArray = getResources().getStringArray(R.array.themepark_array);

        if (!isAdded()) return;
        mServiceCategoryPagerAdapterWithTitle = new ServiceCategoryPagerAdapterWithTitle(getChildFragmentManager());

        for (int i = 0; i < mParkArray.length; i++) {
            mServiceCategoryPagerAdapterWithTitle.addFragment(ThemeParkTabFragment.newInstance(), mParkArray[i]);
        }

//        mServiceCategoryPagerAdapterWithTitle.addFragment(ThemeParkTab1Fragment.newInstance(), mParkArray[0]);
//        mServiceCategoryPagerAdapterWithTitle.addFragment(ThemeParkTab2Fragment.newInstance(), mParkArray[1]);
//        mServiceCategoryPagerAdapterWithTitle.addFragment(ThemeParkTab3Fragment.newInstance(), mParkArray[2]);

        mViewPager.setAdapter(mServiceCategoryPagerAdapterWithTitle);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(mParkPosition);
        tlSlidingTabs.setupWithViewPager(mViewPager, true);

        ViewGroup slidingTabStrip = (ViewGroup) tlSlidingTabs.getChildAt(0);

        for (int i = 0; i < slidingTabStrip.getChildCount() - 1; i++) {
            View v = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.rightMargin = (int) getResources().getDimension(R.dimen._5sdp);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mParkPosition = i;
                LogUtils.e("", "onPageSelected::" + mParkPosition);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(Tag_ThemeparkFragment, "isVisibleToUser::" + isVisibleToUser);
        if (isVisibleToUser) {
            if (mThemeParkPojo == null) {
                getThemePark();
            } else {
                setThemePark();
            }
        } else {
        }
    }

    private void getThemePark() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ThemeParkPojo> mCall = mApiInterface.getThemePark(Otapp.mUniqueID);
        mCall.enqueue(new Callback<ThemeParkPojo>() {
            @Override
            public void onResponse(Call<ThemeParkPojo> call, Response<ThemeParkPojo> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    mThemeParkPojo = response.body();
                    if (mThemeParkPojo != null) {
                        if (mThemeParkPojo.status.equalsIgnoreCase("200")) {

                            setThemePark();

                        } else {

                            Utils.showToast(getActivity(), "" + mThemeParkPojo.message);
                            mThemeParkPojo = null;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ThemeParkPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void setThemePark() {

        if (getActivity() != null && isAdded()) {

            if (mThemeParkPojo != null && mThemeParkPojo.data != null) {

                if (!TextUtils.isEmpty(mThemeParkPojo.data.bannerImg)) {
                    aviProgress.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(mThemeParkPojo.data.bannerImg).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
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

                List<ThemeParkPojo.ThemePark> mThemeList = mThemeParkPojo.data.themePark;
                List<ThemeParkPojo.WaterPark> mWaterList = mThemeParkPojo.data.waterPark;

                List<ThemeParkPojo.Park> mAllParkList = new ArrayList<>();
//                List<ThemeParkPojo.Park> mWaterParkList = new ArrayList<>();
                List<ThemeParkPojo.Park> mThemeParkList = new ArrayList<>();

                if (mThemeList != null && mThemeList.size() > 0) {
                    for (int i = 0; i < mThemeList.size(); i++) {

                        ThemeParkPojo.Park mThemePark = new ThemeParkPojo().new Park();
                        mThemePark.name = mThemeList.get(i).name;
                        mThemePark.availability = mThemeList.get(i).availability;
                        mThemePark.city = mThemeList.get(i).city;
                        mThemePark.image = mThemeList.get(i).image;
                        mThemePark.time = mThemeList.get(i).time;
                        mThemePark.prop_id = mThemeParkPojo.prop_id;
                        mThemeParkList.add(mThemePark);

                        ThemeParkPojo.Park mAllPark = new ThemeParkPojo().new Park();
                        mAllPark.name = mThemeList.get(i).name;
                        mAllPark.availability = mThemeList.get(i).availability;
                        mAllPark.city = mThemeList.get(i).city;
                        mAllPark.image = mThemeList.get(i).image;
                        mAllPark.time = mThemeList.get(i).time;
                        mAllPark.prop_id = mThemeParkPojo.prop_id;
                        mAllParkList.add(mAllPark);
                    }

                }

                if (mWaterList != null && mWaterList.size() > 0) {
                    for (int i = 0; i < mWaterList.size(); i++) {

//                        ThemeParkPojo.Park mWaterPark = new ThemeParkPojo().new Park();
//
//                        mWaterPark.name = mWaterList.get(i).name;
//                        mWaterPark.availability = mWaterList.get(i).availability;
//                        mWaterPark.city = mWaterList.get(i).city;
//                        mWaterPark.image = mWaterList.get(i).image;
//                        mWaterPark.time = mWaterList.get(i).time;
//                        mWaterParkList.add(mWaterPark);

                        ThemeParkPojo.Park mAllPark = new ThemeParkPojo().new Park();
                        mAllPark.name = mWaterList.get(i).name;
                        mAllPark.availability = mWaterList.get(i).availability;
                        mAllPark.city = mWaterList.get(i).city;
                        mAllPark.image = mWaterList.get(i).image;
                        mAllPark.time = mWaterList.get(i).time;
                        mAllPark.prop_id = mThemeParkPojo.prop_id;
                        mAllParkList.add(mAllPark);
                    }

                }

                LogUtils.e("", "mAllParkList::" + mAllParkList);
//                LogUtils.e("", "mWaterParkList::" + mWaterParkList);
                LogUtils.e("", "mThemeParkList::" + mThemeParkList);

                if (mAllParkList != null && mAllParkList.size() > 0) {
                    ThemeParkTabFragment mThemeParkTabFragment = (ThemeParkTabFragment) mServiceCategoryPagerAdapterWithTitle.getItem(0);
                    if (mThemeParkTabFragment != null) {
                        mThemeParkTabFragment.setParkList(mAllParkList, ThemeParkTabFragment.ParkType.All);
                    }
                }

//                if (mWaterParkList != null && mWaterParkList.size() > 0) {
//                    ThemeParkTabFragment mThemeParkTabFragment = (ThemeParkTabFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
//                    if (mThemeParkTabFragment != null) {
//                        mThemeParkTabFragment.setParkList(mWaterParkList, ThemeParkTabFragment.ParkType.Waterpark);
//                    }
//                }

                if (mThemeParkList != null && mThemeParkList.size() > 0) {
                    ThemeParkTabFragment mThemeParkTabFragment = (ThemeParkTabFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
                    if (mThemeParkTabFragment != null) {
                        mThemeParkTabFragment.setParkList(mThemeParkList, ThemeParkTabFragment.ParkType.Themepark);
                    }
                }

//                if (mAllParkList != null && mAllParkList.size() > 0) {
//                    ThemeParkTab1Fragment mThemeParkTabFragment = (ThemeParkTab1Fragment) mServiceCategoryPagerAdapterWithTitle.getItem(0);
//                    if (mThemeParkTabFragment != null) {
//                        mThemeParkTabFragment.setParkList(mAllParkList, ThemeParkTab1Fragment.ParkType.All);
//                    }
//                }
//
//                if (mWaterParkList != null && mWaterParkList.size() > 0) {
//                    ThemeParkTab2Fragment mThemeParkTabFragment = (ThemeParkTab2Fragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
//                    if (mThemeParkTabFragment != null) {
//                        mThemeParkTabFragment.setParkList(mWaterParkList, ThemeParkTab2Fragment.ParkType.Waterpark);
//                    }
//                }
//
//                if (mThemeParkList != null && mThemeParkList.size() > 0) {
//                    ThemeParkTab3Fragment mThemeParkTabFragment = (ThemeParkTab3Fragment) mServiceCategoryPagerAdapterWithTitle.getItem(2);
//                    if (mThemeParkTabFragment != null) {
//                        mThemeParkTabFragment.setParkList(mThemeParkList, ThemeParkTab3Fragment.ParkType.Themepark);
//                    }
//                }

            }
        }

    }

}

