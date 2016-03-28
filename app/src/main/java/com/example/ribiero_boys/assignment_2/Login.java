package com.example.ribiero_boys.assignment_2;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Login extends Fragment implements View.OnClickListener {

    String username,password;
    Button log;
    EditText editTextUsername,editTextPassword;
    CheckBox saveLoginCheckBox;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    boolean saveLogin;
    boolean savePass;

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        //get the ids of all elements
        getIDs(view);
        //for recalling previosly saved user/pass
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", 0);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        savePass = loginPreferences.getBoolean("savePass", false);
        if (saveLogin == true) {
            editTextUsername.setText(loginPreferences.getString("username", ""));
            saveLoginCheckBox.setChecked(true);
        }
        if (savePass == true) {
            editTextPassword.setText(loginPreferences.getString("password", ""));
        }

        log.setOnClickListener(this);

        return view;
    }

    private void getIDs(View V) {
        // fetch all UI elements
        log = (Button) V.findViewById(R.id.buttonLogin);
        editTextUsername = (EditText) V.findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) V.findViewById(R.id.editTextPassword);
        saveLoginCheckBox = (CheckBox) V.findViewById(R.id.saveLoginCheckBox);
    }

    private void endActivityAndStartNew(Intent i) {
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        if (v==log){

            final Intent intent=new Intent(getContext(),Main.class);
            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();
            Bundle infoToPass=new Bundle();
            infoToPass.putString("username",username);
            infoToPass.putString("password",password);
            intent.putExtra("info",infoToPass);

            if (saveLoginCheckBox.isChecked()) {
                //save the username in shared prefs
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", username);

                new AlertDialog.Builder(this.getContext(), AlertDialog.THEME_HOLO_LIGHT)
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
