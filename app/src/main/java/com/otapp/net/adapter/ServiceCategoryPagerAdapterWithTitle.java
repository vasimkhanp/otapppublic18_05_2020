package com.otapp.net.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


//public class PagerAdapterWithTitle extends FragmentPagerAdapter {
public class ServiceCategoryPagerAdapterWithTitle extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ServiceCategoryPagerAdapterWithTitle(FragmentManager childFragmentManager) {
        super(childFragmentManager);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment mFragment, String title) {
        mFragmentList.add(mFragment);
        mFragmentTitleList.add(title);
    }

    public void addFragment(Fragment mFragment, String title, Bundle bundle) {
        if (bundle != null) {
            if (mFragment.getArguments() == null) {
                mFragment.setArguments(bundle);
            } else {
                mFragment.getArguments().putAll(bundle);
            }
        }
        mFragmentList.add(mFragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public List<Fragment> getFragmentList() {
        return mFragmentList;
    }
}
