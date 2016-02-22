package com.ribieroboys.moodleplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    //Button back;
    Bundle profileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent dataReceived = getIntent();
        profileInfo = dataReceived.getBundleExtra("profileInfo");

        TextView username = (TextView) findViewById(R.id.profile_username);
        username.setText(profileInfo.getString("user"));
        TextView fullname = (TextView) findViewById(R.id.profile_fullname);
        fullname.setText(profileInfo.getString("firstName") + " " + profileInfo.getString("lastName"));
        TextView entryNo = (TextView) findViewById(R.id.profile_entryno);
        entryNo.setText(profileInfo.getString("entryNo"));
        TextView email = (TextView) findViewById(R.id.profile_email);
        email.setText(profileInfo.getString("email"));

/*        back=(Button) findViewById(R.id.button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end();
            }
        });*/
    }

    public void end(){
        this.finish();
    }

    @Override
    public void onBackPressed() {
        this.finish();
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
            case R.id.action_settings:
                final Intent goToProfile = new Intent(Profile.this, Profile.class);
                goToProfile.putExtra("profileInfo", profileInfo);
                startActivity(goToProfile);
                end();
                return true;

            case R.id.logout:
                final Intent intent2 = new Intent(Profile.this, Login.class);
                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK)
                        .setTitle("Logout")
                        .setMessage("Do you wish to log out?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent2);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
