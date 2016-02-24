package com.ribieroboys.moodleplus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class Grades extends Fragment {
    TextView grades;
    JSONArray gradesJSON, coursesJSON;

    public Grades() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grades, container, false);
        grades = (TextView) rootView.findViewById(R.id.grades);

        try {
            //retreive grades
            gradesJSON = (new JSONObject(getArguments().getString("/default/grades.json"))).getJSONArray("grades");
            coursesJSON = (new JSONObject(getArguments().getString("/default/grades.json"))).getJSONArray("courses");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(gradesJSON.length() > 0) {
            //parse the grades JSON
            String textToSet = "";
            try {
                for (int i = 0; i < gradesJSON.length(); i++) {
                    textToSet += Integer.toString(i + 1) + ". ";
                    textToSet += ((JSONObject) coursesJSON.get(i)).getString("code") + "\n";
                    JSONObject grades = ((JSONObject) gradesJSON.get(i));
                    textToSet += "\tScored " + grades.getString("score") + " / " + grades.getString("out_of") + " in " + grades.getString("name") + "\n";
                    textToSet += "\t(Weightage:\t" + grades.getString("weightage") + ")\n\n";
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            grades.setText(textToSet);
        }
        else {
            grades.setText("No grades available yet!");
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
