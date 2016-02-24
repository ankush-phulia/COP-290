package com.ribieroboys.moodleplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
/**
 * Created by nitin on 22/2/16.
 */
public class TimePass extends AppCompatActivity {
    int time = 700;    // in milliseconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent next = new Intent(getIntent());
                next.setClass(TimePass.this, Main.class);
                startActivity(next);
                finish();
            }
        }, time);

    }
}
