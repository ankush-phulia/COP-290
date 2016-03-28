package com.example.ribiero_boys.assignment_2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Notifications extends AppCompatActivity {

    Bundle profileInfo;
    boolean spl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileInfo=getIntent().getBundleExtra("info");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newcomp = new Intent(Notifications.this, New_complaint.class);
                newcomp.putExtra("info",profileInfo);
                startActivity(newcomp);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                return true;

            case R.id.action_settings:
                //Profile selected
                final Intent goToProfile = new Intent(Notifications.this, Profile.class);
                goToProfile.putExtra("info",profileInfo);
                goToProfile.putExtra("spl",spl);
                end();
                startActivity(goToProfile);
                return true;

            case R.id.logout:
                //Logout selected
                final Intent logout = new Intent(Notifications.this, Login.class);
                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Logout")
                        .setMessage("Do you wish to log out?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final Intent goToStart = new Intent(Notifications.this, Start.class);
                                end();
                                startActivity(goToStart);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;

            case R.id.special:
                //special selected
                final Intent goToSpecial = new Intent(Notifications.this, Special.class);
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
