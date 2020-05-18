package com.otapp.net.home.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.Events.Core.PaymentSuceesResponse;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;
import com.otapp.net.home.adapter.MyBookingFlighAdapter;
import com.otapp.net.home.core.MyBookingResponse;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyBookingTicketDetails extends AppCompatActivity {

    @BindView(R.id.tvUserName)
    TextView tvClientName;
    @BindView(R.id.tvPnr)
    TextView tvPnrNo;
    @BindView(R.id.recyclerFlightDetails)
    RecyclerView recyclerFightDetails;
    @BindView(R.id.tvTicketNo)
            TextView tvTicketNo;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.tvBookingID)
            TextView tvBookingID;
    RecyclerView.LayoutManager flightDetailsRecyManager;
    String fligDetailsRespons="";
    List<MyBookingResponse.MyBookingTransactoins.FlightDetails> flightDetailsList;
    MyBookingResponse.MyBookingTransactoins flightDetails;
    MyBookingFlighAdapter myBookingFlighAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking_ticket_details);
        ButterKnife.bind(this);
        flightDetailsList=new ArrayList<>();
        fligDetailsRespons = getIntent().getExtras().getString("flightDetails");
        flightDetailsRecyManager= new LinearLayoutManager(getApplicationContext());
        recyclerFightDetails.setHasFixedSize(true);
        recyclerFightDetails.setLayoutManager(flightDetailsRecyManager);

        if(!fligDetailsRespons.isEmpty()){
            flightDetails=new Gson().fromJson(fligDetailsRespons, MyBookingResponse.MyBookingTransactoins.class);
            if(flightDetails!=null){
                tvClientName.setText("Name : "+flightDetails.name+" "+flightDetails.last_name);
                tvPnrNo.setText(flightDetails.airline_pnr);
                tvTicketNo.setText(flightDetails.ticket_no);
                flightDetailsList=flightDetails.flightDetailsList;

                myBookingFlighAdapter=new MyBookingFlighAdapter(getApplicationContext(),flightDetailsList);
                recyclerFightDetails.setAdapter(myBookingFlighAdapter);
                myBookingFlighAdapter.notifyDataSetChanged();
                if (!TextUtils.isEmpty(flightDetails.ticket_no)) {
                    tvBookingID.setText("" + flightDetails.ticket_no);
                    Bitmap mQRCode = Utils.getQRCode(flightDetails.ticket_no);
                    if (mQRCode != null) {
                        ivQRCode.setImageBitmap(mQRCode);
                    }
                }

            }
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent callHome = new Intent(getApplicationContext(), HomeActivity.class);
        callHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(callHome);*/
        finish();
    }
    @OnClick(R.id.tvBack)
    public void onBackClick(){
        finish();
    }
}
