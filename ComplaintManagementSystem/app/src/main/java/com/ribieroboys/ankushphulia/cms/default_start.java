package com.ribieroboys.ankushphulia.cms;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class default_start extends Fragment {


    public default_start() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_default_start, container, false);
        //fetch the buttons
        Button gotologin=(Button)view.findViewById(R.id.login);
        Button gotosignup=(Button)view.findViewById(R.id.sign_up);

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login LoginFragment = new Login();
                FragmentTransaction change=getFragmentManager().beginTransaction();
                change.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right);
                change.replace(R.id.start_fragments, LoginFragment).addToBackStack("default");
                change.commit();
        }
        });

        gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sign_up SignFragment = new Sign_up();
                FragmentTransaction change=getFragmentManager().beginTransaction();
                change.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,R.anim.slide_in_right,R.anim.slide_out_left);
                change.replace(R.id.start_fragments, SignFragment).addToBackStack("default2");
                change.commit();
            }
        });

        return view;
    }


}
