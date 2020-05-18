package com.otapp.net.Events.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.otapp.net.Async.Interface.OtappApiServices;
import com.otapp.net.Async.Interface.RestClient;
import com.otapp.net.Events.Adapter.EventListRecyclerAdapter;
import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.R;
import com.otapp.net.helper.SHA;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsActivity extends AppCompatActivity {
    @BindView(R.id.tlEventSlidingTabs)
    TabLayout eventTypeSlideTabsLayout;
    @BindView(R.id.eventListRecycler)
    RecyclerView eventListRecycler;
    @BindView(R.id.lnrNoEvent)
    LinearLayout lnrNoEvent;

    int AuthKey;
    String apiKey="",key="";
    String agentId=AppConstants.agentId;
    String book_from=AppConstants.bookFrom;

    List<EventsListResponse.Events> eventsArrayList;
    EventsListResponse eventsListResponse;
    EventListRecyclerAdapter eventListRecyclerAdapter;
    CountryCodePojo mCountryCodePojo;
    List<EventsListResponse.Events> tempEventListResponse;

    List<EventsListResponse.EventsGenres> eventsGenresList;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        MyPref.setPref(getApplicationContext(), MyPref.PREF_UKEY,"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyPref.setPref(getApplicationContext(), MyPref.PREF_UKEY,"");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);
        eventsArrayList= new ArrayList<>();
        eventListRecycler.setHasFixedSize(true);
        eventListRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        MyPref.setPref(getApplicationContext(), MyPref.PREF_UKEY,"");
        MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_FLAG,"0");
        MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_CODE,"");

        getEventList();

        eventTypeSlideTabsLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tempEventListResponse = new ArrayList<>();
                if(tab.getPosition()==0){
                    setEventList(eventsArrayList);
                }else {
                    if (eventsArrayList != null) {
                        for (int i = 0; i < eventsArrayList.size(); i++) {
                            if (tab.getTag().equals(eventsArrayList.get(i).event_genre)) {
                                tempEventListResponse.add(eventsArrayList.get(i));
                            }
                        }
                        setEventList(tempEventListResponse);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

       // getCountryCodeList();


       /* eventTypeSlideTabsLayout.addTab(eventTypeSlideTabsLayout.newTab().setText(R.string.all).setTag("1"),0);
        eventTypeSlideTabsLayout.addTab(eventTypeSlideTabsLayout.newTab().setText(R.string.all).setTag("2"),1);
        eventTypeSlideTabsLayout.setTabGravity(TabLayout.GRAVITY_FILL);*/
    }
    public void getEventList(){
        boolean isConnected = AppConstants.isConnected(this);
        if(isConnected){

            if(!Utils.isProgressDialogShowing()){
                Utils.showProgressDialog(this);
            }

            Random r = new Random();
            int random = r.nextInt(4 - 1 + 1) + 1;
            if (random > 4 && random < 1) {
                random = 3;
            }
            AuthKey = Integer.parseInt(String.valueOf(random));

            apiKey= calculateHash(AuthKey,calculateHash(1,calculateHash(4, AuthKey+"EvenT1!5t")));

            key=calculateHash(AuthKey,calculateHash(1,calculateHash(4 , agentId+book_from+"GeT1!5t")));
            OtappApiServices otappApiService = RestClient.getClient(true);
            Call<EventsListResponse> mCallEventList = otappApiService.getEventsList(apiKey,agentId,book_from, String.valueOf(AuthKey),key);
            mCallEventList.enqueue(new Callback<EventsListResponse>() {
                @Override
                public void onResponse(Call<EventsListResponse> call, Response<EventsListResponse> response) {
                   /* JSONObject jsonObjectResponse = null;
                    try {
                        jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("Log", "Response : " + jsonObjectResponse);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    Utils.closeProgressDialog();

                    if (response.body() != null) {
                        if (response.body().status == 200) {
                            lnrNoEvent.setVisibility(View.GONE);
                            eventTypeSlideTabsLayout.addTab(eventTypeSlideTabsLayout.newTab().setText(R.string.all).setTag("1"), 0);
                            for (int i = 0; i <= response.body().eventsGenresList.size() ; i++) {
                                eventTypeSlideTabsLayout.addTab(eventTypeSlideTabsLayout.newTab().setText(response.body().eventsGenresList.get(i).genre_name).setTag("" + response.body().eventsGenresList.get(i).genre_id), i+1);
                                Log.d("Log","genrer "+response.body().eventsGenresList.get(i).genre_name+ " "+response.body().eventsGenresList.get(i).genre_id);
                            }
                            eventTypeSlideTabsLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                            eventsArrayList = response.body().eventsList;
                            eventsListResponse = response.body();
                            eventsGenresList = response.body().eventsGenresList;
                            setEventList(eventsArrayList);

                        } else {
                            if (response.body().status == 201) {
                             /*   Toast.makeText(EventsActivity.this, ""+eventsGenresList., Toast.LENGTH_SHORT).show();*/
                                lnrNoEvent.setVisibility(View.VISIBLE);
                                eventListRecycler.setVisibility(View.GONE);
                            } else {
                            //    Toast.makeText(EventsActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        Toast.makeText(EventsActivity.this, "No Events", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EventsListResponse> call, Throwable t) {
                    Utils.closeProgressDialog();
               //     Toast.makeText(EventsActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
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
    public void setEventList(List<EventsListResponse.Events> eventsListResponseList){

        Toast.makeText(this, ""+eventsListResponseList.size(), Toast.LENGTH_SHORT).show();
        if(eventsListResponseList.size()==0){
            lnrNoEvent.setVisibility(View.VISIBLE);
            eventListRecycler.setVisibility(View.GONE);
        }else {
            lnrNoEvent.setVisibility(View.GONE);
            eventListRecycler.setVisibility(View.VISIBLE);
            eventListRecyclerAdapter = new EventListRecyclerAdapter(eventsListResponseList, this, eventsListResponse);
            eventListRecycler.setAdapter(eventListRecyclerAdapter);
            eventListRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
