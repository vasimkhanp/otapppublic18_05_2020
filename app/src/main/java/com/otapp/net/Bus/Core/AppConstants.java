package com.otapp.net.Bus.Core;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class AppConstants {


    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = AppConstants.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = AppConstants.getNetworkInfo(context);
        return (info != null && info.isConnected() && AppConstants.isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
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

    public static String getDeviceName(Context context) {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            //return capitalize(model);
            return model;
        }
        Log.d("Log", "Manufacturer : " + manufacturer);
        Log.d("Log", "Device Model : " + model);
//        Toast.makeText(context, "Device : " + model + manufacturer, Toast.LENGTH_SHORT).show();
        // return capitalize(manufacturer) + " " + model;
        return manufacturer;
    }

    public static String getRandomNumber(int length) {
        String sChars = "1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        while (stringBuilder.length() < length) {    // length of the random string.
            int index = (int) (random.nextFloat() * sChars.length());
            stringBuilder.append(sChars.charAt(index));
        }
        String sRandomString = stringBuilder.toString();
        return sRandomString;
    }

    public static class URL {
        public static String URL = "";  //  api url
    }

    public static class IntentKeys {
        public static String AGENT = "AGENT";
        public static String TYPE = "TYPE";
        public static String STATION_LIST = "STATION_LIST";
        public static String BUSES_LIST = "BUSES_LIST";
        public static String SEARCH_BUS = "SEARCH_BUS";
        public static String BUS = "BUS";
        public static String RETURN_BUS = "RETURN_BUS";
        public static String KEY = "KEY";
        public static final String FARE = "FARE";
        public static final String SELECTED_SEAT = "SELECTED_SEAT";
        public static final String SELECTED_RETURN_SEAT = "SELECTED_RETURN_SEAT";
        public static final String SEAT_MAP = "SEAT_MAP";
        public static final String TICKET_DETAILS = "TICKET_DETAILS";
        public static final String STATEMENT = "STATEMENT";
        public static final String STATEMENT_RECORDS = "STATEMENT_RECORDS";
        public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
        public static final String TOTAL_TICKET_AMOUNT = "TOTAL_TICKET_AMOUNT";
        public static final String TOTAL_CARGO_AMOUNT = "TOTAL_CARGO_AMOUNT";
        public static final String DATE = "DATE";
        public static final String ROUTE = "ROUTE";
        public static final String COLLECTION = "COLLECTION";
        public static final String STOCK_OUT = "STOCK_OUT";
        public static final String STOCK = "STOCK";
        public static final String STOCK_RESPONSE = "STOCK_RESPONSE";
        public static final String UNO = "UNO";
        public static final String SELECTED_STOCK = "SELECTED_STOCK";
        public static final String LINK = "LINK";
        public static final String TITLE = "TITLE";
    }

    private static class ApiConfiguration {
      //  private static final String SERVER_URL = " https://bms.oacl.co.tz/api/Bus-Public-V1/";
        private static final String SERVER_URL = "https://www.managemybus.net/api/Bus-Public-V1/";
        private static final String NODE_SERVER_URL = "";
    }

    public static class ApiNames {
        public static final String API_URL = ApiConfiguration.SERVER_URL + "";
       // public static final String API_URL_TEST = "https://www.managemybus.net/api/nagi_test/";
        public static final String API_URL_TEST = "https://www.managemybus.net/api/Bus-Public-V1/";
        public static final String API_URL_NODE = ApiConfiguration.NODE_SERVER_URL + "";

        public static final String OWNER_ID = "1";

        public static final String LOGIN = "agent_login.php";
        public static final String AGENT_ROLES = "agent_roles.php";
        public static final String CONDUCTOR_ROUTES = "conductor_stnd_routes.php";
        //        public static final String STATIONS_LIST = "stations_list.php";
        public static final String STATIONS_LIST = "Search-Stations";
        public static final String SYNC_TICKETS = "sync_standing_tkts.php";
        public static final String LOAD_CARGO_BUS = "load_cargo_buses.php";
        public static final String CARGO_TICKET_BOOK = "book_cargo_ticket.php";
       // public static final String SEARCH_BUS = "search_bus.php";
        public static final String SEARCH_BUS = "Search-Buses.php";
        //public static final String SEAT_MAP = "seat_map.php";
        public static final String SEAT_MAP = "Seat-Map.php";
      //  public static final String PROCESS_SEAT = "process_seat.php";
        public static final String PROCESS_SEAT = "Process-Seats.php";
        public static final String GET_FARE ="Get-Fare";
        public static final String Reserve_Booking ="Reserve-Booking";

        public static final String BOOK_SEAT = "book_seat.php";
        public static final String PRINT_DUP_TKT = "print_dup_tkt.php";
        public static final String GET_AGENT_LIST_SUPERVISOR = "Get_Agent_List_Supervisor.php";
        public static final String GET_AGENT_STATEMENT = "Get_Agent_Statement.php";
        public static final String GET_BUS_INFO = "Get_Bus_Info.php";
        public static final String PASSENGER_LIST = "Passenger_List.php";
        public static final String CARGO_LIST = "Cargo_List.php";
        public static final String ACTIVATE_LOCKED_USERS = "Activate_Locked_Users.php";
        public static final String CHANGE_PASSWORD = "Change_Password.php";
        public static final String CHANGE_TRAN_PASSWORD = "Change_Tran_Password.php";
        public static final String GET_ROOMS_INFO = "Get_Rooms_info.php";
        public static final String GET_STOCK_INFO = "Get_Stock_info.php";
        public static final String GET_AVAIL_STOCK = "Get_Avail_Stock.php";
        public static final String SAVE_STOCK = "Save_Stock_Items.php";
        public static final String STOCK_OUT = "Stock_Out.php";
        public static final String ROUTE_BUS_SEARCH = "Route_Bus_Search.php";
        public static final String UPLOAD_FILE = "Upload_Exp_Inc_Files.php";
        public static final String SAVE_EXP_INC = "Save_Exp_Inc.php";

    }

    public static class Status {
        public static String ACTIVE = "1";
        public static String INACTIVE = "0";
        public static int SUCCESS = 200;
        public static int FAILED = 404;
        public static int SESSION_OUT = 201;
        public static int NOT_ACCESS = 401;
        public static String TRUE = "0";
        public static String FALSE = "1";
        public static String SYNCED = "0";
        public static String NOT_SYNCED = "1";
        public static int MAX = 202;
        public static int SEAT_BOOKED = 204;
        public static int INPROCESS = 203;

        public static String BOOKED = "0";
        public static String IP = "100.92.60.128";
        public static String IMEI = "864297044447016";
        public static String AVAILABLE = "1";
        public static String SELECTED = "2";
        public static String PROCESSING = "3";
        public static String APP_VERSION = "1.0";
        public static String OWNER_ID= "53";

        public static String AGENT = "0";
        public static String SUPERVISOR = "1";
        public static String CONDUCTOR = "2";
        public static String DRIVER = "3";
        public static String MECHANIC = "4";

        public static String TICKET__DETAILS_SHOW = "3.5";  //  Not showing

        public static int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 111;
        public static int GALLERY_IMAGE_REQUEST_CODE = 112;

    }

    public static class Keys {  //  Shared Preferences Keys
        public static String SELECTED_TAB = "com.smsipl.otappbus.selected_tab";
        public static String LANGUAGE = "com.smsipl.otappbus.language";
        public static String SELECTED_LANGUAGE = "com.smsipl.otappbus.selected_language";
        public static String LOGIN_STATUS = "com.smsipl.otappbus.login_status";
        public static String MAX_DATE = "com.smsipl.otappbus.max_date";
        public static String MAX_SEATS = "com.smsipl.otappbus.max_seats";
        public static String LOGIN_USERNAME = "com.smsipl.otappbus.login_username";
        public static String LOGIN_PASSWORD = "com.smsipl.otappbus.login_password";
        public static String SUB_ID = "com.smsipl.otappbus.subid";
        public static String SELECTED_BUS_ID = "com.smsipl.otappbus.selectedbusid";
        public static String AGENT_ID = "com.smsipl.otappbus.agent_id";
        public static String AGENT_NAME = "com.smsipl.otappbus.agent_name";
        public static String AGENT_CURRENCY = "com.smsipl.otappbus.agent_currency";
        public static String IS_GRANTED_FOR_TKT_SELLING = "com.smsipl.otappbus.is_granted_for_tkt_selling";
        public static String IS_GRANTED_FOR_STND_TKTS = "com.smsipl.otappbus.is_granted_for_stnd_tkts";
        public static String IS_GRANTED_FOR_CARGO_TKTS = "com.smsipl.otappbus.is_granted_for_cargo_tkts";
        public static String IS_GRANTED_FOR_STATEMENTS = "com.smsipl.otappbus.is_granted_for_statements";
        public static String IS_GRANTED_FOR_AGENT_SUMMARY = "com.smsipl.otappbus.is_granted_for_agent_summary";
        public static String IS_GRANTED_FOR_ASIGN_CONDUCTOR_BUS = "com.smsipl.otappbus.is_granted_for_asign_conductor_bus";
        public static String IS_GRANTED_FOR_ASIGN_DRIVER_BUS = "com.smsipl.otappbus.is_granted_for_asign_driver_bus";
        public static String IS_GRANTED_FOR_MECHANICH_BUS = "com.smsipl.otappbus.is_granted_for_mechanich_bus";
        public static String IS_GRANTED_TO_ACTIVATE_AGENT = "com.smsipl.otappbus.is_granted_to_activate_agent";
        public static String IS_GRANTED_FOR_PRNT_TKTS = "com.smsipl.otappbus.is_granted_for_prnt_tkts";
        public static String IS_GRANTED_FOR_PSNGR_LIST = "com.smsipl.otappbus.is_granted_for_psngr_list";
        public static String IS_GRANTED_FOR_CARGO_LIST = "com.smsipl.otappbus.is_granted_for_cargo_list";
        public static String IS_GRANTED_FOR_STOCK_PRINT = "com.smsipl.otappbus.is_granted_for_stock_print";
        public static String IS_GRANTED_FOR_STOCK_IN = "com.smsipl.otappbus.is_granted_for_stock_in";
        public static String IS_GRANTED_FOR_STOCK_OUT = "com.smsipl.otappbus.is_granted_for_stock_out";
        public static String IS_GRANTED_FOR_ADD_INCOME = "com.smsipl.otappbus.is_granted_for_add_income";
        public static String IS_GRANTED_FOR_ADD_EXPENSES = "com.smsipl.otappbus.is_granted_for_add_expenses";
        public static String AGENT_FLOAT_BAL = "com.smsipl.otappbus.agent_float_bal";
        public static String KEY = "com.smsipl.otappbus.key";
        public static String HEADER = "com.smsipl.otappbus.header";
        public static String FOOTER = "com.smsipl.otappbus.footer";
        public static String USERS_NAME = "com.smsipl.otappbus.users_name";
        public static String USERS_EMAIL = "com.smsipl.otappbus.users_email";
        public static String USERS_MOBILE = "com.smsipl.otappbus.users_mobile";
        public static String USERS_MOBILE_IMEI = "com.smsipl.otappbus.users_imei";
        public static String IS_LOGGED_IN = "com.smsipl.otappbus.is_logged_in";
        public static String TIME_IS_00 = "com.smsipl.otappbus.00:00:00";
        public static String TIME_IS_04 = "com.smsipl.otappbus.04:00:00";
        public static String TIME_IS_08 = "com.smsipl.otappbus.08:00:00";
        public static String TIME_IS_12 = "com.smsipl.otappbus.12:00:00";
        public static String TIME_IS_16 = "com.smsipl.otappbus.16:00:00";
        public static String TIME_IS_20 = "com.smsipl.otappbus.20:00:00";

        public static  boolean isReturnBus=false;

    }

    public static class Formats {
        public static String DATE_TIME_FOR_IMAGE = "dd-MMM-yyyy_HHmmss";
        public static String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[@!&*#$%()])(?=.*[a-z])(?=.*[A-Z]).{4,12})";
        public static String DATE_FORMAT = "yyyy-MM-dd";
        public static String TIME_FORMAT = "hh:mm:ss a";
        public static String TIME_FORMAT_24 = "HH:mm:ss";
        public static String DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss a";
        public static String USER_DATE_FORMAT = "dd-MMM-yyyy";
        public static String DATE_TIME_FORMAT_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";


        /*
         *   Password Pattern Details
         *   (            Start of group
         *   (?=.*\d)	 must contains one digit from 0-9
         *   (?=.*[a-z])  must contains one lowercase characters
         *   (?=.*[A-Z])  must contains one uppercase characters
         *   (?=.*[@#$%]) must contains one special symbols in the list "@#$%"    //  Not Used
         *   .            match anything with previous condition checking
         *   {8,20}       length at least 8 characters and maximum of 20
         *   )            End of group
         *
         */

        public static String getStringInNumberFormat(double dNumber) {
            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(3);
            formatter.setMaximumFractionDigits(3);
            return formatter.format(dNumber);
        }

        public static String getStringInCCNumberFormat(double dNumber) {
            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(5);
            formatter.setMaximumFractionDigits(5);
            return formatter.format(dNumber);
        }

        public static String getDate(String sDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_SERVER);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            String sConvertDate = "";
            try {
                date = simpleDateFormat.parse(sDate);
                simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
                sConvertDate = simpleDateFormat.format(date);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return sConvertDate;
        }

        public static String getServerDate(String sDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(USER_DATE_FORMAT);
            Date date = null;
            String sConvertDate = "";
            try {
                date = simpleDateFormat.parse(sDate);
                simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
                sConvertDate = simpleDateFormat.format(date);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return sConvertDate;
        }

        public static String getUserDate(String sDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date date = null;
            String sConvertDate = "";
            try {
                date = simpleDateFormat.parse(sDate);
                simpleDateFormat = new SimpleDateFormat(USER_DATE_FORMAT);
                sConvertDate = simpleDateFormat.format(date);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return sConvertDate;
        }


        public static String getTime(String sDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_SERVER);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String sTime = "";
            Date date = null;
            try {
                date = simpleDateFormat.parse(sDate);
                simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
                sTime = simpleDateFormat.format(date);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return sTime;
        }

        public static String getDateTime(String sDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_SERVER);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            String sConvertDate = "";
            try {
                date = simpleDateFormat.parse(sDate);
                simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
                sConvertDate = simpleDateFormat.format(date);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            return sConvertDate;
        }
    }

    //  Get & Set Values in Shared Preferences
    public static class Preferences {

        // Boolean Preferences (Checkbox)
        public static boolean getBooleanPreference(Context context, String key, boolean defaultValue) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getBoolean(key, defaultValue);
        }

        public static void setBooleanPreferences(Context context, String key, boolean value) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            SharedPreferences.Editor preferenceEditor = preferences.edit();
            preferenceEditor.putBoolean(key, value);
            preferenceEditor.apply();
        }

        public static String getStringPreference(Context context, String key) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString(key, null);
        }

        public static String getRecentRatePreference(Context context, String key) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString(key, "0");
        }

        public static void setStringPreferences(Context context, String key, String value) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

            SharedPreferences.Editor preferenceEditor = preferences.edit();
            preferenceEditor.putString(key, value);
            preferenceEditor.apply();
        }

        public static int getIntPreference(Context context, String key, int defValue) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getInt(key, defValue);
        }

        public static void setIntPreferences(Context context, String key, int value) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            SharedPreferences.Editor preferenceEditor = preferences.edit();
            preferenceEditor.putInt(key, value);
            preferenceEditor.apply();
        }
    }

    //  copy wallet address
    public static class Copy {
        public static void copyText(Context context, String sText) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("WalletAddress", sText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Wallet address has been copied",
                    Toast.LENGTH_LONG).show();
        }
    }

    //  copy Key
    public static class CopyKey {
        public static void copyText(Context context, String sText) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Key", sText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Authentication key has been copied, Please paste this key in Google Authenticator app", Toast.LENGTH_LONG).show();
        }
    }

    public static class Paste {

        public static String pasteText(Context context) {
            String pasteText = "";
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard.hasPrimaryClip() == true) {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                pasteText = item.getText().toString();
            } else {
                Toast.makeText(context, "Nothing to Paste", Toast.LENGTH_SHORT).show();
            }
            return pasteText;
        }

    }


    public static class Directories {
        public static String FOLDER_NAME = "Nagi";
    }

}
