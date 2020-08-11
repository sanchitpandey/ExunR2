package com.sanchit.groceryninja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

public class Login extends AppCompatActivity {
    private EditText name, password;
    private String url ="https://quiet-mountain-65416.herokuapp.com/", TAG="TAG", ACCESS_TOKEN="";
    private RequestQueue queue;
    public final String SHARED_PREFS = "sharedPrefs";

    void saveToken(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("accessToken", ACCESS_TOKEN).apply();
    }

    void loadToken(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        ACCESS_TOKEN = sharedPreferences.getString("accessToken", "");
    }

    void LogIn(){
        startActivity(new Intent(Login.this, MainActivity.class));
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
        setContentView(R.layout.activity_login);
        Button signup = findViewById(R.id.signupSwitchBtn);
        Button login = findViewById(R.id.loginBtn);
        queue = Volley.newRequestQueue(this);
        name = findViewById(R.id.editTextNameLogin);
        password = findViewById(R.id.editTextPasswordLogin);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Signup.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
                }
                else {
                    signinAPI();
                }
            }
        });
    }

    private void signinAPI() {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name.getText().toString());
            jsonBody.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest req = new StringRequest(Request.Method.POST, url+"users/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    Toast.makeText(Login.this, res.get("message").toString(), Toast.LENGTH_SHORT).show();
                    ACCESS_TOKEN = res.get("accessToken").toString();
                    saveToken();
                    LogIn();
                } catch (JSONException e) {
                    Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(Login.this, "Error Occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return jsonBody == null ? null : jsonBody.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody, "utf-8");
                    return null;
                }
            }
        };
        queue.add(req);
    }
}