package com.sanchit.groceryninja;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Fruit extends AppCompatActivity {
    String TAG = "TAG";
    String name="";
    TextView fruitname,cost,desc;
    ImageView fruit;
    ConstraintLayout fruitBG;

    void logoutSeq(){
        startActivity(new Intent(Fruit.this, Onboarding.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);
        Bundle extras = getIntent().getExtras();
        fruitname = findViewById(R.id.fruitName);
        cost = findViewById(R.id.fruitCost);
        desc = findViewById(R.id.fruitDes);
        fruit = findViewById(R.id.fruitImage);
        fruitBG = findViewById(R.id.fruitBg);

        if (extras != null) {
            ArrayList<String> product = extras.getStringArrayList("product");
            fruitname.setText(product.get(0));
            cost.setText("$"+product.get(1));
            desc.setText(product.get(2));
            if (product.get(3).isEmpty()){
                fruit.setImageResource(R.drawable.banana);
            }
            else {
                Picasso.get().load(product.get(3)).into(fruit);
            }
            fruitBG.setBackgroundResource(R.drawable.b_gradient);
        }
    }

    void back(View view){
        finish();
    }
}
