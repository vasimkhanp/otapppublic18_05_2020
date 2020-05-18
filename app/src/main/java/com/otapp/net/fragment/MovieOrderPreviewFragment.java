package com.otapp.net.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.CouponCodeActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.CountryCodeListAdapter;
import com.otapp.net.adapter.CountryCodeSpinAdapter;
import com.otapp.net.adapter.MoviePreviewAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.CouponResponsePojo;
import com.otapp.net.model.FoodListPojo;
import com.otapp.net.model.MovieDetailsPojo;
import com.otapp.net.model.MoviePaymentSummaryPojo;
import com.otapp.net.model.MovieSeat;
import com.otapp.net.model.MovieSuccessPojo;
import com.otapp.net.model.MovieTheaterListPojo;
import com.otapp.net.model.MovieZeroResponse;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.IntentHandler;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieOrderPreviewFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_MovieOrderPreviewFragment = "Tag_" + "MovieOrderPreviewFragment";

    View mView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;
    @BindView(R.id.tvPlace)
    TextView tvPlace;
    @BindView(R.id.tvScreen)
    TextView tvScreen;
    @BindView(R.id.tvDateTime)
    TextView tvDateTime;
    @BindView(R.id.tvSeat)
    TextView tvSeat;
    @BindView(R.id.tvGrandTotalTitle)
    TextView tvGrandTotalTitle;
    @BindView(R.id.tvGrandTotal)
    TextView tvGrandTotal;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvAgreeBox)
    TextView tvAgreeBox;
    @BindView(R.id.tvAgree)
    TextView tvAgree;
    @BindView(R.id.tvAgreeTermsWarning)
    TextView tvAgreeTermsWarning;
    @BindView(R.id.lnrBook)
    LinearLayout lnrBook;
    @BindView(R.id.tvFullname)
    TextView tvFullname;
    @BindView(R.id.tvEmailMobile)
    TextView tvEmailMobile;
    @BindView(R.id.tvSeatQuntity)
    TextView tvSeatQuntity;
    @BindView(R.id.tvClassSeat)
    TextView tvClassSeat;
    @BindView(R.id.tvScreenName)
    TextView tvScreenName;
    @BindView(R.id.ivMovie)
    ImageView ivMovie;
    //    @BindView(R.id.etEmailID)
//    EditText etEmailID;
//    @BindView(R.id.etMobileNumber)
//    EditText etMobileNumber;
//    @BindView(R.id.etFirstName)
//    EditText etFirstName;
    @BindView(R.id.lvSeat)
    ListView lvSeat;
    @BindView(R.id.lnrApplyOffer)
    LinearLayout lnrApplyOffer;
    @BindView(R.id.lnrPerson)
    LinearLayout lnrPerson;

    int personalFlag = 0;
    int enableCountryCode = 0;
    int countryCodePosition;

    String strmobileNumber = "";
    String strCountryCode = "+255";
    TextView tvCancelOffers;


    private int mSeatPrice, mIHF, mTAX, mSeatTotalPrice, mSelectedSeatCount, mFoodPrice, mGlassesPrice, mGrandPrice = 0;
    private float mTotalPrice = 0, totalBeforePromo = 0;
    private String mSelectedDate = "", mMovieID = "", mSelectedScreenId = "", mSelectedTimeId = "", mvScreen = "", mSelectedVdt = "", mCurrency = "", mUserKey = "", mClassSeat = "";
    List<MovieDetailsPojo.MovieDate> mMovieDateList;
    List<MovieTheaterListPojo.Theater> mMovieTheaterList;
    List<MovieTheaterListPojo.ScreenTime> mMovieTimeList;
    private MovieDetailsPojo.Movie mMovie;
    List<MovieSeat> mMovieSeatList;
    List<FoodListPojo.Combo> mComboList = new ArrayList<>();

    CouponResponsePojo mCouponResponsePojo;
    MoviePaymentSummaryPojo mPaymentSummaryPojo;
    List<MoviePaymentSummaryPojo.Summary> mPaymentSummaryList;

    private boolean isTermsAccepted = true;
    private int mMobNumberMaxLength = 9;

    MoviePreviewAdapter mMoviePreviewAdapter;

    String strPromoCode = "", strPromoText = "", strTicketInfo = "";
    int flag = 0, promoFareAmt = 0, flagMovies = 0, mobFlag = 0;
    String mobileNumber = "";
    String sExtraPGWCharges = "";

    List<CountryCodePojo.CountryCode> tempCountryCodeList;
    CountryCodeListAdapter mCountryCodeSpinAdapter;
    int countryCodeFlag = 0;

    public static MovieOrderPreviewFragment newInstance() {
        MovieOrderPreviewFragment fragment = new MovieOrderPreviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_movie_order_preview, container, false);
        ButterKnife.bind(this, mView);

        tvCancelOffers = mView.findViewById(R.id.cancelOffers);
        //showPersonDetailsDialog();
        tempCountryCodeList = new ArrayList<>();
        InitializeControls();
        //showPersonDetailsDialog();
        tvCancelOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* mTotalPrice=totalBeforePromo;
                tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mTotalPrice));
                Toast.makeText(getContext(), "Offer cancled by you!", Toast.LENGTH_SHORT).show();*/
                flag = 1;
                // InitializeControls();
                getPaymentSummary();
            }
        });
        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //    Toast.makeText(getContext(), "back", Toast.LENGTH_SHORT).show();
                    String sCouponcode = MyPref.getPref(getContext(), "PromoCode", strPromoCode);
                    if (sCouponcode != null) {
                        if (sCouponcode.equals("")) {
                            popBackStack();
                        } else {
                            removeCouponCode();
                        }
                    } else {
                        removeCouponCode();
                    }
                }
                return true;
            }
        });


        return mView;
    }

    private void InitializeControls() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            mSeatPrice = 0;
            mIHF = 0;
            mTAX = 0;
            mSeatTotalPrice = 0;
            mSelectedSeatCount = 0;
            mFoodPrice = 0;
            mGlassesPrice = 0;

            mSelectedDate = "";
            mMovieID = "";
            mSelectedScreenId = "";
            mSelectedTimeId = "";
            mvScreen = "";
            mSelectedVdt = "";
            mCurrency = "";
            mUserKey = "";

            mUserKey = bundle.getString(Constants.BNDL_USER_KEY);
            mSelectedDate = bundle.getString(Constants.BNDL_MOVIE_DATE_ID);
            mSelectedScreenId = bundle.getString(Constants.BNDL_MOVIE_CINEMA_ID);
            mSelectedTimeId = bundle.getString(Constants.BNDL_MOVIE_TIME_ID);
            mMovie = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE), MovieDetailsPojo.Movie.class);

//            ArrayList<MoviePreview> mMoviePreviewList = new ArrayList<>();

            if (mMovie != null) {
                mMovieID = mMovie.movieId;

                tvName.setText("" + mMovie.name);
                tvLanguage.setText("(" + mMovie.movieRestriction + ") | " + mMovie.language + " | " + mMovie.movieFormat);

                if (!TextUtils.isEmpty(mMovie.image)) {
                    Picasso.get().load(mMovie.image.replace("slider", "thumbnail")).into(ivMovie);
                }

                mIHF = mMovie.ihf;
                mTAX = mMovie.tax;

                if (!TextUtils.isEmpty(mMovie.mUserFirstName) && !TextUtils.isEmpty(mMovie.mUserLastName) && !TextUtils.isEmpty(mMovie.mUserEmail) && !TextUtils.isEmpty(mMovie.mUserPhone)) {

                    lnrPerson.setVisibility(View.VISIBLE);
                    tvFullname.setText(mMovie.mUserFirstName + " " + mMovie.mUserLastName);
                    if (mMovie.mUserPhone.length() > 10) {
                        tvEmailMobile.setText(mMovie.mUserEmail + "   |   " + "" + mMovie.mUserPhone);
                    } else {
                        tvEmailMobile.setText(mMovie.mUserEmail + "   |   " + mMovie.mUserCountryCode + "" + mMovie.mUserPhone);
                    }

                } else {

                    if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {

                        mMovie.mUserFirstName = MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, "");
                        mMovie.mUserLastName = MyPref.getPref(getActivity(), MyPref.PREF_USER_LAST_NAME, "");
                        mMovie.mUserEmail = MyPref.getPref(getActivity(), MyPref.PREF_USER_EMAIL, "");
                        mMovie.mUserPhone = MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "");

                        lnrPerson.setVisibility(View.VISIBLE);
                        tvFullname.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, "") + " " + MyPref.getPref(getActivity(), MyPref.PREF_USER_LAST_NAME, ""));
                        tvEmailMobile.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_EMAIL, "") + "   |   " + MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));

                        showPersonDetailsDialog();

                    } else {
                        lnrPerson.setVisibility(View.GONE);
                        showPersonDetailsDialog();
                    }

                }

            }

            mMovieDateList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_DATE_LIST), new TypeToken<List<MovieDetailsPojo.MovieDate>>() {
            }.getType());
            mMovieTheaterList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_CINEMA_LIST), new TypeToken<List<MovieTheaterListPojo.Theater>>() {
            }.getType());
            mMovieTimeList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_TIME_LIST), new TypeToken<List<MovieTheaterListPojo.ScreenTime>>() {
            }.getType());
            mMovieSeatList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_SEAT_LIST), new TypeToken<List<MovieSeat>>() {
            }.getType());
            if (bundle.containsKey(Constants.BNDL_MOVIE_FOOD_LIST)) {
                mComboList = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_FOOD_LIST), new TypeToken<List<FoodListPojo.Combo>>() {
                }.getType());
            }

            Spannable wordtoSpan = new SpannableString("" + tvAgree.getText().toString());
            wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_movie)), 15, tvAgree.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvAgree.setText(wordtoSpan);

            if (isTermsAccepted) {
                tvAgreeBox.setBackgroundResource(R.drawable.ic_checkmark_done);
                tvAgreeTermsWarning.setVisibility(View.GONE);
            } else {
                tvAgreeBox.setBackgroundResource(R.drawable.ic_checkmark_undone);
            }

            StringBuilder builderSeat = new StringBuilder();
            if (mMovieSeatList != null && mMovieSeatList.size() > 0) {

                for (int i = 0; i < mMovieSeatList.size(); i++) {
                    if (mMovieSeatList.get(i).status.equals(Constants.SEAT_SELECTED)) {
                        if (builderSeat.length() > 0) {
                            builderSeat.append("," + mMovieSeatList.get(i).seat);
                        } else {
                            builderSeat.append(mMovieSeatList.get(i).seat);
                        }
                        mSelectedSeatCount++;
                    }
                }

                tvSeatQuntity.setText("" + mSelectedSeatCount);
                if (mSelectedSeatCount > 1) {
                    tvSeat.setText(mSelectedSeatCount + " " + getString(R.string.seats_bracket));
                } else {
                    tvSeat.setText(mSelectedSeatCount + " " + getString(R.string.seat));
                }
                tvClassSeat.setText("" + builderSeat.toString());
                mClassSeat = "" + builderSeat.toString();


            }

            if (mMovieTheaterList != null && mMovieTheaterList.size() > 0) {
                for (int i = 0; i < mMovieTheaterList.size(); i++) {
                    if (mMovieTheaterList.get(i).screenId.equals(mSelectedScreenId)) {
                        tvScreen.setText("" + mMovieTheaterList.get(i).screenName);
                        tvPlace.setText("" + mMovieTheaterList.get(i).cinemaAddress);
                        break;
                    }
                }
            }

            for (int i = 0; i < mMovieTimeList.size(); i++) {
                if (mMovieTimeList.get(i).mtid.equals(mSelectedTimeId)) {
                    mvScreen = mMovieTimeList.get(i).mvScreen;
                    mSelectedVdt = mMovieTimeList.get(i).vdt;
                    mSeatPrice = mMovieTimeList.get(i).price;
                    mCurrency = mMovieTimeList.get(i).currency;
                    LogUtils.e("", mSelectedTimeId + " " + mvScreen + " " + mSelectedVdt + " " + mSeatPrice + " " + mCurrency);

                    StringBuilder stringBuilder = new StringBuilder();
                    if (!TextUtils.isEmpty(mSelectedDate)) {
                        try {
                            Date mDateTime = DateFormate.sdfMovieDate.parse(mSelectedDate);
                            String mMovieDayFormate = DateFormate.sdfPersonDisplayDate.format(mDateTime);
                            stringBuilder.append(mMovieDayFormate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            stringBuilder.append(mSelectedDate);
                        }
                    }

                    stringBuilder.append(" | " + mSelectedVdt);
//                    if (!TextUtils.isEmpty(mSelectedVdt)) {
//                        try {
//                            Date mDateTime = DateFormate.sdfEvent24Time.parse(mSelectedVdt);
//                            String mMovieTimeFormate = DateFormate.sdfEvent12Time.format(mDateTime);
//                            stringBuilder.append(" | " + mMovieTimeFormate);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//
//                        }
//                    }

                    tvDateTime.setText(stringBuilder.toString());
                    tvScreenName.setText("" + mMovieTimeList.get(i).cinemaScreenName);

                    mSeatTotalPrice = mSelectedSeatCount * mSeatPrice;

//                    MoviePreview mSeatPreview = new MoviePreview(getString(R.string.ticket_price_title) + "\n(" + mSelectedSeatCount + "x" + Utils.setPrice(mSeatPrice) + ")", mCurrency + " " + Utils.setPrice(mSeatTotalPrice));
//                    mMoviePreviewList.add(mSeatPreview);

                    break;
                }
            }


            List<FoodListPojo.Combo> mTempComboList = new ArrayList<>();
            if (mComboList != null && mComboList.size() > 0) {

                for (int i = 0; i < mComboList.size(); i++) {
                    if (mComboList.get(i).foodCount > 0) {
                        mTempComboList.add(mComboList.get(i));

//                        MoviePreview mFoodPreview = new MoviePreview(mComboList.get(i).name + "\n(" + mComboList.get(i).foodCount + "x" + Utils.setPrice(mComboList.get(i).price) + ")", mCurrency + " " + Utils.setPrice((mComboList.get(i).foodCount * mComboList.get(i).price)));
//                        mMoviePreviewList.add(mFoodPreview);

                        mFoodPrice = mFoodPrice + (mComboList.get(i).foodCount * mComboList.get(i).price);
                    }
                }

            }

            if (mMovie.glassesQuantity > 0) {

                mGlassesPrice = mMovie.glassesQuantity * mMovie.glassPrice;

//                MoviePreview mGlassesPreview = new MoviePreview(getString(R.string.glasses_price_title) + "\n(" + mMovie.glassesQuantity + "x" + Utils.setPrice(mMovie.glassPrice) + ")", mCurrency + " " + Utils.setPrice(mGlassesPrice));
//                mMoviePreviewList.add(mGlassesPreview);

            }

            mGrandPrice = mSeatTotalPrice + mTAX + mIHF + mFoodPrice + mGlassesPrice;

            int cnvFixedFee = 0, cnvTotalFee = 0;
            if (mMovie.cnvFixedFee > 0) {
                cnvFixedFee = mMovie.cnvFixedFee;
            }

            int cnvPerFee = 0;
            if (mMovie.cnvPerFee > 0) {
                cnvPerFee = (int) (mGrandPrice * ((float) mMovie.cnvPerFee / (float) 100));
            }

            cnvTotalFee = cnvFixedFee + cnvPerFee;

//            MoviePreview mCnvPreview = new MoviePreview(getString(R.string.convenience_chanrge_title), mCurrency + " " + Utils.setPrice(cnvTotalFee));
//            mMoviePreviewList.add(mCnvPreview);

            mTotalPrice = mGrandPrice + cnvTotalFee;
            totalBeforePromo = mTotalPrice;

//            tvGrandTotal.setText(mCurrency + " " + Utils.setPrice(mTotalPrice));
//            tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mTotalPrice));

            mMoviePreviewAdapter = new MoviePreviewAdapter(getActivity(), mCurrency);
            lvSeat.setAdapter(mMoviePreviewAdapter);

            getPaymentSummary();

        }

        if (Otapp.mCountryCodePojoList == null || Otapp.mCountryCodePojoList.size() == 0) {
            getCountryCodeList();
        } else {

        }

        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
            //  lnrApplyOffer.setVisibility(View.GONE);
        } else {
            lnrApplyOffer.setVisibility(View.VISIBLE);
        }

        tvBack.setOnClickListener(this);
        lnrBook.setOnClickListener(this);
        tvAgreeBox.setOnClickListener(this);
        lnrApplyOffer.setOnClickListener(this);
        lnrPerson.setOnClickListener(this);

//        etFirstName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                etFirstName.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ "));
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
    }

    private void getPaymentSummary() {

        if (getActivity() == null) {
            return;
        }

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }

        StringBuilder builderFood = new StringBuilder();
        if (mComboList != null && mComboList.size() > 0) {
            for (int i = 0; i < mComboList.size(); i++) {
                if (mComboList.get(i).foodCount > 0) {

                    for (int j = 0; j < mComboList.get(i).foodCount; j++) {
                        if (builderFood.length() > 0) {
                            builderFood.append("," + mComboList.get(i).mcid);
                        } else {
                            builderFood.append(mComboList.get(i).mcid);
                        }
                    }
                }
            }
        }

        if (mMovie.glassesQuantity > 0) {
            for (int j = 0; j < mMovie.glassesQuantity; j++) {
                if (builderFood.length() > 0) {
                    builderFood.append("," + "11");
                } else {
                    builderFood.append("11"); // 11 for movie 3d glasses
                }
            }
        }

        String mSelectedFoodValue = "";
        if (builderFood != null && builderFood.length() > 0) {
            mSelectedFoodValue = builderFood.toString();
        }

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("mv_screen", "" + mvScreen);
        jsonParams.put("movie_id", "" + mMovieID);
        jsonParams.put("dt", "" + mSelectedDate);
        jsonParams.put("show_time", "" + mSelectedVdt);
        jsonParams.put("combos", "" + mSelectedFoodValue);
        jsonParams.put("max_seats", "" + mSelectedSeatCount);
        jsonParams.put("mv_ukey", "" + mUserKey);

        Log.d("Log", "mv_screen " + mvScreen);
        Log.d("Log", "movie_id " + mMovieID);
        Log.d("Log", "dt " + mSelectedDate);
        Log.d("Log", "show_time " + mSelectedVdt);
        Log.d("Log", "combos " + mSelectedFoodValue);
        Log.d("Log", "max_seats " + mSelectedSeatCount);
        Log.d("Log", "mv_ukey " + mUserKey);


        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {

            jsonParams.put("promo_id", "" + mCouponResponsePojo.promo_id);
            Log.d("Log", "promo_id " + mCouponResponsePojo.promo_id);

        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<MoviePaymentSummaryPojo> mCall = mApiInterface.getMoviePaymentSummary(jsonParams);
        mCall.enqueue(new Callback<MoviePaymentSummaryPojo>() {
            @Override
            public void onResponse(Call<MoviePaymentSummaryPojo> call, Response<MoviePaymentSummaryPojo> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    MoviePaymentSummaryPojo mMoviePaymentSummaryPojo = response.body();
                    if (mMoviePaymentSummaryPojo != null) {
                        if (mMoviePaymentSummaryPojo.status.equalsIgnoreCase("200")) {
                            mPaymentSummaryPojo = mMoviePaymentSummaryPojo;
                            mPaymentSummaryList = mMoviePaymentSummaryPojo.data.fare;
                            strPromoCode = mMoviePaymentSummaryPojo.data.promoOffers.promoCode;
                            strPromoText = mMoviePaymentSummaryPojo.data.promoOffers.promoText;
                            promoFareAmt = mMoviePaymentSummaryPojo.data.promoFare.promoFare;
                            if (mPaymentSummaryList != null && mPaymentSummaryList.size() > 0) {

                                mTotalPrice = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).price;
                                // totalBeforePromo=mTotalPrice;
                                String title = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).lable;
                                if (!TextUtils.isEmpty(title)) {
                                    tvGrandTotalTitle.setText("" + title);
                                }
                                tvGrandTotal.setText(mCurrency + " " + Utils.setPrice(mTotalPrice));
                                tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mTotalPrice));

                                mMoviePreviewAdapter.addAll(mPaymentSummaryList.subList(0, mPaymentSummaryList.size() - 1));
                            }


                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviePaymentSummaryPojo> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });
    }

    /*@OnClick(R.id.cancelOffers)
    void onOfferCancel(){
        mTotalPrice=totalBeforePromo;
        tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mTotalPrice));
    }*/

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            String sCouponcode = MyPref.getPref(getContext(), "PromoCode", strPromoCode);
            if (sCouponcode != null) {
                if (sCouponcode.equals("")) {
                    popBackStack();
                } else {
                    removeCouponCode();
                }
            } else {
                removeCouponCode();
            }


        } else if (view == lnrBook) {

            if (!isTermsAccepted) {
                tvAgreeTermsWarning.setVisibility(View.VISIBLE);
                return;
            }

            if (TextUtils.isEmpty(mMovie.mUserFirstName) || TextUtils.isEmpty(mMovie.mUserLastName) || TextUtils.isEmpty(mMovie.mUserEmail) || TextUtils.isEmpty(mMovie.mUserPhone)) {
                showPersonDetailsDialog();
                return;
            }

            if (mPaymentSummaryPojo != null) {
                LogUtils.e("", "mMoviePaymentSummaryPojo is not null mTotalPrice:" + mTotalPrice);
                if (mTotalPrice > 0) {

//            if (isValidField()) {
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
                    bundle.putString(Constants.BNDL_MOVIE_PAYMENT_SUMMARY, new Gson().toJson(mPaymentSummaryPojo));
                    if (mCouponResponsePojo != null) {
                        bundle.putString(Constants.BNDL_MOVIE_COUPON, new Gson().toJson(mCouponResponsePojo));
                    } else {
                        bundle.putString(Constants.BNDL_MOVIE_COUPON, "");
                    }
                    if (mComboList != null && mComboList.size() > 0) {
                        bundle.putString(Constants.BNDL_MOVIE_FOOD_LIST, new Gson().toJson(mComboList));
                    }

                    bundle.putString(Constants.BNDL_FULLNAME, mMovie.mUserFirstName + " " + mMovie.mUserLastName);
                    bundle.putString(Constants.BNDL_EMAIL, mMovie.mUserEmail);
                    bundle.putString(Constants.BNDL_MOBILE_NUMBER, mobileNumber);
                    Log.d("Log", "mobile=" + mobileNumber);
                    bundle.putString("PromoCode", strPromoCode);
                    bundle.putString("PromoText", strPromoText);

                    switchFragment(MovieOrderPaymentFragment.newInstance(), MovieOrderPaymentFragment.Tag_MovieOrderPaymentFragment, bundle);
                    //   switchFragment(MovieOrderReviewFragment.newInstance(), MovieOrderReviewFragment.Tag_MovieOrderReviewFragment, bundle);
                    personalFlag = 0;
                } else {
                    callZeroPayment();
                }
            } else {
                LogUtils.e("", "mMoviePaymentSummaryPojo is null");
            }

        } else if (view == lnrPerson) {
            showPersonDetailsDialog();
        } else if (view == tvAgreeBox) {
            isTermsAccepted = !isTermsAccepted;
            if (isTermsAccepted) {
                tvAgreeBox.setBackgroundResource(R.drawable.ic_checkmark_done);
                tvAgreeTermsWarning.setVisibility(View.GONE);
            } else {
                tvAgreeBox.setBackgroundResource(R.drawable.ic_checkmark_undone);
            }
        } else if (view == lnrApplyOffer) {

            JSONObject jsonObject = new JSONObject();


            try {
                JSONArray jsonArray = new JSONArray();

                JSONObject jsonTktInfo = new JSONObject();
                jsonTktInfo.put("tkt_id", "" + mSelectedTimeId);
                jsonTktInfo.put("tot_tkt_id_tkts_count", "" + mSelectedSeatCount);
                jsonTktInfo.put("single_fare", "" + mSeatPrice);
                jsonTktInfo.put("seats", "" + mClassSeat);

                jsonArray.put(jsonTktInfo);

                jsonObject.put("tkts_info", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            strTicketInfo = jsonObject.toString();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BNDL_PROPERTY_ID, "" + mSelectedScreenId);
            bundle.putString(Constants.BNDL_TICKET_INFO, "" + jsonObject.toString());
            bundle.putString(Constants.BNDL_TOTAL_FARE, "" + promoFareAmt); //
            bundle.putString(Constants.BNDL_USER_KEY, "" + mUserKey);
            bundle.putString(Constants.BNDL_CURRENCY, "" + mCurrency);

            IntentHandler.startActivityForResult(getActivity(), CouponCodeActivity.class, bundle, Constants.RC_MOVIE_COUPON_CODE);


        }

    }

    public void removeCouponCode() {

        String mPromoCode = "";
        String mCurrency = "";
        String mPlatform = "A";


//        key=*MD5(SHA512(promo_code+prop_id+tkt_info+tot_fare+ukey+cur+platform+"pr0mOCode"))

        String mKey = Utils.getCouponCodeKey(mPromoCode + mSelectedScreenId + strTicketInfo + promoFareAmt + mUserKey + mCurrency + mPlatform + "pr0mOCode");

//        https://www.managemyticket.net/android/api/promo/Validate_Promo.php?promo_code=STANCHART800&prop_id=23&tkt_info={"tkts_info": [{"tkt_id": "4770", "tot_tkt_id_tkts_count": "2", "single_fare": "10000.00", "seats": "A1,A2"}]}&tot_fare=20000.00&ukey=TEST1234&cur=TSH&platform=W&key=b651cdfb275d53e9f39b1f379b26ac68

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }
        Map<String, String> jsonParams = new HashMap<>();

        jsonParams.put("promo_code", "" + mPromoCode);
        jsonParams.put("cur", "" + mCurrency);

        jsonParams.put("key", "" + mKey);
        jsonParams.put("platform", "" + mPlatform);
        jsonParams.put("ukey", "" + mUserKey);
        jsonParams.put("tot_fare", "" + promoFareAmt);
        jsonParams.put("prop_id", "" + mSelectedScreenId);
        jsonParams.put("tkt_info", "" + strTicketInfo);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<CouponResponsePojo> mCall = mApiInterface.validateCouponCode(jsonParams);
        mCall.enqueue(new Callback<CouponResponsePojo>() {
            @Override
            public void onResponse(Call<CouponResponsePojo> call, Response<CouponResponsePojo> response) {
                mCouponResponsePojo = response.body();
                if (Utils.isProgressDialogShowing()) {
                    Utils.closeProgressDialog();
                }
                if (mCouponResponsePojo != null) {
                    if (mCouponResponsePojo.status.equalsIgnoreCase("200")) {

                    } else {
                        MyPref.setPref(getActivity(), "ENABLED_PAYMENT_METHODS", "0");
                        if (mCouponResponsePojo.message.equals("Invalid currecny or currecny not found!!")) {
                            Utils.showToast(getActivity(), "Promo Code Successfully removed");
                        } else {
                            Utils.showToast(getActivity(), mCouponResponsePojo.message);
                        }
                        MyPref.setPref(getContext(), "PromoCode", "");
                        popBackStack();
                    }
                }
            }

            @Override
            public void onFailure(Call<CouponResponsePojo> call, Throwable t) {
                if (Utils.isProgressDialogShowing()) {
                    Utils.closeProgressDialog();
                }
            }
        });

    }


    private void callZeroPayment() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        if (!Utils.isProgressDialogShowing()) {
            Utils.showFlightProgressDialog(getActivity());
        }

        StringBuilder builderFood = new StringBuilder();
        if (mComboList != null && mComboList.size() > 0) {
            for (int i = 0; i < mComboList.size(); i++) {
                if (mComboList.get(i).foodCount > 0) {

                    for (int j = 0; j < mComboList.get(i).foodCount; j++) {
                        if (builderFood.length() > 0) {
                            builderFood.append("," + mComboList.get(i).mcid);
                        } else {
                            builderFood.append(mComboList.get(i).mcid);
                        }
                    }
                }
            }
        }

        if (mMovie.glassesQuantity > 0) {
            for (int j = 0; j < mMovie.glassesQuantity; j++) {
                if (builderFood.length() > 0) {
                    builderFood.append("," + "11");
                } else {
                    builderFood.append("11"); // 11 for movie 3d glasses
                }
            }
        }

        String mSelectedFoodValue = "";
        if (builderFood != null && builderFood.length() > 0) {
            mSelectedFoodValue = builderFood.toString();
        }

        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("mv_screen", "" + mvScreen);
        jsonParams.put("movie_id", "" + mMovieID);
        jsonParams.put("dt", "" + mSelectedDate);
        jsonParams.put("show_time", "" + mSelectedVdt);
        jsonParams.put("combos", "" + mSelectedFoodValue);
        jsonParams.put("max_seats", "" + mSelectedSeatCount);
        jsonParams.put("mv_ukey", "" + mUserKey);
        jsonParams.put("cust_name", "" + (mMovie.mUserFirstName + " " + mMovie.mUserLastName));
        jsonParams.put("cust_email", "" + mMovie.mUserEmail);
        jsonParams.put("cust_mob", "" + mMovie.mUserCountryCode + mMovie.mUserPhone);
        jsonParams.put("mv_mtid", "" + mSelectedTimeId);
        jsonParams.put("is_booking_from", "AND");

        if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
            jsonParams.put("cust_id", "" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, ""));
        }

        if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
            jsonParams.put("promo_id", "" + mCouponResponsePojo.promo_id);
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<MovieZeroResponse> mCall = mApiInterface.getMovieZeroPayment(jsonParams);
        mCall.enqueue(new Callback<MovieZeroResponse>() {
            @Override
            public void onResponse(Call<MovieZeroResponse> call, Response<MovieZeroResponse> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    MovieZeroResponse mMovieZeroResponse = response.body();
                    if (mMovieZeroResponse != null) {
                        if (mMovieZeroResponse.status.equalsIgnoreCase("200")) {
                            if (!TextUtils.isEmpty(mMovieZeroResponse.tkt_no)) {
                                setPaymentSucessful(mMovieZeroResponse.tkt_no);
                            }
                        } else {
                            Utils.showToast(getActivity(), mMovieZeroResponse.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieZeroResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void setPaymentSucessful(String mBookingID) {
        Utils.closeProgressDialog();
        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<MovieSuccessPojo> mCall = mApiInterface.getPaymentSuccess(mBookingID, Otapp.mUniqueID);
        mCall.enqueue(new Callback<MovieSuccessPojo>() {
            @Override
            public void onResponse(Call<MovieSuccessPojo> call, Response<MovieSuccessPojo> response) {

                Utils.closeProgressDialog();
                if (response.isSuccessful()) {

                    MovieSuccessPojo mMovieSuccessPojo = response.body();
                    if (mMovieSuccessPojo != null) {
                        if (mMovieSuccessPojo.status.equalsIgnoreCase("200")) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_MOVIE_RESPONSE, new Gson().toJson(mMovieSuccessPojo.data));
                            switchFragment(MovieOrderReviewFragment.newInstance(), MovieOrderReviewFragment.Tag_MovieOrderReviewFragment, bundle);
                        } else {
                            Utils.showToast(getActivity(), mMovieSuccessPojo.message);
                            popBackStack(ServiceFragment.Tag_ServiceFragment);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<MovieSuccessPojo> call, Throwable t) {
                LogUtils.e("", "onFailure::" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_MOVIE_COUPON_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                String mCouponResponse = data.getExtras().getString(Constants.BNDL_COUPON_RESPONSE);
                if (!TextUtils.isEmpty(mCouponResponse)) {
                    mCouponResponsePojo = new Gson().fromJson(mCouponResponse, CouponResponsePojo.class);
                    if (mCouponResponsePojo != null) {
//                        mTotalPrice = (int) (mTotalPrice - Float.parseFloat(mCouponResponsePojo.discount));
//
//                        tvGrandTotal.setText(mCurrency + " " + Utils.setPrice(mTotalPrice));
//                        tvTotal.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mTotalPrice));

                        getPaymentSummary();

                        //   lnrApplyOffer.setVisibility(View.GONE);


                    }
                }
            }

        }
    }

    //    private boolean isValidField() {
//
//        if (TextUtils.isEmpty(etFirstName.getText().toString())) {
//            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_fullname));
//            return false;
//        } else if (TextUtils.isEmpty(etEmailID.getText().toString())) {
//            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_email));
//            return false;
//        } else if (!Utils.isValidEmail(etEmailID.getText().toString())) {
//            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_email));
//            return false;
//        } else if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
//            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_phone));
//            return false;
//        } else if (etMobileNumber.getText().toString().length() < 9 || etMobileNumber.getText().toString().length() > 9) {
//            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone));
//            return false;
//        }
//
//        return true;
//    }

    private void showPersonDetailsDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_movie_user_info);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        EditText etEmailID = (EditText) dialog.findViewById(R.id.etEmailID);
        EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);
        EditText etFirstName = (EditText) dialog.findViewById(R.id.etFirstName);
        EditText etLastName = (EditText) dialog.findViewById(R.id.etLastName);
        View view = dialog.findViewById(R.id.divider);
        final TextView spinCountryCode = dialog.findViewById(R.id.spinCountryCode);
//        final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
        final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        final TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);

        etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
        if (MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "").equals("")) {
            spinCountryCode.setText("+255");
        }
        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //       etFirstName.setSelection(etFirstName.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //        etLastName.setSelection(etLastName.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //         etMobileNumber.setSelection(etMobileNumber.getText().length());
                if (charSequence.toString().trim().equals("")) {

//                    Toast.makeText(getContext(), "chage", Toast.LENGTH_SHORT).show();
                    spinCountryCode.setVisibility(View.VISIBLE);
                    enableCountryCode = 1;
                    strCountryCode = spinCountryCode.getText().toString();
                 /*   CountryCodePojo.CountryCode countryCode = (CountryCodePojo.CountryCode) spinCountryCode.getSelectedItem();
                    strCountryCode=countryCode.code;
                    Log.d("Log","country code 1 "+strCountryCode);*/
                    if (MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("") || MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")) {
                        // spinCountryCode.setSelection(214);
                        spinCountryCode.setText("+255");
                    }
                    etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
                    view.setVisibility(View.VISIBLE);
                    flagMovies = 0;
                }
                if (charSequence.toString().trim().length() < 8) {
                    mobFlag = 1;
                } else {
                    mobFlag = 0;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etEmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //        etEmailID.setSelection(etEmailID.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spinCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dailog_spin_country_code);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                EditText editSearchCountryCode = dialog.findViewById(R.id.searchCountryCode);
                ListView listCountryCode = dialog.findViewById(R.id.listViewCountryCode);
                TextView tvCancle = dialog.findViewById(R.id.tvCancel);

                mCountryCodeSpinAdapter = new CountryCodeListAdapter(getActivity(), Otapp.mCountryCodePojoList);
                listCountryCode.setAdapter(mCountryCodeSpinAdapter);

                listCountryCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (countryCodeFlag == 1) {
                            if (tempCountryCodeList.get(position).code.equals("")) {
                                spinCountryCode.setText("+255");
                                strCountryCode = spinCountryCode.getText().toString();
                            } else {
                                spinCountryCode.setText(tempCountryCodeList.get(position).code);
                                strCountryCode = spinCountryCode.getText().toString();
                            }
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
                            countryCodeFlag = 0;
                        } else {
                            if (Otapp.mCountryCodePojoList.get(position).code.equals("")) {
                                spinCountryCode.setText("+255");
                                strCountryCode = spinCountryCode.getText().toString();
                            } else {
                                spinCountryCode.setText(Otapp.mCountryCodePojoList.get(position).code);
                                strCountryCode = spinCountryCode.getText().toString();
                            }
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
                        }
                        dialog.dismiss();
                    }
                });

                editSearchCountryCode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String search = s.toString();
                        tempCountryCodeList = new ArrayList<>();
                        if (search.equals("")) {
                            mCountryCodeSpinAdapter = new CountryCodeListAdapter(getActivity(), Otapp.mCountryCodePojoList);
                            listCountryCode.setAdapter(mCountryCodeSpinAdapter);
                            mCountryCodeSpinAdapter.notifyDataSetChanged();
                            countryCodeFlag = 0;
                        } else {
                            for (int i = 0; i < Otapp.mCountryCodePojoList.size(); i++) {

                                if (Otapp.mCountryCodePojoList.get(i).name.toUpperCase().startsWith(search.toUpperCase()) || Otapp.mCountryCodePojoList.get(i).code.replaceAll("\\+", "").startsWith(search)) {
                                    tempCountryCodeList.add(Otapp.mCountryCodePojoList.get(i));
                                    countryCodeFlag = 1;
                                }

                            }
                            if (tempCountryCodeList.size() == 0) {
                                Utils.showToast(getActivity(), "No Country Code Found");
                            }
                            mCountryCodeSpinAdapter = new CountryCodeListAdapter(getActivity(), tempCountryCodeList);
                            listCountryCode.setAdapter(mCountryCodeSpinAdapter);
                            mCountryCodeSpinAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                tvCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        /*if (!TextUtils.isEmpty(mMovie.mUserFirstName) && !TextUtils.isEmpty(mMovie.mUserLastName) && !TextUtils.isEmpty(mMovie.mUserEmail) && !TextUtils.isEmpty(mMovie.mUserPhone)) {
            etFirstName.setText(mMovie.mUserFirstName);
            etLastName.setText(mMovie.mUserLastName);
            etEmailID.setText(mMovie.mUserEmail);
            etMobileNumber.setText(mMovie.mUserPhone);
        }*/
        String mob = MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "");
        if (mob.length() == 12) {

            if (MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, "").contains("+")) {
                etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
            } else {
                etMobileNumber.setText("+" + MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
            }
            etMobileNumber.setSelection(etMobileNumber.getText().length());
            spinCountryCode.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            flag = 1;
        } else {
            etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));
            etMobileNumber.setSelection(etMobileNumber.getText().length());
            spinCountryCode.setVisibility(View.VISIBLE);
            if (MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("") || MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals("+")) {
                /* spinCountryCode.setSelection(214);*/
                spinCountryCode.setText("+255");
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
            } else {
                spinCountryCode.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""));
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
            }
            view.setVisibility(View.VISIBLE);
            flag = 0;
        }

        if (personalFlag == 1) {
            String[] splitedName = new String[]{};
            String[] splitedMobile = new String[]{};
            if (!tvFullname.getText().toString().equals("")) {
                splitedName = tvFullname.getText().toString().split("\\s+");
                splitedMobile = tvEmailMobile.getText().toString().trim().split("\\s+");

                Log.d("Log", "EmailMobile " + tvEmailMobile.getText().toString().trim());
                Log.d("Log", "name " + splitedName.length);
                Log.d("Log", "mobile " + splitedMobile.length);

          /*  if(splitedName.length<=1||splitedMobile.length<=1){
                Utils.showToast(getActivity(), "Please! Fill All Details");
            }else {*/
                mMovie.mUserFirstName = splitedName[0];
                mMovie.mUserLastName = splitedName[1];
                mMovie.mUserEmail = splitedMobile[0];
                Log.d("Log", "first " + tvEmailMobile.getText().toString().trim());
                mMovie.mUserPhone = splitedMobile[2].replaceAll("\\s", "");
                Log.d("Log", "second " + mMovie.mUserPhone);
                etFirstName.setText(mMovie.mUserFirstName);
                etLastName.setText(mMovie.mUserLastName);
                etLastName.setText(mMovie.mUserLastName);
                etEmailID.setText(mMovie.mUserEmail);
                if (enableCountryCode == 1) {
                    spinCountryCode.setText(strCountryCode);
                    etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
                }
                Log.d("Log", "country code 3 " + strCountryCode);
                if (enableCountryCode == 1) {
                    spinCountryCode.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    strCountryCode = spinCountryCode.getText().toString();

                    spinCountryCode.setText(strCountryCode);
                    etMobileNumber.setText(strmobileNumber);
                    etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});

                } else {
                    etMobileNumber.setText(strmobileNumber);
                    etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13 - spinCountryCode.getText().length())});
                }

                /*  }*/
            } else {
                Utils.showToast(getActivity(), "Please! Fill All Details");
            }
        } else {
            etFirstName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, ""));
            etLastName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_LAST_NAME, ""));
            etEmailID.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_EMAIL, ""));
            spinCountryCode.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""));

        }
       /* etFirstName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_FIRST_NAME, ""));
        etLastName.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_LAST_NAME, ""));*/
        //etEmailID.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_EMAIL, ""));
        etFirstName.setSelection(etFirstName.getText().length());
        etLastName.setSelection(etLastName.getText().length());
        etEmailID.setSelection(etEmailID.getText().length());
        etMobileNumber.setSelection(etMobileNumber.getText().length());
        //  spinCountryCode.setSelection(214);
        spinCountryCode.setText(strCountryCode);

        //  etMobileNumber.setText(MyPref.getPref(getActivity(), MyPref.PREF_USER_MOB, ""));


       /* CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
        spinCountryCode.setAdapter(mCountryCodeSpinAdapter);*/
       /* if (!TextUtils.isEmpty(mMovie.mUserCountryCode)) {
            spinCountryCode.setSelection(Utils.getPreviousCountryPosition(getActivity(), mMovie.mUserCountryCode));
        } else {
            spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
        }*/
       /* if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
            spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
        } else {
            for(int i=0;i<Otapp.mCountryCodePojoList.size();i++){
                if(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").equals(Otapp.mCountryCodePojoList.get(i).code)){
                    spinCountryCode.setSelection(i);
                    break;
                }
            }
        }*/
       /* spinCountryCode.setSelection(countryCodePosition);
        spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                mMobNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobNumberMaxLength)});
                CountryCodePojo.CountryCode countryCode =  Otapp.mCountryCodePojoList.get(i);
                countryCodePosition=i;
                Log.d("Log","coutnry code position "+i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/

        etFirstName.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etFirstName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });

        etLastName.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etLastName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                personalFlag = 1;
                strmobileNumber = etMobileNumber.getText().toString();
                strCountryCode = spinCountryCode.getText().toString();

                if (TextUtils.isEmpty(etFirstName.getText().toString())) {
                    Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_firstname));
                    return;
                } else if (TextUtils.isEmpty(etLastName.getText().toString())) {
                    Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_lastname));
                    return;
                } else if (TextUtils.isEmpty(etEmailID.getText().toString())) {
                    Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_email));
                    return;
                } else if (!Utils.isValidEmail(etEmailID.getText().toString())) {
                    Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_email));
                    return;
                } else if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                    Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_phone));
                    return;
                } /*else  if(enableCountryCode==1){
                    if (etMobileNumber.getText().toString().length() < mMobNumberMaxLength || etMobileNumber.getText().toString().length() > mMobNumberMaxLength) {
                        Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobNumberMaxLength));
                        return ;
                    }
                }*/ else if (MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, "").length() > 1) {
                    if (etMobileNumber.getText().toString().length() + spinCountryCode.getText().toString().length() != 13) {
                        Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone_number));
                        return;
                    }
                } else {
                    if (enableCountryCode == 1) {
                        if (etMobileNumber.getText().toString().length() + spinCountryCode.getText().toString().length() != 13) {
                            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone_number));
                            return;
                        }
                    } else {
                        if (etMobileNumber.getText().toString().length() != 13) {
                            Utils.showToast(getActivity(), "" + getString(R.string.alert_enter_valid_phone_number));
                            return;
                        }
                    }

                }
                    /*if(mobFlag==1){
                    Utils.showToast(getActivity(), "" + String.format("Enter Valid Number", "" + mMobNumberMaxLength));
                    return ;
                }else if(personalFlag==1){
                    if(etMobileNumber.getText().toString().length()!=13) {
                        Utils.showToast(getActivity(), "" + String.format("Enter Valid Number", "" + mMobNumberMaxLength));
                        return;
                    }
                }*/

                mMovie.mUserFirstName = etFirstName.getText().toString();
                mMovie.mUserLastName = etLastName.getText().toString();
                mMovie.mUserEmail = etEmailID.getText().toString();
                mMovie.mUserPhone = etMobileNumber.getText().toString();
                if (Otapp.mCountryCodePojoList != null && Otapp.mCountryCodePojoList.size() > 0) {
                    mMovie.mUserCountryCode = spinCountryCode.getText().toString();
                } else {
                    mMovie.mUserCountryCode = MyPref.PREF_DEFAULT_COUNTRY_CODE;
                }
                if (etMobileNumber.getText().length() > 10) {
                    mobileNumber = etMobileNumber.getText().toString();
                    Log.d("Log", "Mobile number " + mobileNumber);
                } else {
                    mobileNumber = spinCountryCode.getText().toString() + etMobileNumber.getText().toString();
                }
                lnrPerson.setVisibility(View.VISIBLE);
                tvFullname.setText(mMovie.mUserFirstName + " " + mMovie.mUserLastName);
                if (mMovie.mUserPhone.length() > 10) {
                    tvEmailMobile.setText(mMovie.mUserEmail + "   |   " + "" + mMovie.mUserPhone);
                } else {
                    tvEmailMobile.setText(mMovie.mUserEmail + "   |   " + mMovie.mUserCountryCode + "" + mMovie.mUserPhone);
                }

                Utils.hideKeyboard(getActivity());


                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovie != null) {
                    if (mMovie.mUserFirstName != null) {
                        if (Otapp.mCountryCodePojoList != null && Otapp.mCountryCodePojoList.size() > 0) {
                            mMovie.mUserCountryCode = spinCountryCode.getText().toString();
                        } else {
                            mMovie.mUserCountryCode = MyPref.PREF_DEFAULT_COUNTRY_CODE;
                        }

                        tvFullname.setText(mMovie.mUserFirstName + " " + mMovie.mUserLastName);
                        if (mMovie.mUserPhone.length() > 10) {
                            tvEmailMobile.setText(mMovie.mUserEmail + "   |   " + "" + mMovie.mUserPhone);
                        } else {
                            tvEmailMobile.setText(mMovie.mUserEmail + "   |   " + mMovie.mUserCountryCode + "" + mMovie.mUserPhone);
                        }
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
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

    private void getCountryCodeList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());


        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("user_token", "" + Otapp.mUniqueID);

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<CountryCodePojo> mCall = mApiInterface.getCountryCodeList(jsonParams);
        mCall.enqueue(new Callback<CountryCodePojo>() {
            @Override
            public void onResponse(Call<CountryCodePojo> call, Response<CountryCodePojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    CountryCodePojo mCountryCodePojo = response.body();
                    if (mCountryCodePojo != null) {
                        if (mCountryCodePojo.status.equalsIgnoreCase("200")) {

                            Otapp.mCountryCodePojoList = mCountryCodePojo.data;
                            Otapp.mAdsPojoList = mCountryCodePojo.ad5;

                        } else {
                            Utils.showToast(getActivity(), "" + mCountryCodePojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryCodePojo> call, Throwable t) {
                LogUtils.e("", "onFailure:" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });

    }

}









