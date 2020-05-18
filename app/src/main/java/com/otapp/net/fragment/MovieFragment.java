package com.otapp.net.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.content.res.AppCompatResources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.otapp.net.R;
import com.otapp.net.adapter.MovieFeaturedAdapter;
import com.otapp.net.adapter.ServiceCategoryPagerAdapterWithTitle;
import com.otapp.net.application.Otapp;
import com.otapp.net.model.MovieListPojo;
import com.otapp.net.rest.ApiInterface;
import com.otapp.net.rest.RestClient;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;
import com.otapp.net.view.AutoScrollViewPager;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends BaseFragment {

    public static String Tag_MovieFragment = "Tag_" + "MovieFragment";

    View mView;

    //    @BindView(R.id.ivFeaturedMovie)
//    ImageView ivFeaturedMovie;
//    @BindView(R.id.aviProgress)
//    AVLoadingIndicatorView aviProgress;
    @BindView(R.id.vpFeatured)
//    ViewPager vpFeatured;
            AutoScrollViewPager vpFeatured;
    //    @BindView(R.id.tlDots)
//    TabLayout tlDots;
    @BindView(R.id.etMovieSearch)
    EditText etMovieSearch;
    @BindView(R.id.tlSlidingTabs)
    TabLayout tlSlidingTabs;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private int mServicePosition = 0;

    ServiceCategoryPagerAdapterWithTitle mServiceCategoryPagerAdapterWithTitle;

    MovieListPojo.Data mMovieListPojoData;

    public static MovieFragment newInstance() {
        MovieFragment fragment = new MovieFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_service_movie, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        List<String> mMovieTabList = Arrays.asList(getResources().getStringArray(R.array.movie_array));
        if (!isAdded()) return;
        mServiceCategoryPagerAdapterWithTitle = new ServiceCategoryPagerAdapterWithTitle(getChildFragmentManager());

//        for (int i = 0; i < mMovieTabList.size(); i++) {
//        Fragment mFragment = CurrentMovieTabFragment.newInstance();
//            Bundle bundle = new Bundle();
//            bundle.putString(Constants.BNDL_MOVIE_TAB, mMovieTabList.get(i));
        mServiceCategoryPagerAdapterWithTitle.addFragment(CurrentMovieTabFragment.newInstance(), mMovieTabList.get(0));
        mServiceCategoryPagerAdapterWithTitle.addFragment(UpcomingMovieTabFragment.newInstance(), mMovieTabList.get(1));
//        }


        mViewPager.setAdapter(mServiceCategoryPagerAdapterWithTitle);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(mServicePosition);
        tlSlidingTabs.setupWithViewPager(mViewPager, true);

        setTabBG(R.drawable.tab_left_select, R.drawable.tab_right_unselect);

        tlSlidingTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tlSlidingTabs.getSelectedTabPosition() == 0) {
                    setTabBG(R.drawable.tab_left_select, R.drawable.tab_right_unselect);
                } else {
                    setTabBG(R.drawable.tab_left_unselect, R.drawable.tab_right_select);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mServicePosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        etMovieSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text = etMovieSearch.getText().toString().toLowerCase(Locale.getDefault());
                Log.d(Tag_MovieFragment,"Text To Search : "+text);

                if (mServiceCategoryPagerAdapterWithTitle != null && mServiceCategoryPagerAdapterWithTitle.getCount() > 0) {
                    CurrentMovieTabFragment mMovieTabFragment = (CurrentMovieTabFragment) mServiceCategoryPagerAdapterWithTitle.getItem(0);
                    if (mMovieTabFragment != null) {
                        mMovieTabFragment.setSearchText(text);
                    }
                }

                if (mServiceCategoryPagerAdapterWithTitle != null && mServiceCategoryPagerAdapterWithTitle.getCount() > 1) {
                    UpcomingMovieTabFragment mMovieTabFragment = (UpcomingMovieTabFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
                    if (mMovieTabFragment != null) {
                        mMovieTabFragment.setSearchText(text);
                    }
                }

            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(Tag_MovieFragment, "isVisibleToUser::" + isVisibleToUser);

        if (isVisibleToUser) {
            if (mMovieListPojoData == null) {
                LogUtils.e("", "mMovieListPojoData is null");
//                boolean isGotMovieList = false;
//                if (getActivity() != null) {
//                    String mMovieList = MyPref.getPref(getActivity(), MyPref.PREF_MOVIE_RESPONSE, "");
//                    LogUtils.e("", "mMovieList::"+mMovieList);
//                    if (!TextUtils.isEmpty(mMovieList)) {
//                        mMovieListPojoData = new Gson().fromJson(mMovieList, MovieListPojo.class);
//                        if (mMovieListPojoData != null) {
//                            isGotMovieList = true;
//                            LogUtils.e("", "called from cache");
//                            setMovieList();
//                        }
//                    }
//                }

                getMovieList();
            } else {
                LogUtils.e("", "mMovieListPojoData is not null");
                setMovieList();
            }
        } else {
        }
    }

    private void getMovieList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.msg_no_internet));
            return;
        }

        Utils.showProgressDialog(getActivity());

        ApiInterface mApiInterface = RestClient.getClient(true);
        Call<MovieListPojo> mCall = mApiInterface.getMovieList(Otapp.mUniqueID);
        mCall.enqueue(new Callback<MovieListPojo>() {
            @Override
            public void onResponse(Call<MovieListPojo> call, Response<MovieListPojo> response) {

                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    MovieListPojo mMovieListPojo = response.body();
                    if (mMovieListPojo != null) {
                        if (mMovieListPojo.status.equalsIgnoreCase("200")) {
                            mMovieListPojoData = mMovieListPojo.data;
//                            MyPref.setPref(getActivity(), MyPref.PREF_MOVIE_RESPONSE, new Gson().toJson(mMovieListPojoData));
                            setMovieList();

                        } else {
                            Utils.showToast(getActivity(), "" + mMovieListPojo.message);
                            mMovieListPojoData = null;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieListPojo> call, Throwable t) {
                Utils.closeProgressDialog();
                LogUtils.e("", "onFailure:" + t.getMessage());
            }
        });

    }

    private void setMovieList() {

        if (getActivity() != null && isAdded()) {

            if (mMovieListPojoData != null) {

//                MovieListPojo.TrendingMovies mTrendingMovies = mMovieListPojoData.data.trendingMovies;
//                if (mTrendingMovies != null) {
//                    ivFeaturedMovie.setVisibility(View.VISIBLE);
//                    aviProgress.setVisibility(View.VISIBLE);
//                    Picasso.get().load(mTrendingMovies.image).into(ivFeaturedMovie, new com.squareup.picasso.Callback() {
//                        @Override
//                        public void onSuccess() {
//                            aviProgress.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            aviProgress.setVisibility(View.GONE);
//                        }
//                    });
//                } else {
//                    ivFeaturedMovie.setVisibility(View.GONE);
//                    aviProgress.setVisibility(View.GONE);
//                }
                List<String> mBannerImages = mMovieListPojoData.bannerImages;
                if (mBannerImages != null && mBannerImages.size() > 0) {
                    MovieFeaturedAdapter mMovieFeaturedAdapter = new MovieFeaturedAdapter(getActivity(), mBannerImages);
                    vpFeatured.setAdapter(mMovieFeaturedAdapter);
                    vpFeatured.setCurrentItem(0);
                    vpFeatured.startAutoScroll();
//                    tlDots.setupWithViewPager(vpFeatured, true);
                }

                List<MovieListPojo.CurrentMovie> mCurrentMovies = mMovieListPojoData.currentMovies;
                if (mCurrentMovies != null && mCurrentMovies.size() > 0) {
                    CurrentMovieTabFragment mMovieTabFragment = (CurrentMovieTabFragment) mServiceCategoryPagerAdapterWithTitle.getItem(0);
                    mMovieTabFragment.setCurrentMovieList(mCurrentMovies);
                }

                List<MovieListPojo.UpcomingMovie> mUpcomingMovies = mMovieListPojoData.upcomingMovies;
                if (mUpcomingMovies != null && mUpcomingMovies.size() > 0) {
                    UpcomingMovieTabFragment mMovieTabFragment = (UpcomingMovieTabFragment) mServiceCategoryPagerAdapterWithTitle.getItem(1);
                    mMovieTabFragment.setUpcomingMovieList(mUpcomingMovies);
                }
            }

        }

    }

    private void setTabBG(int tab1, int tab2) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ViewGroup tabStrip = (ViewGroup) tlSlidingTabs.getChildAt(0);
            View tabView1 = tabStrip.getChildAt(0);
            View tabView2 = tabStrip.getChildAt(1);
            if (tabView1 != null) {
                int paddingStart = tabView1.getPaddingStart();
                int paddingTop = tabView1.getPaddingTop();
                int paddingEnd = tabView1.getPaddingEnd();
                int paddingBottom = tabView1.getPaddingBottom();
                ViewCompat.setBackground(tabView1, AppCompatResources.getDrawable(tabView1.getContext(), tab1));
                ViewCompat.setPaddingRelative(tabView1, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }

            if (tabView2 != null) {
                int paddingStart = tabView2.getPaddingStart();
                int paddingTop = tabView2.getPaddingTop();
                int paddingEnd = tabView2.getPaddingEnd();
                int paddingBottom = tabView2.getPaddingBottom();
                ViewCompat.setBackground(tabView2, AppCompatResources.getDrawable(tabView2.getContext(), tab2));
                ViewCompat.setPaddingRelative(tabView2, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }
        }
    }
}
