

package com.carrickane.view;
import java.util.Date;

public interface CalendarListener {

    //initializing listeners
    void onDateSelected(Date date);
    void onMonthChanged(Date time);
}
