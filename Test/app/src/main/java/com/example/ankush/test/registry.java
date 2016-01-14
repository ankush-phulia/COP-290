package com.example.ankush.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registry extends AppCompatActivity {

    public boolean isValid(String entry_no){

        /*if (entry_no.length()!=11){
            return false;
        }
        else if (!entry_no.substring(4,6).toLowerCase().equals("cs") &&!entry_no.substring(4,6).toLowerCase().equals("ee")&&!entry_no.substring(4,6).toLowerCase().equals("mt")&&!entry_no.substring(4,6).toLowerCase().equals("me")){
            return false;
        }
        else if (!entry_no.substring(0,4).matches("[0-9]+") || !entry_no.substring(6,11).matches("[0-9]+")){
            return false;
        }
        else if (Integer.parseInt(entry_no.substring(0,4))>2014 ||Integer.parseInt(entry_no.substring(0,4))<2010 || Integer.parseInt(entry_no.substring(7,8))!=0 ){
            return false;
        }
        else{
            return true;
        }*/

        // Using RegEx to check instead of checking as done above
        String Regex_Pattern = "^201[0-4](bb|BB|cs|CS|ce|CE|ch|CH|ee|EE|mt|MT|me|ME|tt|TT)[1-7][0-9]{4}$";
        Pattern p = Pattern.compile(Regex_Pattern);
        Matcher m = p.matcher(entry_no);
        return m.find();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        final Context context=this;
        Button btn = (Button)findViewById(R.id.button2);
        final EditText team_name=(EditText) findViewById(R.id.editText);
        final EditText name1=(EditText) findViewById(R.id.editText2);
        final EditText entry_no1=(EditText) findViewById(R.id.editText3);
        final EditText name2=(EditText) findViewById(R.id.editText4);
        final EditText entry_no2=(EditText) findViewById(R.id.editText5);
        final EditText name3=(EditText) findViewById(R.id.editText6);
        final EditText entry_no3=(EditText) findViewById(R.id.editText7);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // checking if entries are filled properly
                if (team_name.getText().toString().length()==0){
                    new AlertDialog.Builder(context)
                            .setTitle("Team Name Unspecifed")
                            .setMessage("Please enter the Team Name")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else if (name1.getText().toString().length()==0){
                    new AlertDialog.Builder(context)
                            .setTitle("Name Unspecifed")
                            .setMessage("Please enter the Name of Member 1")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else if (entry_no1.getText().toString().length()==0){
                    new AlertDialog.Builder(context)
                            .setTitle("Entry Number Unspecifed")
                            .setMessage("Please enter the Entry Number of Member 1")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else if (!isValid(entry_no1.getText().toString())){
                    new AlertDialog.Builder(context)
                            .setTitle("Invalid Entry Number")
                            .setMessage("Please enter a valid Entry Number for Member 1")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    entry_no1.setText("");
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else if (name2.getText().toString().length()==0){
                    new AlertDialog.Builder(context)
                            .setTitle("Name Unspecifed")
                            .setMessage("Please enter the Name of Member 2")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else if (entry_no2.getText().toString().length()==0){
                    new AlertDialog.Builder(context)
                            .setTitle("Entry Number Unspecifed")
                            .setMessage("Please enter the Entry Number of Member 2")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else if (!isValid(entry_no2.getText().toString())){
                    new AlertDialog.Builder(context)
                            .setTitle("Invalid Entry Number")
                            .setMessage("Please enter a valid Entry Number for Member 2")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    entry_no2.setText("");
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else if (name3.getText().toString().length()==0){
                    new AlertDialog.Builder(context)
                            .setTitle("Name Unspecifed")
                            .setMessage("Please enter the Name of Member 3")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else if (entry_no3.getText().toString().length()==0){
                    new AlertDialog.Builder(context)
                            .setTitle("Entry Number Unspecifed")
                            .setMessage("Please enter the Entry Number of Member 3")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else if (!isValid(entry_no3.getText().toString())){
                    new AlertDialog.Builder(context)
                            .setTitle("Invalid Entry Number")
                            .setMessage("Please enter a valid Entry Number for Member 3")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    entry_no3.setText("");
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else /* all the entries are filled properly */ {
                    Intent intent = new Intent(registry.this, Welcome.class);
                    startActivity(intent);
                    Toast.makeText(registry.this, "Team Registered",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
