package com.otapp.net.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.R;
import com.otapp.net.adapter.CountryCodeSpinAdapter;
import com.otapp.net.adapter.MoviePaymentAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.CouponResponsePojo;
import com.otapp.net.model.FoodListPojo;
import com.otapp.net.model.MovieDetailsPojo;
import com.otapp.net.model.MoviePaymentSummaryPojo;
import com.otapp.net.model.MovieSeat;
import com.otapp.net.model.MovieTheaterListPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieOrderPaymentFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_MovieOrderPaymentFragment = "Tag_" + "MovieOrderPaymentFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPlace)
    TextView tvPlace;
    @BindView(R.id.tvDate)
    TextView tvDate;
    //    @BindView(R.id.tvFoodBeverage)
//    TextView tvFoodBeverage;
//    @BindView(R.id.tvGlassesPrice)
//    TextView tvGlassesPrice;
//    @BindView(R.id.tvConvenienceCharge)
//    TextView tvConvenienceCharge;
//    @BindView(R.id.tvSubTotal)
//    TextView tvSubTotal;
//    @BindView(R.id.tvIHF)
//    TextView tvIHF;
//    @BindView(R.id.tvTaxes)
//    TextView tvTaxes;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.ivAds1)
    ImageView ivAds1;
    @BindView(R.id.ivAds2)
    ImageView ivAds2;
    //    @BindView(R.id.rlFoodBeverage)
//    RelativeLayout rlFoodBeverage;
//    @BindView(R.id.rlConvienceCharge)
//    RelativeLayout rlConvienceCharge;
//    @BindView(R.id.rlGlasses)
//    RelativeLayout rlGlasses;
    @BindView(R.id.rltDebicCard)
    RelativeLayout rltDebicCard;
    @BindView(R.id.rltTigoPesa)
    RelativeLayout rltTigoPesa;
    @BindView(R.id.rltMPesa)
    RelativeLayout rltMPesa;
    @BindView(R.id.rltAirtelMoney)
    RelativeLayout rltAirtelMoney;
    @BindView(R.id.lvSeat)
    ListView lvSeat;
    @BindView(R.id.tvTotal)
    TextView tvTotal;

    String strCardNo, strCardType, strCardExpiryDt, strCardCvv, strMonth, strYear;
    int year = 0, expYear = 0;
    int month = 1;
    int promoFlaq = 0;

    String strPromoCode = "", strPromoText = "";
    private MoviePaymentAdapter mMoviePaymentAdapter;


    private int mSeatPrice, mIHF, mTAX, mSeatTotalPrice, mSelectedSeatCount, mFoodCount, mGlassesPrice, cnvTotalFee = 0;
    private float mTotalAmount;
    private String mSelectedDate = "", mMovieID = "", mSelectedScreenId = "", mSelectedTimeId = "", mvScreen = "", mSelectedVdt = "", mCurrency = "", mUserKey = "", mSelectedFoodValue = "";
    private String mUserEmail = "", mUserFullName = "", mUserMobileNumber = "";
    List<MovieDetailsPojo.MovieDate> mMovieDateList;
    List<MovieTheaterListPojo.Theater> mMovieTheaterList;
    List<MovieTheaterListPojo.ScreenTime> mMovieTimeList;
    private MovieDetailsPojo.Movie mMovie;
    private MoviePaymentSummaryPojo mMoviePaymentSummaryPojo;
    private CouponResponsePojo mCouponResponsePojo;
    List<MovieSeat> mMovieSeatList;
    List<FoodListPojo.Combo> mComboList;

    String sCRDBCharges = "";
    String sMPesaCharges = "";
    String sTigoCharges = "";
    String sAirtelCharges = "";

    public boolean isCreditCardEnabled = false, isMpesaEnabled = false, isTigoEnabled = false, isAirtelMoneyEnabled = false;
    private int mMobInnerNumberMaxLength = 9;

    public static MovieOrderPaymentFragment newInstance() {
        MovieOrderPaymentFragment fragment = new MovieOrderPaymentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_movie_order_payment, container, false);
        ButterKnife.bind(this, mView);
        tvPay.setVisibility(View.GONE);
        InitializeControls();

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
            mFoodCount = 0;
            mTotalAmount = 0;

            mUserFullName = bundle.getString(Constants.BNDL_FULLNAME);
            mUserEmail = bundle.getString(Constants.BNDL_EMAIL);
            mUserMobileNumber = bundle.getString(Constants.BNDL_MOBILE_NUMBER);


            mUserKey = bundle.getString(Constants.BNDL_USER_KEY);
            mSelectedDate = bundle.getString(Constants.BNDL_MOVIE_DATE_ID);
            mSelectedScreenId = bundle.getString(Constants.BNDL_MOVIE_CINEMA_ID);
            mSelectedTimeId = bundle.getString(Constants.BNDL_MOVIE_TIME_ID);
            String mCoupon = bundle.getString(Constants.BNDL_MOVIE_COUPON);

            strPromoCode = bundle.getString("PromoCode");
            strPromoText = bundle.getString("PromoText");
            try {
                strPromoText = new String(Base64.decode(strPromoText, Base64.DEFAULT), "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("Log", "Er : " + e.getMessage());
            }

            if (!TextUtils.isEmpty(mCoupon)) {
                mCouponResponsePojo = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_COUPON), CouponResponsePojo.class);
            }
            mMoviePaymentSummaryPojo = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE_PAYMENT_SUMMARY), MoviePaymentSummaryPojo.class);
            mMovie = new Gson().fromJson(bundle.getString(Constants.BNDL_MOVIE), MovieDetailsPojo.Movie.class);
            if (mMovie != null) {
                mMovieID = mMovie.movieId;

                tvName.setText("" + mMovie.name);
                mIHF = mMovie.ihf;
                mTAX = mMovie.tax;

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

            if (mMovieSeatList != null && mMovieSeatList.size() > 0) {
                for (int i = 0; i < mMovieSeatList.size(); i++) {
                    if (mMovieSeatList.get(i).status.equals(Constants.SEAT_SELECTED)) {
                        mSelectedSeatCount++;
                    }
                }
            }

            if (mMovieTheaterList != null && mMovieTheaterList.size() > 0) {
                for (int i = 0; i < mMovieTheaterList.size(); i++) {
                    if (mMovieTheaterList.get(i).screenId.equals(mSelectedScreenId)) {
                        tvPlace.setText("" + mMovieTheaterList.get(i).screenName);
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

                    tvDate.setText(mSelectedDate + " | " + mSelectedVdt);
                    mSeatTotalPrice = mSelectedSeatCount * mSeatPrice;

//                    if (mIHF > 0) {
//                        tvIHF.setText(mCurrency + " " + Utils.setPrice(mIHF));
//                    }
//
//                    if (mTAX > 0) {
//                        tvTaxes.setText(mCurrency + " " + Utils.setPrice(mTAX));
//                    }
//
//                    tvSubTotal.setText(mCurrency + " " + Utils.setPrice(mSeatTotalPrice));

                    break;
                }
            }

            mMoviePaymentAdapter = new MoviePaymentAdapter(getActivity(), mCurrency);
            lvSeat.setAdapter(mMoviePaymentAdapter);

            if (mMoviePaymentSummaryPojo != null) {

                List<MoviePaymentSummaryPojo.Summary> mPaymentSummaryList = mMoviePaymentSummaryPojo.data.fare;

                if (mPaymentSummaryList != null && mPaymentSummaryList.size() > 0) {

                    mTotalAmount = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).price;
                    tvPay.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mTotalAmount));
                    tvTotal.setText(mCurrency + " " + Utils.setPrice(mTotalAmount));
                    mMoviePaymentAdapter.addAll(mPaymentSummaryList.subList(0, mPaymentSummaryList.size() - 1));
                }

                List<MoviePaymentSummaryPojo.PaymentAllowed> paymentAllowed = mMoviePaymentSummaryPojo.data.paymentAllowed;
                if (paymentAllowed != null && paymentAllowed.size() > 0) {
                    for (int i = 0; i < paymentAllowed.size(); i++) {
                        LogUtils.e("", "paymentName::" + paymentAllowed.get(i).paymentName);
                        if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("Credit/Debit Card")) {
                            sCRDBCharges = paymentAllowed.get(i).sExtraPGWCharges;
                            if (mCouponResponsePojo != null && !TextUtils.isEmpty(mCouponResponsePojo.applicable_pgws)) {
                                if (mCouponResponsePojo.applicable_pgws.contains("0") || mCouponResponsePojo.applicable_pgws.contains("1")) {
                                    isCreditCardEnabled = true;
                                }
                            } else {
                                isCreditCardEnabled = true;
                            }

                        } else if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("Tigo Pesa")) {
                            sTigoCharges = paymentAllowed.get(i).sExtraPGWCharges;
                            if (mCouponResponsePojo != null && !TextUtils.isEmpty(mCouponResponsePojo.applicable_pgws)) {
                                if (mCouponResponsePojo.applicable_pgws.contains("0") || mCouponResponsePojo.applicable_pgws.contains("2")) {
                                    isTigoEnabled = true;
                                }
                            } else {
                                isTigoEnabled = true;
                            }
                        } else if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("MPESA")) {
                            sMPesaCharges = paymentAllowed.get(i).sExtraPGWCharges;
                            if (mCouponResponsePojo != null && !TextUtils.isEmpty(mCouponResponsePojo.applicable_pgws)) {
                                if (mCouponResponsePojo.applicable_pgws.contains("0") || mCouponResponsePojo.applicable_pgws.contains("3")) {
                                    isMpesaEnabled = true;
                                }
                            } else {
                                isMpesaEnabled = true;
                            }
                        } else if (paymentAllowed.get(i).paymentName.equalsIgnoreCase("Airtel")) {
                            sAirtelCharges = paymentAllowed.get(i).sExtraPGWCharges;
                            if (mCouponResponsePojo != null && !TextUtils.isEmpty(mCouponResponsePojo.applicable_pgws)) {
                                if (mCouponResponsePojo.applicable_pgws.contains("0") || mCouponResponsePojo.applicable_pgws.contains("4")) {
                                    isAirtelMoneyEnabled = true;
                                }
                            } else {
                                isAirtelMoneyEnabled = true;
                            }
                        }
                    }

                } else {
                    LogUtils.e("", "paymentAllowed is null");
                    isCreditCardEnabled = false;
                    isTigoEnabled = false;
                    isMpesaEnabled = false;
                    isAirtelMoneyEnabled = false;
                }

            }

            LogUtils.e("", "isCreditCardEnabled::" + isCreditCardEnabled);
            LogUtils.e("", "isTigoEnabled::" + isTigoEnabled);
            LogUtils.e("", "isMpesaEnabled::" + isMpesaEnabled);
            LogUtils.e("", "isAirtelMoneyEnabled::" + isAirtelMoneyEnabled);

//            List<FoodListPojo.Combo> mTempComboList = new ArrayList<>();

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


//                        mTempComboList.add(mComboList.get(i));
                        mFoodCount = mFoodCount + (mComboList.get(i).foodCount * mComboList.get(i).price);
                    }
                }

//                if (mFoodCount > 0) {
//                    rlFoodBeverage.setVisibility(View.VISIBLE);
//
//                    tvFoodBeverage.setText(mCurrency + " " + Utils.setPrice(mFoodCount));
//
//                } else {
//                    rlFoodBeverage.setVisibility(View.GONE);
//                }

            } else {
//                rlFoodBeverage.setVisibility(View.GONE);
            }

            LogUtils.e("", "mMovie.glassesQuantity::" + mMovie.glassesQuantity);
            if (mMovie.glassesQuantity > 0) {
                mGlassesPrice = mMovie.glassesQuantity * mMovie.glassPrice;
//                tvGlassesPrice.setText(mCurrency + " " + mGlassesPrice);
//                rlGlasses.setVisibility(View.VISIBLE);
//                for (int j = 0; j < mMovie.glassesQuantity; j++) {
//                    if (builderFood.length() > 0) {
//                        builderFood.append("," + "11");
//                    } else {
//                        builderFood.append("11"); // 11 for movie 3d glasses
//                    }
//                }
            } else {
//                rlGlasses.setVisibility(View.GONE);
            }


            if (builderFood != null && builderFood.length() > 0) {
                mSelectedFoodValue = builderFood.toString();
            }

            int mGrandPrice = mSeatTotalPrice + mTAX + mIHF + mFoodCount + mGlassesPrice;

            int cnvFixedFee = 0;
            if (mMovie.cnvFixedFee > 0) {
                cnvFixedFee = mMovie.cnvFixedFee;
            }

            int cnvPerFee = 0;
            if (mMovie.cnvPerFee > 0) {
                cnvPerFee = (int) (mGrandPrice * ((float) mMovie.cnvPerFee / (float) 100));
            }

            cnvTotalFee = cnvFixedFee + cnvPerFee;

//            tvConvenienceCharge.setText(mCurrency + " " + Utils.setPrice(cnvTotalFee));

//            mTotalAmount = mSeatTotalPrice + mTAX + mIHF + mFoodCount + mGlassesPrice + cnvTotalFee;
//            tvPay.setText(getString(R.string.pay) + " " + mCurrency + " " + Utils.setPrice(mTotalAmount));


        }

        if (Otapp.mAdsPojoList != null && Otapp.mAdsPojoList.size() > 0) {
            for (int i = 0; i < Otapp.mAdsPojoList.size(); i++) {
                CountryCodePojo.Ad5 mAds = Otapp.mAdsPojoList.get(i);
                if (mAds != null) {
                    if (mAds.page.equalsIgnoreCase("Payment page") && mAds.location.equalsIgnoreCase("Center Slide 1")) {
                        if (!TextUtils.isEmpty(mAds.image_path)) {
                            ivAds1.setVisibility(View.VISIBLE);
                            Picasso.get().load(mAds.image_path).into(ivAds1);
                            ivAds1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (!TextUtils.isEmpty(mAds.is_open_in_app)) {

                                        if (mAds.is_open_in_app.equals("1")) {

                                            if (!TextUtils.isEmpty(mAds.link)) {
                                                setModule(mAds.link);
                                            }

                                        } else if (mAds.is_open_in_app.equals("0")) {

                                            if (!TextUtils.isEmpty(mAds.link)) {
                                                String url = mAds.link;
                                                if (!url.startsWith("http")) {
                                                    url = "http://" + url;
                                                }
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                startActivity(intent);
                                            }

                                        }
                                    }
                                }
                            });
                        } else {
                            ivAds1.setVisibility(View.GONE);
                        }
                    } else if (mAds.page.equalsIgnoreCase("Payment page") && mAds.location.equalsIgnoreCase("Center Slide 2")) {
                        if (!TextUtils.isEmpty(mAds.image_path)) {
                            ivAds2.setVisibility(View.VISIBLE);
                            Picasso.get().load(mAds.image_path).into(ivAds2);
                            ivAds2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (!TextUtils.isEmpty(mAds.is_open_in_app)) {

                                        if (mAds.is_open_in_app.equals("1")) {

                                            if (!TextUtils.isEmpty(mAds.link)) {
                                                setModule(mAds.link);
                                            }

                                        } else if (mAds.is_open_in_app.equals("0")) {

                                            if (!TextUtils.isEmpty(mAds.link)) {
                                                String url = mAds.link;
                                                if (!url.startsWith("http")) {
                                                    url = "http://" + url;
                                                }
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                startActivity(intent);
                                            }

                                        }
                                    }
                                }
                            });
                        } else {
                            ivAds2.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }

        tvBack.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        rltDebicCard.setOnClickListener(this);
        rltTigoPesa.setOnClickListener(this);
        rltMPesa.setOnClickListener(this);
        rltAirtelMoney.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String sApplicablePGWS = MyPref.getPref(getContext(), "ENABLED_PAYMENT_METHODS", "0");
        Log.d("Log", "Applicable PGW " + sApplicablePGWS);
        if (view == tvBack) {
            popBackStack();
        } else if (view == rltDebicCard) {
            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("1") || sApplicablePGWS.contains("5")) {
                if (sApplicablePGWS.contains("1")) {
                    if (mCurrency.equals("TSH")) {
                        if (!isCreditCardEnabled) {
                            Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                            return;
                        }

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_card_info);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        Spinner spinnerCardType = dialog.findViewById(R.id.spinnerCardType);
                        EditText editTextCardNo = dialog.findViewById(R.id.editCardNo);
                        EditText editTextCvv = dialog.findViewById(R.id.editCvv);
                        TextView btSubmit = dialog.findViewById(R.id.tvSubmit);
                        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                        TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                        TextView tvPromoText = dialog.findViewById(R.id.promotText);
                        Spinner spinnerMonth = dialog.findViewById(R.id.spinnerMonth);
                        Spinner spinnerYear = dialog.findViewById(R.id.spinnerYear);
                        if (sCRDBCharges.equals("")){
                            tvCharges.setVisibility(View.GONE);
                        }else if (sCRDBCharges.equals("0.00")){
                            tvCharges.setVisibility(View.GONE);
                        }else {
                            tvCharges.setText("Extra Charges Applicable : "+mCurrency+" "+sCRDBCharges);
                            tvCharges.setVisibility(View.VISIBLE);
                        }
                        editTextCardNo.addTextChangedListener(new TextWatcher() {
                            private static final char space = ' ';
                            int count = 0;

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    int len = s.toString().length();
                                    String cardDigit[] = strPromoCode.split(",");

                                    String str = s.toString().replaceAll("\\s", "");
                                    Log.d("Log", "Length :- " + len);
                                    if (len == 7) {
                                        for (int i = 0; i < cardDigit.length; i++) {
                                            if (str.equals(cardDigit[i].toString())) {
                                                tvPromoText.setVisibility(View.VISIBLE);
                                                tvPromoText.setText(strPromoText);
                                                promoFlaq = 1;
                                                Log.d("Log", "length " + len);
                                                break;
                                            } else {
                                                tvPromoText.setVisibility(View.GONE);
                                                Log.d("Log", "length " + len);
                                            }
                                        }
                                    } else {
                                        if (promoFlaq == 1) {
                                            tvPromoText.setVisibility(View.VISIBLE);
                                            tvPromoText.setText(strPromoText);
                                        } else {
                                            tvPromoText.setVisibility(View.GONE);
                                            promoFlaq = 0;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (count <= editTextCardNo.getText().toString().length()
                                        && (editTextCardNo.getText().toString().length() == 4
                                        || editTextCardNo.getText().toString().length() == 9
                                        || editTextCardNo.getText().toString().length() == 14)) {
                                    editTextCardNo.setText(editTextCardNo.getText().toString() + " ");
                                    int pos = editTextCardNo.getText().length();
                                    editTextCardNo.setSelection(pos);
                                } else if (count >= editTextCardNo.getText().toString().length()
                                        && (editTextCardNo.getText().toString().length() == 4
                                        || editTextCardNo.getText().toString().length() == 9
                                        || editTextCardNo.getText().toString().length() == 14)) {
                                    editTextCardNo.setText(editTextCardNo.getText().toString().substring(0, editTextCardNo.getText().toString().length() - 1));
                                    int pos = editTextCardNo.getText().length();
                                    editTextCardNo.setSelection(pos);
                                }
                                count = editTextCardNo.getText().toString().length();
                            }
                        });

                        List<String> cardTypeList = new ArrayList<>();
                        cardTypeList.add("Select Card Type");
                        cardTypeList.add("Mastercard");
                        cardTypeList.add("Visa");
                        cardTypeList.add("American Express");
                        cardTypeList.add("Amex Corporate Purchase Card");
                        cardTypeList.add("Diners Club");
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, cardTypeList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCardType.setAdapter(dataAdapter);

                        year = Calendar.getInstance().get(Calendar.YEAR);
                        expYear = year;
                        List<String> yearList = new ArrayList<>();
                        List<String> monthList = new ArrayList<>();
                        for (int i = 0; i < 32; i++) {
                            yearList.add("" + year);
                            year++;
                        }
                        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, yearList);
                        spinnerYear.setAdapter(yearAdapter);

                        for (int i = month; i <= 12; i++) {
                            if (i <= 9) {
                                monthList.add("0" + i);
                            } else {
                                monthList.add("" + i);
                            }
                        }
                        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, monthList);
                        spinnerMonth.setAdapter(monthAdapter);
/*
                        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int currnetYear = Integer.parseInt(spinnerYear.getSelectedItem().toString());
                                monthList.clear();
                                if (expYear == currnetYear) {
                                    DateFormat dateFormat = new SimpleDateFormat("MM");
                                    Date date = new Date();
                                    Log.d("Month", dateFormat.format(date));
                                    month = Integer.parseInt(dateFormat.format(date));
                                    for (int i = month; i <= 12; i++) {
                                        if (i <= 9) {
                                            monthList.add("0" + i);
                                        } else {
                                            monthList.add("" + i);
                                        }
                                    }
                                } else {
                                    for (int i = 1; i <= 12; i++) {
                                        if (i <= 9) {
                                            monthList.add("0" + i);
                                        } else {
                                            monthList.add("" + i);
                                        }
                                    }

                                }

                                ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, monthList);
                                spinnerMonth.setAdapter(monthAdapter);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
*/


                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                    dialog.cancel();
                                }

                            }
                        });

                        btSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String lastDigitYear;
                                String cardType = "";

                                if (spinnerCardType.getSelectedItem().toString().equals("Select Card Type")) {
                                    Utils.showToast(getActivity(), getString(R.string.alert_card_card_type));
                                    return;
                                } else {
                                    strCardType = spinnerCardType.getSelectedItem().toString();
                                    if (strCardType.equals("Mastercard")) {
                                        cardType = "Mastercard";
                                    } else if (strCardType.equals("Visa")) {
                                        cardType = "Visa";
                                    } else if (strCardType.equals("American Express")) {
                                        cardType = "Amex";
                                    } else if (strCardType.equals("Amex Corporate Purchase Card")) {
                                        cardType = "AmexPurchaseCard";
                                    } else if (strCardType.equals("Visa")) {
                                        cardType = "Dinersclub";
                                    }
                                }
                                if (TextUtils.isEmpty(editTextCardNo.getText().toString())) {
                                    Utils.showToast(getActivity(), getString(R.string.alert_card_number));
                                    return;
                                } else {
                                    strCardNo = editTextCardNo.getText().toString().replaceAll("\\s", "");
                                }
                                if (editTextCardNo.getText().length() < 16) {
                                    Utils.showToast(getActivity(), getString(R.string.alart_valid_card_number));
                                    return;
                                } else {
                                    strCardNo = editTextCardNo.getText().toString().replaceAll("\\s", "");
                                }

                                if (TextUtils.isEmpty(editTextCvv.getText().toString())) {
                                    Utils.showToast(getActivity(), getString(R.string.alert_card_cvv));
                                    return;
                                } else if (editTextCvv.getText().length() < 3) {
                                    Utils.showToast(getActivity(), getString(R.string.aler_card_cvv_lenth));
                                    return;
                                } else {
                                    strCardCvv = editTextCvv.getText().toString();
                                }

                                strMonth = spinnerMonth.getSelectedItem().toString();
                                strYear = spinnerYear.getSelectedItem().toString();
                                lastDigitYear = strYear.substring(2, 4);


                                Log.d("Log", "exp " + strCardExpiryDt);
                                Log.d("Log", "cvv " + strCardCvv);
                                Log.d("Log", "Year " + lastDigitYear);


                                if (mTotalAmount >= 100) {

                                    if (!Utils.isInternetConnected(getActivity())) {
                                        Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                        return;
                                    }

                                    String mCustId = "";
                                    if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                        mCustId = MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                                    }

                                    String mPromoId = "";
                                    if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                        mPromoId = "" + mCouponResponsePojo.promo_id;
                                    }


                      /*  String mUrl = "https://managemyticket.net/android/api/movie_migs_server_v3.php?mv_screen=" + mvScreen + "&dt=" + mSelectedDate
                                + "&show_time=" + mSelectedVdt + "&movie_id=" + mMovieID + "&max_seats=" + mSelectedSeatCount + "&cust_name=" + mUserFullName
                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&combos=" + mSelectedFoodValue + "&mv_ukey=" + mUserKey + "&mv_mtid="
                                + mSelectedTimeId + "&is_booking_from=MOB" + mCustId + mPromoId;*/

                                    Map<String, String> parameter = new HashMap<String, String>();
                                    String mUrl = "https://www.managemyticket.net/android/api/movie_migs_server_v4.php"/*?cardexp=" + lastDigitYear+strMonth + "&cardno=" + strCardNo
                                + "&cardcvv=" + strCardCvv + "&card_type=" + cardType +"&mv_screen=" + mvScreen + "&dt=" + mSelectedDate
                                + "&show_time=" + mSelectedVdt + "&movie_id=" + mMovieID + "&max_seats=" + mSelectedSeatCount + "&cust_name=" + mUserFullName
                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&combos=" + mSelectedFoodValue + "&mv_ukey=" + mUserKey + "&mv_mtid="
                                + mSelectedTimeId + "&is_booking_from=MOB" + mCustId + mPromoId+"&promo_id="*/;

                                    parameter.put("careexp", lastDigitYear + strMonth);
                                    parameter.put("cardno", strCardNo);
                                    parameter.put("cardcvv", strCardCvv);
                                    parameter.put("cardtype", cardType);
                                    parameter.put("mvscreen", mvScreen);
                                    parameter.put("dt", mSelectedDate);
                                    parameter.put("showtime", mSelectedVdt);
                                    parameter.put("movieid", mMovieID);
                                    parameter.put("maxseat", String.valueOf(mSelectedSeatCount));
                                    parameter.put("custname", mUserFullName);
                                    parameter.put("custemail", mUserEmail);
                                    parameter.put("custmob", mUserMobileNumber);
                                    parameter.put("combos", mSelectedFoodValue);
                                    parameter.put("mvuskey", mUserKey);
                                    parameter.put("mvmtid", mSelectedTimeId);
                                    parameter.put("isbookingfrom", "AND");
                                    parameter.put("cust_id", mCustId);
                                    parameter.put("promo_id", mPromoId);


                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.BNDL_TITLE, mMovie.name);
                                    bundle.putString(Constants.BNDL_URL, mUrl);
                                    bundle.putSerializable("postdata", (Serializable) parameter);
                                    bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_DEBIT);

                                    switchFragment(MoviePaymentProcessFragment.newInstance(), MoviePaymentProcessFragment.Tag_MoviePaymentProcessFragment, bundle);
                                    dialog.dismiss();
                                    Utils.hideKeyboard(getActivity());

                                } else {
                                    Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 100"));
                                }

                            }
                        });
                        dialog.show();
                    } else {
                        Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                    }
                } else if (sApplicablePGWS.contains("5")) {
                    if (mCurrency.equals("USD")) {
                        if (!isCreditCardEnabled) {
                            Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                            return;
                        }

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_card_info);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        Spinner spinnerCardType = dialog.findViewById(R.id.spinnerCardType);
                        EditText editTextCardNo = dialog.findViewById(R.id.editCardNo);
                        EditText editTextCvv = dialog.findViewById(R.id.editCvv);
                        TextView btSubmit = dialog.findViewById(R.id.tvSubmit);
                        TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                        TextView tvPromoText = dialog.findViewById(R.id.promotText);
                        Spinner spinnerMonth = dialog.findViewById(R.id.spinnerMonth);
                        Spinner spinnerYear = dialog.findViewById(R.id.spinnerYear);
                        if (sCRDBCharges.equals("")){
                            tvCharges.setVisibility(View.GONE);
                        }else if (sCRDBCharges.equals("0.00")){
                            tvCharges.setVisibility(View.GONE);
                        }else {
                            tvCharges.setText("Extra Charges Applicable : "+mCurrency+" "+sCRDBCharges);
                            tvCharges.setVisibility(View.VISIBLE);
                        }
                        editTextCardNo.addTextChangedListener(new TextWatcher() {
                            private static final char space = ' ';
                            int count = 0;

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    int len = s.toString().length();
                                    String cardDigit[] = strPromoCode.split(",");

                                    String str = s.toString().replaceAll("\\s", "");
                                    Log.d("Log", "Length :- " + len);
                                    if (len == 7) {
                                        for (int i = 0; i < cardDigit.length; i++) {
                                            if (str.equals(cardDigit[i].toString())) {
                                                tvPromoText.setVisibility(View.VISIBLE);
                                                tvPromoText.setText(strPromoText);
                                                promoFlaq = 1;
                                                Log.d("Log", "length " + len);
                                                break;
                                            } else {
                                                tvPromoText.setVisibility(View.GONE);
                                                Log.d("Log", "length " + len);
                                            }
                                        }
                                    } else {
                                        if (promoFlaq == 1) {
                                            tvPromoText.setVisibility(View.VISIBLE);
                                            tvPromoText.setText(strPromoText);
                                        } else {
                                            tvPromoText.setVisibility(View.GONE);
                                            promoFlaq = 0;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (count <= editTextCardNo.getText().toString().length()
                                        && (editTextCardNo.getText().toString().length() == 4
                                        || editTextCardNo.getText().toString().length() == 9
                                        || editTextCardNo.getText().toString().length() == 14)) {
                                    editTextCardNo.setText(editTextCardNo.getText().toString() + " ");
                                    int pos = editTextCardNo.getText().length();
                                    editTextCardNo.setSelection(pos);
                                } else if (count >= editTextCardNo.getText().toString().length()
                                        && (editTextCardNo.getText().toString().length() == 4
                                        || editTextCardNo.getText().toString().length() == 9
                                        || editTextCardNo.getText().toString().length() == 14)) {
                                    editTextCardNo.setText(editTextCardNo.getText().toString().substring(0, editTextCardNo.getText().toString().length() - 1));
                                    int pos = editTextCardNo.getText().length();
                                    editTextCardNo.setSelection(pos);
                                }
                                count = editTextCardNo.getText().toString().length();
                            }
                        });

                        List<String> cardTypeList = new ArrayList<>();
                        cardTypeList.add("Select Card Type");
                        cardTypeList.add("Mastercard");
                        cardTypeList.add("Visa");
                        cardTypeList.add("American Express");
                        cardTypeList.add("Amex Corporate Purchase Card");
                        cardTypeList.add("Diners Club");
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, cardTypeList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCardType.setAdapter(dataAdapter);

                        year = Calendar.getInstance().get(Calendar.YEAR);
                        expYear = year;
                        List<String> yearList = new ArrayList<>();
                        List<String> monthList = new ArrayList<>();
                        for (int i = 0; i < 32; i++) {
                            yearList.add("" + year);
                            year++;
                        }
                        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, yearList);
                        spinnerYear.setAdapter(yearAdapter);
                        for (int i = month; i <= 12; i++) {
                            if (i <= 9) {
                                monthList.add("0" + i);
                            } else {
                                monthList.add("" + i);
                            }
                        }
                        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, monthList);
                        spinnerMonth.setAdapter(monthAdapter);
/*
                        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int currnetYear = Integer.parseInt(spinnerYear.getSelectedItem().toString());
                                monthList.clear();
                                if (expYear == currnetYear) {
                                    DateFormat dateFormat = new SimpleDateFormat("MM");
                                    Date date = new Date();
                                    Log.d("Month", dateFormat.format(date));
                                    month = Integer.parseInt(dateFormat.format(date));
                                    for (int i = month; i <= 12; i++) {
                                        if (i <= 9) {
                                            monthList.add("0" + i);
                                        } else {
                                            monthList.add("" + i);
                                        }
                                    }
                                } else {
                                    for (int i = 1; i <= 12; i++) {
                                        if (i <= 9) {
                                            monthList.add("0" + i);
                                        } else {
                                            monthList.add("" + i);
                                        }
                                    }

                                }

                                ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, monthList);
                                spinnerMonth.setAdapter(monthAdapter);
                                editTextCvv.requestFocus();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
*/


                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                    dialog.cancel();
                                }

                            }
                        });

                        btSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String lastDigitYear;
                                String cardType = "";

                                if (spinnerCardType.getSelectedItem().toString().equals("Select Card Type")) {
                                    Utils.showToast(getActivity(), getString(R.string.alert_card_card_type));
                                    return;
                                } else {
                                    strCardType = spinnerCardType.getSelectedItem().toString();
                                    if (strCardType.equals("Mastercard")) {
                                        cardType = "Mastercard";
                                    } else if (strCardType.equals("Visa")) {
                                        cardType = "Visa";
                                    } else if (strCardType.equals("American Express")) {
                                        cardType = "Amex";
                                    } else if (strCardType.equals("Amex Corporate Purchase Card")) {
                                        cardType = "AmexPurchaseCard";
                                    } else if (strCardType.equals("Visa")) {
                                        cardType = "Dinersclub";
                                    }
                                }
                                if (TextUtils.isEmpty(editTextCardNo.getText().toString())) {
                                    Utils.showToast(getActivity(), getString(R.string.alert_card_number));
                                    return;
                                } else {
                                    strCardNo = editTextCardNo.getText().toString().replaceAll("\\s", "");
                                }
                                if (editTextCardNo.getText().length() < 16) {
                                    Utils.showToast(getActivity(), getString(R.string.alart_valid_card_number));
                                    return;
                                } else {
                                    strCardNo = editTextCardNo.getText().toString().replaceAll("\\s", "");
                                }

                                if (TextUtils.isEmpty(editTextCvv.getText().toString())) {
                                    Utils.showToast(getActivity(), getString(R.string.alert_card_cvv));
                                    return;
                                } else if (editTextCvv.getText().length() < 3) {
                                    Utils.showToast(getActivity(), getString(R.string.aler_card_cvv_lenth));
                                    return;
                                } else {
                                    strCardCvv = editTextCvv.getText().toString();
                                }

                                strMonth = spinnerMonth.getSelectedItem().toString();
                                strYear = spinnerYear.getSelectedItem().toString();
                                lastDigitYear = strYear.substring(2, 4);


                                Log.d("Log", "exp " + strCardExpiryDt);
                                Log.d("Log", "cvv " + strCardCvv);
                                Log.d("Log", "Year " + lastDigitYear);


                                if (mTotalAmount >= 100) {

                                    if (!Utils.isInternetConnected(getActivity())) {
                                        Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                        return;
                                    }

                                    String mCustId = "";
                                    if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                        mCustId = MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                                    }

                                    String mPromoId = "";
                                    if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                        mPromoId = "" + mCouponResponsePojo.promo_id;
                                    }


                      /*  String mUrl = "https://managemyticket.net/android/api/movie_migs_server_v3.php?mv_screen=" + mvScreen + "&dt=" + mSelectedDate
                                + "&show_time=" + mSelectedVdt + "&movie_id=" + mMovieID + "&max_seats=" + mSelectedSeatCount + "&cust_name=" + mUserFullName
                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&combos=" + mSelectedFoodValue + "&mv_ukey=" + mUserKey + "&mv_mtid="
                                + mSelectedTimeId + "&is_booking_from=MOB" + mCustId + mPromoId;*/

                                    Map<String, String> parameter = new HashMap<String, String>();
                                    String mUrl = "https://www.managemyticket.net/android/api/movie_migs_server_v4.php"/*?cardexp=" + lastDigitYear+strMonth + "&cardno=" + strCardNo
                                + "&cardcvv=" + strCardCvv + "&card_type=" + cardType +"&mv_screen=" + mvScreen + "&dt=" + mSelectedDate
                                + "&show_time=" + mSelectedVdt + "&movie_id=" + mMovieID + "&max_seats=" + mSelectedSeatCount + "&cust_name=" + mUserFullName
                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&combos=" + mSelectedFoodValue + "&mv_ukey=" + mUserKey + "&mv_mtid="
                                + mSelectedTimeId + "&is_booking_from=MOB" + mCustId + mPromoId+"&promo_id="*/;

                                    parameter.put("careexp", lastDigitYear + strMonth);
                                    parameter.put("cardno", strCardNo);
                                    parameter.put("cardcvv", strCardCvv);
                                    parameter.put("cardtype", cardType);
                                    parameter.put("mvscreen", mvScreen);
                                    parameter.put("dt", mSelectedDate);
                                    parameter.put("showtime", mSelectedVdt);
                                    parameter.put("movieid", mMovieID);
                                    parameter.put("maxseat", String.valueOf(mSelectedSeatCount));
                                    parameter.put("custname", mUserFullName);
                                    parameter.put("custemail", mUserEmail);
                                    parameter.put("custmob", mUserMobileNumber);
                                    parameter.put("combos", mSelectedFoodValue);
                                    parameter.put("mvuskey", mUserKey);
                                    parameter.put("mvmtid", mSelectedTimeId);
                                    parameter.put("isbookingfrom", "AND");
                                    parameter.put("cust_id", mCustId);
                                    parameter.put("promo_id", mPromoId);


                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.BNDL_TITLE, mMovie.name);
                                    bundle.putString(Constants.BNDL_URL, mUrl);
                                    bundle.putSerializable("postdata", (Serializable) parameter);
                                    bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_DEBIT);

                                    switchFragment(MoviePaymentProcessFragment.newInstance(), MoviePaymentProcessFragment.Tag_MoviePaymentProcessFragment, bundle);
                                    dialog.dismiss();
                                    Utils.hideKeyboard(getActivity());

                                } else {
                                    Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 100"));
                                }

                            }
                        });
                        dialog.show();
                    } else {
                        Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                    }
                } else {
                    if (!isCreditCardEnabled) {
                        Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                        return;
                    }

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_card_info);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    Spinner spinnerCardType = dialog.findViewById(R.id.spinnerCardType);
                    EditText editTextCardNo = dialog.findViewById(R.id.editCardNo);
                    EditText editTextCvv = dialog.findViewById(R.id.editCvv);
                    TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                    TextView btSubmit = dialog.findViewById(R.id.tvSubmit);
                    TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                    TextView tvPromoText = dialog.findViewById(R.id.promotText);
                    Spinner spinnerMonth = dialog.findViewById(R.id.spinnerMonth);
                    Spinner spinnerYear = dialog.findViewById(R.id.spinnerYear);
                    if (sCRDBCharges.equals("")){
                        tvCharges.setVisibility(View.GONE);
                    }else if (sCRDBCharges.equals("0.00")){
                        tvCharges.setVisibility(View.GONE);
                    }else {
                        tvCharges.setText("Extra Charges Applicable : "+mCurrency+" "+sCRDBCharges);
                        tvCharges.setVisibility(View.VISIBLE);
                    }
                    editTextCardNo.addTextChangedListener(new TextWatcher() {
                        private static final char space = ' ';
                        int count = 0;

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            try {
                                int len = s.toString().length();
                                String cardDigit[] = strPromoCode.split(",");

                                String str = s.toString().replaceAll("\\s", "");
                                Log.d("Log", "Length :- " + len);

                                if (len == 7) {
                                    for (int i = 0; i < cardDigit.length; i++) {
                                        if (str.equals(cardDigit[i].toString())) {
                                            tvPromoText.setVisibility(View.VISIBLE);
                                            tvPromoText.setText(strPromoText);
                                            promoFlaq = 1;
                                            Log.d("Log", "length " + len);
                                            break;
                                        } else {
                                            tvPromoText.setVisibility(View.GONE);
                                            Log.d("Log", "length " + len);
                                        }
                                    }
                                } else {
                                    if (promoFlaq == 1) {

                                        tvPromoText.setVisibility(View.VISIBLE);
                                        tvPromoText.setText(strPromoText);
                                    } else {
                                        tvPromoText.setVisibility(View.GONE);
                                        promoFlaq = 0;
                                    }
                                }
                                if (len < 7) {
                                    promoFlaq = 0;
                                    tvPromoText.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (count <= editTextCardNo.getText().toString().length()
                                    && (editTextCardNo.getText().toString().length() == 4
                                    || editTextCardNo.getText().toString().length() == 9
                                    || editTextCardNo.getText().toString().length() == 14)) {
                                editTextCardNo.setText(editTextCardNo.getText().toString() + " ");
                                int pos = editTextCardNo.getText().length();
                                editTextCardNo.setSelection(pos);
                            } else if (count >= editTextCardNo.getText().toString().length()
                                    && (editTextCardNo.getText().toString().length() == 4
                                    || editTextCardNo.getText().toString().length() == 9
                                    || editTextCardNo.getText().toString().length() == 14)) {
                                editTextCardNo.setText(editTextCardNo.getText().toString().substring(0, editTextCardNo.getText().toString().length() - 1));
                                int pos = editTextCardNo.getText().length();
                                editTextCardNo.setSelection(pos);
                            }
                            count = editTextCardNo.getText().toString().length();
                        }
                    });

                    List<String> cardTypeList = new ArrayList<>();
                    cardTypeList.add("Select Card Type");
                    cardTypeList.add("Mastercard");
                    cardTypeList.add("Visa");
                    cardTypeList.add("American Express");
                    cardTypeList.add("Amex Corporate Purchase Card");
                    cardTypeList.add("Diners Club");
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, cardTypeList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCardType.setAdapter(dataAdapter);

                    year = Calendar.getInstance().get(Calendar.YEAR);
                    expYear = year;
                    List<String> yearList = new ArrayList<>();
                    List<String> monthList = new ArrayList<>();
                    for (int i = 0; i < 32; i++) {
                        yearList.add("" + year);
                        year++;
                    }
                    ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, yearList);
                    spinnerYear.setAdapter(yearAdapter);

                    for (int i = month; i <= 12; i++) {
                        if (i <= 9) {
                            monthList.add("0" + i);
                        } else {
                            monthList.add("" + i);
                        }
                    }
                    ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, monthList);
                    spinnerMonth.setAdapter(monthAdapter);
/*
                    spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int currnetYear = Integer.parseInt(spinnerYear.getSelectedItem().toString());
                            monthList.clear();
                            if (expYear == currnetYear) {
                                DateFormat dateFormat = new SimpleDateFormat("MM");
                                Date date = new Date();
                                Log.d("Month", dateFormat.format(date));
                                month = Integer.parseInt(dateFormat.format(date));
                                for (int i = month; i <= 12; i++) {
                                    if (i <= 9) {
                                        monthList.add("0" + i);
                                    } else {
                                        monthList.add("" + i);
                                    }
                                }
                            } else {
                                for (int i = 1; i <= 12; i++) {
                                    if (i <= 9) {
                                        monthList.add("0" + i);
                                    } else {
                                        monthList.add("" + i);
                                    }
                                }

                            }

                            ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.layout_spinner_date, monthList);
                            spinnerMonth.setAdapter(monthAdapter);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
*/


                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                                dialog.cancel();
                            }

                        }
                    });

                    btSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String lastDigitYear;
                            String cardType = "";

                            if (spinnerCardType.getSelectedItem().toString().equals("Select Card Type")) {
                                Utils.showToast(getActivity(), getString(R.string.alert_card_card_type));
                                return;
                            } else {
                                strCardType = spinnerCardType.getSelectedItem().toString();
                                if (strCardType.equals("Mastercard")) {
                                    cardType = "Mastercard";
                                } else if (strCardType.equals("Visa")) {
                                    cardType = "Visa";
                                } else if (strCardType.equals("American Express")) {
                                    cardType = "Amex";
                                } else if (strCardType.equals("Amex Corporate Purchase Card")) {
                                    cardType = "AmexPurchaseCard";
                                } else if (strCardType.equals("Visa")) {
                                    cardType = "Dinersclub";
                                }
                            }
                            if (TextUtils.isEmpty(editTextCardNo.getText().toString())) {
                                Utils.showToast(getActivity(), getString(R.string.alert_card_number));
                                return;
                            } else {
                                strCardNo = editTextCardNo.getText().toString().replaceAll("\\s", "");
                            }
                            if (editTextCardNo.getText().length() < 16) {
                                Utils.showToast(getActivity(), getString(R.string.alart_valid_card_number));
                                return;
                            } else {
                                strCardNo = editTextCardNo.getText().toString().replaceAll("\\s", "");
                            }

                            if (TextUtils.isEmpty(editTextCvv.getText().toString())) {
                                Utils.showToast(getActivity(), getString(R.string.alert_card_cvv));
                                return;
                            } else if (editTextCvv.getText().length() < 3) {
                                Utils.showToast(getActivity(), getString(R.string.aler_card_cvv_lenth));
                                return;
                            } else {
                                strCardCvv = editTextCvv.getText().toString();
                            }

                            strMonth = spinnerMonth.getSelectedItem().toString();
                            strYear = spinnerYear.getSelectedItem().toString();
                            lastDigitYear = strYear.substring(2, 4);


                            Log.d("Log", "exp " + strCardExpiryDt);
                            Log.d("Log", "cvv " + strCardCvv);
                            Log.d("Log", "Year " + lastDigitYear);


                            if (mTotalAmount >= 100) {

                                if (!Utils.isInternetConnected(getActivity())) {
                                    Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                    return;
                                }

                                String mCustId = "";
                                if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                    mCustId = MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                                }

                                String mPromoId = "";
                                if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                    mPromoId = "" + mCouponResponsePojo.promo_id;
                                }


                      /*  String mUrl = "https://managemyticket.net/android/api/movie_migs_server_v3.php?mv_screen=" + mvScreen + "&dt=" + mSelectedDate
                                + "&show_time=" + mSelectedVdt + "&movie_id=" + mMovieID + "&max_seats=" + mSelectedSeatCount + "&cust_name=" + mUserFullName
                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&combos=" + mSelectedFoodValue + "&mv_ukey=" + mUserKey + "&mv_mtid="
                                + mSelectedTimeId + "&is_booking_from=MOB" + mCustId + mPromoId;*/

                                Map<String, String> parameter = new HashMap<String, String>();
                                String mUrl = "https://www.managemyticket.net/android/api/movie_migs_server_v4.php"/*?cardexp=" + lastDigitYear+strMonth + "&cardno=" + strCardNo
                                + "&cardcvv=" + strCardCvv + "&card_type=" + cardType +"&mv_screen=" + mvScreen + "&dt=" + mSelectedDate
                                + "&show_time=" + mSelectedVdt + "&movie_id=" + mMovieID + "&max_seats=" + mSelectedSeatCount + "&cust_name=" + mUserFullName
                                + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&combos=" + mSelectedFoodValue + "&mv_ukey=" + mUserKey + "&mv_mtid="
                                + mSelectedTimeId + "&is_booking_from=MOB" + mCustId + mPromoId+"&promo_id="*/;

                                parameter.put("careexp", lastDigitYear + strMonth);
                                parameter.put("cardno", strCardNo);
                                parameter.put("cardcvv", strCardCvv);
                                parameter.put("cardtype", cardType);
                                parameter.put("mvscreen", mvScreen);
                                parameter.put("dt", mSelectedDate);
                                parameter.put("showtime", mSelectedVdt);
                                parameter.put("movieid", mMovieID);
                                parameter.put("maxseat", String.valueOf(mSelectedSeatCount));
                                parameter.put("custname", mUserFullName);
                                parameter.put("custemail", mUserEmail);
                                parameter.put("custmob", mUserMobileNumber);
                                parameter.put("combos", mSelectedFoodValue);
                                parameter.put("mvuskey", mUserKey);
                                parameter.put("mvmtid", mSelectedTimeId);
                                parameter.put("isbookingfrom", "AND");
                                parameter.put("cust_id", mCustId);
                                parameter.put("promo_id", mPromoId);


                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.BNDL_TITLE, mMovie.name);
                                bundle.putString(Constants.BNDL_URL, mUrl);
                                bundle.putSerializable("postdata", (Serializable) parameter);
                                bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_DEBIT);

                                switchFragment(MoviePaymentProcessFragment.newInstance(), MoviePaymentProcessFragment.Tag_MoviePaymentProcessFragment, bundle);
                                dialog.dismiss();
                                Utils.hideKeyboard(getActivity());

                            } else {
                                Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 100"));
                            }

                        }
                    });
                    dialog.show();
                }
            } else {
                Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
            }
        } else if (view == rltTigoPesa) {

            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("2")) {

                if (!isTigoEnabled) {
                    Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                    return;
                }

                if (mTotalAmount >= 1000) {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_payment_mpesa);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                    final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                    TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                    TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
//                final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                    Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                    final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);

                    tvTitle.setText("" + getString(R.string.tigo_pesa));
                    if (sTigoCharges.equals("")){
                        tvCharges.setVisibility(View.GONE);
                    }else if (sTigoCharges.equals("0.00")){
                        tvCharges.setVisibility(View.GONE);
                    }else {
                        tvCharges.setText("Extra Charges Applicable : "+mCurrency+" "+sTigoCharges);
                        tvCharges.setVisibility(View.VISIBLE);
                    }
                    CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
                    spinCountryCode.setAdapter(mCountryCodeSpinAdapter);
                    spinCountryCode.setSelection(214);
                    spinCountryCode.setEnabled(false);
                    if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                        spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
                    } else {
                        spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
                    }

                    etMobileNumber.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            etMobileNumber.setSelection(etMobileNumber.getText().length());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                            mMobInnerNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobInnerNumberMaxLength)});
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    tvConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                                Utils.showToast(getActivity(), getString(R.string.alert_enter_phone));
                                return;
                            }

                            if (etMobileNumber.getText().toString().length() < mMobInnerNumberMaxLength || etMobileNumber.getText().toString().length() > mMobInnerNumberMaxLength) {
                                Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobInnerNumberMaxLength));
                                return;
                            }

                            if (!Utils.isInternetConnected(getActivity())) {
                                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                return;
                            }

                            String mCustId = "";
                            if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                mCustId = "&cust_id=" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                            }

                            String mPromoId = "";
                            if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                mPromoId = "&promo_id=" + mCouponResponsePojo.promo_id;
                            }

                            String mUrl = "https://www.managemyticket.net/android/api/movie_tigo_pesa_v3.php?mv_screen=" + mvScreen + "&dt=" + mSelectedDate
                                    + "&show_time=" + mSelectedVdt + "&movie_id=" + mMovieID + "&max_seats=" + mSelectedSeatCount + "&cust_name=" + mUserFullName
                                    + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&combos=" + mSelectedFoodValue + "&tigo=" + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code
                                    + "" + etMobileNumber.getText().toString() + "&tigo_pesa_term=1" + "&mv_ukey=" + mUserKey + "&mv_mtid=" + mSelectedTimeId + "&is_booking_from=AND" + mCustId + mPromoId;


                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_TITLE, mMovie.name);
                            bundle.putString(Constants.BNDL_URL, mUrl);
                            bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_TIGO);

                            switchFragment(MoviePaymentProcessFragment.newInstance(), MoviePaymentProcessFragment.Tag_MoviePaymentProcessFragment, bundle);

                            tvCancel.performClick();
                        }
                    });

                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                                dialog.cancel();
                                Utils.hideKeyboard(getActivity());
                            }
                        }
                    });

                    dialog.show();

                } else {
                    Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 1000"));
                }
            } else {
                //  Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();

                Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));
            }
        } else if (view == rltMPesa) {
            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("3")) {
                if (!isMpesaEnabled) {
                    Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                    return;
                }

                if (mTotalAmount >= 100) {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_payment_mpesa);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                    final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                    final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                    TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
//                final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                    Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                    final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);
                    if (sMPesaCharges.equals("")){
                        tvCharges.setVisibility(View.GONE);
                    }else if (sMPesaCharges.equals("0")){
                        tvCharges.setVisibility(View.GONE);
                    }else {
                        tvCharges.setText("Extra Charges Applicable : "+mCurrency+" "+sMPesaCharges);
                        tvCharges.setVisibility(View.VISIBLE);
                    }
                    tvTitle.setText("" + getString(R.string.mpesa));

                    etMobileNumber.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            etMobileNumber.setSelection(etMobileNumber.getText().length());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
                    spinCountryCode.setAdapter(mCountryCodeSpinAdapter);
                    spinCountryCode.setEnabled(false);
                    spinCountryCode.setSelection(214);
                    if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                        spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
                    } else {
                        spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
                    }

                    spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                            mMobInnerNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobInnerNumberMaxLength)});
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    tvConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                                Utils.showToast(getActivity(), getString(R.string.alert_enter_phone));
                                return;
                            }

                            if (etMobileNumber.getText().toString().length() < mMobInnerNumberMaxLength || etMobileNumber.getText().toString().length() > mMobInnerNumberMaxLength) {
                                Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobInnerNumberMaxLength));
                                return;
                            }

                            if (!Utils.isInternetConnected(getActivity())) {
                                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                return;
                            }

                            String mCustId = "";
                            if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                mCustId = "&cust_id=" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                            }

                            String mPromoId = "";
                            if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                mPromoId = "&promo_id=" + mCouponResponsePojo.promo_id;
                            }

                            String mUrl = "https://managemyticket.net/android/api/movie_mpesa_v3.php?mv_screen=" + mvScreen + "&dt=" + mSelectedDate
                                    + "&show_time=" + mSelectedVdt + "&movie_id=" + mMovieID + "&max_seats=" + mSelectedSeatCount + "&cust_name=" + mUserFullName
                                    + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&combos=" + mSelectedFoodValue
                                    + "&mpesa=" + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code + "" + etMobileNumber.getText().toString() + "&mpesa_term=1"
                                    + "&mv_ukey=" + mUserKey + "&mv_mtid=" + mSelectedTimeId + "&is_booking_from=AND" + mCustId + mPromoId;


                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_TITLE, mMovie.name);
                            bundle.putString(Constants.BNDL_URL, mUrl);
                            bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_MPESA);

                            switchFragment(MoviePaymentProcessFragment.newInstance(), MoviePaymentProcessFragment.Tag_MoviePaymentProcessFragment, bundle);

                            tvCancel.performClick();
                        }
                    });

                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                                dialog.cancel();
                                Utils.hideKeyboard(getActivity());
                            }
                        }
                    });

                    dialog.show();

                } else {
                    Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 100"));
                }
            } else {
                //  Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();

                Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));
            }
        } else if (view == rltAirtelMoney) {
            if (sApplicablePGWS.contains("0") || sApplicablePGWS.contains("4")) {

                if (!isAirtelMoneyEnabled) {
                    Utils.showToast(getActivity(), getString(R.string.msg_payment_disable));
                    return;
                }

                if (mTotalAmount >= 100) {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_payment_mpesa);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                    final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
                    TextView tvCharges = (TextView) dialog.findViewById(R.id.charges);
                    TextView tvConfirm = (TextView) dialog.findViewById(R.id.tvConfirm);
//                final TextView tvCountryCode = (TextView) termsDialog.findViewById(R.id.tvCountryCode);
                    Spinner spinCountryCode = (Spinner) dialog.findViewById(R.id.spinCountryCode);
                    final EditText etMobileNumber = (EditText) dialog.findViewById(R.id.etMobileNumber);

                    tvTitle.setText("" + getString(R.string.airtel_money));
                    if (sAirtelCharges.equals("")){
                        tvCharges.setVisibility(View.GONE);
                    }else if (sAirtelCharges.equals("0.00")){
                        tvCharges.setVisibility(View.GONE);
                    }else {
                        tvCharges.setText("Extra Charges Applicable : "+mCurrency+" "+sAirtelCharges);
                        tvCharges.setVisibility(View.VISIBLE);
                    }
                    etMobileNumber.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            etMobileNumber.setSelection(etMobileNumber.getText().length());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    CountryCodeSpinAdapter mCountryCodeSpinAdapter = new CountryCodeSpinAdapter(getActivity(), R.layout.list_item_country_code, Otapp.mCountryCodePojoList);
                    spinCountryCode.setAdapter(mCountryCodeSpinAdapter);
                    spinCountryCode.setEnabled(false);
                    spinCountryCode.setSelection(214);
                    if (TextUtils.isEmpty(MyPref.getPref(getActivity(), MyPref.PREF_USER_COUNTRY_CODE, ""))) {
                        spinCountryCode.setSelection(Utils.getUserCountryPosition(getActivity()));
                    } else {
                        spinCountryCode.setSelection(Utils.getCountryPosition(getActivity()));
                    }

                    spinCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            MyPref.setPref(getActivity(), MyPref.PREF_SELECTED_COUNTRY_CODE, Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code);
                            mMobInnerNumberMaxLength = Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).mobLength;
                            etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMobInnerNumberMaxLength)});
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    tvConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (TextUtils.isEmpty(etMobileNumber.getText().toString())) {
                                Utils.showToast(getActivity(), getString(R.string.alert_enter_phone));
                                return;
                            }

                            if (etMobileNumber.getText().toString().length() < mMobInnerNumberMaxLength || etMobileNumber.getText().toString().length() > mMobInnerNumberMaxLength) {
                                Utils.showToast(getActivity(), "" + String.format(getString(R.string.alert_enter_valid_phone), "" + mMobInnerNumberMaxLength));
                                return;
                            }

                            if (!Utils.isInternetConnected(getActivity())) {
                                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                                return;
                            }

                            String mCustId = "";
                            if (MyPref.getPref(getActivity(), MyPref.PREF_IS_LOGGED, false)) {
                                mCustId = "&cust_id=" + MyPref.getPref(getActivity(), MyPref.PREF_USER_ID, "");
                            }

                            String mPromoId = "";
                            if (mCouponResponsePojo != null && !TextUtils.isEmpty("" + mCouponResponsePojo.promo_id)) {
                                mPromoId = "&promo_id=" + mCouponResponsePojo.promo_id;
                            }

                            String mUrl = "https://managemyticket.net/android/api/movie_airtel.php?mv_screen=" + mvScreen + "&dt=" + mSelectedDate
                                    + "&show_time=" + mSelectedVdt + "&movie_id=" + mMovieID + "&max_seats=" + mSelectedSeatCount + "&cust_name=" + mUserFullName
                                    + "&cust_email=" + mUserEmail + "&cust_mob=" + mUserMobileNumber + "&combos=" + mSelectedFoodValue
                                    + "&airtel=" + Otapp.mCountryCodePojoList.get(spinCountryCode.getSelectedItemPosition()).code + "" + etMobileNumber.getText().toString() + "&airtel_term=1"
                                    + "&mv_ukey=" + mUserKey + "&mv_mtid=" + mSelectedTimeId + "&is_booking_from=AND" + mCustId + mPromoId;

//                        https://www.managemyticket.net/android/api/movie_airtel.php?mv_screen=1&dt=2019-09-20&show_time=18:30&movie_id=14&max_seats=1&cust_name=NIkhil&cust_email=npansare0@gmail.com&cust_mob=+919767719693&combos=&mv_ukey=201909206537&mv_mtid=2843&cnv_fee=0&cust_id&is_booking_from=MOB&promo_id=&airtel=+255689256021&airtel_term=1


                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_TITLE, mMovie.name);
                            bundle.putString(Constants.BNDL_URL, mUrl);
                            bundle.putString(Constants.BNDL_PAYMENT_TYPE, Constants.BNDL_PAYMENT_TYPE_AIRTEL);

                            switchFragment(MoviePaymentProcessFragment.newInstance(), MoviePaymentProcessFragment.Tag_MoviePaymentProcessFragment, bundle);

                            tvCancel.performClick();
                        }
                    });

                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                                dialog.cancel();
                                Utils.hideKeyboard(getActivity());
                            }
                        }
                    });

                    dialog.show();

                } else {
                    Utils.showToast(getActivity(), String.format(getString(R.string.alert_minimum_amount), mCurrency + " 100"));
                }
            } else {
                //    Toast.makeText(getActivity(), getString(R.string.msg_payment_disable), Toast.LENGTH_SHORT).show();

                Utils.showToast(getActivity(), String.format(getString(R.string.msg_payment_disable)));

            }
        }


    }

    private void setModule(String module) {

        if (!TextUtils.isEmpty(module)) {
            int i = 0;
            if (module.equalsIgnoreCase("movie")) {
                i = 0;
            } else if (module.equalsIgnoreCase("themepark")) {
                i = 1;
            } else if (module.equalsIgnoreCase("event")) {
                i = 2;
            } else if (module.equalsIgnoreCase("bus")) {
                i = 3;
            } else if (module.equalsIgnoreCase("flight")) {
                i = 4;
            } else if (module.equalsIgnoreCase("hotels")) {
                i = 5;
            } else if (module.equalsIgnoreCase("ferry")) {
                i = 6;
            } else if (module.equalsIgnoreCase("tours")) {
                i = 7;
            }

            if (!Utils.isInternetConnected(getActivity())) {
                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                return;
            }

            Fragment mFragment = ServiceFragment.newInstance();
            ((ServiceFragment) mFragment).setPosition(i);
            switchFragment(mFragment, ServiceFragment.Tag_ServiceFragment);

        }

    }
}
