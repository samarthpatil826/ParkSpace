package com.example.app4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class UserProfile extends AppCompatActivity {

    ImageButton navBack;
    TextView accNav, logout;


    //mongo database
    String AppId;
    App app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        SharedPreferences sharedPreferences;

        logout = findViewById(R.id.logout1);
        Realm.init(this);
        AppId = "application-0-uuhoa";
        app = new App(new AppConfiguration.Builder(AppId).build());
        User user = app.currentUser();

        navBack = (ImageButton) findViewById(R.id.navBack);
        navBack.setOnClickListener(v -> {
        //    startActivity(new Intent(UserProfile.this, HomePage.class));
            finish();

        });

        accNav = findViewById(R.id.accNav);
        accNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, UserAccInfo.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user != null) {
                    user.logOutAsync(result -> {
                        if (result.isSuccess()) {
                            Log.v("AUTH", "Successfully logged out.");
                            Toast.makeText(UserProfile.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserProfile.this, UserLogin.class));

                            SharedPreferences preferences =getSharedPreferences(UserLogin.fileName, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();

                            finish();
                        } else {
                            Log.e("ERROR: ", result.getError().toString());
                            Toast.makeText(UserProfile.this, "Unable to Logout", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserProfile.this, HomePage.class));
                        }
                    });
                }
            }
        });
    }
//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(UserProfile.this,HomePage.class));
//        finish();
//    }
}