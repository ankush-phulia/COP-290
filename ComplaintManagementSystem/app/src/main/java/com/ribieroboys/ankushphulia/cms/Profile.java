package com.ribieroboys.ankushphulia.cms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    String name;
    String username;
    String password;
    int designation;
    Bundle profileInfo;
    boolean spl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set valus of the fields visible
        profileInfo=getIntent().getBundleExtra("info");
        try {
            JSONObject json=new JSONObject(profileInfo.getString("json"));
            setparameters(profileInfo);
            getExtras(designation, json);

            final Context cont=this;
           /* Button save =(Button)findViewById(R.id.buttonSave);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText chpass = (EditText) findViewById(R.id.editTextPassword3);
                    final Intent intent = new Intent(Profile.this, Main.class);
                    if (chpass.getText().toString().equals(password)) {
                        end();
                    } else {
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
                                    profileInfo2.putString("username", username);
                                    profileInfo2.putString("password", chpass.getText().toString());
                                    intent.putExtra("info", profileInfo2);
                                    Toast.makeText(cont, "Password Set Successfully", Toast.LENGTH_LONG).show();
                                    end();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(cont, "Incorrect Previous password entered", Toast.LENGTH_LONG).show();
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
            });*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setparameters(Bundle profileInfo) {
        name=profileInfo.getString("name"," ");
        password=profileInfo.getString("password", " ");
        username=profileInfo.getString("username", " ");
        designation=profileInfo.getInt("type", 0);
        getFields(designation);
        TextView namet=(TextView)findViewById(R.id.TextViewName);
        namet.setText(name);
        TextView uname=(TextView)findViewById(R.id.TextViewUsername2);
        uname.setText(username);
        final TextView chpass=(TextView)findViewById(R.id.editTextPassword3);
        chpass.setText(password);
        TextView des=(TextView)findViewById(R.id.TextViewDesignation);
        switch (designation) {
            case 0:
                des.setText("Student");
                break;
            case 1:
                des.setText("Faculty");
                break;
            case 2:
                des.setText("Employee");
                break;
            case 3:
                des.setText("Others");
                break;
        }
    }

    public void getFields(int designation){
        LinearLayout otherfields=(LinearLayout)findViewById(R.id.Extras2);
        otherfields.setOrientation(LinearLayout.VERTICAL);
        designation_format2 Format=new designation_format2();
        Format.cont=this;
        switch (designation){
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

    public void getExtras(int designation,JSONObject json){

        LinearLayout otherfields=(LinearLayout)findViewById(R.id.Extras2);
        otherfields.setOrientation(LinearLayout.VERTICAL);
        designation_format2 Format=new designation_format2();
        Format.cont=this;
        switch (designation){
            case 0:
                //populate the fields with data
                TextView dep = (TextView) findViewById(R.id.dep10);
                TextView hostel = (TextView) findViewById(R.id.hostel10);
                TextView por = (TextView) findViewById(R.id.por10);
                TextView room = (TextView) findViewById(R.id.room0);
                try {
                    dep.setText(json.getInt("Department"));
                    hostel.setText(json.getInt("Hostel"));
                    por.setText(json.getString("POR"));
                    room.setText(json.getString("Room"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case 1:
                //populate the fields with data
                TextView dep2=(TextView)findViewById(R.id.dep20);
                TextView addr_fac_employee=(TextView)findViewById(R.id.address10);
                TextView addnlcharge=(TextView)findViewById(R.id.por20);
                TextView hostel2=(TextView)findViewById(R.id.hostel20);
                try {
                    dep2.setText(json.getInt("Department"));
                    hostel2.setText(json.getInt("Hostel"));
                    addnlcharge.setText(json.getString("POR"));
                    addr_fac_employee.setText(json.getString("Address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
            case 2:
                //populate the fields with data
                TextView work=(TextView)findViewById(R.id.work0);
                TextView addr_employee=(TextView)findViewById(R.id.address20);

                try {
                    work.setText(json.getString("Work"));
                    addr_employee.setText(json.getString("Address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case 3:
                //populate the fields with data
                TextView addr_others=(EditText)findViewById(R.id.address3);
                try {
                    addr_others.setText(json.getString("Address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Intent intent=getIntent();
        spl=intent.getBooleanExtra("spl",false);
        if (spl){
            getMenuInflater().inflate(R.menu.main2, menu);
            return true;
        }
        else{
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.notifications:
                //Notifications selected
                final Intent goToNotifications = new Intent(Profile.this, Notifications.class);
                goToNotifications.putExtra("info",profileInfo);
                goToNotifications.putExtra("spl",spl);
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

            case R.id.special:
                //special selected
                final Intent goToSpecial = new Intent(Profile.this, Special.class);
                goToSpecial.putExtra("info",profileInfo);
                goToSpecial.putExtra("spl",spl);
                end();
                startActivity(goToSpecial);
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
