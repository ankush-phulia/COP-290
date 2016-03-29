package com.example.ribiero_boys.assignment_2;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class View_complaint extends Fragment {

    String user;
    String receiver;
    String topic;
    String descr;
    String resolved;
    Integer compl_id;
    ListView listView;
    String scope;
    ImageButton upvotes;
    ImageButton downvotes;
    TextView votes;
    String loggeduser;
    ArrayList<Integer> repl_with_ids;

    public View_complaint() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_view_complaint, container, false);

        //assign values to fields
        user=getArguments().getString("user", " ");
        topic=getArguments().getString("complaint_title", " ");
        compl_id=getArguments().getInt("cid", 0);
        descr=getArguments().getString("desc", "");
        scope=getArguments().getString("scope", " ");
        receiver=getArguments().getString("receiver"," ");
        loggeduser=getArguments().getString("loggeduser"," ");
        if (getArguments().getString("type").equals("Pending Complaints Made")||getArguments().getString("type").equals("Pending Complaints Received")){
            resolved="Resolved";
        }
        else{
            resolved="Pending";
        }

        ((Main)getActivity()).hideFloatingActionButton();

        FloatingActionButton fab3 = (FloatingActionButton) v.findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to add new reply
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Post a Reply");
                builder.setMessage("Enter your Comments");
                // Set up the input
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);
                // Set up the buttons
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), input.getText().toString(), Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        populateElements(v);
        upvotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                votes.setText((Integer.parseInt(votes.getText().toString()) + 1));
            }
        });
        downvotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                votes.setText((Integer.parseInt(votes.getText().toString()) - 1));
            }
        });

        //option to resolve complaint only with reciever
        Button resolved=(Button)v.findViewById(R.id.buttonResolved);
        if (loggeduser.equals(receiver) && resolved.equals("Pending") && !scope.equals("Personal")){
            resolved.setVisibility(View.VISIBLE);
        }
        else if(resolved.equals("Pending") && scope.equals("Personal")){
            resolved.setVisibility(View.VISIBLE);
        }
        else{
            resolved.setVisibility(View.INVISIBLE);
        }


        return v;
    }

    public void populateElements(View v){

        TextView title=(TextView)v.findViewById(R.id.textViewTitle);
        title.setText(topic);
        TextView descrip=(TextView)v.findViewById(R.id.textViewDescription);
        descrip.setText(descr);
        TextView sts=(TextView)v.findViewById(R.id.textViewStatus);
        sts.setText(resolved);
        TextView scpe=(TextView)v.findViewById(R.id.textViewScope);
        scpe.setText(scope);
        upvotes=(ImageButton)v.findViewById(R.id.Upvote);
        downvotes=(ImageButton)v.findViewById(R.id.Downvote);
        votes=(TextView) v.findViewById(R.id.Votes);
        votes.setText(getArguments().getString("votes"));

        // Populate our list with groups and it's children
        List<String> items = new ArrayList<String>();
        items.add("Reply 1");
        items.add("Reply 2");
        items.add("Reply 3");

        listView = (ListView) v.findViewById(R.id.listViewReplies);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items));

    }


}
