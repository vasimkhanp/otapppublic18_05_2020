package com.otapp.net.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.otapp.net.Bus.Core.Station;
import com.otapp.net.Bus.Fragments.SearchBusFragment;
import com.otapp.net.Events.Activity.EventsActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.ServiceCategoryPagerAdapterWithTitle;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.utils.Utils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceFragment extends BaseFragment {

    public static String Tag_ServiceFragment = "Tag_" + "ServiceFragment";

    View mView;

    //    @BindView(R.id.tvBus)
//    TextView tvBus;
//    @BindView(R.id.tvHotel)
//    TextView tvHotel;
//    @BindView(R.id.tvEvent)
//    TextView tvEvent;
//    @BindView(R.id.tvFlight)
//    TextView tvFlight;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.tlSlidingTabs)
    TabLayout tlSlidingTabs;

    ServiceCategoryPagerAdapterWithTitle mServiceCategoryPagerAdapterWithTitle;
    private int mServicePosition = 0;

    public static ServiceFragment newInstance() {
        ServiceFragment fragment = new ServiceFragment();
        return fragment;
    }

    public void setPosition(int position) {
        mServicePosition = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_service, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        List<String> mServiceList = Arrays.asList(getResources().getStringArray(R.array.service_array));

        if (!isAdded()) return;
        mServiceCategoryPagerAdapterWithTitle = new ServiceCategoryPagerAdapterWithTitle(getChildFragmentManager());

        for (int i = 0; i < mServiceList.size(); i++) {

            if (mServiceList.get(i).equalsIgnoreCase(getString(R.string.movie))) {
                mServiceCategoryPagerAdapterWithTitle.addFragment(MovieFragment.newInstance(), mServiceList.get(i));
            } else if (mServiceList.get(i).equalsIgnoreCase(getString(R.string.theme_park))) {
               mServiceCategoryPagerAdapterWithTitle.addFragment(ThemeparkFragment.newInstance(), mServiceList.get(i));
                //Toast.makeText(getContext(), "Comming Soon..", Toast.LENGTH_SHORT).show();
            } else if (mServiceList.get(i).equalsIgnoreCase(getString(R.string.event))) {
                mServiceCategoryPagerAdapterWithTitle.addFragment(EventFragment.newInstance(), mServiceList.get(i));

            } else if (mServiceList.get(i).equalsIgnoreCase(getString(R.string.bus))) {
                mServiceCategoryPagerAdapterWithTitle.addFragment(SearchBusFragment.newInstance(), mServiceList.get(i));
            } else if (mServiceList.get(i).equalsIgnoreCase(getString(R.string.flight))) {
                mServiceCategoryPagerAdapterWithTitle.addFragment(FlightFragment.newInstance(), mServiceList.get(i));
            } else if (mServiceList.get(i).equalsIgnoreCase(getString(R.string.hotel))) {
                mServiceCategoryPagerAdapterWithTitle.addFragment(HotelFragment.newInstance(), mServiceList.get(i));
            } else if (mServiceList.get(i).equalsIgnoreCase(getString(R.string.ferry))) {
                mServiceCategoryPagerAdapterWithTitle.addFragment(FerryFragment.newInstance(), mServiceList.get(i));
            } else if (mServiceList.get(i).equalsIgnoreCase(getString(R.string.tours))) {
                mServiceCategoryPagerAdapterWithTitle.addFragment(TourFragment.newInstance(), mServiceList.get(i));
            }
        }

        mViewPager.setAdapter(mServiceCategoryPagerAdapterWithTitle);
        mViewPager.setOffscreenPageLimit(7);
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

    public void setFromCity(FlightCityPojo.City mFromCity) {
        if (mServiceCategoryPagerAdapterWithTitle != null) {
            FlightFragment mFlightFragment = (FlightFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
            if (mFlightFragment != null) {
                mFlightFragment.setFromCity(mFromCity);
            }
        }
    }
   /* public void setBusFromCity(Station station){
        if (mServiceCategoryPagerAdapterWithTitle != null) {
            SearchBusFragment mFlightFragment = (SearchBusFragment) mServiceCategoryPagerAdapterWithTitle.getItem(3);
            if (mFlightFragment != null) {
                mFlightFragment.setFromCity(station);
            }
        }
    }*/

    public void setToCity(FlightCityPojo.City mToCity) {
        if (mServiceCategoryPagerAdapterWithTitle != null) {
            FlightFragment mFlightFragment = (FlightFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
            if (mFlightFragment != null) {
                mFlightFragment.setToCity(mToCity);
            }
        }
    }
    /*public void setBusToCity(Station station){
        if (mServiceCategoryPagerAdapterWithTitle != null) {
            SearchBusFragment mFlightFragment = (SearchBusFragment) mServiceCategoryPagerAdapterWithTitle.getItem(3);
            if (mFlightFragment != null) {
                mFlightFragment.setToCity(station);
            }
        }
    }*/

    public void setCurrency(FlightCityPojo.Currency mCurrency) {
        if (mServiceCategoryPagerAdapterWithTitle != null) {
            FlightFragment mFlightFragment = (FlightFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
            if (mFlightFragment != null) {
                mFlightFragment.setCurrency(mCurrency);
            }
        }
    }

    public void setFlightError(String message) {
        if (mServicePosition == 1) {
            Utils.showToast(getActivity(), "" + message);
        }
    }
}
