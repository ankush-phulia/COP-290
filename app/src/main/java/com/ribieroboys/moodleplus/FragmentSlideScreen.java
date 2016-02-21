package com.ribieroboys.moodleplus;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by nitin on 20/2/16.
 */
public class FragmentSlideScreen extends Fragment{

    public static final String ARG_PAGE = "page";
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static FragmentSlideScreen create(int pageNumber) {
        Log.d("FragmentSlideScreen", "create");

        FragmentSlideScreen fragment = new FragmentSlideScreen();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("FragmentSlideScreen", "onCreate");

        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("FragmentSlideScreen", "onCreateView");

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide_screen, container, false);
        ((TextView) rootView.findViewById(R.id.fragmentTextBox)).setText(
                "Course " + mPageNumber + ": ");

        return rootView;

    }

    public int getPageNumber() {
        return mPageNumber;
    }
}
