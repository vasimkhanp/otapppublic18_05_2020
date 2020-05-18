
package com.otapp.net.home.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.ParkPaymentReviewAdapter;
import com.otapp.net.adapter.ParkReviewCartItemAdapter;
import com.otapp.net.fragment.HomeFragment;
import com.otapp.net.fragment.ThemeParkPaymentFragment;
import com.otapp.net.model.ThemeParkReconfirmResponse;
import com.otapp.net.model.ThemeParkSuccessPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.SaveTicket;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookingsThemeParkSuccessDetails extends AppCompatActivity implements View.OnClickListener  {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPark)
    TextView tvPark;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvPlace)
    TextView tvPlace;
    @BindView(R.id.tvQuantity)
    TextView tvQuantity;
    @BindView(R.id.tvBuyerName)
    TextView tvBuyerName;
    @BindView(R.id.tvBookingID)
    TextView tvBookingID;
    @BindView(R.id.tvTotalTitle)
    TextView tvTotalTitle;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvCall)
    TextView tvCall;
    @BindView(R.id.tvShare)
    TextView tvShare;
    @BindView(R.id.tvResendConfirmation)
    TextView tvResendConfirmation;
    @BindView(R.id.lvCart)
    ListView lvCart;
    @BindView(R.id.lvTicketPrice)
    ListView lvTicketPrice;
    @BindView(R.id.ivThemePark)
    ImageView ivThemePark;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.aviProgress)
    AVLoadingIndicatorView aviProgress;
    @BindView(R.id.svContainer)
    ScrollView svContainer;
    @BindView(R.id.mainlayout)
    LinearLayout linearLayoutMain;

    private ThemeParkSuccessPojo mThemeParkSuccessPojo;

    ParkReviewCartItemAdapter mParkReviewCartItemAdapter;
    ParkPaymentReviewAdapter mParkPaymentReviewAdapter;
    public String finalAmount="";
    float price=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_theme_park_success_details);

        ButterKnife.bind(this);
        finalAmount= ThemeParkPaymentFragment.finalAmount;
        InitializeControls();
        aviProgress.setVisibility(View.GONE);
       /* mView.setFocusableInTouchMode(true);
        mView.requestFocus();*/
    }
    private void InitializeControls() {

        mParkReviewCartItemAdapter = new ParkReviewCartItemAdapter(getApplicationContext());
        lvCart.setAdapter(mParkReviewCartItemAdapter);
        String mResponse = getIntent().getExtras().getString(Constants.BNDL_THEME_PARK_RESPONSE);

            if (!TextUtils.isEmpty(mResponse)) {
                mThemeParkSuccessPojo = new Gson().fromJson(mResponse, ThemeParkSuccessPojo.class);

                if (mThemeParkSuccessPojo != null) {

                    tvPark.setText("" + mThemeParkSuccessPojo.type);
                    tvName.setText("" + mThemeParkSuccessPojo.parkName);
                    tvTitle.setText("" + mThemeParkSuccessPojo.parkName);
                    tvDate.setText("" + mThemeParkSuccessPojo.bookingDate);
                    tvTime.setText("" + mThemeParkSuccessPojo.timings);
                    tvPlace.setText("" + mThemeParkSuccessPojo.address);
                    tvBuyerName.setText("" + mThemeParkSuccessPojo.name);
                    tvQuantity.setText("" + mThemeParkSuccessPojo.totalTicket);


                    MyPref.setPref(getApplicationContext(), MyPref.PREF_PARK_DATE, 0l);
                    MyPref.setPref(getApplicationContext(), MyPref.PREF_PARK_CART_ID, "");
                    MyPref.setPref(getApplicationContext(), Constants.BNDL_PARK_DETAILS, "");
                    MyPref.setPref(getApplicationContext(), Constants.BNDL_PARK, "");
                    MyPref.setPref(getApplicationContext(), MyPref.PREF_PARK_CART_COUNT, 0);

                    if (!TextUtils.isEmpty(mThemeParkSuccessPojo.bookingId)) {
                        tvBookingID.setText("" + mThemeParkSuccessPojo.bookingId);
                        Bitmap mQRCode = Utils.getQRCode(mThemeParkSuccessPojo.bookingId);
                        if (mQRCode != null) {
                            ivQRCode.setImageBitmap(mQRCode);
                        }
                    }

                    if (!TextUtils.isEmpty(mThemeParkSuccessPojo.image)) {
                        aviProgress.setVisibility(View.VISIBLE);
                        Picasso.get().load(mThemeParkSuccessPojo.image).into(ivThemePark, new com.squareup.picasso.Callback() {
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

                    mParkReviewCartItemAdapter.addAll(mThemeParkSuccessPojo.data);

                    if (mThemeParkSuccessPojo.fareList != null && mThemeParkSuccessPojo.fareList.size() > 0) {
                        String mTitle = mThemeParkSuccessPojo.fareList.get(mThemeParkSuccessPojo.fareList.size() - 1).lable;
                        price = mThemeParkSuccessPojo.fareList.get(mThemeParkSuccessPojo.fareList.size() - 1).price;

                        if (!TextUtils.isEmpty(mTitle)) {
                            tvTotalTitle.setText("" + mTitle);
                        }

                        tvTotal.setText(mThemeParkSuccessPojo.currency + " " + Utils.setPrice(price));

                        mParkPaymentReviewAdapter = new ParkPaymentReviewAdapter(getApplicationContext(), mThemeParkSuccessPojo.currency);
                        lvTicketPrice.setAdapter(mParkPaymentReviewAdapter);
                        mParkPaymentReviewAdapter.addAll(mThemeParkSuccessPojo.fareList.subList(0, mThemeParkSuccessPojo.fareList.size() - 1));

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
            // popBackStack(ServiceFragment.Tag_ServiceFragment);
         //   switchFragment(HomeFragment.newInstance(), HomeFragment.Tag_HomeFragment, true);
            finish();

        } else if (view == tvShare) {

            if (mThemeParkSuccessPojo != null) {

                         String mShareText = mThemeParkSuccessPojo.parkName + " " + getString(R.string.booked) + " " + getString(R.string.qty)+" "
                        + mThemeParkSuccessPojo.totalTicket + " " +mThemeParkSuccessPojo.currency + " " + Utils.setPrice(price);
               try {
                    Bitmap bitmap = getBitmapFromView(linearLayoutMain, linearLayoutMain.getChildAt(0).getHeight(), linearLayoutMain.getChildAt(0).getWidth());
                    takeScreenshot(bitmap, mShareText);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        } else if (view == tvCall) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 5);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+255" + "677555999"));
                startActivity(intent);
            }


        } else if (view == tvResendConfirmation) {
            if (mThemeParkSuccessPojo != null && !TextUtils.isEmpty(mThemeParkSuccessPojo.bookingId)) {
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
    private void takeScreenshot(Bitmap b,String mShareText) {
        try {
            SaveTicket savecard = new SaveTicket();
            boolean result = savecard.saveMovieTicket(getApplicationContext(), b);
            // image naming and path  to include sd card  appending name you choose for file


                   /* Intent shareCardIntent = new Intent();
                    shareCardIntent.setAction(Intent.ACTION_SEND);
                    shareCardIntent.setType("image/jpg");
                    shareCardIntent.putExtra(Intent.EXTRA_TEXT, mShareText);
                    shareCardIntent.putExtra(Intent.EXTRA_STREAM,
                            Uri.parse(getActivity().getExternalCacheDir().getPath() + "/Otapp/"
                                    + SaveTicket.NameOfFile));
                    shareCardIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareCardIntent, "Share via"));*/
                  /*  Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
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
        Call<ThemeParkReconfirmResponse> mCall = mApiInterface.resendThemeParkConfirmation(mThemeParkSuccessPojo.bookingId);
        mCall.enqueue(new Callback<ThemeParkReconfirmResponse>() {
            @Override
            public void onResponse(Call<ThemeParkReconfirmResponse> call, Response<ThemeParkReconfirmResponse> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    ThemeParkReconfirmResponse mApiResponse = response.body();
                    if (mApiResponse != null && mApiResponse.status==200) {
                        Utils.showToast(getApplicationContext(), mApiResponse.message);
                    } else {
                        Utils.showToast(getApplicationContext(), mApiResponse.message);
                    }
                }

            }

            @Override
            public void onFailure(Call<ThemeParkReconfirmResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }
}