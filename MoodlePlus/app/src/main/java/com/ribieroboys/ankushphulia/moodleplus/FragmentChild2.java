package com.ribieroboys.ankushphulia.moodleplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

public class FragmentChild2 extends Fragment {
    JSONArray gradesJSON;
    TextView gradesView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_child2, container, false);
        gradesView = (TextView) view.findViewById(R.id.grades_frag);

        try {
            gradesJSON = new JSONArray(getArguments().getString("Grades"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gradesJSON.length() > 0) {
            String textToSet = "";
            try {
                for (int i = 0; i < gradesJSON.length(); i++) {
                    textToSet += Integer.toString(i + 1) + ". ";
                    JSONObject grades = ((JSONObject) gradesJSON.get(i));
                    textToSet += grades.getString("name") + "\n";
                    textToSet +=
                            "\tScored "
                                    + grades.getString("score")
                                    + " / "
                                    + grades.getString("out_of")
                                    + "\n";
                    textToSet += "\t(Weightage:\t" + grades.getString("weightage") + ")\n\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            gradesView.setText(textToSet);
        } else {
            gradesView.setText("No grades available yet!");
        }

        return view;
    }
}
