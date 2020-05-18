package com.otapp.net.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class PAdapter  extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    List<Fragment> fragmentList;

    public PAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mNumOfTabs = fragmentList.size();
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
