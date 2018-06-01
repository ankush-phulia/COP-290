package com.ribieroboys.moodleplus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Grades extends Fragment {
    TextView grades;
    JSONArray gradesJSON, coursesJSON;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    public Grades() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grades, container, false);
        grades = (TextView) rootView.findViewById(R.id.grades);
        /*        expListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        listAdapter = new ExpandableListAdapter(this.getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        prepareListData();*/

        try {
            // retreive grades
            gradesJSON =
                    (new JSONObject(getArguments().getString("/default/grades.json")))
                            .getJSONArray("grades");
            coursesJSON =
                    (new JSONObject(getArguments().getString("/default/grades.json")))
                            .getJSONArray("courses");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gradesJSON.length() > 0) {
            // parse the grades JSON
            String textToSet = "";
            try {
                for (int i = 0; i < gradesJSON.length(); i++) {
                    textToSet += " " + Integer.toString(i + 1) + ". ";
                    textToSet +=
                            ((JSONObject) coursesJSON.get(i)).getString("code").toUpperCase()
                                    + "\n";
                    JSONObject grades = ((JSONObject) gradesJSON.get(i));
                    textToSet +=
                            "\tScored "
                                    + grades.getString("score")
                                    + " / "
                                    + grades.getString("out_of")
                                    + " in "
                                    + grades.getString("name")
                                    + "\n";
                    textToSet += "\t(Weightage:\t" + grades.getString("weightage") + ")\n\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            grades.setText(textToSet);
        } else {
            grades.setText("No grades available yet!");
        }

        /*expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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
        */
        return rootView;
    }

    /* private void prepareListData() {
        try {

            for (int assignNo=0; assignNo < gradesJSON.length(); assignNo++) {
                String s=coursesJSON.get(assignNo).getString("code");
                if (!listDataHeader.contains(s)){
                    listDataHeader.add(s);
                }
                String textToSet="";
                JSONObject grades = ((JSONObject) gradesJSON.get(assignNo));
                textToSet += "\tScored " + grades.getString("score") + " / " + grades.getString("out_of") + " in " + grades.getString("name") + "\n";
                textToSet += "\t(Weightage:\t" + grades.getString("weightage") + ")\n\n";
                List<String> childData = new ArrayList<>();
                childData.add(textToSet);
                listDataChild.put(listDataHeader.get(assignNo), childData);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
