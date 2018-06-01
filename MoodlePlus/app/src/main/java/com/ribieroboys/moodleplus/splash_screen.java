package com.ribieroboys.moodleplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class splash_screen extends AppCompatActivity {

    int time = 1500; // in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        /*        ImageView iitdlog=(ImageView) findViewById(R.id.iitdlogo);
        iitdlog.setImageResource(R.mipmap.iitd_logo);
        ImageView moodlelog=(ImageView) findViewById(R.id.moodlelogo);
        moodlelog.setImageResource(R.mipmap.moodle);*/

        // Handler to delay movement to Login Page

        new Handler()
                .postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(splash_screen.this, Login.class);
                                startActivity(i);
                                finish();
                            }
                        },
                        time);
    }
}
