package com.ribieroboys.moodleplus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dashboard extends Fragment {

    String user;
    String pass;
    CalendarView calendarview;

    public Dashboard() {
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
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // retreive user details
        user = getArguments().getString("user");
        pass = getArguments().getString("pass");
        TextView welcome = (TextView) rootView.findViewById(R.id.textView14);
        welcome.setText("Welcome " + user);

        calendar_setup(rootView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void calendar_setup(View v) {
        // Setup the calendar view
        calendarview = (CalendarView) v.findViewById(R.id.calendarView);
        long d = calendarview.getDate();
        String dateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date(d));
        // set start date to be displayed
        String start = "01" + dateString.substring(2);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long s = date.getTime();
        calendarview.setMinDate(s);

        // set end date to be displayed
        String end;
        int month = Integer.parseInt(dateString.substring(3, 5));
        if (month == 2) {
            end = "29" + dateString.substring(2);
        } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10
                || month == 12) {
            end = "31" + dateString.substring(2);
        } else {
            end = "30" + dateString.substring(2);
        }
        Date date2 = null;
        try {
            date2 = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long e = date2.getTime();
        calendarview.setMaxDate(e);
    }
}
