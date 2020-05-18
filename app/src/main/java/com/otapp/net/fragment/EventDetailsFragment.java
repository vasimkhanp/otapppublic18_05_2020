package com.otapp.net.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otapp.net.R;
import com.otapp.net.adapter.EventTicketTypeAdapter;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.EventDetailsPojo;
import com.otapp.net.model.EventListPojo;
import com.otapp.net.model.EventSeatBookedPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.DateFormate;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsFragment extends BaseFragment implements View.OnClickListener {

    public static String Tag_EventDetailsFragment = "Tag_" + "EventDetailsFragment";

    View mView;

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvDetails)
    TextView tvDetails;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvBook)
    TextView tvBook;
    @BindView(R.id.ivEvent)
    ImageView ivEvent;
    @BindView(R.id.aviProgress)
    AVLoadingIndicatorView aviProgress;

    //    private String mSelectedDate = "";
    private String mUserKey = "";
    private float total = 0;

    Animation animClose, animOpen;

    //    private EventListPojo.Events mEvents;
    private EventDetailsPojo.EventDetails mEventDetails;

    public static EventDetailsFragment newInstance() {
        EventDetailsFragment fragment = new EventDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        animOpen = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_open);
        animClose = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_close);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String mEvent = bundle.getString(Constants.BNDL_EVENT_DETAILS);
            if (!TextUtils.isEmpty(mEvent)) {
                EventListPojo.Events mEvents = new Gson().fromJson(mEvent, EventListPojo.Events.class);
                if (mEvents != null) {
                    tvTitle.setText("" + mEvents.eventName);
                    tvName.setText("" + mEvents.eventName);
                    StringBuilder strBuilderDetails = new StringBuilder();
                    strBuilderDetails.append(mEvents.eventCategory);

                    if (!TextUtils.isEmpty(mEvents.eventLanguage)) {
                        strBuilderDetails.append(" | " + mEvents.eventLanguage);
                    }

                    tvDetails.setText("" + strBuilderDetails.toString());

                    StringBuilder strBuilderPrice = new StringBuilder();
                    if (mEvents.eventDate != null && mEvents.eventDate.size() > 0) {
                        strBuilderPrice.append(mEvents.eventDate.get(0));
                    }


                    if (!TextUtils.isEmpty(mEvents.eventAddress)) {
                        if (strBuilderPrice.length() > 0) {
                            strBuilderPrice.append(" | " + mEvents.eventAddress);
                        } else {
                            strBuilderPrice.append(mEvents.eventAddress);
                        }
                    }

                    if (mEvents.eventPrice > 0) {
                        strBuilderDetails.append(" | " + mEvents.ticketCurrency + " " + Utils.setPrice(mEvents.eventPrice) + " " + getString(R.string.onwards));
                    }

                    tvPrice.setText("" + strBuilderDetails.toString());

//                    if (!TextUtils.isEmpty(mEvents.eventPhoto)) {
//                        aviProgress.setVisibility(View.VISIBLE);
//                        Picasso.get().load(mEvents.eventPhoto).into(ivEvent, new com.squareup.picasso.Callback() {
//                            @Override
//                            public void onSuccess() {
//                                aviProgress.setVisibility(View.GONE);
//                            }
//
//                            @Override
//                            public void onError(Exception e) {
//                                aviProgress.setVisibility(View.GONE);
//                            }
//                        });
//                    } else {
//                        aviProgress.setVisibility(View.GONE);
//                    }

                    setEventDetails(mEvents.eventId);
                }

            }
        }

        tvBack.setOnClickListener(this);
        tvBook.setOnClickListener(this);
    }

    private void setEventDetails(String eventId) {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());



        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<EventDetailsPojo> mCall = mApiInterface.getEventDetails(eventId, Otapp.mUniqueID);
        mCall.enqueue(new Callback<EventDetailsPojo>() {
            @Override
            public void onResponse(Call<EventDetailsPojo> call, Response<EventDetailsPojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    EventDetailsPojo mEventDetailsPojo = response.body();
                    if (mEventDetailsPojo != null) {
                        if (mEventDetailsPojo.status.equalsIgnoreCase("200")) {

                            mEventDetails = mEventDetailsPojo.data;
                            mUserKey = mEventDetailsPojo.uKey;

                            if (mEventDetails != null) {


                                tvTitle.setText("" + mEventDetails.eventName);
                                tvName.setText("" + mEventDetails.eventName);

                                StringBuilder strBuilderDetails = new StringBuilder();
                                strBuilderDetails.append(mEventDetails.eventCategory);

                                if (!TextUtils.isEmpty(mEventDetails.eventLanguage)) {
                                    strBuilderDetails.append(" | " + mEventDetails.eventLanguage);
                                }

                                if (!TextUtils.isEmpty(mEventDetails.eventRemainingTime)) {
                                    strBuilderDetails.append(" | " + mEventDetails.eventRemainingTime);
                                }

                                tvDetails.setText("" + strBuilderDetails.toString());

                                StringBuilder strBuilderPrice = new StringBuilder();
                                LogUtils.e("", "mEventDetails.eventDate::" + mEventDetails.eventDate);
                                if (mEventDetails.eventDate != null && mEventDetails.eventDate.size() > 0) {
                                    strBuilderPrice.append(mEventDetails.eventDate.get(0));
                                }


                                if (!TextUtils.isEmpty(mEventDetails.eventAddr)) {
                                    if (strBuilderPrice.length() > 0) {
                                        strBuilderPrice.append(" | " + mEventDetails.eventAddr);
                                    } else {
                                        strBuilderPrice.append(mEventDetails.eventAddr);
                                    }
                                }

                                String mOnwards = "onwards";
                                try {
                                    if (getActivity() != null) {
                                        mOnwards = getString(R.string.onwards);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (mEventDetails.eventPrice > 0) {
                                    strBuilderPrice.append(" | " + mEventDetails.eventCurrency + " " + Utils.setPrice(mEventDetails.eventPrice) + " " + mOnwards);
                                }

                                tvPrice.setText("" + strBuilderPrice.toString());

                                if (!TextUtils.isEmpty(mEventDetails.eventPhoto)) {
                                    Picasso.get().load(mEventDetails.eventPhoto).into(ivEvent);
                                }

                            }
                        } else {
                            Utils.showToast(getContext(), "" + mEventDetailsPojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EventDetailsPojo> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });


    }

    @Override
    public void onClick(View view) {

        if (view == tvBack) {
            popBackStack();
        } else if (view == tvBook) {
            if (mEventDetails != null && mEventDetails.eventDate != null && mEventDetails.eventDate.size() > 0) {
                mEventDetails.eventSelectedDate = mEventDetails.eventDate.get(0);
                LogUtils.e("", "mEventDetails.eventSelectedDate::" + mEventDetails.eventSelectedDate);
//                showDateDialog();
                showCategoryDialog();
            }
        }
    }

//    private void showDateDialog() {
//
//        final Dialog termsDialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
//        termsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        termsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        termsDialog.setCanceledOnTouchOutside(false);
//        termsDialog.setContentView(R.layout.dialog_event_date_time);
//
//        final RelativeLayout rlMain = (RelativeLayout) termsDialog.findViewById(R.id.rlMain);
//        final LinearLayout lnrContainer = (LinearLayout) termsDialog.findViewById(R.id.lnrContainer);
//        final RecyclerView rvDate = (RecyclerView) termsDialog.findViewById(R.id.rvDate);
////        final RecyclerView rvTime = (RecyclerView) termsDialog.findViewById(R.id.rvTime);
//        final TextView tvProceed = (TextView) termsDialog.findViewById(R.id.tvProceed);
//        final TextView tvBackArrow = (TextView) termsDialog.findViewById(R.id.tvBackArrow);
//
//        rvDate.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
////        rvTime.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//
//        ArrayList<EventDatePojo> mEventDatePojoList = new ArrayList<>();
//        for (int i = 0; i < mEventDetails.eventDate.size(); i++) {
//            EventDatePojo mEventDatePojo = new EventDatePojo();
//            mEventDatePojo.setmDate(mEventDetails.eventDate.get(i));
//            if (!TextUtils.isEmpty(mEventDetails.eventSelectedDate) && mEventDetails.eventSelectedDate.equalsIgnoreCase(mEventDetails.eventDate.get(i))) {
//                mEventDatePojo.setSelected(true);
//            } else {
//                mEventDatePojo.setSelected(false);
//            }
//            mEventDatePojoList.add(mEventDatePojo);
//
//        }
//
//        EventDateAdapter mEventDateAdapter = new EventDateAdapter(getActivity(), mEventDatePojoList, new EventDateAdapter.OnEventDateClickListener() {
//            @Override
//            public void onEventDateClicked(String date) {
//                mEventDetails.eventSelectedDate = date;
//                tvProceed.performClick();
//            }
//        });
//        rvDate.setAdapter(mEventDateAdapter);
//
////        ArrayList<EventDatePojo> mEventTimePojoList = new ArrayList<>();
////        for (int i = 0; i < mEventDetails.eventTime.size(); i++) {
////            EventDatePojo mEventTimePojo = new EventDatePojo();
////            mEventTimePojo.setmDate(mEventDetails.eventTime.get(i));
////            if (!TextUtils.isEmpty(mEventDetails.eventSelectedTime) && mEventDetails.eventSelectedTime.equalsIgnoreCase(mEventDetails.eventTime.get(i))) {
////                mEventTimePojo.setSelected(true);
////            } else {
////                mEventTimePojo.setSelected(false);
////            }
////            mEventTimePojoList.add(mEventTimePojo);
////
////        }
////
////        EventDateAdapter mEventTimeAdapter = new EventDateAdapter(getActivity(), mEventTimePojoList, new EventDateAdapter.OnEventDateClickListener() {
////            @Override
////            public void onEventDateClicked(String time) {
////                mEventDetails.eventSelectedTime = time;
////            }
////        });
////
////        rvTime.setAdapter(mEventTimeAdapter);
//
//        rlMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        lnrContainer.startAnimation(animClose);
//                    }
//                }, 10);
//
//            }
//        });
//
//
//        tvBackArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                rlMain.performClick();
//            }
//        });
//
//        tvProceed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (TextUtils.isEmpty(mEventDetails.eventSelectedDate)) {
//                    Utils.showToast(getActivity(), getString(R.string.alert_select_event_date));
//                    return;
//                }
//
////                if (TextUtils.isEmpty(mEventDetails.eventSelectedTime)) {
////                    Utils.showToast(getActivity(), getString(R.string.alert_select_event_time));
////                    return;
////                }
//
//
//                if (termsDialog != null && termsDialog.isShowing()) {
//                    termsDialog.dismiss();
//                    termsDialog.cancel();
//                }
//                showCategoryDialog();
////                rlMain.performClick();
//
////                new Handler().postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        showCategoryDialog();
////                    }
////                }, 100);
//
//            }
//        });
//
//        animClose.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (termsDialog != null && termsDialog.isShowing()) {
//                            termsDialog.dismiss();
//                            termsDialog.cancel();
//                        }
//                    }
//                }, 10);
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        lnrContainer.startAnimation(animOpen);
//        termsDialog.show();
//
//    }

//    String mSelectedFloorId = "";

    int mTotalSeat;

    private void showCategoryDialog() {

        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_event_category);

        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
        final ListView lvCategory = (ListView) dialog.findViewById(R.id.lvCategory);
        final TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        final TextView tvTotal = (TextView) dialog.findViewById(R.id.tvTotal);
        final TextView tvProceed = (TextView) dialog.findViewById(R.id.tvProceed);
        final TextView tvBackArrow = (TextView) dialog.findViewById(R.id.tvBackArrow);
        final TextView tvTotalAmount = (TextView) dialog.findViewById(R.id.tvTotalAmount);
//        final Spinner spinFloor = (Spinner) termsDialog.findViewById(R.id.spinFloor);

        mTotalSeat = 0;
        setTotal(tvTotalAmount);

        final List<EventDetailsPojo.TicketType> mTicketTypeList = new ArrayList<>();
        mTicketTypeList.addAll(mEventDetails.ticketType);

        final EventTicketTypeAdapter mEventTicketTypeAdapter = new EventTicketTypeAdapter(getActivity());
        lvCategory.setAdapter(mEventTicketTypeAdapter);
        mEventTicketTypeAdapter.addAll(mTicketTypeList);

        LogUtils.e("", "mEventDetails.eventSelectedTime::" + mEventDetails.eventSelectedDate);
        if (!TextUtils.isEmpty(mEventDetails.eventSelectedDate)) {
            try {
                Date mDateTime = DateFormate.sdfServerDateTime.parse(mEventDetails.eventSelectedDate);
                String mEventDateTime = DateFormate.sdfEventDateTime.format(mDateTime);
                LogUtils.e("", "mEventDateTime" + mEventDateTime);
                if (!TextUtils.isEmpty(mEventDateTime)) {
                    tvTitle.setText(mEventDateTime);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        mSelectedFloorId = "";

//        if (mEventDetails.floors != null && mEventDetails.floors.size() > 0) {
//            spinFloor.setVisibility(View.VISIBLE);
//
//            List<String> mFloorList = new ArrayList<>();
//            for (int i = 0; i < mEventDetails.floors.size(); i++) {
//                mFloorList.add(mEventDetails.floors.get(i).floorName);
//            }
//            mFloorList.add(0, getString(R.string.select_floor));
//
//            ArrayAdapter mFloorAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mFloorList);
//            mFloorAdapter.setDropDownViewResource(R.layout.spin_drop_down_item);
//            spinFloor.setAdapter(mFloorAdapter);
//            spinFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                    String mTime = adapterView.getItemAtPosition(i).toString();
//                    LogUtils.e("", "mTime::" + mTime);
//                    if (!TextUtils.isEmpty(mTime) && !mTime.equalsIgnoreCase(getString(R.string.select_floor))) {
//                        mSelectedFloorId = mEventDetails.floors.get(i - 1).floorId;
//                        LogUtils.e("", "mSelectedFloorId::" + mSelectedFloorId);
//                        if (mTicketTypeList != null && mTicketTypeList.size() > 0) {
//                            mTicketTypeList.clear();
//                        }
//                        for (int j = 0; j < mEventDetails.ticketType.size(); j++) {
//                            LogUtils.e("", j + " Checked:" + mEventDetails.ticketType.get(j).floorId.equalsIgnoreCase(mSelectedFloorId));
//                            if (mEventDetails.ticketType.get(j).floorId.equalsIgnoreCase(mSelectedFloorId)) {
//                                mTicketTypeList.add(mEventDetails.ticketType.get(j));
//                            }
////                            mEventDetails.ticketType.get(j).ticketCount = 0;
////                            mEventDetails.ticketType.get(j).isSelected = false;
//                        }
//                        LogUtils.e("", "mTicketTypeList::" + mTicketTypeList.size() + " " + mTicketTypeList);
//                        mEventTicketTypeAdapter.addAll(mTicketTypeList);
//                        mEventTicketTypeAdapter.notifyDataSetChanged();
//                        if (mTicketTypeList != null && mTicketTypeList.size() > 0) {
//                            lvCategory.setVisibility(View.VISIBLE);
//                        } else {
//                            lvCategory.setVisibility(View.GONE);
//                        }
//                    } else {
//                        mSelectedFloorId = "";
//                        lvCategory.setVisibility(View.GONE);
//                    }
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//                }
//            });
//
//        } else {
//            spinFloor.setVisibility(View.GONE);
//        }


        mEventTicketTypeAdapter.setListener(new EventTicketTypeAdapter.OnEventTicketTypeListener() {
            @Override
            public void onPlusClicked(int position) {

                EventDetailsPojo.TicketType mTicketType = mEventDetails.ticketType.get(position);
                if (mTicketType.ticketCount < mTicketType.ticketAvailable) {
                    mTicketType.ticketCount++;
                    mTicketType.isSelected = true;
                    mTotalSeat++;
                    setTotal(tvTotalAmount);
                    mEventTicketTypeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onMinusClicked(int position) {

                EventDetailsPojo.TicketType mTicketType = mEventDetails.ticketType.get(position);

                if (mTicketType.ticketCount > 1) {
                    mTicketType.ticketCount--;
                    mTicketType.isSelected = true;
                    mTotalSeat--;
                } else {
                    mTicketType.ticketCount = 0;
                    mTicketType.isSelected = false;
                }
                setTotal(tvTotalAmount);
                mEventTicketTypeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onAddClicked(int position) {
                EventDetailsPojo.TicketType mTicketType = mEventDetails.ticketType.get(position);
                mTicketType.isSelected = true;
                if (mTicketType.ticketCount < 1) {
                    mTicketType.ticketCount = 1;
                    mTotalSeat++;
                }
                setTotal(tvTotalAmount);
                mEventTicketTypeAdapter.notifyDataSetChanged();
            }
        });

//        mEventTicketTypeAdapter.addAll(mEventDetails.ticketType);

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lnrContainer.startAnimation(animClose);
                    }
                }, 10);

            }
        });

        tvBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }
//                showDateDialog();

            }
        });

        tvProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (mEventDetails.floors != null && mEventDetails.floors.size() > 0 && TextUtils.isEmpty(mSelectedFloorId)) {
//                    Utils.showToast(getActivity(), getString(R.string.alert_select_floor));
//                    return;
//                }


//                if (mTotalSeat > 9) {
//                    Utils.showToast(getActivity(), getString(R.string.alert_event_seat_validation));
//                    return;
//                }

                if (total <= 0) {
                    Utils.showToast(getActivity(), getString(R.string.alert_select_ticket_type));
                    return;
                }

                bookEventSeat();

//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.BNDL_EVENT_DETAILS, new Gson().toJson(mEventDetails));
//                bundle.putString(Constants.BNDL_USER_KEY, mUserKey);
//                if (!TextUtils.isEmpty(mSelectedFloorId)) {
//                    bundle.putString(Constants.BNDL_EVENT_FLOOR_ID, mSelectedFloorId);
//
//                }
//
//                switchFragment(EventOrderPreviewFragment.newInstance(), EventOrderPreviewFragment.Tag_EventOrderPreviewFragment, bundle);
                rlMain.performClick();

            }
        });

        animClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }
                }, 10);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        lnrContainer.startAnimation(animOpen);
        dialog.show();

    }

    private void setTotal(TextView tvTotal) {

        total = 0;
        String mCurrency = "";
        if (mEventDetails.ticketType != null && mEventDetails.ticketType.size() > 0) {
            for (int i = 0; i < mEventDetails.ticketType.size(); i++) {
                EventDetailsPojo.TicketType mTicketType = mEventDetails.ticketType.get(i);
                if (mTicketType != null && mTicketType.ticketCount > 0 && !TextUtils.isEmpty(mTicketType.ticketAmount)) {
                    mCurrency = mTicketType.ticketCurrency;
                    total = total + (mTicketType.ticketCount * Float.parseFloat(mTicketType.ticketAmount));
                }
            }
        }
        if (total > 0) {
            tvTotal.setText(mCurrency + " " + Utils.setPrice(total));
        } else {
            tvTotal.setText("");
        }

    }

    int mSeatCount = 0;

    private void bookEventSeat() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        String mTickets = "";
        mSeatCount = 0;
        if (mEventDetails.ticketType != null && mEventDetails.ticketType.size() > 0) {
            JSONArray mJsonArray = new JSONArray();
            try {
                for (int i = 0; i < mEventDetails.ticketType.size(); i++) {
                    if (mEventDetails.ticketType.get(i).isSelected) {
                        JSONObject mJsonObject = new JSONObject();
                        mJsonObject.put("ticket_id", mEventDetails.ticketType.get(i).ticketId);
                        mJsonObject.put("floor_id", mEventDetails.ticketType.get(i).floorId);
                        mJsonObject.put("no_of_ticket", mEventDetails.ticketType.get(i).ticketCount);
                        mJsonArray.put(mJsonObject);
                        mSeatCount = mSeatCount + mEventDetails.ticketType.get(i).ticketCount;
//                        mSeatCount++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mTickets = mJsonArray.toString();
            LogUtils.e("", "mTickets::" + mTickets);
        }

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<EventSeatBookedPojo> mCall = mApiInterface.bookEventSeat(mEventDetails.eventId, mUserKey,
                mEventDetails.eventSelectedDate.split(" ")[0], mEventDetails.eventSelectedDate.split(" ")[1], mTickets, Otapp.mUniqueID);
        mCall.enqueue(new Callback<EventSeatBookedPojo>() {
            @Override
            public void onResponse(Call<EventSeatBookedPojo> call, Response<EventSeatBookedPojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    EventSeatBookedPojo mEventSeatBookedPojo = response.body();
                    if (mEventSeatBookedPojo != null) {
                        if (mEventSeatBookedPojo.status.equalsIgnoreCase("200")) {

                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.BNDL_EVENT_DETAILS, new Gson().toJson(mEventDetails));
                            bundle.putString(Constants.BNDL_USER_KEY, mUserKey);
                            bundle.putString(Constants.BNDL_EVENT_SEAT_COUNT, "" + mSeatCount);
//                            if (!TextUtils.isEmpty(mSelectedFloorId)) {
//                                bundle.putString(Constants.BNDL_EVENT_FLOOR_ID, mSelectedFloorId);
//
//                            }

                            switchFragment(EventOrderPreviewFragment.newInstance(), EventOrderPreviewFragment.Tag_EventOrderPreviewFragment, bundle);

                        } else {
                            Utils.showToast(getContext(), "" + mEventSeatBookedPojo.message);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EventSeatBookedPojo> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });


    }


}
