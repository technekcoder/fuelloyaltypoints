package com.technek.parrotnight.http_network;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technek.parrotnight.util.Config;
import com.technek.parrotnight.util.ContentProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SNOW DEN on 07/02/2018.
 */

public class NetworkCalls {
    private static ContentProvider alpha_cp;
    private static NetworkCalls net = null;
    private ProgressDialog progressDialog;


    private NetworkCalls() {
    }

    public static NetworkCalls getInstance() {
        if (net == null) {
            net = new NetworkCalls();
        }

        return net;
    }

    public void initCall(Context context, String url, Map<String, String> params, NetworkInterface listener) {
        Config config = Config.getInstance();
        alpha_cp = new ContentProvider(context);
        config.refreshInstance(context);
        showProgress(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("RESPONSE_SUCCESS", response);
                    hideDialog();
                    listener.response(response);
                }, error -> {
            hideDialog();
            Log.d("RESPONSE_ERROR", error.toString());
            Toast.makeText(context, "Check Your Connection", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> maps = params;
                if (maps == null)
                    maps = new HashMap<>();
                maps.put("DSN_NAME", alpha_cp.spLicences.getString("DSN_NAME", ""));
                maps.put("DB_USER", alpha_cp.spLicences.getString("DB_USER", ""));
                maps.put("DB_PWD", alpha_cp.spLicences.getString("DB_PWD", ""));
                return maps;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void hideDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        Log.d("DIALOG_PRO", "kill dialog");

    }

    private void showProgress(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }
}
