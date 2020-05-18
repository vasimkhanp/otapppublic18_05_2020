package com.otapp.net.Bus.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.Bus.Core.Agent;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.AvailableBuses;
import com.otapp.net.Bus.Core.BookTicketResponse;
import com.otapp.net.Bus.Core.GetFareResponse;
import com.otapp.net.Bus.Core.PassengerBus;
import com.otapp.net.Bus.Core.SHA;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.Bus.Core.Seat;
import com.otapp.net.Bus.Core.SeatMap;
import com.otapp.net.Bus.Core.Station;
import com.otapp.net.Bus.Core.TelephonyInfo;
import com.otapp.net.Bus.Network.OtappApiInstance;
import com.otapp.net.Bus.Network.OtappApiService;
import com.otapp.net.Bus.Network.OtappResponsePresenter;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;
import com.otapp.net.utils.MyPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class PassengerDetailsActivity extends AppCompatActivity implements OtappResponsePresenter {

    @BindView(R.id.listViewPassengers)
    ListView listViewPassengers;
    @BindView(R.id.textViewBookTicket)
    TextView textViewBookTicket;
    @BindView(R.id.textViewDetails)
    TextView textViewDetails;
    @BindView(R.id.textViewBus)
    TextView textViewBus;
    @BindView(R.id.textViewBusRoute)
    TextView textViewBusRoute;
    @BindView(R.id.spinnerCountryCode)
    Spinner spinnerCountryCode;
    @BindView(R.id.textViewCountryCode)
    TextView textViewCountryCode;
    @BindView(R.id.editTextPassengersPhoneNumber)
    EditText editTextPassengersPhoneNumber;
    @BindView(R.id.editTextPassengersEmail)
    EditText editTextPassengersEmail;
    @BindView(R.id.linearLayoutReturn)
    LinearLayout linearLayoutReturn;
    @BindView(R.id.textViewReturnBusRoute)
    TextView textViewReturnBusRoute;
    @BindView(R.id.textViewReturnDetails)
    TextView textViewReturnDetails;
    @BindView(R.id.textViewReturnBus)
    TextView textViewReturnBus;
    @BindView(R.id.textViewReturnTravelDate)
    TextView textViewReturnTravelDate;
    @BindView(R.id.textViewArrivalDate)
    TextView textViewArrivalDate;
    @BindView(R.id.textViewReturnSeatType)
    TextView textViewReturnSeatType;

    @BindView(R.id.linearLayoutPassenger1)
    LinearLayout linearLayoutPassenger1;
    @BindView(R.id.layoutOnworReturnSeat1)
    LinearLayout layoutOnwordReturnSeat1;
    @BindView(R.id.textViewOnwordSeatNo1)
    TextView textViewOnwordSeatNo1;
    @BindView(R.id.tvSeatIdOnword1)
    TextView textViewSeatIdOnword1;
    @BindView(R.id.textViewSeatDetails1)
    TextView textViewSeatDetails1;
    @BindView(R.id.textViewSeatNo1)
    TextView textViewSeatNo1;
    @BindView(R.id.editTextPassengersName)
    EditText editTextPassengersName;
    @BindView(R.id.editTextPassengersName1)
    EditText editTextPassengersName1;
    @BindView(R.id.editTextPassengersAge1)
    EditText editTextPassengersAge1;
    @BindView(R.id.editTextPassportNumber1)
    EditText editTextPassportNumber1;
    @BindView(R.id.radioGroupGender1)
    RadioGroup radioGroupGender1;
    @BindView(R.id.radioGroupCType1)
    RadioGroup radioGroupCType1;



    @BindView(R.id.linearLayoutPassenger2)
    LinearLayout linearLayoutPassenger2;
    @BindView(R.id.layoutOnworReturnSeat2)
    LinearLayout layoutOnwordReturnSeat2;
    @BindView(R.id.textViewOnwordSeatNo2)
    TextView textViewOnwordSeatNo2;
    @BindView(R.id.tvSeatIdOnword2)
    TextView textViewSeatIdOnword2;
    @BindView(R.id.textViewSeatDetails2)
    TextView textViewSeatDetails2;
    @BindView(R.id.textViewSeatNo2)
    TextView textViewSeatNo2;
    @BindView(R.id.editTextPassengersName2)
    EditText editTextPassengersName2;
    @BindView(R.id.editTextPassengersAge2)
    EditText editTextPassengersAge2;
    @BindView(R.id.editTextPassportNumber2)
    EditText editTextPassportNumber2;
    @BindView(R.id.radioGroupGender2)
    RadioGroup radioGroupGender2;
    @BindView(R.id.radioGroupCType2)
    RadioGroup radioGroupCType2;

    @BindView(R.id.linearLayoutPassenger3)
    LinearLayout linearLayoutPassenger3;
    @BindView(R.id.layoutOnworReturnSeat3)
    LinearLayout layoutOnwordReturnSeat3;
    @BindView(R.id.textViewOnwordSeatNo3)
    TextView textViewOnwordSeatNo3;
    @BindView(R.id.tvSeatIdOnword3)
    TextView textViewSeatIdOnword3;
    @BindView(R.id.textViewSeatDetails3)
    TextView textViewSeatDetails3;
    @BindView(R.id.textViewSeatNo3)
    TextView textViewSeatNo3;
    @BindView(R.id.editTextPassengersName3)
    EditText editTextPassengersName3;
    @BindView(R.id.editTextPassengersAge3)
    EditText editTextPassengersAge3;

    @BindView(R.id.editTextPassportNumber3)
    EditText editTextPassportNumber3;
    @BindView(R.id.radioGroupGender3)
    RadioGroup radioGroupGender3;
    @BindView(R.id.radioGroupCType3)
    RadioGroup radioGroupCType3;

    @BindView(R.id.linearLayoutPassenger4)
    LinearLayout linearLayoutPassenger4;
    @BindView(R.id.layoutOnworReturnSeat4)
    LinearLayout layoutOnwordReturnSeat4;
    @BindView(R.id.textViewOnwordSeatNo4)
    TextView textViewOnwordSeatNo4;
    @BindView(R.id.tvSeatIdOnword4)
    TextView textViewSeatIdOnword4;
    @BindView(R.id.textViewSeatDetails4)
    TextView textViewSeatDetails4;
    @BindView(R.id.textViewSeatNo4)
    TextView textViewSeatNo4;
    @BindView(R.id.editTextPassengersName4)
    EditText editTextPassengersName4;
    @BindView(R.id.editTextPassengersAge4)
    EditText editTextPassengersAge4;
    @BindView(R.id.editTextPassportNumber4)
    EditText editTextPassportNumber4;
    @BindView(R.id.radioGroupGender4)
    RadioGroup radioGroupGender4;
    @BindView(R.id.radioGroupCType4)
    RadioGroup radioGroupCType4;

    @BindView(R.id.linearLayoutPassenger5)
    LinearLayout linearLayoutPassenger5;
    @BindView(R.id.layoutOnworReturnSeat5)
    LinearLayout layoutOnwordReturnSeat5;
    @BindView(R.id.textViewOnwordSeatNo5)
    TextView textViewOnwordSeatNo5;
    @BindView(R.id.tvSeatIdOnword5)
    TextView textViewSeatIdOnword5;
    @BindView(R.id.textViewSeatDetails5)
    TextView textViewSeatDetails5;
    @BindView(R.id.textViewSeatNo5)
    TextView textViewSeatNo5;
    @BindView(R.id.editTextPassengersName5)
    EditText editTextPassengersName5;
    @BindView(R.id.editTextPassengersAge5)
    EditText editTextPassengersAge5;
    @BindView(R.id.editTextPassportNumber5)
    EditText editTextPassportNumber5;
    @BindView(R.id.radioGroupGender5)
    RadioGroup radioGroupGender5;
    @BindView(R.id.radioGroupCType5)
    RadioGroup radioGroupCType5;

    @BindView(R.id.linearLayoutPassenger6)
    LinearLayout linearLayoutPassenger6;
    @BindView(R.id.layoutOnworReturnSeat6)
    LinearLayout layoutOnwordReturnSeat6;
    @BindView(R.id.textViewOnwordSeatNo6)
    TextView textViewOnwordSeatNo6;
    @BindView(R.id.tvSeatIdOnword6)
    TextView textViewSeatIdOnword6;
    @BindView(R.id.textViewSeatDetails6)
    TextView textViewSeatDetails6;
    @BindView(R.id.textViewSeatNo6)
    TextView textViewSeatNo6;
    @BindView(R.id.editTextPassengersName6)
    EditText editTextPassengersName6;
    @BindView(R.id.editTextPassengersAge6)
    EditText editTextPassengersAge6;
    @BindView(R.id.editTextPassportNumber6)
    EditText editTextPassportNumber6;
    @BindView(R.id.radioGroupGender6)
    RadioGroup radioGroupGender6;
    @BindView(R.id.radioGroupCType6)
    RadioGroup radioGroupCType6;

    @BindView(R.id.linearLayoutPassengerReturn1)
    LinearLayout linearLayoutPassengerReturn1;
    @BindView(R.id.textViewSeatDetailsReturn1)
    TextView textViewSeatDetailsReturn1;
    @BindView(R.id.textViewSeatNoReturn1)
    TextView textViewSeatNoReturn1;
    @BindView(R.id.editTextPassengersNameReturn1)
    EditText editTextPassengersNameReturn1;
    @BindView(R.id.editTextPassengersAgeReturn1)
    EditText editTextPassengersAgeReturn1;
    @BindView(R.id.editTextPassportNumberReturn1)
    EditText editTextPassportNumberReturn1;
    @BindView(R.id.radioGroupGenderReturn1)
    RadioGroup radioGroupGenderReturn1;
    @BindView(R.id.radioGroupCTypeReturn1)
    RadioGroup radioGroupCTypeReturn1;

    @BindView(R.id.linearLayoutPassengerReturn2)
    LinearLayout linearLayoutPassengerReturn2;

    @BindView(R.id.textViewSeatNoReturn2)
    TextView textViewSeatNoReturn2;
    @BindView(R.id.editTextPassengersNameReturn2)
    EditText editTextPassengersNameReturn2;
    @BindView(R.id.editTextPassengersAgeReturn2)
    EditText editTextPassengersAgeReturn2;
    @BindView(R.id.editTextPassportNumberReturn2)
    EditText editTextPassportNumberReturn2;
    @BindView(R.id.radioGroupGenderReturn2)
    RadioGroup radioGroupGenderReturn2;
    @BindView(R.id.radioGroupCTypeReturn2)
    RadioGroup radioGroupCTypeReturn2;

    @BindView(R.id.linearLayoutPassengerReturn3)
    LinearLayout linearLayoutPassengerReturn3;

    @BindView(R.id.textViewSeatNoReturn3)
    TextView textViewSeatNoReturn3;
    @BindView(R.id.editTextPassengersNameReturn3)
    EditText editTextPassengersNameReturn3;
    @BindView(R.id.editTextPassengersAgeReturn3)
    EditText editTextPassengersAgeReturn3;
    @BindView(R.id.editTextPassportNumberReturn3)
    EditText editTextPassportNumberReturn3;
    @BindView(R.id.radioGroupGenderReturn3)
    RadioGroup radioGroupGenderReturn3;
    @BindView(R.id.radioGroupCTypeReturn3)
    RadioGroup radioGroupCTypeReturn3;

    @BindView(R.id.linearLayoutPassengerReturn4)
    LinearLayout linearLayoutPassengerReturn4;
    @BindView(R.id.textViewSeatNoReturn4)
    TextView textViewSeatNoReturn4;
    @BindView(R.id.editTextPassengersNameReturn4)
    EditText editTextPassengersNameReturn4;
    @BindView(R.id.editTextPassengersAgeReturn4)
    EditText editTextPassengersAgeReturn4;
    @BindView(R.id.editTextPassportNumberReturn4)
    EditText editTextPassportNumberReturn4;
    @BindView(R.id.radioGroupGenderReturn4)
    RadioGroup radioGroupGenderReturn4;
    @BindView(R.id.radioGroupCTypeReturn4)
    RadioGroup radioGroupCTypeReturn4;

    @BindView(R.id.linearLayoutPassengerReturn5)
    LinearLayout linearLayoutPassengerReturn5;

    @BindView(R.id.textViewSeatNoReturn5)
    TextView textViewSeatNoReturn5;
    @BindView(R.id.editTextPassengersNameReturn5)
    EditText editTextPassengersNameReturn5;
    @BindView(R.id.editTextPassengersAgeReturn5)
    EditText editTextPassengersAgeReturn5;
    @BindView(R.id.editTextPassportNumberReturn5)
    EditText editTextPassportNumberReturn5;
    @BindView(R.id.radioGroupGenderReturn5)
    RadioGroup radioGroupGenderReturn5;
    @BindView(R.id.radioGroupCTypeReturn5)
    RadioGroup radioGroupCTypeReturn5;

    @BindView(R.id.linearLayoutPassengerReturn6)
    LinearLayout linearLayoutPassengerReturn6;

    @BindView(R.id.textViewSeatNoReturn6)
    TextView textViewSeatNoReturn6;
    @BindView(R.id.editTextPassengersNameReturn6)
    EditText editTextPassengersNameReturn6;
    @BindView(R.id.editTextPassengersAgeReturn6)
    EditText editTextPassengersAgeReturn6;
    @BindView(R.id.editTextPassportNumberReturn6)
    EditText editTextPassportNumberReturn6;
    @BindView(R.id.radioGroupGenderReturn6)
    RadioGroup radioGroupGenderReturn6;
    @BindView(R.id.radioGroupCTypeReturn6)
    RadioGroup radioGroupCTypeReturn6;


    @BindView(R.id.tvSeatId1)
    TextView tvSeatId1;
    @BindView(R.id.tvSeatId2)
    TextView tvSeatId2;
    @BindView(R.id.tvSeatId3)
    TextView tvSeatId3;
    @BindView(R.id.tvSeatId4)
    TextView tvSeatId4;
    @BindView(R.id.tvSeatId5)
    TextView tvSeatId5;
    @BindView(R.id.tvSeatId6)
    TextView tvSeatId6;

    @BindView(R.id.tvSeatIdReturn1)
    TextView tvSeatIdReturn1;
    @BindView(R.id.tvSeatIdReturn2)
    TextView tvSeatIdReturn2;
    @BindView(R.id.tvSeatIdReturn3)
    TextView tvSeatIdReturn3;
    @BindView(R.id.tvSeatIdReturn4)
    TextView tvSeatIdReturn4;
    @BindView(R.id.tvSeatIdReturn5)
    TextView tvSeatIdReturn5;
    @BindView(R.id.tvSeatIdReturn6)
    TextView tvSeatIdReturn6;



    @BindString(R.string.please_enter_valid_transction_password)
    String sValidTransactionPassword;
    @BindString(R.string.voltage_error)
    String sVoltageError;
    @BindString(R.string.isprinter_operation)
    String sIsPrinterOperation;
    @BindString(R.string.noPaper)
    String sNoPaper;
    @BindString(R.string.low_power)
    String sLowPower;
    @BindString(R.string.no_reaction)
    String sNoReaction;
    @BindString(R.string.get_fail)
    String sGetFail;
    @BindString(R.string.enter_transaction_password)
    String sEnterTransctionPssword;
    @BindString(R.string.only_alphabets_are_allowed)
    String onlyAlphabetsAllowed;
    @BindString(R.string.loading)
    String loading;
    @BindString(R.string.enter_valid_phone_number)
    String sEnterValidMobileNumber;
    @BindString(R.string.enter_valid_age)
    String sValidAge;
    @BindString(R.string.enter_valid_email_id)
    String sValidEmailId;
    @BindString(R.string.no_internet_connection)
    String sNoInternetConnection;
    @BindString(R.string.enter_passenger_name)
    String sEnterPassengerName;
    @BindString(R.string.enter_passenger_passport_number)
    String enter_passenger_passport_number;

    @BindString(R.string.ticket_no_colon)
    String sTicketNoL;
    @BindString(R.string.traveldate)
    String sTravelDate;
    @BindString(R.string.departuretime)
    String sDepartureTimeLabel;
    @BindString(R.string.agent_name)
    String agent_name;
    @BindString(R.string.Bus_Name)
    String sBusNameL;
    @BindString(R.string.Bus_No)
    String Bus_No;
    @BindString(R.string.Single_fare)
    String single_fare;
    @BindString(R.string.total_fare_colon)
    String total_fare;
    @BindString(R.string.from)
    String from;
    @BindString(R.string.To)
    String to;
    @BindString(R.string.boarding)
    String boarding;
    @BindString(R.string.dropping)
    String dropping;
    @BindString(R.string.boarding_point)
    String boarding_point;
    @BindString(R.string.dropping_point)
    String dropping_point;
    @BindString(R.string.departure_bus)
    String departure_bus;
    @BindString(R.string.return_bus)
    String return_bus;
    @BindString(R.string.print_end_message)
    String print_end_message;
    @BindString(R.string.Number_of_passengers)
    String Number_of_passengers;
    @BindString(R.string.Passenger_Details)
    String Passenger_Details;
    @BindString(R.string.Seat_Numbers)
    String Seat_Numbers;
    @BindString(R.string.seat_No)
    String seat_no;
    @BindString(R.string.name)
    String name;

    private String sIP;
    private AvailableBuses passengerBus;
    private AvailableBuses departurePassengerBus;
    private AvailableBuses avilableReturnBuses;
    private SearchBusDetails searchBusDetails;
    private String sFromId;
    private String sToId;
    private String sFrom;
    private String sTo;
    //    @BindView(R.id.imageViewBack)
//    ImageView imageViewBack;
    private String sDate;
    private ProgressDialog progressDialog;
    private HashMap<String, Seat> hashMapSelectedSeats = new HashMap<>();
    private HashMap<String, Seat> hashMapSelectedReturnSeats = new HashMap<>();
    private ArrayList<Seat> arrayListOnWords= new ArrayList<>();
    private ArrayList<Seat> arrayListReturn= new ArrayList<>();
    private double Fare = 0;
    private String sCountryCode = "+248";
    private String sMobile = "";
    private String sEmail = "";
    private String sIMEI;
    private String sTicketNo;
    private int AuthKey;
    private String sAppVersion;
    private String sApiKey;
    private String sLanguage = "1";
    private Agent agent;
    private String sLatitude = "0.0";
    private String sLongitude = "0.0";

    private String sAsId = "";
    private String sTdId = "";
    private String sUKey = "";
    private String sJourneyDate = "";
    private String sBoardingId = "";
    private String sBoardingPoint = "";
    private String sDropingPoint = "";
    private String sDroppingId = "";
    private String sName = "";
    private String sGender = "";
    private String sPassengerDetails = "";
    private String sReturnPassengerDetails = "";
    private String sAgentId = "";
    private String sTransactionUser = "";
    private String sPassengerName = "";
    private String sAgentName = "";
    private String sSeats = "";
    private String sPassengerPhoneNo = "";
    private String sAppKey = "";


    private ArrayList<String> countryCodeArrayList = new ArrayList<>();
    private String sCopy = "SENDER COPY";
    private String sTransactionPassword = "";
    public int state;
    private Dialog dialogTransactionPassword;
    private RadioButton radioButtonPassenger1;
    private RadioButton radioButtonPassenger2;
    private RadioButton radioButtonPassenger3;
    private RadioButton radioButtonPassenger4;
    private RadioButton radioButtonPassenger5;
    private RadioButton radioButtonPassenger6;
    private RadioButton radioButtonCT1;
    private RadioButton radioButtonCT2;
    private RadioButton radioButtonCT3;
    private RadioButton radioButtonCT4;
    private RadioButton radioButtonCT5;
    private RadioButton radioButtonCT6;
    private RadioButton radioButtonPassengerReturn1;
    private RadioButton radioButtonPassengerReturn2;
    private RadioButton radioButtonPassengerReturn3;
    private RadioButton radioButtonPassengerReturn4;
    private RadioButton radioButtonPassengerReturn5;
    private RadioButton radioButtonPassengerReturn6;
    private RadioButton radioButtonCTReturn1;
    private RadioButton radioButtonCTReturn2;
    private RadioButton radioButtonCTReturn3;
    private RadioButton radioButtonCTReturn4;
    private RadioButton radioButtonCTReturn5;
    private RadioButton radioButtonCTReturn6;
    private int seatCount = 0,returnSeatCount=0;
    private String sPassengerName1 = "";
    private int sPassengerAge1;
    private String sPassengerGender1 = "";
    private String sPassengerPassport1 = "";

    private String sPassengerName2 = "";
    private int sPassengerAge2;
    private String sPassengerGender2 = "";
    private String sPassengerPassport2 = "";

    private String sPassengerName3 = "";
    private int sPassengerAge3;
    private String sPassengerGender3 = "";
    private String sPassengerPassport3 = "";

    private String sPassengerName4 = "";
    private int sPassengerAge4;
    private String sPassengerGender4 = "";
    private String sPassengerPassport4 = "";

    private String sPassengerName5 = "";
    private int sPassengerAge5;
    private String sPassengerGender5 = "";
    private String sPassengerPassport5 = "";

    private String sPassengerName6 = "";
    private int sPassengerAge6;
    private String sPassengerGender6 = "";
    private String sPassengerPassport6 = "";

    private String sPassengerNameReturn1 = "";
    private String sPassengerNameReturn2 = "";
    private String sPassengerNameReturn3 = "";
    private String sPassengerNameReturn4 = "";
    private String sPassengerNameReturn5 = "";
    private String sPassengerNameReturn6 = "";



    private LocationManager locationManager;
    private Bundle bundle;
    private SeatMap seatMap;
    private BookTicketResponse bookTicketResponse = null;
    private Station stationFrom;
    private Station stationTo;

    @BindString(R.string.Travel_date)
    String sDateLabel;
    @BindView(R.id.textViewSeatType)
    TextView textViewSeatType;

    @BindView(R.id.imageViewHeaderBack)
    ImageView imageViewHeaderBack;
    @BindView(R.id.textViewHeaderTitle)
    TextView textViewHeaderTitle;


    Seat seat;

    @OnClick(R.id.imageViewHeaderBack)
    void onBack() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details);
        ButterKnife.bind(this);
        bundle = savedInstanceState;
        agent = new Agent();
        textViewHeaderTitle.setText("Passenger Details");
//        getLocalIpAddress();
//        getDetails();
        searchBusDetails = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEARCH_BUS);
        if(!AppConstants.Keys.isReturnBus){
        passengerBus = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.BUS);
            Toast.makeText(this, "onward passenger details", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, ""+passengerBus.getBus_operator(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, ""+passengerBus.getBus_type(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, ""+passengerBus.getArrival_date()+passengerBus.getArrvl_time(), Toast.LENGTH_SHORT).show();
        }else {
            passengerBus=searchBusDetails.getAvailableBuses();
            Toast.makeText(this, "REtrun passenger details", Toast.LENGTH_SHORT).show();
        }
        avilableReturnBuses = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.RETURN_BUS);

        seatMap = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEAT_MAP);
        sUKey = getIntent().getExtras().getString(AppConstants.IntentKeys.KEY);
        hashMapSelectedSeats = (HashMap<String, Seat>) getIntent().getSerializableExtra(AppConstants.IntentKeys.SELECTED_SEAT);
        hashMapSelectedReturnSeats = (HashMap<String, Seat>) getIntent().getSerializableExtra(AppConstants.IntentKeys.SELECTED_RETURN_SEAT);
        arrayListOnWords=searchBusDetails.getArrayListOnWords();
        arrayListReturn=searchBusDetails.getArrayListReturn();
        Fare = getIntent().getExtras().getDouble(AppConstants.IntentKeys.FARE);
        stationFrom = searchBusDetails.getStationFrom();
        stationTo = searchBusDetails.getStationTo();

       /* hashMapSelectedReturnSeats=searchBusDetails.getHashMapSelectedReturnSeats();
        hashMapSelectedSeats= searchBusDetails.getHashMapSelectedSeats();*/
        //Log.d("Log","onword Tickets size "+hashMapSelectedSeats.size());
      //  Log.d("Log","return Tickets size "+hashMapSelectedReturnSeats.size());

//        Toast.makeText(this, "" + searchBusDetails.getStationFrom().getsName(), Toast.LENGTH_SHORT).show();

        if(searchBusDetails.getsBoarding().equals("")){
            searchBusDetails.setsBoarding(searchBusDetails.getsFrom());
        }
        if(searchBusDetails.getsDropping().equals("")){
            searchBusDetails.setsDropping(searchBusDetails.getsTo());
        }
        if(AppConstants.Keys.isReturnBus){
            /*if(searchBusDetails.getsReturnBoarding().equals("")){*/
                searchBusDetails.setsReturnBoarding(searchBusDetails.getsReturnFrom());
         /*   }*/
            if(searchBusDetails.getsReturnDropping().equals("")){
                searchBusDetails.setsReturnDropping(searchBusDetails.getsReturnTo());
            }
        }

        if (searchBusDetails.isReturn()) {
            linearLayoutReturn.setVisibility(View.VISIBLE);
           // linearLayoutPassengerReturn1.setVisibility(View.VISIBLE);
            departurePassengerBus = searchBusDetails.getAvailableBuses();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            Date returndate = null;
            String sDate = "";
            String sReturnDate = "";
            String sTime = "";
//            try {
//                date = simpleDateFormat.parse(departurePassengerBus.getsNewJourneyDate());
//                returndate = simpleDateFormat.parse(passengerBus.getsNewJourneyDate());
//                simpleDateFormat = new SimpleDateFormat("E,  dd-MMM-yyyy");
//                sDate = simpleDateFormat.format(date);
//                boolean isLangSelected = AppConstants.Preferences.getBooleanPreference(getApplicationContext(), AppConstants.Keys.LANGUAGE, false);
//                if (isLangSelected) {
//                    String sLanguage = AppConstants.Preferences.getStringPreference(getApplicationContext(), AppConstants.Keys.SELECTED_LANGUAGE);
//                    if (sLanguage != null) {
//                        if (sLanguage.equals("en")) {
//                            sDate = simpleDateFormat.format(date);
//                        } else {
//                            if (sDate.startsWith("Sun")) {
//                                sDate = sDate.replaceAll("Sun", "Jpili");
//                            } else if (sDate.startsWith("Mon")) {
//                                sDate = sDate.replaceAll("Mon", "Jtatu");
//                            } else if (sDate.startsWith("Tue")) {
//                                sDate = sDate.replaceAll("Tue", "Jnne");
//                            } else if (sDate.startsWith("Wed")) {
//                                sDate = sDate.replaceAll("Wed", "Jtano");
//                            } else if (sDate.startsWith("Thu")) {
//                                sDate = sDate.replaceAll("Thu", "Alh");
//                            } else if (sDate.startsWith("Fri")) {
//                                sDate = sDate.replaceAll("Fri", "Ijumaa");
//                            } else {
//                                sDate = sDate.replaceAll("Sat", "Jmos");
//                            }
//                        }
//                    }
//                } else {
//                    sDate = simpleDateFormat.format(date);
//                }
//                sReturnDate = simpleDateFormat.format(returndate);
//                if (isLangSelected) {
//                    String sLanguage = AppConstants.Preferences.getStringPreference(getApplicationContext(), AppConstants.Keys.SELECTED_LANGUAGE);
//                    if (sLanguage != null) {
//                        if (sLanguage.equals("en")) {
//                            sReturnDate = simpleDateFormat.format(returndate);
//                        } else {
//                            if (sReturnDate.startsWith("Sun")) {
//                                sReturnDate = sReturnDate.replaceAll("Sun", "Jpili");
//                            } else if (sReturnDate.startsWith("Mon")) {
//                                sReturnDate = sReturnDate.replaceAll("Mon", "Jtatu");
//                            } else if (sReturnDate.startsWith("Tue")) {
//                                sReturnDate = sReturnDate.replaceAll("Tue", "Jnne");
//                            } else if (sReturnDate.startsWith("Wed")) {
//                                sReturnDate = sReturnDate.replaceAll("Wed", "Jtano");
//                            } else if (sReturnDate.startsWith("Thu")) {
//                                sReturnDate = sReturnDate.replaceAll("Thu", "Alh");
//                            } else if (sReturnDate.startsWith("Fri")) {
//                                sReturnDate = sReturnDate.replaceAll("Fri", "Ijumaa");
//                            } else {
//                                sReturnDate = sReturnDate.replaceAll("Sat", "Jmos");
//                            }
//                        }
//                    }
//                } else {
//                    sReturnDate = simpleDateFormat.format(returndate);
//                }
//            } catch (ParseException e1) {
//                e1.printStackTrace();
//            }
            sFromId = searchBusDetails.getsReturnFromId();
            sToId = searchBusDetails.getsReturnToId();
            textViewBus.setText(passengerBus.getBus_name());
            Log.d("Log","To Stop"+searchBusDetails.getsReturnTo());
            Log.d("Log","From  Stop"+searchBusDetails.getsReturnFrom());

         //   textViewReturnTravelDate.setText(sDateLabel + " : " + sReturnDate + " | " + searchBusDetails.getPassengerBus().getsDeparture().replaceAll("Hrs", ""));
            if (searchBusDetails.getsReturnBoarding().equals("")) {
                textViewReturnDetails.setText(boarding_point + " : " + searchBusDetails.getsReturnFrom() + " | " + dropping_point + " : " + searchBusDetails.getsReturnTo());
            } else {
                textViewReturnDetails.setText(boarding_point + " : " + searchBusDetails.getsReturnBoarding() + " | " + dropping_point + " : " + searchBusDetails.getsReturnDropping());
            }
      //      textViewDate.setText(sDateLabel + " : " + sDate + " | " + departurePassengerBus.getsDeparture().replaceAll("Hrs", ""));
           // textViewBus.setText(departurePassengerBus.getBus_name());
            if(AppConstants.Keys.isReturnBus) {
                textViewReturnBus.setText(avilableReturnBuses.getBus_name());
                textViewReturnBusRoute.setText(searchBusDetails.getsReturnFrom() + " - " + searchBusDetails.getsReturnTo());
                textViewReturnDetails.setText(boarding_point + " : " + searchBusDetails.getsReturnBoarding() + " | " + dropping_point + " : " + searchBusDetails.getsReturnDropping());
                textViewReturnTravelDate.setText(avilableReturnBuses.getArrival_date()+" | "+avilableReturnBuses.getArrvl_time());
                textViewReturnSeatType.setText(avilableReturnBuses.getBus_type());
            }
            textViewBusRoute.setText(searchBusDetails.getsFrom() + " - " + searchBusDetails.getsTo());
            textViewSeatType.setText(passengerBus.getBus_type());
            textViewArrivalDate.setText(passengerBus.getArrival_date()+" | "+passengerBus.getArrvl_time());

            if (searchBusDetails.getsBoarding().equals("")) {
                textViewDetails.setText(boarding_point + " : " + searchBusDetails.getsFrom() + " | " + dropping_point + " : " + searchBusDetails.getsTo());
            } else {
                textViewDetails.setText(boarding_point + " : " + searchBusDetails.getsBoarding() + " | " + dropping_point + " : " + searchBusDetails.getsDropping());
            }
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            String sDate = "";
            String sTime = "";
/*
            try {
                date = simpleDateFormat.parse(passengerBus.getsNewJourneyDate());
                simpleDateFormat = new SimpleDateFormat("E,  dd-MMM-yyyy");
                sDate = simpleDateFormat.format(date);
                boolean isLangSelected = AppConstants.Preferences.getBooleanPreference(getApplicationContext(), AppConstants.Keys.LANGUAGE, false);
                if (isLangSelected) {
                    String sLanguage = AppConstants.Preferences.getStringPreference(getApplicationContext(), AppConstants.Keys.SELECTED_LANGUAGE);
                    if (sLanguage != null) {
                        if (sLanguage.equals("en")) {
                            sDate = simpleDateFormat.format(date);
                        } else {
                            if (sDate.startsWith("Sun")) {
                                sDate = sDate.replaceAll("Sun", "Jpili");
                            } else if (sDate.startsWith("Mon")) {
                                sDate = sDate.replaceAll("Mon", "Jtatu");
                            } else if (sDate.startsWith("Tue")) {
                                sDate = sDate.replaceAll("Tue", "Jnne");
                            } else if (sDate.startsWith("Wed")) {
                                sDate = sDate.replaceAll("Wed", "Jtano");
                            } else if (sDate.startsWith("Thu")) {
                                sDate = sDate.replaceAll("Thu", "Alh");
                            } else if (sDate.startsWith("Fri")) {
                                sDate = sDate.replaceAll("Fri", "Ijumaa");
                            } else {
                                sDate = sDate.replaceAll("Sat", "Jmos");
                            }
                        }
                    }
                } else {
                    sDate = simpleDateFormat.format(date);
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
*/
            sFromId = searchBusDetails.getsFromId();
            sToId = searchBusDetails.getsToId();
          //  textViewDate.setText(sDateLabel + " : " + sDate + " | " + passengerBus.getsDeparture().replaceAll("Hrs", ""));
            linearLayoutReturn.setVisibility(View.GONE);
            textViewSeatType.setText(passengerBus.getBus_type());
            textViewArrivalDate.setText(passengerBus.getArrival_date()+" | "+passengerBus.getArrvl_time());
            textViewBus.setText(passengerBus.getBus_name());
            textViewBusRoute.setText(searchBusDetails.getsFrom() + " - " + searchBusDetails.getsTo());
            textViewDetails.setText(boarding_point + " : " + searchBusDetails.getsBoarding() + " | " + dropping_point + " : " + searchBusDetails.getsDropping());
            if (searchBusDetails.getsBoarding().equals("")) {
                textViewDetails.setText(boarding_point + " : " + searchBusDetails.getsFrom() + " | " + dropping_point + " : " + searchBusDetails.getsTo());
            } else {
                textViewDetails.setText(boarding_point + " : " + searchBusDetails.getsBoarding() + " | " + dropping_point + " : " + searchBusDetails.getsDropping());
            }
        }
        countryCodeArrayList.add("+245");
        countryCodeArrayList.add("+248");


//        ArrayAdapter<CountryCode> senderCodeArrayAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_item, countryCodeArrayList);
//        spinnerCountryCode.setAdapter(senderCodeArrayAdapter);


        getSelectedSeats();
       // seatCount = hashMapSelectedSeats.size();
        seatCount= arrayListOnWords.size();
      //  Toast.makeText(this, "onwaed seat Count "+seatCount, Toast.LENGTH_SHORT).show();

        switch (seatCount) {
            case 1:
                hideVisibility();
                linearLayoutPassenger1.setVisibility(View.VISIBLE);
                if(searchBusDetails.getDiffNoOfSeatAllowed().equals("0")&&AppConstants.Keys.isReturnBus){
                    layoutOnwordReturnSeat1.setVisibility(View.VISIBLE);
                }
                editTextPassportNumber1.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListOnWords.size() > 0) {
/*
                    for (String sKey : hashMapSelectedSeats.keySet()) {
                        seat = hashMapSelectedSeats.get(sKey);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            //sSelectedSeats += "," + seat.getsSeatNo();

                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                           textViewSeatNo1.setText(seatName);
                            tvSeatId1.setText(seat.getsSeatNo());
                        }
                    }
*/
                    for(int i=0;i<arrayListOnWords.size();i++){
                        seat = arrayListOnWords.get(i);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if(searchBusDetails.getDiffNoOfSeatAllowed().equals("0")) {
                                textViewSeatNo1.setText("Depart "+seatName);
                            }else {
                                textViewSeatNo1.setText(seatName);
                            }
                            tvSeatId1.setText(seat.getsSeatNo());
                        }
                    }
                    if(AppConstants.Keys.isReturnBus) {
                        for (int i = 0; i < arrayListReturn.size(); i++) {
                            seat = arrayListReturn.get(i);
                            if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                                String seatNameArray[] = seat.getsSeatNo().split("-");
                                String seatName = "";
                                if (seatNameArray.length > 0) {
                                    seatName = seatNameArray[3];
                                }
                                textViewOnwordSeatNo1.setText("Return "+seatName);
                                textViewSeatIdOnword1.setText(seat.getsSeatNo());
                            }
                        }
                    }

                }

                break;
            case 2:
                hideVisibility();
                linearLayoutPassenger1.setVisibility(View.VISIBLE);
                linearLayoutPassenger2.setVisibility(View.VISIBLE);
                if(searchBusDetails.getDiffNoOfSeatAllowed().equals("0")&&AppConstants.Keys.isReturnBus){
                    layoutOnwordReturnSeat1.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat2.setVisibility(View.VISIBLE);
                }
              //  editTextPassportNumber2.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListOnWords.size() > 0) {
                    int i=1;
                    for (int j=0;j<arrayListOnWords.size();j++) {

                        seat = arrayListOnWords.get(j);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            //sSelectedSeats += "," + seat.getsSeatNo();

                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if(i==1){
                            textViewSeatNo1.setText(seatName);
                                tvSeatId1.setText(seat.getsSeatNo());
                            }else{
                                textViewSeatNo2.setText(seatName);
                                tvSeatId2.setText(seat.getsSeatNo());
                            }
                        }
                        i++;
                    }if(AppConstants.Keys.isReturnBus){
                        i=0;
                        for (int j=0;j<arrayListReturn.size();j++) {

                            seat = arrayListReturn.get(j);
                            if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                                //sSelectedSeats += "," + seat.getsSeatNo();

                                String seatNameArray[] = seat.getsSeatNo().split("-");
                                String seatName = "";
                                if (seatNameArray.length > 0) {
                                    seatName = seatNameArray[3];
                                }
                                if(i==1){
                                    textViewOnwordSeatNo1.setText(seatName);
                                    textViewSeatIdOnword1.setText(seat.getsSeatNo());
                                }else{
                                    textViewOnwordSeatNo2.setText(seatName);
                                    textViewSeatIdOnword2.setText(seat.getsSeatNo());
                                }
                            }
                            i++;
                        }
                    }
                }

                break;
            case 3:
                hideVisibility();
                linearLayoutPassenger1.setVisibility(View.VISIBLE);
                linearLayoutPassenger2.setVisibility(View.VISIBLE);
                linearLayoutPassenger3.setVisibility(View.VISIBLE);
                if(searchBusDetails.getDiffNoOfSeatAllowed().equals("0")&&AppConstants.Keys.isReturnBus){
                    layoutOnwordReturnSeat1.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat2.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat3.setVisibility(View.VISIBLE);
                }
                editTextPassportNumber3.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListOnWords.size() > 0) {
                    int i=1;
                    for (int j=0;j<arrayListOnWords.size();j++) {

                        seat = arrayListOnWords.get(j);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            //sSelectedSeats += "," + seat.getsSeatNo();

                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if(i==1){
                                textViewSeatNo1.setText(seatName);
                                tvSeatId1.setText(seat.getsSeatNo());
                            }else{
                                if(i==2) {
                                    textViewSeatNo2.setText(seatName);
                                    tvSeatId2.setText(seat.getsSeatNo());
                                }else {
                                    textViewSeatNo3.setText(seatName);
                                    tvSeatId3.setText(seat.getsSeatNo());
                                }
                            }
                        }
                        i++;
                    }if(AppConstants.Keys.isReturnBus){
                        i=0;
                        for (int j=0;j<arrayListReturn.size();j++) {

                            seat = arrayListReturn.get(j);
                            if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                                //sSelectedSeats += "," + seat.getsSeatNo();

                                String seatNameArray[] = seat.getsSeatNo().split("-");
                                String seatName = "";
                                if (seatNameArray.length > 0) {
                                    seatName = seatNameArray[3];
                                }
                                if(i==1){
                                    textViewOnwordSeatNo1.setText(seatName);
                                    textViewSeatIdOnword1.setText(seat.getsSeatNo());
                                }else{
                                    if(i==2) {
                                        textViewOnwordSeatNo2.setText(seatName);
                                        textViewSeatIdOnword2.setText(seat.getsSeatNo());
                                    }else {
                                        textViewOnwordSeatNo3.setText(seatName);
                                        textViewSeatIdOnword3.setText(seat.getsSeatNo());
                                    }
                                }
                            }
                            i++;
                        }
                    }
                }

                break;
            case 4:
                hideVisibility();
                linearLayoutPassenger1.setVisibility(View.VISIBLE);
                linearLayoutPassenger2.setVisibility(View.VISIBLE);
                linearLayoutPassenger3.setVisibility(View.VISIBLE);
                linearLayoutPassenger4.setVisibility(View.VISIBLE);
                if(searchBusDetails.getDiffNoOfSeatAllowed().equals("0")&&AppConstants.Keys.isReturnBus){
                    layoutOnwordReturnSeat1.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat2.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat3.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat4.setVisibility(View.VISIBLE);
                }
                editTextPassportNumber4.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListOnWords.size() > 0) {
                    int i=1;
                    for (int j=0;j<arrayListOnWords.size();j++) {

                        seat = arrayListOnWords.get(j);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            //sSelectedSeats += "," + seat.getsSeatNo();

                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if(i==1){
                                textViewSeatNo1.setText(seatName);
                                tvSeatId1.setText(seat.getsSeatNo());
                            }else{
                                if(i==2) {
                                    textViewSeatNo2.setText(seatName);
                                    tvSeatId2.setText(seat.getsSeatNo());
                                }else {if(i==3) {
                                    textViewSeatNo3.setText(seatName);
                                    tvSeatId3.setText(seat.getsSeatNo());
                                }else {
                                    textViewSeatNo4.setText(seatName);
                                    tvSeatId4.setText(seat.getsSeatNo());
                                }

                                }
                            }
                        }
                        i++;
                    }
                    if (AppConstants.Keys.isReturnBus) {
                        i=0;
                        for (int j=0;j<arrayListReturn.size();j++) {

                            seat = arrayListReturn.get(j);
                            if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                                //sSelectedSeats += "," + seat.getsSeatNo();

                                String seatNameArray[] = seat.getsSeatNo().split("-");
                                String seatName = "";
                                if (seatNameArray.length > 0) {
                                    seatName = seatNameArray[3];
                                }
                                if(i==1){

                                    textViewOnwordSeatNo1.setText(seatName);
                                    textViewSeatIdOnword1.setText(seat.getsSeatNo());
                                }else{
                                    if(i==2) {
                                        textViewOnwordSeatNo2.setText(seatName);
                                        textViewSeatIdOnword2.setText(seat.getsSeatNo());
                                    }else {if(i==3) {
                                        textViewOnwordSeatNo3.setText(seatName);
                                        textViewSeatIdOnword3.setText(seat.getsSeatNo());
                                    }else {
                                        textViewOnwordSeatNo4.setText(seatName);
                                        textViewSeatIdOnword4.setText(seat.getsSeatNo());
                                    }

                                    }
                                }
                            }
                            i++;
                        }


                    }

                }

                break;
            case 5:
                hideVisibility();
                linearLayoutPassenger1.setVisibility(View.VISIBLE);
                linearLayoutPassenger2.setVisibility(View.VISIBLE);
                linearLayoutPassenger3.setVisibility(View.VISIBLE);
                linearLayoutPassenger4.setVisibility(View.VISIBLE);
                linearLayoutPassenger5.setVisibility(View.VISIBLE);
                if(searchBusDetails.getDiffNoOfSeatAllowed().equals("0")&&AppConstants.Keys.isReturnBus){
                    layoutOnwordReturnSeat1.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat2.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat3.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat4.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat5.setVisibility(View.VISIBLE);
                }

                editTextPassportNumber5.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListOnWords.size() > 0) {
                    int i=1;
                    for (int j=0;j<arrayListOnWords.size();j++) {

                        seat = arrayListOnWords.get(j);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            //sSelectedSeats += "," + seat.getsSeatNo();

                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if(i==1){
                                textViewSeatNo1.setText(seatName);
                                tvSeatId1.setText(seat.getsSeatNo());
                            }else{
                                if(i==2) {
                                    textViewSeatNo2.setText(seatName);
                                    tvSeatId2.setText(seat.getsSeatNo());
                                }else {if(i==3) {
                                    textViewSeatNo3.setText(seatName);
                                    tvSeatId3.setText(seat.getsSeatNo());
                                }else {if(i==4) {
                                    textViewSeatNo4.setText(seatName);
                                    tvSeatId4.setText(seat.getsSeatNo());
                                }else {
                                    textViewSeatNo5.setText(seatName);
                                    tvSeatId5.setText(seat.getsSeatNo());
                                }
                                }

                                }
                            }
                        }
                        i++;
                    }if(AppConstants.Keys.isReturnBus){
                        i=0;
                        for (int j=0;j<arrayListReturn.size();j++) {

                            seat = arrayListReturn.get(j);
                            if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                                //sSelectedSeats += "," + seat.getsSeatNo();

                                String seatNameArray[] = seat.getsSeatNo().split("-");
                                String seatName = "";
                                if (seatNameArray.length > 0) {
                                    seatName = seatNameArray[3];
                                }
                                if(i==1){
                                    textViewOnwordSeatNo1.setText(seatName);
                                    textViewSeatIdOnword1.setText(seat.getsSeatNo());
                                }else{
                                    if(i==2) {
                                        textViewOnwordSeatNo2.setText(seatName);
                                        textViewSeatIdOnword2.setText(seat.getsSeatNo());
                                    }else {if(i==3) {
                                       textViewOnwordSeatNo3.setText(seatName);
                                       textViewSeatIdOnword3.setText(seat.getsSeatNo());
                                    }else {if(i==4) {
                                        textViewOnwordSeatNo4.setText(seatName);
                                        textViewSeatIdOnword4.setText(seat.getsSeatNo());
                                    }else {
                                        textViewOnwordSeatNo5.setText(seatName);
                                        textViewSeatIdOnword5.setText(seat.getsSeatNo());
                                    }
                                    }

                                    }
                                }
                            }
                            i++;
                        }

                    }
                }

                break;
            case 6:
                linearLayoutPassenger1.setVisibility(View.VISIBLE);
                linearLayoutPassenger2.setVisibility(View.VISIBLE);
                linearLayoutPassenger3.setVisibility(View.VISIBLE);
                linearLayoutPassenger4.setVisibility(View.VISIBLE);
                linearLayoutPassenger5.setVisibility(View.VISIBLE);
                linearLayoutPassenger6.setVisibility(View.VISIBLE);
                if(searchBusDetails.getDiffNoOfSeatAllowed().equals("0")&&AppConstants.Keys.isReturnBus){
                    layoutOnwordReturnSeat1.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat2.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat3.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat4.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat5.setVisibility(View.VISIBLE);
                    layoutOnwordReturnSeat6.setVisibility(View.VISIBLE);
                }

                editTextPassportNumber6.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListOnWords.size() > 0) {
                    int i=1;
                    for (int j=0;j<arrayListOnWords.size();j++) {

                        seat = arrayListOnWords.get(j);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            //sSelectedSeats += "," + seat.getsSeatNo();

                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if(i==1){
                                textViewSeatNo1.setText(seatName);
                                tvSeatId1.setText(seat.getsSeatNo());
                            }else{
                                if(i==2) {
                                    textViewSeatNo2.setText(seatName);
                                    tvSeatId2.setText(seat.getsSeatNo());
                                }else {if(i==3) {
                                    textViewSeatNo3.setText(seatName);
                                    tvSeatId3.setText(seat.getsSeatNo());
                                }else {if(i==4) {
                                    textViewSeatNo4.setText(seatName);
                                    tvSeatId4.setText(seat.getsSeatNo());
                                }else {if(i==5) {
                                    textViewSeatNo5.setText(seatName);
                                    tvSeatId5.setText(seat.getsSeatNo());
                                }else {
                                    textViewSeatNo6.setText(seatName);
                                    tvSeatId6.setText(seat.getsSeatNo());
                                }
                                }
                                }

                                }
                            }
                        }
                        i++;
                    }if(AppConstants.Keys.isReturnBus){
                        i=0;
                        for (int j=0;j<arrayListReturn.size();j++) {

                            seat = arrayListReturn.get(j);
                            if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                                //sSelectedSeats += "," + seat.getsSeatNo();

                                String seatNameArray[] = seat.getsSeatNo().split("-");
                                String seatName = "";
                                if (seatNameArray.length > 0) {
                                    seatName = seatNameArray[3];
                                }
                                if(i==1){
                                   textViewOnwordSeatNo1.setText(seatName);
                                   textViewSeatIdOnword1.setText(seat.getsSeatNo());
                                }else{
                                    if(i==2) {
                                        textViewOnwordSeatNo2.setText(seatName);
                                        textViewSeatIdOnword2.setText(seat.getsSeatNo());
                                    }else {if(i==3) {
                                        textViewOnwordSeatNo3.setText(seatName);
                                        textViewSeatIdOnword3.setText(seat.getsSeatNo());
                                    }else {if(i==4) {
                                        textViewOnwordSeatNo4.setText(seatName);
                                        textViewSeatIdOnword4.setText(seat.getsSeatNo());
                                    }else {if(i==5) {
                                        textViewOnwordSeatNo5.setText(seatName);
                                        textViewSeatIdOnword5.setText(seat.getsSeatNo());
                                    }else {
                                        textViewOnwordSeatNo6.setText(seatName);
                                        textViewSeatIdOnword6.setText(seat.getsSeatNo());
                                    }
                                    }
                                    }

                                    }
                                }
                            }
                            i++;
                        }

                    }
                }

                break;
        }

        if(searchBusDetails.getDiffNoOfSeatAllowed().equals("1")){
            Toast.makeText(this, "retrun Called", Toast.LENGTH_SHORT).show();
    if(AppConstants.Keys.isReturnBus) {
        returnSeatCount = arrayListReturn.size();
        switch (returnSeatCount) {
            case 1:
                hideVisibilityReturn();
                linearLayoutPassengerReturn1.setVisibility(View.VISIBLE);
                editTextPassportNumberReturn1.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListReturn.size() > 0) {

                    for (int i = 0; i < arrayListReturn.size(); i++) {
                        seat = arrayListReturn.get(i);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            textViewSeatNoReturn1.setText(seatName);
                            tvSeatIdReturn1.setText(seat.getsSeatNo());
                        }
                    }
                }

                break;
            case 2:
                hideVisibilityReturn();
                linearLayoutPassengerReturn1.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn1.setVisibility(View.VISIBLE);
                editTextPassportNumberReturn2.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListReturn.size() > 0) {
                    int i = 1;
                    for (int j = 0; j < arrayListReturn.size(); j++) {

                        seat = arrayListReturn.get(j);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            //sSelectedSeats += "," + seat.getsSeatNo();

                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if (i == 1) {
                                textViewSeatNoReturn1.setText(seatName);
                                tvSeatIdReturn1.setText(seat.getsSeatNo());
                            } else {
                                textViewSeatNoReturn2.setText(seatName);
                                tvSeatIdReturn2.setText(seat.getsSeatNo());
                            }
                        }
                        i++;
                    }
                }

                break;
            case 3:
                hideVisibilityReturn();
                linearLayoutPassengerReturn1.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn2.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn3.setVisibility(View.VISIBLE);
                editTextPassportNumberReturn3.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListReturn.size() > 0) {
                    int i = 1;
                    for (int j = 0; j < arrayListReturn.size(); j++) {

                        seat = arrayListReturn.get(j);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            //sSelectedSeats += "," + seat.getsSeatNo();

                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if (i == 1) {
                                textViewSeatNoReturn1.setText(seatName);
                                tvSeatIdReturn1.setText(seat.getsSeatNo());
                            } else {
                                if (i == 2) {
                                    textViewSeatNoReturn2.setText(seatName);
                                    tvSeatIdReturn2.setText(seat.getsSeatNo());
                                } else {
                                    textViewSeatNoReturn3.setText(seatName);
                                    tvSeatIdReturn3.setText(seat.getsSeatNo());
                                }
                            }
                        }
                        i++;
                    }
                }

                break;
            case 4:
                hideVisibility();
                linearLayoutPassengerReturn1.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn2.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn3.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn4.setVisibility(View.VISIBLE);
                editTextPassportNumberReturn4.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListReturn.size() > 0) {
                    int i = 1;
                    for (int j = 0; j < arrayListReturn.size(); j++) {

                        seat = arrayListReturn.get(j);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            //sSelectedSeats += "," + seat.getsSeatNo();

                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if (i == 1) {
                                textViewSeatNoReturn1.setText(seatName);
                                tvSeatIdReturn1.setText(seat.getsSeatNo());
                            } else {
                                if (i == 2) {
                                    textViewSeatNoReturn2.setText(seatName);
                                    tvSeatIdReturn2.setText(seat.getsSeatNo());
                                } else {
                                    if (i == 3) {
                                        textViewSeatNoReturn3.setText(seatName);
                                        tvSeatIdReturn3.setText(seat.getsSeatNo());
                                    } else {
                                        textViewSeatNoReturn4.setText(seatName);
                                        tvSeatIdReturn4.setText(seat.getsSeatNo());
                                    }

                                }
                            }
                        }
                        i++;
                    }
                }

                break;
            case 5:
                hideVisibility();
                linearLayoutPassengerReturn1.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn2.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn3.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn4.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn5.setVisibility(View.VISIBLE);
                editTextPassportNumberReturn5.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListReturn.size() > 0) {
                    int i = 1;
                    for (int j = 0; j < arrayListReturn.size(); j++) {

                        seat = arrayListReturn.get(j);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            //sSelectedSeats += "," + seat.getsSeatNo();

                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if (i == 1) {
                                textViewSeatNoReturn1.setText(seatName);
                                tvSeatIdReturn1.setText(seat.getsSeatNo());
                            } else {
                                if (i == 2) {
                                    textViewSeatNoReturn2.setText(seatName);
                                    tvSeatIdReturn2.setText(seat.getsSeatNo());
                                } else {
                                    if (i == 3) {
                                        textViewSeatNoReturn3.setText(seatName);
                                        tvSeatIdReturn3.setText(seat.getsSeatNo());
                                    } else {
                                        if (i == 4) {
                                            textViewSeatNoReturn4.setText(seatName);
                                            tvSeatIdReturn4.setText(seat.getsSeatNo());
                                        } else {
                                            textViewSeatNoReturn5.setText(seatName);
                                            tvSeatIdReturn5.setText(seat.getsSeatNo());
                                        }
                                    }

                                }
                            }
                        }
                        i++;
                    }
                }

                break;
            case 6:
                linearLayoutPassengerReturn1.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn2.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn3.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn4.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn5.setVisibility(View.VISIBLE);
                linearLayoutPassengerReturn6.setVisibility(View.VISIBLE);
                editTextPassportNumberReturn6.setImeOptions(EditorInfo.IME_ACTION_DONE);
                if (arrayListReturn.size() > 0) {
                    int i = 1;
                    for (int j = 0; j < arrayListReturn.size(); j++) {

                        seat = arrayListReturn.get(j);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            //sSelectedSeats += "," + seat.getsSeatNo();

                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if (i == 1) {
                                textViewSeatNoReturn1.setText(seatName);
                                tvSeatIdReturn1.setText(seat.getsSeatNo());
                            } else {
                                if (i == 2) {
                                    textViewSeatNoReturn2.setText(seatName);
                                    tvSeatIdReturn2.setText(seat.getsSeatNo());
                                } else {
                                    if (i == 3) {
                                        textViewSeatNoReturn3.setText(seatName);
                                        tvSeatIdReturn3.setText(seat.getsSeatNo());
                                    } else {
                                        if (i == 4) {
                                            textViewSeatNoReturn4.setText(seatName);
                                            tvSeatIdReturn4.setText(seat.getsSeatNo());
                                        } else {
                                            if (i == 5) {
                                                textViewSeatNoReturn5.setText(seatName);
                                                tvSeatIdReturn5.setText(seat.getsSeatNo());
                                            } else {
                                                textViewSeatNoReturn6.setText(seatName);
                                                tvSeatIdReturn6.setText(seat.getsSeatNo());
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        i++;
                    }
                }

                break;
        }
    }
}
        spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                CountryCode countryCode = countryCodeArrayList.get(position);
//                sCountryCode = countryCode.getCountryCode();
                sCountryCode = countryCodeArrayList.get(position);
//                Toast.makeText(PassengerDetailsActivity.this, "Country Code : " + sCountryCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*editTextPassengersName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextPassengersName1.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextPassengersName1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextPassengersName.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
//        textViewScreenTitle.requestFocus();
        editTextPassengersName.addTextChangedListener(textWatcher);
        editTextPassengersName1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isPassenger = true;
                isPassengerName = false;
                editTextPassengersName1.addTextChangedListener(textWatcher);
                return false;
            }
        });

        editTextPassengersName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isPassenger = false;
                isPassengerName = true;
                editTextPassengersName.addTextChangedListener(textWatcher);
                return false;
            }
        });
    }

    boolean isPassenger = false;
    boolean isPassengerName = true;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isPassenger) {
                editTextPassengersName.removeTextChangedListener(textWatcher);
                editTextPassengersName.setText(s.toString());
            }
            if (isPassengerName) {
                editTextPassengersName1.removeTextChangedListener(textWatcher);
                editTextPassengersName1.setText(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
//        getCurrentLocation();
    }




    private void getDetails() {
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
        sIMEI = telephonyInfo.getImsiSIM1();
        PackageInfo pInfo = null;
        String AndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
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

    private void hideVisibility() {
        linearLayoutPassenger1.setVisibility(View.GONE);
        linearLayoutPassenger2.setVisibility(View.GONE);
        linearLayoutPassenger3.setVisibility(View.GONE);
        linearLayoutPassenger4.setVisibility(View.GONE);
        linearLayoutPassenger5.setVisibility(View.GONE);
        linearLayoutPassenger6.setVisibility(View.GONE);
    }
    private void hideVisibilityReturn(){
        linearLayoutPassengerReturn1.setVisibility(View.GONE);
        linearLayoutPassengerReturn2.setVisibility(View.GONE);
        linearLayoutPassengerReturn3.setVisibility(View.GONE);
        linearLayoutPassengerReturn4.setVisibility(View.GONE);
        linearLayoutPassengerReturn5.setVisibility(View.GONE);
        linearLayoutPassengerReturn6.setVisibility(View.GONE);
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String mobile) {
        return android.util.Patterns.PHONE.matcher(mobile).matches();
    }

    /*private boolean checkPrinterStatus() {
        boolean isOK = false;
        CurrentPrinterStatusEnum statusPrinter = CurrentPrinterStatusEnum.fromCode(printer.getCurrentPrinterStatus());
        switch (statusPrinter) {
            case PRINTER_STATUS_OK:
                if (!printer.isPrinterOperation()) {
                    //Toast.makeText(getApplicationContext(), "isNotPrinterOperation", Toast.LENGTH_SHORT).show();
                    if (printer.voltageCheck()) {
                        isOK = true;
//                        printData(ticket);
                    } else {
                        //showError("VOLTAGE ERROR");
                        isOK = false;
                        Toast.makeText(PassengerDetailsActivity.this, sVoltageError, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // showError("Exist Operation");
                    isOK = false;
                    Toast.makeText(PassengerDetailsActivity.this, sIsPrinterOperation, Toast.LENGTH_SHORT).show();
                }
                break;
            case PRINTER_STATUS_NO_PAPER:
                //showError("PRINTER_STATUS_NO_PAPER");
                Toast.makeText(PassengerDetailsActivity.this, sNoPaper, Toast.LENGTH_SHORT).show();
                isOK = false;
                break;
            case PRINTER_STATUS_NO_REACTION:
                //showError("PRINTER_STATUS_NO_REACTION");
                Toast.makeText(PassengerDetailsActivity.this, sNoReaction, Toast.LENGTH_SHORT).show();
                isOK = false;
                break;
            case PRINTER_STATUS_GET_FAILE:
                //       showError("PPRINTER_STATUS_GET_FAILE");
                Toast.makeText(PassengerDetailsActivity.this, sGetFail, Toast.LENGTH_SHORT).show();
                isOK = false;
                break;
            case PRINTER_STATUS_LOW_POWER:
                //showError("PRINTER_STATUS_LOW_POWER");
                isOK = true;
                break;
        }
        return isOK;
    }*/


    @OnClick(R.id.textViewBookTicket)
    void onBook() {
        if (isValidInfo()) {
            getFare();
         //   success();
        }
    }
    public void getFare(){
        getPassengerDetails();
        if(AppConstants.Keys.isReturnBus && searchBusDetails.getDiffNoOfSeatAllowed().equals("1")){
            getReturnPassengerDetails();
        }
        boolean isConnected=AppConstants.isConnected(getApplicationContext());
        if (isConnected) {
            showProgress();
            Random r = new Random();
            int random = r.nextInt(4 - 1 + 1) + 1;
            if (random > 4 && random < 1) {
                random = 3;
            }
             AuthKey = Integer.parseInt(String.valueOf(random));
            String asid= MyPref.getPref(getApplicationContext(),MyPref.ASID,"");
            String ukey= MyPref.getPref(getApplicationContext(),MyPref.PREF_BUS_UKEY,"");
            String compId=com.otapp.net.utils.AppConstants.sCompId;
            String agentId=com.otapp.net.utils.AppConstants.sAgentId;
            String isFrom=com.otapp.net.utils.AppConstants.sIsFrom;
            String currency="1";
            String promoid="";
           // String retrunAsid=passengerBus.getAsi_id();
            String retrunAsid="";
            String retrunUkey="";
                    if(AppConstants.Keys.isReturnBus){
                        retrunAsid=avilableReturnBuses.getAsi_id();
                        retrunUkey=MyPref.getPref(getApplicationContext(),MyPref.RETURN_UKEY,"");
                    }
          /*  String retrunAsid="";
            String retrunUkey="";*/
            String postKey;
            String apiKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"Get-F2re")));
           /* if(AppConstants.Keys.isReturnBus){
                 postKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,asid+currency+retrunUkey+promoid+
                        compId+agentId+isFrom+"gEtF@r3")));
            }else {*/
                 postKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4, asid+retrunAsid + currency + ukey+retrunUkey + promoid +
                        compId + agentId + isFrom + "gEtF@r3")));
          /*  }*/
            Log.d("Log","asid "+asid);
            Log.d("Log","ukey "+ukey);
            Log.d("Log","compId "+compId);
            Log.d("Log","agentId "+agentId);
            Log.d("Log","isFrom "+isFrom);
            Log.d("Log","currency "+currency);
            Log.d("Log","promoid "+promoid);
            Log.d("Log","apiKey "+apiKey);
            Log.d("Log","postKey "+postKey);
            Log.d("Log","AuthKey "+AuthKey);
            Log.d("Log","retrunAsid "+retrunAsid);
            Log.d("Log","retrunUkey "+retrunUkey);

            OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
            Call<GetFareResponse> getFareResponseCall=otappbusApiService.getFare(apiKey,String.valueOf(AuthKey),asid,postKey,ukey,
                    promoid,compId,agentId,isFrom,currency,retrunAsid,retrunUkey);
            getFareResponseCall.enqueue(new Callback<GetFareResponse>() {
                @Override
                public void onResponse(Call<GetFareResponse> call, Response<GetFareResponse> response) {
                    hideProgress();
                    try {
                        JSONObject jsonObjectResponse = null;
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response : " + jsonObjectResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(response.body()!=null){
                        if(response.body().status==AppConstants.Status.SUCCESS){
                            Intent intent = new Intent(getApplicationContext(), BusBookingActivity.class);
                           // intent.putExtra("Json",response.body().toString());
                            intent.putParcelableArrayListExtra("fare",response.body().fareArrayList);
                            intent.putParcelableArrayListExtra("pgw",response.body().pgwsArrayList);
                            intent.putExtra("totalFare",response.body().promo_fare);
                           /* intent.putExtra("contactPersonName",editTextPassengersName.getText().toString());
                            intent.putExtra("contactPersonEmail",editTextPassengersEmail.getText().toString());
                            intent.putExtra("contactPersonPhone",textViewCountryCode.getText()+editTextPassengersPhoneNumber.getText().toString());*/
                           searchBusDetails.setsContactEmailAdd(editTextPassengersEmail.getText().toString());
                           searchBusDetails.setsContactPersonName(editTextPassengersName.getText().toString());
                           searchBusDetails.setsContactPhone(textViewCountryCode.getText()+editTextPassengersPhoneNumber.getText().toString());

                            intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
                            intent.putExtra(AppConstants.IntentKeys.RETURN_BUS,avilableReturnBuses);
                            intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                            intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                            intent.putExtra("PassegerDetails",sPassengerDetails);
                            intent.putExtra("ReturnPassegerDetails",sReturnPassengerDetails);

                            startActivity(intent);
                        }else{
                            Toast.makeText(PassengerDetailsActivity.this, ""+response.body().message, Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<GetFareResponse> call, Throwable t) {
                    hideProgress();
                }
            });

        }
    }

    //    @OnClick(R.id.textViewBookTicket)
    void getReturnPassengerDetails(){
        if(isValidInfo()) {
            String sSelectedSeats = "";
            int selectedId;
            int selectedCTId;

            sReturnPassengerDetails = "[";
            String sReturnPassengerDetails1 = "";
            String sReturnPassengerDetails2 = "";
            String sReturnPassengerDetails3 = "";
            String sReturnPassengerDetails4 = "";
            String sReturnPassengerDetails5 = "";
            String sReturnPassengerDetails6 = "";

            switch (returnSeatCount) {
                case 1:
                    sSelectedSeats = textViewSeatNoReturn1.getText().toString();
                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGenderReturn1.getCheckedRadioButtonId();
                    selectedCTId = radioGroupCTypeReturn1.getCheckedRadioButtonId();
                    radioButtonCTReturn1 = findViewById(selectedCTId);
                    radioButtonPassengerReturn1 = (RadioButton) findViewById(selectedId);


                  /*  sPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerName1 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT1.getText().toString()+ "\"," +
                          //  "\"mobile\": \"" + "" + "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" + tvSeatId1.getText().toString()  +"\""+
                            "}]";
                    Log.d("Log", "Passenger1 : " + sPassengerDetails1);
                    sPassengerDetails += sPassengerDetails1;*/

                    JSONObject passengers1 = new JSONObject();
                    try {
                        passengers1.put("name", sPassengerNameReturn1);
                        passengers1.put("gender", radioButtonPassengerReturn1.getText().toString());
                        passengers1.put("category", radioButtonCTReturn1.getText().toString());
                        passengers1.put("passport", sPassengerPassport1);
                        passengers1.put("seat_id", tvSeatIdReturn1.getText().toString());


                        JSONArray jsonArray = new JSONArray();

                        jsonArray.put(passengers1);
                    /*JSONObject passengersObj = new JSONObject();
                    passengersObj.put("passengers", jsonArray);*/
                        String jsonStr = jsonArray.toString();
                        sReturnPassengerDetails = jsonStr;
                        Log.d("Log", "Passenger1 : " + sReturnPassengerDetails);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    sSelectedSeats = textViewSeatNoReturn1.getText().toString() + ", " + textViewSeatNoReturn2.getText().toString();
                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGenderReturn1.getCheckedRadioButtonId();
                    radioButtonPassengerReturn1 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn2.getCheckedRadioButtonId();
                    radioButtonPassengerReturn2 = (RadioButton) findViewById(selectedId);

                    selectedCTId = radioGroupCTypeReturn1.getCheckedRadioButtonId();
                    radioButtonCT1 = findViewById(selectedCTId);
                    selectedCTId = radioGroupCTypeReturn2.getCheckedRadioButtonId();
                    radioButtonCT2 = findViewById(selectedCTId);
                    sReturnPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerNameReturn1 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn1.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn1.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger1 : " + sReturnPassengerDetails1);
                    if (sPassengerNameReturn2.equals("")) {
                        sPassengerNameReturn2 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge2 == 0) {
                        sPassengerAge2 = sPassengerAge1;
                    }
                    if (sPassengerPassport2.equals("")) {
                        sPassengerPassport2 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails2 = "{" +
                            "\"name\": \"" + sPassengerNameReturn2 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn2.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn2.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport2 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn2.getText().toString() + "\"" +
                            "}]";
                    Log.d("Log", "Passenger2 : " + sReturnPassengerDetails2);
                    sReturnPassengerDetails += sReturnPassengerDetails1 + sReturnPassengerDetails2;
                    break;
                case 3:
                    sSelectedSeats = textViewSeatNoReturn1.getText().toString() + ", " + textViewSeatNoReturn2.getText().toString() + ", " + textViewSeatNoReturn3.getText().toString();
                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGenderReturn1.getCheckedRadioButtonId();
                    radioButtonPassengerReturn1 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn2.getCheckedRadioButtonId();
                    radioButtonPassengerReturn2 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn3.getCheckedRadioButtonId();
                    radioButtonPassengerReturn3 = (RadioButton) findViewById(selectedId);

                    selectedCTId = radioGroupCTypeReturn1.getCheckedRadioButtonId();
                    radioButtonCTReturn1 = findViewById(selectedCTId);
                    selectedCTId = radioGroupCTypeReturn2.getCheckedRadioButtonId();
                    radioButtonCTReturn2 = findViewById(selectedCTId);
                    selectedCTId = radioGroupCTypeReturn3.getCheckedRadioButtonId();
                    radioButtonCTReturn3 = findViewById(selectedCTId);


                    sReturnPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerNameReturn1 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn1.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn1.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger1 : " + sReturnPassengerDetails1);
                    if (sPassengerNameReturn2.equals("")) {
                        sPassengerNameReturn2 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge2 == 0) {
                        sPassengerAge2 = sPassengerAge1;
                    }
                    if (sPassengerPassport2.equals("")) {
                        sPassengerPassport2 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails2 = "{" +
                            "\"name\": \"" + sPassengerNameReturn2 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn2.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn2.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport2 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn2.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger2 : " + sReturnPassengerDetails2);

                    if (sPassengerNameReturn3.equals("")) {
                        sPassengerNameReturn3 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge3 == 0) {
                        sPassengerAge3 = sPassengerAge1;
                    }
                    if (sPassengerPassport3.equals("")) {
                        sPassengerPassport3 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails3 = "{" +
                            "\"name\": \"" + sPassengerNameReturn3 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn3.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn3.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport3 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn3.getText().toString() + "\"" +
                            "}]";
                    Log.d("Log", "Passenger3 : " + sReturnPassengerDetails3);
                    sReturnPassengerDetails += sReturnPassengerDetails1 + sReturnPassengerDetails2 + sReturnPassengerDetails3;
                    break;
                case 4:
                    sSelectedSeats = textViewSeatNoReturn1.getText().toString() + ", " + textViewSeatNoReturn2.getText().toString() + ", " + textViewSeatNoReturn3.getText().toString() + ", " + textViewSeatNoReturn4.getText().toString();
                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGenderReturn1.getCheckedRadioButtonId();
                    radioButtonPassengerReturn1 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn2.getCheckedRadioButtonId();
                    radioButtonPassengerReturn2 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn3.getCheckedRadioButtonId();
                    radioButtonPassengerReturn3 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn4.getCheckedRadioButtonId();
                    radioButtonPassengerReturn4 = (RadioButton) findViewById(selectedId);

                    selectedCTId = radioGroupCTypeReturn1.getCheckedRadioButtonId();
                    radioButtonCTReturn1 = findViewById(selectedCTId);
                    selectedCTId = radioGroupCTypeReturn2.getCheckedRadioButtonId();
                    radioButtonCTReturn2 = findViewById(selectedCTId);
                    selectedCTId = radioGroupCTypeReturn3.getCheckedRadioButtonId();
                    radioButtonCTReturn3 = findViewById(selectedCTId);
                    selectedCTId = radioGroupCTypeReturn4.getCheckedRadioButtonId();
                    radioButtonCTReturn4 = findViewById(selectedCTId);


                    sReturnPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerNameReturn1 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn1.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn1.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger1 : " + sReturnPassengerDetails1);
                    if (sPassengerNameReturn2.equals("")) {
                        sPassengerNameReturn2 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge2 == 0) {
                        sPassengerAge2 = sPassengerAge1;
                    }
                    if (sPassengerPassport2.equals("")) {
                        sPassengerPassport2 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails2 = "{" +
                            "\"name\": \"" + sPassengerNameReturn2 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn2.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn2.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport2 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn2.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger2 : " + sReturnPassengerDetails2);

                    if (sPassengerNameReturn3.equals("")) {
                        sPassengerNameReturn3 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge3 == 0) {
                        sPassengerAge3 = sPassengerAge1;
                    }
                    if (sPassengerPassport3.equals("")) {
                        sPassengerPassport3 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails3 = "{" +
                            "\"name\": \"" + sPassengerNameReturn3 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn3.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn3.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport3 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn3.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger3 : " + sReturnPassengerDetails3);


                    if (sPassengerNameReturn4.equals("")) {
                        sPassengerNameReturn4 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge4 == 0) {
                        sPassengerAge4 = sPassengerAge1;
                    }
                    if (sPassengerPassport4.equals("")) {
                        sPassengerPassport4 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails4 = "{" +
                            "\"name\": \"" + sPassengerNameReturn4 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn4.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn4.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport4 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn4.getText().toString() + "\"" +
                            "}]";
                    Log.d("Log", "Passenger4 : " + sReturnPassengerDetails4);
                    sReturnPassengerDetails += sReturnPassengerDetails1 + sReturnPassengerDetails2 + sReturnPassengerDetails3 + sReturnPassengerDetails4;
                    break;
                case 5:
                    sSelectedSeats = textViewSeatNoReturn1.getText().toString() + ", " + textViewSeatNoReturn2.getText().toString() + ", "
                            + textViewSeatNoReturn3.getText().toString() + ", " + textViewSeatNoReturn4.getText().toString() + ", " + textViewSeatNoReturn5.getText().toString();

                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGenderReturn1.getCheckedRadioButtonId();
                    radioButtonPassengerReturn1 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn2.getCheckedRadioButtonId();
                    radioButtonPassengerReturn2 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn3.getCheckedRadioButtonId();
                    radioButtonPassengerReturn3 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn4.getCheckedRadioButtonId();
                    radioButtonPassengerReturn4 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn5.getCheckedRadioButtonId();
                    radioButtonPassengerReturn5 = (RadioButton) findViewById(selectedId);

                    sReturnPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerNameReturn1 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn1.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn1.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger1 : " + sReturnPassengerDetails1);
                    if (sPassengerNameReturn2.equals("")) {
                        sPassengerNameReturn2 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge2 == 0) {
                        sPassengerAge2 = sPassengerAge1;
                    }
                    if (sPassengerPassport2.equals("")) {
                        sPassengerPassport2 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails2 = "{" +
                            "\"name\": \"" + sPassengerNameReturn2 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn2.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn2.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport2 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn2.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger2 : " + sReturnPassengerDetails2);

                    if (sPassengerNameReturn3.equals("")) {
                        sPassengerNameReturn3 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge3 == 0) {
                        sPassengerAge3 = sPassengerAge1;
                    }
                    if (sPassengerPassport3.equals("")) {
                        sPassengerPassport3 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails3 = "{" +
                            "\"name\": \"" + sPassengerNameReturn3 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn3.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn3.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport3 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn3.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger3 : " + sReturnPassengerDetails3);


                    if (sPassengerNameReturn4.equals("")) {
                        sPassengerNameReturn4 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge4 == 0) {
                        sPassengerAge4 = sPassengerAge1;
                    }
                    if (sPassengerPassport4.equals("")) {
                        sPassengerPassport4 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails4 = "{" +
                            "\"name\": \"" + sPassengerNameReturn4 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn4.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn4.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport4 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn4.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger4 : " + sReturnPassengerDetails4);


                    if (sPassengerName5.equals("")) {
                        sPassengerNameReturn5 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge5 == 0) {
                        sPassengerAge5 = sPassengerAge1;
                    }
                    if (sPassengerPassport5.equals("")) {
                        sPassengerPassport5 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails5 = "{" +
                            "\"name\": \"" + sPassengerNameReturn5 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn5.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn5.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport5 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn5.getText().toString() + "\"" +
                            "}]";
                    Log.d("Log", "Passenger5 : " + sReturnPassengerDetails5);
                    sReturnPassengerDetails += sReturnPassengerDetails1 + sReturnPassengerDetails2 + sReturnPassengerDetails3 + sReturnPassengerDetails4 + sReturnPassengerDetails5;
                    break;
                case 6:
                    sSelectedSeats = textViewSeatNoReturn1.getText().toString() + ", " + textViewSeatNoReturn2.getText().toString() + ", "
                            + textViewSeatNoReturn3.getText().toString() + ", " + textViewSeatNoReturn4.getText().toString() + ", "
                            + textViewSeatNoReturn5.getText().toString() + ", " + textViewSeatNoReturn6.getText().toString();
                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGenderReturn1.getCheckedRadioButtonId();
                    radioButtonPassengerReturn1 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn2.getCheckedRadioButtonId();
                    radioButtonPassengerReturn2 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn3.getCheckedRadioButtonId();
                    radioButtonPassengerReturn3 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn4.getCheckedRadioButtonId();
                    radioButtonPassengerReturn4 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn5.getCheckedRadioButtonId();
                    radioButtonPassengerReturn5 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGenderReturn6.getCheckedRadioButtonId();
                    radioButtonPassengerReturn6 = (RadioButton) findViewById(selectedId);

                    sReturnPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerNameReturn1 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn1.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn1.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger1 : " + sReturnPassengerDetails1);
                    if (sPassengerName2.equals("")) {
                        sPassengerNameReturn2 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge2 == 0) {
                        sPassengerAge2 = sPassengerAge1;
                    }
                    if (sPassengerPassport2.equals("")) {
                        sPassengerPassport2 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails2 = "{" +
                            "\"name\": \"" + sPassengerNameReturn2 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn2.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn2.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport2 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn2.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger2 : " + sReturnPassengerDetails2);

                    if (sPassengerNameReturn3.equals("")) {
                        sPassengerNameReturn3 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge3 == 0) {
                        sPassengerAge3 = sPassengerAge1;
                    }
                    if (sPassengerPassport3.equals("")) {
                        sPassengerPassport3 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails3 = "{" +
                            "\"name\": \"" + sPassengerNameReturn3 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn3.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn3.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport3 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn3.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger3 : " + sReturnPassengerDetails3);

                    if (sPassengerNameReturn4.equals("")) {
                        sPassengerNameReturn4 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge4 == 0) {
                        sPassengerAge4 = sPassengerAge1;
                    }
                    if (sPassengerPassport4.equals("")) {
                        sPassengerPassport4 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails4 = "{" +
                            "\"name\": \"" + sPassengerNameReturn4 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn4.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn4.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport4 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn4.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger4 : " + sReturnPassengerDetails4);


                    if (sPassengerNameReturn5.equals("")) {
                        sPassengerNameReturn5 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge5 == 0) {
                        sPassengerAge5 = sPassengerAge1;
                    }
                    if (sPassengerPassport5.equals("")) {
                        sPassengerPassport5 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails5 = "{" +
                            "\"name\": \"" + sPassengerNameReturn5 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn5.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn5.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport5 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn5.getText().toString() + "\"" +
                            "},";
                    Log.d("Log", "Passenger5 : " + sReturnPassengerDetails5);

                    if (sPassengerNameReturn6.equals("")) {
                        sPassengerNameReturn6 = sPassengerNameReturn1;
                    }
                    if (sPassengerAge6 == 0) {
                        sPassengerAge6 = sPassengerAge1;
                    }
                    if (sPassengerPassport6.equals("")) {
                        sPassengerPassport6 = sPassengerPassport1;
                    }
                    sReturnPassengerDetails6 = "{" +
                            "\"name\": \"" + sPassengerNameReturn6 + "\"," +
                            "\"gender\": \"" + radioButtonPassengerReturn6.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCTReturn6.getText().toString() + "\"," +
                            "\"passport\": \"" + sPassengerPassport6 + "\"," +
                            "\"seat_id\": \"" + tvSeatIdReturn6.getText().toString() + "\"" +
                            "}]";
                    Log.d("Log", "Passenger6 : " + sReturnPassengerDetails6);
                    sReturnPassengerDetails += sReturnPassengerDetails1 + sReturnPassengerDetails2 + sReturnPassengerDetails3 + sReturnPassengerDetails4 + sReturnPassengerDetails5 + sReturnPassengerDetails6;
                    break;
            }

            Log.d("Log", "Passengers Details : " + sReturnPassengerDetails);

            //    dialogTransactionPassword.cancel();
//            getLocalIpAddress();
//            getDetails();
            //   bookTicket();
//            success();
//            Toast.makeText(this, "Ticket Successfully Booked...", Toast.LENGTH_SHORT).show();
        }
    }
    void getPassengerDetails() {
        if (isValidInfo()) {
            String sSelectedSeats = "";
            int selectedId;
            int selectedCTId;

            sPassengerDetails = "[";
            String sPassengerDetails1 = "";
            String sPassengerDetails2 = "";
            String sPassengerDetails3 = "";
            String sPassengerDetails4 = "";
            String sPassengerDetails5 = "";
            String sPassengerDetails6 = "";

            switch (seatCount) {
                case 1:
                    sSelectedSeats = textViewSeatNo1.getText().toString();
                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGender1.getCheckedRadioButtonId();
                    selectedCTId= radioGroupCType1.getCheckedRadioButtonId();
                    radioButtonCT1=findViewById(selectedCTId);
                    radioButtonPassenger1 = (RadioButton) findViewById(selectedId);


                  /*  sPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerName1 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT1.getText().toString()+ "\"," +
                          //  "\"mobile\": \"" + "" + "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" + tvSeatId1.getText().toString()  +"\""+
                            "}]";
                    Log.d("Log", "Passenger1 : " + sPassengerDetails1);
                    sPassengerDetails += sPassengerDetails1;*/

                    JSONObject passengers1 = new JSONObject();
                    JSONObject passengers2 = new JSONObject();
                    try {
                        passengers1.put("name", sPassengerName1);
                        passengers1.put("gender", radioButtonPassenger1.getText().toString());
                        passengers1.put("category", radioButtonCT1.getText().toString());
                        passengers1.put("passport", sPassengerPassport1);
                        passengers1.put("seat_id", tvSeatId1.getText().toString());


                    JSONArray jsonArray = new JSONArray();
                    JSONArray jsonReturnArryy= new JSONArray();
                    jsonArray.put(passengers1);
                    /*JSONObject passengersObj = new JSONObject();
                    passengersObj.put("passengers", jsonArray);*/
                     String jsonStr = jsonArray.toString();

                    sPassengerDetails=jsonStr;
                    if(AppConstants.Keys.isReturnBus) {
                        if (searchBusDetails.getDiffNoOfSeatAllowed().equals("0")) {
                            passengers2.put("name", sPassengerName1);
                            passengers2.put("gender", radioButtonPassenger1.getText().toString());
                            passengers2.put("category", radioButtonCT1.getText().toString());
                            passengers2.put("passport", sPassengerPassport1);
                            passengers2.put("seat_id", textViewSeatIdOnword1.getText().toString());
                            jsonReturnArryy.put(passengers2);
                            sReturnPassengerDetails = jsonReturnArryy.toString();
                            Log.d("Log", "sReturnPassengerDetails : " + sReturnPassengerDetails);
                        }
                    }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    sSelectedSeats = textViewSeatNo1.getText().toString() + ", " + textViewSeatNo2.getText().toString();
                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGender1.getCheckedRadioButtonId();
                    radioButtonPassenger1 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender2.getCheckedRadioButtonId();
                    radioButtonPassenger2 = (RadioButton) findViewById(selectedId);

                    selectedCTId= radioGroupCType1.getCheckedRadioButtonId();
                    radioButtonCT1=findViewById(selectedCTId);
                    selectedCTId= radioGroupCType2.getCheckedRadioButtonId();
                    radioButtonCT2=findViewById(selectedCTId);
                    sPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerName1 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT1.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" +tvSeatId1.getText().toString()   +"\""+
                            "},";
                    Log.d("Log", "Passenger1 : " + sPassengerDetails1);
                    if (sPassengerName2.equals("")) {
                        sPassengerName2 = sPassengerName1;
                    }
                    if (sPassengerAge2 == 0) {
                        sPassengerAge2 = sPassengerAge1;
                    }
                    if (sPassengerPassport2.equals("")) {
                        sPassengerPassport2 = sPassengerPassport1;
                    }
                    sPassengerDetails2 = "{" +
                            "\"name\": \"" + sPassengerName2 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger2.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT2.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport2 + "\"," +
                            "\"seat_id\": \"" + tvSeatId2.getText().toString()  +"\"" +
                            "}]";
                    Log.d("Log", "Passenger2 : " + sPassengerDetails2);
                    sPassengerDetails += sPassengerDetails1 + sPassengerDetails2;
                    break;
                case 3:
                    sSelectedSeats = textViewSeatNo1.getText().toString() + ", " + textViewSeatNo2.getText().toString() + ", " + textViewSeatNo3.getText().toString();
                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGender1.getCheckedRadioButtonId();
                    radioButtonPassenger1 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender2.getCheckedRadioButtonId();
                    radioButtonPassenger2 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender3.getCheckedRadioButtonId();
                    radioButtonPassenger3 = (RadioButton) findViewById(selectedId);

                    selectedCTId= radioGroupCType1.getCheckedRadioButtonId();
                    radioButtonCT1=findViewById(selectedCTId);
                    selectedCTId= radioGroupCType2.getCheckedRadioButtonId();
                    radioButtonCT2=findViewById(selectedCTId);
                    selectedCTId= radioGroupCType3.getCheckedRadioButtonId();
                    radioButtonCT3=findViewById(selectedCTId);



                    sPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerName1 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT1.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" +tvSeatId1.getText().toString()  +"\"" +
                            "},";
                    Log.d("Log", "Passenger1 : " + sPassengerDetails1);
                    if (sPassengerName2.equals("")) {
                        sPassengerName2 = sPassengerName1;
                    }
                    if (sPassengerAge2 == 0) {
                        sPassengerAge2 = sPassengerAge1;
                    }
                    if (sPassengerPassport2.equals("")) {
                        sPassengerPassport2 = sPassengerPassport1;
                    }
                    sPassengerDetails2 = "{" +
                            "\"name\": \"" + sPassengerName2 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger2.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT2.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport2 + "\"," +
                            "\"seat_id\": \"" + tvSeatId2.getText().toString()  +"\"" +
                            "},";
                    Log.d("Log", "Passenger2 : " + sPassengerDetails2);

                    if (sPassengerName3.equals("")) {
                        sPassengerName3 = sPassengerName1;
                    }
                    if (sPassengerAge3 == 0) {
                        sPassengerAge3 = sPassengerAge1;
                    }
                    if (sPassengerPassport3.equals("")) {
                        sPassengerPassport3 = sPassengerPassport1;
                    }
                    sPassengerDetails3 = "{" +
                            "\"name\": \"" + sPassengerName3 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger3.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT3.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport3 + "\"," +
                            "\"seat_id\": \"" + tvSeatId3.getText().toString()   +"\""+
                            "}]";
                    Log.d("Log", "Passenger3 : " + sPassengerDetails3);
                    sPassengerDetails += sPassengerDetails1 + sPassengerDetails2 + sPassengerDetails3;
                    break;
                case 4:
                    sSelectedSeats = textViewSeatNo1.getText().toString() + ", " + textViewSeatNo2.getText().toString() + ", " + textViewSeatNo3.getText().toString() + ", " + textViewSeatNo4.getText().toString();
                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGender1.getCheckedRadioButtonId();
                    radioButtonPassenger1 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender2.getCheckedRadioButtonId();
                    radioButtonPassenger2 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender3.getCheckedRadioButtonId();
                    radioButtonPassenger3 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender4.getCheckedRadioButtonId();
                    radioButtonPassenger4 = (RadioButton) findViewById(selectedId);

                    selectedCTId= radioGroupCType1.getCheckedRadioButtonId();
                    radioButtonCT1=findViewById(selectedCTId);
                    selectedCTId= radioGroupCType2.getCheckedRadioButtonId();
                    radioButtonCT2=findViewById(selectedCTId);
                    selectedCTId= radioGroupCType3.getCheckedRadioButtonId();
                    radioButtonCT3=findViewById(selectedCTId);
                    selectedCTId= radioGroupCType4.getCheckedRadioButtonId();
                    radioButtonCT4=findViewById(selectedCTId);



                    sPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerName1 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT1.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" + tvSeatId1.getText().toString()  +"\"" +
                            "},";
                    Log.d("Log", "Passenger1 : " + sPassengerDetails1);
                    if (sPassengerName2.equals("")) {
                        sPassengerName2 = sPassengerName1;
                    }
                    if (sPassengerAge2 == 0) {
                        sPassengerAge2 = sPassengerAge1;
                    }
                    if (sPassengerPassport2.equals("")) {
                        sPassengerPassport2 = sPassengerPassport1;
                    }
                    sPassengerDetails2 = "{" +
                            "\"name\": \"" + sPassengerName2 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger2.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT2.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport2 + "\"," +
                            "\"seat_id\": \"" + tvSeatId2.getText().toString()  +"\"" +
                            "},";
                    Log.d("Log", "Passenger2 : " + sPassengerDetails2);

                    if (sPassengerName3.equals("")) {
                        sPassengerName3 = sPassengerName1;
                    }
                    if (sPassengerAge3 == 0) {
                        sPassengerAge3 = sPassengerAge1;
                    }
                    if (sPassengerPassport3.equals("")) {
                        sPassengerPassport3 = sPassengerPassport1;
                    }
                    sPassengerDetails3 = "{" +
                            "\"name\": \"" + sPassengerName3 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger3.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT3.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport3 + "\"," +
                            "\"seat_id\": \"" + tvSeatId3.getText().toString()   +"\""+
                            "},";
                    Log.d("Log", "Passenger3 : " + sPassengerDetails3);


                    if (sPassengerName4.equals("")) {
                        sPassengerName4 = sPassengerName1;
                    }
                    if (sPassengerAge4 == 0) {
                        sPassengerAge4 = sPassengerAge1;
                    }
                    if (sPassengerPassport4.equals("")) {
                        sPassengerPassport4 = sPassengerPassport1;
                    }
                    sPassengerDetails4 = "{" +
                            "\"name\": \"" + sPassengerName4 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger4.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT4.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport4 + "\"," +
                            "\"seat_id\": \"" + tvSeatId4.getText().toString()  +"\""+
                            "}]";
                    Log.d("Log", "Passenger4 : " + sPassengerDetails4);
                    sPassengerDetails += sPassengerDetails1 + sPassengerDetails2 + sPassengerDetails3 + sPassengerDetails4;
                    break;
                case 5:
                    sSelectedSeats = textViewSeatNo1.getText().toString() + ", " + textViewSeatNo2.getText().toString() + ", "
                            + textViewSeatNo3.getText().toString() + ", " + textViewSeatNo4.getText().toString() + ", " + textViewSeatNo5.getText().toString();

                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGender1.getCheckedRadioButtonId();
                    radioButtonPassenger1 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender2.getCheckedRadioButtonId();
                    radioButtonPassenger2 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender3.getCheckedRadioButtonId();
                    radioButtonPassenger3 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender4.getCheckedRadioButtonId();
                    radioButtonPassenger4 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender5.getCheckedRadioButtonId();
                    radioButtonPassenger5 = (RadioButton) findViewById(selectedId);

                    sPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerName1 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT1.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" + tvSeatId1.getText().toString()   +"\""+
                            "},";
                    Log.d("Log", "Passenger1 : " + sPassengerDetails1);
                    if (sPassengerName2.equals("")) {
                        sPassengerName2 = sPassengerName1;
                    }
                    if (sPassengerAge2 == 0) {
                        sPassengerAge2 = sPassengerAge1;
                    }
                    if (sPassengerPassport2.equals("")) {
                        sPassengerPassport2 = sPassengerPassport1;
                    }
                    sPassengerDetails2 = "{" +
                            "\"name\": \"" + sPassengerName2 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger2.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT2.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport2 + "\"," +
                            "\"seat_id\": \"" + tvSeatId2.getText().toString()  +"\"" +
                            "},";
                    Log.d("Log", "Passenger2 : " + sPassengerDetails2);

                    if (sPassengerName3.equals("")) {
                        sPassengerName3 = sPassengerName1;
                    }
                    if (sPassengerAge3 == 0) {
                        sPassengerAge3 = sPassengerAge1;
                    }
                    if (sPassengerPassport3.equals("")) {
                        sPassengerPassport3 = sPassengerPassport1;
                    }
                    sPassengerDetails3 = "{" +
                            "\"name\": \"" + sPassengerName3 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger3.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT3.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport3 + "\"," +
                            "\"seat_id\": \"" + tvSeatId3.getText().toString()  +"\"" +
                            "},";
                    Log.d("Log", "Passenger3 : " + sPassengerDetails3);


                    if (sPassengerName4.equals("")) {
                        sPassengerName4 = sPassengerName1;
                    }
                    if (sPassengerAge4 == 0) {
                        sPassengerAge4 = sPassengerAge1;
                    }
                    if (sPassengerPassport4.equals("")) {
                        sPassengerPassport4 = sPassengerPassport1;
                    }
                    sPassengerDetails4 = "{" +
                            "\"name\": \"" + sPassengerName4 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger4.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT4.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport4 + "\"," +
                            "\"seat_id\": \"" + tvSeatId4.getText().toString()  +"\"" +
                            "},";
                    Log.d("Log", "Passenger4 : " + sPassengerDetails4);


                    if (sPassengerName5.equals("")) {
                        sPassengerName5 = sPassengerName1;
                    }
                    if (sPassengerAge5 == 0) {
                        sPassengerAge5 = sPassengerAge1;
                    }
                    if (sPassengerPassport5.equals("")) {
                        sPassengerPassport5 = sPassengerPassport1;
                    }
                    sPassengerDetails5 = "{" +
                            "\"name\": \"" + sPassengerName5 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger5.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT5.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport5 + "\"," +
                            "\"seat_id\": \"" + tvSeatId5.getText().toString()  +"\"" +
                            "}]";
                    Log.d("Log", "Passenger5 : " + sPassengerDetails5);
                    sPassengerDetails += sPassengerDetails1 + sPassengerDetails2 + sPassengerDetails3 + sPassengerDetails4 + sPassengerDetails5;
                    break;
                case 6:
                    sSelectedSeats = textViewSeatNo1.getText().toString() + ", " + textViewSeatNo2.getText().toString() + ", "
                            + textViewSeatNo3.getText().toString() + ", " + textViewSeatNo4.getText().toString() + ", "
                            + textViewSeatNo5.getText().toString() + ", " + textViewSeatNo6.getText().toString();
                    sSelectedSeats = sSelectedSeats.replaceAll(seat_no + " ", "");
                    selectedId = radioGroupGender1.getCheckedRadioButtonId();
                    radioButtonPassenger1 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender2.getCheckedRadioButtonId();
                    radioButtonPassenger2 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender3.getCheckedRadioButtonId();
                    radioButtonPassenger3 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender4.getCheckedRadioButtonId();
                    radioButtonPassenger4 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender5.getCheckedRadioButtonId();
                    radioButtonPassenger5 = (RadioButton) findViewById(selectedId);
                    selectedId = radioGroupGender6.getCheckedRadioButtonId();
                    radioButtonPassenger6 = (RadioButton) findViewById(selectedId);

                    sPassengerDetails1 = "{" +
                            "\"name\": \"" + sPassengerName1 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger1.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT1.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport1 + "\"," +
                            "\"seat_id\": \"" + tvSeatId1.getText().toString()  +"\"" +
                            "},";
                    Log.d("Log", "Passenger1 : " + sPassengerDetails1);
                    if (sPassengerName2.equals("")) {
                        sPassengerName2 = sPassengerName1;
                    }
                    if (sPassengerAge2 == 0) {
                        sPassengerAge2 = sPassengerAge1;
                    }
                    if (sPassengerPassport2.equals("")) {
                        sPassengerPassport2 = sPassengerPassport1;
                    }
                    sPassengerDetails2 = "{" +
                            "\"name\": \"" + sPassengerName2 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger2.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT2.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport2 + "\"," +
                            "\"seat_id\": \"" + tvSeatId2.getText().toString()  +"\"" +
                            "},";
                    Log.d("Log", "Passenger2 : " + sPassengerDetails2);

                    if (sPassengerName3.equals("")) {
                        sPassengerName3 = sPassengerName1;
                    }
                    if (sPassengerAge3 == 0) {
                        sPassengerAge3 = sPassengerAge1;
                    }
                    if (sPassengerPassport3.equals("")) {
                        sPassengerPassport3 = sPassengerPassport1;
                    }
                    sPassengerDetails3 = "{" +
                            "\"name\": \"" + sPassengerName3 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger3.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT3.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport3 + "\"," +
                            "\"seat_id\": \"" + tvSeatId3.getText().toString()  +"\"" +
                            "},";
                    Log.d("Log", "Passenger3 : " + sPassengerDetails3);

                    if (sPassengerName4.equals("")) {
                        sPassengerName4 = sPassengerName1;
                    }
                    if (sPassengerAge4 == 0) {
                        sPassengerAge4 = sPassengerAge1;
                    }
                    if (sPassengerPassport4.equals("")) {
                        sPassengerPassport4 = sPassengerPassport1;
                    }
                    sPassengerDetails4 = "{" +
                            "\"name\": \"" + sPassengerName4+ "\"," +
                            "\"gender\": \"" + radioButtonPassenger4.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT4.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport4 + "\"," +
                            "\"seat_id\": \"" + tvSeatId4.getText().toString()   +"\""+
                            "},";
                    Log.d("Log", "Passenger4 : " + sPassengerDetails4);


                    if (sPassengerName5.equals("")) {
                        sPassengerName5 = sPassengerName1;
                    }
                    if (sPassengerAge5 == 0) {
                        sPassengerAge5 = sPassengerAge1;
                    }
                    if (sPassengerPassport5.equals("")) {
                        sPassengerPassport5 = sPassengerPassport1;
                    }
                    sPassengerDetails5 = "{" +
                            "\"name\": \"" + sPassengerName5 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger5.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT5.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport5 + "\"," +
                            "\"seat_id\": \"" + tvSeatId5.getText().toString() +"\"" +
                            "},";
                    Log.d("Log", "Passenger5 : " + sPassengerDetails5);

                    if (sPassengerName6.equals("")) {
                        sPassengerName6 = sPassengerName1;
                    }
                    if (sPassengerAge6 == 0) {
                        sPassengerAge6 = sPassengerAge1;
                    }
                    if (sPassengerPassport6.equals("")) {
                        sPassengerPassport6 = sPassengerPassport1;
                    }
                    sPassengerDetails6 = "{" +
                            "\"name\": \"" + sPassengerName6 + "\"," +
                            "\"gender\": \"" + radioButtonPassenger6.getText().toString() + "\"," +
                            "\"category\": \"" + radioButtonCT6.getText().toString()+ "\"," +
                            "\"passport\": \"" + sPassengerPassport6 + "\"," +
                            "\"seat_id\": \"" + tvSeatId6.getText().toString()  +"\""+
                            "}]";
                    Log.d("Log", "Passenger6 : " + sPassengerDetails6);
                    sPassengerDetails += sPassengerDetails1 + sPassengerDetails2 + sPassengerDetails3 + sPassengerDetails4 + sPassengerDetails5 + sPassengerDetails6;
                    break;
            }

            Log.d("Log", "Passengers Details : " + sPassengerDetails);

        //    dialogTransactionPassword.cancel();
//            getLocalIpAddress();
//            getDetails();
         //   bookTicket();
//            success();
//            Toast.makeText(this, "Ticket Successfully Booked...", Toast.LENGTH_SHORT).show();
        }
    }

    private void success() {
        Intent intent = new Intent(this, BusBookingActivity.class);
        startActivity(intent);
    }

    private void bookTicket() {
        boolean isConnected = AppConstants.isConnected(this);
        if (isConnected) {

//            Fare = 2;
//            sCountryCode = (String) spinnerCountryCode.getSelectedItem();
            if (sCountryCode.startsWith("+")) {
                sCountryCode = sCountryCode.substring(1, sCountryCode.length());
            }

            sPassengerPhoneNo = sCountryCode + sPassengerPhoneNo;
            Random r = new Random();
            int random = r.nextInt(4 - 1 + 1) + 1;
            if (random > 4 && random < 1) {
                random = 3;
            }
            Agent agent = new Agent();
            agent.setsAgentId("924");
            AuthKey = Integer.parseInt(String.valueOf(random));
            showProgress();
            sIP = AppConstants.Status.IP;
            sIMEI = AppConstants.Status.IMEI;
            sAppVersion = AppConstants.Status.APP_VERSION;

            String sHashedTranstionPassword = null;
            try {
                sHashedTranstionPassword = SHA.calculateHash(AuthKey, SHA.SHA512(SHA.SHA1(sTransactionPassword)));
                System.out.println("Transaction Password:" + sHashedTranstionPassword);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

         /*   String sKey = SHA.calculateHash(AuthKey, agent.getsAgentId() + AppConstants.ApiNames.OWNER_ID + AuthKey + "b0Ok");
            sApiKey = SHA.calculateHash(AuthKey, AppConstants.ApiNames.OWNER_ID + agent.getsAgentId()
                    + sHashedTranstionPassword + sPassengerDetails + sPassengerPhoneNo + sEmail + sFromId + sToId
                    + passengerBus.getsNewJourneyDate() + seatMap.getsBusRouteSeatId() + passengerBus.getsBusID() + sUKey + "" + Fare
                    + sLanguage + searchBusDetails.getsDate() + sIMEI + sLatitude + sLongitude + sIP + AuthKey + "B0Ok5e@t");
            String sUrl = AppConstants.ApiNames.API_URL + AppConstants.ApiNames.BOOK_SEAT + sKey;*/

            /*Log.d("Log", "Routes URL : " + sUrl);
            Log.d("Log", "owner_id : " + AppConstants.ApiNames.OWNER_ID);
            Log.d("Log", "agent_id : " + agent.getsAgentId());
            Log.d("Log", "AuthKey : " + String.valueOf(AuthKey));
            Log.d("Log", "imei : " + sIMEI);
            Log.d("Log", "lat : " + sLatitude);
            Log.d("Log", "long : " + sLongitude);
            Log.d("Log", "ip : " + sIP);
            Log.d("Log", "key : " + sApiKey);
            Log.d("Log", "app_ver : " + sAppVersion);
            Log.d("Log", "tran_pass : " + sHashedTranstionPassword);
            Log.d("Log", "passenger_details : " + sPassengerDetails);
            Log.d("Log", "mobile : " + sPassengerPhoneNo);
            Log.d("Log", "email : " + sEmail);
            Log.d("Log", "fromID : " + sFromId);
            Log.d("Log", "toID : " + sToId);
            Log.d("Log", "journey_Date : " + passengerBus.getsNewJourneyDate());
            Log.d("Log", "bus_route_seat_id : " + seatMap.getsBusRouteSeatId());
            Log.d("Log", "bus_id : " + passengerBus.getsBusID());
            Log.d("Log", "ukey : " + sKey);
            Log.d("Log", "seat_counter : " + seatCount);
            Log.d("Log", "sold_fare : " + Fare);
            Log.d("Log", "language : " + sLanguage);
            Log.d("Log", "user_selected_date : " + searchBusDetails.getsDate());
            Log.d("Log", "key : " + sApiKey);*/

            OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
//            Call<BookTicketResponse> callGetStations = otappbusApiService.bookTicket(sKey, AppConstants.ApiNames.OWNER_ID, agent.getsAgentId(),
//                    String.valueOf(AuthKey), sIMEI, sLatitude, sLongitude, sIP, sAppVersion, sHashedTranstionPassword,
//                    sPassengerDetails, sPassengerPhoneNo, sEmail, sFromId, sToId, passengerBus.getsNewJourneyDate(),
//                    seatMap.getsBusRouteSeatId(), passengerBus.getsBusID(), sUKey, "" + seatCount,
//                    "" + Fare, searchBusDetails.getsDate(), sApiKey, sLanguage);

            /*normalTicketInfo = new NormalTicketInfo(sPassengerDetails, sPassengerPhoneNo, sEmail, sFromId, sToId,
                    passengerBus.getsNewJourneyDate(), seatMap.getsBusRouteSeatId(), passengerBus.getsBusID(), sUKey,
                    "" + seatCount, "" + Fare, sLanguage, sDate, bookTicketResponse);
*/
            //Log.d("Log", "Book Ticket URL : " + callGetStations.request().url());
/*
            callGetStations.enqueue(new Callback<BookTicketResponse>() {
                @Override
                public void onResponse(Call<BookTicketResponse> call, Response<BookTicketResponse> response) {
                    JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response : " + jsonObjectResponse);
                    } catch (JSONException e) {
                        Toast.makeText(PassengerDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    if (response.body().getStatus() == AppConstants.Status.SUCCESS) {
                        bookTicketResponse = response.body();
                        String sLoginHistory = "";
                        try {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.DATE_FORMAT);
                            sLoginHistory = simpleDateFormat.format(calendar.getTime());
                        } catch (Exception ex) {
                            Toast.makeText(PassengerDetailsActivity.this, "Error : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        String sEmail = agent.getsAgentName();
                        if (agent != null) {
                            sEmail = agent.getsAgentName();
                        }

                        Toast.makeText(PassengerDetailsActivity.this, bookTicketResponse.getsMessage(), Toast.LENGTH_SHORT).show();
                        onSuccess();
                    } else if (response.body().getStatus() == AppConstants.Status.SESSION_OUT) {
                        bookTicketResponse = response.body();
                        String sLoginHistory = "";
                        try {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.DATE_FORMAT);
                            sLoginHistory = simpleDateFormat.format(calendar.getTime());
                        } catch (Exception ex) {
                            Toast.makeText(PassengerDetailsActivity.this, "Error : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        String sEmail = agent.getsAgentName();
                        if (agent != null) {
                            sEmail = agent.getsAgentName();
                        }

                        Intent intent = new Intent(PassengerDetailsActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (response.body().getStatus() == AppConstants.Status.NOT_ACCESS) {
                        bookTicketResponse = response.body();
                        String sLoginHistory = "";
                        try {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.DATE_FORMAT);
                            sLoginHistory = simpleDateFormat.format(calendar.getTime());
                        } catch (Exception ex) {
                            Toast.makeText(PassengerDetailsActivity.this, "Error : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        String sEmail = agent.getsAgentName();
                        if (agent != null) {
                            sEmail = agent.getsAgentName();
                        }
                        Intent intent = new Intent(PassengerDetailsActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PassengerDetailsActivity.this, response.body().getsMessage(), Toast.LENGTH_SHORT).show();
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<BookTicketResponse> call, Throwable t) {
                    hideProgress();
                    Log.d("Log", "Error : " + t.getMessage());
                    Toast.makeText(PassengerDetailsActivity.this, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
*/

        } else {
            Toast.makeText(this, sNoInternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

    private void bookReturnTicket() {
        boolean isConnected = AppConstants.isConnected(this);
        if (isConnected) {
//            Fare = 2;
//            sCountryCode = (String) spinnerCountryCode.getSelectedItem();
            if (sCountryCode.startsWith("+")) {
                sCountryCode = sCountryCode.substring(1, sCountryCode.length());
            }

            sPassengerPhoneNo = sCountryCode + sPassengerPhoneNo;
//            showProgress();

            Random r = new Random();
            int random = r.nextInt(4 - 1 + 1) + 1;
            if (random > 4 && random < 1) {
                random = 3;
            }
            Agent agent = new Agent();
            agent.setsAgentId("924");
            AuthKey = Integer.parseInt(String.valueOf(random));
            showProgress();
            sIP = AppConstants.Status.IP;
            sIMEI = AppConstants.Status.IMEI;
            sAppVersion = AppConstants.Status.APP_VERSION;
            String sHashedTranstionPassword = null;
            try {
                sHashedTranstionPassword = SHA.calculateHash(AuthKey, SHA.SHA512(SHA.SHA1(sTransactionPassword)));
                System.out.println("Transaction Password:" + sHashedTranstionPassword);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String sKey = SHA.calculateHash(AuthKey, agent.getsAgentId() + AppConstants.ApiNames.OWNER_ID + AuthKey + "b0Ok");
          /*  sApiKey = SHA.calculateHash(AuthKey, AppConstants.ApiNames.OWNER_ID + agent.getsAgentId()
                    + sHashedTranstionPassword + sPassengerDetails + sPassengerPhoneNo + sEmail + sFromId + sToId
                    + passengerBus.getsNewJourneyDate() + seatMap.getsBusRouteSeatId() + passengerBus.getsBusID() + sUKey + "" + Fare
                    + sLanguage + searchBusDetails.getsDate() + sIMEI + sLatitude + sLongitude + sIP + AuthKey + "B0Ok5e@t");
            String sUrl = AppConstants.ApiNames.API_URL + AppConstants.ApiNames.BOOK_SEAT + sKey;*/

           /* Log.d("Log", "Routes URL : " + sUrl);
            Log.d("Log", "owner_id : " + AppConstants.ApiNames.OWNER_ID);
            Log.d("Log", "agent_id : " + agent.getsAgentId());
            Log.d("Log", "AuthKey : " + String.valueOf(AuthKey));
            Log.d("Log", "imei : " + sIMEI);
            Log.d("Log", "lat : " + sLatitude);
            Log.d("Log", "long : " + sLongitude);
            Log.d("Log", "ip : " + sIP);
            Log.d("Log", "key : " + sApiKey);
            Log.d("Log", "app_ver : " + sAppVersion);
            Log.d("Log", "tran_pass : " + sHashedTranstionPassword);
            Log.d("Log", "passenger_details : " + sPassengerDetails);
            Log.d("Log", "mobile : " + sPassengerPhoneNo);
            Log.d("Log", "email : " + sEmail);
            Log.d("Log", "fromID : " + sFromId);
            Log.d("Log", "toID : " + sToId);
            Log.d("Log", "journey_Date : " + passengerBus.getsNewJourneyDate());
            Log.d("Log", "bus_route_seat_id : " + seatMap.getsBusRouteSeatId());
            Log.d("Log", "bus_id : " + passengerBus.getsBusID());
            Log.d("Log", "ukey : " + sKey);
            Log.d("Log", "seat_counter : " + seatCount);
            Log.d("Log", "sold_fare : " + Fare);
            Log.d("Log", "language : " + sLanguage);
            Log.d("Log", "user_selected_date : " + searchBusDetails.getsDate());
            Log.d("Log", "key : " + sApiKey);*/

          /*  OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
            Call<BookTicketResponse> callGetStations = otappbusApiService.bookTicket(sKey, AppConstants.ApiNames.OWNER_ID, agent.getsAgentId(),
                    String.valueOf(AuthKey), sIMEI, sLatitude, sLongitude, sIP, sAppVersion, sHashedTranstionPassword,
                    sPassengerDetails, sPassengerPhoneNo, sEmail, sFromId, sToId, passengerBus.getsNewJourneyDate(),
                    seatMap.getsBusRouteSeatId(), passengerBus.getsBusID(), sUKey, "" + seatCount,
                    "" + Fare, searchBusDetails.getsDate(), sApiKey, sLanguage);*/

          /*  normalTicketInfo = new NormalTicketInfo(sPassengerDetails, sPassengerPhoneNo, sEmail, sFromId, sToId,
                    passengerBus.getsNewJourneyDate(), seatMap.getsBusRouteSeatId(), passengerBus.getsBusID(), sUKey,
                    "" + seatCount, "" + Fare, sLanguage, sDate, bookTicketResponse);*/

          //  Log.d("Log", "Book Ticket URL : " + callGetStations.request().url());
/*
            callGetStations.enqueue(new Callback<BookTicketResponse>() {
                @Override
                public void onResponse(Call<BookTicketResponse> call, Response<BookTicketResponse> response) {
                    JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response : " + jsonObjectResponse);
                    } catch (JSONException e) {
                        Toast.makeText(PassengerDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    if (response.body().getStatus() == AppConstants.Status.SUCCESS) {
                        bookTicketResponse = response.body();
                        String sLoginHistory = "";
                        try {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.DATE_FORMAT);
                            sLoginHistory = simpleDateFormat.format(calendar.getTime());
                        } catch (Exception ex) {
                            Toast.makeText(PassengerDetailsActivity.this, "Error : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        String sEmail = agent.getsAgentName();
                        if (agent != null) {
                            sEmail = agent.getsAgentName();
                        }
                        Toast.makeText(PassengerDetailsActivity.this, bookTicketResponse.getsMessage(), Toast.LENGTH_SHORT).show();
                        onSuccess();
                    } else if (response.body().getStatus() == AppConstants.Status.SESSION_OUT) {
                        bookTicketResponse = response.body();
                        String sLoginHistory = "";
                        try {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.DATE_FORMAT);
                            sLoginHistory = simpleDateFormat.format(calendar.getTime());
                        } catch (Exception ex) {
                            Toast.makeText(PassengerDetailsActivity.this, "Error : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        String sEmail = agent.getsAgentName();
                        if (agent != null) {
                            sEmail = agent.getsAgentName();
                        }
                        Intent intent = new Intent(PassengerDetailsActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (response.body().getStatus() == AppConstants.Status.NOT_ACCESS) {
                        bookTicketResponse = response.body();
                        String sLoginHistory = "";
                        try {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.DATE_FORMAT);
                            sLoginHistory = simpleDateFormat.format(calendar.getTime());
                        } catch (Exception ex) {
                            Toast.makeText(PassengerDetailsActivity.this, "Error : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        String sEmail = agent.getsAgentName();
                        if (agent != null) {
                            sEmail = agent.getsAgentName();
                        }
                        Intent intent = new Intent(PassengerDetailsActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PassengerDetailsActivity.this, response.body().getsMessage(), Toast.LENGTH_SHORT).show();
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<BookTicketResponse> call, Throwable t) {
                    hideProgress();
                    Log.d("Log", "Error : " + t.getMessage());
                    Toast.makeText(PassengerDetailsActivity.this, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
*/


        } else {
            Toast.makeText(this, sNoInternetConnection, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResponse(String sResponse) {
        hideProgress();
        Log.d("Log", "Response : " + sResponse);
        if (sResponse != null) {
            try {
                JSONObject jsonObject = new JSONObject(sResponse);
                int sStatus = jsonObject.getInt("status");
                String sMessage = jsonObject.getString("message");
                if (sStatus == AppConstants.Status.SUCCESS) {
//                    dialogTransactionPassword.dismiss();
                    String sTicketNo = jsonObject.getString("ticketno");
                    String sSingleTicketFare = jsonObject.getString("singleticketfare");
                    String stotalFare = jsonObject.getString("totalfare");
                    String sDepartureTime = jsonObject.getString("departuretime");
                    String sBusType = jsonObject.getString("bustype");
                    String sBusOperator = jsonObject.getString("busoperator");
                    String sBusNo = jsonObject.getString("busno");
                    String sBookingTime = jsonObject.getString("booking_time");

                    JSONObject jsonObjectAgent = jsonObject.getJSONObject("access_roles");

                    String sAgentId = jsonObjectAgent.getString("agent_id");
                    String sAgentName = jsonObjectAgent.getString("agent_name");
                    String sAgentCurrency = jsonObjectAgent.getString("agent_currency");
                    String sIsGrantedForTicketSelling = jsonObjectAgent.getString("is_granted_for_tkt_selling");
                    String sIsGrantedForStandingTicket = jsonObjectAgent.getString("is_granted_for_stnd_tkts");
                    String sIsGrantedForCargoTicket = jsonObjectAgent.getString("is_granted_for_cargo_tkts");
                    String sIsGrantedForStatements = jsonObjectAgent.getString("is_granted_for_statements");
                    String sIsGrantedForAgentSummary = jsonObjectAgent.getString("is_granted_for_agent_summary");
                    String sIsGrantedForAsignConductorBus = jsonObjectAgent.getString("is_granted_for_asign_conductor_bus");
                    String sIsGrantedForAsignDriverBus = jsonObjectAgent.getString("is_granted_for_asign_driver_bus");
                    String sIsGrantedForMechanichBus = jsonObjectAgent.getString("is_granted_for_mechanich_bus");
                    String sIsGrantedForActivateAgent = jsonObjectAgent.getString("is_granted_to_activate_agent");
                    String sIsGrantedForPrintTicket = jsonObjectAgent.getString("is_granted_for_prnt_tkts");
                    String sIsGrantedForPassengerList = jsonObjectAgent.getString("is_granted_for_psngr_list");
                    String sIsGrantedForCargoList = jsonObjectAgent.getString("is_granted_for_cargo_list");
                    String sAgentFloatBal = jsonObjectAgent.getString("agent_float_bal");
                    String sIsGrantedToShowFloat = jsonObjectAgent.getString("is_granted_to_show_float");
                    String sAgentUsername = jsonObjectAgent.getString("agent_username");
                    String sUserType = jsonObjectAgent.getString("user_type");
                    String sKey = jsonObjectAgent.getString("key");
                    String sLoginHistory = "";
                    try {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.DATE_FORMAT);
                        sLoginHistory = simpleDateFormat.format(calendar.getTime());
//                        Toast.makeText(this, "Date : " + sLoginHistory, Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(this, "Error : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    String sLEmail = agent.getsAgentName();
                    AppConstants.Preferences.setBooleanPreferences(PassengerDetailsActivity.this.getApplicationContext(), AppConstants.Keys.LOGIN_STATUS, true);

                    Toast.makeText(this, sMessage, Toast.LENGTH_SHORT).show();
                    onSuccess();
                } else if (sStatus == AppConstants.Status.SESSION_OUT) {
                    JSONObject jsonObjectAgent = jsonObject.getJSONObject("access_roles");

                    String sAgentId = jsonObjectAgent.getString("agent_id");
                    String sAgentName = jsonObjectAgent.getString("agent_name");
                    String sAgentCurrency = jsonObjectAgent.getString("agent_currency");
                    String sIsGrantedForTicketSelling = jsonObjectAgent.getString("is_granted_for_tkt_selling");
                    String sIsGrantedForStandingTicket = jsonObjectAgent.getString("is_granted_for_stnd_tkts");
                    String sIsGrantedForCargoTicket = jsonObjectAgent.getString("is_granted_for_cargo_tkts");
                    String sIsGrantedForStatements = jsonObjectAgent.getString("is_granted_for_statements");
                    String sIsGrantedForAgentSummary = jsonObjectAgent.getString("is_granted_for_agent_summary");
                    String sIsGrantedForAsignConductorBus = jsonObjectAgent.getString("is_granted_for_asign_conductor_bus");
                    String sIsGrantedForAsignDriverBus = jsonObjectAgent.getString("is_granted_for_asign_driver_bus");
                    String sIsGrantedForMechanichBus = jsonObjectAgent.getString("is_granted_for_mechanich_bus");
                    String sIsGrantedForActivateAgent = jsonObjectAgent.getString("is_granted_to_activate_agent");
                    String sIsGrantedForPrintTicket = jsonObjectAgent.getString("is_granted_for_prnt_tkts");
                    String sIsGrantedForPassengerList = jsonObjectAgent.getString("is_granted_for_psngr_list");
                    String sIsGrantedForCargoList = jsonObjectAgent.getString("is_granted_for_cargo_list");
                    String sAgentFloatBal = jsonObjectAgent.getString("agent_float_bal");
                    String sIsGrantedToShowFloat = jsonObjectAgent.getString("is_granted_to_show_float");
                    String sAgentUsername = jsonObjectAgent.getString("agent_username");
                    String sUserType = jsonObjectAgent.getString("user_type");
                    String sKey = jsonObjectAgent.getString("key");
                    String sLoginHistory = "";
                    try {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.DATE_FORMAT);
                        sLoginHistory = simpleDateFormat.format(calendar.getTime());
//                        Toast.makeText(this, "Date : " + sLoginHistory, Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(this, "Error : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    String sLEmail = agent.getsAgentName();
                    Toast.makeText(this, sMessage, Toast.LENGTH_SHORT).show();
                    AppConstants.Preferences.setBooleanPreferences(PassengerDetailsActivity.this.getApplicationContext(), AppConstants.Keys.LOGIN_STATUS, true);

                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (sStatus == AppConstants.Status.NOT_ACCESS) {
                    JSONObject jsonObjectAgent = jsonObject.getJSONObject("access_roles");

                    String sAgentId = jsonObjectAgent.getString("agent_id");
                    String sAgentName = jsonObjectAgent.getString("agent_name");
                    String sAgentCurrency = jsonObjectAgent.getString("agent_currency");
                    String sIsGrantedForTicketSelling = jsonObjectAgent.getString("is_granted_for_tkt_selling");
                    String sIsGrantedForStandingTicket = jsonObjectAgent.getString("is_granted_for_stnd_tkts");
                    String sIsGrantedForCargoTicket = jsonObjectAgent.getString("is_granted_for_cargo_tkts");
                    String sIsGrantedForStatements = jsonObjectAgent.getString("is_granted_for_statements");
                    String sIsGrantedForAgentSummary = jsonObjectAgent.getString("is_granted_for_agent_summary");
                    String sIsGrantedForAsignConductorBus = jsonObjectAgent.getString("is_granted_for_asign_conductor_bus");
                    String sIsGrantedForAsignDriverBus = jsonObjectAgent.getString("is_granted_for_asign_driver_bus");
                    String sIsGrantedForMechanichBus = jsonObjectAgent.getString("is_granted_for_mechanich_bus");
                    String sIsGrantedForActivateAgent = jsonObjectAgent.getString("is_granted_to_activate_agent");
                    String sIsGrantedForPrintTicket = jsonObjectAgent.getString("is_granted_for_prnt_tkts");
                    String sIsGrantedForPassengerList = jsonObjectAgent.getString("is_granted_for_psngr_list");
                    String sIsGrantedForCargoList = jsonObjectAgent.getString("is_granted_for_cargo_list");
                    String sAgentFloatBal = jsonObjectAgent.getString("agent_float_bal");
                    String sIsGrantedToShowFloat = jsonObjectAgent.getString("is_granted_to_show_float");
                    String sAgentUsername = jsonObjectAgent.getString("agent_username");
                    String sUserType = jsonObjectAgent.getString("user_type");
                    String sKey = jsonObjectAgent.getString("key");
                    String sLoginHistory = "";
                    try {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.DATE_FORMAT);
                        sLoginHistory = simpleDateFormat.format(calendar.getTime());
//                        Toast.makeText(this, "Date : " + sLoginHistory, Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(this, "Error : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    String sLEmail = agent.getsAgentName();
                    Toast.makeText(this, sMessage, Toast.LENGTH_SHORT).show();
                    AppConstants.Preferences.setBooleanPreferences(PassengerDetailsActivity.this.getApplicationContext(), AppConstants.Keys.LOGIN_STATUS, true);

                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    onResponseError(sMessage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onSuccess() {
        onSuccessPrint();
    }

    @Override
    public void onResponseError(String sResponse) {
        Toast.makeText(this, sResponse, Toast.LENGTH_SHORT).show();
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

    private void getSelectedSeats() {
        int i = 0;
/*
        if (hashMapSelectedSeats.size() > 0) {
            for (String sKey : hashMapSelectedSeats.keySet()) {
                Seat seat = hashMapSelectedSeats.get(sKey);
//                PassengerInfo passengerInfo = new PassengerInfo(seat.getsSeatNo(), "", "", "");
//                passengersArrayList.add(passengerInfo);
                if (i == 0) {
                    textViewSeatNo1.setText(seat_no + " " + seat.getsSeatNo());
                } else if (i == 1) {
                    textViewSeatNo2.setText(seat_no + " " + seat.getsSeatNo());
                } else if (i == 2) {
                    textViewSeatNo3.setText(seat_no + " " + seat.getsSeatNo());
                } else if (i == 3) {
                    textViewSeatNo4.setText(seat_no + " " + seat.getsSeatNo());
                } else if (i == 4) {
                    textViewSeatNo5.setText(seat_no + " " + seat.getsSeatNo());
                } else if (i == 5) {
                    textViewSeatNo6.setText(seat_no + " " + seat.getsSeatNo());
                }
                i++;
            }
//            passengerInfoAdapter = new PassengerInfoAdapter(this, passengersArrayList, this);
//            listViewPassengers.setAdapter(passengerInfoAdapter);
        }
*/
    }


    private boolean isValidInfo() {
        boolean isValid = false;
        sPassengerName = editTextPassengersName.getText().toString().trim();
        sPassengerName1 = editTextPassengersName1.getText().toString().trim();
        if (!editTextPassengersAge1.getText().toString().equals("")) {
            sPassengerAge1 = Integer.parseInt(editTextPassengersAge1.getText().toString().trim());
        } else {
            sPassengerAge1 = 0;
        }
        sPassengerPassport1 = editTextPassportNumber1.getText().toString().trim();
        sPassengerName2 = editTextPassengersName2.getText().toString().trim();
        if (!editTextPassengersAge2.getText().toString().equals("")) {
            sPassengerAge2 = Integer.parseInt(editTextPassengersAge2.getText().toString().trim());
        } else {
            sPassengerAge2 = 0;
        }
        sPassengerPassport2 = editTextPassportNumber2.getText().toString().trim();
        sPassengerName3 = editTextPassengersName3.getText().toString().trim();
        if (!editTextPassengersAge3.getText().toString().equals("")) {
            sPassengerAge3 = Integer.parseInt(editTextPassengersAge3.getText().toString().trim());
        } else {
            sPassengerAge3 = 0;
        }
        sPassengerPassport3 = editTextPassportNumber3.getText().toString().trim();
        sPassengerName4 = editTextPassengersName4.getText().toString().trim();
        if (!editTextPassportNumber4.getText().toString().equals("")) {
            sPassengerAge4 = Integer.parseInt(editTextPassengersAge4.getText().toString().trim());
        } else {
            sPassengerAge4 = 0;
        }
        sPassengerPassport4 = editTextPassportNumber4.getText().toString().trim();
        sPassengerName5 = editTextPassengersName5.getText().toString().trim();
        if (!editTextPassengersAge5.getText().toString().equals("")) {
            sPassengerAge5 = Integer.parseInt(editTextPassengersAge5.getText().toString().trim());
        } else {
            sPassengerAge5 = 0;
        }
        sPassengerPassport5 = editTextPassportNumber5.getText().toString().trim();
        sPassengerName6 = editTextPassengersName6.getText().toString().trim();
        if (!editTextPassengersAge6.getText().toString().equals("")) {
            sPassengerAge6 = Integer.parseInt(editTextPassengersAge6.getText().toString().trim());
        } else {
            sPassengerAge6 = 0;
        }
        sPassengerPassport6 = editTextPassportNumber6.getText().toString().trim();

        sPassengerNameReturn1 = editTextPassengersNameReturn1.getText().toString().trim();
        sPassengerNameReturn2 = editTextPassengersNameReturn2.getText().toString().trim();
        sPassengerNameReturn3 = editTextPassengersNameReturn3.getText().toString().trim();
        sPassengerNameReturn4 = editTextPassengersNameReturn4.getText().toString().trim();
        sPassengerNameReturn5 = editTextPassengersNameReturn5.getText().toString().trim();
        sPassengerNameReturn6 = editTextPassengersNameReturn6.getText().toString().trim();


        sPassengerPhoneNo = editTextPassengersPhoneNumber.getText().toString().trim();
        sEmail = editTextPassengersEmail.getText().toString().trim();
//        sCountryCode = (String) spinnerCountryCode.getSelectedItem();
        if (sCountryCode.startsWith("+")) {
            sCountryCode = sCountryCode.substring(1, sCountryCode.length());
        }
//        Toast.makeText(this, "Name : " + sPassengerName1, Toast.LENGTH_SHORT).show();
        if (sPassengerName.equals("") || sPassengerName1.equals("") || !isValidMobile(sPassengerPhoneNo) || !isValidMobile(sPassengerPhoneNo)
                || sPassengerPhoneNo.length() < 9 || !sPassengerName1.matches("[a-zA-Z ]+")) {

            isValid = false;

            if (sPassengerName1.equals("")) {
                editTextPassengersName1.setError(sEnterPassengerName);
                editTextPassengersName1.requestFocus();
                editTextPassengersName1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        sPassengerName1 = s.toString();
                        if (sPassengerName1.equals("")) {
                            editTextPassengersName1.setError(sEnterPassengerName);
                        } else {
                            editTextPassengersName1.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            } else if (!sPassengerName1.matches("[a-zA-Z ]+")) {
                editTextPassengersName1.setError(onlyAlphabetsAllowed);
                editTextPassengersName1.requestFocus();
                editTextPassengersName1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        sPassengerName1 = s.toString();
                        if (!sPassengerName1.matches("[a-zA-Z ]+")) {
                            editTextPassengersName1.setError(onlyAlphabetsAllowed);
                        } else {
                            editTextPassengersName1.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            } else if (sPassengerName.equals("")) {
                editTextPassengersName.setError("Enter Passenger Name");
                editTextPassengersName.requestFocus();
                editTextPassengersName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        sPassengerName = s.toString();
                        if (sPassengerName.equals("")) {
                            editTextPassengersName.setError("Enter Passenger Name");
                        } else {
                            editTextPassengersName.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            } else {
                editTextPassengersName1.setError(null);
                editTextPassengersName.setError(null);
            }

            if (sPassengerPhoneNo.length() < 9) {
                editTextPassengersPhoneNumber.setError(sEnterValidMobileNumber);
                editTextPassengersPhoneNumber.requestFocus();
                editTextPassengersPhoneNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        sPassengerPhoneNo = s.toString();
                        if (sPassengerPhoneNo.length() < 9) {
                            editTextPassengersPhoneNumber.setError(sEnterValidMobileNumber);
                        } else {
                            editTextPassengersPhoneNumber.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            } else if (!isValidMobile(sPassengerPhoneNo)) {
                editTextPassengersPhoneNumber.setError(sEnterValidMobileNumber);
                editTextPassengersPhoneNumber.requestFocus();
                editTextPassengersPhoneNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        sPassengerPhoneNo = s.toString();
                        if (!isValidMobile(sPassengerPhoneNo)) {
                            editTextPassengersPhoneNumber.setError(sEnterValidMobileNumber);
                        } else {
                            editTextPassengersPhoneNumber.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        } else {
            if (editTextPassengersEmail.getText().toString().equals("")) {
                isValid = true;
                isValid = checkNames();
            } else {
                if (!isValidEmail(sEmail)) {
                    isValid = false;
                    editTextPassengersEmail.setError(sValidEmailId);
                    editTextPassengersEmail.requestFocus();
                    editTextPassengersEmail.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            sEmail = s.toString();
                            if (!isValidEmail(sEmail)) {
                                editTextPassengersEmail.setError(sValidEmailId);
                            } else {
                                editTextPassengersEmail.setError(null);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else {
                    isValid = true;
                    isValid = checkNames();
                }
            }

            /*if (seatCount == 1) {
                if (sPassengerAge1 <= 0) {
                    if (!editTextPassengersAge1.getText().toString().equals("")) {
                        isValid = false;
                        checkPassengerAge1();
                    } else {
                        isValid = true;
                    }
                } else {
                    isValid = true;
                }
            } else if (seatCount == 2) {
                if (sPassengerAge2 <= 0) {
//                    Toast.makeText(this, "Age  : " + editTextPassengersAge2.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (!editTextPassengersAge2.getText().toString().equals("")) {
                        isValid = false;
                        checkPassengerAge2();
                    } else {
                        isValid = true;
                    }
                } else {
                    isValid = true;
                }
            } else if (seatCount == 3) {
                if (sPassengerAge2 <= 0 || sPassengerAge3 <= 0) {
                    if (!editTextPassengersAge2.getText().toString().equals("")) {
                        isValid = false;
                        checkPassengerAge2();
                    } else {
                        if (!editTextPassengersAge3.getText().toString().equals("")) {
                            isValid = false;
                            checkPassengerAge3();
                        } else {
                            isValid = true;
                        }
                    }
                } else {
                    isValid = true;
                }
            } else if (seatCount == 4) {
                if (sPassengerAge2 <= 0 || sPassengerAge3 <= 0 || sPassengerAge4 <= 0) {
                    if (!editTextPassengersAge2.getText().toString().equals("")) {
                        isValid = false;
                        checkPassengerAge2();
                    } else {
                        if (!editTextPassengersAge3.getText().toString().equals("")) {
                            isValid = false;
                            checkPassengerAge3();
                        } else {
                            if (!editTextPassengersAge4.getText().toString().equals("")) {
                                isValid = false;
                                checkPassengerAge4();
                            } else {
                                isValid = true;
                            }
                        }
                    }
                } else {
                    isValid = true;
                }
            } else if (seatCount == 5) {
                if (sPassengerAge2 <= 0 || sPassengerAge3 <= 0 || sPassengerAge4 <= 0 || sPassengerAge5 <= 0) {
                    if (!editTextPassengersAge2.getText().toString().equals("")) {
                        isValid = false;
                        checkPassengerAge2();
                    } else {
                        if (!editTextPassengersAge3.getText().toString().equals("")) {
                            isValid = false;
                            checkPassengerAge3();
                        } else {
                            if (!editTextPassengersAge4.getText().toString().equals("")) {
                                isValid = false;
                                checkPassengerAge4();
                            } else {
                                if (!editTextPassengersAge5.getText().toString().equals("")) {
                                    isValid = false;
                                    checkPassengerAge5();
                                } else {
                                    isValid = true;
                                }
                            }
                        }
                    }
                } else {
                    isValid = true;
                }
            } else if (seatCount == 6) {
                if (sPassengerAge2 <= 0 || sPassengerAge3 <= 0 || sPassengerAge4 <= 0 || sPassengerAge5 <= 0 || sPassengerAge6 <= 0) {
                    if (!editTextPassengersAge3.getText().toString().equals("")) {
                        isValid = false;
                        checkPassengerAge3();
                    } else {
                        if (!editTextPassengersAge4.getText().toString().equals("")) {
                            isValid = false;
                            checkPassengerAge4();
                        } else {
                            if (!editTextPassengersAge5.getText().toString().equals("")) {
                                isValid = false;
                                checkPassengerAge5();
                            } else {
                                if (!editTextPassengersAge6.getText().toString().equals("")) {
                                    isValid = false;
                                    checkPassengerAge6();
                                } else {
                                    isValid = true;
                                }
                            }
                        }
                    }
                } else {
                    isValid = true;
                }
            } else {
                isValid = true;
            }*/

        }
        return isValid;
    }

    private boolean checkNames() {
        boolean isValid = false;
        if (seatCount == 1) {
            if (sPassengerName1.equals("")) {
                if (!editTextPassengersName1.getText().toString().equals("")) {
                    isValid = false;
                    checkPassengerName1();
                } else {
                    isValid = true;
                }
            } else {
                isValid = true;
            }
        } else if (seatCount == 2) {
            if (sPassengerName2.equals("")) {
                checkPassengerName2();
            } else {
                isValid = true;
            }
        } else if (seatCount == 3) {
            if (sPassengerName2.equals("") || sPassengerName3.equals("")) {
                if (sPassengerName2.equals("")) {
                    isValid = false;
                    checkPassengerName2();
                } else {
                    if (sPassengerName3.equals("")) {
                        isValid = false;
                        checkPassengerName3();
                    } else {
                        isValid = true;
                    }
                }
            } else {
                isValid = true;
            }
        } else if (seatCount == 4) {
            if (sPassengerName2.equals("") || sPassengerName3.equals("") || sPassengerName4.equals("")) {
                if (sPassengerName2.equals("")) {
                    isValid = false;
                    checkPassengerName2();
                } else {
                    if (sPassengerName3.equals("")) {
                        isValid = false;
                        checkPassengerName3();
                    } else {
                        if (sPassengerName4.equals("")) {
                            isValid = false;
                            checkPassengerName4();
                        } else {
                            isValid = true;
                        }
                    }
                }
            } else {
                isValid = true;
            }
        } else if (seatCount == 5) {
            if (sPassengerName2.equals("") || sPassengerName3.equals("") || sPassengerName4.equals("") || sPassengerName5.equals("")) {
                if (sPassengerName2.equals("")) {
                    isValid = false;
                    checkPassengerName2();
                } else {
                    if (sPassengerName3.equals("")) {
                        isValid = false;
                        checkPassengerName3();
                    } else {
                        if (sPassengerName4.equals("")) {
                            isValid = false;
                            checkPassengerName4();
                        } else {
                            if (sPassengerName5.equals("")) {
                                isValid = false;
                                checkPassengerName5();
                            } else {
                                isValid = true;
                            }
                        }
                    }
                }
            } else {
                isValid = true;
            }
        } else if (seatCount == 6) {
            if (sPassengerName2.equals("") || sPassengerName3.equals("") || sPassengerName4.equals("") || sPassengerName5.equals("") || sPassengerName6.equals("")) {
                if (sPassengerName2.equals("")) {
                    isValid = false;
                    checkPassengerName2();
                } else {
                    if (sPassengerName3.equals("")) {
                        isValid = false;
                        checkPassengerName3();
                    } else {
                        if (sPassengerName4.equals("")) {
                            isValid = false;
                            checkPassengerName4();
                        } else {
                            if (sPassengerName5.equals("")) {
                                isValid = false;
                                checkPassengerName5();
                            } else {
                                if (sPassengerName6.equals("")) {
                                    isValid = false;
                                    checkPassengerName6();
                                } else {
                                    isValid = true;
                                }
                            }
                        }
                    }
                }
            } else {
                isValid = true;
            }
        } else {
            isValid = true;
        }

        /*if (stationFrom.getsStationCountry().equals(stationTo.getsStationCountry())) {
//            isValid = true;
        } else {
            if (seatCount == 1) {
                if (sPassengerPassport1.equals("")) {
                    if (sPassengerPassport1.equals("")) {
                        isValid = false;
                        checkPassengerPassport1();
                    } else {
                        isValid = true;
                    }
                } else {
                    isValid = true;
                }
            } else if (seatCount == 2) {
                if (sPassengerPassport1.equals("") || sPassengerPassport2.equals("")) {
                    if (sPassengerPassport1.equals("")) {
                        isValid = false;
                        checkPassengerPassport1();
                    } else {
                        if (sPassengerPassport2.equals("")) {
                            isValid = false;
                            checkPassengerPassport2();
                        } else {
                            isValid = true;
                        }
                    }
                }
            } else if (seatCount == 3) {
                if (sPassengerPassport1.equals("") || sPassengerPassport2.equals("") || sPassengerPassport3.equals("")) {
                    if (sPassengerPassport1.equals("")) {
                        isValid = false;
                        checkPassengerPassport1();
                    } else {
                        if (sPassengerPassport2.equals("")) {
                            isValid = false;
                            checkPassengerPassport2();
                        } else {
                            if (sPassengerPassport3.equals("")) {
                                isValid = false;
                                checkPassengerPassport3();
                            } else {
                                isValid = true;
                            }
                        }
                    }
                } else {
                    isValid = true;
                }
            } else if (seatCount == 4) {
                if (sPassengerPassport1.equals("") || sPassengerPassport2.equals("") || sPassengerPassport3.equals("") || sPassengerPassport4.equals("")) {
                    if (sPassengerPassport1.equals("")) {
                        isValid = false;
                        checkPassengerPassport1();
                    } else {
                        if (sPassengerPassport2.equals("")) {
                            isValid = false;
                            checkPassengerPassport2();
                        } else {
                            if (sPassengerPassport3.equals("")) {
                                isValid = false;
                                checkPassengerPassport3();
                            } else {
                                if (sPassengerPassport4.equals("")) {
                                    isValid = false;
                                    checkPassengerPassport4();
                                } else {
                                    isValid = true;
                                }
                            }
                        }
                    }
                } else {
                    isValid = true;
                }
            } else if (seatCount == 5) {
                if (sPassengerPassport1.equals("") || sPassengerPassport2.equals("") || sPassengerPassport3.equals("") || sPassengerPassport4.equals("") || sPassengerPassport5.equals("")) {
                    if (sPassengerPassport1.equals("")) {
                        isValid = false;
                        checkPassengerPassport1();
                    } else {
                        if (sPassengerPassport2.equals("")) {
                            isValid = false;
                            checkPassengerPassport2();
                        } else {
                            if (sPassengerPassport3.equals("")) {
                                isValid = false;
                                checkPassengerPassport3();
                            } else {
                                if (sPassengerPassport4.equals("")) {
                                    isValid = false;
                                    checkPassengerPassport4();
                                } else {
                                    if (sPassengerPassport5.equals("")) {
                                        isValid = false;
                                        checkPassengerPassport5();
                                    } else {
                                        isValid = true;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    isValid = true;
                }
            } else if (seatCount == 6) {
                if (sPassengerPassport1.equals("") || sPassengerPassport2.equals("") || sPassengerPassport3.equals("") || sPassengerPassport4.equals("") || sPassengerPassport5.equals("") || sPassengerPassport6.equals("")) {
                    if (sPassengerPassport1.equals("")) {
                        isValid = false;
                        checkPassengerPassport1();
                    } else {
                        if (sPassengerPassport2.equals("")) {
                            isValid = false;
                            checkPassengerPassport2();
                        } else {
                            if (sPassengerPassport3.equals("")) {
                                isValid = false;
                                checkPassengerPassport3();
                            } else {
                                if (sPassengerPassport4.equals("")) {
                                    isValid = false;
                                    checkPassengerPassport4();
                                } else {
                                    if (sPassengerPassport5.equals("")) {
                                        isValid = false;
                                        checkPassengerPassport5();
                                    } else {
                                        if (sPassengerPassport6.equals("")) {
                                            isValid = false;
                                            checkPassengerPassport6();
                                        } else {
                                            isValid = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    isValid = true;
                }
            } else {
                isValid = true;
            }
        }*/
        return isValid;
    }


    private void checkPassengerName1() {
        if (sPassengerName1.equals("")) {
            editTextPassengersName1.setError(sEnterPassengerName);
            editTextPassengersName1.requestFocus();
            editTextPassengersName1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerName1 = s.toString();
                        if (sPassengerName1.equals("")) {
                            editTextPassengersName1.setError(sEnterPassengerName);
                        } else {
                            editTextPassengersName1.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerName2() {
        if (sPassengerName2.equals("")) {
            editTextPassengersName2.setError(sEnterPassengerName);
            editTextPassengersName2.requestFocus();
            editTextPassengersName2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerName2 = s.toString();
                        if (sPassengerName2.equals("")) {
                            editTextPassengersName2.setError(sEnterPassengerName);
                        } else {
                            editTextPassengersName2.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerName3() {
        if (sPassengerName3.equals("")) {
            editTextPassengersName3.setError(sEnterPassengerName);
            editTextPassengersName3.requestFocus();
            editTextPassengersName3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerName3 = s.toString();
                        if (sPassengerName3.equals("")) {
                            editTextPassengersName3.setError(sEnterPassengerName);
                        } else {
                            editTextPassengersName3.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerName4() {
        if (sPassengerName4.equals("")) {
            editTextPassengersName4.setError(sEnterPassengerName);
            editTextPassengersName4.requestFocus();
            editTextPassengersName4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerName4 = s.toString();
                        if (sPassengerName4.equals("")) {
                            editTextPassengersName4.setError(sEnterPassengerName);
                        } else {
                            editTextPassengersName4.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerName5() {
        if (sPassengerName5.equals("")) {
            editTextPassengersName5.setError(sEnterPassengerName);
            editTextPassengersName5.requestFocus();
            editTextPassengersName5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerName5 = s.toString();
                        if (sPassengerName5.equals("")) {
                            editTextPassengersName5.setError(sEnterPassengerName);
                        } else {
                            editTextPassengersName5.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerName6() {
        if (sPassengerName6.equals("")) {
            editTextPassengersName6.setError(sEnterPassengerName);
            editTextPassengersName6.requestFocus();
            editTextPassengersName6.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerName6 = s.toString();
                        if (sPassengerName6.equals("")) {
                            editTextPassengersName6.setError(sEnterPassengerName);
                        } else {
                            editTextPassengersName6.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerPassport1() {
        if (sPassengerPassport1.equals("")) {
            editTextPassportNumber1.setError(enter_passenger_passport_number);
            editTextPassportNumber1.requestFocus();
            editTextPassportNumber1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerPassport1 = s.toString();
                        if (sPassengerPassport1.equals("")) {
                            editTextPassportNumber1.setError(enter_passenger_passport_number);
                        } else {
                            editTextPassportNumber1.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerPassport2() {
        if (sPassengerPassport2.equals("")) {
            editTextPassportNumber2.setError(enter_passenger_passport_number);
            editTextPassportNumber2.requestFocus();
            editTextPassportNumber2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerPassport2 = s.toString();
                        if (sPassengerPassport2.equals("")) {
                            editTextPassportNumber2.setError(enter_passenger_passport_number);
                        } else {
                            editTextPassportNumber2.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerPassport3() {
        if (sPassengerPassport3.equals("")) {
            editTextPassportNumber3.setError(enter_passenger_passport_number);
            editTextPassportNumber3.requestFocus();
            editTextPassportNumber3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerPassport3 = s.toString();
                        if (sPassengerPassport3.equals("")) {
                            editTextPassportNumber3.setError(enter_passenger_passport_number);
                        } else {
                            editTextPassportNumber3.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerPassport4() {
        if (sPassengerPassport4.equals("")) {
            editTextPassportNumber4.setError(enter_passenger_passport_number);
            editTextPassportNumber4.requestFocus();
            editTextPassportNumber4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerPassport4 = s.toString();
                        if (sPassengerPassport4.equals("")) {
                            editTextPassportNumber4.setError(enter_passenger_passport_number);
                        } else {
                            editTextPassportNumber4.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerPassport5() {
        if (sPassengerPassport5.equals("")) {
            editTextPassportNumber5.setError(enter_passenger_passport_number);
            editTextPassportNumber5.requestFocus();
            editTextPassportNumber5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerPassport5 = s.toString();
                        if (sPassengerPassport5.equals("")) {
                            editTextPassportNumber5.setError(enter_passenger_passport_number);
                        } else {
                            editTextPassportNumber5.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerPassport6() {
        if (sPassengerPassport6.equals("")) {
            editTextPassportNumber6.setError(enter_passenger_passport_number);
            editTextPassportNumber6.requestFocus();
            editTextPassportNumber6.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerPassport6 = s.toString();
                        if (sPassengerPassport6.equals("")) {
                            editTextPassportNumber6.setError(enter_passenger_passport_number);
                        } else {
                            editTextPassportNumber6.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerAge1() {
        if (sPassengerAge1 <= 0) {
            editTextPassengersAge1.setError(sValidAge);
            editTextPassengersAge1.requestFocus();
            editTextPassengersAge1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerAge1 = Integer.parseInt(s.toString());
                        if (sPassengerAge1 <= 0) {
                            editTextPassengersAge1.setError(sValidAge);
                        } else {
                            editTextPassengersAge1.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerAge2() {
        if (sPassengerAge2 <= 0) {
            editTextPassengersAge2.setError(sValidAge);
            editTextPassengersAge2.requestFocus();
            editTextPassengersAge2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerAge2 = Integer.parseInt(s.toString());
                        if (sPassengerAge2 <= 0) {
                            editTextPassengersAge2.setError(sValidAge);
                        } else {
                            editTextPassengersAge2.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerAge3() {
        if (sPassengerAge3 <= 0) {
            editTextPassengersAge3.setError(sValidAge);
            editTextPassengersAge3.requestFocus();
            editTextPassengersAge3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerAge3 = Integer.parseInt(s.toString());
                        if (sPassengerAge3 <= 0) {
                            editTextPassengersAge3.setError(sValidAge);
                        } else {
                            editTextPassengersAge3.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerAge4() {
        if (sPassengerAge4 <= 0) {
            editTextPassengersAge4.setError(sValidAge);
            editTextPassengersAge4.requestFocus();
            editTextPassengersAge4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerAge4 = Integer.parseInt(s.toString());
                        if (sPassengerAge4 <= 0) {
                            editTextPassengersAge4.setError(sValidAge);
                        } else {
                            editTextPassengersAge4.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerAge5() {
        if (sPassengerAge5 <= 0) {
            editTextPassengersAge5.setError(sValidAge);
            editTextPassengersAge5.requestFocus();
            editTextPassengersAge5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerAge5 = Integer.parseInt(s.toString());
                        if (sPassengerAge5 <= 0) {
                            editTextPassengersAge5.setError(sValidAge);
                        } else {
                            editTextPassengersAge5.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void checkPassengerAge6() {
        if (sPassengerAge6 <= 0) {
            editTextPassengersAge6.setError(sValidAge);
            editTextPassengersAge6.requestFocus();
            editTextPassengersAge6.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().equals("")) {
                        sPassengerAge6 = Integer.parseInt(s.toString());
                        if (sPassengerAge6 <= 0) {
                            editTextPassengersAge6.setError(sValidAge);
                        } else {
                            editTextPassengersAge6.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @BindString(R.string.Phone_No)
    String Phone_No;

    private void onSuccessPrint() {
        Intent intent = new Intent(this, PassengerTicketDetails.class);
//        intent.putExtra(AppConstants.IntentKeys.TICKET_DETAILS, normalTicketInfo);
        intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
        intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
        startActivity(intent);
    }
}