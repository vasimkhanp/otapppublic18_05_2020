package com.otapp.net.Events.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.loadingview.LoadingView;
import com.google.gson.Gson;
import com.otapp.net.Events.Adapter.EventRecyclerPaymentSummaryAdapter;
import com.otapp.net.Events.Core.EventPaymentSummaryResponse;
import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.R;
import com.otapp.net.adapter.CountryCodeSpinAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.io.Serializable;
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

/*import com.otapp.net.application.Otapp;*/

public class EventPaymentActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView imgBack;
    @BindView(R.id.toolbarTitle)
    TextView pageTitle;
    @BindView(R.id.tvEventAddress)
    TextView tvEventAddress;
    @BindView(R.id.tvEventName)
    TextView tvEventName;
    @BindView(R.id.tvEventDate)
    TextView tvEventDate;
    @BindView(R.id.tvNoOfTickets)
    TextView tvNoOfTickets;
    @BindView(R.id.loadingView)
    LoadingView loadingView;
    @BindView(R.id.recycleViewFare)
    RecyclerView recyclerViewFare;
    @BindView(R.id.tvGrandTotal)
    TextView tvGrandTotal;

    @BindView(R.id.linearCard)
    LinearLayout linearLayoutCard;
    @BindView(R.id.linearTigo)
    LinearLayout linearLayoutTigo;
    @BindView(R.id.linearMpesa)
    LinearLayout linearLayoutMpesa;
    @BindView(R.id.linearAirtel)
    LinearLayout linearLayoutAirtelMoney;
    @BindView(R.id.imageViewAd)
    ImageView imageViewAdv;


    String strDate="",monthsplit="",strTime="";
    String timeArray[]= new String[]{};
    String strEventResponse = "", strFareResponse = "", strCurrency = "", strEventId = "", strEventDate = "", strUkey = "",strPromoId="",sMPesaCharges="";
    String sTigoPesaCharges="", sAirtelPesaCharges="",sCrdbCharges="",strMailId="",strMobile="";
    int noOfTickets;

    String strCardNo, strCardType, strCardExpiryDt, strCardCvv, strMonth, strYear;
    int year = 0, expYear = 0;
    int month = 0;

    private float mTotalAmount;

    public boolean isCreditCardEnabled = false, isMpesaEnabled = false, isTigoEnabled = false, isAirtelMoneyEnabled = false;

    private int mMobInnerNumberMaxLength = 9;

    EventsListResponse.Events eventsListResponse;
    EventPaymentSummaryResponse.Fare fareResponse;
    List<EventPaymentSummaryResponse.Fare> fareList;

    EventPaymentSummaryResponse paymentSumarryResponse;
    List<EventPaymentSummaryResponse.Pgws> eventPaymetPGWList = new ArrayList<>();

    EventRecyclerPaymentSummaryAdapter eventRecyclerPaymentSummary;
    CountryCodePojo mCountryCodePojo;
    List<CountryCodePojo.Ad5> advCountrycode = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_payment);

        ButterKnife.bind(this);
        fareList = new ArrayList<>();

        pageTitle.setText(R.string.almost_there);
        strEventResponse = getIntent().getExtras().getString("events");
        fareList = getIntent().getExtras().getParcelableArrayList("fare");
        noOfTickets = getIntent().getExtras().getInt("noOfTickets");
        strCurrency = getIntent().getExtras().getString("currency");
        strMailId=getIntent().getExtras().getString("email");
        strMobile=getIntent().getExtras().getString("mobile");

        eventsListResponse = new Gson().fromJson(strEventResponse, EventsListResponse.Events.class);

        paymentSumarryResponse = new Gson().fromJson(getIntent().getExtras().getString("paymentSumarryResponse"), EventPaymentSummaryResponse.class);


        fareResponse = new Gson().fromJson(strFareResponse, EventPaymentSummaryResponse.Fare.class);
        recyclerViewFare.setHasFixedSize(true);
        recyclerViewFare.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initialize();

    }

    public void initialize() {
        advCountrycode= Otapp.mAdsPojoList;
        try {
            byte[] data = Base64.decode(eventsListResponse.event_address, Base64.DEFAULT);
            tvEventAddress.setText(new String(data, "UTF-8") + "," + eventsListResponse.event_city);
        } catch (Exception e) {
            e.printStackTrace();
        }

        strUkey= MyPref.getPref(getApplicationContext(), MyPref.PREF_UKEY,"");
        strEventDate= MyPref.getPref(getApplicationContext(), MyPref.PREF_EVENT_DATE,"");
        strEventId= MyPref.getPref(getApplicationContext(), MyPref.PREF_EVENT_ID,"");
        strPromoId= MyPref.getPref(getApplicationContext(), MyPref.PREF_PROMO_ID,"");

        tvEventName.setText(eventsListResponse.event_name);
        tvNoOfTickets.setText("" + noOfTickets);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(strEventDate);

          /*  String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);*/
            strDate = (String) android.text.format.DateFormat.format("dd", date); // 20
            monthsplit = (String) android.text.format.DateFormat.format("MMM", date); // Jun
            /*String monthNumber = (String) android.text.format.DateFormat.format("MM", date); // 06
            String year = (String) android.text.format.DateFormat.format("yyyy", date); // 2013*/
        }catch (Exception e){
            e.printStackTrace();
        }
        timeArray=strEventDate.split(" ");
        strTime=timeArray[1];



        tvEventDate.setText(strDate+" "+monthsplit+" | "+strTime);
        eventRecyclerPaymentSummary = new EventRecyclerPaymentSummaryAdapter(getApplicationContext(), fareList, strCurrency);
        recyclerViewFare.setAdapter(eventRecyclerPaymentSummary);
        eventRecyclerPaymentSummary.notifyDataSetChanged();
        tvGrandTotal.setText(fareList.get(fareList.size() - 1).amount);
        mTotalAmount = Float.parseFloat(fareList.get(fareList.size() - 1).amount.replaceAll(",",""));

        Glide.with(getApplicationContext()).load(advCountrycode.get(5).image_path).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(imageViewAdv);

        if (paymentSumarryResponse != null) {

            eventPaymetPGWList = paymentSumarryResponse.pgwsList;

            if (paymentSumarryResponse.pgwsList.size() > 0 && paymentSumarryResponse.pgwsList != null) {

                for (int i = 0; i < paymentSumarryResponse.pgwsList.size(); i++) {

                    if (paymentSumarryResponse.pgwsList.get(i).pgw_name.equalsIgnoreCase("Credit/Debit Card")) {
                        sCrdbCharges=paymentSumarryResponse.pgwsList.get(i).extra_pgw_charges;
                        if (paymentSumarryResponse.pgwsList.get(i).pgw_enabled.equals("1")) {
                            isCreditCardEnabled = true;
                        }else {
                            isCreditCardEnabled = false;
                        }
                    } else {
                        if (paymentSumarryResponse.pgwsList.get(i).pgw_name.equalsIgnoreCase("Tigo Pesa")) {
                            sTigoPesaCharges=paymentSumarryResponse.pgwsList.get(i).extra_pgw_charges;
                            if (paymentSumarryResponse.pgwsList.get(i).pgw_enabled.equals("1")) {
                                isTigoEnabled = true;
                            }else {
                                isTigoEnabled = false;
                            }
                        } else {
                            if (paymentSumarryResponse.pgwsList.get(i).pgw_name.equalsIgnoreCase("MPESA")) {
                                sMPesaCharges=paymentSumarryResponse.pgwsList.get(i).extra_pgw_charges;
                                if (paymentSumarryResponse.pgwsList.get(i).pgw_enabled.equals("1")) {
                                    isMpesaEnabled = true;
                                }else {
                                    isMpesaEnabled = false;
                                }
                            } else {
                                if (paymentSumarryResponse.pgwsList.get(i).pgw_name.equalsIgnoreCase("Airtel")) {
                                    sAirtelPesaCharges=paymentSumarryResponse.pgwsList.get(i).extra_pgw_charges;
                                    if (paymentSumarryResponse.pgwsList.get(i).pgw_enabled.equals("1")) {
                                        isAirtelMoneyEnabled = true;
                                    }else {
                                        isAirtelMoneyEnabled = false;
                                    }
                                }
                            }
                        }
                    }
                }


            } else {
                isCreditCardEnabled = false;
                isTigoEnabled = false;
                isMpesaEnabled = false;
                isAirtelMoneyEnabled = false;
            }
        }


    }

    @OnClick(R.id.linearCard)
    public void cardPayment() {
        String sApplicablePGWS = MyPref.getPref(getApplicationContext(), "ENABLED_PAYMENT_METHODS", "0");
        if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("1") || sApplicablePGWS.contains("5")) {
            final Dialog dialog = new Dialog(this);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_card_info);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            final Spinner spinnerCardType = dialog.findViewById(R.id.spinnerCardType);
            final EditText ccEditText = dialog.findViewById(R.id.editCardNo);
            final EditText editTextCvv = dialog.findViewById(R.id.editCvv);
            TextView btSubmit = dialog.findViewById(R.id.tvSubmit);
            TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
            final Spinner spinnerMonth = dialog.findViewById(R.id.spinnerMonth);
            final Spinner spinnerYear = dialog.findViewById(R.id.spinnerYear);

            ccEditText.addTextChangedListener(new TextWatcher() {
                private static final char space = ' ';
                int count = 0;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (count <= ccEditText.getText().toString().length()
                            && (ccEditText.getText().toString().length() == 4
                            || ccEditText.getText().toString().length() == 9
                            || ccEditText.getText().toString().length() == 14)) {
                        ccEditText.setText(ccEditText.getText().toString() + " ");
                        int pos = ccEditText.getText().length();
                        ccEditText.setSelection(pos);
                    } else if (count >= ccEditText.getText().toString().length()
                            && (ccEditText.getText().toString().length() == 4
                            || ccEditText.getText().toString().length() == 9
                            || ccEditText.getText().toString().length() == 14)) {
                        ccEditText.setText(ccEditText.getText().toString().substring(0, ccEditText.getText().toString().length() - 1));
                        int pos = ccEditText.getText().length();
                        ccEditText.setSelection(pos);
                    }
                    count = ccEditText.getText().toString().length();
                }
            });

            List<String> cardTypeList = new ArrayList<>();
            cardTypeList.add("Select Card Type");
            cardTypeList.add("Mastercard");
            cardTypeList.add("Visa");
            cardTypeList.add("American Express");
            cardTypeList.add("Amex Corporate Purchase Card");
            cardTypeList.add("Diners Club");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_spinner_date, cardTypeList);
            spinnerCardType.setAdapter(dataAdapter);


            year = Calendar.getInstance().get(Calendar.YEAR);
            expYear = year;
            List<String> yearList = new ArrayList<>();
            final List<String> monthList = new ArrayList<>();
            for (int i = 0; i < 32; i++) {
                yearList.add("" + year);
                year++;
            }
            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_spinner_date, yearList);
            spinnerYear.setAdapter(yearAdapter);

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

                    ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_spinner_date, monthList);
                    spinnerMonth.setAdapter(monthAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lastDigitYear;
                    String cardType = "";

                    if (spinnerCardType.getSelectedItem().toString().equals("Select Card Type")) {
                        Utils.showToast(getApplicationContext(), getString(R.string.alert_card_card_type));
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
                    if (TextUtils.isEmpty(ccEditText.getText().toString())) {
                        Utils.showToast(getApplicationContext(), getString(R.string.alert_card_number));
                        return;
                    } else {
                        strCardNo = ccEditText.getText().toString().replaceAll("\\s", "");
                    }
                    if (ccEditText.getText().length() < 19) {
                        Utils.showToast(getApplicationContext(), getString(R.string.alart_valid_card_number));
                        return;
                    } else {
                        strCardNo = ccEditText.getText().toString().replaceAll("\\s", "");
                    }

                    if (TextUtils.isEmpty(editTextCvv.getText().toString())) {
                        Utils.showToast(getApplicationContext(), getString(R.string.alert_card_cvv));
                        return;
                    } else if (editTextCvv.getText().length() < 3) {
                        Utils.showToast(getApplicationContext(), getString(R.string.aler_card_cvv_lenth));
                        return;
                    } else {
                        strCardCvv = editTextCvv.getText().toString();
                    }
                    //       month=Calendar.getInstance().get(Calendar.MONTH);


                    strMonth = spinnerMonth.getSelectedItem().toString();
                    strYear = spinnerYear.getSelectedItem().toString();
                    Log.d("Log", "mont :" + month + "  " + strMonth);
                    Log.d("Log", "Year :" + year + "  " + strYear);
                    Log.d("Log", "Year :" + expYear);

                    lastDigitYear = strYear.substring(2, 4);


                    Log.d("Log", "exp " + strCardExpiryDt);
                    Log.d("Log", "cvv " + strCardCvv);
                    Log.d("Log", "Year " + lastDigitYear);

                    if (mTotalAmount >= 100) {
                        if (!Utils.isInternetConnected(getApplicationContext())) {
                            Utils.showToast(getApplicationContext(), getString(R.string.msg_no_internet));
                            return;
                        }
                        String mCustId = "";
                        if (MyPref.getPref(getApplicationContext(), MyPref.PREF_IS_LOGGED, false)) {
                            mCustId = MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_ID, "");
                        }
                        Map<String, String> parameter = new HashMap<String, String>();

                        String mUrl = "https://www.managemyticket.net/api/Event/Event_CRDB.php?";

                        parameter.put("careexp", lastDigitYear + strMonth);
                        parameter.put("cardno", strCardNo);
                        parameter.put("cardcvv", strCardCvv);
                        parameter.put("cardtype", cardType);
                        parameter.put("no_tkts", String.valueOf(noOfTickets));
                        parameter.put("cust_name", MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_FULLNAME, ""));
                        parameter.put("cust_email", strMailId);
                        parameter.put("cust_mob", strMobile);
                        parameter.put("ukey",strUkey );
                        parameter.put("evt_date_time", strEventDate);
                        parameter.put("evt_pid", strEventId);
                        parameter.put("cust_id", mCustId);
                        parameter.put("promo_id", strPromoId);

                        Intent intent = new Intent(getApplicationContext(), EventPaymentProcessingActivity.class);
                        intent.putExtra(AppConstants.BNDL_TITLE,eventsListResponse.event_name);
                       // Toast.makeText(EventPaymentActivity.this, ""+eventsListResponse.event_name, Toast.LENGTH_SHORT).show();
                        intent.putExtra(AppConstants.BNDL_URL, mUrl);
                        intent.putExtra("postdata", (Serializable) parameter);
                        intent.putExtra(AppConstants.BNDL_PAYMENT_TYPE, AppConstants.BNDL_PAYMENT_TYPE_DEBIT);
                        startActivity(intent);

                    }

                }
            });


            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        dialog.cancel();
                    }

                }
            });

            dialog.show();
        }
    }

    @OnClick(R.id.linearMpesa)
    public void mPesaPayment() {
        String sApplicablePGWS = MyPref.getPref(getApplicationContext(), "ENABLED_PAYMENT_METHODS", "0");
        if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("3")) {

            if (!isMpesaEnabled) {
                Utils.showToast(getApplicationContext(), getString(R.string.msg_payment_disable));
                return;
            }

            if (mTotalAmount >= 100) {
                final Dialog dialog = new Dialog(this);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_payment_mpesa);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
//                final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                final Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);
                TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                tvTitle.setText("" + getString(R.string.mpesa));
                if (sMPesaCharges.equals("")) {
                    tvCharges.setVisibility(View.GONE);
                } else if (sMPesaCharges.equals("0.00")|| sMPesaCharges.equals("0")) {
                    tvCharges.setVisibility(View.GONE);
                } else {
                    tvCharges.setText("Extra Charges Applicable : " + strCurrency + " " + sMPesaCharges);
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

                CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getApplicationContext(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
                spinCountryCode.setAdapter(mCountryCodeSpinAdapter);

                if (TextUtils.isEmpty(MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                    spinCountryCode.setSelection(Utils.getUserCountryPosition(getApplicationContext()));
                } else {
                    spinCountryCode.setSelection(Utils.getCountryPosition(getApplicationContext()));
                }
                spinCountryCode.setEnabled(false);
                spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        MyPref.setPref(getApplicationContext(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                        mMobInnerNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                        etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobInnerNumberMaxLength)});
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                            /*Utils.hideKeyboard();*/
                        }

                    }
                });

                tvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                            Utils.showToast(getApplicationContext(), getString(R.string.alert_enter_phone));
                            return;
                        }

                        if (etMobileNumber.getText().toString().length() < mMobInnerNumberMaxLength || etMobileNumber.getText().toString().length() > mMobInnerNumberMaxLength) {
                            Utils.showToast(getApplicationContext(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobInnerNumberMaxLength));
                            return;
                        }

                        if (!Utils.isInternetConnected(getApplicationContext())) {
                            Utils.showToast(getApplicationContext(), getString(R.string.msg_no_internet));
                            return;
                        }
                        String mCustId = "";
                        if (MyPref.getPref(getApplicationContext(), MyPref.PREF_IS_LOGGED, false)) {
                            mCustId = MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_ID, "");
                        }

                        String mob= Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code+etMobileNumber.getText().toString();

                        Map<String, String> parameter = new HashMap<String, String>();

                        String mUrl="https://www.managemyticket.net/api/Event/Event_Mpesa.php?";

                        parameter.put("no_tkts", String.valueOf(noOfTickets));
                        parameter.put("cust_name", MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_FULLNAME, ""));
                        parameter.put("cust_email", strMailId);
                        parameter.put("cust_mob", strMobile);
                        parameter.put("ukey",strUkey );
                        parameter.put("evt_date_time", strEventDate);
                        parameter.put("evt_pid", strEventId);
                        parameter.put("cust_id", mCustId);
                        parameter.put("promo_id", strPromoId);
                        parameter.put("mPesa",mob);

                        Intent intent = new Intent(getApplicationContext(), EventPaymentProcessingActivity.class);
                        intent.putExtra(AppConstants.BNDL_TITLE,eventsListResponse.event_name);
                        intent.putExtra(AppConstants.BNDL_URL, mUrl);
                        intent.putExtra("postdata", (Serializable) parameter);
                        intent.putExtra(AppConstants.BNDL_PAYMENT_TYPE, AppConstants.BNDL_PAYMENT_TYPE_MPESA);
                        startActivity(intent);

                    }
                });


                dialog.show();
            }
        }
    }
    @OnClick(R.id.linearTigo)
    public void tigoPesaPayment(){
        String sApplicablePGWS = MyPref.getPref(getApplicationContext(), "ENABLED_PAYMENT_METHODS", "0");
        if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("2")) {
            if (!isTigoEnabled) {
                Utils.showToast(getApplicationContext(), getString(R.string.msg_payment_disable));
                return;
            }
            if(mTotalAmount>100){
                final Dialog dialog = new Dialog(this);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_payment_mpesa);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
//                final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                final Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);
                TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);

                tvTitle.setText("" + getString(R.string.tigo_pesa));
                if (sTigoPesaCharges.equals("")) {
                    tvCharges.setVisibility(View.GONE);
                } else if (sTigoPesaCharges.equals("0.00")|| sTigoPesaCharges.equals("0")) {
                    tvCharges.setVisibility(View.GONE);
                } else {
                    tvCharges.setText("Extra Charges Applicable : " + strCurrency + " " + sTigoPesaCharges);
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

                CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getApplicationContext(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
                spinCountryCode.setAdapter(mCountryCodeSpinAdapter);

                if (TextUtils.isEmpty(MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                    spinCountryCode.setSelection(Utils.getUserCountryPosition(getApplicationContext()));
                } else {
                    spinCountryCode.setSelection(Utils.getCountryPosition(getApplicationContext()));
                }

                spinCountryCode.setEnabled(false);
                spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        MyPref.setPref(getApplicationContext(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                        mMobInnerNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                        etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobInnerNumberMaxLength)});
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                            /*Utils.hideKeyboard();*/
                        }

                    }
                });

                tvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                            Utils.showToast(getApplicationContext(), getString(R.string.alert_enter_phone));
                            return;
                        }

                        if (etMobileNumber.getText().toString().length() < mMobInnerNumberMaxLength || etMobileNumber.getText().toString().length() > mMobInnerNumberMaxLength) {
                            Utils.showToast(getApplicationContext(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobInnerNumberMaxLength));
                            return;
                        }

                        if (!Utils.isInternetConnected(getApplicationContext())) {
                            Utils.showToast(getApplicationContext(), getString(R.string.msg_no_internet));
                            return;
                        }

                        String mCustId = "";
                        if (MyPref.getPref(getApplicationContext(), MyPref.PREF_IS_LOGGED, false)) {
                            mCustId = "&cust_id=" + MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_ID, "");
                        }
                        String mob= Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code+etMobileNumber.getText().toString();

                        Map<String, String> parameter = new HashMap<String, String>();

                        String mUrl="https://www.managemyticket.net/api/Event/Event_Tigo.php?";

                        parameter.put("no_tkts", String.valueOf(noOfTickets));
                        parameter.put("cust_name", MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_FULLNAME, ""));
                        parameter.put("cust_email", strMailId);
                        parameter.put("cust_mob", strMobile);
                        parameter.put("ukey",strUkey );
                        parameter.put("evt_date_time", strEventDate);
                        parameter.put("evt_pid", strEventId);
                        parameter.put("cust_id", mCustId);
                        parameter.put("promo_id", strPromoId);
                        parameter.put("tigo",mob);

                        Intent intent = new Intent(getApplicationContext(), EventPaymentProcessingActivity.class);
                        intent.putExtra(AppConstants.BNDL_TITLE,eventsListResponse.event_name);
                        intent.putExtra(AppConstants.BNDL_URL, mUrl);
                        intent.putExtra("postdata", (Serializable) parameter);
                        intent.putExtra(AppConstants.BNDL_PAYMENT_TYPE, AppConstants.BNDL_PAYMENT_TYPE_TIGO);
                        startActivity(intent);

                    }
                });


                dialog.show();
            }
        }
    }
    @OnClick(R.id.linearAirtel)
    public void airtelPayment(){
        String sApplicablePGWS = MyPref.getPref(getApplicationContext(), "ENABLED_PAYMENT_METHODS", "0");
        if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("4")) {
            if (!isAirtelMoneyEnabled) {
                Utils.showToast(getApplicationContext(), getString(R.string.msg_payment_disable));
                return;
            }

            if(mTotalAmount>=100){
                final Dialog dialog = new Dialog(this);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_payment_mpesa);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
//                final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                final Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);
                TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                tvTitle.setText("" + getString(R.string.airtel_money));
                if (sMPesaCharges.equals("")) {
                    tvCharges.setVisibility(View.GONE);
                } else if (sAirtelPesaCharges.equals("0.00")|| sAirtelPesaCharges.equals("0")) {
                    tvCharges.setVisibility(View.GONE);
                } else {
                    tvCharges.setText("Extra Charges Applicable : " + strCurrency + " " + sAirtelPesaCharges);
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
                CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getApplicationContext(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
                spinCountryCode.setAdapter(mCountryCodeSpinAdapter);

                if (TextUtils.isEmpty(MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                    spinCountryCode.setSelection(Utils.getUserCountryPosition(getApplicationContext()));
                } else {
                    spinCountryCode.setSelection(Utils.getCountryPosition(getApplicationContext()));
                }
                spinCountryCode.setEnabled(false);
                spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        MyPref.setPref(getApplicationContext(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                        mMobInnerNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                        etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobInnerNumberMaxLength)});
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                            /*Utils.hideKeyboard();*/
                        }

                    }
                });

                tvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                            Utils.showToast(getApplicationContext(), getString(R.string.alert_enter_phone));
                            return;
                        }

                        if (etMobileNumber.getText().toString().length() < mMobInnerNumberMaxLength || etMobileNumber.getText().toString().length() > mMobInnerNumberMaxLength) {
                            Utils.showToast(getApplicationContext(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobInnerNumberMaxLength));
                            return;
                        }

                        if (!Utils.isInternetConnected(getApplicationContext())) {
                            Utils.showToast(getApplicationContext(), getString(R.string.msg_no_internet));
                            return;
                        }

                        String mCustId = "";
                        if (MyPref.getPref(getApplicationContext(), MyPref.PREF_IS_LOGGED, false)) {
                            mCustId = "&cust_id=" + MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_ID, "");
                        }
                        String mob= Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code+etMobileNumber.getText().toString();

                        Map<String, String> parameter = new HashMap<String, String>();

                        String mUrl="https://www.managemyticket.net/api/Event/Event_Airtel.php?";

                        parameter.put("no_tkts", String.valueOf(noOfTickets));
                        parameter.put("cust_name", MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_FULLNAME, ""));
                        parameter.put("cust_email", strMailId);
                        parameter.put("cust_mob", strMobile);
                        parameter.put("ukey",strUkey );
                        parameter.put("evt_date_time", strEventDate);
                        parameter.put("evt_pid", strEventId);
                        parameter.put("cust_id", mCustId);
                        parameter.put("promo_id", strPromoId);
                        parameter.put("airtel",mob);

                        Intent intent = new Intent(getApplicationContext(), EventPaymentProcessingActivity.class);
                        intent.putExtra(AppConstants.BNDL_TITLE,eventsListResponse.event_name);
                        intent.putExtra(AppConstants.BNDL_URL, mUrl);
                        intent.putExtra("postdata", (Serializable) parameter);
                        intent.putExtra(AppConstants.BNDL_PAYMENT_TYPE, AppConstants.BNDL_PAYMENT_TYPE_AIRTEL);
                        startActivity(intent);


                    }
                });

                dialog.show();
            }
        }
    }

/*
    private void getCountryCodeList() {
        boolean isConnected = AppConstants.isConnected(getApplicationContext());
        if (isConnected) {
            Map<String, String> jsonParams = new HashMap<>();
            jsonParams.put("user_token", "" + Otapp.mUniqueID);
            loadingView.start();
            OtappApiServices otappApiServices = RestClient.getClient(true);
            Call<CountryCodePojo> mCall = otappApiServices.getCountryCodeList("");
            mCall.enqueue(new Callback<CountryCodePojo>() {
                @Override
                public void onResponse(Call<CountryCodePojo> call, Response<CountryCodePojo> response) {
                    loadingView.stop();
                    if (response.body().status.equals("200")) {
                        mCountryCodePojo = response.body();
                        Otapp.mCountryCodePojoList = mCountryCodePojo.data;
                        Otapp.mAdsPojoList = mCountryCodePojo.ad5;
                        advCountrycode = mCountryCodePojo.ad5;
                        initialize();

                    }
                }

                @Override
                public void onFailure(Call<CountryCodePojo> call, Throwable t) {
                    loadingView.stop();
                    Toast.makeText(EventPaymentActivity.this, "" + Otapp.mCountryCodePojoList.size(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
*/
@OnClick(R.id.back)
    public void onBack(){
    finish();
}

}
