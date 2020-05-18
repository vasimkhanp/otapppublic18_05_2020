package com.otapp.net.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.FlightReviewOrderAdapter;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightOneDetailsPojo;
import com.otapp.net.model.FlightSuccessPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightReviewOrderFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_FlightReviewOrderFragment = "Tag_" + "FlightReviewOrderFragment";

    View mView;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvPnr)
    TextView tvPnr;
    @BindView(R.id.tvBookingID)
    TextView tvBookingID;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.lvOrder)
    ListView lvOrder;

    FlightReviewOrderAdapter mFlightReviewOrderAdapter;

    FlightOneDetailsPojo mFlightOneDetailsPojo;
    FlightCity mFlightCity;

    FlightSuccessPojo.Data mFlightSuccessPojo;

    public static FlightReviewOrderFragment newInstance() {
        FlightReviewOrderFragment fragment = new FlightReviewOrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_flight_review_order, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();
        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    switchFragment(HomeFragment.newInstance(), HomeFragment.Tag_HomeFragment, true);
                }
                return true;
            }
        });
        return mView;
    }

    private void InitializeControls() {

        mFlightReviewOrderAdapter = new FlightReviewOrderAdapter(getActivity());
        lvOrder.setAdapter(mFlightReviewOrderAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mFlightCity = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT), FlightCity.class);
            mFlightOneDetailsPojo = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT_DETAILS_RESPONSE), FlightOneDetailsPojo.class);
            String mResponse = bundle.getString(Constants.BNDL_FLIGHT_RESPONSE);
            Log.d("Log","Response msg"+mResponse);
            if (!TextUtils.isEmpty(mResponse)) {
                mFlightSuccessPojo = new Gson().fromJson(mResponse, FlightSuccessPojo.Data.class);
                if (mFlightSuccessPojo != null) {
                    tvPnr.setText(mFlightSuccessPojo.pnr);
                    if (!TextUtils.isEmpty(mFlightSuccessPojo.ticketNumber)) {
                        tvBookingID.setText(mFlightSuccessPojo.ticketNumber);
                        Bitmap mQRCode = Utils.getQRCode(mFlightSuccessPojo.ticketNumber);
                        if (mQRCode != null) {
                            ivQRCode.setImageBitmap(mQRCode);
                        }
                    }
                }
            }

            if (mFlightCity != null){
                mFlightReviewOrderAdapter.setClass(mFlightCity.clasName);
            }

            if (mFlightOneDetailsPojo != null){
                tvName.setText(getString(R.string.name)+": "+mFlightOneDetailsPojo.data.userName);
                mFlightReviewOrderAdapter.addAll(mFlightOneDetailsPojo.data.flightDetails);
            }
        }

        tvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {



        if (view == tvBack) {
           // popBackStack(ServiceFragment.Tag_ServiceFragment);
            switchFragment(HomeFragment.newInstance(), HomeFragment.Tag_HomeFragment, true);
        }
    }
}
