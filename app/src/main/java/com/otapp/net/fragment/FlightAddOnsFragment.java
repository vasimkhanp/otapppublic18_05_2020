package com.otapp.net.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.ServiceCategoryPagerAdapterWithTitle;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightOneDetailsPojo;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.model.FlightReturnListPojo;
import com.otapp.net.utils.Constants;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightAddOnsFragment extends BaseFragment {

    public static String Tag_FlightAddOnsFragment = "Tag_" + "FlightAddOnsFragment";

    View mView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tlSlidingTabs)
    TabLayout tlSlidingTabs;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    FlightOneListPojo.Data mFlightData;
    FlightOneDetailsPojo mFlightOneDetailsPojo;
    FlightCity mFlightCity;

    FlightReturnListPojo.Data mFlightReturnData;
    String mFlightUid = "";

    private int mPosition = -1;

    ServiceCategoryPagerAdapterWithTitle mServiceCategoryPagerAdapterWithTitle;

    public static FlightAddOnsFragment newInstance() {
        FlightAddOnsFragment fragment = new FlightAddOnsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_flight_add_ons, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            mFlightCity = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT), FlightCity.class);
            mFlightOneDetailsPojo = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT_DETAILS_RESPONSE), FlightOneDetailsPojo.class);
            mPosition = bundle.getInt(Constants.CITY_TYPE_POSITION);
            if (mPosition == 0) {
                mFlightData = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT_ONE_DETAILS), FlightOneListPojo.Data.class);
            } else if (mPosition == 1) {
                mFlightUid = bundle.getString(Constants.BNDL_FLIGHT_UID, "");
                mFlightReturnData = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT_RETURN_DETAILS), FlightReturnListPojo.Data.class);
            }
        }

        List<String> mFlightArray = Arrays.asList(getResources().getStringArray(R.array.flight_addons_array));

        if (!isAdded()) return;
        mServiceCategoryPagerAdapterWithTitle = new ServiceCategoryPagerAdapterWithTitle(getChildFragmentManager());

        Bundle bundle1 = new Bundle();
        bundle1.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
        if (mPosition == 0) {
            bundle1.putString(Constants.BNDL_FLIGHT_ONE_DETAILS, new Gson().toJson(mFlightData));
        } else if (mPosition == 1) {
            bundle1.putString(Constants.BNDL_FLIGHT_UID, mFlightUid);
            bundle1.putString(Constants.BNDL_FLIGHT_RETURN_DETAILS, new Gson().toJson(mFlightReturnData));
        }
        bundle1.putString(Constants.BNDL_FLIGHT_DETAILS_RESPONSE, new Gson().toJson(mFlightOneDetailsPojo));
        bundle1.putInt(Constants.CITY_TYPE_POSITION, mPosition);

        if (mFlightOneDetailsPojo.data.baggages != null && mFlightOneDetailsPojo.data.baggages.size() > 0) {
            tlSlidingTabs.setVisibility(View.VISIBLE);
        } else {
            mFlightArray.remove(mFlightArray.size() - 1);
            tlSlidingTabs.setVisibility(View.GONE);
        }

        for (int i = 0; i < mFlightArray.size(); i++) {
            if (i == 0) {
                Fragment mFragment = FlightMealFragment.newInstance();
                mFragment.setArguments(bundle1);
                mServiceCategoryPagerAdapterWithTitle.addFragment(mFragment, mFlightArray.get(i));
            } else if (i == 1) {
                Fragment mFragment = FlightBaggageFragment.newInstance();
                mFragment.setArguments(bundle1);
                mServiceCategoryPagerAdapterWithTitle.addFragment(mFragment, mFlightArray.get(i));
            }

        }

        mViewPager.setAdapter(mServiceCategoryPagerAdapterWithTitle);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(0);
        tlSlidingTabs.setupWithViewPager(mViewPager, true);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    tlSlidingTabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.green_veg));
                } else {
                    tlSlidingTabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

}
