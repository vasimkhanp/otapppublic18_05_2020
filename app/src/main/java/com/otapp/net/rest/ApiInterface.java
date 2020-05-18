package com.otapp.net.rest;


import com.google.gson.JsonObject;
import com.otapp.net.model.AddReviewPojo;
import com.otapp.net.model.ApiResponse;
import com.otapp.net.model.ComingSoonPojo;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.model.CouponResponsePojo;
import com.otapp.net.model.EventCategoryPojo;
import com.otapp.net.model.EventDetailsPojo;
import com.otapp.net.model.EventListPojo;
import com.otapp.net.model.EventPaymentSummaryPojo;
import com.otapp.net.model.EventSeatBookedPojo;
import com.otapp.net.model.EventSuccessPojo;
import com.otapp.net.model.EventZeroResponse;
import com.otapp.net.model.FlightCityPojo;
import com.otapp.net.model.FlightMultiCityPojo;
import com.otapp.net.model.FlightOneDetailsPojo;
import com.otapp.net.model.FlightOneListPojo;
import com.otapp.net.model.FlightPaymentSummaryPojo;
import com.otapp.net.model.FlightReturnListPojo;
import com.otapp.net.model.FlightSuccessPojo;
import com.otapp.net.model.FoodListPojo;
import com.otapp.net.model.HomeAdsResponse;
import com.otapp.net.model.MovieAdvertiseResponse;
import com.otapp.net.model.MovieDetailsPojo;
import com.otapp.net.model.MovieListPojo;
import com.otapp.net.model.MoviePaymentSummaryPojo;
import com.otapp.net.model.MovieSeatListPojo;
import com.otapp.net.model.MovieSuccessPojo;
import com.otapp.net.model.MovieTheaterListPojo;
import com.otapp.net.model.MovieZeroResponse;
import com.otapp.net.model.ParkPaymentSummaryPojo;
import com.otapp.net.model.ParkZeroResponse;
import com.otapp.net.model.RecomendedMoviePojo;
import com.otapp.net.model.ReviewListPojo;
import com.otapp.net.model.SeatProcessedPojo;
import com.otapp.net.model.ThemeParkCartListPojo;
import com.otapp.net.model.ThemeParkDetailsPojo;
import com.otapp.net.model.ThemeParkPojo;
import com.otapp.net.model.ThemeParkReconfirmResponse;
import com.otapp.net.model.ThemeParkRideListPojo;
import com.otapp.net.model.ThemeParkSuccessPojo;
import com.otapp.net.model.UpcomingTripPojo;
import com.otapp.net.model.UpdatePojo;
import com.otapp.net.model.UpdateRideDateModel;
import com.otapp.net.model.UserEditPojo;
import com.otapp.net.model.UserPojo;
import com.otapp.net.utils.Constants;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by JITEN-PC on 21-12-2016.
 */

public interface ApiInterface {

    @GET("auth/signup.php")
    Call<ApiResponse> registerUser(@QueryMap Map<String, String> options);

    @GET("auth/login.php")
    Call<UserPojo> loginUser(@QueryMap Map<String, String> options);

    @GET("auth/forget_password.php")
    Call<ApiResponse> forgotPassword(@QueryMap Map<String, String> options);

    @GET("auth/logout.php")
    Call<ApiResponse> logout(@QueryMap Map<String, String> options);

    @GET("auth/social_login.php")
    Call<UserPojo> loginUserWithSocial(@QueryMap Map<String, String> options);

    @GET("auth/reset_password.php")
    Call<ApiResponse> resetPassword(@QueryMap Map<String, String> options);

    @GET("auth/delete_profile_image.php")
    Call<ApiResponse> removePicture(@QueryMap Map<String, String> options);

    @GET("add_movie_reviews.php")
    Call<AddReviewPojo> addMovieReview(@QueryMap Map<String, String> options);

    @GET("get_movie_reviews.php")
    Call<ReviewListPojo> getReviewList(@QueryMap Map<String, String> options);

    @GET("auth/getBookings.php")
    Call<UpcomingTripPojo> getUpcomingTripList(@QueryMap Map<String, String> options);

    @GET("promo/Validate_Promo.php")
    Call<CouponResponsePojo> validateCouponCode(@QueryMap Map<String, String> options);


    @GET("get_movie_fare_cal.php")
    Call<MoviePaymentSummaryPojo> getMoviePaymentSummary(@QueryMap Map<String, String> options);

    @GET("movie_zeropay_booking.php")
    Call<MovieZeroResponse> getMovieZeroPayment(@QueryMap Map<String, String> options);


    @GET("promo/Additional_verify.php")
    Call<ApiResponse> verifyCouponCode(@QueryMap Map<String, String> options);

    @GET("auth/getCompletedBookings.php")
    Call<UpcomingTripPojo> getCompleteTripList(@QueryMap Map<String, String> options);

    @GET("auth/getCancelledBookings.php")
    Call<UpcomingTripPojo> getCancelledTripList(@QueryMap Map<String, String> options);

    @GET("get_country_code_v2.php")
    Call<CountryCodePojo> getCountryCodeList(@QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST()
    Call<UpdatePojo> checkVersion(@Url String api, @FieldMap Map<String, String> params);

    @GET("get_event_category.php")
    Call<EventCategoryPojo> getEventCategory(@Query("user_token") String user_token);

    @GET("get_event_list.php")
    Call<EventListPojo> getEventList(@Query("user_token") String user_token);



    @GET("get_event_detail_v2.php")
    Call<EventDetailsPojo> getEventDetails(@Query("event_id") String event_id, @Query("user_token") String user_token);


    @GET("get_event_fare_cal.php")
    Call<EventPaymentSummaryPojo> getEventPaymentSummary(@QueryMap Map<String, String> options);

    @GET("event_zeropay_booking.php")
    Call<EventZeroResponse> getEventZeroPayment(@QueryMap Map<String, String> options);

    @GET("event_process_seat_delete.php")
    Call<SeatProcessedPojo> deleteProcessedEventSeatsList(@Query("ukey") String mUserKey, @Query("user_token") String user_token);

    @GET("event_seats.php")
    Call<EventSeatBookedPojo> bookEventSeat(@Query("event_id") String event_id, @Query("ukey") String ukey, @Query("event_date") String event_date,
                                            @Query("event_time") String event_time, @Query("tickets") String tickets, @Query("user_token") String user_token);

    @GET("event_payment_success_v3.php")
    Call<EventSuccessPojo> getEventPaymentSuccess(@Query("tkt_no") String mBookingID, @Query("user_token") String user_token);

    @GET("resend_event_confirmation.php")
    Call<ApiResponse> resendEventConfirmation(@Query("tkt_no") String mBookingID, @Query("user_token") String user_token);


    @GET("homescreen.php")
    Call<HomeAdsResponse> getHomeAds(@Query("user_token") String user_token);

    @GET("get_movie_list_v2.php")
    Call<MovieListPojo> getMovieList(@Query("user_token") String user_token);

    @GET("get_movie_coming_soon.php")
    Call<ComingSoonPojo> getComingSoonMovieList(@Query("user_token") String user_token);

    @GET("get_recommended_movies.php")
    Call<RecomendedMoviePojo> getRecomendedMovieList(@Query("movie_id") String movie_id, @Query("user_token") String user_token);

    @GET("get_movie_details_v2.php")
    Call<MovieDetailsPojo> getMovieDetails(@QueryMap Map<String, String> options);

    @GET("add_movie_like.php")
    Call<ApiResponse> likeMovie(@QueryMap Map<String, String> options);

//    @GET("get_movie_screen.php")
//    Call<MovieScreenListPojo> getMovieScreenList(@Query("movie_id") String movie_id, @Query("dt") String date, @Query("user_token") String user_token);

    @GET("adverts.php")
    Call<MovieAdvertiseResponse> getMovieAdvertiseList(@Query("user_token") String user_token);

    @GET("get_movie_screen_time_v2.php")
    Call<MovieTheaterListPojo> getMovieTheaterList(@Query("movie_id") String movie_id, @Query("dt") String date, @Query("user_token") String user_token);

//    @GET("get_movie_showtime.php")
//    Call<MovieTimeListPojo> getMovieTimeList(@Query("movie_id") String movie_id, @Query("dt") String date, @Query("screen_id") String screen_id, @Query("user_token") String user_token);

    @GET("theatre_screen_v2.php")
    Call<MovieSeatListPojo> getMovieSeatsList(@Query("mv_screen") String screen_id, @Query("user_token") String user_token);

    @GET("get_bk_prcs_sts.php")
    Call<SeatProcessedPojo> getProcessedSeatsList(@Query("mtid") String mtid, @Query("ukey") String user_key, @Query("user_token") String user_token);

    @GET("process_seats_delete.php")
    Call<SeatProcessedPojo> deleteProcessedSeatsList(@Query("ukey") String user_key, @Query("user_token") String user_token);

    @GET("process_movie_seats.php")
    Call<SeatProcessedPojo> setProcessedSeats(@Query("mtid") String mtid, @Query("seat") String seat, @Query("user_key") String user_key, @Query("user_token") String user_token);

    @GET("food_combo_v2.php")
    Call<FoodListPojo> getFoodList(@Query("mv_screen") String mv_screen, @Query("dt") String dt, @Query("show_time") String show_time, @Query("movie_id") String movie_id,
                                   @Query("user_token") String user_token);

    @GET
    Call<String> getPaymentApi(@Url String url);

    @GET("movie_booked_details_v3.php")
    Call<MovieSuccessPojo> getPaymentSuccess(@Query("tkt_no") String mBookingID, @Query("user_token") String user_token);

    @GET("resend_movie_confirmation.php")
    Call<ApiResponse> resendMovieConfirmation(@Query("tkt_no") String mBookingID, @Query("user_token") String user_token);

    @GET("Resend_TP_Confirmation.php")
    Call<ThemeParkReconfirmResponse> resendThemeParkConfirmation(@Query("tkt_no") String mBookingID);

    @GET("first_theme_park.php")
    Call<ThemeParkPojo> getThemePark(@Query("user_token") String user_token);

    @GET("get_theme_park_list.php")
    Call<ThemeParkRideListPojo> getThemeParkRideList(@Query("user_token") String user_token);

    @GET("get_theme_park_detail_v2.php")
    Call<ThemeParkDetailsPojo> getThemeParkDetails(@Query("tp_id") String tp_id, @Query("tp_ukey") String tp_ukey, @Query("user_token") String user_token);

    @GET("get_my_cart.php")
    Call<ThemeParkCartListPojo> getThemeParkCartList(@Query("tp_ukey") String tp_ukey, @Query("user_token") String user_token);

    @GET("insert_tp_process_seats.php")
    Call<ApiResponse> bookThemePark(@Query("tp_id") String tp_id, @Query("tp_dt") String tp_dt, @Query("tp_ukey") String tp_ukey, @Query("adult_ticket_count") String adult_ticket_count,
                                    @Query("child_ticket_count") String child_ticket_count, @Query("stud_ticket_count") String stud_ticket_count, @Query("user_token") String user_token);

    @GET("remove_ride_from_cart.php")
    Call<ApiResponse> removeRideItem(@Query("tp_id") String tp_id, @Query("ukey") String ukey, @Query("user_token") String user_token);


    @GET("get_theme_park_fare_cal.php")
    Call<ParkPaymentSummaryPojo> getParkPaymentSummary(@QueryMap Map<String, String> options);

    @GET("themepark_zeropay_booking.php")
    Call<ParkZeroResponse> getParkZeroPayment(@QueryMap Map<String, String> options);

    @GET("update_tp_ride_date.php")
    Call<JsonObject> getRideDateUpdate(@Query("ukey") String uKey, @Query("ride_dt") String rideDate);

    @GET("payment_success_tp_v2.php")
    Call<ThemeParkSuccessPojo> getThemeParkPaymentSuccess(@Query("tkt_no") String tkt_no, @Query("user_token") String user_token);

    @GET(Constants.API_FLIGHT_TITLE+"/getairportdetails_v2.php")
    Call<FlightCityPojo> getAirportList(@Query("user_token") String user_token);

    @GET(Constants.API_FLIGHT_TITLE+"/get_flight_fare_cal.php")
    Call<FlightPaymentSummaryPojo> getFlightPaymentSummary(@QueryMap Map<String, String> options);

    @GET(Constants.API_FLIGHT_TITLE+"/searchonewayflight.php")
    Call<FlightOneListPojo> getFlightOneList(@Query("from") String from, @Query("to") String to, @Query("date") String date, @Query("adult_count") String adult_count,
                                             @Query("child_count") String child_count, @Query("infants_count") String infants_count, @Query("class") String className, @Query("currency") String currencyName,
                                             @Query("time") String time, @Query("is_refundable_fare") String is_refundable_fare, @Query("airlines") String airlines,
                                             @Query("user_token") String user_token, @Query("flight_auth_token") String flight_auth_token);

    @GET(Constants.API_FLIGHT_TITLE+"/getonewaydetails.php")
    Call<FlightOneDetailsPojo> getFlightOneDetails(@Query("uid") String uid, @Query("key") String key, @Query("currency") String currencyName, @Query("user_token") String user_token, @Query("flight_auth_token") String flight_auth_token);


    @GET(Constants.API_FLIGHT_TITLE+"/searchreturnflight.php")
    Call<FlightReturnListPojo> getReturnFlightList(@Query("from") String from, @Query("to") String to, @Query("dp_date") String dp_date, @Query("re_date") String re_date, @Query("adult_count") String adult_count,
                                                   @Query("child_count") String child_count, @Query("infants_count") String infants_count, @Query("class") String className, @Query("currency") String currencyName,
                                                   @Query("time") String time, @Query("is_refundable_fare") String is_refundable_fare, @Query("airlines") String airlines, @Query("user_token") String user_token, @Query("flight_auth_token") String flight_auth_token);

    @GET(Constants.API_FLIGHT_TITLE+"/getreturntripdetails.php")
    Call<FlightOneDetailsPojo> getFlightReturnDetails(@Query("uid") String uid, @Query("key") String key, @Query("currency") String currencyName, @Query("user_token") String user_token, @Query("flight_auth_token") String flight_auth_token);

    @GET(Constants.API_FLIGHT_TITLE+"/searchmulticity.php")
    Call<FlightMultiCityPojo> getMultiCityFlightList(@Query("journey") String journey, @Query("adult_count") String adult_count,
                                                     @Query("child_count") String child_count, @Query("infants_count") String infants_count, @Query("class") String className, @Query("currency") String currencyName,
                                                     @Query("time") String time, @Query("airlines") String airlines, @Query("user_token") String user_token);

    @GET(Constants.API_FLIGHT_TITLE+"/bookWithAirline.php")
    Call<FlightSuccessPojo> getFlightPaymentSuccess(@Query("tkt_no") String mBookingID, @Query("user_token") String user_token, @Query("flight_auth_token") String flight_auth_token);

    @GET
    Call<ApiResponse> saveFlightDetails(@Url String url);

    @FormUrlEncoded
    @POST("auth/editProfile.php")
    Call<UserEditPojo> saveProfile(@FieldMap Map<String, String> params);
}
