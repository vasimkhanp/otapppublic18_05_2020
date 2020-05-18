package com.otapp.net.Async.Interface;

import com.otapp.net.Events.Core.ApiResponse;
import com.otapp.net.Events.Core.DeleteSeatsResponse;
import com.otapp.net.Events.Core.EventPaymentSummaryResponse;
import com.otapp.net.Events.Core.EventSeatProcessingResponse;
import com.otapp.net.Events.Core.EventsListResponse;
import com.otapp.net.Events.Core.GetTicketTypeResponse;
import com.otapp.net.Events.Core.PaymentSuceesResponse;
import com.otapp.net.Events.Core.PromoCodeResponse;
import com.otapp.net.Events.Core.TicketNumberResponse;
import com.otapp.net.Events.Core.VerifyPromoResponse;
import com.otapp.net.PromoCode.ZeroPaymentPromoResponse;
import com.otapp.net.home.core.MyBookingFlightResponse;
import com.otapp.net.home.core.MyBookingResponse;
import com.otapp.net.model.CountryCodePojo;
import com.otapp.net.utils.AppConstants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OtappApiServices {

    @GET(AppConstants.ApiNames.COUNTRY_CODE)
    Call<CountryCodePojo> getCountryCodeList(@Query("user_token") String userToken);

    @GET(AppConstants.ApiNames.RESEND_CONFIRMATION)
    Call<ApiResponse> resendEventConfirmation(@Query("tkt_no") String mBookingID, @Query("user_token") String user_token);

   /* @GET(AppConstants.ApiNames.MOVIE_LIST)
    Call<MovieListResponseModel> getMovieList(@Query("user_token") String user_token);


    @GET(AppConstants.ApiNames.MOVIE_DETAILS)
    Call<MovieDetailsResponse> getMovieDetails(@Query("movie_id") String movie_id, @Query("user_token") String user_token);

    @GET(AppConstants.ApiNames.SCREEM_TIME)
    Call<MoviesDateDayResponse> getMovieTheaterList(@Query("movie_id") String movie_id, @Query("dt") String date, @Query("user_token") String user_token);*/

    @FormUrlEncoded
    @POST(AppConstants.ApiNames.EVENTS_LIST)
    Call<EventsListResponse> getEventsList(@Query("key") String apiKey, @Field("agent_id") String agent_id,
                                           @Field("book_from") String book_from, @Field("auth_key") String auth_key,
                                           @Field("key") String key);

    @FormUrlEncoded
    @POST(AppConstants.ApiNames.EVENTS_SEAT_PROCESSING)
    Call<EventSeatProcessingResponse> seatProcessing(@Query("key") String apiKey, @Field("agent_id") String agent_id,
                                                     @Field("book_from") String book_from, @Field("auth_key") String auth_key,
                                                     @Field("event_id") String event_id, @Field("tkt_id") String tkt_id,
                                                     @Field("event_date") String event_date, @Field("no_tkts") String no_tkts,
                                                     @Field("ukey") String ukey, @Field("key") String key);

    @FormUrlEncoded
    @POST(AppConstants.ApiNames.EVENTS_GET_FARE)
    Call<EventPaymentSummaryResponse> getPaymentSummary(@Query("key") String apiKey, @Field("agent_id") String agent_id,
                                                        @Field("book_from") String book_from, @Field("auth_key") String auth_key,
                                                        @Field("event_id") String event_id, @Field("ukey") String uKey,
                                                        @Field("promo_id") String promo_id, @Field("key") String key);

   @GET(AppConstants.ApiNames.PROMO_CODE)
    Call<PromoCodeResponse> getPromoCode(@Query("promo_code") String promoCode, @Query("prop_id") String promoId, @Query("tkt_info") String ticketInof,
                                         @Query("tot_fare") String totalFare, @Query("ukey") String uKey,
                                         @Query("cur") String currency, @Query("platform") String platForm,
                                         @Query("key") String key);

   @GET(AppConstants.ApiNames.PROMO_CODE_VERIFY)
    Call<VerifyPromoResponse> getVerifyPromo(@Query("verify_code") String verify_code, @Query("ukey") String ukey, @Query("key") String key);


    @FormUrlEncoded
   @POST(AppConstants.ApiNames.PROMO_TICKET_SUCCESS)
    Call<PaymentSuceesResponse> getSuccessTicketInfo(@Query("key") String apiKey, @Field("agent_id") String agent_id,
                                                     @Field("book_from") String book_from, @Field("auth_key") String auth_key,
                                                     @Field("key") String key, @Field("tkt") String tkt);


    @FormUrlEncoded
    @POST(AppConstants.ApiNames.PROMO_ZERO_PAYMENT)
    Call<ZeroPaymentPromoResponse> getPromoZeroPayment(@Query("key") String apiKey, @Field("agent_id") String agent_id,
                                                       @Field("book_from") String book_from, @Field("auth_key") String auth_key,
                                                       @Field("event_id") String event_id, @Field("ukey") String uKey,
                                                       @Field("promo_id") String promo_id,
                                                       @Field("event_date") String event_date, @Field("no_tkts") String no_tkts,
                                                       @Field("cust_id") String cust_id, @Field("cust_name") String cust_name,
                                                       @Field("email") String email, @Field("mob") String mob,
                                                       @Field("req_from") String req_from,
                                                       @Field("key") String key);


    @FormUrlEncoded
    @POST(AppConstants.ApiNames.NUMBER_AIRTEL)
    Call<TicketNumberResponse> getAirtelPaymentProceed(@Query("key") String apiKey, @Field("agent_id") String agent_id,
                                                       @Field("book_from") String book_from, @Field("auth_key") String auth_key,
                                                       @Field("event_id") String event_id, @Field("ukey") String uKey,
                                                       @Field("promo_id") String promo_id,
                                                       @Field("event_date") String event_date, @Field("no_tkts") String no_tkts,
                                                       @Field("cust_id") String cust_id, @Field("cust_name") String cust_name,
                                                       @Field("email") String email, @Field("mob") String mob,
                                                       @Field("airtel_term") String airtel_term, @Field("req_from") String req_from,
                                                       @Field("key") String key, @Field("airtel") String airtel);

    @FormUrlEncoded
    @POST(AppConstants.ApiNames.NUMBER_TIGO)
    Call<TicketNumberResponse> getTigoPaymentProceed(@Query("key") String apiKey, @Field("agent_id") String agent_id,
                                                     @Field("book_from") String book_from, @Field("auth_key") String auth_key,
                                                     @Field("event_id") String event_id, @Field("ukey") String uKey,
                                                     @Field("promo_id") String promo_id,
                                                     @Field("event_date") String event_date, @Field("no_tkts") String no_tkts,
                                                     @Field("cust_id") String cust_id, @Field("cust_name") String cust_name,
                                                     @Field("email") String email, @Field("mob") String mob,
                                                     @Field("tigo_term") String tigo_term, @Field("req_from") String req_from,
                                                     @Field("key") String key, @Field("tigo") String tigo);
    @FormUrlEncoded
    @POST(AppConstants.ApiNames.NUMBER_MPESA)
    Call<TicketNumberResponse> getMpesaPaymentProceed(@Query("key") String apiKey, @Field("agent_id") String agent_id,
                                                      @Field("book_from") String book_from, @Field("auth_key") String auth_key,
                                                      @Field("event_id") String event_id, @Field("ukey") String uKey,
                                                      @Field("promo_id") String promo_id,
                                                      @Field("event_date") String event_date, @Field("no_tkts") String no_tkts,
                                                      @Field("cust_id") String cust_id, @Field("cust_name") String cust_name,
                                                      @Field("email") String email, @Field("mob") String mob,
                                                      @Field("mpesa_term") String mpesa_term, @Field("req_from") String req_from,
                                                      @Field("key") String key, @Field("mpesa") String mpesa);

    @FormUrlEncoded
    @POST(AppConstants.ApiNames.GET_TICKET_TYPE)
    Call<GetTicketTypeResponse> getTicket(@Query("key") String apiKey,  @Field("agent_id") String agent_id,
                                          @Field("book_from") String book_from, @Field("auth_key") String auth_key,
                                          @Field("event_id") String event_id,@Field("floor_id") String floorId,
                                          @Field("event_date") String eventDate, @Field("key") String key);




    @FormUrlEncoded
    @POST(AppConstants.ApiNames.CLEAR_SEATS)
    Call<DeleteSeatsResponse> deleteEventSeats(@Query("key") String sKey, @Field("agent_id") String sAgentId,
                                               @Field("book_from") String sBookFrom, @Field("auth_key") String sAuthKey,
                                               @Field("ukey") String sUKey, @Field("key") String sPostKey);


    @FormUrlEncoded
    @POST(AppConstants.ApiNames.MY_BOOKINGS)
    Call<MyBookingResponse> getMyBookings(@Query("key") String key,@Field("cust_log_key") String custLogKey,
                                          @Field("cust_log_id") String custLogId,@Field("service_id") String serviceId,
                                          @Field("auth_key") String authKey,@Field("req_type") String reqType,
                                          @Field("pg_no")String pageNo);

}
