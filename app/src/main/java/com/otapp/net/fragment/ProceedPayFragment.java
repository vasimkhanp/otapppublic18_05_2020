package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otapp.net.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProceedPayFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_ProceedPayFragment = "Tag_" + "ProceedPayFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvProceedPay)
    TextView tvProceedPay;

    public static ProceedPayFragment newInstance() {
        ProceedPayFragment fragment = new ProceedPayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_proceed_pay, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {


        tvBack.setOnClickListener(this);
        tvProceedPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == tvProceedPay) {

            switchFragment(PaymentFragment.newInstance(), PaymentFragment.Tag_PaymentFragment);

        } else if (view == tvBack) {

            popBackStack();

        }
    }
}
