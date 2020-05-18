package com.otapp.net.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.AutoCompleteCityAdapter;
import com.otapp.net.adapter.AutoCompleteCurrencyAdapter;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.slycalendarview.SlyCalendarDialog;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightOneWayFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_FlightOneWayFragment = "Tag_" + "FlightOneWayFragment";

    View mView;

    @BindView(R.id.tvSearchFlight)
    TextView tvSearchFlight;
    @BindView(R.id.etFromCity)
    AutoCompleteTextView etFromCity;
    @BindView(R.id.etToCity)
    AutoCompleteTextView etToCity;
    @BindView(R.id.etEconomy)
    AutoCompleteTextView etEconomy;
    @BindView(R.id.etCurrency)
    AutoCompleteTextView etCurrency;
    @BindView(R.id.etDate)
    EditText etDate;
    @BindView(R.id.etTravellers)
    EditText etTravellers;

    List<FlightCityPojo.Clas> mFlightClassList;
    List<FlightCityPojo.Currency> mFlightCurrencyList;
    List<FlightCityPojo.City> mFlightCityList;
    FlightCityPojo.Data mFlightCityPojo;
    List<FlightCityPojo.City> mNationalList = new ArrayList<>();
    private int mPosition = -1;


    //    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;
    Calendar mStartCalDate = Calendar.getInstance();
    Calendar mEndCalDate = Calendar.getInstance();
    //    DatePickerDialog dateStartPickerDialog, dateEndPickerDialog;
    String mFromCityCode = "", mToCityCode = "", mStartDate = "", mEndDate = "", mClass = "";
    private boolean isInternationFlight;

    FlightCity mFlightCity = new FlightCity();
    FlightCityPojo.City mFromCity, mToCity;
    FlightCityPojo.Currency mCurrency;


    public static FlightOneWayFragment newInstance() {
        FlightOneWayFragment fragment = new FlightOneWayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_flight_one_way, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    private void InitializeControls() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPosition = bundle.getInt(Constants.CITY_TYPE_POSITION);
        }

        mFlightCity.adultCount = 1;
        etTravellers.setText(mFlightCity.adultCount + " " + getString(R.string.adults));

//        mStartYear = mStartCalDate.get(Calendar.YEAR);
//        mStartMonth = mStartCalDate.get(Calendar.MONTH);
//        mStartDay = mStartCalDate.get(Calendar.DAY_OF_MONTH);
//
//        mEndYear = mEndCalDate.get(Calendar.YEAR);
//        mEndMonth = mEndCalDate.get(Calendar.MONTH);
//        mEndDay = mEndCalDate.get(Calendar.DAY_OF_MONTH);
//
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
//                        if (mPosition == 0) {
//                            etDate.setText("" + DateFormate.sdfParkDisplayDate.format(mStartCalDate.getTime()));
//
//                            mStartDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
//                        }
//
//                        if (mPosition == 1) {// return
//
//                            // end date
//                            mEndCalDate.set(Calendar.YEAR, year);
//                            mEndCalDate.set(Calendar.MONTH, monthOfYear);
//                            mEndCalDate.set(Calendar.DAY_OF_MONTH, (dayOfMonth));
//
//                            mEndYear = mEndCalDate.get(Calendar.YEAR);
//                            mEndMonth = mEndCalDate.get(Calendar.MONTH);
//                            mEndDay = mEndCalDate.get(Calendar.DAY_OF_MONTH);
//
//                            Calendar mStartCal = Calendar.getInstance();
//                            mStartCal.setTime(mStartCalDate.getTime());
//                            dateEndPickerDialog.getDatePicker().setMinDate(mStartCal.getTimeInMillis() - 10000);
//                            mStartCal.add(Calendar.MONTH, 6);
//                            dateEndPickerDialog.getDatePicker().setMaxDate(mStartCal.getTimeInMillis() - 10000);
//
//                            if (dateEndPickerDialog != null) {
//                                dateEndPickerDialog.show();
//                            }
//                        }
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
//                        etDate.setText("" + DateFormate.sdfParkDisplayDate.format(mStartCalDate.getTime()) + " - " + DateFormate.sdfParkDisplayDate.format(mEndCalDate.getTime()));
//
//                        mStartDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
//                        mEndDate = DateFormate.sdfFlightServerDate.format(mEndCalDate.getTime());
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
//        mStartCal.add(Calendar.MONTH, 6);
//        dateStartPickerDialog.getDatePicker().setMaxDate(mStartCal.getTimeInMillis() - 10000);
//        dateEndPickerDialog.getDatePicker().setMaxDate(mStartCal.getTimeInMillis() - 10000);

        Calendar mStartCal = Calendar.getInstance();
        mStartCal.add(Calendar.DAY_OF_MONTH, 1);
        mEndCalDate.setTime(mStartCal.getTime());


        etTravellers.setOnClickListener(this);
        etDate.setOnClickListener(this);
        tvSearchFlight.setOnClickListener(this);

//        etFromCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean isFocused) {
//                LogUtils.e("", isFocused + " isFocused etFromCity " + (etFromCity.getAdapter() != null));
//                if (isFocused && etFromCity.getAdapter() != null) {
//                    etFromCity.showDropDown();
//                }
//            }
//        });
//
//        etToCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean isFocused) {
//                LogUtils.e("", isFocused + " isFocused etToCity " + (etToCity.getAdapter() != null));
//                if (isFocused && etToCity.getAdapter() != null) {
//                    etToCity.showDropDown();
//                }
//            }
//        });

        etEconomy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                LogUtils.e("", isFocused + " isFocused etEconomy " + (etEconomy.getAdapter() != null));
                if (isFocused && etEconomy.getAdapter() != null) {
                    etEconomy.showDropDown();
                }
            }
        });

        etCurrency.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                LogUtils.e("", isFocused + " isFocused etEconomy " + (etCurrency.getAdapter() != null));
                if (isFocused && etCurrency.getAdapter() != null) {
                    etCurrency.showDropDown();
                }
            }
        });

    }

    public void setAirportDetails(FlightCityPojo.Data mFlightCityBeans) {

        mFlightCityList = mFlightCityBeans.cities;
        mFlightClassList = mFlightCityBeans.clas;
        mFlightCurrencyList = mFlightCityBeans.currencies;
        this.mFlightCityPojo = mFlightCityBeans;


        if (getActivity() == null) {
            return;
        }

        if (mFlightCityList != null && mFlightCityList.size() > 0) {
            mNationalList.addAll(mFlightCityList);
//            List<String> mCityList = new ArrayList<>();
//            for (int i = 0; i < mFlightCityList.size(); i++) {
//                mCityList.add(mFlightCityList.get(i).cityName);
//            }
//            ArrayAdapter<String> adapterFromCity = new ArrayAdapter<String>
//                    (getActivity(), R.layout.autocomplete_drop_down_item, mCityList);
//            etFromCity.setAdapter(adapterFromCity);


            AutoCompleteCityAdapter mFromCityAdapter = new AutoCompleteCityAdapter(getActivity(), R.layout.list_item_airport_city_drop_down, mFlightCityList);
            etFromCity.setAdapter(mFromCityAdapter);
            etFromCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    mFromCity = (FlightCityPojo.City) adapterView.getItemAtPosition(pos);
                    LogUtils.e("", "mFromCity::" + mFromCity.airportCode);
                    etFromCity.setSelection(etFromCity.getText().length());

                    ServiceFragment mServiceFragment = (ServiceFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ServiceFragment.Tag_ServiceFragment);
                    if (mServiceFragment != null) {
                        LogUtils.e("", "mFlightFragment is not null");
                        mServiceFragment.setFromCity(mFromCity);
                    } else {
                        LogUtils.e("", "mFlightFragment is null");
                    }
                }
            });


//            ArrayAdapter<String> adapterToCity = new ArrayAdapter<String>
//                    (getActivity(), R.layout.autocomplete_drop_down_item, mCityList);
//            etToCity.setAdapter(adapterToCity);

            AutoCompleteCityAdapter mToCityAdapter = new AutoCompleteCityAdapter(getActivity(), R.layout.list_item_airport_city_drop_down, mFlightCityList);
            etToCity.setAdapter(mToCityAdapter);
            etToCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    mToCity = (FlightCityPojo.City) adapterView.getItemAtPosition(pos);
                    LogUtils.e("", "mToCity::" + mToCity.airportCode);
                    etToCity.setSelection(etToCity.getText().length());

                    ServiceFragment mServiceFragment = (ServiceFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ServiceFragment.Tag_ServiceFragment);
                    if (mServiceFragment != null) {
                        LogUtils.e("", "mFlightFragment is not null");
                        mServiceFragment.setToCity(mToCity);
                    } else {
                        LogUtils.e("", "mFlightFragment is null");
                    }
                }
            });

        }


        if (mFlightClassList != null && mFlightClassList.size() > 0) {
            List<String> mClassList = new ArrayList<>();
            int position = 0;
            for (int i = 0; i < mFlightClassList.size(); i++) {
                mClassList.add(mFlightClassList.get(i).className);
                if (mFlightClassList.get(i).className.equalsIgnoreCase("economy")) {
                    position = i;
                }
            }
            ArrayAdapter<String> adapterClass = new ArrayAdapter<String>
                    (getActivity(), R.layout.autocomplete_drop_down_item, mClassList);
            etEconomy.setAdapter(adapterClass);
            final int finalPos = position;
            etEconomy.postDelayed(new Runnable() {
                @Override
                public void run() {
                    etEconomy.setText(mFlightClassList.get(finalPos).className);
                    etEconomy.setSelection(etEconomy.getText().length());
                }
            }, 500);

        }

        if (mFlightCurrencyList != null && mFlightCurrencyList.size() > 0) {
            List<String> mCurrencyList = new ArrayList<>();
            int position = 0;
            for (int i = 0; i < mFlightCurrencyList.size(); i++) {
                mCurrencyList.add(mFlightCurrencyList.get(i).currencyName);
                if (mFlightCurrencyList.get(i).currencyName.equalsIgnoreCase("TZS")) {
                    position = i;
                    mCurrency = mFlightCurrencyList.get(i);
                }
            }
            AutoCompleteCurrencyAdapter adapterCurrency = new AutoCompleteCurrencyAdapter
                    (getActivity(), R.layout.list_item_currency_drop_down, mFlightCurrencyList);
            etCurrency.setAdapter(adapterCurrency);
            final int finalPos = position;
            etCurrency.postDelayed(new Runnable() {
                @Override
                public void run() {
                    etCurrency.setText(mFlightCurrencyList.get(finalPos).currencyName);
                    etCurrency.setSelection(etCurrency.getText().length());
                }
            }, 500);

            etCurrency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

                    mCurrency = (FlightCityPojo.Currency) adapterView.getItemAtPosition(pos);
                    etCurrency.setSelection(etCurrency.getText().length());

                    ServiceFragment mServiceFragment = (ServiceFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ServiceFragment.Tag_ServiceFragment);
                    if (mServiceFragment != null) {
                        LogUtils.e("", "mFlightFragment is not null");
                        mServiceFragment.setCurrency(mCurrency);
                    } else {
                        LogUtils.e("", "mFlightFragment is null");
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

                if (mFlightCity.adultCount > 2) {
                    mFlightCity.adultCount--;
                } else {
                    mFlightCity.adultCount = 1;
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
                    etTravellers.setText("");
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

    @Override
    public void onClick(View view) {

        if (view == etTravellers) {
            showTravellerDialog();
        } else if (view == etDate) {

            if (mPosition == 0) {

                new SlyCalendarDialog()
                        .setSingle(true)
                        .setFirstMonday(false)
                        .setStartDate(mStartCalDate.getTime())
                        .setCallback(new SlyCalendarDialog.Callback() {
                            @Override
                            public void onCancelled() {

                            }

                            @Override
                            public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {

                                if (mPosition == 0 && firstDate != null) {
                                    mStartCalDate.setTime(firstDate.getTime());
                                    etDate.setText("" + DateFormate.sdfParkDisplayDate.format(mStartCalDate.getTime()));
                                    mStartDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());

                                }

                            }
                        })
                        .show(getActivity().getSupportFragmentManager(), "TAG_ONE_CALENDAR");

            } else if (mPosition == 1) {

                new SlyCalendarDialog()
                        .setSingle(false)
                        .setFirstMonday(false)
                        .setStartDate(mStartCalDate.getTime())
                        .setEndDate(mEndCalDate.getTime())
                        .setCallback(new SlyCalendarDialog.Callback() {
                            @Override
                            public void onCancelled() {

                            }

                            @Override
                            public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {

                                if (mPosition == 1 && firstDate != null && secondDate != null) {

                                    mStartCalDate.setTime(firstDate.getTime());
                                    mEndCalDate.setTime(secondDate.getTime());

                                    etDate.setText("" + DateFormate.sdfParkDisplayDate.format(mStartCalDate.getTime()) + " - " + DateFormate.sdfParkDisplayDate.format(mEndCalDate.getTime()));
                                    mStartDate = DateFormate.sdfFlightServerDate.format(mStartCalDate.getTime());
                                    mEndDate = DateFormate.sdfFlightServerDate.format(mEndCalDate.getTime());
                                }

                            }
                        })
                        .show(getActivity().getSupportFragmentManager(), "TAG_CALENDAR");
            }


//            if (dateStartPickerDialog != null) {
//                dateStartPickerDialog.show();
//            }
        } else if (view == tvSearchFlight) {

            if (isValidField()) {

                Bundle bundle = new Bundle();
               /* bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
                bundle.putString(Constants.BNDL_CITY_LIST, new Gson().toJson(mNationalList));
                bundle.putInt(Constants.CITY_TYPE_POSITION, mPosition);
*/
                MyPref.setPref(getContext(),Constants.BNDL_FLIGHT,new Gson().toJson(mFlightCity));
                MyPref.setPref(getContext(),Constants.BNDL_CITY_LIST, new Gson().toJson(mNationalList));
                MyPref.setPref(getContext(),Constants.CITY_TYPE_POSITION, mPosition);

                Log.d("Log", "Flight City : " + new Gson().toJson(mFlightCity));
                Log.d("Log", "National List : " + new Gson().toJson(mNationalList));
                Log.d("Log", "Position : " + mPosition);

                if (mPosition == 0) {
                    switchFragment(FlightOneAirlineListFragment.newInstance(), FlightOneAirlineListFragment.Tag_FlightOneAirlineListFragment, bundle);
                } else {
                    switchFragment(FlightReturnDepartureAirlineListFragment.newInstance(), FlightReturnDepartureAirlineListFragment.Tag_FlightReturnDepartureAirlineListFragment, bundle);
                }
            }
        }
    }

    private boolean isValidField() {

        mFromCityCode = "";
        mFromCityCode = "";
        mToCityCode = "";

        int total = mFlightCity.adultCount + mFlightCity.childCount + mFlightCity.infantCount;

        if (TextUtils.isEmpty(etFromCity.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.alert_departure_city));
            return false;
        } else if (TextUtils.isEmpty(etToCity.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.alert_destination_city));
            return false;
        } else if (mFromCity != null && mToCity != null && TextUtils.equals(mFromCity.cityCode, mToCity.cityCode)) {
            Utils.showToast(getActivity(), getString(R.string.alert_city_not_same));
            return false;
        } else if (TextUtils.isEmpty(mStartDate)) {
            Utils.showToast(getActivity(), getString(R.string.alert_departure_date));
            return false;
        } else if (mPosition == 1 && TextUtils.isEmpty(mEndDate)) {
            Utils.showToast(getActivity(), getString(R.string.alert_return_date));
            return false;
        } else if (mPosition == 1 && mStartCalDate.after(mEndCalDate)) {
            Utils.showToast(getActivity(), getString(R.string.alert_valid_date));
            return false;
        } else if (total == 0) {
            Utils.showToast(getActivity(), getString(R.string.alert_add_traveller));
            return false;
        } else if (TextUtils.isEmpty(etEconomy.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.alert_class));
            return false;
        } else if (TextUtils.isEmpty(etCurrency.getText().toString())) {
            Utils.showToast(getActivity(), getString(R.string.alert_currency));
            return false;
        }

        if (mFromCity != null) {
            mFromCityCode = mFromCity.airportCode;
        }

        if (mToCity != null) {
            mToCityCode = mToCity.airportCode;
        }

//        for (int i = 0; i < mFlightCityList.size(); i++) {
//            if (mFlightCityList.get(i).cityName.equals(etFromCity.getText().toString())) {
//                mFromCityCode = mFlightCityList.get(i).cityCode;
//            }
//
//            if (mFlightCityList.get(i).cityName.equals(etToCity.getText().toString())) {
//                mToCityCode = mFlightCityList.get(i).cityCode;
//            }
//        }

        if (mFlightClassList != null && mFlightClassList.size() > 0) {
            for (int i = 0; i < mFlightClassList.size(); i++) {
                if (mFlightClassList.get(i).className.equals(etEconomy.getText().toString())) {
                    mClass = mFlightClassList.get(i).classCode;
                    break;
                }
            }
        }

        if (TextUtils.isEmpty(mFromCityCode)) {
            Utils.showToast(getActivity(), getString(R.string.alert_departure_city));
            return false;
        } else if (TextUtils.isEmpty(mToCityCode)) {
            Utils.showToast(getActivity(), getString(R.string.alert_destination_city));
            return false;
        } else if (TextUtils.isEmpty(mClass)) {
            Utils.showToast(getActivity(), getString(R.string.alert_class));
            return false;
        } else if (mCurrency == null) {
            Utils.showToast(getActivity(), getString(R.string.alert_currency));
            return false;
        }

        if (mFromCity.countryName.equals(mToCity.countryName)) {
            mFlightCity.isInternationFlight = false;
        } else {
            mFlightCity.isInternationFlight = true;
        }

        mFlightCity.fromCity = etFromCity.getText().toString();
        mFlightCity.toCity = etToCity.getText().toString();
        mFlightCity.from = mFromCityCode;
        mFlightCity.to = mToCityCode;
        mFlightCity.clas = mClass;
        mFlightCity.clasName = etEconomy.getText().toString();
        mFlightCity.departDate = mStartDate;
        mFlightCity.displayDepartDate = mStartCalDate.getTimeInMillis();
        mFlightCity.displayReturnDate = mEndCalDate.getTimeInMillis();
        mFlightCity.type = mPosition;
        mFlightCity.currencyName = etCurrency.getText().toString();
        mFlightCity.traveller = etTravellers.getText().toString();
        mFlightCity.cnvFixedFee = mCurrency.cnvFixedFee;
        mFlightCity.cnvPerFee = mCurrency.cnvPerFee;
        mFlightCity.flightAuthToken = mFlightCityPojo.flightAuthToken;

        if (!TextUtils.isEmpty(mEndDate)) {
            mFlightCity.returnDate = mEndDate;
        }

        return true;
    }

    public void setFromCity(FlightCityPojo.City mFromSelectedCity) {
        LogUtils.e("", "setFromCity:");
        mFromCity = mFromSelectedCity;
        etFromCity.setText(mFromCity.cityName);
        etFromCity.setSelection(etFromCity.getText().length());
//        for (int i = 0; i < mFlightCityList.size(); i++) {
//            if (mFromSelectedCity.cityName.equals(mFlightCityList.get(i).cityName)) {
//                LogUtils.e("", "i::"+i);
//                etFromCity.setSelection(i);
//            }
//        }

    }

    public void setToCity(FlightCityPojo.City mToSelectedCity) {
        LogUtils.e("", "setToCity:");
        mToCity = mToSelectedCity;
        etToCity.setText(mToCity.cityName);
        etToCity.setSelection(etToCity.getText().length());
//        for (int i = 0; i < mFlightCityList.size(); i++) {
//            if (mToCity.cityName.equals(mFlightCityList.get(i).cityName)) {
//                LogUtils.e("", "i::"+i);
//                etToCity.setSelection(i);
//            }
//        }
    }

    public void setCurrency(FlightCityPojo.Currency mSelectedCurrency) {
        LogUtils.e("", "setToCity:");
        mCurrency = mSelectedCurrency;
        etCurrency.setText(mCurrency.currencyName);
        etCurrency.setSelection(etCurrency.getText().length());
//        for (int i = 0; i < mFlightCityList.size(); i++) {
//            if (mToCity.cityName.equals(mFlightCityList.get(i).cityName)) {
//                LogUtils.e("", "i::"+i);
//                etToCity.setSelection(i);
//            }
//        }
    }
}
