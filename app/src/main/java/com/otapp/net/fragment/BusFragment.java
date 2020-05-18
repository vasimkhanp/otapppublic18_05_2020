package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otapp.net.R;
import com.otapp.net.utils.LogUtils;

import butterknife.ButterKnife;

public class BusFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_BusFragment = "Tag_" + "BusFragment";

    View mView;
//    @BindView(R.id.tvSearchBuses)
//    TextView tvSearchBuses;

    public static BusFragment newInstance() {
        BusFragment fragment = new BusFragment();
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

//        tvSearchBuses.setOnClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(Tag_BusFragment, "isVisibleToUser::"+isVisibleToUser);
        if (isVisibleToUser) {
        }
        else {
        }
    }

    @Override
    public void onClick(View view) {

//        if (view == tvSearchBuses) {
//
//            switchFragment(BusListFragment.newInstance(), BusListFragment.Tag_BusListFragment);
//
//        }
    }
}
