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

public class Notifications extends Fragment {
    TextView notifications;
    JSONArray notiJSON;

    public Notifications() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        notifications = (TextView) rootView.findViewById(R.id.notifications);

        try {
            //retreive notifications
            notiJSON = (new JSONObject(getArguments().getString("/default/notifications.json"))).getJSONArray("notifications");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(notiJSON.length() > 0) {
            //Parse the Notifications JSON
            String textToSet = "";
            try {
                for (int i = 0; i < notiJSON.length(); i++) {
                    textToSet += Integer.toString(i+1) + ". ";
                    textToSet += ((JSONObject) notiJSON.get(i)).getString("description") + "\n";
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            notifications.setText(textToSet);
        }
        else {
            notifications.setText("No notifications yet!");
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
