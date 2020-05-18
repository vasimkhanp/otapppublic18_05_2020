package com.otapp.net.Bus.Fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.Bus.Activity.SelectBusActivity;
import com.otapp.net.Bus.Adapter.AutoCompleteBusCityAdapter;
import com.otapp.net.Bus.Core.Agent;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.AvailableBuses;
import com.otapp.net.Bus.Core.PassengerBus;
import com.otapp.net.Bus.Core.SHA;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.Bus.Core.SearchBusResponse;
import com.otapp.net.Bus.Core.Station;
import com.otapp.net.Bus.Core.StationResponse;
import com.otapp.net.Bus.Core.TelephonyInfo;
import com.otapp.net.Bus.Network.ApiInstance;
import com.otapp.net.Bus.Network.OtappApiInstance;
import com.otapp.net.Bus.Network.OtappApiService;
import com.otapp.net.R;
import com.otapp.net.adapter.AutoCompleteCityAdapter;
import com.otapp.net.adapter.ServiceCategoryPagerAdapterWithTitle;
import com.otapp.net.fragment.FlightOneWayFragment;
import com.otapp.net.fragment.ServiceFragment;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Random;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.otapp.net.Bus.Core.SHA.calculateHash;

//0t@pP2o20

public class SearchBusFragment extends Fragment {


    @BindView(R.id.buttonSearch)
    Button buttonSearch;
    @BindView(R.id.linearLayoutPassengersView)
    LinearLayout linearLayoutPassengersView;
    @BindView(R.id.relativeLayoutBook)
    RelativeLayout relativeLayoutBook;
    @BindView(R.id.linearLayoutReturn)
    LinearLayout linearLayoutReturn;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewReturnDate)
    TextView textViewReturnDate;
    @BindView(R.id.actFrom)
    AutoCompleteTextView actFrom;
    @BindView(R.id.actTo)
    AutoCompleteTextView actTo;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.textViewOneWay)
    TextView textViewOneWay;
    @BindView(R.id.textViewReturn)
    TextView textViewReturn;
    @BindView(R.id.textViewMonth)
    TextView textViewMonth;
    @BindView(R.id.textViewDay)
    TextView textViewDay;
    @BindView(R.id.textViewReturnMonth)
    TextView textViewReturnMonth;
    @BindView(R.id.textViewReturnDay)
    TextView textViewReturnDay;
    @BindView(R.id.linearLayoutDepartureDays)
    LinearLayout linearLayoutDepartureDays;
    @BindView(R.id.linearLayoutDeparture)
    LinearLayout linearLayoutDeparture;
    @BindView(R.id.imgOneWay)
    ImageView imgOneWay;
    @BindView(R.id.imgRoundTrip)
    ImageView imgRoundTrip;

    private Bundle bundle;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog returnDatePickerDialog;
    private Agent agent;
    private Station stationFrom = null;
    private Station stationTo = null;
    private ArrayList<Station> stationArrayList = new ArrayList<>();
    private ArrayAdapter<Station> fromArrayAdapter;
    private ArrayAdapter<Station> toArrayAdapter;
    private ArrayList<AvailableBuses> passengerBusArrayList = new ArrayList<>();
    AvailableBuses availableBuses;

    private ProgressDialog progressDialog;
    private String sTicketNo;
    private int AuthKey;
    private String sIP = AppConstants.Status.IP;
    private String sIMEI = AppConstants.Status.IMEI;
    private String sAppVersion = AppConstants.Status.APP_VERSION;
    private String sApiKey;
    private String sLanguage = "1";
    private String sGender = "Male";
    private String sLatitude = "0.0";
    private String sLongitude = "0.0";
    private LocationManager locationManager;
    private String sFrom="";
    private String sTo="";
    private String sDate="";
    private String sReturnDate = "";
    private String sFromId="";
    private String sToId="";
    private String sCompId= com.otapp.net.utils.AppConstants.sCompId;
    private String sAgentId= com.otapp.net.utils.AppConstants.sAgentId;
    private String sIsFrom= com.otapp.net.utils.AppConstants.sIsFrom;
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
    private View view;
    private boolean isReturn;
    private SimpleDateFormat simpleDateFormatServer;
    private SimpleDateFormat simpleDayFormat;
    private SimpleDateFormat simpleMonthFormat;
    ServiceCategoryPagerAdapterWithTitle mServiceCategoryPagerAdapterWithTitle;

    public static SearchBusFragment newInstance() {
        SearchBusFragment fragment = new SearchBusFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_bus, container, false);
        ButterKnife.bind(this, view);

        onOneWay();
        stationArrayList = new ArrayList<>();
        getStations();
        MyPref.setPref(getContext(), MyPref.PREF_BUS_UKEY, "");
        MyPref.setPref(getContext(), MyPref.RETURN_UKEY, "");
        calendar = Calendar.getInstance();
        simpleDateFormatServer = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat = new SimpleDateFormat("dd");
        simpleMonthFormat = new SimpleDateFormat("MMMM");
        simpleDayFormat = new SimpleDateFormat("EEEE");
        textViewDate.setText(simpleDateFormat.format(calendar.getTime()));
        textViewDay.setText(simpleDayFormat.format(calendar.getTime()));
        textViewMonth.setText(simpleMonthFormat.format(calendar.getTime()));
        sDate = simpleDateFormatServer.format(calendar.getTime());
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        textViewReturnDate.setText(simpleDateFormat.format(calendar.getTime()));
        textViewReturnDay.setText(simpleDayFormat.format(calendar.getTime()));
        textViewReturnMonth.setText(simpleMonthFormat.format(calendar.getTime()));
        sReturnDate = simpleDateFormatServer.format(calendar.getTime());

        textViewDate.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return view;
    }

    @OnClick(R.id.textViewToday)
    void onToday(){
        textViewTomorrow.setTextColor(colorGrey);
        textViewToday.setTextColor(colorPrimary);
        calendar = Calendar.getInstance();
        simpleDateFormatServer = new SimpleDateFormat("yyyy-mm-dd");
        simpleDateFormat = new SimpleDateFormat("dd");
        simpleMonthFormat = new SimpleDateFormat("MMMM");
        simpleDayFormat = new SimpleDateFormat("EEEE");
        textViewDate.setText(simpleDateFormat.format(calendar.getTime()));
        textViewDay.setText(simpleDayFormat.format(calendar.getTime()));
        textViewMonth.setText(simpleMonthFormat.format(calendar.getTime()));
        sDate = simpleDateFormatServer.format(calendar.getTime());
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        textViewReturnDate.setText(simpleDateFormat.format(calendar.getTime()));
        sReturnDate = simpleDateFormatServer.format(calendar.getTime());
    }

    @BindColor(R.color.colorGrey)
    int colorGrey;
    @BindColor(R.color.colorPrimary)
    int colorPrimary;
    @BindView(R.id.textViewToday)
    TextView textViewToday;
    @BindView(R.id.textViewTomorrow)
    TextView textViewTomorrow;
    @OnClick(R.id.textViewTomorrow)
    void onTomorrow(){
        textViewTomorrow.setTextColor(colorPrimary);
        textViewToday.setTextColor(colorGrey);
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        simpleDateFormatServer = new SimpleDateFormat("yyyy-mm-dd");
        simpleDateFormat = new SimpleDateFormat("dd");
        simpleMonthFormat = new SimpleDateFormat("MMMM");
        simpleDayFormat = new SimpleDateFormat("EEEE");
        textViewDate.setText(simpleDateFormat.format(calendar.getTime()));
        textViewDay.setText(simpleDayFormat.format(calendar.getTime()));
        textViewMonth.setText(simpleMonthFormat.format(calendar.getTime()));
        sDate = simpleDateFormatServer.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        textViewReturnDate.setText(simpleDateFormat.format(calendar.getTime()));
        sReturnDate = simpleDateFormatServer.format(calendar.getTime());
    }

    @OnClick(R.id.textViewOneWay)
    void onOneWay() {
        onReset();
        isReturn = false;
        AppConstants.Keys.isReturnBus=false;
        linearLayoutDepartureDays.setVisibility(View.VISIBLE);
        linearLayoutReturn.setVisibility(View.GONE);
        textViewOneWay.setTextColor(Color.WHITE);
        textViewOneWay.setBackgroundResource(R.drawable.button_background);
        imgRoundTrip.setVisibility(View.GONE);
        imgOneWay.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.textViewReturn)
    void onReturn() {
        onReset();
        isReturn = true;
        AppConstants.Keys.isReturnBus=true;
        linearLayoutDepartureDays.setVisibility(View.GONE);
        linearLayoutReturn.setVisibility(View.VISIBLE);
        textViewReturn.setTextColor(Color.WHITE);
        textViewReturn.setBackgroundResource(R.drawable.button_background);
        imgRoundTrip.setVisibility(View.VISIBLE);
        imgOneWay.setVisibility(View.GONE);
    }

    void onReset() {
        textViewOneWay.setTextColor(Color.BLACK);
        textViewReturn.setTextColor(Color.BLACK);
        textViewOneWay.setBackgroundResource(R.drawable.edittext_background);
        textViewReturn.setBackgroundResource(R.drawable.edittext_background);
    }

   /* @OnCheckedChanged({R.id.radioButtonOneWay, R.id.radioButtonReturn})
    public void onRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (button == radioButtonReturn) {
            isReturn = false;
            linearLayoutReturn.setVisibility(View.GONE);
        } else {
            isReturn = true;
            linearLayoutReturn.setVisibility(View.VISIBLE);
        }
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }*/

    @OnClick(R.id.buttonSearch)
    void onClick() {
        AppConstants.Preferences.setIntPreferences(getActivity(), AppConstants.Keys.SELECTED_TAB, 0);
        if (isValidInfo()) {
           getBuses();
           // onSuccessBuses("SHF32K45SLHJCSJAG");
        }
    }

    @OnClick(R.id.textViewDate)
    void onSelectDate() {
        showDatePickerDialog();
    }

    @OnClick(R.id.textViewReturnDate)
    void onSelectReturnDate() {
        showReturnDatePickerDialog();
    }
    @OnClick(R.id.linearLayoutDeparture)
    void onDepatrureTimeClick(){
        showDatePickerDialog();
    }
    @OnClick(R.id.linearLayoutReturn)
    void onRetrunDateSelection(){
        showReturnDatePickerDialog();
    }

    private void getDetails() {
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(getActivity());
        sIMEI = telephonyInfo.getImsiSIM1();
        PackageInfo pInfo = null;
        String AndroidId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
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

    Calendar calenderDeparture;

    /*public void showDatePickerDialog() {   //  Date Picker
        simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.USER_DATE_FORMAT);
        final Calendar newDate = Calendar.getInstance();
        calendar = Calendar.getInstance();
        if (datePickerDialog != null) {
            if (datePickerDialog.isShowing())
                datePickerDialog.dismiss();
        }
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate.set(year, monthOfYear, dayOfMonth);
                calenderDeparture = newDate;
                textViewDate.setText(simpleDateFormat.format(newDate.getTime()));
                Calendar calendar = newDate;
                calendar.add(Calendar.DATE, 1);
                simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.USER_DATE_FORMAT);
                textViewReturnDate.setText(simpleDateFormat.format(calendar.getTime()));
                String strDate = String.valueOf(newDate.getTime());
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
        String sDate = AppConstants.Preferences.getStringPreference(getActivity(), AppConstants.Keys.MAX_DATE);
        if (sDate != null) {
            Calendar calendarMax = Calendar.getInstance();
            calendarMax.add(Calendar.DAY_OF_MONTH, Integer.parseInt(sDate));
            datePickerDialog.getDatePicker().setMaxDate(calendarMax.getTime().getTime());
        }
        if (!datePickerDialog.isShowing()) {
            datePickerDialog.show();
        }
    }

    public void showReturnDatePickerDialog() {   //  Date Picker
        simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.USER_DATE_FORMAT);
        Calendar newDate = Calendar.getInstance();
        if (calenderDeparture != null) {
            newDate = calenderDeparture;
        }
        calendar = Calendar.getInstance();
        if (returnDatePickerDialog != null) {
            if (returnDatePickerDialog.isShowing())
                returnDatePickerDialog.dismiss();
        }
        final Calendar finalNewDate = newDate;
        returnDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                finalNewDate.set(year, monthOfYear, dayOfMonth);
                textViewReturnDate.setText(simpleDateFormat.format(finalNewDate.getTime()));
                String strDate = String.valueOf(finalNewDate.getTime());
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        returnDatePickerDialog.getDatePicker().setMinDate(this.calendar.getTime().getTime());
        String sDate = AppConstants.Preferences.getStringPreference(getActivity(), AppConstants.Keys.MAX_DATE);
        if (sDate != null) {
            Calendar calendarMax = Calendar.getInstance();
            calendarMax.add(Calendar.DAY_OF_MONTH, Integer.parseInt(sDate));
            returnDatePickerDialog.getDatePicker().setMaxDate(calendarMax.getTime().getTime());
        }
        if (!returnDatePickerDialog.isShowing()) {
            returnDatePickerDialog.show();
        }

    }*/

    public void showDatePickerDialog() {   //  Date Picker
        final Calendar newDate = Calendar.getInstance();
        calendar = Calendar.getInstance();
        if (datePickerDialog != null) {
            if (datePickerDialog.isShowing())
                datePickerDialog.dismiss();
        }
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate.set(year, monthOfYear, dayOfMonth);
                calenderDeparture = newDate;
                sDate = simpleDateFormatServer.format(newDate.getTime());
                textViewDate.setText(simpleDateFormat.format(newDate.getTime()));
                textViewDay.setText(simpleDayFormat.format(newDate.getTime()));
                textViewMonth.setText(simpleMonthFormat.format(newDate.getTime()));
                Calendar calendar = newDate;
                calendar.add(Calendar.DATE, 1);
                sReturnDate = simpleDateFormatServer.format(calendar.getTime());
                textViewReturnDate.setText(simpleDateFormat.format(calendar.getTime()));
                textViewReturnDay.setText(simpleDayFormat.format(calendar.getTime()));
                textViewReturnMonth.setText(simpleMonthFormat.format(calendar.getTime()));
                String strDate = String.valueOf(newDate.getTime());
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));


            datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());

        /*if (sDate != null) {
            Calendar calendarMax = Calendar.getInstance();
            calendarMax.add(Calendar.DAY_OF_MONTH, Integer.parseInt(sDate));
            datePickerDialog.getDatePicker().setMaxDate(calendarMax.getTime().getTime());
        }*/
        if (!datePickerDialog.isShowing()) {
            datePickerDialog.show();
        }
    }

    public void showReturnDatePickerDialog() {   //  Date Picker
        Calendar newDate = Calendar.getInstance();
        if (calenderDeparture != null) {
            newDate = calenderDeparture;
        }
        calendar = Calendar.getInstance();
        if (returnDatePickerDialog != null) {
            if (returnDatePickerDialog.isShowing())
                returnDatePickerDialog.dismiss();
        }
        final Calendar finalNewDate = newDate;
        returnDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                finalNewDate.set(year, monthOfYear, dayOfMonth);
                sReturnDate = simpleDateFormatServer.format(finalNewDate.getTime());
                textViewReturnDate.setText(simpleDateFormat.format(finalNewDate.getTime()));
                textViewReturnDay.setText(simpleDayFormat.format(finalNewDate.getTime()));
                textViewReturnMonth.setText(simpleMonthFormat.format(finalNewDate.getTime()));
                String strDate = String.valueOf(finalNewDate.getTime());
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

       // returnDatePickerDialog.getDatePicker().setMinDate(this.calendar.getTime().getTime());
        returnDatePickerDialog.getDatePicker().setMinDate(Long.parseLong(simpleDateFormat.format(newDate.getTime())));
        /*String sDate = AppConstants.Preferences.getStringPreference(getActivity(), AppConstants.Keys.MAX_DATE);
        if (sDate != null) {
            Calendar calendarMax = Calendar.getInstance();
            calendarMax.add(Calendar.DAY_OF_MONTH, Integer.parseInt(sDate));
            returnDatePickerDialog.getDatePicker().setMaxDate(calendarMax.getTime().getTime());
        }*/
        if (!returnDatePickerDialog.isShowing()) {
            returnDatePickerDialog.show();
        }

    }

    private void getBuses() {  //  Get Standing Route Details
        boolean isConnected = AppConstants.isConnected(getActivity());
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
            showProgress();
          /*  sIP = AppConstants.Status.IP;
            sIMEI = AppConstants.Status.IMEI;
            sAppVersion = AppConstants.Status.APP_VERSION;*/
//            sDate = AppConstants.Formats.getServerDate(textViewDate.getText().toString().trim());
//            sReturnDate = AppConstants.Formats.getServerDate(textViewReturnDate.getText().toString().trim());

            String sKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"Get-5e@rChbu5e$")));
            sApiKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4, sFromId+sToId+sDate+sCompId+sAgentId+sIsFrom+"5e@rcHbu5e$")));

            OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
            Call<SearchBusResponse> callGetStations = otappbusApiService.searchBuses(sKey,  String.valueOf(AuthKey), sFromId, sToId,sCompId,sDate, sAgentId,sIsFrom,sApiKey);

            Log.d("Log", "Key : " + sKey);
            Log.d("Log", "agent : " + sAgentId);
            Log.d("Log", "AuthKey : " + AuthKey);
            Log.d("Log", "From Id : " + sFromId);
            Log.d("Log", "To Id : " + sToId);
            Log.d("Log", "Comp Id : " + sCompId);
            Log.d("Log", " isFrom : " + sIsFrom);
            Log.d("Log", "Date : " + sDate);
            Log.d("Log", "APIkey: " + sApiKey);
            Log.d("Log", "Get Buses : " + callGetStations.request().url());
            callGetStations.enqueue(new Callback<SearchBusResponse>() {
                @Override
                public void onResponse(Call<SearchBusResponse> call, Response<SearchBusResponse> response) {
                    if (response.body() != null) {
                        if (response.body().status == AppConstants.Status.SUCCESS) {
                            JSONObject jsonObjectResponse = null;
                            try {
                                jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                                Log.d("Log", "Response bus list : " + jsonObjectResponse);
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            SearchBusResponse searchBusResponse = response.body();
                            passengerBusArrayList = searchBusResponse.getAvailableBusesArrayList();
                            Log.d("Log","passengerlist size="+passengerBusArrayList.size());
//                        routes = response.body();
                            onSuccessBuses();
                        } else {
                            Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error Null", Toast.LENGTH_SHORT).show();
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<SearchBusResponse> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(getActivity(), "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            hideProgress();
            Toast.makeText(getActivity(), sNoInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void getStations() {  //  Get Stations
//        Toast.makeText(getActivity(), "Searching", Toast.LENGTH_SHORT).show();
        boolean isConnected = AppConstants.isConnected(getActivity());
        if (isConnected) {
            showProgress();
            Random r = new Random();
            int random = r.nextInt(4 - 1 + 1) + 1;
            if (random > 4 && random < 1) {
                random = 3;
            }
            AuthKey = Integer.parseInt(String.valueOf(random));


            /*String sKey = AuthKey + "5e@rcH";*/

              //  sApiKey = SHA.MD5(SHA.SHA512(AuthKey + "Get-5e@rCh5tn$"));

                sApiKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey + "Get-5e@rCh5tn$")));

            OtappApiService otappbusApiService = ApiInstance.RetrofitInstance().create(OtappApiService.class);
            Call<StationResponse> callGetStations = otappbusApiService.getStations(sApiKey, "" + AuthKey);
            Log.d("Log SS", "auth key : " + AuthKey);
            Log.d("Log SS", "Get Standing Routes URL : " + callGetStations.request().url());
            callGetStations.enqueue(new Callback<StationResponse>() {
                @Override
                public void onResponse(Call<StationResponse> call, Response<StationResponse> response) {
                    if (response.body() != null) {
                        JSONObject jsonObjectResponse = null;
                        try {
                            jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                            Log.d("Log", "Response Station List : " + jsonObjectResponse);
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        if (response.body().getStatus() == AppConstants.Status.SUCCESS) {

                            stationArrayList = response.body().getStationArrayList();
                            onSuccessStations();
                        } else {
                            Toast.makeText(getActivity(), response.body().getsMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<StationResponse> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(getContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            hideProgress();
            Toast.makeText(getContext(), sNoInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void onSuccessStations() {
//        Login();
        /*fromArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, stationArrayList);
        actFrom.setAdapter(fromArrayAdapter);
        toArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, stationArrayList);
        actTo.setAdapter(toArrayAdapter);
        actFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (stationArrayList != null && stationArrayList.size() != 0) {
                    stationFrom = (Station) actFrom.getAdapter().getItem(position);
                    if(stationFrom.equals(stationTo)){
                        Toast.makeText(getContext(), "Please Select Different Station", Toast.LENGTH_SHORT).show();
                        actFrom.setText("");
                        stationFrom=null;
                    }
                }
            }
        });*/

/*
        actTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (stationArrayList != null && stationArrayList.size() != 0) {
                    stationTo = (Station) actTo.getAdapter().getItem(position);
                    if(stationTo.equals(stationFrom)){
                        Toast.makeText(getContext(), "Please Select Different Station", Toast.LENGTH_SHORT).show();
                        actTo.setText("");
                        stationTo=null;
                    }
                }
            }
        });
*/
        AutoCompleteBusCityAdapter mFromCityAdapter = new AutoCompleteBusCityAdapter(getActivity(), R.layout.list_item_airport_city_drop_down, stationArrayList);
        actFrom.setAdapter(mFromCityAdapter);
        actFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
              //  mFromCity = (FlightCityPojo.City) adapterView.getItemAtPosition(pos);
                stationFrom = (Station) actFrom.getAdapter().getItem(pos);
                actFrom.setSelection(actFrom.getText().length());
                if(stationFrom.equals(stationTo)){
                    Toast.makeText(getContext(), "Please Select Different Station", Toast.LENGTH_SHORT).show();
                    actFrom.setText("");
                    stationFrom=null;
                }
               /* ServiceFragment mServiceFragment = (ServiceFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ServiceFragment.Tag_ServiceFragment);
                if (mServiceFragment != null) {
                    LogUtils.e("", "mFlightFragment is not null");
                    mServiceFragment.setBusFromCity(stationFrom);
                } else {
                    LogUtils.e("", "mFlightFragment is null");
                }*/
            }
        });
        AutoCompleteBusCityAdapter mToCityAdapter = new AutoCompleteBusCityAdapter(getActivity(), R.layout.list_item_airport_city_drop_down, stationArrayList);
        actTo.setAdapter(mToCityAdapter);
        actTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //  mFromCity = (FlightCityPojo.City) adapterView.getItemAtPosition(pos);
                stationTo = (Station) actTo.getAdapter().getItem(pos);
                actTo.setSelection(actTo.getText().length());
                if(stationTo.equals(stationFrom)){
                    Toast.makeText(getContext(), "Please Select Different Station", Toast.LENGTH_SHORT).show();
                    actTo.setText("");
                    stationTo=null;
                }
                /*ServiceFragment mServiceFragment = (ServiceFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ServiceFragment.Tag_ServiceFragment);
                if (mServiceFragment != null) {
                    LogUtils.e("", "mFlightFragment is not null");
                    mServiceFragment.setBusToCity(stationFrom);
                } else {
                    LogUtils.e("", "mFlightFragment is null");
                }*/
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        MyPref.setPref(getContext(),MyPref.RETURN_UKEY,"");
        MyPref.setPref(getContext(), MyPref.PREF_BUS_UKEY,"");
    }

    private void showProgress() {
        Utils.showProgressDialog(getActivity());
    }

    private void hideProgress() {
        Utils.closeProgressDialog();
    }


    private void onSuccessBuses() {
      /*  passengerBusArrayList = new ArrayList<>();*/

       /* PassengerBus passengerBus = new PassengerBus("Neeta Travels", "758444",
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


        if (passengerBusArrayList.size() != 0) {
            Log.d("Log","Search bus "+sFrom+ sTo+ sDate+ sFromId+ sToId+ isReturn+ sReturnDate);
            SearchBusDetails searchBusDetails = new SearchBusDetails(sFrom, sTo, sDate, sFromId, sToId, isReturn, sReturnDate, "", "", "", "", sTo, sFrom, sToId, sFromId, null, stationFrom, stationTo);
            Log.d("Log","To Stop"+searchBusDetails.getsReturnTo());
            Log.d("Log","From  Stop"+searchBusDetails.getsReturnFrom());

            Intent intent = new Intent(getActivity(), SelectBusActivity.class);
            intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);

            intent.putExtra(AppConstants.IntentKeys.BUSES_LIST, passengerBusArrayList);
            intent.putExtra("from",actFrom.getText().toString());
            intent.putExtra("to",actTo.getText().toString());
            intent.putExtra("journydate",sDate);

           // intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
            getActivity().startActivity(intent);
            actFrom.setText("");
            actTo.setText("");
            actTo.setError(null);
            actFrom.setError(null);
            stationFrom = null;
            stationTo = null;
        } else {
            Toast.makeText(getActivity(), "No Bus Found...", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidInfo() {
        boolean isValid = false;
        sFrom = actFrom.getText().toString().trim();
        sTo = actTo.getText().toString().trim();
//        sDate = textViewDate.getText().toString().trim();

        if (sFrom.equals("") || sTo.equals("") || sDate.equals("")) {
            isValid = false;

            if (sDate.equals("")) {
                Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
            }
            if (sTo.equals("")) {
                actTo.setError(sInvalidTo);
                actTo.requestFocus();
                actTo.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        sTo = s.toString();
                        if (sTo.equals("")) {
                            actTo.setError(sInvalidTo);
                        } else {
                            actTo.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            } else {
                actTo.setError(null);
            }
            if (sFrom.equals("")) {
                actFrom.setError(sInvalidFrom);
                actFrom.requestFocus();
                actFrom.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        sFrom = s.toString();
                        if (sFrom.equals("")) {
                            actFrom.setError(sOnlyAlphabets);
                        } else {
                            actFrom.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            } else {
                actFrom.setError(null);
            }
        } else {
            if (stationFrom != null) {
                sFromId = stationFrom.getsStationId();
                isValid = true;
                if (stationTo != null) {
                    sToId = stationTo.getsStationId();
                    isValid = true;
                } else {
                    isValid = false;
                    actTo.requestFocus();
                    actTo.setError(sSelectStationError);
                }
            } else {
                isValid = false;
                actFrom.requestFocus();
                actFrom.setError(sSelectStationError);
            }
        }
        return isValid;
    }


    private void Login() {  //  Call Login api
        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }
        AuthKey = Integer.parseInt(String.valueOf(random));
        showProgress();
        sIP = AppConstants.Status.IP;
        sIMEI = AppConstants.Status.IMEI;
        sAppVersion = AppConstants.Status.APP_VERSION;
        boolean isConnected = AppConstants.isConnected(getActivity());
        if (isConnected) {
            showProgress();
            String sEmail = "smstest";
            String sPassword = "7428";
            String sKeyUrl = AppConstants.ApiNames.OWNER_ID + AuthKey + "l0g!n";
            sPassword = calculateHash(2, sPassword);
            sPassword = calculateHash(4, sPassword);
            sPassword = calculateHash(AuthKey, sPassword);
            String sKey = calculateHash(AuthKey, sKeyUrl);
            String sFinalKey = sEmail + sPassword +
                    sIMEI + sLatitude + sLongitude + sIP + AppConstants.ApiNames.OWNER_ID + AuthKey + "5mS!0g1n";
            sApiKey = calculateHash(AuthKey, sFinalKey);//final sKey
            String sUrl = AppConstants.ApiNames.API_URL + AppConstants.ApiNames.LOGIN + sKey;

            Log.d("log", "Email : " + sEmail);
            Log.d("log", "Password : " + sPassword);
            Log.d("log", "Key : " + sKey);
            Log.d("log", "IMEI : " + sIMEI);
            Log.d("log", "Latitude : " + sLatitude);
            Log.d("log", "Longitude : " + sLongitude);
            Log.d("log", "IP : " + sIP);
            Log.d("log", "Owner ID : " + AppConstants.ApiNames.OWNER_ID);
            Log.d("log", "API Key : " + sApiKey);
            Log.d("log", "Language : " + sLanguage);
            Log.d("log", "Auth Key : " + AuthKey);


            OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
            Call<Agent> callLogin = otappbusApiService.checkLogin(sKey, sEmail, sPassword, AppConstants.ApiNames.OWNER_ID,
                    String.valueOf(AuthKey), sIMEI, sLatitude, sLongitude, sIP, sApiKey, sAppVersion, sLanguage);
            Log.d("Log", "Login URL : " + callLogin.request().url());
            callLogin.enqueue(new Callback<Agent>() {
                @Override
                public void onResponse(Call<Agent> call, Response<Agent> response) {
                    if (response.body().getStatus() == (AppConstants.Status.SUCCESS)) {
                        JSONObject jsonObjectResponse = null;
                        Agent agent = response.body();
                        try {
                            jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                            Log.d("Log", "Response : " + jsonObjectResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Log.d("Log","W : "+agent.)
                        String sLoginHistory = "";
                        try {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.Formats.DATE_FORMAT);
                            sLoginHistory = simpleDateFormat.format(calendar.getTime());
                            AppConstants.Preferences.setStringPreferences(getActivity(), AppConstants.Keys.LOGIN_USERNAME, agent.getsAgentId());
                        } catch (Exception ex) {
                        }
                        agent.setsLoginHistory(sLoginHistory);
                        agent.setsName(agent.getsAgentName());
                        agent.setsUserId(sEmail);
//                        Toast.makeText(LoginActivity.this, "ID : "+agent.getsAgentId(), Toast.LENGTH_SHORT).show();
                        Log.d("Log", "Response :- " + agent.getItemArrayList().toString());
                    } else if (response.body().getStatus() == AppConstants.Status.SESSION_OUT) {    //  Inactive Agent or blocked user
                        Toast.makeText(getActivity(), response.body().getsMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "" + response.body().getsMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Agent> call, Throwable t) {
//                    hideProgress();
                    Toast.makeText(getActivity(), "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {

        }
    }
   /* public void setFromCity(Station mFromCity) {
        if (mServiceCategoryPagerAdapterWithTitle != null){
            SearchBusFragment mFlightOneWayFragment = (SearchBusFragment) mServiceCategoryPagerAdapterWithTitle.getItem(0);
            if (mFlightOneWayFragment != null) {
                mFlightOneWayFragment.setFromCity(mFromCity);
            }

            SearchBusFragment mFlightOneWayFragment1 = (SearchBusFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
            if (mFlightOneWayFragment1 != null) {
                mFlightOneWayFragment1.setFromCity(mFromCity);
            }
        }
    }
    public void setToCity(Station mToCity) {
        if (mServiceCategoryPagerAdapterWithTitle != null){
            SearchBusFragment mFlightOneWayFragment = (SearchBusFragment) mServiceCategoryPagerAdapterWithTitle.getItem(0);
            if (mFlightOneWayFragment != null) {
                mFlightOneWayFragment.setToCity(mToCity);
            }

            SearchBusFragment mFlightOneWayFragment1 = (SearchBusFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
            if (mFlightOneWayFragment1 != null) {
                mFlightOneWayFragment1.setToCity(mToCity);
            }
        }
    }*/
   public void setFromCity(Station mFromSelectedCity) {
       LogUtils.e("", "setFromCity:");
       stationFrom = mFromSelectedCity;
       actFrom.setText(stationFrom.getsName());
       actFrom.setSelection(actFrom.getText().length());
//        for (int i = 0; i < mFlightCityList.size(); i++) {
//            if (mFromSelectedCity.cityName.equals(mFlightCityList.get(i).cityName)) {
//                LogUtils.e("", "i::"+i);
//                etFromCity.setSelection(i);
//            }
//        }

   }

    public void setToCity(Station mToSelectedCity) {
        LogUtils.e("", "setToCity:");
        stationTo = mToSelectedCity;
        actTo.setText(stationTo.getsName());
        actTo.setSelection(actTo.getText().length());
//        for (int i = 0; i < mFlightCityList.size(); i++) {
//            if (mToCity.cityName.equals(mFlightCityList.get(i).cityName)) {
//                LogUtils.e("", "i::"+i);
//                etToCity.setSelection(i);
//            }
//        }
    }
}