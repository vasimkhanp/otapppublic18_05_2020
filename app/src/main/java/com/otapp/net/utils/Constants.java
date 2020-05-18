package com.otapp.net.utils;

import java.text.DecimalFormat;

public class Constants {

    public static final int RC_SIGN_IN = 1001;
    public static final int RC_YT_DIALOG_REQUEST = 1002;
    public static final int RC_ONE_WAY_FILTER = 1100;
    public static final int RC_RETURN_FILTER = 1101;
    public static final int RC_MULTI_CITY_FILTER = 1102;
    public static final int RC_FLIGHT_DETAIL_SIGN_IN = 1005;
    public static final int RC_IMAGE = 2050;
    public static final int RC_MOVIE_COUPON_CODE = 3001;
    public static final int RC_EVENT_COUPON_CODE = 3002;
    public static final int RC_PARK_COUPON_CODE = 3003;

    public static String BNDL_EVENT = "BNDL_EVENT";
    public static final String BNDL_CATEGORY_ID = "BNDL_CATEGORY_ID";
    public static final String BNDL_EVENT_LIST = "BNDL_EVENT_LIST";
    public static final String BNDL_EVENT_DETAILS = "BNDL_EVENT_DETAILS";
    public static final String BNDL_EVENT_FLOOR_ID = "BNDL_EVENT_FLOOR_ID";
    public static final String BNDL_EVENT_SEAT_COUNT = "BNDL_EVENT_SEAT_COUNT";
    public static final String BNDL_EVENT_DATE = "BNDL_EVENT_DATE";
    public static final String BNDL_EVENT_TICKET = "BNDL_EVENT_TICKET";
    public static final String BNDL_EVENT_PAYMENT_SUMMARY = "BNDL_EVENT_PAYMENT_SUMMARY";
    public static final String BNDL_EVENT_COUPON = "BNDL_EVENT_COUPON";

    public static final String BNDL_MOVIE_ID = "BNDL_MOVIE_ID";
    public static final String BNDL_MOVIE_STATE = "BNDL_MOVIE_STATE";
    public static final String BNDL_MOVIE_TYPE = "BNDL_MOVIE_TYPE";
    public static final String BNDL_MOVIE_TYPE_CURRENT = "BNDL_MOVIE_TYPE_CURRENT";
    public static final String BNDL_MOVIE_TYPE_UPCOMING = "BNDL_MOVIE_TYPE_UPCOMING";
    public static final String BNDL_MOVIE = "BNDL_MOVIE";
    public static final String BNDL_MOVIE_DATE_LIST = "BNDL_MOVIE_DATE_LIST";
    public static final String BNDL_MOVIE_CINEMA_LIST = "BNDL_MOVIE_CINEMA_LIST";
    public static final String BNDL_MOVIE_TIME_LIST = "BNDL_MOVIE_TIME_LIST";
    public static final String BNDL_MOVIE_DATE_ID = "BNDL_MOVIE_DATE_ID";
    public static final String BNDL_MOVIE_CINEMA_ID = "BNDL_MOVIE_CINEMA_ID";
    public static final String BNDL_MOVIE_TIME_ID = "BNDL_MOVIE_TIME_ID";
    public static final String BNDL_MOVIE_SEAT_LIST = "BNDL_MOVIE_SEAT_LIST";
    public static final String BNDL_MOVIE_PAYMENT_SUMMARY = "BNDL_MOVIE_PAYMENT_SUMMARY";
    public static final String BNDL_MOVIE_COUPON = "BNDL_MOVIE_COUPON";
    public static final String BNDL_MOVIE_FOOD_LIST = "BNDL_MOVIE_FOOD_LIST";
    public static final String BNDL_USER_KEY = "BNDL_USER_KEY";
    public static final String BNDL_MAX_SEAT = "BNDL_MAX_SEAT";
    public static final String BNDL_TITLE = "BNDL_TITLE";
    public static final String BNDL_URL = "BNDL_URL";
    public static final String BNDL_PAYMENT_TYPE = "BNDL_PAYMENT_TYPE";
    public static final String BNDL_PAYMENT_TYPE_DEBIT = "BNDL_PAYMENT_TYPE_DEBIT";
    public static final String BNDL_PAYMENT_TYPE_TIGO = "BNDL_PAYMENT_TYPE_TIGO";
    public static final String BNDL_PAYMENT_TYPE_MPESA = "BNDL_PAYMENT_TYPE_MPESA";
    public static final String BNDL_PAYMENT_TYPE_AIRTEL = "BNDL_PAYMENT_TYPE_AIRTEL";
    public static final String BNDL_MOVIE_RESPONSE = "BNDL_MOVIE_RESPONSE";
    public static final String BNDL_THEME_PARK_RESPONSE = "BNDL_THEME_PARK_RESPONSE";

    public static final String BNDL_PARK = "BNDL_PARK";
    public static final String BNDL_PARK_ID = "BNDL_PARK_ID";
    public static final String BNDL_PARK_DETAILS = "BNDL_PARK_DETAILS";
    public static final String BNDL_PARK_CART_LIST = "BNDL_PARK_CART_LIST";
    public static final String BNDL_IS_PARK_FROM_RIDE = "BNDL_IS_PARK_FROM_RIDE";

    public static final String BNDL_FLIGHT = "BNDL_FLIGHT";
    public static final String BNDL_FLIGHT_NAME_LIST = "BNDL_FLIGHT_NAME_LIST";
    public static final String BNDL_CITY_LIST = "BNDL_CITY_LIST                     ";
    public static final String BNDL_FLIGHT_ONE_DETAILS = "BNDL_FLIGHT_ONE_DETAILS";
    public static final String BNDL_FLIGHT_RETURN_DETAILS = "BNDL_FLIGHT_RETURN_DETAILS";
    public static final String BNDL_FLIGHT_DETAILS_RESPONSE = "BNDL_FLIGHT_DETAILS_RESPONSE";
    public static final String BNDL_FLIGHT_PAYMENT_SUMMARY = "BNDL_FLIGHT_PAYMENT_SUMMARY";
    public static final String BNDL_FLIGHT_RESPONSE = "BNDL_FLIGHT_RESPONSE";
    public static final String BNDL_FLIGHT_TRAVELLER = "BNDL_FLIGHT_TRAVELLER";
    public static final String BNDL_FLIGHT_UID = "BNDL_FLIGHT_UID";
    public static final String BNDL_FLIGHT_CITY_COUNT = "BNDL_FLIGHT_CITY_COUNT";
    public static final String CITY_TYPE_POSITION = "CITY_TYPE_POSITION";
    public static final String BNDL_AIRLINE_LIST = "BNDL_AIRLINE_LIST";
    public static final String BNDL_PREFERRED_AIRLINE_LIST = "BNDL_PREFERRED_AIRLINE_LIST";

    public static final String BNDL_FLIGHT_SESSION_TIME = "BNDL_FLIGHT_SESSION_TIME";
    public static final String BNDL_FLIGHT_SEARCH_LIST_SESSION_TIME = "BNDL_FLIGHT_SEARCH_LIST_SESSION_TIME";
    public static final String BNDL_AIRLINE_TIME = "BNDL_AIRLINE_TIME";
    public static final String BNDL_AIRLINE_REFUNDABLE = "BNDL_AIRLINE_REFUNDABLE";

    public static final String BNDL_FULLNAME = "BNDL_FULLNAME";
    public static final String BNDL_EMAIL = "BNDL_EMAIL";
    public static final String BNDL_MOBILE_NUMBER = "BNDL_MOBILE_NUMBER";

    public static final String BNDL_SCREEN_NAME = "BNDL_SCREEN_NAME";

    public static final String BNDL_PROPERTY_ID = "BNDL_PROPERTY_ID";
    public static final String BNDL_TICKET_INFO = "BNDL_TICKET_INFO";
    public static final String BNDL_TOTAL_FARE = "BNDL_TOTAL_FARE";
    public static final String BNDL_CURRENCY = "BNDL_CURRENCY";
    public static final String BNDL_COUPON_RESPONSE = "BNDL_COUPON_RESPONSE";

    public static final String YEAR = "YEAR";
    public static final String MONTH = "MONTH";
    public static final String DAY = "DAY";

    public static final String SEAT_NONE = "SEAT_NONE";
    public static final String SEAT_AVAILALBLE = "SEAT_AVAILALBLE";
    //    public static final String SEAT_INVISIBLE = "SEAT_INVISIBLE";
    public static final String SEAT_PROCESSED = "SEAT_PROCESSED";
    public static final String SEAT_SELECTED = "SEAT_SELECTED";
    public static final String SEAT_BOOKED = "SEAT_BOOKED";

    public static final String BNDL_TRAILER_LINK = "BNDL_TRAILER_LINK";

    public static final String FILTER_FLIGHT_ADD_CITY = "FILTER_FLIGHT_ADD_CITY";

    public static final String BNDL_MOVIE_TAB = "BNDL_MOVIE_TAB";
    public static final String YouTubeKey = "AIzaSyDW4FehOqDo6GqiVEN8i2Lz_UWyl_hsnaA";

    public static final String API_FLIGHT_TITLE="flights";

    public static DecimalFormat mDecimal = new DecimalFormat("0.00");
    public static DecimalFormat mPriceFormate = new DecimalFormat("#,###,###,###.##");


}
