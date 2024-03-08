package com.example.app4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StationDetails extends AppCompatActivity {

    TextView status, address, contact, price, ather,arjun,gest,synergy,evigo, ports, stationName;
    Button bookBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        status = findViewById(R.id.currentStatus);
        address = findViewById(R.id.stationAddress);
        ather = findViewById(R.id.currentLocation1);
        arjun = findViewById(R.id.currentLocation2);
        bookBtn = findViewById(R.id.bookBtn);
        gest = findViewById(R.id.currentLocation3);
        synergy = findViewById(R.id.currentLocation4);
        evigo = findViewById(R.id.currentLocation5);
        ports = findViewById(R.id.noOfPorts);
        contact = findViewById(R.id.contactNumber);
        stationName = findViewById(R.id.station_name);
        price = findViewById(R.id.price1);

        Intent intent = getIntent();
        String str = intent.getStringExtra("message");
        String str1 = intent.getStringExtra("status");
        String port = intent.getStringExtra("ports");
        String links = intent.getStringExtra("link");
        String price1 = intent.getStringExtra("price");

        ports.setText(port);
        status.setText(str1);
        stationName.setText(str);
        price.setText(price1);
        ather.setMovementMethod(LinkMovementMethod.getInstance());
        arjun.setMovementMethod(LinkMovementMethod.getInstance());
        gest.setMovementMethod(LinkMovementMethod.getInstance());
        synergy.setMovementMethod(LinkMovementMethod.getInstance());
        evigo.setMovementMethod(LinkMovementMethod.getInstance());

        try {
            switch (links) {
                case "ather":
                    ather.setVisibility(View.VISIBLE);
                    break;
                case "evigo":
                    evigo.setVisibility(View.VISIBLE);
                    break;
                case "gest":
                    gest.setVisibility(View.VISIBLE);
                    break;
                case "synergy":
                    synergy.setVisibility(View.VISIBLE);
                    break;
                case "arjun":
                    arjun.setVisibility(View.VISIBLE);
                    break;
            }
        } catch (Exception exception){
            Toast.makeText(this, "Your Current Location", Toast.LENGTH_SHORT).show();
            finish();
        }

        Intent intent5 = new Intent(getApplicationContext(), SlotBooking.class);
        switch (str) {
            case "Evigo Charge Charging Station":
            case "Go Easy Smart Technology Pvt Ltd":
            case "Arjun Engineering Services":
            case "Synergy Solutions AC Charging Station":
            case "Ather Grid Charging Station":
                intent5.putExtra("price",price1);
                break;
        }

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent5);
                finish();
            }
        });
    }
}