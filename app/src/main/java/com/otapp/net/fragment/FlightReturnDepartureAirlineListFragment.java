package com.otapp.net.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.FlightFilterActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.AirlineReturnFilterAdapter;
import com.otapp.net.adapter.FlightReturnDepartureAdapter;
import com.otapp.net.adapter.PreferedAirlineFilterAdapter;
import com.otapp.net.adapter.PreferedReturnArlineFilterAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.model.FlightReturnListPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightReturnDepartureAirlineListFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_FlightReturnDepartureAirlineListFragment = "Tag_" + "FlightReturnDepartureAirlineListFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDetails)
    TextView tvDetails;
    //    @BindView(R.id.tvDeparture)
//    TextView tvDeparture;
//    @BindView(R.id.tvDestination)
//    TextView tvDestination;
//    @BindView(R.id.tvDateTime)
//    TextView tvDateTime;
//    @BindView(R.id.tvDestinationDate)
//    TextView tvDestinationDate;
    @BindView(R.id.lnrDate)
    LinearLayout lnrDate;
    @BindView(R.id.tvFilter)
    TextView tvFilter;
    //    @BindView(R.id.tvDeparturePrice)
//    TextView tvDeparturePrice;
//    @BindView(R.id.tvReturnPrice)
//    TextView tvReturnPrice;
//    @BindView(R.id.tvTotalPrice)
//    TextView tvTotalPrice;
//    @BindView(R.id.lnrBook)
//    TextView lnrBook;
    @BindView(R.id.lnrFilter)
    LinearLayout lnrFilter;
    //    @BindView(R.id.lnrPrice)
//    LinearLayout lnrPrice;
    @BindView(R.id.lvDepartureAirline)
    ListView lvDepartureAirline;
    //    @BindView(R.id.lvReturnAirline)
//    ListView lvReturnAirline;
    @BindView(R.id.rvFilters)
    RecyclerView rvFilters;

    FlightCity mFlightCity;
    List<FlightCityPojo.City> mFlightCityList;
    FlightReturnListPojo mFlightReturnListPojo;

    FlightReturnDepartureAdapter mFlightReturnDepartureAdapter;
    //    FlightReturnDestinationAdapter mFlightReturnDestinationAdapter;
    AirlineReturnFilterAdapter mAirlineReturnFilterAdapter;

    private int mPosition = -1;

    List<FlightReturnListPojo.Airlines> mAirlineList;
    List<FlightReturnListPojo.Airlines> mTempAirlineList = new ArrayList<>();
    List<FlightReturnListPojo.Airlines> mPreferredAirlineList = new ArrayList<>();
    List<FlightReturnListPojo.Airlines> mTempArlineFilterList= new ArrayList<>();

    private CountDownTimer countDownTimer;

    private String mTimeSlot = "0", mPreferredFlight = "", mCurrency = "", mRefundableFlight = "0";

    String mDialogTimeSlot = "0", mDialogPreferredFlight = "", mDailogRefundableFlight = "0";
    int bookingTimeStatus=0;


    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;
    Calendar mStartCalDate = Calendar.getInstance();
    Calendar mEndCalDate = Calendar.getInstance();
//    DatePickerDialog dateStartPickerDialog, dateEndPickerDialog;

    public static FlightReturnDepartureAirlineListFragment newInstance() {
        FlightReturnDepartureAirlineListFragment fragment = new FlightReturnDepartureAirlineListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_return_departure_flight_list, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        mFlightReturnDepartureAdapter = new FlightReturnDepartureAdapter(getActivity(), new FlightReturnDepartureAdapter.OnFlightSelectListener() {
            @Override
            public void onFlightSelected(FlightReturnListPojo.Data mFlightData) {

                MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, 0l);

                Bundle bundle = new Bundle();

                MyPref.setPref(getContext(),Constants.BNDL_FLIGHT,new Gson().toJson(mFlightCity));
                MyPref.setPref(getContext(),Constants.BNDL_CITY_LIST, new Gson().toJson(mFlightCityList));
                MyPref.setPref(getContext(),Constants.CITY_TYPE_POSITION, mPosition);
//                Toast.makeText(getActivity(), "Adapter : "+mPosition, Toast.LENGTH_SHORT).show();
                MyPref.setPref(getContext(),Constants.BNDL_FLIGHT_NAME_LIST, new Gson().toJson(mFlightReturnListPojo.airlines));
                MyPref.setPref(getContext(),Constants.BNDL_FLIGHT_RETURN_DETAILS, new Gson().toJson(mFlightData));
//                bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
//                bundle.putString(Constants.BNDL_FLIGHT_NAME_LIST, new Gson().toJson(mFlightReturnListPojo.airlines));
//                bundle.putString(Constants.BNDL_CITY_LIST, new Gson().toJson(mFlightCityList));
//                bundle.putString(Constants.BNDL_FLIGHT_RETURN_DETAILS, new Gson().toJson(mFlightData));
//                bundle.putInt(Constants.CITY_TYPE_POSITION, mPosition);
                switchFragment(FlightReturnSelectionFragment.newInstance(), FlightReturnSelectionFragment.Tag_FlightReturnSelectionFragment, bundle);


//                for (int i = 0; i < mFlightReturnListPojo.data.depature.size(); i++) {
//                    if (mFlightReturnListPojo.data.depature.get(i).uid.equals(mFlightData.uid)) {
//                        mFlightReturnListPojo.data.depature.get(i).isSelected = true;
//                    } else {
//                        mFlightReturnListPojo.data.depature.get(i).isSelected = false;
//                    }
//                }
//                setFilterFlightList(false);
            }
        });

        lvDepartureAirline.setAdapter(mFlightReturnDepartureAdapter);

        lvDepartureAirline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });

//        mFlightReturnDestinationAdapter = new FlightReturnDestinationAdapter(getActivity(), new FlightReturnDestinationAdapter.OnFlightSelectListener() {
//            @Override
//            public void onFlightSelected(FlightReturnListPojo.Return mFlightData) {
//
//                for (int i = 0; i < mFlightReturnListPojo.data.returnFlight.size(); i++) {
//                    if (mFlightReturnListPojo.data.returnFlight.get(i).uid.equals(mFlightData.uid)) {
//                        mFlightReturnListPojo.data.returnFlight.get(i).isSelected = true;
//                    } else {
//                        mFlightReturnListPojo.data.returnFlight.get(i).isSelected = false;
//                    }
//                }
//                setFilterFlightList(false);
//            }
//        });
//        lvReturnAirline.setAdapter(mFlightReturnDestinationAdapter);

        mAirlineReturnFilterAdapter = new AirlineReturnFilterAdapter(getActivity(), new AirlineReturnFilterAdapter.OnAirlineClickListener() {
            @Override
            public void onAirlineClicked(List<FlightReturnListPojo.Airlines> mAirline) {
                mAirlineList = mAirline;
                if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
                    mTempAirlineList.clear();
                }
                mTempAirlineList.addAll(mAirlineList);
                setFilterFlightList(true);
            }
        });
        rvFilters.setAdapter(mAirlineReturnFilterAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String sFlightCity=MyPref.getPref(getContext(),Constants.BNDL_FLIGHT,"");
            mFlightCity = new Gson().fromJson(sFlightCity, FlightCity.class);


            String sFlightCityList=MyPref.getPref(getContext(),Constants.BNDL_CITY_LIST,"");
            mFlightCityList = new Gson().fromJson(sFlightCityList, new TypeToken<ArrayList<FlightCityPojo.City>>() {
            }.getType());


            mPosition = MyPref.getPref(getContext(), Constants.CITY_TYPE_POSITION, 0);
//            mPosition = bundle.getInt(Constants.CITY_TYPE_POSITION);
//            Toast.makeText(getActivity(), "D : "+mPosition, Toast.LENGTH_SHORT).show();
            if (mFlightCity != null) {
                try {
                    tvTitle.setText(mFlightCity.fromCity + " " + getString(R.string.to_) + " " + mFlightCity.toCity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                tvDeparture.setText(getString(R.string.departure) + "\n" + mFlightCity.from + " - " + mFlightCity.to);
//                tvDestination.setText(getString(R.string.retrn) + "\n" + mFlightCity.to + " - " + mFlightCity.from);

                mStartCalDate.setTimeInMillis(mFlightCity.displayDepartDate);
                mEndCalDate.setTimeInMillis(mFlightCity.displayReturnDate);

                mStartYear = mStartCalDate.get(Calendar.YEAR);
                mStartMonth = mStartCalDate.get(Calendar.MONTH);
                mStartDay = mStartCalDate.get(Calendar.DAY_OF_MONTH);

                mEndYear = mEndCalDate.get(Calendar.YEAR);
                mEndMonth = mEndCalDate.get(Calendar.MONTH);
                mEndDay = mEndCalDate.get(Calendar.DAY_OF_MONTH);
                try{
                tvDetails.setText(getString(R.string.depart) + " " + DateFormate.sdfFlightDisplayDate.format(mStartCalDate.getTime()) + " - " + getString(R.string.retrn) + " " + DateFormate.sdfFlightDisplayDate.format(mEndCalDate.getTime()) + " | " + mFlightCity.traveller + " | " + mFlightCity.clasName);
            }catch (Exception e){
                    e.printStackTrace();
                }
//                tvDateTime.setText("" + DateFormate.sdfCalDisplayDate.format(mStartCalDate.getTime()));
//                tvDestinationDate.setText("" + DateFormate.sdfCalDisplayDate.format(mEndCalDate.getTime()));

                getFlightList();
            }
        }

//        dateStartPickerDialog = new DatePickerDialog(getActivity(),
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//
//                        mStartCalDate.set(Calendar.YEAR, year);
//                        mStartCalDate.set(Calendar.MONTH, monthOfYear);
//                        mStartCalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                        // end date
//                        mEndCalDate.set(Calendar.YEAR, year);
//                        mEndCalDate.set(Calendar.MONTH, monthOfYear);
//                        mEndCalDate.set(Calendar.DAY_OF_MONTH, (dayOfMonth + 1));
//
//                        mEndYear = mEndCalDate.get(Calendar.YEAR);
//                        mEndMonth = mEndCalDate.get(Calendar.MONTH);
//                        mEndDay = mEndCalDate.get(Calendar.DAY_OF_MONTH);
//
//                        Calendar mStartCal = Calendar.getInstance();
//                        mStartCal.setTime(mStartCalDate.getTime());
//                        dateEndPickerDialog.getDatePicker().setMinDate(mStartCal.getTimeInMillis() - 10000);
//                        mStartCal.add(Calendar.MONTH, 6);
//                        dateEndPickerDialog.getDatePicker().setMaxDate(mStartCal.getTimeInMillis() - 10000);
//
//                        if (dateEndPickerDialog != null) {
//                            dateEndPickerDialog.show();
//                        }
//
////                        getFlightList();
//
//                    }
//                }, mStartYear, mStartMonth, mStartDay);
//
//        dateEndPickerDialog = new DatePickerDialog(getActivity(),
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//
//                        mEndCalDate.set(Calendar.YEAR, year);
//                        mEndCalDate.set(Calendar.MONTH, monthOfYear);
//                        mEndCalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                        if (mStartCalDate.after(mEndCalDate)) {
//                            Utils.showToast(getActivity(), getString(R.string.alert_valid_date));
//                            return;
//                        }
//
//                        tvDetails.setText(getString(R.string.depart) + " " + DateFormate.sdfFlightDisplayDate.format(mStartCalDate.getTime()) + " - " + getString(R.string.retrn) + " " + DateFormate.sdfFlightDisplayDate.format(mEndCalDate.getTime()) + " | " + mFlightCity.traveller + " | " + mFlightCity.clasName);
////                        tvDateTime.setText("" + DateFormate.sdfCalDisplayDate.format(mStartCalDate.getTime()));
//                        mFlightCity.departDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
//
////                        tvDestinationDate.setText("" + DateFormate.sdfCalDisplayDate.format(mEndCalDate.getTime()));
//                        mFlightCity.returnDate = DateFormate.sdfFlightServerDate.format(mEndCalDate.getTime());
//
//                        getFlightList();
//
//                    }
//                }, mEndYear, mEndMonth, mEndDay);
//
//        dateStartPickerDialog.setTitle("" + getString(R.string.departure_date));
//        dateEndPickerDialog.setTitle("" + getString(R.string.return_date));
//
//        Calendar mStartCal = Calendar.getInstance();
//        dateStartPickerDialog.getDatePicker().setMinDate(mStartCal.getTimeInMillis() - 10000);
//        dateEndPickerDialog.getDatePicker().setMinDate(mStartCal.getTimeInMillis() - 10000);
//        mStartCal.add(Calendar.MONTH, 6); // 180days
//        dateStartPickerDialog.getDatePicker().setMaxDate(mStartCal.getTimeInMillis() - 10000);
//        dateEndPickerDialog.getDatePicker().setMaxDate(mStartCal.getTimeInMillis() - 10000);

        tvBack.setOnClickListener(this);
        lnrDate.setOnClickListener(this);
        tvFilter.setOnClickListener(this);
//        lnrBook.setOnClickListener(this);

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
//                Utils.showToast(getActivity(), getString(R.string.msg_session_expire));
                if (getActivity() != null) {
                    try {
                        Utils.showToast(getActivity(), getString(R.string.msg_refresh_list));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    getFlightList();
                }

            }
        }.start();

    }

    private void getFlightList() {

        if (getActivity() == null) {
            return;
        }

        if (!Utils.isInternetConnected(getActivity())) {
            try {
                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }


        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<FlightReturnListPojo> mCall = mApiInterface.getReturnFlightList(mFlightCity.from, mFlightCity.to, mFlightCity.departDate, mFlightCity.returnDate, "" + mFlightCity.adultCount, "" + mFlightCity.childCount, "" + mFlightCity.infantCount, mFlightCity.clas, mFlightCity.currencyName, mTimeSlot, mRefundableFlight, mPreferredFlight, Otapp.mUniqueID, mFlightCity.flightAuthToken);
        mCall.enqueue(new Callback<FlightReturnListPojo>() {
            @Override
            public void onResponse(Call<FlightReturnListPojo> call, Response<FlightReturnListPojo> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    mFlightReturnListPojo = response.body();
                    if (mFlightReturnListPojo != null) {
                        if (mFlightReturnListPojo.status.equalsIgnoreCase("200")) {

                            if (mFlightReturnListPojo.data != null && mFlightReturnListPojo.data.size() > 0) {

                                if(mTempArlineFilterList.size()==0){
                                    mTempArlineFilterList=mFlightReturnListPojo.airlines;
                                }

                                int previousIndex = -1, last2Index = -1, minimalDuration = 0;
                                for (int i = 0; i < mFlightReturnListPojo.data.size(); i++) {
                                    if (mFlightReturnListPojo.data.get(i).cities.get(0).get(0).size() == 1 && mFlightReturnListPojo.data.get(i).cities.get(1).get(0).size() == 1) {

                                        int mDuration = 0;
                                        for (int j = 0; j < mFlightReturnListPojo.data.get(i).cities.get(0).get(0).size(); j++) {
                                            mDuration = mDuration + mFlightReturnListPojo.data.get(i).cities.get(0).get(0).get(j).duration;
                                        }

                                        for (int j = 0; j < mFlightReturnListPojo.data.get(i).cities.get(1).get(0).size(); j++) {
                                            mDuration = mDuration + mFlightReturnListPojo.data.get(i).cities.get(1).get(0).get(j).duration;
                                        }

                                        if (minimalDuration == 0) {


                                            minimalDuration = mDuration;
//                                            minimalDuration = (mFlightReturnListPojo.data.get(i).cities.get(0).get(0).get(0).duration
//                                                    + mFlightReturnListPojo.data.get(i).cities.get(1).get(0).get(0).duration);
                                            previousIndex = i;
                                            try {
                                                mFlightReturnListPojo.data.get(i).tag = getContext().getString(R.string.fastest);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        } else if (minimalDuration > mDuration) {
                                            mFlightReturnListPojo.data.get(previousIndex).tag = "";
                                            last2Index = previousIndex;
                                            previousIndex = i;
                                            try {
                                                mFlightReturnListPojo.data.get(i).tag = getString(R.string.fastest);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                                if (TextUtils.isEmpty(mFlightReturnListPojo.data.get(0).tag)) {
                                    try {
                                        mFlightReturnListPojo.data.get(0).tag = "" + getString(R.string.cheapest);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    if (last2Index > 0) {
                                        try {
                                            mFlightReturnListPojo.data.get(last2Index).tag = getString(R.string.fastest);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        previousIndex = last2Index;
                                        if (mFlightReturnListPojo.data.get(0).tag.equalsIgnoreCase(getString(R.string.fastest))) {
                                            try {
                                                mFlightReturnListPojo.data.get(0).tag = "" + getString(R.string.cheapest);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }


                                if (previousIndex > 1) {
                                    FlightReturnListPojo.Data mData = mFlightReturnListPojo.data.get(previousIndex);
                                    mFlightReturnListPojo.data.remove(previousIndex);
                                    mFlightReturnListPojo.data.add(1, mData);
                                }

                                stopCountdown();
                                startTimer(5 * 60 * 1000);
                            }

                            setFlightList();
//                            mLine.setVisibility(View.VISIBLE);
//                            lnrPrice.setVisibility(View.VISIBLE);

                        } else if (mFlightReturnListPojo.status.equalsIgnoreCase("404")) {

                            Utils.showToast(getActivity(), "" + mFlightReturnListPojo.message);
                            mFlightReturnDepartureAdapter.addAll(null);
//                            mFlightReturnDestinationAdapter.addAll(null);
                            mFlightReturnListPojo = null;
//                            mLine.setVisibility(View.GONE);
//                            lnrPrice.setVisibility(View.GONE);

                        } else {

                            Utils.showToast(getActivity(), "" + mFlightReturnListPojo.message);
                            mFlightReturnListPojo = null;
//                            mLine.setVisibility(View.GONE);
//                            lnrPrice.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FlightReturnListPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void setFlightList() {
        mFlightReturnDepartureAdapter.addAll(mFlightReturnListPojo.data);
//        List<String> mAirlineList = new ArrayList<>();
        if (mFlightReturnListPojo.data != null) {
//            for (int i = 0; i < mFlightReturnListPojo.data.size(); i++) {
//                if (mFlightReturnListPojo.data.get(i).cities != null) {
//                    if (!mAirlineList.contains(mFlightReturnListPojo.data.get(i).cities.get(i).airlineName)) {
//                        mAirlineList.add(mFlightReturnListPojo.data.get(i).cities.get(i).airlineName);
//                    }
//                }
//            }
            mAirlineList = mFlightReturnListPojo.airlines;
            if (mAirlineList == null) {
                mAirlineList = new ArrayList<>();
            }

            FlightReturnListPojo.Airlines mAirlines = new FlightReturnListPojo().new Airlines();
            mAirlines.code = "NS0";
            try {
                mAirlines.name = getString(R.string.non_stop);
            }catch (Exception e){
                e.printStackTrace();
            }
            mAirlineList.add(0, mAirlines);

            if (mTempAirlineList != null && mTempAirlineList.size() > 0) {
                mAirlineList.clear();
                mAirlineList.addAll(mTempAirlineList);
                setFilterFlightList(true);
            }

            if (mPreferredAirlineList.size() == 0) {
                mPreferredAirlineList.addAll(mAirlineList);
            }

            mAirlineReturnFilterAdapter.addAll(mTempArlineFilterList);
            lnrFilter.setVisibility(View.VISIBLE);
        } else {
            lnrFilter.setVisibility(View.GONE);
        }
    }

    private void setFilterFlightList(boolean isShowToast) {

        List<FlightReturnListPojo.Data> mFlightList = new ArrayList<>();

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

        if (mFlightReturnListPojo != null) {
            if (mFlightReturnListPojo.data != null && mFlightReturnListPojo.data.size() > 0) {
                for (int i = 0; i < mFlightReturnListPojo.data.size(); i++) {

//                if (isNonStop && mFlightReturnListPojo.data.get(i).cities.get(0).get(0).size() == 1 && mFlightReturnListPojo.data.get(i).cities.get(1).get(0).size() == 1) {
//                    mFlightList.add(mFlightReturnListPojo.data.get(i));
//                    continue;
//                }

//                if (mLineList != null && mLineList.size() > 0) {
                    List<FlightReturnListPojo.AirlineTitles> airlineTItles = mFlightReturnListPojo.data.get(i).airlineTItles;
                    for (int j = 0; j < airlineTItles.size(); j++) {

                        if (isNonStop && mLineList.size() == 0) {
                            if (mFlightReturnListPojo.data.get(i).cities.get(0).get(0).size() == 1 && mFlightReturnListPojo.data.get(i).cities.get(1).get(0).size() == 1) {
                                mFlightList.add(mFlightReturnListPojo.data.get(i));
                            }
                        } else if (isNonStop && mLineList.size() > 0) {
                            if (mLineList.contains(airlineTItles.get(j).code)) {
                                if (mFlightReturnListPojo.data.get(i).cities.get(0).get(0).size() == 1 && mFlightReturnListPojo.data.get(i).cities.get(1).get(0).size() == 1) {
                                    mFlightList.add(mFlightReturnListPojo.data.get(i));
                                }
                            }
                        } else {
                            if (mLineList.contains(airlineTItles.get(j).code)) {
                                mFlightList.add(mFlightReturnListPojo.data.get(i));
                            }
                        }
                    }
                    if (!isOptionSelected) {
                        mFlightList.addAll(mFlightReturnListPojo.data);
                    }

                    if (mFlightList.size() > 0) {
                     //   Utils.showToast(getActivity(), mFlightList.size() + " out of " + mFlightReturnListPojo.data.size());
                    } else {
                       // Utils.showToast(getActivity(), getString(R.string.msg_no_flight_return));
                    }

                    mFlightReturnDepartureAdapter.addAll(mFlightList);
//                }

                }
            }
        }



    }

    @Override
    public void onClick(View view) {
//        if (view == lnrDate) {
//            if (dateStartPickerDialog != null) {
//                dateStartPickerDialog.show();
//            }
//        } else
        if (view == tvBack) {
            popBackStack();
        } else if (view == tvFilter) {

            /*if (mFlightReturnListPojo != null && mFlightReturnListPojo.airlines != null) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
                bundle.putString(Constants.BNDL_AIRLINE_TIME, mTimeSlot);
                bundle.putString(Constants.BNDL_AIRLINE_LIST, new Gson().toJson(mPreferredAirlineList));
                bundle.putString(Constants.BNDL_PREFERRED_AIRLINE_LIST, mPreferredFlight);
                IntentHandler.startActivityForResult(getActivity(), FlightFilterActivity.class, bundle, Constants.RC_RETURN_FILTER);
            }*/
            //if (mFlightReturnListPojo != null && mFlightReturnListPojo.airlines != null) {
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

                List<FlightReturnListPojo.Airlines> mDialogAirlinesList = mPreferredAirlineList;
            if(bookingTimeStatus==1){
                tvMorning.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
            }else if(bookingTimeStatus==2){
                tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                tvDay.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
            }else if(bookingTimeStatus==3){
                tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                tvEvening.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
            }else if(bookingTimeStatus==4){
                tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                tvMoon.setBackgroundColor(getResources().getColor(R.color.gray_e1));
            }else {
                bookingTimeStatus=0;
            }
/*
                FlightCity mFlightCity;*/
                PreferedReturnArlineFilterAdapter mPreferedRetrunAirlineFilterAdapter;


                if (mFlightCity != null) {
                    try {
                        tvDepartureFrom.setText(getString(R.string.departure_from) + " " + mFlightCity.fromCity);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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
                mPreferedRetrunAirlineFilterAdapter = new PreferedReturnArlineFilterAdapter(getContext(), new PreferedReturnArlineFilterAdapter.OnAirlineClickListener() {
                    @Override
                    public void onAirlineClicked(int position, boolean isSelected) {
                        LogUtils.e("", "onAirlineClicked " + position + " " + isSelected);
                        mTempArlineFilterList.get(position).isSelected = isSelected;
                    }
                });
                lvFlights.setAdapter(mPreferedRetrunAirlineFilterAdapter);
                mPreferedRetrunAirlineFilterAdapter.addAll(mTempArlineFilterList);

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
                        } else {
                            mDailogRefundableFlight = "0";
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
                        //mAirlineList = mPreferredAirlineList;
                        mTimeSlot = mDialogTimeSlot;
                        mRefundableFlight = mDailogRefundableFlight;
                        getFlightList();
                        dialog.dismiss();
                    }
                });
                tvMorning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bookingTimeStatus == 1) {
                            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                            bookingTimeStatus = 0;
                        } else {
                            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                            tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                            tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                            tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                            bookingTimeStatus=1;
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
                            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                            bookingTimeStatus = 0;
                        } else {
                            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                            tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                            tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                            tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                            bookingTimeStatus = 2;
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
                            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                            bookingTimeStatus = 0;
                        } else {
                            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                            tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                            tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                            tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                            bookingTimeStatus=3;
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
                            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                            bookingTimeStatus = 0;
                        } else {
                            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                            tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                            tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                            tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                            bookingTimeStatus=4;
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
            }


        }
//        else if (view == lnrBook) {
//
//            FlightReturnListPojo.Depature mFlightDepature = null;
//            FlightReturnListPojo.Return mFlightReturn = null;
//
//            if (mFlightReturnListPojo.data != null) {
//
//                if (mFlightReturnListPojo.data.depature != null && mFlightReturnListPojo.data.depature.size() > 0) {
//                    for (int i = 0; i < mFlightReturnListPojo.data.depature.size(); i++) {
//                        if (mFlightReturnListPojo.data.depature.get(i).isSelected) {
//                            mFlightDepature = mFlightReturnListPojo.data.depature.get(i);
//                            break;
//                        }
//                    }
//                }
//
//                if (mFlightReturnListPojo.data.returnFlight != null && mFlightReturnListPojo.data.returnFlight.size() > 0) {
//                    for (int i = 0; i < mFlightReturnListPojo.data.returnFlight.size(); i++) {
//                        if (mFlightReturnListPojo.data.returnFlight.get(i).isSelected) {
//                            mFlightReturn = mFlightReturnListPojo.data.returnFlight.get(i);
//                            break;
//                        }
//                    }
//                }
//
//            }
//
//            if (mFlightDepature == null) {
//                Utils.showToast(getActivity(), getString(R.string.alert_departure_flight));
//                return;
//            }
//            if (mFlightReturn == null) {
//                Utils.showToast(getActivity(), getString(R.string.alert_return_flight));
//                return;
//            }
//
//            Bundle bundle = new Bundle();
//            bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
//            bundle.putString(Constants.BNDL_FLIGHT_DETAILS_DEPARTURE, new Gson().toJson(mFlightDepature));
//            bundle.putString(Constants.BNDL_FLIGHT_RETURN_DETAILS, new Gson().toJson(mFlightReturn));
//            bundle.putInt(Constants.CITY_TYPE_POSITION, mPosition);
//            switchFragment(FlightOneDetailsFragment.newInstance(), FlightOneDetailsFragment.Tag_FlightOneDetailsFragment, bundle);
//
//        }

    //}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_RETURN_FILTER && resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
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
