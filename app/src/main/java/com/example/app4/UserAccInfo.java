package com.example.app4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class UserAccInfo extends AppCompatActivity {


    TextView text110, text111, text112, text113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_acc_info);

        text110 = findViewById(R.id.first_name);
        text111 = findViewById(R.id.last_name);
        text112 = findViewById(R.id.email2);
        text113 = findViewById(R.id.phnno23);
    }

}