package com.sanchit.groceryninja;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity {
    EditText name, desc, num, picURL, available;
    String ACCESS_TOKEN, url="https://quiet-mountain-65416.herokuapp.com/";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        queue = Volley.newRequestQueue(this);
        ACCESS_TOKEN = getIntent().getExtras().getString("accessToken");
        name = findViewById(R.id.addName);
        desc = findViewById(R.id.addDesc);
        num = findViewById(R.id.addNum);
        picURL = findViewById(R.id.addUrl);
        available = findViewById(R.id.addAvailable);
        findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() || num.getText().toString().isEmpty()){
                    Toast.makeText(AddProduct.this, "Please specify a name and cost", Toast.LENGTH_SHORT).show();
                }
                else {
                    addAPI();
                }
            }
        });
    }

    private void addAPI() {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name.getText().toString());
            jsonBody.put("cost", num.getText().toString());
            jsonBody.put("picURL", picURL.getText().toString());
            jsonBody.put("desc", desc.getText().toString());
            jsonBody.put("available", available.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest req = new StringRequest(Request.Method.POST, url+"products?type=ADD", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddProduct.this, "ADDED", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(AddProduct.this, "Error Occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+ACCESS_TOKEN);
                return headers;
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