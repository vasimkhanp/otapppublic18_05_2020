package com.otapp.net.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.R;
import com.otapp.net.adapter.CountryCodeSpinAdapter;
import com.otapp.net.adapter.FlightPaymentSummaryAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightOneDetailsPojo;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.model.FlightPaymentSummaryPojo;
import com.otapp.net.model.FlightPerson;
import com.otapp.net.model.FlightReturnListPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightPaymentFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_FlightPaymentFragment = "Tag_" + "FlightPaymentFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvFlight1)
    TextView tvFlight1;
    @BindView(R.id.ivAirline1)
    ImageView ivAirline1;
    @BindView(R.id.tvDate1)
    TextView tvDate1;
    @BindView(R.id.tvTime1)
    TextView tvTime1;
    @BindView(R.id.tvFlight2)
    TextView tvFlight2;
    @BindView(R.id.tvSession)
    TextView tvSession;
    //    @BindView(R.id.tvBaseFare)
//    TextView tvBaseFare;
//    @BindView(R.id.tvFeeSurcharge)
//    TextView tvFeeSurcharge;
//    @BindView(R.id.tvAddOns)
//    TextView tvAddOns;
//    @BindView(R.id.tvConvenienceCharge)
//    TextView tvConvenienceCharge;
    @BindView(R.id.ivAirline2)
    ImageView ivAirline2;
    @BindView(R.id.tvDate2)
    TextView tvDate2;
    @BindView(R.id.tvTime2)
    TextView tvTime2;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvPriceTitle)
    TextView tvPriceTitle;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.lnrReturn)
    LinearLayout lnrReturn;
    @BindView(R.id.lvTicketPrice)
    ListView lvTicketPrice;
    @BindView(R.id.lnrSessionTime)
    LinearLayout lnrSessionTime;
    @BindView(R.id.rltDebicCard)
    RelativeLayout rltDebicCard;
    @BindView(R.id.rltTigoPesa)
    RelativeLayout rltTigoPesa;
    @BindView(R.id.rltMPesa)
    RelativeLayout rltMPesa;
    @BindView(R.id.rltAirtelMoney)
    RelativeLayout rltAirtelMoney;

    FlightOneListPojo.Data mFlightData;
    FlightOneDetailsPojo mFlightOneDetailsPojo;
    FlightCity mFlightCity;
    List<FlightPerson> mPersoneList;

    FlightReturnListPojo.Data mFlightReturnData;
    String mFlightUid = "", mKey = "", mAdult = "", mChildren = "", mInfant = "";
    private String mFlightNameArray = "";

    String strCardNo, strCardType, strCardExpiryDt, strCardCvv, strMonth, strYear;
    int year = 0, expYear = 0;
    int month = 1;
    int mLimitAmount;

    private int mPosition = -1;
    private int mMobInnerNumberMaxLength = 9;
    int totalPassenger = 0;

    String mCurrency = "";
    float mTotalAmount = 0;

    float tigoAmount = 0;


    int cnvFixedFee = 0, cnvPerFee = 0, cnvTotalFee = 0;

    private FlightPaymentSummaryPojo mFlightPaymentSummaryPojo;

    public boolean isCreditCardEnabled = false, isMpesaEnabled = false, isTigoEnabled = false, isAirtelMoneyEnabled = false;

    float mAddonsPrice = 0;

    private CountDownTimer mCountDownTimer;
    private FlightPaymentSummaryAdapter mFlightPaymentSummaryAdapter;

    String sCRDBCharges = "";
    String sMPesaCharges = "";
    String sTigoCharges = "";
    String sAirtelCharges = "";


    public static FlightPaymentFragment newInstance() {
        FlightPaymentFragment fragment = new FlightPaymentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_flight_payment, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        if (MyPref.getPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, 0l) > 0l) {
            startTimer(MyPref.getPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, 0l));
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
//            mFlightCity = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT), FlightCity.class);
            String sFlightCity = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT, "");
            mFlightCity = new Gson().fromJson(sFlightCity, FlightCity.class);
            String sFlightOneDetailsPojo = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_DETAILS_RESPONSE, "");
            mFlightOneDetailsPojo = new Gson().fromJson(sFlightOneDetailsPojo, FlightOneDetailsPojo.class);
            String sFlightPaymentSummaryPojo = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_PAYMENT_SUMMARY, "");
            mFlightPaymentSummaryPojo = new Gson().fromJson(sFlightPaymentSummaryPojo, FlightPaymentSummaryPojo.class);
            String sPersoneList = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_TRAVELLER, "");
            mPersoneList = new Gson().fromJson(sPersoneList, new TypeToken<ArrayList<FlightPerson>>() {
            }.getType());
            mPosition = MyPref.getPref(getContext(), Constants.CITY_TYPE_POSITION, 0);
//            mPosition = bundle.getInt(Constants.CITY_TYPE_POSITION);
            mFlightNameArray = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_NAME_LIST, "");
            if (mPosition == 0) {
                String sFlightData = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_ONE_DETAILS, "");
                mFlightData = new Gson().fromJson(sFlightData, FlightOneListPojo.Data.class);

                lnrReturn.setVisibility(View.GONE);
                if (mFlightData != null) {
                    mFlightUid = mFlightData.uid;
                    LogUtils.e("", "mFlightUid::" + mFlightUid);
                    mKey = mFlightData.key;
                    List<FlightOneListPojo.Cities> cities = mFlightData.cities;
                    if (cities != null && cities.size() > 0) {
                        String mStop = "";
                        if (cities.size() > 1) {
                            mStop = (cities.size() - 1) + " " + getString(R.string.stop);
                        } else {
                            mStop = getString(R.string.non_stop);
                        }
                        tvFlight1.setText(cities.get(0).startAirportCity + " " + getString(R.string.to) + " " + cities.get(cities.size() - 1).endAirportCity);
                        tvDate1.setText(Utils.getFlightDate(cities.get(0).startDate) + "\n" + mStop);
                        tvTime1.setText(Utils.getFlightTime(cities.get(0).startDate) + " - " + Utils.getFlightTime(cities.get(cities.size() - 1).endDate));
                        if (!TextUtils.isEmpty(cities.get(0).logo)) {
                            Picasso.get().load(cities.get(0).logo).into(ivAirline1);
                        }

                    }
                }
            } else if (mPosition == 1) {
                mFlightUid = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_UID, "");
                String sFlightReturnData = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_RETURN_DETAILS, "");
                mFlightReturnData = new Gson().fromJson(sFlightReturnData, FlightReturnListPojo.Data.class);

                mKey = mFlightReturnData.key;

                lnrReturn.setVisibility(View.VISIBLE);
                if (mFlightReturnData != null) {

                    int mDepartPos = 0, mReturnPos = 0;
                    if (!TextUtils.isEmpty(mFlightUid)) {
                        if (mFlightUid.split("-").length > 2) {
                            String depart = mFlightUid.split("-")[1];
                            String retrn = mFlightUid.split("-")[2];
                            if (!TextUtils.isEmpty(depart)) {
                                mDepartPos = Integer.parseInt(depart);
                            }
                            if (!TextUtils.isEmpty(retrn)) {
                                mReturnPos = Integer.parseInt(retrn);
                            }
                        }
                    }

                    //depart
                    List<FlightReturnListPojo.City> cities = mFlightReturnData.cities.get(0).get(mDepartPos);
                    if (cities != null && cities.size() > 0) {
                        String mStop = "";
                        if (cities.size() > 1) {
                            mStop = (cities.size() - 1) + " " + getString(R.string.stop);
                        } else {
                            mStop = getString(R.string.non_stop);
                        }
                        tvFlight1.setText(cities.get(0).startAirportCity + " " + getString(R.string.to) + " " + cities.get(cities.size() - 1).endAirportCity);
                        tvDate1.setText(Utils.getFlightTripDate(cities.get(0).startDate) + "\n" + mStop);
                        tvTime1.setText(Utils.getFlightTime(cities.get(0).startDate) + " - " + Utils.getFlightTime(cities.get(cities.size() - 1).endDate));
                        if (!TextUtils.isEmpty(cities.get(0).logo)) {
                            Picasso.get().load(cities.get(0).logo).into(ivAirline1);
                        }

                    }

                    //return
                    List<FlightReturnListPojo.City> cities1 = mFlightReturnData.cities.get(1).get(mReturnPos);
                    if (cities1 != null && cities1.size() > 0) {
                        String mStop = "";
                        if (cities1.size() > 1) {
                            mStop = (cities1.size() - 1) + " " + getString(R.string.stop);
                        } else {
                            mStop = getString(R.string.non_stop);
                        }
                        tvFlight2.setText(cities1.get(0).startAirportCity + " " + getString(R.string.to) + " " + cities1.get(cities1.size() - 1).endAirportCity);
                        tvDate2.setText(Utils.getFlightTripDate(cities1.get(0).startDate) + "\n" + mStop);
                        tvTime2.setText(Utils.getFlightTime(cities1.get(0).startDate) + " - " + Utils.getFlightTime(cities1.get(cities1.size() - 1).endDate));
                        if (!TextUtils.isEmpty(cities1.get(0).logo)) {
                            Picasso.get().load(cities1.get(0).logo).into(ivAirline2);
                        }

                    }
                }
            }

            mCurrency = mFlightOneDetailsPojo.data.currency;

            mFlightPaymentSummaryAdapter = new FlightPaymentSummaryAdapter(getActivity(), mCurrency);
            lvTicketPrice.setAdapter(mFlightPaymentSummaryAdapter);

            if (mFlightPaymentSummaryPojo != null) {

                List<FlightPaymentSummaryPojo.Summary> mPaymentSummaryList = mFlightPaymentSummaryPojo.data.fare;

                if (mPaymentSummaryList != null && mPaymentSummaryList.size() > 0) {

                    mTotalAmount = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).price;
                    //mTotalAmount = 100;
                    tigoAmount = mTotalAmount;
                    String title = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).lable;
                    if (!TextUtils.isEmpty(title)) {
                        tvPriceTitle.setText("" + title);
                    }

                    tvPrice.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(mTotalAmount));

                    mFlightPaymentSummaryAdapter.addAll(mPaymentSummaryList.subList(0, mPaymentSummaryList.size() - 1));
                }

                List<FlightPaymentSummaryPojo.PaymentAllowed> paymentAllowed = mFlightPaymentSummaryPojo.data.paymentAllowed;
                if (paymentAllowed != null && paymentAllowed.size() > 0) {
                    for (int i = 0; i < paymentAllowed.size(); i++) {
                        LogUtils.e("", "paymentName::" + paymentAllowed.get(i).paymentName);
                        Log.d("Tigo", "Tigo   " + paymentAllowed.get(i).paymentName);
                        if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("Credit/Debit Card")) {
                            sCRDBCharges = paymentAllowed.get(i).sExtraPGWCharges;
                            isCreditCardEnabled = true;
                            Log.d("Tigo", " credit true  ");
                        } else if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("Tigo Pesa")) {
                            sTigoCharges = paymentAllowed.get(i).sExtraPGWCharges;
                            isTigoEnabled = true;
                            Log.d("Tigo", " tigo true  ");

                        } else if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("MPESA")) {
                            sMPesaCharges = paymentAllowed.get(i).sExtraPGWCharges;
                            isMpesaEnabled = true;
                            Log.d("Tigo", " mpesa true  ");

                        } else if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("Airtel")) {
                            sAirtelCharges = paymentAllowed.get(i).sExtraPGWCharges;
                            isAirtelMoneyEnabled = true;
                            Log.d("Tigo", " airtel true  ");

                        }
                    }

                } else {
                    LogUtils.e("", "paymentAllowed is null");
                    isCreditCardEnabled = false;
                    isTigoEnabled = false;
                    isMpesaEnabled = false;
                    isAirtelMoneyEnabled = false;
                }

            }

            LogUtils.e("", "isCreditCardEnabled::" + isCreditCardEnabled);
            LogUtils.e("", "isTigoEnabled::" + isTigoEnabled);
            LogUtils.e("", "isMpesaEnabled::" + isMpesaEnabled);

            if (mPersoneList != null && mPersoneList.size() > 0) {
                JSONArray jsonArrayAdult = new JSONArray();
                JSONArray jsonArrayChild = new JSONArray();
                JSONArray jsonArrayInfant = new JSONArray();
                for (int i = 0; i < mPersoneList.size(); i++) {

                    if (mPersoneList.get(i).name.contains(getString(R.string.adult))) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("title", mPersoneList.get(i).type.toUpperCase());
                            jsonObject.put("firstName", mPersoneList.get(i).firstname);
                            jsonObject.put("middleName", TextUtils.isEmpty(mPersoneList.get(i).middlename) ? "" : mPersoneList.get(i).middlename);
                            jsonObject.put("lastName", mPersoneList.get(i).lastname);
//                            jsonObject.put("phone", mPersoneList.get(i).phone);
                            String ph = mFlightOneDetailsPojo.data.userPhone;
                            ph = ph.substring(1);
                            jsonObject.put("phone", ph);
                            //  jsonObject.put("phone", mFlightOneDetailsPojo.data.userPhone);
                            Log.d("Log", "DOB : " + mPersoneList.get(i).dob);
                            if (mPersoneList.get(i).dob == null) {
                                jsonObject.put("dob", "");
                            } else {
                                jsonObject.put("dob", mPersoneList.get(i).dob);
                            }
                            if (mPersoneList.get(i).mMeal != null) {
                                LogUtils.e("", "mPersoneList.get(i).mMeal not null for adult");
                                JSONObject jsonMeal = new JSONObject();
                                jsonMeal.put("code", mPersoneList.get(i).mMeal.code);
                                jsonMeal.put("remarks", mPersoneList.get(i).mMeal.name);
                                jsonMeal.put("price", mPersoneList.get(i).mMeal.price);
                                jsonMeal.put("quantity", "1");
                                jsonObject.put("meal", jsonMeal);
                            } else {
                                JSONObject jsonMeal = new JSONObject();
                                jsonMeal.put("code", "");
                                jsonMeal.put("remarks", "");
                                jsonMeal.put("price", "");
                                jsonMeal.put("quantity", "");
                                jsonObject.put("meal", jsonMeal);
                                LogUtils.e("", "mPersoneList.get(i).mMeal is null for adult");
                            }

                            if (mPersoneList.get(i).mBaggage != null) {
                                JSONObject jsonBaggage = new JSONObject();
                                jsonBaggage.put("code", mPersoneList.get(i).mBaggage.code);
                                jsonBaggage.put("remarks", mPersoneList.get(i).mBaggage.name);
                                jsonBaggage.put("price", mPersoneList.get(i).mBaggage.price);
                                jsonBaggage.put("quantity", "1");
                                jsonObject.put("baggagges", jsonBaggage);
                                LogUtils.e("", "mPersoneList.get(i).mBaggage not null for adult");
                            } else {
                                JSONObject jsonBaggage = new JSONObject();
                                jsonBaggage.put("code", "");
                                jsonBaggage.put("remarks", "");
                                jsonBaggage.put("price", "");
                                jsonBaggage.put("quantity", "");
                                jsonObject.put("baggagges", jsonBaggage);
                                LogUtils.e("", "mPersoneList.get(i).mBaggage is null for adult");
                            }

                            JSONObject jsonPassport = new JSONObject();
                            if (mPersoneList.get(i).passportIssueDate == null) {
                                jsonPassport.put("documentIssueDate", "");
                            } else {
                                jsonPassport.put("documentIssueDate", mPersoneList.get(i).passportIssueDate);
                            }
                            if (mPersoneList.get(i).passportExpDate == null) {
                                jsonPassport.put("documentExpiryDate", "");
                            } else {
                                jsonPassport.put("documentExpiryDate", mPersoneList.get(i).passportExpDate);
                            }
                            if (mPersoneList.get(i).citizenShip == null) {
                                jsonPassport.put("documentCitizenShip", "");
                            } else {
                                jsonPassport.put("documentCitizenShip", mPersoneList.get(i).citizenShip);
                            }
                            if (mPersoneList.get(i).issuedBy == null) {
                                jsonPassport.put("documentIssueBy", "");
                            } else {
                                jsonPassport.put("documentIssueBy", mPersoneList.get(i).issuedBy);
                            }
                            if (mPersoneList.get(i).passportNumber == null) {
                                jsonPassport.put("documentNumber", "");
                                jsonPassport.put("documentType", "");
                            } else {
                                jsonPassport.put("documentNumber", mPersoneList.get(i).passportNumber);
                                jsonPassport.put("documentType", "P");
                            }
                           /* if (mPersoneList.get(i). == null) {
                                jsonPassport.put("documentType", "P");
                            } else {
                                jsonPassport.put("documentType", mPersoneList.get(i).passportNumber);
                            }*/
//                            jsonPassport.put("documentType", TextUtils.isEmpty(mPersoneList.get(i).passportNumber) ? null : "P");
                            jsonObject.put("documents", jsonPassport);

                            jsonArrayAdult.put(jsonObject);


                            Log.d("Log", "Adult :- " + jsonArrayAdult);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (mPersoneList.get(i).name.contains(getString(R.string.child))) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("title", mPersoneList.get(i).type.toUpperCase());
                            jsonObject.put("firstName", mPersoneList.get(i).firstname);
                            jsonObject.put("middleName", TextUtils.isEmpty(mPersoneList.get(i).middlename) ? "" : mPersoneList.get(i).middlename);
                            jsonObject.put("lastName", mPersoneList.get(i).lastname);
//                            jsonObject.put("phone", mPersoneList.get(i).phone);
                            String ph = mFlightOneDetailsPojo.data.userPhone;
                            ph = ph.substring(1);
                            jsonObject.put("phone", ph);
                            //  jsonObject.put("phone", mFlightOneDetailsPojo.data.userPhone);
                            Log.d("Log", "DOB : " + mPersoneList.get(i).dob);
                            if (mPersoneList.get(i).dob == null) {
                                jsonObject.put("dob", "");
                            } else {
                                jsonObject.put("dob", mPersoneList.get(i).dob);
                            }
                            if (mPersoneList.get(i).mMeal != null) {
                                LogUtils.e("", "mPersoneList.get(i).mMeal not null for adult");
                                JSONObject jsonMeal = new JSONObject();
                                jsonMeal.put("code", mPersoneList.get(i).mMeal.code);
                                jsonMeal.put("remarks", mPersoneList.get(i).mMeal.name);
                                jsonMeal.put("price", mPersoneList.get(i).mMeal.price);
                                jsonMeal.put("quantity", "1");
                                jsonObject.put("meal", jsonMeal);
                            } else {
                                JSONObject jsonMeal = new JSONObject();
                                jsonMeal.put("code", "");
                                jsonMeal.put("remarks", "");
                                jsonMeal.put("price", "");
                                jsonMeal.put("quantity", "");
                                jsonObject.put("meal", jsonMeal);
                                LogUtils.e("", "mPersoneList.get(i).mMeal is null for adult");
                            }

                            if (mPersoneList.get(i).mBaggage != null) {
                                JSONObject jsonBaggage = new JSONObject();
                                jsonBaggage.put("code", mPersoneList.get(i).mBaggage.code);
                                jsonBaggage.put("remarks", mPersoneList.get(i).mBaggage.name);
                                jsonBaggage.put("price", mPersoneList.get(i).mBaggage.price);
                                jsonBaggage.put("quantity", "1");
                                jsonObject.put("baggagges", jsonBaggage);
                                LogUtils.e("", "mPersoneList.get(i).mBaggage not null for adult");
                            } else {
                                JSONObject jsonBaggage = new JSONObject();
                                jsonBaggage.put("code", "");
                                jsonBaggage.put("remarks", "");
                                jsonBaggage.put("price", "");
                                jsonBaggage.put("quantity", "");
                                jsonObject.put("baggagges", jsonBaggage);
                                LogUtils.e("", "mPersoneList.get(i).mBaggage is null for adult");
                            }

                            JSONObject jsonPassport = new JSONObject();
                            if (mPersoneList.get(i).passportIssueDate == null) {
                                jsonPassport.put("documentIssueDate", "");
                            } else {
                                jsonPassport.put("documentIssueDate", mPersoneList.get(i).passportIssueDate);
                            }
                            if (mPersoneList.get(i).passportExpDate == null) {
                                jsonPassport.put("documentExpiryDate", "");
                            } else {
                                jsonPassport.put("documentExpiryDate", mPersoneList.get(i).passportExpDate);
                            }
                            if (mPersoneList.get(i).citizenShip == null) {
                                jsonPassport.put("documentCitizenShip", "");
                            } else {
                                jsonPassport.put("documentCitizenShip", mPersoneList.get(i).citizenShip);
                            }
                            if (mPersoneList.get(i).issuedBy == null) {
                                jsonPassport.put("documentIssueBy", "");
                            } else {
                                jsonPassport.put("documentIssueBy", mPersoneList.get(i).issuedBy);
                            }
                            if (mPersoneList.get(i).passportNumber == null) {
                                jsonPassport.put("documentNumber", "");
                                jsonPassport.put("documentType", "");
                            } else {
                                jsonPassport.put("documentNumber", mPersoneList.get(i).passportNumber);
                                jsonPassport.put("documentType", "P");
                            }
                           /* if (mPersoneList.get(i). == null) {
                                jsonPassport.put("documentType", "P");
                            } else {
                                jsonPassport.put("documentType", mPersoneList.get(i).passportNumber);
                            }*/
//                            jsonPassport.put("documentType", TextUtils.isEmpty(mPersoneList.get(i).passportNumber) ? null : "P");
                            jsonObject.put("documents", jsonPassport);

                            jsonArrayChild.put(jsonObject);

                            Log.d("Log", "Child :- " + jsonArrayChild);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (mPersoneList.get(i).name.contains(getString(R.string.infant))) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("title", mPersoneList.get(i).type.toUpperCase());
                            jsonObject.put("firstName", mPersoneList.get(i).firstname);
                            jsonObject.put("middleName", TextUtils.isEmpty(mPersoneList.get(i).middlename) ? "" : mPersoneList.get(i).middlename);
                            jsonObject.put("lastName", mPersoneList.get(i).lastname);
//                            jsonObject.put("phone", mPersoneList.get(i).phone);
                            String ph = mFlightOneDetailsPojo.data.userPhone;
                            ph = ph.substring(1);
                            jsonObject.put("phone", ph);
                            //  jsonObject.put("phone", mFlightOneDetailsPojo.data.userPhone);
                            Log.d("Log", "DOB : " + mPersoneList.get(i).dob);
                            if (mPersoneList.get(i).dob == null) {
                                jsonObject.put("dob", "");
                            } else {
                                jsonObject.put("dob", mPersoneList.get(i).dob);
                            }
                            if (mPersoneList.get(i).mMeal != null) {
                                LogUtils.e("", "mPersoneList.get(i).mMeal not null for adult");
                                JSONObject jsonMeal = new JSONObject();
                                jsonMeal.put("code", mPersoneList.get(i).mMeal.code);
                                jsonMeal.put("remarks", mPersoneList.get(i).mMeal.name);
                                jsonMeal.put("price", mPersoneList.get(i).mMeal.price);
                                jsonMeal.put("quantity", "1");
                                jsonObject.put("meal", jsonMeal);
                            } else {
                                JSONObject jsonMeal = new JSONObject();
                                jsonMeal.put("code", "");
                                jsonMeal.put("remarks", "");
                                jsonMeal.put("price", "");
                                jsonMeal.put("quantity", "");
                                jsonObject.put("meal", jsonMeal);
                                LogUtils.e("", "mPersoneList.get(i).mMeal is null for adult");
                            }

                            if (mPersoneList.get(i).mBaggage != null) {
                                JSONObject jsonBaggage = new JSONObject();
                                jsonBaggage.put("code", mPersoneList.get(i).mBaggage.code);
                                jsonBaggage.put("remarks", mPersoneList.get(i).mBaggage.name);
                                jsonBaggage.put("price", mPersoneList.get(i).mBaggage.price);
                                jsonBaggage.put("quantity", "1");
                                jsonObject.put("baggagges", jsonBaggage);
                                LogUtils.e("", "mPersoneList.get(i).mBaggage not null for adult");
                            } else {
                                JSONObject jsonBaggage = new JSONObject();
                                jsonBaggage.put("code", "");
                                jsonBaggage.put("remarks", "");
                                jsonBaggage.put("price", "");
                                jsonBaggage.put("quantity", "");
                                jsonObject.put("baggagges", jsonBaggage);
                                LogUtils.e("", "mPersoneList.get(i).mBaggage is null for adult");
                            }

                            JSONObject jsonPassport = new JSONObject();
                            if (mPersoneList.get(i).passportIssueDate == null) {
                                jsonPassport.put("documentIssueDate", "");
                            } else {
                                jsonPassport.put("documentIssueDate", mPersoneList.get(i).passportIssueDate);
                            }
                            if (mPersoneList.get(i).passportExpDate == null) {
                                jsonPassport.put("documentExpiryDate", "");
                            } else {
                                jsonPassport.put("documentExpiryDate", mPersoneList.get(i).passportExpDate);
                            }
                            if (mPersoneList.get(i).citizenShip == null) {
                                jsonPassport.put("documentCitizenShip", "");
                            } else {
                                jsonPassport.put("documentCitizenShip", mPersoneList.get(i).citizenShip);
                            }
                            if (mPersoneList.get(i).issuedBy == null) {
                                jsonPassport.put("documentIssueBy", "");
                            } else {
                                jsonPassport.put("documentIssueBy", mPersoneList.get(i).issuedBy);
                            }
                            if (mPersoneList.get(i).passportNumber == null) {
                                jsonPassport.put("documentNumber", "");
                                jsonPassport.put("documentType", "");
                            } else {
                                jsonPassport.put("documentNumber", mPersoneList.get(i).passportNumber);
                                jsonPassport.put("documentType", "P");
                            }
                           /* if (mPersoneList.get(i). == null) {
                                jsonPassport.put("documentType", "P");
                            } else {
                                jsonPassport.put("documentType", mPersoneList.get(i).passportNumber);
                            }*/
//                            jsonPassport.put("documentType", TextUtils.isEmpty(mPersoneList.get(i).passportNumber) ? null : "P");
                            jsonObject.put("documents", jsonPassport);

                            jsonArrayInfant.put(jsonObject);

                            Log.d("Log", "Infant :- " + jsonArrayInfant);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                mAdult = jsonArrayAdult.toString();
                mChildren = jsonArrayChild.toString();
                mInfant = jsonArrayInfant.toString();
            }

            if (mFlightOneDetailsPojo != null && mFlightOneDetailsPojo.data != null) {
                tvName.setText(mFlightOneDetailsPojo.data.userName);
                tvEmail.setText(mFlightOneDetailsPojo.data.userPhone + ", " + mFlightOneDetailsPojo.data.userEmail);

                mAddonsPrice = 0;
                if (mPersoneList != null && mPersoneList.size() > 0) {
                    for (int i = 0; i < mPersoneList.size(); i++) {
                        if (mPersoneList.get(i).mMeal != null) {
                            mAddonsPrice = mAddonsPrice + mPersoneList.get(i).mMeal.price;
                        }

                        if (mPersoneList.get(i).mBaggage != null) {
                            mAddonsPrice = mAddonsPrice + mPersoneList.get(i).mBaggage.price;
                        }
                    }
                }

//                mTotalAmount = mFlightOneDetailsPojo.data.grandTotal + mAddonsPrice;

//                float grandPrice = mFlightOneDetailsPojo.data.grandTotal + mAddonsPrice;
//
//                cnvFixedFee = 0;
//                if (mFlightCity.cnvFixedFee > 0) {
//                    cnvFixedFee = mFlightCity.cnvFixedFee;
//                }
//
//                cnvPerFee = 0;
//                if (mFlightCity.cnvPerFee > 0) {
//                    cnvPerFee = (int) (grandPrice * ((float) mFlightCity.cnvPerFee / (float) 100));
//                }
//
//                cnvTotalFee = cnvFixedFee + cnvPerFee;
//
//                mTotalAmount = grandPrice + cnvTotalFee;
//
//                tvBaseFare.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(mFlightOneDetailsPojo.data.baseFare));
//                tvAddOns.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(mAddonsPrice));
//                tvFeeSurcharge.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(mFlightOneDetailsPojo.data.tax));
//                tvConvenienceCharge.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(cnvTotalFee));

//                tvPrice.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(mTotalAmount));
            }

//            if (mFlightCity != null && !TextUtils.isEmpty(mFlightCity.currencyName)) {
//                if (mFlightCity.currencyName.equalsIgnoreCase("usd")) {
//                    isTigoEnabled = false;
//                    isMpesaEnabled = false;
//                    isAirtelMoneyEnabled = false;
//                } else {
//                    isTigoEnabled = true;
//                    isMpesaEnabled = true;
//                    isAirtelMoneyEnabled = true;
//                }
//            }
        }

        tvBack.setOnClickListener(this);
        rltDebicCard.setOnClickListener(this);
        rltTigoPesa.setOnClickListener(this);
        rltMPesa.setOnClickListener(this);
        rltAirtelMoney.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {


        if (view == tvBack) {
            popBackStack();
        } else if (view == rltDebicCard) {

            mLimitAmount = 100;

            if (mFlightCity.currencyName.equalsIgnoreCase("usd")) {
                mLimitAmount = 1;
            } else {
                mLimitAmount = 100;
            }

            final Dialog dialog = new Dialog(getActivity());
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_card_info);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Spinner spinnerCardType = dialog.findViewById(R.id.spinnerCardType);
            EditText editTextCardNo = dialog.findViewById(R.id.editCardNo);
            EditText editTextCvv = dialog.findViewById(R.id.editCvv);
            TextView btSubmit = dialog.findViewById(R.id.tvSubmit);
            TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
            TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
            Spinner spinnerMonth = dialog.findViewById(R.id.spinnerMonth);
            Spinner spinnerYear = dialog.findViewById(R.id.spinnerYear);

            if (sCRDBCharges.equals("")){
                tvCharges.setVisibility(View.GONE);
            }else if (sCRDBCharges.equals("0.00")){
                tvCharges.setVisibility(View.GONE);
            }else {
                tvCharges.setText("Extra Charges Applicable : "+mCurrency+" "+sCRDBCharges);
                tvCharges.setVisibility(View.VISIBLE);
            }
            List<String> cardTypeList = new ArrayList<>();
            cardTypeList.add("Select Card Type");
            cardTypeList.add("Mastercard");
            cardTypeList.add("Visa");
            cardTypeList.add("American Express");
            cardTypeList.add("Amex Corporate Purchase Card");
            cardTypeList.add("Diners Club");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, cardTypeList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCardType.setAdapter(dataAdapter);
            editTextCardNo.addTextChangedListener(new TextWatcher() {
                private static final char space = ' ';
                int count = 0;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                 /*   int len=s.toString().length();

                    if (before == 0 && (len == 4 || len == 9 || len == 14 )) {
                        ccEditText.append(' ');
                    }*/
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (count <= editTextCardNo.getText().toString().length()
                            && (editTextCardNo.getText().toString().length() == 4
                            || editTextCardNo.getText().toString().length() == 9
                            || editTextCardNo.getText().toString().length() == 14)) {
                        editTextCardNo.setText(editTextCardNo.getText().toString() + " ");
                        int pos = editTextCardNo.getText().length();
                        editTextCardNo.setSelection(pos);
                    } else if (count >= editTextCardNo.getText().toString().length()
                            && (editTextCardNo.getText().toString().length() == 4
                            || editTextCardNo.getText().toString().length() == 9
                            || editTextCardNo.getText().toString().length() == 14)) {
                        editTextCardNo.setText(editTextCardNo.getText().toString().substring(0, editTextCardNo.getText().toString().length() - 1));
                        int pos = editTextCardNo.getText().length();
                        editTextCardNo.setSelection(pos);
                    }
                    count = editTextCardNo.getText().toString().length();
                }
            });


            year = Calendar.getInstance().get(Calendar.YEAR);
            expYear = year;
            List<String> yearList = new ArrayList<>();
            List<String> monthList = new ArrayList<>();
            for (int i = 0; i < 32; i++) {
                yearList.add("" + year);
                year++;
            }
            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, yearList);
            spinnerYear.setAdapter(yearAdapter);

            for (int i = month; i <= 12; i++) {
                if (i <= 9) {
                    monthList.add("0" + i);
                } else {
                    monthList.add("" + i);
                }
            }
            ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, monthList);
            spinnerMonth.setAdapter(monthAdapter);
/*
            spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int currnetYear = Integer.parseInt(spinnerYear.getSelectedItem().toString());
                    monthList.clear();
                    if (expYear == currnetYear) {
                        DateFormat dateFormat = new SimpleDateFormat("MM");
                        Date date = new Date();
                        Log.d("Month", dateFormat.format(date));
                        month = Integer.parseInt(dateFormat.format(date));
                        for (int i = month; i <= 12; i++) {
                            if (i <= 9) {
                                monthList.add("0" + i);
                            } else {
                                monthList.add("" + i);
                            }
                        }
                    } else {
                        for (int i = 1; i <= 12; i++) {
                            if (i <= 9) {
                                monthList.add("0" + i);
                            } else {
                                monthList.add("" + i);
                            }
                        }

                    }

                    ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, monthList);
                    spinnerMonth.setAdapter(monthAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
*/


            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        dialog.cancel();
                    }

                }
            });
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lastDigitYear;
                    String cardType = "";

                    if (spinnerCardType.getSelectedItem().toString().equals("Select Card Type")) {
                        Utils.showToast(getActivity(), getString(R.string.alert_card_card_type));
                        return;
                    } else {
                        strCardType = spinnerCardType.getSelectedItem().toString();
                        if (strCardType.equals("Mastercard")) {
                            cardType = "Mastercard";
                        } else if (strCardType.equals("Visa")) {
                            cardType = "Visa";
                        } else if (strCardType.equals("American Express")) {
                            cardType = "Amex";
                        } else if (strCardType.equals("Amex Corporate Purchase Card")) {
                            cardType = "AmexPurchaseCard";
                        } else if (strCardType.equals("Visa")) {
                            cardType = "Dinersclub";
                        }
                    }
                    if (TextUtils.isEmpty(editTextCardNo.getText().toString())) {
                        Utils.showToast(getActivity(), getString(R.string.alert_card_number));
                        return;
                    } else {
                        strCardNo = editTextCardNo.getText().toString().replaceAll("\\s", "");
                    }
                    if (editTextCardNo.getText().length() < 16) {
                        Utils.showToast(getActivity(), getString(R.string.alart_valid_card_number));
                        return;
                    } else {
                        strCardNo = editTextCardNo.getText().toString().replaceAll("\\s", "");
                    }

                    if (TextUtils.isEmpty(editTextCvv.getText().toString())) {
                        Utils.showToast(getActivity(), getString(R.string.alert_card_cvv));
                        return;
                    } else if (editTextCvv.getText().length() < 3) {
                        Utils.showToast(getActivity(), getString(R.string.aler_card_cvv_lenth));
                        return;
                    } else {
                        strCardCvv = editTextCvv.getText().toString();
                    }

                    strMonth = spinnerMonth.getSelectedItem().toString();
                    strYear = spinnerYear.getSelectedItem().toString();
                    lastDigitYear = strYear.substring(2, 4);


                    Log.d("Log", "exp " + strCardExpiryDt);
                    Log.d("Log", "cvv " + strCardCvv);
                    Log.d("Log", "Year " + lastDigitYear);

                    if (mTotalAmount >= mLimitAmount) {

                        if (!Utils.isInternetConnected(getActivity())) {
                            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                            return;
                        }

                        String mCustId = "";
                        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                            mCustId = "&cust_id=" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                        }

//                String mUrl = "https://testing.managemyticket.net/android/api/movie_migs_server.php?mv_screen=" + mvScreen + "&dt=" + mSelectedDate
//                        + "&show_time=" + mSelectedVdt + "&movie_id=" + mMovieID + "&max_seats=" + mSelectedSeatCount + "&cust_name=" + mUserFullName
//                        + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&combos=" + mSelectedFoodValue + "&mv_ukey=" + mUserKey + "&mv_mtid=" + mSelectedTimeId;

                        String depature_arv_date = "", return_arv_date = "", booking_type = "", connected_flights = "";

                        Calendar mStartCal = Calendar.getInstance();
                        mStartCal.setTimeInMillis(mFlightCity.displayDepartDate);

                        Log.d("Log", "Start Cal :- " + mFlightCity.displayDepartDate);
                        Log.d("Log", "Start Cal :- " + mStartCal);
                        Calendar mEndCal = Calendar.getInstance();
                        mEndCal.setTimeInMillis(mFlightCity.displayReturnDate);
                        Log.d("Log", "End Cal :- " + mFlightCity.displayReturnDate);
                        Log.d("Log", "End Cal : " + mEndCal);

                        int departureDuration = 0, returnDuration = 0;
                        String sDeparture = "", sReturnDeparture = "";
                        String sArriving = "", sReturnArriving = "";
                        if (mPosition == 0) {
                            booking_type = "one_way";
                            for (int i = 0; i < mFlightOneDetailsPojo.data.flightDetails.size(); i++) {
                                departureDuration = departureDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                                sDeparture = mFlightOneDetailsPojo.data.journeyStartDate;
                                sArriving = mFlightOneDetailsPojo.data.journeyEndDate;
                                Log.d("Log", "Departure : " + sDeparture);
                                Log.d("Log", "Arriving : " + sArriving);
                            }
                        } else if (mPosition == 1) {
                            booking_type = "return_way";
                            for (int i = 0; i < mFlightOneDetailsPojo.data.flightDetails.size(); i++) {
                                if (!TextUtils.isEmpty(mFlightOneDetailsPojo.data.flightDetails.get(i).flag)) {
                                    if (mFlightOneDetailsPojo.data.flightDetails.get(i).flag.equalsIgnoreCase("depature")) {
                                        sDeparture = mFlightOneDetailsPojo.data.journeyStartDate;
                                        sArriving = mFlightOneDetailsPojo.data.flightDetails.get(i).endDate;

                                        Log.d("Log", "Departure : " + sDeparture);
                                        Log.d("Log", "Arriving : " + sArriving);
                                        departureDuration = departureDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                                    } else if (mFlightOneDetailsPojo.data.flightDetails.get(i).flag.equalsIgnoreCase("return")) {
                                        if (sReturnDeparture.equals("")) {
                                            sReturnDeparture = mFlightOneDetailsPojo.data.flightDetails.get(i).startDate;
                                        }
                                        if (i == mFlightOneDetailsPojo.data.flightDetails.size() - 1) {
                                            sReturnArriving = mFlightOneDetailsPojo.data.flightDetails.get(i).endDate;
                                        }
                                        Log.d("Log", "Return Departure :- " + sReturnDeparture);
//                                        Log.d("Log", "Return Departure -:- " + sDepartureDate);
                                        returnDuration = returnDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                                    }
                                }
                            }
                        }

                        connected_flights = new Gson().toJson(mFlightOneDetailsPojo.data.flightDetails);

                        if (mFlightOneDetailsPojo.data != null) {
                            depature_arv_date = mFlightOneDetailsPojo.data.flightDetails.get(0).endDate;
                            if (mPosition == 1) {
                                return_arv_date = mFlightOneDetailsPojo.data.flightDetails.get(mFlightOneDetailsPojo.data.flightDetails.size() - 1).endDate;
                            }
                        }

                        Map<String, String> parameter = new HashMap<String, String>();
                        String mUrl = "https://www.managemyticket.net/android/api/" + Constants.API_FLIGHT_TITLE + "/crdb_payment_v1.php"/*?uuid=" + mFlightUid + "&key=" + mKey
                                + "&adults=" + mAdult + "&children=" + mChildren + "&infants=" + mInfant
                                + "&cust_email=" + mFlightOneDetailsPojo.data.userEmail + "&cust_mob=" + mFlightOneDetailsPojo.data.userPhone
                                + "&total=" + mTotalAmount + "&currency=" + mFlightOneDetailsPojo.data.currency + mCustId + "&start_city=" + mFlightCity.fromCity + "&end_city=" + mFlightCity.toCity
                                + "&class=" + mFlightCity.clasName + "&depature_date=" + DateFormate.sdfAirportServerDate.format(mStartCal.getTime())
                                + "&depature_arv_date=" + depature_arv_date + "&return_date=" + DateFormate.sdfAirportServerDate.format(mEndCal.getTime())
                                + "&return_arv_date=" + return_arv_date + "&booking_type=" + booking_type + "&is_booking_from=MOB&depature_duration=" + departureDuration
                                + "&return_duration=" + returnDuration + "&connected_flights=" + connected_flights + "&flight_auth_token=" + mFlightCity.flightAuthToken + "&airline_name=" + mFlightNameArray + "&promo_id=&ukey="*/;


                        parameter.put("careexp", lastDigitYear + strMonth);
                        parameter.put("cardno", strCardNo);
                        parameter.put("cardcvv", strCardCvv);
                        parameter.put("cardtype", cardType);
                        parameter.put("uuid", mFlightUid);
                        parameter.put("key", mKey);
                        parameter.put("adults", mAdult);
                        parameter.put("children", mChildren);
                        parameter.put("infants", mInfant);
                        parameter.put("cust_email", mFlightOneDetailsPojo.data.userEmail);
                        parameter.put("cust_mob", mFlightOneDetailsPojo.data.userPhone);
                        parameter.put("total", String.valueOf(mTotalAmount));
                        parameter.put("currency", mFlightOneDetailsPojo.data.currency + mCustId);
                        parameter.put("start_city", mFlightCity.fromCity);
                        parameter.put("end_city", mFlightCity.toCity);
                        parameter.put("class", mFlightCity.clasName);
//                        String sDepartureDate = DateFormate.sdfAirportServerDate.format(mStartCal.getTime());
//                        sDepartureDate = sDepartureDate.substring(0,11)+mFlightCity.

//                        parameter.put("depature_date", DateFormate.sdfAirportServerDate.format(mStartCal.getTime()));
                        parameter.put("depature_date", sDeparture);
                        parameter.put("depature_arv_date", sArriving);
                        parameter.put("return_date", sReturnDeparture);
                        parameter.put("return_arv_date", sReturnArriving);
                        parameter.put("booking_type", booking_type);
                        parameter.put("is_booking_from", "AND");
                        parameter.put("depature_duration", String.valueOf(departureDuration));
                        parameter.put("return_duration", String.valueOf(returnDuration));
                        parameter.put("connected_flights", connected_flights);
                        parameter.put("flight_auth_token", mFlightCity.flightAuthToken);
                        parameter.put("airline_name", mFlightNameArray);
                        parameter.put("promo_id", "");
                        parameter.put("ukey", "");


                        Log.d("Log", "careexp " + lastDigitYear + strMonth);
                        Log.d("Log", "cardno " + strCardNo);
                        Log.d("Log", "cardcvv " + strCardCvv);
                        Log.d("Log", "cardtype " + cardType);
                        Log.d("Log", "uuid " + mFlightUid);
                        Log.d("Log", "key " + mKey);
                        Log.d("Log", "adults" + mAdult);
                        Log.d("Log", "mChildren" + mChildren);

                        Log.d("Log", "infant" + mInfant);
                        Log.d("Log", "cust_email " + mFlightOneDetailsPojo.data.userEmail);
                        Log.d("Log", "cust_mob " + mFlightOneDetailsPojo.data.userPhone);
                        Log.d("Log", "total " + String.valueOf(mTotalAmount));
                        Log.d("Log", "currency " + mFlightOneDetailsPojo.data.currency + mCustId);
                        Log.d("Log", "start_city " + mFlightCity.fromCity);
                        Log.d("Log", "end_city " + mFlightCity.toCity);
                        Log.d("Log", "class" + mFlightCity.clasName);
                        Log.d("Log", "depature_date " + sDeparture);
                        Log.d("Log", "depature_arv_date " + sArriving);
                        Log.d("Log", "return_date " + sReturnDeparture);
                        Log.d("Log", "return_arv_date " + sReturnArriving);
                        Log.d("Log", "Date " + DateFormate.sdfAirportServerDate.format(mStartCal.getTime()));
                        Log.d("Log", "Adults " + mAdult);
                        Log.d("Log", "Childre" + mChildren);
                        Log.d("Log", "infant" + mInfant);

                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.BNDL_TITLE, getString(R.string.flight));
                        bundle.putString(Constants.BNDL_URL, mUrl);
                        bundle.putSerializable("postdata", (Serializable) parameter);
                        bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_DEBIT);
                        bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
                        bundle.putString(Constants.BNDL_FLIGHT_DETAILS_RESPONSE, new Gson().toJson(mFlightOneDetailsPojo));

                        switchFragment(FlightPaymentProcessFragment.newInstance(), FlightPaymentProcessFragment.Tag_FlightPaymentProcessFragment, bundle);

                        dialog.dismiss();

                        MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, 0l);
                        stopCountdown();

                        Utils.hideKeyboard(getActivity());

                    } else {
                        Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " " + mLimitAmount));
                    }


                }
            });


            dialog.show();


        } else if (view == rltTigoPesa) {

            if (!isTigoEnabled) {
                Utils.showToast(getActivity(), getString(R.string.msg_payment_disable_currency));
                return;
            }

            int mLimitAmount = 1000;

            if (mFlightCity.currencyName.equalsIgnoreCase("usd")) {
                mLimitAmount = 1;
            } else {
                mLimitAmount = 1000;
            }


            /*  if (mTotalAmount >= mLimitAmount) {*/

            final Dialog dialog = new Dialog(getActivity());
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_payment_mpesa);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
            final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
            TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
            TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
//                final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
            Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
            final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);

            tvTitle.setText("" + getString(R.string.tigo_pesa));
            if (sTigoCharges.equals("")){
                tvCharges.setVisibility(View.GONE);
            }else if (sTigoCharges.equals("0.00")){
                tvCharges.setVisibility(View.GONE);
            }else {
                tvCharges.setText("Extra Charges Applicable : "+mCurrency+" "+sTigoCharges);
                tvCharges.setVisibility(View.VISIBLE);
            }
            etMobileNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    etMobileNumber.setSelection(etMobileNumber.getText().length());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
            spinCountryCode.setAdapter(mCountryCodeSpinAdapter);
            spinCountryCode.setSelection(214);
            spinCountryCode.setEnabled(false);
            if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
            } else {
                spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
            }

            spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                    mMobInnerNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                    etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobInnerNumberMaxLength)});
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                        Utils.showToast(getActivity(), getString(R.string.alert_enter_phone));
                        return;
                    }

                    if (etMobileNumber.getText().toString().length() < mMobInnerNumberMaxLength || etMobileNumber.getText().toString().length() > mMobInnerNumberMaxLength) {
                        Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobInnerNumberMaxLength));
                        return;
                    }

                    if (!Utils.isInternetConnected(getActivity())) {
                        Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                        return;
                    }


                    String mCustId = "";
                    if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                        mCustId = "&cust_id=" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                    }

                    String depature_arv_date = "", return_arv_date = "", booking_type = "", connected_flights = "";

                    Calendar mStartCal = Calendar.getInstance();
                    mStartCal.setTimeInMillis(mFlightCity.displayDepartDate);

                    Calendar mEndCal = Calendar.getInstance();
                    mEndCal.setTimeInMillis(mFlightCity.displayReturnDate);

                    int departureDuration = 0, returnDuration = 0;
                    if (mPosition == 0) {
                        booking_type = "one_way";
                        for (int i = 0; i < mFlightOneDetailsPojo.data.flightDetails.size(); i++) {
                            departureDuration = departureDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                        }
                    } else if (mPosition == 1) {
                        booking_type = "return_way";
                        for (int i = 0; i < mFlightOneDetailsPojo.data.flightDetails.size(); i++) {
                            if (!TextUtils.isEmpty(mFlightOneDetailsPojo.data.flightDetails.get(i).flag)) {
                                if (mFlightOneDetailsPojo.data.flightDetails.get(i).flag.equalsIgnoreCase("depature")) {
                                    departureDuration = departureDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                                } else if (mFlightOneDetailsPojo.data.flightDetails.get(i).flag.equalsIgnoreCase("return")) {
                                    returnDuration = returnDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                                }
                            }
                        }
                    }

                    connected_flights = new Gson().toJson(mFlightOneDetailsPojo.data.flightDetails);

                    if (mFlightOneDetailsPojo.data != null) {
                        depature_arv_date = mFlightOneDetailsPojo.data.flightDetails.get(0).endDate;
                        if (mPosition == 1) {
                            return_arv_date = mFlightOneDetailsPojo.data.flightDetails.get(mFlightOneDetailsPojo.data.flightDetails.size() - 1).endDate;
                        }
                    }

                    String mUrl = "https://managemyticket.net/android/api/" + Constants.API_FLIGHT_TITLE + "/tigo_pesa_payment.php?uuid=" + mFlightUid + "&key=" + mKey
                            + "&adults=" + mAdult + "&children=" + mChildren + "&infants=" + mInfant
                            + "&cust_email=" + mFlightOneDetailsPojo.data.userEmail + "&cust_mob=" + mFlightOneDetailsPojo.data.userPhone
                            + "&total=" + tigoAmount + "&currency=" + mFlightOneDetailsPojo.data.currency + "&tigo=" + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code + ""
                            + etMobileNumber.getText().toString() + mCustId + "&start_city=" + mFlightCity.fromCity + "&end_city=" + mFlightCity.toCity
                            + "&class=" + mFlightCity.clasName + "&depature_date=" + DateFormate.sdfAirportServerDate.format(mStartCal.getTime())
                            + "&depature_arv_date=" + depature_arv_date + "&return_date=" + DateFormate.sdfAirportServerDate.format(mEndCal.getTime())
                            + "&return_arv_date=" + return_arv_date + "&booking_type=" + booking_type + "&is_booking_from=AND&depature_duration=" + departureDuration
                            + "&return_duration=" + returnDuration + "&connected_flights=" + connected_flights + "&flight_auth_token=" + mFlightCity.flightAuthToken + "&airline_name=" + mFlightNameArray + "&promo_id=&ukey=";


                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.BNDL_TITLE, getString(R.string.flight));
                    bundle.putString(Constants.BNDL_URL, mUrl);
                    bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_TIGO);
                    bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
                    bundle.putString(Constants.BNDL_FLIGHT_DETAILS_RESPONSE, new Gson().toJson(mFlightOneDetailsPojo));

                    switchFragment(FlightPaymentProcessFragment.newInstance(), FlightPaymentProcessFragment.Tag_FlightPaymentProcessFragment, bundle);

                    MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, 0l);
                    stopCountdown();

                    tvCancel.performClick();
                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        dialog.cancel();
                        Utils.hideKeyboard(getActivity());
                    }
                }
            });

            dialog.show();

           /* } else {
                Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " " + mLimitAmount));
            }*/

        } else if (view == rltMPesa) {

            if (!isMpesaEnabled) {
                Utils.showToast(getActivity(), getString(R.string.msg_payment_disable_currency));
                return;
            }

            int mLimitAmount = 100;

            if (mFlightCity.currencyName.equalsIgnoreCase("usd")) {
                mLimitAmount = 1;
            } else {
                mLimitAmount = 100;
            }

            if (mTotalAmount >= mLimitAmount) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_payment_mpesa);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
                TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
//                final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);
                if (sMPesaCharges.equals("")){
                    tvCharges.setVisibility(View.GONE);
                }else if (sMPesaCharges.equals("0.00")){
                    tvCharges.setVisibility(View.GONE);
                }else {
                    tvCharges.setText("Extra Charges Applicable : "+mCurrency+" "+sMPesaCharges);
                    tvCharges.setVisibility(View.VISIBLE);
                }
                tvTitle.setText("" + getString(R.string.mpesa));

                etMobileNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        etMobileNumber.setSelection(etMobileNumber.getText().length());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
                spinCountryCode.setAdapter(mCountryCodeSpinAdapter);
                spinCountryCode.setSelection(214);
                spinCountryCode.setEnabled(false);
                if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                    spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
                } else {
                    spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
                }

                spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                        mMobInnerNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                        etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobInnerNumberMaxLength)});
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                tvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                            Utils.showToast(getActivity(), getString(R.string.alert_enter_phone));
                            return;
                        }

                        if (etMobileNumber.getText().toString().length() < mMobInnerNumberMaxLength || etMobileNumber.getText().toString().length() > mMobInnerNumberMaxLength) {
                            Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobInnerNumberMaxLength));
                            return;
                        }

                        if (!Utils.isInternetConnected(getActivity())) {
                            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                            return;
                        }

                        String mCustId = "";
                        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                            mCustId = "&cust_id=" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                        }

                        String depature_arv_date = "", return_arv_date = "", booking_type = "", connected_flights = "";

                        Calendar mStartCal = Calendar.getInstance();
                        mStartCal.setTimeInMillis(mFlightCity.displayDepartDate);

                        Calendar mEndCal = Calendar.getInstance();
                        mEndCal.setTimeInMillis(mFlightCity.displayReturnDate);

                        int departureDuration = 0, returnDuration = 0;
                        if (mPosition == 0) {
                            booking_type = "one_way";
                            for (int i = 0; i < mFlightOneDetailsPojo.data.flightDetails.size(); i++) {
                                departureDuration = departureDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                            }
                        } else if (mPosition == 1) {
                            booking_type = "return_way";
                            for (int i = 0; i < mFlightOneDetailsPojo.data.flightDetails.size(); i++) {
                                if (!TextUtils.isEmpty(mFlightOneDetailsPojo.data.flightDetails.get(i).flag)) {
                                    if (mFlightOneDetailsPojo.data.flightDetails.get(i).flag.equalsIgnoreCase("depature")) {
                                        departureDuration = departureDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                                    } else if (mFlightOneDetailsPojo.data.flightDetails.get(i).flag.equalsIgnoreCase("return")) {
                                        returnDuration = returnDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                                    }
                                }
                            }
                        }

                        connected_flights = new Gson().toJson(mFlightOneDetailsPojo.data.flightDetails);

                        if (mFlightOneDetailsPojo.data != null) {
                            depature_arv_date = mFlightOneDetailsPojo.data.flightDetails.get(0).endDate;
                            if (mPosition == 1) {
                                return_arv_date = mFlightOneDetailsPojo.data.flightDetails.get(mFlightOneDetailsPojo.data.flightDetails.size() - 1).endDate;
                            }
                        }

                        String mUrl = "https://managemyticket.net/android/api/" + Constants.API_FLIGHT_TITLE + "/mpesa_payment.php?uuid=" + mFlightUid + "&key=" + mKey
                                + "&adults=" + mAdult + "&children=" + mChildren + "&infants=" + mInfant
                                + "&cust_email=" + mFlightOneDetailsPojo.data.userEmail + "&cust_mob=" + mFlightOneDetailsPojo.data.userPhone
                                + "&total=" + mTotalAmount + "&currency=" + mFlightOneDetailsPojo.data.currency + "&mpesa=" + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code + ""
                                + etMobileNumber.getText().toString() + mCustId + "&start_city=" + mFlightCity.fromCity + "&end_city=" + mFlightCity.toCity
                                + "&class=" + mFlightCity.clasName + "&depature_date=" + DateFormate.sdfAirportServerDate.format(mStartCal.getTime())
                                + "&depature_arv_date=" + depature_arv_date + "&return_date=" + DateFormate.sdfAirportServerDate.format(mEndCal.getTime())
                                + "&return_arv_date=" + return_arv_date + "&booking_type=" + booking_type + "&is_booking_from=AND&depature_duration=" + departureDuration
                                + "&return_duration=" + returnDuration + "&connected_flights=" + connected_flights + "&flight_auth_token=" + mFlightCity.flightAuthToken + "&airline_name=" + mFlightNameArray + "&promo_id=&ukey=";

                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.BNDL_TITLE, getString(R.string.flight));
                        bundle.putString(Constants.BNDL_URL, mUrl);
                        bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_MPESA);
                        bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
                        bundle.putString(Constants.BNDL_FLIGHT_DETAILS_RESPONSE, new Gson().toJson(mFlightOneDetailsPojo));

                        switchFragment(FlightPaymentProcessFragment.newInstance(), FlightPaymentProcessFragment.Tag_FlightPaymentProcessFragment, bundle);

                        MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, 0l);
                        stopCountdown();

                        tvCancel.performClick();
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                            Utils.hideKeyboard(getActivity());
                        }
                    }
                });

                dialog.show();

            } else {
                Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " " + mLimitAmount));
            }

        } else if (view == rltAirtelMoney) {

            if (!isAirtelMoneyEnabled) {
                Utils.showToast(getActivity(), getString(R.string.msg_payment_disable_currency));
                return;
            }

            int mLimitAmount = 100;

            if (mFlightCity.currencyName.equalsIgnoreCase("usd")) {
                mLimitAmount = 1;
            } else {
                mLimitAmount = 100;
            }

            if (mTotalAmount >= mLimitAmount) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_payment_mpesa);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
                TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
//                final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);
                if (sAirtelCharges.equals("")){
                    tvCharges.setVisibility(View.GONE);
                }else if (sAirtelCharges.equals("0.00")){
                    tvCharges.setVisibility(View.GONE);
                }else {
                    tvCharges.setText("Extra Charges Applicable : "+mCurrency+" "+sAirtelCharges);
                    tvCharges.setVisibility(View.VISIBLE);
                }
                tvTitle.setText("" + getString(R.string.airtel_money));

                etMobileNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        etMobileNumber.setSelection(etMobileNumber.getText().length());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
                spinCountryCode.setAdapter(mCountryCodeSpinAdapter);
                spinCountryCode.setSelection(214);
                spinCountryCode.setEnabled(false);
                if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                    spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
                } else {
                    spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
                }

                spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                        mMobInnerNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                        etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobInnerNumberMaxLength)});
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                tvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                            Utils.showToast(getActivity(), getString(R.string.alert_enter_phone));
                            return;
                        }

                        if (etMobileNumber.getText().toString().length() < mMobInnerNumberMaxLength || etMobileNumber.getText().toString().length() > mMobInnerNumberMaxLength) {
                            Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobInnerNumberMaxLength));
                            return;
                        }

                        if (!Utils.isInternetConnected(getActivity())) {
                            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                            return;
                        }

                        String mCustId = "";
                        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                            mCustId = "&cust_id=" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                        }

                        String depature_arv_date = "", return_arv_date = "", booking_type = "", connected_flights = "";

                        Calendar mStartCal = Calendar.getInstance();
                        mStartCal.setTimeInMillis(mFlightCity.displayDepartDate);

                        Calendar mEndCal = Calendar.getInstance();
                        mEndCal.setTimeInMillis(mFlightCity.displayReturnDate);

                        int departureDuration = 0, returnDuration = 0;
                        if (mPosition == 0) {
                            booking_type = "one_way";
                            for (int i = 0; i < mFlightOneDetailsPojo.data.flightDetails.size(); i++) {
                                departureDuration = departureDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                            }
                        } else if (mPosition == 1) {
                            booking_type = "return_way";
                            for (int i = 0; i < mFlightOneDetailsPojo.data.flightDetails.size(); i++) {
                                if (!TextUtils.isEmpty(mFlightOneDetailsPojo.data.flightDetails.get(i).flag)) {
                                    if (mFlightOneDetailsPojo.data.flightDetails.get(i).flag.equalsIgnoreCase("depature")) {
                                        departureDuration = departureDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                                    } else if (mFlightOneDetailsPojo.data.flightDetails.get(i).flag.equalsIgnoreCase("return")) {
                                        returnDuration = returnDuration + mFlightOneDetailsPojo.data.flightDetails.get(i).duration;
                                    }
                                }
                            }
                        }

                        connected_flights = new Gson().toJson(mFlightOneDetailsPojo.data.flightDetails);

                        if (mFlightOneDetailsPojo.data != null) {
                            depature_arv_date = mFlightOneDetailsPojo.data.flightDetails.get(0).endDate;
                            return_arv_date = mFlightOneDetailsPojo.data.flightDetails.get(mFlightOneDetailsPojo.data.flightDetails.size() - 1).endDate;
                        }

                        String mUrl = "https://managemyticket.net/android/api/" + Constants.API_FLIGHT_TITLE + "/flight_airtel.php?uuid=" + mFlightUid + "&key=" + mKey
                                + "&adults=" + mAdult + "&children=" + mChildren + "&infants=" + mInfant
                                + "&cust_email=" + mFlightOneDetailsPojo.data.userEmail + "&cust_mob=" + mFlightOneDetailsPojo.data.userPhone
                                + "&total=" + mTotalAmount + "&currency=" + mFlightOneDetailsPojo.data.currency + "&airtel_term=1&airtel=" + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code + ""
                                + etMobileNumber.getText().toString() + mCustId + "&start_city=" + mFlightCity.fromCity + "&end_city=" + mFlightCity.toCity
                                + "&class=" + mFlightCity.clasName + "&depature_date=" + DateFormate.sdfAirportServerDate.format(mStartCal.getTime())
                                + "&depature_arv_date=" + depature_arv_date + "&return_date=" + DateFormate.sdfAirportServerDate.format(mEndCal.getTime())
                                + "&return_arv_date=" + return_arv_date + "&booking_type=" + booking_type + "&is_booking_from=AND&depature_duration=" + departureDuration
                                + "&return_duration=" + returnDuration + "&connected_flights=" + connected_flights + "&flight_auth_token=" + mFlightCity.flightAuthToken + "&airline_name=" + mFlightNameArray + "&promo_id=&ukey=";

                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.BNDL_TITLE, getString(R.string.flight));
                        bundle.putString(Constants.BNDL_URL, mUrl);
                        bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_AIRTEL);
                        bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
                        bundle.putString(Constants.BNDL_FLIGHT_DETAILS_RESPONSE, new Gson().toJson(mFlightOneDetailsPojo));

                        switchFragment(FlightPaymentProcessFragment.newInstance(), FlightPaymentProcessFragment.Tag_FlightPaymentProcessFragment, bundle);

                        MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, 0l);
                        stopCountdown();

                        tvCancel.performClick();
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                            Utils.hideKeyboard(getActivity());
                        }
                    }
                });

                dialog.show();

            } else {
                Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " " + mLimitAmount));
            }

        }


    }

    private void stopCountdown() {
        LogUtils.e("", "stopCountdown");
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
            lnrSessionTime.setVisibility(View.GONE);
        }
    }

    private void startTimer(long msUntilFinished) {

        long millis = msUntilFinished;
        String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        tvSession.setText(String.format(getString(R.string.sessing_five_minute), hms));

        lnrSessionTime.setVisibility(View.VISIBLE);

        MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, millis);

        mCountDownTimer = new CountDownTimer(msUntilFinished, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                tvSession.setText(String.format(getString(R.string.sessing_five_minute), hms));

                MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, millis);
            }

            public void onFinish() {
                mCountDownTimer = null;
                MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, 0l);
                Utils.showToast(getActivity(), getString(R.string.msg_session_expire));
                if (mPosition == 0) {
                    popBackStack(FlightOneAirlineListFragment.Tag_FlightOneAirlineListFragment);
                } else if (mPosition == 1) {
                    popBackStack(FlightReturnDepartureAirlineListFragment.Tag_FlightReturnDepartureAirlineListFragment);
                } else {
                    popBackStack(ServiceFragment.Tag_ServiceFragment);
                }
            }
        }.start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopCountdown();
    }

}
