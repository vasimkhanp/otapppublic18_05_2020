package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.otapp.net.R;
import com.otapp.net.adapter.UpcomingTripAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.UpcomingTripPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingTripsFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_UpcomingTripsFragment = "Tag_" + "UpcomingTripsFragment";

    View mView;

    @BindView(R.id.tvPrevious)
    TextView tvPrevious;
    @BindView(R.id.lnrEmptyTravel)
    LinearLayout lnrEmptyTravel;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.lvTrips)
    ListView lvTrips;

    UpcomingTripAdapter mUpcomingTripAdapter;
    UpcomingTripPojo mUpcomingTripData;
    List<UpcomingTripPojo.UpcomingTrip> mUpcomingTripList = new ArrayList<>();
    ArrayList<String> mPageNumberList = new ArrayList<>();

    private String mPosition = "0";
    private int page_no = 1;
    public int PAGE_SIZE = 1, PAGE_LIMIT = 10;
    private boolean isLastPage = false, isLoading = false;

    public static UpcomingTripsFragment newInstance() {
        UpcomingTripsFragment fragment = new UpcomingTripsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_upcoming_trips, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPosition = bundle.getString(Constants.CITY_TYPE_POSITION);
        }

        mUpcomingTripAdapter = new UpcomingTripAdapter(getActivity(), this);
        lvTrips.setAdapter(mUpcomingTripAdapter);

        lvTrips.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mUpcomingTripList.size() > 1) {
//                    page_no = mUpcomingTripList.size() / PAGE_LIMIT;
                    if (page_no < 1) {
                        page_no = 1;
                    }
                    LogUtils.e("", "onScroll page_no::"+page_no);
                }

                if (!isLoading && !isLastPage) {
                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {

                        isLoading = true;
                        page_no = page_no + 1;
                        LogUtils.e("", "Load more page_number::" + page_no);
                        getTripNextList();
                    }
                }

            }
        });

//        tvPrevious.setOnClickListener(this);
//        tvNext.setOnClickListener(this);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(Tag_UpcomingTripsFragment, "isVisibleToUser::" + isVisibleToUser);
        if (isVisibleToUser) {
            if (mUpcomingTripData == null || mUpcomingTripList.size() == 0) {
                LogUtils.e("", mPosition + " mUpcomingTripData is null");
                getTripList();
            } else {
                LogUtils.e("", mPosition + " mUpcomingTripData is not null");

                PAGE_SIZE = mUpcomingTripData.total_count;

                if (mUpcomingTripData.data != null) {
//                    if (mUpcomingTripList != null && mUpcomingTripList.size() > 0){
//                        mUpcomingTripList.clear();
//                    }
//                    mUpcomingTripList.addAll(mUpcomingTripData.data);
                    mUpcomingTripAdapter.addAll(mUpcomingTripList);
                }

                if (mUpcomingTripList.size() % PAGE_LIMIT == 0 && mUpcomingTripList.size() < PAGE_SIZE) {
                    isLastPage = false;
                } else {
                    isLastPage = true;
                }

            }
        } else {
        }
    }

    private void getTripList() {

//        if (getActivity() != null) {

            if (!Utils.isInternetConnected(getActivity())) {
                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                return;
            }

            Utils.showProgressDialog(getActivity());

            if (mUpcomingTripList != null && mUpcomingTripList.size() > 0) {
                mUpcomingTripList.clear();
            }

            page_no = 1;

            if (mPageNumberList != null && mPageNumberList.size() > 0) {
                mPageNumberList.clear();
                mPageNumberList.add("" + page_no);

            }

            Map<String, String> jsonParams = new HashMap<>();
            jsonParams.put("user_token", "" + Otapp.mUniqueID);
            jsonParams.put("page_no", "" + page_no);
            jsonParams.put("key", "10e66c071dafe0e258258f179984f21a");

//            if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                if (getActivity() != null) {
                    jsonParams.put("cust_id", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
                    jsonParams.put("cust_log_key", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_KEY, ""));
                }else{
                    jsonParams.put("cust_id", "" + MyTripFragment.cust_id);
                    jsonParams.put("cust_log_key", "" + MyTripFragment.cust_log_key);
                }
//            }

            ApiInterface mApiInterface = RestClient.getClient(true);

            Call<UpcomingTripPojo> mCall = null;
            LogUtils.e("", "mPosition::" + mPosition);
            if (mPosition.equals("0")) {
                mCall = mApiInterface.getUpcomingTripList(jsonParams);
            } else if (mPosition.equals("1")) {
                mCall = mApiInterface.getCompleteTripList(jsonParams);
            } else if (mPosition.equals("2")) {
                mCall = mApiInterface.getCancelledTripList(jsonParams);
            }

            mCall.enqueue(new Callback<UpcomingTripPojo>() {
                @Override
                public void onResponse(Call<UpcomingTripPojo> call, Response<UpcomingTripPojo> response) {

                    Utils.closeProgressDialog();

                    lnrEmptyTravel.setVisibility(View.VISIBLE);

                    if (response.isSuccessful()) {
                        UpcomingTripPojo mUpcomingTripPojo = response.body();
                        if (mUpcomingTripPojo != null) {
                            if (mUpcomingTripPojo.status.equalsIgnoreCase("200")) {

                                LogUtils.e("", "onResponse page_no::"+page_no);

                                mUpcomingTripData = mUpcomingTripPojo;

                                PAGE_SIZE = mUpcomingTripPojo.total_count;

                                if (mUpcomingTripPojo.data != null) {
                                    mUpcomingTripList.addAll(mUpcomingTripData.data);
                                    mUpcomingTripAdapter.addAll(mUpcomingTripList);
                                }

                                if (mUpcomingTripList.size() % PAGE_LIMIT == 0 && mUpcomingTripList.size() < PAGE_SIZE) {
                                    isLastPage = false;
                                } else {
                                    isLastPage = true;
                                }

                                lnrEmptyTravel.setVisibility(View.GONE);

                            } else {
                                page_no = 1;
                                Utils.showToast(getActivity(), "" + mUpcomingTripPojo.message);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpcomingTripPojo> call, Throwable t) {
                    Utils.closeProgressDialog();
                    page_no = 1;
                    isLoading = false;
                    LogUtils.e("", "onFailure:" + t.getMessage());
                }
            });


//        }

    }

    private void getTripNextList() {

//        if (getActivity() != null) {

            if (!Utils.isInternetConnected(getActivity())) {
                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                return;
            }

            Utils.showProgressDialog(getActivity());

            Map<String, String> jsonParams = new HashMap<>();
            jsonParams.put("user_token", "" + Otapp.mUniqueID);
            jsonParams.put("page_no", "" + page_no);
            jsonParams.put("key", "10e66c071dafe0e258258f179984f21a");

//            if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                if (getActivity() != null) {
                    jsonParams.put("cust_id", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
                    jsonParams.put("cust_log_key", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_KEY, ""));
                }else{
                    jsonParams.put("cust_id", "" + MyTripFragment.cust_id);
                    jsonParams.put("cust_log_key", "" + MyTripFragment.cust_log_key);
                }
//            }

            ApiInterface mApiInterface = RestClient.getClient(true);

            Call<UpcomingTripPojo> mCall = null;
            LogUtils.e("", "mPosition::" + mPosition);
            if (mPosition.equals("0")) {
                mCall = mApiInterface.getUpcomingTripList(jsonParams);
            } else if (mPosition.equals("1")) {
                mCall = mApiInterface.getCompleteTripList(jsonParams);
            } else if (mPosition.equals("2")) {
                mCall = mApiInterface.getCancelledTripList(jsonParams);
            }

            mCall.enqueue(new Callback<UpcomingTripPojo>() {
                @Override
                public void onResponse(Call<UpcomingTripPojo> call, Response<UpcomingTripPojo> response) {

                    Utils.closeProgressDialog();
                    isLoading = false;


                    if (response.isSuccessful()) {
                        UpcomingTripPojo mUpcomingTripPojo = response.body();
                        if (mUpcomingTripPojo != null) {
                            if (mUpcomingTripPojo.status.equalsIgnoreCase("200")) {

                                LogUtils.e("", "onResponse Next page_no::"+page_no);
                                if (!mPageNumberList.contains("" + page_no)) {
                                    mUpcomingTripList.addAll(mUpcomingTripData.data);
                                    mPageNumberList.add("" + page_no);
                                }

                                if (mUpcomingTripList.size() % PAGE_LIMIT == 0 && mUpcomingTripList.size() < PAGE_SIZE) {
                                    isLastPage = false;
                                } else {
                                    isLastPage = true;
                                }

                                mUpcomingTripAdapter.addAll(mUpcomingTripList);

                                lnrEmptyTravel.setVisibility(View.GONE);

                            } else {
                                Utils.showToast(getActivity(), "" + mUpcomingTripPojo.message);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpcomingTripPojo> call, Throwable t) {
                    Utils.closeProgressDialog();
                    isLoading = false;
                    LogUtils.e("", "onFailure:" + t.getMessage());
                }
            });


//        }

    }


    public void onTripClicked(UpcomingTripPojo.UpcomingTrip mUpcomingTrip) {

    }

    @Override
    public void onClick(View view) {

    }
}
