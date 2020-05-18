package com.otapp.net.home.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.MovieReviewPaymentAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.fragment.HomeFragment;
import com.otapp.net.fragment.ServiceFragment;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.MovieSuccessPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.SaveTicket;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookingMoviesDetails extends AppCompatActivity implements View.OnClickListener {
    public static String Tag_MovieOrderReviewFragment = "Tag_" + "MovieOrderReviewFragment";

    View mView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvPlace)
    TextView tvPlace;
    @BindView(R.id.tvQuantity)
    TextView tvQuantity;
    @BindView(R.id.tvScreen)
    TextView tvScreen;
    @BindView(R.id.tvSeat)
    TextView tvSeat;
    @BindView(R.id.tvBookingID)
    TextView tvBookingID;
    //    @BindView(R.id.tvTicketPriceTitle)
//    TextView tvTicketPriceTitle;
//    @BindView(R.id.tvTicketPrice)
//    TextView tvTicketPrice;
//    @BindView(R.id.tv3DGlasses)
//    TextView tv3DGlasses;
//    @BindView(R.id.tvIHF)
//    TextView tvIHF;
//    @BindView(R.id.tvTaxes)
//    TextView tvTaxes;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvBuyerName)
    TextView tvBuyerName;
    @BindView(R.id.tvCall)
    TextView tvCall;
    @BindView(R.id.tvShare)
    TextView tvShare;
    @BindView(R.id.tvResendConfirmation)
    TextView tvResendConfirmation;
    //    @BindView(R.id.tvCombo)
//    TextView tvCombo;
//    @BindView(R.id.tvConvenienceCharge)
//    TextView tvConvenienceCharge;
//    @BindView(R.id.rltCombo)
//    RelativeLayout rltCombo;
//    @BindView(R.id.rlt3dGlasses)
//    RelativeLayout rlt3dGlasses;
    @BindView(R.id.ivMovie)
    ImageView ivMovie;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.ivAds1)
    ImageView ivAds1;
    @BindView(R.id.ivAds2)
    ImageView ivAds2;
    @BindView(R.id.lvSeat)
    ListView lvSeat;
    @BindView(R.id.lnrContainer)
    LinearLayout lnrContainer;
    @BindView(R.id.svContainer)
    ScrollView svContainer;
    @BindView(R.id.aviProgress)
    AVLoadingIndicatorView aviProgress;
    @BindView(R.id.mainlayout)
    LinearLayout linearLayoutMain;

    private MovieSuccessPojo.MovieSuccess mMovieSuccessPojo;

    private MovieReviewPaymentAdapter mMoviePaymentAdapter;

    private float mTotalAmount = 0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking_movies_details);
        ButterKnife.bind(this);

        Utils.closeProgressDialog();
        aviProgress.setVisibility(View.GONE);
        InitializeControls();
       /* mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    switchFragment(HomeFragment.newInstance(), HomeFragment.Tag_HomeFragment, true);
                }
                return true;
            }
        });*/
    }
    private void InitializeControls() {

       String response=getIntent().getExtras().getString(Constants.BNDL_MOVIE_RESPONSE);
        if (response != null) {
            if (!TextUtils.isEmpty(response)) {
                mMovieSuccessPojo = new Gson().fromJson(response, MovieSuccessPojo.MovieSuccess.class);
                if (mMovieSuccessPojo != null) {
                    tvTitle.setText("" + mMovieSuccessPojo.movieName);
                    tvName.setText("" + mMovieSuccessPojo.movieName);
                    tvLanguage.setText("(" + mMovieSuccessPojo.movieLang + ")");
                    tvDate.setText("" + mMovieSuccessPojo.showDate + " | " + mMovieSuccessPojo.showTime);
//                    tvTime.setText("" + mMovieSuccessPojo.showTime);
                    tvBuyerName.setText("" + mMovieSuccessPojo.custName);
                    if (!TextUtils.isEmpty(mMovieSuccessPojo.address)) {
                        tvPlace.setText("" + mMovieSuccessPojo.theaterScreen + "\n" + mMovieSuccessPojo.address);
                    } else {
                        tvPlace.setText("" + mMovieSuccessPojo.theaterScreen);
                    }
                    tvQuantity.setText("" + mMovieSuccessPojo.processSeats); // mMovieSuccessPojo.maxSeats);
//                    tvScreen.setText("" + mMovieSuccessPojo.screenNumber);
                    tvSeat.setText("" + mMovieSuccessPojo.screenNumber);

                    tvVersion.setText("" + mMovieSuccessPojo.movieFormat);

                    mMoviePaymentAdapter = new MovieReviewPaymentAdapter(getApplicationContext(), mMovieSuccessPojo.currency);
                    lvSeat.setAdapter(mMoviePaymentAdapter);

//                    tvTicketPriceTitle.setText(getString(R.string.tickets) + " (" + mMovieSuccessPojo.maxSeats + ")");
//                    tvTicketPrice.setText(mMovieSuccessPojo.currency + " " + Utils.setPrice(mMovieSuccessPojo.moviePrice));
//                    tvIHF.setText(mMovieSuccessPojo.currency + " " + mMovieSuccessPojo.ihf);
//                    tvTaxes.setText(mMovieSuccessPojo.currency + " " + mMovieSuccessPojo.tax);
//
//
//                    if (mMovieSuccessPojo.combosPrice > 0) {
//                        rltCombo.setVisibility(View.VISIBLE);
//                        tvCombo.setText(mMovieSuccessPojo.currency + " " + Utils.setPrice(mMovieSuccessPojo.combosPrice));
//                    } else {
//                        rltCombo.setVisibility(View.GONE);
//                    }
//
//                    if (mMovieSuccessPojo.glass_count > 0) {
//                        rlt3dGlasses.setVisibility(View.VISIBLE);
//                        tv3DGlasses.setText(mMovieSuccessPojo.currency + " " + Utils.setPrice(mMovieSuccessPojo.glass_count * mMovieSuccessPojo.glass_price));
//                    } else {
//                        rlt3dGlasses.setVisibility(View.GONE);
//                    }
//
//                    int cnvFixedFee = 0, cnvTotalFee = 0;
//                    if (mMovieSuccessPojo.cnv_fixed_fee > 0) {
//                        cnvFixedFee = mMovieSuccessPojo.cnv_fixed_fee;
//                    }
//
//                    int cnvPerFee = 0;
//                    if (mMovieSuccessPojo.cnv_per_fee > 0) {
//                        cnvPerFee = (int) (mMovieSuccessPojo.totalAmount * ((float)mMovieSuccessPojo.cnv_per_fee / (float)100));
//                    }
//
//                    cnvTotalFee = cnvFixedFee + cnvPerFee;
//
//                    tvConvenienceCharge.setText(mMovieSuccessPojo.currency + " " + Utils.setPrice(cnvTotalFee));

                    List<MovieSuccessPojo.Fare> mPaymentSummaryList = mMovieSuccessPojo.fareList;

                    if (mPaymentSummaryList != null && mPaymentSummaryList.size() > 0) {

                        mTotalAmount = mPaymentSummaryList.get(mPaymentSummaryList.size() - 1).price;
                        tvTotal.setText(mMovieSuccessPojo.currency + " " + Utils.setPrice(mTotalAmount));

                        mMoviePaymentAdapter.addAll(mPaymentSummaryList.subList(0, mPaymentSummaryList.size() - 1));
                    }


                    if (!TextUtils.isEmpty(mMovieSuccessPojo.bookingId)) {
                        tvBookingID.setText("" + mMovieSuccessPojo.bookingId);
                        Bitmap mQRCode = Utils.getQRCode(mMovieSuccessPojo.bookingId);
                        if (mQRCode != null) {
                            ivQRCode.setImageBitmap(mQRCode);
                        }
                    }

                    if (!TextUtils.isEmpty(mMovieSuccessPojo.image)) {
                        //   aviProgress.setVisibility(View.VISIBLE);
                        Picasso.get().load(mMovieSuccessPojo.image).into(ivMovie, new com.squareup.picasso.Callback() {
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

                }
            }
        }

        if (Otapp.mAdsPojoList != null && Otapp.mAdsPojoList.size() > 0) {
            for (int i = 0; i < Otapp.mAdsPojoList.size(); i++) {
                CountryCodePojo.Ad5 mAds = Otapp.mAdsPojoList.get(i);
                if (mAds != null) {
                    if (mAds.page.equalsIgnoreCase("Ticket page") && mAds.location.equalsIgnoreCase("Bottom Banner 1")) {
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
                    } else if (mAds.page.equalsIgnoreCase("Ticket page") && mAds.location.equalsIgnoreCase("Bottom Banner 2")) {
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
        aviProgress.setVisibility(View.GONE);
        tvBack.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvCall.setOnClickListener(this);
        tvResendConfirmation.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            finish();
        } else if (view == tvShare) {
            if (mMovieSuccessPojo != null) {
                String mShareText = mMovieSuccessPojo.movieName + " " + getString(R.string.booked) + " " + getString(R.string.seat_no) + " "
                        + mMovieSuccessPojo.processSeats + " " + getString(R.string.price) + " " + mMovieSuccessPojo.currency + " " + Utils.setPrice(mTotalAmount);
                try {
                    Bitmap bitmap = getBitmapFromView(linearLayoutMain, linearLayoutMain.getChildAt(0).getHeight(), linearLayoutMain.getChildAt(0).getWidth());
                    takeScreenshot(bitmap, mShareText);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        } else if (view == tvCall) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 5);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+255" + "677555999"));
                startActivity(intent);
            }


        } else if (view == tvResendConfirmation) {
            if (mMovieSuccessPojo != null && !TextUtils.isEmpty(mMovieSuccessPojo.bookingId)) {
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
            boolean result = savecard.saveMovieTicket(getApplicationContext(), b);
            // image naming and path  to include sd card  appending name you choose for file
          /*  getActivity().runOnUiThread(new Runnable() {
                public void run() {*/
                  /*  Intent shareCardIntent = new Intent(android.content.Intent.ACTION_SEND);
                 //   shareCardIntent.setAction(Intent.ACTION_SEND);
                    shareCardIntent.setType("application/image");
                    //shareCardIntent.setType("image/jpeg");
                    shareCardIntent.putExtra(android.content.Intent.EXTRA_TEXT, mShareText);
                    shareCardIntent.putExtra(Intent.EXTRA_STREAM,
                            Uri.parse(getActivity().getExternalCacheDir().getPath() + "/Otapp/"
                                    + SaveTicket.NameOfFile));
                    shareCardIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareCardIntent, "Share via"));*/
                   /* Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, mShareText);
                    whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getApplicationContext().getExternalCacheDir().getPath() + "/Otapp/"
                            + SaveTicket.NameOfFile));
                    whatsappIntent.setType("image/jpeg");
                    whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        getApplicationContext().startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Utils.showToast(getApplicationContext(), "Whatsapp have not been installed.");
                    }*/
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putExtra(Intent.EXTRA_TEXT,mShareText);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getApplicationContext().getExternalCacheDir().getPath() + "/Otapp/"
                    + SaveTicket.NameOfFile));
            shareIntent.setType("*/*");
            startActivity(Intent.createChooser(shareIntent,""));
           /*     }
            });*/
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void resendConfirmation() {
        if (!Utils.isInternetConnected(getApplicationContext())) {
            Utils.showToast(getApplicationContext(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getApplicationContext());

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ApiResponse> mCall = mApiInterface.resendMovieConfirmation(mMovieSuccessPojo.bookingId, Otapp.mUniqueID);
        mCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null && mApiResponse.status.equalsIgnoreCase("200")) {
                        Utils.showToast(getApplicationContext(), mApiResponse.message);
                    } else {
                        Utils.showToast(getApplicationContext(), mApiResponse.message);
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

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

            if (!Utils.isInternetConnected(getApplicationContext())) {
                Utils.showToast(getApplicationContext(), getString(R.string.msg_no_internet));
                return;
            }

         /*  popBackStack(ServiceFragment.Tag_ServiceFragment);

            Fragment mFragment = ServiceFragment.newInstance();
            ((ServiceFragment) mFragment).setPosition(i);
            switchFragment(mFragment, ServiceFragment.Tag_ServiceFragment);*/

        }

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
            Utils.showProgressDialog(getApplicationContext());
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
            boolean result = savecard.saveMovieTicket(getApplicationContext(), mCurrentBitmap);
            LogUtils.e("", "result::" + result);
            if (result) {
                return "done";
            }
            return null;
        }

//        @Override
//        protected void onPostExecute(String result) {
//
//            Utils.closeProgressDialog();
//            LogUtils.e("", "onPostExecute result::" + result);
//            if (!TextUtils.isEmpty(result) && result.equalsIgnoreCase("done")) {
//                LogUtils.e("", "Running");
//                this.runOnUiThread(new Runnable() {
//                    public void run() {
//
//                        Intent shareCardIntent = new Intent();
//                        shareCardIntent.setAction(Intent.ACTION_SEND);
//                        shareCardIntent.setType("*/*");
//                        shareCardIntent.putExtra(Intent.EXTRA_TEXT, mShareText);
//                        shareCardIntent.putExtra(Intent.EXTRA_STREAM,
//                                Uri.parse(getActivity().getExternalCacheDir().getPath() + "/Otapp/"
//                                        + SaveTicket.NameOfFile));
//                        shareCardIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        startActivity(Intent.createChooser(shareCardIntent, "Share via"));
//                    }
//                });
//            }
//
//        }
    }
}
