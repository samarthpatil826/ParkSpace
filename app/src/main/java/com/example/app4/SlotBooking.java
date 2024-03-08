package com.example.app4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class SlotBooking extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] station1 = {"0","1","2","3","4","5","6"};
    String[] station2 = {"0","10","20","30","40","50","60","70","80","90","100"};
    ToggleButton toggleButton;
    LinearLayout linearLayout;
    TextView timeText, percentText, textQues, costPerkWh, estimatedCost;
    String selectedText = null;
    Float kWh = 3.0f;
    Float battery = 30.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_booking);

        Intent intent5 = getIntent();
        String price = intent5.getStringExtra("price");
        toggleButton = findViewById(R.id.timepercent);
        Spinner spinner = findViewById(R.id.spinner);
        linearLayout = findViewById(R.id.linearLayoutBook);
        timeText = findViewById(R.id.timeTxt);
        percentText = findViewById(R.id.percentTxt);
        textQues = findViewById(R.id.textQues);
        costPerkWh = findViewById(R.id.costperkWh);
        costPerkWh.setText(price + " Rs/Unit");
        estimatedCost = findViewById(R.id.estimatecost);
//        Toast.makeText(SlotBooking.this, selectedText, Toast.LENGTH_SHORT).show();




        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter ad;


                if (toggleButton.isChecked()) {
                    linearLayout.setVisibility(View.VISIBLE);
                    percentText.setBackgroundColor(getResources().getColor(R.color.TRANSPERENT2));
                    timeText.setBackgroundColor(getResources().getColor(R.color.TRANSPERENT));
                    textQues.setText("How much percent you want to charge your bike?");
                    ad = new ArrayAdapter(SlotBooking.this, android.R.layout.simple_spinner_item, station2);
                    ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(ad);


                } else {
                    Toast.makeText(SlotBooking.this, selectedText, Toast.LENGTH_SHORT).show();
                    linearLayout.setVisibility(View.VISIBLE);
                    percentText.setBackgroundColor(getResources().getColor(R.color.TRANSPERENT));
                    timeText.setBackgroundColor(getResources().getColor(R.color.TRANSPERENT2));
                    textQues.setText("How much time you want to charge your bike[in hours]?");
                    ad = new ArrayAdapter(SlotBooking.this, android.R.layout.simple_spinner_item, station1);
                    ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(ad);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent intent5 = getIntent();
                String price = intent5.getStringExtra("price");
                if (toggleButton.isChecked()) {
                    selectedText = station2[position];
                    estimatedCost.setText(Float.toString(((Float.parseFloat(selectedText)/100)*battery)*Float.parseFloat(price)));
                } else {
                    selectedText = station1[position];
                    estimatedCost.setText(Float.toString((Float.parseFloat(selectedText)*kWh)*Float.parseFloat(price)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        for (int i=0;i<=4;i++) {
            toggleButton.performClick();
            toggleButton.setPressed(true);
            toggleButton.invalidate();
            estimatedCost = findViewById(R.id.estimatecost);

            int finalI = i;
            toggleButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toggleButton.setPressed(false);
                    toggleButton.invalidate();
                    if (finalI ==2) {
//                        Toast.makeText(SlotBooking.this, selectedText, Toast.LENGTH_SHORT).show();
                    }
                }
            },1000);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void returnBack(View view) {
        finish();
    }
}


