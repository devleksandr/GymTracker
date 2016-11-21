package com.carrickane.gymtracker.database;

import com.orm.SugarRecord;

/**
 * Created by carrickane on 17.11.2016.
 */

public class ExerciseKindData extends SugarRecord {

    //table columns
    String exerciseKind;


    //required empty constructor
    public ExerciseKindData() {
    }

    //initializing column vars
    public ExerciseKindData(String exerciseKind) {
        this.exerciseKind = exerciseKind;

    }

    //building string with saved elements
    @Override
    public String toString() {
        return  exerciseKind;
    }

}
