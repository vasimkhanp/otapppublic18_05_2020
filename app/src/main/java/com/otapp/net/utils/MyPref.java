package com.otapp.net.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Set;

public class MyPref {

    public static String PREF_IS_LOGGED = "PREF_IS_LOGGED";
    public static String PREF_IS_SKIPED = "PREF_IS_SKIPED";

    public static String PREF_PARK_CART_ID = "PREF_PARK_CART_ID";
    public static String PREF_PARK_CART_COUNT = "PREF_PARK_CART_COUNT";
    public static String PREF_PARK_DATE = "PREF_PARK_DATE";

    public static String PREF_USER_ID = "PREF_USER_ID";
    public static String PREF_USER_FIRST_NAME = "PREF_USER_FIRST_NAME";
    public static String PREF_USER_LAST_NAME = "PREF_USER_LAST_NAME";
    public static String PREF_USER_FULLNAME = "PREF_USER_FULLNAME";
    public static String PREF_USER_EMAIL = "PREF_USER_EMAIL";
    public static String PREF_SELECTED_COUNTRY_CODE = "PREF_SELECTED_COUNTRY_CODE";
    public static String PREF_USER_COUNTRY_CODE = "PREF_USER_COUNTRY_CODE";
    public static String PREF_USER_MOB = "PREF_USER_MOB";
    public static String PREF_USER_PROFILE = "PREF_USER_PROFILE";
    public static String PREF_USER_GENDER = "PREF_USER_GENDER";
    public static String PREF_USER_MARITAL_STATUS = "PREF_USER_MARITAL_STATUS";
    public static String PREF_USER_KEY = "PREF_USER_KEY";
    public static String PREF_USER_LOG_ID = "PREF_USER_LOG_ID";
    public static String PREF_MOVIE_RESPONSE = "PREF_MOVIE_RESPONSE";
    public static String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    public static String PREF_DEFAULT_COUNTRY_CODE = "+255";
    public static String PREF_BUS_UKEY = "PREF_BUS_UKEY";
    public static String PREF_DIFF_RETURN_PASSNR = "PREF_DIFF_RETURN_PASSNR";


    public static String PREF_UKEY = "PREF_UKEY";
    public static String PREF_EVENT_DATE="EVENT_DATE";
    public static String PREF_EVENT_ID="EVENT_ID";
    public static String PREF_CURRENCY="CURRENCY";
    public static String PREF_PROMO_ID ="PROMORESPOSNSE";
    public static String PREF_PROMO_CODE="PROMO_CODE";
    public static String PREF_PROMO_FLAG="PROMOFLAG";
    public static  String PREF_SPINER_ENABLE="";
    public static  String SERVICE_ID="";
    public static  String REQUEST_TYPE="REQUEST_TYPE";
    public static String ASID = "ASID";
    public static String RETURN_UKEY = "RETURN_UKEY";
    public static boolean RETURN_BUS=false;




    public static void setPref(Context mContext, String key, String value) {

        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setPref(Context mContext, String key, Boolean value) {

        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setPref(Context mContext, String key, Float value) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void setPref(Context mContext, String key, int value) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setPref(Context mContext, String key, long value) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setPref(Context mContext, String key,
                               Set<String> valueSet) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(mContext).edit();
        editor.putStringSet(key, valueSet);
        editor.commit();
    }

    public static String getPref(Context mContext, String key, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(key, defValue);
    }

    public static boolean getPref(Context mContext, String key, boolean defValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getBoolean(key, defValue);
    }

    public static float getPref(Context mContext, String key, float defValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getFloat(key, defValue);
    }

    public static int getPref(Context mContext, String key, int defValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(
                key, defValue);
    }

    public static long getPref(Context mContext, String key, long defValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getLong(
                key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getPref(Context mContext, String key,
                                      Set<String> defValueSet) {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getStringSet(key, defValueSet);
    }
}

