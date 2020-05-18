package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.AddonsMealAdapter;
import com.otapp.net.model.FlightCity;
import com.otapp.net.model.FlightOneDetailsPojo;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.model.FlightReturnListPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightMealFragment extends BaseFragment {

    public static String Tag_FlightMealFragment = "Tag_" + "FlightMealFragment";

    View mView;

    @BindView(R.id.lvAddonsList)
    ListView lvAddonsList;

    AddonsMealAdapter mAddonsMealAdapter;

    FlightOneListPojo.Data mFlightData;
    FlightOneDetailsPojo mFlightOneDetailsPojo;
    FlightCity mFlightCity;

    FlightReturnListPojo.Data mFlightReturnData;
    String mFlightUid = "";

    private int mPosition = -1;

    List<FlightOneDetailsPojo.Meal> mMealsList;

    public static FlightMealFragment newInstance() {
        FlightMealFragment fragment = new FlightMealFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_flight_addons_list, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {



        Bundle bundle = getArguments();
        if (bundle != null) {

            mFlightCity = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT), FlightCity.class);
            mFlightOneDetailsPojo = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT_DETAILS_RESPONSE), FlightOneDetailsPojo.class);
            mPosition = bundle.getInt(Constants.CITY_TYPE_POSITION);

            if (mPosition == 0) {
                mFlightData = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT_ONE_DETAILS), FlightOneListPojo.Data.class);
            } else if (mPosition == 1) {
                mFlightUid = bundle.getString(Constants.BNDL_FLIGHT_UID, "");
                mFlightReturnData = new Gson().fromJson(bundle.getString(Constants.BNDL_FLIGHT_RETURN_DETAILS), FlightReturnListPojo.Data.class);
            }

            if (mFlightOneDetailsPojo != null) {
                mMealsList = mFlightOneDetailsPojo.data.meals;
                mAddonsMealAdapter = new AddonsMealAdapter(getActivity(), mMealsList, mFlightOneDetailsPojo.data.currency);
                lvAddonsList.setAdapter(mAddonsMealAdapter);
            }
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(Tag_FlightMealFragment, "isVisibleToUser::" + isVisibleToUser);
        if (isVisibleToUser) {
        } else {
        }
    }
}
