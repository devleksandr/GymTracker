package com.carrickane.gymtracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by carrickane on 16.11.2016.
 */

public class Statistics extends AppCompatActivity {

    GraphView graphMonthly;
    GraphView graphYearly;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        //find and setup drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.
                OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);
                }
                else if (id == R.id.nav_statistics) {
                    Intent intent = new Intent(getBaseContext(),Statistics.class);
                    startActivity(intent);
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        graphMonthly = (GraphView) findViewById(R.id.graph_monthly);
        graphYearly = (GraphView) findViewById(R.id.graph_yearly);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

        });
        graphMonthly.addSeries(series);

        BarGraphSeries<DataPoint> seriesYearly = new BarGraphSeries<>(new DataPoint[]{
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
        graphYearly.addSeries(seriesYearly);

        // styling
        seriesYearly.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        seriesYearly.setSpacing(100);

// draw values on top
        seriesYearly.setDrawValuesOnTop(true);
        seriesYearly.setValuesOnTopColor(Color.RED);
//series.setValuesOnTopSize(50);
    }
}
