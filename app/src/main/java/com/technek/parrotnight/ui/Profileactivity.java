package com.technek.parrotnight.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.technek.parrotnight.R;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profileactivity extends AppCompatActivity implements View.OnClickListener{

    Context context;
    TextView txt_username;
    EditText txt_phone;
    TextView txt_points;
    TextView txt_expired;
    EditText txt_vehicle;
Button Btnbtn_add_points;
    Button Btnbtn_view_points;
    Button Btnbtn_redeem_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        initalizeWidgets();
    }

    private void createListeners() {
        Btnbtn_add_points.setOnClickListener(this);
        Btnbtn_view_points.setOnClickListener(this);
        Btnbtn_redeem_points.setOnClickListener(this);

    }

    private void initalizeWidgets() {
        txt_username = findViewById(R.id.txt_customername);
        txt_phone= findViewById(R.id.txt_phone);
        txt_points = findViewById(R.id.txt_cust_points);
        txt_expired= findViewById(R.id.txt_expiredate);
        txt_vehicle= findViewById(R.id.txt_vehicle);
        Btnbtn_add_points= findViewById(R.id.btn_add_points);
        Btnbtn_view_points=findViewById(R.id.btn_view_points);
        Btnbtn_redeem_points= findViewById(R.id.btn_redeem_points);
        createListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_points:
                nextActivity(malimain.class);
                break;
            case R.id.btn_view_points:
                nextActivity(malimain.class);
                break;
            case R.id.btn_redeem_points:
               nextActivity(malimain.class);
                break;
            default:
                break;
        }
    }
    private void nextActivity(Class nextClass) {
        overridePendingTransition(0, 0);
        startActivity(new Intent(context, nextClass));
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
