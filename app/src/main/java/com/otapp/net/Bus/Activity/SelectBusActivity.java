package com.otapp.net.Bus.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.otapp.net.Bus.Adapter.BusOperatorNameAdapter;
import com.otapp.net.Bus.Adapter.PassengerBusAdapter;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.AvailableBuses;
import com.otapp.net.Bus.Core.BusOperatorName;
import com.otapp.net.Bus.Core.PassengerBus;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.Bus.Impl.BusOperatorSelectedListener;
import com.otapp.net.R;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.MyPref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectBusActivity extends AppCompatActivity implements BusOperatorSelectedListener {

    @BindView(R.id.textViewRoute)
    TextView textViewRoute;
    @BindView(R.id.recyclerViewBusRoutes)
    RecyclerView recyclerViewBusRoute;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewTo)
    TextView textViewTo;
    @BindView(R.id.textViewDeparture)
    TextView textViewDeparture;
    @BindView(R.id.layoutACBuses)
    LinearLayout layoutAcBuses;
    @BindView(R.id.layoutFilter)
    LinearLayout layoutFilter;
    @BindView(R.id.layoutTimeBuses)
    LinearLayout layoutTimeBuses;
    @BindView(R.id.imgOffAcBuses)
    ImageView imgOffAcBuses;
    @BindView(R.id.imgOnAcBuses)
    ImageView imgOnAcBuses;
    @BindView(R.id.imgOffTimeBuses)
    ImageView imgOffTimeBuses;
    @BindView(R.id.imgOnTimeBuses)
    ImageView imgOnTimeBuses;

    @BindString(R.string.Travel_date)
    String sDateLabel, fromRoute = "", toRoute = "";
    String sDate;
    Date date;
    int bookingTimeStatus = 0;
    String mTimeSlot = "0", mDialogTimeSlot = "0";

    int timeFilterFlag = 0, acFilterFlag = 0,semiLuxuryflag=0,luxuryFlag=0,ordinaryFlag=0;
    String strBusOperatorName = "";

    private RecyclerView.LayoutManager layoutManager;
    private PassengerBusAdapter passengerBusAdapter;
    private ArrayList<AvailableBuses> passengerBusArrayList = new ArrayList<>();
    private ArrayList<AvailableBuses> filterPassengerBusArrayList = new ArrayList<>();
    private ArrayList<AvailableBuses> timePassengerBusArrayList = new ArrayList<>();
    private ArrayList<AvailableBuses> acrPassengerBusArrayList = new ArrayList<>();
    private ArrayList<AvailableBuses> filterBusOperatorArrayList = new ArrayList<>();
    private ArrayList<BusOperatorName> busOperatorNameArrayList = new ArrayList<>();
    private ArrayList<AvailableBuses> luxuryBusArrayList = new ArrayList<>();
    private ArrayList<AvailableBuses> timeSloteArrayList = new ArrayList<>();
    private ArrayList<String> seatingPreference = new ArrayList<>();
    private ArrayList<String> nameArrayList=new ArrayList<>();
    BusOperatorName busOperatorName;

    private SearchBusDetails searchBusDetails;
    private SimpleDateFormat simpleDayFormat;
    private SimpleDateFormat simpleMonthFormat;
    private String sKey;
    private Calendar calendar;
    private BusOperatorNameAdapter busOperatorNameAdapter;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        MyPref.setPref(getApplicationContext(), MyPref.RETURN_UKEY, "");
        MyPref.setPref(getApplicationContext(), MyPref.PREF_BUS_UKEY, "");
        MyPref.setPref(getApplicationContext(), MyPref.ASID, "");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);
        ButterKnife.bind(this);

        searchBusDetails = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEARCH_BUS);
        passengerBusArrayList = getIntent().getExtras().getParcelableArrayList(AppConstants.IntentKeys.BUSES_LIST);
        sKey = getIntent().getExtras().getString(AppConstants.IntentKeys.KEY);
      /*  fromRoute=getIntent().getStringExtra("from");
        toRoute=getIntent().getStringExtra("to");
        sDate=getIntent().getStringExtra("journydate");*/
        fromRoute = searchBusDetails.getsFrom();
        toRoute = searchBusDetails.getsTo();
        sDate = searchBusDetails.getsDate();

        MyPref.setPref(getApplicationContext(), MyPref.PREF_BUS_UKEY, "");
        MyPref.setPref(getApplicationContext(), MyPref.RETURN_UKEY, "");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = simpleDateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat = new SimpleDateFormat("dd");
        simpleMonthFormat = new SimpleDateFormat("MMM");
        simpleDayFormat = new SimpleDateFormat("EEE");
        //textViewDate.setText(simpleDayFormat.format(calendar.getTime())+","+simpleDateFormat.format(calendar.getTime())+simpleDayFormat.format(calendar.getTime()));
        String Date = (String) DateFormat.format("EEE", date) + "," + DateFormat.format("dd", date) + " " + DateFormat.format("MMM", date);
        textViewDate.setText(Date);

//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        String sDate = "";
        String sTime = "";
      /*  try {
            date = simpleDateFormat.parse(searchBusDetails.getsDate());
            simpleDateFormat = new SimpleDateFormat("E,  dd-MMM-yyyy");
            sDate = simpleDateFormat.format(date);
        } catch (ParseException e1) {
            e1.printStackTrace();
            Toast.makeText(SelectBusActivity.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
        }*/


        textViewRoute.setText(fromRoute);
        textViewTo.setText(toRoute);
//        Toast.makeText(this, ""+sDate, Toast.LENGTH_SHORT).show();
        //   textViewDate.setText(sDate);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewBusRoute.setLayoutManager(layoutManager);
        recyclerViewBusRoute.setHasFixedSize(false);


        passengerBusAdapter = new PassengerBusAdapter(this, passengerBusArrayList, searchBusDetails);
        recyclerViewBusRoute.setAdapter(passengerBusAdapter);
        passengerBusAdapter.notifyDataSetChanged();

        if (passengerBusArrayList.size() != 0) {
            textViewDeparture.setText("Showing all " + passengerBusArrayList.size() + " Buses");
        }
        for (int i = 0; i < passengerBusArrayList.size(); i++) {
            if (!busOperatorNameArrayList.contains(passengerBusArrayList.get(i).getBus_operator())) {
                busOperatorName = new BusOperatorName(passengerBusArrayList.get(i).getBus_operator(), false);
                busOperatorNameArrayList.add(busOperatorName);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MyPref.setPref(getApplicationContext(), MyPref.RETURN_UKEY, "");
        MyPref.setPref(getApplicationContext(), MyPref.PREF_BUS_UKEY, "");
        MyPref.setPref(getApplicationContext(), MyPref.ASID, "");

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

    @OnClick(R.id.layoutTimeBuses)
    void onTimeFilter() {
        if (timeFilterFlag == 0) {
            timeFilterFlag = 1;
            imgOnTimeBuses.setVisibility(View.VISIBLE);
            imgOffTimeBuses.setVisibility(View.GONE);
            if (acFilterFlag == 1) {
                filterPassengerBusArrayList = acrPassengerBusArrayList;
                for (int i = 0; i < acrPassengerBusArrayList.size(); i++) {
                    String time[] = acrPassengerBusArrayList.get(i).dep_time.split(":");
                    int depTime = Integer.parseInt(time[0]);
                    if (depTime >= 18) {
                        filterPassengerBusArrayList.add(acrPassengerBusArrayList.get(i));
                    }
                }
                passengerBusAdapter = new PassengerBusAdapter(this, filterPassengerBusArrayList, searchBusDetails);
                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                passengerBusAdapter.notifyDataSetChanged();
                if (passengerBusArrayList.size() != 0) {
                    textViewDeparture.setText("Showing all " + filterPassengerBusArrayList.size() + " Buses");
                }

            } else {
                timePassengerBusArrayList = new ArrayList<>();
                for (int i = 0; i < passengerBusArrayList.size(); i++) {
                    String time[] = passengerBusArrayList.get(i).dep_time.split(":");
                    int depTime = Integer.parseInt(time[0]);
                    if (depTime >= 18) {
                        timePassengerBusArrayList.add(passengerBusArrayList.get(i));
                    }
                }
                passengerBusAdapter = new PassengerBusAdapter(this, timePassengerBusArrayList, searchBusDetails);
                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                passengerBusAdapter.notifyDataSetChanged();
                if (passengerBusArrayList.size() != 0) {
                    textViewDeparture.setText("Showing all " + timePassengerBusArrayList.size() + " Buses");
                }
            }
        } else {
            timeFilterFlag = 0;
            imgOffTimeBuses.setVisibility(View.VISIBLE);
            imgOnTimeBuses.setVisibility(View.GONE);
            if (acFilterFlag == 1) {
                acrPassengerBusArrayList = new ArrayList<>();
                for (int i = 0; i < passengerBusArrayList.size(); i++) {
                    if (passengerBusArrayList.get(i).getIs_ac().equals("1")) {
                        acrPassengerBusArrayList.add(passengerBusArrayList.get(i));
                    }
                }
                passengerBusAdapter = new PassengerBusAdapter(this, acrPassengerBusArrayList, searchBusDetails);
                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                if (passengerBusArrayList.size() != 0) {
                    textViewDeparture.setText("Showing all " + acrPassengerBusArrayList.size() + " Buses");
                }
            } else {
                passengerBusAdapter = new PassengerBusAdapter(this, passengerBusArrayList, searchBusDetails);
                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                passengerBusAdapter.notifyDataSetChanged();
                if (passengerBusArrayList.size() != 0) {
                    textViewDeparture.setText("Showing all " + passengerBusArrayList.size() + " Buses");
                }
            }
        }
    }

    @OnClick(R.id.layoutACBuses)
    void onAcBusSelect() {
        if (acFilterFlag == 0) {
            acFilterFlag = 1;
            imgOnAcBuses.setVisibility(View.VISIBLE);
            imgOffAcBuses.setVisibility(View.GONE);
            if (timeFilterFlag == 1) {
                filterPassengerBusArrayList = timePassengerBusArrayList;
                for (int i = 0; i < timePassengerBusArrayList.size(); i++) {
                    if (passengerBusArrayList.get(i).getIs_ac().equals("1")) {
                        filterPassengerBusArrayList.add(timePassengerBusArrayList.get(i));
                    }
                }
                passengerBusAdapter = new PassengerBusAdapter(this, filterPassengerBusArrayList, searchBusDetails);
                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                passengerBusAdapter.notifyDataSetChanged();
                if (passengerBusArrayList.size() != 0) {
                    textViewDeparture.setText("Showing all " + filterPassengerBusArrayList.size() + " Buses");
                }

            } else {
                acrPassengerBusArrayList = new ArrayList<>();
                for (int i = 0; i < passengerBusArrayList.size(); i++) {
                    if (passengerBusArrayList.get(i).getIs_ac().equals("1")) {
                        acrPassengerBusArrayList.add(passengerBusArrayList.get(i));
                    }
                }
                passengerBusAdapter = new PassengerBusAdapter(this, acrPassengerBusArrayList, searchBusDetails);
                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                passengerBusAdapter.notifyDataSetChanged();
                if (passengerBusArrayList.size() != 0) {
                    textViewDeparture.setText("Showing all " + acrPassengerBusArrayList.size() + " Buses");
                }
            }
        } else {
            acFilterFlag = 0;
            imgOffAcBuses.setVisibility(View.VISIBLE);
            imgOnAcBuses.setVisibility(View.GONE);
            if (timeFilterFlag == 1) {
                timePassengerBusArrayList = new ArrayList<>();
                for (int i = 0; i < passengerBusArrayList.size(); i++) {
                    String time[] = passengerBusArrayList.get(i).dep_time.split(":");
                    int depTime = Integer.parseInt(time[0]);
                    if (depTime >= 18) {
                        timePassengerBusArrayList.add(passengerBusArrayList.get(i));
                    }
                }
                passengerBusAdapter = new PassengerBusAdapter(this, timePassengerBusArrayList, searchBusDetails);
                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                passengerBusAdapter.notifyDataSetChanged();
                if (passengerBusArrayList.size() != 0) {
                    textViewDeparture.setText("Showing all " + timePassengerBusArrayList.size() + " Buses");

                }
            } else {
                passengerBusAdapter = new PassengerBusAdapter(this, passengerBusArrayList, searchBusDetails);
                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                passengerBusAdapter.notifyDataSetChanged();
                if (passengerBusArrayList.size() != 0) {
                    textViewDeparture.setText("Showing all " + passengerBusArrayList.size() + " Buses");
                }
            }
        }
    }

    @OnClick(R.id.layoutFilter)
    void layoutFilter() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bus_filter);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvBack = dialog.findViewById(R.id.tvBack);

        TextView tvMorning;
        tvMorning = dialog.findViewById(R.id.tvMorning);

        TextView tvDay;
        tvDay = dialog.findViewById(R.id.tvDay);

        TextView tvEvening;
        tvEvening = dialog.findViewById(R.id.tvEvening);

        TextView tvMoon;
        tvMoon = dialog.findViewById(R.id.tvMoon);

        TextView tvApplyFilters;
        tvApplyFilters = dialog.findViewById(R.id.tvApplyFilters);

        LinearLayout semiLuxuryLayout = dialog.findViewById(R.id.layoutSemiLuxury);
        LinearLayout luxuryLayout = dialog.findViewById(R.id.layoutLuxury);
        LinearLayout ordinaryLayout = dialog.findViewById(R.id.layoutOrdinary);
        RadioButton semiLuxuryRadio= dialog.findViewById(R.id.semiLuxuryRadioButton);
        RadioButton luxuryRadio= dialog.findViewById(R.id.luxuryRadioButton);
        RadioButton ordinaryRadio=dialog.findViewById(R.id.ordinaryRadioButton);

        RecyclerView recyclerViewBusOperator = dialog.findViewById(R.id.busOpreratorRecycler);

        recyclerViewBusOperator.setHasFixedSize(true);
        recyclerViewBusOperator.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ArrayList<String> busTypeList = new ArrayList<>();
      //  busOperatorNameArrayList=new ArrayList<>();

        if(luxuryFlag==1){
            luxuryRadio.setChecked(true);
        }
        if(semiLuxuryflag==1){
            semiLuxuryRadio.setChecked(true);
        }
        if (bookingTimeStatus == 1) {
            tvMorning.setBackgroundColor(getResources().getColor(R.color.gray_e1));
            tvDay.setBackgroundColor(getResources().getColor(R.color.white));
            tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
            tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
        } else if (bookingTimeStatus == 2) {
            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
            tvDay.setBackgroundColor(getResources().getColor(R.color.gray_e1));
            tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
            tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
        } else if (bookingTimeStatus == 3) {
            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
            tvDay.setBackgroundColor(getResources().getColor(R.color.white));
            tvEvening.setBackgroundColor(getResources().getColor(R.color.gray_e1));
            tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
        } else if (bookingTimeStatus == 4) {
            tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
            tvDay.setBackgroundColor(getResources().getColor(R.color.white));
            tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
            tvMoon.setBackgroundColor(getResources().getColor(R.color.gray_e1));
        } else {
            bookingTimeStatus = 0;
        }


        for (int i = 0; i < passengerBusArrayList.size(); i++) {

            String strbusType[] = passengerBusArrayList.get(i).bus_type.split(" ");
            String type = "";
            for (int j = 1; j < strbusType.length; j++) {
                type = type +strbusType[j];
            }
            if (!busTypeList.contains(type)) {
                Log.d("Log",""+passengerBusArrayList.get(i).bus_type+"   "+type);
                busTypeList.add(type);
            }
            Log.d("Log","bustypeList "+busTypeList.size());
        }
        if (busTypeList.contains("SemiLuxury")) {
            semiLuxuryLayout.setVisibility(View.VISIBLE);
        }else {
            semiLuxuryLayout.setVisibility(View.GONE);
        }
        if (busTypeList.contains("Luxury")) {
            luxuryLayout.setVisibility(View.VISIBLE);
        }else {
            luxuryLayout.setVisibility(View.GONE);
        }
        if (busTypeList.contains("Ordinary")) {
            ordinaryLayout.setVisibility(View.VISIBLE);
        }else {
            ordinaryLayout.setVisibility(View.GONE);
        }

       /* for (int i = 0; i < passengerBusArrayList.size(); i++) {
            if (!busOperatorNameArrayList.contains(passengerBusArrayList.get(i).getBus_operator())) {
                busOperatorName = new BusOperatorName(passengerBusArrayList.get(i).getBus_operator(), false);
                busOperatorNameArrayList.add(busOperatorName);
            }
        }*/

        busOperatorNameAdapter = new BusOperatorNameAdapter(getApplicationContext(), busOperatorNameArrayList, this);
        recyclerViewBusOperator.setAdapter(busOperatorNameAdapter);
        busOperatorNameAdapter.notifyDataSetChanged();

        if (mDialogTimeSlot.equals("1")) {
            mDialogTimeSlot = "0";
            tvMorning.performClick();
        } else if (mDialogTimeSlot.equals("2")) {
            mDialogTimeSlot = "0";
            tvDay.performClick();
        } else if (mDialogTimeSlot.equals("3")) {
            mDialogTimeSlot = "0";
            tvEvening.performClick();
        } else if (mDialogTimeSlot.equals("4")) {
            mDialogTimeSlot = "0";
            tvMoon.performClick();
        }


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getActivity(), "Status : "+bookingTimeStatus, Toast.LENGTH_SHORT).show();
                if (bookingTimeStatus == 1) {
                    tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                    bookingTimeStatus = 0;
                } else {
                   // tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                    tvMorning.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                    tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                    tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                    tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                    bookingTimeStatus = 1;
                    /*if (mDialogTimeSlot.equals("1")) {
                        mDialogTimeSlot = "0";
                    } else {
                        mDialogTimeSlot = "1";
                        tvMorning.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                    }*/

                }
            }
        });
        tvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookingTimeStatus == 2) {
                    tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                    bookingTimeStatus = 0;
                } else {
                    bookingTimeStatus = 2;
                    tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                    tvDay.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                  //  tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                    tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                    tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                   /* if (mDialogTimeSlot.equals("2")) {
                        mDialogTimeSlot = "0";
                    } else {
                        mDialogTimeSlot = "2";
                        tvDay.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                    }*/
                }
            }
        });
        tvEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookingTimeStatus == 3) {
                    tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                    bookingTimeStatus = 0;
                } else {
                    bookingTimeStatus = 3;
                   tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                    tvEvening.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                    tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                   // tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                    tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                   /* if (mDialogTimeSlot.equals("3")) {
                        mDialogTimeSlot = "0";
                    } else {
                        mDialogTimeSlot = "3";
                        tvEvening.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                    }*/
                }
            }
        });
        tvMoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookingTimeStatus == 4) {
                    tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                    bookingTimeStatus = 0;
                } else {
                    bookingTimeStatus = 4;

                    tvMorning.setBackgroundColor(getResources().getColor(R.color.white));
                    tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                    tvEvening.setBackgroundColor(getResources().getColor(R.color.white));
                 //   tvMoon.setBackgroundColor(getResources().getColor(R.color.white));
                    tvMoon.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                   /* if (mDialogTimeSlot.equals("4")) {
                        mDialogTimeSlot = "0";
                    } else {
                        mDialogTimeSlot = "4";
                        tvMoon.setBackgroundColor(getResources().getColor(R.color.gray_e1));
                    }*/
                }
            }
        });



        luxuryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(luxuryFlag==0){
                    luxuryRadio.setChecked(true);
                    luxuryFlag=1;
                }else {
                    luxuryRadio.setChecked(false);
                    luxuryFlag=0;
                }
            }
        });
        semiLuxuryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(semiLuxuryflag==0){
                    semiLuxuryRadio.setChecked(true);
                    semiLuxuryflag=1;
                }else {
                    semiLuxuryRadio.setChecked(false);
                    semiLuxuryflag=0;
                }
            }
        });

        tvApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(acFilterFlag==1 && timeFilterFlag==1){
                        filterBusOperatorArrayList=new ArrayList<>();
                        if(nameArrayList.size()>0){
                            for(int i=0;i<nameArrayList.size();i++){
                                for(int j=0;j<filterPassengerBusArrayList.size();j++){
                                    if(filterBusOperatorArrayList.get(j).getBus_operator().equals(nameArrayList.get(i))){
                                        filterBusOperatorArrayList.add(filterPassengerBusArrayList.get(j));
                                    }
                                }
                            }
                            if(luxuryFlag==1){
                              luxuryBusArrayList= new ArrayList<>();
                                for(int i=0;i<filterBusOperatorArrayList.size();i++){
                                    String strbusType[] = filterBusOperatorArrayList.get(i).bus_type.split(" ");
                                    String type = "";
                                    for (int j = 1; j < strbusType.length; j++) {
                                        type = type + strbusType[i];
                                    }
                                    if(type.equals("Luxury")){
                                        luxuryBusArrayList.add(filterBusOperatorArrayList.get(i));
                                    }
                                }
                                passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), luxuryBusArrayList, searchBusDetails);
                                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                                passengerBusAdapter.notifyDataSetChanged();
                                if (passengerBusArrayList.size() != 0) {
                                    textViewDeparture.setText("Showing all " + luxuryBusArrayList.size() + " Buses");
                                }
                            }else {
                                passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), filterBusOperatorArrayList, searchBusDetails);
                                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                                passengerBusAdapter.notifyDataSetChanged();
                                if (passengerBusArrayList.size() != 0) {
                                    textViewDeparture.setText("Showing all " + filterBusOperatorArrayList.size() + " Buses");
                                }
                            }
                        }

                }else if(acFilterFlag==1){
                        filterBusOperatorArrayList=new ArrayList<>();
                        if(nameArrayList.size()>0){
                            for(int i=0;i<nameArrayList.size();i++){
                                for(int j=0;j<acrPassengerBusArrayList.size();j++){
                                    if(acrPassengerBusArrayList.get(j).getBus_operator().equals(nameArrayList.get(i))){
                                        filterBusOperatorArrayList.add(acrPassengerBusArrayList.get(j));
                                    }
                                }
                            }
                            passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), filterBusOperatorArrayList, searchBusDetails);
                            recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                            passengerBusAdapter.notifyDataSetChanged();
                            if (passengerBusArrayList.size() != 0) {
                                textViewDeparture.setText("Showing all " + filterBusOperatorArrayList.size() + " Buses");
                            }
                        }
                    }else if(timeFilterFlag==1){
                        filterBusOperatorArrayList=new ArrayList<>();
                        if(nameArrayList.size()>0){
                            for(int i=0;i<nameArrayList.size();i++){
                                for(int j=0;j<timePassengerBusArrayList.size();j++){
                                    if(timePassengerBusArrayList.get(j).getBus_operator().equals(nameArrayList.get(i))){
                                        filterBusOperatorArrayList.add(timePassengerBusArrayList.get(j));
                                    }
                                }
                            }
                            passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), filterBusOperatorArrayList, searchBusDetails);
                            recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                            passengerBusAdapter.notifyDataSetChanged();
                            if (passengerBusArrayList.size() != 0) {
                                textViewDeparture.setText("Showing all " + filterBusOperatorArrayList.size() + " Buses");
                            }
                        }
                    }else {
                        filterBusOperatorArrayList= new ArrayList<>();
                        if(nameArrayList.size()>0){
                            for(int i=0;i<nameArrayList.size();i++){
                                for(int j=0;j<passengerBusArrayList.size();j++){
                                    if(passengerBusArrayList.get(j).getBus_operator().equals(nameArrayList.get(i))){
                                        filterBusOperatorArrayList.add(passengerBusArrayList.get(j));
                                    }
                                }
                            }
                            passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), filterBusOperatorArrayList, searchBusDetails);
                            recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                            passengerBusAdapter.notifyDataSetChanged();
                            if (passengerBusArrayList.size() != 0) {
                                textViewDeparture.setText("Showing all " + filterBusOperatorArrayList.size() + " Buses");
                            }
                        }else  if(nameArrayList.size()==0) {
                            if (luxuryFlag == 1 && semiLuxuryflag==1) {
                               // Toast.makeText(SelectBusActivity.this, "luxry semi both", Toast.LENGTH_SHORT).show();
                                luxuryBusArrayList = new ArrayList<>();
                                for (int i = 0; i < passengerBusArrayList.size(); i++) {
                                    String strbusType[] = passengerBusArrayList.get(i).bus_type.split(" ");
                                    String type = "";
                                    for (int j = 1; j < strbusType.length; j++) {
                                        type = type + strbusType[j];
                                    }
                                    if (type.equals("Luxury")||type.equals("SemiLuxury")) {
                                        luxuryBusArrayList.add(passengerBusArrayList.get(i));
                                    }
                                }
                                passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), luxuryBusArrayList, searchBusDetails);
                                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                                passengerBusAdapter.notifyDataSetChanged();
                                if (passengerBusArrayList.size() != 0) {
                                    textViewDeparture.setText("Showing all " + luxuryBusArrayList.size() + " Buses");
                                }
                            }else if(luxuryFlag==1){
                              //  Toast.makeText(SelectBusActivity.this, "luxry  ", Toast.LENGTH_SHORT).show();

                                luxuryBusArrayList = new ArrayList<>();
                                for (int i = 0; i < passengerBusArrayList.size(); i++) {
                                    String strbusType[] = passengerBusArrayList.get(i).bus_type.split(" ");
                                    String type = "";
                                    for (int j = 1; j < strbusType.length; j++) {
                                        type = type + strbusType[i];
                                    }
                                    if (type.equals("Luxury")) {
                                        luxuryBusArrayList.add(passengerBusArrayList.get(i));
                                    }
                                }
                                passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), luxuryBusArrayList, searchBusDetails);
                                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                                passengerBusAdapter.notifyDataSetChanged();
                                if (passengerBusArrayList.size() != 0) {
                                    textViewDeparture.setText("Showing all " + luxuryBusArrayList.size() + " Buses");
                                }
                            }else if(semiLuxuryflag==1){
                               // Toast.makeText(SelectBusActivity.this, " semi ", Toast.LENGTH_SHORT).show();

                                luxuryBusArrayList = new ArrayList<>();
                                for (int i = 0; i < passengerBusArrayList.size(); i++) {
                                    String strbusType[] = passengerBusArrayList.get(i).bus_type.split(" ");
                                    String type = "";
                                    for (int j = 1; j < strbusType.length; j++) {
                                        type = type + strbusType[j];
                                    }
                                    if (type.equals("SemiLuxury")) {
                                        luxuryBusArrayList.add(passengerBusArrayList.get(i));
                                    }
                                }
                                passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), luxuryBusArrayList, searchBusDetails);
                                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                                passengerBusAdapter.notifyDataSetChanged();
                                if (passengerBusArrayList.size() != 0) {
                                    textViewDeparture.setText("Showing all " + luxuryBusArrayList.size() + " Buses");
                                }
                            }else {  if(bookingTimeStatus==1){
                                Log.d("Log","Booking time slot 1");
                                timeSloteArrayList= new ArrayList<>();
                                for (int i = 0; i < passengerBusArrayList.size(); i++) {
                                    String time[] = passengerBusArrayList.get(i).dep_time.split(":");
                                    int depTime = Integer.parseInt(time[0]);
                                    if (depTime >= 6 && depTime<=12 ) {
                                        timeSloteArrayList.add(passengerBusArrayList.get(i));
                                    }
                                }
                                passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), timeSloteArrayList, searchBusDetails);
                                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                                passengerBusAdapter.notifyDataSetChanged();
                                if (passengerBusArrayList.size() != 0) {
                                    textViewDeparture.setText("Showing all " + timeSloteArrayList.size() + " Buses");
                                }
                            }else if(bookingTimeStatus==2){
                                timeSloteArrayList= new ArrayList<>();
                                for (int i = 0; i < passengerBusArrayList.size(); i++) {
                                    String time[] = passengerBusArrayList.get(i).dep_time.split(":");
                                    int depTime = Integer.parseInt(time[0]);
                                    if (depTime >= 12 && depTime<=18 ) {
                                        timeSloteArrayList.add(passengerBusArrayList.get(i));
                                    }
                                }
                                passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), timeSloteArrayList, searchBusDetails);
                                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                                passengerBusAdapter.notifyDataSetChanged();
                                if (passengerBusArrayList.size() != 0) {
                                    textViewDeparture.setText("Showing all " + timeSloteArrayList.size() + " Buses");
                                }
                            }else if(bookingTimeStatus==3){
                                timeSloteArrayList= new ArrayList<>();
                                for (int i = 0; i < passengerBusArrayList.size(); i++) {
                                    String time[] = passengerBusArrayList.get(i).dep_time.split(":");
                                    int depTime = Integer.parseInt(time[0]);
                                    if (depTime >= 18 && depTime<=24 ) {
                                        timeSloteArrayList.add(passengerBusArrayList.get(i));
                                    }
                                }
                                passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), timeSloteArrayList, searchBusDetails);
                                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                                passengerBusAdapter.notifyDataSetChanged();
                                if (passengerBusArrayList.size() != 0) {
                                    textViewDeparture.setText("Showing all " + timeSloteArrayList.size() + " Buses");
                                }
                            }else if(bookingTimeStatus==4){
                                timeSloteArrayList= new ArrayList<>();
                                for (int i = 0; i < passengerBusArrayList.size(); i++) {
                                    String time[] = passengerBusArrayList.get(i).dep_time.split(":");
                                    int depTime = Integer.parseInt(time[0]);
                                    if (depTime >= 00 && depTime<=06 ) {
                                        timeSloteArrayList.add(passengerBusArrayList.get(i));
                                    }
                                }
                                passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), timeSloteArrayList, searchBusDetails);
                                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                                passengerBusAdapter.notifyDataSetChanged();
                                if (passengerBusArrayList.size() != 0) {
                                    textViewDeparture.setText("Showing all " + timeSloteArrayList.size() + " Buses");
                                }
                            }else {


                                passengerBusAdapter = new PassengerBusAdapter(getApplicationContext(), passengerBusArrayList, searchBusDetails);
                                recyclerViewBusRoute.setAdapter(passengerBusAdapter);
                                passengerBusAdapter.notifyDataSetChanged();
                                if (passengerBusArrayList.size() != 0) {
                                    textViewDeparture.setText("Showing all " + passengerBusArrayList.size() + " Buses");
                                }
                            }
                            }
                        }

                    }



                    dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    public void selectedBusOperator(String sBusOperatorName,int flag) {
        strBusOperatorName = sBusOperatorName;

        if(flag==1){
            if(!nameArrayList.contains(strBusOperatorName)) {
                nameArrayList.add(strBusOperatorName);
            }
        }else {
            nameArrayList.remove(strBusOperatorName);
        }
     //   Toast.makeText(this, "Bus Opeartor size " + nameArrayList.size(), Toast.LENGTH_SHORT).show();
/*
        if (acFilterFlag == 1 && timeFilterFlag == 1) {
            for (int i = 0; i < filterPassengerBusArrayList.size(); i++) {
                if (filterPassengerBusArrayList.get(i).getBus_operator().equals(strBusOperatorName)) {
                    filterBusOperatorArrayList.add(filterPassengerBusArrayList.get(i));
                }
            }
            passengerBusAdapter = new PassengerBusAdapter(this, filterBusOperatorArrayList, searchBusDetails);
            recyclerViewBusRoute.setAdapter(passengerBusAdapter);
            passengerBusAdapter.notifyDataSetChanged();
            if (passengerBusArrayList.size() != 0) {
                textViewDeparture.setText("Showing all " + timePassengerBusArrayList.size() + " Buses");
            }

        }
*/
        busOperatorNameAdapter.notifyDataSetChanged();
    }
}
