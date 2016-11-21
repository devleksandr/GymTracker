package com.carrickane.gymtracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by carrickane on 16.11.2016.
 */

public class StatisticsActivity extends AppCompatActivity {

    GraphView graphMonthly;
    GraphView graphYearly;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);
        graphMonthly = (GraphView) findViewById(R.id.graph_monthly);
        graphYearly = (GraphView) findViewById(R.id.graph_yearly);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

        });
        graphMonthly.addSeries(series);

        BarGraphSeries<DataPoint> seriesblabla = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 7),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 7),
                new DataPoint(6, 5),
                new DataPoint(7, 3),
                new DataPoint(8, 2),
                new DataPoint(9, 6),
                new DataPoint(10, 7),
                new DataPoint(11, 5),
                new DataPoint(12, 3),
                new DataPoint(13, 2),
                new DataPoint(14, 6),
                new DataPoint(15, 7),
                new DataPoint(16, 5),
                new DataPoint(17, 3),
                new DataPoint(18, 2),
                new DataPoint(19, 6)
        });
        graphYearly.addSeries(seriesblabla);

        // styling
        seriesblabla.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        seriesblabla.setSpacing(100);

// draw values on top
        seriesblabla.setDrawValuesOnTop(true);
        seriesblabla.setValuesOnTopColor(Color.RED);
//series.setValuesOnTopSize(50);
    }
}
