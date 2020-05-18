package com.otapp.net.Events.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.Async.Interface.OtappApiServices;
import com.otapp.net.Async.Interface.RestClient;
import com.otapp.net.Events.Adapter.EventReviewTicketTypeAdapter;
import com.otapp.net.Events.Core.ApiResponse;
import com.otapp.net.Events.Core.PaymentSuceesResponse;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;
import com.otapp.net.utils.AppConstants;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.SaveTicket;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketSuccessResponseActivity extends AppCompatActivity {
    @BindView(R.id.tvName)
    TextView tvName;
   /* @BindView(R.id.tvCity)
    TextView tvCity;*/
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
    /* @BindView(R.id.lvTicketPrice)
     ListView lvTicketPrice;*/
  /*  @BindView(R.id.tvSeatNumberTitle)
    TextView tvSeatNumberTitle;
    @BindView(R.id.tvSeatNumber)*/
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
    @BindView(R.id.tvTaxableAmount)
    TextView tvTaxableAmount;
    @BindView(R.id.tvDiscount)
    TextView tvDiscount;
    @BindView(R.id.tvTotalVat)
    TextView tvTotalVat;
    @BindView(R.id.tvConvenienceFee)
    TextView tvConvenienceFee;
    @BindView(R.id.tvTotalExclTax)
    TextView tvTotalExclTax;
    @BindView(R.id.tvBookingDateTime)
    TextView tvBookingDateTime;
    @BindView(R.id.tvPaymentType)
    TextView tvPaymentType;
    @BindView(R.id.back)
    ImageView backButton;
    @BindView(R.id.toolbarTitle)
    TextView pageTitle;

    EventReviewTicketTypeAdapter mEventReviewTicketTypeAdapter;
    PaymentSuceesResponse mEventSuccessPojo;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent callHome = new Intent(getApplicationContext(), HomeActivity.class);
        callHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(callHome);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_success_response);
        ButterKnife.bind(this);
        pageTitle.setText(R.string.event);
        pageTitle.setGravity(Gravity.CENTER);

      /*  backButton.setVisibility(View.GONE);*/
        InitializeControls();
    }

    public void InitializeControls() {
        mEventReviewTicketTypeAdapter = new EventReviewTicketTypeAdapter(getApplicationContext());
        lvTicketType.setAdapter(mEventReviewTicketTypeAdapter);

        String mResponse = getIntent().getExtras().getString(AppConstants.BNDL_MOVIE_RESPONSE);
        if (!mResponse.isEmpty()) {
            mEventSuccessPojo = new Gson().fromJson(mResponse, PaymentSuceesResponse.class);
            if (mEventSuccessPojo != null) {
                tvName.setText("" + mEventSuccessPojo.event_name);
                tvDate.setText("" + mEventSuccessPojo.event_date + " | " + mEventSuccessPojo.event_time);
                tvBuyerName.setText("" + mEventSuccessPojo.name);
           /*     tvCity.setText("" + mEventSuccessPojo.event_city);*/

                byte[] data = Base64.decode(mEventSuccessPojo.event_addrs, Base64.DEFAULT);
                try {
                    tvPlace.setText(new String(data, "UTF-8") + "," + mEventSuccessPojo.event_city);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                tvBookingID.setText("" + mEventSuccessPojo.tkt_no);
                List<PaymentSuceesResponse.Seats> mSeatsList = mEventSuccessPojo.seatsList;
                mEventReviewTicketTypeAdapter.addAll(mSeatsList);

                if (!TextUtils.isEmpty(mEventSuccessPojo.tkt_no)) {
                    tvBookingID.setText("" + mEventSuccessPojo.tkt_no);
                    Bitmap mQRCode = Utils.getQRCode(mEventSuccessPojo.tkt_no);
                    if (mQRCode != null) {
                        ivQRCode.setImageBitmap(mQRCode);
                    }
                }

                if (!TextUtils.isEmpty(mEventSuccessPojo.event_img)) {
                    //    aviProgress.setVisibility(View.VISIBLE);
                    Picasso.get().load(mEventSuccessPojo.event_img).into(ivEvent, new com.squareup.picasso.Callback() {
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

                tvTotalExclTax.setText(mEventSuccessPojo.tkt_cur+" "+mEventSuccessPojo.tot_excl_tax);
                tvTotalVat.setText(mEventSuccessPojo.tkt_cur+" "+mEventSuccessPojo.tot_vat);
                tvDiscount.setText(mEventSuccessPojo.tkt_cur+" "+mEventSuccessPojo.discount);
                tvConvenienceFee.setText(mEventSuccessPojo.tkt_cur+" "+mEventSuccessPojo.conv_fee);
                tvTaxableAmount.setText(mEventSuccessPojo.tkt_cur+" "+mEventSuccessPojo.taxable_amount);
                tvTotal.setText(mEventSuccessPojo.tkt_cur+" "+mEventSuccessPojo.paid_amount);

                tvBookingDateTime.setText(mEventSuccessPojo.booking_time);
                tvBuyerName.setText(mEventSuccessPojo.booked_by);
                tvPaymentType.setText(mEventSuccessPojo.pay_type);
                MyPref.setPref(getApplicationContext(), MyPref.PREF_PROMO_FLAG,"0");


               /* StringBuilder strSeatBuilder = new StringBuilder();
                if (mSeatsList != null && mSeatsList.size() > 0) {

                    for (int i = 0; i < mSeatsList.size(); i++) {
                        if (!TextUtils.isEmpty(mSeatsList.get(i).seats)) {
                            if (strSeatBuilder.length() > 0) {
                                strSeatBuilder.append("," + mSeatsList.get(i).seats);
                            } else {
                                strSeatBuilder.append(mSeatsList.get(i).seats);
                            }
                        }
                    }

                    if (strSeatBuilder != null && strSeatBuilder.length() > 0) {
                        tvSeatNumber.setText("" + strSeatBuilder.toString());
                    }

                }*/

            }
        }

    }

    @OnClick(R.id.tvResendConfirmation)
    public void resendConfirmationClick(){
        resendConfirmation();
    }

    @OnClick(R.id.tvShare)
    public void shareTicket(){
        if(mEventSuccessPojo!=null){
            String mShareText = mEventSuccessPojo.event_name + " " + getString(R.string.booked) + " " + getString(R.string.seat_no) + " "
                   + getString(R.string.price) + " " + mEventSuccessPojo.tkt_cur + " " + mEventSuccessPojo.paid_amount;

            try {
                Bitmap bitmap = getBitmapFromView(linearLayoutMain, linearLayoutMain.getChildAt(0).getHeight(), linearLayoutMain.getChildAt(0).getWidth());
                takeScreenshot(bitmap, mShareText);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
    @OnClick(R.id.back)
    public void back(){
        Intent callHome = new Intent(getApplicationContext(), HomeActivity.class);
        callHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(callHome);
        finish();
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
    private void takeScreenshot(Bitmap b, final String mShareText) {
        try {
            SaveTicket savecard = new SaveTicket();
            boolean result = savecard.saveMovieTicket(getApplicationContext(), b);
            // image naming and path  to include sd card  appending name you choose for file
            this.runOnUiThread(new Runnable() {
                public void run() {

                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                 /*   whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, mShareText);
                    whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getApplicationContext().getExternalCacheDir().getPath() + "/Otapp/"
                            + SaveTicket.NameOfFile));
                    whatsappIntent.setType("image/jpeg");
                    whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        try {
                       startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Utils.showToast(getApplicationContext(), "Whatsapp have not been installed.");
                    }
                    */

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                  shareIntent.putExtra(Intent.EXTRA_TEXT,mShareText);
                  shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getApplicationContext().getExternalCacheDir().getPath() + "/Otapp/"
                          + SaveTicket.NameOfFile));
                    shareIntent.setType("*/*");
                    startActivity(Intent.createChooser(shareIntent,""));


                }
            });
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

        Utils.showProgressDialog(this);

      //  ApiInterface mApiInterface = RestClient.getClient(true);
        OtappApiServices otappApiServices = RestClient.getClient(true);
        Call<ApiResponse> mCall = otappApiServices.resendEventConfirmation(mEventSuccessPojo.tkt_no, "");
        Log.d("Log",mEventSuccessPojo.tkt_no);
        Log.d("Log",RestClient.API_PREFIX+ AppConstants.ApiNames.RESEND_CONFIRMATION);

        mCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    ApiResponse mApiResponse = response.body();
                    if (mApiResponse != null && mApiResponse.status.equalsIgnoreCase("200")) {
                       // Utils.showToast(getApplicationContext(), mApiResponse.message);
                        Toast.makeText(TicketSuccessResponseActivity.this, ""+mApiResponse.message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TicketSuccessResponseActivity.this, ""+mApiResponse.message, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }


}
