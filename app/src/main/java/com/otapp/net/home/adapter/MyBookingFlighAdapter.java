package com.otapp.net.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.home.core.MyBookingResponse;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class MyBookingFlighAdapter extends RecyclerView.Adapter<MyBookingFlighAdapter.MyViewHolder> {
    Context context;
    List<MyBookingResponse.MyBookingTransactoins.FlightDetails> flightDetailsList;
    MyBookingResponse.MyBookingTransactoins.FlightDetails flightDetails;
    public MyBookingFlighAdapter(Context context, List<MyBookingResponse.MyBookingTransactoins.FlightDetails> flightDetailsList) {
        this.context = context;
        this.flightDetailsList = flightDetailsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_mybooking_flight,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        flightDetails=flightDetailsList.get(i);
        if(flightDetails!=null) {
            Picasso.get().load(flightDetails.logo).into(myViewHolder.ivFligtLogo, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    myViewHolder.ivFligtLogo.setImageResource(R.drawable.plan_icon);
                }
            });
            myViewHolder.tvflightCompany.setText(flightDetails.carrier);
            myViewHolder.tvflightCompanyWithpnr.setText(flightDetails.carrier + "-" + flightDetails.flight_no);
            myViewHolder.tvFromCity.setText(flightDetails.start_Station);
            myViewHolder.tvToCity.setText(flightDetails.end_Station);
            myViewHolder.tvFromCityCode.setText(flightDetails.start_airport);
            myViewHolder.tvToCityCode.setText(flightDetails.end_airport);
            myViewHolder.tvFromCity2.setText(flightDetails.start_Station);
            myViewHolder.tvToCity2.setText(flightDetails.end_Station);
            myViewHolder.tvBages.setText(flightDetails.baggage_info);
            myViewHolder.tvFlightDuration.setText(flightDetails.flight_duration);
            if(flightDetails.baggage_info!=null&& !flightDetails.baggage_info.equals("")) {
                myViewHolder.layoutLaugage.setVisibility(View.VISIBLE);
                myViewHolder.tvBages.setText(flightDetails.baggage_info);
               /* myViewHolder.tvBaggage.setText(flightDetails.baggage_info);*/
            }else {
                myViewHolder.layoutLaugage.setVisibility(View.GONE);
            }
            if(flightDetails.lay_over_span!=null&& !flightDetails.lay_over_span.equals("")) {
                myViewHolder.layoutLayoverTime.setVisibility(View.VISIBLE);
                myViewHolder.tvFlightLayoverTime.setText("Layover at "+flightDetails.start_Station+" for "+flightDetails.lay_over_span);
            }else {
                myViewHolder.layoutLayoverTime.setVisibility(View.GONE);
            }
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = format.parse(flightDetails.start_date_time);
                /* date.setHours(date.getHours() + 8);*/
                String strDate, strMonth, time, strDay;

                strDay = (String) android.text.format.DateFormat.format("EEE", date);
                strDate = (String) android.text.format.DateFormat.format("dd", date); // 20
                strMonth = (String) android.text.format.DateFormat.format("MMM", date); // Jun
                time = (String) android.text.format.DateFormat.format("HH:mm aa", date); // Jun

                myViewHolder.tvDeparureDate.setText(strDay + "," + strDate + " " + strMonth);
                myViewHolder.tvDepartureTime.setText(time.toUpperCase());

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = format.parse(flightDetails.end_date_time);
                String strDate, strMonth, time, strDay;

                strDay = (String) android.text.format.DateFormat.format("EEE", date);
                strDate = (String) android.text.format.DateFormat.format("dd", date); // 20
                strMonth = (String) android.text.format.DateFormat.format("MMM", date); // Jun
                time = (String) android.text.format.DateFormat.format("HH:mm aa", date); // Jun

                myViewHolder.tvArrivalDate.setText(strDay + "," + strDate + " " + strMonth);

                myViewHolder.tvArrivalTime.setText(time.toUpperCase());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public int getItemCount() {
        return flightDetailsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvflightCompany,tvflightCompanyWithpnr,tvFlightClass,tvFromCity,tvFromCityCode,tvFromCity2,tvToCity,tvToCityCode,tvToCity2;
        TextView tvDepartureTime,tvDeparureDate,tvArrivalDate,tvArrivalTime,tvBages,tvFlightDuration,tvFlightLayoverTime;
        ImageView ivFligtLogo;
        RelativeLayout layoutLaugage,layoutLayoverTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvflightCompany=itemView.findViewById(R.id.flightCompany);
            tvflightCompanyWithpnr=itemView.findViewById(R.id.flightCompanyWithPnr);
            tvFlightClass=itemView.findViewById(R.id.flightClass);
            tvFromCity=itemView.findViewById(R.id.tvFromCityName);
            tvFromCityCode=itemView.findViewById(R.id.tvCityCodeName);
            tvFromCity2=itemView.findViewById(R.id.tvFromCityName2);
            tvToCity=itemView.findViewById(R.id.tvToCityName);
            tvToCityCode=itemView.findViewById(R.id.tvToCityNameCode);
            tvToCity2=itemView.findViewById(R.id.tvToCityName2);
            tvDepartureTime=itemView.findViewById(R.id.tvDepartureTime);
            tvDeparureDate=itemView.findViewById(R.id.tvDepartureDate);
            tvArrivalDate=itemView.findViewById(R.id.tvArraivalDate);
            tvArrivalTime=itemView.findViewById(R.id.tvArriavalTime);
            ivFligtLogo=itemView.findViewById(R.id.ivFligtLogo);
            tvBages=itemView.findViewById(R.id.tvBaggage);
            tvFlightDuration=itemView.findViewById(R.id.flightDuration);
            tvFlightLayoverTime=itemView.findViewById(R.id.flightLayoverTime);
            layoutLaugage=itemView.findViewById(R.id.relativeLayoutLaugage);
            layoutLayoverTime=itemView.findViewById(R.id.layoutLayoverTime);
        }
    }
}
