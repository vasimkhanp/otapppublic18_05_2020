package com.otapp.net.Bus.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.otapp.net.Bus.Adapter.BusPaymentSummeryAdapter;
import com.otapp.net.Bus.Adapter.RecyclerPGWAdapter;
import com.otapp.net.Bus.Core.Agent;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.AvailableBuses;
import com.otapp.net.Bus.Core.GetFareResponse;
import com.otapp.net.Bus.Core.ReserveTicketResponse;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.Bus.Core.Seat;
import com.otapp.net.Bus.Network.OtappApiInstance;
import com.otapp.net.Bus.Network.OtappApiService;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.otapp.net.Bus.Core.SHA.calculateHash;

public class BusBookingActivity extends AppCompatActivity {

    @BindView(R.id.imageViewHeaderBack)
    ImageView imageViewHeaderBack;
    @BindView(R.id.recycleViewFare)
    RecyclerView recyclerViewFare;
    @BindView(R.id.imageViewAd)
            ImageView imageViewAdv;
    @BindView(R.id.tvTotal)
    TextView textViewTotalFare;
    @BindView(R.id.tvRouteName)
            TextView tvRouteName;
    @BindView(R.id.tvJournyDate)
            TextView tvJournyDate;
    @BindView(R.id.tvBusType)
    TextView tvBusType;
    @BindView(R.id.tvReturnRouteName)
    TextView tvReturnRouteName;
    @BindView(R.id.tvReturnJournyDate)
    TextView tvReturnJournyDate;
    @BindView(R.id.tvReturnBusType)
    TextView tvReturnBusType;
    @BindView(R.id.tvToRouteName)
    TextView tvToRouteName;
    @BindView(R.id.tvBusName)
    TextView tvBusName;
    @BindView(R.id.tvReturnToRouteName)
    TextView tvReturnToRouteName;
    @BindView(R.id.tvReturnBusName)
    TextView tvReturnBusName;
    @BindView(R.id.layoutReturnDetails)
    LinearLayout layoutReturnDetails;
    @BindView(R.id.tvQuantity)
            TextView tvQuantity;
    @BindView(R.id.recyclerPGW)
    RecyclerView recylerPGW;

    @BindView(R.id.linearCard)
    LinearLayout linearCard;
    @BindView(R.id.linearTigo)
    LinearLayout linearTigo;
    @BindView(R.id.linearMpesa)
    LinearLayout linearMpesa;
    @BindView(R.id.linearAirtel)
    LinearLayout linearAirtel;
    @BindView(R.id.linearNidc)
    LinearLayout linearNidc;
    @BindString(R.string.loading)
    String loading;
    private ProgressDialog progressDialog;
    JSONObject jsonObject=null;
    ArrayList<GetFareResponse.Fare> fareArrayList;
    ArrayList<GetFareResponse.Pgws> pgwsArrayList;
    BusPaymentSummeryAdapter busPaymentSummeryAdapter;
    List<CountryCodePojo.Ad5> advCountrycode = new ArrayList<>();
    String sTigoPesaCharges="", sAirtelPesaCharges="",sCrdbCharges="",sNidcCharges="",sMPesaCharges="";
    public boolean isCreditCardEnabled = false, isMpesaEnabled = false, isTigoEnabled = false, isAirtelMoneyEnabled = false,isNidcEnabled=false;
    String sTotalPromoFare="",sContactPersonName="",sContactPersonEmail="",sContactPersonPhone="",sPassengerDetails="",sReturnPassengerDetails="";
    int AuthKey;

    private AvailableBuses availableBuses;
    private AvailableBuses availableReturnBuses;
    private SearchBusDetails searchBusDetails;
    private HashMap<String, Seat> hashMapSelectedSeats = new HashMap<>();
    private ArrayList<Seat> arrayListOnWord= new ArrayList<>();
    private ArrayList<Seat> arrayListReturn= new ArrayList<>();
    JSONArray array,returnArray;

    private SimpleDateFormat simpleDayFormat;
    private SimpleDateFormat simpleMonthFormat;
    SimpleDateFormat simpleDateFormat;
    Date date;
    RecyclerPGWAdapter recyclerPGWAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_booking);
        ButterKnife.bind(this);
        advCountrycode= Otapp.mAdsPojoList;
        recyclerViewFare.setHasFixedSize(true);
        recyclerViewFare.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        fareArrayList=getIntent().getParcelableArrayListExtra("fare");
        pgwsArrayList=getIntent().getParcelableArrayListExtra("pgw");
        sTotalPromoFare=getIntent().getStringExtra("totalFare");
        sPassengerDetails=getIntent().getStringExtra("PassegerDetails");
        sReturnPassengerDetails=getIntent().getStringExtra("ReturnPassegerDetails");
        sContactPersonName=getIntent().getStringExtra("contactPersonName");
        sContactPersonEmail=getIntent().getStringExtra("contactPersonEmail");
        sContactPersonPhone=getIntent().getStringExtra("contactPersonPhone");
        availableBuses = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.BUS);
        availableReturnBuses = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.RETURN_BUS);
        searchBusDetails = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEARCH_BUS);
        hashMapSelectedSeats = (HashMap<String, Seat>) getIntent().getSerializableExtra(AppConstants.IntentKeys.SELECTED_SEAT);
        arrayListOnWord=searchBusDetails.getArrayListOnWords();
        arrayListReturn=searchBusDetails.getArrayListReturn();
        recylerPGW.setHasFixedSize(true);
        recylerPGW.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Log.d("Log","fareList "+fareArrayList.size());
        Log.d("Log","pgwList "+pgwsArrayList .size());
        try {
            array = new JSONArray(sPassengerDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            returnArray = new JSONArray(sReturnPassengerDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }
         simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = simpleDateFormat.parse(searchBusDetails.getsDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat = new SimpleDateFormat("dd");
        simpleMonthFormat = new SimpleDateFormat("MMM");
        simpleDayFormat = new SimpleDateFormat("EEE");
        //textViewDate.setText(simpleDayFormat.format(calendar.getTime())+","+simpleDateFormat.format(calendar.getTime())+simpleDayFormat.format(calendar.getTime()));
        String Date = (String) DateFormat.format("EEE", date) + "," + DateFormat.format("dd", date) + " " + DateFormat.format("MMM", date);
        tvJournyDate.setText(Date);
        int noOfTickets=0;
        if(AppConstants.Keys.isReturnBus) {
             noOfTickets = arrayListOnWord.size() + arrayListReturn.size();
        }else {
             noOfTickets=arrayListOnWord.size();
        }



        tvQuantity.setText(""+noOfTickets);
        tvRouteName.setText(searchBusDetails.getsFrom());
        tvToRouteName.setText(searchBusDetails.getsTo());
        tvBusName.setText(availableBuses.getBus_name());
        tvBusType.setText(availableBuses.getBus_type());

        if(AppConstants.Keys.isReturnBus) {
            layoutReturnDetails.setVisibility(View.VISIBLE);
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = simpleDateFormat.parse(searchBusDetails.getsReturnDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            simpleDateFormat = new SimpleDateFormat("dd");
            simpleMonthFormat = new SimpleDateFormat("MMM");
            simpleDayFormat = new SimpleDateFormat("EEE");
            //textViewDate.setText(simpleDayFormat.format(calendar.getTime())+","+simpleDateFormat.format(calendar.getTime())+simpleDayFormat.format(calendar.getTime()));
            Date = (String) DateFormat.format("EEE", date) + "," + DateFormat.format("dd", date) + " " + DateFormat.format("MMM", date);
            tvReturnJournyDate.setText(Date);
            tvReturnRouteName.setText(searchBusDetails.getsReturnFrom());
            tvReturnToRouteName.setText(searchBusDetails.getsReturnTo());
            tvReturnBusName.setText(availableReturnBuses.getBus_name());
            tvReturnBusType.setText(availableReturnBuses.getBus_type());
        }else {
            layoutReturnDetails.setVisibility(View.GONE);
        }
       // tvReturnRouteName.setText(searchBusDetails.getR);
/*sContactPersonPhone=sContactPersonPhone.replaceAll("\\+","");*/
if(advCountrycode!=null&& advCountrycode.size()<=6) {
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
}

        busPaymentSummeryAdapter= new BusPaymentSummeryAdapter(getApplicationContext(),fareArrayList);
        recyclerViewFare.setAdapter(busPaymentSummeryAdapter);
        busPaymentSummeryAdapter.notifyDataSetChanged();



        DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");
        //textViewTotalFare.setText(sTotalPromoFare);
        textViewTotalFare.setText(fareArrayList.get(fareArrayList.size()-1).amount);
     //   textViewTotalFare.setText(String.valueOf(decimalFormat.format(sTotalPromoFare)) );
        if(pgwsArrayList.size()>0 && pgwsArrayList!=null){
            for (int i = 0; i < pgwsArrayList.size(); i++) {

                if (pgwsArrayList.get(i).pgw_name.equalsIgnoreCase("Credit\\/Debit Card")) {
                    sCrdbCharges=pgwsArrayList.get(i).extra_pgw_charges;
                    if (pgwsArrayList.get(i).pgw_enabled.equals("1")) {
                        isCreditCardEnabled = true;
                        linearCard.setVisibility(View.VISIBLE);
                    }else {
                        isCreditCardEnabled = false;
                        linearCard.setVisibility(View.GONE);
                    }
                } else {
                    if (pgwsArrayList.get(i).pgw_name.equalsIgnoreCase("Tigo Pesa")) {
                        sTigoPesaCharges=pgwsArrayList.get(i).extra_pgw_charges;
                        if (pgwsArrayList.get(i).pgw_enabled.equals("1")) {
                            isTigoEnabled = true;
                            linearTigo.setVisibility(View.VISIBLE);
                        }else {
                            isTigoEnabled = false;
                            linearTigo.setVisibility(View.GONE);
                        }
                    } else {
                        if (pgwsArrayList.get(i).pgw_name.equalsIgnoreCase("MPESA")) {
                            sMPesaCharges=pgwsArrayList.get(i).extra_pgw_charges;
                            if (pgwsArrayList.get(i).pgw_enabled.equals("1")) {
                                isMpesaEnabled = true;
                                linearMpesa.setVisibility(View.VISIBLE);
                            }else {
                                isMpesaEnabled = false;
                                linearMpesa.setVisibility(View.GONE);
                            }
                        } else {
                            if (pgwsArrayList.get(i).pgw_name.equalsIgnoreCase("Airtel")) {
                                sAirtelPesaCharges=pgwsArrayList.get(i).extra_pgw_charges;
                                if (pgwsArrayList.get(i).pgw_enabled.equals("1")) {
                                    isAirtelMoneyEnabled = true;
                                    linearAirtel.setVisibility(View.VISIBLE);
                                }else {
                                    isAirtelMoneyEnabled = false;
                                    linearAirtel.setVisibility(View.GONE);
                                }
                            }else {
                                if (pgwsArrayList.get(i).pgw_name.equalsIgnoreCase("NIDC")) {
                                    sNidcCharges=pgwsArrayList.get(i).extra_pgw_charges;
                                    if (pgwsArrayList.get(i).pgw_enabled.equals("1")) {
                                        isNidcEnabled = true;

                                    }else {
                                        isNidcEnabled = false;

                                    }
                                }
                            }
                        }
                    }
                }
            }

        }else {
            isCreditCardEnabled = false;
            isTigoEnabled = false;
            isMpesaEnabled = false;
            isAirtelMoneyEnabled = false;
            isNidcEnabled = false;
        }
        recyclerPGWAdapter= new RecyclerPGWAdapter(this,pgwsArrayList,searchBusDetails,availableBuses,availableReturnBuses,sPassengerDetails,sReturnPassengerDetails);
        recylerPGW.setAdapter(recyclerPGWAdapter);
        recyclerPGWAdapter.notifyDataSetChanged();

    }
    @OnClick(R.id.linearNidc)
    public void onClickLinearNidc(){
        String payCode="NIDC";
        if (!isNidcEnabled) {
            Utils.showToast(getApplicationContext(), getString(R.string.msg_payment_disable));
            return;
        }
        boolean isConnected=AppConstants.isConnected(getApplicationContext());
        if(isConnected){
            showProgress();
            Random r = new Random();
            int random = r.nextInt(4 - 1 + 1) + 1;
            if (random > 4 && random < 1) {
                random = 3;
            }
            AuthKey = Integer.parseInt(String.valueOf(random));

            String compId=com.otapp.net.utils.AppConstants.sCompId;
            String agentId=com.otapp.net.utils.AppConstants.sAgentId;
            String isFrom=com.otapp.net.utils.AppConstants.sIsFrom;
            String currency="1";
            String mCustId = "";
            if (MyPref.getPref(getApplicationContext(), MyPref.PREF_IS_LOGGED, false)) {
                mCustId = MyPref.getPref(getApplicationContext(), MyPref.PREF_USER_ID, "");
            }

            Log.d("Log","AuthKey "+AuthKey);
            Log.d("Log","phone "+sContactPersonPhone);
            Log.d("Log","email "+sContactPersonEmail);
            Log.d("Log","compId "+compId);
            Log.d("Log","agentId "+agentId);
            Log.d("Log","isFrom "+isFrom);
            Log.d("Log","currency "+currency);
            Log.d("Log","mCustId "+mCustId);



            String sFromId=searchBusDetails.getsFromId();
            String sToID=searchBusDetails.getsToId();
            String sTravelDate= searchBusDetails.getsDate();
            String subId= availableBuses.getSub_id();
            String sTdiId= availableBuses.getTdi_id();
            String sLbID= availableBuses.getLb_id();
            String sPbID= availableBuses.getPbi_id();
            String sAsiID= availableBuses.getAsi_id();
            String sUkey= MyPref.getPref(getApplicationContext(),MyPref.PREF_BUS_UKEY,"");
            String sBordingPoint= searchBusDetails.getsBoarding();
            if(sBordingPoint.equals("")){
                sBordingPoint= searchBusDetails.getsFrom();
            }
            String sDropingPoint= searchBusDetails.getsDropping();
            if(sDropingPoint.equals("")){
                sDropingPoint= searchBusDetails.getsTo();
            }
/*Log.d("Log", "ONWORD DETAILS");
            Log.d("Log","mCustId "+mCustId);
            Log.d("Log","sFromId "+sFromId);
            Log.d("Log","sToID "+sToID);
            Log.d("Log","sTravelDate "+sTravelDate);
            Log.d("Log","subId "+subId);
            Log.d("Log","sTdiId "+sTdiId);
            Log.d("Log","sLbID "+sLbID);
            Log.d("Log","sPbID "+sPbID);
            Log.d("Log","sAsiID "+sAsiID);
            Log.d("Log","sUkey "+sUkey);
            Log.d("Log","sBordingPoint "+sBordingPoint);
            Log.d("Log","sDropingPoint "+sDropingPoint);
            Log.d("Log","PAssengers befor json "+sPassengerDetails);
            Log.d("Log","onwwor array = "+array.toString());
            Log.d("Log","rentrn array = "+returnArray.toString());*/


            JSONObject obj = new JSONObject();
            JSONObject returnObj= new JSONObject();
            try {
                obj.put("from_id", sFromId);
                obj.put("to_id", sToID);
                obj.put("trvl_dt", sTravelDate);
                obj.put("sub_id", subId);
                obj.put("tdi_id", sTdiId);
                obj.put("lb_id", sLbID);
                obj.put("pbi_id", sPbID);
                obj.put("asi_id", sAsiID);
                obj.put("ukey", sUkey);
                obj.put("boarding", sBordingPoint);
                obj.put("dropping", sDropingPoint);
                obj.put("passengers", array);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
          //  String passengers=obj.toString();
        //    Log.d("Log","passengers "+passengers);
            Log.d("Log","onWord Passenger "+obj.toString());
            String retrnPassenger="";

           /* Log.d("Log", "RETURN DETAILS");
            Log.d("Log","mCustId "+mCustId);
            Log.d("Log","sFromId "+searchBusDetails.getsReturnFromId());
            Log.d("Log","sToID "+searchBusDetails.getsReturnToId());
            Log.d("Log","sTravelDate "+searchBusDetails.getsReturnDate());
            Log.d("Log","subId "+availableReturnBuses.sub_id);
            Log.d("Log","sTdiId "+availableReturnBuses.tdi_id);
            Log.d("Log","sLbID "+availableReturnBuses.lb_id);
            Log.d("Log","sPbID "+availableReturnBuses.pbi_id);
            Log.d("Log","sAsiID "+availableReturnBuses.asi_id);
            Log.d("Log","return jsson array "+returnArray.toString());*/


            if(AppConstants.Keys.isReturnBus){
                try {
                    returnObj.put("from_id", searchBusDetails.getsReturnFromId());
                    returnObj.put("to_id", searchBusDetails.getsReturnToId());
                    returnObj.put("trvl_dt", searchBusDetails.getsReturnDate());
                    returnObj.put("sub_id", availableReturnBuses.getSub_id());
                    returnObj.put("tdi_id", availableReturnBuses.getTdi_id());
                    returnObj.put("lb_id", availableReturnBuses.getLb_id());
                    returnObj.put("pbi_id", availableReturnBuses.getPbi_id());
                    returnObj.put("asi_id", availableReturnBuses.getAsi_id());
                    returnObj.put("ukey", MyPref.getPref(getApplicationContext(),MyPref.RETURN_UKEY,""));
                    returnObj.put("boarding", searchBusDetails.getsReturnBoarding());
                    returnObj.put("dropping", searchBusDetails.getsReturnDropping());
                    returnObj.put("passengers", returnArray);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            Log.d("Log","retrun Json "+returnObj.toString());


            String apiKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"Re5Erve5e@t")));
            String postKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,obj.toString()+returnObj.toString()
                    +sContactPersonPhone+sContactPersonEmail+currency+mCustId+payCode+compId+agentId+isFrom+"Re5erVeb0)k")));
            Log.d("Log","apikey "+apiKey);
            Log.d("Log","postKEy "+postKey);
            OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
            Call<ReserveTicketResponse> getReserveBooking= otappbusApiService.getReserveBooking(apiKey,String.valueOf(AuthKey),postKey,compId,agentId,
                    isFrom,currency,sContactPersonPhone,sContactPersonEmail,obj.toString(),returnObj.toString(),mCustId,payCode);
            getReserveBooking.enqueue(new Callback<ReserveTicketResponse>() {
                @Override
                public void onResponse(Call<ReserveTicketResponse> call, Response<ReserveTicketResponse> response) {
                    hideProgress();
                    JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response : " + jsonObjectResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(BusBookingActivity.this, ""+response.body().message, Toast.LENGTH_SHORT).show();
                    if(response.body().status==AppConstants.Status.SUCCESS){
                        MyPref.setPref(getApplicationContext(), MyPref.PREF_BUS_UKEY,"");
                        MyPref.setPref(getApplicationContext(), MyPref.RETURN_UKEY,"");
                        MyPref.setPref(getApplicationContext(), MyPref.ASID,"");

                        Intent intent =new Intent(getApplicationContext(),BookingReserveActivity.class);
                        intent.putExtra(com.otapp.net.utils.AppConstants.BNDL_BUS_RESPONSE,jsonObjectResponse.toString());
                        startActivity(intent);
                    }else {
                        Toast.makeText(BusBookingActivity.this, ""+response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReserveTicketResponse> call, Throwable t) {
                            hideProgress();
                    Toast.makeText(BusBookingActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    @OnClick(R.id.linearCard)
    public void onClickLinearCard(){
        if (!isCreditCardEnabled) {
            Utils.showToast(getApplicationContext(), getString(R.string.msg_payment_disable));
            return;
        }
    }
    @OnClick(R.id.linearTigo)
    public void onClickLinearTigo(){
        if (!isTigoEnabled) {
            Utils.showToast(getApplicationContext(), getString(R.string.msg_payment_disable));
            return;
        }
    }
    @OnClick(R.id.linearMpesa)
    public void onClickLinearMpesa(){
        if (!isAirtelMoneyEnabled) {
            Utils.showToast(getApplicationContext(), getString(R.string.msg_payment_disable));
            return;
        }
    }
    @OnClick(R.id.linearAirtel)
    public void onClickLinearAirtel(){
        if (!isAirtelMoneyEnabled) {
            Utils.showToast(getApplicationContext(), getString(R.string.msg_payment_disable));
            return;
        }
    }

    @OnClick(R.id.imageViewHeaderBack)
    void onBack() {
       finish();
    }

    @OnClick(R.id.tvPay)
    void onPay() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void showProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(loading);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
