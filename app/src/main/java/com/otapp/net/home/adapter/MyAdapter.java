package com.otapp.net.home.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import android.widget.Toast;

import com.otapp.net.application.Otapp;
import com.otapp.net.home.fragment.MyBookingFragment;
import com.otapp.net.home.fragment.MyBookingRecylerFragment;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.MyPref;

public class MyAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;
    public static String SERVICEID = "";

    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();

                bundle.putString("serviceId", "1");
                bundle.putString("requestType", "0");

                bundle.putString("pageNo", "0");
                SERVICEID = "1";
                MyBookingRecylerFragment myBookingFragmentEvent = new MyBookingRecylerFragment();
                myBookingFragmentEvent.setArguments(bundle);
                return myBookingFragmentEvent;
            case 1:
                bundle = new Bundle();
                bundle.putString("serviceId", "2");
                bundle.putString("requestType", "0");

                bundle.putString("pageNo", "0");
                SERVICEID = "2";
                MyBookingRecylerFragment myBookingFragmentMovies = new MyBookingRecylerFragment();
                myBookingFragmentMovies.setArguments(bundle);
                return myBookingFragmentMovies;
            case 2:
                bundle = new Bundle();
                bundle.putString("serviceId", "3");
                bundle.putString("requestType", "0");

                bundle.putString("pageNo", "0");
                SERVICEID = "3";
                MyBookingRecylerFragment myBookingFragmentThemePark = new MyBookingRecylerFragment();
                myBookingFragmentThemePark.setArguments(bundle);
                return myBookingFragmentThemePark;
            case 3:
                bundle = new Bundle();
                bundle.putString("serviceId", "4");
                bundle.putString("requestType", "0");

                bundle.putString("pageNo", "0");
                SERVICEID = "4";
                MyBookingRecylerFragment myBookingFragmentFlight = new MyBookingRecylerFragment();
                myBookingFragmentFlight.setArguments(bundle);
                return myBookingFragmentFlight;
            default:
                return null;
        }

        /*  position= position+1;
         */
        // return myBookingFragment;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return Otapp.mServiceList.get(position).service_name;
    }
}
