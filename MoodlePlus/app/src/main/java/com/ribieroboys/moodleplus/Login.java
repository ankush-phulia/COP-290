package com.ribieroboys.moodleplus;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends Activity implements OnClickListener {

    String username,password;
    Button log;
    EditText editTextUsername,editTextPassword;
    CheckBox saveLoginCheckBox;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    final Context context=this;
    JSONObject userJSON;
    JSONArray courseJSON;
    Bundle infoToPass;
    ArrayList<String> courseListCodes;
    Intent intent;
    int attempts=0;

    int count ;
    int keyvlaue = -1 ;

    final String url = "http://192.168.43.186:8000";
    public boolean success = false;

    public String[] tokenize(String id){
        return id.split(":",2) ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        getIDs();

        log.setOnClickListener(this);

        // Setup shared preferneces to store USername and pasword if required
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
            //listener on password for autocomplete
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
        // fetch all UI elements
        log = (Button) findViewById(R.id.buttonLogin);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        success = false;
    }

    public void onClick(View view) {
        //clcick handler for login button
        if (view == log) {
            intent = new Intent(Login.this, TimePass.class);

            //extract username and password and put in the bundle to forward
            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();

            if (username.equals("") || password.equals("")) {
                if (username.equals("")) {
                    Toast.makeText(Login.this, "Username field Empty", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(Login.this, "Password field Empty", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //add information to bundle to pass
            infoToPass = new Bundle();
            infoToPass.putString("user", username);
            infoToPass.putString("pass", password);
            infoToPass.putString("url", url);

            // store user ids
            String userid;
            for (int i = 1; i <= count; i++) {
                userid = loginPreferences.getString(Integer.toString(i), "");
                if (!userid.equals("")) {
                    String[] tokens = tokenize(userid);
                    if (tokens[0].equals(username)) {
                        keyvlaue = i;
                        break;
                    }
                }
            }

            intent.putExtra("URL", url);

            retreive_credentials();

            if (!success && attempts == 1) {
                attempts++;
                return;
            } else if (!success && attempts > 1) {
                //show error dialog on invalid user/pass
                new AlertDialog.Builder(Login.this, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Invalid Credentials")
                        .setMessage("Either Username or Password is Incorrect")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {return;
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

            } else {
                retreive_courses();
                retreive_notifications();
                retreive_grades();
                //offer to save the password if username is also set to tbe saved
                if (saveLoginCheckBox.isChecked()) {
                    if (keyvlaue != -1) {
                        loginPrefsEditor.putString(Integer.toString(keyvlaue), "");
                        loginPrefsEditor.commit();
                    }
                    new AlertDialog.Builder(Login.this, AlertDialog.THEME_HOLO_LIGHT)
                            .setTitle("Remember Password")
                            .setMessage("Should the Password be saved?")
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    if (keyvlaue != -1) {
                                        loginPrefsEditor.putString(Integer.toString(keyvlaue), username + ":");
                                        loginPrefsEditor.commit();
                                    }
                                        endActivityAndStartNew(intent);
                                    }
                                })
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                  if (keyvlaue != -1) {
                                      loginPrefsEditor.putString(Integer.toString(keyvlaue), username + ":" + password);
                                      loginPrefsEditor.commit();
                                  } else {
                                      loginPrefsEditor.putString(Integer.toString(count), username + ":" + password);
                                      loginPrefsEditor.commit();
                                  }
                                      endActivityAndStartNew(intent);
                                }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                    endActivityAndStartNew(intent);
                }
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

    public void retreive_credentials(){
        // check for validity of credentials
        attempts++;
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
                        Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

        // send the request to global request queue
        RequestQ.getInstance().addToRequestQ(getReq);
    }

    public void retreive_courses(){
        //retreive course lists
        String courseURL = url + "/courses/list.json";
        StringRequest Req = new StringRequest(Request.Method.GET,
                courseURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // receive reply from server
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            System.out.println(response);
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
                        Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQ.getInstance().addToRequestQ(Req);
    }

    public void retreive_notifications(){
        //retrieve notifications
        String notiURL = url + "/default/notifications.json";
        StringRequest Req = new StringRequest(Request.Method.GET,
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
                        Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQ.getInstance().addToRequestQ(Req);
    }

    public void retreive_grades(){
        //retrieve grades
        String gradesURL = url + "/default/grades.json";
        StringRequest Req = new StringRequest(Request.Method.GET,
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
                        Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQ.getInstance().addToRequestQ(Req);
    }

}
