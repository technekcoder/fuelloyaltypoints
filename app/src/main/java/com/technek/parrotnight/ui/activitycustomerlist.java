package com.technek.parrotnight.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.technek.parrotnight.R;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technek.parrotnight.adapter.customeradapter;
import com.technek.parrotnight.models.ClientDetails;
import com.technek.parrotnight.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activitycustomerlist extends AppCompatActivity {
    Context context;
    AlertDialog dialog;
    List<ClientDetails> customersList = new ArrayList<>();
    ListView list_customers ;
    ImageView bt_clear ;
    ImageView bt_delete;
    SearchView sv_search_customer ;
    RotateAnimation rotate;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor sharedPrefsEditor;

    public static final String FREQUENT_ITEMS = "SHORTITEMLIST";
    private static final String SHARED_PREFS = "sharedPrefs";
    SharedPreferences.Editor editor;
    String fetch_customers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_search_dialog);
context=this;
        fetchcustomers();
        list_customers = findViewById(R.id.listDiagCustCustomerListView);
        bt_clear = findViewById(R.id.bt_clear);
        bt_delete = findViewById(R.id.bt_delete);
        sv_search_customer = findViewById(R.id.sv_search_customer);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listview_header,list_customers,false);
        list_customers.addHeaderView(header);
    }

    private void fetchcustomers() {
        if (CheckNetworkConnection()) {
            setProgressDialog();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CUSTOMERS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            display_customers(response);
                            Log.d("DISTINCT", "Loc Size--: " + response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Check Connection: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        }
    }
    private void display_customers(String string){
        //fetch_customers = sharedPrefs.getString(getString(R.string.fetch_customers), null);
        if (!string.contains("LEDGER_NUMBER")) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray result = jsonObject.getJSONArray("customers");

            customersList.clear();

            int i = 0;
            while (i < result.length()) {
                ClientDetails customer = new ClientDetails();

                JSONObject jsonItem = result.getJSONObject(i);

                customer.setLedgerName(jsonItem.getString("LEDGER_NAME"));
                customer.setLoyaltycardno(jsonItem.getString("CARD_NUMBER"));
                customer.setLedger_points(jsonItem.getString("LOYALTY_POINTS"));
                customer.setLedger_card(jsonItem.getString("LEDGER_NUMBER"));

                i++;

                customersList.add(customer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        hideProgressDialog();
        final customeradapter customersAdapter = new customeradapter(context, customersList);
        list_customers.setAdapter(customersAdapter);

        list_customers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sv_search_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                sv_search_customer.setIconified(false);
            }
        });

        sv_search_customer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customersAdapter.filter(s);
                return false;
            }
        });

    }
    public void setProgressDialog() {

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
        tvText.setText("fetching loyalty card list ...");
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
        dialog.dismiss();
    }

        private boolean CheckNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(activitycustomerlist.this, "please ensure you have an internet connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!networkInfo.isConnected()) {
            Toast.makeText(activitycustomerlist.this, "please ensure you have an internet connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
