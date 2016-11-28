package com.carrickane.gymtracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrickane.gymtracker.database.ExerciseData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.carrickane.gymtracker.Constants.MONTH_ARRAY;
import static com.carrickane.gymtracker.Constants.WEEK_ARRAY;

/**
 * Created by carrickane on 16.11.2016.
 */

public class Statistics extends Fragment {

    GraphView graphWeekly;
    GraphView graphYearly;
    DataPoint[] values;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater,parent,savedInstanceState);
        View v = inflater.inflate(R.layout.graphs,parent,false);
        graphWeekly = (GraphView) v.findViewById(R.id.graph_weekly);
        graphYearly = (GraphView) v.findViewById(R.id.graph_yearly);

        //setting max X coordinates for each graph
        graphWeekly.getViewport().setMaxX(7);
        graphYearly.getViewport().setMaxX(12);

        //filling descriptions for X coordinates on week and year from constant arrays
        StaticLabelsFormatter labelsFormatter = new StaticLabelsFormatter(graphWeekly);
        labelsFormatter.setHorizontalLabels(WEEK_ARRAY);
        graphWeekly.getGridLabelRenderer().setLabelFormatter(labelsFormatter);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphYearly);
        staticLabelsFormatter.setHorizontalLabels(MONTH_ARRAY);
        graphYearly.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        //initializing and drawing linear graph for current week by calling method generateWeeklyData()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateWeeklyData());
        graphWeekly.addSeries(series);

        //initializing and drawing bar graph for current year by calling method generateYearlyData()
        BarGraphSeries<DataPoint> seriesYearly = new BarGraphSeries<>(generateYearlyData());
        graphYearly.addSeries(seriesYearly);

        // styling yearly graph
        seriesYearly.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4,
                        (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        //set spacing between bars
        seriesYearly.setSpacing(30);

        // draw values on top of bars
        seriesYearly.setDrawValuesOnTop(true);
        seriesYearly.setValuesOnTopColor(Color.RED);
        seriesYearly.setValuesOnTopSize(20);

        return v;
    }


    private DataPoint[] generateWeeklyData() {
        //getting current day of week
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        int weekIndex = 7;
        values = new DataPoint[weekIndex];
        //get count of exercises for each day of week (from first day to last)
        for (int i = 0; i < weekIndex; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String queryWeek = sdf.format(cal.getTime());
            double x = i;
            double y = (double) ExerciseData.findWithQuery(ExerciseData.class,
                    "SELECT * FROM EXERCISE_DATA WHERE DATE_INSERT =?",queryWeek).size();
            //setting coordinates
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
            //moving for next day of week
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        return values;
    }

    private DataPoint[] generateYearlyData() {
        //getting current month
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,Calendar.JANUARY);
        int monthIndex = 12;
        DataPoint[] values = new DataPoint[monthIndex];
        //get count of exercises for each month of year (from first to last)
        for (int i=0; i < monthIndex; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
            String queryMonth = sdf.format(cal.getTime());
            String completeArgs = new StringBuilder().append("SELECT * FROM EXERCISE_DATA " +
                    "WHERE DATE_INSERT LIKE").append("'%").append(queryMonth).
                    append("%'").toString();
            double x = i;
            double y = (double) ExerciseData.findWithQuery(ExerciseData.class, completeArgs).size();
            //setting coordinates
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
            //moving for next month
            cal.add(Calendar.MONTH, 1);
        }
        return values;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameMain,mainActivityFragment).commit();
    }

}