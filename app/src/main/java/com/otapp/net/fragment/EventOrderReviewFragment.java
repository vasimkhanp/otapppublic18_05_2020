package com.otapp.net.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.EventReviewTicketPriceAdapter;
import com.otapp.net.adapter.EventReviewTicketTypeAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.model.EventSuccessPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.SaveTicket;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventOrderReviewFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_EventOrderReviewFragment = "Tag_" + "EventOrderReviewFragment";

    View mView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvBuyerName)
    TextView tvBuyerName;
    @BindView(R.id.tvPlace)
    TextView tvPlace;
    @BindView(R.id.ivEvent)
    ImageView ivEvent;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.lvTicketType)
    ListView lvTicketType;
    @BindView(R.id.lvTicketPrice)
    ListView lvTicketPrice;
    @BindView(R.id.tvSeatNumberTitle)
    TextView tvSeatNumberTitle;
    @BindView(R.id.tvSeatNumber)
    TextView tvSeatNumber;
    @BindView(R.id.tvBookingID)
    TextView tvBookingID;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvCall)
    TextView tvCall;
    @BindView(R.id.tvTotalTitle)
    TextView tvTotalTitle;
    @BindView(R.id.tvShare)
    TextView tvShare;
    @BindView(R.id.tvResendConfirmation)
    TextView tvResendConfirmation;
    @BindView(R.id.lnrContainer)
    LinearLayout lnrContainer;
    @BindView(R.id.aviProgress)
    AVLoadingIndicatorView aviProgress;
    @BindView(R.id.svContain)
    NestedScrollView svContainer;
    @BindView(R.id.mainlayout)
     LinearLayout linearLayoutMain;

    EventReviewTicketTypeAdapter mEventReviewTicketTypeAdapter;
    EventReviewTicketPriceAdapter mEventReviewTicketPriceAdapter;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");


    private float mTotalPrice = 0;

    private EventSuccessPojo.EventSuccess mEventSuccessPojo;


    public static EventOrderReviewFragment newInstance() {
        EventOrderReviewFragment fragment = new EventOrderReviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_event_order_review, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();
        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    switchFragment(HomeFragment.newInstance(), HomeFragment.Tag_HomeFragment, true);
                }
                return true;
            }
        });
        return mView;
    }

    private void InitializeControls() {

        mEventReviewTicketTypeAdapter = new EventReviewTicketTypeAdapter(getActivity());
        lvTicketType.setAdapter(mEventReviewTicketTypeAdapter);

        mEventReviewTicketPriceAdapter = new EventReviewTicketPriceAdapter(getActivity());
        lvTicketPrice.setAdapter(mEventReviewTicketPriceAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String mResponse = bundle.getString(Constants.BNDL_MOVIE_RESPONSE);
            if (!TextUtils.isEmpty(mResponse)) {
                mEventSuccessPojo = new Gson().fromJson(mResponse, EventSuccessPojo.EventSuccess.class);
                if (mEventSuccessPojo != null) {
                    tvName.setText("" + mEventSuccessPojo.eventName);
                    tvDate.setText("" + mEventSuccessPojo.eventDate + " | " + mEventSuccessPojo.eventTime);
//                    tvTime.setText("" + mEventSuccessPojo.eventTime);
                    tvBuyerName.setText("" + mEventSuccessPojo.custName);
                    tvCity.setText("" + mEventSuccessPojo.eventCity);
                    tvPlace.setText("" + mEventSuccessPojo.eventAddress);

                    tvBookingID.setText("" + mEventSuccessPojo.bookingId);
                    List<EventSuccessPojo.Seats> mSeatsList = mEventSuccessPojo.seats;
                    List<EventSuccessPojo.Fare> mFareList = mEventSuccessPojo.fareList;

                    mEventReviewTicketTypeAdapter.addAll(mSeatsList);

                    if (mFareList != null && mFareList.size() > 0) {

                        mTotalPrice = mFareList.get(mFareList.size() - 1).price;
                        String mTitle = mFareList.get(mFareList.size() - 1).lable;
                        String sValue = String.valueOf(decimalFormat.format(mTotalPrice));
                        tvTotal.setText(mEventSuccessPojo.currency + " " + sValue);

                        if (!TextUtils.isEmpty(mTitle)) {
                            tvTotalTitle.setText("" + mTitle);
                        }

                        mEventReviewTicketPriceAdapter.addAll(mFareList.subList(0, mFareList.size() - 1), mEventSuccessPojo.currency);
                    }

                    if (!TextUtils.isEmpty(mEventSuccessPojo.bookingId)) {
                        tvBookingID.setText("" + mEventSuccessPojo.bookingId);
                        Bitmap mQRCode = Utils.getQRCode(mEventSuccessPojo.bookingId);
                        if (mQRCode != null) {
                            ivQRCode.setImageBitmap(mQRCode);
                        }
                    }

                    if (!TextUtils.isEmpty(mEventSuccessPojo.image)) {
                        //    aviProgress.setVisibility(View.VISIBLE);
                        Picasso.get().load(mEventSuccessPojo.image).into(ivEvent, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                aviProgress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                aviProgress.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        aviProgress.setVisibility(View.GONE);
                    }

                    StringBuilder strSeatBuilder = new StringBuilder();
                    if (mSeatsList != null && mSeatsList.size() > 0) {

                        for (int i = 0; i < mSeatsList.size(); i++) {
                            if (!TextUtils.isEmpty(mSeatsList.get(i).Seat)) {
                                if (strSeatBuilder.length() > 0) {
                                    strSeatBuilder.append("," + mSeatsList.get(i).Seat);
                                } else {
                                    strSeatBuilder.append(mSeatsList.get(i).Seat);
                                }
                            }
                        }

                        if (strSeatBuilder != null && strSeatBuilder.length() > 0) {
                            tvSeatNumber.setText("" + strSeatBuilder.toString());
                        }

                    }

                }
            }
        }
        aviProgress.setVisibility(View.GONE);
        tvBack.setOnClickListener(this);
        tvCall.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvResendConfirmation.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            //  popBackStack(ServiceFragment.Tag_ServiceFragment);
            switchFragment(HomeFragment.newInstance(), HomeFragment.Tag_HomeFragment, true);
        } else if (view == tvCall) {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 5);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+255" + "677555999"));
                startActivity(intent);
            }

        } else if (view == tvShare) {

            if (mEventSuccessPojo != null) {

                String mShareText = mEventSuccessPojo.eventName + " " + getString(R.string.booked) + " " + getString(R.string.seat_no) + " "
                        + tvSeatNumber.getText().toString() + " " + getString(R.string.price) + " " + mEventSuccessPojo.currency + " " + Utils.setPrice(mTotalPrice);

               /* Bitmap bitmap = getBitmapFromView(svContainer, svContainer.getNestedScrollAxes(), svContainer.getNestedScrollAxes());
                takeScreenshot(bitmap,"hello");*/
               try {
                   Bitmap bitmap = getBitmapFromView(linearLayoutMain, linearLayoutMain.getChildAt(0).getHeight(), linearLayoutMain.getChildAt(0).getWidth());
                   takeScreenshot(bitmap, mShareText);
               }catch (Exception e){
                   e.printStackTrace();
               }
/*
                try {
                    Dialog fb_event_info = new Dialog(getContext());
                    fb_event_info.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    fb_event_info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    fb_event_info.setContentView(R.layout.fragment_event_order_review);


                    TextView tvName = fb_event_info.findViewById(R.id.tvName);
                    TextView tvDate = fb_event_info.findViewById(R.id.tvDate);
                    TextView tvBuyerName = fb_event_info.findViewById(R.id.tvBuyerName);
                    TextView tvCity = fb_event_info.findViewById(R.id.tvCity);
                    TextView tvPlace = fb_event_info.findViewById(R.id.tvPlace);
                    TextView tvTotal = fb_event_info.findViewById(R.id.tvTotal);

                    ImageView ivEvent = fb_event_info.findViewById(R.id.ivEvent);

                    ImageView ivQRCode = fb_event_info.findViewById(R.id.ivQRCode);
                    ListView lvTicketType = fb_event_info.findViewById(R.id.lvTicketType);
                    ListView lvTicketPrice = fb_event_info.findViewById(R.id.lvTicketPrice);
                    TextView tvSeatNumber = fb_event_info.findViewById(R.id.tvSeatNumber);
                    TextView tvBookingID = fb_event_info.findViewById(R.id.tvBookingID);

                    TextView tvTotalTitle = fb_event_info.findViewById(R.id.tvTotalTitle);
                    AVLoadingIndicatorView aviProgress = fb_event_info.findViewById(R.id.aviProgress);
                    NestedScrollView svContainer = fb_event_info.findViewById(R.id.svContain);


                    mEventReviewTicketTypeAdapter = new EventReviewTicketTypeAdapter(getActivity());
                    lvTicketType.setAdapter(mEventReviewTicketTypeAdapter);

                    mEventReviewTicketPriceAdapter = new EventReviewTicketPriceAdapter(getActivity());
                    lvTicketPrice.setAdapter(mEventReviewTicketPriceAdapter);

                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        String mResponse = bundle.getString(Constants.BNDL_MOVIE_RESPONSE);
                        if (!TextUtils.isEmpty(mResponse)) {
                            mEventSuccessPojo = new Gson().fromJson(mResponse, EventSuccessPojo.EventSuccess.class);
                            if (mEventSuccessPojo != null) {
                                tvName.setText("" + mEventSuccessPojo.eventName);
                                tvDate.setText("" + mEventSuccessPojo.eventDate + " | " + mEventSuccessPojo.eventTime);
//                    tvTime.setText("" + mEventSuccessPojo.eventTime);
                                tvBuyerName.setText("" + mEventSuccessPojo.custName);
                                tvCity.setText("" + mEventSuccessPojo.eventCity);
                                tvPlace.setText("" + mEventSuccessPojo.eventAddress);

                                tvBookingID.setText("" + mEventSuccessPojo.bookingId);
                                List<EventSuccessPojo.Seats> mSeatsList = mEventSuccessPojo.seats;
                                List<EventSuccessPojo.Fare> mFareList = mEventSuccessPojo.fareList;

                                mEventReviewTicketTypeAdapter.addAll(mSeatsList);

                                if (mFareList != null && mFareList.size() > 0) {

                                    mTotalPrice = mFareList.get(mFareList.size() - 1).price;
                                    String mTitle = mFareList.get(mFareList.size() - 1).lable;
                                    String sValue = String.valueOf(decimalFormat.format(mTotalPrice));
                                    tvTotal.setText(mEventSuccessPojo.currency + " " + sValue);

                                    if (!TextUtils.isEmpty(mTitle)) {
                                        tvTotalTitle.setText("" + mTitle);
                                    }

                                    mEventReviewTicketPriceAdapter.addAll(mFareList.subList(0, mFareList.size() - 1), mEventSuccessPojo.currency);
                                }

                                if (!TextUtils.isEmpty(mEventSuccessPojo.bookingId)) {
                                    tvBookingID.setText("" + mEventSuccessPojo.bookingId);
                                    Bitmap mQRCode = Utils.getQRCode(mEventSuccessPojo.bookingId);
                                    if (mQRCode != null) {
                                        ivQRCode.setImageBitmap(mQRCode);
                                    }
                                }

                                if (!TextUtils.isEmpty(mEventSuccessPojo.image)) {
                                    Picasso.get().load(mEventSuccessPojo.image).into(ivEvent, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {
                                            aviProgress.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            aviProgress.setVisibility(View.GONE);
                                        }
                                    });
                                } else {
                                    aviProgress.setVisibility(View.GONE);
                                }

                                StringBuilder strSeatBuilder = new StringBuilder();
                                if (mSeatsList != null && mSeatsList.size() > 0) {

                                    for (int i = 0; i < mSeatsList.size(); i++) {
                                        if (!TextUtils.isEmpty(mSeatsList.get(i).Seat)) {
                                            if (strSeatBuilder.length() > 0) {
                                                strSeatBuilder.append("," + mSeatsList.get(i).Seat);
                                            } else {
                                                strSeatBuilder.append(mSeatsList.get(i).Seat);
                                            }
                                        }
                                    }

                                    if (strSeatBuilder != null && strSeatBuilder.length() > 0) {
                                        tvSeatNumber.setText("" + strSeatBuilder.toString());
                                    }

                                }

                            }
                        }
                    }
                    final NestedScrollView lnr_fb_info = svContainer;

                    lnr_fb_info.setDrawingCacheEnabled(true);

                    lnr_fb_info.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                    lnr_fb_info.layout(0, 0, lnr_fb_info.getMeasuredWidth(), lnr_fb_info.getMeasuredHeight());
                    lnr_fb_info.buildDrawingCache(true);


                    Bitmap bitmap = Bitmap.createBitmap(lnr_fb_info.getDrawingCache());

                    takeScreenshot(bitmap, "hello");

                } catch (Exception e) {
                    Log.d("Log", "Error : " + e.getMessage());
                    Toast.makeText(getActivity(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
*/

                //  final NestedScrollView lnr_fb_info = fb_event_info.findViewById(R.id.svContain);

                //     new ShareTicketTask(mShareText).execute();

//                ShareCompat.IntentBuilder
//                        .from(getActivity())
//                        .setText(mShareText)
////                        .setStream(Uri.parse(mEventSuccessPojo.image))
//                        .setType("text/plain")
//                        .setChooserTitle(getString(R.string.app_name))
//                        .startChooser();
            }
        } else if (view == tvResendConfirmation) {
            if (mEventSuccessPojo != null && !TextUtils.isEmpty(mEventSuccessPojo.bookingId)) {
                resendConfirmation();
            }
        }
    }

    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }
    private void takeScreenshot(Bitmap b, String mShareText) {
        try {
            SaveTicket savecard = new SaveTicket();
            boolean result = savecard.saveMovieTicket(getActivity(), b);
            // image naming and path  to include sd card  appending name you choose for file
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                  /*  Intent shareCardIntent = new Intent();
                    shareCardIntent.setAction(Intent.ACTION_SEND);
                    shareCardIntent.setType("image/jpg");
                    shareCardIntent.putExtra(Intent.EXTRA_TEXT, mShareText);
                    shareCardIntent.putExtra(Intent.EXTRA_STREAM,
                            Uri.parse(getActivity().getExternalCacheDir().getPath() + "/Otapp/"
                                    + SaveTicket.NameOfFile));
                    shareCardIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareCardIntent, "Share via"));*/
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, mShareText);
                    whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getActivity().getExternalCacheDir().getPath() + "/Otapp/"
                            + SaveTicket.NameOfFile));
                    whatsappIntent.setType("image/jpeg");
                    whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        getActivity().startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Utils.showToast(getActivity(), "Whatsapp have not been installed.");
                    }
                }
            });
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void resendConfirmation() {
        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ApiResponse> mCall = mApiInterface.resendEventConfirmation(mEventSuccessPojo.bookingId, Otapp.mUniqueID);
        mCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null && mApiResponse.status.equalsIgnoreCase("200")) {
                        Utils.showToast(getActivity(), mApiResponse.message);
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

    }

    class ShareTicketTask extends AsyncTask<String, String, String> {

        Bitmap mCurrentBitmap = null;
        private String mShareText = "";

        public ShareTicketTask(String mShareText) {
            this.mShareText = mShareText;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProgressDialog(getActivity());
//            int totalHeight = svContainer.getChildAt(0).getHeight();
//            int totalWidth = svContainer.getChildAt(0).getWidth();

            svContainer.setDrawingCacheEnabled(true);
//            lnrContainer.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//            lnrContainer.layout(0, 0, lnrContainer.getMeasuredWidth(), lnrContainer.getMeasuredHeight());
//            lnrContainer.buildDrawingCache(true);
            Bitmap mTempBitmap = svContainer.getDrawingCache();
            if (mTempBitmap != null) {
                LogUtils.e("", "mTempBitmap is not null");
                mCurrentBitmap = Bitmap.createBitmap(mTempBitmap);
            } else {
                LogUtils.e("", "mTempBitmap is null");
            }

            svContainer.setDrawingCacheEnabled(false);
        }

        @Override
        protected String doInBackground(String... params) {


            SaveTicket savecard = new SaveTicket();
            boolean result = savecard.saveMovieTicket(getActivity(), mCurrentBitmap);
            LogUtils.e("", "result::" + result);
            if (result) {
                return "done";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            Utils.closeProgressDialog();
            LogUtils.e("", "onPostExecute result::" + result);
            if (!TextUtils.isEmpty(result) && result.equalsIgnoreCase("done")) {
                LogUtils.e("", "Running");
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent shareCardIntent = new Intent();
                        shareCardIntent.setAction(Intent.ACTION_SEND);
                        shareCardIntent.setType("*/*");
                        shareCardIntent.putExtra(Intent.EXTRA_TEXT, mShareText);
                        shareCardIntent.putExtra(Intent.EXTRA_STREAM,
                                Uri.parse(getActivity().getExternalCacheDir().getPath() + "/Otapp/"
                                        + SaveTicket.NameOfFile));
                        shareCardIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareCardIntent, "Share via"));
                    }
                });
            }

        }
    }
}
