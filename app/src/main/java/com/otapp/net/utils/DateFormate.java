package com.otapp.net.utils;

import java.text.SimpleDateFormat;


public class DateFormate {

    public static SimpleDateFormat sdfMovieDate = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat sdfMovieDay = new SimpleDateFormat("EEEE");
    public static SimpleDateFormat sdfMovieDateSelection = new SimpleDateFormat("dd MMMM");

    public static SimpleDateFormat sdfServerDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat sdfEventDateTime = new SimpleDateFormat("MMM,dd | kk:mm");
    public static SimpleDateFormat sdfEventDate = new SimpleDateFormat("MMM,dd");
    public static SimpleDateFormat sdfDay = new SimpleDateFormat("EEE");
    public static SimpleDateFormat sdfDate = new SimpleDateFormat("dd");
    public static SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM");

    public static SimpleDateFormat sdfEvent12Time = new SimpleDateFormat("hh:mm a");
    public static SimpleDateFormat sdfEvent24Time = new SimpleDateFormat("kk:mm");

    public static SimpleDateFormat sdfParkDisplayDate = new SimpleDateFormat("EEE, dd MMM");
    public static SimpleDateFormat sdfPersonDisplayDate = new SimpleDateFormat("EEE, dd MMM yyyy");
    public static SimpleDateFormat sdfTripDisplayDate = new SimpleDateFormat("dd MMM yyyy");
    public static SimpleDateFormat sdfParkServerDate = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat sdfMyBookingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdfFlightServerDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static SimpleDateFormat sdfAirportServerDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static SimpleDateFormat sdfFlightDisplayDate = new SimpleDateFormat("MMM dd");
    public static SimpleDateFormat sdfCalDisplayDate = new SimpleDateFormat("dd MMM");
    public static SimpleDateFormat sdfDateCompare = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat sdf24Time = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat sdfDuration = new SimpleDateFormat("hh'h' mm'm'");
}
