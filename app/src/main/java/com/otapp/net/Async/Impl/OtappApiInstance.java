package com.otapp.net.Async.Impl;

import com.otapp.net.utils.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtappApiInstance {
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance(){
    if(retrofit==null){
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
        .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS).
                writeTimeout(90, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstants.ApiNames.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    return retrofit;
    }
}

