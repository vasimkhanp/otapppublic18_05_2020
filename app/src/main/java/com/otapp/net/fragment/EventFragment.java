package com.otapp.net.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.Async.Interface.OtappApiServices;
import com.otapp.net.Async.Interface.RestClient;
import com.otapp.net.Events.Activity.EventsActivity;
import com.otapp.net.Events.Adapter.EventListRecyclerAdapter;
import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.ServiceCategoryPagerAdapterWithTitle;
import com.otapp.net.application.Otapp;
import com.otapp.net.helper.SHA;
import com.otapp.net.model.EventListPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
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

public class EventFragment extends BaseFragment {

    public static String Tag_EventFragment = "Tag_" + "EventFragment";

    View mView;

    @BindView(R.id.tlEventSlidingTabs)
    TabLayout eventTypeSlideTabsLayout;
 /*   @BindView(R.id.mViewPager)
    ViewPager mViewPager;*/
    @BindView(R.id.lnrNoEvent)
    LinearLayout lnrNoEvent;
    @BindView(R.id.tvEvenBetter)
    TextView tvEvenBetter;
    @BindView(R.id.eventListRecycler)
    RecyclerView eventListRecycler;

    List<EventsListResponse.Events> eventsArrayList;
    ServiceCategoryPagerAdapterWithTitle mServiceCategoryPagerAdapterWithTitle;
    private int mServicePosition = 0;
    EventListRecyclerAdapter eventListRecyclerAdapter;
    EventsListResponse eventsListResponse;
    EventsListResponse mEventListPojo;
    List<EventsListResponse.EventsGenres> eventsGenresList;
    List<EventsListResponse.Events> tempEventListResponse;
    /*private EventListPojo mEventListPojo;*/

    public static EventFragment newInstance() {
        EventFragment fragment = new EventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_service_event, container, false);
        ButterKnife.bind(this, mView);

      /*  Intent intent = new Intent(getContext(), EventsActivity.class);
        getActivity().startActivity(intent);*/
        InitializeControls();

        setLinkableText();
        return mView;
    }

    private void getEventCategory() {
        boolean isConnected = AppConstants.isConnected(getActivity());
        if(isConnected) {

            String apiKey, key;
            int AuthKey;

            if (!Utils.isInternetConnected(getActivity())) {
                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                return;
            }

            Utils.showProgressDialog(getActivity());
            Random r = new Random();
            int random = r.nextInt(4 - 1 + 1) + 1;
            if (random > 4 && random < 1) {
                random = 3;
            }
            AuthKey = Integer.parseInt(String.valueOf(random));
            String agentId = AppConstants.agentId;
            String book_from = AppConstants.bookFrom;
            apiKey = calculateHash(AuthKey, calculateHash(1, calculateHash(4, AuthKey + "EvenT1!5t")));

            key = calculateHash(AuthKey, calculateHash(1, calculateHash(4, agentId + book_from + "GeT1!5t")));
            OtappApiServices otappApiService = RestClient.getClient(true);
            Call<EventsListResponse> mCallEventList = otappApiService.getEventsList(apiKey, agentId, book_from, String.valueOf(AuthKey), key);
            mCallEventList.enqueue(new Callback<EventsListResponse>() {
                @Override
                public void onResponse(Call<EventsListResponse> call, Response<EventsListResponse> response) {
                    if (response.isSuccessful()) {
                        eventsListResponse = response.body();
                        if (eventsListResponse != null) {
                            if (eventsListResponse.status == 200) {
                                lnrNoEvent.setVisibility(View.GONE);

                                eventTypeSlideTabsLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                                eventsArrayList = response.body().eventsList;
                                eventsListResponse = response.body();
                                eventsGenresList = response.body().eventsGenresList;
                                // setEventList();
                                eventTypeSlideTabsLayout.addTab(eventTypeSlideTabsLayout.newTab().setText(R.string.all).setTag("1"), 0);
                                for (int i = 0; i <= response.body().eventsGenresList.size()-1 ; i++) {
                                    eventTypeSlideTabsLayout.addTab(eventTypeSlideTabsLayout.newTab().setText(response.body().eventsGenresList.get(i).genre_name).setTag("" + response.body().eventsGenresList.get(i).genre_id), i+1);

                                }
                                setEventList(eventsArrayList);
                            } else if (response.body().status == 201) {
                                lnrNoEvent.setVisibility(View.VISIBLE);
                                eventListRecycler.setVisibility(View.GONE);
                            } else {

                             //   Utils.showToast(getActivity(), "" + eventsListResponse.message);
                                eventsListResponse = null;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<EventsListResponse> call, Throwable t) {

                }
            });
       /* ApiInterface mApiInterface = RestClient.getClient(true);
        Call<EventListPojo> mCall = mApiInterface.getEventList(Otapp.mUniqueID);
        mCall.enqueue(new Callback<EventListPojo>() {
            @Override
            public void onResponse(Call<EventListPojo> call, Response<EventListPojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    mEventListPojo = response.body();
                    if (mEventListPojo != null) {
                        if (mEventListPojo.status.equalsIgnoreCase("200")) {
                            lnrNoEvent.setVisibility(View.GONE);
                            setEventList();

                        } else if (mEventListPojo.status.equalsIgnoreCase("404")) {
                            lnrNoEvent.setVisibility(View.VISIBLE);
                            mEventListPojo = null;
                        } else {

                            Utils.showToast(getActivity(), "" + mEventListPojo.message);
                            mEventListPojo = null;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EventListPojo> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });*/
        }else {
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void InitializeControls() {
        eventListRecycler.setHasFixedSize(true);
        eventListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        getEventCategory();
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


        /*if (!isAdded()) return;
        mServiceCategoryPagerAdapterWithTitle = new ServiceCategoryPagerAdapterWithTitle(getChildFragmentManager());

        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(mServicePosition);
        tlEventSlidingTabs.setupWithViewPager(mViewPager, true);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mServicePosition = i;
                LogUtils.e("", "onPageSelected::" + mServicePosition);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });*/

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(Tag_EventFragment, "isVisibleToUser::" + isVisibleToUser+" "+isAdded());
        if (isVisibleToUser) {
            if (mEventListPojo == null) {
               /* getEventCategory();*/
            } else {
             //   setEventList();
            }
        } else {
        }
    }


    public void setEventList(List<EventsListResponse.Events> eventsListResponseList){

        if(eventsListResponseList.size()==0){
            lnrNoEvent.setVisibility(View.VISIBLE);
            eventListRecycler.setVisibility(View.GONE);
        }else {
            lnrNoEvent.setVisibility(View.GONE);
            eventListRecycler.setVisibility(View.VISIBLE);
            eventListRecyclerAdapter = new EventListRecyclerAdapter(eventsListResponseList,getContext(), eventsListResponse);
            eventListRecycler.setAdapter(eventListRecyclerAdapter);
            eventListRecyclerAdapter.notifyDataSetChanged();
        }
    }
/*
    private void setEventList() {

        List<EventListPojo.EventCategory> mEventCategoryList = mEventListPojo.categories;

        if (getActivity() != null && isAdded()) {

            if (mEventCategoryList != null && mEventCategoryList.size() > 0) {

                EventListPojo.EventCategory mEventCategory = new EventListPojo().new EventCategory();
                mEventCategory.categoryId = "0";
                mEventCategory.categoryName = getString(R.string.all);
                mEventCategoryList.set(0, mEventCategory);

                for (int i = 0; i < mEventCategoryList.size(); i++) {
                    EventListFragment mFragment = EventListFragment.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.BNDL_CATEGORY_ID, mEventCategoryList.get(i).categoryId);

                    List<EventListPojo.Events> mEventParamList = new ArrayList<>();
                    if (mEventCategoryList.get(i).categoryId.equals("0")) {
                        mEventParamList.addAll(mEventListPojo.events);
                    } else {
                        if (mEventListPojo.events != null && mEventListPojo.events.size() > 0) {
                            for (int j = 0; j < mEventListPojo.events.size(); j++) {
                                if (mEventListPojo.events.get(j).eventCategory.equals(mEventCategoryList.get(i).categoryName)) {
                                    mEventParamList.add(mEventListPojo.events.get(j));
                                }
                            }
                        }
                    }

                    bundle.putString(Constants.BNDL_EVENT_LIST, new Gson().toJson(mEventParamList));

                    mServiceCategoryPagerAdapterWithTitle.addFragment(mFragment, mEventCategoryList.get(i).categoryName, bundle);
                }

                mViewPager.setAdapter(mServiceCategoryPagerAdapterWithTitle);

            }
        }

    }
*/

    private void setLinkableText() {

        Spannable wordtoSpan = new SpannableString("" + tvEvenBetter.getText().toString());
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tab_selected)), 29, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tab_selected)), 42, 48, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new UnderlineSpan(), 29, 38, 0);
        wordtoSpan.setSpan(new UnderlineSpan(), 42, 48, 0);
        ClickableSpan clickableSpanThempark = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (getActivity() instanceof HomeActivity) {
                    popBackStack(ServiceFragment.Tag_ServiceFragment);
                    HomeActivity activity = (HomeActivity) getActivity();
                    activity.loadThemepark();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpanMovie = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (getActivity() instanceof HomeActivity) {
                    popBackStack(ServiceFragment.Tag_ServiceFragment);
                    HomeActivity activity = (HomeActivity) getActivity();
                    activity.loadMovie();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
            }
        };
        wordtoSpan.setSpan(clickableSpanThempark, 29, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(clickableSpanMovie, 42, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvEvenBetter.setText(wordtoSpan);
        tvEvenBetter.setMovementMethod(LinkMovementMethod.getInstance());
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
