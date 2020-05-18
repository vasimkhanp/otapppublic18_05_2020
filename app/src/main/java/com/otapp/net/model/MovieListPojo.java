package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieListPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Data data;


    public class Data {

        @SerializedName("current_movies")
        @Expose
        public List<CurrentMovie> currentMovies = null;
        @SerializedName("upcoming_movies")
        @Expose
        public List<UpcomingMovie> upcomingMovies = null;
        @SerializedName("trending_movies")
        @Expose
        public TrendingMovies trendingMovies;
        @SerializedName("banner_images")
        @Expose
        public List<String> bannerImages;

    }

    public class CurrentMovie {

        @SerializedName("movie_id")
        @Expose
        public String movieId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("language")
        @Expose
        public String language;
        @SerializedName("movie_genre")
        @Expose
        public String movieGenre;
        @SerializedName("movie_restriction")
        @Expose
        public String movieRestriction;
        @SerializedName("movie_duration")
        @Expose
        public String movieDuration;
        @SerializedName("movie_format")
        @Expose
        public String movieFormat;
        @SerializedName("movie_cinema")
        @Expose
        public String movieCinema;

    }

    public class TrendingMovies {

        @SerializedName("movie_id")
        @Expose
        public String movieId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("language")
        @Expose
        public String language;
        @SerializedName("movie_genre")
        @Expose
        public String movieGenre;
        @SerializedName("movie_restriction")
        @Expose
        public String movieRestriction;
        @SerializedName("movie_duration")
        @Expose
        public String movieDuration;
        @SerializedName("movie_format")
        @Expose
        public String movieFormat;

    }

    public class UpcomingMovie {

        @SerializedName("movie_id")
        @Expose
        public String movieId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("language")
        @Expose
        public String language;
        @SerializedName("movie_genre")
        @Expose
        public String movieGenre;
        @SerializedName("movie_restriction")
        @Expose
        public String movieRestriction;
        @SerializedName("movie_duration")
        @Expose
        public String movieDuration;
        @SerializedName("movie_format")
        @Expose
        public String movieFormat;
        @SerializedName("movie_cinema")
        @Expose
        public String movieCinema;
        @SerializedName("book_stat")
        @Expose
        public String bookStat;

    }
}
