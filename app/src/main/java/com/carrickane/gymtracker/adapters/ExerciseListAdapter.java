package com.carrickane.gymtracker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carrickane.gymtracker.R;
import com.carrickane.gymtracker.database.ExerciseData;

import java.util.List;

/**
 * Created by carrickane on 16.11.2016.
 */

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {

    private static List<ExerciseData> exerciseData;

    //initializing elements via constructor
    public ExerciseListAdapter(List<ExerciseData> exerciseData) {
        this.exerciseData = exerciseData;
    }

    //initializing view holder
    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView exerciseTV;

        ViewHolder(View itemView) {
            super(itemView);
            exerciseTV = (TextView) itemView.findViewById(R.id.exerciseTV);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,
                parent,false);
        return (new ViewHolder(v));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ExerciseData exercise = exerciseData.get(position);
        holder.exerciseTV.setText(exercise.toString());
    }

    @Override
    public int getItemCount() {
        return exerciseData.size();
    }
}
