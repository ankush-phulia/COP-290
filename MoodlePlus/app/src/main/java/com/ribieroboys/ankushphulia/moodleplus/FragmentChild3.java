package com.ribieroboys.ankushphulia.moodleplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class FragmentChild3 extends Fragment {
    JSONArray threadsJSON;
    ExpandableListAdapterSecond listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    FloatingActionButton newth;

    ArrayList<String> threads;
    ArrayList<String> comments;
    ArrayList<String> timesReadable;
    ArrayList<String> threadID;
    ArrayList<String> commentUsers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_child3, container, false);

        try {
            threadsJSON = new JSONArray(getArguments().getString("Threads"));

            expListView = (ExpandableListView) view.findViewById(R.id.expandableListViewThreads);
            newth = (FloatingActionButton) view.findViewById(R.id.add_thread);

            newth.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent newThread = new Intent(getContext(), NewThread.class);
                            newThread.putExtra(
                                    "courseCode", getArguments().getString("courseCode"));

                            newThread.putExtra("URL", getArguments().getString("URL"));
                            newThread.putExtra(
                                    "/default/notifications.json",
                                    getArguments().getString("/default/notifications.json"));
                            newThread.putExtra(
                                    "/default/grades.json",
                                    getArguments().getString("/default/grades.json"));
                            newThread.putExtra(
                                    "/courses/list.json",
                                    getArguments().getString("/courses/list.json"));
                            newThread.putStringArrayListExtra(
                                    "courseListCodes",
                                    getArguments().getStringArrayList("courseListCodes"));
                            newThread.putExtra(
                                    "loginResponse", getArguments().getBundle("loginResponse"));
                            startActivity(newThread);
                        }
                    });

            prepareListData();
            listAdapter =
                    new ExpandableListAdapterSecond(
                            this.getContext(), listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        expListView.setOnChildClickListener(
                new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(
                            ExpandableListView parent,
                            View v,
                            int groupPosition,
                            int childPosition,
                            long id) {
                        Intent newComment = new Intent(getContext(), ViewThread.class);
                        newComment.putExtra("courseCode", getArguments().getString("courseCode"));
                        newComment.putExtra("threadID", threadID.get(groupPosition));
                        newComment.putExtra("threads", threads.get(groupPosition));
                        newComment.putExtra("comments", comments.get(groupPosition));
                        newComment.putExtra("timesReadable", timesReadable.get(groupPosition));
                        newComment.putExtra("commentUsers", commentUsers.get(groupPosition));

                        newComment.putExtra("URL", getArguments().getString("URL"));
                        newComment.putExtra(
                                "/default/notifications.json",
                                getArguments().getString("/default/notifications.json"));
                        newComment.putExtra(
                                "/default/grades.json",
                                getArguments().getString("/default/grades.json"));
                        newComment.putExtra(
                                "/courses/list.json",
                                getArguments().getString("/courses/list.json"));
                        newComment.putStringArrayListExtra(
                                "courseListCodes",
                                getArguments().getStringArrayList("courseListCodes"));
                        newComment.putExtra(
                                "loginResponse", getArguments().getBundle("loginResponse"));
                        startActivity(newComment);
                        return true;
                    }
                });

        expListView.setOnGroupClickListener(
                new ExpandableListView.OnGroupClickListener() {

                    @Override
                    public boolean onGroupClick(
                            ExpandableListView parent, View v, int groupPosition, long id) {
                        switch (groupPosition) {
                            default:
                                return false;
                        }
                    }
                });

        expListView.setOnGroupExpandListener(
                new ExpandableListView.OnGroupExpandListener() {

                    @Override
                    public void onGroupExpand(int groupPosition) {}
                });

        expListView.setOnGroupCollapseListener(
                new ExpandableListView.OnGroupCollapseListener() {

                    @Override
                    public void onGroupCollapse(int groupPosition) {}
                });

        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        threads = new ArrayList<>();
        comments = new ArrayList<>();
        timesReadable = new ArrayList<>();
        threadID = new ArrayList<>();
        commentUsers = new ArrayList<>();

        try {
            for (int threadNo = 0; threadNo < threadsJSON.length(); threadNo++) {
                JSONObject thread = (JSONObject) threadsJSON.get(threadNo);
                threads.add(thread.toString());
                String header = thread.getString("title");
                listDataHeader.add(header);

                List<String> childData = new ArrayList<>();
                childData.add(
                        "Updated on: "
                                + thread.getString("updated_at")
                                + "\n"
                                + thread.getString("description"));
                listDataChild.put(listDataHeader.get(threadNo), childData);

                getComments(thread.getString("id"));
                threadID.add(thread.getString("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getComments(String id) {
        try {
            String threadsURL = getArguments().getString("URL") + "/threads/thread.json/" + id;
            StringRequest threadsReq =
                    new StringRequest(
                            Request.Method.GET,
                            threadsURL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // receive reply from server
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        comments.add(
                                                jsonResponse.getJSONArray("comments").toString());
                                        timesReadable.add(
                                                jsonResponse
                                                        .getJSONArray("times_readable")
                                                        .toString());
                                        commentUsers.add(
                                                jsonResponse
                                                        .getJSONArray("comment_users")
                                                        .toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Toast.makeText(FragmentChild3.this, "Connection Error",
                                    // Toast.LENGTH_LONG).show();
                                }
                            });

            RequestQ.getInstance().addToRequestQ(threadsReq);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
