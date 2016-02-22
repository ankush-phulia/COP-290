package com.ribieroboys.moodleplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentChild extends Fragment {
    String childname;
    String course;
    TextView textViewChildName;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Bundle bundle = getArguments();
        childname = bundle.getString("data");
        course = bundle.getString("course");

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        getIDs(view);

        return view;
    }

    private void getIDs(View view) {
        textViewChildName = (TextView) view.findViewById(R.id.textViewChild);
        textViewChildName.setText(course + "\n" + childname);
    }

}
