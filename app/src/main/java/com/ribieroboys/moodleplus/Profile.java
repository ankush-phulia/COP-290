package com.ribieroboys.moodleplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    String user;
    String pass;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent data = getIntent();
        user = data.getStringExtra("user");
        pass = data.getStringExtra("pass");
        TextView username= (TextView) findViewById(R.id.textView11);
        username.setText(user);
        TextView fullname= (TextView) findViewById(R.id.textView13);
        fullname.setText(" ");

        back = (Button) findViewById(R.id.button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    public void end(){
        this.finish();
    }
}
