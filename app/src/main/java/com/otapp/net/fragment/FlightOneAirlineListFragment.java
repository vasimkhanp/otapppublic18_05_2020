package com.otapp.net.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.FlightFilterActivity;
import com.otapp.net.FlightsFilters;
import com.otapp.net.R;
import com.otapp.net.adapter.AirlineOneFilterAdapter;
import com.otapp.net.adapter.FlightOneAdapter;
import com.otapp.net.adapter.PreferedAirlineFilterAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightOneAirlineListFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_FlightOneAirlineListFragment = "Tag_" + "FlightOneAirlineListFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDetails)
    TextView tvDetails;
    //    @BindView(R.id.tvDeparture)
//    TextView tvDeparture;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvFilter)
    TextView tvFilter;
    @BindView(R.id.tvToday)
    TextView tvToday;
    @BindView(R.id.tvTomorrow)
    TextView tvTomorrow;
    @BindView(R.id.tvDayAfterTomorrow)
    TextView tvDayAfterTomorrow;
    @BindView(R.id.tvFourthDay)
    TextView tvFourthDay;
    @BindView(R.id.tvNoFlight)
    TextView tvNoFlight;
    @BindView(R.id.lnrFilter)
    LinearLayout lnrFilter;
    @BindView(R.id.lvAirline)
    ListView lvAirline;
    @BindView(R.id.rvFilters)
    RecyclerView rvFilters;

    FlightCity mFlightCity;
    List<FlightCityPojo.City> mFlightCityList;
    FlightOneListPojo mFlightOneListPojo;

    FlightOneAdapter mFlightOneAdapter;
    AirlineOneFilterAdapter mAirlineOneFilterAdapter;

    List<FlightOneListPojo.Airlines> mAirlineList;
    List<FlightOneListPojo.Airlines> mTempAirlineList = new ArrayList<>();
    List<FlightOneListPojo.Airlines> mPreferredAirlineList = new ArrayList<>();
    List<FlightOneListPojo.Airlines> mTempArlineFilterList = new ArrayList<>();

    private String mTimeSlot = "0", mPreferredFlight = "", mRefundableFlight = "0";

    String mDialogTimeSlot = "0", mDialogPreferredFlight = "", mDailogRefundableFlight = "0";

    Calendar mStartCalDate = Calendar.getInstance();
    private int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;
    int bookingTimeStatus = 0;
    int refundableStatus=0;

    private int mPosition = -1;

    private CountDownTimer countDownTimer;

    public static FlightOneAirlineListFragment newInstance() {
        FlightOneAirlineListFragment fragment = new FlightOneAirlineListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_one_flight_list, container, false);
        ButterKnife.bind(this, mView);
        InitializeControls();
        return mView;
    }

    private void InitializeControls() {
        mFlightOneAdapter = new FlightOneAdapter(getActivity(), new FlightOneAdapter.OnFlightSelectListener() {
            @Override
            public void onFlightSelected(FlightOneListPojo.Data mFlightData) {
                MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, 0l);
                Bundle bundle = new Bundle();

                MyPref.setPref(getContext(),Constants.BNDL_FLIGHT,new Gson().toJson(mFlightCity));
                MyPref.setPref(getContext(),Constants.BNDL_CITY_LIST, new Gson().toJson(mFlightCityList));
                MyPref.setPref(getContext(),Constants.CITY_TYPE_POSITION, mPosition);
                MyPref.setPref(getContext(),Constants.BNDL_FLIGHT_NAME_LIST, new Gson().toJson(mFlightOneListPojo.airlines));
                MyPref.setPref(getContext(),Constants.BNDL_FLIGHT_ONE_DETAILS, new Gson().toJson(mFlightData));
//                bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
//                bundle.putString(Constants.BNDL_FLIGHT_NAME_LIST, new Gson().toJson(mFlightOneListPojo.airlines));
//                bundle.putString(Constants.BNDL_CITY_LIST, new Gson().toJson(mFlightCityList));
               // bundle.putString(Constants.BNDL_FLIGHT_ONE_DETAILS, new Gson().toJson(mFlightData));
//                bundle.putInt(Constants.CITY_TYPE_POSITION, mPosition);
            //    bundle.putParcelable("flightData", (Parcelable) mFlightData);
                switchFragment(FlightOneDetailsFragment.newInstance(), FlightOneDetailsFragment.Tag_FlightOneDetailsFragment, bundle);
            }
        });
        lvAirline.setAdapter(mFlightOneAdapter);

        mAirlineOneFilterAdapter = new AirlineOneFilterAdapter(getActivity(), new AirlineOneFilterAdapter.OnAirlineClickListener() {
            @Override
            public void onAirlineClicked(List<FlightOneListPojo.Airlines> mAirline) {
                mAirlineList = mAirline;
                if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
                    mTempAirlineList.clear();
                }
                mTempAirlineList.addAll(mAirlineList);
                setFilterFlightList();
            }
        });
        rvFilters.setAdapter(mAirlineOneFilterAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {

            String sFlightCity=MyPref.getPref(getContext(),Constants.BNDL_FLIGHT,"");
            mFlightCity = new Gson().fromJson(sFlightCity, FlightCity.class);


            String sFlightCityList=MyPref.getPref(getContext(),Constants.BNDL_CITY_LIST,"");
            mFlightCityList = new Gson().fromJson(sFlightCityList, new TypeToken<ArrayList<FlightCityPojo.City>>() {
            }.getType());

            if (mFlightCityList != null && mFlightCityList.size() > 0) {
                LogUtils.e("", "mFlightCityList size:" + mFlightCityList.size());
            }
            mPosition =MyPref.getPref(getContext(),Constants.CITY_TYPE_POSITION,0);
            if (mFlightCity != null) {
                tvTitle.setText(mFlightCity.fromCity + " " + getString(R.string.to_) + " " + mFlightCity.toCity);
//                tvDeparture.setText(getString(R.string.departure_flight) + " " + mFlightCity.from + " - " + mFlightCity.to);

                mStartCalDate.setTimeInMillis(mFlightCity.displayDepartDate);

                mYear = mStartCalDate.get(Calendar.YEAR);
                mMonth = mStartCalDate.get(Calendar.MONTH);
                mDay = mStartCalDate.get(Calendar.DAY_OF_MONTH);

                tvDetails.setText(getString(R.string.depart) + " " + DateFormate.sdfFlightDisplayDate.format(mStartCalDate.getTime()) + " | " + mFlightCity.traveller + " | " + mFlightCity.clasName);
                tvDate.setText("" + DateFormate.sdfCalDisplayDate.format(mStartCalDate.getTime()));

                setDayStatus();
                getFlightList();
            }
        }

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        mStartCalDate.set(Calendar.YEAR, year);
                        mStartCalDate.set(Calendar.MONTH, monthOfYear);
                        mStartCalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        tvDetails.setText(getString(R.string.depart) + " " + DateFormate.sdfFlightDisplayDate.format(mStartCalDate.getTime()) + " | " + mFlightCity.traveller + " | " + mFlightCity.clasName);
                        tvDate.setText("" + DateFormate.sdfCalDisplayDate.format(mStartCalDate.getTime()));

                        mFlightCity.departDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());

                        setDayStatus();

                        if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
                            mTempAirlineList.clear();
                        }

                        getFlightList();

                    }
                }, mYear, mMonth, mDay);

        Calendar mCal = Calendar.getInstance();
        datePickerDialog.getDatePicker().setMinDate(mCal.getTimeInMillis() - 10000);
        mCal.add(Calendar.MONTH, 36);
        datePickerDialog.getDatePicker().setMaxDate(mCal.getTimeInMillis() - 10000); // for 360 days

        tvBack.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvFilter.setOnClickListener(this);
        tvToday.setOnClickListener(this);
        tvTomorrow.setOnClickListener(this);
        tvDayAfterTomorrow.setOnClickListener(this);
        tvFourthDay.setOnClickListener(this);

    }

    private void stopCountdown() {
        LogUtils.e("", "stopCountdown");
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopCountdown();
//        Toast.makeText(getActivity(), "onDestroyView", Toast.LENGTH_SHORT).show();
    }

    private void startTimer(long msUntilFinished) {

        long millis = msUntilFinished;
//        String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
//        tvSession.setText(String.format(getString(R.string.sessing_five_minute), hms));
//
//        lnrSessionTime.setVisibility(View.VISIBLE);

//        MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SEARCH_LIST_SESSION_TIME, millis);

        countDownTimer = new CountDownTimer(msUntilFinished, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
//                String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
//                tvSession.setText(String.format(getString(R.string.sessing_five_minute), hms));

//                MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SEARCH_LIST_SESSION_TIME, millis);
            }

            public void onFinish() {
                countDownTimer = null;
//                MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SEARCH_LIST_SESSION_TIME, 0l);
                if (getActivity() != null) {
                    Utils.showToast(getActivity(), getString(R.string.msg_refresh_list));
                    getFlightList();
                }


            }
        }.start();

    }

    private void setDayStatus() {

        tvTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
        tvTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
        tvToday.setBackgroundResource(R.drawable.bg_button_tab_unselected);
        tvToday.setTextColor(getResources().getColor(R.color.gray_4f));
        tvDayAfterTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
        tvDayAfterTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
        tvFourthDay.setBackgroundResource(R.drawable.bg_button_tab_unselected);
        tvFourthDay.setTextColor(getResources().getColor(R.color.gray_4f));

//        Calendar now = Calendar.getInstance();
//        LogUtils.e("", "DateFormate.sdfDateCompare.format(now.getTime())::" + DateFormate.sdfDateCompare.format(now.getTime()) + " " + DateFormate.sdfDateCompare.format(mStartCalDate.getTime()) + " " + DateFormate.sdfDateCompare.format(now.getTime()).equals(DateFormate.sdfDateCompare.format(mStartCalDate.getTime())));
//        if (DateFormate.sdfDateCompare.format(now.getTime()).equals(DateFormate.sdfDateCompare.format(mStartCalDate.getTime()))) {
//
//            tvToday.setBackgroundResource(R.drawable.bg_button_tab);
//            tvToday.setTextColor(getResources().getColor(R.color.white));
//            tvTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
//            tvTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
//            tvDayAfterTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
//            tvDayAfterTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
//        }
//
//        Calendar tomorrow = Calendar.getInstance();
//        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
//        LogUtils.e("", "DateFormate.sdfDateCompare.format(tomorrow.getTime())::" + DateFormate.sdfDateCompare.format(tomorrow.getTime()) + " " + DateFormate.sdfDateCompare.format(mStartCalDate.getTime()) + " " + DateFormate.sdfDateCompare.format(tomorrow.getTime()).equals(DateFormate.sdfDateCompare.format(mStartCalDate.getTime())));
//        if (DateFormate.sdfDateCompare.format(tomorrow.getTime()).equals(DateFormate.sdfDateCompare.format(mStartCalDate.getTime()))) {
//            tvTomorrow.setBackgroundResource(R.drawable.bg_button_tab);
//            tvTomorrow.setTextColor(getResources().getColor(R.color.white));
//            tvToday.setBackgroundResource(R.drawable.bg_button_tab_unselected);
//            tvToday.setTextColor(getResources().getColor(R.color.gray_4f));
//            tvDayAfterTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
//            tvDayAfterTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
//        }
//
//        Calendar dayAfterTomorrow = Calendar.getInstance();
//        dayAfterTomorrow.add(Calendar.DAY_OF_MONTH, 2);
//        LogUtils.e("", "DateFormate.sdfDateCompare.format(tomorrow.getTime())::" + DateFormate.sdfDateCompare.format(dayAfterTomorrow.getTime()) + " " + DateFormate.sdfDateCompare.format(mStartCalDate.getTime()) + " " + DateFormate.sdfDateCompare.format(dayAfterTomorrow.getTime()).equals(DateFormate.sdfDateCompare.format(mStartCalDate.getTime())));
//        if (DateFormate.sdfDateCompare.format(dayAfterTomorrow.getTime()).equals(DateFormate.sdfDateCompare.format(mStartCalDate.getTime()))) {
//            tvDayAfterTomorrow.setBackgroundResource(R.drawable.bg_button_tab);
//            tvDayAfterTomorrow.setTextColor(getResources().getColor(R.color.white));
//            tvToday.setBackgroundResource(R.drawable.bg_button_tab_unselected);
//            tvToday.setTextColor(getResources().getColor(R.color.gray_4f));
//            tvTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
//            tvTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
//        }

        Calendar current = Calendar.getInstance();
        current.setTime(mStartCalDate.getTime());
//        if (DateFormate.sdfDateCompare.format(current.getTime()).equals(DateFormate.sdfDateCompare.format(mStartCalDate.getTime()))) {
//            tvToday.setText("" + getString(R.string.today));
//            tvTomorrow.setText("" + getString(R.string.tomorrow));
//            tvDayAfterTomorrow.setText("" + getString(R.string.day_after_tomorrow));
//        } else {
        current.add(Calendar.DAY_OF_MONTH, 1);
        tvToday.setText(DateFormate.sdfCalDisplayDate.format(current.getTime()));
        tvToday.setTag(current.getTimeInMillis());
        current.add(Calendar.DAY_OF_MONTH, 1);
        tvTomorrow.setText(DateFormate.sdfCalDisplayDate.format(current.getTime()));
        tvTomorrow.setTag(current.getTimeInMillis());
        current.add(Calendar.DAY_OF_MONTH, 1);
        tvDayAfterTomorrow.setText(DateFormate.sdfCalDisplayDate.format(current.getTime()));
        tvDayAfterTomorrow.setTag(current.getTimeInMillis());
        current.add(Calendar.DAY_OF_MONTH, 1);
        tvFourthDay.setText(DateFormate.sdfCalDisplayDate.format(current.getTime()));
        tvFourthDay.setTag(current.getTimeInMillis());
//        }

    }

    private void getFlightList() {
        try {
            if (getActivity() == null) {
                return;
            }

            if (!Utils.isInternetConnected(getActivity())) {
                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                return;
            }

            if (!Utils.isProgressDialogShowing()) {
                Utils.showFlightProgressDialog(getActivity());
            }


            ApiInterface mApiInterface = RestClient.getClient(true);

            Log.d("Log", "from city " + mFlightCity.from);
            Log.d("Log", "to city " + mFlightCity.to);
            Log.d("Log", "dpDate " + mFlightCity.departDate);
            Log.d("Log", "adult " + mFlightCity.adultCount);
            Log.d("Log", "child " + mFlightCity.childCount);
            Log.d("Log", "infant " + mFlightCity.infantCount);
            Log.d("Log", "clas " + mFlightCity.clas);
            Log.d("Log", "currency " + mFlightCity.currencyName);
            Log.d("Log", "timeslot " + mTimeSlot);
            Log.d("Log", "refundablefligt " + mRefundableFlight);
            Log.d("Log", "preferredflight " + mPreferredFlight);
            Log.d("Log", "unique " + Otapp.mUniqueID);
            Log.d("Log", "authtoken " + mFlightCity.flightAuthToken);


            Call<FlightOneListPojo> mCall = mApiInterface.getFlightOneList(mFlightCity.from, mFlightCity.to, mFlightCity.departDate, "" + mFlightCity.adultCount,
                    "" + mFlightCity.childCount, "" + mFlightCity.infantCount, mFlightCity.clas, mFlightCity.currencyName, mTimeSlot,
                    mRefundableFlight, mPreferredFlight, Otapp.mUniqueID, mFlightCity.flightAuthToken);
            mCall.enqueue(new Callback<FlightOneListPojo>() {
                @Override
                public void onResponse(Call<FlightOneListPojo> call, Response<FlightOneListPojo> response) {
                    LogUtils.e("", "onResponse: ");
                    Utils.closeProgressDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObjectResponse = null;
                            jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                            Log.d("Log", "Response Search: " + jsonObjectResponse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mFlightOneListPojo = response.body();
                        if (mFlightOneListPojo != null) {
                            if (mFlightOneListPojo.status != null)
                                if (mFlightOneListPojo.status.equalsIgnoreCase("200")) {

                                    if (mFlightOneListPojo.data != null) {
                                        if (mTempArlineFilterList.size() == 0) {
                                            mTempArlineFilterList = mFlightOneListPojo.airlines;
                                        }

                                        int previousIndex = -1, last2Index = -1;
                                        long minimalDuration = 0;
                                        for (int i = 0; i < mFlightOneListPojo.data.size(); i++) {
                                            List<FlightOneListPojo.Cities> cities = mFlightOneListPojo.data.get(i).cities;
//                                    if (mFlightOneListPojo.data.get(i).cities.size() == 1) {
                                            try {
/*
                                        Date date1 = DateFormate.sdfAirportServerDate.parse(cities.get(0).startDate);
                                  Date date2 = DateFormate.sdfAirportServerDate.parse(cities.get(cities.size() - 1).endDate);
                                      long mDurationLayover = (date2.getTime() - date1.getTime());*/
                                                long mDuration = 0;
                                                for (int j = 0; j < cities.size(); j++) {
                                                    mDuration = mDuration + cities.get(j).duration;
                                                }

                                                if (minimalDuration == 0) {
                                                    minimalDuration = mDuration;
                                                    LogUtils.e("", i + "minimalDuration::" + minimalDuration + " " + mFlightOneListPojo.data.get(i).fares.total.grandTotal);
                                                    previousIndex = i;
                                                    mFlightOneListPojo.data.get(i).tag = getString(R.string.fastest);
                                                } else if (minimalDuration > mDuration) {
                                                    mFlightOneListPojo.data.get(previousIndex).tag = "";
                                                    last2Index = previousIndex;
                                                    previousIndex = i;
                                                    minimalDuration = mDuration;
                                                    LogUtils.e("", i + "minimalDuration::" + minimalDuration + " " + mFlightOneListPojo.data.get(i).fares.total.grandTotal + " " + last2Index);
                                                    mFlightOneListPojo.data.get(i).tag = getString(R.string.fastest);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
//                                    }
                                        }

                                        if (TextUtils.isEmpty(mFlightOneListPojo.data.get(0).tag)) {
                                            mFlightOneListPojo.data.get(0).tag = "" + getContext().getString(R.string.cheapest);
                                        } else {
                                            if (last2Index > 0) {
                                                mFlightOneListPojo.data.get(last2Index).tag = getString(R.string.fastest);
                                                previousIndex = last2Index;
                                                if (mFlightOneListPojo.data.get(0).tag.equalsIgnoreCase(getString(R.string.fastest))) {
                                                    mFlightOneListPojo.data.get(0).tag = "" + getString(R.string.cheapest);
                                                }
                                            }
                                        }


                                        if (previousIndex > 1) {
                                            FlightOneListPojo.Data mData = mFlightOneListPojo.data.get(previousIndex);
                                            mFlightOneListPojo.data.remove(previousIndex);
                                            mFlightOneListPojo.data.add(1, mData);
                                        }

                                        stopCountdown();
                                        startTimer(5 * 60 * 1000);
                                    }

                                    setFlightList();

                                } else if (mFlightOneListPojo.status.equalsIgnoreCase("404")) {

                                    mFlightOneAdapter.addAll(mFlightOneListPojo.data);
                                    Utils.showToast(getActivity(), "" + mFlightOneListPojo.message);
                                    mFlightOneListPojo = null;
                                } else {

                                    Utils.showToast(getActivity(), "" + mFlightOneListPojo.message);
                                    mFlightOneListPojo = null;
                                }
                        }
                    }
                }

                @Override
                public void onFailure(Call<FlightOneListPojo> call, Throwable t) {
                    LogUtils.e("", "onFailure: " + t.getMessage());
                    Utils.closeProgressDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error Too Large", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFlightList() {

        try {


//        List<FlightOneListPojo.Cities> mCitiesList = mFlightReturnListPojo.data.cities;
//        FlightOneListPojo.Fares mFares = mFlightReturnListPojo.data.fares;
//
//        if (mCitiesList != null && mCitiesList.size() > 0) {
            mFlightOneAdapter.addAll(mFlightOneListPojo.data);
//        List<String> mAirlineList = new ArrayList<>();

            mFlightCity.displayDepartDate = mStartCalDate.getTimeInMillis();
            mFlightCity.departDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());

            if (mFlightOneListPojo.data != null) {
//            for (int i = 0; i < mFlightReturnListPojo.data.size(); i++) {
//                if (mFlightReturnListPojo.data.get(i).cities != null) {
//                    if (!mAirlineList.contains(mFlightReturnListPojo.data.get(i).cities.get(i).airlineName)) {
//                        mAirlineList.add(mFlightReturnListPojo.data.get(i).cities.get(i).airlineName);
//                    }
//                }
//            }
                mAirlineList = mFlightOneListPojo.airlines;
                if (mAirlineList == null) {
                    mAirlineList = new ArrayList<>();
                }

                FlightOneListPojo.Airlines mAirlines = new FlightOneListPojo().new Airlines();
                mAirlines.code = "NS0";
                mAirlines.name = getString(R.string.non_stop);
                mAirlineList.add(0, mAirlines);

                if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
                    mAirlineList.clear();
                    mAirlineList.addAll(mTempAirlineList);
                    setFilterFlightList();
                }

                if (mPreferredAirlineList.size() == 0) {
                    mPreferredAirlineList.addAll(mAirlineList);
                }


                mAirlineOneFilterAdapter.addAll(mTempArlineFilterList);
                lnrFilter.setVisibility(View.VISIBLE);
            } else {
                lnrFilter.setVisibility(View.GONE);
            }
//            mFlightReturnDepartureAdapter.addAll(mCitiesList);
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFilterFlightList() {
        List<FlightOneListPojo.Data> mFlightList = new ArrayList<>();

        boolean isNonStop = false, isOptionSelected = false;
        List<String> mLineList = new ArrayList<>();
        for (int i = 0; i < mAirlineList.size(); i++) {
            if (mAirlineList.get(i).isSelected) {
                if (mAirlineList.get(i).code.equalsIgnoreCase("NS0")) {
                    isNonStop = true;
                    isOptionSelected = true;
                } else {
                    mLineList.add(mAirlineList.get(i).code);
                    isOptionSelected = true;
                }
            }
        }
        if (mFlightOneListPojo != null) {
            if (mFlightOneListPojo.data != null) {
                if (mFlightOneListPojo.data.size() > 0) {
                    for (int i = 0; i < mFlightOneListPojo.data.size(); i++) {
//                List<FlightOneListPojo.Cities> cities = mFlightOneListPojo.data.get(i).cities;
//                if (isNonStop && cities != null && cities.size() == 1) {
//                    mFlightList.add(mFlightOneListPojo.data.get(i));
//                    continue;
//                }

//

                        if (isNonStop && mLineList.size() == 0) {
                            List<FlightOneListPojo.Cities> cities = mFlightOneListPojo.data.get(i).cities;
                            if (cities != null && cities.size() == 1) {
                                mFlightList.add(mFlightOneListPojo.data.get(i));
                            }
                        } else if (isNonStop && mLineList.size() > 0) {
                            if (mLineList.contains(mFlightOneListPojo.data.get(i).cities.get(0).airline)) {
                                List<FlightOneListPojo.Cities> cities = mFlightOneListPojo.data.get(i).cities;
                                if (cities != null && cities.size() == 1) {
                                    mFlightList.add(mFlightOneListPojo.data.get(i));
                                }
                            }
                        } else {
                            if (mLineList.contains(mFlightOneListPojo.data.get(i).cities.get(0).airline)) {
                                mFlightList.add(mFlightOneListPojo.data.get(i));
                            }
                        }
                    }
                    if (!isOptionSelected) {
                        mFlightList.addAll(mFlightOneListPojo.data);
                    }

                    if (mFlightList.size() > 0) {
                        Utils.showToast(getActivity(), mFlightList.size() + " out of " + mFlightOneListPojo.data.size());
                    } else {
                        Utils.showToast(getActivity(), getString(R.string.msg_no_flight));
                    }

                    mFlightOneAdapter.addAll(mFlightList);

                }

            }
        }

//        if (isNonStop){
//            for (int i = 0; i < mAirlineList.size(); i++) {
//                if (!mAirlineList.get(i).code.equalsIgnoreCase("NS0")) {
//                    mAirlineList.get(i).isSelected = false;
//                }
//                LogUtils.e("", " mAirlineList code :"+mAirlineList.get(i).code);
//                LogUtils.e("", " mAirlineList name :"+mAirlineList.get(i).name);
//                LogUtils.e("", " mAirlineList isSelected :"+mAirlineList.get(i).isSelected);
//            }
//            LogUtils.e("", "mAirlineList inside size::"+mAirlineList.size());
//            mAirlineOneFilterAdapter.nonStopSelected();
//        }


    }

    @Override
    public void onClick(View view) {
        if (view == tvDate) {
            if (datePickerDialog != null) {
                datePickerDialog.show();
            }
        } else if (view == tvBack) {
            popBackStack();
        } else if (view == tvFilter) {
//            if (mFlightOneListPojo != null && mFlightOneListPojo.airlines != null) {
            try {
                    /*Bundle bundle = new Bundle();
                    bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
                    bundle.putString(Constants.BNDL_AIRLINE_TIME, mTimeSlot);
                    bundle.putString(Constants.BNDL_AIRLINE_LIST, new Gson().toJson(mPreferredAirlineList));
                    bundle.putString(Constants.BNDL_PREFERRED_AIRLINE_LIST, mPreferredFlight);*/
                /*    Intent intent = new Intent(getContext(),FlightFilterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                    intent.putExtra(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
//                    intent.putExtra(Constants.BNDL_AIRLINE_TIME, mTimeSlot);
//                    intent.putExtra(Constants.BNDL_AIRLINE_LIST, new Gson().toJson(mPreferredAirlineList));
//                    intent.putExtra(Constants.BNDL_PREFERRED_AIRLINE_LIST, mPreferredFlight);
                    startActivityForResult(intent, Constants.RC_ONE_WAY_FILTER);*/
                //IntentHandler.startActivityForResult(getActivity(), FlightFilterActivity.class, bundle, Constants.RC_ONE_WAY_FILTER);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final Dialog dialog = new Dialog(getActivity());
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.fragment_airline_filter);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


            TextView tvTitle;
            tvTitle = dialog.findViewById(R.id.tvTitle);
            TextView tvBack;
            tvBack = dialog.findViewById(R.id.tvBack);
            TextView tvMorning;
            tvMorning = dialog.findViewById(R.id.tvMorning);

            TextView tvDay;
            tvDay = dialog.findViewById(R.id.tvDay);

            TextView tvEvening;
            tvEvening = dialog.findViewById(R.id.tvEvening);

            TextView tvMoon;
            tvMoon = dialog.findViewById(R.id.tvMoon);

            TextView tvDepartureFrom;
            tvDepartureFrom = dialog.findViewById(R.id.tvDepartureFrom);

            TextView tvApplyFilters;
            tvApplyFilters = dialog.findViewById(R.id.tvApplyFilters);

            CheckBox cbRefundFlight;
            cbRefundFlight = dialog.findViewById(R.id.cbRefundFlight);

            CheckBox cbFreeMeal;
            cbFreeMeal = dialog.findViewById(R.id.cbFreeMeal);

            LinearLayout lnrRefundableFlight;
            lnrRefundableFlight = dialog.findViewById(R.id.lnrRefundableFlight);

            LinearLayout lnrFreeMeal;
            lnrFreeMeal = dialog.findViewById(R.id.lnrFreeMeal);
            ListView lvFlights;
            lvFlights = dialog.findViewById(R.id.lvFlights);

            if(refundableStatus==1){
                cbRefundFlight.setChecked(true);
            }else {
                cbRefundFlight.setChecked(false);
            }

            if (bookingTimeStatus == 1) {
                tvMorning.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (bookingTimeStatus == 2) {
                tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                tvDay.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (bookingTimeStatus == 3) {
                tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                tvEvening.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (bookingTimeStatus == 4) {
                tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                tvMoon.setBackgroundColor(getResources().getColor(R.color.gray_e1));
            } else {
                bookingTimeStatus = 0;
            }
            /* List<FlightOneListPojo.Airlines> mDialogAirlinesList=mPreferredAirlineList;*/

/*
                FlightCity mFlightCity;*/
            PreferedAirlineFilterAdapter mPreferedAirlineFilterAdapter;


            if (mFlightCity != null) {
                tvDepartureFrom.setText(getString(R.string.departure_from) + " " + mFlightCity.fromCity);
            }


            if (mTempArlineFilterList != null && mTempArlineFilterList.size() > 0) {
                for (int i = 0; i < mTempArlineFilterList.size(); i++) {
                    if (mTempArlineFilterList.get(i).code.equalsIgnoreCase("NS0")) {
                        mTempArlineFilterList.remove(i);
                        break;
                    }
                }

                for (int i = 0; i < mTempArlineFilterList.size(); i++) {
                    if (mPreferredFlight.contains(mTempArlineFilterList.get(i).code)) {
                        mTempArlineFilterList.get(i).isSelected = true;
                    } else {
                        mTempArlineFilterList.get(i).isSelected = false;
                    }
                }
            }
            mPreferedAirlineFilterAdapter = new PreferedAirlineFilterAdapter(getContext(), new PreferedAirlineFilterAdapter.OnAirlineClickListener() {
                @Override
                public void onAirlineClicked(int position, boolean isSelected) {
                    LogUtils.e("", "onAirlineClicked " + position + " " + isSelected);
                    mTempArlineFilterList.get(position).isSelected = isSelected;
                }
            });
            lvFlights.setAdapter(mPreferedAirlineFilterAdapter);
            mPreferedAirlineFilterAdapter.addAll(mTempArlineFilterList);

            tvBack.setOnClickListener(this);
            lnrRefundableFlight.setOnClickListener(this);
            lnrFreeMeal.setOnClickListener(this);
            tvApplyFilters.setOnClickListener(this);
            tvMorning.setOnClickListener(this);
            tvDay.setOnClickListener(this);
            tvEvening.setOnClickListener(this);
            tvMoon.setOnClickListener(this);

            if (mDialogTimeSlot.equals("1")) {
                mDialogTimeSlot = "0";
                tvMorning.performClick();
            } else if (mDialogTimeSlot.equals("2")) {
                mDialogTimeSlot = "0";
                tvDay.performClick();
            } else if (mDialogTimeSlot.equals("3")) {
                mDialogTimeSlot = "0";
                tvEvening.performClick();
            } else if (mDialogTimeSlot.equals("4")) {
                mDialogTimeSlot = "0";
                tvMoon.performClick();
            }

            tvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            lnrRefundableFlight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbRefundFlight.setChecked(!cbRefundFlight.isChecked());
                    if (cbRefundFlight.isChecked()) {
                        mDailogRefundableFlight = "1";
                        refundableStatus=1;
                    } else {
                        mDailogRefundableFlight = "0";
                        refundableStatus=0;
                    }
                }
            });
            lnrFreeMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbFreeMeal.setChecked(!cbFreeMeal.isChecked());
                }
            });
            tvApplyFilters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder strBuilder = new StringBuilder();
                    mPreferredFlight = "";
                    for (int i = 0; i < mTempArlineFilterList.size(); i++) {
                        if (mTempArlineFilterList.get(i).isSelected) {
                            if (strBuilder.length() > 0) {
                                strBuilder.append(", " + mTempArlineFilterList.get(i).code);
                            } else {
                                strBuilder.append(mTempArlineFilterList.get(i).code);
                            }
                        }
                    }
                    mPreferredFlight = strBuilder.toString();
//                        mAirlineList = mPreferredAirlineList;
                    mTimeSlot = mDialogTimeSlot;
                    mRefundableFlight = mDailogRefundableFlight;
                    getFlightList();
                    dialog.dismiss();
                }
            });
            tvMorning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getActivity(), "Status : "+bookingTimeStatus, Toast.LENGTH_SHORT).show();
                    if (bookingTimeStatus == 1) {
                        tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                        bookingTimeStatus = 0;
                    } else {
                        tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                        tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                        tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                        tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                        bookingTimeStatus = 1;
                        if (mDialogTimeSlot.equals("1")) {
                            mDialogTimeSlot = "0";
                        } else {
                            mDialogTimeSlot = "1";
                            tvMorning.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                        }
                    }
                }
            });
            tvDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bookingTimeStatus == 2) {
                        tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                        bookingTimeStatus = 0;
                    } else {
                        bookingTimeStatus = 2;
                        tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                        tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                        tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                        tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                        if (mDialogTimeSlot.equals("2")) {
                            mDialogTimeSlot = "0";
                        } else {
                            mDialogTimeSlot = "2";
                            tvDay.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                        }
                    }
                }
            });
            tvEvening.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bookingTimeStatus == 3) {
                        tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                        bookingTimeStatus = 0;
                    } else {
                        bookingTimeStatus = 3;
                        tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                        tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                        tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                        tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                        if (mDialogTimeSlot.equals("3")) {
                            mDialogTimeSlot = "0";
                        } else {
                            mDialogTimeSlot = "3";
                            tvEvening.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                        }
                    }
                }
            });
            tvMoon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bookingTimeStatus == 4) {
                        tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                        bookingTimeStatus = 0;
                    } else {
                        bookingTimeStatus = 4;
                        tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                        tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                        tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                        tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                        if (mDialogTimeSlot.equals("4")) {
                            mDialogTimeSlot = "0";
                        } else {
                            mDialogTimeSlot = "4";
                            tvMoon.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                        }
                    }
                }
            });


            dialog.show();

              /* intent.putExtra(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
               intent.putExtra(Constants.BNDL_AIRLINE_TIME, mTimeSlot);
               intent.putExtra(Constants.BNDL_AIRLINE_LIST, new Gson().toJson(mPreferredAirlineList));
               intent.putExtra(Constants.BNDL_PREFERRED_AIRLINE_LIST, mPreferredFlight);*/

            // IntentHandler.startActivity(getActivity(), FlightFiltersActivity.class);

             /*   Intent intent = new Intent(getContext(), FlightsFilters.class);
                getContext().startActivity(intent);
                getActivity().finish();*/
//            }else {
//                Toast.makeText(getContext(), "blank", Toast.LENGTH_SHORT).show();
//            }
        } else if (view == tvToday) {

            tvToday.setBackgroundResource(R.drawable.bg_button_tab);
            tvToday.setTextColor(getResources().getColor(R.color.white));
            tvTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
            tvDayAfterTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvDayAfterTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
            tvFourthDay.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvFourthDay.setTextColor(getResources().getColor(R.color.gray_4f));

            Calendar now = Calendar.getInstance();
            long mili = (long) tvToday.getTag();
            now.setTimeInMillis(mili);
//            LogUtils.e("", "now.get(Calendar.DATE)::" + DateFormate.sdfDateCompare.format(now.getTime()));
//            if (!DateFormate.sdfDateCompare.format(now.getTime()).equals(DateFormate.sdfDateCompare.format(mStartCalDate.getTime()))) {
            mStartCalDate.setTime(now.getTime());

            mYear = mStartCalDate.get(Calendar.YEAR);
            mMonth = mStartCalDate.get(Calendar.MONTH);
            mDay = mStartCalDate.get(Calendar.DAY_OF_MONTH);

            tvDetails.setText(getString(R.string.depart) + " " + DateFormate.sdfFlightDisplayDate.format(mStartCalDate.getTime()) + " | " + mFlightCity.traveller + " | " + mFlightCity.clasName);
            tvDate.setText("" + DateFormate.sdfCalDisplayDate.format(mStartCalDate.getTime()));

            mFlightCity.departDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());

            if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
                mTempAirlineList.clear();
            }
//            setDayStatus();
            getFlightList();
//            }

        } else if (view == tvTomorrow) {

            tvTomorrow.setBackgroundResource(R.drawable.bg_button_tab);
            tvTomorrow.setTextColor(getResources().getColor(R.color.white));
            tvToday.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvToday.setTextColor(getResources().getColor(R.color.gray_4f));
            tvDayAfterTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvDayAfterTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
            tvFourthDay.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvFourthDay.setTextColor(getResources().getColor(R.color.gray_4f));

            Calendar tomorrow = Calendar.getInstance();
            long mili = (long) tvTomorrow.getTag();
            tomorrow.setTimeInMillis(mili);
//            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
//            LogUtils.e("", "tomorrow.get(Calendar.DATE)::" + DateFormate.sdfDateCompare.format(tomorrow.getTime()));
//            if (!DateFormate.sdfDateCompare.format(tomorrow.getTime()).equals(DateFormate.sdfDateCompare.format(mStartCalDate.getTime()))) {
            mStartCalDate.setTime(tomorrow.getTime());

            mYear = mStartCalDate.get(Calendar.YEAR);
            mMonth = mStartCalDate.get(Calendar.MONTH);
            mDay = mStartCalDate.get(Calendar.DAY_OF_MONTH);

            tvDetails.setText(getString(R.string.depart) + " " + DateFormate.sdfFlightDisplayDate.format(mStartCalDate.getTime()) + " | " + mFlightCity.traveller + " | " + mFlightCity.clasName);
            tvDate.setText("" + DateFormate.sdfCalDisplayDate.format(mStartCalDate.getTime()));

            mFlightCity.departDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());

            if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
                mTempAirlineList.clear();
            }
//            setDayStatus();
            getFlightList();
//            }

        } else if (view == tvDayAfterTomorrow) {

            tvDayAfterTomorrow.setBackgroundResource(R.drawable.bg_button_tab);
            tvDayAfterTomorrow.setTextColor(getResources().getColor(R.color.white));
            tvToday.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvToday.setTextColor(getResources().getColor(R.color.gray_4f));
            tvTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
            tvFourthDay.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvFourthDay.setTextColor(getResources().getColor(R.color.gray_4f));
//
            Calendar dayAfterTomorrow = Calendar.getInstance();
            long mili = (long) tvDayAfterTomorrow.getTag();
            dayAfterTomorrow.setTimeInMillis(mili);
//            dayAfterTomorrow.add(Calendar.DAY_OF_MONTH, 2);
//            LogUtils.e("", "dayAfterTomorrow.get(Calendar.DATE)::" + DateFormate.sdfDateCompare.format(dayAfterTomorrow.getTime()));
//            if (!DateFormate.sdfDateCompare.format(dayAfterTomorrow.getTime()).equals(DateFormate.sdfDateCompare.format(mStartCalDate.getTime()))) {
            mStartCalDate.setTime(dayAfterTomorrow.getTime());

            mYear = mStartCalDate.get(Calendar.YEAR);
            mMonth = mStartCalDate.get(Calendar.MONTH);
            mDay = mStartCalDate.get(Calendar.DAY_OF_MONTH);

            tvDetails.setText(getString(R.string.depart) + " " + DateFormate.sdfFlightDisplayDate.format(mStartCalDate.getTime()) + " | " + mFlightCity.traveller + " | " + mFlightCity.clasName);
            tvDate.setText("" + DateFormate.sdfCalDisplayDate.format(mStartCalDate.getTime()));

            mFlightCity.departDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());

            if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
                mTempAirlineList.clear();
            }
//            setDayStatus();
            getFlightList();
//        }
        } else if (view == tvFourthDay) {

            tvFourthDay.setBackgroundResource(R.drawable.bg_button_tab);
            tvFourthDay.setTextColor(getResources().getColor(R.color.white));
            tvToday.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvToday.setTextColor(getResources().getColor(R.color.gray_4f));
            tvDayAfterTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvDayAfterTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
            tvTomorrow.setBackgroundResource(R.drawable.bg_button_tab_unselected);
            tvTomorrow.setTextColor(getResources().getColor(R.color.gray_4f));
//
            Calendar dayForth = Calendar.getInstance();
            long mili = (long) tvFourthDay.getTag();
            dayForth.setTimeInMillis(mili);
//            dayAfterTomorrow.add(Calendar.DAY_OF_MONTH, 2);
//            LogUtils.e("", "dayAfterTomorrow.get(Calendar.DATE)::" + DateFormate.sdfDateCompare.format(dayAfterTomorrow.getTime()));
//            if (!DateFormate.sdfDateCompare.format(dayAfterTomorrow.getTime()).equals(DateFormate.sdfDateCompare.format(mStartCalDate.getTime()))) {
            mStartCalDate.setTime(dayForth.getTime());

            mYear = mStartCalDate.get(Calendar.YEAR);
            mMonth = mStartCalDate.get(Calendar.MONTH);
            mDay = mStartCalDate.get(Calendar.DAY_OF_MONTH);

            tvDetails.setText(getString(R.string.depart) + " " + DateFormate.sdfFlightDisplayDate.format(mStartCalDate.getTime()) + " | " + mFlightCity.traveller + " | " + mFlightCity.clasName);
            tvDate.setText("" + DateFormate.sdfCalDisplayDate.format(mStartCalDate.getTime()));

            mFlightCity.departDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());

            if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
                mTempAirlineList.clear();
            }
//            setDayStatus();
            getFlightList();
//        }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_ONE_WAY_FILTER && resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                mTimeSlot = bundle.getString(Constants.BNDL_AIRLINE_TIME, "0");
                mRefundableFlight = bundle.getString(Constants.BNDL_AIRLINE_REFUNDABLE, "0");
                mPreferredFlight = bundle.getString(Constants.BNDL_PREFERRED_AIRLINE_LIST, "");
                if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
                    mTempAirlineList.clear();
                }
                getFlightList();
            }
        }
    }
}
