package com.ribieroboys.moodleplus;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends Activity implements OnClickListener {

    String username,password;
    Button log;
    EditText editTextUsername,editTextPassword;
    CheckBox saveLoginCheckBox;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    int count ;
    //final String url = "http://tapi.cse.iitd.ernet.in:1805";
    int keyvlaue = -1 ;
    final String url = "127.0.0.1:8000";
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        log = (Button) findViewById(R.id.buttonLogin);
        log.setOnClickListener(this);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
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

    public void onClick(View view) {

        if (view == log) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextUsername.getWindowToken(), 0);

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

            if (!isValid(username)) {
                Toast.makeText(Login.this, "Invalid username", Toast.LENGTH_LONG).show();
                editTextUsername.setText("");
                editTextPassword.setText("");
                editTextUsername.requestFocus() ;
                return ;
            }
            // check for validity of credentials
            /*String tempURL = url + "/default/login.json?userid=" + username + "&password=" + password;
            StringRequest getReq = new StringRequest(Request.Method.GET,
                    tempURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // receive reply from server
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                success = jsonResponse.get("success").toString().equals("true");
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

            RequestQ.getInstance().addToRequestQ(getReq);
            if(!success)
                return;*/

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

            final Intent intent = new Intent(Login.this, Main.class);

            intent.putExtra("user",username);
            intent.putExtra("pass", password);

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
