package com.otapp.net.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.otapp.net.R;
import com.otapp.net.application.Otapp;
import com.otapp.net.rest.RestClient;
import com.splunk.mint.Mint;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static void showToast(Context context, String msg) {
        if (context == null) {
            return;
        }

        try {
            Toast toast = Toast.makeText(context, msg + "", Toast.LENGTH_LONG);
            View view = toast.getView();
            if (view != null) {
                view.getBackground().setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                TextView text = view.findViewById(android.R.id.message);
                if (text != null) {
                    text.setTextColor(Color.WHITE);
                }
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, msg + "", Toast.LENGTH_LONG).show();
        }

    }

    public static boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    static Dialog progressDialog;

    public static void showProgressDialog(Context context) {

        try {

            if (isProgressDialogShowing()) {
                return;
            }

            if (((Activity) context) != null && ((Activity) context).isFinishing()) {
                return;
            }

            progressDialog = new Dialog(context, R.style.StyledDialog);
            if (progressDialog != null) {
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.dialog_progressbar);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showFlightProgressDialog(Context context) {

        try {

            if (isProgressDialogShowing()) {
                return;
            }

            if (((Activity) context) != null && ((Activity) context).isFinishing()) {
                return;
            }

            progressDialog = new Dialog(context, R.style.StyledDialog);
            if (progressDialog != null) {
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.dialog_flight_progressbar);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isProgressDialogShowing() {
        if (progressDialog != null) {
            return progressDialog.isShowing();
        }
        return false;
    }

    public static void closeProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isInternetConnected(Context context) {

        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static int getDipsFromPixel(Context context, float pixels) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static void printHashKey(Context mContext) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                LogUtils.e("", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("", "printHashKey():" + e.getMessage());
        } catch (Exception e) {
            LogUtils.e("", "printHashKey():" + e.getMessage());
        }
    }

    public static String Sha512(String input) {
        MessageDigest objSHA = null;
        try {
            objSHA = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bytSHA = objSHA.digest(input.getBytes());
        BigInteger intNumber = new BigInteger(1, bytSHA);
        String strHashCode = intNumber.toString(16);
        while (strHashCode.length() < 128) {
            strHashCode = "0" + strHashCode;
        }
        return strHashCode;
    }


    public static String MD5(String input) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
//            digest.update(input.getBytes("UTF-8"));
            digest.update(input.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));
            }
//            for (byte aMessageDigest : messageDigest) {
//                String h = Integer.toHexString(0xFF & aMessageDigest);
//                while (h.length() < 2)
//                    h = "0" + h;
//                hexString.append(h);
//            }
            LogUtils.e("", "MD5::" + hexString.toString() + " input::" + input);

            return hexString.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

//    public static String MD5(String string) {
//        byte[] hash;
//
//        try {
//            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Huh, MD5 should be supported?", e);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
//        }
//
//        StringBuilder hex = new StringBuilder(hash.length * 2);
//        for (byte b : hash) {
//            if ((b & 0xFF) < 0x10) {
//                hex.append("0");
//            }
//            hex.append(Integer.toHexString(b & 0xFF));
//        }
//        return hex.toString();
//    }

    public static String GetHash(int authKey, String string) {
        String sHash = null;
        switch (authKey) {
            case 1:
                sHash = MD5(MD5(string));
                break;
            case 2:
                try {
                    sHash = SHA1(MD5(string));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                sHash = SHA256(MD5(string));
                break;
            case 4:
                sHash = SHA512(MD5(string));
                break;
        }
        return sHash;
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static String SHA256(String input) {

        String hashtext = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashtext;
    }

    public static String SHA512(String input) {

        String hashtext = null;
        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-512");
//            byte[] messageDigest = md.digest(input.getBytes());
//            BigInteger no = new BigInteger(1, messageDigest);
//            hashtext = no.toString(16);
//            while (hashtext.length() < 32) {
//                hashtext = "0" + hashtext;
//            }

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }

            hashtext = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        LogUtils.e("", "SHA512::" + hashtext);

        return hashtext;
    }

    public static String getRandomString(int length) {
        String sChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        while (stringBuilder.length() < length) {    // length of the random string.
            int index = (int) (random.nextFloat() * sChars.length());
            stringBuilder.append(sChars.charAt(index));
        }
        String sRandomString = stringBuilder.toString();
        return sRandomString;
    }

    public static int getAuthKey() {
        int authKey;
        Random r = new Random();
        int random = r.nextInt(4 - 1 + 1) + 1;
        if (random > 4 && random < 1) {
            random = 3;
        }
        authKey = Integer.parseInt(String.valueOf(random));
        return authKey;
    }

    public static String getPassword(String mPassword) {
        LogUtils.e("", "mPassword::" + mPassword);
        String mRandomString = getRandomString(4);
        LogUtils.e("", "mRandomString::" + mRandomString);
        String mSha512 = SHA512(mPassword);
        String mPwd = MD5(mSha512);
        mPwd = mRandomString + mPwd + mRandomString;
        return mPwd;
    }

    public static String getToken() {
        String mOtapp2019 = "Otapp@2019";
        String mRandomString = getRandomString(4);
        LogUtils.e("", "mRandomString::" + mRandomString);
        String mToken = MD5(mOtapp2019);
        mToken = mRandomString + mToken + mRandomString;
        Log.d("Log ","Header "+mToken);
        return mToken;

    }

    public static String getCouponCodeKey(String mData) {

        String mSha512 = SHA512(mData);
        String mkey = MD5(mSha512);

        LogUtils.e("", mkey + " mkey mData code:" + mData);
        return mkey;
    }

    public static Bitmap getQRCode(String mBookingID) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(mBookingID, BarcodeFormat.QR_CODE, 256, 256);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setMint(Application mContext) {
        Mint.initAndStartSession(mContext, RestClient.MINT_KEY);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getFlightTime(String mTime) {
        if (!TextUtils.isEmpty(mTime)) {
            try {
                Date date = DateFormate.sdfAirportServerDate.parse(mTime);
                if (date != null) {
                    String formattedDate = DateFormate.sdfEvent24Time.format(date);
                    return formattedDate;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getFlight12Time(String mTime) {
        if (!TextUtils.isEmpty(mTime)) {
            try {
                Date date = DateFormate.sdfAirportServerDate.parse(mTime);
                if (date != null) {
                    String formattedDate = DateFormate.sdfEvent12Time.format(date);
                    return formattedDate;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getFlightDate(String mTime) {
        if (!TextUtils.isEmpty(mTime)) {
            try {
                Date date = DateFormate.sdfAirportServerDate.parse(mTime);
                if (date != null) {
                    String formattedDate = DateFormate.sdfParkDisplayDate.format(date);
                    return formattedDate;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getFlightTripDate(String mTime) {
        if (!TextUtils.isEmpty(mTime)) {
            try {
                Date date = DateFormate.sdfAirportServerDate.parse(mTime);
                if (date != null) {
                    String formattedDate = DateFormate.sdfFlightDisplayDate.format(date);
                    return formattedDate;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public static String getTimeDifference(Date startDate, Date endDate) {

        long different = endDate.getTime() - startDate.getTime();
//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//
//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//
//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;

        long secondsInMilli = different / 1000;
        long elapsedHours = secondsInMilli / 3600;
        long elapsedMinutes = (secondsInMilli % 3600) / 60;


        StringBuilder strBuilder = new StringBuilder();
//        if (elapsedDays > 0) {
//            strBuilder.append(elapsedDays + "d ");
//        }

        if (elapsedHours > 0) {

            strBuilder.append(elapsedHours + "h ");
        }

        if (elapsedMinutes > 0) {
            strBuilder.append(elapsedMinutes + "m");
        }

        long elapsedSeconds = different / secondsInMilli;

        if (strBuilder.length() > 0) {
            return strBuilder.toString();
        } else {
            return "";
        }

    }

    public static String getTimeDifference(long different) {


//        long secondsInMilli = different / 1000;
        long secondsInMilli = different;
        long elapsedHours = secondsInMilli / 3600;
        long elapsedMinutes = (secondsInMilli % 3600) / 60;


        StringBuilder strBuilder = new StringBuilder();
//        if (elapsedDays > 0) {
//            strBuilder.append(elapsedDays + "d ");
//        }

        if (elapsedHours > 0) {

            strBuilder.append(elapsedHours + "h ");
        }

        if (elapsedMinutes > 0) {
            strBuilder.append(elapsedMinutes + "m");
        }

        long elapsedSeconds = different / secondsInMilli;

        if (strBuilder.length() > 0) {
            return strBuilder.toString();
        } else {
            return "";
        }

    }

    public static int getYearTimeDifference(Date startDate, Date endDate) {

        Calendar calStart = Calendar.getInstance(Locale.getDefault());
        calStart.setTime(startDate);
        Calendar calEnd = Calendar.getInstance(Locale.getDefault());
        calEnd.setTime(endDate);
        int diff = calEnd.get(Calendar.YEAR) - calStart.get(Calendar.YEAR);
        if (calStart.get(Calendar.MONTH) > calEnd.get(Calendar.MONTH) ||
                (calStart.get(Calendar.MONTH) == calEnd.get(Calendar.MONTH) && calStart.get(Calendar.DATE) > calEnd.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static String getBase64FromBitmap(Bitmap bitmap) {

        String encodedImage = "";

        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encImage = Base64.encodeToString(b, Base64.DEFAULT);

            return encImage;
        }

        return encodedImage;
    }

    public static void showDialog(Context activity, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.app_name));
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.okay, null);

        AlertDialog alert1 = builder.create();
        alert1.show();

    }

    public static String setPrice(float total) {
        return Constants.mPriceFormate.format(total);
    }

    public static String getDatePostfix(int date) {
        switch (date) {
            case 1:
            case 21:
            case 31:
                return "st";
            case 2:
            case 22:
                return "nd";
            case 3:
            case 23:
                return "rd";
            default:
                return "th";
        }
    }

    public static int getUserCountryPosition(Context mContext) {
        int mPos = 0;
        if (!TextUtils.isEmpty(MyPref.getPref(mContext, MyPref.PREF_USER_COUNTRY_CODE, MyPref.PREF_DEFAULT_COUNTRY_CODE)) && Otapp.mCountryCodePojoList != null) {
            for (int i = 0; i < Otapp.mCountryCodePojoList.size(); i++) {
                LogUtils.e("", "getUserCountryPosition::" + Otapp.mCountryCodePojoList.get(i).code + " " + MyPref.getPref(mContext, MyPref.PREF_USER_COUNTRY_CODE, MyPref.PREF_DEFAULT_COUNTRY_CODE)
                        + " result:" + Otapp.mCountryCodePojoList.get(i).code.equals(MyPref.getPref(mContext, MyPref.PREF_USER_COUNTRY_CODE, MyPref.PREF_DEFAULT_COUNTRY_CODE)));
                if (Otapp.mCountryCodePojoList.get(i).code.equals(MyPref.getPref(mContext, MyPref.PREF_USER_COUNTRY_CODE, MyPref.PREF_DEFAULT_COUNTRY_CODE))) {
                    mPos = i;
                    break;
                }
            }
        }
        return mPos;
    }

    public static int getCountryPosition(Context mContext) {
        int mPos = 0;
        if (!TextUtils.isEmpty(MyPref.getPref(mContext, MyPref.PREF_SELECTED_COUNTRY_CODE, MyPref.PREF_DEFAULT_COUNTRY_CODE)) && Otapp.mCountryCodePojoList != null) {
            for (int i = 0; i < Otapp.mCountryCodePojoList.size(); i++) {
                LogUtils.e("", "getCountryPosition::" + Otapp.mCountryCodePojoList.get(i).code + " " + MyPref.getPref(mContext, MyPref.PREF_SELECTED_COUNTRY_CODE, MyPref.PREF_DEFAULT_COUNTRY_CODE)
                        + " result:" + Otapp.mCountryCodePojoList.get(i).code.equals(MyPref.getPref(mContext, MyPref.PREF_SELECTED_COUNTRY_CODE, MyPref.PREF_DEFAULT_COUNTRY_CODE)));
                if (Otapp.mCountryCodePojoList.get(i).code.equals(MyPref.getPref(mContext, MyPref.PREF_SELECTED_COUNTRY_CODE, MyPref.PREF_DEFAULT_COUNTRY_CODE))) {
                    mPos = i;
                    break;
                }
            }
        }
        LogUtils.e("", "mPos::" + mPos);
        return mPos;
    }

    public static int getPreviousCountryPosition(Context mContext, String mPreviousCountryCode) {
        int mPos = 0;
        if (!TextUtils.isEmpty(mPreviousCountryCode) && Otapp.mCountryCodePojoList != null) {
            for (int i = 0; i < Otapp.mCountryCodePojoList.size(); i++) {
                if (Otapp.mCountryCodePojoList.get(i).code.equals(mPreviousCountryCode)) {
                    mPos = i;
                    break;
                }
            }
        }
        return mPos;
    }


}
