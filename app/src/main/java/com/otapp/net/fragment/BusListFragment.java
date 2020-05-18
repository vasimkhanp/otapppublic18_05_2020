package com.otapp.net.fragment;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otapp.net.FilterBusActivity;
import com.otapp.net.R;
import com.otapp.net.utils.IntentHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusListFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_BusListFragment = "Tag_" + "BusListFragment";

    View mView;

    @BindView(R.id.lnrFilter)
    LinearLayout lnrFilter;
    @BindView(R.id.lnrTime)
    LinearLayout lnrTime;
    @BindView(R.id.lnrAC)
    LinearLayout lnrAC;
    @BindView(R.id.lnrSeating)
    LinearLayout lnrSeating;
    @BindView(R.id.lnrSort)
    LinearLayout lnrSort;
    @BindView(R.id.lnrTimePreferences)
    LinearLayout lnrTimePreferences;
    @BindView(R.id.lnrSeatingPreferences)
    LinearLayout lnrSeatingPreferences;
    @BindView(R.id.lnrSortPreferences)
    LinearLayout lnrSortPreferences;
    @BindView(R.id.tvDropTime)
    TextView tvDropTime;
    @BindView(R.id.tvDropSeating)
    TextView tvDropSeating;
    @BindView(R.id.tvDropSort)
    TextView tvDropSort;
    @BindView(R.id.clBottom)
    ConstraintLayout clBottom;

    TextView tvFastest;
    TextView tvCheapest;
    TextView tvEarliest;
    TextView tvMorning;
    TextView tvDay;
    TextView tvEvening;
    TextView tvMoon;
    TextView tvAcSeater;
    TextView tvNonAcSeater;
    TextView tvAcSleeper;
    TextView tvNonAcSleeper;


    public static BusListFragment newInstance() {
        BusListFragment fragment = new BusListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_bus_list, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {


        tvMorning = lnrTimePreferences.findViewById(R.id.tvMorning);
        tvDay = lnrTimePreferences.findViewById(R.id.tvDay);
        tvEvening = lnrTimePreferences.findViewById(R.id.tvEvening);
        tvMoon = lnrTimePreferences.findViewById(R.id.tvMoon);

        tvAcSeater = lnrSeatingPreferences.findViewById(R.id.tvAcSeater);
        tvAcSleeper = lnrSeatingPreferences.findViewById(R.id.tvAcSleeper);
        tvNonAcSeater = lnrSeatingPreferences.findViewById(R.id.tvNonAcSeater);
        tvNonAcSleeper = lnrSeatingPreferences.findViewById(R.id.tvNonAcSleeper);

        tvFastest = lnrSortPreferences.findViewById(R.id.tvFastest);
        tvCheapest = lnrSortPreferences.findViewById(R.id.tvCheapest);
        tvEarliest = lnrSortPreferences.findViewById(R.id.tvEarliest);

        hideAllFilterPrerences();

        lnrFilter.setOnClickListener(this);
        lnrTime.setOnClickListener(this);
        lnrAC.setOnClickListener(this);
        lnrSeating.setOnClickListener(this);
        lnrSort.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        if (view == lnrFilter) {

            IntentHandler.startActivity(getActivity(), FilterBusActivity.class);

        } else if (view == lnrAC) {

            switchFragment(BusSelectSeatFragment.newInstance(), BusSelectSeatFragment.Tag_BusSelectSeatFragment);

        } else if (view == lnrTime) {

//            hideAllFilterPrerences();

            if (lnrTimePreferences.getVisibility() != View.VISIBLE) {
                lnrTimePreferences.setVisibility(View.VISIBLE);
                tvDropTime.setVisibility(View.VISIBLE);

                tvDropSeating.setVisibility(View.GONE);
                tvDropSort.setVisibility(View.GONE);

                lnrSeatingPreferences.setVisibility(View.GONE);
                lnrSortPreferences.setVisibility(View.GONE);

            } else {
                lnrTimePreferences.setVisibility(View.GONE);
                tvDropTime.setVisibility(View.GONE);
            }

        } else if (view == lnrSeating) {


            if (lnrSeatingPreferences.getVisibility() != View.VISIBLE) {
                lnrSeatingPreferences.setVisibility(View.VISIBLE);
                tvDropSeating.setVisibility(View.VISIBLE);

                tvDropTime.setVisibility(View.GONE);
                tvDropSort.setVisibility(View.GONE);

                lnrTimePreferences.setVisibility(View.GONE);
                lnrSortPreferences.setVisibility(View.GONE);
//                hideAllFilterPrerences();
            } else {
                lnrSeatingPreferences.setVisibility(View.GONE);
                tvDropSeating.setVisibility(View.GONE);
            }

        } else if (view == lnrSort) {


            if (lnrSortPreferences.getVisibility() != View.VISIBLE) {
                lnrSortPreferences.setVisibility(View.VISIBLE);
                tvDropSort.setVisibility(View.VISIBLE);

                tvDropTime.setVisibility(View.GONE);
                tvDropSeating.setVisibility(View.GONE);

                lnrTimePreferences.setVisibility(View.GONE);
                lnrSeatingPreferences.setVisibility(View.GONE);
//                hideAllFilterPrerences();
            } else {
                lnrSortPreferences.setVisibility(View.GONE);
                tvDropSort.setVisibility(View.GONE);
            }

        } else if (view == tvFastest || view == tvCheapest || view == tvEarliest) {
            hideAllFilterPrerences();
        } else if (view == tvMorning || view == tvDay || view == tvEvening || view == tvMoon) {
            hideAllFilterPrerences();
        } else if (view == tvAcSeater || view == tvAcSleeper || view == tvNonAcSeater || view == tvNonAcSleeper) {
            hideAllFilterPrerences();
        }
    }

    private void hideAllFilterPrerences() {

        tvDropTime.setVisibility(View.GONE);
        tvDropSeating.setVisibility(View.GONE);
        tvDropSort.setVisibility(View.GONE);

        lnrTimePreferences.setVisibility(View.GONE);
        lnrSeatingPreferences.setVisibility(View.GONE);
        lnrSortPreferences.setVisibility(View.GONE);

    }

}
