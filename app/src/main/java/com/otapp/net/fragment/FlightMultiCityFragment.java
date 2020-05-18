package com.otapp.net.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.FlightCityAdapter;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightMultiCityFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_FlightMultiCityFragment = "Tag_" + "FlightMultiCityFragment";

    View mView;

    @BindView(R.id.tvAddCity)
    TextView tvAddCity;
    @BindView(R.id.tvSearchFlight)
    TextView tvSearchFlight;
    @BindView(R.id.lvCity)
    ListView lvCity;
    @BindView(R.id.etTravellers)
    EditText etTravellers;
    @BindView(R.id.etEconomy)
    AutoCompleteTextView etEconomy;

    private int mCitySize = 1;
    private String mClass = "";

    List<FlightCityPojo.City> mFlightCityList;
    List<FlightCityPojo.Clas> mFlightClassList;

    List<FlightCity> mFlightCityArrayList = new ArrayList<>();
    FlightCity mFlightCity = new FlightCity();

    FlightCityAdapter mFlightCityAdapter;

    public static FlightMultiCityFragment newInstance() {
        FlightMultiCityFragment fragment = new FlightMultiCityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_flight_multicity, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        mFlightCityAdapter = new FlightCityAdapter(getActivity(), this);
        lvCity.setAdapter(mFlightCityAdapter);

        FlightCity mFlightCity = new FlightCity();
        mFlightCityArrayList.add(mFlightCity);
        mFlightCityAdapter.addAll(mFlightCityArrayList);

        tvAddCity.setOnClickListener(this);
        etTravellers.setOnClickListener(this);
        tvSearchFlight.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view == tvAddCity) {

            if (mCitySize < 4) {

                mCitySize++;

                FlightCity mFlightCity = new FlightCity();
                mFlightCityArrayList.add(mFlightCity);
                mFlightCityAdapter.addAll(mFlightCityArrayList);
                Intent intent = new Intent(Constants.FILTER_FLIGHT_ADD_CITY);
                intent.putExtra(Constants.BNDL_FLIGHT_CITY_COUNT, mFlightCityArrayList.size());
                getActivity().sendBroadcast(intent);

            } else {
                Utils.showToast(getActivity(), getString(R.string.alert_max_city));
            }
        } else if (view == etTravellers) {
            showTravellerDialog();
        } else if (view == tvSearchFlight) {
            if (isValidField()) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
                switchFragment(FlightMultiCityAirlineListFragment.newInstance(), FlightMultiCityAirlineListFragment.Tag_FlightMultiCityAirlineListFragment, bundle);

            }
        }
    }

    public void setAirportDetails(FlightCityPojo.Data mFlightCityPojo) {

        mFlightCityList = mFlightCityPojo.cities;
        mFlightClassList = mFlightCityPojo.clas;

        mFlightCityAdapter.setCityList(mFlightCityList);

        if (mFlightClassList != null && mFlightClassList.size() > 0) {
            List<String> mClassList = new ArrayList<>();
            for (int i = 0; i < mFlightClassList.size(); i++) {
                mClassList.add(mFlightClassList.get(i).className);
            }
            ArrayAdapter<String> adapterClass = new ArrayAdapter<String>
                    (getActivity(), R.layout.autocomplete_drop_down_item, mClassList);
            etEconomy.setAdapter(adapterClass);

            etEconomy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    LogUtils.e("", isFocused + " isFocused etEconomy " + (etEconomy.getAdapter() != null));
                    if (isFocused && etEconomy.getAdapter() != null) {
                        etEconomy.showDropDown();
                    }
                }
            });
        }

    }

    private void showTravellerDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_flight_traveller);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMinusAdults = (TextView) dialog.findViewById(R.id.tvMinusAdults);
        final TextView tvQuantityAdults = (TextView) dialog.findViewById(R.id.tvQuantityAdults);
        final TextView tvPlusAdults = (TextView) dialog.findViewById(R.id.tvPlusAdults);
        final TextView tvMinusChildren = (TextView) dialog.findViewById(R.id.tvMinusChildren);
        final TextView tvQuantityChildren = (TextView) dialog.findViewById(R.id.tvQuantityChildren);
        final TextView tvPlusChildren = (TextView) dialog.findViewById(R.id.tvPlusChildren);
        final TextView tvMinusInfants = (TextView) dialog.findViewById(R.id.tvMinusInfants);
        final TextView tvQuantityInfants = (TextView) dialog.findViewById(R.id.tvQuantityInfants);
        final TextView tvPlusInfants = (TextView) dialog.findViewById(R.id.tvPlusInfants);
        final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView tvDone = (TextView) dialog.findViewById(R.id.tvDone);

        tvQuantityAdults.setText("" + mFlightCity.adultCount);
        tvQuantityChildren.setText("" + mFlightCity.childCount);
        tvQuantityInfants.setText("" + mFlightCity.infantCount);

        tvMinusAdults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFlightCity.adultCount > 1) {
                    mFlightCity.adultCount--;
                } else {
                    mFlightCity.adultCount = 0;
                }
                tvQuantityAdults.setText("" + mFlightCity.adultCount);

            }
        });

        tvPlusAdults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFlightCity.adultCount++;
                tvQuantityAdults.setText("" + mFlightCity.adultCount);
            }
        });

        tvMinusChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFlightCity.childCount > 1) {
                    mFlightCity.childCount--;
                } else {
                    mFlightCity.childCount = 0;
                }
                tvQuantityChildren.setText("" + mFlightCity.childCount);

            }
        });

        tvPlusChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFlightCity.childCount++;
                tvQuantityChildren.setText("" + mFlightCity.childCount);
            }
        });

        tvMinusInfants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFlightCity.infantCount > 1) {
                    mFlightCity.infantCount--;
                } else {
                    mFlightCity.infantCount = 0;
                }
                tvQuantityInfants.setText("" + mFlightCity.infantCount);

            }
        });

        tvPlusInfants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFlightCity.infantCount++;
                tvQuantityInfants.setText("" + mFlightCity.infantCount);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null) {
                    dialog.dismiss();
                    dialog.cancel();
                }

            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int total = mFlightCity.adultCount + mFlightCity.childCount + mFlightCity.infantCount;
                if (total <= 0) {
                    Utils.showToast(getActivity(), getString(R.string.alert_add_traveller));
                    return;
                }

                StringBuilder strTraveller = new StringBuilder();
                if (mFlightCity.adultCount > 0) {
                    strTraveller.append(mFlightCity.adultCount + " " + getString(R.string.adults));
                }

                if (mFlightCity.childCount > 0) {
                    if (strTraveller.length() > 0) {
                        strTraveller.append(", " + mFlightCity.childCount + " " + getString(R.string.childrens));
                    } else {
                        strTraveller.append(mFlightCity.childCount + " " + getString(R.string.childrens));
                    }
                }

                if (mFlightCity.infantCount > 0) {
                    if (strTraveller.length() > 0) {
                        strTraveller.append(", " + mFlightCity.infantCount + " " + getString(R.string.infants));
                    } else {
                        strTraveller.append(mFlightCity.infantCount + " " + getString(R.string.infants));
                    }
                }

                etTravellers.setText(strTraveller.toString());

                if (dialog != null) {
                    dialog.dismiss();
                    dialog.cancel();
                }

            }
        });

        dialog.show();

    }

    public void onDeleteCity(int position) {

        mCitySize--;

//        mFlightCityArrayList.remove(position);
//        mFlightCityAdapter.addAll(mFlightCityArrayList);
        Intent intent = new Intent(Constants.FILTER_FLIGHT_ADD_CITY);
        intent.putExtra(Constants.BNDL_FLIGHT_CITY_COUNT, mFlightCityArrayList.size());
        getActivity().sendBroadcast(intent);

    }

    private boolean isValidField() {

        if (mFlightCityArrayList != null && mFlightCityArrayList.size() > 0) {
            for (int i = 0; i < mFlightCityArrayList.size(); i++) {
                if (TextUtils.isEmpty(mFlightCityArrayList.get(i).fromCity)) {
                    Utils.showToast(getActivity(), getString(R.string.alert_departure_city));
                    return false;
                } else if (TextUtils.isEmpty(mFlightCityArrayList.get(i).toCity)) {
                    Utils.showToast(getActivity(), getString(R.string.alert_destination_city));
                    return false;
                } else if (TextUtils.equals(mFlightCityArrayList.get(i).fromCity, mFlightCityArrayList.get(i).toCity)) {
                    Utils.showToast(getActivity(), getString(R.string.alert_city_not_same));
                    return false;
                } else if (TextUtils.isEmpty(mFlightCityArrayList.get(i).departDate)) {
                    Utils.showToast(getActivity(), getString(R.string.alert_departure_date));
                    return false;
                }
            }
        }

        int total = mFlightCity.adultCount + mFlightCity.childCount + mFlightCity.infantCount;

        if (total == 0) {
            Utils.showToast(getActivity(), getString(R.string.alert_add_traveller));
            return false;
        } else if (TextUtils.isEmpty(etEconomy.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.alert_class));
            return false;
        }

        for (int i = 0; i < mFlightCityList.size(); i++) {
            for (int j = 0; j < mFlightCityArrayList.size(); j++) {
                if (mFlightCityList.get(i).cityName.equals(mFlightCityArrayList.get(j).fromCity)) {
                    mFlightCityArrayList.get(j).from = mFlightCityList.get(i).cityCode;
                }

                if (mFlightCityList.get(i).cityName.equals(mFlightCityArrayList.get(j).toCity)) {
                    mFlightCityArrayList.get(j).to = mFlightCityList.get(i).cityCode;
                }
            }

        }

        for (int j = 0; j < mFlightCityArrayList.size(); j++) {
            LogUtils.e("", j + " mFlightCityArrayList.get(i).cityName::" + mFlightCityArrayList.get(j).from);
            LogUtils.e("", j + " mFlightCityArrayList.get(i).fromCity::" + mFlightCityArrayList.get(j).fromCity);
            LogUtils.e("", j + " mFlightCityArrayList.get(i).cityName::" + mFlightCityArrayList.get(j).to);
            LogUtils.e("", j + " mFlightCityArrayList.get(i).toCity::" + mFlightCityArrayList.get(j).toCity);
        }

        for (int i = 0; i < mFlightClassList.size(); i++) {
            if (mFlightClassList.get(i).className.equals(etEconomy.getText().toString())) {
                mClass = mFlightClassList.get(i).classCode;
                break;
            }
        }

        mFlightCity.clas = mClass;
        mFlightCity.clasName = etEconomy.getText().toString();
        mFlightCity.type = 2;
        mFlightCity.traveller = etTravellers.getText().toString();
        mFlightCity.mFlightCityList = mFlightCityArrayList;

        return true;
    }
}
