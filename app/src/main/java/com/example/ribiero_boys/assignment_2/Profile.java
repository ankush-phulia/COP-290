package com.example.ribiero_boys.assignment_2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity {

    String name;
    String username;
    String password;
    String designation;
    Bundle profileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set valus of the fields visible
        profileInfo=getIntent().getBundleExtra("info");
        setparameters(profileInfo);
        getExtras(designation, profileInfo);

        final Context cont=this;
        Button save =(Button)findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText chpass=(EditText)findViewById(R.id.editTextPassword3);
                final Intent intent=new Intent(Profile.this,Main.class);
                if (chpass.getText().toString().equals(password)){
                    end();
                }
                else{
                    //create dialog box confirming password change
                    AlertDialog.Builder builder = new AlertDialog.Builder(cont);
                    builder.setTitle("Change of Password");
                    builder.setMessage("Enter your Previous Password");
                    // Set up the input
                    final EditText input = new EditText(cont);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);
                    // Set up the buttons
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (password.equals(input.getText().toString())) {
                                Bundle profileInfo2 = new Bundle();
                                profileInfo2.putString("username",username);
                                profileInfo2.putString("password",chpass.getText().toString());
                                intent.putExtra("info",profileInfo2);
                                Toast.makeText(cont, "Password Set Successfully",Toast.LENGTH_LONG).show();
                                end();
                                startActivity(intent);
                            } else {
                                Toast.makeText(cont, "Incorrect Previous password entered",Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }
                        }
                    });
                    builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }

            }
        });

    }

    public void setparameters(Bundle profileInfo) {
        name=profileInfo.getString("name"," ");
        password=profileInfo.getString("password", " ").toString();
        username=profileInfo.getString("username", " ").toString();
        designation=profileInfo.getString("designation", "Student");
        getFields(designation);
        TextView namet=(TextView)findViewById(R.id.TextViewName);
        namet.setText(name);
        TextView uname=(TextView)findViewById(R.id.TextViewUsername2);
        uname.setText(username);
        final EditText chpass=(EditText)findViewById(R.id.editTextPassword3);
        chpass.setText(password);
        TextView des=(TextView)findViewById(R.id.TextViewDesignation);
        des.setText(designation);
    }

    public void getFields(String designation){
        LinearLayout otherfields=(LinearLayout)findViewById(R.id.Extras2);
        otherfields.setOrientation(LinearLayout.VERTICAL);
        designation_format Format=new designation_format();
        Format.cont=this;
        switch (designation){
            case "Student":
                //student selected
                Format.populate_student(otherfields);
                break;
            case "Faculty":
                //faculty selected
                Format.populate_faculty(otherfields);
                break;
            case "Employee":
                //employee selected
                Format.populate_employee(otherfields);
                break;
            case "Others":
                //others selected
                Format.populate_other(otherfields);
                break;
        }
    }

    public void getExtras(String designation,Bundle profileInfo){

        LinearLayout otherfields=(LinearLayout)findViewById(R.id.Extras2);
        otherfields.setOrientation(LinearLayout.VERTICAL);
        designation_format Format=new designation_format();
        Format.cont=this;
        switch (designation){
            case "Student":
                //populate the fields with data
                Spinner dep=(Spinner)findViewById(R.id.dep1);
                Spinner hostel=(Spinner)findViewById(R.id.hostel1);
                AutoCompleteTextView por=(AutoCompleteTextView)findViewById(R.id.por1);
                EditText room=(EditText)findViewById(R.id.room);
                dep.setSelection(profileInfo.getInt("Department",0));
                hostel.setSelection(profileInfo.getInt("Hostel",0));
                por.setText(profileInfo.getString("POR", "Nothing"));
                room.setText(profileInfo.getString("Room","AA00"));
                break;
            case "Faculty":
                //populate the fields with data
                Spinner dep2=(Spinner)findViewById(R.id.dep2);
                EditText addr_fac_employee=(EditText)findViewById(R.id.address1);
                AutoCompleteTextView addnlcharge=(AutoCompleteTextView)findViewById(R.id.por2);
                Spinner hostel2=(Spinner)findViewById(R.id.hostel2);
                dep2.setSelection(profileInfo.getInt("Department",0));
                hostel2.setSelection(profileInfo.getInt("Hostel",0));
                addnlcharge.setText(profileInfo.getString("POR","Nothing"));
                addr_fac_employee.setText(profileInfo.getString("Address","Not on Campus"));

                break;
            case "Employee":
                //populate the fields with data
                AutoCompleteTextView work=(AutoCompleteTextView)findViewById(R.id.work);
                EditText addr_employee=(EditText)findViewById(R.id.address2);
                work.setText(profileInfo.getString("Work","Nothing"));
                addr_employee.setText(profileInfo.getString("Address","Not on Campus"));

                break;
            case "Others":
                //populate the fields with data
                EditText addr_others=(EditText)findViewById(R.id.address3);
                addr_others.setText(profileInfo.getString("Address","Not on Campus"));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.notifications:
                //Notifications selected
                final Intent goToNotifications = new Intent(Profile.this, Notifications.class);
                goToNotifications.putExtra("info",profileInfo);
                end();
                startActivity(goToNotifications);
                return true;

            case R.id.action_settings:
                return true;

            case R.id.logout:
                //Logout selected
                final Intent logout = new Intent(Profile.this, Login.class);
                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Logout")
                        .setMessage("Do you wish to log out?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final Intent goToStart = new Intent(Profile.this, Start.class);
                                end();
                                startActivity(goToStart);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void end() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        //close the activity
        end();
    }

}
