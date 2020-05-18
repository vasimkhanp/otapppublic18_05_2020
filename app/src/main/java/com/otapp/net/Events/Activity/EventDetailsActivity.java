package com.otapp.net.Events.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.otapp.net.Async.Interface.OtappApiServices;
import com.otapp.net.Async.Interface.RestClient;
import com.otapp.net.Events.Adapter.GetTicketClassTypeAdatpter;
import com.otapp.net.Events.Adapter.RecyclerEventDateTimeAdapter;
import com.otapp.net.Events.Adapter.RecyclerFloorSelectionAdapter;
import com.otapp.net.Events.Adapter.TicketClassAdapter;
import com.otapp.net.Events.Adapter.ViewPagerSliderImaeAdapter;
import com.otapp.net.Events.Core.DeleteSeatsResponse;
import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.Events.Core.GetTicketTypeResponse;
import com.otapp.net.Events.interfac.EventDateSelectionInterface;
import com.otapp.net.Events.interfac.EventTicketClassInterface;
import com.otapp.net.Events.interfac.EventTicketDescInterface;
import com.otapp.net.Events.interfac.SeatProcessResponseIntfceTT;
import com.otapp.net.Events.interfac.SeatProcessingResponseInterface;
import com.otapp.net.R;
import com.otapp.net.helper.Interface.ProgressDialogInterface;
import com.otapp.net.helper.SHA;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity implements EventTicketClassInterface, EventDateSelectionInterface,
        SeatProcessingResponseInterface, ProgressDialogInterface, SeatProcessResponseIntfceTT {
    @BindView(R.id.toolbarTitle)
    TextView pageTitle;
    @BindView(R.id.back)
    ImageView imgBack;
    /* @BindView(R.id.ivEvent)
     ImageView imageViewEvent;*/
    @BindView(R.id.tvName)
    TextView textViewEventName;
    @BindView(R.id.tvEventAddress)
    TextView tvEventAddress;
    @BindView(R.id.tvEventDate)
    TextView tvEventDate;
    @BindView(R.id.tvEventPrice)
    TextView tvEventPrice;
    @BindView(R.id.recyclerSelectDate)
    RecyclerView recyclerViewSelectDate;
    @BindView(R.id.recyclerFloor)
    RecyclerView recyclerViewFloorSelection;
    @BindView(R.id.linearFloor)
    LinearLayout linearLayoutFloor;

    @BindView(R.id.recyclerTicketClass)
    RecyclerView recyclerViewTicketClass;
    @BindView(R.id.tvToatlPrice)
    TextView tvTotalPrice;

    @BindView(R.id.linearPackage)
    LinearLayout linearLayoutPackage;


    @BindView(R.id.tvContinueButton)
    TextView tvContinueButton;
   /* @BindView(R.id.recyclerSliderImage)
    RecyclerView recyclerViewSliderImage;*/

    EventsListResponse.Events events;
    List<EventsListResponse.Events> eventsList;
    List<EventsListResponse.Events.EventDates> eventDatesList;
    List<EventsListResponse.Events.EventTickets> eventTicketsList;
    List<EventsListResponse.Events.EventTickets.Tickets> floorTicketClassList;
    List<GetTicketTypeResponse.GetTickets> floorTicketList;
    GetTicketTypeResponse floorTicketLisResponse;

    String evnetName = "", event = "";
    RecyclerView.LayoutManager horizontalRecyclerViewLayout;
    RecyclerView.LayoutManager horizontalRecyclerFloorName;
    RecyclerView.LayoutManager horizontalRecylcerSilderImage;
    RecyclerEventDateTimeAdapter eventDateTimeAdapter;
    RecyclerFloorSelectionAdapter floorSelectionAdapter;
    TicketClassAdapter ticketClassAdapter;
    GetTicketClassTypeAdatpter getTicketClassTypeAdatpter;
    ProgressDialog progressDialog;
    String strFloorId, strEventDate = "", strTotalPrice = "";
    double seatAmount = 0;
    int AuthKey;
    String apiKey = "", key = "";
    public Map<String, EventsListResponse.Events.EventTickets.Tickets> tempTicketMap;
    public Map<String, GetTicketTypeResponse.GetTickets> tempTicketTypeMap;
    int ticketFlag = 0;
    Timer timer;
    int page = 0;
    Date date;
    String strDate, monthsplit;
    List<EventsListResponse.Events.EventTickets.Tickets> tempTicketArrayList;
    List<GetTicketTypeResponse.GetTickets> tempGetTicketList;
    List<String> silderImagesList;
    private Handler handler;
    String tempDate = "";
    private int delay = 5000;

    ViewPager viewPager;
    ViewPagerSliderImaeAdapter viewPagerSliderImaeAdapter;
    public EventTicketClassInterface ticketClassInterface;

    Runnable runnable = new Runnable() {
        public void run() {
            if (viewPagerSliderImaeAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);
        pageTitle.setText(R.string.event);
        eventsList = new ArrayList<>();
        eventDatesList = new ArrayList<>();
        eventTicketsList = new ArrayList<>();
        floorTicketClassList = new ArrayList<>();
        silderImagesList = new ArrayList<>();
        floorTicketList = new ArrayList<>();
        handler = new Handler();
        event = getIntent().getExtras().getString("data");
        evnetName = getIntent().getExtras().getString("evnetName");
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tempTicketMap = new HashMap<>();
        tempTicketTypeMap = new HashMap<>();
        tempTicketArrayList = new ArrayList<>();
        tempGetTicketList = new ArrayList<>();
        EventsListResponse eventsListResponse = new Gson().fromJson(event, EventsListResponse.class);

        MyPref.setPref(getApplicationContext(), MyPref.PREF_UKEY, "");
        MyPref.setPref(getApplicationContext(), MyPref.PREF_SPINER_ENABLE, "");
        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }
        AuthKey = Integer.parseInt(String.valueOf(random));

        recyclerViewTicketClass.setHasFixedSize(true);
        recyclerViewTicketClass.setNestedScrollingEnabled(false);
        recyclerViewTicketClass.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        for (int i = 0; i < eventsListResponse.eventsList.size(); i++) {
            if (evnetName.equals(eventsListResponse.eventsList.get(i).event_name)) {
                events = eventsListResponse.eventsList.get(i);
            }
        }

        if (events != null) {
            textViewEventName.setText(events.event_name);
            MyPref.setPref(getApplicationContext(), MyPref.PREF_EVENT_ID, events.event_id);
            try {
                byte[] data = Base64.decode(events.event_address, Base64.DEFAULT);
                tvEventAddress.setText(new String(data, "UTF-8") + "," + events.event_city);
            } catch (Exception e) {
                e.printStackTrace();
            }
            eventDatesList = events.eventDatesList;
            if (eventDatesList.size() != 0) {
                if (eventDatesList.size() == 1) {
                    try {
                        String firstDate = eventDatesList.get(0).event_date;
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        date = format.parse(firstDate);
                        strDate = (String) android.text.format.DateFormat.format("dd", date); // 20
                        monthsplit = (String) android.text.format.DateFormat.format("MMM", date); // Jun
                        firstDate = strDate + " " + monthsplit;
                        tvEventDate.setText(firstDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    Log.d("Log", "datelist sixe =" + eventsList.size());
                    String firstDate = eventDatesList.get(0).event_date;
                    String lastDate = eventDatesList.get(eventDatesList.size() - 1).event_date;
                    Log.d("Log", "Event date = " + firstDate + " == " + lastDate);

                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        date = format.parse(firstDate);
                        strDate = (String) android.text.format.DateFormat.format("dd", date); // 20
                        monthsplit = (String) android.text.format.DateFormat.format("MMM", date); // Jun
                        firstDate = strDate + " " + monthsplit;
                        date = format.parse(lastDate);
                        strDate = (String) android.text.format.DateFormat.format("dd", date); // 20
                        monthsplit = (String) android.text.format.DateFormat.format("MMM", date); // Jun
                        lastDate = strDate + " " + monthsplit;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    tvEventDate.setText(firstDate + "-" + lastDate);
                }

            }


            tvEventPrice.setText(events.event_start_fare);


            recyclerViewSelectDate.setHasFixedSize(true);
            horizontalRecyclerViewLayout = new LinearLayoutManager(getApplicationContext());
            ((LinearLayoutManager) horizontalRecyclerViewLayout).setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewSelectDate.setLayoutManager(horizontalRecyclerViewLayout);
            if (eventDatesList.size() != 0) {
                eventDatesList.get(0).isSelected = true;
                strEventDate = eventDatesList.get(0).event_date;
            }
            eventDateTimeAdapter = new RecyclerEventDateTimeAdapter(getApplicationContext(), eventDatesList, this);
            recyclerViewSelectDate.setAdapter(eventDateTimeAdapter);
            eventDateTimeAdapter.notifyDataSetChanged();


            eventTicketsList = events.eventTicketsList;

            recyclerViewFloorSelection.setHasFixedSize(true);
            horizontalRecyclerFloorName = new LinearLayoutManager(getApplicationContext());
            ((LinearLayoutManager) horizontalRecyclerFloorName).setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewFloorSelection.setLayoutManager(horizontalRecyclerFloorName);
            if (eventTicketsList.size() != 0) {
                eventTicketsList.get(0).isSeleected = true;
            }


            if (eventDatesList.size() == 1) {
                strEventDate = eventDatesList.get(0).event_date;
            }
            if (eventTicketsList.get(0).getTickets != null) {
                if (eventTicketsList.get(0).getTickets.equals("1")) {
                    /*   for (int i = 0; i < eventTicketsList.size(); i++) {*/

                    if (!eventTicketsList.get(0).floor_id.equals("")) {
                        recyclerViewFloorSelection.setVisibility(View.VISIBLE);
                        floorSelectionAdapter = new RecyclerFloorSelectionAdapter(this, eventTicketsList, this);
                        recyclerViewFloorSelection.setAdapter(floorSelectionAdapter);
                        floorSelectionAdapter.notifyDataSetChanged();
                        strFloorId = eventTicketsList.get(0).floor_id;
                        getTicketClass();
                    } else {
                        linearLayoutFloor.setVisibility(View.GONE);
                        if (eventTicketsList.get(0).ticketsList.size() == 0) {
                            strFloorId = "";
                            getTicketClass();
                           /*     getTicketClassTypeAdatpter = new GetTicketClassTypeAdatpter(this, floorTicketList, events, strEventDate, this, tempTicketTypeMap, this);
                                recyclerViewTicketClass.setAdapter(getTicketClassTypeAdatpter);
                                getTicketClassTypeAdatpter.notifyDataSetChanged();*/
                        } else {
                            floorTicketClassList = eventTicketsList.get(0).ticketsList;
                            Log.d("Log", "" + floorTicketClassList.size());

                            recyclerViewTicketClass.setHasFixedSize(true);
                            recyclerViewTicketClass.setNestedScrollingEnabled(false);
                            recyclerViewTicketClass.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                            getTicketClassTypeAdatpter = new GetTicketClassTypeAdatpter(EventDetailsActivity.this, floorTicketList, events, strEventDate, this, tempTicketTypeMap, this);
                            recyclerViewTicketClass.setAdapter(getTicketClassTypeAdatpter);
                            getTicketClassTypeAdatpter.notifyDataSetChanged();
                        }
                    }
                    /*    }*/
                } else {

                    /*   for (int i = 0; i < eventTicketsList.size(); i++) {*/

                    if (!eventTicketsList.get(0).floor_id.equals("")) {
                        recyclerViewFloorSelection.setVisibility(View.VISIBLE);
                        floorSelectionAdapter = new RecyclerFloorSelectionAdapter(getApplicationContext(), eventTicketsList, this);
                        recyclerViewFloorSelection.setAdapter(floorSelectionAdapter);
                        floorSelectionAdapter.notifyDataSetChanged();
                    } else {
                        linearLayoutFloor.setVisibility(View.GONE);
                        if (eventTicketsList.get(0).ticketsList.size() == 0) {

                            recyclerViewTicketClass.setHasFixedSize(true);
                            recyclerViewTicketClass.setNestedScrollingEnabled(false);
                            recyclerViewTicketClass.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            strFloorId = "";
                            getTicketClass();

                        } else {
                            floorTicketClassList = eventTicketsList.get(0).ticketsList;
                            Log.d("Log", "" + floorTicketClassList.size());

                            recyclerViewTicketClass.setHasFixedSize(true);
                            recyclerViewTicketClass.setNestedScrollingEnabled(false);
                            recyclerViewTicketClass.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                            ticketClassAdapter = new TicketClassAdapter(this, floorTicketClassList, events, strEventDate, this, tempTicketMap, this);
                            recyclerViewTicketClass.setAdapter(ticketClassAdapter);
                            ticketClassAdapter.notifyDataSetChanged();
                        }
                    }
                    /*  }*/
                }
            }


            silderImagesList.clear();
            silderImagesList.add(events.event_slider);
            addImagesToViewPager();
           /* silderImagesList.add(events.event_slider);
            if(events.event_slider_1!=null && !events.event_slider_1.equals("")){
                silderImagesList.add(events.event_slider_1);
            }
            if(events.event_slider_2!=null && !events.event_slider_1.equals("")){
                silderImagesList.add(events.event_slider_2);
            }

            viewPagerSliderImaeAdapter = new ViewPagerSliderImaeAdapter(this,silderImagesList);
            viewPager.setAdapter(viewPagerSliderImaeAdapter);
            viewPager.setCurrentItem(0);*/


        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                page = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }


    @OnClick(R.id.tvContinueButton)
    public void onContinueClick() {
        tempGetTicketList.clear();
        tempTicketArrayList.clear();
        //seatAmount = 0.0;
        if (eventTicketsList.get(0).getTickets != null) {
            if (eventTicketsList.get(0).getTickets.equals("1")) {
                for (Map.Entry<String, GetTicketTypeResponse.GetTickets> entry : tempTicketTypeMap.entrySet()) {
                    // seatAmount = seatAmount + Double.parseDouble(entry.getValue().tkt_amount) * Double.parseDouble(String.valueOf(entry.getValue().count));
                    if (!entry.getValue().tkt_id.equals("")) {
                        tempGetTicketList.add(entry.getValue());
                    }
                }
                if (tempGetTicketList.size() < 1) {
                    Toast.makeText(this, "Please Select Seats", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), EventOrderPreview.class);

                    intent.putExtra("events", new Gson().toJson(events));
                    intent.putExtra("flag", "1");
                    intent.putParcelableArrayListExtra("temp", (ArrayList<? extends Parcelable>) tempGetTicketList);
                    startActivity(intent);
                }
            } else {
                for (Map.Entry<String, EventsListResponse.Events.EventTickets.Tickets> entry : tempTicketMap.entrySet()) {
                    //  seatAmount = seatAmount + Double.parseDouble(entry.getValue().tkt_amount.replaceAll(",","")) * Double.parseDouble(String.valueOf(entry.getValue().count));
                    if (!entry.getValue().tkt_id.equals("")) {
                        tempTicketArrayList.add(entry.getValue());
                    }
                }
                if (tempTicketArrayList.size() < 1) {
                    Toast.makeText(this, "Please Select Seats", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), EventOrderPreview.class);
                    intent.putExtra("flag", "0");
                    intent.putExtra("events", new Gson().toJson(events));
                    intent.putParcelableArrayListExtra("temp", (ArrayList<? extends Parcelable>) tempTicketArrayList);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void selectedFloorId(EventsListResponse.Events.EventTickets floorId) {
        floorTicketClassList = floorId.ticketsList;
        /*Toast.makeText(this, ""+strEventDate, Toast.LENGTH_SHORT).show();*/
        if (floorTicketClassList.size() > 0) {
            linearLayoutPackage.setVisibility(View.VISIBLE);
            recyclerViewTicketClass.setNestedScrollingEnabled(false);
            recyclerViewTicketClass.setHasFixedSize(true);
            recyclerViewTicketClass.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            ticketClassAdapter = new TicketClassAdapter(this, floorTicketClassList, events, strEventDate, this, tempTicketMap, this);
            recyclerViewTicketClass.setAdapter(ticketClassAdapter);
            ticketClassAdapter.notifyDataSetChanged();
        } else {
            linearLayoutPackage.setVisibility(View.GONE);
            //   Toast.makeText(this, "No Seats Available..", Toast.LENGTH_SHORT).show();
        }


        //   recyclerViewTicketClass.smoothScrollToPosition(floorTicketClassList.size());

    }

    @Override
    public void selectedTicketList(GetTicketTypeResponse getTickets) {
        floorTicketList = getTickets.getTicketsList;
        if (floorTicketList.size() > 0) {
            linearLayoutPackage.setVisibility(View.VISIBLE);
            recyclerViewTicketClass.setNestedScrollingEnabled(false);
            recyclerViewTicketClass.setHasFixedSize(true);
            recyclerViewTicketClass.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            getTicketClassTypeAdatpter = new GetTicketClassTypeAdatpter(EventDetailsActivity.this, floorTicketList, events, strEventDate, this, tempTicketTypeMap, this);
            recyclerViewTicketClass.setAdapter(getTicketClassTypeAdatpter);
            getTicketClassTypeAdatpter.notifyDataSetChanged();
        } else {
            linearLayoutPackage.setVisibility(View.GONE);
        }
    }


    @Override
    public void getEventDate(String eventDate, String eventSlider) {
        MyPref.setPref(getApplicationContext(), MyPref.PREF_SPINER_ENABLE, "");
        if (tempTicketTypeMap.size() > 0 || tempTicketMap.size() > 0) {
            deleteSeats();
        }
        strEventDate = eventDate;
        if (eventTicketsList.get(0).getTickets != null) {
            if (eventTicketsList.get(0).getTickets.equals("1")) {
                getTicketClass();
                if (eventSlider != null && !eventSlider.equals("")) {
                    silderImagesList.clear();
                    silderImagesList.add(eventSlider);
                    addImagesToViewPager();

                } else {
                    silderImagesList.clear();
                    silderImagesList.add(events.event_slider);
                    addImagesToViewPager();

                }
            }
        } else {

            ticketClassAdapter = new TicketClassAdapter(this, floorTicketClassList, events, strEventDate, this, tempTicketMap, this);
            recyclerViewTicketClass.setAdapter(ticketClassAdapter);
            ticketClassAdapter.notifyDataSetChanged();

            if (eventSlider != null && !eventSlider.equals("")) {
                silderImagesList.clear();
                silderImagesList.add(eventSlider);
                addImagesToViewPager();

            } else {
                silderImagesList.clear();
                silderImagesList.add(events.event_slider);
                addImagesToViewPager();

            }
        }
    }

    private void deleteSeats() {
        if (!Utils.isProgressDialogShowing()) {
            Utils.showProgressDialog(this);
        }

        strFloorId = "";
        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }
        String strUkey = MyPref.getPref(getApplicationContext(), MyPref.PREF_UKEY, "");
        AuthKey = Integer.parseInt(String.valueOf(random));
        apiKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4, AuthKey + "De1Pr0ce5")));
        key = calculateHash(AuthKey, calculateHash(1, calculateHash(4, AppConstants.agentId + AppConstants.bookFrom + strUkey + "De1Pr)ce5$")));


        OtappApiServices otappApiServices = RestClient.getClient(true);
        Call<DeleteSeatsResponse> ticketTypeResponseCall = otappApiServices.deleteEventSeats(apiKey, AppConstants.agentId, AppConstants.bookFrom, String.valueOf(AuthKey), strUkey, key);
        ticketTypeResponseCall.enqueue(new Callback<DeleteSeatsResponse>() {
            @Override
            public void onResponse(Call<DeleteSeatsResponse> call, Response<DeleteSeatsResponse> response) {
                Utils.closeProgressDialog();
                JSONObject jsonObjectResponse = null;
                try {
                    jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                    Log.d("Log", "Response : " + jsonObjectResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Toast.makeText(EventDetailsActivity.this, "" + response.body().getsMessage(), Toast.LENGTH_SHORT).show();
                        tvTotalPrice.setText(response.body().getsTotalFare());
                        tempTicketTypeMap.clear();
                        tempTicketMap.clear();
                        if (eventTicketsList.get(0).getTickets.equals("1")) {
                            //  getTicketClass();
                        } else {
                            for (int i = 0; i < floorTicketClassList.size(); i++) {
                                if (floorTicketClassList.get(i).count > 0) {
                                    floorTicketClassList.get(i).count = 0;
                                }
                            }
                            ticketClassAdapter = new TicketClassAdapter(EventDetailsActivity.this, floorTicketClassList, events, strEventDate, EventDetailsActivity.this, tempTicketMap, EventDetailsActivity.this);
                            recyclerViewTicketClass.setAdapter(ticketClassAdapter);
                            ticketClassAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteSeatsResponse> call, Throwable t) {
                Utils.closeProgressDialog();

            }
        });
    }

    @Override
    public void seatToatalPrice(String totalPrice, Map<String, EventsListResponse.Events.EventTickets.Tickets> tempTicketMap) {
        strTotalPrice = totalPrice;
        tvTotalPrice.setText(strTotalPrice);
        this.tempTicketMap = tempTicketMap;
        if (tempTicketMap.size() == 0) {
            MyPref.setPref(this, MyPref.PREF_SPINER_ENABLE, "");
            if (eventTicketsList.get(0).getTickets.equals("0")) {
                for (int i = 0; i < floorTicketList.size(); i++) {
                    if (floorTicketClassList.get(i).setEnabled == false) {
                        floorTicketClassList.get(i).setEnabled = true;
                    }
                }
                ticketClassAdapter = new TicketClassAdapter(EventDetailsActivity.this, floorTicketClassList, events, strEventDate, EventDetailsActivity.this, tempTicketMap, EventDetailsActivity.this);
                recyclerViewTicketClass.setAdapter(ticketClassAdapter);
                ticketClassAdapter.notifyDataSetChanged();
            }
        }


    }

    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {

        progressDialog.dismiss();
    }

    @OnClick(R.id.back)
    public void onBack() {
        finish();
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

    @Override
    public void seatGetTicketToatalPrice(String totalPrice, Map<String, GetTicketTypeResponse.GetTickets> tempTicketMap) {
        strTotalPrice = totalPrice;
        tvTotalPrice.setText(strTotalPrice);
        this.tempTicketTypeMap = tempTicketMap;
        if (tempTicketTypeMap.size() == 0) {
            MyPref.setPref(this, MyPref.PREF_SPINER_ENABLE, "");
            if (eventTicketsList.get(0).getTickets.equals("1")) {
                for (int i = 0; i < floorTicketList.size(); i++) {
                    if (floorTicketList.get(i).setEnabled == false) {
                        floorTicketList.get(i).setEnabled = true;
                    }
                }
                getTicketClassTypeAdatpter = new GetTicketClassTypeAdatpter(getApplicationContext(), floorTicketList, events, strEventDate, EventDetailsActivity.this, tempTicketTypeMap, EventDetailsActivity.this);
                recyclerViewTicketClass.setAdapter(getTicketClassTypeAdatpter);
                getTicketClassTypeAdatpter.notifyDataSetChanged();
            } else {
                getTicketClass();
            }
        }

    }

    public void getTicketClass() {

      /*  if (!Utils.isProgressDialogShowing()) {
            Utils.showProgressDialog(this);
        }
*/
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //  strFloorId = "";
        apiKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4, AuthKey + "EvenTt!cketYpE")));
        key = calculateHash(AuthKey, calculateHash(1, calculateHash(4, AppConstants.agentId + events.event_id + strFloorId + strEventDate +
                AppConstants.bookFrom + "T!ckEtYpE")));


        OtappApiServices otappApiServices = RestClient.getClient(true);
        Call<GetTicketTypeResponse> ticketTypeResponseCall = otappApiServices.getTicket(apiKey, AppConstants.agentId, AppConstants.bookFrom, String.valueOf(AuthKey), events.event_id, strFloorId,
                strEventDate, key);
        ticketTypeResponseCall.enqueue(new Callback<GetTicketTypeResponse>() {
            @Override
            public void onResponse(Call<GetTicketTypeResponse> call, Response<GetTicketTypeResponse> response) {
                //    Utils.closeProgressDialog();
                progressDialog.dismiss();
                JSONObject jsonObjectResponse = null;
                try {
                    jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                    Log.d("Log", "Response : " + jsonObjectResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.body() != null) {
                    if (response.body().status == 200) {
                        floorTicketList = response.body().getTicketsList;
                        Log.d("Log", "flor size=" + floorTicketList.size());
                        getTicketClassTypeAdatpter = new GetTicketClassTypeAdatpter(getApplicationContext(), floorTicketList, events, strEventDate, EventDetailsActivity.this, tempTicketTypeMap, EventDetailsActivity.this);
                        recyclerViewTicketClass.setAdapter(getTicketClassTypeAdatpter);
                        getTicketClassTypeAdatpter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetTicketTypeResponse> call, Throwable t) {
                //  Utils.closeProgressDialog();
                progressDialog.dismiss();

            }
        });
    }

    public void addImagesToViewPager() {

        if (events.event_slider_1 != null && !events.event_slider_1.equals("")) {
            silderImagesList.add(events.event_slider_1);
        }
        if (events.event_slider_2 != null && !events.event_slider_1.equals("")) {
            silderImagesList.add(events.event_slider_2);
        }

        viewPagerSliderImaeAdapter = new ViewPagerSliderImaeAdapter(this, silderImagesList);
        viewPager.setAdapter(viewPagerSliderImaeAdapter);
        viewPager.setCurrentItem(0);
    }

}
