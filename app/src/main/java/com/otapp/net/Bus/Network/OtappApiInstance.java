package com.otapp.net.Bus.Network;


import com.otapp.net.Bus.Core.AppConstants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtappApiInstance {
    private static Retrofit retrofit;

    /*public static Retrofit RetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();


            *//*retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(AppConstants.ApiNames.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();*//*
            retrofit = new Retrofit.Builder()
                    .client(new OkHttpClient.Builder()
                            .addNetworkInterceptor(new Interceptor() {

                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request.Builder builder = chain.request().newBuilder();

                                    builder.addHeader("Accept-Language", "en");
                                    Request request = builder.build();
                                    Response response = chain.proceed(request);

                                    return response;
                                }
                            }).connectTimeout(120, TimeUnit.SECONDS)
                            .writeTimeout(120, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS).build())
                    .baseUrl(AppConstants.ApiNames.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

           *//* Retrofit ret = new Retrofit.Builder()
                    .client(new OkHttpClient.Builder()
                            .addNetworkInterceptor(new Interceptor() {

                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request.Builder builder = chain.request().newBuilder();

                                    builder.addHeader("Accept-Language", "Your value");
                                    Request request = builder.build();
                                    Response response = chain.proceed(request);

                                    return response;
                                }
                            }).build())
                    .build();*//*
        }
        return retrofit;
    }*/


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();


            /*retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(AppConstants.ApiNames.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();*/
            retrofit = new Retrofit.Builder()
                    .client(new OkHttpClient.Builder()
                            .addNetworkInterceptor(new Interceptor() {

                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request.Builder builder = chain.request().newBuilder();

                                    builder.addHeader("Accept-Language", "en");
                                    Request request = builder.build();
                                    Response response = chain.proceed(request);

                                    return response;
                                }
                            }).connectTimeout(120, TimeUnit.SECONDS)
                            .writeTimeout(120, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS).build())
                    .baseUrl(AppConstants.ApiNames.API_URL_TEST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
