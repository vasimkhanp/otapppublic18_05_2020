package com.otapp.net.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.FlightFilterActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.AirlineMultiCityFilterAdapter;
import com.otapp.net.adapter.FlightMultiCityAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightMultiCityPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightMultiCityAirlineListFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_FlightMultiCityAirlineListFragment = "Tag_" + "FlightMultiCityAirlineListFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDetails)
    TextView tvDetails;
    @BindView(R.id.tvFilter)
    TextView tvFilter;
    @BindView(R.id.lnrFilter)
    LinearLayout lnrFilter;
    @BindView(R.id.lvAirline)
    ListView lvAirline;
    @BindView(R.id.rvFilters)
    RecyclerView rvFilters;

    List<FlightCity> mFlightCityList;

    FlightCity mFlightCity;
    FlightMultiCityPojo mFlightMultiCityPojo;

    List<FlightMultiCityPojo.Airlines> mAirlineList;
    List<FlightMultiCityPojo.Airlines> mPreferredAirlineList = new ArrayList<>();

    FlightMultiCityAdapter mFlightMultiCityAdapter;
    AirlineMultiCityFilterAdapter mAirlineMultiCityFilterAdapter;

    private String mTimeSlot = "0", mPreferredFlight = "", mJourney = "";

    public static FlightMultiCityAirlineListFragment newInstance() {
        FlightMultiCityAirlineListFragment fragment = new FlightMultiCityAirlineListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_multicity_flight_list, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        mFlightMultiCityAdapter = new FlightMultiCityAdapter(getActivity(), new FlightMultiCityAdapter.OnFlightSelectListener() {
            @Override
            public void onFlightSelected(FlightMultiCityPojo.Data mFlightData) {

                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
                bundle.putString(Constants.BNDL_FLIGHT_ONE_DETAILS, new Gson().toJson(mFlightData));
                switchFragment(FlightOneDetailsFragment.newInstance(), FlightOneDetailsFragment.Tag_FlightOneDetailsFragment, bundle);
            }
        });
        lvAirline.setAdapter(mFlightMultiCityAdapter);

        mAirlineMultiCityFilterAdapter = new AirlineMultiCityFilterAdapter(getActivity(), new AirlineMultiCityFilterAdapter.OnAirlineClickListener() {
            @Override
            public void onAirlineClicked(List<FlightMultiCityPojo.Airlines> mAirline) {
                mAirlineList = mAirline;
                setFilterFlightList();
            }
        });
        rvFilters.setAdapter(mAirlineMultiCityFilterAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String sFlightCity= MyPref.getPref(getContext(),Constants.BNDL_FLIGHT,"");
            mFlightCity = new Gson().fromJson(sFlightCity, FlightCity.class);


            if (mFlightCity != null) {


                mFlightCityList = mFlightCity.mFlightCityList;

                Calendar mStartCalDate = Calendar.getInstance();
                JSONArray mJsonArray = new JSONArray();
                try {
                    if (mFlightCityList != null && mFlightCityList.size() > 0) {
                        for (int i = 0; i < mFlightCityList.size(); i++) {
                            JSONObject mJsonObject = new JSONObject();
                            mJsonObject.put("from", mFlightCityList.get(i).from);
                            mJsonObject.put("to", mFlightCityList.get(i).to);
                            mJsonObject.put("date", mFlightCityList.get(i).departDate);
                            mJsonArray.put(mJsonObject);
                            if (i == 0) {
                                mStartCalDate.setTimeInMillis(mFlightCityList.get(i).displayDepartDate);
                            }
                        }
                        tvTitle.setText(mFlightCityList.get(0).fromCity + " " + getString(R.string.to_) + " " + mFlightCityList.get(mFlightCityList.size() - 1).toCity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (mJsonArray != null && mJsonArray.length() > 0) {
                    mJourney = mJsonArray.toString();
                    LogUtils.e("", "mJsonArray::" + mJourney);
                }

                tvDetails.setText(getString(R.string.depart) + " " + DateFormate.sdfFlightDisplayDate.format(mStartCalDate.getTime()) + " | " + mFlightCity.traveller + " | " + mFlightCity.clasName);

                getFlightList();
            }
        }

        tvBack.setOnClickListener(this);
        tvFilter.setOnClickListener(this);
    }

    private void getFlightList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }


        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<FlightMultiCityPojo> mCall = mApiInterface.getMultiCityFlightList(mJourney, "" + mFlightCity.adultCount, "" + mFlightCity.childCount, "" + mFlightCity.infantCount, mFlightCity.clas, mFlightCity.currencyName, mTimeSlot, mPreferredFlight, Otapp.mUniqueID);
        mCall.enqueue(new Callback<FlightMultiCityPojo>() {
            @Override
            public void onResponse(Call<FlightMultiCityPojo> call, Response<FlightMultiCityPojo> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    mFlightMultiCityPojo = response.body();
                    if (mFlightMultiCityPojo != null) {
                        if (mFlightMultiCityPojo.status.equalsIgnoreCase("200")) {

                            setFlightList();

                        } else if (mFlightMultiCityPojo.status.equalsIgnoreCase("404")) {

                            mFlightMultiCityAdapter.addAll(mFlightMultiCityPojo.data);
                            Utils.showToast(getActivity(), "" + mFlightMultiCityPojo.message);
                            mFlightMultiCityPojo = null;
                        } else {

                            Utils.showToast(getActivity(), "" + mFlightMultiCityPojo.message);
                            mFlightMultiCityPojo = null;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FlightMultiCityPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void setFlightList() {
//
//        if (mCitiesList != null && mCitiesList.size() > 0) {
        mFlightMultiCityAdapter.addAll(mFlightMultiCityPojo.data);
//        List<String> mAirlineList = new ArrayList<>();
        if (mFlightMultiCityPojo.data != null) {
//            for (int i = 0; i < mFlightReturnListPojo.data.size(); i++) {
//                if (mFlightReturnListPojo.data.get(i).cities != null) {
//                    if (!mAirlineList.contains(mFlightReturnListPojo.data.get(i).cities.get(i).airlineName)) {
//                        mAirlineList.add(mFlightReturnListPojo.data.get(i).cities.get(i).airlineName);
//                    }
//                }
//            }
            mAirlineList = mFlightMultiCityPojo.airlines;
            if (mAirlineList == null) {
                mAirlineList = new ArrayList<>();
            }

            FlightMultiCityPojo.Airlines mAirlines = new FlightMultiCityPojo().new Airlines();
            mAirlines.code = "NS0";
            mAirlines.name = getString(R.string.non_stop);
            mAirlineList.add(0, mAirlines);

            if (mPreferredAirlineList.size() == 0) {
                mPreferredAirlineList.addAll(mAirlineList);
            }

            mAirlineMultiCityFilterAdapter.addAll(mAirlineList);
            lnrFilter.setVisibility(View.VISIBLE);
        } else {
            lnrFilter.setVisibility(View.GONE);
        }
//            mFlightReturnDepartureAdapter.addAll(mCitiesList);
//        }
    }

    private void setFilterFlightList() {

        List<FlightMultiCityPojo.Data> mFlightList = new ArrayList<>();

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

        if (mFlightMultiCityPojo.data != null && mFlightMultiCityPojo.data.size() > 0) {
            for (int i = 0; i < mFlightMultiCityPojo.data.size(); i++) {

                List<List<FlightMultiCityPojo.City>> citiesOuter = mFlightMultiCityPojo.data.get(i).cities.get(0);
                boolean isNonStopTrue = true;
                for (int j = 0; j < citiesOuter.size(); j++) {
                    List<FlightMultiCityPojo.City> cities = citiesOuter.get(j);
                    if (isNonStop && cities != null && cities.size() > 1) {
                        isNonStopTrue = false;
                    }
                }

                if (isNonStop && isNonStopTrue) {
                    mFlightList.add(mFlightMultiCityPojo.data.get(i));
                    continue;
                }

                if (mLineList != null && mLineList.size() > 0) {
                    List<FlightMultiCityPojo.AirlineTitles> airlineTItles = mFlightMultiCityPojo.data.get(i).airlineTItles;
                    for (int j = 0; j < airlineTItles.size(); j++) {
                        if (mLineList.contains(airlineTItles.get(j).code)) {
                            mFlightList.add(mFlightMultiCityPojo.data.get(i));
                        }
                    }

                }

            }
        }

        if (!isOptionSelected) {
            mFlightList.addAll(mFlightMultiCityPojo.data);
        }


        if (mFlightList.size() > 0) {
            Utils.showToast(getActivity(), mFlightList.size() + " out of " + mFlightMultiCityPojo.data.size());
        } else {
            Utils.showToast(getActivity(), getString(R.string.msg_no_flight_return));
        }
//        Utils.showToast(getActivity(), mFlightList.size() + " out of " + mFlightMultiCityPojo.data.size());

        mFlightMultiCityAdapter.addAll(mFlightList);
    }

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            popBackStack();
        } else if (view == tvFilter) {

            if (mFlightMultiCityPojo != null && mFlightMultiCityPojo.airlines != null) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_AIRLINE_TIME, mTimeSlot);
                bundle.putString(Constants.BNDL_AIRLINE_LIST, new Gson().toJson(mPreferredAirlineList));
                bundle.putString(Constants.BNDL_PREFERRED_AIRLINE_LIST, mPreferredFlight);
                IntentHandler.startActivityForResult(getActivity(), FlightFilterActivity.class, bundle, Constants.RC_MULTI_CITY_FILTER);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_MULTI_CITY_FILTER && resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                mTimeSlot = bundle.getString(Constants.BNDL_AIRLINE_TIME, "0");
                mPreferredFlight = bundle.getString(Constants.BNDL_PREFERRED_AIRLINE_LIST, "");
                getFlightList();
            }
        }
    }
}
