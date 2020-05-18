package com.otapp.net.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.TrailerPlayerActivity;
import com.otapp.net.adapter.MovieAdvertiseAdapter;
import com.otapp.net.adapter.MovieCinemaAdapter;
import com.otapp.net.adapter.MovieDateAdapter;
import com.otapp.net.adapter.TotalSeatAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.MovieDetailsPojo;
import com.otapp.net.model.MovieTheaterListPojo;
import com.otapp.net.model.TotalSeat;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;
import com.otapp.net.view.AutoScrollViewPager;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDateSelectionFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_MovieDateSelectionFragment = "Tag_" + "MovieDateSelectionFragment";

    View mView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvDateTime)
    TextView tvDateTime;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;
    @BindView(R.id.tvGrade)
    TextView tvGrade;
    @BindView(R.id.tvPlay)
    TextView tvPlay;
    @BindView(R.id.lnrDetails)
    LinearLayout lnrDetails;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.vpAdvertise)
    AutoScrollViewPager vpAdvertise;
    @BindView(R.id.tlDots)
    TabLayout tlDots;
    @BindView(R.id.rvDate)
    RecyclerView rvDate;
    @BindView(R.id.lvTheater)
    ListView lvTheater;

    MovieCinemaAdapter mMovieCinemaAdapter;

    private String mSelectedDate = "", mMovieID = "", mUserKey = "", mSelectedScreenId = "", mSelectedTimeId = "";
    private int mMaxSeat;

    private MovieDetailsPojo.Movie mMovie;
    List<MovieDetailsPojo.MovieDate> mMovieDateList;
    List<MovieTheaterListPojo.Theater> mMovieTheaterList;
    List<MovieTheaterListPojo.ScreenTime> mMovieTimeList;

    MovieDateAdapter mMovieDateAdapter;

    public static MovieDateSelectionFragment newInstance() {
        MovieDateSelectionFragment fragment = new MovieDateSelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_movie_date_selection, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        mMovieCinemaAdapter = new MovieCinemaAdapter(getActivity(), new MovieCinemaAdapter.OnMovieScreenTimeSelected() {
            @Override
            public void onMovieScreenTimeSelected(int mTheaterPos, int mScreenTimePos, MovieTheaterListPojo.ScreenTime mScreenTime) {

                LogUtils.e("", "mTheaterPos::" + mTheaterPos + " mScreenTimePos::" + mScreenTimePos);

                mSelectedScreenId = mMovieTheaterList.get(mTheaterPos).screenId;
                mMovieTimeList = mMovieTheaterList.get(mTheaterPos).screenTime;
                mSelectedTimeId = mScreenTime.mtid;

                if (mMovieTheaterList != null && mMovieTheaterList.size() > 0) {
                    for (int i = 0; i < mMovieTheaterList.size(); i++) {
                        MovieTheaterListPojo.Theater mTheater = mMovieTheaterList.get(i);
                        if (mTheater != null && mTheater.screenTime != null && mTheater.screenTime.size() > 0) {
                            for (int j = 0; j < mTheater.screenTime.size(); j++) {
                                if (mScreenTime.mtid.equalsIgnoreCase(mTheater.screenTime.get(j).mtid)) {
                                    mTheater.screenTime.get(j).isSelected = true;
                                } else {
                                    mTheater.screenTime.get(j).isSelected = false;
                                }
                            }

                        }
                    }
                    setMovieTheaterList();
                }

                showTotalSeatDialog(mScreenTime, mScreenTime.seat_stauts, mScreenTime.available_seats);


            }
        });

        lvTheater.setAdapter(mMovieCinemaAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {

            mMovie = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE), MovieDetailsPojo.Movie.class);
            if (mMovie != null) {
                mMovieID = mMovie.movieId;
                if (!TextUtils.isEmpty(mMovie.name)) {
                    tvTitle.setText(mMovie.name);
                } else {
                    tvTitle.setText("");
                }

                if (!TextUtils.isEmpty(mMovie.movieRestriction)) {
                    tvGrade.setText(mMovie.movieRestriction);
                } else {
                    tvGrade.setText("");
                }

                StringBuilder strGenre = new StringBuilder();

                if (!TextUtils.isEmpty(mMovie.movieDuration)) {
                    try {
                        Date mDateTime = DateFormate.sdf24Time.parse(mMovie.movieDuration);
                        String mMovieTimeFormate = DateFormate.sdfDuration.format(mDateTime);
                        if (!TextUtils.isEmpty(mMovieTimeFormate)) {
                            strGenre.append(mMovieTimeFormate);
                        } else {
                            strGenre.append(mMovie.movieDuration);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        strGenre.append(mMovie.movieDuration);
                    }
                }

                strGenre.append("  |  " + mMovie.movieReleaseDate);
                strGenre.append("  |  " + mMovie.movieGenre);

                if (strGenre != null && strGenre.length() > 0) {
                    tvDateTime.setText(strGenre.toString());
                } else {
                    tvDateTime.setText("");
                }

                tvLanguage.setText("" + mMovie.language);

                if (!TextUtils.isEmpty(mMovie.image)) {
                    Picasso.get().load(mMovie.image).into(ivPhoto);
                }

                mMovieDateList = mMovie.movieDates;
            }

            if (mMovieDateList != null && mMovieDateList.size() > 0) {

                mSelectedDate = mMovieDateList.get(0).dt;
                mMovieDateList.get(0).isSelected = true;

                mMovieDateAdapter = new MovieDateAdapter(getActivity(), mMovieDateList, new MovieDateAdapter.OnMovieDateClickListener() {
                    @Override
                    public void onMovieDateClicked(String date) {
                        mSelectedDate = date;
                        getTheaterList();
                    }
                });

                rvDate.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                rvDate.setAdapter(mMovieDateAdapter);

                getTheaterList();
            }
        }

        tvBack.setOnClickListener(this);
        tvPlay.setOnClickListener(this);


        if (Otapp.mAdsPojoList != null && Otapp.mAdsPojoList.size() > 0) {
            setMovieAdvertise();
        } else {
            getCountryCodeList();
        }


    }

    private void setMovieAdvertise() {
        List<CountryCodePojo.Ad5> mAdvertiseImages = new ArrayList<>();
        if (Otapp.mAdsPojoList != null && Otapp.mAdsPojoList.size() > 0) {
            for (int i = 0; i < Otapp.mAdsPojoList.size(); i++) {
                if (Otapp.mAdsPojoList.get(i).page.equalsIgnoreCase("Movie show time page") && Otapp.mAdsPojoList.get(i).location.startsWith("Top Slider")) {
                    mAdvertiseImages.add(Otapp.mAdsPojoList.get(i));
                }
            }
        }

        if (mAdvertiseImages != null && mAdvertiseImages.size() > 0) {
            MovieAdvertiseAdapter mMovieAdvertiseAdapter = new MovieAdvertiseAdapter(getActivity(), mAdvertiseImages, this);
            vpAdvertise.setAdapter(mMovieAdvertiseAdapter);
            vpAdvertise.setCurrentItem(0);
            vpAdvertise.startAutoScroll();
//                    tlDots.setupWithViewPager(vpFeatured, true);

            ivPhoto.setVisibility(View.GONE);
            tvPlay.setVisibility(View.GONE);
            vpAdvertise.setVisibility(View.VISIBLE);
            tlDots.setVisibility(View.VISIBLE);

        } else {

            ivPhoto.setVisibility(View.VISIBLE);
            tvPlay.setVisibility(View.VISIBLE);
            vpAdvertise.setVisibility(View.GONE);
            tlDots.setVisibility(View.GONE);
        }
    }

//    private void getAdvertiseList() {
//        if (!Utils.isInternetConnected(getActivity())) {
//            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
//            return;
//        }
//
//        ApiInterface mApiInterface = RestClient.getClient(true);
//        Call<MovieAdvertiseResponse> mCall = mApiInterface.getMovieAdvertiseList(Otapp.mUniqueID);
//        mCall.enqueue(new Callback<MovieAdvertiseResponse>() {
//            @Override
//            public void onResponse(Call<MovieAdvertiseResponse> call, Response<MovieAdvertiseResponse> response) {
//
//                if (response.isSuccessful()) {
//                    MovieAdvertiseResponse mMovieAdvertiseResponse = response.body();
//
//                    if (mMovieAdvertiseResponse != null) {
//                        if (mMovieAdvertiseResponse.status.equalsIgnoreCase("200")) {
//
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieAdvertiseResponse> call, Throwable t) {
//
//            }
//        });
//    }

    private void getTheaterList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<MovieTheaterListPojo> mCall = mApiInterface.getMovieTheaterList(mMovieID, mSelectedDate, Otapp.mUniqueID);
        mCall.enqueue(new Callback<MovieTheaterListPojo>() {
            @Override
            public void onResponse(Call<MovieTheaterListPojo> call, Response<MovieTheaterListPojo> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    MovieTheaterListPojo mMovieTheaterListPojo = response.body();
                    if (mMovieTheaterListPojo != null) {
                        if (mMovieTheaterListPojo.status.equalsIgnoreCase("200")) {
                            mMovieTheaterList = mMovieTheaterListPojo.theaters;
                            mUserKey = mMovieTheaterListPojo.userKey;
                        } else {
                            mMovieTheaterList = null;
                        }
                        setMovieTheaterList();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieTheaterListPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });
    }

    private void setMovieTheaterList() {

        mMovieCinemaAdapter.addAll(mMovieTheaterList);

        if (mMovieTheaterList == null || mMovieTheaterList.size() == 0) {
            Utils.showToast(getActivity(), getString(R.string.alert_no_screen));
        }

    }

    private void getCountryCodeList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("user_token", "" + Otapp.mUniqueID);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<CountryCodePojo> mCall = mApiInterface.getCountryCodeList(jsonParams);
        mCall.enqueue(new Callback<CountryCodePojo>() {
            @Override
            public void onResponse(Call<CountryCodePojo> call, Response<CountryCodePojo> response) {

                if (response.isSuccessful()) {
                    CountryCodePojo mCountryCodePojo = response.body();
                    if (mCountryCodePojo != null) {
                        if (mCountryCodePojo.status.equalsIgnoreCase("200")) {

                            Otapp.mCountryCodePojoList = mCountryCodePojo.data;
                            Otapp.mAdsPojoList = mCountryCodePojo.ad5;

                            setMovieAdvertise();

                        } else {
                            Utils.showToast(getActivity(), "" + mCountryCodePojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryCodePojo> call, Throwable t) {
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

    }

    private void showTotalSeatDialog(final MovieTheaterListPojo.ScreenTime mScreenTime, String seatStauts, int available_seats) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_movie_seat);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        GridView gvTotalSeat = (GridView) dialog.findViewById(R.id.gvTotalSeat);
        final TextView tvPrice = (TextView) dialog.findViewById(R.id.tvPrice);
        final TextView tvStatus = (TextView) dialog.findViewById(R.id.tvStatus);
        TextView tvBookNow = (TextView) dialog.findViewById(R.id.tvBookNow);
        final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);

        if (!TextUtils.isEmpty(seatStauts)) {
            tvStatus.setText("" + seatStauts);
            tvStatus.setTextColor(getResources().getColor(R.color.green_available));
        }

        final ArrayList<TotalSeat> mTotalSeatList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            TotalSeat mTotalSeat = new TotalSeat();
            mTotalSeat.seat = i;
            if (i == 1) {
                mTotalSeat.isSelected = true;
                mMaxSeat = 1;
            } else {
                mTotalSeat.isSelected = false;
            }

            mTotalSeatList.add(mTotalSeat);
        }

        tvPrice.setText(mScreenTime.currency + " " + Utils.setPrice(mMaxSeat * mScreenTime.price));

        final TotalSeatAdapter mTotalSeatAdapter = new TotalSeatAdapter(getActivity());
        mTotalSeatAdapter.addAll(mTotalSeatList);
        gvTotalSeat.setAdapter(mTotalSeatAdapter);

        mTotalSeatAdapter.setListener(new TotalSeatAdapter.OnMovieSeatClickListener() {
            @Override
            public void onMoieSelected(int position) {
                for (int i = 0; i < mTotalSeatList.size(); i++) {
                    if (i == position) {
                        mTotalSeatList.get(i).isSelected = true;
                    } else {
                        mTotalSeatList.get(i).isSelected = false;
                    }

                }
                mTotalSeatAdapter.addAll(mTotalSeatList);
                mMaxSeat = mTotalSeatList.get(position).seat;
                tvPrice.setText(mScreenTime.currency + " " + Utils.setPrice(mMaxSeat * mScreenTime.price));

                if (mMaxSeat > available_seats) {
                    tvStatus.setText("" + getString(R.string.unavailable));
                    tvStatus.setTextColor(getResources().getColor(R.color.red));
                } else {
                    if (!TextUtils.isEmpty(seatStauts)) {
                        tvStatus.setText("" + seatStauts);
                        tvStatus.setTextColor(getResources().getColor(R.color.green_available));
                    }
                }
            }
        });


        tvBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMaxSeat < 1) {
                    Utils.showToast(getActivity(), getString(R.string.alert_select_total_ticket));
                    return;
                }

                if (mMaxSeat > available_seats) {
                    Utils.showToast(getActivity(), getString(R.string.alert_select_available_ticket));
                    return;
                }

                Utils.hideKeyboard(getActivity());


                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_MOVIE, new Gson().toJson(mMovie));
                bundle.putString(Constants.BNDL_USER_KEY, mUserKey);
                bundle.putInt(Constants.BNDL_MAX_SEAT, mMaxSeat);
                bundle.putString(Constants.BNDL_MOVIE_DATE_ID, mSelectedDate);
                bundle.putString(Constants.BNDL_MOVIE_DATE_LIST, new Gson().toJson(mMovieDateList));
                bundle.putString(Constants.BNDL_MOVIE_CINEMA_ID, mSelectedScreenId);
                bundle.putString(Constants.BNDL_MOVIE_CINEMA_LIST, new Gson().toJson(mMovieTheaterList));
                bundle.putString(Constants.BNDL_MOVIE_TIME_ID, mSelectedTimeId);
                bundle.putString(Constants.BNDL_MOVIE_TIME_LIST, new Gson().toJson(mMovieTimeList));
                switchFragment(MovieSeatFragment.newInstance(), MovieSeatFragment.Tag_MovieSeatFragment, bundle);

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMovieTheaterList != null && mMovieTheaterList.size() > 0) {
                    for (int i = 0; i < mMovieTheaterList.size(); i++) {
                        MovieTheaterListPojo.Theater mTheater = mMovieTheaterList.get(i);
                        if (mTheater != null && mTheater.screenTime != null && mTheater.screenTime.size() > 0) {
                            for (int j = 0; j < mTheater.screenTime.size(); j++) {
                                mTheater.screenTime.get(j).isSelected = false;
                            }

                        }
                    }
                    setMovieTheaterList();
                }


                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        dialog.show();

    }


    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            popBackStack();
        } else if (view == tvPlay) {

            if (mMovie != null) {

                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_TRAILER_LINK, mMovie.movieTrailer);
                IntentHandler.startActivity(getActivity(), TrailerPlayerActivity.class, bundle);

            }
        }
    }
}
