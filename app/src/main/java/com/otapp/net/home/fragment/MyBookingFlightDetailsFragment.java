package com.otapp.net.home.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.home.adapter.MyBookingFlighAdapter;
import com.otapp.net.home.core.MyBookingResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyBookingFlightDetailsFragment extends Fragment {

    @BindView(R.id.tvUserName)
    TextView tvClientName;
    @BindView(R.id.tvPnr)
    TextView tvPnrNo;
    @BindView(R.id.recyclerFlightDetails)
    RecyclerView recyclerFightDetails;
    @BindView(R.id.tvTicketNo)
    TextView tvTicketNo;
    RecyclerView.LayoutManager flightDetailsRecyManager;
    String fligDetailsRespons="";
    List<MyBookingResponse.MyBookingTransactoins.FlightDetails> flightDetailsList;
    MyBookingResponse.MyBookingTransactoins flightDetails;
    MyBookingFlighAdapter myBookingFlighAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_booking_flight_details, container, false);
        ButterKnife.bind(getActivity());
        flightDetailsList=new ArrayList<>();
        fligDetailsRespons = getActivity().getIntent().getExtras().getString("flightDetails");
        flightDetailsRecyManager= new LinearLayoutManager(getContext());
        recyclerFightDetails.setHasFixedSize(true);
        recyclerFightDetails.setLayoutManager(flightDetailsRecyManager);

        if(!fligDetailsRespons.isEmpty()){
            flightDetails=new Gson().fromJson(fligDetailsRespons, MyBookingResponse.MyBookingTransactoins.class);
            if(flightDetails!=null){
                tvClientName.setText("Name : "+flightDetails.name+" "+flightDetails.last_name);
                tvPnrNo.setText(flightDetails.airline_pnr);
                tvTicketNo.setText(flightDetails.ticket_no);
                flightDetailsList=flightDetails.flightDetailsList;
                myBookingFlighAdapter=new MyBookingFlighAdapter(getContext(),flightDetailsList);
                recyclerFightDetails.setAdapter(myBookingFlighAdapter);
                myBookingFlighAdapter.notifyDataSetChanged();
            }
        }
        return view;
    }

}
