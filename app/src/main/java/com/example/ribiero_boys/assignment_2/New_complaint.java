package com.example.ribiero_boys.assignment_2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class New_complaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    Button submit;
    String from;
    AutoCompleteTextView to;
    EditText topic;
    EditText descr;
    Spinner scope;
    TextView scp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);

        //get the spinner and populate it
        Spinner spinner = (Spinner) findViewById(R.id.spinnerScope);
        List<String> categories = new ArrayList<String>();
        categories.add("Personal");
        categories.add("Hostel Level");
        categories.add("Department Level");
        categories.add("Institute Level");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);

        from=getIntent().getBundleExtra("info").getString("username"," ");
        getIDs();

        submit=(Button)findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==submit){
            Bundle newcompl=new Bundle();
            newcompl.putString("to",to.getText().toString());
            newcompl.putString("from",from);
            newcompl.putString("topic",topic.getText().toString());
            newcompl.putString("descr",descr.getText().toString());
            scp=(TextView)scope.getSelectedView();
            newcompl.putString("scope",scp.getText().toString());
            Toast.makeText(this, "Complaint Made", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    public void getIDs(){
        to=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        topic=(EditText)findViewById(R.id.editText2);
        descr=(EditText)findViewById(R.id.editText3);
        scope=(Spinner)findViewById(R.id.spinnerScope);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
