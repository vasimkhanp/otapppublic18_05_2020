package com.otapp.net.Bus.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.Bus.Core.Agent;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.AvailableBuses;
import com.otapp.net.Bus.Core.ProcessSeat;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.Bus.Core.Seat;
import com.otapp.net.Bus.Core.SeatMap;
import com.otapp.net.Bus.Impl.SeatChangeListener;
import com.otapp.net.Bus.Network.OtappApiInstance;
import com.otapp.net.Bus.Network.OtappApiService;
import com.otapp.net.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.otapp.net.Bus.Core.SHA.calculateHash;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatsViewHolder> /*implements OtappResponsePresenter */{

    private Context context;
    private ArrayList<Seat> seatsArrayList;
    private SeatChangeListener seatChangeListener;
    private SearchBusDetails searchBusDetails;
    private AvailableBuses availableBuses;
    private String sKey;
    private SeatMap seatMap;
    private String sIP;
    private ProgressDialog progressDialog;
    private String sIMEI;
    private String sTicketNo;
    private int AuthKey;
    private String sAppVersion;
    private String sApiKey;
    private String sLanguage = "1";
    private String sGender = "Male";
    private String sLatitude = "0.0";
    private String sLongitude = "0.0";
 //   private Agent agent;
    private Seat seat;
    ProcessSeat processSeat;

    private ArrayList<Object> lowerSeatArrayList;
    private ArrayList<Object> upperSeatArrayList;
    private String sProcessSeats="";
    private String sAvailableSets="";
    private String[] sSeatTypeArray;
    private String[] sSeatsNameArray;
    String sSeatNo;

   /* public SeatAdapter(Context context, ArrayList<Object> lowerSeatArrayList, ArrayList<Object> upperSeatArrayList, String sProcessSeats, String sAvailableSets) {
        this.context = context;
        this.lowerSeatArrayList = lowerSeatArrayList;
        this.upperSeatArrayList = upperSeatArrayList;
        this.sProcessSeats = sProcessSeats;
        this.sAvailableSets = sAvailableSets;
    }*/
    public SeatAdapter(Context context, ArrayList<Seat> seatsArrayList,
                       SeatChangeListener seatChangeListener, SearchBusDetails searchBusDetails,
                       AvailableBuses availableBuses, String sKey, SeatMap seatMap, String sSeatTypeArray[]) {
        this.context = context;
        this.seatsArrayList = seatsArrayList;
        this.seatChangeListener = seatChangeListener;
        this.searchBusDetails = searchBusDetails;
        this.availableBuses = availableBuses;
        this.sKey = sKey;
        this.seatMap = seatMap;
        this.sSeatTypeArray=sSeatTypeArray;
    }

      //  agent = new Agent();
//        getDetails();
//        getLocalIpAddress();
   // }

    @NonNull
    @Override
    public SeatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_seat_adapter, viewGroup, false);
        SeatsViewHolder seatsViewHolder = new SeatsViewHolder(view);
        return seatsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SeatsViewHolder seatsViewHolder, int i) {
        Seat seat = seatsArrayList.get(i);
       /* Log.d("Log","Vasim : "+seat.toString());
        sSeatIdArray=seat.toString().split(",");
        Log.d("Log","number of seats "+sSeatIdArray.length);
*/

    //   Log.d("Log","Vasim "+seatsArrayList.size());
        if (seat.getsSeatNo().equals("0")) {
            seatsViewHolder.linearLayoutSeats.setVisibility(View.INVISIBLE);
        } else {
            seatsViewHolder.linearLayoutSeats.setVisibility(View.VISIBLE);
        }
         sSeatNo = seat.getsSeatNo();
        String sStatus = seat.getsIsSelected();
        String sFare= seat.getsFare();
        String sSeatTypeId= seat.getsTypeId();
       /* Log.d("Log","Vasim "+sSeatNo);
        Log.d("Log","Vasim "+sStatus);
        Log.d("Log","SeatTypeID "+seat.getsTypeId());
        Log.d("Log","SeatTypeName "+seat.getsType());*/
        sSeatsNameArray=sSeatNo.split("-");

        if(sSeatsNameArray.length>1) {
            seatsViewHolder.textViewSeatNo.setText(sSeatsNameArray[3]);
        }


        if(sSeatTypeId.equals("0")){
            if (sStatus.equals(AppConstants.Status.BOOKED)) {
                seatsViewHolder.imageViewSeat.setImageResource(R.drawable.ic_bus_seat_booked);
            } else if (sStatus.equals(AppConstants.Status.AVAILABLE)) {
                seatsViewHolder.imageViewSeat.setImageResource(R.drawable.ic_bus_seat_available);
            } else if (sStatus.equals(AppConstants.Status.SELECTED)) {
                seatsViewHolder.imageViewSeat.setImageResource(R.drawable.ic_bus_seat_selected);
            } else if (sStatus.equals(AppConstants.Status.PROCESSING)) {
                seatsViewHolder.imageViewSeat.setImageResource(R.drawable.ic_bus_seat_processing);
            }

        }else {
            if (sStatus.equals(AppConstants.Status.BOOKED)) {
                seatsViewHolder.imageViewSeat.setImageResource(R.drawable.ic_bus_star_seat_booked);
            } else if (sStatus.equals(AppConstants.Status.AVAILABLE)) {
                seatsViewHolder.imageViewSeat.setImageResource(R.drawable.ic_bus_star_seat_available);
            } else if (sStatus.equals(AppConstants.Status.SELECTED)) {
                seatsViewHolder.imageViewSeat.setImageResource(R.drawable.ic_bus_star_seat_selected);
            } else if (sStatus.equals(AppConstants.Status.PROCESSING)) {
                seatsViewHolder.imageViewSeat.setImageResource(R.drawable.ic_bus_star_seat_processing);
            }
        }

    }

    @Override
    public int getItemCount() {
        return seatsArrayList.size();

    }


/*
    private void processSeat() {
        boolean isConnected = AppConstants.isConnected(context);
        if (isConnected) {
            showProgress();
            String sFromId = searchBusDetails.getsFromId();
            String sToId = searchBusDetails.getsToId();
            String sDate = passengerBus.getsNewJourneyDate();
            String sBusId = passengerBus.getsBusID();
            if (searchBusDetails.isReturn()) {
                if (searchBusDetails.getAvailableBuses() != null) {
                    sFromId = searchBusDetails.getsReturnFromId();
                    sToId = searchBusDetails.getsReturnToId();
                }
            }
            String sRouteId = seatMap.getsRouteId();
            String sSubRouteId = seatMap.getsSubRouteId();
            String sBusRouteId = seatMap.getsBusRouteId();
            String sBusRouteScheduleId = seatMap.getsBusRouteScheduleId();
            String sBusRouteSeatId = seatMap.getsBusRouteSeatId();
            String sSeatId = seat.getsSeatNo();

            Random r = new Random();
            int random = r.nextInt(4 - 1 + 1) + 1;
            if (random > 4 && random < 1) {
                random = 3;
            }
            Agent agent = new Agent();
            agent.setsAgentId("924");
            AuthKey = Integer.parseInt(String.valueOf(random));
//            showProgress();
            sIP = AppConstants.Status.IP;
            sIMEI = AppConstants.Status.IMEI;
            sAppVersion = AppConstants.Status.APP_VERSION;

            String sURLKey = SHA.calculateHash(AuthKey, agent.getsAgentId() + AppConstants.ApiNames.OWNER_ID + AuthKey + "pr0cE5$");
            sApiKey = SHA.calculateHash(AuthKey, AppConstants.ApiNames.OWNER_ID + agent.getsAgentId()
                    + sFromId + sToId + sDate + sBusId + sKey + sRouteId + sSubRouteId + sBusRouteId
                    + sBusRouteScheduleId + sBusRouteSeatId + sSeatId + sIMEI + sLatitude + sLongitude + sIP + AuthKey + "P5e@t");
            String sUrl = AppConstants.ApiNames.API_URL + AppConstants.ApiNames.PROCESS_SEAT + sURLKey;

            OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
            Call<ProcessSeat> callGetStations = otappbusApiService.processSeat(sURLKey, AppConstants.ApiNames.OWNER_ID, agent.getsAgentId(),
                    String.valueOf(AuthKey), sFromId, sToId, sDate, sBusId, sKey, seatMap.getsRouteId(),
                    seatMap.getsSubRouteId(), seatMap.getsBusRouteId(), seatMap.getsBusRouteScheduleId(),
                    seatMap.getsBusRouteSeatId(), seat.getsSeatNo(), sIMEI, sLatitude, sLongitude, sIP, sApiKey, sAppVersion, sLanguage);
            Log.d("Log", "Process Seats URL : " + callGetStations.request().url());
            Log.d("Log", "Bus Route Schedule Id : " + seatMap.getsBusRouteScheduleId());
            Log.d("Log", "sDate : " + sDate);
            callGetStations.enqueue(new Callback<ProcessSeat>() {
                @Override
                public void onResponse(Call<ProcessSeat> call, Response<ProcessSeat> response) {
                    ProcessSeat processSeat = response.body();
                    JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response : " + jsonObjectResponse);
                    } catch (JSONException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    int Status = processSeat.getStatus();
                    String sMessage = processSeat.getsMessage();
                    if (Status == AppConstants.Status.SUCCESS) {
                        seat.setsFare("0");
                        Toast.makeText(context, sMessage, Toast.LENGTH_SHORT).show();
                        seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.SELECTED, "0");
                    } else if (Status == AppConstants.Status.INPROCESS) {
                        seat.setsFare("0");
                        Toast.makeText(context, sMessage, Toast.LENGTH_LONG).show();
                        seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.PROCESSING, "0");
                    } else if (Status == AppConstants.Status.SEAT_BOOKED) {
                        Toast.makeText(context, sMessage, Toast.LENGTH_LONG).show();
                        seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.BOOKED, "0");
                    } else if (Status == AppConstants.Status.MAX) {
                        Toast.makeText(context, sMessage, Toast.LENGTH_SHORT).show();
                        seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.AVAILABLE, "0");
                    } else {
                        seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.AVAILABLE, "0");
                        Toast.makeText(context, sMessage, Toast.LENGTH_SHORT).show();
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<ProcessSeat> call, Throwable t) {
                    hideProgress();
                    Toast.makeText(context, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            */
/*HashMap<String, String> parameter = new HashMap<String, String>();
            parameter.put("owner_id", AppConstants.ApiNames.OWNER_ID);
            parameter.put("agent_id", agent.getsAgentId());
            parameter.put("auth_key", String.valueOf(AuthKey));
            parameter.put("from_id", sFromId);
            parameter.put("to_id", sToId);
            parameter.put("trvl_dt", sDate);
            parameter.put("bus_id", sBusId);
            parameter.put("ukey", sKey);
            parameter.put("route_id", seatMap.getsRouteId());
            parameter.put("sub_route_id", seatMap.getsSubRouteId());
            parameter.put("bus_route_id", seatMap.getsBusRouteId());
            parameter.put("bus_route_schedule_id", seatMap.getsBusRouteScheduleId());
            parameter.put("bus_route_seat_id", seatMap.getsBusRouteSeatId());
            parameter.put("seat_id", seat.getsSeatNo());
            parameter.put("imei", sIMEI);
            parameter.put("lat", sLatitude);
            parameter.put("long", sLongitude);
            parameter.put("ip", sIP);
            parameter.put("key", sApiKey);
            parameter.put("app_ver", sAppVersion);

            Log.d("Log", "Process Seat URL : " + sUrl);
            Log.d("Log", "owner_id : " + AppConstants.ApiNames.OWNER_ID);
            Log.d("Log", "agent_id : " + agent.getsAgentId());
            Log.d("Log", "AuthKey : " + String.valueOf(AuthKey));
            Log.d("Log", "FromId : " + sFromId);
            Log.d("Log", "ToId : " + sToId);
            Log.d("Log", "Date : " + sDate);
            Log.d("Log", "bus_id : " + sBusId);
            Log.d("Log", "ukey : " + sKey);
            Log.d("Log", "route_id : " + seatMap.getsRouteId());
            Log.d("Log", "sub_route_id : " + seatMap.getsSubRouteId());
            Log.d("Log", "bus_route_id : " + seatMap.getsBusRouteId());
            Log.d("Log", "bus_route_schedule_id : " + seatMap.getsBusRouteScheduleId());
            Log.d("Log", "bus_route_seat_id : " + seatMap.getsBusRouteSeatId());
            Log.d("Log", "seat_id : " + seat.getsSeatNo());
            Log.d("Log", "imei : " + sIMEI);
            Log.d("Log", "lat : " + sLatitude);
            Log.d("Log", "long : " + sLongitude);
            Log.d("Log", "ip : " + sIP);
            Log.d("Log", "key : " + sApiKey);
            Log.d("Log", "app_ver : " + sAppVersion);

            otappbusAsyncTask = new NagiAsyncTask(context, sUrl, Request.Method.POST, parameter, this);
            otappbusAsyncTask.execute();*//*

        }
    }
*/

/*
    private void getDetails() {
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(context);
        sIMEI = telephonyInfo.getImsiSIM1();
        PackageInfo pInfo = null;
        String AndroidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        sAppVersion = pInfo.versionName;
        sAppVersion = AppConstants.Status.APP_VERSION;
        //RANDOM NUMBER
        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }
        AuthKey = Integer.parseInt(String.valueOf(random));
    }
*/
private void processSeat(){
    boolean isConnected = AppConstants.isConnected(context);
    if(isConnected){
        showProgress();
        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }

       // AuthKey = Integer.parseInt(String.valueOf(random));
      AuthKey=2;
        String subID= availableBuses.getSub_id();
        String TdId=availableBuses.getTdi_id();
        String lBId= availableBuses.getLb_id();
        String  pbiId= availableBuses.getPbi_id();
        String asiId= availableBuses.getAsi_id();
        String compId=com.otapp.net.utils.AppConstants.sCompId;
        String agentId=com.otapp.net.utils.AppConstants.sAgentId;
        String isFrom=com.otapp.net.utils.AppConstants.sIsFrom;
        String seatId=seat.getsSeatNo();
        String currency="1";
        String ukey=sKey;
        String from_id="";
        String toId="";
        String travelDate="";
        Log.d("Log","bordin point "+searchBusDetails.getsBoarding());
       // if(AppConstants.Keys.isReturnBus && !searchBusDetails.getsBoarding().isEmpty()){
        if(searchBusDetails.isRetrunActivityCalled()){
            Log.d("Log","RETRURN");
             from_id=searchBusDetails.getsReturnFromId();
             toId= searchBusDetails.getsReturnToId();
             travelDate=searchBusDetails.getsReturnDate();
        }else {
            Log.d("Log","onword");
            from_id=searchBusDetails.getsFromId();
            toId= searchBusDetails.getsToId();
             travelDate=searchBusDetails.getsDate();
        }


        String seatTypeId=seat.getsTypeId();
        String apiKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"Se@tPr0cE5$")));
        String postKey =calculateHash(AuthKey, calculateHash(1, calculateHash(4,from_id + toId + travelDate + subID +TdId+lBId+pbiId+asiId+seatId+currency+seatTypeId +ukey +compId+agentId+isFrom+"5e@Tpr)cE5$")));

        Log.d("Log","sbID "+subID);
        Log.d("Log","TdId "+TdId);
        Log.d("Log","lBId "+lBId);
        Log.d("Log","pbiId "+pbiId);
        Log.d("Log","asiId "+asiId);
        Log.d("Log","compId "+compId);
        Log.d("Log","agentId "+agentId);
        Log.d("Log","isFrom "+isFrom);
        Log.d("Log","seatId "+seatId);
        Log.d("Log","currency "+currency);
        Log.d("Log","ukey "+ukey);
        Log.d("Log","from_id "+from_id);
        Log.d("Log","toId "+toId);
        Log.d("Log","travelDate "+travelDate);
        Log.d("Log","apiKey "+apiKey);
        Log.d("Log","seatTypeid "+seatTypeId);

        Log.d("Log","key "+postKey);
        Log.d("Log","Auth key "+AuthKey);
        OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
        Call<ProcessSeat> seatProcessing = otappbusApiService.processSeat(apiKey,String.valueOf(AuthKey),subID,postKey,TdId,lBId,pbiId,asiId,
                compId,agentId,isFrom,seatId,currency,ukey,from_id,toId,travelDate,seatTypeId);
        seatProcessing.enqueue(new Callback<ProcessSeat>() {
            @Override
            public void onResponse(Call<ProcessSeat> call, Response<ProcessSeat> response) {
                hideProgress();
                if(response.body()!=null){
                    JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response SeatProcessing : " + jsonObjectResponse);
                    } catch (JSONException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    if(response.body().getStatus()==AppConstants.Status.SUCCESS) {
                        Toast.makeText(context, "" + response.body().getsMessage(), Toast.LENGTH_SHORT).show();
                        processSeat=response.body();
                      //  seat.setsIsSelected(AppConstants.Status.SELECTED);
                        seatChangeListener.onSeatChanged(seat.getsSeatNo(),AppConstants.Status.SELECTED,response.body().getsFare());

                    }else {
                        if(response.body().getStatus()==203) {
                            Toast.makeText(context, "" + response.body().getsMessage(), Toast.LENGTH_SHORT).show();
                            seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.AVAILABLE, response.body().getsFare());
                        }else {
                            if(response.body().getStatus()==201){
                              //  seat.setsIsSelected(AppConstants.Status.BOOKED);
                                Toast.makeText(context, ""+response.body().getsMessage(), Toast.LENGTH_SHORT).show();
                                seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.BOOKED, response.body().getsFare());

                            }else if(response.body().getStatus()==202){
                                //seat.setsIsSelected(AppConstants.Status.PROCESSING);
                                seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.PROCESSING, response.body().getsFare());
                                Toast.makeText(context, ""+response.body().getsMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ProcessSeat> call, Throwable t) {
                hideProgress();
            }
        });


    }
}


    private void showProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("" + context.getResources().getString(R.string.Loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

   /* @Override
    public void onResponse(String sResponse) {
        hideProgress();
        Log.d("Log", "Response : " + sResponse);
        if (sResponse != null) {
            try {
                JSONObject jsonObject = new JSONObject(sResponse);
                int Status = jsonObject.getInt("status");
                String sMessage = jsonObject.getString("message");
                if (Status == (AppConstants.Status.SUCCESS)) {
                    seat.setsFare("0");
                    Toast.makeText(context, sMessage, Toast.LENGTH_SHORT).show();
                    seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.SELECTED, "0");
                } else if (Status == (AppConstants.Status.INPROCESS)) {
                    seat.setsFare("0");
                    Toast.makeText(context, sMessage, Toast.LENGTH_LONG).show();
                    seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.PROCESSING, "0");
                } else if (Status == (AppConstants.Status.SEAT_BOOKED)) {
                    Toast.makeText(context, sMessage, Toast.LENGTH_LONG).show();
                    seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.BOOKED, "0");
                } else if (Status == (AppConstants.Status.MAX)) {
                    Toast.makeText(context, sMessage, Toast.LENGTH_SHORT).show();
                    seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.AVAILABLE, "0");
                } else {
                    seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.AVAILABLE, "0");
                    Toast.makeText(context, sMessage, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(context, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResponseError(String sResponse) {

    }*/

    public class SeatsViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayoutSeats;
        private ImageView imageViewSeat;
        private TextView textViewSeatNo;

        public SeatsViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutSeats = itemView.findViewById(R.id.linearLayoutSeats);
            imageViewSeat = itemView.findViewById(R.id.imageViewSeat);
            textViewSeatNo = itemView.findViewById(R.id.textViewSeatNo);

            linearLayoutSeats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* agent =new Agent();
                    agent.setsAgentId("924");
                    agent.setsUserType(AppConstants.Status.AGENT);*/
//                        Toast.makeText(context, "Type : "+agent.getsUserType(), Toast.LENGTH_SHORT).show();

                            if (getAdapterPosition() != -1) {
                                seat = seatsArrayList.get(getAdapterPosition());
                                String sStatus = seat.getsIsSelected();
                                Log.d("Log","seat position "+seat);
                                Log.d("Log","satus "+sStatus);
                                if (sStatus.equals(AppConstants.Status.BOOKED)) {
                                    Toast.makeText(context, "" + context.getResources().getString(R.string.Seat_Already_Booked), Toast.LENGTH_SHORT).show();
                                } else if (sStatus.equals(AppConstants.Status.AVAILABLE)) {
                                //    seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.SELECTED, "0");
                                    if(searchBusDetails.isRetrunActivityCalled()){


                                        if(searchBusDetails.getArrayListReturn()!=null) {


                                            if (searchBusDetails.getArrayListOnWords().size() > searchBusDetails.getArrayListReturn().size()) {
                                             //   Toast.makeText(context, "size "+searchBusDetails.getArrayListReturn().size(), Toast.LENGTH_SHORT).show();
                                                seat.setsIsSelected(AppConstants.Status.SELECTED);
                                                processSeat();
                                            } else {
                                                Toast.makeText(context, "More Selection Not Allowed!!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }else {

                                            seat.setsIsSelected(AppConstants.Status.SELECTED);
                                            processSeat();
                                        }
                                    }else {
                                        seat.setsIsSelected(AppConstants.Status.SELECTED);
                                        processSeat();
                                    }

                                } else if (sStatus.equals(AppConstants.Status.SELECTED)) {
                                    seat.setsIsSelected(AppConstants.Status.AVAILABLE);
                                 //   seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.AVAILABLE, "0");

                                    processSeat();
                                } else if (sStatus.equals(AppConstants.Status.PROCESSING)) {
                                    Toast.makeText(context, "" + context.getResources().getString(R.string.Seat_in_processing), Toast.LENGTH_SHORT).show();
                                }
                            }

                    }
//                    seat = seatsArrayList.get(getAdapterPosition());
//                    seat.setsStatus(AppConstants.Status.SELECTED);
//                    seatChangeListener.onSeatChanged(seat.getsSeatNo(), AppConstants.Status.SELECTED, "0");



            });
        }
    }
}
