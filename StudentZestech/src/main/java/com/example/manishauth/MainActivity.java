package com.example.manishauth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.manishauth.firsttimepreview.FirstActivity;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=1500;
    ImageView spalshimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spalshimg= findViewById(R.id.bulb);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, FirstActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

        spalshimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spalshimg.setImageResource(R.drawable.blubglow);
            }
        });
    }
}
