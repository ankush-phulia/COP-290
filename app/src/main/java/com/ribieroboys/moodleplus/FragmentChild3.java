package com.ribieroboys.moodleplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentChild3 extends Fragment {
    TextView threadsView;
    JSONArray threadsJSON;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    FloatingActionButton newth;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_child3, container, false);
        threadsView = (TextView) view.findViewById(R.id.textViewChild3);

        try {
            threadsJSON = new JSONArray(getArguments().getString("Threads"));

            expListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
            newth =(FloatingActionButton) view.findViewById(R.id.add_thread);

            newth.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), NewThread.class);
                    startActivity(intent);
                }
            });



            listAdapter = new ExpandableListAdapter(this.getContext(), listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
            prepareListData();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                return true;
            }
        });


        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                switch (groupPosition) {

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


        return view;
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
        listDataChild.put(listDataHeader.get(0), Overview);
        listDataChild.put(listDataHeader.get(1), Notifications);
        listDataChild.put(listDataHeader.get(2), Grades);
        listDataChild.put(listDataHeader.get(3), Courses);
    }



}
