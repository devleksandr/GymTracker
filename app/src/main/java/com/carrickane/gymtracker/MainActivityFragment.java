package com.carrickane.gymtracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by carrickane on 16.11.2016.
 */

public class MainActivityFragment extends Fragment {

    RecyclerView recentList;
    private RecyclerView.Adapter adapter;
    List<ExerciseData> lastTenData;

    public MainActivityFragment(){
    }

    @Override
    public View onCreateView (final LayoutInflater inflater,
                              final ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.content_main, container, false);
        recentList = (RecyclerView)v.findViewById(R.id.recentExer);
        lastTenData = ExerciseData.findWithQuery(ExerciseData.class,
                "SELECT * FROM (SELECT * FROM EXERCISE_DATA DESC LIMIT 10) ORDER BY ID DESC");
        //setting recycler view
        recentList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExerciseListAdapter(lastTenData);
        recentList.setAdapter(adapter);
        recentList.addItemDecoration(new ItemDivider(getContext()));
        return v;
    }
}
