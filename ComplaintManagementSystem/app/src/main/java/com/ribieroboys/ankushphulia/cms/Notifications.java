package com.ribieroboys.ankushphulia.cms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Notifications extends AppCompatActivity {

    Bundle profileInfo;
    boolean spl;
    String url = "http://192.168.43.186:8080/notifications";
    AnimatedExpandableListView listView;
    ExampleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileInfo = getIntent().getBundleExtra("info");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent newcomp = new Intent(Notifications.this, New_complaint.class);
                        newcomp.putExtra("info", profileInfo);
                        startActivity(newcomp);
                    }
                });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // fetch all the complaints
        StringRequest threadsReq =
                new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // receive reply from server
                                try {
                                    JSONObject notifications = new JSONObject(response);
                                    JSONArray pers = notifications.getJSONArray("personal");
                                    JSONArray hos = notifications.getJSONArray("hostel");
                                    JSONArray dep = notifications.getJSONArray("department");
                                    JSONArray insti = notifications.getJSONArray("institute");

                                    // get a list of pending users (name+designation displayed)
                                    List<GroupItem> items = new ArrayList<GroupItem>();
                                    items.add(gengroup("Personal", pers));
                                    if (hos.length() > 0) {
                                        items.add(gengroup("Hostel Level", hos));
                                    }
                                    if (dep.length() > 0) {
                                        items.add(gengroup("Department Level", dep));
                                    }
                                    if (insti.length() > 0) {
                                        items.add(gengroup("Institute Level", insti));
                                    }
                                    adapter = new ExampleAdapter(getApplicationContext());
                                    adapter.setData(items);
                                    listView =
                                            (AnimatedExpandableListView)
                                                    findViewById(R.id.listViewNotifications);
                                    listView.setAdapter(adapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(
                                                Notifications.this,
                                                "Connection Error",
                                                Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

        RequestQ.getInstance().addToRequestQ(threadsReq);
    }

    public GroupItem gengroup(String s, JSONArray x) {
        GroupItem g = new GroupItem();
        g.title = s;
        for (int i = 0; i < x.length(); i++) {
            try {
                JSONObject j = x.getJSONObject(i);
                g.items.add(new ChildItem(j.getString("description")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return g;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Intent intent = getIntent();
        spl = intent.getBooleanExtra("spl", false);
        if (spl) {
            getMenuInflater().inflate(R.menu.main2, menu);
            return true;
        } else {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.notifications:
                // Notifications selected
                return true;

            case R.id.action_settings:
                // Profile selected
                final Intent goToProfile = new Intent(Notifications.this, Profile.class);
                goToProfile.putExtra("info", profileInfo);
                goToProfile.putExtra("spl", spl);
                end();
                startActivity(goToProfile);
                return true;

            case R.id.logout:
                // Logout selected
                final Intent logout = new Intent(Notifications.this, Login.class);
                new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Logout")
                        .setMessage("Do you wish to log out?")
                        .setPositiveButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                        .setNegativeButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        final Intent goToStart =
                                                new Intent(Notifications.this, Start.class);
                                        end();
                                        startActivity(goToStart);
                                    }
                                })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;

            case R.id.special:
                // special selected
                final Intent goToSpecial = new Intent(Notifications.this, Special.class);
                goToSpecial.putExtra("info", profileInfo);
                goToSpecial.putExtra("spl", spl);
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
        // close the activity
        end();
    }

    // custom classes meant for expandable lists and their children
    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;

        public ChildItem(String t) {
            this.title = t;
        }
    }

    // placeholders used by adpater
    private static class ChildHolder {
        TextView title;
    }

    private static class GroupHolder {
        TextView title;
    }

    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        // custom listview adapter for the classes we have
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
        public View getRealChildView(
                int groupPosition,
                int childPosition,
                boolean isLastChild,
                View convertView,
                ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                // holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            // holder.hint.setText(item.hint);

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
        public View getGroupView(
                int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
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
