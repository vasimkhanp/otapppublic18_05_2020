package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otapp.net.R;

import butterknife.ButterKnife;

public class MyWalletFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_MyWalletFragment = "Tag_" + "MyWalletFragment";

    View mView;

    public static MyWalletFragment newInstance() {
        MyWalletFragment fragment = new MyWalletFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_wallet, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

    }

    @Override
    public void onClick(View view) {

    }
}
