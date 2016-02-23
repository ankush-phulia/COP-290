package com.ribieroboys.moodleplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

public class FragmentChild1 extends Fragment {
    TextView assignView;
    JSONArray assignJSON;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_child1, container, false);
        assignView = (TextView) view.findViewById(R.id.textViewChild1);

        try {
            assignJSON = new JSONArray(getArguments().getString("Assignments"));
            //do something here

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}
