package com.otapp.net.fragment;

import android.app.Dialog;
import android.os.Bundle;
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
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.CountryCodeSpinAdapter;
import com.otapp.net.adapter.EventPaymentAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.CouponResponsePojo;
import com.otapp.net.model.EventDetailsPojo;
import com.otapp.net.model.EventPaymentSummaryPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventPaymentFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_EventPaymentFragment = "Tag_" + "EventPaymentFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPlace)
    TextView tvPlace;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvQuantity)
    TextView tvQuantity;
    //    @BindView(R.id.tvSubTotal)
//    TextView tvSubTotal;
//    @BindView(R.id.tvIHF)
//    TextView tvIHF;
//    @BindView(R.id.tvTaxes)
//    TextView tvTaxes;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.rltDebicCard)
    RelativeLayout rltDebicCard;
    @BindView(R.id.rltTigoPesa)
    RelativeLayout rltTigoPesa;
    @BindView(R.id.rltMPesa)
    RelativeLayout rltMPesa;
    @BindView(R.id.rltAirtelMoney)
    RelativeLayout rltAirtelMoney;
    @BindView(R.id.lvSeat)
    ListView lvSeat;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    String strCardNo, strCardType, strCardExpiryDt, strCardCvv, strMonth, strYear;
    int year = 0, expYear = 0;
    int month = 1;
    String sCRDBCharges = "";
    String sMPesaCharges = "";
    String sTigoCharges = "";
    String sAirtelCharges = "";


    private String mUserFullName = "", mUserEmail = "", mUserMobileNumber = "", mUserKey = "", mSeatCount = "";
    private float mTotalAmount;
    public boolean isCreditCardEnabled = false, isMpesaEnabled = false, isTigoEnabled = false, isAirtelMoneyEnabled = false;

    private int mMobInnerNumberMaxLength = 9;

    private EventPaymentAdapter mEventPaymentAdapter;

    private EventPaymentSummaryPojo mEventPaymentSummaryPojo;
    private CouponResponsePojo mCouponResponsePojo;
    private EventDetailsPojo.EventDetails mEventDetails;

    public static EventPaymentFragment newInstance() {
        EventPaymentFragment fragment = new EventPaymentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_event_payment, container, false);
        ButterKnife.bind(this, mView);
        tvPay.setVisibility(View.GONE);
        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            String mEvent = bundle.getString(Constants.BNDL_EVENT_DETAILS);
            mUserKey = bundle.getString(Constants.BNDL_USER_KEY);
            mSeatCount = bundle.getString(Constants.BNDL_EVENT_SEAT_COUNT);
            mUserFullName = bundle.getString(Constants.BNDL_FULLNAME);
            mUserEmail = bundle.getString(Constants.BNDL_EMAIL);
            mUserMobileNumber = bundle.getString(Constants.BNDL_MOBILE_NUMBER);

            String mCoupon = bundle.getString(Constants.BNDL_EVENT_COUPON);
            if (!TextUtils.isEmpty(mCoupon)) {
                mCouponResponsePojo = new Gson().fromJson(bundle.getString(Constants.BNDL_EVENT_COUPON), CouponResponsePojo.class);
            }
            if (mCouponResponsePojo == null) {
              //  Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
            }
            mEventPaymentSummaryPojo = new Gson().fromJson(bundle.getString(Constants.BNDL_EVENT_PAYMENT_SUMMARY), EventPaymentSummaryPojo.class);

            if (!TextUtils.isEmpty(mEvent)) {
                mEventDetails = new Gson().fromJson(mEvent, EventDetailsPojo.EventDetails.class);

                if (mEventDetails != null) {
                    tvName.setText("" + mEventDetails.eventName);
                    tvPlace.setText("" + mEventDetails.eventAddr + ", " + mEventDetails.eventCity);
                    tvDate.setText("" + mEventDetails.eventSelectedDate);

                    int ticketCount = 0;
                    float total = 0;

                    for (int i = 0; i < mEventDetails.ticketType.size(); i++) {
                        EventDetailsPojo.TicketType mTicketType = mEventDetails.ticketType.get(i);
                        if (mTicketType != null && mTicketType.ticketCount > 0 && !TextUtils.isEmpty(mTicketType.ticketAmount)) {
                            total = total + (mTicketType.ticketCount * Float.parseFloat(mTicketType.ticketAmount));
                            ticketCount = ticketCount + mTicketType.ticketCount;
                        }
                    }

                    tvQuantity.setText("" + ticketCount);
//                    tvSubTotal.setText(mEventDetails.eventCurrency + " " + Utils.setPrice(total));
//                    tvIHF.setText(mEventDetails.eventCurrency + " " + Utils.setPrice(mEventDetails.IHF));
//                    tvTaxes.setText(mEventDetails.eventCurrency + " " + Utils.setPrice(mEventDetails.Taxes));
//                    mTotalAmount = (int) (total + mEventDetails.IHF + mEventDetails.Taxes);
//                    tvPay.setText(getString(R.string.pay) + " " + mEventDetails.eventCurrency + " " + Utils.setPrice((total + mEventDetails.IHF + mEventDetails.Taxes)));


                }

                mEventPaymentAdapter = new EventPaymentAdapter(getActivity(), mEventDetails.eventCurrency);
                lvSeat.setAdapter(mEventPaymentAdapter);

                if (mEventPaymentSummaryPojo != null) {

                    List<EventPaymentSummaryPojo.Summary> mPaymentSummaryList = mEventPaymentSummaryPojo.data.fare;
                    if (mPaymentSummaryList != null && mPaymentSummaryList.size() > 0) {

                        mTotalAmount = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).price;
                        tvPay.setText(getString(R.string.pay) + " " + mEventDetails.eventCurrency + " " + Utils.setPrice(mTotalAmount));
                        tvTotal.setText(mEventDetails.eventCurrency + " " + Utils.setPrice(mTotalAmount));

                        mEventPaymentAdapter.addAll(mPaymentSummaryList.subList(0, mPaymentSummaryList.size() - 1));
                    }

                    List<EventPaymentSummaryPojo.PaymentAllowed> paymentAllowed = mEventPaymentSummaryPojo.data.paymentAllowed;
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

        tvBack.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        rltDebicCard.setOnClickListener(this);
        rltTigoPesa.setOnClickListener(this);
        rltMPesa.setOnClickListener(this);
        rltAirtelMoney.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String sApplicablePGWS = MyPref.getPref(getContext(), "ENABLED_PAYMENT_METHODS", "0");
        Log.d("Log", "Applicable PGW " + sApplicablePGWS);
        String mPromoId = "";
        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
            mPromoId = "" + mCouponResponsePojo.promo_id;
        }
    //    Toast.makeText(getActivity(), "Promo ID " + mPromoId, Toast.LENGTH_SHORT).show();
        Log.d("Log", "mPromoId " + mPromoId);
        if (view == tvBack) {
            popBackStack();
        } else if (view == rltDebicCard) {
            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("1") || sApplicablePGWS.contains("5")) {
                if (sApplicablePGWS.contains("1")) {
                    if (mEventDetails.eventCurrency.equals("TSH")) {
                        if (!isCreditCardEnabled) {
                            Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                            return;
                        }

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_card_info);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        Spinner spinnerCardType = dialog.findViewById(R.id.spinnerCardType);
                        final EditText ccEditText = dialog.findViewById(R.id.editCardNo);
                        EditText editTextCvv = dialog.findViewById(R.id.editCvv);
                        TextView btSubmit = dialog.findViewById(R.id.tvSubmit);
                        TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                        Spinner spinnerMonth = dialog.findViewById(R.id.spinnerMonth);
                        Spinner spinnerYear = dialog.findViewById(R.id.spinnerYear);
                        if (sCRDBCharges.equals("")){
                            tvCharges.setVisibility(View.GONE);
                        }else if (sCRDBCharges.equals("0.00")){
                            tvCharges.setVisibility(View.GONE);
                        }else {
                            tvCharges.setText("Extra Charges Applicable : "+mEventDetails.eventCurrency+" "+sCRDBCharges);
                            tvCharges.setVisibility(View.VISIBLE);
                        }
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
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_item, cardTypeList);
                        spinnerCardType.setAdapter(dataAdapter);


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
                        String finalMPromoId = mPromoId;
                        btSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
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
                                if (TextUtils.isEmpty(ccEditText.getText().toString())) {
                                    Utils.showToast(getActivity(), getString(R.string.alert_card_number));
                                    return;
                                } else {
                                    strCardNo = ccEditText.getText().toString().replaceAll("\\s", "");
                                }
                                if (ccEditText.getText().length() < 19) {
                                    Utils.showToast(getActivity(), getString(R.string.alart_valid_card_number));
                                    return;
                                } else {
                                    strCardNo = ccEditText.getText().toString().replaceAll("\\s", "");
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

                                    if (!Utils.isInternetConnected(getActivity())) {
                                        Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                        return;
                                    }

                                    String mCustId = "";
                                    if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                        mCustId = MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                                    }


                                    Map<String, String> parameter = new HashMap<String, String>();
                                    String mUrl = "https://www.managemyticket.net/android/api/event_migs_server_v3.php"/*?seats=" + mSeatCount + "&cust_name=" + mUserFullName
                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&ukey=" + mUserKey + "&evt_date_time="
                                + mEventDetails.eventSelectedDate + "&evt_pid=" + mEventDetails.eventId + mCustId + mPromoId*/;

                                    Log.d("Log", "mPromoId " + finalMPromoId);
                                    parameter.put("careexp", lastDigitYear + strMonth);
                                    parameter.put("cardno", strCardNo);
                                    parameter.put("cardcvv", strCardCvv);
                                    parameter.put("cardtype", cardType);
                                    parameter.put("seats", mSeatCount);
                                    parameter.put("cust_name", mUserFullName);
                                    parameter.put("cust_email", mUserEmail);
                                    parameter.put("cust_mob", mUserMobileNumber);
                                    parameter.put("ukey", mUserKey);
                                    parameter.put("evt_date_time", mEventDetails.eventSelectedDate);
                                    parameter.put("evt_pid", mEventDetails.eventId);
                                    parameter.put("cust_id", mCustId);
                                    parameter.put("promo_id", finalMPromoId);

                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.BNDL_TITLE, mEventDetails.eventName);
                                    bundle.putString(Constants.BNDL_URL, mUrl);
                                    bundle.putSerializable("postdata", (Serializable) parameter);
                                    bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_DEBIT);

                                    dialog.dismiss();

                                    switchFragment(EventPaymentProcessFragment.newInstance(), EventPaymentProcessFragment.Tag_EventPaymentProcessFragment, bundle);
                                    Utils.hideKeyboard(getActivity());

                                } else {
                                    Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mEventDetails.eventCurrency + " 100"));
                                }
                            }
                        });
                        dialog.show();

                    } else {
                        Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                    }
                } else if (sApplicablePGWS.contains("5")) {
                    if (mEventDetails.eventCurrency.equals("USD")) {
                        if (!isCreditCardEnabled) {
                            Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                            return;
                        }

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_card_info);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        Spinner spinnerCardType = dialog.findViewById(R.id.spinnerCardType);
                        final EditText ccEditText = dialog.findViewById(R.id.editCardNo);
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
                            tvCharges.setText("Extra Charges Applicable : "+mEventDetails.eventCurrency+" "+sCRDBCharges);
                            tvCharges.setVisibility(View.VISIBLE);
                        }

                        ccEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                                    spinnerYear.requestFocus();
                                    spinnerYear.performClick();
                                }
                                return false;
                            }
                        });
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
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_item, cardTypeList);
                        spinnerCardType.setAdapter(dataAdapter);


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
                            public void onClick(View view) {
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
                                if (TextUtils.isEmpty(ccEditText.getText().toString())) {
                                    Utils.showToast(getActivity(), getString(R.string.alert_card_number));
                                    return;
                                } else {
                                    strCardNo = ccEditText.getText().toString().replaceAll("\\s", "");
                                }
                                if (ccEditText.getText().length() < 19) {
                                    Utils.showToast(getActivity(), getString(R.string.alart_valid_card_number));
                                    return;
                                } else {
                                    strCardNo = ccEditText.getText().toString().replaceAll("\\s", "");
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

                                    if (!Utils.isInternetConnected(getActivity())) {
                                        Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                        return;
                                    }

                                    String mCustId = "";
                                    if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                        mCustId = MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                                    }

                                    String mPromoId = "";
                                    if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                        mPromoId = "" + mCouponResponsePojo.promo_id;
                                    }

                                    Map<String, String> parameter = new HashMap<String, String>();
                                    String mUrl = "https://www.managemyticket.net/android/api/event_migs_server_v3.php"/*?seats=" + mSeatCount + "&cust_name=" + mUserFullName
                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&ukey=" + mUserKey + "&evt_date_time="
                                + mEventDetails.eventSelectedDate + "&evt_pid=" + mEventDetails.eventId + mCustId + mPromoId*/;

                                    Log.d("Log", "mPromoId " + mPromoId);
                                    parameter.put("careexp", lastDigitYear + strMonth);
                                    parameter.put("cardno", strCardNo);
                                    parameter.put("cardcvv", strCardCvv);
                                    parameter.put("cardtype", cardType);
                                    parameter.put("seats", mSeatCount);
                                    parameter.put("cust_name", mUserFullName);
                                    parameter.put("cust_email", mUserEmail);
                                    parameter.put("cust_mob", mUserMobileNumber);
                                    parameter.put("ukey", mUserKey);
                                    parameter.put("evt_date_time", mEventDetails.eventSelectedDate);
                                    parameter.put("evt_pid", mEventDetails.eventId);
                                    parameter.put("cust_id", mCustId);
                                    parameter.put("promo_id", mPromoId);

                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.BNDL_TITLE, mEventDetails.eventName);
                                    bundle.putString(Constants.BNDL_URL, mUrl);
                                    bundle.putSerializable("postdata", (Serializable) parameter);
                                    bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_DEBIT);

                                    dialog.dismiss();

                                    switchFragment(EventPaymentProcessFragment.newInstance(), EventPaymentProcessFragment.Tag_EventPaymentProcessFragment, bundle);
                                    Utils.hideKeyboard(getActivity());

                                } else {
                                    Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mEventDetails.eventCurrency + " 100"));
                                }
                            }
                        });
                        dialog.show();

                    } else {
                        Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                    }
                } else {
                    if (!isCreditCardEnabled) {
                        Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                        return;
                    }

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_card_info);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    Spinner spinnerCardType = dialog.findViewById(R.id.spinnerCardType);
                    final EditText ccEditText = dialog.findViewById(R.id.editCardNo);
                    EditText editTextCvv = dialog.findViewById(R.id.editCvv);
                    TextView btSubmit = dialog.findViewById(R.id.tvSubmit);
                    TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                    TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                    Spinner spinnerMonth = dialog.findViewById(R.id.spinnerMonth);
                    Spinner spinnerYear = dialog.findViewById(R.id.spinnerYear);
                    if (sCRDBCharges.equals("")){
                        tvCharges.setVisibility(View.GONE);
                    }else if (sCRDBCharges.equals("0.00")){
                        tvCharges.setVisibility(View.GONE);
                    }else {
                        tvCharges.setText("Extra Charges Applicable : "+mEventDetails.eventCurrency+" "+sCRDBCharges);
                        tvCharges.setVisibility(View.VISIBLE);
                    }
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
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_item, cardTypeList);
                    spinnerCardType.setAdapter(dataAdapter);


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
                        public void onClick(View view) {
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
                            if (TextUtils.isEmpty(ccEditText.getText().toString())) {
                                Utils.showToast(getActivity(), getString(R.string.alert_card_number));
                                return;
                            } else {
                                strCardNo = ccEditText.getText().toString().replaceAll("\\s", "");
                            }
                            if (ccEditText.getText().length() < 19) {
                                Utils.showToast(getActivity(), getString(R.string.alart_valid_card_number));
                                return;
                            } else {
                                strCardNo = ccEditText.getText().toString().replaceAll("\\s", "");
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

                                if (!Utils.isInternetConnected(getActivity())) {
                                    Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                    return;
                                }

                                String mCustId = "";
                                if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                    mCustId = MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                                }

                                String mPromoId = "";
                                if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                    mPromoId = "" + mCouponResponsePojo.promo_id;
                                }

                                Map<String, String> parameter = new HashMap<String, String>();
                                String mUrl = "https://www.managemyticket.net/android/api/event_migs_server_v3.php"/*?seats=" + mSeatCount + "&cust_name=" + mUserFullName
                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&ukey=" + mUserKey + "&evt_date_time="
                                + mEventDetails.eventSelectedDate + "&evt_pid=" + mEventDetails.eventId + mCustId + mPromoId*/;

                                Log.d("Log", "mPromoId " + mPromoId);
                                parameter.put("careexp", lastDigitYear + strMonth);
                                parameter.put("cardno", strCardNo);
                                parameter.put("cardcvv", strCardCvv);
                                parameter.put("cardtype", cardType);
                                parameter.put("seats", mSeatCount);
                                parameter.put("cust_name", mUserFullName);
                                parameter.put("cust_email", mUserEmail);
                                parameter.put("cust_mob", mUserMobileNumber);
                                parameter.put("ukey", mUserKey);
                                parameter.put("evt_date_time", mEventDetails.eventSelectedDate);
                                parameter.put("evt_pid", mEventDetails.eventId);
                                parameter.put("cust_id", mCustId);
                                parameter.put("promo_id", mPromoId);


                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.BNDL_TITLE, mEventDetails.eventName);
                                bundle.putString(Constants.BNDL_URL, mUrl);
                                bundle.putSerializable("postdata", (Serializable) parameter);
                                bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_DEBIT);

                                dialog.dismiss();

                                switchFragment(EventPaymentProcessFragment.newInstance(), EventPaymentProcessFragment.Tag_EventPaymentProcessFragment, bundle);
                                Utils.hideKeyboard(getActivity());

                            } else {
                                Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mEventDetails.eventCurrency + " 100"));
                            }
                        }
                    });
                    dialog.show();

                }
            } else {
                Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
            }
        } else if (view == rltTigoPesa) {
            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("2")) {
                if (!isTigoEnabled) {
                    Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                    return;
                }

                if (mTotalAmount >= 1000) {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_payment_mpesa);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                    final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                    TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
//                final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                    Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                    TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                    final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);
                    if (sTigoCharges.equals("")){
                        tvCharges.setVisibility(View.GONE);
                    }else if (sTigoCharges.equals("0.00")){
                        tvCharges.setVisibility(View.GONE);
                    }else {
                        tvCharges.setText("Extra Charges Applicable : "+mEventDetails.eventCurrency+" "+sTigoCharges);
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

                    if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                        spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
                    } else {
                        spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
                    }
                    spinCountryCode.setSelection(214);
                    spinCountryCode.setEnabled(false);
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

                            String mUrl = "https://managemyticket.net/android/api/event_tigo_pesa_v2.php?seats=" + mSeatCount + "&cust_name=" + mUserFullName
                                    + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&ukey=" + mUserKey + "&tigo=" + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code
                                    + "" + etMobileNumber.getText().toString() + "&tigo_term=1&evt_date_time=" + mEventDetails.eventSelectedDate + "&evt_pid=" + mEventDetails.eventId + mCustId + mPromoId;

                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_TITLE, mEventDetails.eventName);
                            bundle.putString(Constants.BNDL_URL, mUrl);
                            bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_TIGO);

                            switchFragment(EventPaymentProcessFragment.newInstance(), EventPaymentProcessFragment.Tag_EventPaymentProcessFragment, bundle);

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
                    Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mEventDetails.eventCurrency + " 1000"));
                }
            } else {
                //   Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();

                Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));
            }

        } else if (view == rltMPesa) {
            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("3")) {
                if (!isMpesaEnabled) {
                    Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                    return;
                }

                if (mTotalAmount >= 100) {

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

                    tvTitle.setText("" + getString(R.string.mpesa));
                    if (sMPesaCharges.equals("")){
                        tvCharges.setVisibility(View.GONE);
                    }else if (sMPesaCharges.equals("0.00")){
                        tvCharges.setVisibility(View.GONE);
                    }else {
                        tvCharges.setText("Extra Charges Applicable : "+mEventDetails.eventCurrency+" "+sMPesaCharges);
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

                    if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                        spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
                    } else {
                        spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
                    }
                    spinCountryCode.setSelection(214);
                    spinCountryCode.setEnabled(false);
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

                            String mUrl = "https://managemyticket.net/android/api/event_mpesa_v2.php?seats=" + mSeatCount + "&cust_name=" + mUserFullName
                                    + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&ukey=" + mUserKey + "&mpesa=" + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code
                                    + "" + etMobileNumber.getText().toString() + "&mpesa_term=1&evt_date_time=" + mEventDetails.eventSelectedDate + "&evt_pid=" + mEventDetails.eventId + mCustId + mPromoId;


                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_TITLE, mEventDetails.eventName);
                            bundle.putString(Constants.BNDL_URL, mUrl);
                            bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_MPESA);

                            switchFragment(EventPaymentProcessFragment.newInstance(), EventPaymentProcessFragment.Tag_EventPaymentProcessFragment, bundle);

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
                    Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mEventDetails.eventCurrency + " 100"));
                }
            } else {
                //   Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();

                Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));
            }

        } else if (view == rltAirtelMoney) {
            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("4")) {

                if (!isAirtelMoneyEnabled) {
                    Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                    return;
                }

                if (mTotalAmount >= 100) {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_payment_mpesa);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                    final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                    TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                    TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
//                final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                    Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                    final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);

                    tvTitle.setText("" + getString(R.string.airtel_money));
                    if (sAirtelCharges.equals("")){
                        tvCharges.setVisibility(View.GONE);
                    }else if (sAirtelCharges.equals("0.00")){
                        tvCharges.setVisibility(View.GONE);
                    }else {
                        tvCharges.setText("Extra Charges Applicable : "+mEventDetails.eventCurrency+" "+sAirtelCharges);
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

                    if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                        spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
                    } else {
                        spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
                    }

                    spinCountryCode.setSelection(214);
                    spinCountryCode.setEnabled(false);
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
//                        API: https://managemyticket.net/android/api/event_airtel.php?seats=1&ukey=91801201908210919&cust_name=Nikhil&cust_email=otapp.test007@gmail.com&cust_mob=+255689256021&evt_pid=1&evt_date_time=2019-09-21 23:55&cust_id&promo_id&airtel=+255689256021&airtel_term=1

                            String mUrl = "https://managemyticket.net/android/api/event_airtel.php?seats=" + mSeatCount + "&cust_name=" + mUserFullName
                                    + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&ukey=" + mUserKey + "&airtel=" + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code
                                    + "" + etMobileNumber.getText().toString() + "&airtel_term=1&evt_date_time=" + mEventDetails.eventSelectedDate + "&evt_pid=" + mEventDetails.eventId + mCustId + mPromoId;

                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_TITLE, mEventDetails.eventName);
                            bundle.putString(Constants.BNDL_URL, mUrl);
                            bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_AIRTEL);

                            switchFragment(EventPaymentProcessFragment.newInstance(), EventPaymentProcessFragment.Tag_EventPaymentProcessFragment, bundle);

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
                    Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mEventDetails.eventCurrency + " 100"));
                }
            } else {
                //Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();

                Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));
            }
        }
    }
}
