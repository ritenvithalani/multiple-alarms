package com.example.pravallika.multiplealarms.helpers;

import java.util.Calendar;

/**
 * Created by RitenVithlani on 4/24/17.
 */

public class Utility {

    public static String today() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String today = months[month] + " " + day + ", " + year;

        return today;
    }

    public static String now() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        String now = hour + ":" + minute;

        return now;
    }
}
