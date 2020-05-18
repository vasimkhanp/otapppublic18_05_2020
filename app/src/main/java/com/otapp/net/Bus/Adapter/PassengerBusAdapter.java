package com.otapp.net.Bus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.otapp.net.Bus.Activity.BoardingPointActivity;
import com.otapp.net.Bus.Activity.SeatSelectionActivity;
import com.otapp.net.Bus.Core.Agent;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.AvailableBuses;
import com.otapp.net.Bus.Core.PassengerBus;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.Bus.Core.Seat;
import com.otapp.net.Bus.Core.SeatMap;
import com.otapp.net.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PassengerBusAdapter extends RecyclerView.Adapter<PassengerBusAdapter.PassengerBusViewHolder> {

    private Context context;
    private ArrayList<AvailableBuses> passengerBusArrayList;
    private ArrayList<ReturnAvailableBus> passengerRetrunBusArrayList;
    private String sKey;
    private SearchBusDetails searchBusDetails;
    private Agent agent;
    AvailableBuses availableBuses;
    AvailableBuses availableReturnBuses;
    String[] strBoardingPoint;
    String[] strDropingPoint;

    public PassengerBusAdapter(Context context, ArrayList<AvailableBuses> passengerBusArrayList, SearchBusDetails searchBusDetails) {
        this.context = context;
        this.passengerBusArrayList = passengerBusArrayList;
        this.searchBusDetails = searchBusDetails;

    }

    @NonNull
    @Override
    public PassengerBusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_passenger_bus_adapter, viewGroup, false);
        PassengerBusViewHolder passengerBusViewHolder = new PassengerBusViewHolder(view);
        return passengerBusViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerBusViewHolder passengerBusViewHolder, int i) {
        //if(AppConstants.Keys.isReturnBus && !searchBusDetails.getsBoarding().equals("")){
        if(searchBusDetails.isRetrunActivityCalled()){

            availableReturnBuses=passengerBusArrayList.get(i);
            String journiduration[]=availableReturnBuses.getJourney_duration().split(":");
          //  Toast.makeText(context, "REturn Bus Available", Toast.LENGTH_SHORT).show();
            passengerBusViewHolder.textViewBusRoute.setText(availableReturnBuses.getBus_name());
            passengerBusViewHolder.textViewArrivingTime.setText(availableReturnBuses.getArrvl_time());
            passengerBusViewHolder.textViewDepartureTime.setText(availableReturnBuses.getDep_time());
            passengerBusViewHolder.textViewSeatType.setText(availableReturnBuses.getBus_type());
            passengerBusViewHolder.textViewSeats.setText(availableReturnBuses.getAvail_seats() + " " + context.getResources().getString(R.string.seats_left));
            passengerBusViewHolder.textViewFare.setText(availableReturnBuses.getMin_fare());
            if(journiduration.length>0) {
                passengerBusViewHolder.textViewJourneyTime.setText("- " + journiduration[0]+"h " +journiduration[1] +"m"+" -");
            }
        }else {
            availableBuses=passengerBusArrayList.get(i);
         //   Toast.makeText(context, "Onword Bus Available", Toast.LENGTH_SHORT).show();
            String journiduration[]=availableBuses.getJourney_duration().split(":");
            // PassengerBus passengerBus = passengerBusArrayList.get(passengerBusViewHolder.getAdapterPosition());
            passengerBusViewHolder.textViewBusRoute.setText(availableBuses.getBus_name());
            passengerBusViewHolder.textViewArrivingTime.setText(availableBuses.getArrvl_time());
            passengerBusViewHolder.textViewDepartureTime.setText(availableBuses.getDep_time());
            passengerBusViewHolder.textViewSeatType.setText(availableBuses.getBus_type());
            passengerBusViewHolder.textViewSeats.setText(availableBuses.getAvail_seats() + " " + context.getResources().getString(R.string.seats_left));
            passengerBusViewHolder.textViewFare.setText(availableBuses.getMin_fare());
            if(journiduration.length>0) {
                passengerBusViewHolder.textViewJourneyTime.setText("- " + journiduration[0]+"h " +journiduration[1] +"m"+" -");
            }
            //  passengerBusViewHolder.textViewDiscountFare.setText("TSH 100");
            // passengerBusViewHolder.textViewDiscountFare.setPaintFlags(passengerBusViewHolder.textViewDiscountFare.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        passengerBusViewHolder.textViewBoardingPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Seat> hashMapSelectedSeats = new HashMap<>();
                SeatMap seatMap = new SeatMap();
                double Fare = 0;
                Intent intent = new Intent(context, BoardingPointActivity.class);
                intent.putExtra("KEYRE", "0");

                strBoardingPoint=passengerBusArrayList.get(i).getStrBoardingPoints();
                strDropingPoint=passengerBusArrayList.get(i).getStrDropingPoints();

                searchBusDetails.setAvailableReturnBuses(availableReturnBuses);

              //  Toast.makeText(context, "bdSize "+strBoardingPoint.length, Toast.LENGTH_SHORT).show();
                intent.putExtra("boardigPoint",strBoardingPoint);
                intent.putExtra("dropingPoint",strDropingPoint);
                intent.putExtra(AppConstants.IntentKeys.BUS, passengerBusArrayList.get(i));
                intent.putExtra(AppConstants.IntentKeys.RETURN_BUS, availableReturnBuses);
                intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                intent.putExtra(AppConstants.IntentKeys.SELECTED_SEAT, hashMapSelectedSeats);
                intent.putExtra(AppConstants.IntentKeys.FARE, Fare);
                intent.putExtra(AppConstants.IntentKeys.SEAT_MAP, seatMap);
                context.startActivity(intent);

            }
        });
//        passengerBusViewHolder.textViewJourneyTime.setText("\u2022 " + passengerBus.getsJourneyDuration());
    }

    @Override
    public int getItemCount() {
        return passengerBusArrayList.size();
    }

    public class PassengerBusViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewBusRoute;
        private TextView textViewArrivingTime;
        private TextView textViewDepartureTime;
        private TextView textViewJourneyTime;
        private TextView textViewSeatType;
        private TextView textViewSeats;
        private TextView textViewFare;
        private TextView textViewBoardingPoint;
        private TextView textViewDiscountFare;

        public PassengerBusViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewBusRoute = (TextView) itemView.findViewById(R.id.textViewBusRoute);
            textViewArrivingTime = (TextView) itemView.findViewById(R.id.textViewArrivingTime);
            textViewDepartureTime = (TextView) itemView.findViewById(R.id.textViewDepartureTime);
            textViewJourneyTime = (TextView) itemView.findViewById(R.id.textViewJourneyTime);
            textViewSeatType = (TextView) itemView.findViewById(R.id.textViewSeatType);
            textViewSeats = (TextView) itemView.findViewById(R.id.textViewSeats);
            textViewFare = (TextView) itemView.findViewById(R.id.textViewFare);
            textViewDiscountFare = (TextView) itemView.findViewById(R.id.textViewDiscountFare);
            textViewBoardingPoint = (TextView) itemView.findViewById(R.id.textViewBoardingPoint);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* AvailableBuses availableBuses = null;
                    AvailableBuses availableReturnBuses=null;*/
                    if (getAdapterPosition() != -1) {
                        if(searchBusDetails.isRetrunActivityCalled()){
                            availableReturnBuses = passengerBusArrayList.get(getAdapterPosition());
                        }else {

                            availableBuses = passengerBusArrayList.get(getAdapterPosition());
                        }

                     //   if (!passengerBus.getsIsEnabled().equals("false")) {
//                            Intent intent = new Intent(context, BoardingPointActivity.class);
                        searchBusDetails.setAvailableReturnBuses(availableReturnBuses);
                            Intent intent = new Intent(context, SeatSelectionActivity.class);
                            intent.putExtra(AppConstants.IntentKeys.BUS, availableBuses);
                            intent.putExtra(AppConstants.IntentKeys.RETURN_BUS, availableReturnBuses);
                            intent.putExtra(AppConstants.IntentKeys.SEARCH_BUS, searchBusDetails);
                            intent.putExtra(AppConstants.IntentKeys.KEY, sKey);
                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                    }
                      /*  } else {
                            Toast.makeText(context, "" + context.getResources().getString(R.string.Booking_is_not_enabled_please_try_later), Toast.LENGTH_SHORT).show();
                        }*/
                    }
            });
        }
    }
}
