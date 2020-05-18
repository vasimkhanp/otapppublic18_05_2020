package com.otapp.net.Async.Interface;

/**
 * Created by JITEN-PC on 28-12-2016.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.otapp.net.BuildConfig;
import com.otapp.net.utils.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestClient {

    public static String API_PREFIX = "https://www.managemyticket.net/api/";

    public static String MINT_KEY = "96488592";

    public static OtappApiServices getClient(boolean isGson) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(120, TimeUnit.SECONDS);
        httpClient.readTimeout(600, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("auth_token", Utils.getToken())
//                        .header("Content-Type", "application/json")
                        .method(original.method(),original.body())
                        .build();
                return chain.proceed(request);
            }
        }).build();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.interceptors().add(logging);
        }

        Retrofit retrofit = null;

        if (isGson) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_PREFIX)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build()).build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_PREFIX)
                    .client(httpClient.build()).build();
        }


        return retrofit.create(OtappApiServices.class);
    }

}
