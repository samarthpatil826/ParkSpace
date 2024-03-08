package com.example.app4;

//...................................................................................
//................................Importing libraries................................
//...................................................................................

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class UserLogin extends AppCompatActivity {

    EditText pass2, phnno;
    ImageButton showBtn, hideBtn;



    // string appid for mongodb realm
    String AppId = "application-0-uuhoa";
    App app;
    SharedPreferences sharedPreferences;

    public static final String fileName = "login";
    public static final String Username = "login";
    public static final String Password = "login";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        phnno = findViewById(R.id.phoneNumber);
        pass2 = findViewById(R.id.passWORD);

        Realm.init(this);

        //MongoAuth

        TextView txtView = findViewById(R.id.redirect1);

        txtView.setOnClickListener(v -> startActivity(new Intent(UserLogin.this, UserSignup.class)));

        if(sharedPreferences.contains(Username)){
            startActivity(new Intent(UserLogin.this, HomePage.class));
            Log.v("User", "Logged In Successfully");
//            Toast.makeText(UserLogin.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
        }
        Button b1 = findViewById(R.id.loginButton);
        b1.setOnClickListener(v -> {


            app = new App(new AppConfiguration.Builder(AppId).build());

            if (TextUtils.isEmpty(pass2.getText().toString()) && TextUtils.isEmpty(phnno.getText().toString())) {
                Toast.makeText(UserLogin.this, "Enter Credentials!!!", Toast.LENGTH_SHORT).show();
            } else {
                try {

                    Credentials credentials = Credentials.emailPassword(phnno.getText().toString(), pass2.getText().toString());
                    app.loginAsync(credentials, result -> {
                    if (result.isSuccess()) {
                        SharedPreferences.Editor e = sharedPreferences.edit();
                        e.putBoolean("rememberMe", true);
                        e.putString(Username, phnno.getText().toString());
                        e.putString(Password, pass2.getText().toString());
                        e.commit();

                        startActivity(new Intent(UserLogin.this, HomePage.class));
                        Log.v("User", "Logged In Successfully");
//                        Toast.makeText(UserLogin.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Log.v("User", "Failed to login");
                        Toast.makeText(UserLogin.this, "Invalid login", Toast.LENGTH_SHORT).show();
                        phnno.getText().clear();
                        pass2.getText().clear();
                    }
                });

                    } catch (Exception exception) {
                        Toast.makeText(this, "Please Enter Proper Credentials!!!", Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }


    public void hidePassword(View view) {
        showBtn = findViewById(R.id.showPass);
        hideBtn = findViewById(R.id.hidePass);

        if (pass2.getTransformationMethod()==PasswordTransformationMethod.getInstance()){
            pass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            hideBtn.setVisibility(View.VISIBLE);
            showBtn.setVisibility(View.GONE);
        }
        else{
            pass2.setTransformationMethod(PasswordTransformationMethod.getInstance());

            hideBtn.setVisibility(View.GONE);
            showBtn.setVisibility(View.VISIBLE);
        }
    }



//    private void saveLoginDetails()
//    {
//        //fill input boxes with stored login and pass
//
//        String login = phnno.getText().toString();
//        String upass = pass2.getText().toString();
//
//        SharedPreferences.Editor e = sharedPreferences.edit();
//        e.putBoolean("rememberMe", true);
//        e.putString("login", login);
//        e.putString("password", upass);
//        e.commit();
//    }
    private void removeLoginDetails()
    {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putBoolean("rememberMe", false);
        e.remove("login");
        e.remove("password");
        e.commit();
    }
}
