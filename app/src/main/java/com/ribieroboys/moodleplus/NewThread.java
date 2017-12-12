package com.ribieroboys.moodleplus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NewThread extends Activity implements View.OnClickListener {

    EditText title;
    EditText Description;
    Button createThread;
    String courseCode;
    String url;

    List<String> Courses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_thread);

        title = (EditText) findViewById(R.id.editText);
        Description = (EditText) findViewById(R.id.editText2);
        createThread = (Button) findViewById((R.id.post));
        createThread.setOnClickListener(this);

        courseCode = getIntent().getStringExtra("courseCode");
        url = getIntent().getStringExtra("URL");
        Courses = getIntent().getStringArrayListExtra("Courses");
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void onClick(View view) {
        if (view == createThread) {
            String loginURL = url + "/threads/new.json?title=" + title.getText() + "&description=" + Description.getText() + "&course_code=" + courseCode;
            StringRequest getReq = new StringRequest(Request.Method.GET,
                    loginURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // receive reply from server
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                System.out.println(jsonResponse.get("success").toString().compareTo("true"));
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(NewThread.this, "Connection Error", Toast.LENGTH_LONG).show();
                        }
                    });

            // send the request to RequestQ
            RequestQ.getInstance().addToRequestQ(getReq);


            Intent backToMain = new Intent(getIntent());
            backToMain.setClass(NewThread.this, Main.class);
            backToMain.putExtra("fromNewThread", true);
            startActivity(backToMain);
            this.finish();
        }
    }
}
