package com.example.ribiero_boys.assignment_2;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankush on 28-Mar-16.
 */
public class designation_format2 {

    Context cont;

    public void populate_student(LinearLayout otherfields){

        TextView ttt1=new TextView(cont);
        ttt1.setText("Depatment");
        ttt1.setTextColor(Color.BLACK);
        ttt1.setTextSize(22);

        //create TextView to choose dep
        TextView dep=new TextView(cont);
        //types of dep
        List<String> categories = new ArrayList<String>();
        categories.add("CSE");
        categories.add("EE");
        categories.add("MZ");
        categories.add("ME");
        categories.add("TT");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(cont, android.R.layout.simple_dropdown_item_1line, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //dep.setAdapter(dataAdapter);
        dep.setId(R.id.dep10);

        TextView ttt2=new TextView(cont);
        ttt2.setText(" ");
        ttt2.setTextSize(6);

        TextView t1=new TextView(cont);
        t1.setText("Hostel");
        t1.setTextSize(22);
        t1.setTextColor(Color.BLACK);

        /*TextView tt=new TextView(cont);
        tt.setText(" ");
        tt.setTextSize(6);*/

        //create TextView for hostels
        TextView hostel=new TextView(cont);
        List<String> categories2 = new ArrayList<String>();
        categories2.add("Girnar");
        categories2.add("Udaigiri");
        categories2.add("Zanskar");
        categories2.add("Satpura");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(cont, android.R.layout.simple_dropdown_item_1line, categories2);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // attaching data adapter to TextView
        //hostel.setAdapter(dataAdapter2);
        hostel.setId(R.id.hostel10);

        //create spaces and other elements
        TextView t2=new TextView(cont);
        t2.setText(" ");
        t2.setTextSize(6);

        TextView t3=new TextView(cont);
        t3.setText("Room Number");
        t3.setTextColor(Color.BLACK);
        t3.setTextSize(22);

        TextView room=new TextView(cont);
        room.setTextSize(20);
        //room.setHint("Enter Room Number, if applicable");
        room.setId(R.id.room0
        );

        TextView tt2=new TextView(cont);
        tt2.setText(" ");
        tt2.setTextSize(6);

        TextView tt3=new TextView(cont);
        tt3.setText("Positon of Responsibility");
        tt3.setTextSize(22);
        tt3.setTextColor(Color.BLACK);

        //create autocomplete textview to choose or enter the POR
        TextView por=new TextView(cont);
        por.setTextSize(20);
        //por.setHint("Enter your Position, if applicable");
        //types of position
        List<String> categories3 = new ArrayList<String>();
        categories3.add("General Secretary");
        categories3.add("Sports Secretary");
        categories3.add("Sports Captain");
        categories3.add("Class COnvener");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(cont, android.R.layout.simple_dropdown_item_1line, categories3);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //por.setAdapter(dataAdapter3);
        //por.setThreshold(1);
        por.setId(R.id.por10);

        TextView t4=new TextView(cont);
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

        TextView t1=new TextView(cont);
        t1.setText("Depatment");
        t1.setTextColor(Color.BLACK);
        t1.setTextSize(22);

        //create TextView to choose dep
        TextView dep=new TextView(cont);
        //types of dep
        List<String> categories = new ArrayList<String>();
        categories.add("CSE");
        categories.add("EE");
        categories.add("MZ");
        categories.add("ME");
        categories.add("TT");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(cont, android.R.layout.simple_dropdown_item_1line, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //dep.setAdapter(dataAdapter);
        dep.setId(R.id.dep20);

        TextView t2=new TextView(cont);
        t2.setText(" ");
        t2.setTextSize(6);

        TextView tt=new TextView(cont);
        tt.setText("Address");
        tt.setTextSize(22);
        tt.setTextColor(Color.BLACK);

        //for address
        TextView addr=new TextView(cont);
        addr.setTextSize(20);
        //addr.setHint("Enter Address, if on campus");
        addr.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        addr.setId(R.id.address10);

        TextView tt2=new TextView(cont);
        tt2.setText(" ");
        tt2.setTextSize(6);

        TextView tt3=new TextView(cont);
        tt3.setText("Additional Charge");
        tt3.setTextSize(22);
        tt3.setTextColor(Color.BLACK);

        //create autocomplete textview to choose or enter the POR
        TextView por=new TextView(cont);
        por.setTextSize(20);
        //por.setHint("Enter your Position, if applicable");
        //types of position
        List<String> categories2 = new ArrayList<String>();
        categories2.add("Director");
        categories2.add("Chairman");
        categories2.add("Commitee Member");
        categories2.add("Warden");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(cont, android.R.layout.simple_dropdown_item_1line, categories2);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
       // por.setAdapter(dataAdapter2);
        //por.setThreshold(1);
        por.setId(R.id.por20);

        TextView t4=new TextView(cont);
        t4.setText(" ");
        t4.setTextSize(6);

        TextView tt1=new TextView(cont);
        tt1.setText("Hostel, if warden");
        tt1.setTextSize(22);
        tt1.setTextColor(Color.BLACK);

       /* TextView ttt=new TextView(cont);
        ttt.setText(" ");
        ttt.setTextSize(6);*/

        //create TextView for hostels
        TextView hostel=new TextView(cont);
        List<String> categories3 = new ArrayList<String>();
        categories3.add("Girnar");
        categories3.add("Udaigiri");
        categories3.add("Zanskar");
        categories3.add("Satpura");
        categories3.add("None");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(cont, android.R.layout.simple_dropdown_item_1line, categories3);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // attaching data adapter to TextView
        //hostel.setAdapter(dataAdapter3);
        hostel.setId(R.id.hostel20);

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

        TextView t1=new TextView(cont);
        t1.setText("Work Type");
        t1.setTextColor(Color.BLACK);
        t1.setTextSize(22);

        //create autocomplete textview to choose or enter the work type
        TextView work=new TextView(cont);
        work.setTextSize(20);
        //work.setHint("Enter your Work Type");
        //types of work
        List<String> categories = new ArrayList<String>();
        categories.add("CSC");
        categories.add("Electrician");
        categories.add("Civil Engineer");
        categories.add("Mess Incharge");
        categories.add("Guard");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(cont, android.R.layout.simple_dropdown_item_1line, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //work.setAdapter(dataAdapter);
        //work.setThreshold(1);
        work.setId(R.id.work0);

       /* TextView t2=new TextView(cont);
        t2.setText(" ");
        t2.setTextSize(6);*/

        TextView tt=new TextView(cont);
        tt.setText("Address");
        tt.setTextSize(22);
        tt.setTextColor(Color.BLACK);

        //for address
        TextView addr=new TextView(cont);
        addr.setTextSize(20);
        //addr.setHint("Enter Address, if on campus");
        addr.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        addr.setId(R.id.address20);

        //add all the elements to linearlayout
        otherfields.addView(t1);
        otherfields.addView(work);
        //otherfields.addView(t2);
        otherfields.addView(tt);
        otherfields.addView(addr);
    }

    public void populate_other(LinearLayout otherfields){
        TextView t1=new TextView(cont);
        t1.setText("Address");
        t1.setTextSize(22);
        t1.setTextColor(Color.BLACK);

        //for address
        TextView addr=new TextView(cont);
        addr.setTextSize(20);
        //addr.setHint("Enter Address, if on campus");
        addr.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        addr.setId(R.id.address30);

        //add all the elements to linearlayout
        otherfields.addView(t1);
        otherfields.addView(addr);
    }
}
