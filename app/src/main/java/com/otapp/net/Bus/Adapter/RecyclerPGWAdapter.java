package com.otapp.net.Bus.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.Bus.Activity.BookingReserveActivity;
import com.otapp.net.Bus.Activity.BusBookingActivity;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.AvailableBuses;
import com.otapp.net.Bus.Core.GetFareResponse;
import com.otapp.net.Bus.Core.ReserveTicketResponse;
import com.otapp.net.Bus.Core.SearchBusDetails;
import com.otapp.net.Bus.Network.OtappApiInstance;
import com.otapp.net.Bus.Network.OtappApiService;
import com.otapp.net.R;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.otapp.net.Bus.Core.SHA.calculateHash;

public class RecyclerPGWAdapter extends RecyclerView.Adapter<RecyclerPGWAdapter.MyViewHolder>{
    Context context;
    ArrayList<GetFareResponse.Pgws> pgwsArrayList;
    SearchBusDetails searchBusDetails;
    AvailableBuses availableBuses;
    AvailableBuses availableBusesReturn;
    String strPassengerDetails;
    String strReturnPassengerDetails;


    GetFareResponse.Pgws pgws;
    boolean isNidcEnabled=false;
    int AuthKey;
    private ProgressDialog progressDialog;
    JSONArray array,returnArray;


    public RecyclerPGWAdapter(Activity context, ArrayList<GetFareResponse.Pgws> pgwsArrayList, SearchBusDetails searchBusDetails, AvailableBuses availableBuses, AvailableBuses availableBusesReturn, String strPassengerDetails, String strReturnPassengerDetails) {
        this.context = context;
        this.pgwsArrayList = pgwsArrayList;
        this.searchBusDetails = searchBusDetails;
        this.availableBuses = availableBuses;
        this.availableBusesReturn = availableBusesReturn;
        this.strPassengerDetails = strPassengerDetails;
        this.strReturnPassengerDetails = strReturnPassengerDetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_pgw_layout,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        pgws=pgwsArrayList.get(i);
        myViewHolder.tvPgwName.setText(pgws.pgw_name);

    }

    @Override
    public int getItemCount() {
        return pgwsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvPgwName;
        LinearLayout layoutPgw;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           tvPgwName=itemView.findViewById(R.id.textViewPgwName);
           layoutPgw=itemView.findViewById(R.id.layoutPgw);

           layoutPgw.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Toast.makeText(context, "pgw adapter called", Toast.LENGTH_SHORT).show();
                    if(!pgws.subPgwsArrayList.isEmpty()){
                       // final Dialog dialog = new Dialog(RecyclerPGWAdapter.this.context);

                        final Dialog dialog = new Dialog(context);
                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_sub_pgws);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                      //  dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        RecyclerView recyclerSubPGW= dialog.findViewById(R.id.recyclerSubPgws);
                        recyclerSubPGW.setLayoutManager(new LinearLayoutManager(context));
                        recyclerSubPGW.setHasFixedSize(true);
                        RecycelerSubPGWAdapter recycelerSubPGWAdapter= new RecycelerSubPGWAdapter(context,pgws.subPgwsArrayList,searchBusDetails,availableBuses,availableBusesReturn,strPassengerDetails,strReturnPassengerDetails);
                        recyclerSubPGW.setAdapter(recycelerSubPGWAdapter);
                        recycelerSubPGWAdapter.notifyDataSetChanged();

                        dialog.show();


                    }else {
                        String payCode=pgwsArrayList.get(getAdapterPosition()).pay_code;
                        if (!pgwsArrayList.get(getAdapterPosition()).pgw_enabled.equals("1")) {
                            Utils.showToast(context, context.getString(R.string.msg_payment_disable));
                            return;
                        }
                        boolean isConnected= AppConstants.isConnected(context);
                        if (isConnected) {
                            showProgress();
                            Random r = new Random();
                            int random = r.nextInt(4 - 1 + 1) + 1;
                            if (random > 4 && random < 1) {
                                random = 3;
                            }
                            AuthKey = Integer.parseInt(String.valueOf(random));

                            String compId=com.otapp.net.utils.AppConstants.sCompId;
                            String agentId=com.otapp.net.utils.AppConstants.sAgentId;
                            String isFrom=com.otapp.net.utils.AppConstants.sIsFrom;
                            String currency="1";
                            String mCustId = "";
                            if (MyPref.getPref(context, MyPref.PREF_IS_LOGGED, false)) {
                                mCustId = MyPref.getPref(context, MyPref.PREF_USER_ID, "");
                            }
                            String sFromId=searchBusDetails.getsFromId();
                            String sToID=searchBusDetails.getsToId();
                            String sTravelDate= searchBusDetails.getsDate();
                            String subId= availableBuses.getSub_id();
                            String sTdiId= availableBuses.getTdi_id();
                            String sLbID= availableBuses.getLb_id();
                            String sPbID= availableBuses.getPbi_id();
                            String sAsiID= availableBuses.getAsi_id();
                            String sUkey= MyPref.getPref(context,MyPref.PREF_BUS_UKEY,"");
                            String sBordingPoint= searchBusDetails.getsBoarding();
                            if(sBordingPoint.equals("")){
                                sBordingPoint= searchBusDetails.getsFrom();
                            }
                            String sDropingPoint= searchBusDetails.getsDropping();
                            if(sDropingPoint.equals("")){
                                sDropingPoint= searchBusDetails.getsTo();
                            }
                            JSONObject obj = new JSONObject();
                            JSONObject returnObj= new JSONObject();
                            try {
                                array = new JSONArray(strPassengerDetails);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                returnArray = new JSONArray(strReturnPassengerDetails);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                obj.put("from_id", sFromId);
                                obj.put("to_id", sToID);
                                obj.put("trvl_dt", sTravelDate);
                                obj.put("sub_id", subId);
                                obj.put("tdi_id", sTdiId);
                                obj.put("lb_id", sLbID);
                                obj.put("pbi_id", sPbID);
                                obj.put("asi_id", sAsiID);
                                obj.put("ukey", sUkey);
                                obj.put("boarding", sBordingPoint);
                                obj.put("dropping", sDropingPoint);
                                obj.put("passengers", array);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            if(AppConstants.Keys.isReturnBus){
                                try {
                                    returnObj.put("from_id", searchBusDetails.getsReturnFromId());
                                    returnObj.put("to_id", searchBusDetails.getsReturnToId());
                                    returnObj.put("trvl_dt", searchBusDetails.getsReturnDate());
                                    returnObj.put("sub_id", availableBusesReturn.getSub_id());
                                    returnObj.put("tdi_id", availableBusesReturn.getTdi_id());
                                    returnObj.put("lb_id", availableBusesReturn.getLb_id());
                                    returnObj.put("pbi_id", availableBusesReturn.getPbi_id());
                                    returnObj.put("asi_id", availableBusesReturn.getAsi_id());
                                    returnObj.put("ukey", MyPref.getPref(context,MyPref.RETURN_UKEY,""));
                                    returnObj.put("boarding", searchBusDetails.getsReturnBoarding());
                                    returnObj.put("dropping", searchBusDetails.getsReturnDropping());
                                    returnObj.put("passengers", returnArray);

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                String apiKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,AuthKey+"Re5Erve5e@t")));
                                String postKey=calculateHash(AuthKey, calculateHash(1, calculateHash(4,obj.toString()+returnObj.toString()
                                        +searchBusDetails.getsContactPhone()+searchBusDetails.getsContactEmailAdd()+currency+mCustId+payCode+compId+agentId+isFrom+"Re5erVeb0)k")));
                                Log.d("Log","apikey "+apiKey);
                                Log.d("Log","postKEy "+postKey);

                                OtappApiService otappbusApiService = OtappApiInstance.getRetrofitInstance().create(OtappApiService.class);
                                Call<ReserveTicketResponse> getReserveBooking= otappbusApiService.getReserveBooking(apiKey,String.valueOf(AuthKey),postKey,compId,agentId,
                                        isFrom,currency,searchBusDetails.getsContactPhone().replaceAll("\\+",""),searchBusDetails.getsContactEmailAdd(),obj.toString(),returnObj.toString(),mCustId,payCode);
                                getReserveBooking.enqueue(new Callback<ReserveTicketResponse>() {
                                    @Override
                                    public void onResponse(Call<ReserveTicketResponse> call, Response<ReserveTicketResponse> response) {
                                        hideProgress();
                                        JSONObject jsonObjectResponse = null;
                                        try {
                                            jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                                            Log.d("Log", "Response : " + jsonObjectResponse);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(context, ""+response.body().message, Toast.LENGTH_SHORT).show();
                                        if(response.body().status==AppConstants.Status.SUCCESS){
                                            MyPref.setPref(context, MyPref.PREF_BUS_UKEY,"");
                                            MyPref.setPref(context, MyPref.RETURN_UKEY,"");
                                            MyPref.setPref(context, MyPref.ASID,"");

                                            Intent intent =new Intent(context, BookingReserveActivity.class);
                                            intent.putExtra(com.otapp.net.utils.AppConstants.BNDL_BUS_RESPONSE,jsonObjectResponse.toString());
                                            context.startActivity(intent);
                                        }else {
                                            Toast.makeText(context, ""+response.body().message, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ReserveTicketResponse> call, Throwable t) {
                                        hideProgress();
                                        Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        }
                    }
               }
           });
        }
        private void showProgress() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        private void hideProgress() {
            if (progressDialog != null) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }
    }
}
