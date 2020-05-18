package com.otapp.net.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.UserReviewAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.AddReviewPojo;
import com.otapp.net.model.MovieDetailsPojo;
import com.otapp.net.model.ReviewListPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieReviewListFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_MovieReviewListFragment = "Tag_" + "MovieReviewListFragment";

    View mView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTotalRateMovie)
    TextView tvTotalRateMovie;
    @BindView(R.id.rbTotalRating)
    RatingBar rbTotalRating;
    @BindView(R.id.tvRateMovie)
    TextView tvRateMovie;
    @BindView(R.id.rbRating)
    RatingBar rbRating;
    @BindView(R.id.lvReview)
    ListView lvReview;

    UserReviewAdapter mUserReviewAdapter;

    private MovieDetailsPojo.Movie mMovie;

    public static MovieReviewListFragment newInstance() {
        MovieReviewListFragment fragment = new MovieReviewListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_movie_review_list, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        mUserReviewAdapter = new UserReviewAdapter(getActivity());
        lvReview.setAdapter(mUserReviewAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {

            mMovie = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE), MovieDetailsPojo.Movie.class);
            if (mMovie != null) {
                tvTitle.setText("" + mMovie.name);
                getReviewList();
            }
        }

        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
            if (mMovie.isUserReviewed == 1) {
                tvRateMovie.setVisibility(View.GONE);
                rbRating.setVisibility(View.VISIBLE);
            } else {
                tvRateMovie.setVisibility(View.VISIBLE);
                rbRating.setVisibility(View.GONE);
            }
        } else {
            tvRateMovie.setVisibility(View.VISIBLE);
            rbRating.setVisibility(View.GONE);
        }

        tvBack.setOnClickListener(this);
        tvRateMovie.setOnClickListener(this);

    }


    private void getReviewList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("movie_id", "" + mMovie.movieId);
        jsonParams.put("user_token", "" + Otapp.mUniqueID);

        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
            jsonParams.put("cust_id", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
            jsonParams.put("cust_log_key", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_KEY, ""));
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ReviewListPojo> mCall = mApiInterface.getReviewList(jsonParams);
        mCall.enqueue(new Callback<ReviewListPojo>() {
            @Override
            public void onResponse(Call<ReviewListPojo> call, Response<ReviewListPojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    ReviewListPojo mReviewListPojo = response.body();
                    if (mReviewListPojo != null) {
                        if (mReviewListPojo.status.equalsIgnoreCase("200")) {

                            if (mReviewListPojo.data != null) {

                                rbRating.setRating(mReviewListPojo.data.userRating);
                                rbTotalRating.setRating(mReviewListPojo.data.avgRating);

                                tvTotalRateMovie.setText("" + mReviewListPojo.data.percentRating + "%");

                                mUserReviewAdapter.addAll(mReviewListPojo.data.userReviewsList);

                            }


                        } else {
                            Utils.showToast(getActivity(), "" + mReviewListPojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewListPojo> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

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
                tvRatePercentage.setText("" + (int) rating * 20 + "%");
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

                Utils.hideKeyboard(getActivity());

                Utils.showProgressDialog(getActivity());

                Map<String, String> jsonParams = new HashMap<>();
                jsonParams.put("movie_id", "" + mMovie.movieId);
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
//                                        tvReviews.setText(mAddReviewPojo.data.totalReviews+" "+getString(R.string.reviews));
//                                        rbUserRate.setRating(mAddReviewPojo.data.avgRating);
//                                        tvUserName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, ""));
//                                        tvCommentTitle.setText(etTitle.getText().toString());
//                                        tvUserComment.setText(etDescription.getText().toString());

                                        mMovie.isUserReviewed = 1;
                                        if (mMovie.isUserReviewed == 1) {
                                            tvRateMovie.setVisibility(View.GONE);
                                            rbRating.setVisibility(View.VISIBLE);
                                        }

                                        HomeActivity activity = (HomeActivity) getActivity();
                                        if (activity != null) {
                                            activity.setMovieRating(mAddReviewPojo.data, etTitle.getText().toString(), etDescription.getText().toString());
                                        }
                                        getReviewList();

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

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            popBackStack();
        } else if (view == tvRateMovie) {

            if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                if (mMovie != null) {
                    showRateDialog();
                }
            } else {
                Utils.showToast(getActivity(), getString(R.string.msg_login_first));
            }
        }
    }
}
