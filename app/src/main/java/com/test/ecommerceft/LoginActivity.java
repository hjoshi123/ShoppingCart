package com.test.ecommerceft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText mPhoneNumber;
    private FirebaseDatabase mDatabase;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPhoneNumber = findViewById(R.id.phone_number);
        mButton = findViewById(R.id.login_button);

        mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mDatabase.getReference();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final String phone = mPhoneNumber.getEditableText().toString();
            if (!phone.isEmpty()) {
                Log.d("LoginActivity",phone);
                myRef.child("Users").push().setValue(phone);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            }
        });
    }
}
