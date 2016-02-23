package com.ribieroboys.moodleplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class FragmentChild3 extends Fragment {
    TextView threadsView;
    JSONArray threadsJSON;

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
            //do something here

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


}
