package com.otapp.net.Bus.Network;

import com.google.gson.JsonObject;
import com.otapp.net.Bus.Core.Agent;
import com.otapp.net.Bus.Core.AppConstants;
import com.otapp.net.Bus.Core.BookTicketResponse;
import com.otapp.net.Bus.Core.GetFareResponse;
import com.otapp.net.Bus.Core.ProcessSeat;
import com.otapp.net.Bus.Core.ReserveTicketResponse;
import com.otapp.net.Bus.Core.SearchBusResponse;
import com.otapp.net.Bus.Core.SeatMap;
import com.otapp.net.Bus.Core.SeatsMap;
import com.otapp.net.Bus.Core.StationResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OtappApiService {

    @GET(AppConstants.ApiNames.STATIONS_LIST)
    Call<StationResponse> getStations(@Query("key") String sKey, @Query("auth_key") String sAuthKey);


    @FormUrlEncoded
    @POST(AppConstants.ApiNames.LOGIN)
    Call<Agent> checkLogin(@Query("key") String sKey, @Field("username") String sUserName,
                           @Field("pswd") String sPassword, @Field("owner_id") String sOwnerId,
                           @Field("auth_key") String sAuthKey, @Field("imei") String sIMEI,
                           @Field("lat") String sLatitude, @Field("long") String sLongitude,
                           @Field("ip") String sIP, @Field("key") String sApiKey,
                           @Field("app_ver") String sAppVersion, @Field("lang") String sLanguage);

    @FormUrlEncoded
    @POST(AppConstants.ApiNames.SEARCH_BUS)
    Call<SearchBusResponse> searchBuses(@Query("key") String sApiKey, @Query("auth_key") String sAuthKey,
                                        @Field("from_id") String sFromId, @Field("to_id") String sToId,
                                       @Field("comp_id") String sCompId ,@Field("trvl_dt") String sTravelDate,
                                        @Field("agent_id") String sAgentID, @Field("is_from") String sIsFrom,
                                        @Field("key") String sKey);
    @FormUrlEncoded
    @POST(AppConstants.ApiNames.SEARCH_BUS)
    Call<JsonObject> searchReturnBuses(@Query("key") String sApiKey, @Query("auth_key") String sAuthKey,
                                        @Field("from_id") String sFromId, @Field("to_id") String sToId,
                                       @Field("comp_id") String sCompId ,@Field("trvl_dt") String sTravelDate,
                                        @Field("agent_id") String sAgentID, @Field("is_from") String sIsFrom,
                                        @Field("key") String sKey);


    @FormUrlEncoded
    @POST(AppConstants.ApiNames.SEAT_MAP)
    Call<JsonObject> getSeats(@Query("key") String sKey, @Query("auth_key") String sAuthKey,
                              @Field("sub_id") String sub_id, @Field("tdi_id") String tdi_id,
                              @Field("lb_id") String lb_id, @Field("pbi_id") String pbi_id,
                              @Field("asi_id") String asi_id,
                              @Field("comp_id") String comp_id, @Field("agent_id") String agent_id,
                              @Field("is_from") String is_from, @Field("key") String key);


    @FormUrlEncoded
    @POST(AppConstants.ApiNames.PROCESS_SEAT)
    Call<ProcessSeat> processSeat(@Query("key") String sApiKey, @Query("auth_key") String sAuthKey,
                                  @Field("sub_id") String sSubID, @Field("key") String sKey,
                                  @Field("tdi_id") String sTdiId, @Field("lb_id") String sLbId,
                                  @Field("pbi_id") String sPbiId, @Field("asi_id") String sAsiIDd,
                                  @Field("comp_id") String sCompId, @Field("agent_id") String sAgentId,
                                  @Field("is_from") String sIsFrom, @Field("seat_id") String sSeatId,
                                  @Field("currency") String sCurrency, @Field("ukey") String sUkey,
                                  @Field("from_id") String sFromId, @Field("to_id") String sToId,
                                  @Field("trvl_dt") String sTravelDt, @Field("seat_type_id") String sSeatTypeId);

    @FormUrlEncoded
    @POST(AppConstants.ApiNames.GET_FARE)
    Call<GetFareResponse> getFare(@Query("key") String sApiKey, @Query("auth_key") String sAuthKey,
                                  @Field("asi_id") String sAsiIDd, @Field("key") String sKey,
                                  @Field("ukey") String sUkey,@Field("promo_id") String sPromoId,
                                  @Field("comp_id") String sCompId, @Field("agent_id") String sAgentId,
                                  @Field("is_from") String sIsFrom,@Field("currency") String sCurrency,
                                  @Field("return_asi_id") String sRetrunAsid,@Field("return_ukey") String sReturnUkey);
    @FormUrlEncoded
    @POST(AppConstants.ApiNames.Reserve_Booking)
    Call<ReserveTicketResponse> getReserveBooking(@Query("key") String sApiKey, @Query("auth_key") String sAuthKey,
                                                  @Field("key") String sKey, @Field("comp_id") String sCompId, @Field("agent_id") String sAgentId,
                                                  @Field("is_from") String sIsFrom, @Field("currency") String sCurrency,
                                                  @Field("phone") String phone, @Field("email") String email, @Field("passengers") String passengers,
                                                  @Field("return_passengers") String return_passengers, @Field("cust_id") String cust_id,
                                                  @Field("pay_code") String pay_code);
    @FormUrlEncoded
    @POST(AppConstants.ApiNames.BOOK_SEAT)
    Call<BookTicketResponse> bookTicket(@Query("key") String sKey, @Field("owner_id") String sOwnerId,
                                        @Field("agent_id") String sAgentId, @Field("auth_key") String sAuthKey,
                                        @Field("imei") String sIMEI, @Field("lat") String sLatitude,
                                        @Field("long") String sLongitude, @Field("ip") String sIP,
                                        @Field("app_ver") String sAppVersion, @Field("tran_pass") String sTransactionPassword,
                                        @Field("passenger_details") String sPassengerDetails,
                                        @Field("mobile") String sPhoneNo,
                                        @Field("email") String sEmail,
                                        @Field("fromID") String sFromId, @Field("toID") String sToId,
                                        @Field("journey_Date") String sTravelDate, @Field("bus_route_seat_id") String sBusRouteSeatId,
                                        @Field("bus_id") String sBusId, @Field("ukey") String sUKey,
                                        @Field("seat_counter") String sSeatCount,
                                        @Field("sold_fare") String sFare,
                                        @Field("user_selected_date") String sUserSelectedDate,
                                        @Field("key") String sApiKey,
                                        @Field("language") String sLanguage);


}