package com.otapp.net.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.otapp.net.R;
import com.otapp.net.fragment.FlightOneDetailsFragment;
import com.otapp.net.model.FlightOneDetailsPojo;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.model.FlightReturnListPojo;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightDetailsAdapter extends BaseAdapter {

    private Context mContext;
    private String clasName;
    private FlightOneDetailsFragment mFlightOneDetailsFragment;
    private boolean isRefundableFare;
    String endTime;
    String startTime;
    String srtFlag = "";
    List<FlightOneDetailsPojo.FlightDetail> mFlightDetailsList = new ArrayList<>();
    FlightOneListPojo.Data mFlightData;
    FlightReturnListPojo.Data mFlightReturnData;
    List<FlightReturnListPojo.Data> mFilightRetrunList= new ArrayList<>();


    List<List<FlightReturnListPojo.City>> cities_J;
    List<FlightReturnListPojo.City> cities_K;
    List<List<FlightReturnListPojo.City>> cities_L;


    public FlightDetailsAdapter(Context mContext, FlightOneDetailsFragment mFlightOneDetailsFragment, String clasName,FlightOneListPojo.Data mFlightData) {
        this.mContext = mContext;
        this.clasName = clasName;
        this.mFlightOneDetailsFragment = mFlightOneDetailsFragment;
        this.mFlightData=mFlightData;
    }
    public FlightDetailsAdapter(Context mContext, FlightOneDetailsFragment mFlightOneDetailsFragment, String clasName, FlightReturnListPojo.Data mFlightReturnData) {
        this.mContext = mContext;
        this.clasName = clasName;
        this.mFlightOneDetailsFragment = mFlightOneDetailsFragment;
        this.mFlightReturnData=mFlightReturnData;
    }


    public void addAll(List<FlightOneDetailsPojo.FlightDetail> mTempFlightDetailsList) {

        if (mFlightDetailsList != null && mFlightDetailsList.size() > 0) {
            mFlightDetailsList.clear();
        }

        if (mTempFlightDetailsList != null && mTempFlightDetailsList.size() > 0) {
            mFlightDetailsList.addAll(mTempFlightDetailsList);
        }

        notifyDataSetChanged();
    }

    public void setStatus(boolean isRefundableFare) {
        this.isRefundableFare = isRefundableFare;
    }

    @Override
    public int getCount() {
        return mFlightDetailsList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFlightDetailsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_flight_details, null);
            ViewHolder holder = new ViewHolder();
            holder.ivAirline = view.findViewById(R.id.ivAirline);
            holder.tvDeparture = view.findViewById(R.id.tvDeparture);
            holder.tvDestination = view.findViewById(R.id.tvDestination);
            holder.tvDate = view.findViewById(R.id.tvDate);
            holder.tvAirline = view.findViewById(R.id.tvAirline);
            holder.tvAirlineNo = view.findViewById(R.id.tvAirlineNo);
            holder.tvAirlineClass = view.findViewById(R.id.tvAirlineClass);
            holder.tvDepartureCity = view.findViewById(R.id.tvDepartureCity);
            holder.tvDepartureCode = view.findViewById(R.id.tvDepartureCode);
            holder.tvDestinationCode = view.findViewById(R.id.tvDestinationCode);
            holder.tvDestinationCity = view.findViewById(R.id.tvDestinationCity);
            holder.tvDepartureTime = view.findViewById(R.id.tvDepartureTime);
            holder.tvDestinationTime = view.findViewById(R.id.tvDestinationTime);
            holder.tvDepartureAirport = view.findViewById(R.id.tvDepartureAirport);
            holder.tvTime = view.findViewById(R.id.tvTime);
            holder.tvDestinationAirport = view.findViewById(R.id.tvDestinationAirport);
            holder.tvBaggage = view.findViewById(R.id.tvBaggage);
            holder.tvStatus = view.findViewById(R.id.tvStatus);
            holder.tvFare = view.findViewById(R.id.tvFare);
            holder.tvOperateCarrier=view.findViewById(R.id.tvOpeateCarrer);
            holder.tvEquipmentName=view.findViewById(R.id.tvEquipmentName);
//            holder.aviProgress = view.findViewById(R.id.aviProgress);
            holder.tvOverlayTime = view.findViewById(R.id.overlayTime);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        final FlightOneDetailsPojo.FlightDetail mFlightDetail = mFlightDetailsList.get(i);

        if (mFlightDetail != null) {
            holder.tvDeparture.setText("" + mFlightDetail.startAirportCity);
            holder.tvDestination.setText("" + mFlightDetail.endAirportCity);
            holder.tvDepartureCity.setText("" + mFlightDetail.startAirportCity);
            holder.tvDestinationCity.setText("" + mFlightDetail.endAirportCity);
            holder.tvDepartureCode.setText("" + mFlightDetail.startAirport);
            holder.tvDestinationCode.setText("" + mFlightDetail.endAirport);
            holder.tvDate.setText("" + Utils.getFlightDate(mFlightDetail.startDate) + " | " + mContext.getString(R.string.non_stop));
            holder.tvDepartureTime.setText("" + Utils.getFlightTime(mFlightDetail.startDate));
            holder.tvDestinationTime.setText("" + Utils.getFlightTime(mFlightDetail.endDate));
            holder.tvAirline.setText(mFlightDetail.airline);
            holder.tvAirlineClass.setText(clasName);
            holder.tvDepartureAirport.setText(mFlightDetail.startAirportName);
            holder.tvDestinationAirport.setText(mFlightDetail.endAirportName);
//            holder.tvStatus.setText(mFlightDetail.status);
            holder.tvStatus.setText(isRefundableFare ? mContext.getString(R.string.refundable) : mContext.getString(R.string.non_refundable));
            holder.tvBaggage.setText(mContext.getString(R.string.baggage) + " " + mFlightDetail.baggageInfo);
            holder.tvAirlineNo.setText("- " + mFlightDetail.flightNumber);
            if(mFlightData!=null) {
                holder.tvEquipmentName.setText(" | " + mFlightData.cities.get(i).flightEupipmentName);
                if (!mFlightData.cities.get(i).operatingCarrier.equals("")) {
                    holder.tvOperateCarrier.setText(" | Operated by " + mFlightData.cities.get(i).operatingCarrier);
                }
            }
            if(mFlightReturnData!=null) {

                for(int j=0; j<mFlightReturnData.cities.size();j++){

                    cities_J=mFlightReturnData.cities.get(j);
                  for(int k=0; k<cities_J.size();k++){
                      cities_K=cities_J.get(k);
                      for(int l=0;l<cities_K.size();l++){
                          FlightReturnListPojo.City city= cities_K.get(l);

                          Log.d("Log", "retrun data "+city.flightEupipmentName);
                          if (mFlightDetail.uid.equals(city.uid)) {
                              if (!city.operatingCarrier.equals("")) {
                                  holder.tvOperateCarrier.setText(" | Operated by " + city.operatingCarrier);
                              }
                              holder.tvEquipmentName.setText(city.flightEupipmentName);
                          }
                      }
                  }

                }


/*
                try {

                  */
/*  if (mFlightDetail.uid.equals(mFlightReturnData.cities.get(i).get(0).get(0).uid)) {*//*

                        Log.d("Log", "oprated by " + mFlightReturnData.cities.get(i).get(0).get(0).flightEupipmentName);
                        if (!mFlightReturnData.cities.get(i).get(0).get(0).operatingCarrier.equals("")) {
                            holder.tvOperateCarrier.setText(" | Operated by " + mFlightReturnData.cities.get(i).get(0).get(0).operatingCarrier);
                        }
                        holder.tvEquipmentName.setText(mFlightReturnData.cities.get(i).get(0).get(0).flightEupipmentName);
                        */
/*    j = mFlightReturnData.cities.size();*//*

                   */
/* } else {
                        Log.d("Log", "else");
                    }*//*


                }catch (Exception e){
                    e.printStackTrace();
                }
*/
            }
            long mDuration = mFlightDetail.duration;

            try {

//                Date date1 = DateFormate.sdfAirportServerDate.parse(mFlightDetail.startDate);
//                Date date2 = DateFormate.sdfAirportServerDate.parse(mFlightDetail.endDate);

                String temp = mFlightDetailsList.get(0).startAirportCity;
                String endtcity = mFlightDetailsList.get(i).startAirportCity;

                srtFlag = mFlightDetailsList.get(i).flag;
                if (i != mFlightDetailsList.size()) {
                    try {
                        endTime = mFlightDetailsList.get(i).endDate;
                        startTime = mFlightDetailsList.get(i + 1).startDate;
                        //   Toast.makeText(mContext, "first", Toast.LENGTH_SHORT).show();

                        // if(!mFlightDetailsList.get(i+1).flag.equals("return")&& !mFlightDetailsList.get(i).flag.equals("depature")) {
                        Log.d("Log", "flage " + srtFlag);
                        if (srtFlag == null) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date1 = format.parse(endTime);
                            Date date2 = format.parse(startTime);
                            long difference = date2.getTime() - date1.getTime();
                            long diffSeconds = difference / 1000 % 60;
                            long diffMinutes = difference / (60 * 1000) % 60;
                            long diffHours = difference / (60 * 60 * 1000);
                            long diffDays = difference / (24 * 60 * 60 * 1000);
                            holder.tvOverlayTime.setText(String.valueOf(holder.tvDestinationCode.getText().toString() + "  " + diffHours + " hrs  " + diffMinutes + " min" + " Layover"));
                            //   Toast.makeText(mContext, "OneWay", Toast.LENGTH_SHORT).show();
                        } else if (mFlightDetailsList.get(i).flag.equals(mFlightDetailsList.get(i + 1).flag)) {
                            Log.d("Log", "endTime " + endTime);
                            Log.d("Log", "startTime " + startTime);
                            //  Toast.makeText(mContext, "" + endTime + "  " + startTime, Toast.LENGTH_SHORT).show();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date1 = format.parse(endTime);
                            Date date2 = format.parse(startTime);
                            long difference = date2.getTime() - date1.getTime();
                            long diffSeconds = difference / 1000 % 60;
                            long diffMinutes = difference / (60 * 1000) % 60;
                            long diffHours = difference / (60 * 60 * 1000);
                            long diffDays = difference / (24 * 60 * 60 * 1000);
                            holder.tvOverlayTime.setText(String.valueOf(holder.tvDestinationCode.getText().toString() + "  " + diffHours + " hrs  " + diffMinutes + " min" + " Layover"));

                                /*Log.d("Log",""+mFlightDetailsList.get(i).flag);
                                Log.d("Log",""+mFlightDetailsList.get(i+1).flag);
                                Log.d("Log","True");*/
                            //  Toast.makeText(mContext, "REturn", Toast.LENGTH_SHORT).show();


                               /* if (i >= mFlightDetailsList.size()) {
                                    holder.tvOverlayTime.setVisibility(View.INVISIBLE);
                                } else {*/

                            //    mFlightDetailsList.get(j).layOvertime = String.valueOf("Layover  " + diffHours + "hrs" + diffMinutes + "min");
                            //  holder.tvOverlayTime.setText(String.valueOf(holder.tvDestinationCode.getText().toString() + "  " + diffHours + " hrs  " + diffMinutes + " min" + " Layover"));
                            /* }*/
                        } else {
                            Log.d("Log", "False");
                            holder.tvOverlayTime.setText("");
                            //   Toast.makeText(mContext, "Esle", Toast.LENGTH_SHORT).show();

                                /*Log.d("Log", "endTime " + endTime);
                                Log.d("Log", "startTime " + startTime);
                                //  Toast.makeText(mContext, "" + endTime + "  " + startTime, Toast.LENGTH_SHORT).show();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date date1 = format.parse(endTime);
                                Date date2 = format.parse(startTime);
                                long difference = date2.getTime() - date1.getTime();
                                long diffSeconds = difference / 1000 % 60;
                                long diffMinutes = difference / (60 * 1000) % 60;
                                long diffHours = difference / (60 * 60 * 1000);
                                long diffDays = difference / (24 * 60 * 60 * 1000);
                                holder.tvOverlayTime.setText(String.valueOf(holder.tvDestinationCode.getText().toString() + "  " + diffHours + " hrs  " + diffMinutes + " min" + " Layover"));

                                Log.d("Log","True");
                               *//* if (i >= mFlightDetailsList.size()) {
                                    holder.tvOverlayTime.setVisibility(View.INVISIBLE);
                                } else {*//*

                                //    mFlightDetailsList.get(j).layOvertime = String.valueOf("Layover  " + diffHours + "hrs" + diffMinutes + "min");
                                //  holder.tvOverlayTime.setText(String.valueOf(holder.tvDestinationCode.getText().toString() + "  " + diffHours + " hrs  " + diffMinutes + " min" + " Layover"));
                                *//* }*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.tvOverlayTime.setVisibility(View.INVISIBLE);
                }


                String mTime = Utils.getTimeDifference(mDuration);
                holder.tvTime.setText(mTime);

            } catch (Exception e) {
                e.printStackTrace();
                holder.tvTime.setText("");
            }

            if (!TextUtils.isEmpty(mFlightDetail.logo)) {
//                holder.aviProgress.setVisibility(View.VISIBLE);

                Picasso.get().load(mFlightDetail.logo).into(holder.ivAirline, new Callback() {
                    @Override
                    public void onSuccess() {
//                        holder.aviProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
//                        holder.aviProgress.setVisibility(View.GONE);
                    }
                });

            } else {
//                holder.aviProgress.setVisibility(View.GONE);
            }

            holder.tvFare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFlightOneDetailsFragment.onFareRuleClicked();
                }
            });
        }

        return view;
    }


    private class ViewHolder {
        TextView tvDeparture, tvDestination, tvDate, tvAirline, tvAirlineNo, tvAirlineClass, tvDepartureCity, tvDepartureCode, tvDestinationCode, tvDestinationCity, tvDepartureTime,
                tvDestinationTime, tvDepartureAirport, tvTime, tvDestinationAirport, tvBaggage, tvStatus, tvFare, tvOverlayTime, tvOperateCarrier, tvEquipmentName;
        ImageView ivAirline;
//        AVLoadingIndicatorView aviProgress;
    }
}
