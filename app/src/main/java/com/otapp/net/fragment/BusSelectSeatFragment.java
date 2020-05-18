package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otapp.net.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusSelectSeatFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_BusSelectSeatFragment = "Tag_" + "BusSelectSeatFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvSelectSeat)
    TextView tvSelectSeat;

    public static BusSelectSeatFragment newInstance() {
        BusSelectSeatFragment fragment = new BusSelectSeatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_bus_select_seat, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {


        tvBack.setOnClickListener(this);
        tvSelectSeat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == tvSelectSeat) {

            switchFragment(ProceedPayFragment.newInstance(), ProceedPayFragment.Tag_ProceedPayFragment);

        } else if (view == tvBack) {

            popBackStack();

        }
    }
}
