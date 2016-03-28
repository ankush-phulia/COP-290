package com.example.ribiero_boys.assignment_2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Special extends AppCompatActivity {

    Bundle profileInfo;
    boolean spl;
    AnimatedExpandableListView listView;
    ExampleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileInfo=getIntent().getBundleExtra("info");
        final Context cont=this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get a list of pending users (name+designation displayed)
        List<String> users_designation=new ArrayList<String>();
        users_designation.add("User 1");
        users_designation.add("User 2");
        users_designation.add("User 3");
        // Populate our list with groups and it's children
        List<GroupItem> items = new ArrayList<GroupItem>();
        for (int i=0; i<3;i++){
            items.add(genGroup(users_designation.get(i)));
        }

        adapter = new ExampleAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.listViewSplReq);
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

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (childPosition) {
                    case 0:
                        //Ask for approval
                        new AlertDialog.Builder(cont, AlertDialog.THEME_HOLO_LIGHT)
                                .setTitle("Aprove "+adapter.getGroup(groupPosition).title)
                                .setMessage("Do you wish approve this user?")
                                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(cont, "Special Status Granted", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return true;

                    case 1:
                        //Ask for approval
                        new AlertDialog.Builder(cont, AlertDialog.THEME_HOLO_LIGHT)
                                .setTitle("Reject "+adapter.getGroup(groupPosition).title)
                                .setMessage("Do you wish to reject this user request?")
                                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(cont, "Request Rejected", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return true;

                    default:
                        return false;
                }

            }
        });
    }

    //create group item to add to expandable list
    public GroupItem genGroup(String s){
        GroupItem pcm=new GroupItem();
        pcm.title=s;
        pcm.items.add(new ChildItem("Approve Special Status"));
        pcm.items.add(new ChildItem("Reject Special Status"));
        return pcm;
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
                final Intent goToNotifications = new Intent(Special.this, Notifications.class);
                goToNotifications.putExtra("info",profileInfo);
                goToNotifications.putExtra("spl",spl);
                end();
                startActivity(goToNotifications);
                return true;

            case R.id.action_settings:
                //Profile selected
                final Intent goToProfile = new Intent(Special.this, Profile.class);
                goToProfile.putExtra("info",profileInfo);
                goToProfile.putExtra("spl",spl);
                end();
                startActivity(goToProfile);
                return true;

            case R.id.logout:
                //Logout selected
                final Intent logout = new Intent(Special.this, Login.class);
                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Logout")
                        .setMessage("Do you wish to log out?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final Intent goToStart = new Intent(Special.this, Start.class);
                                end();
                                startActivity(goToStart);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;

            case R.id.special:
                //special selected
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

}
