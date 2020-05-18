package com.otapp.net.fragment;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otapp.net.HomeActivity;
import com.otapp.net.R;
import com.otapp.net.adapter.EventAdapter;
import com.otapp.net.model.EventListPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventListFragment extends BaseFragment {


    View mView;

    @BindView(R.id.gvEventList)
    GridView gvEventList;
    @BindView(R.id.lnrNoEvent)
    LinearLayout lnrNoEvent;
    @BindView(R.id.tvEvenBetter)
    TextView tvEvenBetter;

    EventAdapter mEventAdapter;


    private String mCategoryID = "";

    List<EventListPojo.Events> mEventList;

    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();
        setLinkableText();

        return mView;
    }

    private void InitializeControls() {

        mEventAdapter = new EventAdapter(getActivity(), this);
        gvEventList.setAdapter(mEventAdapter);



        Bundle bundle = getArguments();
        if (bundle != null) {
            mCategoryID = bundle.getString(Constants.BNDL_CATEGORY_ID);

            String mEvents = bundle.getString(Constants.BNDL_EVENT_LIST);

            if (!TextUtils.isEmpty(mEvents)) {
                mEventList = new Gson().fromJson(mEvents, new TypeToken<List<EventListPojo.Events>>() {
                }.getType());
                LogUtils.e("", "mEventList::" + mEventList.size());
            }
        }

        if (mEventList != null && mEventList.size() > 0) {
            lnrNoEvent.setVisibility(View.GONE);
            mEventAdapter.addAll(mEventList);
        } else {
            lnrNoEvent.setVisibility(View.VISIBLE);
        }

    }

    private void setLinkableText() {

        Spannable wordtoSpan = new SpannableString("" + tvEvenBetter.getText().toString());
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tab_selected)), 29, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tab_selected)), 42, 48, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new UnderlineSpan(), 29, 38, 0);
        wordtoSpan.setSpan(new UnderlineSpan(), 42, 48, 0);
        ClickableSpan clickableSpanThempark = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (getActivity() instanceof HomeActivity){
                    popBackStack(ServiceFragment.Tag_ServiceFragment);
                    HomeActivity activity = (HomeActivity) getActivity();
                    activity.loadThemepark();
                }
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpanMovie = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (getActivity() instanceof HomeActivity){
                    popBackStack(ServiceFragment.Tag_ServiceFragment);
                    HomeActivity activity = (HomeActivity) getActivity();
                    activity.loadMovie();
                }
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
            }
        };
        wordtoSpan.setSpan(clickableSpanThempark, 29, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(clickableSpanMovie, 42, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvEvenBetter.setText(wordtoSpan);
        tvEvenBetter.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void onEventClicked(EventListPojo.Events mEvents) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.BNDL_EVENT_DETAILS, new Gson().toJson(mEvents));

        switchFragment(EventDetailsFragment.newInstance(), EventDetailsFragment.Tag_EventDetailsFragment, bundle);

    }
}
