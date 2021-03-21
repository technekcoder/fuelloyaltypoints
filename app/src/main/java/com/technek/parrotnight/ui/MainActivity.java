package com.technek.parrotnight.ui;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import net.sqlcipher.database.SQLiteDatabase;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.technek.parrotnight.R;
import com.technek.parrotnight.cryptography.AESCrypt;
import com.technek.parrotnight.database.DatabaseAccess;
import com.technek.parrotnight.database.MaliplusDatabaseHelper;
import com.technek.parrotnight.http_network.NetworkCalls;
import com.technek.parrotnight.models.User;
import com.technek.parrotnight.util.Config;
import com.technek.parrotnight.util.ContentProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Build.*;

public class MainActivity extends AppCompatActivity {
    String str_username;
    String str_password;
    private Button Login, Register;
    private TextView Forgot;
    int REPEAT_COUNT = 0;
    TextView resetapp;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private SharedPreferences sharedPrefs;
    public SharedPreferences sharedPrefs_1;
    private static final String USERNAME_KEY = "CURRENT_USER";
    private static final String SHARED_PREFS = "sharedPrefs";

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_1;

    private EditText Username, Password;
    private MaliplusDatabaseHelper dbHelper;
    private String notificationId = "hello";
    private CheckBox chkboxautologin;
    private Context context;
    public static final String HASH_PHRASE = "#PSL2018";
    private DatabaseAccess dbAccess;
    private PopupWindow pw;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    Dialog codeDialog;
    private static final String TAG = "Login";
    public static final String RESET_PASSWORD_USERNAME = "RESET PASSWORD USERNAME";

    AlertDialog dialog;
    ContentProvider alpha_cp;
    NotificationManager notificationManager;

    private ProgressDialog progressDialog;
    public static final String CHANNEL_ID = "com.example.newton.myapplication";
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SQLiteDatabase.loadLibs(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        String codde = decrypt("gUN2LcOtL1mPX0LvLSaORA==");
        String codde_url = decrypt("JVSva1pA1Wb3iWBy6dROwiuvu7RY3kVz+TfqeWs0RaQTJWfV3S4wOuppfwkgsW8I");

        Log.d("Decrypted Code: ", codde);
        Log.d("Decrypted Code: ", codde_url);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        intwidgets();

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("URL") != null) {
            String url = getIntent().getExtras().getString("URL");
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            return;
        }
        checkAndRequestPermissions();

         SharedPreferences sharedpreferences;
        sharedpreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        String login = sharedpreferences.getString(USERNAME_KEY, null);
        if (login != null) {
            nextActivity(malimain.class);        }
        dbAccess = DatabaseAccess.getInstance(context);
        // popup();
        Log.d("NULL_T", "subscription");
        //startService(new Intent(MainActivity.this, Subscriptiontimer.class));

        if (chkboxautologin.isChecked()) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            StringBuilder info = new StringBuilder();
            info.append("notifications:" + sharedPreferences.getBoolean("notifications", false));
        }
        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getReadableDatabase(HASH_PHRASE);

                String name = Username.getText().toString();
                String password = Password.getText().toString();
                if (name.isEmpty() || password.isEmpty()) {
                    Username.setError("*Required");
                } else {
                    Cursor data = dbHelper.getData();
                    authentication();
                }
            }
        });
        resetapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedpreferences.contains("CUSTOMER_URL")) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("CUSTOMER_URL");
                    editor.apply();
                }
              //  viewCodeDialog();
            }
        });

        Forgot.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, forgot_password.class)));
    }

    private void intwidgets() {
        context = this;
        alpha_cp = new ContentProvider(context);

        sharedPrefs = context.getSharedPreferences(getString(R.string.user_details), Context.MODE_PRIVATE);
        sharedPrefs_1 = context.getSharedPreferences("LICENCES", Context.MODE_PRIVATE);

        editor = sharedPrefs.edit();
        editor_1 = sharedPrefs_1.edit();

        if (sharedPrefs_1.getString("CUSTOMER_CODE", null) == null) {
           //  viewCodeDialog();
        } else {
            defineAccessCode(decrypt(sharedPrefs_1.getString("CUSTOMER_CODE", null)));
        }
        Login = findViewById(R.id.login);
        Forgot = findViewById(R.id.forgot);
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.pass);
        Login = findViewById(R.id.login);
        dbHelper = new MaliplusDatabaseHelper(this);
        chkboxautologin = findViewById(R.id.ckboxremember);
        resetapp=findViewById(R.id.resetappcode);
    }


    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            //checkDB = SQLiteDatabase.openDatabase(maliplus.db, null,
            //  SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
        }
        return checkDB != null;
    }

    private void authentication() {
        str_username = Username.getText().toString();
        str_password = Password.getText().toString();

       // setProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CLIENT_LOG_IN, response -> {
            if (response.contains("userdetails") || response.toLowerCase().contains("reset_pass")) {
                verifyDevice();
                responseMethod(response);
            } else {
                Toast.makeText(context, "Username or Password Incorrect." + response, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
          //  hideProgressDialog();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap<>();
                map.put(Config.SU_USER_NAME, str_username);
                map.put(Config.SU_PASSWORD, encrypt(str_password));
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static String encrypt(String string) {

        String encryptedMsg = "";
        try {
            encryptedMsg = AESCrypt.encrypt(Config.ENCRYPT_SALT, string);
        } catch (GeneralSecurityException e) {
        }
        return encryptedMsg;
    }
    private void responseMethod(String json_string) {
        if (!json_string.contains("BRANCH_CODE")) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(json_string);
            JSONArray result;

            if (json_string.contains("reset_pass")) {
                result = jsonObject.getJSONArray("reset_pass");
            } else {
                result = jsonObject.getJSONArray("userdetails");
            }
            JSONObject item = result.getJSONObject(0);

            String userName = item.getString("USERNAME");
            String fullName = item.getString("NAME");
            String branchCode = item.getString("BRANCH_CODE");
            String resetPassword = item.getString("FIRST_TIME");

            Config.loggedInUser = new User(userName, fullName, branchCode);

            if (resetPassword.equalsIgnoreCase("Y")) {
                Intent intent = new Intent(context, ResetPassword.class);
				        intent.putExtra(MainActivity.RESET_PASSWORD_USERNAME, userName);
                startActivity(intent);
            } else {
                if (dbAccess.checkDataExists()) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(USERNAME_KEY, userName);
                    editor.apply();
                    nextActivity(malimain.class);
                }
                else{
                    Intent i = new Intent(MainActivity.this, MaliplusUpdater.class);
                    startActivity(i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void nextActivity(Class nextClass) {
        overridePendingTransition(0, 0);
        startActivity(new Intent(context, nextClass));
        overridePendingTransition(0, 0);
        finish();
    }
    private void popup() {
        Context context = this;
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, MaliplusUpdater.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(System.currentTimeMillis());
        cal.clear();
        cal.set(2018, 12, 15, 9, 30);

        //long currentTime = System.currentTimeMillis();
        //long oneMinute = 1 * 1000;
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                pendingIntent);
        long value = getIntent().getLongExtra("subscriptionend", Long.parseLong("0"));
        if (value >= 61) {
            Login.setEnabled(false);
            Login.setClickable(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Subscription over ,Update to Licenced version");
            builder.setIcon(R.mipmap.logo);
            builder.setCancelable(false);
            builder.setMessage("New version available, select update to update our app")
                    .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final String appName = getPackageName();

                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("www.primesoft.co.ke" + appName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=maliplus")));
                            }

                            finish();

                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Log.d("NULL_T", "subscription still active");
        }
    }

    public void verifyDevice() {
        String deviceName = MODEL;
        String deviceMan = MANUFACTURER;
       // PairValues<String> imeis = capturedeviceid();
        String imeis = capturedeviceid(context);

        Log.d("EXPECTED_URL_2", "" + imeis);

        if (!new Config(context).DEVICE_ID.contains("null")) {

            Log.d("EXPECTED_URL_2", deviceName+ deviceMan+ new Config(context).DEVICE_ID);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, new Config(context).DEVICE_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("RESPONSE", "rte4tt" + response);

                            if (response.contains("failed")) {
                                // progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Unauthorized Device, Contact HQ ", Toast.LENGTH_LONG).show();
                            } else {
                                if (!Username.getText().toString().isEmpty()) {
                                    //authentication();
                                    //signIn();
                                } else {
                                    Toast.makeText(context, "Input Credentials", Toast.LENGTH_SHORT).show();
                                    //  progressDialog.dismiss();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Check Your Internet Connection..", Toast.LENGTH_LONG).show();
                            Log.d("RESPONSE", error.toString());
                            progressDialog.dismiss();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("IME1I", imeis);
                    map.put("DEVICE_NAME", deviceName);
                    map.put("DEVICE_NAME",deviceMan);

                    return map;
                }
            };

            RequestQueue requestQueue1 = Volley.newRequestQueue(context);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue1.add(stringRequest);

        } else {
            defineAccessCode(decrypt(sharedPrefs_1.getString("CUSTOMER_CODE", null)));
            REPEAT_COUNT = 1;
        }

    }


    public void viewCodeDialog() {
        codeDialog = new Dialog(context);
        codeDialog.setContentView(R.layout.code_dialog);
        codeDialog.setCancelable(false);

        final EditText et_licence_code = codeDialog.findViewById(R.id.et_licence_code);
        FloatingActionButton fab_approve_code = codeDialog.findViewById(R.id.fab_approve_code);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(codeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.8f;

        fab_approve_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
String code=et_licence_code.getText().toString().trim();
                defineAccessCode(et_licence_code.getText().toString().trim());
                Toast.makeText(getApplicationContext(), "Working.."+code, Toast.LENGTH_LONG).show();


            }
        });
        codeDialog.show();
        codeDialog.getWindow().setAttributes(lp);
    }


    @Override
    public void onBackPressed() {
        finish();
    }


    public String encrypt_new(String string) {

        String encryptedMsg = "";
        try {
            encryptedMsg = AESCrypt.encrypt(Config.SALT, string);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return encryptedMsg;
    }

    public static String capturedeviceid(Context context) {
        try {

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei;

            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei = telephonyManager.getImei();
                } else {
                    imei = telephonyManager.getDeviceId();
                }

                if (imei != null && !imei.isEmpty()) {
                    return imei;
                } else {
                    return android.os.Build.SERIAL;
                }
            }

        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            return errors.toString();
//e.printStackTrace();
        }

        return "not_found";
    }

    public void verifyUser(final String str_input_name) {
        Log.d("USER", new Config(context).VERIFY_USER);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, new Config(context).VERIFY_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("USER_DETAILS", response);
                        if (response.contains("USER_NUMBER")) {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("user");

                                int i = 0;
                                while (i < result.length()) {
                                    JSONObject aUser = result.getJSONObject(i);

                                    editor.putString(getString(R.string.USER_NUMBER), aUser.getString("USER_NUMBER"));
                                    editor.putString(getString(R.string.USER_PROFILE_NAME), aUser.getString("PROFILE_NAME"));
                                    editor.putString(getString(R.string.USER_GROUP_CODE), aUser.getString("GROUP_CODE"));
                                    //restrict account ->  location Access
                                    editor.putString(getString(R.string.RESTRICT_ACCOUNT), aUser.getString("RESTRICT_LOCATION"));
                                    editor.putString(getString(R.string.USER_CASHACCOUNT), aUser.getString("LOCATION_ACCESS"));


                                    //restrict location -> work location
                                    editor.putString(getString(R.string.RESTRICT_LOCATION), aUser.getString("RESTRICT_ACCOUNT"));

                                    editor.putString(getString(R.string.WORK_LOCATION), aUser.getString("WORK_LOCATION"));
                                    //restrict withdrawal -> RESTRICT WITHDRAWAL
                                    editor.putString(getString(R.string.RESTRICT_WITHDRAWAL), aUser.getString("RESTRICT_WITHDRAWAL"));

                                    editor.commit();

                                    i++;
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            authentication();
                        } else {
                            Toast.makeText(getApplicationContext(), "Incorrect Username, Contact HQ ", Toast.LENGTH_LONG).show();
                            Log.d("USER_DETAILS", response);
                            //  progressDialog.dismiss();
                        }
                        //    progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection..", Toast.LENGTH_LONG).show();
                        Log.d("RESPONSE", error.toString());
                        //progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("USER_NUMBER", str_input_name);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    public void defineAccessCode(final String str_input_name) {
        String code = encrypt_code(str_input_name);
        Map<String, String> maps = new HashMap<>();
        Login.setEnabled(true);
        maps.put("CODE", code);
        String verification_code="x4";
        if(code == verification_code){
            alpha_cp.licenceEditor.putString("CUSTOMER_CODE", code);
            Toast.makeText(context, "Code Verified", Toast.LENGTH_SHORT).show();
        Login.setEnabled(true);
        codeDialog.dismiss();}
        NetworkCalls.getInstance().initCall(context, getString(R.string.ACCESS_CODE), maps, (response) -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray("licences");
                Log.d("EXPECT PRINT", response);


                if (!result.isNull(0)) {
                    Login.setEnabled(true);
                    int i = 0;
                    while (i < result.length()) {
                        JSONObject aUser = result.getJSONObject(i);
/*                                  alpha_cp.userDetailsEditor_1.putString("STATUS", aUser.getString("STATUS"));
                                alpha_cp.userDetailsEditor_1.putString("RESELLER_TYPE", aUser.getString("RESELLER_TYPE"));
                                alpha_cp.userDetailsEditor_1.putString("RESELLER_NAME", aUser.getString("RESELLER_NAME"));
                                alpha_cp.userDetailsEditor_1.putString("SALE_VALUE", aUser.getString("SALE_VALUE"));
                                alpha_cp.userDetailsEditor_1.putString("PAID_AMOUNT", aUser.getString("PAID_AMOUNT"));
                                alpha_cp.userDetailsEditor_1.putString("RESELLER_COMMISSION", aUser.getString("RESELLER_COMMISSION"));
                                alpha_cp.userDetailsEditor_1.putString("COMMISION_PAID", aUser.getString("COMMISION_PAID"));
                                alpha_cp.userDetailsEditor_1.putString("DATE_CREATED", aUser.getString("DATE_CREATED"));*/
                        alpha_cp.licenceEditor.putString("STATUS", aUser.getString("STATUS"));
                        //alpha_cp.licenceEditor.putString("CUSTOMER_CODE", aUser.getString("CUSTOMER_CODE"));

                        alpha_cp.licenceEditor.putString("DATE_EXPIRY", aUser.getString("DATE_EXPIRY"));
                        alpha_cp.licenceEditor.putString("OPTION_SUSPEND", aUser.getString("OPTION_SUSPEND"));
                        alpha_cp.licenceEditor.putString("DAT_SUSPEND", aUser.getString("DAT_SUSPEND"));
                        alpha_cp.licenceEditor.putString("OPTION_CHECK", aUser.getString("OPTION_CHECK"));
                        alpha_cp.licenceEditor.putString("CUSTOMER_URL", decrypt(aUser.getString("CUSTOMER_URL")));
                        alpha_cp.licenceEditor.putString("CUSTOMER_NAME", aUser.getString("CUSTOMER_NAME"));
                        alpha_cp.licenceEditor.putString("DSN_NAME", aUser.getString("DSN_NAME"));
                        alpha_cp.licenceEditor.putString("DB_USER", aUser.getString("DB_USER"));
                        alpha_cp.licenceEditor.putString("DB_PWD", aUser.getString("DB_PWD"));
                        alpha_cp.licenceEditor.putString("DESCRIPTION", aUser.getString("DESCRIPTION"));

                        alpha_cp.licenceEditor.commit();

                        Log.d("EXPECT PRINT", alpha_cp.spLicences.getString("CUSTOMER_NAME", null));
                        Log.d("EXPECT PRINT", alpha_cp.spLicences.getString("CUSTOMER_URL", null));

                        i++;
                    }
                    //new Config(context).URL_STRING = alpha_cp.spLicences.getString("CUSTOMER_URL", null);

                   if (codeDialog != null) {
                        Toast.makeText(context, "Code Verified", Toast.LENGTH_SHORT).show();
                        Login.setEnabled(true);
                        codeDialog.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unregistered Code: Try Again", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.d("JSON_EXXCEPTION", e.getMessage());
                Toast.makeText(getApplicationContext(), "Unregistered Code: Try Again", Toast.LENGTH_LONG).show();
                if (codeDialog != null) {
                    codeDialog.dismiss();
                }
                //viewCodeDialog();
                e.printStackTrace();
            }
        });
    }
    public String encrypt_pass(String string) {

        String encryptedMsg = "";

        try {
            encryptedMsg = AESCrypt.encrypt(Config.SALT, string);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return encryptedMsg;
    }

    public String decrypt(String string) {
        String decryptedMsg = "";
        try {
            Log.d("EXPECT PRINT",decryptedMsg);

            decryptedMsg = AESCrypt.decrypt(Config.CODE, string);
            Log.d("EXPECT PRINT",decryptedMsg);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return decryptedMsg;
    }
        public void setProgressDialog() {
        Login.setEnabled(false);

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;

        TextView tvText = new TextView(this);
        tvText.setText("Logging in ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(ll);

        dialog = builder.create();
        dialog.show();

        Window window = dialog.getWindow();

        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
    }

    private void hideProgressDialog() {
        Login.setEnabled(true);
       // dialog.hide();
    }
        public String encrypt_code(String string) {

            String encryptedMsg = "";
            try {
                encryptedMsg = AESCrypt.encrypt(Config.CODE, string);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            return encryptedMsg;
        }
    private boolean checkAndRequestPermissions() {
        int telephonePermision = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionRecordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);


        List<String> listPermissionsNeeded = new ArrayList<>();

        if (telephonePermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        /*if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionRecordAudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }*/
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}