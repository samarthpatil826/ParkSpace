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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class UserSignup extends AppCompatActivity {


    //...................................................................................
//.............................XML Element Declaration...............................
//...................................................................................
    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone1, edtOTP1, edtName1, edtPass1, edtEmail1;

    //...................................................................................
//........................FireBase Authentication Declaration........................
//...................................................................................
    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;


    // string for storing our verification ID
    private String verificationId;

    // string appid for mongodb realm
    String AppId = "application-0-uuhoa";
    MongoDatabase mongoDatabase;
    MongoClient mongoClient, mongoClient1;
    App app;

    ImageButton showBtn, hideBtn;


    Credentials credentials;

//...................................................................................
//.............................FireBase Authentication...............................
//...................................................................................

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);


        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId).build());

        //...................................................................................
        //.................initializing variables for Buttons and EditText...................
        //...................................................................................
        edtOTP1 = findViewById(R.id.otp);
        // buttons for generating OTP and verifying OTP
        Button verifyOTPBtn1 = findViewById(R.id.verify_btn);
        Button generateOTPBtn1 = (Button) findViewById(R.id.send);
        edtPhone1 = findViewById(R.id.phone);
        edtEmail1 = findViewById(R.id.email);
        edtName1 = findViewById(R.id.reg_name);
        edtPass1 = findViewById(R.id.password);

    //MongoDB




    //...................................................................................
    //.............below line is for getting instance of our FirebaseAuth................
    //...................................................................................
        mAuth = FirebaseAuth.getInstance();


    //...................................................................................
    // ...............setting onclick listener for generate OTP button....................
    //...................................................................................
        generateOTPBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // below line is for checking weather the user
            // has entered his mobile number or not.
                if (TextUtils.isEmpty(edtName1.getText().toString() +
                       edtPass1.getText().toString() + edtPhone1.getText().toString() + edtEmail1.getText().toString())) {
                    Toast.makeText(UserSignup.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }
                else if (isValidPass(edtPass1.getText().toString())) {
                         if (TextUtils.isEmpty(edtPhone1.getText())) {

                        // when mobile number text field is empty
                        // displaying a toast message.
                            Toast.makeText(UserSignup.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                        } else {
                             app.getEmailPassword().registerUserAsync(edtPhone1.getText().toString(),edtPass1.getText().toString(), (App.Result<Void> it) ->{
                             if(it.isSuccess())
                             {
                                 Log.v("User","Registered with email successfully");
                                 Toast.makeText(UserSignup.this, "Registered", Toast.LENGTH_SHORT).show();

                                 credentials = Credentials.emailPassword(edtPhone1.getText().toString(), edtPass1.getText().toString());

                                 app.loginAsync(credentials, new App.Callback<User>() {
                                     @Override
                                     public void onResult(App.Result<User> result) {
                                         if (result.isSuccess()){
                                             Toast.makeText(UserSignup.this, "Already Registered!!!", Toast.LENGTH_SHORT).show();
                                         } else{
                                             String phone = "+91" + edtPhone1.getText().toString();
                                             sendVerificationCode(phone);
                                         }
                                     }
                                 });
                             }
                             else
                             {
                                 Log.v("User","Registration Failed");
                                 Toast.makeText(UserSignup.this, "not Registered", Toast.LENGTH_SHORT).show();
                                 edtEmail1.getText().clear();
                                 edtName1.getText().clear();
                                 edtPhone1.getText().clear();
                                 edtPass1.getText().clear();
                             }
                         });


                        // if the text field is not empty we are calling our
                        // send OTP method for getting OTP from Firebase.


                        //.................mongodb register......................

                         }
                    }
                else{
                    Toast.makeText(UserSignup.this, "Please enter use: a-z, 1-9, A-Z, @ # $ % &", Toast.LENGTH_SHORT).show();
                }
            }
        });

    //...................................................................................
    //.............initializing on click listener for verify otp button..................
    //...................................................................................
        verifyOTPBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(edtOTP1.getText().toString())) {

                // if the OTP text field is empty display
                // a message to user to enter OTP
                    Toast.makeText(UserSignup.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                // if OTP field is not empty calling
                // method to verify the OTP.
                    verifyCode(edtOTP1.getText().toString());
                    Toast.makeText(UserSignup.this, "Registration Successful!!!!", Toast.LENGTH_SHORT).show();


                    credentials = Credentials.emailPassword(edtPhone1.getText().toString(), edtPass1.getText().toString());

                    app.loginAsync(credentials, new App.Callback<User>() {
                        @Override
                        public void onResult(App.Result<User> result) {
                            if (result.isSuccess()) {
                                Log.v("User", "Logged In Successfully");

                                regMongo();
                                startActivity(new Intent(UserSignup.this, HomePage.class));
                            } else {
                                Log.v("User", "Failed to login");
                                Toast.makeText(UserSignup.this, "Invalid login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                   finish();
                    }
             }
});


        TextView login = findViewById(R.id.redirect2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(UserSignup.this, UserLogin.class);
                startActivity(i);


            }
        });
    }

    private void regMongo() {
        User user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("UserData");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("TestData");

      //  ObjectId id = new ObjectId(user.getId());

        Document document = new Document("userId",user.getId()).append("name",edtName1.getText().toString())
                .append("phone number",edtPhone1.getText().toString()).append("password",edtPass1.getText().toString()).append("email",edtEmail1.getText().toString());

        List<Document> list = new ArrayList<>();
        list.add(document);
        mongoCollection.insertMany(list).getAsync(result -> {
            if(result.isSuccess())
            {
                Log.v("Data","Data Inserted Successfully");
            }
            else
            {
                Log.v("Data","Error:"+result.getError().toString());
            }
        });
    }



    //...................................................................................
    //..............................Defining FireBase Auth Functions..............................
    //...................................................................................

    private void signInWithCredential(PhoneAuthCredential credential) {

    // inside this method we are checking if
    // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        // if the code is correct and the task is successful
                        // we are sending our user to new activity.
                            Intent i = new Intent(UserSignup.this, UserLogin.class);
                            startActivity(i);
                            finish();
                        } else {

                        // if the code is not correct then we are
                        // displaying an error message to the user.
                            Toast.makeText(UserSignup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {

    // this method is used for getting
    // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)         // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)         // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    //...................................................................................
    //.................callback method is called on Phone auth provider..................
    //...................................................................................
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks

        // initializing our callbacks for on
        // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    // below method is used when
    // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

        // when we receive the OTP it
        // contains a unique id which
        // we are storing in our string
        // which we have already created.
            verificationId = s;
        }

    // this method is called when user
    // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        // below line is used for getting OTP code
        // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

        // checking if the code
        // is null or not.
            if (code != null) {

            // if the code is not null then
            // we are setting that code to
            // our OTP edittext field.
                edtOTP1.setText(code);

            // after setting this code
            // to OTP edittext field we
            // are calling our verifycode method.
                verifyCode(code);
            }
        }

    // this method is called when firebase doesn't
    // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {

        // displaying error message with firebase exception.
            Toast.makeText(UserSignup.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

// below method is use to verify code from Firebase.
    private void verifyCode(String code) {

    // below line is used for getting getting
    // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

    // after getting credential we are
    // calling sign in method.
        signInWithCredential(credential);
    }

    //...................................................................................
    //................................Password validation................................
    //...................................................................................
    public static boolean isValidPass(String pass1) {
        String regex = "^(?=.*[0-9])" + "^(?=.*[a-z])" + "^(?=.*[A-Z])" + "^(?=.*[@#$%&])" + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);

        if (pass1 == null) {
            return false;
        }
        Matcher m = p.matcher(pass1);

        return m.matches();
    }

    public void hidePassword(View view) {
        showBtn = findViewById(R.id.showPass);
        hideBtn = findViewById(R.id.hidePass);

        if (edtPass1.getTransformationMethod()== PasswordTransformationMethod.getInstance()){
//            pass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edtPass1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            hideBtn.setVisibility(View.VISIBLE);
            showBtn.setVisibility(View.GONE);
        }
        else{
//            pass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edtPass1.setTransformationMethod(PasswordTransformationMethod.getInstance());

            hideBtn.setVisibility(View.GONE);
            showBtn.setVisibility(View.VISIBLE);
        }

    }
}
