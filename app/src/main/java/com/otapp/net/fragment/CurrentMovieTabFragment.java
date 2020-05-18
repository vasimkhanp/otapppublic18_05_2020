package com.otapp.net.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.otapp.net.R;
import com.otapp.net.adapter.CurrentMovieAdapter;
import com.otapp.net.model.MovieListPojo;
import com.otapp.net.utils.Constants;
import com.otapp.net.utils.LogUtils;
import com.otapp.net.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentMovieTabFragment extends BaseFragment {

    public static String Tag_CurrentMovieTabFragment = "Tag_" + "CurrentMovieTabFragment";

    View mView;

    @BindView(R.id.gvMovieList)
    GridView gvMovieList;

    private CurrentMovieAdapter mCurrentMovieAdapter;
    List<MovieListPojo.CurrentMovie> mCurrentMovies;

    public static CurrentMovieTabFragment newInstance() {
        CurrentMovieTabFragment fragment = new CurrentMovieTabFragment();
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

        mCurrentMovieAdapter = new CurrentMovieAdapter(getActivity(), this);
        gvMovieList.setAdapter(mCurrentMovieAdapter);

        gvMovieList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    mCurrentMovieAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });

    }

    public void onMovieClicked(MovieListPojo.CurrentMovie mMovie) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.BNDL_MOVIE_ID, mMovie.movieId);
        bundle.putString(Constants.BNDL_MOVIE_TYPE, Constants.BNDL_MOVIE_TYPE_CURRENT);
        switchFragment(MovieDetailsFragment.newInstance(), MovieDetailsFragment.Tag_MovieDetailsFragment, bundle);

    }

    public void setCurrentMovieList(List<MovieListPojo.CurrentMovie> mCurrentMovies) {
        this.mCurrentMovies = mCurrentMovies;
        mCurrentMovieAdapter.addAll(mCurrentMovies);
    }

    public void setSearchText(String mFilterText) {
        if (mCurrentMovieAdapter != null) {
            mCurrentMovieAdapter.filter(mFilterText);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e(Tag_CurrentMovieTabFragment, "isVisibleToUser::" + isVisibleToUser);
        if (isVisibleToUser) {
            if (mCurrentMovies == null || mCurrentMovies.size() == 0) {
                if (isAdded() && getActivity() != null) {
                    Utils.showToast(getActivity(), getString(R.string.alert_no_current_movie));
                }
            }
        } else {

        }
    }

}
