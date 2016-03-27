package com.example.ribiero_boys.assignment_2;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
        if (otherfields.getChildCount()>0){
            otherfields.removeAllViews();
        }
        switch (position){
            case 0:
                //student selected
                populate_student(otherfields);
                break;
            case 1:
                //faculty selected
                populate_faculty(otherfields);
                break;
            case 2:
                //employee selected
                populate_employee(otherfields);
                break;
            case 3:
                //others selected
                populate_other(otherfields);
                break;
        }

    }

    public void populate_student(LinearLayout otherfields){

        TextView ttt1=new TextView(getContext());
        ttt1.setText("Depatment");
        ttt1.setTextColor(Color.BLACK);
        ttt1.setTextSize(22);

        //create spinner to choose dep
       Spinner dep=new Spinner(getContext());
        //types of dep
        List<String> categories = new ArrayList<String>();
        categories.add("CSE");
        categories.add("EE");
        categories.add("MZ");
        categories.add("ME");
        categories.add("TT");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        dep.setAdapter(dataAdapter);
        dep.setId(40);

        TextView ttt2=new TextView(getContext());
        ttt2.setText(" ");
        ttt2.setTextSize(6);

        TextView t1=new TextView(getContext());
        t1.setText("Hostel");
        t1.setTextSize(22);
        t1.setTextColor(Color.BLACK);

        /*TextView tt=new TextView(getContext());
        tt.setText(" ");
        tt.setTextSize(6);*/
        
        //create spinner for hostels
        Spinner hostel=new Spinner(getContext());
        List<String> categories2 = new ArrayList<String>();
        categories2.add("Girnar");
        categories2.add("Udaigiri");
        categories2.add("Zanskar");
        categories2.add("Satpura");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, categories2);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // attaching data adapter to spinner
        hostel.setAdapter(dataAdapter2);
        hostel.setId(10);

        //create spaces and other elements
        TextView t2=new TextView(getContext());
        t2.setText(" ");
        t2.setTextSize(6);

        TextView t3=new TextView(getContext());
        t3.setText("Room Number");
        t3.setTextColor(Color.BLACK);
        t3.setTextSize(22);

        EditText room=new EditText(getContext());
        room.setTextSize(20);
        room.setHint("Enter Room Number, if applicable");
        room.setId(101);

        TextView tt2=new TextView(getContext());
        tt2.setText(" ");
        tt2.setTextSize(6);

        TextView tt3=new TextView(getContext());
        tt3.setText("Positon of Responsibility");
        tt3.setTextSize(22);
        tt3.setTextColor(Color.BLACK);

        //create autocomplete textview to choose or enter the POR
        AutoCompleteTextView por=new AutoCompleteTextView(getContext());
        por.setTextSize(20);
        por.setHint("Enter your Position, if applicable");
        //types of position
        List<String> categories3 = new ArrayList<String>();
        categories3.add("General Secretary");
        categories3.add("Sports Secretary");
        categories3.add("Sports Captain");
        categories3.add("Class COnvener");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, categories3);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        por.setAdapter(dataAdapter3);
        por.setThreshold(1);
        por.setId(20);

        TextView t4=new TextView(getContext());
        t4.setText(" ");
        t4.setTextSize(6);

        //add all the elements to linearlayout
        otherfields.addView(ttt1);
        otherfields.addView(dep);
        otherfields.addView(ttt2);
        otherfields.addView(t1);
        //otherfields.addView(tt);
        otherfields.addView(hostel);
        otherfields.addView(t2);
        otherfields.addView(t3);
        otherfields.addView(room);
        otherfields.addView(tt2);
        otherfields.addView(tt3);
        otherfields.addView(por);
        otherfields.addView(t4);

    }

    public void populate_faculty(LinearLayout otherfields){

        TextView t1=new TextView(getContext());
        t1.setText("Depatment");
        t1.setTextColor(Color.BLACK);
        t1.setTextSize(22);

        //create spinner to choose dep
        Spinner dep=new Spinner(getContext());
        //types of dep
        List<String> categories = new ArrayList<String>();
        categories.add("CSE");
        categories.add("EE");
        categories.add("MZ");
        categories.add("ME");
        categories.add("TT");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        dep.setAdapter(dataAdapter);
        dep.setId(50);

        TextView t2=new TextView(getContext());
        t2.setText(" ");
        t2.setTextSize(6);

        TextView tt=new TextView(getContext());
        tt.setText("Address");
        tt.setTextSize(22);
        tt.setTextColor(Color.BLACK);

        //for address
        EditText addr=new EditText(getContext());
        addr.setTextSize(20);
        addr.setHint("Enter Address, if on campus");
        addr.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        addr.setId(103);

        TextView tt2=new TextView(getContext());
        tt2.setText(" ");
        tt2.setTextSize(6);

        TextView tt3=new TextView(getContext());
        tt3.setText("Additional Charge");
        tt3.setTextSize(22);
        tt3.setTextColor(Color.BLACK);

        //create autocomplete textview to choose or enter the POR
        AutoCompleteTextView por=new AutoCompleteTextView(getContext());
        por.setTextSize(20);
        por.setHint("Enter your Position, if applicable");
        //types of position
        List<String> categories2 = new ArrayList<String>();
        categories2.add("Director");
        categories2.add("Chairman");
        categories2.add("Commitee Member");
        categories2.add("Warden");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, categories2);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        por.setAdapter(dataAdapter2);
        por.setThreshold(1);
        por.setId(60);

        TextView t4=new TextView(getContext());
        t4.setText(" ");
        t4.setTextSize(6);

        TextView tt1=new TextView(getContext());
        tt1.setText("Hostel, if warden");
        tt1.setTextSize(22);
        tt1.setTextColor(Color.BLACK);

       /* TextView ttt=new TextView(getContext());
        ttt.setText(" ");
        ttt.setTextSize(6);*/

        //create spinner for hostels
        Spinner hostel=new Spinner(getContext());
        List<String> categories3 = new ArrayList<String>();
        categories3.add("Girnar");
        categories3.add("Udaigiri");
        categories3.add("Zanskar");
        categories3.add("Satpura");
        categories3.add("None");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, categories3);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // attaching data adapter to spinner
        hostel.setAdapter(dataAdapter3);
        hostel.setId(70);

        //add all the elements to linearlayout
        otherfields.addView(t1);
        otherfields.addView(dep);
        otherfields.addView(t2);
        otherfields.addView(tt);
        otherfields.addView(addr);
        otherfields.addView(tt2);
        otherfields.addView(tt3);
        otherfields.addView(por);
        otherfields.addView(t4);
        otherfields.addView(tt1);
        //otherfields.addView(ttt);
        otherfields.addView(hostel);

    }

    public void populate_employee(LinearLayout otherfields){

        TextView t1=new TextView(getContext());
        t1.setText("Work Type");
        t1.setTextColor(Color.BLACK);
        t1.setTextSize(22);

        //create autocomplete textview to choose or enter the work type
        AutoCompleteTextView work=new AutoCompleteTextView(getContext());
        work.setTextSize(20);
        work.setHint("Enter your Work Type");
        //types of work
        List<String> categories = new ArrayList<String>();
        categories.add("CSC");
        categories.add("Electrician");
        categories.add("Civil Engineer");
        categories.add("Mess Incharge");
        categories.add("Guard");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        work.setAdapter(dataAdapter);
        work.setThreshold(1);
        work.setId(30);

       /* TextView t2=new TextView(getContext());
        t2.setText(" ");
        t2.setTextSize(6);*/

        TextView tt=new TextView(getContext());
        tt.setText("Address");
        tt.setTextSize(22);
        tt.setTextColor(Color.BLACK);

        //for address
        EditText addr=new EditText(getContext());
        addr.setTextSize(20);
        addr.setHint("Enter Address, if on campus");
        addr.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        addr.setId(101);

        //add all the elements to linearlayout
        otherfields.addView(t1);
        otherfields.addView(work);
        //otherfields.addView(t2);
        otherfields.addView(tt);
        otherfields.addView(addr);
    }

    public void populate_other(LinearLayout otherfields){
        TextView t1=new TextView(getContext());
        t1.setText("Address");
        t1.setTextSize(22);
        t1.setTextColor(Color.BLACK);

        //for address
        EditText addr=new EditText(getContext());
        addr.setTextSize(20);
        addr.setHint("Enter Address, if on campus");
        addr.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        addr.setId(102);

        //add all the elements to linearlayout
        otherfields.addView(t1);
        otherfields.addView(addr);
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
            switch(designtion.getSelectedItemPosition()){
                case 0:
                    Spinner dep=(Spinner)v.getRootView().findViewById(40);
                    Spinner hostel=(Spinner)v.getRootView().findViewById(10);
                    Spinner por=(Spinner)v.getRootView().findViewById(20);
                    EditText room=(EditText)v.getRootView().findViewById(100);
                    break;
                case 1:
                    Spinner dep2=(Spinner)v.getRootView().findViewById(50);

                    break;
                case 2:
                    AutoCompleteTextView work=(AutoCompleteTextView)v.getRootView().findViewById(30);
                    EditText addr_employee=(EditText)v.getRootView().findViewById(101);

                    break;
                case 3:
                    EditText addr_others=(EditText)v.getRootView().findViewById(102);
                    break;
            }
            getActivity().getSupportFragmentManager().popBackStackImmediate();

        }
    }

}
