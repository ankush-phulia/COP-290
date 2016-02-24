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
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends Activity implements OnClickListener {

    String username,password;
    Button log;
    EditText editTextUsername,editTextPassword;
    CheckBox saveLoginCheckBox;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;

    JSONObject userJSON;
    JSONArray courseJSON;
    Bundle infoToPass;
    ArrayList<String> courseListCodes;
    Intent intent;
    int attempts=0;


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
        intent = new Intent(Login.this, TimePass.class);
        success = false;
    }

    public void onClick(View view) {
        //clcick handler for login button
        if (view == log) {

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

            infoToPass = new Bundle();
            infoToPass.putString("user", username);
            infoToPass.putString("pass", password);
            infoToPass.putString("url", url);


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

            intent.putExtra("nextIntent", "Main");
            intent.putExtra("URL", url);
            intent.putExtra("fromNewThread", false);

            if (saveLoginCheckBox.isChecked()) {
                if (keyvlaue == -1) {
                    loginPrefsEditor.putInt("counter", (++count));
                    loginPrefsEditor.putString(Integer.toString(count), username + ":");
                    loginPrefsEditor.commit();
                }
                new AlertDialog.Builder(Login.this, AlertDialog.THEME_HOLO_DARK)
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
                        });


                Retreive rtr = new Retreive(url, username, password, infoToPass, success, userJSON, intent, courseJSON, courseListCodes, Login.this, attempts);
                rtr.retreive_credentials();
                rtr.retreive_courses();
                rtr.retreive_notifications();
                rtr.retreive_grades();
                if (!success && rtr.attempts == 0) {
                    rtr.attempts++;
                    return;
                } else if (!success && rtr.attempts > 0) {
                    //show error dialog on invalid user/pass
                    new AlertDialog.Builder(Login.this, AlertDialog.THEME_HOLO_DARK)
                            .setTitle("Invalid Credentials")
                            .setMessage("Username or Password")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else {
                    //pass things to the next activity
                    intent.putExtra("nextIntent", "Main");
                    intent.putExtra("URL", url);
                    //offer to save the password if username is also set to tbe saved
                    if (saveLoginCheckBox.isChecked()) {
                        if (keyvlaue != -1) {
                            loginPrefsEditor.putString(Integer.toString(keyvlaue), "");
                            loginPrefsEditor.commit();
                        }
                        new AlertDialog.Builder(Login.this, AlertDialog.THEME_HOLO_DARK)
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
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                        endActivityAndStartNew(intent);
                    }
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
