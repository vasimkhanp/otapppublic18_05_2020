package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otapp.net.R;
import com.otapp.net.utils.LogUtils;

import butterknife.ButterKnife;

public class FerryFragment extends BaseFragment {

    public static String Tag_FerryFragment = "Tag_" + "FerryFragment";

    View mView;

    public static FerryFragment newInstance() {
        FerryFragment fragment = new FerryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_service_blank, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(Tag_FerryFragment, "isVisibleToUser::"+isVisibleToUser);
        if (isVisibleToUser) {
        }
        else {
        }
    }
}
