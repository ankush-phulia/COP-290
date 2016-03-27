package com.example.ribiero_boys.assignment_2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void end() {
        this.finish();
    }

}
