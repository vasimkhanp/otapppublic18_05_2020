package com.otapp.net.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.R;
import com.otapp.net.adapter.MovieSeatAdapter;
import com.otapp.net.adapter.MovieTermsAdapter;
import com.otapp.net.adapter.MovieTimeInnerAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.MovieDetailsPojo;
import com.otapp.net.model.MovieSeat;
import com.otapp.net.model.MovieSeatListPojo;
import com.otapp.net.model.MovieTheaterListPojo;
import com.otapp.net.model.SeatProcessedPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;
import com.otapp.net.view.ZoomView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieSeatFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_MovieSeatFragment = "Tag_" + "MovieSeatFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvCinema)
    TextView tvCinema;
    @BindView(R.id.tvScreen)
    TextView tvScreen;
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvSeat)
    TextView tvSeat;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.lnrBook)
    LinearLayout lnrBook;
    //    @BindView(R.id.gvSeat)
//    GridView gvSeat;
    @BindView(R.id.flSeat)
    FrameLayout flSeat;
    @BindView(R.id.rvTimeList)
    RecyclerView rvTimeList;
    //    @BindView(R.id.tlDots)
//    TabLayout tlDots;
    GridView gvSeat;
    Dialog termsDialog;

    MovieSeatAdapter mMovieSeatAdapter;

    private int INDEX_COUNT = 2;
    private int mSeatsCount, mSeatPrice, mMaxSeat, mSelectedSeatCount;

    private String mSelectedDate = "", mMovieID = "", mSelectedScreenId = "", mSelectedTimeId = "", mvScreen = "", mcinemaScreenName = "", mCurrency = "", mUserKey = "";
    List<MovieDetailsPojo.MovieDate> mMovieDateList;
    List<MovieTheaterListPojo.Theater> mMovieTheaterList;
    List<MovieTheaterListPojo.ScreenTime> mMovieTimeList;
    private MovieDetailsPojo.Movie mMovie;
    private boolean isRunning = true;

    List<MovieSeat> mMovieSeatList = new ArrayList<>();

    public static MovieSeatFragment newInstance() {
        MovieSeatFragment fragment = new MovieSeatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_movie_seat, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isRunning = false;
    }

    private void InitializeControls() {

        View mGridView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_seat_grid, null);
        gvSeat = mGridView.findViewById(R.id.gvSeat);
//        gvSeat.setHorizontalSpacing((int) getResources().getDimension(R.dimen._5sdp));
//        gvSeat.setVerticalSpacing((int) getResources().getDimension(R.dimen._5sdp));

        ZoomView zoomView = new ZoomView(getActivity());
        zoomView.addView(gvSeat);
        flSeat.addView(zoomView);


        mMovieSeatAdapter = new MovieSeatAdapter(getActivity(), new MovieSeatAdapter.OnMovieSeatClickListener() {
            @Override
            public void onMoieSelected(int position) {

                if (mMovieSeatList != null && mMovieSeatList.size() > 0) {
                    MovieSeat mMovieSeat = mMovieSeatList.get(position);
                    if (mMovieSeat.status.equalsIgnoreCase(Constants.SEAT_SELECTED)) {
                        mMovieSeat.status = Constants.SEAT_AVAILALBLE;
                        setProcessSeat(mMovieSeat.seat, mMovieSeat.status, mMovieSeat);
                    } else {
                        if (mMaxSeat > 0) {
                            mSelectedSeatCount = 0;
                            for (int i = 0; i < mMovieSeatList.size(); i++) {
                                if (mMovieSeatList.get(i).status.equals(Constants.SEAT_SELECTED)) {
                                    mSelectedSeatCount++;
                                }
                            }

                            if (mSelectedSeatCount < mMaxSeat) {
                                mMovieSeat.status = Constants.SEAT_SELECTED;
                                setProcessSeat(mMovieSeat.seat, mMovieSeat.status, mMovieSeat);
                            } else {
                                Utils.showToast(getActivity(), getString(R.string.alert_max_seat));
                            }

                        } else {
                            Utils.showToast(getActivity(), getString(R.string.alert_max_seat));
//                            mMovieSeat.status = Constants.SEAT_SELECTED;
//                            setProcessSeat(mMovieSeat.seat, mMovieSeat.status, mMovieSeat);
                        }
                    }
                    mMovieSeatAdapter.notifyDataSetChanged();


                    setSeatAndPrice();

                }

            }
        });
        gvSeat.setAdapter(mMovieSeatAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {

            mUserKey = bundle.getString(Constants.BNDL_USER_KEY);
            mMaxSeat = bundle.getInt(Constants.BNDL_MAX_SEAT);
            mSelectedDate = bundle.getString(Constants.BNDL_MOVIE_DATE_ID);
            mSelectedScreenId = bundle.getString(Constants.BNDL_MOVIE_CINEMA_ID);
            mSelectedTimeId = bundle.getString(Constants.BNDL_MOVIE_TIME_ID);
            mMovie = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE), MovieDetailsPojo.Movie.class);
            if (mMovie != null) {
                mMovieID = mMovie.movieId;
                if (!TextUtils.isEmpty(mMovie.name)) {
                    tvTitle.setText(mMovie.name);
                } else {
                    tvTitle.setText("");
                }
            }
            mMovieDateList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_DATE_LIST), new TypeToken<List<MovieDetailsPojo.MovieDate>>() {
            }.getType());
            mMovieTheaterList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_CINEMA_LIST), new TypeToken<List<MovieTheaterListPojo.Theater>>() {
            }.getType());
            mMovieTimeList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_TIME_LIST), new TypeToken<List<MovieTheaterListPojo.ScreenTime>>() {
            }.getType());


            if (!TextUtils.isEmpty(mSelectedDate)) {

                tvDate.setText(mSelectedDate);
                try {
                    Date mDate = DateFormate.sdfMovieDate.parse(mSelectedDate);
                    String mDay = DateFormate.sdfMovieDay.format(mDate);
                    String mDigit = DateFormate.sdfDate.format(mDate);
                    String mMonth = DateFormate.sdfMonth.format(mDate);

                    if (!TextUtils.isEmpty(mDigit)) {
                        String mDateFormate = mDigit + Utils.getDatePostfix(Integer.parseInt(mDigit)) + " " + mMonth;
                        if (!TextUtils.isEmpty(mDateFormate)) {
                            tvDate.setText("" + mDateFormate);
                        }
                    }

                    if (!TextUtils.isEmpty(mDay)) {
                        tvDay.setText(mDay);
                    } else {
                        tvDay.setText("");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            for (int i = 0; i < mMovieTheaterList.size(); i++) {
                if (mMovieTheaterList.get(i).screenId.equalsIgnoreCase(mSelectedScreenId)) {
                    tvCinema.setText(mMovieTheaterList.get(i).screenName);

                    break;
                }
            }

            MovieTimeInnerAdapter mMovieTimeAdapter = new MovieTimeInnerAdapter(getActivity(), new MovieTimeInnerAdapter.OnMovieTimeSelected() {
                @Override
                public void onMovieTimeSelected(int mScreenTimePos, MovieTheaterListPojo.ScreenTime mScreenTime) {
//                    onMovieScreenTimeSelected.onMovieScreenTimeSelected(i, mScreenTimePos, mScreenTime);
                    if (mMovieTimeList != null && mMovieTimeList.size() > 0) {

                        mSelectedTimeId = mMovieTimeList.get(mScreenTimePos).mtid;
                        mvScreen = mMovieTimeList.get(mScreenTimePos).mvScreen;
                        mcinemaScreenName = mMovieTimeList.get(mScreenTimePos).cinemaScreenName;
                        mSeatPrice = mMovieTimeList.get(mScreenTimePos).price;
                        mCurrency = mMovieTimeList.get(mScreenTimePos).currency;

                        tvScreen.setText(" " + mcinemaScreenName);
//                        getBookedSeat();
                        deleteProcessedSeat();
                        getMovieSeatList();
                    }
                }
            });
            mMovieTimeAdapter.addAll(mMovieTimeList);
            rvTimeList.setAdapter(mMovieTimeAdapter);

            for (int i = 0; i < mMovieTimeList.size(); i++) {
//                tlDots.addTab(tlDots.newTab().setText(mMovieTimeList.get(i).vdt));
                if (mMovieTimeList.get(i).mtid.equals(mSelectedTimeId)) {
//                    tlDots.setScrollPosition(i, 0f, true);
                    mvScreen = mMovieTimeList.get(i).mvScreen;
                    mSeatPrice = mMovieTimeList.get(i).price;
                    mCurrency = mMovieTimeList.get(i).currency;
                    mcinemaScreenName = mMovieTimeList.get(i).cinemaScreenName;
                    LogUtils.e("", mSelectedTimeId + " " + mvScreen + " " + mSeatPrice + " " + mCurrency);

                    tvScreen.setText(" " + mcinemaScreenName);
                }
            }

            setSeat(mSelectedSeatCount);
            tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mSelectedSeatCount * mSeatPrice));

//            if (mMaxSeat < 1) {
//                showTotalSeatDialog();
//            }
            getMovieSeatList();
        }

//        tlDots.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                if (mMovieTimeList != null && mMovieTimeList.size() > 0) {
//                    mSelectedTimeId = mMovieTimeList.get(position).mtid;
//                    mSeatPrice = mMovieTimeList.get(position).price;
//                    mCurrency = mMovieTimeList.get(position).currency;
//                    getBookedSeat();
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        tvBack.setOnClickListener(this);
        lnrBook.setOnClickListener(this);
    }

    private void setSeatAndPrice() {

        mSelectedSeatCount = 0;
        for (int i = 0; i < mMovieSeatList.size(); i++) {
            if (mMovieSeatList.get(i).status.equals(Constants.SEAT_SELECTED)) {
                mSelectedSeatCount++;
            }
        }
        setSeat(mSelectedSeatCount);
        tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mSelectedSeatCount * mSeatPrice));
    }

    private void setSeat(int mSelectedSeatCount) {
        if (mSelectedSeatCount > 1) {
            tvSeat.setText(mSelectedSeatCount + " " + getString(R.string.seats_bracket));
        } else {
            tvSeat.setText(mSelectedSeatCount + " " + getString(R.string.seat));
        }
    }

    private void getMovieSeatList() {

        if (getActivity() == null){
            return;
        }

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

//        mvScreen = "12";

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<MovieSeatListPojo> mCall = mApiInterface.getMovieSeatsList(mvScreen, Otapp.mUniqueID);
        mCall.enqueue(new Callback<MovieSeatListPojo>() {
            @Override
            public void onResponse(Call<MovieSeatListPojo> call, Response<MovieSeatListPojo> response) {

                if (response.isSuccessful()) {
                    MovieSeatListPojo mMovieSeatListPojo = response.body();
                    if (mMovieSeatListPojo != null) {
                        if (mMovieSeatListPojo.status.equalsIgnoreCase("200")) {

                            if (!mMovie.isTermsAccepted && mMovieSeatListPojo.termsCondition != null && mMovieSeatListPojo.termsCondition.size() > 0) {
                                showMovieTermsDialog(mMovieSeatListPojo.termsCondition);
                            }

                            if (!TextUtils.isEmpty(mMovieSeatListPojo.screens.maxSeatsPerRow)) {
                                mSeatsCount = Integer.parseInt(mMovieSeatListPojo.screens.maxSeatsPerRow) + INDEX_COUNT;
                                gvSeat.setNumColumns(mSeatsCount);
                                int fontSize = (int) getResources().getDimension(R.dimen._10sdp);
                                if (mSeatsCount > 22) {
                                    gvSeat.setVerticalSpacing((int) getResources().getDimension(R.dimen._1sdp));
                                    gvSeat.setHorizontalSpacing((int) getResources().getDimension(R.dimen._1sdp));
                                    fontSize = (int) getResources().getDimension(R.dimen._6sdp);
                                } else if (mSeatsCount > 19) {
                                    gvSeat.setVerticalSpacing((int) getResources().getDimension(R.dimen._2sdp));
                                    gvSeat.setHorizontalSpacing((int) getResources().getDimension(R.dimen._2sdp));
                                    fontSize = (int) getResources().getDimension(R.dimen._7sdp);
                                } else if (mSeatsCount > 16) {
                                    gvSeat.setVerticalSpacing((int) getResources().getDimension(R.dimen._3sdp));
                                    gvSeat.setHorizontalSpacing((int) getResources().getDimension(R.dimen._3sdp));
                                    fontSize = (int) getResources().getDimension(R.dimen._8sdp);
                                } else if (mSeatsCount > 13) {
                                    gvSeat.setVerticalSpacing((int) getResources().getDimension(R.dimen._4sdp));
                                    gvSeat.setHorizontalSpacing((int) getResources().getDimension(R.dimen._4sdp));
                                    fontSize = (int) getResources().getDimension(R.dimen._9sdp);
                                } else if (mSeatsCount > 10) {
                                    gvSeat.setVerticalSpacing((int) getResources().getDimension(R.dimen._5sdp));
                                    gvSeat.setHorizontalSpacing((int) getResources().getDimension(R.dimen._5sdp));
                                    fontSize = (int) getResources().getDimension(R.dimen._10sdp);
                                }

                                mMovieSeatAdapter.setfontSize(fontSize);

                                List<MovieSeatListPojo.ScreenInfo> mScreenInfoList = mMovieSeatListPojo.screens.screenInfo;

                                if (mMovieSeatList != null && mMovieSeatList.size() > 0) {
                                    mMovieSeatList.clear();
                                }

                                for (int i = 0; i < mScreenInfoList.size(); i++) {

                                    String screenRow = mScreenInfoList.get(i).screenRow;
                                    String screenRowSeats = mScreenInfoList.get(i).screenRowSeats;
                                    LogUtils.e("", i + " screenRowSeats::" + screenRowSeats + " length::" + screenRowSeats.split(",", -1).length);
                                    List<String> mRowSeatList = Arrays.asList(screenRowSeats.split(",", -1));
                                    LogUtils.e("", i + " mRowSeatList::" + mRowSeatList);

                                    for (int j = 0; j < mSeatsCount; j++) {
                                        MovieSeat mMovieSeat = new MovieSeat();
                                        mMovieSeat.seat = "";
                                        mMovieSeat.status = Constants.SEAT_NONE;
                                        if (j == 0) {
                                            mMovieSeat.seat = "" + screenRow;
                                            mMovieSeat.status = Constants.SEAT_NONE;
                                        } else if (j == (mSeatsCount - 1)) {
                                            mMovieSeat.seat = "" + screenRow;
                                            mMovieSeat.status = Constants.SEAT_NONE;
                                        } else {
                                            String mSeat = mRowSeatList.get(j - 1);
                                            mMovieSeat.seat = mSeat;
                                            if (!TextUtils.isEmpty(mSeat)) {
                                                mMovieSeat.status = Constants.SEAT_AVAILALBLE;
                                            } else {
                                                mMovieSeat.status = Constants.SEAT_NONE;
                                            }
//                                            if (mRowSeatList.contains(screenRow + j)) {
//                                                mMovieSeat.seat = screenRow + "" + j;
//                                                mMovieSeat.status = Constants.SEAT_AVAILALBLE;
//                                            }
                                        }
                                        LogUtils.e("", j + " " + mMovieSeat.seat);
                                        mMovieSeatList.add(mMovieSeat);
                                    }
                                }

                                mMovieSeatAdapter.addAll(mMovieSeatList);

                                getBookedSeat();

                                setSeatAndPrice();

                            } else {
                                Utils.closeProgressDialog();
                            }

                        } else {
                            Utils.closeProgressDialog();
                            Utils.showToast(getActivity(), "" + mMovieSeatListPojo.message);
                        }
                    } else {
                        Utils.closeProgressDialog();
                    }
                } else {
                    Utils.closeProgressDialog();
                }

            }

            @Override
            public void onFailure(Call<MovieSeatListPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void setProcessSeat(String seat, final String status, final MovieSeat mMovieSeat) {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<SeatProcessedPojo> mCall = mApiInterface.setProcessedSeats(mSelectedTimeId, seat, mUserKey, Otapp.mUniqueID);
        mCall.enqueue(new Callback<SeatProcessedPojo>() {
            @Override
            public void onResponse(Call<SeatProcessedPojo> call, Response<SeatProcessedPojo> response) {
                try {
                    JSONObject jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                    Log.d("Log", "Response Process Seats : " + jsonObjectResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    if (isRunning) {
                        SeatProcessedPojo mSeatProcessedPojo = response.body();
                        if (mSeatProcessedPojo != null) {
                            if (mSeatProcessedPojo.status.equals("200")) {
                                for (int i = 0; i < mMovieSeatList.size(); i++) {
                                    if (mMovieSeatList.get(i).seat.equals(mMovieSeat.seat)) {
                                        mMovieSeatList.get(i).status = Constants.SEAT_SELECTED;
                                        i = mMovieSeatList.size();
                                    }
                                }
                                mMovieSeatAdapter.notifyDataSetChanged();
                                setSeat(mSelectedSeatCount);
                                tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mSelectedSeatCount * mSeatPrice));
                            } else if (mSeatProcessedPojo.status.equals("201")) {
                                for (int i = 0; i < mMovieSeatList.size(); i++) {
                                    if (mMovieSeatList.get(i).seat.equals(mMovieSeat.seat)) {
                                        mMovieSeatList.get(i).status = Constants.SEAT_PROCESSED;
                                        i = mMovieSeatList.size();
                                        mSelectedSeatCount--;
                                    }
                                }
                                mMovieSeatAdapter.notifyDataSetChanged();
                                setSeat(mSelectedSeatCount);
                                tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mSelectedSeatCount * mSeatPrice));
                            } else if (mSeatProcessedPojo.status.equals("203")) {
                                for (int i = 0; i < mMovieSeatList.size(); i++) {
                                    if (mMovieSeatList.get(i).seat.equals(mMovieSeat.seat)) {
                                        mMovieSeatList.get(i).status = Constants.SEAT_AVAILALBLE;
                                        i = mMovieSeatList.size();
                                    }
                                }
                                mMovieSeatAdapter.notifyDataSetChanged();
                                setSeat(mSelectedSeatCount);
                                tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mSelectedSeatCount * mSeatPrice));
                            } else if (mSeatProcessedPojo.status.equals("404")) {
                                if (status == Constants.SEAT_AVAILALBLE) {
                                    mMovieSeat.status = Constants.SEAT_SELECTED;
                                } else if (status == Constants.SEAT_SELECTED) {
                                    mMovieSeat.status = Constants.SEAT_AVAILALBLE;
                                }
//                                Utils.showToast(getActivity(), "" + mSeatProcessedPojo.status);

                                mMovieSeatAdapter.notifyDataSetChanged();

                                mSelectedSeatCount = 0;
                                for (int i = 0; i < mMovieSeatList.size(); i++) {
                                    if (mMovieSeatList.get(i).status.equals(Constants.SEAT_SELECTED)) {
                                        mSelectedSeatCount++;
                                    }
                                }
                                setSeat(mSelectedSeatCount);
                                tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mSelectedSeatCount * mSeatPrice));
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<SeatProcessedPojo> call, Throwable t) {

            }
        });

    }


    private void getBookedSeat() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showProgressDialog(getActivity());
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<SeatProcessedPojo> mCall = mApiInterface.getProcessedSeatsList(mSelectedTimeId, mUserKey, Otapp.mUniqueID);
        mCall.enqueue(new Callback<SeatProcessedPojo>() {
            @Override
            public void onResponse(Call<SeatProcessedPojo> call, Response<SeatProcessedPojo> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {

                    SeatProcessedPojo mSeatProcessedPojo = response.body();
                    if (mSeatProcessedPojo != null) {
                        if (mSeatProcessedPojo.status.equalsIgnoreCase("200")) {
//                            try {
//                                JSONObject jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
//                                Log.d("Log", "Response Bus List : " + jsonObjectResponse);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }

                            List<String> mProcessedSeatList = new ArrayList<>();
                            if (!TextUtils.isEmpty(mSeatProcessedPojo.processSeats)) {
                                if (mSeatProcessedPojo.processSeats.contains(",")) {
                                    mProcessedSeatList.addAll(Arrays.asList(mSeatProcessedPojo.processSeats.split(",")));
                                } else {
                                    mProcessedSeatList.add(mSeatProcessedPojo.processSeats);
                                }
                            }

                            List<String> mBookedSeatList = new ArrayList<>();
                            if (!TextUtils.isEmpty(mSeatProcessedPojo.bookedSeats)) {
                                if (mSeatProcessedPojo.bookedSeats.contains(",")) {
                                    mBookedSeatList.addAll(Arrays.asList(mSeatProcessedPojo.bookedSeats.split(",")));
                                } else {
                                    mBookedSeatList.add(mSeatProcessedPojo.bookedSeats);
                                }
                            }


                            for (int i = 0; i < mMovieSeatList.size(); i++) {

                                if (mBookedSeatList.contains(mMovieSeatList.get(i).seat) || mProcessedSeatList.contains(mMovieSeatList.get(i).seat)) {

                                    if (mProcessedSeatList.contains(mMovieSeatList.get(i).seat)) {
                                        mMovieSeatList.get(i).status = Constants.SEAT_PROCESSED;
                                    }

                                    if (mBookedSeatList.contains(mMovieSeatList.get(i).seat)) {
                                        mMovieSeatList.get(i).status = Constants.SEAT_BOOKED;
                                    }


                                } else if (mMovieSeatList.get(i).seat.length() > 1) {
                                    mMovieSeatList.get(i).status = Constants.SEAT_AVAILALBLE;
                                }

//                                if (mProcessedSeatList.contains(mMovieSeatList.get(i).seat)) {
//                                    mMovieSeatList.get(i).status = Constants.SEAT_PROCESSED;
//                                } else if (mMovieSeatList.get(i).seat.length() > 1) {
//                                    mMovieSeatList.get(i).status = Constants.SEAT_AVAILALBLE;
//                                }
                            }

//                            if (TextUtils.isEmpty(mSeatProcessedPojo.bookedSeats) && TextUtils.isEmpty(mSeatProcessedPojo.bookedSeats)) {
//                                mSeatsCount
//                            } else {
                            mMovieSeatAdapter.addAll(mMovieSeatList);
//                            }


                        } else {
                            Utils.showToast(getActivity(), "" + mSeatProcessedPojo.message);
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<SeatProcessedPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });


    }

    public void deleteProcessedSeat() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<SeatProcessedPojo> mCall = mApiInterface.deleteProcessedSeatsList(mUserKey, Otapp.mUniqueID);
        mCall.enqueue(new Callback<SeatProcessedPojo>() {
            @Override
            public void onResponse(Call<SeatProcessedPojo> call, Response<SeatProcessedPojo> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<SeatProcessedPojo> call, Throwable t) {
            }
        });

    }

    private void showMovieTermsDialog(List<String> termsCondition) {

        try {

            if (getActivity() == null) {
                termsDialog = new Dialog(getContext());
            } else {
                termsDialog = new Dialog(getActivity());
            }

            termsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            termsDialog.setContentView(R.layout.dialog_movie_terms);
            termsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            ListView lvTerms = (ListView) termsDialog.findViewById(R.id.lvTerms);
            TextView tvAccept = (TextView) termsDialog.findViewById(R.id.tvAccept);
            final TextView tvDecline = (TextView) termsDialog.findViewById(R.id.tvDecline);

            final MovieTermsAdapter mMovieTermsAdapter = new MovieTermsAdapter(getActivity());
            lvTerms.setAdapter(mMovieTermsAdapter);

            mMovieTermsAdapter.addAll(termsCondition);

            tvAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    mMovie.isTermsAccepted = true;

                    if (termsDialog != null && termsDialog.isShowing()) {
                        termsDialog.dismiss();
                        termsDialog.cancel();
                    }

                }
            });

            tvDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (termsDialog != null && termsDialog.isShowing()) {
                        termsDialog.dismiss();
                        termsDialog.cancel();
                    }

                    popBackStack();

                }
            });

            termsDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });

            termsDialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (termsDialog != null) {
            termsDialog.dismiss();
            termsDialog.cancel();
        }
    }

    //    private void showTotalSeatDialog() {
//
//        final Dialog termsDialog = new Dialog(getActivity());
//        termsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        termsDialog.setContentView(R.layout.dialog_movie_seat);
//        termsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        GridView gvTotalSeat = (GridView) termsDialog.findViewById(R.id.gvTotalSeat);
//        final TextView tvPrice = (TextView) termsDialog.findViewById(R.id.tvPrice);
//        TextView tvBookNow = (TextView) termsDialog.findViewById(R.id.tvBookNow);
//        final TextView tvCancel = (TextView) termsDialog.findViewById(R.id.tvCancel);
//
//
//        final ArrayList<TotalSeat> mTotalSeatList = new ArrayList<>();
//        for (int i = 1; i <= 20; i++) {
//            TotalSeat mTotalSeat = new TotalSeat();
//            mTotalSeat.seat = i;
//            if (i == 1) {
//                mTotalSeat.isSelected = true;
//                mMaxSeat = 1;
//            } else {
//                mTotalSeat.isSelected = false;
//            }
//
//            mTotalSeatList.add(mTotalSeat);
//        }
//
//        tvPrice.setText(mCurrency + " " + (mMaxSeat * mSeatPrice));
//
//        final TotalSeatAdapter mTotalSeatAdapter = new TotalSeatAdapter(getActivity());
//        mTotalSeatAdapter.addAll(mTotalSeatList);
//        gvTotalSeat.setAdapter(mTotalSeatAdapter);
//
//        mTotalSeatAdapter.setListener(new TotalSeatAdapter.OnMovieSeatClickListener() {
//            @Override
//            public void onMoieSelected(int position) {
//                for (int i = 0; i < mTotalSeatList.size(); i++) {
//                    if (i == position) {
//                        mTotalSeatList.get(i).isSelected = true;
//                    } else {
//                        mTotalSeatList.get(i).isSelected = false;
//                    }
//
//                }
//                mTotalSeatAdapter.addAll(mTotalSeatList);
//                mMaxSeat = mTotalSeatList.get(position).seat;
//                tvPrice.setText(mCurrency + " " + (mMaxSeat * mSeatPrice));
//            }
//        });
//
//
//        tvBookNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mMaxSeat < 1) {
//                    Utils.showToast(getActivity(), getString(R.string.alert_select_total_ticket));
//                    return;
//                }
//
//                if (termsDialog != null && termsDialog.isShowing()) {
//                    termsDialog.dismiss();
//                    termsDialog.cancel();
//                }
//
//            }
//        });
//
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                popBackStack();
//
//                if (termsDialog != null && termsDialog.isShowing()) {
//                    termsDialog.dismiss();
//                    termsDialog.cancel();
//                }
//
//            }
//        });
//
//        termsDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface termsDialog, int keyCode, KeyEvent event) {
//                return keyCode == KeyEvent.KEYCODE_BACK;
//            }
//        });
//
//        termsDialog.show();
//
//    }

    int glassQuality;

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            deleteProcessedSeat();
            popBackStack();
        } else if (view == lnrBook) {

            mSelectedSeatCount = 0;
            for (int i = 0; i < mMovieSeatList.size(); i++) {
                if (mMovieSeatList.get(i).status.equals(Constants.SEAT_SELECTED)) {
                    mSelectedSeatCount++;
                }
            }

            if (mSelectedSeatCount > 0) {

                if (mSelectedSeatCount != mMaxSeat) {
                    Utils.showToast(getActivity(), getString(R.string.alert_remaining_seat));
                    return;
                }

//                if (!TextUtils.isEmpty(mMovie.movieFormat) && mMovie.movieFormat.contains("3D")) {
//
//                    glassQuality = 0;
//
//                    final Dialog dialog = new Dialog(getActivity());
//                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.dialog_movie_3d_glasses);
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                    TextView tv3dText = (TextView) dialog.findViewById(R.id.tv3dText);
//                    final TextView tvAdd = (TextView) dialog.findViewById(R.id.tvAdd);
//                    final RelativeLayout rltQuantity = (RelativeLayout) dialog.findViewById(R.id.rltQuantity);
//                    final TextView tvMinus = (TextView) dialog.findViewById(R.id.tvMinus);
//                    final TextView tvQuantity = (TextView) dialog.findViewById(R.id.tvQuantity);
//                    final TextView tvPlus = (TextView) dialog.findViewById(R.id.tvPlus);
//                    final TextView tvSkip = (TextView) dialog.findViewById(R.id.tvSkip);
//                    TextView tvContinue = (TextView) dialog.findViewById(R.id.tvContinue);
//
//
//                    Spannable wordtoSpan = new SpannableString(getString(R.string.msg_3d_text));
//                    wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_glasses)), 20, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_glasses)), 48, 51, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    tv3dText.setText(wordtoSpan);
//
//                    tvAdd.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            rltQuantity.setVisibility(View.VISIBLE);
//                            tvAdd.setVisibility(View.GONE);
//                            glassQuality = 1;
//                        }
//                    });
//
//                    tvMinus.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            if (glassQuality > 1) {
//
//                                glassQuality--;
//                                tvQuantity.setText("" + glassQuality);
//
//                            } else {
//                                glassQuality = 0;
//                                rltQuantity.setVisibility(View.GONE);
//                                tvAdd.setVisibility(View.VISIBLE);
//                            }
//
//
//                        }
//                    });
//
//                    tvPlus.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            if (glassQuality < mMaxSeat) {
//                                glassQuality++;
//                                tvQuantity.setText("" + glassQuality);
//                            } else {
//                                Utils.showToast(getActivity(), getString(R.string.alert_limit_glasses));
//                            }
//                        }
//                    });
//
//                    tvSkip.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            if (dialog != null) {
//                                dialog.dismiss();
//                                dialog.cancel();
//                            }
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString(Constants.BNDL_MOVIE, new Gson().toJson(mMovie));
//                            bundle.putString(Constants.BNDL_USER_KEY, mUserKey);
//                            bundle.putString(Constants.BNDL_MOVIE_DATE_ID, mSelectedDate);
//                            bundle.putString(Constants.BNDL_MOVIE_DATE_LIST, new Gson().toJson(mMovieDateList));
//                            bundle.putString(Constants.BNDL_MOVIE_CINEMA_ID, mSelectedScreenId);
//                            bundle.putString(Constants.BNDL_MOVIE_CINEMA_LIST, new Gson().toJson(mMovieTheaterList));
//                            bundle.putString(Constants.BNDL_MOVIE_TIME_ID, mSelectedTimeId);
//                            bundle.putString(Constants.BNDL_MOVIE_TIME_LIST, new Gson().toJson(mMovieTimeList));
//                            bundle.putString(Constants.BNDL_MOVIE_SEAT_LIST, new Gson().toJson(mMovieSeatList));
//                            switchFragment(MovieFoodFragment.newInstance(), MovieFoodFragment.Tag_MovieFoodFragment, bundle);
//
//                        }
//                    });
//
//                    tvContinue.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            if (glassQuality < 1) {
//                                Utils.showToast(getActivity(), getString(R.string.alert_add_glasses));
//                                return;
//                            }
//
//                            if (dialog != null) {
//                                dialog.dismiss();
//                                dialog.cancel();
//                            }
//
//                            mMovie.glassesQuantity = glassQuality;
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString(Constants.BNDL_MOVIE, new Gson().toJson(mMovie));
//                            bundle.putString(Constants.BNDL_USER_KEY, mUserKey);
//                            bundle.putString(Constants.BNDL_MOVIE_DATE_ID, mSelectedDate);
//                            bundle.putString(Constants.BNDL_MOVIE_DATE_LIST, new Gson().toJson(mMovieDateList));
//                            bundle.putString(Constants.BNDL_MOVIE_CINEMA_ID, mSelectedScreenId);
//                            bundle.putString(Constants.BNDL_MOVIE_CINEMA_LIST, new Gson().toJson(mMovieTheaterList));
//                            bundle.putString(Constants.BNDL_MOVIE_TIME_ID, mSelectedTimeId);
//                            bundle.putString(Constants.BNDL_MOVIE_TIME_LIST, new Gson().toJson(mMovieTimeList));
//                            bundle.putString(Constants.BNDL_MOVIE_SEAT_LIST, new Gson().toJson(mMovieSeatList));
//                            switchFragment(MovieFoodFragment.newInstance(), MovieFoodFragment.Tag_MovieFoodFragment, bundle);
//
//                        }
//                    });
//
//                    dialog.show();
//
//                } else {

                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_MOVIE, new Gson().toJson(mMovie));
                bundle.putString(Constants.BNDL_USER_KEY, mUserKey);
                bundle.putString(Constants.BNDL_MOVIE_DATE_ID, mSelectedDate);
                bundle.putString(Constants.BNDL_MOVIE_DATE_LIST, new Gson().toJson(mMovieDateList));
                bundle.putString(Constants.BNDL_MOVIE_CINEMA_ID, mSelectedScreenId);
                bundle.putString(Constants.BNDL_MOVIE_CINEMA_LIST, new Gson().toJson(mMovieTheaterList));
                bundle.putString(Constants.BNDL_MOVIE_TIME_ID, mSelectedTimeId);
                bundle.putString(Constants.BNDL_MOVIE_TIME_LIST, new Gson().toJson(mMovieTimeList));
                bundle.putString(Constants.BNDL_MOVIE_SEAT_LIST, new Gson().toJson(mMovieSeatList));
                switchFragment(MovieFoodFragment.newInstance(), MovieFoodFragment.Tag_MovieFoodFragment, bundle);

//                }

            } else {
                Utils.showToast(getActivity(), getString(R.string.alert_select_seat));
            }
        }
    }
}
