package com.otapp.net.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class AppConstants {

    public static final String apiKey="AIzaSyCTtNj7LfnPvHqfSZVtyzX0KxdM_7wc5yo";
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
    public static boolean isConnected(Context context) {
        NetworkInfo info = AppConstants.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }
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
    private static class ApiConfiguration {
      /*  private static final String SERVER_URL_2 = "https://www.managemyticket.net/android/api/";*/
        private static final String SERVER_URL = "https://www.managemyticket.net/api/";

      //  private static final String NODE_SERVER_URL = "http://13.59.222.160:3000/";
    }
    public static class ApiNames {
        public static final String API_URL = ApiConfiguration.SERVER_URL;
      /*  public static final String API_URL_2= ApiConfiguration.SERVER_URL_2;*/

        //public static final String API_URL_NODE = ApiConfiguration.NODE_SERVER_URL + "";

        public static  final String COUNTRY_CODE="/android/api/get_country_code_v2.php";
        public static  final String PROMO_CODE="/android/api/promo/Validate_Promo.php";
        public static  final String PROMO_CODE_VERIFY="/android/api/promo/Additional_verify.php";

        public static final String RESEND_CONFIRMATION="/android/api/resend_event_confirmation.php";

        public static  final String PROMO_ZERO_PAYMENT="Event/Event_Promo.php";



        public static  final String MOVIE_LIST="get_movie_list_v2.php";
        public static  final String MOVIE_DETAILS="get_movie_details.php";
        public static  final String SCREEM_TIME="get_movie_screen_time_v2.php";




        public static  final String EVENTS_LIST="Event/Get_Event_List_V1.php";
        public static  final String EVENTS_SEAT_PROCESSING="Event/Processing_Seats.php";
        public static  final String EVENTS_GET_FARE="Event/Get_Fare.php";
        public static  final String PROMO_TICKET_SUCCESS="Event/Event_Ticket.php";
        public static  final String NUMBER_AIRTEL ="Event/Event_Airtel.php";
        public static  final String NUMBER_TIGO ="Event/Event_Tigo.php";
        public static  final String NUMBER_MPESA ="Event/Event_Mpesa.php";
        public static  final String GET_TICKET_TYPE ="Event/Get_Ticket_Types.php";

        public static  final String CLEAR_SEATS="Event/Clear_Processing_Seats.php";

        public static  final String MY_BOOKINGS="Profile/My_Bookings.php";



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
        public static String AVAILABLE = "1";
        public static String SELECTED = "2";
        public static String PROCESSING = "3";
        public static String APP_VERSION = "1.0";

        public static String AGENT = "0";
        public static String SUPERVISOR = "1";
        public static String CONDUCTOR = "2";
        public static String DRIVER = "3";
        public static String MECHANIC = "4";



    }
    public static String agentId="5";
    public static String bookFrom="2";
    public static  String SERVICE_ID = "SERVICE_ID";
    public static final String BNDL_COUPON_RESPONSE = "BNDL_COUPON_RESPONSE";
    public static final int RC_EVENT_COUPON_CODE = 3002;
    public static final String BNDL_PAYMENT_TYPE = "BNDL_PAYMENT_TYPE";
    public static final String BNDL_PAYMENT_TYPE_DEBIT = "BNDL_PAYMENT_TYPE_DEBIT";
    public static final String BNDL_PAYMENT_TYPE_TIGO = "BNDL_PAYMENT_TYPE_TIGO";
    public static final String BNDL_PAYMENT_TYPE_MPESA = "BNDL_PAYMENT_TYPE_MPESA";
    public static final String BNDL_PAYMENT_TYPE_AIRTEL = "BNDL_PAYMENT_TYPE_AIRTEL";
    public static final String BNDL_TITLE = "BNDL_TITLE";
    public static final String BNDL_URL = "BNDL_URL";
    public static final String BNDL_MOVIE_RESPONSE = "BNDL_MOVIE_RESPONSE";
    public static final String BNDL_BUS_RESPONSE = "BNDL_BUS_RESPONSE";
    public static String sCompId="64";
    public static String sAgentId="938";
    public static String sIsFrom="2";


    public static String getYoutubeApiKey(){
        return apiKey;
    }


}
