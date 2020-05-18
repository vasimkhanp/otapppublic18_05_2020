package com.otapp.net.Events.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.Async.Interface.OtappApiServices;
import com.otapp.net.Async.Interface.RestClient;
import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.Events.Core.GetTicketTypeResponse;
import com.otapp.net.Events.interfac.EventTicketClassInterface;
import com.otapp.net.R;
import com.otapp.net.helper.SHA;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerFloorSelectionAdapter extends RecyclerView.Adapter<RecyclerFloorSelectionAdapter.MyViewHolder> {
    public Context context;
    public List<EventsListResponse.Events.EventTickets> eventTicketsList;
    public List<EventsListResponse.Events.EventTickets.Tickets> ticketsList;
    public EventTicketClassInterface ticketClassInterface;
    int AuthKey;
    String apiKey="",key="",strEventId="",strFloorId="",strDate="";
    GetTicketTypeResponse floorTicketList;
    ProgressDialog progressDialog;
    int floorFlag=0;
    public RecyclerFloorSelectionAdapter(Context context, List<EventsListResponse.Events.EventTickets> eventTicketsList, EventTicketClassInterface ticketClassInterface) {
        this.context = context;
        this.eventTicketsList = eventTicketsList;
        this.ticketClassInterface = ticketClassInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_floor_selection,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final EventsListResponse.Events.EventTickets eventTickets    = eventTicketsList.get(i);
        progressDialog = new ProgressDialog(context);
        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }
        if(eventTickets.isSeleected){
            myViewHolder.lnrLayout.setBackgroundResource(R.drawable.bg_round_rectangle_blue_border);
            if (i >= 0) {
                ticketClassInterface.selectedFloorId(eventTickets);
            }
            AuthKey = Integer.parseInt(String.valueOf(random));
            strEventId= MyPref.getPref(context, MyPref.PREF_EVENT_ID,"");
            strDate=  MyPref.getPref(context, MyPref.PREF_EVENT_DATE,"");
            getTicketClass();
        }else {
            myViewHolder.lnrLayout.setBackgroundResource(R.drawable.bg_round_rectangle_white);
        }
      /*  if(eventTickets.floor_name.equals(""))
        {
            myViewHolder.relativeLayoutFloor.setVisibility(View.GONE);
        }else {*/
            myViewHolder.tvFloorName.setText(eventTickets.floor_name);
            getTicketClass();
      //  }


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                int random = r.nextInt(4 - 1 + 1) + 1;
                if (random > 4 && random < 1) {
                    random = 3;
                }
                floorFlag=1;
                AuthKey = Integer.parseInt(String.valueOf(random));
                strEventId= MyPref.getPref(context, MyPref.PREF_EVENT_ID,"");
                strFloorId= eventTicketsList.get(i).floor_id;
                strDate=  MyPref.getPref(context, MyPref.PREF_EVENT_DATE,"");
               /* apiKey=calculateHash(AuthKey,calculateHash(1,calculateHash(4 ,AuthKey+"EvenTt!cketYpE")));
                key=calculateHash(AuthKey,calculateHash(1,calculateHash(4 , AppConstants.agentId+strEventId+strFloorId+strDate+
                        AppConstants.bookFrom+"T!ckEtYpE")));*/
                if (eventTicketsList.get(i).getTickets.equals("1")) {

                    for (int j = 0; j < eventTicketsList.size(); j++) {
                        if (i == j) {
                            eventTicketsList.get(i).isSeleected = true;
                            myViewHolder.lnrLayout.setBackgroundResource(R.drawable.bg_round_rectangle_blue_border);
                            getTicketClass();
                        } else {
                            eventTicketsList.get(j).isSeleected = false;
                        }
                    }

                    notifyDataSetChanged();
                 /*   if (i >= 0) {
                        getTicketClass();
                    }*/

                    getTicketClass();


                } else {
                    for (int j = 0; j < eventTicketsList.size(); j++) {
                        if (i == j) {
                            eventTicketsList.get(i).isSeleected = true;
                            myViewHolder.lnrLayout.setBackgroundResource(R.drawable.bg_round_rectangle_blue_border);
                        } else {
                            eventTicketsList.get(j).isSeleected = false;
                        }
                    }

                    notifyDataSetChanged();
                    if (i >= 0) {
                        ticketClassInterface.selectedFloorId(eventTickets);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventTicketsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvFloorName;
        LinearLayout lnrLayout;
        RelativeLayout relativeLayoutFloor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFloorName= itemView.findViewById(R.id.tvFloorName);
            lnrLayout=itemView.findViewById(R.id.lnrLayout);
            relativeLayoutFloor=itemView.findViewById(R.id.relativeFloor);

        }
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
    public void getTicketClass(){

        if(!Utils.isProgressDialogShowing()){
            Utils.showProgressDialog(context);
        }

        if(floorFlag==0){
            strFloorId=eventTicketsList.get(0).floor_id;
        }
        apiKey=calculateHash(AuthKey,calculateHash(1,calculateHash(4 ,AuthKey+"EvenTt!cketYpE")));
        key=calculateHash(AuthKey,calculateHash(1,calculateHash(4 , AppConstants.agentId+strEventId+strFloorId+strDate+
                AppConstants.bookFrom+"T!ckEtYpE")));


        OtappApiServices otappApiServices = RestClient.getClient(true);
        Call<GetTicketTypeResponse> ticketTypeResponseCall = otappApiServices.getTicket(apiKey,AppConstants.agentId,AppConstants.bookFrom, String.valueOf(AuthKey),strEventId,strFloorId,
                strDate,key);
        ticketTypeResponseCall.enqueue(new Callback<GetTicketTypeResponse>() {
            @Override
            public void onResponse(Call<GetTicketTypeResponse> call, Response<GetTicketTypeResponse> response) {
                Utils.closeProgressDialog();
                JSONObject jsonObjectResponse = null;
                try {
                    jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                    Log.d("Log", "Response : " + jsonObjectResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(response.body()!=null){
                    if(response.body().status==200){
                        floorTicketList=response.body();
                        ticketClassInterface.selectedTicketList(floorTicketList);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetTicketTypeResponse> call, Throwable t) {
                Utils.closeProgressDialog();

            }
        });
    }

}
