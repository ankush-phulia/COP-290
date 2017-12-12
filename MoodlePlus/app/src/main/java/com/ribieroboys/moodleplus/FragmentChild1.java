package com.ribieroboys.moodleplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentChild1 extends Fragment {
    JSONArray assignJSON;
    ExpandableListAdapterSecond listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_child1, container, false);

        try {
            assignJSON = new JSONArray(getArguments().getString("Assignments"));

            expListView = (ExpandableListView) view.findViewById(R.id.expandableListViewAssign);

            prepareListData();
            listAdapter = new ExpandableListAdapterSecond(this.getContext(), listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
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
                        return false;
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
        try {
            for (int assignNo=0; assignNo < assignJSON.length(); assignNo++) {
                JSONObject assignment = (JSONObject) assignJSON.get(assignNo);
                String header = assignment.getString("name");
                listDataHeader.add(header);

                List<String> childData = new ArrayList<>();
                childData.add("Deadline: " + assignment.getString("deadline") + "\n");
                childData.add(assignment.getString("description"));
                listDataChild.put(listDataHeader.get(assignNo), childData);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
