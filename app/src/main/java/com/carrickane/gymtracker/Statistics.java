package com.carrickane.gymtracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by carrickane on 16.11.2016.
 */

public class Statistics extends AppCompatActivity {

    GraphView graphWeekly;
    GraphView graphYearly;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphs);
        graphWeekly = (GraphView) findViewById(R.id.graph_weekly);
        graphYearly = (GraphView) findViewById(R.id.graph_yearly);


        graphYearly.getViewport().setScrollable(true);

        graphWeekly.getViewport().setMinX(1);
        graphWeekly.getViewport().setMaxX(7);

        graphYearly.getViewport().setMinX(1);
        graphYearly.getViewport().setMaxX(13);

        // use static labels for horizontal labels for yearly stat
        StaticLabelsFormatter labelsFormatter = new StaticLabelsFormatter(graphWeekly);
        labelsFormatter.setHorizontalLabels(new String[] {"SUN", "MON", "TUE", "WED","THU",
                "FRI","SAT"});
        graphWeekly.getGridLabelRenderer().setLabelFormatter(labelsFormatter);

        // use static labels for horizontal labels for yearly stat
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphYearly);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"JAN", "FEB", "MAR", "APR","MAY",
                "JUN","JUL","AUG","SEP","OCT","NOV","DEC"});
        graphYearly.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateWeeklyData());
        graphWeekly.addSeries(series);

        BarGraphSeries<DataPoint> seriesYearly = new BarGraphSeries<>(generateYearlyData());
        graphYearly.addSeries(seriesYearly);

        // styling
        seriesYearly.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        seriesYearly.setSpacing(60);

        // draw values on top
        seriesYearly.setDrawValuesOnTop(true);
        seriesYearly.setValuesOnTopColor(Color.RED);
        seriesYearly.setValuesOnTopSize(20);
    }

    private DataPoint[] generateWeeklyData() {
        int count = 7;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double y = Math.sin(i+2);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    private DataPoint[] generateYearlyData() {
        int count = 12;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double y = i+2;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
}