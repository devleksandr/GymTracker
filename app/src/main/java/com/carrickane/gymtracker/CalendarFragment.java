package com.carrickane.gymtracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carrickane.gymtracker.database.ExerciseData;
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

import static com.carrickane.gymtracker.Constants.DATE_SHORT;
import static com.carrickane.gymtracker.Constants.FORMAT_DATE_FULL;
import static com.carrickane.gymtracker.Constants.FORMAT_DATE_SHORT;
import static com.carrickane.gymtracker.Constants.SELECTED_DATE;

/**
 * Created by carrickane on 16.11.2016.
 */

public class CalendarFragment extends Fragment {

    static CustomCalendarView calendarView;
    String queryDateShort = null;
    static List<ExerciseData> exerciseData;

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
        final SimpleDateFormat formatShort = new SimpleDateFormat(FORMAT_DATE_SHORT);
        queryDateShort = formatShort.format(currentCalendar.getTime());
        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);
        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //set listener for passing through Scheduler.class with selected date
        calendarView.setCalendarListener(new CalendarListener() {

            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATE_FULL);
                String selectedDate = df.format(date);
                Scheduler scheduler = new Scheduler();
                Bundle bundle = new Bundle();
                bundle.putString(SELECTED_DATE, selectedDate);
                bundle.putString(DATE_SHORT,queryDateShort);
                scheduler.setArguments(bundle);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameMain,scheduler).addToBackStack("Schedule").commit();
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
        return v;
    }

    public static void buildQueryForEvent (String queryDateShort) {

        //building query for monthly exercises
        String completeArgs = new StringBuilder().append("SELECT * FROM EXERCISE_DATA " +
                "WHERE DATE_INSERT LIKE").append("'%").append(queryDateShort).
                append("%'").toString();
        exerciseData = ExerciseData.findWithQuery(ExerciseData.class,completeArgs);

        //if month contains at least one exercise - check all days
        if (exerciseData.size() !=0) {
            int maxDaysInMonth =31;
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
            }
        }
    }

    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            //set grey background for past days in month
            if (CalendarUtils.isPastDay(dayView.getDate())) {
                int color = getResources().getColor(R.color.disabled_date);
                dayView.setBackgroundColor(color);
            }
        }
    }
}
