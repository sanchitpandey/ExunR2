package com.sanchit.groceryninja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Onboarding extends AppCompatActivity {
    FirebaseUser user;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            startActivity(new Intent(Onboarding.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

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