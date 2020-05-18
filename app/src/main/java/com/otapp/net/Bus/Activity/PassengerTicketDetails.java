package com.otapp.net.Bus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.otapp.net.Bus.Adapter.PassengerInfoAdapter;
import com.otapp.net.Bus.Core.Agent;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.BookTicketResponse;
import com.otapp.net.Bus.Core.NormalTicketInfo;
import com.otapp.net.Bus.Core.PassengerBus;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PassengerTicketDetails extends AppCompatActivity {

    @BindView(R.id.textViewBusName)
    TextView textViewBusName;
    @BindView(R.id.textViewSenderCopy)
    TextView textViewSenderCopy;
    @BindView(R.id.textViewTicketNo)
    TextView textViewTicketNo;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewDepartureTime)
    TextView textViewDepartureTime;
    @BindView(R.id.textViewFrom)
    TextView textViewFrom;
    @BindView(R.id.textViewTo)
    TextView textViewTo;
    @BindView(R.id.textViewSingleFare)
    TextView textViewSingleFare;
    @BindView(R.id.textViewAmount)
    TextView textViewAmount;
    @BindView(R.id.textViewTotalSeats)
    TextView textViewTotalSeats;
    @BindView(R.id.listViewPassengerDetails)
    ListView listViewPassengerDetails;

    @BindView(R.id.cardViewReturn)
    CardView cardViewReturn;
    @BindView(R.id.textViewReturnBusName)
    TextView textViewReturnBusName;
    @BindView(R.id.textViewReturnTicketNo)
    TextView textViewReturnTicketNo;
    @BindView(R.id.textViewReturnDate)
    TextView textViewReturnDate;
    @BindView(R.id.textViewReturnDepartureTime)
    TextView textViewReturnDepartureTime;
    @BindView(R.id.textViewReturnFrom)
    TextView textViewReturnFrom;
    @BindView(R.id.textViewReturnTo)
    TextView textViewReturnTo;
    @BindView(R.id.textViewReturnSingleFare)
    TextView textViewReturnSingleFare;
    @BindView(R.id.textViewReturnAmount)
    TextView textViewReturnAmount;
    @BindView(R.id.textViewReturnTotalSeats)
    TextView textViewReturnTotalSeats;
    @BindView(R.id.listViewReturnPassengerDetails)
    ListView listViewReturnPassengerDetails;

    private NormalTicketInfo normalTicketInfo;
    private BookTicketResponse bookTicketResponse;
    private PassengerBus passengerBus;
    private SearchBusDetails searchBusDetails;
    private PassengerInfoAdapter passengerInfoAdapter;
    private Agent agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_ticket_details);
        ButterKnife.bind(this);

        agent = new Agent();

        normalTicketInfo = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.TICKET_DETAILS);
        passengerBus = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.BUS);
        searchBusDetails = getIntent().getExtras().getParcelable(AppConstants.IntentKeys.SEARCH_BUS);
        bookTicketResponse = normalTicketInfo.getBookTicketResponse();

        if (searchBusDetails.isReturn()) {
            cardViewReturn.setVisibility(View.VISIBLE);
            textViewReturnBusName.setText(passengerBus.getsBusOperators());
            textViewReturnTicketNo.setText(bookTicketResponse.getsTicketNo());
            textViewReturnDate.setText(normalTicketInfo.getsNewJourneyDate());
            textViewReturnDepartureTime.setText(passengerBus.getsDeparture().replaceAll("Hrs", ""));
            textViewReturnFrom.setText(searchBusDetails.getsReturnFrom());
            textViewReturnTo.setText(searchBusDetails.getsReturnTo());
            double dSingleFare = 0;
            double dFare = 0;
//            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");
            String sSingleFare = passengerBus.getsFare().replaceAll(",","");
            String sFare = normalTicketInfo.getsFare().replaceAll(",","");
            if (!passengerBus.getsFare().equals("")) {
                dSingleFare = Double.parseDouble(passengerBus.getsFare().replaceAll(",",""));
                sSingleFare = String.valueOf(decimalFormat.format(dSingleFare));
            }
            if (!passengerBus.getsFare().equals("")) {
                dFare = Double.parseDouble(normalTicketInfo.getsFare().replaceAll(",",""));
                sFare = String.valueOf(decimalFormat.format(dFare));
            }
            textViewReturnSingleFare.setText(agent.getsAgentCurrency() + " " + sSingleFare);
            textViewReturnAmount.setText(agent.getsAgentCurrency() + " " + sFare);
            textViewReturnTotalSeats.setText(normalTicketInfo.getSeatCount());
            String sSeatNo = "";
            JSONArray jsonArrayPassengerDetails = null;
        /*try {
            jsonArrayPassengerDetails = new JSONArray(normalTicketInfo.getsPassengerDetails());
            for (int i = 0; i < jsonArrayPassengerDetails.length(); i++) {
                JSONObject jsonObjectPassengerDetails = jsonArrayPassengerDetails.getJSONObject(i);
                sSeatNo += "," + jsonObjectPassengerDetails.getString("passenger_seat_no");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sSeatNo.startsWith(",")) {
            textViewTotalSeats.setText(sSeatNo.substring(1, sSeatNo.length()));
        }*/

            try {
                jsonArrayPassengerDetails = new JSONArray(normalTicketInfo.getsPassengerDetails());
                passengerInfoAdapter = new PassengerInfoAdapter(this, jsonArrayPassengerDetails);
                listViewReturnPassengerDetails.setAdapter(passengerInfoAdapter);
                setListViewHeightBasedOnChildren(listViewReturnPassengerDetails);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


           /* textViewBusName.setText(searchBusDetails.getPassengerBus().getsBusOperators());
            textViewTicketNo.setText(bookTicketResponse.getsTicketNo());
            textViewDate.setText(searchBusDetails.getPassengerBus().getsNewJourneyDate());
            textViewDepartureTime.setText(searchBusDetails.getPassengerBus().getsDeparture().replaceAll("Hrs", ""));*/
            textViewFrom.setText(searchBusDetails.getsFrom());
            textViewTo.setText(searchBusDetails.getsTo());
          /*  if (!passengerBus.getsFare().equals("")) {
                dSingleFare = Double.parseDouble(searchBusDetails.getPassengerBus().getsFare().replaceAll(",",""));
                sSingleFare = String.valueOf(decimalFormat.format(dSingleFare));
            }*/
            if (!passengerBus.getsFare().equals("")) {
                dFare = Double.parseDouble(normalTicketInfo.getsFare().replaceAll(",",""));
                sFare = String.valueOf(decimalFormat.format(dFare));
            }
            textViewSingleFare.setText(agent.getsAgentCurrency() + " " + sSingleFare);
            textViewAmount.setText(agent.getsAgentCurrency() + " " + sFare);
            textViewTotalSeats.setText(normalTicketInfo.getSeatCount());
        /*try {
            jsonArrayPassengerDetails = new JSONArray(normalTicketInfo.getsPassengerDetails());
            for (int i = 0; i < jsonArrayPassengerDetails.length(); i++) {
                JSONObject jsonObjectPassengerDetails = jsonArrayPassengerDetails.getJSONObject(i);
                sSeatNo += "," + jsonObjectPassengerDetails.getString("passenger_seat_no");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sSeatNo.startsWith(",")) {
            textViewTotalSeats.setText(sSeatNo.substring(1, sSeatNo.length()));
        }*/

            try {
                jsonArrayPassengerDetails = new JSONArray(normalTicketInfo.getsPassengerDetails());
                passengerInfoAdapter = new PassengerInfoAdapter(this, jsonArrayPassengerDetails);
                listViewPassengerDetails.setAdapter(passengerInfoAdapter);
                setListViewHeightBasedOnChildren(listViewPassengerDetails);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            cardViewReturn.setVisibility(View.GONE);
            textViewBusName.setText(passengerBus.getsBusOperators());
            textViewTicketNo.setText(bookTicketResponse.getsTicketNo());
            textViewDate.setText(normalTicketInfo.getsNewJourneyDate());
            textViewDepartureTime.setText(passengerBus.getsDeparture().replaceAll("Hrs", ""));
            textViewFrom.setText(searchBusDetails.getsFrom());
            textViewTo.setText(searchBusDetails.getsTo());
            double dSingleFare = 0;
            double dFare = 0;
//            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");
            String sSingleFare = passengerBus.getsFare();
            String sFare = normalTicketInfo.getsFare();
            if (!passengerBus.getsFare().equals("")) {
                dSingleFare = Double.parseDouble(passengerBus.getsFare().replaceAll(",",""));
                sSingleFare = String.valueOf(decimalFormat.format(dSingleFare));
            }
            if (!passengerBus.getsFare().equals("")) {
                dFare = Double.parseDouble(normalTicketInfo.getsFare().replaceAll(",",""));
                sFare = String.valueOf(decimalFormat.format(dFare));
            }
            textViewSingleFare.setText(agent.getsAgentCurrency() + " " + sSingleFare);
            textViewAmount.setText(agent.getsAgentCurrency() + " " + sFare);
            textViewTotalSeats.setText(normalTicketInfo.getSeatCount());
            String sSeatNo = "";
            JSONArray jsonArrayPassengerDetails = null;
        /*try {
            jsonArrayPassengerDetails = new JSONArray(normalTicketInfo.getsPassengerDetails());
            for (int i = 0; i < jsonArrayPassengerDetails.length(); i++) {
                JSONObject jsonObjectPassengerDetails = jsonArrayPassengerDetails.getJSONObject(i);
                sSeatNo += "," + jsonObjectPassengerDetails.getString("passenger_seat_no");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sSeatNo.startsWith(",")) {
            textViewTotalSeats.setText(sSeatNo.substring(1, sSeatNo.length()));
        }*/

            try {
                jsonArrayPassengerDetails = new JSONArray(normalTicketInfo.getsPassengerDetails());
                passengerInfoAdapter = new PassengerInfoAdapter(this, jsonArrayPassengerDetails);
                listViewPassengerDetails.setAdapter(passengerInfoAdapter);
                setListViewHeightBasedOnChildren(listViewPassengerDetails);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        /*textViewBusName.setText(passengerBus.getsBusOperators());
        textViewTicketNo.setText(bookTicketResponse.getsTicketNo());
        textViewDate.setText(normalTicketInfo.getsNewJourneyDate());
        textViewDepartureTime.setText(passengerBus.getsDeparture().replaceAll("Hrs", ""));
        textViewFrom.setText(searchBusDetails.getsFrom());
        textViewTo.setText(searchBusDetails.getsTo());
        double dSingleFare = 0;
        double dFare = 0;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String sSingleFare = passengerBus.getsFare();
        String sFare = normalTicketInfo.getsFare();
        if (!passengerBus.getsFare().equals("")) {
            dSingleFare = Double.parseDouble(passengerBus.getsFare());
            sSingleFare = String.valueOf(decimalFormat.format(dSingleFare));
        }
        if (!passengerBus.getsFare().equals("")) {
            dFare = Double.parseDouble(normalTicketInfo.getsFare());
            sFare = String.valueOf(decimalFormat.format(dFare));
        }
        textViewSingleFare.setText(agent.getsAgentCurrency() + ". " + sSingleFare);
        textViewAmount.setText(agent.getsAgentCurrency() + ". " + sFare);
        textViewTotalSeats.setText(normalTicketInfo.getSeatCount());
        String sSeatNo = "";
        JSONArray jsonArrayPassengerDetails = null;
        *//*try {
            jsonArrayPassengerDetails = new JSONArray(normalTicketInfo.getsPassengerDetails());
            for (int i = 0; i < jsonArrayPassengerDetails.length(); i++) {
                JSONObject jsonObjectPassengerDetails = jsonArrayPassengerDetails.getJSONObject(i);
                sSeatNo += "," + jsonObjectPassengerDetails.getString("passenger_seat_no");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sSeatNo.startsWith(",")) {
            textViewTotalSeats.setText(sSeatNo.substring(1, sSeatNo.length()));
        }*//*

        try {
            jsonArrayPassengerDetails = new JSONArray(normalTicketInfo.getsPassengerDetails());
            passengerInfoAdapter = new PassengerInfoAdapter(this, jsonArrayPassengerDetails);
            listViewPassengerDetails.setAdapter(passengerInfoAdapter);
            setListViewHeightBasedOnChildren(listViewPassengerDetails);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @OnClick(R.id.textViewSenderCopy)
    void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
