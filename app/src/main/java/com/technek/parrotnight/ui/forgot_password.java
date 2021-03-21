package com.technek.parrotnight.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.technek.parrotnight.R;

import com.technek.parrotnight.database.DatabaseAccess;
import com.technek.parrotnight.database.MaliplusDatabaseHelper;

public class forgot_password extends AppCompatActivity {
    private EditText txtEmailReset;
    private Button btnSubmitReset;
    private Context context;
    private MaliplusDatabaseHelper malidb;
    private DatabaseAccess dbAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);
        txtEmailReset = findViewById(R.id.recoveryemail);
        btnSubmitReset= findViewById(R.id.submitButton);
        context=this;
        malidb = new MaliplusDatabaseHelper(this);
        dbAccess = DatabaseAccess.getInstance(context);

        btnSubmitReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = txtEmailReset.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplication(),"Enter your registered email id",Toast.LENGTH_SHORT).show();
                    return;
                }
else {
                    dbAccess.updateEntry("super",2018);
                    Toast.makeText(forgot_password.this, "Please check your email within the next few minutes for more instructions\nAn email was sent to " + email, Toast.LENGTH_SHORT).show();


                }       }});
            }
    }
