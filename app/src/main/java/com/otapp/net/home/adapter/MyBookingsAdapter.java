package com.otapp.net.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.Async.Interface.OtappApiServices;
import com.otapp.net.Async.Interface.RestClient;
import com.otapp.net.Events.Activity.TicketSuccessResponseActivity;
import com.otapp.net.Events.Core.PaymentSuceesResponse;
import com.otapp.net.R;
import com.otapp.net.application.Otapp;
import com.otapp.net.fragment.MovieOrderReviewFragment;
import com.otapp.net.fragment.ServiceFragment;
import com.otapp.net.fragment.ThemeParkOrderReviewFragment;
import com.otapp.net.helper.SHA;
import com.otapp.net.home.core.MyBookingResponse;
import com.otapp.net.home.fragment.MyBookingEventDetails;
import com.otapp.net.home.fragment.MyBookingMoviesDetails;
import com.otapp.net.home.fragment.MyBookingTicketDetails;
import com.otapp.net.home.fragment.MyBookingsThemeParkSuccessDetails;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.model.MovieSuccessPojo;
import com.otapp.net.model.ThemeParkSuccessPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.MyViewHolder> {
    Context context;
    List<MyBookingResponse.MyBookingTransactoins> bookingTransactoinsList;

    MyBookingResponse.MyBookingTransactoins myBookingTransactoins;
    MyBookingResponse.MyBookingTransactoins tempMyBookingTraction;
    /*  List<MyBookingResponse.MyBookingTransactoins.FlightDetails> flightDetailsList=new ArrayList<>();
      MyBookingResponse.MyBookingTransactoins.FlightDetails flightDetails;*/
    int AuthKey;
    String strAgentId = "", strBookFrom = "";
    String strDay, strMonth, strYear;

    FlightCityPojo.Data mFlightCityPojoData;


    public MyBookingsAdapter(Context context, List<MyBookingResponse.MyBookingTransactoins> bookingTransactoinsList) {
        this.context = context;
        this.bookingTransactoinsList = bookingTransactoinsList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_my_bookings, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myBookingTransactoins = bookingTransactoinsList.get(i);
        myViewHolder.tvEventName.setText(myBookingTransactoins.event_name);
        myViewHolder.tvEventCity.setText(myBookingTransactoins.event_city);
        myViewHolder.tvTicketNo.setText(myBookingTransactoins.ticket_no);


        myViewHolder.tvAmount.setText(myBookingTransactoins.tran_amount);
        myViewHolder.tvEventType.setText(myBookingTransactoins.pay_type);


        try {
            byte[] data = Base64.decode(myBookingTransactoins.event_city, Base64.DEFAULT);
            myViewHolder.tvEventCity.setText(new String(data, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = format.parse(myBookingTransactoins.event_date);
            String strDate, strTime;
            strDay = (String) android.text.format.DateFormat.format("EEE", date);
            strDate = (String) android.text.format.DateFormat.format("dd", date); // 20
            strMonth = (String) android.text.format.DateFormat.format("MMM", date); // Jun
            strYear = (String) android.text.format.DateFormat.format("yyyy", date); // Jun
            strTime = (String) android.text.format.DateFormat.format("HH:mm", date); // Jun

            myViewHolder.tvDate.setText(strDay + "," + strDate + " " + strMonth + " " + strYear + " " + strTime);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (myBookingTransactoins.event_thumb != null) {
            myViewHolder.aviProgress.setVisibility(View.VISIBLE);
            myViewHolder.ivEvent.setVisibility(View.GONE);
            Picasso.get().load(myBookingTransactoins.event_thumb).into(myViewHolder.ivEvent, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    myViewHolder.ivEvent.setVisibility(View.VISIBLE);
                    myViewHolder.aviProgress.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    myViewHolder.aviProgress.setVisibility(View.GONE);
                }
            });
        }
        if (myBookingTransactoins.movie_thumb != null) {
            myViewHolder.aviProgress.setVisibility(View.VISIBLE);
            myViewHolder.ivEvent.setVisibility(View.GONE);
            Picasso.get().load(myBookingTransactoins.movie_thumb).into(myViewHolder.ivEvent, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    myViewHolder.ivEvent.setVisibility(View.VISIBLE);
                    myViewHolder.aviProgress.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    myViewHolder.aviProgress.setVisibility(View.GONE);
                }
            });
        }
        if(myBookingTransactoins.tp_name!=null){
            if(!myBookingTransactoins.tp_name.equals("")){
                try {
                    myViewHolder.layoutTime.setVisibility(View.VISIBLE);
                    myViewHolder.layoutDate.setVisibility(View.VISIBLE);
                    byte[] data = Base64.decode(myBookingTransactoins.tp_city, Base64.DEFAULT);
                    myViewHolder.tvEventName.setText(myBookingTransactoins.tp_name);
                    myViewHolder.tvEventCity.setText(new String(data, "UTF-8"));
                    myViewHolder.aviProgress.setVisibility(View.VISIBLE);
                    myViewHolder.ivEvent.setVisibility(View.GONE);
                    myViewHolder.tvThemeDate.setText(myBookingTransactoins.tp_date);
                    myViewHolder.layoutDateTime.setVisibility(View.GONE);
                    Picasso.get().load(myBookingTransactoins.tp_thumb).into(myViewHolder.ivEvent, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            myViewHolder.ivEvent.setVisibility(View.VISIBLE);
                            myViewHolder.aviProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            myViewHolder.aviProgress.setVisibility(View.GONE);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            myViewHolder.layoutTime.setVisibility(View.GONE);
            myViewHolder.layoutDate.setVisibility(View.GONE);
            myViewHolder.layoutDateTime.setVisibility(View.VISIBLE);
        }

        if (myBookingTransactoins.theater_name != null) {
            myViewHolder.tvTheaterName.setVisibility(View.VISIBLE);
            myViewHolder.tvTheaterName.setText(myBookingTransactoins.theater_name + "," + myBookingTransactoins.theater_city);
            myViewHolder.tvTheaterName.setTextColor(Color.parseColor("#000000"));
        }
        if (myBookingTransactoins.movie_name != null) {
            myViewHolder.tvEventName.setText(myBookingTransactoins.movie_name);
            myViewHolder.tvEventName.setTextColor(Color.parseColor("#000000"));
            myViewHolder.llCityDateTimeLayout.setVisibility(View.GONE);

        }
        if (myBookingTransactoins.show_time != null) {
            myViewHolder.layoutMovieTime.setVisibility(View.VISIBLE);

            try {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = format.parse(myBookingTransactoins.show_time);
                String strDate, strMonth, time, year, strDay;
                strDate = (String) android.text.format.DateFormat.format("dd", date); // 20
                strDay = (String) android.text.format.DateFormat.format("EEE", date); // 20
                strMonth = (String) android.text.format.DateFormat.format("MMM", date); // Jun
                time = (String) android.text.format.DateFormat.format("HH:mm", date); // Jun
                year = (String) android.text.format.DateFormat.format("yyyy", date); // Jun
                myViewHolder.tvMoviesTime.setText(strDay + " " + strDate + " " + strMonth + "," + year + " " + time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            myViewHolder.layoutMovieTime.setVisibility(View.GONE);
        }
        if (myBookingTransactoins.airline_pnr != null) {
            myViewHolder.layoutFlight.setVisibility(View.VISIBLE);
            myViewHolder.tvFlightStatus.setVisibility(View.VISIBLE);
            Log.d("Log", "fligt satus " + myBookingTransactoins.tran_stat);
            myViewHolder.tvFlightStatus.setText(myBookingTransactoins.tran_stat);
            myViewHolder.tvEventStatus.setVisibility(View.GONE);
            myViewHolder.tvFromCity.setText(myBookingTransactoins.start_city);
            myViewHolder.tvToCity.setText(myBookingTransactoins.end_city);
            myViewHolder.tvPnrNo.setText(myBookingTransactoins.airline_pnr);

            try {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = format.parse(myBookingTransactoins.departure_date);
                String strDate, strMonth, time, year;
                strDate = (String) android.text.format.DateFormat.format("dd", date); // 20
                strMonth = (String) android.text.format.DateFormat.format("MMM", date); // Jun
                time = (String) android.text.format.DateFormat.format("HH:mm aa", date); // Jun
                year = (String) android.text.format.DateFormat.format("yyyy", date); // Jun
                myViewHolder.tvDepartureDate.setText(strDate + " " + strMonth + " " + year + " " + time.toUpperCase());
            } catch (Exception e) {
                e.printStackTrace();
            }


            myViewHolder.llCityDateTimeLayout.setVisibility(View.GONE);

            Picasso.get().load(R.drawable.plan_icon).into(myViewHolder.ivEvent, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        } else {
            myViewHolder.layoutFlight.setVisibility(View.GONE);
        }
        if (myBookingTransactoins.tran_status != null) {
            myViewHolder.tvEventStatus.setText(myBookingTransactoins.tran_status);
            myViewHolder.tvFlightStatus.setVisibility(View.GONE);
            if (myBookingTransactoins.tran_status.equals("IN-PROCESS")) {
                myViewHolder.tvEventStatus.setTextColor(Color.parseColor("#FFA500"));
            } else if (myBookingTransactoins.tran_status.equals("FAILED") || myBookingTransactoins.tran_status.equals("Failed")) {
                myViewHolder.tvEventStatus.setTextColor(Color.parseColor("#FF0000"));
            } else if (myBookingTransactoins.tran_status.equals("SESSION-TIME-OUT")) {
                myViewHolder.tvEventStatus.setTextColor(Color.parseColor("#FF0000"));
            } else if (myBookingTransactoins.tran_status.equals("CONFIRMED")) {
                myViewHolder.tvEventStatus.setTextColor(Color.parseColor("#39D39F"));
            } else if (myBookingTransactoins.tran_status.equals("CANCELLED")) {
                myViewHolder.tvEventStatus.setTextColor(Color.parseColor("#FF0000"));
            } else {
                myViewHolder.tvEventStatus.setTextColor(Color.parseColor("#FF0000"));
            }
        }
        if (myBookingTransactoins.tran_stat != null) {

            if (myBookingTransactoins.tran_stat.equals("IN-PROCESS")) {
                myViewHolder.tvFlightStatus.setTextColor(Color.parseColor("#FFA500"));
            } else if (myBookingTransactoins.tran_stat.equals("FAILED") || myBookingTransactoins.tran_stat.equals("Failed")) {
                myViewHolder.tvFlightStatus.setTextColor(Color.parseColor("#FF0000"));
            } else if (myBookingTransactoins.tran_stat.equals("SESSION-TIME-OUT")) {
                myViewHolder.tvFlightStatus.setTextColor(Color.parseColor("#FF0000"));
            } else if (myBookingTransactoins.tran_stat.equals("CONFIRMED")) {
                myViewHolder.tvFlightStatus.setTextColor(Color.parseColor("#39D39F"));
            } else {
                myViewHolder.tvFlightStatus.setTextColor(Color.parseColor("#FF0000"));
            }
        }


        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }
        AuthKey = Integer.parseInt(String.valueOf(random));
        strAgentId = AppConstants.agentId;
        strBookFrom = AppConstants.bookFrom;

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if (bookingTransactoinsList.get(i).event_name != null) {
                    if (bookingTransactoinsList.get(i).tran_status.equalsIgnoreCase("CONFIRMED")) {
                        setPaymentSucessful(bookingTransactoinsList.get(i).ticket_no);
                    }
                } else if (bookingTransactoinsList.get(i).movie_name != null) {
                    if (bookingTransactoinsList.get(i).tran_status.equalsIgnoreCase("CONFIRMED")) {
                        setPaymentMoviesSucessful(bookingTransactoinsList.get(i).ticket_no);
                    }
                } else if (bookingTransactoinsList.get(i).airline_pnr != null) {
                    if (bookingTransactoinsList.get(i).tran_stat.equalsIgnoreCase("CONFIRMED")) {
                        tempMyBookingTraction = bookingTransactoinsList.get(i);
                        Intent intent = new Intent(context, MyBookingTicketDetails.class);
                        intent.putExtra("flightDetails", new Gson().toJson(tempMyBookingTraction));
                        context.startActivity(intent);
                  /*  AppCompatActivity activity = (AppCompatActivity) context;
                    Fragment myFragment = new MyBookingFlightDetailsFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.mFrameLayout, myFragment).addToBackStack(null).commit();*/
                    }
                }
                else if(bookingTransactoinsList.get(i).tp_name!= null){
                        if(bookingTransactoinsList.get(i).tran_status.equalsIgnoreCase("CONFIRMED")){
                            setThemeParkPaymentSucessful(bookingTransactoinsList.get(i).ticket_no);
                        }
                    }
                }

        });


    }

    @Override
    public int getItemCount() {
        return bookingTransactoinsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName, tvTicketNo, tvEventCity, tvEventType, tvAmount, tvDate, tvEventStatus, tvTheaterName;
        TextView tvMoviesTime, tvFromCity, tvToCity, tvPnrNo, tvDepartureDate, tvFlightStatus,tvThemeDate;
        ImageView ivEvent;
        LinearLayout llCityDateTimeLayout, layoutMovieTime, layoutFlight,layoutDate,layoutTime,layoutDateTime;
        AVLoadingIndicatorView aviProgress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvTicketNo = itemView.findViewById(R.id.tvTicketNo);
            tvEventCity = itemView.findViewById(R.id.tvCity);
            tvEventType = itemView.findViewById(R.id.tvPaymentType);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvEventStatus = itemView.findViewById(R.id.tvEventStatus);
            ivEvent = itemView.findViewById(R.id.ivEvent);
            tvTheaterName = itemView.findViewById(R.id.tvTheaterName);
            llCityDateTimeLayout = itemView.findViewById(R.id.cityDateTimeLayout);
            tvMoviesTime = itemView.findViewById(R.id.tvMovieTime);
            layoutMovieTime = itemView.findViewById(R.id.layoutMovieTime);
            tvFromCity = itemView.findViewById(R.id.tvFromCity);
            tvToCity = itemView.findViewById(R.id.tvToCity);
            tvPnrNo = itemView.findViewById(R.id.tvPnrNo);
            tvDepartureDate = itemView.findViewById(R.id.tvDepartureDate);
            layoutFlight = itemView.findViewById(R.id.layoutFlight);
            tvFlightStatus = itemView.findViewById(R.id.tvFlightStatus);
            aviProgress = itemView.findViewById(R.id.aviProgress);
            layoutDate=itemView.findViewById(R.id.layoutThemedate);
            layoutTime=itemView.findViewById(R.id.layoutThemeTime);
            tvThemeDate=itemView.findViewById(R.id.tvThemeDate);
            layoutDateTime=itemView.findViewById(R.id.layoutDateTime);


        }
    }

    private void setPaymentSucessful(String mBookingID) {

       /* ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();*/
        Utils.showProgressDialog(context);
        String apiKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4, AuthKey + "tKt!nf0")));

        String key = calculateHash(AuthKey, calculateHash(1, calculateHash(4, strAgentId + strBookFrom + mBookingID + "TkT1nF)")));


        OtappApiServices otappApiServices = RestClient.getClient(true);
        Call<PaymentSuceesResponse> callTicketInfo = otappApiServices.getSuccessTicketInfo(apiKey, strAgentId, strBookFrom, String.valueOf(AuthKey), key, mBookingID);

        callTicketInfo.enqueue(new Callback<PaymentSuceesResponse>() {
            @Override
            public void onResponse(Call<PaymentSuceesResponse> call, Response<PaymentSuceesResponse> response) {
                //  progressDialog.dismiss();
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response Event details : " + jsonObjectResponse);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Utils.closeProgressDialog();
                    PaymentSuceesResponse mEventSuccessPojo = response.body();
                    if (mEventSuccessPojo != null) {
                        if (mEventSuccessPojo.status.equalsIgnoreCase("200")) {

                            Utils.closeProgressDialog();
                            // switchFragment(EventOrderReviewFragment.newInstance(), EventOrderReviewFragment.Tag_EventOrderReviewFragment, bundle);
                            Intent intent = new Intent(context, MyBookingEventDetails.class);
                            intent.putExtra(AppConstants.BNDL_MOVIE_RESPONSE, new Gson().toJson(mEventSuccessPojo));
                            context.startActivity(intent);


                        } else {
                            /* Utils.showToast(getApplicationContext(), mEventSuccessPojo.message);*/
                            //    popBackStack(ServiceFragment.Tag_ServiceFragment);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<PaymentSuceesResponse> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                // progressDialog.dismiss();
                Utils.closeProgressDialog();
            }
        });

    }

    private void setPaymentMoviesSucessful(String mBookingID) {

        Utils.showProgressDialog(context);

        ApiInterface mApiInterface = com.otapp.net.rest.RestClient.getClient(true);
        Call<MovieSuccessPojo> mCall = mApiInterface.getPaymentSuccess(mBookingID, Otapp.mUniqueID);
        mCall.enqueue(new Callback<MovieSuccessPojo>() {
            @Override
            public void onResponse(Call<MovieSuccessPojo> call, Response<MovieSuccessPojo> response) {

                Utils.closeProgressDialog();
                if (response.isSuccessful()) {

                    MovieSuccessPojo mMovieSuccessPojo = response.body();
                    if (mMovieSuccessPojo != null) {
                        if (mMovieSuccessPojo.status.equalsIgnoreCase("200")) {
                    /*   Bundle bundle = new Bundle();
                        bundle.putString(Constants.BNDL_MOVIE_RESPONSE, new Gson().toJson(mMovieSuccessPojo.data)); */
                            // switchFragment(MovieOrderReviewFragment.newInstance(), MovieOrderReviewFragment.Tag_MovieOrderReviewFragment, bundle);
                      /*  AppCompatActivity activity = (AppCompatActivity) context;
                        Fragment myFragment = new MovieOrderReviewFragment();
                        myFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.mFrameLayout, myFragment).addToBackStack(null).commit();*/
                            Intent intent = new Intent(context, MyBookingMoviesDetails.class);
                            intent.putExtra(Constants.BNDL_MOVIE_RESPONSE, new Gson().toJson(mMovieSuccessPojo.data));
                            context.startActivity(intent);
                        } /*else {
                        Utils.showToast(getActivity(), mMovieSuccessPojo.message);
                        popBackStack(ServiceFragment.Tag_ServiceFragment);
                    }*/
                    }

                }
            }

            @Override
            public void onFailure(Call<MovieSuccessPojo> call, Throwable t) {
                LogUtils.e("", "onFailure::" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });


    }
    private void setThemeParkPaymentSucessful(String mBookingID) {

      /*  if (!Utils.isInternetConnected(context)) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }*/
        Utils.showProgressDialog(context);

        ApiInterface mApiInterface = com.otapp.net.rest.RestClient.getClient(true);
        Call<ThemeParkSuccessPojo> mCall = mApiInterface.getThemeParkPaymentSuccess(mBookingID, Otapp.mUniqueID);
        mCall.enqueue(new Callback<ThemeParkSuccessPojo>() {
            @Override
            public void onResponse(Call<ThemeParkSuccessPojo> call, Response<ThemeParkSuccessPojo> response) {

                Utils.closeProgressDialog();
                if (response.isSuccessful()) {

                    ThemeParkSuccessPojo mThemeParkSuccessPojo = response.body();
                    if (mThemeParkSuccessPojo != null) {
                        if (mThemeParkSuccessPojo.status.equalsIgnoreCase("200")) {
                            Intent intent = new Intent(context, MyBookingsThemeParkSuccessDetails.class);
                            intent.putExtra(Constants.BNDL_THEME_PARK_RESPONSE, new Gson().toJson(mThemeParkSuccessPojo));
                            context.startActivity(intent);
                        } else {
                            Utils.showToast(context, mThemeParkSuccessPojo.message);
                           // popBackStack(ServiceFragment.Tag_ServiceFragment);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ThemeParkSuccessPojo> call, Throwable t) {
                LogUtils.e("", "onFailure::" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });


    }


    private String calculateHash(int authKey, String sHash) {
        switch (authKey) {
            case 1:
                sHash = SHA.MD5(sHash);
                break;
            case 2:
                try {
                    sHash = SHA.SHA1(sHash);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    sHash = SHA.SHA256(sHash);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    sHash = SHA.SHA512(sHash);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
        return sHash;
    }
}
