package com.otapp.net;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.adapter.PreferedAirlineFilterAdapter;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightFilterActivity extends BaseActivity implements View.OnClickListener {

    public static String Tag_FlightFilterFragment = "Tag_" + "FlightFilterActivity";

    View mView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvMorning)
    TextView tvMorning;
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.tvEvening)
    TextView tvEvening;
    @BindView(R.id.tvMoon)
    TextView tvMoon;
    @BindView(R.id.tvDepartureFrom)
    TextView tvDepartureFrom;
    @BindView(R.id.tvApplyFilters)
    TextView tvApplyFilters;
    @BindView(R.id.cbRefundFlight)
    CheckBox cbRefundFlight;
    @BindView(R.id.cbFreeMeal)
    CheckBox cbFreeMeal;
    @BindView(R.id.lnrRefundableFlight)
    LinearLayout lnrRefundableFlight;
    @BindView(R.id.lnrFreeMeal)
    LinearLayout lnrFreeMeal;
    @BindView(R.id.lvFlights)
    ListView lvFlights;

    List<FlightOneListPojo.Airlines> mAirlinesList;
    private String mTimeSlot = "0", mPreferredFlight = "", mRefundableFlight = "0";

    FlightCity mFlightCity;
    PreferedAirlineFilterAdapter mPreferedAirlineFilterAdapter;

    public static FlightFilterActivity newInstance() {
        FlightFilterActivity fragment = new FlightFilterActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_airline_filter);
        ButterKnife.bind(this);

//        InitializeControls();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        mView = inflater.inflate(R.layout.fragment_airline_filter, container, false);
//        ButterKnife.bind(this, mView);
//
//        InitializeControls();
//
//        return mView;
//    }


    private void InitializeControls() {

//        Bundle bundle = getArguments();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mFlightCity = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT), FlightCity.class);
            mTimeSlot = bundle.getString(Constants.BNDL_AIRLINE_TIME);
            mPreferredFlight = bundle.getString(Constants.BNDL_PREFERRED_AIRLINE_LIST);
            LogUtils.e("", "mPreferredFlight::" + mPreferredFlight);
            mAirlinesList = new Gson().fromJson(bundle.getString(Constants.BNDL_AIRLINE_LIST), new TypeToken<List<FlightOneListPojo.Airlines>>() {
            }.getType());

            if (mFlightCity != null) {
                tvDepartureFrom.setText(getString(R.string.departure_from) + " " + mFlightCity.fromCity);
            }

        }

        if (mAirlinesList != null && mAirlinesList.size() > 0) {
            for (int i = 0; i < mAirlinesList.size(); i++) {
                if (mAirlinesList.get(i).code.equalsIgnoreCase("NS0")) {
                    mAirlinesList.remove(i);
                    break;
                }
            }

            for (int i = 0; i < mAirlinesList.size(); i++) {
                if (mPreferredFlight.contains(mAirlinesList.get(i).code)) {
                    mAirlinesList.get(i).isSelected = true;
                } else {
                    mAirlinesList.get(i).isSelected = false;
                }
            }
        }
        mPreferedAirlineFilterAdapter = new PreferedAirlineFilterAdapter(FlightFilterActivity.this, new PreferedAirlineFilterAdapter.OnAirlineClickListener() {
            @Override
            public void onAirlineClicked(int position, boolean isSelected) {
                LogUtils.e("", "onAirlineClicked " + position + " " + isSelected);
                mAirlinesList.get(position).isSelected = isSelected;
            }
        });
        lvFlights.setAdapter(mPreferedAirlineFilterAdapter);
        mPreferedAirlineFilterAdapter.addAll(mAirlinesList);

        tvBack.setOnClickListener(this);
        lnrRefundableFlight.setOnClickListener(this);
        lnrFreeMeal.setOnClickListener(this);
        tvApplyFilters.setOnClickListener(this);
        tvMorning.setOnClickListener(this);
        tvDay.setOnClickListener(this);
        tvEvening.setOnClickListener(this);
        tvMoon.setOnClickListener(this);

        if (mTimeSlot.equals("1")) {
            mTimeSlot = "0";
            tvMorning.performClick();
        } else if (mTimeSlot.equals("2")) {
            mTimeSlot = "0";
            tvDay.performClick();
        } else if (mTimeSlot.equals("3")) {
            mTimeSlot = "0";
            tvEvening.performClick();
        } else if (mTimeSlot.equals("4")) {
            mTimeSlot = "0";
            tvMoon.performClick();
        }
    }

   /* private Context getActivity() {
        return FlightFilterActivity.this;
    }*/

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
//            popBackStack(FlightOneAirlineListFragment.Tag_FlightOneAirlineListFragment);
            finish();
        } else if (view == lnrRefundableFlight) {
            cbRefundFlight.setChecked(!cbRefundFlight.isChecked());
            if (cbRefundFlight.isChecked()) {
                mRefundableFlight = "1";
            } else {
                mRefundableFlight = "0";
            }
        } else if (view == lnrFreeMeal) {
            cbFreeMeal.setChecked(!cbFreeMeal.isChecked());
        } else if (view == tvApplyFilters) {

            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < mAirlinesList.size(); i++) {
                if (mAirlinesList.get(i).isSelected) {
                    if (strBuilder.length() > 0) {
                        strBuilder.append(", " + mAirlinesList.get(i).code);
                    } else {
                        strBuilder.append(mAirlinesList.get(i).code);
                    }
                }
            }

            Intent back = new Intent();
            back.putExtra(Constants.BNDL_AIRLINE_TIME, mTimeSlot);
            back.putExtra(Constants.BNDL_AIRLINE_REFUNDABLE, mRefundableFlight);
            back.putExtra(Constants.BNDL_PREFERRED_AIRLINE_LIST, (strBuilder.length() > 0 ? strBuilder.toString() : ""));
            setResult(RESULT_OK, back);
            finish();
//            popBackStack(FlightOneAirlineListFragment.Tag_FlightOneAirlineListFragment);
        } else if (view == tvMorning) {
            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
            tvDay.setBackgroundColor(getResources().getColor(R.color.white));
            tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
            tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
            if (mTimeSlot.equals("1")) {
                mTimeSlot = "0";
            } else {
                mTimeSlot = "1";
                tvMorning.setBackgroundColor(getResources().getColor(R.color.gray_e1));
            }


        } else if (view == tvDay) {
            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
            tvDay.setBackgroundColor(getResources().getColor(R.color.white));
            tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
            tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
            if (mTimeSlot.equals("2")) {
                mTimeSlot = "0";
            } else {
                mTimeSlot = "2";
                tvDay.setBackgroundColor(getResources().getColor(R.color.gray_e1));
            }
        } else if (view == tvEvening) {
            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
            tvDay.setBackgroundColor(getResources().getColor(R.color.white));
            tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
            tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
            if (mTimeSlot.equals("3")) {
                mTimeSlot = "0";
            } else {
                mTimeSlot = "3";
                tvEvening.setBackgroundColor(getResources().getColor(R.color.gray_e1));
            }
        } else if (view == tvMoon) {
            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
            tvDay.setBackgroundColor(getResources().getColor(R.color.white));
            tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
            tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
            if (mTimeSlot.equals("4")) {
                mTimeSlot = "0";
            } else {
                mTimeSlot = "4";
                tvMoon.setBackgroundColor(getResources().getColor(R.color.gray_e1));
            }
        }
    }
}
