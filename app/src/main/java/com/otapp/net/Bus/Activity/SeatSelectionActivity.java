package com.otapp.net.Bus.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.otapp.net.Bus.Adapter.SeatAdapter;
import com.otapp.net.Bus.Core.Agent;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.AvailableBuses;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.Bus.Core.Seat;
import com.otapp.net.Bus.Core.SeatMap;
import com.otapp.net.Bus.Core.SeatType;
import com.otapp.net.Bus.Core.TelephonyInfo;
import com.otapp.net.Bus.Impl.SeatChangeListener;
import com.otapp.net.Bus.Network.OtappApiInstance;
import com.otapp.net.Bus.Network.OtappApiService;
import com.otapp.net.R;
import com.otapp.net.utils.MyPref;

import org.json.JSONArray;
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

public class SeatSelectionActivity extends AppCompatActivity implements /*OtappResponsePresenter,*/ SeatChangeListener {

    @BindView(R.id.textViewArrivingTime)
    TextView textViewArrivingTime;
    @BindView(R.id.textViewTotalFare)
    TextView textViewTotalFare;
    @BindView(R.id.textViewDepartureTime)
    TextView textViewDepartureTime;
    @BindView(R.id.textViewSeatDetails)
    TextView textViewSeatDetails;
    @BindView(R.id.textViewBusName)
    TextView textViewBusName;
    @BindView(R.id.textViewDetails)
    TextView textViewDetails;
    @BindView(R.id.textViewJourneyTime)
    TextView textViewJourneyTime;
    @BindView(R.id.textViewBusRoute)
    TextView textViewBusRoute;
    @BindView(R.id.textViewBusType)
    TextView textViewBusType;
    @BindView(R.id.textViewBook)
    TextView textViewBook;
    @BindView(R.id.textViewTravelDate)
    TextView textViewDate;

    @BindView(R.id.recyclerViewSeats)
    RecyclerView recyclerViewSeats;
    @BindView(R.id.recyclerViewUpperSeats)
    RecyclerView recyclerViewUpperSeats;
    @BindView(R.id.linearLayoutTabSeats)
    LinearLayout linearLayoutTabSeats;


    @BindString(R.string.Loading)
    String sLoading;
    @BindString(R.string.Fare)
    String sFare;
    @BindString(R.string.no_internet_connection)
    String sNoInternet;
    @BindString(R.string.please_select_seats)
    String sSelectBus;
    @BindString(R.string.something_went_wrong_try_again)
    String something_went_wrong;

    private AvailableBuses availableBuses;
    private AvailableBuses availableReturnBuses;
    private String sKey;
    private ArrayList<Seat> seatsArrayList;
    private SeatAdapter seatAdapter;
    private SeatAdapter upperSeatAdapter;

    String[] sSeats = null, sAvailableSeatsArray = null, sProcessingSeatsArray = null;
    String sSelectedSeats = "";
    private String sIP;
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
    private String sFrom;
    private String sTo;
    private String sDate;
    private String sFromId;
    private String sToId;
    private String sBusId;
    private Bundle bundle;
    private Agent agent;
    private SearchBusDetails searchBusDetails;
    private HashMap<String, String> hashMapProcessingSeats = new HashMap<>();
    private HashMap<String, String> hashMapAvailableSeats = new HashMap<>();
    public HashMap<String, Seat> hashMapSelectedSeats ;
    public HashMap<String, Seat> hashMapSelectedReturnSeats;
    private ArrayList<Seat> arrayListOnwordSeats;
    private ArrayList<Seat> arrayListReturnSeat=new ArrayList<>();
    private HashMap<String, SeatType> hashMapSeatsTypes = new HashMap<>();

    private SeatMap seatMap;
    private double Fare = 0;
    private String sub_id;
    private String tdi_id;
    private String lb_id;
    private String  pbi_id;
    private String asi_id;
    private String sCompId= com.otapp.net.utils.AppConstants.sCompId;
    private String sAgentId= com.otapp.net.utils.AppConstants.sAgentId;
    private String sIsFrom= com.otapp.net.utils.AppConstants.sIsFrom;


    private ArrayList<Object> lowerSeatArrayList= new ArrayList<>();
    private ArrayList<Seat> upperSeatsArrayList= new ArrayList<>();
    private String sProcessSeats="";
    private String sAvailableSets="";
    private String sNoOfLowerSeatRow="";
    private String sNoOfUpperSeatRow="";
    private String sUKey="";
    private boolean isUpper=false;
    String sSeatTypeArray[];
    String sFareAmount="";
    boolean isRetrun;

    @BindString(R.string.Travel_date)
    String sDateLabel;

    @BindView(R.id.imageViewHeaderBack)
    ImageView imageViewHeaderBack;
    @BindView(R.id.textViewBus)
    TextView textViewHeaderTitle;
    @BindView(R.id.textViewType)
    TextView textViewType;

    @OnClick(R.id.imageViewHeaderBack)
    void onBack() {
        finish();
    }
    int upperSeatRow;
    GridLayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);
        ButterKnife.bind(this);
        bundle = savedInstanceState;
//        getLocalIpAddress();    //  Get Local IP address
//        getDetails();
        isRetrun=AppConstants.Keys.isReturnBus;
       /* if(!isRetrun && MyPref.getPref(getApplicationContext(),MyPref.PREF_BUS_UKEY,"").equals(""))
        {
            MyPref.setPref(getApplicationContext(), MyPref.PREF_BUS_UKEY, "");
            MyPref.setPref(getApplicationContext(), MyPref.RETURN_UKEY, "");
            MyPref.setPref(getApplicationContext(), MyPref.ASID, "");
        }*/
        agent = new Agent();
        searchBusDetails = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEARCH_BUS);
        availableBuses = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.BUS);

        //availableReturnBuses = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.RETURN_BUS);
        availableReturnBuses=searchBusDetails.getAvailableReturnBuses();

        sKey = getIntent().getExtras().getString(AppConstants.IntentKeys.KEY);
        //sBusId = availableBuses.getsBusID();

        /*if(AppConstants.Keys.isReturnBus&& !searchBusDetails.getsBoarding().equals("")){*/
        if(searchBusDetails.isRetrunActivityCalled()){
            textViewBusRoute.setText(availableReturnBuses.getBus_name());
            textViewBusName.setText(availableReturnBuses.getBus_name());
            textViewType.setText(availableReturnBuses.getBus_type() + " | " + availableReturnBuses.getDep_time());
      /*  textViewArrivingTime.setText(passengerBus.getsArrival().toString().replaceAll("Hrs", ""));
        textViewDepartureTime.setText(passengerBus.getsDeparture().replaceAll("Hrs", ""));*/
            textViewBusType.setText(availableReturnBuses.getBus_type());
            textViewDetails.setText(availableReturnBuses.getBus_type());
            textViewJourneyTime.setText("\u2022 " + availableReturnBuses.getJourney_duration());

//        textViewHeaderTitle.setText(searchBusDetails.getsFrom() + " - " + searchBusDetails.getsTo());
            textViewHeaderTitle.setText(availableReturnBuses.getBus_name());
            searchBusDetails.setArrayListReturn(arrayListReturnSeat);
        }else {


            textViewBusRoute.setText(availableBuses.getBus_name());
            textViewBusName.setText(availableBuses.getBus_name());
            textViewType.setText(availableBuses.getBus_type() + " | " + availableBuses.getDep_time());
      /*  textViewArrivingTime.setText(passengerBus.getsArrival().toString().replaceAll("Hrs", ""));
        textViewDepartureTime.setText(passengerBus.getsDeparture().replaceAll("Hrs", ""));*/
            textViewBusType.setText(availableBuses.getBus_type());
            textViewDetails.setText(availableBuses.getBus_type());
            textViewJourneyTime.setText("\u2022 " + availableBuses.getJourney_duration());

//        textViewHeaderTitle.setText(searchBusDetails.getsFrom() + " - " + searchBusDetails.getsTo());
            textViewHeaderTitle.setText(availableBuses.getBus_name());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        String sDate = "";
        String sTime = "";
        try {
            date = simpleDateFormat.parse(searchBusDetails.getsDate());
            simpleDateFormat = new SimpleDateFormat("E,  dd-MMM-yyyy");
            sDate = simpleDateFormat.format(date);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        textViewDate.setText(sDateLabel + " : " + sDate);
         getSeats();

       // onSeats();
    }

    private void onSeats() {
        seatsArrayList = new ArrayList<>();
        for (int k = 1; k < 35; k++) {

            Seat seat = new Seat("" + k, AppConstants.Status.AVAILABLE);
            seatsArrayList.add(seat);
            GridLayoutManager manager = new GridLayoutManager(this, 5);//6
            recyclerViewSeats.setLayoutManager(manager);
          //  seatAdapter = new SeatAdapter(this, seatsArrayList, this, searchBusDetails, passengerBus, sKey, seatMap);
            recyclerViewSeats.setAdapter(seatAdapter);
        }
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


    @OnClick(R.id.textViewBook)
    void onBook() {

       /* if (hashMapSelectedSeats.size() == 0&& hashMapSelectedReturnSeats.size()==0) {
            Toast.makeText(this, "Please select your seat", Toast.LENGTH_SHORT).show();
        } else {*/
            if (!isRetrun) {
                if(arrayListOnwordSeats!=null) {
                    searchBusDetails.setArrayListOnWords(arrayListOnwordSeats);
                    // Toast.makeText(this, "bording point = "+searchBusDetails.getsBoarding(), Toast.LENGTH_SHORT).show();
                    //   Toast.makeText(this, "onwrord arra list  = "+searchBusDetails.getArrayListOnWords().size(), Toast.LENGTH_SHORT).show();
                    if (searchBusDetails.getsBoarding().equals("")) {
                        Intent intent = new Intent(this, BoardingPointActivity.class);
                        intent.putExtra("KEYRE", "1");
                        intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                        intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                        intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                        intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                        intent.putExtra(AppConstants.IntentKeys.SELECTED_RETURN_SEAT, hashMapSelectedReturnSeats);
                        intent.putExtra(AppConstants.IntentKeys.FARE, sFareAmount);
                        intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                       /* intent.putExtra("boardigPoint", availableBuses.getStrBoardingPoints());
                        intent.putExtra("dropingPoint", availableBuses.getStrDropingPoints());*/
                        startActivity(intent);
                    } else {
                        //  Toast.makeText(this, ""+searchBusDetails.getsBoarding(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, PassengerDetailsActivity.class);
                        intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                        intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                        intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                        intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                        intent.putExtra(AppConstants.IntentKeys.SELECTED_RETURN_SEAT, hashMapSelectedReturnSeats);

                        intent.putExtra(AppConstants.IntentKeys.FARE, sFareAmount);
                        intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                        startActivity(intent);

                    }
                }else {
                    Toast.makeText(this, "Select Seat!!", Toast.LENGTH_SHORT).show();
                }
            }else {
                if(searchBusDetails.isRetrunActivityCalled()) {
                    if (searchBusDetails.getArrayListReturn().size() < searchBusDetails.getArrayListOnWords().size()) {
                        Toast.makeText(this, "Select One More Seat", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                searchBusDetails.setArrayListReturn(arrayListReturnSeat);
                if(MyPref.getPref(getApplicationContext(),MyPref.RETURN_UKEY,"").equals("")) {
                    if(arrayListOnwordSeats==null){
                        Toast.makeText(this, "Select Seat!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    searchBusDetails.setArrayListOnWords(arrayListOnwordSeats);


                }else {
                    if(arrayListReturnSeat==null){
                        Toast.makeText(this, "Select Seat!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    searchBusDetails.setArrayListReturn(arrayListReturnSeat);
                }
                if (searchBusDetails.getsReturnBoarding().equals("")) {
                    Intent intent = new Intent(this, BoardingPointActivity.class);
                    intent.putExtra("KEYRE", "1");
                    intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                    intent.putExtra(AppConstants.IntentKeys.RETURN_BUS, availableReturnBuses);

                    intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                    intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                    intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                    intent.putExtra(AppConstants.IntentKeys.SELECTED_RETURN_SEAT, hashMapSelectedReturnSeats);

                    intent.putExtra(AppConstants.IntentKeys.FARE, sFareAmount);
                    intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                  /*  intent.putExtra("boardigPoint", availableBuses.getStrBoardingPoints());
                    intent.putExtra("dropingPoint", availableBuses.getStrDropingPoints());*/
                    startActivity(intent);
                } else {
                    if(searchBusDetails.isRetrunActivityCalled()) {
/*
                        Toast.makeText(this, "" + searchBusDetails.getsReturnBoarding(), Toast.LENGTH_SHORT).show();
*/
                        Intent intent = new Intent(this, PassengerDetailsActivity.class);
                        intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                        intent.putExtra(AppConstants.IntentKeys.RETURN_BUS, availableReturnBuses);
                        intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                        intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                        intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                        intent.putExtra(AppConstants.IntentKeys.SELECTED_RETURN_SEAT, hashMapSelectedReturnSeats);

                        intent.putExtra(AppConstants.IntentKeys.FARE, sFareAmount);
                        intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                        startActivity(intent);
                    }else {
                       /* Toast.makeText(this, "REtrun activity called", Toast.LENGTH_SHORT).show();*/
                        Intent intent = new Intent(this, ReturnBusActivity.class);
                        intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                        intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                        //   intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                        intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                        intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
                        intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                        startActivity(intent);
                    }

                }
           // }
        }
    }

/*
    @Override
    public void onResponse(String sResponse) {
        Log.d("Log", "Response : " + sResponse);
        hideProgress();
        if (sResponse != null) {
            try {
                JSONObject jsonObject = new JSONObject(sResponse);
                int sStatus = jsonObject.getInt("Status");
                if (sStatus == AppConstants.Status.SUCCESS) {
                    String sMessage = jsonObject.getString("message");
                    seatsArrayList = new ArrayList<>();
                    String busid = jsonObject.getString("busid");
                    String route_id = jsonObject.getString("route_id");
                    String sub_route_id = jsonObject.getString("sub_route_id");
                    String bus_route_id = jsonObject.getString("bus_route_id");
                    String bus_route_schedule_id = jsonObject.getString("bus_route_schedule_id");
                    String fromID = jsonObject.getString("fromID");
                    String toID = jsonObject.getString("toID");
                    String journeyDate = jsonObject.getString("journeyDate");
                    String bus_route_seat_id = jsonObject.getString("bus_route_seat_id");
                    String TotalSeats = jsonObject.getString("TotalSeats");
                    String boarding_time = jsonObject.getString("boarding_time");
                    String sRowCount = jsonObject.getString("Seat_Row_count");
                    JSONArray jsonArraySeats = jsonObject.getJSONArray("seatDetails");
                    String sAvailableSeats = null;
                    String sProcessingSeats = null;
                    int row = Integer.parseInt(sRowCount);
                    for (int i = 0; i < jsonArraySeats.length(); i++) {
                        JSONObject jsonObjectSeat = jsonArraySeats.getJSONObject(i);
                        sAvailableSeats = jsonObjectSeat.getString("available");
                        sProcessingSeats = jsonObjectSeat.getString("processing");
                        for (int j = 1; j <= row; j++) {
                            String sSeatRow = "SeatRow" + j;
                            String sSeatMap = jsonObjectSeat.getString(sSeatRow);
                            sSeats = sSeatMap.split(",");
//                            HashMap<String, ArrayList<String>> listHashMap = new HashMap<>();
//                            String[] occurences = {sSeatMap};
//                            listHashMap.put(sSeatRow, (ArrayList<String>) Arrays.asList(occurences));

                            if (!sProcessingSeats.equals("NULL")) {
                                if (sProcessingSeats.contains(",")) {
                                    sProcessingSeatsArray = sProcessingSeats.split(",");
                                    for (int k = 0; k < sProcessingSeatsArray.length; k++) {
                                        String sProcessingSeat = sProcessingSeatsArray[k];
                                        hashMapProcessingSeats.put(sProcessingSeat, AppConstants.Status.PROCESSING);
                                    }
                                }
                            }
                            if (!sAvailableSeats.equals("NULL")) {
                                if (sAvailableSeats.contains(",")) {
                                    sAvailableSeatsArray = sAvailableSeats.split(",");
                                    for (int k = 0; k < sAvailableSeatsArray.length; k++) {
                                        String sAvailableSeat = sAvailableSeatsArray[k];
                                        hashMapAvailableSeats.put(sAvailableSeat, AppConstants.Status.AVAILABLE);
                                    }
                                }
                            }
                            for (int k = 0; k < sSeats.length; k++) {
                                String sSeat = sSeats[k];
                                Seat seat;
                                if (hashMapProcessingSeats.containsKey(sSeat)) {
                                    seat = new Seat(sSeat, AppConstants.Status.PROCESSING);
                                } else if (hashMapAvailableSeats.containsKey(sSeat)) {
                                    seat = new Seat(sSeat, AppConstants.Status.AVAILABLE);
                                } else {
                                    seat = new Seat(sSeat, AppConstants.Status.BOOKED);
                                }
                                seatsArrayList.add(seat);
//                                Log.d("Log", "Seat No : " + sSeats[k]);
                            }
                        }
                    }

//                    seatMap = new SeatMap(busid, route_id, sub_route_id, bus_route_id, bus_route_schedule_id, fromID, toID, journeyDate, bus_route_seat_id, TotalSeats, boarding_time, sRowCount, sAvailableSeats, sProcessingSeats);
                    GridLayoutManager manager = new GridLayoutManager(this, 6);
                    recyclerViewSeats.setLayoutManager(manager);
                   // seatAdapter = new SeatAdapter(this, seatsArrayList, this, searchBusDetails, passengerBus, sKey, seatMap);
                    recyclerViewSeats.setAdapter(seatAdapter);
                } else {
                    Toast.makeText(this, something_went_wrong, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
*/

    private void getDetails() {
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
        sIMEI = telephonyInfo.getImsiSIM1();
        PackageInfo pInfo = null;
        String AndroidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
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
/*
    private void getSeats(){
        boolean isConnected = AppConstants.isConnected(this);
        if(isConnected){
            showProgress();
            sub_id=availableBuses.getSub_id();
            tdi_id=availableBuses.getTdi_id();
            lb_id=availableBuses.getLb_id();
            pbi_id=availableBuses.getPbi_id();
            asi_id=availableBuses.getAsi_id();

            Log.d("Log","asi "+asi_id);
            AuthKey=1;
            String urlKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"Get-5e@tM2p")));
            String apiKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,sub_id+tdi_id+lb_id+pbi_id+asi_id+sCompId+sAgentId+sIsFrom+"5e@Tm26")));

            Log.d("Log","sub" +sub_id);
            Log.d("Log","tid "+tdi_id);
            Log.d("Log","lbid "+lb_id);
            Log.d("Log","pbi "+pbi_id);
            Log.d("Log","AuthKey "+AuthKey);
            Log.d("Log","comp "+sCompId);
            Log.d("Log","agetid "+sAgentId);
            Log.d("Log","isfrom "+sIsFrom);

            Log.d("Log","url kdy "+urlKey);
            Log.d("Log","api key "+apiKey );

            OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
            Call<SeatsMap> getSeatMap=otappbusApiService.getSeats(urlKey,String.valueOf(AuthKey),sub_id,tdi_id,lb_id,pbi_id,asi_id,sCompId,sAgentId,sIsFrom,apiKey);
            getSeatMap.enqueue(new Callback<SeatsMap>() {
                @Override
                public void onResponse(Call<SeatsMap> call, Response<SeatsMap> response) {
                    hideProgress();
                    if (response.body() != null) {
                        if (response.body().status == AppConstants.Status.SUCCESS) {
                            JSONObject jsonObjectResponse = null;
                            try {
                                jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                                Log.d("Log", "Response 1 : " + jsonObjectResponse);
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            lowerSeatArrayList=response.body().lowerSeatMapArrayList;
                            sAvailableSets=response.body().getAvailable_seats();
                            sProcessSeats=response.body().getProcess_seats();
                            upperSeatArrayList=response.body().upperSeatMapArrayList;
                          //  seatAdapter = new SeatAdapter(SeatSelectionActivity.this, seatsArrayList, SeatSelectionActivity.this, searchBusDetails, availableBuses, sKey, seatMap);
                            GridLayoutManager manager = new GridLayoutManager(SeatSelectionActivity.this, 6);
                            recyclerViewSeats.setLayoutManager(manager);
                            seatAdapter= new SeatAdapter(getApplicationContext(),lowerSeatArrayList,upperSeatArrayList,sProcessSeats,sAvailableSets);
                            recyclerViewSeats.setAdapter(seatAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SeatsMap> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(getApplicationContext(), ""+t.getCause(), Toast.LENGTH_SHORT).show();

                }
            });


        }
    }
*/



/*HashMap<String, String> parameter = new HashMap<String, String>();
            parameter.put("owner_id", AppConstants.ApiNames.OWNER_ID);
            parameter.put("agent_id", agent.getsAgentId());
            parameter.put("auth_key", String.valueOf(AuthKey));
            parameter.put("from_id", sFromId);
            parameter.put("to_id", sToId);
            parameter.put("trvl_dt", sDate);
            parameter.put("bus_id", sBusId);
            parameter.put("ukey", sKey);
            parameter.put("imei", sIMEI);
            parameter.put("lat", sLatitude);
            parameter.put("long", sLongitude);
            parameter.put("ip", sIP);
            parameter.put("key", sApiKey);
            parameter.put("app_ver", sAppVersion);

            Log.d("Log", "Routes URL : " + sUrl);
            Log.d("Log", "owner_id : " + AppConstants.ApiNames.OWNER_ID);
            Log.d("Log", "agent_id : " + agent.getsAgentId());
            Log.d("Log", "AuthKey : " + String.valueOf(AuthKey));
            Log.d("Log", "FromId : " + sFromId);
            Log.d("Log", "ToId : " + sToId);
            Log.d("Log", "Date : " + sDate);
            Log.d("Log", "imei : " + sIMEI);
            Log.d("Log", "lat : " + sLatitude);
            Log.d("Log", "long : " + sLongitude);
            Log.d("Log", "ip : " + sIP);
            Log.d("Log", "key : " + sApiKey);
            Log.d("Log", "app_ver : " + sAppVersion);

            otappbusAsyncTask = new NagiAsyncTask(this, sUrl, Request.Method.POST, parameter, this);
            otappbusAsyncTask.execute();*//*

        } else {
            onResponseError(sNoInternet);
        }
    }
*/


    @Override
    protected void onPause() {
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
        progressDialog = new ProgressDialog(this);
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

/*    @Override
    public void onResponseError(String sResponse) {

    }*/

    @Override
    public void onSeatChanged(String sSeatNo, String sStatus, String sFare) {
        Log.d("Log","sSeatNo  "+sSeatNo);
        Log.d("Log","sStatus  "+sStatus);
        if (seatsArrayList != null) {
            for (int i = 0; i < seatsArrayList.size(); i++) {
                Seat seat = seatsArrayList.get(i);
                if (seat.getsSeatNo().equals(sSeatNo)) {
                    seatsArrayList.get(i).setsIsSelected(sStatus);
                    if (sStatus.equals(AppConstants.Status.AVAILABLE)) {
                        seatsArrayList.get(i).setsFare(sFare);
                    }
                    i = seatsArrayList.size();
                }
            }
        }
      //  Fare = 0;
        if(sFare!=null && sFare!="") {
            String fareAmountStringArray[] = sFare.split(" ");
            sFareAmount= fareAmountStringArray[1].trim();
            Log.d("Log","Fare 2 "+sFareAmount);
        }
        getSelectedSeats();
    }

    private void getSelectedSeats() {
        Seat seat = null;
        sSelectedSeats = "";
       /* hashMapSelectedSeats = new HashMap<>();
        hashMapSelectedReturnSeats = new HashMap<>();*/
       arrayListOnwordSeats=new ArrayList<>();
       arrayListReturnSeat= new ArrayList<>();
        for (int i = 0; i < seatsArrayList.size(); i++) {



            seat = seatsArrayList.get(i);
            if (seat.getsIsSelected() .equals(AppConstants.Status.SELECTED)) {

                if(MyPref.getPref(getApplicationContext(),MyPref.RETURN_UKEY,"").equals("")) {
                    arrayListOnwordSeats.add(seat);
                    //Toast.makeText(this, "onword size = "+arrayListOnwordSeats.size(), Toast.LENGTH_SHORT).show();
                    //Log.d("Log","onword siz="+arrayListOnwordSeats.size());
                }else {
                 //   Log.d("Log","onword size ="+searchBusDetails.getHashMapSelectedSeats().size());

                    arrayListReturnSeat.add(seat);
                   // hashMapSelectedReturnSeats.put(seat.getsSeatNo(),seat);
                }
            //    sSelectedSeats += "," + seat.getsSeatNo();

                String seatNameArray[]= seat.getsSeatNo().split("-");
                String seatName="";
                if(seatNameArray.length>0) {
                    seatName = seatNameArray[3];
                }
                sSelectedSeats+=","+seatName;
            }
        }
        searchBusDetails.setArrayListReturn(arrayListReturnSeat);
        updateFare();
    }

    private void updateFare() {
       // int count = hashMapSelectedSeats.size();
       // double dSingleFare = Double.parseDouble(passengerBus.getsFare().replaceAll(",", ""));
     //   Fare = count * dSingleFare;
//        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");
        String sTotalFare = String.valueOf(decimalFormat.format(Fare));
        if (sSelectedSeats.startsWith(",")) {
            sSelectedSeats = sSelectedSeats.substring(1, sSelectedSeats.length());
        }
        Log.d("Log","sSelected Seat "+sSelectedSeats);
       /* String seatNameArray[]= sSelectedSeats.split("-");
        String seatName="";
        if(seatNameArray.length>0) {
            seatName = seatNameArray[3];
        }
       */
        textViewSeatDetails.setText(sSelectedSeats);
        //textViewSeatDetails.setText(seatName);
        textViewTotalFare.setText("TSH " + sFareAmount);
        seatAdapter.notifyDataSetChanged();
    }
    @OnClick(R.id.linearLayoutTabSeats)
    void onUpperSelect(){
        Toast.makeText(SeatSelectionActivity.this, "Count : " + upperSeatsArrayList.size(), Toast.LENGTH_SHORT).show();
        manager = new GridLayoutManager(SeatSelectionActivity.this, upperSeatRow);
        recyclerViewUpperSeats.setLayoutManager(manager);
        upperSeatAdapter = new SeatAdapter(SeatSelectionActivity.this, upperSeatsArrayList, SeatSelectionActivity.this, searchBusDetails, availableBuses, sUKey, seatMap,sSeatTypeArray);
        recyclerViewUpperSeats.setAdapter(upperSeatAdapter);
    }


    private void getSeats() {  //  Get Standing Route Details
        boolean isConnected = AppConstants.isConnected(this);
        if (isConnected) {
            showProgress();
          //  if(AppConstants.Keys.isReturnBus && !searchBusDetails.getsBoarding().equals("")){
            if(searchBusDetails.isRetrunActivityCalled()||availableReturnBuses!=null){
                sub_id = availableReturnBuses.getSub_id();
                tdi_id = availableReturnBuses.getTdi_id();
                lb_id = availableReturnBuses.getLb_id();
                pbi_id = availableReturnBuses.getPbi_id();
                asi_id = availableReturnBuses.getAsi_id();
            }else {
                sub_id = availableBuses.getSub_id();
                tdi_id = availableBuses.getTdi_id();
                lb_id = availableBuses.getLb_id();
                pbi_id = availableBuses.getPbi_id();
                asi_id = availableBuses.getAsi_id();
            }
            if(MyPref.getPref(getApplicationContext(),MyPref.ASID,"").equals("")) {
                MyPref.setPref(getApplicationContext(), MyPref.ASID, asi_id);
            }
            Log.d("Log","asi "+asi_id);
            AuthKey=1;
            String urlKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"Get-5e@tM2p")));
            String apiKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,sub_id+tdi_id+lb_id+pbi_id+asi_id+sCompId+sAgentId+sIsFrom+"5e@Tm26")));

            Log.d("Log","sub" +sub_id);
            Log.d("Log","tid "+tdi_id);
            Log.d("Log","lbid "+lb_id);
            Log.d("Log","pbi "+pbi_id);
            Log.d("Log","AuthKey "+AuthKey);
            Log.d("Log","comp "+sCompId);
            Log.d("Log","agetid "+sAgentId);
            Log.d("Log","isfrom "+sIsFrom);

            Log.d("Log","url kdy "+urlKey);
            Log.d("Log","api key "+apiKey );

            OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
            Call<JsonObject> getSeatMap=otappbusApiService.getSeats(urlKey,String.valueOf(AuthKey),sub_id,tdi_id,lb_id,pbi_id,asi_id,sCompId,sAgentId,sIsFrom,apiKey);


            getSeatMap.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("Log", "Seat Response : " + response.body().toString());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int Status = jsonObject.getInt("status");
                        int seatRow = 0;
                         upperSeatRow = 0;
                        seatMap = new SeatMap();
                        if (Status == AppConstants.Status.SUCCESS) {
                            String sNoOfLowerSeatMapRow = jsonObject.getString("no_of_lower_seat_map_row");
                            String sNoOfUpperSeatMapRow = jsonObject.getString("no_of_upper_seat_map_row");
                            int lowerCount = Integer.parseInt(sNoOfLowerSeatMapRow);
                            int upperCount = Integer.parseInt(sNoOfUpperSeatMapRow);
                            if (upperCount != 0) {
                                isUpper = true;
                              //  linearLayoutSeatsOptions.setVisibility(View.VISIBLE);
                                linearLayoutTabSeats.setVisibility(View.VISIBLE);

                            }
                            seatsArrayList = new ArrayList<>();
                            upperSeatsArrayList = new ArrayList<>();
//                            seatMap = response.body();
                            try {
                                JSONArray jsonArrayLowerSeats = jsonObject.getJSONArray("lower_seat_map");
                                JSONArray jsonArrayUpperSeats = jsonObject.getJSONArray("upper_seat_map");
                                JSONArray jsonArraySeatType = jsonObject.getJSONArray("seat_types");
                                Log.d("Log", "Response : " + jsonObject);
                                String sProcessSeats = jsonObject.getString("process_seats");
                                String sAvailableSeats = jsonObject.getString("available_seats");
                                String isAllowedDifferRetrunPassenger= jsonObject.getString("is_allowrd_diff_no_of_seat_on_return");
                                //MyPref.setPref(getApplicationContext(), MyPref.PREF_DIFF_RETURN_PASSNR, isAllowedDifferRetrunPassenger);
                                searchBusDetails.setDiffNoOfSeatAllowed(isAllowedDifferRetrunPassenger);
                                sUKey = jsonObject.getString("ukey");
                                String oldUKEy=MyPref.getPref(getApplicationContext(),MyPref.PREF_BUS_UKEY,"");
                                Log.d("Log", " old Ukey "+oldUKEy);
                               /* if( oldUKEy.equals("")){
                                    MyPref.setPref(getApplicationContext(), MyPref.PREF_BUS_UKEY, sUKey);
                                    Log.d("Log", " old Ukey "+MyPref.getPref(getApplicationContext(),MyPref.PREF_BUS_UKEY,""));
                                }else {*/
                                    if(searchBusDetails.isRetrunActivityCalled()) {
                                        if(!searchBusDetails.getsReturnBoarding().equals("")||searchBusDetails.getArrayListOnWords().size()>0) {
                                            MyPref.setPref(getApplicationContext(), MyPref.RETURN_UKEY, sUKey);
                                            Log.d("Log", " new Ukey " + sUKey);
                                        }
                                    }else {
                                        MyPref.setPref(getApplicationContext(), MyPref.PREF_BUS_UKEY, sUKey);
                                    }
                              //  }

                                String sMaxAllowedSeatsPerBooking = jsonObject.getString("max_allowed_seats_per_booking");
                                String sProcessingSeats = sProcessSeats;
                                for (int j = 0; j < lowerCount; j++) {
                                    JSONObject jsonObjectSeat = jsonArrayLowerSeats.getJSONObject(j);
                                    String sSeatRow = "SeatRow" + (j + 1);
                                    Log.d("Log", "SEATS ROW : " + jsonObjectSeat);
                                    String sSeatMap = jsonObjectSeat.getString(sSeatRow);
                                    sSeats = sSeatMap.split(",");
                                    seatRow = sSeats.length;

                                    if (!sProcessingSeats.equals("NULL")) {
                                        if (!sProcessingSeats.equals("")) {
                                            sProcessingSeatsArray = sProcessingSeats.split(",");
                                            for (int k = 0; k < sProcessingSeatsArray.length; k++) {
                                                String sProcessingSeat = sProcessingSeatsArray[k];
                                                hashMapProcessingSeats.put(sProcessingSeat, AppConstants.Status.PROCESSING);
                                            }
                                        }
                                    }
                                    if (!sAvailableSeats.equals("NULL")) {
                                        if (!sAvailableSeats.equals("")) {
                                            sAvailableSeatsArray = sAvailableSeats.split(",");
                                            for (int k = 0; k < sAvailableSeatsArray.length; k++) {
                                                String sAvailableSeat = sAvailableSeatsArray[k];
                                                hashMapAvailableSeats.put(sAvailableSeat, AppConstants.Status.AVAILABLE);
                                            }
                                        }
                                    }
                                    for (int k = 0; k < jsonArraySeatType.length(); k++) {
                                        JSONObject jsonObjectSeatType = jsonArraySeatType.getJSONObject(k);
                                        String sSeatTypeId = jsonObjectSeatType.getString("seat_type_id");
                                        String sSeatType = jsonObjectSeatType.getString("seat_type_name");
                                        String sSeats = jsonObjectSeatType.getString("seats");
                                        /*Log.d("Log","Seat Selection Acti");
                                        Log.d("Log","Seat no "+ sSeats);
                                        Log.d("Log","Seat Type id "+ sSeatTypeId);
                                        Log.d("Log","Seat Type Name "+ sSeatType);*/
                                        if (sSeats.contains(",")) {
                                            sSeatTypeArray = sSeats.split(",");
                                          //  Log.d("Log","hash size = "+sSeatTypeArray.length);
                                            for (int l = 0; l < sSeatTypeArray.length; l++) {
                                              //  String sSeatTYpe = sSeatTypeArray[k];
                                                String sSeatTYpe = sSeatTypeArray[l];
                                           //     Log.d("Log","add hash item = "+sSeatTypeArray[l]);

                                                //SeatType seatType =  new SeatType(sSeatTypeId, sSeatType);
                                                SeatType seatType =  new SeatType(sSeatType,sSeatTypeId);
                                                hashMapSeatsTypes.put(sSeatTYpe, seatType);
                                            }
                                        }
                                    }

                                    for (int k = 0; k < sSeats.length; k++) {
                                        String sSeat = sSeats[k];
                                        Seat seat;
                                        if (hashMapProcessingSeats.containsKey(sSeat)) {
                                            seat = new Seat(sSeat, AppConstants.Status.PROCESSING);
                                        } else if (hashMapAvailableSeats.containsKey(sSeat)) {
                                            seat = new Seat(sSeat, AppConstants.Status.AVAILABLE);
                                        } else {
                                            seat = new Seat(sSeat, AppConstants.Status.BOOKED);
                                        }
                                        if (hashMapSeatsTypes.containsKey(sSeat)) {
                                         //   Log.d("Log","hash seat "+sSeat);
                                            SeatType seatType = hashMapSeatsTypes.get(sSeat);
                                            seat.setsType(seatType.getsType());
                                            seat.setsTypeId(seatType.getsTypeId());
                                        }
                                        seatsArrayList.add(seat);
                                    }

                                }


                                for (int j = 0; j < upperCount; j++) {
                                    JSONObject jsonObjectUpperSeat = jsonArrayUpperSeats.getJSONObject(j);
                                    String sSeatRow = "SeatRow" + (j + 1);
                                    //Log.d("Log", "SEATS ROW : " + jsonObjectUpperSeat);
                                    String sSeatMap = jsonObjectUpperSeat.getString(sSeatRow);
                                    sSeats = sSeatMap.split(",");
                                    upperSeatRow = sSeats.length;

                                    if (!sProcessingSeats.equals("NULL")) {
                                        if (!sProcessingSeats.equals("")) {
                                            sProcessingSeatsArray = sProcessingSeats.split(",");
                                            for (int k = 0; k < sProcessingSeatsArray.length; k++) {
                                                String sProcessingSeat = sProcessingSeatsArray[k];
                                                hashMapProcessingSeats.put(sProcessingSeat, AppConstants.Status.PROCESSING);
                                            }
                                        }
                                    }
                                    if (!sAvailableSeats.equals("NULL")) {
                                        if (!sAvailableSeats.equals("")) {
                                            sAvailableSeatsArray = sAvailableSeats.split(",");
                                            for (int k = 0; k < sAvailableSeatsArray.length; k++) {
                                                String sAvailableSeat = sAvailableSeatsArray[k];
                                                hashMapAvailableSeats.put(sAvailableSeat, AppConstants.Status.AVAILABLE);
                                            }
                                        }
                                    }
                                    for (int k = 0; k < jsonArraySeatType.length(); k++) {
                                        JSONObject jsonObjectSeatType = jsonArraySeatType.getJSONObject(k);
                                        String sSeatTypeId = jsonObjectSeatType.getString("seat_type_id");
                                        String sSeatType = jsonObjectSeatType.getString("seat_type_name");
                                        String sSeats = jsonObjectSeatType.getString("seats");
                                        if (sSeats.contains(",")) {
                                            sSeatTypeArray = sSeats.split(",");
                                            for (int l = 0; l < sSeatTypeArray.length; l++) {
                                                String sSeatTYpe = sSeatTypeArray[k];
                                               // SeatType seatType = new SeatType(sSeatTypeId, sSeatType);
                                                SeatType seatType = new SeatType(sSeatType,sSeatTypeId);
                                                hashMapSeatsTypes.put(sSeatTYpe, seatType);
                                            }
                                        }
                                    }

                                    for (int k = 0; k < sSeats.length; k++) {
                                        String sSeat = sSeats[k];
                                        Seat seat;
                                        if (hashMapProcessingSeats.containsKey(sSeat)) {
                                            seat = new Seat(sSeat, AppConstants.Status.PROCESSING);
                                        } else if (hashMapAvailableSeats.containsKey(sSeat)) {
                                            seat = new Seat(sSeat, AppConstants.Status.AVAILABLE);
                                        } else {
                                            seat = new Seat(sSeat, AppConstants.Status.BOOKED);
                                        }
                                        if (hashMapSeatsTypes.containsKey(sSeat)) {
                                            SeatType seatType = hashMapSeatsTypes.get(sSeat);
                                            seat.setsType(seatType.getsType());
                                            seat.setsTypeId(seatType.getsTypeId());
                                        }
                                        upperSeatsArrayList.add(seat);
                                    }

                                }

                                seatMap.setsAvailableSeats(sAvailableSeats);
                                seatMap.setsProcessingSeats(sProcessingSeats);
                            } catch (JSONException e) {
                                Toast.makeText(SeatSelectionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
//                            Toast.makeText(SeatSelectionActivity.this, "" + seatRow, Toast.LENGTH_SHORT).show();
                             manager = new GridLayoutManager(SeatSelectionActivity.this, seatRow);
                            recyclerViewSeats.setLayoutManager(manager);
                            String sSeatNo = seatsArrayList.get(0).sSeatNo;
                            String sStatus =seatsArrayList.get(0).sIsSelected;
                         //   if(AppConstants.Keys.isReturnBus && !searchBusDetails.getsBoarding().equals("")) {
                            if(searchBusDetails.isRetrunActivityCalled()){
                                seatAdapter = new SeatAdapter(SeatSelectionActivity.this, seatsArrayList, SeatSelectionActivity.this, searchBusDetails, availableReturnBuses, sUKey, seatMap,sSeatTypeArray);
                            }else {
                                seatAdapter = new SeatAdapter(SeatSelectionActivity.this, seatsArrayList, SeatSelectionActivity.this, searchBusDetails, availableBuses, sUKey, seatMap,sSeatTypeArray);
                            }
                            recyclerViewSeats.setAdapter(seatAdapter);

                           /* if (isUpper) {
                                Toast.makeText(SeatSelectionActivity.this, "Count : " + upperSeatsArrayList.size(), Toast.LENGTH_SHORT).show();
                                manager = new GridLayoutManager(SeatSelectionActivity.this, upperSeatRow);
                                recyclerViewUpperSeats.setLayoutManager(manager);
                                upperSeatAdapter = new SeatAdapter(SeatSelectionActivity.this, upperSeatsArrayList, SeatSelectionActivity.this, searchBusDetails, availableBuses, sUKey, seatMap);
                                recyclerViewUpperSeats.setAdapter(upperSeatAdapter);
                            }*/
                        } else {
                            String sMessage = jsonObject.getString("message");
                            Toast.makeText(SeatSelectionActivity.this, "Error : " + sMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    hideProgress();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(SeatSelectionActivity.this, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            /*HashMap<String, String> parameter = new HashMap<String, String>();
            parameter.put("owner_id", AppConstants.ApiNames.OWNER_ID);
            parameter.put("agent_id", agent.getsAgentId());
            parameter.put("auth_key", String.valueOf(AuthKey));
            parameter.put("from_id", sFromId);
            parameter.put("to_id", sToId);
            parameter.put("trvl_dt", sDate);
            parameter.put("bus_id", sBusId);
            parameter.put("ukey", sKey);
            parameter.put("imei", sIMEI);
            parameter.put("lat", sLatitude);
            parameter.put("long", sLongitude);
            parameter.put("ip", sIP);
            parameter.put("key", sApiKey);
            parameter.put("app_ver", sAppVersion);

            Log.d("Log", "Routes URL : " + sUrl);
            Log.d("Log", "owner_id : " + AppConstants.ApiNames.OWNER_ID);
            Log.d("Log", "agent_id : " + agent.getsAgentId());
            Log.d("Log", "AuthKey : " + String.valueOf(AuthKey));
            Log.d("Log", "FromId : " + sFromId);
            Log.d("Log", "ToId : " + sToId);
            Log.d("Log", "Date : " + sDate);
            Log.d("Log", "imei : " + sIMEI);
            Log.d("Log", "lat : " + sLatitude);
            Log.d("Log", "long : " + sLongitude);
            Log.d("Log", "ip : " + sIP);
            Log.d("Log", "key : " + sApiKey);
            Log.d("Log", "app_ver : " + sAppVersion);

            otappbusAsyncTask = new NagiAsyncTask(this, sUrl, Request.Method.POST, parameter, this);
            otappbusAsyncTask.execute();*/
        } else {
        //    onResponseError(sNoInternet);
        }
    }


}
