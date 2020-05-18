package com.otapp.net.home.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.otapp.net.HomeActivity;
import com.otapp.net.LoginActivity;
import com.otapp.net.R;
import com.otapp.net.application.Otapp;
import com.otapp.net.fragment.HomeFragment;
import com.otapp.net.helper.SHA;
import com.otapp.net.home.Interface.PagerInterface;
import com.otapp.net.home.adapter.MyBookingsAdapter;
import com.otapp.net.home.adapter.PAdapter;
import com.otapp.net.home.adapter.PaginatonAdapter;
import com.otapp.net.home.adapter.MyAdapter;
import com.otapp.net.home.core.MyBookingResponse;
import com.otapp.net.home.core.PagerModel;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.MyPref;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyBookingFragment extends Fragment implements PagerInterface {
    @BindView(R.id.toolbarTitle)
    TextView tvToolbarTitle;
    @BindView(R.id.allSelected)
    TableRow tbAllSelected;
    @BindView(R.id.upcomingSelected)
    TableRow tbUpcomingSelected;
    @BindView(R.id.completedSelected)
    TableRow tbCompleted;

    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvCompleted)
    TextView tvCompleted;
    @BindView(R.id.tvUpcoming)
    TextView tvUpcoming;
    @BindView(R.id.viwePager)
    ViewPager viewPager;

    @BindView(R.id.otappTabLayout)
    TabLayout otappFeaturesTabLayout;
    @BindView(R.id.ivNextTab)
    ImageView ivNextTab;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.layoutMyBooking)
    LinearLayout layoutMyBookings;
    @BindView(R.id.tvLoginPage)
    TextView tvLoginPage;
    @BindView(R.id.layoutLogin)
    LinearLayout tvLayoutLogin;
    ArrayList<Fragment> fragmentList = new ArrayList<>();
/*
    @BindView(R.id.recyclerBooking)
    RecyclerView recyclerMyBookings;
    RecyclerView.LayoutManager myBookingLayoutManager;

    @BindView(R.id.recyclerPagination)
    RecyclerView recyclerPagination;

    @BindView(R.id.noEventLayout)
    LinearLayout noEventLayout;

    @BindView(R.id.pagerLayout)h
    LinearLayout pagerLayout;*/


    RecyclerView.LayoutManager paginationLayoutManager;

    // String strServiceId="1";
    String strCustKey;
    String strCustId = "", strRquestType = "0", strPgno = "0";
    int authKey;
    MyBookingsAdapter myBookingsAdapter;
    List<MyBookingResponse.MyBookingTransactoins> myBookingTransactoinsList;
    List<PagerModel> noPageList;
    PagerModel pagerModel;
    MyBookingResponse myBookingResponse;
    PaginatonAdapter paginatonAdapter;
    int fragmetStatus = 0;
    FlightCityPojo.Data mFlightCityPojoData;
    MyAdapter tabPagerAdapter;
    int positon = 1, tabPostion;
    int strServiceId;
    ActionBar actionBar;
    public static String Tag_MyBookingFragment = "Tag_" + "MyBookingFragment";
    View view;
    PAdapter viewPagerAdapter;

    public static MyBookingFragment newInstance() {
        MyBookingFragment fragment = new MyBookingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void switchFragment(Fragment mFragment, String mTag, boolean isBackStack) {

        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.switchFragment(mFragment, mTag, isBackStack);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(), "REsume", Toast.LENGTH_SHORT).show();

        if (MyPref.getPref(getContext(), MyPref.PREF_IS_LOGGED, false)) {
            layoutMyBookings.setVisibility(View.VISIBLE);
            tvLayoutLogin.setVisibility(View.GONE);
            if (otappFeaturesTabLayout != null) {
            /*if (viewPager != null) {
                tabPostion = otappFeaturesTabLayout.getSelectedTabPosition();
                tabPagerAdapter = new MyAdapter(getActivity(), getActivity().getSupportFragmentManager(),
                        otappFeaturesTabLayout.getTabCount());
                viewPager.setAdapter(tabPagerAdapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(otappFeaturesTabLayout));
                otappFeaturesTabLayout.setupWithViewPager(viewPager);
                viewPager.setCurrentItem(0);
                otappFeaturesTabLayout.getTabAt(0).select();
                Toast.makeText(getActivity(), "vn" + tabPostion, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "vnull", Toast.LENGTH_SHORT).show();
            }*/
                onAddFragments();
            } else {
//            Toast.makeText(getActivity(), "tnull", Toast.LENGTH_SHORT).show();

            }
        } else {
            if (isLogin) {
//                Toast.makeText(getActivity(), "Home", Toast.LENGTH_SHORT).show();
//                switchFragment(new HomeFragment(), HomeFragment.Tag_HomeFragment, true);
                Fragment fragment2 = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mFrameLayout, fragment2);
                fragmentTransaction.commit();
                HomeActivity activity = (HomeActivity) getActivity();
                activity.bottomNavigationView.getMenu().getItem(0).setChecked(true);
            } else {
                layoutMyBookings.setVisibility(View.GONE);
                tvLayoutLogin.setVisibility(View.VISIBLE);
            }
        }

    }

    private void onAddFragments() {
        if (viewPagerAdapter == null) {
            for (int i = 0; i < Otapp.mServiceList.size(); i++) {
                if (Otapp.mServiceList.get(i).service_name.contains("/")) {
                    String[] spitedStirng = Otapp.mServiceList.get(i).service_name.split("/");
                    String serviceName = "";
                    for (int j = 0; j < spitedStirng.length; j++) {
                        if (j < spitedStirng.length - 1) {
                            serviceName = serviceName + spitedStirng[j].substring(0, 1).toUpperCase() + spitedStirng[j].substring(1).toLowerCase() + "/";
                        } else {
                            serviceName = serviceName + spitedStirng[j].substring(0, 1).toUpperCase() + spitedStirng[j].substring(1).toLowerCase();
                        }
                    }
                    otappFeaturesTabLayout.addTab(otappFeaturesTabLayout.newTab().setText(serviceName).setTag(Otapp.mServiceList.get(i).service_id), i);
                } else {
                    otappFeaturesTabLayout.addTab(otappFeaturesTabLayout.newTab().setText(Otapp.mServiceList.get(i).service_name.substring(0, 1).toUpperCase() + Otapp.mServiceList.get(i).service_name.substring(1).toLowerCase()).setTag(Otapp.mServiceList.get(i).service_id), i);
                }
            }
            otappFeaturesTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            fragmentList = new ArrayList<Fragment>();

            for (int i = 0; i < Otapp.mServiceList.size(); i++) {
                Bundle bundle = new Bundle();
                bundle.putString("requestType", "0");
                bundle.putString("pageNo", "0");
                bundle.putString("serviceId", "" + (i + 1));
                Fragment fragment = new MyBookingRecylerFragment();
                fragment.setArguments(bundle);
                fragmentList.add(fragment);
            }

            viewPagerAdapter = new PAdapter(getActivity().getSupportFragmentManager(), fragmentList);
//        getActivity().getSupportFragmentManager().executePendingTransactions();
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(otappFeaturesTabLayout));
            otappFeaturesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_booking, container, false);
        ButterKnife.bind(this, view);
        MyPref.setPref(getContext(), MyPref.REQUEST_TYPE, "0");
        back.setVisibility(View.GONE);
        AppConstants.SERVICE_ID = "1";
        MyPref.setPref(getContext(), "SERVICE_ID", "1");
        myBookingTransactoinsList = new ArrayList<>();
        noPageList = new ArrayList<>();
        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }
        authKey = Integer.parseInt(String.valueOf(random));



        /*tabPagerAdapter = new MyAdapter(getActivity(), getActivity().getSupportFragmentManager(),
                otappFeaturesTabLayout.getTabCount());
        viewPager.setAdapter(tabPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(otappFeaturesTabLayout));
        otappFeaturesTabLayout.setupWithViewPager(viewPager);*/


    /*    myBookingLayoutManager= new LinearLayoutManager(getActivity());
        recyclerMyBookings.setHasFixedSize(true);
        recyclerMyBookings.setLayoutManager(myBookingLayoutManager);

        paginationLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) paginationLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerPagination.setHasFixedSize(true);
        recyclerPagination.setLayoutManager(paginationLayoutManager);
*/

        tvToolbarTitle.setVisibility(View.GONE);
        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tbCompleted.setVisibility(View.GONE);
                tbAllSelected.setVisibility(View.VISIBLE);
                tbUpcomingSelected.setVisibility(View.GONE);
               /* strRquestType="0";
                strPgno="0";
                paginatonAdapter=null;
                noPageList= new ArrayList<>();*/
                //   getBooking();
                Bundle bundle = new Bundle();
                bundle.putString("serviceId", "" + positon);
                bundle.putString("requestType", "0");
                bundle.putString("pageNo", "0");
                MyPref.setPref(getContext(), MyPref.REQUEST_TYPE, "0");
              /*  Fragment fragment= new MyBookingRecylerFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.viwePager, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
              //  transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);  // this will manage backstack
                transaction.commit();*/
                tabPostion = otappFeaturesTabLayout.getSelectedTabPosition();

               /* tabPagerAdapter = new MyAdapter(getActivity(), getActivity().getSupportFragmentManager(),
                        otappFeaturesTabLayout.getTabCount());
                tabPagerAdapter.getItem(strServiceId);
                viewPager.setAdapter(tabPagerAdapter);

                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(otappFeaturesTabLayout));
                otappFeaturesTabLayout.setupWithViewPager(viewPager);
             *//*   viewPager.setAdapter(tabPagerAdapter);
                otappFeaturesTabLayout.getTabAt(strServiceId).select();*//*
                viewPager.setCurrentItem(tabPostion);
                viewPager.getAdapter().notifyDataSetChanged();
                otappFeaturesTabLayout.getTabAt(tabPostion).select();*/
               /* viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(otappFeaturesTabLayout));
                otappFeaturesTabLayout.setupWithViewPager(viewPager);*/
                viewPagerAdapter = new PAdapter(getActivity().getSupportFragmentManager(), fragmentList);
//        getActivity().getSupportFragmentManager().executePendingTransactions();
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(otappFeaturesTabLayout));
                viewPager.setCurrentItem(tabPostion);
                otappFeaturesTabLayout.getTabAt(tabPostion).select();
            }
        });
        tvCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tbCompleted.setVisibility(View.VISIBLE);
                tbAllSelected.setVisibility(View.GONE);
                tbUpcomingSelected.setVisibility(View.GONE);
                MyPref.setPref(getContext(), MyPref.REQUEST_TYPE, "2");
              /*  strRquestType="2";
                strPgno="0";
                paginatonAdapter=null;
                noPageList= new ArrayList<>();*/
                //  getBooking();
              /*  Bundle bundle = new Bundle();
                bundle.putString("serviceId",""+positon);
                bundle.putString("requestType","2");
                bundle.putString("pageNo","0");
                Fragment fragment= new MyBookingRecylerFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.layoutRecycler, fragment); // fragment container id in first parameter is the  container(Main layout id) of Activity
                //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);  // this will manage backstack
                transaction.commit();*/
                tabPostion = otappFeaturesTabLayout.getSelectedTabPosition();

                /*tabPagerAdapter = new MyAdapter(getActivity(), getActivity().getSupportFragmentManager(),
                        otappFeaturesTabLayout.getTabCount());
                tabPagerAdapter.getItem(strServiceId);
                viewPager.setAdapter(tabPagerAdapter);

                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(otappFeaturesTabLayout));
                otappFeaturesTabLayout.setupWithViewPager(viewPager);
                //   otappFeaturesTabLayout.getTabAt(strServiceId).select();
                viewPager.setCurrentItem(tabPostion, true);
                viewPager.getAdapter().notifyDataSetChanged();
                */
                viewPagerAdapter = new PAdapter(getActivity().getSupportFragmentManager(), fragmentList);
//        getActivity().getSupportFragmentManager().executePendingTransactions();
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(otappFeaturesTabLayout));
                viewPager.setCurrentItem(tabPostion);
                otappFeaturesTabLayout.getTabAt(tabPostion).select();
            }
        });
        tvUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tbCompleted.setVisibility(View.GONE);
                tbAllSelected.setVisibility(View.GONE);
                tbUpcomingSelected.setVisibility(View.VISIBLE);
                MyPref.setPref(getContext(), MyPref.REQUEST_TYPE, "1");
               /* strRquestType="1";
                strPgno="0";
                paginatonAdapter=null;
                noPageList= new ArrayList<>();*/
                //getBooking();
              /*  Bundle bundle = new Bundle();
                bundle.putString("serviceId",""+positon);
                bundle.putString("requestType","1");
                bundle.putString("pageNo","0");
                Fragment fragment= new MyBookingRecylerFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.layoutRecycler, fragment);
              //  transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();*/
                tabPostion = otappFeaturesTabLayout.getSelectedTabPosition();

                /*tabPagerAdapter = new MyAdapter(getActivity(), getActivity().getSupportFragmentManager(),
                        otappFeaturesTabLayout.getTabCount());
                tabPagerAdapter.getItem(strServiceId);
                viewPager.setAdapter(tabPagerAdapter);

                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(otappFeaturesTabLayout));
                otappFeaturesTabLayout.setupWithViewPager(viewPager);
                //
                viewPager.setCurrentItem(tabPostion, true);
                viewPager.getAdapter().notifyDataSetChanged();
                otappFeaturesTabLayout.getTabAt(tabPostion).select();*/
                viewPagerAdapter = new PAdapter(getActivity().getSupportFragmentManager(), fragmentList);
//        getActivity().getSupportFragmentManager().executePendingTransactions();
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(otappFeaturesTabLayout));
                viewPager.setCurrentItem(tabPostion);
                otappFeaturesTabLayout.getTabAt(tabPostion).select();
            }
        });

        tvLayoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin = true;
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getActivity().startActivity(intent);

            }
        });
        //   otappFeaturesTabLayout.setSelectedTabIndicator(Integer.parseInt(fragmetStatus));


        // getBooking();


        /*otappFeaturesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                strServiceId = tab.getPosition();
                Log.d("Log", "Position : " + tab.getPosition());
                //   Toast.makeText(getActivity(), ""+strServiceId, Toast.LENGTH_SHORT).show();
            *//*    fragmetStatus= Integer.parseInt(strServiceId);
                strPgno="0";
                paginatonAdapter=null;
                noPageList= new ArrayList<>();*//*
                //  getBooking();
                positon = tab.getPosition() + 1;

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

       /* ivNextTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otappFeaturesTabLayout.setScrollX(otappFeaturesTabLayout.getWidth());
                otappFeaturesTabLayout.getTabAt(5).select();
            }
        });*/


       /* recyclerMyBookings.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {
             Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();
                if(fragmetStatus<=Otapp.mServiceList.size()) {
                    fragmetStatus++;
                    otappFeaturesTabLayout.getTabAt(fragmetStatus).select();
                    strServiceId = String.valueOf(fragmetStatus);
                    strPgno = "0";
                    paginatonAdapter = null;
                    noPageList = new ArrayList<>();
                    getBooking();
                }
            }
            public void onSwipeBottom() {
              Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
            }

        });*/
      /*  OnGestureRegisterListener onGestureRegisterListener = new OnGestureRegisterListener(getActivity()) {
            public void onSwipeRight(View view) {
                Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft(View view) {
                Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();
                if(fragmetStatus<=Otapp.mServiceList.size()) {
                    fragmetStatus++;
                    otappFeaturesTabLayout.getTabAt(fragmetStatus).select();
                    strServiceId = String.valueOf(fragmetStatus);
                    strPgno = "0";
                    paginatonAdapter = null;
                    noPageList = new ArrayList<>();
                    getBooking();
                }
            }
            public void onSwipeBottom(View view) {
                // Do something
            }
            public void onSwipeTop(View view) {
                // Do something
                Toast.makeText(getActivity(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onClick(View view) {
                // Do something
            }
            public boolean onLongClick(View view) {
                // Do something
                return true;
            }
        };
        recyclerMyBookings.setOnTouchListener(onGestureRegisterListener);*/


        return view;
    }


    boolean isLogin = false;

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

/*
    public void getBooking(){
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(true);
        progressDialog.show();
        String apiKey="";
        strCustId= MyPref.getPref(getActivity(),MyPref.PREF_USER_ID,"");
        strCustKey= MyPref.getPref(getActivity(),MyPref.PREF_USER_KEY,"");
        apiKey =calculateHash(authKey,calculateHash(1,calculateHash(4 , strCustKey+strCustId+strServiceId+strRquestType+strPgno+authKey+"b0oK!nG")));
        OtappApiServices otappApiServices = RestClient.getClient(true);
        Call<MyBookingResponse> myBookingResponseCall =otappApiServices.getMyBookings(apiKey,strCustKey,strCustId,strServiceId,
                String.valueOf(authKey),strRquestType,strPgno);
        myBookingResponseCall.enqueue(new Callback<MyBookingResponse>() {
            @Override
            public void onResponse(Call<MyBookingResponse> call, Response<MyBookingResponse> response) {
                progressDialog.dismiss();
                JSONObject jsonObjectResponse = null;
                try {
                    jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                    Log.d("Log", "Response : " + jsonObjectResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(response.body()!=null) {
                    if (response.body().status == 200) {
                        myBookingTransactoinsList.clear();
                        if (response.body().myBookingTransactoinsList != null) {
                            noEventLayout.setVisibility(View.GONE);
                            recyclerMyBookings.setVisibility(View.VISIBLE);
                            pagerLayout.setVisibility(View.VISIBLE);
                            myBookingResponse = response.body();

                            myBookingTransactoinsList = response.body().myBookingTransactoinsList;
                            myBookingsAdapter = new MyBookingsAdapter(getActivity(), myBookingTransactoinsList);
                            recyclerMyBookings.setAdapter(myBookingsAdapter);
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
                                paginatonAdapter = new PaginatonAdapter(getActivity(), noPageList, MyBookingFragment.this);
                                recyclerPagination.setAdapter(paginatonAdapter);
                                paginatonAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                       // Toast.makeText(getActivity(), "" + response.body().message, Toast.LENGTH_SHORT).show();
                        noEventLayout.setVisibility(View.VISIBLE);
                        recyclerMyBookings.setVisibility(View.GONE);
                        pagerLayout.setVisibility(View.GONE);

                    }
                }

            }

            @Override
            public void onFailure(Call<MyBookingResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
*/

    @Override
    public void selectedPage(String pageNo) {
        strPgno = pageNo;
        //   getBooking();
    }
}
