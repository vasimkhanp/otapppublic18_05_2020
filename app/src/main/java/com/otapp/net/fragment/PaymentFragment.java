package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otapp.net.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_PaymentFragment = "Tag_" + "PaymentFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvApply)
    TextView tvApply;

    public static PaymentFragment newInstance() {
        PaymentFragment fragment = new PaymentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_payment, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {


        tvBack.setOnClickListener(this);
        tvApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == tvApply) {

        } else if (view == tvBack) {

            popBackStack();

        }
    }
}
