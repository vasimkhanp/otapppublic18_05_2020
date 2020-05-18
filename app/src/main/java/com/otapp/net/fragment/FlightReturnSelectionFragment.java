package com.otapp.net.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.R;
import com.otapp.net.adapter.FlightReturnSelectionAdapter;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.model.FlightReturnListPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightReturnSelectionFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_FlightReturnSelectionFragment = "Tag_" + "FlightReturnSelectionFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDetails)
    TextView tvDetails;
    @BindView(R.id.tvDepartFlights)
    TextView tvDepartFlights;
    @BindView(R.id.tvReturnFlights)
    TextView tvReturnFlights;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvContinue)
    TextView tvContinue;
    @BindView(R.id.ivDepartAirline)
    ImageView ivDepartAirline;
    @BindView(R.id.ivReturnAirline)
    ImageView ivReturnAirline;
    @BindView(R.id.lvDepartFlight)
    ListView lvDepartFlight;
    @BindView(R.id.lvReturnFlight)
    ListView lvReturnFlight;
    String mFlightNameArray = "";
    String stop = "";

    FlightReturnSelectionAdapter mDepartSelectionAdapter, mReturnSelectionAdapter;

    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;
    Calendar mStartCalDate = Calendar.getInstance();
    Calendar mEndCalDate = Calendar.getInstance();

    List<List<FlightReturnListPojo.City>> mDepartFlightList, mReturnFlightList;

    FlightCity mFlightCity;
    List<FlightCityPojo.City> mFlightCityList;
    FlightReturnListPojo.Data mFlightData;
    private int mPosition = -1;

    public static FlightReturnSelectionFragment newInstance() {
        FlightReturnSelectionFragment fragment = new FlightReturnSelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_return_flight_selection_list, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            String sFlightCity = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT, "");
            mFlightCity = new Gson().fromJson(sFlightCity, FlightCity.class);


            String sFlightCityList = MyPref.getPref(getContext(), Constants.BNDL_CITY_LIST, "");
            mFlightCityList = new Gson().fromJson(sFlightCityList, new TypeToken<ArrayList<FlightCityPojo.City>>() {
            }.getType());

            String sFlightData = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_RETURN_DETAILS, "");
            mFlightData = new Gson().fromJson(sFlightData, FlightReturnListPojo.Data.class);
            mPosition = MyPref.getPref(getContext(), Constants.CITY_TYPE_POSITION, 0);
//            Toast.makeText(getActivity(), ""+mPosition, Toast.LENGTH_SHORT).show();
            mFlightNameArray = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_NAME_LIST, "");


            if (mFlightCity != null) {
                tvTitle.setText(mFlightCity.fromCity + " " + getString(R.string.to_) + " " + mFlightCity.toCity);

                mStartCalDate.setTimeInMillis(mFlightCity.displayDepartDate);
                mEndCalDate.setTimeInMillis(mFlightCity.displayReturnDate);

                mStartYear = mStartCalDate.get(Calendar.YEAR);
                mStartMonth = mStartCalDate.get(Calendar.MONTH);
                mStartDay = mStartCalDate.get(Calendar.DAY_OF_MONTH);

                mEndYear = mEndCalDate.get(Calendar.YEAR);
                mEndMonth = mEndCalDate.get(Calendar.MONTH);
                mEndDay = mEndCalDate.get(Calendar.DAY_OF_MONTH);

                tvDetails.setText(getString(R.string.depart) + " " + DateFormate.sdfFlightDisplayDate.format(mStartCalDate.getTime()) + " - " + getString(R.string.retrn) + " " + DateFormate.sdfFlightDisplayDate.format(mEndCalDate.getTime()) + " | " + mFlightCity.traveller + " | " + mFlightCity.clasName);
            }

            if (mFlightData != null) {
                tvPrice.setText(mFlightData.currency + " " + Utils.setPrice(mFlightData.fares.total.grandTotal));

                mDepartFlightList = mFlightData.cities.get(0);
                mReturnFlightList = mFlightData.cities.get(1);

                List<FlightReturnListPojo.AirlineTitles> airlineTItles = mFlightData.airlineTItles;
                if (airlineTItles != null && airlineTItles.size() == 1) {
                    tvDepartFlights.setText(getString(R.string.depart) + " - " + airlineTItles.get(0).name);
                    tvReturnFlights.setText(getString(R.string.retrn) + " - " + airlineTItles.get(0).name);

                    if (!TextUtils.isEmpty(airlineTItles.get(0).logo)) {
                        Picasso.get().load(airlineTItles.get(0).logo).into(ivDepartAirline);
                        Picasso.get().load(airlineTItles.get(0).logo).into(ivReturnAirline);
                    }
                }

                if (mDepartFlightList != null && mDepartFlightList.size() > 0) {
                    for (int i = 0; i < mDepartFlightList.size(); i++) {
                        if (i == 0) {
                            mDepartFlightList.get(i).get(0).isSelected = true;
                            if (airlineTItles != null && airlineTItles.size() > 1) {
                                tvDepartFlights.setText(getString(R.string.depart) + " - " + mDepartFlightList.get(i).get(0).airline);
                                if (!TextUtils.isEmpty(mDepartFlightList.get(i).get(0).logo)) {
                                    Picasso.get().load(mDepartFlightList.get(i).get(0).logo).into(ivDepartAirline);
                                }
                            }
                        } else {
                            mDepartFlightList.get(i).get(0).isSelected = false;
                        }
                    }
                }

                if (mReturnFlightList != null && mReturnFlightList.size() > 0) {
                    for (int i = 0; i < mReturnFlightList.size(); i++) {
                        if (i == 0) {
                            mReturnFlightList.get(i).get(0).isSelected = true;
                            if (airlineTItles != null && airlineTItles.size() > 1) {
                                tvReturnFlights.setText(getString(R.string.retrn) + " - " + mReturnFlightList.get(i).get(0).airline);
                                if (!TextUtils.isEmpty(mReturnFlightList.get(i).get(0).logo)) {
                                    Picasso.get().load(mReturnFlightList.get(i).get(0).logo).into(ivReturnAirline);
                                }
                            }
                        } else {
                            mReturnFlightList.get(i).get(0).isSelected = false;
                        }
                    }
                }

                mDepartSelectionAdapter = new FlightReturnSelectionAdapter(getActivity(), "Depart");
                lvDepartFlight.setAdapter(mDepartSelectionAdapter);
                mDepartSelectionAdapter.addAll(mDepartFlightList);

                mReturnSelectionAdapter = new FlightReturnSelectionAdapter(getActivity(), "Return");
                lvReturnFlight.setAdapter(mReturnSelectionAdapter);
                mReturnSelectionAdapter.addAll(mReturnFlightList);

            }
        }

        tvContinue.setOnClickListener(this);
        tvBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == tvBack) {
            popBackStack();
        } else if (view == tvContinue) {

            List<List<FlightReturnListPojo.City>> mDepartCityList = mDepartSelectionAdapter.getSelectedCity();
            List<List<FlightReturnListPojo.City>> mReturnCityList = mReturnSelectionAdapter.getSelectedCity();

            List<FlightReturnListPojo.City> mDepartCity = null, mReturnCity = null;
            int mDepartPos = 0, mReturnPos = 0;

            for (int i = 0; i < mDepartCityList.size(); i++) {
                if (mDepartCityList.get(i).get(0).isSelected) {
                    mDepartPos = i;
                    mDepartCity = mDepartCityList.get(i);
                    break;
                }
            }

            for (int i = 0; i < mReturnCityList.size(); i++) {
                if (mReturnCityList.get(i).get(0).isSelected) {
                    mReturnPos = i;
                    mReturnCity = mReturnCityList.get(i);
                    break;
                }
            }

            if (mDepartCity == null || mDepartCity.size() == 0) {
                Utils.showToast(getActivity(), getString(R.string.alert_departure_flight));
                return;
            }

            if (mReturnCity == null || mReturnCity.size() == 0) {
                Utils.showToast(getActivity(), getString(R.string.alert_return_flight));
                return;
            }

            Bundle bundle = new Bundle();

            MyPref.setPref(getContext(),Constants.BNDL_FLIGHT,new Gson().toJson(mFlightCity));
            MyPref.setPref(getContext(),Constants.BNDL_FLIGHT_NAME_LIST, mFlightNameArray);
            MyPref.setPref(getContext(),Constants.BNDL_CITY_LIST, new Gson().toJson(mFlightCityList));
            MyPref.setPref(getContext(),Constants.CITY_TYPE_POSITION, mPosition);
            MyPref.setPref(getContext(),Constants.BNDL_FLIGHT_UID,  mFlightData.uid + "-" + mDepartPos + "-" + mReturnPos);
            MyPref.setPref(getContext(),Constants.BNDL_FLIGHT_RETURN_DETAILS, new Gson().toJson(mFlightData));

//            bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
//            bundle.putString(Constants.BNDL_FLIGHT_NAME_LIST, mFlightNameArray);
//            bundle.putString(Constants.BNDL_CITY_LIST, new Gson().toJson(mFlightCityList));
//            bundle.putString(Constants.BNDL_FLIGHT_UID, mFlightData.uid + "-" + mDepartPos + "-" + mReturnPos);
//            bundle.putString(Constants.BNDL_FLIGHT_RETURN_DETAILS, new Gson().toJson(mFlightData));
//            bundle.putInt(Constants.CITY_TYPE_POSITION, mPosition);
            switchFragment(FlightOneDetailsFragment.newInstance(), FlightOneDetailsFragment.Tag_FlightOneDetailsFragment, bundle);
        }
    }
}
