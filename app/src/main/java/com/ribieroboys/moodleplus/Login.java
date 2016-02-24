package com.ribieroboys.moodleplus;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends Activity implements OnClickListener {

    String username,password;
    Button log;
    EditText editTextUsername,editTextPassword;
    CheckBox saveLoginCheckBox;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    Boolean saveUser;
    Boolean savePass;
    JSONObject userJSON;
    JSONArray courseJSON;
    Bundle infoToPass;
    ArrayList<String> courseListCodes;
    Intent intent;
    int attempts=0;

    //final String url = "http://tapi.cse.iitd.ernet.in:1805";
    final String url = "http://192.168.43.186:8000";
    public boolean success;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        getIDs();

        log.setOnClickListener(this);

        // Setup shared preferneces to store USername and pasword if required
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveUser = loginPreferences.getBoolean("saveUser", false);
        savePass = loginPreferences.getBoolean("savePass", false);

        //Retreive USername and passwords of previous session
        if (saveUser == true) {
            editTextUsername.setText(loginPreferences.getString("username", ""));
            saveLoginCheckBox.setChecked(true);
            if (savePass == true) {
                editTextPassword.setText(loginPreferences.getString("password", ""));
            }
        }

    }

    private void getIDs() {
        // fetch all UI elements
        log = (Button) findViewById(R.id.buttonLogin);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        intent = new Intent(Login.this, TimePass.class);
        success = false;
    }

    public void onClick(View view) {
        //clcick handler for login button
        if (view == log) {


            //extract username and password and put in the bundle to forward
            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();
            infoToPass = new Bundle();
            infoToPass.putString("user", username);
            infoToPass.putString("pass", password);
            infoToPass.putString("url", url);

            Retreive rtr=new Retreive(url,username,password,infoToPass,success,userJSON,intent,courseJSON,courseListCodes,Login.this,attempts);
            rtr.retreive_credentials();
            rtr.retreive_courses();
            rtr.retreive_notifications();
            rtr.retreive_grades();
            if(!success && rtr.attempts==0) {
                rtr.attempts++;
                return;
            }
            else if(!success && rtr.attempts>0) {
                //show error dialog on invalid user/pass
                new AlertDialog.Builder(Login.this,AlertDialog.THEME_HOLO_DARK)
                        .setTitle("Invalid Credentials")
                        .setMessage("Username or Password")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
            else{
                //pass things to the next activity
                intent.putExtra("nextIntent", "Main");
                intent.putExtra("URL", url);
                //offer to save the password if username is also set to tbe saved
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
    }

    private void endActivityAndStartNew(Intent i) {
        startActivity(i);
        this.finish();
    }

    @Override
    public void onBackPressed() {
    }

}
