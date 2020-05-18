package com.otapp.net.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.LoginActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.CountryCodeListAdapter;
import com.otapp.net.adapter.CountryCodeSpinAdapter;
import com.otapp.net.adapter.FlightDetailsAdapter;
import com.otapp.net.adapter.FlightPaymentSummaryAdapter;
import com.otapp.net.adapter.FlightPersonAdapter;
import com.otapp.net.adapter.FlightPersonDetailsAdapter;
import com.otapp.net.adapter.FlightRuleAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.model.FlightOneDetailsPojo;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.model.FlightPaymentSummaryPojo;
import com.otapp.net.model.FlightPerson;
import com.otapp.net.model.FlightReturnListPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FlightOneDetailsFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_FlightOneDetailsFragment = "Tag_" + "FlightOneDetailsFragment";
    View mView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTermsCondition)
    TextView tvTermsCondition;
    @BindView(R.id.tvPerson)
    TextView tvPerson;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvAlert)
    TextView tvAlert;
    @BindView(R.id.tvProceed)
    TextView tvProceed;
    @BindView(R.id.tvSecureTrip)
    TextView tvSecureTrip;
    @BindView(R.id.tvLoginText)
    TextView tvLoginText;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvLoginCheckmark)
    TextView tvLoginCheckmark;
    //    @BindView(R.id.tvCountryCode)
//    TextView tvCountryCode;
    @BindView(R.id.spinCountryCode)
    TextView spinCountryCode;
    @BindView(R.id.tvSession)
    TextView tvSession;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;
    @BindView(R.id.lnrSessionTime)
    LinearLayout lnrSessionTime;
    @BindView(R.id.lnrPrice)
    LinearLayout lnrPrice;
    @BindView(R.id.lnrTraveller)
    LinearLayout lnrTraveller;
    @BindView(R.id.rltCancellationBaggage)
    RelativeLayout rltCancellationBaggage;
    @BindView(R.id.switchTranvelInsurance)
    Switch switchTranvelInsurance;
    @BindView(R.id.lvPerson)
    ListView lvPerson;
    @BindView(R.id.lvFlightList)
    ListView lvFlightList;
    @BindView(R.id.nsvView)
    NestedScrollView nsvView;

    String stop = "";
    FlightPersonAdapter mFlightPersonAdapter;

    FlightPersonDetailsAdapter flightPersonDetailsAdapter;

    FlightOneListPojo.Data mFlightData;
    FlightOneDetailsPojo mFlightOneDetailsPojo;
    FlightCity mFlightCity;
    List<FlightCityPojo.City> mFlightCityList;
    List<FlightCityPojo.City> mNationalCountryList = new ArrayList<>();
    List<String> mNationalityList = new ArrayList<>();

    FlightReturnListPojo.Data mFlightReturnData;
    String mFlightUid = "", mKey = "";

    List<CountryCodePojo.CountryCode> tempCountryCodeList;
    CountryCodeListAdapter mCountryCodeSpinAdapter;
    int countryCodeFlag=0;

    private int mPosition = -1;
    private float mAddonsPrice = 0;

    List<FlightOneDetailsPojo.FlightDetail> mFlightDetailsList;
    FlightDetailsAdapter mFlightDetailsAdapter;
    List<FlightPerson> mPersoneList = new ArrayList<>();

    private boolean isLoginCheckmarkDone = false;
    private int mMobNumberMaxLength = 9;
    private String mFlightNameArray = "";

    private FlightPaymentSummaryPojo mFlightPaymentSummaryPojo;
    List<FlightPaymentSummaryPojo.Summary> mPaymentSummaryList;


    List<FlightReturnListPojo.Data> mFlightRetrunList = new ArrayList<>();

    private CountDownTimer countDownTimer;

    public static FlightOneDetailsFragment newInstance() {
        FlightOneDetailsFragment fragment = new FlightOneDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_one_flight_details, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();
       /* mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    View view = getActivity().getCurrentFocus();
                    if(view!=null){
                        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                        Toast.makeText(getContext(), "Close", Toast.LENGTH_SHORT).show();
                    }else {
                        popBackStack();
                    }
                }
                return true;
            }
        });*/
        return mView;
    }

    private void InitializeControls() {

        tempCountryCodeList= new ArrayList<>();
        spinCountryCode.setText("+255");
        if (Otapp.mCountryCodePojoList == null || Otapp.mCountryCodePojoList.size() == 0) {
            getCountryCodeList();
        } else {
           // setSpinCountryCode();
        }

        tvSecureTrip.setText(String.format(getString(R.string.secure_trip), "249"));

        if (MyPref.getPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, 0l) > 0l) {
            startTimer(MyPref.getPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, 0l));
        } else {
            startTimer(10 * 60 * 1000);
//            startTimer(5 * 60 * 1000);
        }

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      //          etEmail.setSelection(etEmail.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      //          etMobileNumber.setSelection(etMobileNumber.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spinCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dailog_spin_country_code);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                EditText editSearchCountryCode= dialog.findViewById(R.id.searchCountryCode);
                ListView listCountryCode= dialog.findViewById(R.id.listViewCountryCode);
                TextView tvCancle= dialog.findViewById(R.id.tvCancel);

                mCountryCodeSpinAdapter= new CountryCodeListAdapter(getActivity(),Otapp.mCountryCodePojoList);
                listCountryCode.setAdapter(mCountryCodeSpinAdapter);

                listCountryCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(countryCodeFlag==1){
                            if(tempCountryCodeList.get(position).code.equals("")){
                                spinCountryCode.setText("+255");
                            }else {
                                spinCountryCode.setText(tempCountryCodeList.get(position).code);
                                closeKeyboard();
                            }
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
                            countryCodeFlag=0;
                        }else {
                            if(Otapp.mCountryCodePojoList.get(position).code.equals("")){
                                spinCountryCode.setText("+255");
                            }else {
                                spinCountryCode.setText(Otapp.mCountryCodePojoList.get(position).code);
                                closeKeyboard();
                            }
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
                        }
                        dialog.dismiss();
                    }
                });

                editSearchCountryCode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String search=s.toString();
                        tempCountryCodeList= new ArrayList<>();
                        if(search.equals("")){
                            mCountryCodeSpinAdapter= new CountryCodeListAdapter(getActivity(),Otapp.mCountryCodePojoList);
                            listCountryCode.setAdapter(mCountryCodeSpinAdapter);
                            mCountryCodeSpinAdapter.notifyDataSetChanged();
                            countryCodeFlag=0;
                        }else {
                            for(int i=0;i<Otapp.mCountryCodePojoList.size();i++){

                                if(Otapp.mCountryCodePojoList.get(i).name.toUpperCase().startsWith(search.toUpperCase())||Otapp.mCountryCodePojoList.get(i).code.replaceAll("\\+","").startsWith(search)){
                                    tempCountryCodeList.add(Otapp.mCountryCodePojoList.get(i));
                                    countryCodeFlag=1;
                                }

                            }
                            if(tempCountryCodeList.size()==0) {
                                Utils.showToast(getActivity(), "No Country Code Found");
                            }
                            mCountryCodeSpinAdapter = new CountryCodeListAdapter(getActivity(), tempCountryCodeList);
                            listCountryCode.setAdapter(mCountryCodeSpinAdapter);
                            mCountryCodeSpinAdapter.notifyDataSetChanged();

                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                tvCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        Bundle bundle = getArguments();
        if (bundle != null) {
            mPosition = MyPref.getPref(getContext(), Constants.CITY_TYPE_POSITION, 0);
            String sFlightCity = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT, "");
            mFlightCity = new Gson().fromJson(sFlightCity, FlightCity.class);

            String sFlightCityList = MyPref.getPref(getContext(), Constants.BNDL_CITY_LIST, "");
            mFlightCityList = new Gson().fromJson(sFlightCityList, new TypeToken<ArrayList<FlightCityPojo.City>>() {
//            mFlightCity = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT), FlightCity.class);
//            mFlightCityList = new Gson().fromJson(bundle.getString(Constants.BNDL_CITY_LIST), new TypeToken<ArrayList<FlightCityPojo.City>>() {
            }.getType());


            mFlightNameArray = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_NAME_LIST, "");

            if (mFlightCityList != null && mFlightCityList.size() > 0) {
//                LogUtils.e("", "mFlightCityList size:" + mFlightCityList.size());
                for (int i = 0; i < mFlightCityList.size(); i++) {
//                    LogUtils.e("", mFlightCityList.get(i).countryName + " " + (mNationalityList.contains(mFlightCityList.get(i).countryName) ? "true" : "false"));
                    if (!mNationalityList.contains(mFlightCityList.get(i).countryName)) {
//                        LogUtils.e("", mFlightCityList.get(i).countryName+" Added");
                        mNationalityList.add(mFlightCityList.get(i).countryName);
                        mNationalCountryList.add(mFlightCityList.get(i));
                    }
                }
            }

            LogUtils.e("", "mNationalityList::" + mNationalityList);
        /*    mFlightDetailsAdapter = new FlightDetailsAdapter(getActivity(), this, mFlightCity.clasName);
            lvFlightList.setAdapter(mFlightDetailsAdapter);*/

            if (mFlightCity != null) {
                tvPerson.setText("" + mFlightCity.traveller);

                if (mPersoneList != null && mPersoneList.size() > 0) {
                    mPersoneList.clear();
                }

                if (mFlightCity.adultCount > 0) {
                    for (int i = 0; i < mFlightCity.adultCount; i++) {
                        FlightPerson mFlightPerson = new FlightPerson();
                        mFlightPerson.name = getString(R.string.adult) + " " + (i + 1);
                        mPersoneList.add(mFlightPerson);
                    }
                }

                if (mFlightCity.childCount > 0) {
                    for (int i = 0; i < mFlightCity.childCount; i++) {
                        FlightPerson mFlightPerson = new FlightPerson();
                        mFlightPerson.name = getString(R.string.child) + " " + (i + 1);
                        mPersoneList.add(mFlightPerson);
                    }
                }

                if (mFlightCity.infantCount > 0) {
                    for (int i = 0; i < mFlightCity.infantCount; i++) {
                        FlightPerson mFlightPerson = new FlightPerson();
                        mFlightPerson.name = getString(R.string.infant) + " " + (i + 1);
                        mPersoneList.add(mFlightPerson);
                    }
                }
            }


//            ViewTreeObserver viewTreeObserver = lvPerson.getViewTreeObserver();
//            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    int height = lvPerson.getMeasuredHeight();
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lnrTraveller.getLayoutParams();
//                    LogUtils.e("", "params.height::"+params.height);
//                    params.height = (int)(getResources().getDimension(R.dimen._40sdp) + height);
//                    LogUtils.e("", "params.height after::"+params.height);
//                    lnrTraveller.setLayoutParams(params);
//                    lnrTraveller.requestLayout();
////                    lvPerson.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//            });


//            mPosition = bundle.getInt(Constants.CITY_TYPE_POSITION);
            if (mPosition == 0) {
                String sFlightData = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_ONE_DETAILS, "");
                mFlightData = new Gson().fromJson(sFlightData, FlightOneListPojo.Data.class);
                mFlightUid = mFlightData.uid;
                mKey = mFlightData.key;
                if (mFlightData != null) {
                    getFlightDetails();
                } else {
                    LogUtils.e("", "mFlightData  is null");
                }
                mFlightDetailsAdapter = new FlightDetailsAdapter(getActivity(), this, mFlightCity.clasName,mFlightData);
                lvFlightList.setAdapter(mFlightDetailsAdapter);
            } else if (mPosition == 1) {

                mFlightUid = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_UID, "");
                String sFlightReturnData = MyPref.getPref(getContext(), Constants.BNDL_FLIGHT_RETURN_DETAILS, "");
                mFlightReturnData = new Gson().fromJson(sFlightReturnData, FlightReturnListPojo.Data.class);
                mKey = mFlightReturnData.key;
                if (!TextUtils.isEmpty(mFlightUid)) {
                    getFlightDetails();
                }

                mFlightDetailsAdapter = new FlightDetailsAdapter(getActivity(), this, mFlightCity.clasName,mFlightReturnData);
                lvFlightList.setAdapter(mFlightDetailsAdapter);
            }

        }

        checkLoggedInUser();


        tvBack.setOnClickListener(this);
        tvAlert.setOnClickListener(this);
        tvProceed.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvLoginCheckmark.setOnClickListener(this);
        rltCancellationBaggage.setOnClickListener(this);
    }


/*
    private void setSpinCountryCode() {

        CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
        spinCountryCode.setAdapter(mCountryCodeSpinAdapter);
        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
            spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
        } else {
            spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
        }
        spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                mMobNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobNumberMaxLength)});
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
*/

    private void getFlightDetails() {

        LogUtils.e("", "getFlightDetails call");

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }


        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<FlightOneDetailsPojo> mCall = null;
        if (mPosition == 0) {
            mCall = mApiInterface.getFlightOneDetails(mFlightData.uid, mFlightData.key, mFlightCity.currencyName, Otapp.mUniqueID, mFlightCity.flightAuthToken);
        } else {
            mCall = mApiInterface.getFlightReturnDetails(mFlightUid, mFlightReturnData.key, mFlightCity.currencyName, Otapp.mUniqueID, mFlightCity.flightAuthToken);
        }

        mCall.enqueue(new Callback<FlightOneDetailsPojo>() {
            @Override
            public void onResponse(Call<FlightOneDetailsPojo> call, Response<FlightOneDetailsPojo> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    mFlightOneDetailsPojo = response.body();
                    if (mFlightOneDetailsPojo != null) {
                        if (mFlightOneDetailsPojo.status.equalsIgnoreCase("200")) {
                            Log.d("Log", "Print ");

                            setFlightDetails();
                            getFlightPaymentSummary();

                        } else {

                            Utils.showToast(getActivity(), "" + mFlightOneDetailsPojo.message);
                            mFlightOneDetailsPojo = null;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FlightOneDetailsPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void getFlightPaymentSummary() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }

        Map<String, String> jsonParams = new HashMap<>();
        if (mPosition == 0) {

            jsonParams.put("uid", "" + mFlightData.uid);
            jsonParams.put("key", "" + mFlightData.key);

        } else {
            jsonParams.put("uid", "" + mFlightUid);
            jsonParams.put("key", "" + mFlightReturnData.key);
        }

        jsonParams.put("flight_auth_token", "" + mFlightCity.flightAuthToken);
        jsonParams.put("user_token", "" + Otapp.mUniqueID);
        jsonParams.put("promo_id", "");
        jsonParams.put("ukey", "");
        jsonParams.put("add_on_price", "" + mAddonsPrice);
        jsonParams.put("totalpassenger", ""+mPersoneList.size() );




        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<FlightPaymentSummaryPojo> mCall = mApiInterface.getFlightPaymentSummary(jsonParams);
        mCall.enqueue(new Callback<FlightPaymentSummaryPojo>() {
            @Override
            public void onResponse(Call<FlightPaymentSummaryPojo> call, Response<FlightPaymentSummaryPojo> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    mFlightPaymentSummaryPojo = response.body();
                    if (mFlightPaymentSummaryPojo != null) {
                        if (mFlightPaymentSummaryPojo.status.equalsIgnoreCase("200")) {
                            JSONObject jsonObjectResponse = null;
                            try {
                                jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                                Log.d("Log", "Fare Response : " + jsonObjectResponse);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mPaymentSummaryList = mFlightPaymentSummaryPojo.data.fare;
                            if (mPaymentSummaryList != null && mPaymentSummaryList.size() > 0) {
                                float mTotalPrice = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).price;
                            //    float mTotalPrice = 100;
                                String title = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).lable;
//                                if (!TextUtils.isEmpty(title)) {
//                                    tvGrandTotalTitle.setText("" + title);
//                                }

                                tvPrice.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(mTotalPrice));
                            }
                        } else {
                            Utils.showToast(getActivity(), mFlightPaymentSummaryPojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FlightPaymentSummaryPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void setFlightDetails() {
        String startTime;
        String endTime;

        if (mFlightOneDetailsPojo.data != null) {
            mFlightDetailsList = mFlightOneDetailsPojo.data.flightDetails;
            mFlightDetailsAdapter.setStatus(mFlightOneDetailsPojo.data.isRefundableFare);
            mFlightDetailsAdapter.addAll(mFlightDetailsList);

            if (mFlightOneDetailsPojo.data != null && mFlightOneDetailsPojo.data.farerules != null) {

                for (int i = 0; i < mFlightOneDetailsPojo.data.farerules.size(); i++) {
                    mFlightOneDetailsPojo.data.farerules.get(i).description = mFlightOneDetailsPojo.data.farerules.get(i).description.substring(3).trim();
                }
            }
            mFlightPersonAdapter = new FlightPersonAdapter(getActivity(), mPersoneList, mFlightOneDetailsPojo, mFlightCity.isInternationFlight, mNationalityList, new FlightPersonAdapter.OnAddonsListener() {
                @Override
                public void onAddonsSelected(int position) {
                    setAddonsPrice();
                }
            });
            lvPerson.setAdapter(mFlightPersonAdapter);
            setListViewHeightBasedOnChildren(lvPerson);
//            mFlightPersonAdapter.setAddons(mFlightOneDetailsPojo);

//            if (mPersoneList != null && mPersoneList.size() > 0) {
//                mFlightPersonAdapter.addAll(mPersoneList);
//            }

         /*   Log.d("Log", "Person List " + mPersoneList.size());
            flightPersonDetailsAdapter = new FlightPersonDetailsAdapter(getContext(), mPersoneList, mFlightOneDetailsPojo, mFlightCity.isInternationFlight, lvPerson, mNationalityList, new FlightPersonAdapter.OnAddonsListener() {
                @Override
                public void onAddonsSelected(int position) {
                    setAddonsPrice();
                }
            });
            lvPerson.setAdapter(flightPersonDetailsAdapter);*/

//            int cnvFixedFee = 0;
//            if (mFlightCity.cnvFixedFee > 0) {
//                cnvFixedFee = mFlightCity.cnvFixedFee;
//            }
//
//            int cnvPerFee = 0;
//            if (mFlightCity.cnvPerFee > 0) {
//                cnvPerFee = (int) (mFlightOneDetailsPojo.data.grandTotal * ((float) mFlightCity.cnvPerFee / (float) 100));
//            }
//
//            int cnvTotalFee = cnvFixedFee + cnvPerFee;
//
//            float total = mFlightOneDetailsPojo.data.grandTotal + cnvTotalFee;
//
//
//            tvPrice.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(total));
        }


    }

    @Override
    public void onClick(View view) {

        if (view == tvBack) {
            popBackStack();
        } else if (view == tvAlert) {

            if (mPaymentSummaryList != null && mPaymentSummaryList.size() > 0) {
                showPriceDialog();
            } else {
                getFlightPaymentSummary();
            }

        } else if (view == tvProceed) {
            LogUtils.e("", "tvProceed clicked");

            if (mFlightOneDetailsPojo != null && mFlightOneDetailsPojo.data != null && mFlightPaymentSummaryPojo != null) {

                if (isValidField()) {


                    if (!Utils.isInternetConnected(getActivity())) {
                        Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                        return;
                    }

                    saveFlightDetailsOnServer();

                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
//                    bundle.putString(Constants.BNDL_FLIGHT_NAME_LIST, mFlightNameArray);

                    MyPref.setPref(getContext(), Constants.BNDL_FLIGHT, new Gson().toJson(mFlightCity));
                    MyPref.setPref(getContext(), Constants.BNDL_FLIGHT_NAME_LIST, mFlightNameArray);
                    if (mPosition == 0) {
//                        Toast.makeText(getActivity(), "Zero", Toast.LENGTH_SHORT).show();
                        MyPref.setPref(getContext(), Constants.BNDL_FLIGHT_ONE_DETAILS, new Gson().toJson(mFlightData));
//                        bundle.putString(Constants.BNDL_FLIGHT_ONE_DETAILS, new Gson().toJson(mFlightData));
                    } else if (mPosition == 1) {
//                        Toast.makeText(getActivity(), "One", Toast.LENGTH_SHORT).show();
                        MyPref.setPref(getContext(), Constants.BNDL_FLIGHT_UID, mFlightUid);
                        MyPref.setPref(getContext(), Constants.BNDL_FLIGHT_RETURN_DETAILS, new Gson().toJson(mFlightReturnData));
//                        bundle.putString(Constants.BNDL_FLIGHT_UID, mFlightUid);
//                        bundle.putString(Constants.BNDL_FLIGHT_RETURN_DETAILS, new Gson().toJson(mFlightReturnData));
                    }

                    MyPref.setPref(getContext(), Constants.BNDL_FLIGHT_DETAILS_RESPONSE, new Gson().toJson(mFlightOneDetailsPojo));
                    MyPref.setPref(getContext(), Constants.BNDL_FLIGHT_PAYMENT_SUMMARY, new Gson().toJson(mFlightPaymentSummaryPojo));
                    MyPref.setPref(getContext(), Constants.BNDL_FLIGHT_TRAVELLER, new Gson().toJson(mPersoneList));
                    MyPref.setPref(getContext(), Constants.CITY_TYPE_POSITION, mPosition);
//                    bundle.putString(Constants.BNDL_FLIGHT_DETAILS_RESPONSE, new Gson().toJson(mFlightOneDetailsPojo));
//                    bundle.putString(Constants.BNDL_FLIGHT_PAYMENT_SUMMARY, new Gson().toJson(mFlightPaymentSummaryPojo));
//                    bundle.putString(Constants.BNDL_FLIGHT_TRAVELLER, new Gson().toJson(mPersoneList));
//                    bundle.putInt(Constants.CITY_TYPE_POSITION, mPosition);

                    switchFragment(FlightPaymentFragment.newInstance(), FlightPaymentFragment.Tag_FlightPaymentFragment, bundle);
                }

            } else {
                Utils.showToast(getActivity(), getString(R.string.msg_something_went));
            }

        } else if (view == rltCancellationBaggage) {
            if (mFlightOneDetailsPojo != null && mFlightOneDetailsPojo.data != null) {
                showRuleDialog(getString(R.string.rule_policy));
            }
        } else if (view == tvLogin) {

            Bundle bundle = new Bundle();
            bundle.putString(Constants.BNDL_SCREEN_NAME, LoginActivity.class.getName());
            IntentHandler.startActivityForResult(getActivity(), LoginActivity.class, bundle, Constants.RC_FLIGHT_DETAIL_SIGN_IN);

        } else if (view == tvLoginCheckmark) {

            isLoginCheckmarkDone = !isLoginCheckmarkDone;
            if (mFlightPersonAdapter != null) {
                if (isLoginCheckmarkDone) {
                    tvLoginCheckmark.setBackgroundResource(R.drawable.ic_checkmark_done);
                    mPersoneList = mFlightPersonAdapter.getPersonData();
                    if (mPersoneList != null && mPersoneList.size() > 0) {
                        mPersoneList.get(0).firstname = MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, "");
                        mPersoneList.get(0).lastname = MyPref.getPref(getActivity(), MyPref.PREF_USER_LAST_NAME, "");

                        Log.d("Log", "Last NAme " + MyPref.getPref(getActivity(), MyPref.PREF_USER_LAST_NAME, ""));
                        Log.d("Log", "firt NAme " + MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, ""));

                        LogUtils.e("", isLoginCheckmarkDone + " firstname::" + mPersoneList.get(0).firstname);
                        LogUtils.e("", isLoginCheckmarkDone + " lastname::" + mPersoneList.get(0).lastname);
                    }
                } else {
                    tvLoginCheckmark.setBackgroundResource(R.drawable.ic_checkmark_undone);
                    mPersoneList = mFlightPersonAdapter.getPersonData();
                    if (mPersoneList != null && mPersoneList.size() > 0) {
                        mPersoneList.get(0).firstname = "";
                        mPersoneList.get(0).lastname = "";
                        LogUtils.e("", isLoginCheckmarkDone + " firstname::" + mPersoneList.get(0).firstname);
                        LogUtils.e("", isLoginCheckmarkDone + " lastname::" + mPersoneList.get(0).lastname);
                    }
                }
                mFlightPersonAdapter.notifyDataSetChanged();
//                flightPersonDetailsAdapter.notifyDataSetChanged();
            }
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listview) {
        ListAdapter listadp = listview.getAdapter();
        if (listadp != null) {
            int totalHeight = 0;
            for (int i = 0; i < listadp.getCount(); i++) {
                View listItem = listadp.getView(i, null, listview);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = totalHeight + (listview.getDividerHeight() * (listadp.getCount() - 1));
            listview.setLayoutParams(params);
            listview.requestLayout();
        }
    }


    private void saveFlightDetailsOnServer() {

        if (!Utils.isInternetConnected(getActivity())) {
            return;
        }

        String depature_arv_date = "", return_arv_date = "";

        Calendar mStartCal = Calendar.getInstance();
        mStartCal.setTimeInMillis(mFlightCity.displayDepartDate);

        if (mFlightOneDetailsPojo.data != null) {
            depature_arv_date = mFlightOneDetailsPojo.data.flightDetails.get(0).endDate;
            return_arv_date = mFlightOneDetailsPojo.data.flightDetails.get(mFlightOneDetailsPojo.data.flightDetails.size() - 1).endDate;
        }

        String mUrl = "";
        if (mPosition == 0) {


            mUrl = "http://managemyticket.net/android/api/" + Constants.API_FLIGHT_TITLE + "/insertJourneyDetails.php?uuid=" + mFlightUid + "&key=" + mKey
                    + "&from_city=" + mFlightCity.fromCity + "&to_city=" + mFlightCity.toCity + "&adult_count=" + mFlightCity.adultCount
                    + "&child_count=" + mFlightCity.childCount + "&infant_count=" + mFlightCity.infantCount
                    + "&class=" + mFlightCity.clasName + "&depature_date=" + DateFormate.sdfAirportServerDate.format(mStartCal.getTime())
                    + "&depature_arv_date=" + depature_arv_date
                    + "&user_token=" + Otapp.mUniqueID;

        } else if (mPosition == 1) {

            Calendar mEndCal = Calendar.getInstance();
            mEndCal.setTimeInMillis(mFlightCity.displayReturnDate);

            mUrl = "http://managemyticket.net/android/api/" + Constants.API_FLIGHT_TITLE + "/insertJourneyDetails.php?uuid=" + mFlightUid + "&key=" + mKey
                    + "&from_city=" + mFlightCity.fromCity + "&to_city=" + mFlightCity.toCity + "&adult_count=" + mFlightCity.adultCount
                    + "&child_count=" + mFlightCity.childCount + "&infant_count=" + mFlightCity.infantCount
                    + "&class=" + mFlightCity.clasName + "&depature_date=" + DateFormate.sdfAirportServerDate.format(mStartCal.getTime())
                    + "&depature_arv_date=" + depature_arv_date
                    + "&return_date=" + DateFormate.sdfAirportServerDate.format(mEndCal.getTime())
                    + "&return_arv_date=" + return_arv_date
                    + "&user_token=" + Otapp.mUniqueID;
        }


        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ApiResponse> mCall = mApiInterface.saveFlightDetails(mUrl);
        mCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


    }

    private boolean isValidField() {

        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_email));
            return false;
        } else if (!Utils.isValidEmail(etEmail.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_email));
            return false;
        } else if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_phone));
            return false;
        } else  if(etMobileNumber.getText().toString().length()+spinCountryCode.getText().toString().length()!=13){
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone_number));
            return false;
        }/*if (etMobileNumber.getText().toString().length() < mMobNumberMaxLength || etMobileNumber.getText().toString().length() > mMobNumberMaxLength) {
            LogUtils.e("", "etMobileNumber.getText().toString()::" + etMobileNumber.getText().toString());
            Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobNumberMaxLength));
            return false;
        }*/


        mPersoneList = mFlightPersonAdapter.getPersonData();
        if (mPersoneList != null && mPersoneList.size() > 0) {

            for (int i = 0; i < mPersoneList.size(); i++) {

                LogUtils.e("", i + " mPersoneList.get(i).type:" + mPersoneList.get(i).type);
                LogUtils.e("", i + " mPersoneList.get(i).firstname:" + mPersoneList.get(i).firstname);
                LogUtils.e("", i + " mPersoneList.get(i).lastname:" + mPersoneList.get(i).lastname);
                LogUtils.e("", i + " mPersoneList.get(i).dob:" + mPersoneList.get(i).dob);

                if (TextUtils.isEmpty(mPersoneList.get(i).type)) {
                    Utils.showToast(getActivity(), "" + getString(R.string.alert_select_person_type) + " for " + mPersoneList.get(i).name);
                    return false;
                } else if (TextUtils.isEmpty(mPersoneList.get(i).firstname)) {
                    Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_firstname) + " for " + mPersoneList.get(i).name);
                    return false;
                }
//                else if (TextUtils.isEmpty(mPersoneList.get(i).middlename)) {
//                    Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_middle) + " for " + mPersoneList.get(i).name);
//                    return false;
//                }
                else if (TextUtils.isEmpty(mPersoneList.get(i).lastname)) {
                    Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_lastname) + " for " + mPersoneList.get(i).name);
                    return false;
                }
//                else if (TextUtils.isEmpty(mPersoneList.get(i).phone)) {
//                    Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_phone) + " for " + mPersoneList.get(i).name);
//                    return false;
//                } else if (mPersoneList.get(i).phone.length() < 13 || mPersoneList.get(i).phone.length() > 13) {
//                    LogUtils.e("", "mPersoneList.get(i).phone::"+mPersoneList.get(i).phone);
//                    Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone) + mPersoneList.get(i).name);
//                    return false;
//                }
                else if (!mPersoneList.get(i).name.contains(getString(R.string.adult)) && TextUtils.isEmpty(mPersoneList.get(i).dob)) {
                    Utils.showToast(getActivity(), "" + getString(R.string.alert_select_dob) + " for " + mPersoneList.get(i).name);
                    return false;
                }

                if (mFlightCity.isInternationFlight) {
                    if (TextUtils.isEmpty(mPersoneList.get(i).passportNumber)) {
                        Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_passport) + " for " + mPersoneList.get(i).name);
                        return false;
                    } else if (TextUtils.isEmpty(mPersoneList.get(i).passportIssueDate)) {
                        Utils.showToast(getActivity(), "" + getString(R.string.alert_select_issue_date) + " for " + mPersoneList.get(i).name);
                        return false;
                    } else if (TextUtils.isEmpty(mPersoneList.get(i).passportExpDate)) {
                        Utils.showToast(getActivity(), "" + getString(R.string.alert_select_exp_date) + " for " + mPersoneList.get(i).name);
                        return false;
                    } else if (TextUtils.isEmpty(mPersoneList.get(i).issuedBy)) {
                        Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_issue_by) + " for " + mPersoneList.get(i).name);
                        return false;
                    } else if (TextUtils.isEmpty(mPersoneList.get(i).citizenShip)) {
                        Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_citizenship) + " for " + mPersoneList.get(i).name);
                        return false;
                    }

                }

                for (int j = 0; j < mNationalCountryList.size(); j++) {
                    if (mNationalCountryList.get(j).countryName.equals(mPersoneList.get(i).issuedBy)) {
                        LogUtils.e("", mNationalCountryList.get(j).countryName + " " + mPersoneList.get(i).issuedBy + " " + mNationalCountryList.get(j).countryCode);
                        mPersoneList.get(i).issuedBy = mNationalCountryList.get(j).countryCode;
                    }

                    if (mNationalCountryList.get(j).countryName.equals(mPersoneList.get(i).citizenShip)) {
                        LogUtils.e("", mNationalCountryList.get(j).countryName + " " + mPersoneList.get(i).citizenShip + " " + mNationalCountryList.get(j).countryCode);
                        mPersoneList.get(i).citizenShip = mNationalCountryList.get(j).countryCode;
                    }
                }

                if (i == 0 && mPersoneList.get(i).name.contains(getString(R.string.adult))) {
                    mFlightOneDetailsPojo.data.userName = mPersoneList.get(i).type + ". " + mPersoneList.get(i).firstname + (TextUtils.isEmpty(mPersoneList.get(i).middlename) ? "" : " " + mPersoneList.get(i).middlename) + " " + mPersoneList.get(i).lastname;
                }
            }
        }

        mFlightOneDetailsPojo.data.userEmail = etEmail.getText().toString();
        mFlightOneDetailsPojo.data.userPhone = spinCountryCode.getText().toString()+ "" + etMobileNumber.getText().toString();

        return true;

    }

    private void setAddonsPrice() {

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

        getFlightPaymentSummary();

//        float grandPrice = mFlightOneDetailsPojo.data.grandTotal + mAddonsPrice;


//        int cnvFixedFee = 0;
//        if (mFlightCity.cnvFixedFee > 0) {
//            cnvFixedFee = mFlightCity.cnvFixedFee;
//        }
//
//        int cnvPerFee = 0;
//        if (mFlightCity.cnvPerFee > 0) {
//            cnvPerFee = (int) (grandPrice * ((float) mFlightCity.cnvPerFee / (float) 100));
//        }
//
//        int cnvTotalFee = cnvFixedFee + cnvPerFee;
//
//        float total = grandPrice + cnvTotalFee;
//
//        tvPrice.setText(mFlightOneDetailsPojo.data.currency + " " + (total));


    }

    public void onFareRuleClicked() {
        if (mFlightOneDetailsPojo != null && mFlightOneDetailsPojo.data != null) {
            showRuleDialog(getString(R.string.fare_rules));
        }
    }

    private void getCountryCodeList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("user_token", "" + Otapp.mUniqueID);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<CountryCodePojo> mCall = mApiInterface.getCountryCodeList(jsonParams);
        mCall.enqueue(new Callback<CountryCodePojo>() {
            @Override
            public void onResponse(Call<CountryCodePojo> call, Response<CountryCodePojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    CountryCodePojo mCountryCodePojo = response.body();
                    if (mCountryCodePojo != null) {
                        if (mCountryCodePojo.status.equalsIgnoreCase("200")) {

                            Otapp.mCountryCodePojoList = mCountryCodePojo.data;
                            Otapp.mAdsPojoList = mCountryCodePojo.ad5;

                        //    setSpinCountryCode();

                        } else {
                            Utils.showToast(getActivity(), "" + mCountryCodePojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryCodePojo> call, Throwable t) {
                LogUtils.e("", "onFailure:" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });

    }

    private void showRuleDialog(String mTitle) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_flight_rule);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView lvRule = (ListView) dialog.findViewById(R.id.lvRule);
        TextView tvOkay = (TextView) dialog.findViewById(R.id.tvOkay);
        final TextView title = (TextView) dialog.findViewById(R.id.title);
//        final TextView tvCancel = (TextView) termsDialog.findViewById(R.id.tvCancel);

        title.setText(mTitle);

        if (mFlightOneDetailsPojo.data != null && mFlightOneDetailsPojo.data.farerules != null) {

//            List<FlightOneDetailsPojo.Farerule> farerules = new ArrayList<>();
//            farerules.addAll(mFlightOneDetailsPojo.data.farerules);
//            for (int i = 0; i < mFlightOneDetailsPojo.data.farerules.size(); i++) {
//                farerules.get(i).description = mFlightOneDetailsPojo.data.farerules.get(i).description.substring(3).trim();
//            }

            final FlightRuleAdapter mFlightRuleAdapter = new FlightRuleAdapter(getActivity());
            mFlightRuleAdapter.addAll(mFlightOneDetailsPojo.data.farerules);
            lvRule.setAdapter(mFlightRuleAdapter);

        }

        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

            }
        });

//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (termsDialog != null && termsDialog.isShowing()) {
//                    termsDialog.dismiss();
//                    termsDialog.cancel();
//                }
//
//            }
//        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        dialog.show();

    }

    private void showPriceDialog() {

        final Animation animClose, animOpen;

        animOpen = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_open);
        animClose = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_close);

        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_flight_total);

        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
        final TextView tvTraveller = (TextView) dialog.findViewById(R.id.tvTraveller);
//        final TextView tvBaseFare = (TextView) dialog.findViewById(R.id.tvBaseFare);
//        final TextView tvFeeSurcharge = (TextView) dialog.findViewById(R.id.tvFeeSurcharge);
//        final TextView tvTotal = (TextView) dialog.findViewById(R.id.tvTotal);
//        final TextView tvAddOns = (TextView) dialog.findViewById(R.id.tvAddOns);
//        final TextView tvConvenienceCharge = (TextView) dialog.findViewById(R.id.tvConvenienceCharge);
        final ListView lvTicketPrice = (ListView) dialog.findViewById(R.id.lvTicketPrice);
        final TextView tvYouPayTitle = (TextView) dialog.findViewById(R.id.tvYouPayTitle);
        final TextView tvYouPay = (TextView) dialog.findViewById(R.id.tvYouPay);
        final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);

        tvTraveller.setText(mFlightCity.traveller);
//        tvBaseFare.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(mFlightOneDetailsPojo.data.baseFare));
//        tvAddOns.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(mAddonsPrice));
//        tvFeeSurcharge.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(mFlightOneDetailsPojo.data.tax));
//        tvTotal.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(mFlightOneDetailsPojo.data.grandTotal));

//        float mGrandPrice = mFlightOneDetailsPojo.data.grandTotal + mAddonsPrice;
//
//        int cnvFixedFee = 0;
//        if (mFlightCity.cnvFixedFee > 0) {
//            cnvFixedFee = mFlightCity.cnvFixedFee;
//        }
//
//        int cnvPerFee = 0;
//        if (mFlightCity.cnvPerFee > 0) {
//            cnvPerFee = (int) (mGrandPrice * ((float) mFlightCity.cnvPerFee / (float) 100));
//        }
//
//        int cnvTotalFee = cnvFixedFee + cnvPerFee;
//
//        float total = mGrandPrice + cnvTotalFee;
//
//        tvConvenienceCharge.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(cnvTotalFee));

        if (mPaymentSummaryList != null && mPaymentSummaryList.size() > 0) {
            float mTotalPrice = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).price;
            String title = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).lable;
            if (!TextUtils.isEmpty(title)) {
                tvYouPayTitle.setText("" + title);
            }

            tvYouPay.setText(mFlightOneDetailsPojo.data.currency + " " + Utils.setPrice(mTotalPrice));

            FlightPaymentSummaryAdapter mFlightPaymentSummaryAdapter = new FlightPaymentSummaryAdapter(getActivity(), mFlightOneDetailsPojo.data.currency);
            lvTicketPrice.setAdapter(mFlightPaymentSummaryAdapter);
            mFlightPaymentSummaryAdapter.addAll(mPaymentSummaryList.subList(0, (mPaymentSummaryList.size() - 1)));
        }


        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lnrContainer.startAnimation(animClose);
                    }
                }, 10);

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rlMain.performClick();

            }
        });

//        tvProceed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                rlMain.performClick();
//
//            }
//        });

        animClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }
                }, 10);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        lnrContainer.startAnimation(animOpen);
        dialog.show();

    }

    private void stopCountdown() {
        LogUtils.e("", "stopCountdown");
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            lnrSessionTime.setVisibility(View.GONE);
        }
    }

    private void startTimer(long msUntilFinished) {

        long millis = msUntilFinished;
        String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        tvSession.setText(String.format(getString(R.string.sessing_five_minute), hms));

        lnrSessionTime.setVisibility(View.VISIBLE);

        MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, millis);

        countDownTimer = new CountDownTimer(msUntilFinished, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                tvSession.setText(String.format(getString(R.string.sessing_five_minute), hms));

                MyPref.setPref(getActivity(), Constants.BNDL_FLIGHT_SESSION_TIME, millis);
            }

            public void onFinish() {
                countDownTimer = null;
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

    public void checkLoggedInUser() {

        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
            tvLogin.setVisibility(View.GONE);
            tvLoginCheckmark.setVisibility(View.VISIBLE);
            tvLoginCheckmark.setBackgroundResource(R.drawable.ic_checkmark_undone);
            isLoginCheckmarkDone = false;
            tvLoginText.setText(getString(R.string.use_my_details));

            etEmail.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_EMAIL, ""));
           // etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));

            if (Otapp.mCountryCodePojoList == null || Otapp.mCountryCodePojoList.size() == 0) {
                getCountryCodeList();
            } else {
                //setSpinCountryCode();
            }

        } else {
            tvLogin.setVisibility(View.VISIBLE);
            tvLoginCheckmark.setVisibility(View.GONE);
            tvLoginText.setText(getString(R.string.login_retrive_details));
        }
    }
    public void closeKeyboard(){
        View view = getActivity().getCurrentFocus();
        if(view!=null){
            final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            Toast.makeText(getContext(), "Close", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Not Close", Toast.LENGTH_SHORT).show();
        }
    }
}
