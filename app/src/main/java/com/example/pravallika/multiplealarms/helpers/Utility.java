package com.example.pravallika.multiplealarms.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by RitenVithlani on 4/24/17.
 */

public class Utility {

    private static String DATE_FORMAT = "MMM dd, yyyy HH:mm";


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

    public static Long getDurationInMillis(String date, String time) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date selectedTimestamp;
        Long duration = new Long(0l);
        try {
            selectedTimestamp = format.parse(date + " " + time);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedTimestamp);

            duration = calendar.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return duration;
    }

    public static String formatMinute(String selectedTime) {
        String formattedTime = selectedTime;
        String min = formattedTime.split(":")[1];

        if (min.length() == 1)
            min = "0" + min;

        formattedTime = formattedTime.split(":")[0] + ":" + min;
        return formattedTime;
    }
}
