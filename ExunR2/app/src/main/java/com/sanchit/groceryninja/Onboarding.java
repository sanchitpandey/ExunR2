package com.sanchit.groceryninja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Onboarding extends AppCompatActivity {
    private String ACCESS_TOKEN="";
    public final String SHARED_PREFS = "sharedPrefs";

    void loadToken(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        ACCESS_TOKEN = sharedPreferences.getString("accessToken", "");
        System.out.println(ACCESS_TOKEN);
    }

    void LogIn(){
        startActivity(new Intent(Onboarding.this, MainActivity.class));
        finish();
    }

    void TokenProcess(){
        loadToken();
        if (!ACCESS_TOKEN.equals("")){
            LogIn();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        TokenProcess();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        TokenProcess();
        Button signup = findViewById(R.id.signupScreenBtn);
        Button login = findViewById(R.id.loginScreenBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Onboarding.this, Signup.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Onboarding.this, Login.class));
            }
        });
    }
}