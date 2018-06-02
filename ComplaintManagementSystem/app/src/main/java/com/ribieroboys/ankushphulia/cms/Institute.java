package com.ribieroboys.ankushphulia.cms;


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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Institute extends Fragment {

    ListView listView;
    ArrayList<Integer> compl_with_ids=new ArrayList<Integer>();
    ArrayList<Integer> votes=new ArrayList<Integer>();
    String ttype;
    List<String> OP = new ArrayList<String>();
    List<String> RC = new ArrayList<String>();
    List<String> desc = new ArrayList<String>();
    String loggeduser;
    Bundle profileInfo;
    Boolean spl;

    public Institute() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_institute, container, false);
        List<String> items = new ArrayList<String>();

        //set complaint type
        TextView type = (TextView)view.findViewById(R.id.textView24);
        ttype = getArguments().getString("type","");
        loggeduser = getArguments().getString("user");
        type.setText(ttype);
        profileInfo = getArguments().getBundle("profileInfo");
        spl = getArguments().getBoolean("spl");

        //get complaint_list
        try {
            JSONObject complaints=new JSONObject(getArguments().getString("cjson"));
            JSONObject type_compl;
            JSONArray relevant_compl;
            switch (ttype){
                case "Pending Complaints Made":
                    type_compl=complaints.getJSONObject("pendingPosted");
                    relevant_compl=type_compl.getJSONArray("institute");
                    //compl_with_ids=getArguments().getIntegerArrayList("compl_ids_pers",new ArrayList<Integer>());
                    // Populate our list with groups and it's children
                    for (int i=0; i<relevant_compl.length();i++){
                        JSONObject cmplt= (JSONObject) relevant_compl.get(i);
                        compl_with_ids.add(cmplt.getInt("complaintID"));
                        items.add(cmplt.getString("topic"));
                        OP.add(cmplt.getString("posterName"));
                        RC.add(cmplt.getString("receiverName"));
                        desc.add(cmplt.getString("description"));
                        votes.add(cmplt.getInt("votes"));
                    }
                    break;
                case "Resolved Complaints Made":
                    type_compl=complaints.getJSONObject("resolvedPosted");
                    relevant_compl=type_compl.getJSONArray("institute");
                    //compl_with_ids=getArguments().getIntegerArrayList("compl_ids_pers",new ArrayList<Integer>());
                    // Populate our list with groups and it's children
                    for (int i=0; i<relevant_compl.length();i++){
                        JSONObject cmplt= (JSONObject) relevant_compl.get(i);
                        compl_with_ids.add(cmplt.getInt("complaintID"));
                        items.add(cmplt.getString("topic"));
                        OP.add(cmplt.getString("posterName"));
                        RC.add(cmplt.getString("receiverName"));
                        desc.add(cmplt.getString("description"));
                        votes.add(cmplt.getInt("votes"));
                    }
                    break;
                case "Pending Complaints Received":
                    type_compl=complaints.getJSONObject("pendingReceived");
                    relevant_compl=type_compl.getJSONArray("institute");
                    //compl_with_ids=getArguments().getIntegerArrayList("compl_ids_pers",new ArrayList<Integer>());
                    // Populate our list with groups and it's children
                    for (int i=0; i<relevant_compl.length();i++){
                        JSONObject cmplt= (JSONObject) relevant_compl.get(i);
                        compl_with_ids.add(cmplt.getInt("complaintID"));
                        items.add(cmplt.getString("topic"));
                        OP.add(cmplt.getString("posterName"));
                        RC.add(cmplt.getString("receiverName"));
                        desc.add(cmplt.getString("description"));
                        votes.add(cmplt.getInt("votes"));
                    }
                    break;
                case "Resolved Complaints Received":
                    type_compl=complaints.getJSONObject("resolvedReceived");
                    relevant_compl=type_compl.getJSONArray("institute");
                    //compl_with_ids=getArguments().getIntegerArrayList("compl_ids_pers",new ArrayList<Integer>());
                    // Populate our list with groups and it's children
                    for (int i=0; i<relevant_compl.length();i++) {
                        JSONObject cmplt = (JSONObject) relevant_compl.get(i);
                        compl_with_ids.add(cmplt.getInt("complaintID"));
                        items.add(cmplt.getString("topic"));
                        OP.add(cmplt.getString("posterName"));
                        RC.add(cmplt.getString("receiverName"));
                        desc.add(cmplt.getString("description"));
                        votes.add(cmplt.getInt("votes"));
                    }
                    break;
            }
            listView = (ListView) view.findViewById(R.id.complaints4);
            listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String sel_compl = ((TextView) view).getText().toString();
                    Integer idd = compl_with_ids.get(position);
                    Bundle args = new Bundle();
                    args.putString("complaint_title", sel_compl);
                    args.putInt("cid", idd);
                    args.putString("scope", "Institute Level");
                    args.putString("user", OP.get(position));
                    args.putString("receiver",RC.get(position));
                    args.putString("desc",desc.get(position));
                    args.putString("type",getArguments().getString("type", ""));
                    args.putInt("votes", votes.get(position));
                    args.putString("loggeduser",loggeduser);
                    args.putBundle("profileInfo", profileInfo);
                    args.putBoolean("spl", spl);

                    View_complaint Complaint = new View_complaint();
                    FragmentTransaction change = getFragmentManager().beginTransaction();
                    change.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                    Complaint.setArguments(args);
                    change.replace(R.id.MainFragments, Complaint).addToBackStack("default");
                    change.commit();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((Main) getActivity()).showFloatingActionButton();

        return view;
    }

}
