package com.example.ribiero_boys.assignment_2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//CREDIT TO IDUNNOLOLZ FOR EXPANDABLELIST ADAPTER
public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String url="http://192.168.43.186:8080/complaintlist";
    AnimatedExpandableListView listView;
    ExampleAdapter adapter;
    Bundle profileInfo;
    FloatingActionButton fab;
    String complaint_list;
    boolean spl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get the app bar ready
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //receive intent from Login
        profileInfo=getIntent().getBundleExtra("info");

        //fab for new complaint
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(),New_complaint.class);
                intent.putExtra("info", getIntent().getBundleExtra("info"));
                intent.putExtra("spl", getIntent().getBooleanExtra("spl", false));
                startActivity(intent);
            }
        });

        //initialise navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Open Overview first
        Overview firstFragment = new Overview();
        firstFragment.setArguments(profileInfo);
        getSupportFragmentManager().beginTransaction().add(R.id.MainFragments, firstFragment).commit();

        // Populate our list with groups and it's children
        List<GroupItem> items = new ArrayList<GroupItem>();
        items.add(genGroup("Pending Complaints Made"));
        items.add(genGroup("Resolved Complaints Made"));
        if (profileInfo.getInt("type",0)==1 || profileInfo.getInt("type",0)==2){
            items.add(genGroup("Pending Complaints Received"));
            items.add(genGroup("Resolved Complaints Received"));
        }

        adapter = new ExampleAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        //fetch all the complaints
        StringRequest threadsReq = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // receive reply from server
                        complaint_list=response;
                        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                @Override
                                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                    Bundle ComplList=new Bundle();
                                    final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    switch (adapter.getGroup(groupPosition).items.get(childPosition).title) {
                                        case "Personal":
                                            //Go to Personal Level
                                            Personal fragPersonal = new Personal();
                                            ComplList.putString("type", adapter.getGroup(groupPosition).title);
                                            ComplList.putString("cjson",complaint_list);
                                            ComplList.putString("user",profileInfo.getString("username"," "));
                                            ComplList.putBundle("profileInfo", profileInfo);
                                            ComplList.putBoolean("spl",spl);
                                            fragPersonal.setArguments(ComplList);
                                            transaction.replace(R.id.MainFragments, fragPersonal);
                                            transaction.commit();
                                            drawer.closeDrawers();
                                            return true;

                                        case "Hostel Level":
                                            //Go to Hostel Level
                                            Hostel fragHostel = new Hostel();
                                            ComplList.putString("type", adapter.getGroup(groupPosition).title);
                                            ComplList.putString("cjson",complaint_list);
                                            ComplList.putString("user",profileInfo.getString("username"," "));
                                            ComplList.putBundle("profileInfo", profileInfo);
                                            ComplList.putBoolean("spl", spl);
                                            fragHostel.setArguments(ComplList);
                                            transaction.replace(R.id.MainFragments, fragHostel);
                                            transaction.commit();
                                            drawer.closeDrawers();
                                            return true;

                                        case "Department Level":
                                            //Go to Department Level
                                            Department fragDepartment = new Department();
                                            ComplList.putString("type",adapter.getGroup(groupPosition).title);
                                            ComplList.putString("cjson",complaint_list);
                                            ComplList.putString("user",profileInfo.getString("username"," "));
                                            ComplList.putBundle("profileInfo", profileInfo);
                                            ComplList.putBoolean("spl", spl);
                                            fragDepartment.setArguments(ComplList);
                                            transaction.replace(R.id.MainFragments, fragDepartment);
                                            transaction.commit();
                                            drawer.closeDrawers();
                                            return true;
                                        case "Institute Level":
                                            //Go to Institute Level
                                            Institute fragInstitute = new Institute();
                                            ComplList.putString("type",adapter.getGroup(groupPosition).title);
                                            ComplList.putString("cjson",complaint_list);
                                            ComplList.putString("user",profileInfo.getString("username"," "));
                                            ComplList.putBundle("profileInfo",profileInfo);
                                            ComplList.putBoolean("spl",spl);
                                            fragInstitute.setArguments(ComplList);
                                            transaction.replace(R.id.MainFragments, fragInstitute);
                                            transaction.commit();
                                            drawer.closeDrawers();
                                            return true;

                                        default:
                                            return false;
                                    }

                                }
                            });
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

    //create group item to add to expandable list
    public GroupItem genGroup(String s){
        GroupItem pcm=new GroupItem();
        pcm.title=s;
        pcm.items.add(new ChildItem("Personal"));
        if (profileInfo.getInt("type",0)==0 || profileInfo.getInt("type",0)==1){
            pcm.items.add(new ChildItem("Hostel Level"));
            pcm.items.add(new ChildItem("Department Level"));
        }
        pcm.items.add(new ChildItem("Institute Level"));
        return pcm;
    }

    //disable back navigation on main activity
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.MainFragments);
            if (f instanceof View_complaint) {
                super.onBackPressed();
            }else{

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Intent intent=getIntent();
        spl=intent.getBooleanExtra("spl",true);
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
                final Intent goToNotifications = new Intent(Main.this, Notifications.class);
                goToNotifications.putExtra("info",profileInfo);
                goToNotifications.putExtra("spl",spl);
                startActivity(goToNotifications);
                return true;

            case R.id.action_settings:
                //Profile selected
                final Intent goToProfile = new Intent(Main.this, Profile.class);
                goToProfile.putExtra("info",profileInfo);
                goToProfile.putExtra("spl",spl);
                startActivity(goToProfile);
                return true;

            case R.id.logout:
                //Logout selected
                final Intent logout = new Intent(Main.this, Login.class);
                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Logout")
                        .setMessage("Do you wish to log out?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final Intent goToStart = new Intent(Main.this, Start.class);
                                startActivity(goToStart);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;

            case R.id.special:
                //special selected
                final Intent goToSpecial = new Intent(Main.this, Special.class);
                goToSpecial.putExtra("info",profileInfo);
                goToSpecial.putExtra("spl",spl);
                startActivity(goToSpecial);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        return true;
    }

    //custom classes meant for expandable lists and their children
    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;

        public ChildItem(String t){
            this.title=t;
        }
    }

    //placeholders used by adpater
    private static class ChildHolder {
        TextView title;
    }

    private static class GroupHolder {
        TextView title;
    }

    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        //custom listview adapter for the classes we have
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                //holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            //holder.hint.setText(item.hint);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }

    public void showFloatingActionButton() {
        fab.show();
    };

    public void hideFloatingActionButton() {
        fab.hide();
    };


}
