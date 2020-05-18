package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.otapp.net.R;
import com.otapp.net.adapter.UpcomingMovieAdapter;
import com.otapp.net.model.MovieListPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpcomingMovieTabFragment extends BaseFragment {

    public static String Tag_UpcomingMovieTabFragment = "Tag_" + "UpcomingMovieTabFragment";

    View mView;

    @BindView(R.id.gvMovieList)
    GridView gvMovieList;

    List<MovieListPojo.UpcomingMovie> mUpcomingMovies;

    private UpcomingMovieAdapter mUpcomingMovieAdapter;

    public static UpcomingMovieTabFragment newInstance() {
        UpcomingMovieTabFragment fragment = new UpcomingMovieTabFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        mUpcomingMovieAdapter = new UpcomingMovieAdapter(getActivity(), this);
        gvMovieList.setAdapter(mUpcomingMovieAdapter);

        gvMovieList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    mUpcomingMovieAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount){

            }
        });

    }

    public void onMovieClicked(MovieListPojo.UpcomingMovie mMovie) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.BNDL_MOVIE_ID, mMovie.movieId);
        bundle.putString(Constants.BNDL_MOVIE_STATE, mMovie.bookStat);
        bundle.putString(Constants.BNDL_MOVIE_TYPE, Constants.BNDL_MOVIE_TYPE_UPCOMING);

        switchFragment(MovieDetailsFragment.newInstance(), MovieDetailsFragment.Tag_MovieDetailsFragment, bundle);

    }

    public void setUpcomingMovieList(List<MovieListPojo.UpcomingMovie> mUpcomingMovies) {
        this.mUpcomingMovies = mUpcomingMovies;
        mUpcomingMovieAdapter.addAll(mUpcomingMovies);
    }

    public void setSearchText(String mFilterText){
        if (mUpcomingMovieAdapter != null){
            mUpcomingMovieAdapter.filter(mFilterText);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(Tag_UpcomingMovieTabFragment, "isVisibleToUser::" + isVisibleToUser);
        if (isVisibleToUser) {
            if (mUpcomingMovies == null || mUpcomingMovies.size() == 0) {
                if (isAdded() && getActivity() != null) {
                    Utils.showToast(getActivity(), getString(R.string.alert_no_upcoming_movie));
                }
            }
        } else {

        }
    }
}
