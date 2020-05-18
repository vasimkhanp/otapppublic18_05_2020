package com.otapp.net.Bus.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.Bus.Core.ReserveTicketResponse;
import com.otapp.net.Events.Core.PaymentSuceesResponse;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookingReserveActivity extends AppCompatActivity {
    JSONObject jsonObject=null;
    String jsonResponse="";
    ReserveTicketResponse ticketResponse;
    ReserveTicketResponse.BookingInfo bookingInfo;
    ReserveTicketResponse.ReturnBookingInfo returnBookingInfo;
    ArrayList<ReserveTicketResponse.BookingInfo.Passengers> passengersArrayList;
    ArrayList<ReserveTicketResponse.ReturnBookingInfo.Passengers> returnPassengersArrayList;
    @BindView(R.id.tvFrom)
    TextView tvFrom;
    @BindView(R.id.tvTo)
    TextView tvTo;
    @BindView(R.id.tvBookingDateTime)
    TextView tvBookigDateTime;
    @BindView(R.id.tvBoardingPoint)
    TextView tvBoardingPoint;
    @BindView(R.id.tvDropingPoint)
    TextView tvDropingPoint;
    @BindView(R.id.tvTotalFare)
    TextView tvTotalFare;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    @BindView(R.id.tvCompanyAddr)
    TextView tvCompanyAdd;
    @BindView(R.id.tvTin)
    TextView tvTin;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.tvBusName)
            TextView tvBusName;
    @BindView(R.id.tvJournyDate)
            TextView tvJournyDate;
    String sTicketNo="";
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvBack)
    TextView backButton;


    @BindView(R.id.tvFromReturn)
    TextView tvFromReturn;
    @BindView(R.id.tvToReturn)
    TextView tvToReturn;
    @BindView(R.id.tvBookingDateTimeReturn)
    TextView tvBookigDateTimeReturn;
    @BindView(R.id.tvBoardingPointReturn)
    TextView tvBoardingPointReturn;
    @BindView(R.id.tvDropingPointReturn)
    TextView tvDropingPointReturn;
    @BindView(R.id.tvTotalFareReturn)
    TextView tvTotalFareReturn;
    @BindView(R.id.tvCompanyNameReturn)
    TextView tvCompanyNameReturn;
    @BindView(R.id.tvCompanyAddrReturn)
    TextView tvCompanyAddReturn;
    @BindView(R.id.tvTinReturn)
    TextView tvTinReturn;
    @BindView(R.id.tvBusNameReturn)
    TextView tvBusNameReturn;
    @BindView(R.id.tvJournyDateReturn)
    TextView tvJournyDateReturn;

    @BindView(R.id.tvPhoneReturn)
    TextView tvPhoneReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_booking_reserve);
        ButterKnife.bind(this);
        tvFrom=findViewById(R.id.tvFrom);
        jsonResponse= getIntent().getStringExtra(com.otapp.net.utils.AppConstants.BNDL_BUS_RESPONSE);
        Log.d("Log",jsonResponse);
        if(!jsonResponse.isEmpty()){
            ticketResponse=new Gson().fromJson(jsonResponse, ReserveTicketResponse.class);
            bookingInfo=ticketResponse.bookingInfo;
            returnBookingInfo= ticketResponse.returnBookingInfo;
            if (!TextUtils.isEmpty(ticketResponse.booking_id)) {
                //tvBookingID.setText("" + ticketResponse.booking_id);
                Bitmap mQRCode = Utils.getQRCode(ticketResponse.booking_id);
                if (mQRCode != null) {
                    ivQRCode.setImageBitmap(mQRCode);
                }
            }
            if(ticketResponse!=null){

                Log.d("Log","bookingTime "+ticketResponse.booking_time);
                tvBookigDateTime.setText(ticketResponse.booking_time);
                if(bookingInfo!=null){
                    String seatNo="";
                    passengersArrayList=bookingInfo.passengersArrayList;
                    for(int i=0;i<passengersArrayList.size();i++){
                        String seat[]=passengersArrayList.get(i).seat_id.split("-");
                        if(seatNo.equals("")){
                            seatNo+=seat[3];
                        }else {
                            seatNo+=","+seat[3];
                        }
                    }

                    tvBoardingPoint.setText(bookingInfo.boarding_point);
                    tvDropingPoint.setText(bookingInfo.dropping);
                    tvTotalFare.setText(bookingInfo.total_fare);
                    tvFrom.setText(bookingInfo.from_stn_name);
                    tvTo.setText(bookingInfo.to_stn_name);
                    tvCompanyName.setText(bookingInfo.bus_company_name);
                    tvTin.setText(bookingInfo.bus_owner_tin);
                    tvBusName.setText(bookingInfo.bus_name);
                    tvJournyDate.setText(bookingInfo.travel_date_time+" | Seat No. "+seatNo);
                    tvPhone.setText(bookingInfo.bus_company_phone);

                    byte[] data = Base64.decode(bookingInfo.bus_owner_address, Base64.DEFAULT);
                    String text = null;
                    try {
                        text = new String(data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    tvCompanyAdd.setText(text);

                }
            }
            if(ticketResponse!=null){

                Log.d("Log","bookingTime "+ticketResponse.booking_time);
                tvBookigDateTimeReturn.setText(ticketResponse.booking_time);
                if(returnBookingInfo!=null){
                    String seatNo="";
                    returnPassengersArrayList=returnBookingInfo.passengersArrayList;
                    for(int i=0;i<returnPassengersArrayList.size();i++){
                        String seat[]=returnPassengersArrayList.get(i).seat_id.split("-");
                        if(seatNo.equals("")){
                            seatNo+=seat[3];
                        }else {
                            seatNo+=","+seat[3];
                        }
                    }

                    tvBoardingPointReturn.setText(returnBookingInfo.boarding_point);
                    tvDropingPointReturn.setText(returnBookingInfo.dropping);
                    tvTotalFareReturn.setText(returnBookingInfo.total_fare);
                    tvFromReturn.setText(returnBookingInfo.from_stn_name);
                    tvToReturn.setText(returnBookingInfo.to_stn_name);
                    tvCompanyNameReturn.setText(returnBookingInfo.bus_company_name);
                    tvTinReturn.setText(returnBookingInfo.bus_owner_tin);
                    tvBusNameReturn.setText(returnBookingInfo.bus_name);
                    tvJournyDateReturn.setText(returnBookingInfo.travel_date_time+" | Seat No. "+seatNo);
                    tvPhoneReturn.setText(returnBookingInfo.bus_company_phone);

                    byte[] data = Base64.decode(returnBookingInfo.bus_owner_address, Base64.DEFAULT);
                    String text = null;
                    try {
                        text = new String(data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    tvCompanyAddReturn.setText(text);

                }
            }

        }

        MyPref.setPref(getApplicationContext(), MyPref.PREF_BUS_UKEY,"");
        MyPref.setPref(getApplicationContext(), MyPref.RETURN_UKEY,"");
        MyPref.setPref(getApplicationContext(), MyPref.ASID,"");


    }
    @OnClick(R.id.tvBack)
    public void back(){
        Intent callHome = new Intent(getApplicationContext(), HomeActivity.class);
        callHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(callHome);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent callHome = new Intent(getApplicationContext(), HomeActivity.class);
        callHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(callHome);
        finish();
    }
}
