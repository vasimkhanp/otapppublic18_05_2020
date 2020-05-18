package com.otapp.net.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.TrailerPlayerActivity;
import com.otapp.net.adapter.RecomendedMovieAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.AddReviewPojo;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.model.MovieDetailsPojo;
import com.otapp.net.model.MovieScreenListPojo;
import com.otapp.net.model.MovieTimeListPojo;
import com.otapp.net.model.RecomendedMoviePojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_MovieDetailsFragment = "Tag_" + "MovieDetailsFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    //    @BindView(R.id.tvName)
//    TextView tvName;
    @BindView(R.id.tvMovieName)
    TextView tvMovieName;
    @BindView(R.id.tvMovieDescription)
    TextView tvMovieDescription;
    //    @BindView(R.id.tvGenre)
//    TextView tvGenre;
//    @BindView(R.id.tvRestriction)
//    TextView tvRestriction;
//    @BindView(R.id.tvTime)
//    TextView tvTime;
    @BindView(R.id.tvMovieDate)
    TextView tvMovieDate;
    @BindView(R.id.tvMovieLanguage)
    TextView tvMovieLanguage;
    @BindView(R.id.tvMovieFormate)
    TextView tvMovieFormate;
    @BindView(R.id.tvBook)
    TextView tvBook;
    @BindView(R.id.tvDirector)
    TextView tvDirector;
    @BindView(R.id.tvCastCrew)
    TextView tvCastCrew;
    @BindView(R.id.tvMovieSubtitle)
    TextView tvMovieSubtitle;
    @BindView(R.id.tvSynopsis)
    TextView tvSynopsis;
    @BindView(R.id.tvReviews)
    TextView tvReviews;
    @BindView(R.id.tvRateIt)
    TextView tvRateIt;
    @BindView(R.id.tvUserViewAll)
    TextView tvUserViewAll;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvCommentTitle)
    TextView tvCommentTitle;
    @BindView(R.id.tvUserComment)
    TextView tvUserComment;
    @BindView(R.id.tvLikes)
    TextView tvLikes;
    @BindView(R.id.tvLikesCount)
    TextView tvLikesCount;
    //    @BindView(R.id.tvCriticsViewAll)
//    TextView tvCriticsViewAll;
//    @BindView(R.id.tvCriticsName)
//    TextView tvCriticsName;
    @BindView(R.id.tvPlay)
    TextView tvPlay;
    //    @BindView(R.id.rbCriticsRate)
//    RatingBar rbCriticsRate;
//    @BindView(R.id.tvCriticsComment)
//    TextView tvCriticsComment;
    @BindView(R.id.tvRecomdedMovie)
    TextView tvRecomdedMovie;
    @BindView(R.id.tvShare)
    TextView tvShare;
    @BindView(R.id.rbUserRate)
    RatingBar rbUserRate;
    //    @BindView(R.id.spinCinema)
//    Spinner spinCinema;
//    @BindView(R.id.spinDate)
//    Spinner spinDate;
//    @BindView(R.id.spinTime)
//    Spinner spinTime;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.rvRecomdedMovie)
    RecyclerView rvRecomdedMovie;
    @BindView(R.id.rltMovieOverlay)
    RelativeLayout rltMovieOverlay;
    @BindView(R.id.lnrRatings)
    LinearLayout lnrRatings;
    @BindView(R.id.lnrUserReview)
    LinearLayout lnrUserReview;
    @BindView(R.id.svContainer)
    ScrollView svContainer;

//    YouTubePlayerSupportFragment youTubePlayerFragment;

    private String mSelectedDate = "", mMovieID = "", mSelectedScreenId = "", mSelectedTimeId = "", mUserKey = "", mMovieType = "", mMovieState = "";

//    List<String> mDateList = new ArrayList<>();
//    List<String> mCinemaList = new ArrayList<>();
//    List<String> mTimeList = new ArrayList<>();
//
//    ArrayAdapter<String> mDateAdapter, mCinemaAdapter, mTimeAdapter;

    List<MovieDetailsPojo.MovieDate> mMovieDateList;
    List<MovieScreenListPojo.Screen> mMovieCinemaList;
    List<MovieTimeListPojo.Time> mMovieTimeList;

    private MovieDetailsPojo.Movie mMovie;
    private List<RecomendedMoviePojo.RecomendedMovie> mRecomendedMovieList;

    public static MovieDetailsFragment newInstance() {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();
//        initYouTube();

        return mView;
    }

    private void InitializeControls() {

        rvRecomdedMovie.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


//        if (!mDateList.contains(getString(R.string.select_date))) {
//            mDateList.add(getString(R.string.select_date));
//        }
//
//        if (!mCinemaList.contains(getString(R.string.select_cinema))) {
//            mCinemaList.add(getString(R.string.select_cinema));
//        }
//
//        if (!mTimeList.contains(getString(R.string.select_time))) {
//            mTimeList.add(getString(R.string.select_time));
//        }
//
//        mDateAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mDateList);
//        mCinemaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mCinemaList);
//        mTimeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mTimeList);
//
//        spinDate.setAdapter(mDateAdapter);
//        spinCinema.setAdapter(mCinemaAdapter);
//        spinTime.setAdapter(mTimeAdapter);

        tvBook.setEnabled(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mMovieID = bundle.getString(Constants.BNDL_MOVIE_ID);
            mMovieType = bundle.getString(Constants.BNDL_MOVIE_TYPE);
            if (!TextUtils.isEmpty(mMovieType) && mMovieType.equalsIgnoreCase(Constants.BNDL_MOVIE_TYPE_UPCOMING)) {
                mMovieState = bundle.getString(Constants.BNDL_MOVIE_STATE);
                if (!TextUtils.isEmpty(mMovieState) && !mMovieState.equalsIgnoreCase("1")){
                    tvBook.setText(getString(R.string.coming_soon));
                    tvBook.setEnabled(false);
                }
            }

            if (!TextUtils.isEmpty(mMovieID)) {
                if (mMovie == null) {
                    getMovieDetails(mMovieID);
                } else {
                    setMovieDetails();
                }
            }
        }

//        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.flYoutubeFragment, youTubePlayerFragment).commit();


        tvBack.setOnClickListener(this);
        tvBook.setOnClickListener(this);
        tvPlay.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvLikes.setOnClickListener(this);
        tvLikesCount.setOnClickListener(this);
        tvReviews.setOnClickListener(this);
        tvRateIt.setOnClickListener(this);
        tvUserViewAll.setOnClickListener(this);

    }

    private void initYouTube() {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.BNDL_TRAILER_LINK, mMovie.movieTrailer);
        IntentHandler.startActivity(getActivity(), TrailerPlayerActivity.class, bundle);

//        youTubePlayerFragment.initialize(Constants.YouTubeKey, new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
//                LogUtils.i("Detail", "YouTube Player onInitializationSuccess");
//
//                if (!wasRestored) {
//                    player.setFullscreen(false);
//                    player.loadVideo(mMovie.movieTrailer.replace("https://www.youtube.com/embed/", ""));
//                    player.play();
//                    player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
//                        @Override
//                        public void onPlaying() {
//                            LogUtils.e("", "onPlaying");
//                            rltMovieOverlay.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onPaused() {
//                            LogUtils.e("", "onPaused");
//                        }
//
//                        @Override
//                        public void onStopped() {
//                            LogUtils.e("", "onStopped");
////                            rltMovieOverlay.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onBuffering(boolean b) {
//                            LogUtils.e("", "onBuffering");
//                        }
//
//                        @Override
//                        public void onSeekTo(int i) {
//                            LogUtils.e("", "onBuffering");
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
//                LogUtils.i("Detail", "Failed: " + errorReason);
//                if (errorReason.isUserRecoverableError()) {
//                    errorReason.getErrorDialog(getActivity(), Constants.RC_YT_DIALOG_REQUEST).show();
//                } else {
//                    String errorMessage = String.format(
//                            "There was an error initializing the YouTubePlayer (%1$s)",
//                            errorReason.toString());
//                    Utils.showToast(getActivity(), errorMessage);
//                }
//            }
//        });

    }

    private void getMovieDetails(final String mMovieID) {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("movie_id", "" + mMovieID);
//        jsonParams.put("movie_id", "46");
        jsonParams.put("user_token", "" + Otapp.mUniqueID);
        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
            jsonParams.put("cust_id", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
//        jsonParams.put("cust_id", "716");
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<MovieDetailsPojo> mCall = mApiInterface.getMovieDetails(jsonParams);

        mCall.enqueue(new Callback<MovieDetailsPojo>() {
            @Override
            public void onResponse(Call<MovieDetailsPojo> call, Response<MovieDetailsPojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    MovieDetailsPojo mMovieDetailsPojo = response.body();
                    if (mMovieDetailsPojo != null) {
                        if (mMovieDetailsPojo.status.equalsIgnoreCase("200")) {

                            mMovie = mMovieDetailsPojo.movie;
                            setMovieDetails();

                        } else {
                            Utils.showToast(getActivity(), "" + mMovieDetailsPojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieDetailsPojo> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

    }

    private void setMovieDetails() {

        if (mMovie != null && isAdded()) {

            tvMovieName.setText("" + mMovie.name);
            tvMovieDate.setText(mMovie.movieRestriction + " | " + mMovie.movieReleaseDate);
            tvMovieFormate.setText(mMovie.movieFormat);
            tvMovieFormate.setVisibility(View.VISIBLE);

            StringBuilder strLanguage = new StringBuilder();
            strLanguage.append(mMovie.language);

            if (!TextUtils.isEmpty(mMovie.mvSubtitle)) {
                strLanguage.append(", " + getString(R.string.sub_title) + ": " + mMovie.mvSubtitle);
            }

            if (strLanguage != null && strLanguage.length() > 0) {
                tvMovieLanguage.setText(strLanguage.toString());
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

            strGenre.append("  |  " + mMovie.movieGenre);

            if (strGenre != null && strGenre.length() > 0) {
                tvMovieDescription.setText(strGenre.toString());
            } else {
                tvMovieDescription.setText("");
            }

            if (!TextUtils.isEmpty(mMovie.movieDirector)) {
                tvDirector.setText(Html.fromHtml(mMovie.movieDirector));
            }

            if (!TextUtils.isEmpty(mMovie.movieCast)) {
                tvCastCrew.setText(Html.fromHtml(mMovie.movieCast));
            }

            if (!TextUtils.isEmpty(mMovie.movieSysnops)) {
                tvSynopsis.setText(Html.fromHtml(mMovie.movieSysnops));
            }

            if (!TextUtils.isEmpty(mMovieType) && mMovieType.equalsIgnoreCase(Constants.BNDL_MOVIE_TYPE_UPCOMING)) {
                lnrRatings.setVisibility(View.GONE);
            } else {

                lnrRatings.setVisibility(View.VISIBLE);

                String mReview = "Reviews";
                try {
                    if (getActivity() != null) {
                        mReview = getString(R.string.reviews);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                tvReviews.setText((mMovie.totalUserReviews + mMovie.totalCriticsReviews) + " " + mReview);
                tvReviews.setText((mMovie.totalUserReviews) + " " + mReview);
                tvUserViewAll.setText(getString(R.string.view_all) + " (" + mMovie.totalUserReviews + ")");
//                tvCriticsViewAll.setText(getString(R.string.view_all) + " (" + mMovie.totalCriticsReviews + ")");
                if (mMovie.userReview != null) {
                    tvUserName.setText("" + mMovie.userReview.userName);
                    tvUserComment.setText("" + Html.fromHtml(mMovie.userReview.userComment));
                    tvCommentTitle.setText("" + Html.fromHtml(mMovie.userReview.title));

                    if (mMovie.userReview.rating > 0) {
                        rbUserRate.setRating(mMovie.userReview.rating);
                    }

                    lnrUserReview.setVisibility(View.VISIBLE);
                } else {
                    lnrUserReview.setVisibility(View.GONE);
                }


//                tvCriticsName.setText("" + mMovie.criticsReview.criticsName);
//                tvCriticsComment.setText("" + mMovie.criticsReview.criticsComment);
            }

            tvLikes.setVisibility(View.VISIBLE);
            tvLikesCount.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(mMovie.likeCount)) {
                int likes = Integer.parseInt(mMovie.likeCount);
                if (likes > 1) {
                    tvLikesCount.setText(likes + " " + getString(R.string.likes));
                } else {
                    if (likes == 0) {
                        tvLikesCount.setText(getString(R.string.like));
                    } else {
                        tvLikesCount.setText(likes + " " + getString(R.string.like));
                    }
                }

            } else {
                tvLikesCount.setText(getString(R.string.like));
            }

            if (mMovie.isLogginUserLiked == 1) {
                tvLikes.setBackgroundResource(R.drawable.ic_liked_movie);
            } else {
                tvLikes.setBackgroundResource(R.drawable.ic_likes_movie);
            }

            if (mMovie.isUserReviewed == 1) {
                tvRateIt.setVisibility(View.GONE);
            } else {
                tvRateIt.setVisibility(View.VISIBLE);
            }

//            List<MovieDetailsPojo.PaymentAllowed> paymentAllowed = mMovie.paymentAllowed;
//            if (mMovie.paymentAllowed != null && mMovie.paymentAllowed.size() > 0) {
//                for (int i = 0; i < mMovie.paymentAllowed.size(); i++) {
//                    LogUtils.e("", "paymentName::" + mMovie.paymentAllowed.get(i).paymentName);
//                }
//            }else{
//                LogUtils.e("", "paymentAllowed is null");
//            }

//            if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {


//            if (mMovie.criticsReview.rating > 0) {
//                rbCriticsRate.setRating(mMovie.criticsReview.rating);
//            }


            if (!TextUtils.isEmpty(mMovie.image)) {
                Picasso.get().load(mMovie.image).into(ivPhoto);
            }

//            if (mMovie.movieDates != null && mMovie.movieDates.size() > 0) {
//                mMovieDateList = mMovie.movieDates;
//                if (mDateList != null && mDateList.size() > 0) {
//                    mDateList.clear();
//                }
//                if (mMovieDateList != null && mMovieDateList.size() > 0) {
//                    mDateList.add(getString(R.string.select_date));
//                    for (int i = 0; i < mMovieDateList.size(); i++) {
//                        mDateList.add(mMovieDateList.get(i).dt);
//                    }
//                }
//
//                mDateAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mDateList);
//                spinDate.setAdapter(mDateAdapter);
//                spinDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                        String mDate = adapterView.getItemAtPosition(i).toString();
//                        if (!TextUtils.isEmpty(mDate) && !mDate.equalsIgnoreCase(getString(R.string.select_date))) {
//                            mSelectedDate = mDate;
//                            getScreenList();
//                        } else {
//                            mSelectedDate = "";
//                        }
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                });
//            }
        }

        if (mRecomendedMovieList == null || mRecomendedMovieList.size() == 0) {
            getRecomendedMovieList();
        } else {
            setRecomendedMovie();
        }

    }

//    private void getScreenList() {
//
//        if (!Utils.isInternetConnected(getActivity())) {
//            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
//            return;
//        }
//
//        Utils.showProgressDialog(getActivity());
//
//        ApiInterface mApiInterface = RestClient.getClient(true);
//        Call<MovieScreenListPojo> mCall = mApiInterface.getMovieScreenList(mMovieID, mSelectedDate);
//        mCall.enqueue(new Callback<MovieScreenListPojo>() {
//            @Override
//            public void onResponse(Call<MovieScreenListPojo> call, Response<MovieScreenListPojo> response) {
//
//                Utils.closeProgressDialog();
//
//                if (response.isSuccessful()) {
//                    MovieScreenListPojo mMovieScreenListPojo = response.body();
//                    if (mMovieScreenListPojo != null) {
//                        if (mMovieScreenListPojo.status.equalsIgnoreCase("200")) {
//
//                            if (mMovieScreenListPojo.data != null && mMovieScreenListPojo.data.size() > 0) {
//                                mMovieCinemaList = mMovieScreenListPojo.data;
//                                if (mCinemaList != null && mCinemaList.size() > 0) {
//                                    mCinemaList.clear();
//                                }
//                                if (mMovieCinemaList != null && mMovieCinemaList.size() > 0) {
//                                    mCinemaList.add(getString(R.string.select_cinema));
//                                    for (int i = 0; i < mMovieCinemaList.size(); i++) {
//                                        mCinemaList.add(mMovieCinemaList.get(i).screenName);
//                                    }
//                                }
//
//                                mCinemaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mCinemaList);
//                                spinCinema.setAdapter(mCinemaAdapter);
//                                spinCinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                    @Override
//                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                                        String mScreen = adapterView.getItemAtPosition(i).toString();
//                                        if (!TextUtils.isEmpty(mScreen) && !mScreen.equalsIgnoreCase(getString(R.string.select_cinema))) {
//                                            mSelectedScreenId = mMovieCinemaList.get(i - 1).screenId;
//                                            getTimeList();
//                                        } else {
//                                            mSelectedScreenId = "";
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                    }
//                                });
//                            }
//
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<MovieScreenListPojo> call, Throwable t) {
//                Utils.closeProgressDialog();
//            }
//        });
//
//    }

//    private void getTimeList() {
//
//        if (!Utils.isInternetConnected(getActivity())) {
//            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
//            return;
//        }
//
//        Utils.showProgressDialog(getActivity());
//
//        ApiInterface mApiInterface = RestClient.getClient(true);
//        Call<MovieTimeListPojo> mCall = mApiInterface.getMovieTimeList(mMovieID, mSelectedDate, mSelectedScreenId);
//        mCall.enqueue(new Callback<MovieTimeListPojo>() {
//            @Override
//            public void onResponse(Call<MovieTimeListPojo> call, Response<MovieTimeListPojo> response) {
//
//                Utils.closeProgressDialog();
//
//                if (response.isSuccessful()) {
//                    MovieTimeListPojo mMovieTimeListPojo = response.body();
//                    if (mMovieTimeListPojo != null) {
//                        if (mMovieTimeListPojo.status.equalsIgnoreCase("200")) {
//
//                            if (mMovieTimeListPojo.data != null && mMovieTimeListPojo.data.size() > 0) {
//                                mMovieTimeList = mMovieTimeListPojo.data;
//                                mUserKey = mMovieTimeListPojo.userKey;
//                                if (mTimeList != null && mTimeList.size() > 0) {
//                                    mTimeList.clear();
//                                }
//                                if (mMovieTimeList != null && mMovieTimeList.size() > 0) {
//                                    mTimeList.add(getString(R.string.select_time));
//                                    for (int i = 0; i < mMovieTimeList.size(); i++) {
//                                        mTimeList.add(mMovieTimeList.get(i).vdt);
//                                    }
//                                }
//
//                                mTimeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mTimeList);
//                                spinTime.setAdapter(mTimeAdapter);
//                                spinTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                    @Override
//                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                                        String mTime = adapterView.getItemAtPosition(i).toString();
//                                        if (!TextUtils.isEmpty(mTime) && !mTime.equalsIgnoreCase(getString(R.string.select_time))) {
//                                            mSelectedTimeId = mMovieTimeList.get(i - 1).mtid;
//                                        } else {
//                                            mSelectedTimeId = "";
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                    }
//                                });
//                            }
//
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<MovieTimeListPojo> call, Throwable t) {
//                Utils.closeProgressDialog();
//            }
//        });
//
//    }

    private void getRecomendedMovieList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<RecomendedMoviePojo> mCall = mApiInterface.getRecomendedMovieList(mMovie.movieId, Otapp.mUniqueID);
        mCall.enqueue(new Callback<RecomendedMoviePojo>() {
            @Override
            public void onResponse(Call<RecomendedMoviePojo> call, Response<RecomendedMoviePojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    RecomendedMoviePojo mRecomendedMoviePojo = response.body();
                    if (mRecomendedMoviePojo != null) {
                        if (mRecomendedMoviePojo.status.equalsIgnoreCase("200")) {

                            mRecomendedMovieList = mRecomendedMoviePojo.recomendedMovie;
                            setRecomendedMovie();

                        } else {
                            Utils.showToast(getActivity(), "" + mRecomendedMoviePojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RecomendedMoviePojo> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

    }

    private void likeMovie() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("movie_id", "" + mMovieID);
        jsonParams.put("user_token", "" + Otapp.mUniqueID);
        jsonParams.put("cust_id", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
        jsonParams.put("cust_log_key", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_KEY, ""));

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ApiResponse> mCall = mApiInterface.likeMovie(jsonParams);
        mCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null) {
                        if (mApiResponse.status.equalsIgnoreCase("200")) {

                            mMovie.isLogginUserLiked = 1;
                            tvLikes.setBackgroundResource(R.drawable.ic_liked_movie);

                            int count = 0;
                            if (!TextUtils.isEmpty(mMovie.likeCount)) {
                                int likes = Integer.parseInt(mMovie.likeCount);
                                count = likes + 1;
                            }

                            mMovie.likeCount = "" + count;

                            if (count > 1) {
                                tvLikesCount.setText(count + " " + getString(R.string.likes));
                            } else {
                                if (count == 0) {
                                    tvLikesCount.setText(getString(R.string.like));
                                } else {
                                    tvLikesCount.setText(count + " " + getString(R.string.like));
                                }
                            }

                        } else {
                            Utils.showToast(getActivity(), "" + mApiResponse.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

    }

    private void setRecomendedMovie() {

        if (mRecomendedMovieList != null && mRecomendedMovieList.size() > 0) {

            tvRecomdedMovie.setVisibility(View.VISIBLE);
            rvRecomdedMovie.setVisibility(View.VISIBLE);

            RecomendedMovieAdapter mRecomendedMovieAdapter = new RecomendedMovieAdapter(getActivity(), mRecomendedMovieList, new RecomendedMovieAdapter.OnMovieClickListener() {
                @Override
                public void onMovieClicked(int position) {
//                    bookStat
                    String mvoiesId=mRecomendedMovieList.get(position).movieId;
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.BNDL_MOVIE_ID,mvoiesId);
                    bundle.putString(Constants.BNDL_MOVIE_TYPE, Constants.BNDL_MOVIE_TYPE_CURRENT);
                    switchFragment(MovieDetailsFragment.newInstance(), MovieDetailsFragment.Tag_MovieDetailsFragment, bundle);
                }
            });
            rvRecomdedMovie.setAdapter(mRecomendedMovieAdapter);

        } else {
            tvRecomdedMovie.setVisibility(View.GONE);
            rvRecomdedMovie.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {

        if (view == tvBack) {
            popBackStack();
        } else if (view == tvBook) {

//            if (isValidField()) {
            if (mMovie != null) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_MOVIE, new Gson().toJson(mMovie));
//                bundle.putString(Constants.BNDL_USER_KEY, mUserKey);
//                bundle.putString(Constants.BNDL_MOVIE_DATE_ID, mSelectedDate);
//                bundle.putString(Constants.BNDL_MOVIE_DATE_LIST, new Gson().toJson(mMovieDateList));
//                bundle.putString(Constants.BNDL_MOVIE_CINEMA_ID, mSelectedScreenId);
//                bundle.putString(Constants.BNDL_MOVIE_CINEMA_LIST, new Gson().toJson(mMovieTheaterList));
//                bundle.putString(Constants.BNDL_MOVIE_TIME_ID, mSelectedTimeId);
//                bundle.putString(Constants.BNDL_MOVIE_TIME_LIST, new Gson().toJson(mMovieTimeList));
//                switchFragment(MovieSeatFragment.newInstance(), MovieSeatFragment.Tag_MovieSeatFragment, bundle);
                switchFragment(MovieDateSelectionFragment.newInstance(), MovieDateSelectionFragment.Tag_MovieDateSelectionFragment, bundle);
            }
//            }

//            new ShareTicketTask().execute();


        } else if (view == tvPlay) {
            if (mMovie != null) {
//                rltMovieOverlay.setVisibility(View.GONE);
                initYouTube();
            }
        } else if (view == tvLikes) {

            if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                if (mMovie != null && mMovie.isLogginUserLiked == 0) {
                    likeMovie();
                }
            } else {
                Utils.showToast(getActivity(), getString(R.string.msg_login_first));
            }

        } else if (view == tvLikesCount) {
            tvLikes.performClick();
        } else if (view == tvShare) {

            String mShareText = "Enjoy movie " + mMovie.name + " on " + getString(R.string.app_name) + " https://www.otapp.net/movies";

            ShareCompat.IntentBuilder
                    .from(getActivity())
                    .setText(mShareText)
//                    .setStream(Uri.parse(mMovie.image))
                    .setType("text/plain")
                    .setChooserTitle(getString(R.string.app_name))
                    .startChooser();

        } else if (view == tvReviews || view == tvUserViewAll) {
            if (mMovie != null) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_MOVIE, new Gson().toJson(mMovie));
                switchFragment(MovieReviewListFragment.newInstance(), MovieReviewListFragment.Tag_MovieReviewListFragment, bundle);
            }
        } else if (view == tvRateIt) {

            if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                if (mMovie != null) {
                    showRateDialog();
                }
            } else {
                Utils.showToast(getActivity(), getString(R.string.msg_login_first));
            }


        }
    }

    private void showRateDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_movie_review);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final TextView tvRatePercentage = (TextView) dialog.findViewById(R.id.tvRatePercentage);
        final RatingBar rvRating = (RatingBar) dialog.findViewById(R.id.rvRating);
        final EditText etTitle = (EditText) dialog.findViewById(R.id.etTitle);
        final EditText etDescription = (EditText) dialog.findViewById(R.id.etDescription);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);

        rvRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                tvRatePercentage.setText("" + (int) (rating * 20) + "%");
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(etTitle.getText().toString())) {
                    Utils.showToast(getActivity(), getString(R.string.alert_enter_title));
                    return;
                }

                if (TextUtils.isEmpty(etDescription.getText().toString())) {
                    Utils.showToast(getActivity(), getString(R.string.alert_enter_description));
                    return;
                }


                //Add review
                if (!Utils.isInternetConnected(getActivity())) {
                    Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                    return;
                }

                Utils.showProgressDialog(getActivity());

                Map<String, String> jsonParams = new HashMap<>();
                jsonParams.put("movie_id", "" + mMovieID);
                jsonParams.put("title", "" + etTitle.getText().toString());
                jsonParams.put("review", "" + etDescription.getText().toString());
                jsonParams.put("rating", "" + rvRating.getRating());
                jsonParams.put("user_token", "" + Otapp.mUniqueID);
                jsonParams.put("cust_id", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
                jsonParams.put("cust_log_key", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_KEY, ""));

                ApiInterface mApiInterface = RestClient.getClient(true);
                Call<AddReviewPojo> mCall = mApiInterface.addMovieReview(jsonParams);
                mCall.enqueue(new Callback<AddReviewPojo>() {
                    @Override
                    public void onResponse(Call<AddReviewPojo> call, Response<AddReviewPojo> response) {

                        Utils.closeProgressDialog();

                        if (response.isSuccessful()) {
                            AddReviewPojo mAddReviewPojo = response.body();
                            if (mAddReviewPojo != null) {
                                if (mAddReviewPojo.status.equalsIgnoreCase("200")) {

                                    if (mAddReviewPojo.data != null) {
                                        lnrRatings.setVisibility(View.VISIBLE);
                                        tvReviews.setText(mAddReviewPojo.data.totalReviews + " " + getString(R.string.reviews));
                                        rbUserRate.setRating(mAddReviewPojo.data.avgRating);
                                        tvUserName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, ""));
                                        tvCommentTitle.setText(Html.fromHtml(etTitle.getText().toString()));
                                        tvUserComment.setText(Html.fromHtml(etDescription.getText().toString()));

                                        mMovie.totalUserReviews = mAddReviewPojo.data.totalReviews;
                                        mMovie.userReview.rating = mAddReviewPojo.data.avgRating;

                                    }

                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.dismiss();
                                        dialog.cancel();
                                    }

                                } else {
                                    Utils.showToast(getActivity(), "" + mAddReviewPojo.message);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddReviewPojo> call, Throwable t) {
                        Utils.closeProgressDialog();
                        LogUtils.e("", "onFailure:" + t.getMessage());
                    }
                });


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

    public void setMovieRating(AddReviewPojo.Review data, String title, String description) {
        LogUtils.e(Tag_MovieDetailsFragment, "setMovieRating avgRating::" + data.avgRating + " percentRating:" + data.percentRating + " totalReviews:" + data.totalReviews + "setMovieRating::" + title + "\n" + description);
        lnrRatings.setVisibility(View.VISIBLE);
        tvReviews.setText(data.totalReviews + " " + getString(R.string.reviews));
        rbUserRate.setRating(data.avgRating);
        tvUserName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, ""));
        tvCommentTitle.setText(Html.fromHtml(title));
        tvUserComment.setText(Html.fromHtml(description));
    }


//    private boolean isValidField() {
//
//        if (TextUtils.isEmpty(mSelectedDate)) {
//            Utils.showToast(getActivity(), getString(R.string.alert_select_date));
//            return false;
//        } else if (TextUtils.isEmpty(mSelectedScreenId)) {
//            Utils.showToast(getActivity(), getString(R.string.alert_select_cinema));
//            return false;
//        } else if (TextUtils.isEmpty(mSelectedTimeId)) {
//            Utils.showToast(getActivity(), getString(R.string.alert_select_time));
//            return false;
//        }
//
//        return true;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.RC_YT_DIALOG_REQUEST) {
            initYouTube();
        }
    }

//    class ShareTicketTask extends AsyncTask<String, String, String> {
//
//        Bitmap mCurrentBitmap = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Utils.showProgressDialog(getActivity());
//            int totalHeight = svContainer.getChildAt(0).getHeight();
//            int totalWidth = svContainer.getChildAt(0).getWidth();
//
//            svContainer.setDrawingCacheEnabled(true);
//            mCurrentBitmap = Bitmap.createBitmap(svContainer.getDrawingCache());
//            svContainer.setDrawingCacheEnabled(false);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//
//            SaveTicket savecard = new SaveTicket();
//            savecard.saveMovieTicket(getActivity(), mCurrentBitmap);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            Utils.closeProgressDialog();
//
//            getActivity().runOnUiThread(new Runnable() {
//                public void run() {
//
//                    String shareName = Constants.BNDL_MOVIE;
//
//                    Intent shareCardIntent = new Intent();
//                    shareCardIntent.setAction(Intent.ACTION_SEND);
//                    shareCardIntent.setType("*/*");
//                    shareCardIntent.putExtra(Intent.EXTRA_TEXT, shareName);
//                    shareCardIntent.putExtra(Intent.EXTRA_STREAM,
//                            Uri.parse(getActivity().getExternalCacheDir().getPath() + "/Otapp/"
//                                    + SaveTicket.NameOfFile));
//                    shareCardIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    startActivity(Intent.createChooser(shareCardIntent, "Share via"));
//                }
//            });
//
//        }
//    }


}
