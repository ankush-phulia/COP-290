package com.example.ribiero_boys.assignment_2;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sign_up extends Fragment implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    Button signup;
    String url="10.42.0.1:8080/signup";
    EditText uname;
    EditText name;
    EditText pass;
    EditText cpass;
    CheckBox spl;
    Spinner designtion;

    public Sign_up() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);

        //get the spinner and populate it
        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerDesignation);
        List<String> categories = new ArrayList<String>();
        categories.add("Student");
        categories.add("Faculty");
        categories.add("Employee");
        categories.add("Others");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);

        signup=(Button)view.findViewById(R.id.buttonRegister);
        signup.setOnClickListener(this);

        uname=(EditText)view.getRootView().findViewById(R.id.editTextUsername2);
        name=(EditText)view.getRootView().findViewById(R.id.editTextName);
        pass=(EditText)view.getRootView().findViewById(R.id.editTextPassword2);
        cpass=(EditText)view.getRootView().findViewById(R.id.editTextPasswordconf);
        spl=(CheckBox)view.getRootView().findViewById(R.id.checkBoxSpecial);
        designtion=(Spinner)view.getRootView().findViewById(R.id.spinnerDesignation);
        add_watchers(uname, name, pass, cpass, view);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        LinearLayout otherfields=(LinearLayout)view.getRootView().getRootView().findViewById(R.id.Extras);
        otherfields.setOrientation(LinearLayout.VERTICAL);
        //instantiate format generator
        designation_format Format=new designation_format();
        Format.cont=getContext();

        if (otherfields.getChildCount()>0){
            otherfields.removeAllViews();
        }
        switch (position){
            case 0:
                //student selected
                Format.populate_student(otherfields);
                break;
            case 1:
                //faculty selected
                Format.populate_faculty(otherfields);
                break;
            case 2:
                //employee selected
                Format.populate_employee(otherfields);
                break;
            case 3:
                //others selected
                Format.populate_other(otherfields);
                break;
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void add_watchers (final TextView t1,final TextView t2, final TextView t3,final TextView t4,final View v){
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                t1.setHintTextColor(Integer.parseInt(String.valueOf(Color.GRAY)));
                t1.setHint("Enter your Username");
                TextView asterisk=(TextView) v.findViewById(R.id.textView21);
                if (t1.getText().length()>0){
                    asterisk.setText("");
                }
                else{
                    asterisk.setText("*");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        t2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                t2.setHintTextColor(Integer.parseInt(String.valueOf(Color.GRAY)));
                t2.setHint("Enter your Full Name");
                TextView asterisk=(TextView) v.findViewById(R.id.textView31);
                if (t2.getText().length()>0){
                    asterisk.setText("");
                }
                else{
                    asterisk.setText("*");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        t3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView asterisk=(TextView) v.findViewById(R.id.textView41);
                t3.setHintTextColor(Integer.parseInt(String.valueOf(Color.GRAY)));
                t3.setHint("Enter your Password");
                if (t3.getText().length()>0){
                    asterisk.setText("");
                }
                else{
                    asterisk.setText("*");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        t4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                t4.setHintTextColor(Integer.parseInt(String.valueOf(Color.GRAY)));
                t4.setHint("Confirm your Password");
                TextView asterisk=(TextView) v.findViewById(R.id.textView51);
                if (t4.getText().length()>0){
                    asterisk.setText("");
                }
                else{
                    asterisk.setText("*");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        //store parameters
        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", uname.getText().toString());
        params.put("name",name.getText().toString());
        params.put("password",pass.getText().toString());
        if (spl.isChecked()){
            params.put("isSpecial", "true");
        }
        else{
            params.put("isSpecial", "false");
        }

        //if button is clicked
        if (v==signup){

            //according to the designation
            switch(designtion.getSelectedItemPosition()){
                case 0:
                    Spinner dep=(Spinner)v.getRootView().findViewById(R.id.dep1);
                    Spinner hostel=(Spinner)v.getRootView().findViewById(R.id.hostel1);
                    AutoCompleteTextView por=(AutoCompleteTextView)v.getRootView().findViewById(R.id.por1);
                    EditText room=(EditText)v.getRootView().findViewById(R.id.room);
                    TextView t =(TextView)(dep.getChildAt(0));
                    TextView t2 =(TextView)(hostel.getChildAt(0));
                    params.put("type","0");
                    params.put("hostel",t2.getText().toString());
                    params.put("roomNo",room.getText().toString());
                    params.put("department",t.getText().toString());
                    params.put("por",por.getText().toString());

                    break;
                case 1:
                    Spinner dep2=(Spinner)v.getRootView().findViewById(R.id.dep2);
                    EditText addr_fac_employee=(EditText)v.getRootView().findViewById(R.id.address1);
                    AutoCompleteTextView addnlcharge=(AutoCompleteTextView)v.getRootView().findViewById(R.id.por2);
                    Spinner hostel2=(Spinner)v.getRootView().findViewById(R.id.hostel2);
                    TextView t3 =(TextView)(dep2.getChildAt(0));
                    TextView t4 =(TextView)(hostel2.getChildAt(0));
                    params.put("type","1");
                    params.put("department",t3.getText().toString());
                    params.put("homeAddress",addr_fac_employee.getText().toString());
                    params.put("additionalCharge",addnlcharge.getText().toString());
                    params.put("warden",t4.getText().toString());

                    break;
                case 2:
                    AutoCompleteTextView work=(AutoCompleteTextView)v.getRootView().findViewById(R.id.work);
                    EditText addr_employee=(EditText)v.getRootView().findViewById(R.id.address2);
                    params.put("type","2");
                    params.put("work",work.getText().toString());
                    params.put("address",addr_employee.getText().toString());

                    break;
                case 3:
                    EditText addr_others=(EditText)v.getRootView().findViewById(R.id.address3);
                    params.put("type","3");
                    params.put("address",addr_others.getText().toString());

                    break;
            }

            //check validly filled
            if (name.getText().toString().equals("")){
                name.setHint("This is a required field");
                name.setHintTextColor(Integer.parseInt(String.valueOf(Color.RED)));
                name.requestFocus();
            }
            else if (uname.getText().toString().equals("")){
                uname.setHint("This is a required field");
                uname.setHintTextColor(Integer.parseInt(String.valueOf(Color.RED)));
                uname.requestFocus();
            }
            else if (pass.getText().toString().equals("")){
                pass.setHint("This is a required field");
                pass.setHintTextColor(Integer.parseInt(String.valueOf(Color.RED)));
                pass.requestFocus();
            }
            //check if password confirmed
            else if (!pass.getText().toString().equals(cpass.getText().toString())){
                new AlertDialog.Builder(getContext(),AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Can't Confirm Paaword")
                        .setMessage("Password and Confirm Password Fields mismatch")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                pass.setText("");
                                cpass.setText("");
                                pass.requestFocus();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            //all good now make request
            else{
                StringRequest postReq = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                boolean success = false;
                                //receive reply from server and display it to the user as appropriate
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    success = (boolean)jsonResponse.get("success");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }finally {
                                    if (success) {
                                        Toast.makeText(getContext(), "User Registered Successfully", Toast.LENGTH_LONG).show();
                                        getActivity().onBackPressed();
                                    }
                                    else{
                                        Toast.makeText(getContext(), "This User Already Exists", Toast.LENGTH_LONG).show();
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
                        return params;
                    }

                };
                //Add the team details to global request queue
                RequestQ.getInstance().addToRequestQ(postReq);
            }
        }
    }

}
