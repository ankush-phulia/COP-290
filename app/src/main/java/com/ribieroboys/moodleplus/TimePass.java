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
                final Intent prevIntent = getIntent();
                String nextIntent = prevIntent.getStringExtra("nextIntent");
                prevIntent.removeExtra("nextIntent");

                Intent next = new Intent(prevIntent);
                Class cls = Login.class;
                switch (nextIntent) {
                    case "Main":
                        cls = Main.class;
                        next.setClass(TimePass.this, cls);
                        break;
                    default:
                        cls = Main.class;
                        next.setClass(TimePass.this, cls);
                }

                startActivity(next);
                finish();
            }
        }, time);

    }
}
