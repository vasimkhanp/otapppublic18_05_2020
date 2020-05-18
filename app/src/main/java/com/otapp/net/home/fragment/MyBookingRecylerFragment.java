package com.otapp.net.home.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.Async.Interface.OtappApiServices;
import com.otapp.net.Async.Interface.RestClient;
import com.otapp.net.R;
import com.otapp.net.helper.SHA;
import com.otapp.net.home.Interface.PagerInterface;
import com.otapp.net.home.adapter.MyBookingsAdapter;
import com.otapp.net.home.adapter.PaginatonAdapter;
import com.otapp.net.home.core.MyBookingResponse;
import com.otapp.net.home.core.PagerModel;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.MyPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyBookingRecylerFragment extends Fragment implements PagerInterface {

  @BindView(R.id.recyclerBooking)
  RecyclerView recyclerViewBooking;
  @BindView(R.id.recyclerPagination)
  RecyclerView recyclerViewPagination;
  @BindView(R.id.pagerLayout)
    LinearLayout linearPagerLayout;
  @BindView(R.id.noEventLayout)
    LinearLayout noEventLayout;


  String strCustId="",strCustKey,strServiceId,strRquestType,strPgno;
  int authKey;
  String strReqType="0";
  public static String temp="1";
    MyBookingsAdapter myBookingsAdapter;
    List<MyBookingResponse.MyBookingTransactoins> myBookingTransactoinsList;
    List<PagerModel> noPageList;
    PagerModel pagerModel;
    MyBookingResponse myBookingResponse;
    PaginatonAdapter paginatonAdapter;
    RecyclerView.LayoutManager myBookingLayoutManager;
    RecyclerView.LayoutManager paginationLayoutManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_booking_recyler, container, false);
        ButterKnife.bind(this,view);
        Bundle bundle = getArguments();
        myBookingTransactoinsList=new ArrayList<>();
        strReqType=MyPref.getPref(getContext(),MyPref.REQUEST_TYPE,"");
        noPageList=new ArrayList<>();
//        Toast.makeText(getContext(), "on Create", Toast.LENGTH_SHORT).show();
        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }
        authKey = Integer.parseInt(String.valueOf(random));

      /*  if(bundle.getString("serviceId")!=null){
            temp=bundle.getString("serviceId");

        }*/
        strServiceId=bundle.getString("serviceId");
        strRquestType=bundle.getString("requestType");
        strRquestType=MyPref.getPref(getContext(),MyPref.REQUEST_TYPE,"0");
        Log.d("Log","strQrestTU "+strRquestType );
        strPgno=bundle.getString("pageNo");

        myBookingLayoutManager= new LinearLayoutManager(getActivity());
        recyclerViewBooking.setHasFixedSize(true);
        recyclerViewBooking.setLayoutManager(myBookingLayoutManager);

        paginationLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) paginationLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPagination.setHasFixedSize(true);
        recyclerViewPagination.setLayoutManager(paginationLayoutManager);

        paginatonAdapter=null;
        noPageList= new ArrayList<>();
        getBooking();

       // timer();
        return view;
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

    public void getBooking() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(true);
        progressDialog.show();
        String apiKey = "";
        strCustId = MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
        strCustKey = MyPref.getPref(getActivity(), MyPref.PREF_USER_KEY, "");
        apiKey = calculateHash(authKey, calculateHash(1, calculateHash(4, strCustKey + strCustId + strServiceId + strRquestType + strPgno + authKey + "b0oK!nG")));
        OtappApiServices otappApiServices = RestClient.getClient(true);
        Call<MyBookingResponse> myBookingResponseCall = otappApiServices.getMyBookings(apiKey, strCustKey, strCustId, strServiceId,
                String.valueOf(authKey), strRquestType, strPgno);

        Log.d("Log","RequresTU[e "+strServiceId);

        myBookingResponseCall.enqueue(new Callback<MyBookingResponse>() {
            @Override
            public void onResponse(Call<MyBookingResponse> call, Response<MyBookingResponse> response) {
                progressDialog.dismiss();
                JSONObject jsonObjectResponse = null;
                try {
                    jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                    Log.d("Log", "Response Thempark: " + jsonObjectResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.body() != null) {
                    if (response.body().status == 200) {

                        myBookingTransactoinsList.clear();
                        if (response.body().myBookingTransactoinsList != null) {
                            noEventLayout.setVisibility(View.GONE);
                            recyclerViewBooking.setVisibility(View.VISIBLE);
                            linearPagerLayout.setVisibility(View.VISIBLE);

                            myBookingResponse = response.body();

                            myBookingTransactoinsList = response.body().myBookingTransactoinsList;
                         //   Toast.makeText(getActivity(), "" + response.body().message+ "size"+myBookingTransactoinsList.size(), Toast.LENGTH_SHORT).show();
                            myBookingsAdapter = new MyBookingsAdapter(getActivity(), myBookingTransactoinsList);
                            recyclerViewBooking.setAdapter(myBookingsAdapter);
                            myBookingsAdapter.notifyDataSetChanged();

                            if (paginatonAdapter == null) {
                                for (int i = 1; i <= Integer.parseInt(myBookingResponse.tot_pages); i++) {
                                    if (i == 1) {
                                        pagerModel = new PagerModel("" + i, true);
                                        noPageList.add(pagerModel);
                                    } else {
                                        pagerModel = new PagerModel("" + i, false);
                                        noPageList.add(pagerModel);
                                    }

                                }
                                paginatonAdapter = new PaginatonAdapter(getActivity(), noPageList, MyBookingRecylerFragment.this);
                                recyclerViewPagination.setAdapter(paginatonAdapter);
                                paginatonAdapter.notifyDataSetChanged();
                            }
                        }

                    } else {
                        /* Toast.makeText(getActivity(), "" + response.body().message+ "size"+myBookingTransactoinsList.size(), Toast.LENGTH_SHORT).show();*/
                        noEventLayout.setVisibility(View.VISIBLE);
                        recyclerViewBooking.setVisibility(View.GONE);
                        linearPagerLayout.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onFailure(Call<MyBookingResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(), "GHJJHDGHKJA", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void selectedPage(String pageNo) {
        strPgno=pageNo;
           getBooking();
    }
    public void timer(){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
                String strRques=MyPref.getPref(getContext(),MyPref.REQUEST_TYPE,"0");
                if (!strRques.equals(strServiceId)){
                    strServiceId=strRques;
                    getBooking();
                }
            }
        }, 0, 1000);
    }
}
