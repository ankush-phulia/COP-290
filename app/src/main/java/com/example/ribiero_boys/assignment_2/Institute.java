package com.example.ribiero_boys.assignment_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Institute extends Fragment {

    ListView listView;
    ArrayList<Integer> compl_with_ids;

    public Institute() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_institute, container, false);

        //set complaint type
        TextView type=(TextView)view.findViewById(R.id.textView24);
        type.setText(getArguments().getString("type",""));

        // Populate our list with groups and it's children
        List<String> items = new ArrayList<String>();
        items.add("Complaint 1");
        items.add("Complaint 2");
        items.add("Complaint 3");

        ((Main)getActivity()).showFloatingActionButton();

        listView = (ListView) view.findViewById(R.id.complaints4);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, items));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sel_compl = ((TextView) view).getText().toString();
                Integer idd = compl_with_ids.get(position);
                Bundle args = new Bundle();
                args.putString("complaint_title", sel_compl);
                args.putInt("cid", idd);
                args.putString("user", getArguments().getString("username", ""));
                args.putString("scope","Institute Level");

                View_complaint Complaint = new View_complaint();
                Complaint.setArguments(args);
                FragmentTransaction change = getFragmentManager().beginTransaction();
                change.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                change.replace(R.id.MainFragments, Complaint).addToBackStack("default");
                change.commit();
            }
        });

        return view;
    }

}
