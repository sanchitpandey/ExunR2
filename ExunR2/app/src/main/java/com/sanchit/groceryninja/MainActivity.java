package com.sanchit.groceryninja;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        Intent i = new Intent(MainActivity.this, Fruit.class);
        if (view.getId()==R.id.bShop){
            i.putExtra("fruit", "b");
        }
        if (view.getId()==R.id.gShop){
            i.putExtra("fruit", "g");
        }
        if (view.getId()==R.id.aShop){
            i.putExtra("fruit", "a");
        }
        startActivity(i);
    }
}
