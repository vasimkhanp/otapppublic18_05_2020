package com.otapp.net.Bus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.otapp.net.Bus.Adapter.BoardingPointAdapter;
import com.otapp.net.Bus.Adapter.DroppingPointAdapter;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.AvailableBuses;
import com.otapp.net.Bus.Core.BDPoints;
import com.otapp.net.Bus.Core.PassengerBus;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.Bus.Core.Seat;
import com.otapp.net.Bus.Core.SeatMap;
import com.otapp.net.Bus.Impl.BoardingPointInterface;
import com.otapp.net.Bus.Impl.DroppingPointInterface;
import com.otapp.net.R;
import com.otapp.net.utils.MyPref;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BoardingPointActivity extends AppCompatActivity implements BoardingPointInterface, DroppingPointInterface {

    @BindView(R.id.listViewPoints)
    ListView listViewPoints;
    @BindView(R.id.listViewDroppingPoints)
    ListView listViewDroppingPoints;
    @BindView(R.id.textViewRoute)
    TextView textViewRoute;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewBPChange)
    TextView textViewBPChange;
    @BindView(R.id.textViewDPChange)
    TextView textViewDPChange;
    @BindView(R.id.linearLayoutBoardingPoint)
    LinearLayout linearLayoutBoardingPoint;
    @BindView(R.id.linearLayoutDroppingPont)
    LinearLayout linearLayoutDroppingPont;
    @BindView(R.id.linearLayoutDPDetails)
    LinearLayout linearLayoutDPDetails;

    @BindView(R.id.textViewBP)
    TextView textViewBP;
    @BindView(R.id.textViewBPTime)
    TextView textViewBPTime;
    @BindView(R.id.textViewBPDate)
    TextView textViewBPDate;

    @BindView(R.id.textViewDP)
    TextView textViewDP;
    @BindView(R.id.textViewDPTime)
    TextView textViewDPTime;
    @BindView(R.id.textViewDPDate)
    TextView textViewDPDate;

    @BindView(R.id.linearLayoutBPDetails)
    LinearLayout linearLayoutBPDetails;

    @BindString(R.string.select_boarding_point)
    String sBoardingLabel;
    @BindView(R.id.textViewSeatDetails)
    TextView tvSeatDetails;
    @BindView(R.id.textViewTotalFare)
    TextView tvTotalFare;

    private AvailableBuses availableBuses;
    private AvailableBuses availableReturnBuses;
    private String sKey;
    private String sBoarding = "";
    private String sDropping = "";
    private SearchBusDetails searchBusDetails;
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private HashMap<String, Seat> hashMapSelectedSeats;
    private HashMap<String, Seat> hashMapSelectedReturnSeats;
    private String Fare = "";
    private SeatMap seatMap;
    private boolean isReturn = false;
    private ArrayList<BDPoints> bdPointsArrayList = new ArrayList<>();
    private ArrayList<BDPoints> droppingPointsArrayList = new ArrayList<>();
    private ArrayList<Seat> arrayListOnWords = new ArrayList<>();
    private ArrayList<Seat> arrayListReturn = new ArrayList<>();
    BDPoints bdPoints;
    Seat seat;

    @BindView(R.id.imageViewHeaderBack)
    ImageView imageViewHeaderBack;
    @BindView(R.id.textViewHeaderTitle)
    TextView textViewHeaderTitle;

    @OnClick(R.id.imageViewHeaderBack)
    void onBack() {
        finish();
    }

    String sKKey = "0";
    String[] boardingPoint;
    String[] droppingPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding_point);
        ButterKnife.bind(this);
        textViewHeaderTitle.setText("Boarding & Drop Point");
        textViewDPChange.setVisibility(View.GONE);
        linearLayoutDroppingPont.setVisibility(View.GONE);
        textViewBPChange.setVisibility(View.GONE);

        availableBuses = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.BUS);
        availableReturnBuses = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.RETURN_BUS);
        searchBusDetails = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEARCH_BUS);
        seatMap = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEAT_MAP);
       /* sKey = getIntent().getExtras().getString(AppConstants.IntentKeys.KEY);
        sKKey = getIntent().getExtras().getString("KEYRE");*/
        Log.d("Log", "Boarding aciti frpm " + searchBusDetails.getsReturnFrom());
        Log.d("Log", "Boarding aciti to " + searchBusDetails.getsReturnTo());
        if (searchBusDetails.isRetrunActivityCalled()) {
            availableReturnBuses = searchBusDetails.getAvailableReturnBuses();
            //  Toast.makeText(getApplicationContext(), "Bus NAme "+availableReturnBuses.getBus_name(), Toast.LENGTH_SHORT).show();
        }



       /* hashMapSelectedSeats = (HashMap<String, Seat>) getIntent().getSerializableExtra(AppConstants.IntentKeys.SELECTED_SEAT);
        hashMapSelectedReturnSeats = (HashMap<String, Seat>) getIntent().getSerializableExtra(AppConstants.IntentKeys.SELECTED_RETURN_SEAT);*/
       /*if(hashMapSelectedSeats==null) {
       hashMapSelectedSeats= new HashMap<>();
           hashMapSelectedSeats=searchBusDetails.getHashMapSelectedSeats();
           Log.d("Log","Hash size= "+hashMapSelectedSeats.size());
       }*/
      /* if(!searchBusDetails.getsBoarding().equals("")) {
           arrayListOnWords = searchBusDetails.getArrayListOnWords();
           Log.d("Log", "Hash size= " + arrayListOnWords.size());
       }*/
        Fare = getIntent().getExtras().getString(AppConstants.IntentKeys.FARE);
        tvTotalFare.setText("" + Fare);
        if (Fare != null) {
            arrayListOnWords = searchBusDetails.getArrayListOnWords();
            //  Toast.makeText(this, "onwrd size "+arrayListOnWords.size(), Toast.LENGTH_SHORT).show();
        }
        if (!MyPref.getPref(getApplicationContext(), MyPref.RETURN_UKEY, "").equals("")) {
            arrayListReturn = searchBusDetails.getArrayListReturn();
        }

        if (Fare != null) {
            if (arrayListOnWords != null) {
                if (arrayListOnWords.size() > 0) {
                    for (int i = 0; i < arrayListOnWords.size(); i++) {
                        seat = arrayListOnWords.get(i);
                        if (seat.getsIsSelected().equals(AppConstants.Status.SELECTED)) {
                            String seatNameArray[] = seat.getsSeatNo().split("-");
                            String seatName = "";
                            if (seatNameArray.length > 0) {
                                seatName = seatNameArray[3];
                            }
                            if (sSelectedSeats.equals("")) {
                                sSelectedSeats += seatName;
                            } else {
                                sSelectedSeats += "," + seatName;
                            }
                        }
                    }
                }
            }
        }
        tvSeatDetails.setText(sSelectedSeats);


        availableBuses = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.BUS);
        boardingPoint = getIntent().getStringArrayExtra("boardigPoint");
        droppingPoint = getIntent().getStringArrayExtra("dropingPoint");

        if (searchBusDetails.isRetrunActivityCalled()) {
            boardingPoint = availableReturnBuses.getStrBoardingPoints();
            droppingPoint = availableReturnBuses.getStrDropingPoints();
            /*if(boardingPoint.length==0){
                textViewBP.setText(searchBusDetails.getsReturnFrom());
                textViewDP.setText(searchBusDetails.getsReturnTo());
            }*/
           /* if(boardingPoint.length==0){
                boardingPoint[0]=searchBusDetails.getsReturnFrom();
            }if(droppingPoint.length==0){
                droppingPoint[0]=searchBusDetails.getsReturnTo();
            }*/
        } else {
            boardingPoint = availableBuses.getStrBoardingPoints();
            droppingPoint = availableBuses.getStrDropingPoints();
            /*if(boardingPoint.length==0){
                textViewBP.setText(searchBusDetails.getsFrom());
                textViewDP.setText(searchBusDetails.getsTo());
            }*/
           /* if(boardingPoint.length==0){
                boardingPoint[0]=searchBusDetails.getsFrom();
            }if(droppingPoint.length==0){
                droppingPoint=searchBusDetails.getsTo();
            }*/
        }
        Log.d("Log", "bordig poing lenth " + boardingPoint);
        Log.d("Log", "droping poiny lengyh " + droppingPoint);
        //getSelectedSeats();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        String sDate = "";
        String sTime = "";

        if (boardingPoint.length > 0) {
            bdPointsArrayList = new ArrayList<>();
            for (int i = 0; i < boardingPoint.length; i++) {
                byte[] data = Base64.decode(boardingPoint[i], Base64.DEFAULT);
                String text = null;
                try {
                    text = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                bdPoints = new BDPoints("" + i, text);
                bdPointsArrayList.add(bdPoints);
            }
        } else {
            if (searchBusDetails.isRetrunActivityCalled()) {
                bdPoints = new BDPoints("0", searchBusDetails.getsReturnFrom());
                bdPointsArrayList.add(bdPoints);
            } else {
                bdPoints = new BDPoints("0", searchBusDetails.getsFrom());
                bdPointsArrayList.add(bdPoints);
            }
        }
        if (droppingPoint.length > 0) {
            droppingPointsArrayList = new ArrayList<>();

            for (int i = 0; i < droppingPoint.length; i++) {
                byte[] data = Base64.decode(droppingPoint[i], Base64.DEFAULT);
                String text = null;
                try {
                    text = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                bdPoints = new BDPoints("" + i, text);

                droppingPointsArrayList.add(bdPoints);
            }
            //   Toast.makeText(this, "droping size "+bdPointsArrayList.size(), Toast.LENGTH_SHORT).show();
        } else {
            if (searchBusDetails.isRetrunActivityCalled()) {
                bdPoints = new BDPoints("0", searchBusDetails.getsReturnTo());
                droppingPointsArrayList.add(bdPoints);
            } else {
                bdPoints = new BDPoints("0", searchBusDetails.getsTo());
                droppingPointsArrayList.add(bdPoints);
            }
        }



//        if (bdPointsArrayList.size() == 0) {
//            if (searchBusDetails.isReturn()) {
//                if (!isReturn && searchBusDetails.getsReturnFrom().equals("")) {
//                    Intent intent = new Intent(this, ReturnBusActivity.class);
//                    intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
//                    intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
//                    intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
//                    intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
//                    intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
//                    intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
//                    startActivity(intent);
//                    finish();
//                } else {
////                Toast.makeText(this, "Else", Toast.LENGTH_SHORT).show();
//                    if (hashMapSelectedSeats.size() == 0) {
//                        Intent intent = new Intent(this, SeatSelectionActivity.class);
//                        intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
//                        intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
//                        intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(this, PassengerDetailsActivity.class);
//                        intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
//                        intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
//                        intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
//                        intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
//                        intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
//                        intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            } else {
////            Toast.makeText(this, "Elese", Toast.LENGTH_SHORT).show();
//                if (hashMapSelectedSeats.size() == 0) {
//                    Intent intent = new Intent(this, SeatSelectionActivity.class);
//                    intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
//                    intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
//                    intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(this, PassengerDetailsActivity.class);
//                    intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
//                    intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
//                    intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
//                    intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
//                    intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
//                    intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        }


//        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, stringArrayList);
        BoardingPointAdapter boardingPointAdapter = new BoardingPointAdapter(this, bdPointsArrayList, this);
        listViewPoints.setAdapter(boardingPointAdapter);


        DroppingPointAdapter droppingPointAdapter = new DroppingPointAdapter(this, droppingPointsArrayList, this);
        listViewDroppingPoints.setAdapter(droppingPointAdapter);
    }

    @OnClick(R.id.textViewBPChange)
    void onBPChange() {
        listViewPoints.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.textViewDPChange)
    void onDPChange() {
        listViewDroppingPoints.setVisibility(View.VISIBLE);
    }

/*
    @OnItemClick(R.id.listViewPoints)
    void OnItemSelected(int position) {

    }

    @OnItemClick(R.id.listViewDroppingPoints)
    void OnDroppingItemSelected(int position) {

    }
*/

    @OnClick(R.id.textViewContinue)
    void onContinue() {

        if (AppConstants.Keys.isReturnBus) {


           /* if(sBoarding.equals("")){
                searchBusDetails.setsReturnBoarding(searchBusDetails.getsReturnFrom());
            }
            if(sDropping.equals("")){
                searchBusDetails.setsReturnDropping(searchBusDetails.getsReturnDropping());
            }*/

//            if (!isReturn && searchBusDetails.getsReturnBoarding().equals("")) {
            if (arrayListOnWords.size() == 0) {
                //      Toast.makeText(this, "onword seat selection called", Toast.LENGTH_SHORT).show();
                if (sBoarding.equals("")) {
                    searchBusDetails.setsBoarding(searchBusDetails.getsFrom());
                }
                if (sDropping.equals("")) {
                    searchBusDetails.setsDropping(searchBusDetails.getsTo());
                }
                Intent intent = new Intent(this, SeatSelectionActivity.class);
                intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                startActivity(intent);
            } else {
                /*if(arrayListReturn.size()==0){
                    if(sBoarding.equals("")){
                        searchBusDetails.setsReturnBoarding(searchBusDetails.getsReturnFrom());
                    }
                    if(sDropping.equals("")){
                        searchBusDetails.setsReturnDropping(searchBusDetails.getsReturnDropping());
                    }
                    Intent intent = new Intent(this, SeatSelectionActivity.class);
                    intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                    intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                    intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                    startActivity(intent);
                }*/
                if(arrayListReturn.size()!=0){
                    Intent intent = new Intent(this, PassengerDetailsActivity.class);
                    intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                    intent.putExtra(AppConstants.IntentKeys.RETURN_BUS, availableReturnBuses);
                    intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                    intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                    intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                    intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
                    intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                    startActivity(intent);
                }else   if (searchBusDetails.isRetrunActivityCalled()) {
                    if (sBoarding.equals("")) {
                        searchBusDetails.setsReturnBoarding(searchBusDetails.getsReturnFrom());
                    }
                    if (sDropping.equals("")) {
                        searchBusDetails.setsReturnDropping(searchBusDetails.getsReturnDropping());
                    }
                    Intent intent = new Intent(this, SeatSelectionActivity.class);
                    intent.putExtra(AppConstants.IntentKeys.RETURN_BUS, availableReturnBuses);
                    intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                    intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                    startActivity(intent);
                } else {
                    //    Toast.makeText(this, "REtrun activity called", Toast.LENGTH_SHORT).show();
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
            /*} else {
                if (sKKey.equals("0")) {
                    Intent intent = new Intent(this, SeatSelectionActivity.class);
                    intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                    intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                    intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                    startActivity(intent);
                } else {
//                Toast.makeText(this, "Else", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, PassengerDetailsActivity.class);
                    intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                    intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                    intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                    intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                    intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
                    intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                    startActivity(intent);
                }
            }*/
        } else {

            if (sBoarding.equals("")) {
                searchBusDetails.setsBoarding(searchBusDetails.getsFrom());
            }
            if (sDropping.equals("")) {
                searchBusDetails.setsDropping(searchBusDetails.getsTo());
            }
            /*if (sKKey.equals("0")) {
                Intent intent = new Intent(this, SeatSelectionActivity.class);
                intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                startActivity(intent);
            } else {
//            Toast.makeText(this, "Elese", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, PassengerDetailsActivity.class);
                intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
                intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                startActivity(intent);
            }*/
            if (/*arrayListOnWords!=null||*/arrayListOnWords.size() != 0) {
                //    Toast.makeText(this, ""+arrayListOnWords.size(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, PassengerDetailsActivity.class);
                intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                intent.putExtra(AppConstants.IntentKeys.RETURN_BUS, availableReturnBuses);
                intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
                intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                startActivity(intent);
            } else {
                //   Toast.makeText(this, ""+arrayListOnWords.size(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SeatSelectionActivity.class);
                intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                startActivity(intent);
            }
        }
        /*Intent intent = new Intent(this, PassengerDetailsActivity.class);
        intent.putExtra(AppConstants.IntentKeys.BUS, passengerBus);
        intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
        intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
        intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
        intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
        intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
        startActivity(intent);*/
    }

    @Override
    public void onBoardingPointSelected(BDPoints bdPoints) {
        sBoarding = bdPoints.getsBDName();
        Log.d("Log", "Bording Point " + sBoarding);
       /* if (AppConstants.Keys.isReturnBus) {
            searchBusDetails.setsReturnBoarding(sBoarding);
        } else {
            searchBusDetails.setsBoarding(sBoarding);
        }*/
        textViewBPChange.setVisibility(View.VISIBLE);
        listViewPoints.setVisibility(View.GONE);
        linearLayoutBPDetails.setVisibility(View.VISIBLE);
        linearLayoutDroppingPont.setVisibility(View.VISIBLE);
        linearLayoutDPDetails.setVisibility(View.VISIBLE);
        textViewBP.setText(sBoarding);
        /*  searchBusDetails.setsBoarding(sBoarding);
        if (searchBusDetails.isReturn()) {
            searchBusDetails.setsReturnBoarding(sBoarding);
        } else {
            searchBusDetails.setsBoarding(sBoarding);
        }*/
        if (searchBusDetails.isRetrunActivityCalled()) {
            searchBusDetails.setsReturnBoarding(sBoarding);
        } else {
            searchBusDetails.setsBoarding(sBoarding);
        }
        linearLayoutDroppingPont.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDroppingPointSelected(BDPoints bdPoints) {
        sDropping = bdPoints.getsBDName();
        textViewDPChange.setVisibility(View.VISIBLE);
        linearLayoutDPDetails.setVisibility(View.VISIBLE);
        listViewDroppingPoints.setVisibility(View.GONE);
        textViewDP.setText(sDropping);
        /*searchBusDetails.setsDropping(sDropping);
        if (searchBusDetails.isReturn()) {
            searchBusDetails.setsReturnDropping(sDropping);
        } else {
            searchBusDetails.setsDropping(sDropping);
        }*/
        if (searchBusDetails.isRetrunActivityCalled()) {
            searchBusDetails.setsReturnDropping(sDropping);
        } else {
            searchBusDetails.setsDropping(sDropping);
        }
    }

    /*    @BindView(R.id.textViewTotalFare)
        TextView textViewTotalFare;
        @BindView(R.id.textViewSeatDetails)
        TextView textViewSeatDetails;*/
    String sSelectedSeats = "";

/*
    private void getSelectedSeats() {
        Seat seat = null;
        sSelectedSeats = "";
        hashMapSelectedSeats = new HashMap<>();
        if (hashMapSelectedSeats.size() > 0) {
            for (String sKey : hashMapSelectedSeats.keySet()) {
                seat = hashMapSelectedSeats.get(sKey);
                if (seat.getsStatus().equals(AppConstants.Status.SELECTED)) {
                    sSelectedSeats += "," + seat.getsSeatNo();
                }
            }
        }
        updateFare();
    }
*/

/*
    private void updateFare() {
        int count = hashMapSelectedSeats.size();
        double dSingleFare = Double.parseDouble(passengerBus.getsFare().replaceAll(",", ""));
        Fare = count * dSingleFare;
//        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");
        String sTotalFare = String.valueOf(decimalFormat.format(Fare));
        if (sSelectedSeats.startsWith(",")) {
            sSelectedSeats = sSelectedSeats.substring(1, sSelectedSeats.length());
        }
        textViewSeatDetails.setText(sSelectedSeats);
        textViewTotalFare.setText("TSH " + sTotalFare);
    }
*/
}
