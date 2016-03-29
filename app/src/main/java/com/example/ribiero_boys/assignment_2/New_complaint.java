package com.example.ribiero_boys.assignment_2;

import android.content.Intent;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class New_complaint extends AppCompatActivity{

    Button submit;
    String from;
    AutoCompleteTextView to;
    EditText topic;
    EditText descr;
    Spinner scope;
    TextView scp;
    String url="http://192.168.43.186:8080/addcomplaint";
    ArrayAdapter<String> dataAdapter;


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
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        from = getIntent().getBundleExtra("info").getString("username"," ");
        getIDs();

        final Intent intent = new Intent(getApplicationContext(),Main.class);
        intent.putExtra("info", getIntent().getBundleExtra("info"));
        intent.putExtra("spl", getIntent().getBooleanExtra("spl", false));

        submit=(Button)findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Map<String, String> newcompl = new HashMap<String, String>();
                //url += "?receiverusername=" + from + "&topic=" + topic.getText().toString() + "&description=" + descr.getText().toString();
                newcompl.put("receiverusername", to.getText().toString());
                newcompl.put("topic", topic.getText().toString());
                newcompl.put("description", descr.getText().toString());
                scp=(TextView)scope.getChildAt(0);

                switch(scp.getText().toString()){
                    case "Personal":
                        newcompl.put("type",String.valueOf(3));
                        //url += "&type=3";
                        break;
                    case "Hostel Level":
                        newcompl.put("type",String.valueOf(1));
                        //url += "&type=1";
                        break;
                    case "Department Level":
                        newcompl.put("type",String.valueOf(0));
                        //url += "&type=0";
                        break;
                    case "Institute Level":
                        newcompl.put("type",String.valueOf(2));
                        //url += "&type=2";
                        break;
                }

                StringRequest postReq = new StringRequest(Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //receive reply from server and refresh main
                        Toast.makeText(getApplicationContext(), "Complaint Lodged", Toast.LENGTH_LONG).show();
                        end();
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    }
                }) {
                    // putting the parameters as key-value pairs to pass
                    @Override
                    public Map<String, String> getParams() {
                        return newcompl;
                    }

                };
                //Add the team details to global request queue
                RequestQ.getInstance().addToRequestQ(postReq);
            }
        });


    }

    public void getIDs(){
        to=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        topic=(EditText)findViewById(R.id.editText2);
        descr=(EditText)findViewById(R.id.editText3);
        scope=(Spinner)findViewById(R.id.spinnerScope);
    }

    public void end(){
        this.finish();
    }

}