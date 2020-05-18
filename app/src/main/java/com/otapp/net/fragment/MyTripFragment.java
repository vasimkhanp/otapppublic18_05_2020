package com.otapp.net.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.otapp.net.R;
import com.otapp.net.adapter.ServiceCategoryPagerAdapterWithTitle;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.MyPref;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyTripFragment extends BaseFragment {

    public static String Tag_MyTripFragment = "Tag_" + "MyTripFragment";

    View mView;

    @BindView(R.id.lnrLogin)
    LinearLayout lnrLogin;
    @BindView(R.id.tlSlidingTabs)
    TabLayout tlSlidingTabs;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private int mServicePosition = 0;

    ServiceCategoryPagerAdapterWithTitle mServiceCategoryPagerAdapterWithTitle;

    public static String cust_id = "", cust_log_key = "";

    public static MyTripFragment newInstance() {
        MyTripFragment fragment = new MyTripFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_trip, container, false);
        ButterKnife.bind(this, mView);

       // InitializeControls();

        return mView;
    }

    private void InitializeControls() {


        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
            lnrLogin.setVisibility(View.GONE);
        } else {
            lnrLogin.setVisibility(View.VISIBLE);
            return;
        }

        cust_id = MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
        cust_log_key = MyPref.getPref(getActivity(), MyPref.PREF_USER_KEY, "");

        List<String> mMyTripsTabList = Arrays.asList(getResources().getStringArray(R.array.my_trips_array));
        if (!isAdded()) return;
        mServiceCategoryPagerAdapterWithTitle = new ServiceCategoryPagerAdapterWithTitle(getChildFragmentManager());

        for (int i = 0; i < mMyTripsTabList.size(); i++) {
            Fragment mFragment = UpcomingTripsFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.CITY_TYPE_POSITION, ""+i);
            mFragment.setArguments(bundle);
            mServiceCategoryPagerAdapterWithTitle.addFragment(mFragment, mMyTripsTabList.get(i));
        }

        mViewPager.setAdapter(mServiceCategoryPagerAdapterWithTitle);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(mServicePosition);
        tlSlidingTabs.setupWithViewPager(mViewPager, true);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mServicePosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }
}
