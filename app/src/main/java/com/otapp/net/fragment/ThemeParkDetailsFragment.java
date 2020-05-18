package com.otapp.net.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.ParkAttractionAdapter;
import com.otapp.net.adapter.ParkGalleryAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.ThemeParkDetailsPojo;
import com.otapp.net.model.ThemeParkPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.MyPref;
import com.otapp.net.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemeParkDetailsFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_ThemeParkDetailsFragment = "Tag_" + "ThemeParkDetailsFragment";

    View mView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvTiming)
    TextView tvTiming;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvAdult)
    TextView tvAdult;
    @BindView(R.id.tvChild)
    TextView tvChild;
    @BindView(R.id.tvStudent)
    TextView tvStudent;
    @BindView(R.id.tvAbout)
    TextView tvAbout;
    @BindView(R.id.tvContinueBooking)
    TextView tvContinueBooking;
    @BindView(R.id.tvImageGallery)
    TextView tvImageGallery;
    @BindView(R.id.tvMajorAttraction)
    TextView tvMajorAttraction;
    @BindView(R.id.tvCartCount)
    TextView tvCartCount;
    @BindView(R.id.lnrStudent)
    LinearLayout lnrStudent;
    @BindView(R.id.lnrChild)
    LinearLayout lnrChild;
    @BindView(R.id.lnrAdult)
    LinearLayout lnrAdult;
    @BindView(R.id.rvImageGallery)
    RecyclerView rvImageGallery;
    @BindView(R.id.rvMajorAttraction)
    RecyclerView rvMajorAttraction;
    @BindView(R.id.ivThemePark)
    ImageView ivThemePark;
    @BindView(R.id.rltCart)
    RelativeLayout rltCart;
    @BindView(R.id.aviProgress)
    AVLoadingIndicatorView aviProgress;
    @BindView(R.id.linearMainLayout)
    NestedScrollView mainLayout;

    ThemeParkDetailsPojo.Details mThemeParkDetails;
    private ThemeParkPojo.Park mPark;

    String mParkID = "";

    public static ThemeParkDetailsFragment newInstance() {
        ThemeParkDetailsFragment fragment = new ThemeParkDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_park_details, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            mParkID = bundle.getString(Constants.BNDL_PARK_ID);
            mPark = new Gson().fromJson(bundle.getString(Constants.BNDL_PARK), ThemeParkPojo.Park.class);
            if (mPark != null) {
                tvTitle.setText("" + mPark.name);
            }

            String mParkCartID = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "");
            if (TextUtils.isEmpty(mParkCartID)) {
//                mParkCartID = getString(R.string.park) + System.currentTimeMillis();
                mParkCartID = ""+ System.currentTimeMillis();
                MyPref.setPref(getActivity(), MyPref.PREF_PARK_CART_ID, "" + mParkCartID);
            }

            getParkDetails();
        }

        tvContinueBooking.setOnClickListener(this);
        rltCart.setOnClickListener(this);
        tvBack.setOnClickListener(this);

    }

    private void getParkDetails() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        String mParkCartID = MyPref.getPref(getActivity(), MyPref.PREF_PARK_CART_ID, "");
        if (TextUtils.isEmpty(mParkCartID)) {
//            mParkCartID = getString(R.string.park) + System.currentTimeMillis();
            mParkCartID = ""+ System.currentTimeMillis();
            MyPref.setPref(getActivity(), MyPref.PREF_PARK_CART_ID, "" + mParkCartID);
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<ThemeParkDetailsPojo> mCall = mApiInterface.getThemeParkDetails(mParkID, mParkCartID, Otapp.mUniqueID);
        mCall.enqueue(new Callback<ThemeParkDetailsPojo>() {
            @Override
            public void onResponse(Call<ThemeParkDetailsPojo> call, Response<ThemeParkDetailsPojo> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    ThemeParkDetailsPojo mThemeParkDetailsPojo = response.body();
                    if (mThemeParkDetailsPojo != null && mThemeParkDetailsPojo.status.equalsIgnoreCase("200")) {

                        if (mThemeParkDetailsPojo.details != null) {

                            mThemeParkDetails = mThemeParkDetailsPojo.details;

                            if (!TextUtils.isEmpty(mThemeParkDetails.image) && getActivity() != null) {

                                aviProgress.setVisibility(View.VISIBLE);
                                Glide.with(getActivity()).load(mThemeParkDetails.image).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        aviProgress.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        aviProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                }).into(ivThemePark);
                            }

                            tvName.setText("" + mThemeParkDetails.tpName + " (" + mThemeParkDetails.tpType + ")");
                            tvTiming.setText("" + mThemeParkDetails.timings);
                            tvAddress.setText("" + mThemeParkDetails.address);
                            if (mThemeParkDetails.adultPrice > 0) {
                                lnrAdult.setVisibility(View.VISIBLE);
                                tvAdult.setText(mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.adultPrice));
                            } else {
                                lnrAdult.setVisibility(View.GONE);
                            }

                            if (mThemeParkDetails.childPrice > 0) {
                                lnrChild.setVisibility(View.VISIBLE);
                                tvChild.setText(mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.childPrice));
                            } else {
                                lnrChild.setVisibility(View.GONE);
                            }

                            if (mThemeParkDetails.studPrice > 0) {
                                lnrStudent.setVisibility(View.VISIBLE);
                                tvStudent.setText(mThemeParkDetails.currrency + " " + Utils.setPrice(mThemeParkDetails.studPrice));
                            } else {
                                lnrStudent.setVisibility(View.GONE);
                            }

                            tvAbout.setText("" + mThemeParkDetails.tpDescription);

                            if (mThemeParkDetails.cartCount > 0) {
                                tvCartCount.setVisibility(View.VISIBLE);
                                tvCartCount.setText("" + mThemeParkDetails.cartCount);
                            } else {
                                tvCartCount.setVisibility(View.GONE);
                            }

                            ParkGalleryAdapter mParkGalleryAdapter = new ParkGalleryAdapter(getActivity(), mThemeParkDetailsPojo.details.imageGallery, mThemeParkDetailsPojo.details.tpName);
                            rvImageGallery.setAdapter(mParkGalleryAdapter);

                            ParkAttractionAdapter mParkAttractionAdapter = new ParkAttractionAdapter(getActivity(), mThemeParkDetailsPojo.details.majorAttractions, mThemeParkDetailsPojo.details.tpName);
                            rvMajorAttraction.setAdapter(mParkAttractionAdapter);

                        }

                    } else {
                        Utils.showToast(getActivity(), "" + mThemeParkDetailsPojo.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<ThemeParkDetailsPojo> call, Throwable t) {
                LogUtils.e("", "onFailure::" + t.getMessage());
                Utils.closeProgressDialog();
            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            popBackStack();
        } else if (view == tvContinueBooking) {
            if (mThemeParkDetails != null) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_PARK_DETAILS, new Gson().toJson(mThemeParkDetails));
                bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                MyPref.setPref(getActivity(), Constants.BNDL_PARK_DETAILS, new Gson().toJson(mThemeParkDetails) );
                MyPref.setPref(getActivity(), Constants.BNDL_PARK, new Gson().toJson(mPark) );
                MyPref.setPref(getActivity(), MyPref.PREF_PARK_CART_COUNT, mThemeParkDetails.cartCount);
                switchFragment(ThemeParkAddCartFragment.newInstance(), ThemeParkAddCartFragment.Tag_ThemeParkAddCartFragment, bundle);
            }
//            Bitmap bitmap = getScreenBitmap(); // Get the bitmap
//            saveTheBitmap(bitmap);
        /*    Bitmap bitmap = getBitmapFromView(mainLayout, mainLayout.getChildAt(0).getHeight(), mainLayout.getChildAt(0).getWidth());
            takeScreenshot(bitmap);*/
//            loadBitmapFromView(mainLayout,mainLayout.getMeasuredWidth(),mainLayout.getMeasuredHeight());
        } else if (view == rltCart) {

            if (mThemeParkDetails != null && mThemeParkDetails.cartCount > 0) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BNDL_PARK_DETAILS, new Gson().toJson(mThemeParkDetails));
                bundle.putString(Constants.BNDL_PARK, new Gson().toJson(mPark));
                bundle.putBoolean(Constants.BNDL_IS_PARK_FROM_RIDE, false);

                MyPref.setPref(getActivity(), Constants.BNDL_PARK_DETAILS, new Gson().toJson(mThemeParkDetails) );
                MyPref.setPref(getActivity(), Constants.BNDL_PARK, new Gson().toJson(mPark) );
                MyPref.setPref(getActivity(), MyPref.PREF_PARK_CART_COUNT, mThemeParkDetails.cartCount);
                switchFragment(ThemeParkMyCartFragment.newInstance(), ThemeParkMyCartFragment.Tag_ThemeParkMyCartFragment, bundle);
            } else {
                Utils.showToast(getActivity(), getString(R.string.alert_add_ticket));
            }

        }
    }

    private void saveTheBitmap(Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory().toString();
        try (FileOutputStream out = new FileOutputStream(path+"hj.jpg")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getScreenBitmap() {

        mainLayout.setDrawingCacheEnabled(true);
        mainLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mainLayout.layout(0, 0, mainLayout.getMeasuredWidth(), mainLayout.getMeasuredHeight());

        mainLayout.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(mainLayout.getDrawingCache());
        mainLayout.setDrawingCacheEnabled(false); // clear drawing cache
        return b;
    }



    //create bitmap from the ScrollView
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


    public  void loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.draw(c);
//        saveTheBitmap(b);
        takeScreenshot(b);
    }
    private void takeScreenshot(Bitmap b) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
        /*    Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());*/
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            b.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
}
