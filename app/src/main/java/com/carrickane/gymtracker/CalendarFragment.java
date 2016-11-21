package com.carrickane.gymtracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrickane.view.CalendarListener;
import com.carrickane.view.CustomCalendarView;
import com.carrickane.view.DayDecorator;
import com.carrickane.view.DayView;
import com.carrickane.view.utils.CalendarUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by carrickane on 16.11.2016.
 */

public class CalendarFragment extends Fragment {

    static CustomCalendarView calendarView;
    String queryDateShort = null;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.calendar, container, false);

        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) v.findViewById(R.id.calendar_view);
        //Initialize calendar with date
        final Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        //Date format for queries
        final SimpleDateFormat formatShort = new SimpleDateFormat("MM-yyyy");
        queryDateShort = formatShort.format(currentCalendar.getTime());
        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);
        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);
        //set listener for passing through EventList.class with selected date

        calendarView.setCalendarListener(new CalendarListener() {

            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String selectedDate = df.format(date);
                Intent intent = new Intent(getContext(), Scheduler.class);
                intent.putExtra("selectedDate", selectedDate);
                intent.putExtra("dateShort",queryDateShort);
                startActivity(intent);
            }

            //getting month changed and building query for events
            @Override
            public void onMonthChanged(Date date) {
                queryDateShort = formatShort.format(date.getTime());
                buildQueryForEvent(queryDateShort);
            }
        });

        //adding calendar day decorators
        List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(new DisabledColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);
        buildQueryForEvent(queryDateShort);
        //building query for checking existing events for a month
        setRetainInstance(true);
        return v;
    }

    public static void buildQueryForEvent (String queryDateShort) {
        List<ExerciseData> exerciseData;
        int maxDaysInMonth = 31;
        for (int i = 1; i <= maxDaysInMonth; i++) {
            String fullDateQuery = new StringBuilder().append(i).append("-").
                    append(queryDateShort).toString();
            exerciseData = ExerciseData.findWithQuery(ExerciseData.class,
                    "SELECT * FROM EXERCISE_DATA WHERE DATE_INSERT = ?", fullDateQuery);

            //if list not empty
            if (exerciseData.size() != 0) {
                Date setDate = null;
                SimpleDateFormat formatFull = new SimpleDateFormat("dd-MM-yyyy");

                //parse selected date
                try {
                    setDate = formatFull.parse(fullDateQuery);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar calendarEvent = Calendar.getInstance();
                calendarEvent.setTime(setDate);
                //calling method SetImageIfEventExist from CustomCalendarView.class
                calendarView.SetImageIfEventExist(calendarEvent);
            }
            //otherwise do nothing
            else {
            }
        }
    }

    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            //set grey background for past days in month
            if (CalendarUtils.isPastDay(dayView.getDate())) {
                int color = Color.parseColor("#a9afb9");
                dayView.setBackgroundColor(color);
            }
        }
    }
}
