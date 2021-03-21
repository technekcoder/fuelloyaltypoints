package com.technek.parrotnight.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;
import com.technek.parrotnight.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.barcode.BarcodeReader;
import com.technek.parrotnight.R;

public class activitybarcodescanner extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    private BarcodeReader barcodeReader;
    private static final String TAG = activitybarcodescanner.class.getSimpleName();
    private Context context;
    SearchView searchView;
    Button BtnSend;
    int MY_SOCKET_TIMEOUT_MS=500;
    EditText txtsearchcustomer;
    String ledger_number;
    String ledger_name;
String ledger_customer_points;
    String ledger_id_number;
    String ledger_debit;
    String ledger_credit;
    String ledger_card_number;
    String ledger_base_location;
    String customer_barcode;
    ToggleButton toggleButton;
    Camera camera;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitybarcode);
        this.mHandler = new Handler();

        this.mHandler.postDelayed(m_Runnable,5000);
        initwidgets();
        context = this;
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
        transparentToolbar();
        BtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customer_barcode=txtsearchcustomer.getText().toString();
                searchBarcode(customer_barcode);
            }
        });
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {

                    //ToDo something
                    camera = Camera.open();
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();

                } else {

                    //ToDo something
                    camera = Camera.open();
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.stopPreview();
                    camera.release();

                }
            }
        });
    }

    private void initwidgets() {
        BtnSend = findViewById(R.id.buttonSend);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
        txtsearchcustomer= findViewById(R.id.search_name);
        toggleButton = findViewById(R.id.onOffFlashlight);

    }

    @Override
    public void onScanned(final Barcode barcode) {
        // play beep sound
        Log.e(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Barcode: " + barcode.displayValue, Toast.LENGTH_LONG).show();

                customer_barcode = barcode.displayValue;
                searchBarcode(customer_barcode);
            }
        });

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Log.e(TAG, "onScannedMultiple: " + barcodes.size());

        String codes = "";
        for (Barcode barcode : barcodes) {
            codes += barcode.displayValue + ", ";
        }

        final String finalCodes = codes;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Barcodes: " + finalCodes, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    private void transparentToolbar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(activitybarcodescanner activitybarcodescanner, final int bits, boolean on) {
        Window win = activitybarcodescanner.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

    }


    private void searchBarcode(String customer) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BARCODE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("NULL_T",""+response);

                        if (response.contains("customer")) {
                            Log.d("NULL_T",""+response);
                            responseMethod(response);
                        }else{
                            Toast.makeText(context, "customer not registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("NULL_T", "o8iyugijo" + error);

                Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("CARD_NUMBER", customer_barcode);
                Log.d("NULL_T", "o8iyugijo" + map);

                return map;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        // MyApplication.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showNotfound() {
        Toast.makeText(getApplicationContext(), "Customer does not exist", Toast.LENGTH_SHORT).show();

    }
    private void responseMethod(String response) {
        try {

            JSONObject jsonObject = new JSONObject(response);

            JSONArray dataArray = jsonObject.getJSONArray("customer");
            if (dataArray.length()>0) {
                int i = 0;
                while (i < dataArray.length()) {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    ledger_number = dataobj.getString("LEDGER_NUMBER");
                    ledger_name = dataobj.getString("LEDGER_NAME");
                    ledger_customer_points = dataobj.getString("CLOSING");
                    ledger_card_number = dataobj.getString("CARD_NUMBER");
                    ledger_base_location = dataobj.getString("BASE_LOCATION");
                    ledger_id_number = dataobj.getString("ID_NUMBER");
                    ledger_debit = dataobj.getString("DEBIT");
                    ledger_credit = dataobj.getString("CREDIT");

                    i++;
                    Log.d("NULL_T", "o8iyugijo" + ledger_number);
                }
                String customer_verify = txtsearchcustomer.getText().toString();

                if (customer_verify.equals(ledger_number)) {
                    Intent intent = new Intent(activitybarcodescanner.this, malimain.class);
                    intent.putExtra("ledger_number", ledger_number);
                    intent.putExtra("ledger_name", ledger_name);
                    intent.putExtra("ledger_base_location", ledger_base_location);
                    intent.putExtra("ledger_id_number", ledger_id_number);
                    intent.putExtra("ledger_customer_points", ledger_customer_points);
                    intent.putExtra("ledger_debit_points", ledger_debit);
                    intent.putExtra("ledger_credit_points", ledger_credit);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(activitybarcodescanner.this, malimain.class);
                    intent.putExtra("ledger_number", ledger_number);
                    intent.putExtra("ledger_name", ledger_name);
                    intent.putExtra("ledger_base_location", ledger_base_location);
                    intent.putExtra("ledger_id_number", ledger_id_number);
                    intent.putExtra("ledger_customer_points", ledger_customer_points);
                    intent.putExtra("ledger_debit_points", ledger_debit);
                    intent.putExtra("ledger_credit_points", ledger_credit);
                    startActivity(intent);
                }
            }
            else{
                showNotfound();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNoUser() {
        Toast.makeText(getApplicationContext(), "not found", Toast.LENGTH_SHORT).show();

    }
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            Toast.makeText(activitybarcodescanner.this,"connection to internet refreshed",Toast.LENGTH_SHORT).show();

            activitybarcodescanner.this.mHandler.postDelayed(m_Runnable, 5000);
        }

    };//

}