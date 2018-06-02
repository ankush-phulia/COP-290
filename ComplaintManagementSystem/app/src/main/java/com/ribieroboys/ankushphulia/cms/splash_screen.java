package com.ribieroboys.ankushphulia.cms;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splash_screen extends AppCompatActivity {

    int time = 1000;    // in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Handler to delay movement to Login Page and display splash for 'time' ms

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash_screen.this,Start.class);
                startActivity(i);
                finish();
            }
        }, time);

    }
}
