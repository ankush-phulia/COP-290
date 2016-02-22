package com.ribieroboys.moodleplus;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
    Bundle infoToPass;

    //final String url = "http://tapi.cse.iitd.ernet.in:1805";
    final String url = "127.0.0.1:8000";
    public boolean success = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

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
    }

    public void onClick(View view) {
        if (view == log) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextUsername.getWindowToken(), 0);

            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();

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
                                success = jsonResponse.get("success").toString().equals("true");
                                userJSON = jsonResponse.getJSONObject("user");
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
                wrongCredential.setText("Invalid Credentials");
                return;
            } else {
                wrongCredential.setText("");
                // put the information to pass
                infoToPass.putString("user", username);
                infoToPass.putString("pass", password);
                infoToPass.putString("url", url);
                try {
                    infoToPass.putString("firstName", userJSON.getString("first_name"));
                    infoToPass.putString("lastName", userJSON.getString("last_name"));
                    infoToPass.putString("entryNo", userJSON.getString("entry_no"));
                    infoToPass.putString("email", userJSON.getString("email"));
                    infoToPass.putInt("id", userJSON.getInt("id"));
                    infoToPass.putBoolean("isStudent", userJSON.getInt("type_") == 0);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            final Intent intent = new Intent(Login.this, Main.class);
            intent.putExtra("loginResponse", infoToPass);

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
