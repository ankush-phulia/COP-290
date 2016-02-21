package com.ribieroboys.moodleplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;


public class Dashboard extends AppCompatActivity {

    String user;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();
        user = data.getStringExtra("user");
        pass = data.getStringExtra("pass");
        TextView welcome = (TextView) findViewById(R.id.textView8);
        welcome.setText("Welcome " + user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                final Intent intent1 = new Intent(Dashboard.this, Profile.class);
                intent1.putExtra("user",user);
                intent1.putExtra("pass",pass);
                startActivity(intent1);
                return true;
            case R.id.logout:
                final Intent intent2 = new Intent(Dashboard.this, Login.class);
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

    @Override
    public void onBackPressed() {
    }

}
