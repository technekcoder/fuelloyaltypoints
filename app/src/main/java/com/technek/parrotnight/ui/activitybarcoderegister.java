package com.technek.parrotnight.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
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
import androidx.core.app.ActivityCompat;

import com.technek.parrotnight.R;

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

public class activitybarcoderegister extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    private BarcodeReader barcodeReader;
    private static final String TAG = activitybarcoderegister.class.getSimpleName();
    private Context context;
    SearchView searchView;
    private static final int CAMERA_REQUEST = 50;

    Button BtnSend;
    String customer;
    EditText txtsearchcustomer;
    ToggleButton toggleButton;
    Camera camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitybarcode);
        initwidgets();
        context = this;
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
        transparentToolbar();
        BtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customer = txtsearchcustomer.getText().toString();
                CheckavailableBarcode(customer);
            }
        });
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                ActivityCompat.requestPermissions(activitybarcoderegister.this,
                        new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
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
        txtsearchcustomer = findViewById(R.id.search_name);
        toggleButton = findViewById(R.id.onOffFlashlight);

    }

    @Override
    public void onScanned(Barcode barcode) {
        // play beep sound
        Log.e(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Barcode: " + barcode.displayValue, Toast.LENGTH_LONG).show();

                customer = barcode.displayValue;
                CheckavailableBarcode(customer);
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

    private void setWindowFlag(activitybarcoderegister activitybarcodescanner, final int bits, boolean on) {
        Window win = activitybarcodescanner.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

    }


    private void showNotfound() {
        Toast.makeText(getApplicationContext(), "Customer does not exist", Toast.LENGTH_SHORT).show();

    }


    private void showNoUser() {
        Toast.makeText(getApplicationContext(), "not found", Toast.LENGTH_SHORT).show();

    }

    private void CheckavailableBarcode(String customer) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BARCODE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("NULL_T",""+response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray dataArray = jsonObject.getJSONArray("customer");
                            Log.d("NULL_T",""+response);
                                int i = 0;
                                while (i < dataArray.length()) {
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    String ledger_number = dataobj.getString("LEDGER_NUMBER");
                                    if(ledger_number.isEmpty()) {
                                        Log.d("NULL_T", "" + response);
                                        Toast.makeText(context, "CARD DOESNT EXIST", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (!ledger_number.equals("CASH-SALES")) {
                                        Log.d("NULL_T", "" + response);
                                        Toast.makeText(context, "CARD ALREADY ASSIGNED TO ANOTHER CUSTOMER", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Intent intent = new Intent(activitybarcoderegister.this, registercustomer.class);
                                        intent.putExtra("register_barcode", customer);
                                        startActivity(intent);
                                    }
                                    i++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("CARD_NUMBER", customer);

                return map;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        // MyApplication.getInstance().addToRequestQueue(jsonObjReq);
    }


}