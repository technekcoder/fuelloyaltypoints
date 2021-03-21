package com.technek.parrotnight.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.technek.parrotnight.R;

import net.sqlcipher.database.SQLiteDatabase;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technek.parrotnight.database.DatabaseAccess;
import com.technek.parrotnight.database.MaliplusDatabaseHelper;
import com.technek.parrotnight.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MaliplusUpdater extends Activity {
    private DatabaseAccess dbAccess;
    private Context context;
    private Button btnImport, btnCheck;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    public static final String HASH_PHRASE = "#PSL2018";
    ProgressBar pb;
    private TextView txtClose, tvdate;
    private MaliplusDatabaseHelper db;
    RotateAnimation rotate;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor sharedPrefsEditor;
    String fetch_config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SQLiteDatabase.loadLibs(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maliplus_updater);
        context = this;
        dbAccess = DatabaseAccess.getInstance(context);
        btnImport = findViewById(R.id.btnimport);
        txtClose = findViewById(R.id.txtclose);
        tvdate = findViewById(R.id.tvdate);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvdate.setText(date);
        db = new MaliplusDatabaseHelper(context);
        sharedPrefs = context.getSharedPreferences(getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
        sharedPrefsEditor = sharedPrefs.edit();
        fetch_config = sharedPrefs.getString(getString(R.string.fetch_config), null);

        intwidgets();
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dbAccess.checkDataExists()) {

                    Intent intent = new Intent(MaliplusUpdater.this, activitybarcodescanner.class);
                    startActivity(intent);
                    Log.d("NULL_T", "login");
                } else {
                    Intent i = new Intent(MaliplusUpdater.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInternetconnection();
                refreshDataFromServer();
                fetchcompanysettings();
            }

            private boolean CheckInternetconnection() {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null) {
                    Toast.makeText(MaliplusUpdater.this, "please ensure you have an internet connection", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!networkInfo.isConnected()) {
                    Toast.makeText(MaliplusUpdater.this, "please ensure you have an internet connection", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });
    }

    private void intwidgets() {
    }



    private void progress() {
    }

    private void refreshDataFromServer() {
        animator();
        Log.d("NULL_T", "start fetching  server");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FUEL_PRICES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        longInfo(response); //solution for displaying more data in logcat
                         Log.d("NULL_T","prices"+response);

                        if (response.contains("fuel_price")) {
                            responseMethod(response);

                                Toast.makeText(context, "Sync Successful", Toast.LENGTH_SHORT).show(); } }
                    private void longInfo(String str) {
                        if(str.length() > 4000) {
                            Log.i("NULL_T",str.substring(0, 4000));
                            longInfo(str.substring(4000));
                        } else
                            Log.i("NULL_T",str); }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (rotate != null) {
                            rotate.cancel();
                        }
                        Toast.makeText(context, "Check Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                //map.put(Config.LOCATION, "Y");

                return map;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void responseMethod(String response) {

        try {
            Log.d("NULL_R","WEDRFG11111111111"+response);

            JSONObject jsonObject = new JSONObject(response);

            JSONArray pricesArray = jsonObject.getJSONArray("fuel_price");
            Log.d("NULL_R","WEDRFG11111111111"+pricesArray);

            int i = 0;
            while (i < pricesArray.length()) {
                JSONObject aItem = pricesArray.getJSONObject(i);
                String TOWN=aItem.getString("TOWN");
                double PMS_PRICE=aItem.getDouble("PMS");
                double AGO_PRICE=aItem.getDouble("AGO");
                double DPK_PRICE=aItem.getDouble("DPK");
                double DPK_LANDING_COST=aItem.getDouble("DPK_LANDING_COST");
                double PMS_LANDING_COST=aItem.getDouble("PMS_LANDING_COST");
                double AGO_LANDING_COST=aItem.getDouble("AGO_LANDING_COST");
                double PMS_GRM=aItem.getDouble("PMS_GRM");
                double AGO_GRM=aItem.getDouble("AGO_GRM");
                double DPK_GRM=aItem.getDouble("DPK_GRM");
                double PMS_LEVIES=aItem.getDouble("PMS_LEVIES");
                double AGO_LEVIES=aItem.getDouble("AGO_LEVIES");
                double DPK_LEVIES=aItem.getDouble("DPK_LEVIES");
                double PMS_VAT=aItem.getDouble("PMS_LEVIES");
                double AGO_VAT=aItem.getDouble("AGO_VAT");
                double DPK_VAT=aItem.getDouble("DPK_VAT");
                dbAccess.insertTaxInfoData(
                        "PMS", PMS_PRICE, PMS_LEVIES,PMS_LANDING_COST, 0.16,
                        "LTR", TOWN, "PMS");
                dbAccess.insertTaxInfoData(
                        "AGO", AGO_PRICE, AGO_LEVIES, PMS_LANDING_COST, 0.16,
                        "LTR", TOWN, "AGO");
                dbAccess.insertTaxInfoData(
                        "DPK", DPK_PRICE, DPK_LEVIES, PMS_LANDING_COST, 0.16,
                        "LTR", TOWN, "DPK");
                i++;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        finish();
    }

/*
    private void responseMethod(String response) {
        Type taxListType = new TypeToken<ArrayList<TaxInfo>>() {
        }.getType();
        Gson gson = new Gson();
        List<TaxInfo> locationPrices = gson.fromJson(response, taxListType);
        if (locationPrices.size() > 0) {
            for (TaxInfo info : locationPrices) {
                dbAccess.insertTaxInfoData(
                        info.getItemCode(), info.getUnitPrice(), info.getLevies(), info.getTaxable(), info.getTaxRate(),
                        info.getSUOM(), info.getLocation(), info.getItemName());
                Log.d("NULL_T", "fetched from server" + locationPrices);
                checkdata();
            }
        }
        finish();
    }
*/


    private void checkdata() {

        SQLiteDatabase database = db.getReadableDatabase(HASH_PHRASE);
        Cursor c = database.rawQuery("SELECT * FROM item_master WHERE salesprice IS NULL", null);
        if (c.getCount() > 0) {
            Log.d("NULL_T", "db has null values");
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        } else {
            dbAccess.controldb("Active", "Y");
            Log.d("NULL_T", "db does not have null values");
        }
    }
    public void animator() {
        rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(400);
        rotate.setRepeatCount(Animation.INFINITE);
        //rotate.setRepeatMode(Animation.RESTART);
    }
    private void fetchcompanysettings() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FUEL_COMPANY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("NULL_T", "user: " + response);

                        // check for success status
                        if (response.contains("company_settings")) {
                            Log.e("NULL_T", "user: " + response);
                            sharedPrefsEditor.putString(getString(R.string.fetch_config), response);
                            sharedPrefsEditor.commit();
                            responseMethodcompany(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Check Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();


                return map;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        // MyApplication.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void responseMethodcompany(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray dataArray = jsonObject.getJSONArray("company_settings");
            int i = 0;
            while (i < dataArray.length()) {
                JSONObject dataobj = dataArray.getJSONObject(i);
                String FULL_NAME = dataobj.getString("FULL_NAME");
                String FISCAL_MONTH = dataobj.getString("FISCAL_MONTH");
                double loyalty_points_amount = dataobj.getDouble("VALUE1");
                double loyalty_redeem_amount = dataobj.getDouble("VALUE2");
                dbAccess.insertCompanydata(FULL_NAME,loyalty_points_amount,loyalty_redeem_amount);
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}