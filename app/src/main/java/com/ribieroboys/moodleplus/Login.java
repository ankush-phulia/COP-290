package com.ribieroboys.moodleplus;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    Boolean saveUser;
    Boolean savePass;
    TextView wrongCredential;
    JSONObject userJSON;
    JSONArray courseJSON;
    Bundle infoToPass;
    ArrayList<String> courseListCodes;

    //final String url = "http://tapi.cse.iitd.ernet.in:1805";
    final String url = "http://192.168.1.248:8000";
    public boolean success;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        getIDs();

        log.setOnClickListener(this);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveUser = loginPreferences.getBoolean("saveUser", false);
        savePass = loginPreferences.getBoolean("savePass", false);

        if (saveUser == true) {
            editTextUsername.setText(loginPreferences.getString("username", ""));
            saveLoginCheckBox.setChecked(true);
            if (savePass == true) {
                editTextPassword.setText(loginPreferences.getString("password", ""));
            }
        }

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

            infoToPass = new Bundle();
            infoToPass.putString("user", username);
            infoToPass.putString("pass", password);
            infoToPass.putString("url", url);

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

            intent.putExtra("nextIntent", "Main");
            intent.putExtra("URL", url);
            intent.putExtra("fromNewThread", false);

            if (saveLoginCheckBox.isChecked()) {
                loginPrefsEditor.putBoolean("saveUser", true);
                loginPrefsEditor.putString("username", username);
                loginPrefsEditor.commit();
                new AlertDialog.Builder(Login.this,AlertDialog.THEME_HOLO_DARK)
                        .setTitle("Remember Password")
                        .setMessage("Should the Password be saved?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                loginPrefsEditor.putBoolean("savePass", false);
                                loginPrefsEditor.commit();
                                endActivityAndStartNew(intent);
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                loginPrefsEditor.putBoolean("savePass", true);
                                loginPrefsEditor.putString("password", password);
                                loginPrefsEditor.commit();
                                endActivityAndStartNew(intent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
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
