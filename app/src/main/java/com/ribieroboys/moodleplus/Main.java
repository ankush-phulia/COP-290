package com.ribieroboys.moodleplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String user;
    String pass;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent data = getIntent();
        user=data.getStringExtra("user");
        pass=data.getStringExtra("pass");

        final Bundle args = new Bundle();
        args.putString("user", user);
        args.putString("pass", pass);

        Dashboard firstFragment=new Dashboard();
        firstFragment.setArguments(data.getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, firstFragment).commit();

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });


        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                switch (groupPosition) {
                    case 0:
                        Dashboard newf = new Dashboard();
                        newf.setArguments(args);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, newf);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawers();
                        return true;
                    case 1:
                        Notifications newf2 = new Notifications();
                        newf2.setArguments(args);
                        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                        transaction2.replace(R.id.frame_container, newf2);
                        transaction2.addToBackStack(null);
                        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawers();
                        transaction2.commit();
                        return true;
                    case 2:
                        Grades newf3 = new Grades();
                        newf3.setArguments(args);
                        FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                        transaction3.replace(R.id.frame_container, newf3);
                        transaction3.addToBackStack(null);
                        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawers();
                        transaction3.commit();
                        return true;
                    case 3:
                        return false;
                    default:
                        return true;
                }

            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Overview");
        listDataHeader.add("Notifications");
        listDataHeader.add("Grades");
        listDataHeader.add("Courses");

        List<String> Overview = new ArrayList<String>();
        List<String> Notifications = new ArrayList<String>();
        List<String> Grades = new ArrayList<String>();
        List<String> Courses = new ArrayList<String>();
        Courses.add("1");
        Courses.add("2");
        Courses.add("3");
        listDataChild.put(listDataHeader.get(0), Overview);
        listDataChild.put(listDataHeader.get(1), Notifications);
        listDataChild.put(listDataHeader.get(2), Grades);
        listDataChild.put(listDataHeader.get(3), Courses);
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
                final Intent intent1 = new Intent(Main.this, Profile.class);
                intent1.putExtra("user",user);
                intent1.putExtra("pass",pass);
                startActivity(intent1);
                return true;
            case R.id.logout:
                final Intent intent2 = new Intent(Main.this, Login.class);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

}
