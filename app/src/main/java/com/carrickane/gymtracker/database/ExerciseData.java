package com.carrickane.gymtracker.database;

import com.orm.SugarRecord;

/**
 * Created by carrickane on 16.11.2016.
 */

public class ExerciseData extends SugarRecord {

    //table columns
    String dateInsert;
    String exerciseType;
    String exerciseSets;
    String exerciseRepeats;

    //required empty constructor
    public ExerciseData() {
    }

    //initializing column vars
    public ExerciseData(String dateInsert, String exerciseType,
                        String exerciseSets, String exerciseRepeats) {
        this.dateInsert = dateInsert;
        this.exerciseType = exerciseType;
        this.exerciseSets = exerciseSets;
        this.exerciseRepeats = exerciseRepeats;
    }

    //building string with saved elements
    @Override
    public String toString() {
        return  "EXERCISE:" + " " + exerciseType + '\n' +
                "SETS:" + " " + exerciseSets + '\n' +
                "REPEATS:" + " " + exerciseRepeats;
    }


}
