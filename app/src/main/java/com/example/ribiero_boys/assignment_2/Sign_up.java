package com.example.ribiero_boys.assignment_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Sign_up extends Fragment implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    Button signup;

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

    @Override
    public void onClick(View v) {
        if (v==signup){
            EditText name=(EditText)v.getRootView().findViewById(R.id.editTextName);
            EditText uname=(EditText)v.getRootView().findViewById(R.id.editTextUsername2);
            EditText pass=(EditText)v.getRootView().findViewById(R.id.editTextPassword2);
            EditText cpass=(EditText)v.getRootView().findViewById(R.id.editTextPasswordconf);
            Spinner designtion=(Spinner)v.getRootView().findViewById(R.id.spinnerDesignation);
            CheckBox spl=(CheckBox)v.getRootView().findViewById(R.id.checkBoxSpecial);
            switch(designtion.getSelectedItemPosition()){
                case 0:
                    Spinner dep=(Spinner)v.getRootView().findViewById(R.id.dep1);
                    Spinner hostel=(Spinner)v.getRootView().findViewById(R.id.hostel1);
                    AutoCompleteTextView por=(AutoCompleteTextView)v.getRootView().findViewById(R.id.por1);
                    EditText room=(EditText)v.getRootView().findViewById(R.id.room);
                    //TextView t =(TextView)(dep.getChildAt(0));

                    break;
                case 1:
                    Spinner dep2=(Spinner)v.getRootView().findViewById(R.id.dep2);
                    EditText addr_fac_employee=(EditText)v.getRootView().findViewById(R.id.address1);
                    AutoCompleteTextView addnlcharge=(AutoCompleteTextView)v.getRootView().findViewById(R.id.por2);
                    Spinner hostel2=(Spinner)v.getRootView().findViewById(R.id.hostel2);
                    break;
                case 2:
                    AutoCompleteTextView work=(AutoCompleteTextView)v.getRootView().findViewById(R.id.work);
                    EditText addr_employee=(EditText)v.getRootView().findViewById(R.id.address2);
                    break;
                case 3:
                    EditText addr_others=(EditText)v.getRootView().findViewById(R.id.address3);
                    break;
            }
            Toast.makeText(getContext(), "Sign Up Successful", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStackImmediate();

        }
    }

}
