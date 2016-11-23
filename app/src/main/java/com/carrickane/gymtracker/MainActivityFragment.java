package com.carrickane.gymtracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrickane.gymtracker.adapters.ViewPagerAdapter;

import static com.carrickane.gymtracker.Constants.CALENDAR;
import static com.carrickane.gymtracker.Constants.RECENT_FRAGMENT;

/**
 * Created by carrickane on 16.11.2016.
 */

public class MainActivityFragment extends Fragment {

    ViewPager viewPager;

    public MainActivityFragment(){
    }

    @Override
    public View onCreateView (final LayoutInflater inflater,
                              final ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.app_bar_main, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        return v;
    }

    //setting fragment switching
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new RecentListFragment(),RECENT_FRAGMENT);
        adapter.addFragment(new CalendarFragment(),CALENDAR);
        viewPager.setAdapter(adapter);
    }

}
