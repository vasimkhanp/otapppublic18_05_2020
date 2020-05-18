package com.otapp.net.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.otapp.net.HomeActivity;

public class BaseFragment extends Fragment {

    public void switchFragment(Fragment mFragment, String mTag) {

        if (getActivity() instanceof HomeActivity) {

            HomeActivity activity = (HomeActivity) getActivity();
            activity.switchFragment(mFragment, mTag, true);
        }
    }

    public void switchFragment(Fragment mFragment, String mTag, Bundle bundle) {

        if (getActivity() instanceof HomeActivity) {

            HomeActivity activity = (HomeActivity) getActivity();

            if (bundle != null) {
                if (mFragment.getArguments() == null) {
                    mFragment.setArguments(bundle);
                } else {
                    mFragment.getArguments().putAll(bundle);
                }
            }

            activity.switchFragment(mFragment, mTag, true);
        }
    }

    public void switchFragment(Fragment mFragment) {

        if (getActivity() instanceof HomeActivity) {

            HomeActivity activity = (HomeActivity) getActivity();
            activity.switchFragment(mFragment, "", false);
        }
    }

    public void switchFragment(Fragment mFragment, Bundle bundle) {

        if (getActivity() instanceof HomeActivity) {

            HomeActivity activity = (HomeActivity) getActivity();

            if (bundle != null) {
                if (mFragment.getArguments() == null) {
                    mFragment.setArguments(bundle);
                } else {
                    mFragment.getArguments().putAll(bundle);
                }
            }

            activity.switchFragment(mFragment, "", false);
        }
    }

    public void switchFragment(Fragment mFragment, String mTag, boolean isBackStack) {

        if (getActivity() instanceof HomeActivity) {

            HomeActivity activity = (HomeActivity) getActivity();
            activity.switchFragment(mFragment, mTag, isBackStack);
        }
    }

    public void popBackStack() {

        try {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.popBackStackTag();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void popBackStack(String mFragTag) {

        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.popBackStackTag(mFragTag);
        }
    }
}
