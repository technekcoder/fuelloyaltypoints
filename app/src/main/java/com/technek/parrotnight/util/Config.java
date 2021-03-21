package com.technek.parrotnight.util;


import android.content.Context;

import com.technek.parrotnight.models.User;

public class Config  {

    private ContentProvider alpha_cp;
    private String CUSTOMER_URL;
    public static  String CLIENT_LOG_IN ;
    public static String FUEL_PRICES_URL ;
    public static String BARCODE_URL ;
    public static String FUEL_COMPANY_URL ;
    public static  String SAVE_TRANSACTION_SERVER ;
    public static String CUSTOMER_REGISTER ;
    public static  String RESET_PASSWORD_URL ;
    public static  String SAVE_TRANSACTION_REDEEM ;
    public static  String CUSTOMERS_URL ;
    public static  String SU_EMAIL ;
    public static  String SU_PASSWORD ;
    public static  String SU_USER_NAME ;
    public static  String SU_PHONE_NUMBER ;
    public static  String SU_BUSINESS_NAME ;
    public static  String SU_BUSINESS_LOCATION ;
   // public String FETCH_UPDATE="http://62.12.113.126:80/super_user/fetchUpdate.php" ;
    public String GET_SETTINGS ;
    public static  String TR_JSON_CODE ;

    public static  String ENCRYPT_SALT = "M45ER4T1";
    public static User loggedInUser;
    private static Config config = null;
    public static String URL_STRING ;
            //= "http://62.12.115.240:8080/fuel/";

    public Config(Context context) {
        //URL_STRING = "http://62.12.115.240:8080/fuel/";
       // URL_STRING = context.getSharedPreferences("LICENCES", Context.MODE_PRIVATE)
         //       .getString("CUSTOMER_URL", null);
    }
    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    private Config() {
        //"http://192.168.0.120:9090/ZANAS_API/index.php/
    }
    public void refreshInstance(Context ctx) {
        alpha_cp = new ContentProvider(ctx);
        //URL_STRING = alpha_cp.spLicences.getString("CUSTOMER_URL", null);
        URL_STRING = "http://192.168.100.36/fuel/";
        CLIENT_LOG_IN = URL_STRING + "client_log_in.php";
        FUEL_PRICES_URL = URL_STRING + "getfuelprices.php";
        BARCODE_URL = URL_STRING + "getCustomers.php";
        FUEL_COMPANY_URL = URL_STRING + "fetchFiscal.php";
        SAVE_TRANSACTION_SERVER = URL_STRING + "save_cash_order.php";
        CUSTOMER_REGISTER = URL_STRING + "insertNewCustomer.php";
        RESET_PASSWORD_URL = URL_STRING + "resetPassword.php";
        SAVE_TRANSACTION_REDEEM = URL_STRING + "save_redeem_transaction.php";
        CUSTOMERS_URL = URL_STRING + "customerlist.php";
        SU_EMAIL = "EMAIL";
        SU_PASSWORD = "PASSWORD";
        SU_USER_NAME = "USER_NAME";
        SU_PHONE_NUMBER = "PHONE_NUMBER";
        SU_BUSINESS_NAME = "BUSINESS_NAME";
        SU_BUSINESS_LOCATION = "BUSINESS_LOCATION";
        GET_SETTINGS = URL_STRING + "fetchSettings.php";
        TR_JSON_CODE = "JSON_TRANSACTIONS";
        ENCRYPT_SALT = "M45ER4T1";
        User loggedInUser;
    }
        //USER_URLS

    public String  VERIFY_USER = URL_STRING + "verifyUser.php";
        public String SIGN_IN = URL_STRING + "signIn.php";
        public String DEVICE_ID = URL_STRING + "getDeviceId.php";
        public String UPDATE_PASSWORD = URL_STRING + "updatePassword.php";
        public String CREATE_USER = URL_STRING + "createUser.php";
    public String FETCH_UPDATE="http://62.12.113.126:80/super_user/fetchUpdate.php" ;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static final String SALT = "ZANAS";
    public static String CODE = "primesoft";
        //request id
        public static final String CUSTOMER_ID = "id";
        public static String WORK_LOCATION = "branch_code";
        public static String LOCATION = "branch_code";


      //  public static String SALT = "primesoft";


}

