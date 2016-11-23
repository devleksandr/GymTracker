package com.carrickane.gymtracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
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

public class Scheduler extends Activity {

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
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduler);

        Intent intent = getIntent();
        dateInsert = intent.getStringExtra(SELECTED_DATE);
        dateShort = intent.getStringExtra(DATE_SHORT);

        exercisesList = (RecyclerView) findViewById(R.id.exercisesList);
        //setting recycler view
        exercisesList.setLayoutManager(new LinearLayoutManager(this));
        exercisesData = ExerciseData.findWithQuery(ExerciseData.class,QUERY_FULL_LIST,dateInsert);
        adapter = new ExerciseListAdapter(exercisesData);
        exercisesList.setAdapter(adapter);
        exercisesList.addItemDecoration(new ItemDivider(this));
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

        addFAB = (FloatingActionButton) findViewById(R.id.addFAB);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExerciseViaDialog();
            }
        });
    }

    private void addExerciseViaDialog() {

        final Spinner exerciseSpinner;
        final FloatingActionButton addExercise;
        final EditText exerciseET;
        final EditText setsET;
        final EditText repeatsET;

        final AlertDialog.Builder dialog = new AlertDialog.Builder(Scheduler.this);
        dialog.setTitle(R.string.add_exercise);
        View v = getLayoutInflater().inflate(R.layout.add_exercise_dialog, null);
        dialog.setView(v);
        exerciseSpinner = (Spinner)v.findViewById(R.id.exercisesSpinner);
        addExercise = (FloatingActionButton) v.findViewById(R.id.addExerciseFAB);
        exerciseET = (EditText) v.findViewById(R.id.exerciseET);
        setsET = (EditText) v.findViewById(R.id.setsET);
        repeatsET = (EditText) v.findViewById(R.id.repeatsET);
        exerciseET.setVisibility(View.INVISIBLE);

        //setting spinner adapter for choosing required element
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
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

                List<ExerciseKindData> kind = ExerciseKindData.findWithQuery
                        (ExerciseKindData.class,SINGLE_KIND_OF_EXERCISE,exerciseType);
                if (kind.size() == 0) {
                    ExerciseKindData kindData = new ExerciseKindData(exerciseType);
                    kindData.save();
                }
                else {
                }
                setsET.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                exerciseSets = setsET.getText().toString();
                repeatsET.setImeOptions(EditorInfo.IME_ACTION_DONE);
                exerciseRepeats = repeatsET.getText().toString();

                //saving data to database
                ExerciseData exerciseData = new ExerciseData(dateInsert,exerciseType,
                        exerciseSets,exerciseRepeats);
                exerciseData.save();
                Intent intent = new Intent(Scheduler.this,MainActivity.class);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {  }
        });
        dialog.show();
    }

    @Override
        public void onBackPressed() {
                super.onBackPressed();
                Intent intent = new Intent(Scheduler.this,MainActivity.class);
                startActivity(intent);
        }
}
