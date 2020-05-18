package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.MovieFoodAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.FoodListPojo;
import com.otapp.net.model.MovieDetailsPojo;
import com.otapp.net.model.MovieSeat;
import com.otapp.net.model.MovieTheaterListPojo;
import com.otapp.net.model.SeatProcessedPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFoodFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_MovieFoodFragment = "Tag_" + "MovieFoodFragment";

    View mView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvNoCombo)
    TextView tvNoCombo;
    @BindView(R.id.ivAds)
    ImageView ivAds;
    @BindView(R.id.lnrSkip)
    LinearLayout lnrSkip;
    @BindView(R.id.lnrContinue)
    LinearLayout lnrContinue;
    @BindView(R.id.gvFoodList)
    GridView gvFoodList;

    private int mSeatPrice;
    private String mSelectedDate = "", mMovieID = "", mSelectedScreenId = "", mSelectedTimeId = "", mvScreen = "", mSelectedVdt = "", mCurrency = "", mUserKey = "";
    List<MovieDetailsPojo.MovieDate> mMovieDateList;
    List<MovieTheaterListPojo.Theater> mMovieTheaterList;
    List<MovieTheaterListPojo.ScreenTime> mMovieTimeList;
    private MovieDetailsPojo.Movie mMovie;
    List<MovieSeat> mMovieSeatList;

    List<FoodListPojo.Combo> mComboList;
    private int total;

    MovieFoodAdapter mMovieFoodAdapter;

    public static MovieFoodFragment newInstance() {
        MovieFoodFragment fragment = new MovieFoodFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_movie_food, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {


        Bundle bundle = getArguments();
        if (bundle != null) {

            mUserKey = bundle.getString(Constants.BNDL_USER_KEY);
            mSelectedDate = bundle.getString(Constants.BNDL_MOVIE_DATE_ID);
            mSelectedScreenId = bundle.getString(Constants.BNDL_MOVIE_CINEMA_ID);
            mSelectedTimeId = bundle.getString(Constants.BNDL_MOVIE_TIME_ID);
            mMovie = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE), MovieDetailsPojo.Movie.class);
            if (mMovie != null) {
                mMovieID = mMovie.movieId;
            }
            mMovieDateList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_DATE_LIST), new TypeToken<List<MovieDetailsPojo.MovieDate>>() {
            }.getType());
            mMovieTheaterList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_CINEMA_LIST), new TypeToken<List<MovieTheaterListPojo.Theater>>() {
            }.getType());
            mMovieTimeList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_TIME_LIST), new TypeToken<List<MovieTheaterListPojo.ScreenTime>>() {
            }.getType());
            mMovieSeatList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_SEAT_LIST), new TypeToken<List<MovieSeat>>() {
            }.getType());

            for (int i = 0; i < mMovieTimeList.size(); i++) {
                if (mMovieTimeList.get(i).mtid.equals(mSelectedTimeId)) {
                    mvScreen = mMovieTimeList.get(i).mvScreen;
                    mSelectedVdt = mMovieTimeList.get(i).vdt;
                    mSeatPrice = mMovieTimeList.get(i).price;
                    mCurrency = mMovieTimeList.get(i).currency;
                    LogUtils.e("", mSelectedTimeId + " " + mvScreen + " " + mSelectedVdt + " " + mSeatPrice + " " + mCurrency);
                }
            }

            if (mComboList == null || mComboList.size() == 0) {
                getFoodList();
            } else {
                setFoodList();
            }

        }

        tvBack.setOnClickListener(this);
        lnrContinue.setOnClickListener(this);
        lnrSkip.setOnClickListener(this);
    }

    private void getFoodList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<FoodListPojo> mCall = mApiInterface.getFoodList(mvScreen, mSelectedDate, mSelectedVdt, mMovieID, Otapp.mUniqueID);
        mCall.enqueue(new Callback<FoodListPojo>() {
            @Override
            public void onResponse(Call<FoodListPojo> call, Response<FoodListPojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    FoodListPojo mFoodListPojo = response.body();
                    if (mFoodListPojo != null) {
                        if (mFoodListPojo.status.equals("200")) {

                            tvNoCombo.setVisibility(View.GONE);

                            mComboList = mFoodListPojo.data.combos;
                            List<FoodListPojo.Advertisement> advertisements = mFoodListPojo.data.advertisements;
                            if (advertisements != null && advertisements.size() > 0) {

                                Picasso.get().load(advertisements.get(0).image).into(ivAds);

                            }
                            setFoodList();

                        } else {
//                            Utils.showToast(getActivity(), mFoodListPojo.message);
                            tvNoCombo.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FoodListPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });
    }

    private void setFoodList() {

        if (mComboList != null && mComboList.size() > 0) {

            tvNoCombo.setVisibility(View.GONE);

            mMovieFoodAdapter = new MovieFoodAdapter(getActivity(), mComboList);
            gvFoodList.setAdapter(mMovieFoodAdapter);

            mMovieFoodAdapter.setListener(new MovieFoodAdapter.OnFoodListener() {
                @Override
                public void onPlusClicked(int position) {
                    FoodListPojo.Combo mFoodCombo = mComboList.get(position);
                    if (mFoodCombo.foodCount < mFoodCombo.maxQuntity) {
                        mFoodCombo.foodCount++;
                        mFoodCombo.isSelected = true;

                        mMovieFoodAdapter.notifyDataSetChanged();
                        setFoodPrice();
                    }
                }

                @Override
                public void onMinusClicked(int position) {

                    FoodListPojo.Combo mFoodCombo = mComboList.get(position);

                    if (mFoodCombo.foodCount > 1) {
                        mFoodCombo.foodCount--;
                        mFoodCombo.isSelected = true;

                    } else {
                        mFoodCombo.foodCount = 0;
                        mFoodCombo.isSelected = false;
                    }

                    setFoodPrice();
                    mMovieFoodAdapter.notifyDataSetChanged();
                }

                @Override
                public void onAddClicked(int position) {

                    FoodListPojo.Combo mFoodCombo = mComboList.get(position);
                    mFoodCombo.isSelected = true;
                    if (mFoodCombo.foodCount < 1) {
                        mFoodCombo.foodCount = 1;
                    }
                    mMovieFoodAdapter.notifyDataSetChanged();
                    setFoodPrice();
                }
            });
        } else {
            tvNoCombo.setVisibility(View.VISIBLE);
        }

    }

    private void setFoodPrice() {
        total = 0;
        if (mComboList != null && mComboList.size() > 0) {
            for (int i = 0; i < mComboList.size(); i++) {
                FoodListPojo.Combo mFoodCombo = mComboList.get(i);
                if (mFoodCombo != null && mFoodCombo.foodCount > 0) {
                    total = total + (mFoodCombo.foodCount * mFoodCombo.price);
                }
            }
        }

        if (total > 0) {
            lnrContinue.setVisibility(View.VISIBLE);
            lnrSkip.setVisibility(View.GONE);
            tvTotal.setText(getString(R.string.total) + " " + mComboList.get(0).currency + " " + Utils.setPrice(total));
        } else {
            lnrSkip.setVisibility(View.VISIBLE);
            lnrContinue.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == tvBack) {
//            popBackStack();
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.onBackPressed();
            }
        } else if (view == lnrContinue) {


//            if (total > 0) {
//
//                List<FoodListPojo.Combo> mTempComboList = new ArrayList<>();
//                for (int i = 0; i < mComboList.size(); i++) {
//                    if (mComboList.get(i).foodCount > 0) {
//                        mTempComboList.add(mComboList.get(i));
//                    }
//                }
//
//                final Dialog termsDialog = new Dialog(getActivity());
//                termsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                termsDialog.setContentView(R.layout.dialog_movie_food_list);
//                termsDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                ListView lvSeat = (ListView) termsDialog.findViewById(R.id.lvSeat);
//                TextView tvTotal = (TextView) termsDialog.findViewById(R.id.tvTotal);
//                final TextView tvModify = (TextView) termsDialog.findViewById(R.id.tvModify);
//                TextView tvConfirm = (TextView) termsDialog.findViewById(R.id.tvConfirm);
//
//                tvTotal.setText(mComboList.get(0).currency + " " + Utils.setPrice(total));
//
//                FoodQuantityAdapter mFoodQuantityAdapter = new FoodQuantityAdapter(getActivity(), mTempComboList);
//                lvSeat.setAdapter(mFoodQuantityAdapter);
//
//                tvConfirm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
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
            bundle.putString(Constants.BNDL_MOVIE_FOOD_LIST, new Gson().toJson(mComboList));
            switchFragment(MovieOrderPreviewFragment.newInstance(), MovieOrderPreviewFragment.Tag_MovieOrderPreviewFragment, bundle);
//
//                        tvModify.performClick();
//                    }
//                });
//
//                tvModify.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (termsDialog != null && termsDialog.isShowing()) {
//                            termsDialog.dismiss();
//                            termsDialog.cancel();
//                        }
//                    }
//                });
//
//                termsDialog.show();
//
//            } else {
//                Utils.showToast(getActivity(), getString(R.string.alert_select_food));
//            }

        } else if (view == lnrSkip) {

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
            switchFragment(MovieOrderPreviewFragment.newInstance(), MovieOrderPreviewFragment.Tag_MovieOrderPreviewFragment, bundle);

        }
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
}
