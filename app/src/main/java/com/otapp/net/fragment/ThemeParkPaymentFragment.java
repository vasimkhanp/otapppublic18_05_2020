package com.otapp.net.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.CouponCodeActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.CountryCodeListAdapter;
import com.otapp.net.adapter.CountryCodeSpinAdapter;
import com.otapp.net.adapter.ParkPaymentAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.CouponResponsePojo;
import com.otapp.net.model.ParkPaymentSummaryPojo;
import com.otapp.net.model.ParkZeroResponse;
import com.otapp.net.model.ThemeParkCartListPojo;
import com.otapp.net.model.ThemeParkDetailsPojo;
import com.otapp.net.model.ThemeParkPojo;
import com.otapp.net.model.ThemeParkSuccessPojo;
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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemeParkPaymentFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_ThemeParkPaymentFragment = "Tag_" + "ThemeParkPaymentFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPlace)
    TextView tvPlace;
    @BindView(R.id.tvDate)
    TextView tvDate;
    //    @BindView(R.id.tvSubTotal)
//    TextView tvSubTotal;
//    @BindView(R.id.tvTotal)
//    TextView tvTotal;
    //    @BindView(R.id.tvCountryCode)
//    TextView tvCountryCode;
    @BindView(R.id.spinCountryCode)
    TextView spinCountryCode;
    @BindView(R.id.tvPay)
    TextView tvPay;

    @BindView(R.id.etEmailID)
    EditText etEmailID;
    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.rltDebicCard)
    RelativeLayout rltDebicCard;
    @BindView(R.id.rltTigoPesa)
    RelativeLayout rltTigoPesa;
    @BindView(R.id.rltMPesa)
    RelativeLayout rltMPesa;
    @BindView(R.id.rltAirtelMoney)
    RelativeLayout rltAirtelMoney;
    @BindView(R.id.lnrApplyOffer)
    LinearLayout lnrApplyOffer;
    @BindView(R.id.lvSeat)
    ListView lvSeat;
    @BindView(R.id.paymentLayout)
    LinearLayout paymentLinearLayout;
    @BindView(R.id.divider)
    View view;
    /*  @BindView(R.id.promoCodeTVPay)
      TextView tvPromoCodePay;*/
    /* @BindView(R.id.linerPayDiscount)*/
    RelativeLayout linearPayDiscout;

    String strCardNo, strCardType, strCardExpiryDt, strCardCvv, strMonth, strYear;
    int year = 0, expYear = 0;
    int month = 1, flag = 0, mobFlag = 0;

    public static String finalAmount = "";
    private float mTotalAmount = 0;
    private int mMobNumberMaxLength = 9;
    private int mMobInnerNumberMaxLength = 9;
    private String mCurrency = "";

    String srtTicketInfo = "";
    String themeParkProp = "";
    String mUserKey = "";
    int promoFareAmt = 0;

    List<ThemeParkCartListPojo.CartItem> mCartList = null;
    List<CountryCodePojo.CountryCode> tempCountryCodeList;
    CountryCodeListAdapter mCountryCodeSpinAdapter;
    int countryCodeFlag=0;
    ParkPaymentAdapter mParkPaymentAdapter;

    CouponResponsePojo mCouponResponsePojo;
    ParkPaymentSummaryPojo mPaymentSummaryPojo;
    List<ParkPaymentSummaryPojo.Summary> mPaymentSummaryList;


    ThemeParkPojo.Park mPark;
    ThemeParkDetailsPojo.Details mThemeParkDetails;
    private String mUserEmail = "", mUserFullName = "", mUserMobileNumber = "";
    String sCRDBCharges = "";
    String sMPesaCharges = "";
    String sTigoCharges = "";
    String sAirtelCharges = "";

    public boolean isCreditCardEnabled = false, isMpesaEnabled = false, isTigoEnabled = false, isAirtelMoneyEnabled = false;

    public static ThemeParkPaymentFragment newInstance() {
        ThemeParkPaymentFragment fragment = new ThemeParkPaymentFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_park_payment, container, false);
        ButterKnife.bind(this, mView);
        //  linearPayDiscout=mView.findViewById(R.id.linerPayDiscount);
        InitializeControls();

        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //  Toast.makeText(getContext(), "back", Toast.LENGTH_SHORT).show();
                    String sCouponcode = MyPref.getPref(getContext(), "PromoCode", "");
                    if (sCouponcode != null) {
                        if (sCouponcode.equals("")) {
                            popBackStack();
                        } else {
                            removeCouponCode();
                        }
                    } else {
                        removeCouponCode();
                    }
                }
                return true;
            }
        });


        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        String mob = MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "");
        if(mob.length()==12){
            if(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "").contains("+")){
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));

            }else {
                etMobileNumber.setText("+" + MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});

            }
            etMobileNumber.setSelection(etMobileNumber.getText().length());
            spinCountryCode.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            flag=1;
        }else {
            etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
            etMobileNumber.setSelection(etMobileNumber.getText().length());
            spinCountryCode.setVisibility(View.VISIBLE);
            if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("")||MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")) {
                spinCountryCode.setText("+255");
            }
            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
            view.setVisibility(View.VISIBLE);
            flag=0;
        }
    }

    private void InitializeControls() {

        mTotalAmount = 0;
        tempCountryCodeList= new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPark = new Gson().fromJson(bundle.getString(Constants.BNDL_PARK), ThemeParkPojo.Park.class);
            mThemeParkDetails = new Gson().fromJson(bundle.getString(Constants.BNDL_PARK_DETAILS), ThemeParkDetailsPojo.Details.class);
            mCartList = new Gson().fromJson(bundle.getString(Constants.BNDL_PARK_CART_LIST), new TypeToken<List<ThemeParkCartListPojo.CartItem>>() {
            }.getType());

            Calendar mCalDate = Calendar.getInstance();
            LogUtils.e("", "payment PREF_PARK_DATE::" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_DATE, 0l));
            if (MyPref.getPref(getActivity(), MyPref.PREF_PARK_DATE, 0l) > 0l) {
                mCalDate.setTimeInMillis(MyPref.getPref(getActivity(), MyPref.PREF_PARK_DATE, 0l));
            }

            LogUtils.e("", "mCalDate::" + mCalDate.getTime() + " " + DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()));
            if (mPark != null) {
                tvName.setText(mPark.name);
                tvPlace.setText("" + mPark.city);
                tvDate.setText(DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()) + " | " + mThemeParkDetails.timings);
            }

            if (mThemeParkDetails != null) {

                mCurrency = mThemeParkDetails.currrency;

                mParkPaymentAdapter = new ParkPaymentAdapter(getActivity(), mCurrency);
                lvSeat.setAdapter(mParkPaymentAdapter);

                getPaymentSummary();


//                if (mCartList != null && mCartList.size() > 0) {
//                    for (int i = 0; i < mCartList.size(); i++) {
//                        ThemeParkCartListPojo.CartItem mCartItem = mCartList.get(i);
//                        if (mCartItem != null) {
//
//                            if (mCartItem.adultTicketCount > 0) {
//                                mTotalAmount = mTotalAmount + (mCartItem.adultTicketCount * mCartItem.adultPrice);
//                            }
//                            if (mCartItem.childTicketCount > 0) {
//                                mTotalAmount = mTotalAmount + (mCartItem.childTicketCount * mCartItem.childPrice);
//                            }
//
//                            if (mCartItem.studTicketCount > 0) {
//                                mTotalAmount = mTotalAmount + (mCartItem.studTicketCount * mCartItem.studPrice);
//                            }
//                        }
//                    }
//                }


//                tvSubTotal.setText(mCurrency + " " + Utils.setPrice(mTotalAmount));
//                tvTotal.setText(mCurrency + " " + Utils.setPrice(mTotalAmount));
//                tvPay.setText(mCurrency + " " + Utils.setPrice(mTotalAmount));
            }
        }

        if (Otapp.mCountryCodePojoList == null || Otapp.mCountryCodePojoList.size() == 0) {
            getCountryCodeList();
        } else {
            if(MyPref.PREF_USER_COUNTRY_CODE.length()>1){
                spinCountryCode.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""));
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
            }
        }

        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
            //  lnrApplyOffer.setVisibility(View.GONE);
        } else {
            lnrApplyOffer.setVisibility(View.VISIBLE);
        }

        tvBack.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        rltDebicCard.setOnClickListener(this);
        rltTigoPesa.setOnClickListener(this);
        rltMPesa.setOnClickListener(this);
        rltAirtelMoney.setOnClickListener(this);
        lnrApplyOffer.setOnClickListener(this);

        etFirstName.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etFirstName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });

        etLastName.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etLastName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });

        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //       etFirstName.setSelection(etFirstName.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //         etLastName.setSelection(etLastName.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etEmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //          etEmailID.setSelection(etEmailID.getText().length());
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
                if (charSequence.toString().trim().equals("")) {
                    Log.d("Log", "clicked");
//                    Toast.makeText(getContext(), "chage", Toast.LENGTH_SHORT).show();
                    spinCountryCode.setVisibility(View.VISIBLE);
                    if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("")||MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")) {
                       // spinCountryCode.setSelection(214);
                        spinCountryCode.setText("+255");
                    }
                    etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
                    view.setVisibility(View.VISIBLE);
                    flag = 0;
                }
                if (charSequence.toString().trim().length() < 13) {
                    mobFlag = 1;
                } else {
                    mobFlag = 0;
                }
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
                            }
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
                            countryCodeFlag=0;
                        }else {
                            if(Otapp.mCountryCodePojoList.get(position).code.equals("")){
                                spinCountryCode.setText("+255");
                            }else {
                                spinCountryCode.setText(Otapp.mCountryCodePojoList.get(position).code);
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


        String mob = MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "");
        if(mob.length()==12){
            if(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "").contains("+")){
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));

            }else {
                etMobileNumber.setText("+" + MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});

            }
            etMobileNumber.setSelection(etMobileNumber.getText().length());
            spinCountryCode.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            flag=1;
        }else {
            etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
            etMobileNumber.setSelection(etMobileNumber.getText().length());
            spinCountryCode.setVisibility(View.VISIBLE);
            if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("")||MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")) {
                spinCountryCode.setText("+255");
            }
            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
            view.setVisibility(View.VISIBLE);
            flag=0;
        }
        etFirstName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, ""));
        etLastName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_LAST_NAME, ""));
        etEmailID.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_EMAIL, ""));
        etFirstName.setSelection(etFirstName.getText().length());
        etLastName.setSelection(etLastName.getText().length());
        etEmailID.setSelection(etEmailID.getText().length());
//        etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
    //    Log.d("Log", "Mobile= " + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code + "" + etMobileNumber.getText().toString());
    }

/*
    private void setSpinCountryCode() {

        CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
        spinCountryCode.setAdapter(mCountryCodeSpinAdapter);
       */
/* if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
            spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
        } else {
            spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
        }*//*

        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
            spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
        } else {
            for (int i = 0; i < Otapp.mCountryCodePojoList.size(); i++) {
                Log.d("Log", MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""));
                Log.d("Log", "" + Otapp.mCountryCodePojoList.get(i).code);
                if (MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals(Otapp.mCountryCodePojoList.get(i).code)) {
                    spinCountryCode.setSelection(i);
                    break;
                }
            }
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

    /* @OnClick(R.id.linerPayDiscount)
     void onClick(){
         if(mTotalAmount<=0.0){
             paymentLinearLayout.setVisibility(View.GONE);
                     Toast.makeText(getContext(), "Hi", Toast.LENGTH_SHORT).show();
         }else {
             Toast.makeText(getContext(), "Bye", Toast.LENGTH_SHORT).show();
         }
     }
 */
    private void getPaymentSummary() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("ukey", "" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, ""));

        int promoid = 0;
        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
            jsonParams.put("promo_id", "" + mCouponResponsePojo.promo_id);
            promoid = mCouponResponsePojo.promo_id;
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ParkPaymentSummaryPojo> mCall = mApiInterface.getParkPaymentSummary(jsonParams);
        mCall.enqueue(new Callback<ParkPaymentSummaryPojo>() {
            @Override
            public void onResponse(Call<ParkPaymentSummaryPojo> call, Response<ParkPaymentSummaryPojo> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "TP Payment Summary Response : " + jsonObjectResponse);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ParkPaymentSummaryPojo mParkPaymentSummaryPojo = response.body();
                    if (mParkPaymentSummaryPojo != null) {
                        if (mParkPaymentSummaryPojo.status.equalsIgnoreCase("200")) {
                            mPaymentSummaryPojo = mParkPaymentSummaryPojo;
                            mPaymentSummaryList = mParkPaymentSummaryPojo.data.fare;
                            promoFareAmt = mParkPaymentSummaryPojo.data.promoFare.promoFare;

                            if (mPaymentSummaryList != null && mPaymentSummaryList.size() > 0) {

                                mTotalAmount = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).price;
                                String title = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).lable;

//                                if (!TextUtils.isEmpty(title)) {
//                                    tvGrandTotalTitle.setText("" + title);
//                                }

//                                tvGrandTotal.setText(mCurrency + " " + Utils.setPrice(mTotalPrice));
//                                tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mTotalPrice));
                                if (mPaymentSummaryPojo != null) {
                                    if (mTotalAmount <= 0.0) {
                                        tvPay.setVisibility(View.VISIBLE);
                                        paymentLinearLayout.setVisibility(View.GONE);
                                        tvPay.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (isValidField()) {
                                                    finalAmount = tvPay.getText().toString();
                                                    callZeroPayment();
                                                }
                                            }
                                        });
                                    } else {
                                        tvPay.setVisibility(View.GONE);
                                    }
                                }
                                tvPay.setText(mCurrency + " " + Utils.setPrice(mTotalAmount));
                                finalAmount = tvPay.getText().toString();

                                mParkPaymentAdapter.addAll(mPaymentSummaryList);
                            }

                            List<ParkPaymentSummaryPojo.PaymentAllowed> paymentAllowed = mParkPaymentSummaryPojo.data.paymentAllowed;
                            if (paymentAllowed != null && paymentAllowed.size() > 0) {
                                for (int i = 0; i < paymentAllowed.size(); i++) {
                                    LogUtils.e("", "paymentName::" + paymentAllowed.get(i).paymentName);
                                    if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("Credit/Debit Card")) {
                                        sCRDBCharges = paymentAllowed.get(i).sExtraPGWCharges;
                                        if (mCouponResponsePojo != null && !TextUtils.isEmpty(mCouponResponsePojo.applicable_pgws)) {
                                            if (mCouponResponsePojo.applicable_pgws.contains("0") || mCouponResponsePojo.applicable_pgws.contains("1")) {
                                                isCreditCardEnabled = true;
                                            }
                                        } else {
                                            isCreditCardEnabled = true;
                                        }

                                    } else if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("Tigo Pesa")) {
                                        sTigoCharges = paymentAllowed.get(i).sExtraPGWCharges;
                                        if (mCouponResponsePojo != null && !TextUtils.isEmpty(mCouponResponsePojo.applicable_pgws)) {
                                            if (mCouponResponsePojo.applicable_pgws.contains("0") || mCouponResponsePojo.applicable_pgws.contains("2")) {
                                                isTigoEnabled = true;
                                            }
                                        } else {
                                            isTigoEnabled = true;
                                        }
                                    } else if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("MPESA")) {
                                        sMPesaCharges = paymentAllowed.get(i).sExtraPGWCharges;
                                        if (mCouponResponsePojo != null && !TextUtils.isEmpty(mCouponResponsePojo.applicable_pgws)) {
                                            if (mCouponResponsePojo.applicable_pgws.contains("0") || mCouponResponsePojo.applicable_pgws.contains("3")) {
                                                isMpesaEnabled = true;
                                            }
                                        } else {
                                            isMpesaEnabled = true;
                                        }
                                    } else if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("Airtel")) {
                                        sAirtelCharges = paymentAllowed.get(i).sExtraPGWCharges;
                                        if (mCouponResponsePojo != null && !TextUtils.isEmpty(mCouponResponsePojo.applicable_pgws)) {
                                            if (mCouponResponsePojo.applicable_pgws.contains("0") || mCouponResponsePojo.applicable_pgws.contains("4")) {
                                                isAirtelMoneyEnabled = true;
                                            }
                                        } else {
                                            isAirtelMoneyEnabled = true;
                                        }
                                    }
                                }

                            } else {
                                LogUtils.e("", "paymentAllowed is null");
                                isCreditCardEnabled = false;
                                isTigoEnabled = false;
                                isMpesaEnabled = false;
                                isAirtelMoneyEnabled = false;
                            }

                            LogUtils.e("", "isCreditCardEnabled::" + isCreditCardEnabled);
                            LogUtils.e("", "isTigoEnabled::" + isTigoEnabled);
                            LogUtils.e("", "isMpesaEnabled::" + isMpesaEnabled);
                            LogUtils.e("", "isAirtelMoneyEnabled::" + isAirtelMoneyEnabled);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ParkPaymentSummaryPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        String sApplicablePGWS = MyPref.getPref(getContext(), "ENABLED_PAYMENT_METHODS", "0");
        Log.d("Log", "Applicable PGW " + sApplicablePGWS);
        if (view == tvBack) {
            String sCouponcode = MyPref.getPref(getContext(), "PromoCode", "");
            if (sCouponcode != null) {
                if (sCouponcode.equals("")) {
                    popBackStack();
                } else {
                    removeCouponCode();
                }
            } else {
                removeCouponCode();
            }
        } else if (view == lnrApplyOffer) {

            JSONObject jsonObject = new JSONObject();

            int mAmount = 0;
            try {

                JSONArray jsonArray = new JSONArray();


                if (mCartList != null && mCartList.size() > 0) {


                    for (int i = 0; i < mCartList.size(); i++) {
                        ThemeParkCartListPojo.CartItem mCartItem = mCartList.get(i);

                        byte[] data = Base64.decode(mCartItem.tpId, Base64.DEFAULT);
                        String mTpId = "";
                        try {
                            mTpId = new String(data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        if (mCartItem != null && mCartItem.adultTicketCount > 0) {

                            mAmount = mAmount + mCartItem.adultPrice * mCartItem.adultTicketCount;

                            JSONObject jsonTktInfo = new JSONObject();
                            jsonTktInfo.put("tkt_id", "" + mTpId);
                            jsonTktInfo.put("tot_tkt_id_tkts_count", "" + mCartItem.adultTicketCount);
                            jsonTktInfo.put("single_fare", "" + mCartItem.adultPrice);
                            jsonTktInfo.put("seats", "");

                            jsonArray.put(jsonTktInfo);

                        } else {

                            JSONObject jsonTktInfo = new JSONObject();
                            jsonTktInfo.put("tkt_id", "");
                            jsonTktInfo.put("tot_tkt_id_tkts_count", "");
                            jsonTktInfo.put("single_fare", "");
                            jsonTktInfo.put("seats", "");

                            jsonArray.put(jsonTktInfo);

                        }
                    }
                }


                jsonObject.put("tkts_info", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Bundle bundle = new Bundle();
            srtTicketInfo = jsonObject.toString();
            themeParkProp = mPark.prop_id;
            mUserKey = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "");
//            bundle.putString(Constants.BNDL_PROPERTY_ID, "" + mThemeParkDetails.tpId);
            bundle.putString(Constants.BNDL_PROPERTY_ID, "" + mPark.prop_id);
            bundle.putString(Constants.BNDL_TICKET_INFO, "" + jsonObject.toString());
            bundle.putString(Constants.BNDL_TOTAL_FARE, "" + promoFareAmt); //
            bundle.putString(Constants.BNDL_USER_KEY, "" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, ""));
            bundle.putString(Constants.BNDL_CURRENCY, "" + mCurrency);

            IntentHandler.startActivityForResult(getActivity(), CouponCodeActivity.class, bundle, Constants.RC_PARK_COUPON_CODE);

        } else if (view == rltDebicCard) {
            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("1") || sApplicablePGWS.contains("5")) {
                if (sApplicablePGWS.contains("1")) {
                    if (mCurrency.equals("TSH")) {
                        if (isValidField()) {

                            if (!isCreditCardEnabled) {
                                Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                                return;
                            }

                            final Dialog dialog = new Dialog(getActivity());
                            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_card_info);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            Spinner spinnerCardType = dialog.findViewById(R.id.spinnerCardType);
                            EditText editTextCardNo = dialog.findViewById(R.id.editCardNo);
                            EditText editTextCvv = dialog.findViewById(R.id.editCvv);
                            TextView btSubmit = dialog.findViewById(R.id.tvSubmit);
                            TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                            TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                            Spinner spinnerMonth = dialog.findViewById(R.id.spinnerMonth);
                            Spinner spinnerYear = dialog.findViewById(R.id.spinnerYear);

                            if (sCRDBCharges.equals("")) {
                                tvCharges.setVisibility(View.GONE);
                            } else if (sCRDBCharges.equals("0.00")) {
                                tvCharges.setVisibility(View.GONE);
                            } else {
                                tvCharges.setText("Extra Charges Applicable : " + mCurrency + " " + sCRDBCharges);
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
                                    editTextCvv.requestFocus();
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

                       /* if(mPaymentSummaryPojo!=null){
                            if(mTotalAmount<=0.0){
                                paymentLinearLayout.setVisibility(View.GONE);
                                linearPayDiscout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callZeroPayment();
                                    }
                                });
                            }
                        }*/

                                    if (mPaymentSummaryPojo != null) {
                                        if (mTotalAmount > 0) {
                                            if (mTotalAmount >= 100) {

                                                if (!Utils.isInternetConnected(getActivity())) {
                                                    Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                                    return;
                                                }

                                                String mCustId = "";
                                                if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                                    mCustId = "&cust_id=" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                                                }

                                                String mPromoId = "";
                                                if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                                    mPromoId = "&promo_id=" + mCouponResponsePojo.promo_id;
                                                }


                                                Map<String, String> parameter = new HashMap<String, String>();

                                                String mUrl = "https://www.managemyticket.net/android/api/theme_park_migs_server_v3.php"/*?tp_ukey=" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "") + "&cust_name=" + mUserFullName
                                            + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + mCustId + mPromoId*/;


                                                parameter.put("careexp", lastDigitYear + strMonth);
                                                parameter.put("cardno", strCardNo);
                                                parameter.put("cardcvv", strCardCvv);
                                                parameter.put("cardtype", cardType);
                                                parameter.put("tp_ukey", MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, ""));
                                                parameter.put("cust_name", mUserFullName);
                                                parameter.put("cust_email", mUserEmail);
                                                parameter.put("cust_mob", mUserMobileNumber);
                                                parameter.put("cust_id", mCustId);
                                                parameter.put("promo_id", mPromoId);

                                                Log.d("Log", "custID" + mCustId);
                                                Log.d("Log", "promo" + mPromoId);

                                                Bundle bundle = new Bundle();
                                                bundle.putString(Constants.BNDL_TITLE, mPark.name);
                                                bundle.putString(Constants.BNDL_URL, mUrl);
                                                bundle.putSerializable("postdata", (Serializable) parameter);
//                    bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                                                bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_DEBIT);
                                                switchFragment(ThemeParkProcessFragment.newInstance(), ThemeParkProcessFragment.Tag_ThemeParkProcessFragment, bundle);
                                                dialog.dismiss();
                                                Utils.hideKeyboard(getActivity());
                                            } else {
                                                Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 100"));
                                            }
                                        } else {
                                            callZeroPayment();
                                        }
                                    }


                                }
                            });

                            dialog.show();


                        }
                    } else {
                        //Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();
                        Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));
                    }
                } else if (sApplicablePGWS.contains("5")) {
                    if (mCurrency.equals("USD")) {
                        if (isValidField()) {

                            if (!isCreditCardEnabled) {
                                Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                                return;
                            }

                            final Dialog dialog = new Dialog(getActivity());
                            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_card_info);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            Spinner spinnerCardType = dialog.findViewById(R.id.spinnerCardType);
                            EditText editTextCardNo = dialog.findViewById(R.id.editCardNo);
                            EditText editTextCvv = dialog.findViewById(R.id.editCvv);
                            TextView btSubmit = dialog.findViewById(R.id.tvSubmit);
                            TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                            TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                            Spinner spinnerMonth = dialog.findViewById(R.id.spinnerMonth);
                            Spinner spinnerYear = dialog.findViewById(R.id.spinnerYear);

                            if (sCRDBCharges.equals("")) {
                                tvCharges.setVisibility(View.GONE);
                            } else if (sCRDBCharges.equals("0.00")) {
                                tvCharges.setVisibility(View.GONE);
                            } else {
                                tvCharges.setText("Extra Charges Applicable : " + mCurrency + " " + sCRDBCharges);
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

                       /* if(mPaymentSummaryPojo!=null){
                            if(mTotalAmount<=0.0){
                                paymentLinearLayout.setVisibility(View.GONE);
                                linearPayDiscout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callZeroPayment();
                                    }
                                });
                            }
                        }*/

                                    if (mPaymentSummaryPojo != null) {
                                        if (mTotalAmount > 0) {
                                            if (mTotalAmount >= 100) {

                                                if (!Utils.isInternetConnected(getActivity())) {
                                                    Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                                    return;
                                                }

                                                String mCustId = "";
                                                if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                                    mCustId = "&cust_id=" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                                                }

                                                String mPromoId = "";
                                                if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                                    mPromoId = "&promo_id=" + mCouponResponsePojo.promo_id;
                                                }


                                                Map<String, String> parameter = new HashMap<String, String>();

                                                String mUrl = "https://www.managemyticket.net/android/api/theme_park_migs_server_v3.php"/*?tp_ukey=" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "") + "&cust_name=" + mUserFullName
                                            + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + mCustId + mPromoId*/;


                                                parameter.put("careexp", lastDigitYear + strMonth);
                                                parameter.put("cardno", strCardNo);
                                                parameter.put("cardcvv", strCardCvv);
                                                parameter.put("cardtype", cardType);
                                                parameter.put("tp_ukey", MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, ""));
                                                parameter.put("cust_name", mUserFullName);
                                                parameter.put("cust_email", mUserEmail);
                                                parameter.put("cust_mob", mUserMobileNumber);
                                                parameter.put("cust_id", mCustId);
                                                parameter.put("promo_id", mPromoId);

                                                Log.d("Log", "custID" + mCustId);
                                                Log.d("Log", "promo" + mPromoId);


                                                Bundle bundle = new Bundle();
                                                bundle.putString(Constants.BNDL_TITLE, mPark.name);
                                                bundle.putString(Constants.BNDL_URL, mUrl);
                                                bundle.putSerializable("postdata", (Serializable) parameter);
//                    bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                                                bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_DEBIT);

                                                switchFragment(ThemeParkProcessFragment.newInstance(), ThemeParkProcessFragment.Tag_ThemeParkProcessFragment, bundle);

                                                dialog.dismiss();

                                                Utils.hideKeyboard(getActivity());
                                            } else {
                                                Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 100"));
                                            }

                                        } else {

                                            callZeroPayment();
                                        }
                                    }


                                }
                            });

                            dialog.show();


                        }
                    } else {
                        //   Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();

                        Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));
                    }
                } else {
                    if (isValidField()) {

                        if (!isCreditCardEnabled) {
                            Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                            return;
                        }

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_card_info);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        Spinner spinnerCardType = dialog.findViewById(R.id.spinnerCardType);
                        EditText editTextCardNo = dialog.findViewById(R.id.editCardNo);
                        EditText editTextCvv = dialog.findViewById(R.id.editCvv);
                        TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                        TextView btSubmit = dialog.findViewById(R.id.tvSubmit);
                        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                        Spinner spinnerMonth = dialog.findViewById(R.id.spinnerMonth);
                        Spinner spinnerYear = dialog.findViewById(R.id.spinnerYear);
                        if (sCRDBCharges.equals("")) {
                            tvCharges.setVisibility(View.GONE);
                        } else if (sCRDBCharges.equals("0.00")) {
                            tvCharges.setVisibility(View.GONE);
                        } else {
                            tvCharges.setText("Extra Charges Applicable : " + mCurrency + " " + sCRDBCharges);
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

                       /* if(mPaymentSummaryPojo!=null){
                            if(mTotalAmount<=0.0){
                                paymentLinearLayout.setVisibility(View.GONE);
                                linearPayDiscout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callZeroPayment();
                                    }
                                });
                            }
                        }*/

                                if (mPaymentSummaryPojo != null) {
                                    if (mTotalAmount > 0) {
                                        if (mTotalAmount >= 100) {

                                            if (!Utils.isInternetConnected(getActivity())) {
                                                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                                return;
                                            }

                                            String mCustId = "";
                                            if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                                mCustId = "&cust_id=" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                                            }

                                            String mPromoId = "";
                                            if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                                mPromoId = "&promo_id=" + mCouponResponsePojo.promo_id;
                                            }


                                            Map<String, String> parameter = new HashMap<String, String>();

                                            String mUrl = "https://www.managemyticket.net/android/api/theme_park_migs_server_v3.php"/*?tp_ukey=" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "") + "&cust_name=" + mUserFullName
                                            + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + mCustId + mPromoId*/;
                                            Log.d("Log", "URL : " + mUrl);

                                            parameter.put("careexp", lastDigitYear + strMonth);
                                            parameter.put("cardno", strCardNo);
                                            parameter.put("cardcvv", strCardCvv);
                                            parameter.put("cardtype", cardType);
                                            parameter.put("tp_ukey", MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, ""));
                                            parameter.put("cust_name", mUserFullName);
                                            parameter.put("cust_email", mUserEmail);
                                            parameter.put("cust_mob", mUserMobileNumber);
                                            parameter.put("cust_id", mCustId);
                                            parameter.put("promo_id", mPromoId);

                                            Log.d("Log", "custID" + mCustId);
                                            Log.d("Log", "promo" + mPromoId);


                                            Bundle bundle = new Bundle();
                                            bundle.putString(Constants.BNDL_TITLE, mPark.name);
                                            bundle.putString(Constants.BNDL_URL, mUrl);
                                            bundle.putSerializable("postdata", (Serializable) parameter);
//                    bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                                            bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_DEBIT);

                                            switchFragment(ThemeParkProcessFragment.newInstance(), ThemeParkProcessFragment.Tag_ThemeParkProcessFragment, bundle);

                                            dialog.dismiss();

                                            Utils.hideKeyboard(getActivity());
                                        } else {
                                            Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 100"));
                                        }

                                    } else {

                                        callZeroPayment();
                                    }
                                }


                            }
                        });

                        dialog.show();


                    }
                }
            } else {
                //   Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();

                Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));
            }

        } else if (view == rltTigoPesa) {
            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("2")) {
                if (isValidField()) {

                    if (!isTigoEnabled) {
                        Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                        return;
                    }

                    if (mPaymentSummaryPojo != null) {
                        if (mTotalAmount > 0) {
                            if (mTotalAmount >= 1000) {

                                final Dialog dialog = new Dialog(getActivity());
                                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_payment_mpesa);
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                                TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                                final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                                TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
//                    final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                                Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                                final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);

                                if (sTigoCharges.equals("")) {
                                    tvCharges.setVisibility(View.GONE);
                                } else if (sTigoCharges.equals("0.00")) {
                                    tvCharges.setVisibility(View.GONE);
                                } else {
                                    tvCharges.setText("Extra Charges Applicable : " + mCurrency + " " + sTigoCharges);
                                    tvCharges.setVisibility(View.VISIBLE);
                                }
                                tvTitle.setText("" + getString(R.string.tigo_pesa));

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

                                        String mPromoId = "";
                                        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                            mPromoId = "&promo_id=" + mCouponResponsePojo.promo_id;
                                        }

                                        String mUrl = "https://managemyticket.net/android/api/theme_park_tigo_pesa_v2.php?tp_ukey=" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "") + "&cust_name=" + mUserFullName
                                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&tigo="
                                                + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code + "" + etMobileNumber.getText().toString() + "&tigo_term=1" + mCustId + mPromoId;

                                        Bundle bundle = new Bundle();
                                        bundle.putString(Constants.BNDL_TITLE, mPark.name);
                                        bundle.putString(Constants.BNDL_URL, mUrl);
//                            bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                                        bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_TIGO);

                                        switchFragment(ThemeParkProcessFragment.newInstance(), ThemeParkProcessFragment.Tag_ThemeParkProcessFragment, bundle);

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
                                Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 1000"));
                            }
                        } else {
                            callZeroPayment();
                        }
                    }
                }
            } else {
                // Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();

                Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));
            }
        } else if (view == rltMPesa) {

            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("3")) {
                if (isValidField()) {

                    if (!isMpesaEnabled) {
                        Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                        return;
                    }

                    if (mPaymentSummaryPojo != null) {
                        if (mTotalAmount > 0) {
                            if (mTotalAmount >= 100) {

                                final Dialog dialog = new Dialog(getActivity());
                                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_payment_mpesa);
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                                final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                                TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
//                    final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                                TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                                Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                                final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);

                                tvTitle.setText("" + getString(R.string.mpesa));
                                if (sMPesaCharges.equals("")) {
                                    tvCharges.setVisibility(View.GONE);
                                } else if (sMPesaCharges.equals("0.00")) {
                                    tvCharges.setVisibility(View.GONE);
                                } else {
                                    tvCharges.setText("Extra Charges Applicable : " + mCurrency + " " + sMPesaCharges);
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

                                        String mPromoId = "";
                                        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                            mPromoId = "&promo_id=" + mCouponResponsePojo.promo_id;
                                        }

                                        String mUrl = "https://managemyticket.net/android/api/theme_park_mpesa_v2.php?tp_ukey=" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "") + "&cust_name=" + mUserFullName
                                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&mpesa="
                                                + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code + "" + etMobileNumber.getText().toString() + "&mpesa_term=1" + mCustId + mPromoId;

                                        Bundle bundle = new Bundle();
                                        bundle.putString(Constants.BNDL_TITLE, mPark.name);
                                        bundle.putString(Constants.BNDL_URL, mUrl);
//                            bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                                        bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_MPESA);

                                        switchFragment(ThemeParkProcessFragment.newInstance(), ThemeParkProcessFragment.Tag_ThemeParkProcessFragment, bundle);

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
                                Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 100"));
                            }

                        } else {
                            callZeroPayment();
                        }
                    }
                }
            } else {
                //  Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();

                Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));
            }

        } else if (view == rltAirtelMoney) {
            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("4")) {
                if (isValidField()) {

                    if (!isAirtelMoneyEnabled) {
                        Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                        return;
                    }

                    if (mPaymentSummaryPojo != null) {
                        if (mTotalAmount > 0) {
                            if (mTotalAmount >= 100) {

                                final Dialog dialog = new Dialog(getActivity());
                                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_payment_mpesa);
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                                final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                                TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
                                TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
//                    final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                                Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                                final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);

                                tvTitle.setText("" + getString(R.string.airtel_money));
                                if (sAirtelCharges.equals("")) {
                                    tvCharges.setVisibility(View.GONE);
                                } else if (sAirtelCharges.equals("0.00")) {
                                    tvCharges.setVisibility(View.GONE);
                                } else {
                                    tvCharges.setText("Extra Charges Applicable : " + mCurrency + " " + sAirtelCharges);
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
                                spinCountryCode.setEnabled(false);
                                spinCountryCode.setSelection(214);
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

                                        String mPromoId = "";
                                        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                            mPromoId = "&promo_id=" + mCouponResponsePojo.promo_id;
                                        }

//                                    API: https://www.managemyticket.net/android/api/themepark_airtel.php?tp_ukey=1569056062091&cust_name=Nikhil&cust_email=otapp.test007@gmail.com&cust_mob=+255895623124&cust_id=716&airtel=+255895623124&airtel_term=1

                                        String mUrl = "https://managemyticket.net/android/api/themepark_airtel.php?tp_ukey=" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "") + "&cust_name=" + mUserFullName
                                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&airtel="
                                                + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code + "" + etMobileNumber.getText().toString() + "&airtel_term=1" + mCustId + mPromoId;

                                        Bundle bundle = new Bundle();
                                        bundle.putString(Constants.BNDL_TITLE, mPark.name);
                                        bundle.putString(Constants.BNDL_URL, mUrl);
//                            bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                                        bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_AIRTEL);

                                        switchFragment(ThemeParkProcessFragment.newInstance(), ThemeParkProcessFragment.Tag_ThemeParkProcessFragment, bundle);

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
                                Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 100"));
                            }

                        } else {
                            callZeroPayment();
                        }
                    }
                }
            } else {
                //  Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();

                Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));
            }
        }

    }

    private void callZeroPayment() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("tp_ukey", "" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, ""));
        jsonParams.put("cust_name", "" + mUserFullName);
        jsonParams.put("cust_email", "" + mUserEmail);
        jsonParams.put("cust_mob", "" + spinCountryCode.getText().toString()+ "" + etMobileNumber.getText().toString());

        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
            jsonParams.put("cust_id", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
        }

        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
            jsonParams.put("promo_id", "" + mCouponResponsePojo.promo_id);
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ParkZeroResponse> mCall = mApiInterface.getParkZeroPayment(jsonParams);
        mCall.enqueue(new Callback<ParkZeroResponse>() {
            @Override
            public void onResponse(Call<ParkZeroResponse> call, Response<ParkZeroResponse> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    ParkZeroResponse mParkZeroResponse = response.body();
                    if (mParkZeroResponse != null) {
                        if (mParkZeroResponse.status.equalsIgnoreCase("200")) {
                            if (!TextUtils.isEmpty(mParkZeroResponse.tkt_no)) {
                                setPaymentSucessful(mParkZeroResponse.tkt_no);
                            }
                        } else {
                            Utils.showToast(getActivity(), mParkZeroResponse.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ParkZeroResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void setPaymentSucessful(String mBookingID) {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }
/*
        Utils.showProgressDialog(getActivity());*/

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ThemeParkSuccessPojo> mCall = mApiInterface.getThemeParkPaymentSuccess(mBookingID, Otapp.mUniqueID);
        mCall.enqueue(new Callback<ThemeParkSuccessPojo>() {
            @Override
            public void onResponse(Call<ThemeParkSuccessPojo> call, Response<ThemeParkSuccessPojo> response) {

                Utils.closeProgressDialog();
                if (response.isSuccessful()) {

                    ThemeParkSuccessPojo mThemeParkSuccessPojo = response.body();
                    if (mThemeParkSuccessPojo != null) {
                        if (mThemeParkSuccessPojo.status.equalsIgnoreCase("200")) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_THEME_PARK_RESPONSE, new Gson().toJson(mThemeParkSuccessPojo));
                            switchFragment(ThemeParkOrderReviewFragment.newInstance(), ThemeParkOrderReviewFragment.Tag_ThemeParkOrderReviewFragment, bundle);
                        } else {
                            Utils.showToast(getActivity(), mThemeParkSuccessPojo.message);
                            popBackStack(ServiceFragment.Tag_ServiceFragment);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ThemeParkSuccessPojo> call, Throwable t) {
                LogUtils.e("", "onFailure::" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_PARK_COUPON_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                String mCouponResponse = data.getExtras().getString(Constants.BNDL_COUPON_RESPONSE);
                if (!TextUtils.isEmpty(mCouponResponse)) {
                    mCouponResponsePojo = new Gson().fromJson(mCouponResponse, CouponResponsePojo.class);
                    if (mCouponResponsePojo != null) {
//                        mTotalPrice = (int) (mTotalPrice - Float.parseFloat(mCouponResponsePojo.discount));
//
//                        tvGrandTotal.setText(mCurrency + " " + Utils.setPrice(mTotalPrice));
//                        tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mTotalPrice));

                        getPaymentSummary();

                        //  lnrApplyOffer.setVisibility(View.GONE);


                    }
                }
            }

        }
    }

    private boolean isValidField() {

        mUserEmail = "" + etEmailID.getText().toString();
        mUserFullName = "" + etFirstName.getText().toString() + " " + etLastName.getText().toString();
        //  mUserMobileNumber = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code + "" + etMobileNumber.getText().toString();
        if (etMobileNumber.getText().length() > 10) {
            mUserMobileNumber = etMobileNumber.getText().toString();
        } else {
            mUserMobileNumber = spinCountryCode.getText().toString() + "" + etMobileNumber.getText().toString();
        }

        if (TextUtils.isEmpty(etFirstName.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_firstname));
            return false;
        } else if (TextUtils.isEmpty(etLastName.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_lastname));
            return false;
        } else if (TextUtils.isEmpty(etEmailID.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_email));
            return false;
        } else if (!Utils.isValidEmail(etEmailID.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_email));
            return false;
        } else if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_phone));
            return false;
        } else /*if (flag == 0) {
            if (etMobileNumber.getText().toString().length() < mMobNumberMaxLength || etMobileNumber.getText().toString().length() > mMobNumberMaxLength) {
                Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobNumberMaxLength));
                return false;
            }
        } else if (mobFlag == 1) {
            Utils.showToast(getActivity(), "" + String.format("Enter Valid Number", "" + mMobNumberMaxLength));
            return false;
        }*/
            if(flag==0){
                if (etMobileNumber.getText().toString().length() + spinCountryCode.getText().toString().length() != 13) {
                     Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone_number));

                    return false;
                }
            }else {
                if ((MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+"))) {
                    if (etMobileNumber.getText().toString().length() != 13) {
                        Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone_number));
                        return false;
                    }
                } else {
                    if (etMobileNumber.getText().toString().length() + spinCountryCode.getText().toString().length() != 13) {
                        Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone_number));
                        return false;
                    }
                }
            }
        return true;
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

                    /*        setSpinCountryCode();*/

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

    public void removeCouponCode() {

        String mPromoCode = "";
        String mCurrency = "";
        String mPlatform = "A";


//        key=*MD5(SHA512(promo_code+prop_id+tkt_info+tot_fare+ukey+cur+platform+"pr0mOCode"))

        String mKey = Utils.getCouponCodeKey(mPromoCode + themeParkProp + srtTicketInfo + promoFareAmt + mUserKey + mCurrency + mPlatform + "pr0mOCode");

//        https://www.managemyticket.net/android/api/promo/Validate_Promo.php?promo_code=STANCHART800&prop_id=23&tkt_info={"tkts_info": [{"tkt_id": "4770", "tot_tkt_id_tkts_count": "2", "single_fare": "10000.00", "seats": "A1,A2"}]}&tot_fare=20000.00&ukey=TEST1234&cur=TSH&platform=W&key=b651cdfb275d53e9f39b1f379b26ac68

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }
        Map<String, String> jsonParams = new HashMap<>();

        jsonParams.put("promo_code", "" + mPromoCode);
        jsonParams.put("cur", "" + mCurrency);

        jsonParams.put("key", "" + mKey);
        jsonParams.put("platform", "" + mPlatform);
        jsonParams.put("ukey", "" + mUserKey);
        jsonParams.put("tot_fare", "" + promoFareAmt);
        jsonParams.put("prop_id", "" + themeParkProp);
        jsonParams.put("tkt_info", "" + srtTicketInfo);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<CouponResponsePojo> mCall = mApiInterface.validateCouponCode(jsonParams);
        mCall.enqueue(new Callback<CouponResponsePojo>() {
            @Override
            public void onResponse(Call<CouponResponsePojo> call, Response<CouponResponsePojo> response) {
                mCouponResponsePojo = response.body();
                if (Utils.isProgressDialogShowing()) {
                    Utils.closeProgressDialog();
                }
                if (mCouponResponsePojo != null) {
                    if (mCouponResponsePojo.status.equalsIgnoreCase("200")) {

                    } else {

                        MyPref.setPref(getActivity(), "ENABLED_PAYMENT_METHODS", "0");

                        if (mCouponResponsePojo.message.equals("Invalid currecny or currecny not found!!")) {
                            Utils.showToast(getActivity(), "Promo Code Successfully removed");
                        } else {
                            Utils.showToast(getActivity(), mCouponResponsePojo.message);
                        }
                        MyPref.setPref(getContext(), "PromoCode", "");
                        popBackStack();
                    }
                }
            }

            @Override
            public void onFailure(Call<CouponResponsePojo> call, Throwable t) {
                if (Utils.isProgressDialogShowing()) {
                    Utils.closeProgressDialog();
                }
            }
        });

    }

}
