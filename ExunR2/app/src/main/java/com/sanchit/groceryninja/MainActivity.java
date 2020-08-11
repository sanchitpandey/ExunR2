package com.sanchit.groceryninja;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.JWT;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String TAG = "TAG", ACCESS_TOKEN="", url ="https://quiet-mountain-65416.herokuapp.com/", name="User";
    RequestQueue queue;
    JSONArray fruits;
    ArrayList<Product> fruitList;
    ProductAdaptor adapter;
    private final String SHARED_PREFS = "sharedPrefs";
    JWT jwt;

    void saveToken(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("accessToken", ACCESS_TOKEN).apply();
    }

    void loadToken(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        ACCESS_TOKEN = sharedPreferences.getString("accessToken", "");
    }

    void TokenProcess(){
        loadToken();
        if (ACCESS_TOKEN.equals("")){
            saveToken();
            finish();
        }

    }

    void mapFruits(){
        if (fruitList!=null){
            fruitList.clear();
        }
        if (fruits.length()>0){
            for (int i=0; i<fruits.length(); i++){
                try {
                    JSONObject y = (JSONObject) fruits.get(i);
                    Product x = new Product(y.get("name").toString(), y.get("cost").toString(), y.get("desc").toString(), y.get("picURL").toString(), y.get("available").toString());
                    fruitList.add(x);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void logoutSeq(){
        if (!ACCESS_TOKEN.equals("")){
            ACCESS_TOKEN = "";
            saveToken();
        }
        startActivity(new Intent(MainActivity.this, Onboarding.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        TokenProcess();
    }

    void callAPI(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+"products",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray res = new JSONArray(response);
                            fruits = res;
                            mapFruits();
                            update();
                            System.out.println(((JSONObject)res.get(0)).get("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");
            }
        });
        queue.add(stringRequest);
    }

    private void update() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        fruitList = new ArrayList<>();
        TokenProcess();
        jwt = new JWT(ACCESS_TOKEN);
        name = jwt.getClaim("name").asString();

        // Recycler View
        RecyclerView recyclerView = findViewById(R.id.fruitCards);
        adapter = new ProductAdaptor(fruitList, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ((TextView)findViewById(R.id.hey_noah)).setText("Hey " + name+",");
        findViewById(R.id.sidebar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutSeq();
            }
        });

        callAPI();

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete a product").setMessage("Enter a product name");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAPI(input.getText().toString());
                    }
                });
                builder.show();
            }
        });

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddProduct.class);
                i.putExtra("accessToken", ACCESS_TOKEN);
                startActivity(i);
            }
        });
    }

    private void deleteAPI(String name) {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest req = new StringRequest(Request.Method.POST, url+"products?type=DELETE", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Error Occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
