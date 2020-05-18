package com.otapp.net.Events.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.Async.Interface.OtappApiServices;
import com.otapp.net.Async.Interface.RestClient;
import com.otapp.net.Events.Core.EventSeatProcessingResponse;
import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.Events.Core.GetTicketTypeResponse;
import com.otapp.net.Events.interfac.SeatProcessingResponseInterface;
import com.otapp.net.R;
import com.otapp.net.helper.Interface.ProgressDialogInterface;
import com.otapp.net.helper.JustifiedTextView;
import com.otapp.net.helper.SHA;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.MyPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketClassAdapter extends RecyclerView.Adapter<TicketClassAdapter.MyviewHolder> {
    public Context context;
    public List<EventsListResponse.Events.EventTickets.Tickets> ticketsClassList;
    EventsListResponse.Events.EventTickets.Tickets ticketsClass;
    EventsListResponse.Events events;
    String eventDate;
    ProgressDialog progressDialog;

    int classRecallFlag=1;

    SeatProcessingResponseInterface seatProcessingResponseInterface;
    ProgressDialogInterface progressDialogInterface;




    public Map<String, EventsListResponse.Events.EventTickets.Tickets> tempTicketMap;


    public  TicketClassAdapter(Context context, List<EventsListResponse.Events.EventTickets.Tickets> ticketsClassList, EventsListResponse.Events events, String eventDate,
                                 SeatProcessingResponseInterface seatProcessingResponseInterface, Map<String, EventsListResponse.Events.EventTickets.Tickets> tempTicketMap,
                                 ProgressDialogInterface progressDialogInterface) {
        this.context = context;
        this.ticketsClassList = ticketsClassList;
        this.events = events;
        this.eventDate = eventDate;
        this.seatProcessingResponseInterface = seatProcessingResponseInterface;
        this.tempTicketMap = tempTicketMap;
        this.progressDialogInterface= progressDialogInterface;
        MyPref.setPref(context, MyPref.PREF_EVENT_DATE, eventDate);

    }

   ArrayAdapter<String> adapter;
    String[] arraySpinner = new String[]{
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    };
    int AuthKey;
    String agentId = AppConstants.agentId, bookFrom = AppConstants.bookFrom;
    String eventId = "", ticketId = "", noOFTicket = "0", uKey = "", key = "", apiKey = "";

    public TicketClassAdapter(Context context, List<EventsListResponse.Events.EventTickets.Tickets> ticketsClassList) {
        this.context = context;
        this.ticketsClassList = ticketsClassList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reccyler_ticket_class_selection, viewGroup, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyviewHolder myviewHolder, final int i) {
        Log.d("Log","array size "+ticketsClassList.size());
        if (ticketsClassList.size() != 0) {


            if(tempTicketMap.size()!=0) {
                myviewHolder.spSelectdNoOfTicket.setEnabled(false);
                myviewHolder.spinerLayout.setBackgroundResource(R.drawable.bg_round_filled_gray);
            }else {
                ticketsClassList.get(i).setEnabled=true;
                myviewHolder.spinerLayout.setBackgroundResource(R.drawable.bg_round_border_gray);
            }


            progressDialog = new ProgressDialog(context);
            ticketsClass = ticketsClassList.get(i);
            myviewHolder.tvTicketClass.setText(ticketsClass.tkt_name);
            myviewHolder.tvPrice.setText(ticketsClass.tkt_currency + " " + ticketsClass.tkt_amount);

      //      Toast.makeText(context, ""+ticketsClassList.size(), Toast.LENGTH_SHORT).show();

            adapter = new ArrayAdapter<String>(context,
                    R.layout.layout_spinner_item, arraySpinner);
            adapter.setDropDownViewResource(R.layout.layout_spinner_item);
            myviewHolder.spSelectdNoOfTicket.setAdapter(adapter);
            if (ticketsClass.tkt_desc.equals("")){
                myviewHolder.icInfo.setVisibility(View.GONE);
            }else {
                myviewHolder.icInfo.setVisibility(View.VISIBLE);

            }

            myviewHolder.spSelectdNoOfTicket.setSelection(ticketsClass.count, false);

            myviewHolder.icInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ticketsClass=ticketsClassList.get(i);
                    String sTicketDesc = "";
                    byte[] data = Base64.decode(ticketsClass.tkt_desc, Base64.DEFAULT);
                    try {
                        sTicketDesc = new String(data, "UTF-8");
                        Log.d("Log", "Service Name : " + sTicketDesc);
                    } catch (UnsupportedEncodingException e) {
                        Toast.makeText(context, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(10, 10, 10, 0);
                    JustifiedTextView justifiedTextView = new JustifiedTextView(context,null);
                    justifiedTextView.setBackgroundColor(Color.WHITE);
                    justifiedTextView.setTextColor(Color.BLACK);
                    justifiedTextView.setLayoutParams(params);
                    justifiedTextView.setPadding(20,10,10,10);
                    justifiedTextView.setText("\t"+ticketsClass.tkt_name+"\n\n"+sTicketDesc);

                    new SimpleTooltip.Builder(context)
                            .anchorView(v)
                            //   .text("\t"+ticketsClass.tkt_name+"\n\n"+sTicketDesc)
                            .text("\t"+ticketsClass.tkt_name+"\n\n"+sTicketDesc)
                            //  .text(html)
                            .backgroundColor(Color.parseColor("#ffffff"))
                            .textColor(Color.parseColor("#000000"))
                            .gravity(Gravity.TOP)
                            .arrowColor(Color.parseColor("#FFFFFF"))
                            .transparentOverlay(false)
                            .padding(10f)
                            .margin(10f)
                            .contentView(justifiedTextView)
                            .arrowDrawable(R.drawable.arrow2)
                            .arrowHeight(25)
                            .overlayOffset(2f)
                            .build()
                            .show();
                  /*  Formatter fmt = new Formatter();

                    fmt = new Formatter();
                    fmt.format("%1$-10s", "Description here Other possibility is templating. If you actually have a template where you wish to replace a couple of");*/
                    /*String format = "%1$-40s\n";
                   String html= String.format(format,"Description here Other possibility is templating. If you actually have a template where you wish to replace a couple of");*/
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        new SimpleTooltip.Builder(context)
                                .anchorView(v)
                             //   .text("\t"+ticketsClass.tkt_name+"\n\n"+sTicketDesc)
                                .text(Html.fromHtml("<h6>Title</h6><br><p align=\"justify\">Description here Other possibility is templating. If you actually have a template where you wish to replace a couple of</p>", Html.FROM_HTML_MODE_COMPACT))
                              //  .text(html)
                                .backgroundColor(Color.parseColor("#ffffff"))
                                .textColor(Color.parseColor("#000000"))
                                .gravity(Gravity.TOP)
                                .arrowColor(Color.parseColor("#FFFFFF"))
                                .transparentOverlay(false)
                                .arrowDrawable(R.drawable.arrow2)
                                .arrowHeight(25)
                                .overlayOffset(2f)
                                .build()
                                .show();
                    }else {
                        new SimpleTooltip.Builder(context)
                                .anchorView(v)
                            //     .text("\t"+ticketsClass.tkt_name+"\n\n"+sTicketDesc)
                                .text(Html.fromHtml("<h6>Title</h6><br><p align=\"justify\">Description here Other possibility is templating. If you actually have a template where you wish to replace a couple of</p>"))
                              //  .text(html)
                                .backgroundColor(Color.parseColor("#ffffff"))
                                .textColor(Color.parseColor("#000000"))
                                .gravity(Gravity.TOP)
                                .arrowColor(Color.parseColor("#FFFFFF"))
                                .transparentOverlay(false)
                                .arrowDrawable(R.drawable.arrow2)
                                .arrowHeight(25)
                                .overlayOffset(2f)
                                .build()
                                .show();
                    }*/
//                    Toast.makeText(context, "Error : " ,Toast.LENGTH_SHORT).show();
                    //    showDescription(ticketsClass);
                }
            });



            myviewHolder.spSelectdNoOfTicket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                       if (classRecallFlag == 0) {
                           myviewHolder.seatProccessing(i);
                       } else {
                           classRecallFlag = 0;
                         /*  progressDialog.dismiss();*/
                       }



                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            myviewHolder.spSelectdNoOfTicket.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    classRecallFlag = 0;
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return ticketsClassList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvTicketClass, tvPrice;
        Spinner spSelectdNoOfTicket;
        LinearLayout spinerLayout;
        ImageView icInfo;


        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            tvTicketClass = itemView.findViewById(R.id.tvTicketClass);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            spSelectdNoOfTicket = itemView.findViewById(R.id.spSelectNoOfTicket);
            icInfo= itemView.findViewById(R.id.ic_Info);
            spinerLayout= itemView.findViewById(R.id.spinerLayout);
            /* loadingView=itemView.findViewById(R.id.loadingView);*/


        }

        public void seatProccessing(final int postion) {
            boolean isConnected = AppConstants.isConnected(context);
            if (isConnected) {

                progressDialogInterface.showProgressDialog();
                Random r = new Random();
                int random = r.nextInt(4 - 1 + 1) + 1;
                if (random > 4 && random < 1) {
                    random = 3;
                }
                AuthKey = Integer.parseInt(String.valueOf(random));
                eventId = events.event_id;
                ticketId = ticketsClassList.get(getAdapterPosition()).tkt_id;
                noOFTicket = spSelectdNoOfTicket.getSelectedItem().toString();
                uKey = MyPref.getPref(context, MyPref.PREF_UKEY, "");

                MyPref.setPref(context, MyPref.PREF_EVENT_ID, events.event_id);

                key = calculateHash(AuthKey, calculateHash(1, calculateHash(4, agentId + bookFrom + eventId + ticketId + eventDate + noOFTicket + uKey + "Pr)ce5$")));

                apiKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4, AuthKey + "EvenTPr0ce5$")));

                OtappApiServices otappApiService = RestClient.getClient(true);
                Call<EventSeatProcessingResponse> mCallSeatProceesing = otappApiService.seatProcessing(apiKey, agentId, bookFrom, String.valueOf(AuthKey), eventId, ticketId, eventDate, noOFTicket, uKey, key);

                mCallSeatProceesing.enqueue(new Callback<EventSeatProcessingResponse>() {
                    @Override
                    public void onResponse(Call<EventSeatProcessingResponse> call, Response<EventSeatProcessingResponse> response) {

                        progressDialogInterface.closeProgressDialog();
                        JSONObject jsonObjectResponse = null;
                        try {
                            jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                            Log.d("Log", "Response : " + jsonObjectResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (response.body().status == 200) {

                            if(response.body().selected_seats.equals("0")){
                                spSelectdNoOfTicket.setSelection(0);
                                String currency[] = response.body().tot_fare.split("\\.");

                                MyPref.setPref(context, MyPref.PREF_CURRENCY, currency[0]);
                                int no = Integer.parseInt(noOFTicket);
                                ticketsClassList.get(postion).count = Integer.parseInt(noOFTicket);
                                if (noOFTicket.equals("0")) {
                                    tempTicketMap.remove(ticketId);
                                } else {
                                    tempTicketMap.put(ticketId, ticketsClassList.get(postion));
                                }
                                Toast.makeText(context, "" + response.body().message, Toast.LENGTH_SHORT).show();
                                MyPref.setPref(context, MyPref.PREF_UKEY, response.body().ukey);
                                seatProcessingResponseInterface.seatToatalPrice(response.body().tot_fare, tempTicketMap);
                                classRecallFlag = 1;
                            }else {

                                String currency[] = response.body().tot_fare.split("\\.");

                                MyPref.setPref(context, MyPref.PREF_CURRENCY, currency[0]);
                                ticketsClassList.get(postion).count = Integer.parseInt(noOFTicket);
                                if (noOFTicket.equals("0")) {
                                    tempTicketMap.remove(ticketId);
                                } else {
                                    tempTicketMap.put(ticketId, ticketsClassList.get(postion));
                                }

                                Toast.makeText(context, "" + response.body().message, Toast.LENGTH_SHORT).show();
                                MyPref.setPref(context, MyPref.PREF_UKEY, response.body().ukey);
                                Log.d("Log", "UKEY = " + response.body().ukey);
                                spSelectdNoOfTicket.setSelection(Integer.parseInt(response.body().selected_seats));
                                seatProcessingResponseInterface.seatToatalPrice(response.body().tot_fare, tempTicketMap);
                                String isEnable = MyPref.getPref(context, MyPref.PREF_SPINER_ENABLE, "");
                                if (!isEnable.equals("1")) {
                                    for (int i = 0; i < ticketsClassList.size(); i++) {

                                        if (!ticketsClassList.get(postion).tkt_currency.equals(ticketsClassList.get(i).tkt_currency)) {
                                            ticketsClassList.get(i).setEnabled = false;

                                            notifyItemChanged(i);
                                        }
                                    }
                                    MyPref.setPref(context, MyPref.PREF_SPINER_ENABLE, "1");

                                }
                                classRecallFlag = 1;
                            }
                        }else {
                            if (response.body().status == 401) {
                                Toast.makeText(context, "" + response.body().message, Toast.LENGTH_SHORT).show();
                                MyPref.setPref(context, MyPref.PREF_UKEY, response.body().ukey);
                                spSelectdNoOfTicket.setSelection(0);
                                classRecallFlag=1;

                            } else {
                               Toast.makeText(context, "" + response.body().message, Toast.LENGTH_SHORT).show();
                                spSelectdNoOfTicket.setSelection(0);
                                classRecallFlag=1;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<EventSeatProcessingResponse> call, Throwable t) {
                        //Utils.closeProgressDialog();
                        progressDialogInterface.closeProgressDialog();
                        Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            } else {
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
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

    }
}
