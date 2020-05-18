package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.ParkAdapter;
import com.otapp.net.model.ThemeParkPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemeParkTabFragment extends BaseFragment {

    public static String Tag_ThemeParkTabFragment = "Tag_" + "ThemeParkTabFragment";

    View mView;

    @BindView(R.id.lvPark)
    ListView lvPark;

    private ParkAdapter mParkAdapter;
    List<ThemeParkPojo.Park> mParkList;

    public static enum ParkType {All, Waterpark, Themepark}

    ParkType mParkType;

    public static ThemeParkTabFragment newInstance() {
        ThemeParkTabFragment fragment = new ThemeParkTabFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_theme_park_list, container, false);
        ButterKnife.bind(this, mView);
        LogUtils.e("" + Tag_ThemeParkTabFragment, "onCreateView");

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        mParkAdapter = new ParkAdapter(getActivity(), this);
        lvPark.setAdapter(mParkAdapter);

    }

    public void onParkClicked(ThemeParkPojo.Park mPark) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
        switchFragment(ThemeParkRidesFragment.newInstance(), ThemeParkRidesFragment.Tag_ThemeParkRidesFragment, bundle);

    }


    public void setParkList(List<ThemeParkPojo.Park> mParkList, ParkType mParkType) {
        this.mParkType = mParkType;
        this.mParkList = mParkList;

        if (mParkAdapter != null) {
            mParkAdapter.addAll(mParkList);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(Tag_ThemeParkTabFragment, "isVisibleToUser::" + isVisibleToUser);
        if (isVisibleToUser) {
            if (mParkList == null || mParkList.size() == 0) {
                if (isAdded() && getActivity() != null) {
                    if (mParkType != null) {

                        if (mParkType == ParkType.Waterpark) {
                            Utils.showToast(getActivity(), getString(R.string.alert_no_water_park));
                        } else if (mParkType == ParkType.Themepark) {
                            Utils.showToast(getActivity(), getString(R.string.alert_no_theme_park));
                        } else {
                            Utils.showToast(getActivity(), getString(R.string.alert_no_park));
                        }
                    }

                }
            } else {
//                if (mParkAdapter != null) {
//                    mParkAdapter.addAll(mParkList);
//                }
            }
        } else {

        }
    }

}
