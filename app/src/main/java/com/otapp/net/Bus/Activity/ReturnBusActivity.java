package com.otapp.net.Bus.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.otapp.net.Bus.Adapter.PassengerBusAdapter;
import com.otapp.net.Bus.Core.Agent;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.AvailableBuses;
import com.otapp.net.Bus.Core.PassengerBus;
import com.otapp.net.Bus.Core.SHA;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.Bus.Core.SearchBusResponse;
import com.otapp.net.Bus.Core.Seat;
import com.otapp.net.Bus.Core.SeatMap;
import com.otapp.net.Bus.Core.TelephonyInfo;
import com.otapp.net.Bus.Network.OtappApiInstance;
import com.otapp.net.Bus.Network.OtappApiService;
import com.otapp.net.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.otapp.net.Bus.Core.SHA.calculateHash;

public class ReturnBusActivity extends AppCompatActivity {


    @BindView(R.id.imageViewHeaderBack)
    ImageView imageViewBack;
    @BindView(R.id.textViewRoute)
    TextView textViewRoute;
    @BindView(R.id.recyclerViewBusRoutes)
    RecyclerView recyclerViewBusRoute;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewBusRoute)
    TextView textViewBusRoute;
    @BindView(R.id.textViewSeats)
    TextView textViewSeats;
    @BindView(R.id.textViewSeatType)
    TextView textViewSeatType;
    @BindView(R.id.textViewDetails)
    TextView textViewDetails;
    @BindView(R.id.textViewFare)
    TextView textViewFare;

    @BindString(R.string.Travel_date)
    String sDateLabel;

    private RecyclerView.LayoutManager layoutManager;
    private PassengerBusAdapter passengerBusAdapter;
    private ArrayList<AvailableBuses> passengerRetrunBusArrayList = new ArrayList<>();
    private SearchBusDetails searchBusDetails;
    private String sKey;
    private ProgressDialog progressDialog;
    private String sIMEI;
    private String sTicketNo;
    private int AuthKey;
    private String sAppVersion;
    private String sApiKey;
    private String sLanguage = "1";
    private String sGender = "Male";
    private String sLatitude = "0.0";
    private String sLongitude = "0.0";
    private LocationManager locationManager;
    private AvailableBuses availableBuses;
    private HashMap<String, Seat> hashMapSelectedSeats = new HashMap<>();
    private double Fare = 0;
    private SeatMap seatMap;
    private String sSeats = "";
    private Agent agent;
    private String sIP;
    private String sFrom;
    private String sFromId;
    private String sTo;
    private String sToId;
    private String sDate;
    private String sReturnDate = "";
    @BindString(R.string.please_enter_station_name)
    String sInvalidTo;
    @BindString(R.string.please_enter_station_name)
    String sInvalidFrom;
    @BindString(R.string.only_alphabets_are_allowed)
    String sOnlyAlphabets;
    @BindString(R.string.please_select_station_form_list)
    String sSelectStationError;
    @BindString(R.string.Loading)
    String sLoading;
    @BindString(R.string.no_internet_connection)
    String sNoInternet;
    @BindString(R.string.seat)
    String sSeatLabel;
    String sFare="";

    private String sCompId= com.otapp.net.utils.AppConstants.sCompId;
    private String sAgentId= com.otapp.net.utils.AppConstants.sAgentId;
    private String sIsFrom= com.otapp.net.utils.AppConstants.sIsFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_bus);
        ButterKnife.bind(this);

        agent = new Agent();
        imageViewBack.setVisibility(View.VISIBLE);

        availableBuses = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.BUS);
        searchBusDetails = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEARCH_BUS);

            seatMap = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEAT_MAP);
         //   sKey = getIntent().getExtras().getString(AppConstants.IntentKeys.KEY);
            hashMapSelectedSeats = (HashMap<String, Seat>) getIntent().getSerializableExtra(AppConstants.IntentKeys.SELECTED_SEAT);
        sFare = getIntent().getExtras().getString(AppConstants.IntentKeys.FARE);
//       hashMapSelectedSeats = new HashMap<>();

        searchBusDetails.setRetrunActivityCalled(true);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        String sDate = "";
        String sTime = "";
        try {
            date = simpleDateFormat.parse(searchBusDetails.getsReturnDate());
            simpleDateFormat = new SimpleDateFormat("E,  dd-MMM-yyyy");
            sDate = simpleDateFormat.format(date);
        } catch (ParseException e1) {
            e1.printStackTrace();
            Toast.makeText(ReturnBusActivity.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
        }
        textViewRoute.setText(searchBusDetails.getsReturnFrom()+ " - " + searchBusDetails.getsReturnTo());
        textViewBusRoute.setText(availableBuses.getBus_name());
        textViewSeatType.setText(availableBuses.getBus_type());

//        DecimalFormat decimalFormat = new DecimalFormat("0.00");
       /* DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");
        String sTotalFare = String.valueOf(decimalFormat.format(sFare));*/
        textViewFare.setText("TSH. "+sFare);
        textViewDetails.setText(searchBusDetails.getsFrom() + " - " + searchBusDetails.getsTo());
        getSelectedSeats();
//        Toast.makeText(this, "" + sDate, Toast.LENGTH_SHORT).show();
        textViewDate.setText(sDate);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewBusRoute.setLayoutManager(layoutManager);
        recyclerViewBusRoute.setHasFixedSize(false);
     /*   sFrom = searchBusDetails.getsFrom();
        sFromId = searchBusDetails.getsFromId();
        sTo = searchBusDetails.getsTo();
        sToId = searchBusDetails.getsToId();*/
//        getDetails();
//        getLocalIpAddress();
       getBuses();
       // Toast.makeText(this, "getBusess", Toast.LENGTH_SHORT).show();


      /*  passengerBusArrayList = new ArrayList<>();
        PassengerBus passengerBus = new PassengerBus("Neeta Travels", "758444",
                "1", "AC Seater 2*2", "07:00", "10:00",
                "03:00 Hrs", "1000", sDate, "", "30");
        passengerBusArrayList.add(passengerBus);
        passengerBus = new PassengerBus("VRL Travels", "545121",
                "2", "AC Seater 2*2", "11:00", "02:00",
                "03:00 Hrs", "800", sDate, "", "28");
        passengerBusArrayList.add(passengerBus);
        passengerBus = new PassengerBus("SRS Travels", "545121",
                "2", "AC Seater 2*2", "12:00", "02:30",
                "02:30 Hrs", "900", sDate, "", "27");
        passengerBusArrayList.add(passengerBus);
        passengerBus = new PassengerBus("VRL Travels", "545121",
                "2", "AC Seater 2*2", "04:00", "08:00",
                "04:00 Hrs", "600", sDate, "", "20");
        passengerBusArrayList.add(passengerBus);*/
     //   onSuccessBuses("HJKGHJ4HJHJ53");
      //  onSuccessBuses();
    }

    private void getDetails() {
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(ReturnBusActivity.this);
        sIMEI = telephonyInfo.getImsiSIM1();
        PackageInfo pInfo = null;
        String AndroidId = Settings.Secure.getString(ReturnBusActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            pInfo = ReturnBusActivity.this.getPackageManager().getPackageInfo(ReturnBusActivity.this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        sAppVersion = pInfo.versionName;
        sAppVersion = AppConstants.Status.APP_VERSION;
        //RANDOM NUMBER
        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }
        AuthKey = Integer.parseInt(String.valueOf(random));
    }

    public void getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                     inetAddressEnumeration.hasMoreElements(); ) {
                    InetAddress inetAddress = inetAddressEnumeration.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        Log.d("Login Activity ", inetAddress.getHostAddress());
                        sIP = inetAddress.getHostAddress();
                        return;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    private void getSelectedSeats() {
       /* int i = 0;
        String sFare="";*/

/*
        if (hashMapSelectedSeats.size() > 0) {
            for (String sKey : hashMapSelectedSeats.keySet()) {
                Seat seat = hashMapSelectedSeats.get(sKey);
                sSeats += "," + seat.getsSeatNo();
                sFare=seat.getsFare();
                i++;
            }
            if (!sSeats.equals("")) {
                textViewSeats.setText(sSeatLabel + " : " + sSeats.substring(1, sSeats.length()));
                textViewFare.setText(sFare);
            }
//            passengerInfoAdapter = new PassengerInfoAdapter(this, passengersArrayList, this);
//            listViewPassengers.setAdapter(passengerInfoAdapter);
        }
*/
        String sSelectedSeats = "";
/*
        if(hashMapSelectedSeats!=null) {
            if (hashMapSelectedSeats.size() > 0) {
                for (String sKey : hashMapSelectedSeats.keySet()) {
                    Seat  seat = hashMapSelectedSeats.get(sKey);
                    if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                        //sSelectedSeats += "," + seat.getsSeatNo();

                        String seatNameArray[] = seat.getsSeatNo().split("-");
                        String seatName = "";
                        if (seatNameArray.length > 0) {
                            seatName = seatNameArray[3];
                        }
                        if(sSelectedSeats.equals("")){
                            sSelectedSeats+=seatName;
                        }else {
                            sSelectedSeats += "," + seatName;
                        }
                    }
                }
            }
        }
*/
        if(searchBusDetails.getArrayListOnWords()!=null) {
            if (searchBusDetails.getArrayListOnWords().size() > 0) {
                for (int i=0;i<searchBusDetails.getArrayListOnWords().size();i++) {
                    Seat  seat = searchBusDetails.getArrayListOnWords().get(i);
                    if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                        //sSelectedSeats += "," + seat.getsSeatNo();

                        String seatNameArray[] = seat.getsSeatNo().split("-");
                        String seatName = "";
                        if (seatNameArray.length > 0) {
                            seatName = seatNameArray[3];
                        }
                        if(sSelectedSeats.equals("")){
                            sSelectedSeats+=seatName;
                        }else {
                            sSelectedSeats += "," + seatName;
                        }
                    }
                }
            }
        }
        textViewSeats.setText(sSeatLabel + " : " + sSelectedSeats);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Log", "Language : " + Locale.getDefault().getDisplayLanguage());
        if (!Locale.getDefault().getDisplayLanguage().equals("English")) {
            Toast.makeText(this, "This app supports only english language, Please select english language", Toast.LENGTH_SHORT).show();
            Locale locale = new Locale("English");
            Locale.setDefault(locale);
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//            Locale.setDefault(Locale);
        }
    }

    @OnClick(R.id.imageViewHeaderBack)
    void OnBack() {
        finish();
    }

    private void getBuses() {  //  Get Standing Route Details
    //    Toast.makeText(this, "getBusess", Toast.LENGTH_SHORT).show();
        boolean isConnected = AppConstants.isConnected(getApplicationContext());
        if (isConnected) {
            Random r = new Random();
            int random = r.nextInt(4 - 1 + 1) + 1;
            if (random > 4 && random < 1) {
                random = 3;
            }
           /* Agent agent = new Agent();
            agent.setsAgentId("924");*/
            AuthKey = Integer.parseInt(String.valueOf(random));
            //AuthKey=1;
            //sDate="2020-04-20";
          /*  sIP = AppConstants.Status.IP;
            sIMEI = AppConstants.Status.IMEI;
            sAppVersion = AppConstants.Status.APP_VERSION;*/
//            sDate = AppConstants.Formats.getServerDate(textViewDate.getText().toString().trim());
//            sReturnDate = AppConstants.Formats.getServerDate(textViewReturnDate.getText().toString().trim());

            sFromId=searchBusDetails.getsReturnFromId();
            sToId=searchBusDetails.getsReturnToId();
            sReturnDate=searchBusDetails.getsReturnDate();
            sFrom=searchBusDetails.getsReturnFrom();
            sTo=searchBusDetails.getsReturnTo();
            Log.d("Log","To Stop"+searchBusDetails.getsReturnTo());
            Log.d("Log","From  Stop"+searchBusDetails.getsReturnFrom());
                String sApiKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"Get-5e@rChbu5e$")));
            String postKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4, sFromId+sToId+sReturnDate+sCompId+sAgentId+sIsFrom+"5e@rcHbu5e$")));

            OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
            Call<SearchBusResponse> callGetStations = otappbusApiService.searchBuses(sApiKey,String.valueOf(AuthKey), sFromId, sToId,sCompId,sReturnDate, sAgentId,sIsFrom,postKey);

            Log.d("Log", "Key : " + sKey);
            Log.d("Log", "agent : " + sAgentId);
            Log.d("Log", "AuthKey : " + AuthKey);
            Log.d("Log", "From Id : " + sFromId);
            Log.d("Log", "To Id : " + sToId);
            Log.d("Log", "Comp Id : " + sCompId);
            Log.d("Log", " isFrom : " + sIsFrom);
            Log.d("Log", "Date : " + sReturnDate);
            Log.d("Log", "APIkey: " + sApiKey);
            Log.d("Log", "Get Buses : " + callGetStations.request().url());
            showProgress();

            callGetStations.enqueue(new Callback<SearchBusResponse>() {
                @Override
                public void onResponse(Call<SearchBusResponse> call, Response<SearchBusResponse> response) {
                    if (response.body() != null) {
                        if (response.body().status == AppConstants.Status.SUCCESS) {
                            JSONObject jsonObjectResponse = null;
                            try {
                                jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                                Log.d("Log", "Response : " + jsonObjectResponse);
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            SearchBusResponse searchBusResponse = response.body();
                            passengerRetrunBusArrayList = searchBusResponse.getAvailableBusesArrayList();

//                        routes = response.body();
                            onSuccessBuses();
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error Null", Toast.LENGTH_SHORT).show();
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<SearchBusResponse> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(ReturnBusActivity.this, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
/*
            callGetStations.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<SearchBusResponse> call, Response<SearchBusResponse> response) {
                    Log.d("Log","Responnse");
                    hideProgress();
                    JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response bus list : " + jsonObjectResponse);
                        Toast.makeText(ReturnBusActivity.this, ""+jsonObjectResponse.get("ststus"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("Log","Responnse eroor");
                    Toast.makeText(ReturnBusActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
hideProgress();
                }
            });
*/

        } else {
            hideProgress();
            Toast.makeText(ReturnBusActivity.this, sNoInternet, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void showProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(ReturnBusActivity.this);
        progressDialog.setMessage(sLoading);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }


    private void onSuccessBuses() {
//        Toast.makeText(ReturnBusActivity.this, "" + isReturn, Toast.LENGTH_SHORT).show();
        this.sKey = sKey;
        if (passengerRetrunBusArrayList.size() != 0) {
            searchBusDetails.setAvailableBuses(availableBuses);
           /* searchBusDetails.setsReturnFrom(sTo);
            searchBusDetails.setsReturnFromId(sToId);
            searchBusDetails.setsReturnTo(sFrom);
            searchBusDetails.setsReturnToId(sFromId);*/

            passengerBusAdapter = new PassengerBusAdapter(this, passengerRetrunBusArrayList,searchBusDetails);
            recyclerViewBusRoute.setAdapter(passengerBusAdapter);
            passengerBusAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(ReturnBusActivity.this, "No Bus Found...", Toast.LENGTH_SHORT).show();
        }
    }

}
