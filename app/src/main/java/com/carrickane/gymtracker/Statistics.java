package com.carrickane.gymtracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater,parent,savedInstanceState);
        View v = inflater.inflate(R.layout.graphs,parent,false);
        graphWeekly = (GraphView) v.findViewById(R.id.graph_weekly);
        graphYearly = (GraphView) v.findViewById(R.id.graph_yearly);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        graphYearly.getViewport().setScrollable(true);

        //graphWeekly.getViewport().setMinX(1);
        graphWeekly.getViewport().setMaxX(7);

        //graphYearly.getViewport().setMinX(1);
        graphYearly.getViewport().setMaxX(13);

        StaticLabelsFormatter labelsFormatter = new StaticLabelsFormatter(graphWeekly);
        labelsFormatter.setHorizontalLabels(WEEK_ARRAY);
        graphWeekly.getGridLabelRenderer().setLabelFormatter(labelsFormatter);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphYearly);
        staticLabelsFormatter.setHorizontalLabels(MONTH_ARRAY);
        graphYearly.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateWeeklyData());
        graphWeekly.addSeries(series);

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
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        int weekIndex = 7;
        values = new DataPoint[weekIndex];
        for (int i = 0; i < weekIndex; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String queryWeek = sdf.format(cal.getTime());
            double x = i;
            double y = (double) ExerciseData.findWithQuery(ExerciseData.class,
                    "SELECT * FROM EXERCISE_DATA WHERE DATE_INSERT =?",queryWeek).size();
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        return values;
    }

    private DataPoint[] generateYearlyData() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,Calendar.JANUARY);
        int monthIndex = 12;
        DataPoint[] values = new DataPoint[monthIndex];
        for (int i=0; i < monthIndex; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
            String queryMonth = sdf.format(cal.getTime());
            String completeArgs = new StringBuilder().append("SELECT * FROM EXERCISE_DATA " +
                    "WHERE DATE_INSERT LIKE").append("'%").append(queryMonth).
                    append("%'").toString();
            double x = i;
            double y = (double) ExerciseData.findWithQuery(ExerciseData.class, completeArgs).size();
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
            cal.add(Calendar.MONTH, 1);
        }
        return values;
    }
}