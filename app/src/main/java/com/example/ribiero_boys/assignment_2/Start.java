package com.example.ribiero_boys.assignment_2;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        default_start choose = new default_start();
        getSupportFragmentManager().beginTransaction().add(R.id.start_fragments, choose).commit();
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.start_fragments);
        if (f instanceof default_start) {
        //nothing for default_start fragment
        }else{
            super.onBackPressed();
        }
    }

}
