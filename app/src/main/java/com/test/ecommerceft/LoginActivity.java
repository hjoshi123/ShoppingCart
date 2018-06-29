package com.test.ecommerceft;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText mPhoneNumber, mName;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPhoneNumber = findViewById(R.id.phone_number);
        mName = findViewById(R.id.name);
        mButton = findViewById(R.id.login_button);

        mButton.setOnClickListener(v -> {
            final String phone = mPhoneNumber.getEditableText().toString();
            final String name = mName.getEditableText().toString();
            if (!phone.isEmpty() && !name.isEmpty()) {
                Log.d("LoginActivity",phone);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("phone", phone);
                intent.putExtra("name", name);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
