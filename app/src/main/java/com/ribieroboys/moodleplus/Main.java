package com.ribieroboys.moodleplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DrawerLayout drawer;
    List<String> Courses;
    ArrayList<String> CourseOverviewData;
    ArrayList<String> CourseAssignmentsData;
    ArrayList<String> CourseGradesData;
    ArrayList<String> CourseThreadsData;
    Bundle profileInfo;
    String notiJSON;
    String gradesJSON;
    String url;

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

        final Intent dataReceived = getIntent();
        profileInfo = dataReceived.getBundleExtra("loginResponse");
        Courses = dataReceived.getStringArrayListExtra("courseListCodes");
        url = dataReceived.getStringExtra("URL");

        notiJSON = (dataReceived.getStringExtra("/default/notifications.json"));
        gradesJSON = (dataReceived.getStringExtra("/default/grades.json"));

        Dashboard firstFragment = new Dashboard();
        firstFragment.setArguments(profileInfo);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, firstFragment).commit();

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        prepareListData(Courses);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Course fragCourse = new Course();
                Bundle courseDetails = new Bundle();
                courseDetails.putString("courseCode", Courses.get(childPosition));
                courseDetails.putString("Overview", CourseOverviewData.get(childPosition));
                courseDetails.putString("Assignments", CourseAssignmentsData.get(childPosition));
                courseDetails.putString("Grades", CourseGradesData.get(childPosition));
                courseDetails.putString("Threads", CourseThreadsData.get(childPosition));

                fragCourse.setArguments(courseDetails);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragCourse);
                transaction.addToBackStack(null);
                transaction.commit();

                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawers();

                return true;
            }
        });


        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                switch (groupPosition) {
                    case 0:
                        Dashboard fragDashboard = new Dashboard();
                        fragDashboard.setArguments(profileInfo);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, fragDashboard);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawers();
                        return true;

                    case 1:
                        Notifications fragNotifications = new Notifications();
                        Bundle notiBundle = new Bundle();
                        notiBundle.putString("/default/notifications.json", notiJSON);
                        fragNotifications.setArguments(notiBundle);
                        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                        transaction2.replace(R.id.frame_container, fragNotifications);
                        transaction2.addToBackStack(null);
                        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawers();
                        transaction2.commit();
                        return true;

                    case 2:
                        Grades fragGrades = new Grades();
                        Bundle gradesBundle = new Bundle();
                        gradesBundle.putString("/default/grades.json", gradesJSON);
                        fragGrades.setArguments(gradesBundle);
                        FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                        transaction3.replace(R.id.frame_container, fragGrades);
                        transaction3.addToBackStack(null);
                        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawers();
                        transaction3.commit();
                        return true;

                    case 3:
                        JSONArray courseList;
                        try {
                            courseList = (new JSONObject(dataReceived.getStringExtra("/courses/list.json"))).getJSONArray("courses");

                            CourseOverviewData = new ArrayList<String>();
                            CourseGradesData = new ArrayList<String>();
                            CourseAssignmentsData = new ArrayList<String>();
                            CourseThreadsData = new ArrayList<String>();

                            for (int courseNo = 0; courseNo < Courses.size(); courseNo++) {
                                CourseOverviewData.add(courseList.getString(courseNo));
                                String CourseCode = Courses.get(courseNo);

                                String assignmentURL = url + "/courses/course.json/" + CourseCode + "/assignments";
                                StringRequest assignReq = new StringRequest(Request.Method.GET,
                                        assignmentURL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // receive reply from server
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    CourseAssignmentsData.add(jsonResponse.getJSONArray("assignments").toString());
                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(Main.this, "Connection Error", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                RequestQ.getInstance().addToRequestQ(assignReq);

                                String gradesURL = url + "/courses/course.json/" + CourseCode + "/grades";
                                StringRequest gradesReq = new StringRequest(Request.Method.GET,
                                        gradesURL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // receive reply from server
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    CourseGradesData.add(jsonResponse.getJSONArray("grades").toString());
                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(Main.this, "Connection Error", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                RequestQ.getInstance().addToRequestQ(gradesReq);

                                String threadsURL = url + "/courses/course.json/" + CourseCode + "/threads";
                                StringRequest threadsReq = new StringRequest(Request.Method.GET,
                                        threadsURL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // receive reply from server
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    CourseThreadsData.add(jsonResponse.getJSONArray("course_threads").toString());
                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(Main.this, "Connection Error", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                RequestQ.getInstance().addToRequestQ(threadsReq);

                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        return false;

                    default:
                        return true;
                }

            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
    }

    private void prepareListData(List<String> Courses) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Overview");
        listDataHeader.add("Notifications");
        listDataHeader.add("Grades");
        listDataHeader.add("Courses");

        List<String> Overview = new ArrayList<String>();
        List<String> Notifications = new ArrayList<String>();
        List<String> Grades = new ArrayList<String>();
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
                final Intent goToProfile = new Intent(Main.this, Profile.class);
                goToProfile.putExtra("profileInfo", profileInfo);
                startActivity(goToProfile);
                return true;

            case R.id.logout:
                final Intent logout = new Intent(Main.this, Login.class);
                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK)
                        .setTitle("Logout")
                        .setMessage("Do you wish to log out?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String logoutURL = url + "/default/logout.json";
                                StringRequest getReq = new StringRequest(Request.Method.GET,
                                        logoutURL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(Main.this, "Connection Error", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                // send the request to RequestQ
                                RequestQ.getInstance().addToRequestQ(getReq);
                                startActivity(logout);
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
