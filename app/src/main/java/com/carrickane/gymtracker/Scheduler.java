package com.carrickane.gymtracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.carrickane.gymtracker.adapters.ExerciseListAdapter;
import com.carrickane.gymtracker.database.ExerciseData;
import com.carrickane.gymtracker.database.ExerciseKindData;

import java.util.ArrayList;
import java.util.List;

import static com.carrickane.gymtracker.Constants.DATE_SHORT;
import static com.carrickane.gymtracker.Constants.QUERY_FULL_LIST;
import static com.carrickane.gymtracker.Constants.QUERY_KIND_OF_EXERCISE;
import static com.carrickane.gymtracker.Constants.SELECTED_DATE;
import static com.carrickane.gymtracker.Constants.SINGLE_KIND_OF_EXERCISE;

/**
 * Created by carrickane on 16.11.2016.
 */

public class Scheduler extends Fragment {

    RecyclerView exercisesList;
    FloatingActionButton addFAB;
    String dateShort;
    String dateInsert;
    String exerciseType;
    String exerciseSets;
    String exerciseRepeats;
    List<ExerciseData> exercisesData;
    RecyclerView.Adapter adapter;
    ArrayList<String> exercises;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View v = inflater.inflate(R.layout.scheduler,parent,false);

        dateInsert = getArguments().getString(SELECTED_DATE);
        dateShort = getArguments().getString(DATE_SHORT);

        exercisesList = (RecyclerView) v.findViewById(R.id.exercisesList);
        //setting recycler view
        exercisesList.setLayoutManager(new LinearLayoutManager(getContext()));
        exercisesData = ExerciseData.findWithQuery(ExerciseData.class,QUERY_FULL_LIST,dateInsert);
        adapter = new ExerciseListAdapter(exercisesData);
        exercisesList.setAdapter(adapter);
        exercisesList.addItemDecoration(new ItemDivider(getContext()));
        List kinds = ExerciseKindData.findWithQuery(ExerciseKindData.class,QUERY_KIND_OF_EXERCISE);
        exercises = new ArrayList<>(kinds);
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            //do nothing with moving element
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            //delete element form list and database by swiping
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //get position in list
                final int position = viewHolder.getAdapterPosition();
                //get position in database
                final ExerciseData data = exercisesData.get(position);
                //removing from list
                exercisesData.remove(viewHolder.getAdapterPosition());
                //telling adapter about changing
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
                //delete from database
                data.delete();
            }
        };
        //attaching helper to recycler view
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(exercisesList);

        addFAB = (FloatingActionButton) v.findViewById(R.id.addFAB);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExerciseViaDialog();
            }
        });
        return v;
    }

    private void addExerciseViaDialog() {

        final Spinner exerciseSpinner;
        final FloatingActionButton addExercise;
        final EditText exerciseET;
        final EditText setsET;
        final EditText repeatsET;

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.add_exercise);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.add_exercise_dialog, null);
        dialog.setView(v);
        exerciseSpinner = (Spinner)v.findViewById(R.id.exercisesSpinner);
        addExercise = (FloatingActionButton) v.findViewById(R.id.addExerciseFAB);
        exerciseET = (EditText) v.findViewById(R.id.exerciseET);
        setsET = (EditText) v.findViewById(R.id.setsET);
        repeatsET = (EditText) v.findViewById(R.id.repeatsET);
        exerciseET.setVisibility(View.INVISIBLE);

        //setting spinner adapter for choosing required element
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, exercises);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(spinnerAdapter);

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exerciseSpinner.setVisibility(View.INVISIBLE);
                exerciseType = null;
                exerciseET.setVisibility(View.VISIBLE);
                exerciseET.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            }
        });

        dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (exerciseSpinner.getVisibility() == View.VISIBLE) {
                    exerciseType = exerciseSpinner.getSelectedItem().toString();
                }
                else {
                    exerciseType = exerciseET.getText().toString();
                }

                //if type of exercise isn't present in database,add them. Otherwise do nothing
                List<ExerciseKindData> kind = ExerciseKindData.findWithQuery
                        (ExerciseKindData.class,SINGLE_KIND_OF_EXERCISE,exerciseType);

                if (kind.size() == 0) {
                    ExerciseKindData kindData = new ExerciseKindData(exerciseType);
                    kindData.save();
                }

                setsET.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                exerciseSets = setsET.getText().toString();
                repeatsET.setImeOptions(EditorInfo.IME_ACTION_DONE);
                exerciseRepeats = repeatsET.getText().toString();

                //saving data to database
                ExerciseData exerciseData = new ExerciseData(dateInsert,exerciseType,
                        exerciseSets,exerciseRepeats);
                exerciseData.save();
                //moving to main fragment after saving exercise
                onDetach();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {  }
        });
        dialog.show();
    }

    @Override
        public void onDetach() {
        super.onDetach();
        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain,mainActivityFragment).commit();
    }
}
