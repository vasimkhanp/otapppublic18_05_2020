package com.otapp.net.Events.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.loadingview.LoadingView;
import com.google.gson.Gson;
import com.otapp.net.Async.Interface.OtappApiServices;
import com.otapp.net.Async.Interface.RestClient;
import com.otapp.net.Events.Adapter.CountryCodeListAdapter;
import com.otapp.net.Events.Adapter.EventRecyclerPaymentSummaryAdapter;
import com.otapp.net.Events.Adapter.SeatAmountBaseAdapter;
import com.otapp.net.Events.Adapter.SeatAmountGetTypeBaseAdapter;
import com.otapp.net.Events.Core.EventPaymentSummaryResponse;
import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.Events.Core.GetTicketTypeResponse;
import com.otapp.net.Events.Core.PaymentSuceesResponse;
import com.otapp.net.Events.Core.PromoCodeResponse;
import com.otapp.net.PromoCode.Activity.PromoCodeActivity;
import com.otapp.net.PromoCode.ZeroPaymentPromoResponse;
import com.otapp.net.R;
import com.otapp.net.application.Otapp;
import com.otapp.net.helper.SHA;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventOrderPreview extends AppCompatActivity {

    @BindView(R.id.toolbarTitle)
    TextView pageTitle;
    @BindView(R.id.back)
    ImageView imgBack;
    @BindView(R.id.tvEventAddress)
    TextView tvEventAddress;
    @BindView(R.id.tvEventName)
    TextView tvEventName;
    @BindView(R.id.tvEventDate)
    TextView tvEventDate;
    @BindView(R.id.tvNoOfTickets)
    TextView tvNoOfTickets;
    @BindView(R.id.lvNoOfSeats)
    RecyclerView lvNoOfSeats;
    @BindView(R.id.loadingView)
    LoadingView loadingView;
    @BindView(R.id.recycleViewFare)
    RecyclerView recyclerViewFare;
    @BindView(R.id.tvFullname)
    TextView tvFullName;
    @BindView(R.id.tvEmailMobile)
    TextView tvEmailMobile;
    @BindView(R.id.tvToatlPrice)
    TextView tvTotalPrice;
    @BindView(R.id.offerLayout)
    LinearLayout linearLayoutOffers;
    @BindView(R.id.tvGrandTotal)
    TextView tvGrandTotal;
    @BindView(R.id.linearContinue)
    LinearLayout linearLayoutContinue;
    @BindView(R.id.cbTermsCondtions)
    CheckBox checkBox;
    @BindView(R.id.lnrPerson)
    LinearLayout linearLayoutPerson;
    @BindView(R.id.tvAgreeTermsWarning)
    TextView tvAgreeTemsWarning;

    String terms = "0";
    int mobFlag = 0,cancleFlag=0;
    int layoutPersonFlag = 0;

        /*    TextView tvTaxableAmount;
    @BindView(R.id.tvPaybleAmount)
            TextView tvPaybleAmount;
    @BindView(R.id.tvTotalExclTax)
            TextView tvTotalExcTax;
    @BindView(R.id.tvTotalVat)
            TextView tvTotalVat;
    @BindView(R.id.tvDiscount)
            TextView tvDiscount;
    @BindView(R.id.tvConvenienceFee)
            TextView tvConvenienceFee;*/

    String strDate = "", monthsplit = "", strTime = "";
    String timeArray[] = new String[]{};

    String strEvents = "", strEventDate = "", strEventId = "", strUkey = "", strUserFirstName = "", strLastName = "", strMailId = "", strCountryCode = "", strMobileNumber = "";
    EventsListResponse.Events eventsListResponse;

    private float mTotalPrice = 0;

    public Map<String, EventsListResponse.Events.EventTickets.Tickets> tempTicketMap;
    List<EventsListResponse.Events.EventTickets.Tickets> tempArrylist;
    List<GetTicketTypeResponse.GetTickets> tempGetTicketArrylist;

    List<EventPaymentSummaryResponse.Fare> fareList;

    EventRecyclerPaymentSummaryAdapter eventRecyclerPaymentSummary;
    SeatAmountBaseAdapter seatAmountBaseAdapter;
    SeatAmountGetTypeBaseAdapter seatAmountGetTypeBaseAdapter;
    EventPaymentSummaryResponse.Fare fare;

    PromoCodeResponse mCouponResponsePojo;
    EventPaymentSummaryResponse eventPaymentSummaryResponse;
    CountryCodeListAdapter mCountryCodeSpinAdapter;
    List<CountryCodePojo.CountryCode> tempCountryCodeList;

    String currecy = "", promoId = "";
    int enableCountryCode = 0;
    int noOfTickets = 0, flagMovies = 0;
    int countryCodeFlag = 0;
    String promoCode = "", strTotalFare = "";
    int AuthKey;
    String key = "", apiKey = "", agentId = "", bookFrom = "";
    String ticketTypeFlag = "", strPromoFlag = "";
    String mob;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        promoCode = MyPref.getPref(getApplicationContext(), MyPref.PREF_PROMO_CODE, "");
        if (promoCode.equals("")) {
            finish();
        } else {
            strPromoFlag = MyPref.getPref(getApplicationContext(), MyPref.PREF_PROMO_FLAG, "");
            if (strPromoFlag.equals("1")) {
                removePromoCode();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_order_preview);
        ButterKnife.bind(this);
        pageTitle.setText(R.string.Order_Priview);
        pageTitle.setGravity(Gravity.CENTER);
        tempTicketMap = new HashMap<>();
        tempArrylist = new ArrayList<>();
        fareList = new ArrayList<>();


        strEvents = getIntent().getExtras().getString("events");
        Bundle bundle = getIntent().getExtras();

        ticketTypeFlag = bundle.getString("flag");
        if (ticketTypeFlag.equals("1")) {
            tempGetTicketArrylist = bundle.getParcelableArrayList("temp");
        } else {
            tempArrylist = bundle.getParcelableArrayList("temp");
        }
        eventsListResponse = new Gson().fromJson(strEvents, EventsListResponse.Events.class);
        strEventDate = MyPref.getPref(getApplicationContext(), MyPref.PREF_EVENT_DATE, "");

        strEventId = MyPref.getPref(getApplicationContext(), MyPref.PREF_EVENT_ID, "");
        strUkey = MyPref.getPref(getApplicationContext(), MyPref.PREF_UKEY, "");
        strUserFirstName = MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_FIRST_NAME, "");
        strLastName = MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_LAST_NAME, "");
        strMailId = MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_EMAIL, "");
        strMobileNumber = MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_MOB, "");

        /*if(strMobileNumber.toString().length()>10){
            tvEmailMobile.setText(strMailId.toString() + "   |    " + strMobileNumber.toString());
        }else {
            tvEmailMobile.setText(strMailId.toString() + "   |    "+strMobileNumber.toString());
        }*/
        if (!strMobileNumber.equals("")) {
            if (strMobileNumber.startsWith("+")) {
                tvEmailMobile.setText(strMailId.toString() + "   |    " + strMobileNumber.toString());
            } else {
                tvEmailMobile.setText(strMailId.toString() + "   |    +" + strMobileNumber.toString());
            }
        }
        recyclerViewFare.setHasFixedSize(true);
        recyclerViewFare.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initialize();


    }

    @OnClick(R.id.lnrPerson)
    public void onPersonInfo() {
        layoutPersonFlag = 1;
        showPersonalDailog();
    }

    @OnClick(R.id.offerLayout)
    public void promoCodeClick() {
        Intent intentPromoCode = new Intent(getApplicationContext(), PromoCodeActivity.class);
        intentPromoCode.putExtra("noOfTickets", noOfTickets);
        intentPromoCode.putParcelableArrayListExtra("ticketInfo", (ArrayList<? extends Parcelable>) tempArrylist);
        intentPromoCode.putExtra("totalFare", eventPaymentSummaryResponse.promo_fare);
        startActivity(intentPromoCode);
    }

    @OnClick(R.id.linearContinue)
    public void callPaymentPage() {

        if (Double.parseDouble(fareList.get(fareList.size() - 1).amount.replaceAll(",", "")) == 0) {
            callZeroPaymentPromo();
        } else {
            if (terms.equals("1")) {

                if (tvFullName.getText().toString().trim().equals("") || tvFullName.getText().toString().trim().equals(" ") || tvEmailMobile.getText().toString().trim().equals("")) {
                    showPersonalDailog();
                } else {
                    Intent intent = new Intent(getApplicationContext(), EventPaymentActivity.class);
                    intent.putExtra("events", new Gson().toJson(eventsListResponse));
                    intent.putParcelableArrayListExtra("fare", (ArrayList<? extends Parcelable>) fareList);
                    intent.putExtra("paymentSumarryResponse", new Gson().toJson(eventPaymentSummaryResponse));
                    intent.putExtra("noOfTickets", noOfTickets);
                    intent.putExtra("currency", currecy);
                    intent.putExtra("email", strMailId);
                    intent.putExtra("mobile", strMobileNumber);
                    startActivity(intent);
                }
            } else {
                tvAgreeTemsWarning.setVisibility(View.VISIBLE);
            }
        }
    }

    public void initialize() {

        try {
            byte[] data = Base64.decode(eventsListResponse.event_address, Base64.DEFAULT);
            tvEventAddress.setText(new String(data, "UTF-8") + "," + eventsListResponse.event_city);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvEventName.setText(eventsListResponse.event_name);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(strEventDate);


            strDate = (String) android.text.format.DateFormat.format("dd", date); // 20
            monthsplit = (String) android.text.format.DateFormat.format("MMM", date); // Jun

        } catch (Exception e) {
            e.printStackTrace();
        }
        timeArray = strEventDate.split(" ");
        strTime = timeArray[1];


        tvEventDate.setText(strDate + " " + monthsplit + " | " + strTime);

        if (ticketTypeFlag.equals("1")) {
            for (int i = 0; i < tempGetTicketArrylist.size(); i++) {
                noOfTickets = noOfTickets + tempGetTicketArrylist.get(i).count;
            }
        } else {
            for (int i = 0; i < tempArrylist.size(); i++) {
                noOfTickets = noOfTickets + tempArrylist.get(i).count;
            }
        }
        tvNoOfTickets.setText("" + noOfTickets);

        lvNoOfSeats.setHasFixedSize(true);
        lvNoOfSeats.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if (ticketTypeFlag.equals("1")) {
            seatAmountGetTypeBaseAdapter = new SeatAmountGetTypeBaseAdapter(getApplicationContext(), tempGetTicketArrylist);
            lvNoOfSeats.setAdapter(seatAmountGetTypeBaseAdapter);
            seatAmountGetTypeBaseAdapter.notifyDataSetChanged();
        } else {
            seatAmountBaseAdapter = new SeatAmountBaseAdapter(getApplicationContext(), tempArrylist);
            lvNoOfSeats.setAdapter(seatAmountBaseAdapter);
            seatAmountBaseAdapter.notifyDataSetChanged();
        }


        tvFullName.setText(strUserFirstName + " " + strLastName);

       /* if(strMobileNumber.length()>10){
            tvEmailMobile.setText(strMailId + "   |    " + strMobileNumber);
        }else {
            tvEmailMobile.setText(strMailId + "   |    "+MyPref.getPref(getApplicationContext(),MyPref.PREF_USER_COUNTRY_CODE,"") +strMobileNumber);
        }*/

      /*   = new SeatAmountBaseAdapter(getApplicationContext(), tempArrylist);
        lvNoOfSeats.setAdapter(customAdapter);*/

        MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_ID, "");
        MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_ID, "");
        getPaymentSummary();
        showPersonalDailog();
        if (checkBox.isChecked()) {
            terms = "1";
        } else {
            terms = "0";
        }
    }

    @OnClick(R.id.cbTermsCondtions)
    public void onCheckboxSelected() {
        if (checkBox.isChecked()) {
            terms = "1";
            tvAgreeTemsWarning.setVisibility(View.GONE);
        } else {
            terms = "0";
        }
    }

    public void getPaymentSummary() {

        boolean isConnected = AppConstants.isConnected(getApplicationContext());
        if (isConnected) {

            //loadingView.start();
            if (!Utils.isProgressDialogShowing()) {
                Utils.showProgressDialog(this);
            }


            Random r = new Random();
            int random = r.nextInt(4 - 1 + 1) + 1;
            if (random > 4 && random < 1) {
                random = 3;
            }
            AuthKey = Integer.parseInt(String.valueOf(random));
            agentId = AppConstants.agentId;
            bookFrom = AppConstants.bookFrom;

            apiKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4, AuthKey + "f@rW5")));
            key = calculateHash(AuthKey, calculateHash(1, calculateHash(4, agentId + bookFrom + strEventId + strUkey + promoId + "F@rW$")));

            Log.d("Log", "api kay " + apiKey);
            Log.d("Log", "key =" + key);
           /* if(!promoId.equals("")){
                strUkey=MyPref.getPref(getApplicationContext(),MyPref.PREF_PROMO_UKEY,"");
            }*/

            Log.d("Log", strUkey);

            OtappApiServices otappApiServices = RestClient.getClient(true);
            Call<EventPaymentSummaryResponse> mCallEventPaymentSummary = otappApiServices.getPaymentSummary(apiKey, agentId, bookFrom, String.valueOf(AuthKey), strEventId, strUkey, promoId, key);
            mCallEventPaymentSummary.enqueue(new Callback<EventPaymentSummaryResponse>() {
                @Override
                public void onResponse(Call<EventPaymentSummaryResponse> call, Response<EventPaymentSummaryResponse> response) {
                    // loadingView.stop();
                    Utils.closeProgressDialog();
                    if (response.body().status == 200) {

                        JSONObject jsonObjectResponse = null;
                        try {
                            jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                            Log.d("Log", "Response : " + jsonObjectResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        eventPaymentSummaryResponse = response.body();
                        fareList = response.body().fareList;
                        currecy = MyPref.getPref(getApplicationContext(), MyPref.PREF_CURRENCY, "");
                        recyclerViewFare.setHasFixedSize(true);

                        eventRecyclerPaymentSummary = new EventRecyclerPaymentSummaryAdapter(getApplicationContext(), fareList, currecy);
                        recyclerViewFare.setAdapter(eventRecyclerPaymentSummary);
                        eventRecyclerPaymentSummary.notifyDataSetChanged();
                        tvTotalPrice.setText(fareList.get(fareList.size() - 1).amount);
                        tvGrandTotal.setText(currecy + " " + fareList.get(fareList.size() - 1).amount);
                    }
                }

                @Override
                public void onFailure(Call<EventPaymentSummaryResponse> call, Throwable t) {
                    //  loadingView.stop();
                    Utils.closeProgressDialog();
                    Toast.makeText(EventOrderPreview.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private String calculateHash(int authKey, String sHash) {
        switch (authKey) {
            case 1:
                sHash = SHA.MD5(sHash);
                break;
            case 2:
                try {
                    sHash = SHA.SHA1(sHash);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    sHash = SHA.SHA256(sHash);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    sHash = SHA.SHA512(sHash);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
        return sHash;
    }

    public void showPersonalDailog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_user_info);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final EditText etEmailID = (EditText) dialog.findViewById(R.id.etEmailID);
        final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);
        final EditText etFirstName = (EditText) dialog.findViewById(R.id.etFirstName);
        final EditText etLastName = (EditText) dialog.findViewById(R.id.etLastName);
        final View view = dialog.findViewById(R.id.divider);
        final TextView spinCountryCode = dialog.findViewById(R.id.spinCountryCode);
//        final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
        final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        final TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);

        etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
        /*if(MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_MOB, "").equals("")){
            spinCountryCode.setText("+255");
        }*/

        if (mobFlag == 0) {
            mob = MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_MOB, "");
        } else {
            mob = strMobileNumber;
        }
        Log.d("Log", "mob = " + mob);
        if (mob.length() >= 12) {

            if (mob.contains("+")) {
                etMobileNumber.setText(strMobileNumber);
            } else {
                if (strMobileNumber.contains("+")) {
                    etMobileNumber.setText(strMobileNumber);
                } else {
                    etMobileNumber.setText("+" + strMobileNumber);
                }
            }
            etMobileNumber.setSelection(etLastName.getText().length());
            spinCountryCode.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            mobFlag = 1;
        } else {
            if (strMobileNumber.length() > 10) {
                etMobileNumber.setText(strMobileNumber);
                spinCountryCode.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                mobFlag = 1;
            } else {
                etMobileNumber.setText(strMobileNumber);
                etMobileNumber.setSelection(etLastName.getText().length());
                spinCountryCode.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                mobFlag = 0;
            }
            /*  mobFlag=1;*/
        }

        etFirstName.setText(strUserFirstName);
        etLastName.setText(strLastName);
        etEmailID.setText(strMailId);
        etFirstName.setSelection(etFirstName.getText().length());
        etLastName.setSelection(etLastName.getText().length());
        etEmailID.setSelection(etEmailID.getText().length());
        etMobileNumber.setSelection(etMobileNumber.getText().length());
        spinCountryCode.setText("+255");

        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().trim().equals("")) {

                    spinCountryCode.setVisibility(View.VISIBLE);
                    enableCountryCode = 1;
                    strCountryCode = spinCountryCode.getText().toString();
                    mobFlag = 0;
                 /*   CountryCodePojo.CountryCode countryCode = (CountryCodePojo.CountryCode) spinCountryCode.getSelectedItem();
                    strCountryCode=countryCode.code;
                    Log.d("Log","country code 1 "+strCountryCode);*/
                    if (MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("") || MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")) {
                        // spinCountryCode.setSelection(214);
                        spinCountryCode.setText("+255");
                    }
                    etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
                    view.setVisibility(View.VISIBLE);
                } else {
                    //  etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13-spinCountryCode.getText().length())});
                    if (mobFlag == 1) {
                        etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                    } else {
                        etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
                    }
                }
                if (layoutPersonFlag == 1 && enableCountryCode == 0) {
                    etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancleFlag==0) {
                    tvFullName.setText(MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_FIRST_NAME, "") + " " + MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_LAST_NAME, ""));
                    if (etMobileNumber.getText().toString().length() > 10) {
                        tvEmailMobile.setText( MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_EMAIL, "") + "   |    " + strMobileNumber);
                    } else {
                        tvEmailMobile.setText( MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_EMAIL, "") + "   |    " + spinCountryCode.getText().toString() + strMobileNumber);
                    }
                }else {
                    tvFullName.setText(strUserFirstName+ " " + strLastName);
                    tvEmailMobile.setText( etEmailID.getText().toString() + "   |    " + strMobileNumber);
                   /* if (etMobileNumber.getText().toString().length()> 10) {
                        tvEmailMobile.setText( etEmailID.getText().toString() + "   |    " + strMobileNumber);
                    } else {
                        tvEmailMobile.setText( etEmailID.getText().toString() + "   |    " + spinCountryCode.getText().toString() + strMobileNumber);
                    }*/
                }
                dialog.dismiss();
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancleFlag=1;
                if (TextUtils.isEmpty(etFirstName.getText().toString().trim())) {
                    Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_firstname));
                    return;
                } else if (TextUtils.isEmpty(etLastName.getText().toString().trim())) {
                    Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_lastname));
                    return;
                } else if (TextUtils.isEmpty(etEmailID.getText().toString())) {
                    Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_email));
                    return;
                } else if (!Utils.isValidEmail(etEmailID.getText().toString())) {
                    Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_valid_email));
                    return;
                } else if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                    Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_phone));
                    return;
                } else if (mobFlag == 1) {
                    if (etMobileNumber.getText().toString().trim().length() != 13) {
                        Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_valid_phone_number));
                        return;
                    }
                } else {
                    if (layoutPersonFlag == 1) {
                        if (mobFlag == 0) {
                            if (etMobileNumber.getText().toString().trim().length() + spinCountryCode.getText().toString().trim().length() != 13) {
                                Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_valid_phone_number));
                                return;
                            }
                        } else {
                            if (etMobileNumber.getText().toString().trim().length() != 13) {
                                Log.d("Log", "length  " + etMobileNumber.getText().toString().trim().length() + spinCountryCode.getText().toString().trim().length());
                                Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_valid_phone_number));
                                return;
                            }
                        }
                           /* if (etMobileNumber.getText().toString().trim().length() != 13) {
                                Log.d("Log", "length  " + etMobileNumber.getText().toString().trim().length() + spinCountryCode.getText().toString().trim().length());
                                Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_valid_phone_number));
                                return;
                            }*/
                    } else {
                        if (etMobileNumber.getText().toString().trim().length() + spinCountryCode.getText().toString().trim().length() != 13) {
                            Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_valid_phone_number));
                            return;
                        }
                    }
                }

                    /*else
                    if(MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_COUNTRY_CODE, "").length()>1&&layoutPersonFlag==0){
                        Log.d("Log","length  "+etMobileNumber.getText().toString().trim().length()+spinCountryCode.getText().toString().trim().length());
                        if(etMobileNumber.getText().toString().trim().length()+spinCountryCode.getText().toString().trim().length()!=13) {
                            Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_valid_phone_number));
                            return ;
                        }
                    }else {
                        if(enableCountryCode==1){
                            if(etMobileNumber.getText().toString().length()+spinCountryCode.getText().toString().length()!=13) {
                                Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_valid_phone_number));
                                return ;
                            }
                        }else {
                            if (etMobileNumber.getText().toString().length() != 13) {
                                Utils.showToast(getApplicationContext(), "" + getString(R.string.alert_enter_valid_phone_number));
                                return;
                            }
                        }

                    }*/
                strUserFirstName = etFirstName.getText().toString();
                strLastName = etLastName.getText().toString();
                strMailId = etEmailID.getText().toString();
                strMobileNumber = etMobileNumber.getText().toString();
                if (strMobileNumber.length() > 10) {

                } else {
                    strMobileNumber = spinCountryCode.getText().toString() + strMobileNumber;
                }

                tvFullName.setText(etFirstName.getText().toString() + " " + etLastName.getText().toString());

                if (etMobileNumber.getText().toString().length() > 10) {
                    tvEmailMobile.setText(etEmailID.getText().toString() + "   |    " + etMobileNumber.getText().toString());
                } else {
                    tvEmailMobile.setText(etEmailID.getText().toString() + "   |    " + spinCountryCode.getText().toString() + etMobileNumber.getText().toString());
                }


                MyPref.setPref(getApplicationContext(), MyPref.PREF_USER_FULLNAME, strUserFirstName + " " + strLastName);
                mobFlag = 1;
                dialog.dismiss();
            }
        });


        spinCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EventOrderPreview.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dailog_spin_country_code);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                EditText editSearchCountryCode = dialog.findViewById(R.id.searchCountryCode);
                final ListView listCountryCode = dialog.findViewById(R.id.listViewCountryCode);
                TextView tvCancle = dialog.findViewById(R.id.tvCancel);

                mCountryCodeSpinAdapter = new CountryCodeListAdapter(getApplicationContext(), Otapp.mCountryCodePojoList);
                listCountryCode.setAdapter(mCountryCodeSpinAdapter);

                listCountryCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (countryCodeFlag == 1) {
                            if (tempCountryCodeList.get(position).code.equals("")) {
                                spinCountryCode.setText("+255");
                                strCountryCode = spinCountryCode.getText().toString();
                            } else {
                                spinCountryCode.setText(tempCountryCodeList.get(position).code);
                                strCountryCode = spinCountryCode.getText().toString();
                            }
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
                            countryCodeFlag = 0;
                        } else {
                            if (Otapp.mCountryCodePojoList.get(position).code.equals("")) {
                                spinCountryCode.setText("+255");
                                strCountryCode = spinCountryCode.getText().toString();
                            } else {
                                spinCountryCode.setText(Otapp.mCountryCodePojoList.get(position).code);
                                strCountryCode = spinCountryCode.getText().toString();
                            }
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
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
                        String search = s.toString();
                        tempCountryCodeList = new ArrayList<>();
                        if (search.equals("")) {
                            mCountryCodeSpinAdapter = new CountryCodeListAdapter(getApplicationContext(), Otapp.mCountryCodePojoList);
                            listCountryCode.setAdapter(mCountryCodeSpinAdapter);
                            mCountryCodeSpinAdapter.notifyDataSetChanged();
                            countryCodeFlag = 0;
                        } else {
                            for (int i = 0; i < Otapp.mCountryCodePojoList.size(); i++) {

                                if (Otapp.mCountryCodePojoList.get(i).name.toUpperCase().startsWith(search.toUpperCase()) || Otapp.mCountryCodePojoList.get(i).code.replaceAll("\\+", "").startsWith(search)) {
                                    tempCountryCodeList.add(Otapp.mCountryCodePojoList.get(i));
                                    countryCodeFlag = 1;
                                }

                            }
                            if (tempCountryCodeList.size() == 0) {
                                Utils.showToast(getApplicationContext(), "No Country Code Found");
                            }
                            mCountryCodeSpinAdapter = new CountryCodeListAdapter(getApplicationContext(), tempCountryCodeList);
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


        dialog.show();


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
       /* String respons=MyPref.getPref(getApplicationContext(),MyPref.PREF_PROMO_ID,"");
        mCouponResponsePojo=  new Gson().fromJson(respons, PromoCodeResponse.class);*/
        promoId = MyPref.getPref(getApplicationContext(), MyPref.PREF_PROMO_ID, "");
        if (!promoId.equals("")) {
            getPaymentSummary();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
/*
        if (requestCode == AppConstants.RC_EVENT_COUPON_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                String mCouponResponse = data.getExtras().getString(AppConstants.BNDL_COUPON_RESPONSE);
                if (!TextUtils.isEmpty(mCouponResponse)) {
                    mCouponResponsePojo = new Gson().fromJson(mCouponResponse, PromoCodeResponse.class);
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
*/
    }

    public void removePromoCode() {

        JSONObject ticketInfo = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject tickt = new JSONObject();
        try {
            for (int i = 0; i < tempArrylist.size(); i++) {
                ticketInfo.put("tkt_id", tempArrylist.get(i).tkt_id);
                ticketInfo.put("tot_tkt_id_tkts_count", tempArrylist.get(i).count);
                ticketInfo.put("single_fare", "0.00");
                ticketInfo.put("seats", "");
                jsonArray.put(ticketInfo);
            }
            tickt.put("tkts_info", jsonArray);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        String strTicketInfo = tickt.toString();
        String key = "";

        promoCode = "";
        currecy = "";


        strTotalFare = eventPaymentSummaryResponse.promo_fare;
        key = calculateHash(1, calculateHash(4, promoCode + strEventId + strTicketInfo + strTotalFare + strUkey + currecy + "A" + "pr0mOCode"));
        OtappApiServices otappApiServices = RestClient.getClient(true);
        Call<PromoCodeResponse> mCallPromoCode = otappApiServices.getPromoCode(promoCode, strEventId, strTicketInfo, strTotalFare, strUkey, currecy, "A", key);
        mCallPromoCode.enqueue(new Callback<PromoCodeResponse>() {
            @Override
            public void onResponse(Call<PromoCodeResponse> call, Response<PromoCodeResponse> response) {
                JSONObject jsonObjectResponse = null;
                try {
                    jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                    Log.d("Log", "Response : " + jsonObjectResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (response.body().status.equals("404")) {
                    Toast.makeText(EventOrderPreview.this, "" + response.body().message, Toast.LENGTH_SHORT).show();
                    MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_CODE, "");
                    MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_FLAG, "0");
                }
            }

            @Override
            public void onFailure(Call<PromoCodeResponse> call, Throwable t) {

            }
        });

    }

    public void callZeroPaymentPromo() {
        boolean isConnected = AppConstants.isConnected(getApplicationContext());
        if (isConnected) {
          /*  if(!Utils.isProgressDialogShowing()){
                Utils.showProgressDialog(getApplicationContext());
            }*/
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
            String strCustName = MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_FULLNAME, "");
            String strCustMob = strMobileNumber;
            String strCustEMail = strMailId;
            String strCustId = MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_ID, "");

            apiKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4, AuthKey + "b0OkproM)")));
            key = calculateHash(AuthKey, calculateHash(1, calculateHash(4, agentId + bookFrom + strEventId + strEventDate + strUkey + promoId + noOfTickets + strCustId +
                    strCustName + strCustEMail + strCustMob + "AND" + "Bo0kPr0mo")));

            OtappApiServices otappApiServices = RestClient.getClient(true);
            Call<ZeroPaymentPromoResponse> mCallZeroPayment = otappApiServices.getPromoZeroPayment(apiKey, agentId, bookFrom, String.valueOf(AuthKey), strEventId, strUkey, promoId,
                    strEventDate, String.valueOf(noOfTickets), strCustId, strCustName, strCustEMail, strCustMob, "AND", key);
            mCallZeroPayment.enqueue(new Callback<ZeroPaymentPromoResponse>() {
                @Override
                public void onResponse(Call<ZeroPaymentPromoResponse> call, Response<ZeroPaymentPromoResponse> response) {
                    /* Utils.closeProgressDialog();*/
                    progressDialog.dismiss();
                    JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response : " + jsonObjectResponse);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //               Toast.makeText(EventOrderPreview.this, ""+response.body().message, Toast.LENGTH_SHORT).show();
                    if (response.body().status == 200) {

                        setPaymentSucessful(response.body().ticket);
                    }

                }

                @Override
                public void onFailure(Call<ZeroPaymentPromoResponse> call, Throwable t) {
                    /*   Utils.closeProgressDialog();*/
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void setPaymentSucessful(String mBookingID) {
        Utils.closeProgressDialog();
//        popBackStack(ServiceFragment.Tag_ServiceFragment);

        /*   Toast.makeText(this, "book suceess method", Toast.LENGTH_SHORT).show();*/
        if (!Utils.isInternetConnected(getApplicationContext())) {
            Utils.showToast(getApplicationContext(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getApplicationContext());

        String apiKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4, AuthKey + "tKt!nf0")));

        String key = calculateHash(AuthKey, calculateHash(1, calculateHash(4, agentId + bookFrom + mBookingID + "TkT1nF)")));


        OtappApiServices otappApiServices = RestClient.getClient(true);
        Call<PaymentSuceesResponse> callTicketInfo = otappApiServices.getSuccessTicketInfo(apiKey, agentId, bookFrom, String.valueOf(AuthKey), key, mBookingID);

        callTicketInfo.enqueue(new Callback<PaymentSuceesResponse>() {
            @Override
            public void onResponse(Call<PaymentSuceesResponse> call, Response<PaymentSuceesResponse> response) {
                if (response.isSuccessful()) {
                    Utils.closeProgressDialog();
                    PaymentSuceesResponse mEventSuccessPojo = response.body();
                    if (mEventSuccessPojo != null) {
                        if (mEventSuccessPojo.status.equalsIgnoreCase("200")) {

                            Utils.closeProgressDialog();
                            // switchFragment(EventOrderReviewFragment.newInstance(), EventOrderReviewFragment.Tag_EventOrderReviewFragment, bundle);
                            Intent intent = new Intent(getApplicationContext(), TicketSuccessResponseActivity.class);
                            intent.putExtra(AppConstants.BNDL_MOVIE_RESPONSE, new Gson().toJson(mEventSuccessPojo));
                            startActivity(intent);

                        } else {
                            Utils.showToast(getApplicationContext(), mEventSuccessPojo.message);
                            //    popBackStack(ServiceFragment.Tag_ServiceFragment);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<PaymentSuceesResponse> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.back)
    public void onBack() {
        strPromoFlag = MyPref.getPref(getApplicationContext(), MyPref.PREF_PROMO_FLAG, "");
        if (strPromoFlag.equals("1")) {
            removePromoCode();
        }
        finish();
    }


}
