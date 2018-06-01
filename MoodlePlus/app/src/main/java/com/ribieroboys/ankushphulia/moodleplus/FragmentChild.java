package com.ribieroboys.ankushphulia.moodleplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONObject;

public class FragmentChild extends Fragment {
    TextView overview, creditStructure;
    JSONObject overviewJSON;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        overview = (TextView) view.findViewById(R.id.overview_frag);
        creditStructure = (TextView) view.findViewById(R.id.credit_structure);

        try {
            overviewJSON = new JSONObject(getArguments().getString("Overview"));

            String textToSet = overviewJSON.getString("code") + "\n";
            textToSet += overviewJSON.getString("name");
            overview.setText(textToSet);

            textToSet = overviewJSON.getString("description") + "\n";
            textToSet += "Credits:\t" + overviewJSON.getString("credits") + "\n";
            textToSet += "(L-T-P:\t" + overviewJSON.getString("l_t_p") + ")\n\n";
            creditStructure.setText(textToSet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}
