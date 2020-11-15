package com.example.lostandfoundoxford;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button found,lost;
    AlphaAnimation click = new AlphaAnimation(1F, 0.8F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lost = findViewById(R.id.Lost);
        found = findViewById(R.id.Found);


        found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(click);
                takeImage();
            }
        });

        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(click);
                findItem();
            }
        });
    }
    void takeImage(){
        Intent i = new Intent(getApplicationContext(),CameraCaptureActivity.class);
        startActivity(i);
    }

    void findItem() {
        Intent i = new Intent(getApplicationContext(),MapActivity.class);
        startActivity(i);
    }
}