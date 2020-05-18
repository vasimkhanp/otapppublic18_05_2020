package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecomendedMoviePojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<RecomendedMovie> recomendedMovie = null;

    public class RecomendedMovie {

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
        @SerializedName("book_stat")
        @Expose
        public String bookStat;

    }

}
