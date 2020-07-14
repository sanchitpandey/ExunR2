package com.sanchit.groceryninja;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Fruit extends AppCompatActivity {
    String TAG = "TAG";
    String name="";
    TextView fruitname,cost,desc;
    ImageView fruit;
    ConstraintLayout fruitBG;

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
            name = extras.getString("fruit");
            if (name.equals("a")){
                fruitname.setText("Apple");
                cost.setText("$4.99");
                desc.setText("Fresh! lorem ipsum dolor sit amet. lorem ipsum dolor sit amet. lorem ipsum dolor sit amet.");
                fruit.setImageResource(R.drawable.apple);
                fruitBG.setBackgroundResource(R.drawable.a_gradient);
            }
            else if (name.equals("g")) {
                fruitname.setText("Avocado");
                cost.setText("$2.99");
                desc.setText("Fresh! lorem ipsum dolor sit amet. lorem ipsum dolor sit amet. lorem ipsum dolor sit amet.");
                fruit.setImageResource(R.drawable.avocado1);
                fruitBG.setBackgroundResource(R.drawable.g_gradient);
            }
            else {
                fruitname.setText("Fresh Banana");
                cost.setText("$1.99");
                fruit.setImageResource(R.drawable.banana);
                fruitBG.setBackgroundResource(R.drawable.b_gradient);
                desc.setText("Fruits and vegetables contain many vitamins and minerals that are good for your health. These include vitamins A,C and E, magnesium, zinc, phosphorus.");
            }
        }
    }

    void back(View view){
        finish();
    }
}
