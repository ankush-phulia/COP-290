package com.ribieroboys.moodleplus;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Login extends Activity implements OnClickListener {

    String username,password;
    Button log;
    EditText editTextUsername,editTextPassword;
    CheckBox saveLoginCheckBox;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    TextView wrongCredential;
    JSONObject userJSON;
    JSONArray courseJSON;
    Bundle infoToPass;
    ArrayList<String> courseListCodes;


    int count ;
    //final String url = "http://tapi.cse.iitd.ernet.in:1805";
    int keyvlaue = -1 ;
    final String url = "192.168.1.248:8000";
    public boolean success = false;

    public boolean isValid(String entry_no){

        // Using RegEx to check validity of entry no.
        return true ;
    }

    public String[] tokenize(String id){
        return id.split(":",2) ;
    }

    @Override



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        getIDs();

        log.setOnClickListener(this);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        count = loginPreferences.getInt("counter",0) ;



        int j = count ;
        while (j != 0) {
            String id = loginPreferences.getString(Integer.toString(j), "");
            if (!id.equals("")){
                String[] tokens = tokenize(id) ;
                editTextUsername.setText(tokens[0]);
                saveLoginCheckBox.setChecked(true);
                editTextPassword.setText(tokens[1]);
                break ;
            }
            j-- ;
        }

        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String user = editTextUsername.getText().toString() ;
                String user_id ;
                for (int i = 1 ; i <= count ; i++){
                    user_id = loginPreferences.getString(Integer.toString(i),"");
                    if (!user_id.equals("")){
                        String[] token = user_id.split(":",2) ;
                        if (user.equals(token[0])){
                            editTextPassword.setText(token[1]);
                            saveLoginCheckBox.setChecked(true);
                            break ;
                        }
                    }
                }
            }
        });
    }

    private void getIDs() {
        log = (Button) findViewById(R.id.buttonLogin);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        wrongCredential = (TextView) findViewById(R.id.wrongCredential);
        success = false;
    }

    public void onClick(View view) {

        if (view == log) {
            final Intent intent = new Intent(Login.this, TimePass.class);
            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();
            if (username.equals("") || password.equals("")) {
                if (username.equals("")) {
                    Toast.makeText(Login.this, "Username field Empty", Toast.LENGTH_LONG).show();
                    return  ;
                }
                else {
                    Toast.makeText(Login.this, "Password field Empty", Toast.LENGTH_LONG).show();
                    return ;
                }
            }


            infoToPass = new Bundle();
            infoToPass.putString("user", username);
            infoToPass.putString("pass", password);
            infoToPass.putString("url", url);

            if (!isValid(username)) {
                Toast.makeText(Login.this, "Invalid username", Toast.LENGTH_LONG).show();
                editTextUsername.setText("");
                editTextPassword.setText("");
                editTextUsername.requestFocus() ;
                return ;
            }

            // check for validity of credentials
            String loginURL = url + "/default/login.json?userid=" + username + "&password=" + password;
            StringRequest getReq = new StringRequest(Request.Method.GET,
                    loginURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // receive reply from server
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                System.out.println(jsonResponse.has("success"));
                                success = jsonResponse.get("success").toString().equals("true");
                                userJSON = jsonResponse.getJSONObject("user");

                                // put the information to pass
                                infoToPass.putString("firstName", userJSON.getString("first_name"));
                                System.out.println(userJSON.getString("first_name"));
                                infoToPass.putString("lastName", userJSON.getString("last_name"));
                                infoToPass.putString("entryNo", userJSON.getString("entry_no"));
                                infoToPass.putString("email", userJSON.getString("email"));
                                infoToPass.putInt("id", userJSON.getInt("id"));
                                infoToPass.putBoolean("isStudent", userJSON.getInt("type_") == 0);
                                intent.putExtra("loginResponse", infoToPass);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this, "Connection Error", Toast.LENGTH_LONG).show();
                        }
                });

            // send the request to RequestQ
            RequestQ.getInstance().addToRequestQ(getReq);

            if(!success) {
                return;
            }


            String courseURL = url + "/courses/list.json";
            StringRequest Req = new StringRequest(Request.Method.GET,
                    courseURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // receive reply from server
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                courseJSON = jsonResponse.getJSONArray("courses");

                                // Extract the course list codes
                                courseListCodes = new ArrayList<>();
                                for(int i=0; i<courseJSON.length(); i++) {
                                    JSONObject course = (JSONObject) courseJSON.get(i);
                                    courseListCodes.add(course.getString("code"));
                                    intent.putStringArrayListExtra("courseListCodes", courseListCodes);
                                }
                                intent.putExtra("/courses/list.json", response);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this, "Connection Error", Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQ.getInstance().addToRequestQ(Req);


            String notiURL = url + "/default/notifications.json";
            Req = new StringRequest(Request.Method.GET,
                    notiURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            intent.putExtra("/default/notifications.json", response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this, "Connection Error", Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQ.getInstance().addToRequestQ(Req);


            String gradesURL = url + "/default/grades.json";
            Req = new StringRequest(Request.Method.GET,
                    gradesURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            intent.putExtra("/default/grades.json", response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this, "Connection Error", Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQ.getInstance().addToRequestQ(Req);

            String userid ;
            for (int i = 1 ; i <= count ;i++ ){
                userid =  loginPreferences.getString(Integer.toString(i),"") ;
                if (!userid.equals("")){
                    String[] tokens = tokenize(userid) ;
                    if (tokens[0].equals(username)){
                        keyvlaue = i ;
                        break ;
                    }
                }
            }

            intent.putExtra("nextIntent", "Main");
            intent.putExtra("URL", url);
            intent.putExtra("fromNewThread", false);

            if (saveLoginCheckBox.isChecked()) {
                if (keyvlaue == -1) {
                    loginPrefsEditor.putInt("counter", (++count));
                    loginPrefsEditor.putString(Integer.toString(count), username + ":");
                    loginPrefsEditor.commit();
                }
                new AlertDialog.Builder(Login.this,AlertDialog.THEME_HOLO_DARK)
                        .setTitle("Remember Password")
                        .setMessage("Should the Password be saved?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (keyvlaue != -1) {
                                    loginPrefsEditor.putString(Integer.toString(keyvlaue),username + ":") ;
                                    loginPrefsEditor.commit() ;
                                }

                                endActivityAndStartNew(intent);
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                    if (keyvlaue != -1){
                                        loginPrefsEditor.putString(Integer.toString(keyvlaue), username + ":" + password);
                                        loginPrefsEditor.commit();
                                    }else {
                                        loginPrefsEditor.putString(Integer.toString(count), username + ":" + password);
                                        loginPrefsEditor.commit();
                                    }


                                endActivityAndStartNew(intent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else {
                if (keyvlaue != -1) {
                    loginPrefsEditor.putString(Integer.toString(keyvlaue),"") ;
                    loginPrefsEditor.commit();
                }

                endActivityAndStartNew(intent);
            }

        }
    }

    private void endActivityAndStartNew(Intent i) {
        startActivity(i);
        this.finish();
    }

    @Override
    public void onBackPressed() {
    }

}
