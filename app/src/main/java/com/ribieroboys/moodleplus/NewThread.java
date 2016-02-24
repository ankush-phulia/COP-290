package com.ribieroboys.moodleplus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class NewThread extends AppCompatActivity {

    EditText title;
    EditText Description;
    Bundle threadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_thread);

        title=(EditText) findViewById(R.id.editText);
        Description=(EditText) findViewById(R.id.editText2);



    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
