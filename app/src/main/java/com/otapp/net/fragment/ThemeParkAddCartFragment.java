package com.otapp.net.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.otapp.net.R;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.model.ThemeParkDetailsPojo;
import com.otapp.net.model.ThemeParkPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemeParkAddCartFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_ThemeParkAddCartFragment = "Tag_" + "ThemeParkAddCartFragment";

    View mView;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvSelectedDate)
    TextView tvSelectedDate;
    @BindView(R.id.tvSubTotal)
    TextView tvSubTotal;
    @BindView(R.id.tvChildPrice)
    TextView tvChildPrice;
    @BindView(R.id.tvChildMinus)
    TextView tvChildMinus;
    @BindView(R.id.tvChildQuantity)
    TextView tvChildQuantity;
    @BindView(R.id.tvChildPlus)
    TextView tvChildPlus;
    @BindView(R.id.tvAdultPrice)
    TextView tvAdultPrice;
    @BindView(R.id.tvAdultMinus)
    TextView tvAdultMinus;
    @BindView(R.id.tvAdultQuantity)
    TextView tvAdultQuantity;
    @BindView(R.id.tvAdultPlus)
    TextView tvAdultPlus;
    @BindView(R.id.tvStudentPrice)
    TextView tvStudentPrice;
    @BindView(R.id.tvStudentMinus)
    TextView tvStudentMinus;
    @BindView(R.id.tvStudentQuantity)
    TextView tvStudentQuantity;
    @BindView(R.id.tvStudentPlus)
    TextView tvStudentPlus;
    @BindView(R.id.tvChildName)
    TextView tvChildName;
    @BindView(R.id.tvChildSubTotal)
    TextView tvChildSubTotal;
    @BindView(R.id.tvAdultName)
    TextView tvAdultName;
    @BindView(R.id.tvAdultSubTotal)
    TextView tvAdultSubTotal;
    @BindView(R.id.tvStudentName)
    TextView tvStudentName;
    @BindView(R.id.tvStudentSubTotal)
    TextView tvStudentSubTotal;
    @BindView(R.id.tvTotalName)
    TextView tvTotalName;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.tvAddToCart)
    TextView tvAddToCart;
    @BindView(R.id.tvContinueBooking)
    TextView tvContinueBooking;
    @BindView(R.id.tvCartCount)
    TextView tvCartCount;
    /*  @BindView(R.id.tvDateDown)
      TextView tvDateDown;*/
    @BindView(R.id.lnrTotal)
    LinearLayout lnrTotal;
    @BindView(R.id.lnrStudent)
    LinearLayout lnrStudent;
    @BindView(R.id.lnrChild)
    LinearLayout lnrChild;
    @BindView(R.id.lnrAdult)
    LinearLayout lnrAdult;
    @BindView(R.id.lnrSubTotal)
    LinearLayout lnrSubTotal;
    @BindView(R.id.lnrAdultType)
    LinearLayout lnrAdultType;
    @BindView(R.id.lnrChildType)
    LinearLayout lnrChildType;
    @BindView(R.id.lnrStudentType)
    LinearLayout lnrStudentType;
    @BindView(R.id.rlDate)
    RelativeLayout rlDate1;
    @BindView(R.id.rltCart)
    RelativeLayout rltCart;

    private int mYear, mMonth, mDay, mTotalPrice = 0, mCartCount = 0, flag = 0;
    String dateFlage = "";
    String sSelectedDate = "";

    private ThemeParkPojo.Park mPark;
    DatePickerDialog datePickerDialog;

    ThemeParkDetailsPojo.Details mThemeParkDetails;
    Calendar mCalDate = Calendar.getInstance();

    public static ThemeParkAddCartFragment newInstance() {
        ThemeParkAddCartFragment fragment = new ThemeParkAddCartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_park_add_cart, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        int sCart = MyPref.getPref(getContext(), MyPref.PREF_PARK_CART_COUNT, 0);

        if (mCartCount == 0) {

            tvDate.setText("" + DateFormate.sdfParkDisplayDate.format(new Date()));
            tvSelectedDate.setText("" + DateFormate.sdfParkDisplayDate.format(new Date()));
            tvCartCount.setText("");

            mCalDate = Calendar.getInstance();
            MyPref.setPref(getContext(), "RideDate", mCalDate.getTimeInMillis());

            MyPref.setPref(getActivity(), MyPref.PREF_PARK_DATE, mCalDate.getTimeInMillis());
            Log.d("Log", "IF ");
        } else {
            if (mCalDate != null) {
                String date = MyPref.getPref(getContext(), "RideDate", "2018");
                Log.d("Log", "Date :- " + date);
                tvCartCount.setText("" + sCart);
                tvDate.setText("" + date);
                Log.d("Log", "Else IF ");
                tvSelectedDate.setText("" + date);
            } else {
                Log.d("Log", "Else");
            }
        }
    }

    private void InitializeControls() {

        mTotalPrice = 0;
        mCartCount = 0;
//        tvDate.setText("" + DateFormate.sdfParkDisplayDate.format(new Date()));
        mYear = mCalDate.get(Calendar.YEAR);
        mMonth = mCalDate.get(Calendar.MONTH);
        mDay = mCalDate.get(Calendar.DAY_OF_MONTH);

        LogUtils.e("", "PREF_PARK_DATE::" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_DATE, 0l));

        if (MyPref.getPref(getActivity(), MyPref.PREF_PARK_DATE, 0l) > 0l) {
            mCalDate.setTimeInMillis(MyPref.getPref(getActivity(), MyPref.PREF_PARK_DATE, 0l));
            //   rlDate.setOnClickListener(null);
            //  tvDateDown.setVisibility(View.GONE);

            rlDate1.setOnClickListener(this);

        } else {
            // tvDateDown.setVisibility(View.VISIBLE);

            rlDate1.setOnClickListener(this);
        }

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        mCalDate.set(Calendar.YEAR, year);
                        mCalDate.set(Calendar.MONTH, monthOfYear);
                        mCalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        MyPref.setPref(getContext(), "RideDate", DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()));
                        MyPref.setPref(getActivity(), MyPref.PREF_PARK_DATE, mCalDate.getTimeInMillis());

                        Log.d("Log", "Api Date : " + DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()));
                        setDate();

                    }
                }, mYear, mMonth, mDay);

        Calendar mCal = Calendar.getInstance();
        datePickerDialog.getDatePicker().setMinDate(mCal.getTimeInMillis() - 10000);
        mCal.add(Calendar.MONTH, 3);
        datePickerDialog.getDatePicker().setMaxDate(mCal.getTimeInMillis() - 10000); // for 90 days

        Bundle bundle = getArguments();
        if (bundle != null) {
            mThemeParkDetails = new Gson().fromJson(bundle.getString(Constants.BNDL_PARK_DETAILS), ThemeParkDetailsPojo.Details.class);
            mPark = new Gson().fromJson(bundle.getString(Constants.BNDL_PARK), ThemeParkPojo.Park.class);
            if (mThemeParkDetails != null) {

                //  mCartCount = mThemeParkDetails.cartCount;
                mCartCount = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_COUNT, mCartCount);

                tvTime.setText(mThemeParkDetails.timings);
                setDate();
                if (mThemeParkDetails.adultPrice > 0) {
                    lnrAdultType.setVisibility(View.VISIBLE);

                    tvAdultPrice.setText(mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.adultPrice));
                    setAdultPrice();
                } else {
                    lnrAdultType.setVisibility(View.GONE);
                }

                if (mThemeParkDetails.childPrice > 0) {
                    lnrChildType.setVisibility(View.VISIBLE);

                    tvChildPrice.setText(mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.childPrice));
                    setChildPrice();
                } else {
                    lnrChildType.setVisibility(View.GONE);
                }

                if (mThemeParkDetails.studPrice > 0) {
                    lnrStudentType.setVisibility(View.VISIBLE);

                    tvStudentPrice.setText(mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.studPrice));
                    setStudentPrice();
                } else {
                    lnrStudentType.setVisibility(View.GONE);
                }


            }
        }


        tvBack.setOnClickListener(this);
        tvAdultMinus.setOnClickListener(this);
        tvAdultPlus.setOnClickListener(this);
        tvChildMinus.setOnClickListener(this);
        tvChildPlus.setOnClickListener(this);
        tvStudentMinus.setOnClickListener(this);
        tvStudentPlus.setOnClickListener(this);
        tvContinueBooking.setOnClickListener(this);
        tvAddToCart.setOnClickListener(this);
        rltCart.setOnClickListener(this);

    }

    /*
       public void showDailog(){
           datePickerDialog = new DatePickerDialog(getActivity(),
                   new DatePickerDialog.OnDateSetListener() {

                       @Override
                       public void onDateSet(DatePicker view, int year,
                                             int monthOfYear, int dayOfMonth) {

                           mCalDate.set(Calendar.YEAR, year);
                           mCalDate.set(Calendar.MONTH, monthOfYear);
                           mCalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                           setDate();

                       }
                   }, mYear, mMonth, mDay);
            datePickerDialog.show();
           Calendar mCal = Calendar.getInstance();
           datePickerDialog.getDatePicker().setMinDate(mCal.getTimeInMillis() - 10000);
           mCal.add(Calendar.MONTH, 3);
           datePickerDialog.getDatePicker().setMaxDate(mCal.getTimeInMillis() - 10000);
       }
    */
    private void setDate() {
        LogUtils.e("", "mCalDate::" + mCalDate.getTime() + " " + DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()));
        String date = DateFormate.sdfMovieDate.format(mCalDate.getTime());
       /* if(mCartCount<1){
            tvDate.setText("" + DateFormate.sdfParkDisplayDate.format(new Date()));

        }else {*/
        tvDate.setText("" + DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()));
        //   }
        tvSelectedDate.setText("" + DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()));
        if (flag == 1) {
            if (mCartCount > 0) {
                if (DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()).equals(DateFormate.sdfParkDisplayDate.format(new Date()))) {
                    Toast.makeText(getContext(), "Please Choose Next Date", Toast.LENGTH_SHORT).show();
                    datePickerDialog.show();

                } else {
                    try {
//                        Toast.makeText(getContext(), DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()), Toast.LENGTH_SHORT).show();
                        String mParkCartID = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "");
                        final ApiInterface mApiInterface = RestClient.getClient(true);
                        Call<JsonObject> mCall = mApiInterface.getRideDateUpdate(mParkCartID, date);
                        rltCart.setEnabled(false);
                        tvAddToCart.setEnabled(false);
                        tvContinueBooking.setEnabled(false);
                        mCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                rltCart.setEnabled(true);
                                tvAddToCart.setEnabled(true);
                                tvContinueBooking.setEnabled(true);
                                try {
                                    JSONObject jsonObjectResponse = null;
                                    jsonObjectResponse = new JSONObject(new Gson().toJson(response.body()));
                                    Log.d("Log", "Response Update: " + jsonObjectResponse);
                                    String status = jsonObjectResponse.getString("status");
                                    if (status.equals("200")) {
                                        Toast.makeText(getContext(), "" + jsonObjectResponse.getString("message"), Toast.LENGTH_SHORT).show();
                                        MyPref.setPref(getContext(), "RideDate", DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()));
                                        Log.d("Log", "Api Date : " + DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()));
                                        MyPref.setPref(getActivity(), MyPref.PREF_PARK_DATE, mCalDate.getTimeInMillis());
                                    } else {
                                        Toast.makeText(getContext(), "" + jsonObjectResponse.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                rltCart.setEnabled(true);
                                tvAddToCart.setEnabled(true);
                                tvContinueBooking.setEnabled(true);
                                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            flag = 0;
        }


    }

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            popBackStack();
        } else if (view == tvAdultMinus) {
            if (mThemeParkDetails.adultQuntity > 0) {
                mThemeParkDetails.adultQuntity--;
                tvAdultQuantity.setText("" + mThemeParkDetails.adultQuntity);
                setAdultPrice();
            }
        } else if (view == tvAdultPlus) {
            mThemeParkDetails.adultQuntity++;
            tvAdultQuantity.setText("" + mThemeParkDetails.adultQuntity);
            setAdultPrice();
        } else if (view == tvChildMinus) {
            if (mThemeParkDetails.childQuntity > 0) {
                mThemeParkDetails.childQuntity--;
                tvChildQuantity.setText("" + mThemeParkDetails.childQuntity);
                setChildPrice();
            }
        } else if (view == tvChildPlus) {
            mThemeParkDetails.childQuntity++;
            tvChildQuantity.setText("" + mThemeParkDetails.childQuntity);
            setChildPrice();
        } else if (view == tvStudentMinus) {
            if (mThemeParkDetails.studentQuntity > 0) {
                mThemeParkDetails.studentQuntity--;
                tvStudentQuantity.setText("" + mThemeParkDetails.studentQuntity);
                setStudentPrice();
            }
        } else if (view == tvStudentPlus) {
            mThemeParkDetails.studentQuntity++;
            tvStudentQuantity.setText("" + mThemeParkDetails.studentQuntity);
            setStudentPrice();
        } else if (view == tvAddToCart) {
            Log.d("Log","Calnderdate "+DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()));
            Log.d("Log","Today date "+DateFormate.sdfParkDisplayDate.format(new Date()));

            if (DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()).equals(DateFormate.sdfParkDisplayDate.format(new Date()))) {
                Toast.makeText(getContext(), "Please Choose Next Date", Toast.LENGTH_SHORT).show();
                datePickerDialog.show();
            } else {
                if (mTotalPrice > 0) {

                    String mParkCartID = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "");
//                if (TextUtils.isEmpty(mParkCartID)) {
//                    mParkCartID = getString(R.string.park) + System.currentTimeMillis();
//                    MyPref.setPref(getActivity(), MyPref.PREF_PARK_CART_ID, "" + mParkCartID);
//                }

                    addItemToCart();

                } else {
                    Utils.showToast(getActivity(), getString(R.string.alert_add_ticket));
                }
            }
        } else if (view == tvContinueBooking) {
            if (DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()).equals(DateFormate.sdfParkDisplayDate.format(new Date()))) {
                Toast.makeText(getContext(), "Please Choose Next Date", Toast.LENGTH_SHORT).show();
                datePickerDialog.show();
            } else {
                popBackStack(ThemeParkDetailsFragment.Tag_ThemeParkDetailsFragment);
            }
        } else if (view == rltCart) {

            if (DateFormate.sdfParkDisplayDate.format(mCalDate.getTime()).equals(DateFormate.sdfParkDisplayDate.format(new Date()))) {
                Toast.makeText(getContext(), "Please Choose Next Date", Toast.LENGTH_SHORT).show();
                datePickerDialog.show();
            } else {
                if (mCartCount > 0) {

                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.BNDL_PARK_DETAILS, new Gson().toJson(mThemeParkDetails));
                    bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                    bundle.putBoolean(Constants.BNDL_IS_PARK_FROM_RIDE, false);
                    MyPref.setPref(getActivity(), Constants.BNDL_PARK_DETAILS, new Gson().toJson(mThemeParkDetails));
                    MyPref.setPref(getActivity(), Constants.BNDL_PARK, new Gson().toJson(mPark));
                    MyPref.setPref(getActivity(), MyPref.PREF_PARK_CART_COUNT, mCartCount);
                    MyPref.setPref(getActivity(), "cartCall", 1);
                    switchFragment(ThemeParkMyCartFragment.newInstance(), ThemeParkMyCartFragment.Tag_ThemeParkMyCartFragment, bundle);


                } else {
                    Utils.showToast(getActivity(), getString(R.string.alert_add_ticket));
                }
            }
        } else if (view == rlDate1) {
            if (datePickerDialog != null) {
                flag = 1;
                datePickerDialog.show();
            }

            //  datePickerDialog.show();
        }
    }

    private void deleteFromCart() {
        String mParkCartID = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "");
        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ApiResponse> mCall = mApiInterface.removeRideItem(mThemeParkDetails.tpId, mParkCartID, Otapp.mUniqueID);
        mCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null) {
                        if (mApiResponse.status.equals("200")) {
                            addItemToCart();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }

    private void addItemToCart() {

        try {
            if (!Utils.isInternetConnected(getActivity())) {
                Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
                return;
            }

            Utils.showProgressDialog(getActivity());

            String mParkCartID = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "");

            final ApiInterface mApiInterface = RestClient.getClient(true);
//            Toast.makeText(getContext(), DateFormate.sdfMovieDate.format(mCalDate.getTime()), Toast.LENGTH_SHORT).show();
            Call<ApiResponse> mCall = mApiInterface.bookThemePark(mThemeParkDetails.tpId, DateFormate.sdfMovieDate.format(mCalDate.getTime()), mParkCartID, "" + mThemeParkDetails.adultQuntity, "" + mThemeParkDetails.childQuntity, "" + mThemeParkDetails.studentQuntity, Otapp.mUniqueID);
            mCall.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Utils.closeProgressDialog();
                    if (response.isSuccessful()) {
                        ApiResponse mApiResponse = response.body();
                        if (mApiResponse != null && mApiResponse.status.equals("200")) {
                            Utils.showToast(getActivity(), "" + mApiResponse.message);
                            mCartCount = mApiResponse.cartCount;
                            if (mCartCount > 0) {
                                tvCartCount.setVisibility(View.VISIBLE);
                                tvCartCount.setText("" + mCartCount);
                                MyPref.setPref(getActivity(), MyPref.PREF_PARK_CART_COUNT, mCartCount);
                            } else {
                                tvCartCount.setVisibility(View.GONE);
                            }
                            LogUtils.e("", "getTimeInMillis::" + mCalDate.getTimeInMillis());
                            MyPref.setPref(getActivity(), MyPref.PREF_PARK_DATE, mCalDate.getTimeInMillis());
                            LogUtils.e("", "PREF_PARK_DATE::" + MyPref.getPref(getActivity(), MyPref.PREF_PARK_DATE, 0l));
                        } else if (mApiResponse != null && mApiResponse.status.equals("201")) {
                            //    Utils.showToast(getActivity(), mApiResponse.message);
                            deleteFromCart();
                        } else {
                            Utils.showToast(getActivity(), mApiResponse.message);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Utils.closeProgressDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAdultPrice() {

        tvAdultName.setText(mThemeParkDetails.tpName + " " + mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.adultPrice) + " x " + mThemeParkDetails.adultQuntity + " (" + getString(R.string.adult) + ")");
        tvAdultSubTotal.setText(mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.adultPrice * mThemeParkDetails.adultQuntity));

        if (mThemeParkDetails.adultQuntity > 0) {
            lnrAdult.setVisibility(View.VISIBLE);
        } else {
            lnrAdult.setVisibility(View.GONE);
        }

        setTotalPrice();
    }

    private void setChildPrice() {

        tvChildName.setText(mThemeParkDetails.tpName + " " + mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.childPrice) + " x " + mThemeParkDetails.childQuntity + " (" + getString(R.string.child) + ")");
        tvChildSubTotal.setText(mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.childPrice * mThemeParkDetails.childQuntity));

        if (mThemeParkDetails.childQuntity > 0) {
            lnrChild.setVisibility(View.VISIBLE);
        } else {
            lnrChild.setVisibility(View.GONE);
        }

        setTotalPrice();
    }

    private void setStudentPrice() {

        tvStudentName.setText(mThemeParkDetails.tpName + " " + mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.studPrice) + " x " + mThemeParkDetails.studentQuntity + " (" + getString(R.string.student) + ")");
        tvStudentSubTotal.setText(mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.studPrice * mThemeParkDetails.studentQuntity));

        if (mThemeParkDetails.studentQuntity > 0) {
            lnrStudent.setVisibility(View.VISIBLE);
        } else {
            lnrStudent.setVisibility(View.GONE);
        }

        setTotalPrice();
    }

    private void setTotalPrice() {

//        tvCartCount.setText("" + (mThemeParkDetails.adultQuntity + mThemeParkDetails.childQuntity + mThemeParkDetails.studentQuntity));
//        tvCartCount.setText("" + mThemeParkDetails.cartCount);
        //  Toast.makeText(getContext(), ""+ mThemeParkDetails.cartCount, Toast.LENGTH_SHORT).show();
        if (mCartCount > 0) {
            tvCartCount.setVisibility(View.VISIBLE);
            tvCartCount.setText("" + mCartCount);
        } else {
            tvCartCount.setVisibility(View.GONE);
        }


        mTotalPrice = (mThemeParkDetails.adultQuntity * mThemeParkDetails.adultPrice) +
                (mThemeParkDetails.childQuntity * mThemeParkDetails.childPrice) +
                (mThemeParkDetails.studentQuntity * mThemeParkDetails.studPrice);

        if (mTotalPrice > 0) {
            lnrSubTotal.setVisibility(View.VISIBLE);
            lnrTotal.setVisibility(View.VISIBLE);
        } else {
            lnrSubTotal.setVisibility(View.GONE);
            lnrTotal.setVisibility(View.GONE);
        }

        tvSubTotal.setText(mThemeParkDetails.currrency + " " + Utils.setPrice(mTotalPrice));
        tvTotalPrice.setText(mThemeParkDetails.currrency + " " + Utils.setPrice(mTotalPrice));


    }

}
