package com.technek.parrotnight.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technek.parrotnight.R;
import com.technek.parrotnight.util.Config;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {

    private TextView txtUsername;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private Button btnResetPassword;
    private Bundle bundle;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String TAG = "ResetPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_reset_password);

        bundle = new Bundle();
        context = this;
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPrefs), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initWidgets();
        setBundle();
        setListeners();
    }

    private void initWidgets() {
        txtUsername = findViewById(R.id.txtResetPasswordUsername);
        txtPassword = findViewById(R.id.txtResetPassword1);
        txtConfirmPassword = findViewById(R.id.txtResetPassword2);
        btnResetPassword = findViewById(R.id.btnResetPassword);
    }

    private void setBundle() {
        bundle = getIntent().getExtras();

        if (bundle != null) {
            String userName = bundle.getString(MainActivity.RESET_PASSWORD_USERNAME);
            if (userName == null) {
                userName = "";
            }

            txtUsername.setText(userName);
        }
    }

    private void setListeners() {
        btnResetPassword.setOnClickListener(e -> {
            resetPassword();
        });
    }

    private boolean validatePassword() {
        if (txtPassword.getText().toString().isEmpty()) {
            txtPassword.setError("Enter Password");
            return false;
        }
        if (txtConfirmPassword.getText().toString().isEmpty()) {
            txtConfirmPassword.setError("Confirm Password");
            return false;
        }
        if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
            txtConfirmPassword.setError("Password Mismatch.");
            return false;
        }

        return true;
    }

    private void resetPassword() {
        if (validatePassword()) {
            requestPasswordReset();
        }
    }

    private void requestPasswordReset() {
        String username = txtUsername.getText().toString();
        String oldPassword = "";
        String newPassword = MainActivity.encrypt(txtConfirmPassword.getText().toString());
        String resetMessage = "RESET";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.RESET_PASSWORD_URL, response -> {

            Log.d(TAG, "requestPasswordReset: " + response);
            if (response.toLowerCase().contains("not allowed")) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            } else if (response.toLowerCase().contains("password reset successfully")) {
                editor.putString("CLIENT_USER_NAME", username);
                editor.commit();

                startActivity(new Intent(context, MainActivity.class));
                finish();
            } else {
                Toast.makeText(context, "Error Reseting password.", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap<>();
                map.put("USERNAME", username);
                map.put("OLD_PASSWORD", oldPassword);
                map.put("NEW_PASSWORD", newPassword);
                map.put("RESET", resetMessage);
                Log.d("NULL_T",""+map);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
