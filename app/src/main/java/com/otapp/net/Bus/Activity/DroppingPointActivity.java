package com.otapp.net.Bus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.BDPoints;
import com.otapp.net.Bus.Core.PassengerBus;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.Bus.Core.Seat;
import com.otapp.net.Bus.Core.SeatMap;
import com.otapp.net.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class DroppingPointActivity extends AppCompatActivity {


    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.listViewPoints)
    ListView listViewPoints;
    @BindView(R.id.textViewRoute)
    TextView textViewRoute;
    @BindView(R.id.textViewDate)
    TextView textViewDate;

    @BindString(R.string.select_dropping_point)
    String sDroppingLabel;

    private PassengerBus passengerBus;
    private String sKey;
    private SearchBusDetails searchBusDetails;
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private String sDropping = "";
    private HashMap<String, Seat> hashMapSelectedSeats = new HashMap<>();
    private double Fare = 0;
    private SeatMap seatMap;
    private boolean isReturn = false;
    private ArrayList<BDPoints> bdPointsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropping_point);
        ButterKnife.bind(this);
        imageViewBack.setVisibility(View.VISIBLE);

        passengerBus = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.BUS);
        searchBusDetails = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEARCH_BUS);
        seatMap = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEAT_MAP);
        sKey = getIntent().getExtras().getString(AppConstants.IntentKeys.KEY);
        hashMapSelectedSeats = (HashMap<String, Seat>) getIntent().getSerializableExtra(AppConstants.IntentKeys.SELECTED_SEAT);
        Fare = getIntent().getExtras().getDouble(AppConstants.IntentKeys.FARE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        String sDate = "";
        String sTime = "";
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
            Toast.makeText(DroppingPointActivity.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
        }

        textViewRoute.setText(passengerBus.getsBusOperators());
//        Toast.makeText(this, "" + sDate, Toast.LENGTH_SHORT).show();
        textViewDate.setText(sDate);

//        stringArrayList.add("Same");
//        stringArrayList.add("Himo");
//        stringArrayList.add("Moshi");
//        stringArrayList.add("USA River");
//        stringArrayList.add("Arusha");

        if (searchBusDetails.isReturn()) {
            if (searchBusDetails.getsDropping().equals("")) {
                isReturn = false;
                bdPointsArrayList = searchBusDetails.getStationTo().getBdPointsArrayList();
                stringArrayList.add("Same");
                stringArrayList.add("Himo");
                stringArrayList.add("Moshi");
                stringArrayList.add("USA River");
                stringArrayList.add("Arusha");
                textViewRoute.setText(searchBusDetails.getsFrom() + " - " + searchBusDetails.getsTo());
            } else {
                bdPointsArrayList = searchBusDetails.getStationFrom().getBdPointsArrayList();
                stringArrayList.add("Ubungo");
                stringArrayList.add("Mbezi");
                stringArrayList.add("Maili Moja");
                stringArrayList.add("Chalinze");
                textViewRoute.setText(searchBusDetails.getsReturnFrom() + " - " + searchBusDetails.getsReturnTo());
                isReturn = true;
            }
        } else {
            bdPointsArrayList = searchBusDetails.getStationTo().getBdPointsArrayList();
            isReturn = false;
            textViewRoute.setText(searchBusDetails.getsFrom() + " - " + searchBusDetails.getsTo());
            stringArrayList.add("Same");
            stringArrayList.add("Himo");
            stringArrayList.add("Moshi");
            stringArrayList.add("USA River");
            stringArrayList.add("Arusha");
        }

        if (bdPointsArrayList.size() == 0) {
            if (searchBusDetails.isReturn()) {
                if (!isReturn && searchBusDetails.getsReturnFrom().equals("")) {
                    Intent intent = new Intent(this, ReturnBusActivity.class);
                    intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
                    intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                    intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                    intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                    intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
                    intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                    startActivity(intent);
                    finish();
                } else {
//                Toast.makeText(this, "Else", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, PassengerDetailsActivity.class);
                    intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
                    intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                    intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                    intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                    intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
                    intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                    startActivity(intent);
                    finish();
                }
            } else {
//            Toast.makeText(this, "Elese", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, PassengerDetailsActivity.class);
                intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
                intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
                intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                startActivity(intent);
                finish();
            }
        }
//        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_single_choice, stringArrayList);
        ArrayAdapter<BDPoints> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, bdPointsArrayList);
        listViewPoints.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listViewPoints.setAdapter(stringArrayAdapter);

    }

    @OnItemClick(R.id.listViewPoints)
    void OnItemSelected(int position) {
        sDropping = bdPointsArrayList.get(position).getsBDName();
        if (isReturn) {
            searchBusDetails.setsReturnDropping(sDropping);
        } else {
            searchBusDetails.setsDropping(sDropping);
        }
//        Toast.makeText(this, ""+isReturn, Toast.LENGTH_SHORT).show();
        if (searchBusDetails.isReturn()) {
            if (!isReturn && searchBusDetails.getsReturnBoarding().equals("")) {
                Intent intent = new Intent(this, ReturnBusActivity.class);
                intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
                intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
                intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                startActivity(intent);
            } else {
//                Toast.makeText(this, "Else", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, PassengerDetailsActivity.class);
                intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
                intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
                intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                startActivity(intent);
            }
        } else {
//            Toast.makeText(this, "Elese", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PassengerDetailsActivity.class);
            intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
            intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
            intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
            intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
            intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
            intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
            startActivity(intent);
        }
    }

    @OnClick(R.id.imageViewBack)
    void onBack() {
        finish();
    }
}
