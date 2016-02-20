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

public class Login extends Activity implements OnClickListener {

    String username,password;
    Button log;
    EditText editTextUsername,editTextPassword;
    CheckBox saveLoginCheckBox;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    Boolean saveUser;
    Boolean savePass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        log = (Button)findViewById(R.id.buttonLogin);
        log.setOnClickListener(this);
        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
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

    public void onClick(View view) {
        if (view == log) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextUsername.getWindowToken(), 0);

            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();
            final Intent intent=new Intent(Login.this, Dashboard.class);
            intent.putExtra("user",username);
            intent.putExtra("pass", password);
            boolean sp;
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
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                loginPrefsEditor.putBoolean("savePass", true);
                                loginPrefsEditor.putString("password", password);
                                loginPrefsEditor.commit();
                                startActivity(intent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
                startActivity(intent);
            }

        }
    }

}
