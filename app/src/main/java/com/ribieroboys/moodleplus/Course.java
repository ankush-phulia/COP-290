package com.ribieroboys.moodleplus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Course extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    String courseCode;

    @Override
    public void onCreate(Bundle savedInstanceState){
        courseCode = getArguments().getString("courseCode");

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent, container, false);

        this.getIDs(view);
        this.setEvents();
        this.addPage("Overview",0);
        this.addPage("Assignments",1);
        this.addPage("Grades",2);
        this.addPage("Threads",3);

        viewPager.setCurrentItem(0);

        return view;
    }

    private void getIDs(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.my_viewpager);
        adapter = new ViewPagerAdapter(getFragmentManager(), getActivity());
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) view.findViewById(R.id.my_tab_layout);
        getArguments().getString("courseCode");
    }

    int selectedTabPosition;

    private void setEvents() {

        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                viewPager.setCurrentItem(tab.getPosition());
                selectedTabPosition = viewPager.getCurrentItem();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
        });
    }

    public void addPage(String pagename, int i) {
        switch(i){
            case 0:
                FragmentChild fragmentChild = new FragmentChild();
                fragmentChild.setArguments(getArguments());
                adapter.addFrag(fragmentChild, pagename);
                notifyAdapterAndPager();
                break;

            case 1:
                FragmentChild1 fragmentChild1 = new FragmentChild1();
                fragmentChild1.setArguments(getArguments());
                adapter.addFrag(fragmentChild1, pagename);
                notifyAdapterAndPager();
                break;

            case 2:
                FragmentChild2 fragmentChild2 = new FragmentChild2();
                fragmentChild2.setArguments(getArguments());
                adapter.addFrag(fragmentChild2, pagename);
                notifyAdapterAndPager();
                break;

            case 3:
                FragmentChild3 fragmentChild3 = new FragmentChild3();
                fragmentChild3.setArguments(getArguments());
                adapter.addFrag(fragmentChild3, pagename);
                notifyAdapterAndPager();
                break;
        }
    }

    private void notifyAdapterAndPager() {
        adapter.notifyDataSetChanged();
        if (adapter.getCount() > 0)
            tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(adapter.getCount() - 1);
        setupTabLayout();
    }

    public void setupTabLayout() {
        selectedTabPosition = viewPager.getCurrentItem();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(adapter.getTabView(i));
        }

    }

}
