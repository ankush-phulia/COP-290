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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends Fragment implements View.OnClickListener {

    String username,password;
    Button log;
    String url="http://10.42.0.1:8080/login";
    EditText editTextUsername,editTextPassword;
    CheckBox saveLoginCheckBox;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    Bundle infoToPass=new Bundle();
    boolean saveLogin;
    boolean savePass;
    boolean spl;
    String name;
    Integer type;

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
                                create_loginRequest(intent);
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                loginPrefsEditor.putBoolean("savePass", true);
                                loginPrefsEditor.putString("password", password);
                                loginPrefsEditor.commit();
                                create_loginRequest(intent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
                create_loginRequest(intent);
            }

        }

    }

    //login request posts parameters and gives response
    public void create_loginRequest(final Intent intent){
        StringRequest postReq = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Integer success = -2;
                //receive reply from server and display it to the user as appropriate
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    success = (Integer)jsonResponse.get("success");
                    type=(Integer)jsonResponse.get("type");
                    name=jsonResponse.getString("name");
                    spl=jsonResponse.getBoolean("isSpecial");
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    //check the response messages
                    switch(success){
                        case -2://not registered
                            new AlertDialog.Builder(getContext(),AlertDialog.THEME_HOLO_LIGHT)
                                    .setTitle("User Not Registered")
                                    .setMessage("This username is not Registered")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            break;
                        case -1://invalid password
                            new AlertDialog.Builder(getContext(),AlertDialog.THEME_HOLO_LIGHT)
                                    .setTitle("Invalid Password")
                                    .setMessage("Please Enter Correct Password")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            editTextPassword.setText("");
                                            editTextPassword.requestFocus();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            break;
                        case 0://sign up pending
                            new AlertDialog.Builder(getContext(),AlertDialog.THEME_HOLO_LIGHT)
                                    .setTitle("User Not Registered")
                                    .setMessage("Sign Up Request of this User is still being Processed")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            break;
                        case 1://valid credentials
                            infoToPass.putString("username", username);
                            infoToPass.putString("password",password);
                            infoToPass.putString("name", name);
                            infoToPass.putInt("type", type);
                            infoToPass.putString("json",response);
                            intent.putExtra("spl",spl);
                            intent.putExtra("info", infoToPass);
                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_LONG).show();
                            endActivityAndStartNew(intent);
                            break;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
            }
        }) {

            // putting the parameters as key-value pairs to pass
            @Override
            public Map<String, String> getParams() {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password",password);
                return params;
            }

        };
        //Add the team details to global request queue
        RequestQ.getInstance().addToRequestQ(postReq);
    }
}
