package com.otapp.net.utils;

import android.util.Log;

public class LogUtils {

    public static boolean isLogEnabled = true;


    public static void e(String tag, String msg) {
        if (isLogEnabled) {
            Log.e("-" + tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isLogEnabled) {
            Log.v("-" + tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isLogEnabled) {
            Log.d("-" + tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isLogEnabled) {
            Log.i("-" + tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isLogEnabled) {
            Log.w("-" + tag, msg);
        }
    }


}
