package com.otapp.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.otapp.net.application.Otapp;
import com.otapp.net.fragment.EventOrderPreviewFragment;
import com.otapp.net.fragment.EventPaymentProcessFragment;
import com.otapp.net.fragment.FlightMultiCityAirlineListFragment;
import com.otapp.net.fragment.FlightOneAirlineListFragment;
import com.otapp.net.fragment.FlightOneDetailsFragment;
import com.otapp.net.fragment.FlightReturnDepartureAirlineListFragment;
import com.otapp.net.fragment.HomeFragment;
import com.otapp.net.fragment.MovieDetailsFragment;
import com.otapp.net.fragment.MovieFoodFragment;
import com.otapp.net.fragment.MovieOrderPreviewFragment;
import com.otapp.net.fragment.MoviePaymentProcessFragment;
import com.otapp.net.fragment.MovieSeatFragment;
import com.otapp.net.fragment.MyTripFragment;
import com.otapp.net.fragment.MyWalletFragment;
import com.otapp.net.fragment.ProfileEditFragment;
import com.otapp.net.fragment.ProfileFragment;
import com.otapp.net.fragment.ThemeParkPaymentFragment;
import com.otapp.net.home.fragment.MyBookingFragment;
import com.otapp.net.model.AddReviewPojo;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.mBottomNavigation)
    public BottomNavigationView bottomNavigationView;

    private String mLastFragmentTag = "";

    public static String IS_EXIT = "IS_EXIT";

//    HomeAdsResponse.Data mHomeAdsResponseData;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        deleteCache(getApplicationContext());
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "PaUSE", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "Restart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();

    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        InitializeControls();

    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }*/

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void InitializeControls() {


//        bottomNavigationView.getMenu().getItem(1).setEnabled(false);
//        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                            String tagLastFragment = getSupportFragmentManager().getBackStackEntryAt(
                                    getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                            Fragment mFragment = getSupportFragmentManager().findFragmentByTag(tagLastFragment);
                            if (mFragment instanceof MoviePaymentProcessFragment) {
                                MoviePaymentProcessFragment mMoviePaymentProcessFragment = (MoviePaymentProcessFragment) mFragment;
                                String mPaymentType = mMoviePaymentProcessFragment.getPaymentType();
                                if (!TextUtils.isEmpty(mPaymentType)) {
                                    if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_MPESA)) {
                                        Utils.showToast(getActivity(), getString(R.string.msg_back_mpesa));
                                        return false;
                                    } else if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_TIGO)) {
                                        Utils.showToast(getActivity(), getString(R.string.msg_back_tigo));
                                        return false;
                                    }
                                }
                            } else if (mFragment instanceof EventPaymentProcessFragment) {
                                EventPaymentProcessFragment mEventPaymentProcessFragment = (EventPaymentProcessFragment) mFragment;
                                String mPaymentType = mEventPaymentProcessFragment.getPaymentType();
                                if (!TextUtils.isEmpty(mPaymentType)) {
                                    if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_MPESA)) {
                                        Utils.showToast(getActivity(), getString(R.string.msg_back_mpesa));
                                        return false;
                                    } else if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_TIGO)) {
                                        Utils.showToast(getActivity(), getString(R.string.msg_back_tigo));
                                        return false;
                                    }
                                }
                            }
                        }

                        Fragment mSelectedFragment = null;
                        String mFragmentTag = "";
                        switch (item.getItemId()) {
                            case R.id.actionHome:
                                mSelectedFragment = HomeFragment.newInstance();
                                mFragmentTag = HomeFragment.Tag_HomeFragment;
//                                switchFragment(mSelectedFragment, mFragmentTag, false);
                                break;
                            case R.id.actionProfile:
                                mSelectedFragment = ProfileFragment.newInstance();
                                mFragmentTag = ProfileFragment.Tag_ProfileFragment;
                                break;
                            case R.id.actionMyBooking:
                                mSelectedFragment = MyBookingFragment.newInstance();
                                mFragmentTag = MyBookingFragment.Tag_MyBookingFragment;
                               // Toast.makeText(HomeActivity.this, "This Feature is coming soon..", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.actionMyWallet:
                                mSelectedFragment = MyWalletFragment.newInstance();
                                mFragmentTag = MyWalletFragment.Tag_MyWalletFragment;
                                break;
                        }

                        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount() - 1; ++i) {
                                getSupportFragmentManager().popBackStack();
                            }
//                            getSupportFragmentManager().popBackStack(HomeFragment.Tag_HomeFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }


                        switchFragment(mSelectedFragment, mFragmentTag, false);

                        LogUtils.e("", "item.getItemId():" + item.getItemId() + " " + (item.getItemId() == R.id.actionHome));
//                        if (item.getItemId() == R.id.actionHome) {
//                            if (mHomeAdsResponseData != null) {
//                                LogUtils.e("", "mHomeAdsResponseData is not null");
//                                setHomeAdsList();
//                            } else {
//                                LogUtils.e("", "mHomeAdsResponseData is null");
//                            }
//                        }
                        return true;
                    }
                });

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {

                        try {

                            int count = getSupportFragmentManager().getBackStackEntryCount();
                            String tagLastFragment = getSupportFragmentManager().getBackStackEntryAt(
                                    getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                            LogUtils.e("", "OnBackStackChangedListener count::" + count + " tagLastFragment::" + tagLastFragment);

                            if (!TextUtils.isEmpty(tagLastFragment)) {
                                Fragment mFragment = getSupportFragmentManager().findFragmentByTag(tagLastFragment);
//                                if (mFragment instanceof HomeFragment || mFragment instanceof ServiceFragment || mFragment instanceof MovieFragment || mFragment instanceof MovieDetailsFragment
//                                        || mFragment instanceof MovieSeatFragment || mFragment instanceof MovieDateSelectionFragment || mFragment instanceof MovieFoodFragment || mFragment instanceof MovieOrderPreviewFragment
//                                        || mFragment instanceof MovieOrderPaymentFragment || mFragment instanceof MoviePaymentProcessFragment || mFragment instanceof MovieReviewListFragment || mFragment instanceof MovieOrderReviewFragment
//                                        || mFragment instanceof EventFragment || mFragment instanceof EventDetailsFragment || mFragment instanceof EventOrderPreviewFragment || mFragment instanceof EventPaymentFragment
//                                        || mFragment instanceof EventPaymentProcessFragment || mFragment instanceof EventOrderReviewFragment || mFragment instanceof ThemeparkFragment || mFragment instanceof ThemeParkDetailsFragment
//                                        || mFragment instanceof ThemeParkAddCartFragment|| mFragment instanceof ThemeParkMyCartFragment|| mFragment instanceof ThemeParkRidesFragment|| mFragment instanceof ThemeParkPaymentFragment
//                                        || mFragment instanceof ThemeParkProcessFragment || mFragment instanceof ThemeParkOrderReviewFragment || mFragment instanceof FlightFragment || mFragment instanceof FlightOneWayFragment
//                                        || mFragment instanceof FlightOneAirlineListFragment || mFragment instanceof FlightReturnDepartureAirlineListFragment || mFragment instanceof FlightOneDetailsFragment || mFragment instanceof FlightReturnSelectionFragment
//                                        || mFragment instanceof FlightOneDetailsFragment|| mFragment instanceof FlightPaymentFragment|| mFragment instanceof FlightPaymentProcessFragment || mFragment instanceof FlightReviewOrderFragment) {
//
//                                    if (bottomNavigationView != null) {
//                                        bottomNavigationView.setSelectedItemId(R.id.actionHome);
//                                    }
//                                } else if (mFragment instanceof MyTripFragment) {
//                                    if (bottomNavigationView != null) {
//                                        bottomNavigationView.setSelectedItemId(R.id.actionMyBooking);
//                                    }
//                                } else if (mFragment instanceof ProfileFragment || mFragment instanceof ProfileEditFragment) {
//                                    if (bottomNavigationView != null) {
//                                        bottomNavigationView.setSelectedItemId(R.id.actionProfile);
//                                    }
//                                } else if (mFragment instanceof MyWalletFragment) {
//                                    if (bottomNavigationView != null) {
//                                        bottomNavigationView.setSelectedItemId(R.id.actionMyWallet);
//                                    }
//                                }


                                if (mFragment instanceof HomeFragment) {

                                    if (bottomNavigationView != null) {
                                        bottomNavigationView.setSelectedItemId(R.id.actionHome);
                                    }
                                } else if (mFragment instanceof MyTripFragment) {
                                    if (bottomNavigationView != null) {
                                        bottomNavigationView.setSelectedItemId(R.id.actionMyBooking);
                                    }
                                } else if (mFragment instanceof ProfileFragment) {
                                    if (bottomNavigationView != null) {
                                        bottomNavigationView.setSelectedItemId(R.id.actionProfile);
                                    }
                                } else if (mFragment instanceof MyWalletFragment) {
                                    if (bottomNavigationView != null) {
                                        bottomNavigationView.setSelectedItemId(R.id.actionMyWallet);
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

        switchFragment(HomeFragment.newInstance(), HomeFragment.Tag_HomeFragment, true);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.actionHome);
        }

//        if (mHomeAdsResponseData == null) {
//            getHomeAdsList();
//        } else {
//            setHomeAdsList();
//        }

//        if (Otapp.mCountryCodePojoList == null || Otapp.mCountryCodePojoList.size() == 0) {
//            getCountryCodeList();
//        }

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

//    private void setHomeAdsList() {
//        HomeFragment mHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.Tag_HomeFragment);
//        if (mHomeFragment != null) {
//            LogUtils.e("", "mHomeFragment is not null");
//            mHomeFragment.setHomeAdsList(mHomeAdsResponseData);
//        } else {
//            LogUtils.e("", "mHomeFragment is null");
//        }
//    }

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

    @Override
    protected void onResume() {
        super.onResume();
        MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_FLAG,"0");
        MyPref.setPref(getApplicationContext(),MyPref.PREF_PROMO_CODE,"");
    }
   /*   @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        Utils.showToast(getActivity(), "getBackStackEntryCount::" + getSupportFragmentManager().getBackStackEntryCount());
        try {


            if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {

                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                switchFragment(HomeFragment.newInstance(), HomeFragment.Tag_HomeFragment, true);

                if (bottomNavigationView != null) {
                    bottomNavigationView.setSelectedItemId(R.id.actionHome);
                }

//            HomeFragment mHomeFragment = HomeFragment.newInstance();
//            if (mHomeFragment.isAdded()) {
//
//                Utils.showToast(getActivity(), "Fragment already added OnResume");
//                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//                switchFragment(HomeFragment.newInstance(), HomeFragment.Tag_HomeFragment, true);
//                Utils.showToast(getActivity(), "HomeFragment Set in OnResume");
//                if (bottomNavigationView != null) {
//                    bottomNavigationView.setSelectedItemId(R.id.actionHome);
//                }
//
//                return;
//            } else {
//                switchFragment(HomeFragment.newInstance(), HomeFragment.Tag_HomeFragment, true);
//                Utils.showToast(getActivity(), "HomeFragment Set in OnResume");
//                if (bottomNavigationView != null) {
//                    bottomNavigationView.setSelectedItemId(R.id.actionHome);
//                }
//            }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    public void switchFragment(Fragment mSelectedFragment, String mFragmentTag, boolean isBackStack) {

        try {

            if (!TextUtils.isEmpty(mFragmentTag)) {
                mLastFragmentTag = mFragmentTag;
            }

            FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
            mTransaction.replace(R.id.mFrameLayout, mSelectedFragment, mFragmentTag);
            if (isBackStack) {
                mTransaction.addToBackStack(mFragmentTag);
            }
            getSupportFragmentManager().executePendingTransactions();
            mTransaction.commitAllowingStateLoss();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void popBackStackTag(String mFragTag) {
        try {
            getSupportFragmentManager().popBackStack(mFragTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void popBackStackTag() {

        try {

            int count = getSupportFragmentManager().getBackStackEntryCount();
            String tagLastFragment = getSupportFragmentManager().getBackStackEntryAt(
                    getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            LogUtils.e("", "onBackPressed count::" + count + " tagLastFragment::" + tagLastFragment);

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    if (isExitNeed) {
                        Intent back = new Intent();
                        back.putExtra(IS_EXIT, true);
                        setResult(RESULT_OK, back);
                        finish();
                    } else {
                        Utils.showToast(getActivity(), getString(R.string.msg_press_exit));
                        isExitNeed = true;
                    }
                } else {

                    getSupportFragmentManager().popBackStack();
                    isExitNeed = false;
                }

            } else {

                Intent back = new Intent();
                back.putExtra(IS_EXIT, true);
                setResult(RESULT_OK, back);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isExitNeed = false;

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            String tagLastFragmentStr = getSupportFragmentManager().getBackStackEntryAt(
                    getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            Fragment mLastFragment = getSupportFragmentManager().findFragmentByTag(tagLastFragmentStr);
            if (mLastFragment instanceof MoviePaymentProcessFragment) {
                MoviePaymentProcessFragment mMoviePaymentProcessFragment = (MoviePaymentProcessFragment) mLastFragment;
                String mPaymentType = mMoviePaymentProcessFragment.getPaymentType();
                if (!TextUtils.isEmpty(mPaymentType)) {
                    if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_MPESA)) {
                        Utils.showToast(getActivity(), getString(R.string.msg_back_mpesa));
                        return;
                    } else if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_TIGO)) {
                        Utils.showToast(getActivity(), getString(R.string.msg_back_tigo));
                        return;
                    }
                }
            } else if (mLastFragment instanceof EventPaymentProcessFragment) {
                EventPaymentProcessFragment mEventPaymentProcessFragment = (EventPaymentProcessFragment) mLastFragment;
                String mPaymentType = mEventPaymentProcessFragment.getPaymentType();
                if (!TextUtils.isEmpty(mPaymentType)) {
                    if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_MPESA)) {
                        Utils.showToast(getActivity(), getString(R.string.msg_back_mpesa));
                        return;
                    } else if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_TIGO)) {
                        Utils.showToast(getActivity(), getString(R.string.msg_back_tigo));
                        return;
                    } else if (mPaymentType.equalsIgnoreCase(Constants.BNDL_PAYMENT_TYPE_AIRTEL)) {
                        Utils.showToast(getActivity(), getString(R.string.msg_back_airtel));
                        return;
                    }
                }
            }

            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                String tagLastFragment = getSupportFragmentManager().getBackStackEntryAt(
                        getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                Fragment mFragment = getSupportFragmentManager().findFragmentByTag(tagLastFragment);
                if (mFragment instanceof MovieSeatFragment) {
                    MovieSeatFragment mMovieSeatFragment = (MovieSeatFragment) mFragment;
                    mMovieSeatFragment.deleteProcessedSeat();
                } else if (mFragment instanceof MovieFoodFragment) {
                    MovieFoodFragment mMovieFoodFragment = (MovieFoodFragment) mFragment;
                    mMovieFoodFragment.deleteProcessedSeat();
                    popBackStackTag();
                } else if (mFragment instanceof EventOrderPreviewFragment) {
                    EventOrderPreviewFragment mEventOrderPreviewFragment = (EventOrderPreviewFragment) mFragment;
                    mEventOrderPreviewFragment.deleteProcessedSeat();
                }
            }

            popBackStackTag();
        }

    }

    private Context getActivity() {
        return HomeActivity.this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.e("", "Home Activity onActivityResult requestCode:" + requestCode + " resultCode:" + resultCode);
        if (requestCode == Constants.RC_YT_DIALOG_REQUEST) {
            MovieDetailsFragment mMovieDetailsFragment = (MovieDetailsFragment) getSupportFragmentManager().findFragmentByTag(MovieDetailsFragment.Tag_MovieDetailsFragment);
            if (mMovieDetailsFragment != null) {
                mMovieDetailsFragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == Constants.RC_ONE_WAY_FILTER) {
            FlightOneAirlineListFragment mFlightOneAirlineListFragment = (FlightOneAirlineListFragment) getSupportFragmentManager().findFragmentByTag(FlightOneAirlineListFragment.Tag_FlightOneAirlineListFragment);
            if (mFlightOneAirlineListFragment != null) {
                mFlightOneAirlineListFragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == Constants.RC_RETURN_FILTER) {
            FlightReturnDepartureAirlineListFragment mFlightReturnDepartureAirlineListFragment = (FlightReturnDepartureAirlineListFragment) getSupportFragmentManager().findFragmentByTag(FlightReturnDepartureAirlineListFragment.Tag_FlightReturnDepartureAirlineListFragment);
            if (mFlightReturnDepartureAirlineListFragment != null) {
                mFlightReturnDepartureAirlineListFragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == Constants.RC_MULTI_CITY_FILTER) {
            FlightMultiCityAirlineListFragment mFlightMultiCityAirlineListFragment = (FlightMultiCityAirlineListFragment) getSupportFragmentManager().findFragmentByTag(FlightMultiCityAirlineListFragment.Tag_FlightMultiCityAirlineListFragment);
            if (mFlightMultiCityAirlineListFragment != null) {
                mFlightMultiCityAirlineListFragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == Constants.RC_SIGN_IN) {
            ProfileFragment mProfileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag(ProfileFragment.Tag_ProfileFragment);
            if (mProfileFragment != null) {
                mProfileFragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == Constants.RC_IMAGE) {
            ProfileEditFragment mProfileEditFragment = (ProfileEditFragment) getSupportFragmentManager().findFragmentByTag(ProfileEditFragment.Tag_ProfileEditFragment);
            if (mProfileEditFragment != null) {
                mProfileEditFragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == Constants.RC_FLIGHT_DETAIL_SIGN_IN) {
            FlightOneDetailsFragment mFlightOneDetailsFragment = (FlightOneDetailsFragment) getSupportFragmentManager().findFragmentByTag(FlightOneDetailsFragment.Tag_FlightOneDetailsFragment);
            if (mFlightOneDetailsFragment != null) {
                mFlightOneDetailsFragment.checkLoggedInUser();
            }
        } else if (requestCode == Constants.RC_MOVIE_COUPON_CODE && resultCode == Activity.RESULT_OK) {
            MovieOrderPreviewFragment mMovieOrderPreviewFragment = (MovieOrderPreviewFragment) getSupportFragmentManager().findFragmentByTag(MovieOrderPreviewFragment.Tag_MovieOrderPreviewFragment);
            if (mMovieOrderPreviewFragment != null) {
                mMovieOrderPreviewFragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == Constants.RC_EVENT_COUPON_CODE && resultCode == Activity.RESULT_OK) {
            EventOrderPreviewFragment mEventOrderPreviewFragment = (EventOrderPreviewFragment) getSupportFragmentManager().findFragmentByTag(EventOrderPreviewFragment.Tag_EventOrderPreviewFragment);
            if (mEventOrderPreviewFragment != null) {
                mEventOrderPreviewFragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == Constants.RC_PARK_COUPON_CODE && resultCode == Activity.RESULT_OK) {
            ThemeParkPaymentFragment mThemeParkPaymentFragment = (ThemeParkPaymentFragment) getSupportFragmentManager().findFragmentByTag(ThemeParkPaymentFragment.Tag_ThemeParkPaymentFragment);
            if (mThemeParkPaymentFragment != null) {
                mThemeParkPaymentFragment.onActivityResult(requestCode, resultCode, data);
            }
        }


    }

    public void loadThemepark() {
        HomeFragment mHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.Tag_HomeFragment);
        if (mHomeFragment != null) {
            mHomeFragment.loadThemepark();
        }
    }

    public void loadMovie() {
        HomeFragment mHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.Tag_HomeFragment);
        if (mHomeFragment != null) {
            mHomeFragment.loadMovie();
        }
    }

    public void setMovieRating(AddReviewPojo.Review data, String title, String description) {
        LogUtils.e("Home Activity", "setMovieRating avgRating::" + data.avgRating + " percentRating:" + data.percentRating + " totalReviews:" + data.totalReviews + "setMovieRating::" + title + "\n" + description);
        MovieDetailsFragment mMovieDetailsFragment = (MovieDetailsFragment) getSupportFragmentManager().findFragmentByTag(MovieDetailsFragment.Tag_MovieDetailsFragment);
        if (mMovieDetailsFragment != null) {
            mMovieDetailsFragment.setMovieRating(data, title, description);
        } else {
            LogUtils.e("", "mMovieDetailsFragment is null");
        }
    }
}
