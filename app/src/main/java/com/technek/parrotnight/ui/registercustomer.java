package com.technek.parrotnight.ui;

import android.Manifest;
import android.app.ProgressDialog;

import net.sqlcipher.database.SQLiteDatabase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technek.parrotnight.R;
import com.technek.parrotnight.database.DatabaseAccess;
import com.technek.parrotnight.database.MaliplusDatabaseHelper;
import com.technek.parrotnight.models.PairValues;
import com.technek.parrotnight.util.Misc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.technek.parrotnight.util.Config.CUSTOMER_REGISTER;

public class registercustomer extends AppCompatActivity {
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private Button btnRegister;
    private EditText username;
    private EditText txtFullname;
    private EditText txtKra;
    private EditText txtEmail;
    private EditText txtPhone, txt_qr_code;
    private EditText txtcustomerlocation;
    private EditText txtcompanyname;
    private Context context;
    private TextView tvLoginlink;
    private MaliplusDatabaseHelper mydb;
    public static final String HASH_PHRASE = "#PSL2018";
    private DatabaseAccess dbAccess;
    private ProgressDialog progressDialog1;
    private CheckBox termsofuse;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private String fetch_config;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );
    String display_qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SQLiteDatabase.loadLibs(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerlayout);
        initWidgets();
        context = this;
        sharedPrefs = getSharedPreferences(getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        fetch_config = sharedPrefs.getString(getString(R.string.fetch_config), null);
        setconfig(fetch_config);
        mydb = new MaliplusDatabaseHelper(this);
        dbAccess = DatabaseAccess.getInstance(context);
        progressDialog1 = new ProgressDialog(this);
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        Bundle bundle = getIntent().getExtras();
        display_qr_code = getIntent().getStringExtra("register_barcode");
        txt_qr_code.setText(display_qr_code);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

    }

    private void setconfig(String fetch_config) {
        try {
            JSONObject jsonObject = new JSONObject(fetch_config);
            JSONArray result = jsonObject.getJSONArray("company_settings");


            int i = 0;
            while (i < result.length()) {
                JSONObject dataobj = result.getJSONObject(i);

                String company_name = dataobj.getString("FULL_NAME");
                txtcompanyname.setText(company_name);
                i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private PairValues<String> capturedeviceid() {
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PairValues<String> imeis = new PairValues<>("", "");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);

            }

        }
        String imeiNumber1;
        String imeiNumber2 = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            imeiNumber1 = tManager.getDeviceId(0);
            imeiNumber2 = tManager.getDeviceId(1);
            Log.d("NULL_T", "" + imeiNumber1 + "" + imeiNumber2);
        } else {
            imeiNumber1 = tManager.getDeviceId();
            // Log.d("NULL_T", "" + Imei);
        }
        imeis.setValues(imeiNumber1, imeiNumber2);
        return imeis;
    }


    private void initWidgets() {
        txtFullname = findViewById(R.id.txtfullname);
        btnRegister = findViewById(R.id.btnRegister);
        txt_qr_code = findViewById(R.id.txtqr_code);
        txtEmail = findViewById(R.id.txtemail);
        txtPhone = findViewById(R.id.txtphone);
        txtcompanyname = findViewById(R.id.txtcompanyname);
        username = findViewById(R.id.txtusername);
        txtcustomerlocation = findViewById(R.id.txtcustomerlocation);
    }

    private void signup() {
        String namestr = (username.getText().toString());
        String fullnamestr = (txtFullname.getText().toString());
        String emailstr = (txtEmail.getText().toString());
        String companystr = (txtcompanyname).getText().toString();
        double phonestr = Misc.removeNull(txtPhone.getText().toString());
        String phonelength = txtPhone.getText().toString();
         if (companystr.isEmpty()) {
            txtcompanyname.requestFocus();
        }
//         else if (!EMAIL_ADDRESS_PATTERN.matcher(emailstr).matches()) {
//            txtEmail.setError("enter correct email pattern");
//            txtEmail.requestFocus();
//        }
         else if (namestr.isEmpty()) {
            txtFullname.setError("Required");
            txtFullname.requestFocus();
        } /*else if (emailstr.isEmpty()) {
            txtEmail.setError("Required");
            txtEmail.requestFocus();
        } */
        else if (phonelength.length()<9) {
            txtPhone.setError("too short\n verify digits");
            txtPhone.requestFocus();
        } else {
            UsernameAvailable();
        }
    }

    private void UsernameAvailable() {
        SQLiteDatabase database = mydb.getReadableDatabase(HASH_PHRASE);

        Cursor c = database.rawQuery("SELECT * FROM customer_details WHERE username='"
                + txtFullname.getText().toString().trim() + "'", null);
        if (c.getCount() > 0) {
            txtFullname.requestFocus();
            txtFullname.setError("User already exists");
        } else {
            save();
        }
        c.close();
        mydb.close();
    }

    private void save() {
        String emailstr="oilibyacustomer@gmail.com";
        PairValues<String> imeis = capturedeviceid();
        String namestr = (username.getText().toString());
        String fullnamestr = (txtFullname.getText().toString());

        //emailstr = (txtEmail.getText().toString());
        String companystr = (txtcompanyname).getText().toString();
        String qr_code_str = (txt_qr_code).getText().toString();
        String firstimei=imeis.getValue1();
        String secondimei=imeis.getValue1();
        double phonestr = Double.parseDouble(txtPhone.getText().toString());
        String customerlocation = "NAKURU";//txtcustomerlocation.getText().toString();
        String androidid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        progressDialog1.setMessage("Registering........");
        progressDialog1.show();
        dbAccess.insertUserdata(namestr, fullnamestr, emailstr, qr_code_str, companystr, phonestr, customerlocation, firstimei, secondimei);


        progressDialog1.hide();
        progressDialog1.dismiss();
String phone= String.valueOf(phonestr);
        registertoservermaliplus(namestr, emailstr, qr_code_str, phone, customerlocation, companystr, fullnamestr, firstimei, secondimei);
    }

    private boolean registertoservermaliplus(final String name, final String email, final String qr_code_str, final String phone, final String location, final String companyname,
                                             final String fullname, final String firstimei, final String secondimei) {

        if (CheckNetworkConnection()) {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest postRequest = new StringRequest(Request.Method.POST, CUSTOMER_REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                customtoast();
                                txtEmail.setText("");
                                txtFullname.setText("");
                                txtPhone.setText("");
                                txtcompanyname.setText("");
                                txt_qr_code.setText("");
                            }
                            else{
                                Toast.makeText(context, " EXISTS", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, error -> {
                finish();
            }
            ) {
                @Override
                protected Map<String, String> getParams() {         // Adding parameters
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("LEDGER_NUMBER", name);
                    params.put("LEDGER_NAME", fullname);
                    params.put("EMAIL", String.valueOf(email));
                    params.put("CARD_NUMBER", qr_code_str);
                    params.put("REFERENCE_NUMBER", qr_code_str);
                    params.put("TELEPHONES", "0722000000");
                    params.put("CONTACT_TITLE", companyname);
                    params.put("LEDGER_GROUP", "MOB");
                    params.put("BASE_LOCATION", location);
                    Log.d("NULL_T","hilkjhjk"+params);

                    return params;
                }
            };
            postRequest.setShouldCache(false);
            queue.add(postRequest);

        } else {
            Toast.makeText(registercustomer.this, "not connected to network", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void deviceregistration(double registernumber) {
        int rowId = 1;
        SQLiteDatabase database = mydb.getReadableDatabase(HASH_PHRASE);
        Cursor c = database.rawQuery("UPDATE customer_details WHERE ID=" + rowId + "SET device_system_number=" + registernumber, null);

    }

    private boolean CheckNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(registercustomer.this, "please ensure you have an internt connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!networkInfo.isConnected()) {
            Toast.makeText(registercustomer.this, "please ensure you have an internt connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }// permissions this app might request.
        }
    }*/
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
    public void customtoast(){

        Toast toast = Toast.makeText(this, "User Registered successfully!", Toast.LENGTH_LONG);
        View toastView = toast.getView();
        TextView toastMessage = toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(15);
        toastMessage.setTextColor(Color.BLACK);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setCompoundDrawablePadding(10);
        toastView.setBackgroundColor(Color.LTGRAY);
        toast.show();
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
        nextActivity(malimain.class);
    }

    private void nextActivity(Class nextClass) {
        overridePendingTransition(0, 0);
        startActivity(new Intent(context, nextClass));
        overridePendingTransition(0, 0);
        finish();
    }
}


