package com.otapp.net.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.CouponCodeActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.CountryCodeListAdapter;
import com.otapp.net.adapter.CountryCodeSpinAdapter;
import com.otapp.net.adapter.EventPreviewAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.CouponResponsePojo;
import com.otapp.net.model.EventDetailsPojo;
import com.otapp.net.model.EventPaymentSummaryPojo;
import com.otapp.net.model.EventSuccessPojo;
import com.otapp.net.model.EventZeroResponse;
import com.otapp.net.model.SeatProcessedPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventOrderPreviewFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_EventOrderPreviewFragment = "Tag_" + "EventOrderPreviewFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBackArrow)
    TextView tvBackArrow;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPlace)
    TextView tvPlace;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvQuantity)
    TextView tvQuantity;
    //    @BindView(R.id.tvTicketType)
//    TextView tvTicketType;
//    @BindView(R.id.tvTotal)
//    TextView tvTotal;
    @BindView(R.id.tvProceed)
    TextView tvProceed;
    //    @BindView(R.id.tvCountryCode)
//    TextView tvCountryCode;
    @BindView(R.id.spinCountryCode)
    TextView spinCountryCode;
    @BindView(R.id.etEmailID)
    EditText etEmailID;
    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.lnrApplyOffer)
    LinearLayout lnrApplyOffer;
    @BindView(R.id.lvSeat)
    ListView lvSeat;
    @BindView(R.id.divider)
    View view;

    private int mMobNumberMaxLength = 9;

    private String mUserKey = "", mSeatCount = "", prevString = "", mEventID = "", strTicketInfo = "", strEventId = "";
    private float mTotalPrice = 0, promoFareAmt = 0;
    int flag=0,mobFlag=0;

    CouponResponsePojo mCouponResponsePojo;
    EventPaymentSummaryPojo mPaymentSummaryPojo;
    List<EventPaymentSummaryPojo.Summary> mPaymentSummaryList;

    List<CountryCodePojo.CountryCode> tempCountryCodeList;
    int countryCodeFlag=0;
    CountryCodeListAdapter mCountryCodeSpinAdapter;
    private EventPreviewAdapter mEventPreviewAdapter;

    private EventDetailsPojo.EventDetails mEventDetails;

    public static EventOrderPreviewFragment newInstance() {
        EventOrderPreviewFragment fragment = new EventOrderPreviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_event_order_preview, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();
        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
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
        String mob=MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "");
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
        tempCountryCodeList= new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String mEvent = bundle.getString(Constants.BNDL_EVENT_DETAILS);
            mUserKey = bundle.getString(Constants.BNDL_USER_KEY);
            mSeatCount = bundle.getString(Constants.BNDL_EVENT_SEAT_COUNT);

            if (!TextUtils.isEmpty(mEvent)) {
                mEventDetails = new Gson().fromJson(mEvent, EventDetailsPojo.EventDetails.class);
                if (mEventDetails != null) {
                    tvName.setText("" + mEventDetails.eventName);
                    tvTitle.setText("" + mEventDetails.eventName);
                    tvPlace.setText("" + mEventDetails.eventAddr + ", " + mEventDetails.eventCity);
                    tvDate.setText("" + mEventDetails.eventSelectedDate);

                    mEventID = mEventDetails.eventId;

                    int ticketCount = 0;
//                    float total = 0;
                    StringBuilder strBuilderTicket = new StringBuilder();

                    for (int i = 0; i < mEventDetails.ticketType.size(); i++) {
                        EventDetailsPojo.TicketType mTicketType = mEventDetails.ticketType.get(i);
                        if (mTicketType != null && mTicketType.ticketCount > 0 && !TextUtils.isEmpty(mTicketType.ticketAmount)) {
//                            total = total + (mTicketType.ticketCount * Float.parseFloat(mTicketType.ticketAmount));
                            ticketCount = ticketCount + mTicketType.ticketCount;
                            if (strBuilderTicket.length() > 0) {
                                strBuilderTicket.append("," + mTicketType.ticketType);
                            } else {
                                strBuilderTicket.append(mTicketType.ticketType);
                            }

                        }
                    }

//                    if (strBuilderTicket != null && strBuilderTicket.length() > 0) {
//                        tvTicketType.setText("" + strBuilderTicket.toString());
//                    }
                    tvQuantity.setText("" + ticketCount);
//                    tvTotal.setText(mEventDetails.eventCurrency + " " + Utils.setPrice(total));

                }

            }
        }

        if (Otapp.mCountryCodePojoList == null || Otapp.mCountryCodePojoList.size() == 0) {
            getCountryCodeList();
        } else {
         /*   setSpinCountryCode();*/
            if(MyPref.PREF_USER_COUNTRY_CODE.length()>1){
                spinCountryCode.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""));
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
            }
        }

        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
            //    lnrApplyOffer.setVisibility(View.GONE);
        } else {
            lnrApplyOffer.setVisibility(View.VISIBLE);
        }

        tvBack.setOnClickListener(this);
        tvBackArrow.setOnClickListener(this);
        tvProceed.setOnClickListener(this);
        lnrApplyOffer.setOnClickListener(this);

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
      //          etFirstName.setSelection(etFirstName.getText().length());
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
     //           etMobileNumber.setSelection(etMobileNumber.getText().length());
                if(charSequence.toString().trim().equals("")){
//                    Toast.makeText(getContext(), "chage", Toast.LENGTH_SHORT).show();
                    spinCountryCode.setVisibility(View.VISIBLE);
                    if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("")||MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")) {
                       /* spinCountryCode.setSelection(214);*/
                        spinCountryCode.setText("+255");
                    }
                    etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
                    view.setVisibility(View.VISIBLE);
                    flag=0;
                }
                if(charSequence.toString().trim().length()<13){
                    mobFlag=1;
                }else {
                    mobFlag=0;
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
                                Toast.makeText(getActivity(), "No Country Code Found", Toast.LENGTH_SHORT).show();
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
        mEventPreviewAdapter = new EventPreviewAdapter(getActivity(), mEventDetails.eventCurrency);
        lvSeat.setAdapter(mEventPreviewAdapter);

        getPaymentSummary();

        String mob=MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "");
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



    }

   /* private void setSpinCountryCode() {

        CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
        spinCountryCode.setAdapter(mCountryCodeSpinAdapter);
 if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
            spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
        } else {
            spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
        }

        if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
            spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
        } else {
          for(int i=0;i<Otapp.mCountryCodePojoList.size();i++){
              Log.d("Log",MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""));
              Log.d("Log",""+Otapp.mCountryCodePojoList.get(i).code);
  if(Otapp.mCountryCodePojoList.get(i).code.equals("+255"))
              {
                  Log.d("Log","spinervalue "+i);
              }

              if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals(Otapp.mCountryCodePojoList.get(i).code)){
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

    }*/

    private void getPaymentSummary() {
        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("ev_pid", "" + mEventID);
        jsonParams.put("ukey", "" + mUserKey);

        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
            jsonParams.put("promo_id", "" + mCouponResponsePojo.promo_id);
            Log.d("Log","promo_id "+ mCouponResponsePojo.promo_id);
        }
        Log.d("Log","evetnId "+mEventID);
        Log.d("Log","ukey "+mUserKey);


        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<EventPaymentSummaryPojo> mCall = mApiInterface.getEventPaymentSummary(jsonParams);
        mCall.enqueue(new Callback<EventPaymentSummaryPojo>() {
            @Override
            public void onResponse(Call<EventPaymentSummaryPojo> call, Response<EventPaymentSummaryPojo> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    EventPaymentSummaryPojo mEventPaymentSummaryPojo = response.body();
                    if (mEventPaymentSummaryPojo != null) {
                        if (mEventPaymentSummaryPojo.status.equalsIgnoreCase("200")) {
                            JSONObject jsonObjectResponse = null;
                            try {
                                jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                                Log.d("Log", "Response getSummary : " + jsonObjectResponse);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mPaymentSummaryPojo = mEventPaymentSummaryPojo;
                            mPaymentSummaryList = mEventPaymentSummaryPojo.data.fare;
                            promoFareAmt = mEventPaymentSummaryPojo.data.promoFare.promoFare;

                            if (mPaymentSummaryList != null && mPaymentSummaryList.size() > 0) {

                                mTotalPrice = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).price;
                                String title = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).lable;
//                                if (!TextUtils.isEmpty(title)) {
//                                    tvGrandTotalTitle.setText("" + title);
//                                }

//                                tvTotal.setText(mEventDetails.eventCurrency + " " + Utils.setPrice(mTotalPrice));


                                mEventPreviewAdapter.addAll(mPaymentSummaryList);
                            }


                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EventPaymentSummaryPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        String sApplicablePGWS = MyPref.getPref(getContext(), "ENABLED_PAYMENT_METHODS", "0");
        Log.d("Log", "Applicable PGW " + sApplicablePGWS);
        if (view == tvBack) {
           /* deleteProcessedSeat();
            popBackStack();*/
            String sCouponcode = MyPref.getPref(getContext(), "PromoCode", "");
            if (sCouponcode != null) {
                if (sCouponcode.equals("")) {
                    deleteProcessedSeat();
                    popBackStack();
                } else {
                    removeCouponCode();
                }
            } else {
                removeCouponCode();
            }
        } else if (view == tvBackArrow) {
            deleteProcessedSeat();
            popBackStack();
        } else if (view == tvProceed) {

            if (isValidField()) {

                if (mPaymentSummaryPojo != null) {

                    if (mTotalPrice > 0) {

                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.BNDL_EVENT_DETAILS, new Gson().toJson(mEventDetails));
                        bundle.putString(Constants.BNDL_USER_KEY, mUserKey);
                        bundle.putString(Constants.BNDL_EVENT_SEAT_COUNT, mSeatCount);

                        bundle.putString(Constants.BNDL_EVENT_PAYMENT_SUMMARY, new Gson().toJson(mPaymentSummaryPojo));
                        if (mCouponResponsePojo != null) {
                            Log.d("Log", "Couponb Response : Not Null "+ mCouponResponsePojo.promo_id);
                            bundle.putString(Constants.BNDL_EVENT_COUPON, new Gson().toJson(mCouponResponsePojo));
                        } else {
                            Log.d("Log", "Couponb Response : Null");
                            bundle.putString(Constants.BNDL_EVENT_COUPON, "");
                        }

                        bundle.putString(Constants.BNDL_FULLNAME, etFirstName.getText().toString() + " " + etLastName.getText().toString());
                        bundle.putString(Constants.BNDL_EMAIL, etEmailID.getText().toString());

                        if(etMobileNumber.getText().length()>10) {
                            bundle.putString(Constants.BNDL_MOBILE_NUMBER, etMobileNumber.getText().toString());
                        }else {
                            bundle.putString(Constants.BNDL_MOBILE_NUMBER, spinCountryCode.getText().toString() + "" + etMobileNumber.getText().toString());
                        }

                        switchFragment(EventPaymentFragment.newInstance(), EventPaymentFragment.Tag_EventPaymentFragment, bundle);
                        //switchFragment(EventOrderReviewFragment.newInstance(), EventOrderReviewFragment.Tag_EventOrderReviewFragment, bundle);

                    } else {
                        callZeroPayment();
                    }
                } else {
                    LogUtils.e("", "mMoviePaymentSummaryPojo is null");
                }
            }

        } else if (view == lnrApplyOffer) {

            JSONObject jsonObject = new JSONObject();


            try {

                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < mEventDetails.ticketType.size(); i++) {
                    EventDetailsPojo.TicketType mTicketType = mEventDetails.ticketType.get(i);
                    if (mTicketType != null && mTicketType.ticketCount > 0 && !TextUtils.isEmpty(mTicketType.ticketAmount)) {

                        JSONObject jsonTktInfo = new JSONObject();
                        jsonTktInfo.put("tkt_id", "" + mTicketType.ticketId);
                        jsonTktInfo.put("tot_tkt_id_tkts_count", "" + mTicketType.ticketCount);
                        jsonTktInfo.put("single_fare", "" + mTicketType.ticketAmount);
                        jsonTktInfo.put("seats", "");

                        jsonArray.put(jsonTktInfo);

                    }
                }


                jsonObject.put("tkts_info", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            strTicketInfo = jsonObject.toString();
            strEventId = mEventDetails.eventId;
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BNDL_PROPERTY_ID, "" + mEventDetails.eventId);
            bundle.putString(Constants.BNDL_TICKET_INFO, "" + jsonObject.toString());
            bundle.putString(Constants.BNDL_TOTAL_FARE, "" + promoFareAmt); //
            bundle.putString(Constants.BNDL_USER_KEY, "" + mUserKey);
            bundle.putString(Constants.BNDL_CURRENCY, "" + mEventDetails.eventCurrency);

            IntentHandler.startActivityForResult(getActivity(), CouponCodeActivity.class, bundle, Constants.RC_EVENT_COUPON_CODE);

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
        jsonParams.put("seats", "" + mSeatCount);
        jsonParams.put("ukey", "" + mUserKey);
        jsonParams.put("cust_name", "" + etFirstName.getText().toString() + " " + etLastName.getText().toString());
        jsonParams.put("cust_email", "" + etEmailID.getText().toString());
        jsonParams.put("cust_mob", "" + spinCountryCode.getText().toString()+ "" + etMobileNumber.getText().toString());
        jsonParams.put("evt_pid", "" + mEventID);
        jsonParams.put("evt_date_time", "" + mEventDetails.eventSelectedDate);

        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
            jsonParams.put("cust_id", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
        }

        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
            jsonParams.put("promo_id", "" + mCouponResponsePojo.promo_id);
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<EventZeroResponse> mCall = mApiInterface.getEventZeroPayment(jsonParams);
        mCall.enqueue(new Callback<EventZeroResponse>() {
            @Override
            public void onResponse(Call<EventZeroResponse> call, Response<EventZeroResponse> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    EventZeroResponse mEventZeroResponse = response.body();
                    if (mEventZeroResponse != null) {
                        if (mEventZeroResponse.status.equalsIgnoreCase("200")) {
                            if (!TextUtils.isEmpty(mEventZeroResponse.tkt_no)) {
                                setPaymentSucessful(mEventZeroResponse.tkt_no);
                            }
                        } else {
                            Utils.showToast(getActivity(), mEventZeroResponse.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EventZeroResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void setPaymentSucessful(String mBookingID) {
        Utils.closeProgressDialog();
        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<EventSuccessPojo> mCall = mApiInterface.getEventPaymentSuccess(mBookingID, Otapp.mUniqueID);
        mCall.enqueue(new Callback<EventSuccessPojo>() {
            @Override
            public void onResponse(Call<EventSuccessPojo> call, Response<EventSuccessPojo> response) {

                Utils.closeProgressDialog();
                if (response.isSuccessful()) {

                    EventSuccessPojo mEventSuccessPojo = response.body();
                    if (mEventSuccessPojo != null) {
                        if (mEventSuccessPojo.status.equalsIgnoreCase("200")) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_MOVIE_RESPONSE, new Gson().toJson(mEventSuccessPojo.data));
                            switchFragment(EventOrderReviewFragment.newInstance(), EventOrderReviewFragment.Tag_EventOrderReviewFragment, bundle);
                        } else {
                            Utils.showToast(getActivity(), mEventSuccessPojo.message);
                            popBackStack(ServiceFragment.Tag_ServiceFragment);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<EventSuccessPojo> call, Throwable t) {
                LogUtils.e("", "onFailure::" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_EVENT_COUPON_CODE && resultCode == Activity.RESULT_OK) {
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
        } else
           /* if(flag==0){
                if (etMobileNumber.getText().toString().length() < mMobNumberMaxLength || etMobileNumber.getText().toString().length() > mMobNumberMaxLength) {
                    Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobNumberMaxLength));
                    return false;
                }
            }else if(mobFlag==1){
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


    public void deleteProcessedSeat() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!TextUtils.isEmpty(mUserKey)) {

            ApiInterface mApiInterface = RestClient.getClient(true);
            Call<SeatProcessedPojo> mCall = mApiInterface.deleteProcessedEventSeatsList(mUserKey, Otapp.mUniqueID);
            mCall.enqueue(new Callback<SeatProcessedPojo>() {
                @Override
                public void onResponse(Call<SeatProcessedPojo> call, Response<SeatProcessedPojo> response) {
                    if (response.isSuccessful()) {

                    }
                }

                @Override
                public void onFailure(Call<SeatProcessedPojo> call, Throwable t) {
                }
            });
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

                   /*         setSpinCountryCode();*/

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

        String mKey = Utils.getCouponCodeKey(mPromoCode + strEventId + strTicketInfo + promoFareAmt + mUserKey + mCurrency + mPlatform + "pr0mOCode");

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
        jsonParams.put("prop_id", "" + strEventId);
        jsonParams.put("tkt_info", "" + strTicketInfo);

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
