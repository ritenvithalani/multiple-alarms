package com.example.pravallika.multiplealarms.utils;

import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.MAX_VALUE;

/**
 * Created by RitenVithlani on 4/24/17.
 */

public class Utility {

    private static String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static String[] daysOfWeek = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    // Returns today in format MMM dd, yyyy
    public static String today() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String today = months[month] + " " + day + ", " + year;

        return today;
    }

    // Today is Thu (date 27 Apr, 2017). Input is "Sun". It will return date on Sunday in MMM dd, yyyy format
    public static String getDateFromDayOfWeek(String dayOfWeek, String selectedTime) {
        //Add +1 since indexOf is 0 index based and Calendar.DAY_OF_MONTH is 1 index based
        int dayIndex = Arrays.asList(daysOfWeek).indexOf(dayOfWeek) + 1;
        int dayIndexToday = Utility.getDayOfWeekIndex(today());

        // By default, assume selected dayOfWeek is today
        int distanceBetweenDays = 0;

        // Find out how far is dayIndex from today, then to get the date add that many days to today
        // selected day is ahead of today
        if (dayIndex > dayIndexToday) {
            distanceBetweenDays = dayIndex - dayIndexToday;
        }
        // today is ahead of selected day
        else if (dayIndex < dayIndexToday) {
            distanceBetweenDays = 7 - dayIndexToday + dayIndex;
        }
        // selected day is same as today
        else {
            String currentTime = Utility.now();
            // If selected time is greater than current time, then set alarm for today
            if (Utility.compareTime(selectedTime, currentTime)) {
                distanceBetweenDays = 0;
            }
            // If selected time is less than current time, then set alarm for next week on the same day
            else {
                distanceBetweenDays = 7;
            }
        }

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, distanceBetweenDays);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String date = months[month] + " " + day + ", " + year;

        return date;
    }

    // Returns next day in format MMM dd, yyyy
    public static String tomorrow() {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String tomorrow = months[month] + " " + day + ", " + year;

        return tomorrow;
    }

    // Input day in Apr 27, 2017 format, output will be "Thu"
    public static String getDayOfWeek(String day) {
        int dayOfWeekIndex = calculateDayOfWeek(day);
        return daysOfWeek[dayOfWeekIndex - 1];
    }

    // Input day in Apr 27, 2017 format, output will be 5
    public static int getDayOfWeekIndex(String day) {
        int dayOfWeekIndex = calculateDayOfWeek(day);
        return dayOfWeekIndex;
    }

    // Input day in Apr 27, 2017 format, output will be 5 since it is Thursday on this day. (1 for Sunday and 7 for Saturday)
    private static int calculateDayOfWeek(String day) {
        SimpleDateFormat format = new SimpleDateFormat(MultipleAlarmConstants.DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        Date givenDay;
        try {
            givenDay = format.parse(day);
            calendar.setTime(givenDay);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    // Returns the current time in format HH:mm
    public static String now() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        String now = hour + ":" + minute;

        return now;
    }

    // Converts the given date and time into milliseconds
    public static Long getDurationInMillis(String date, String time) {
        SimpleDateFormat format = new SimpleDateFormat(MultipleAlarmConstants.DATE_TIME_FORMAT);
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

    // If input time is 10:7, output will be 10:07
    public static String formatMinute(String selectedTime) {
        String formattedTime = selectedTime;
        String min = formattedTime.split(":")[1];

        if (min.length() == 1)
            min = "0" + min;

        formattedTime = formattedTime.split(":")[0] + ":" + min;
        return formattedTime;
    }

    // Will return true if time1Str > time2Str
    public static boolean compareTime(String time1Str, String time2Str) {
        boolean time1Greater = false;
        SimpleDateFormat parser = new SimpleDateFormat(MultipleAlarmConstants.TIME_FORMAT);

        try {
            Date time1 = parser.parse(time1Str);
            Date time2 = parser.parse(time2Str);
            if (time1.after(time2)) {
                time1Greater = true;
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }
        return time1Greater;
    }

    // Will return true if date1Str > date2Str
    public static boolean compareDate(String date1Str, String date2Str) {
        boolean date1Greater = false;
        SimpleDateFormat parser = new SimpleDateFormat(MultipleAlarmConstants.DATE_FORMAT);

        try {
            Date date1 = parser.parse(date1Str);
            Date date2 = parser.parse(date2Str);
            if (date1.after(date2)) {
                date1Greater = true;
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }
        return date1Greater;
    }

    // If today is Monday 10AM, selectedTime is 8AM, it will return NextAvailableDayOfWeek i.e. Tue
    public static String getNextAvailableDayOfWeek(String selectedTime) {
        String nextAvailableDay;
        String currentTime = Utility.now();
        if (Utility.compareTime(selectedTime, currentTime)) {
            nextAvailableDay = Utility.getDayOfWeek(today());
        } else {
            nextAvailableDay = Utility.getDayOfWeek(tomorrow());
        }

        return nextAvailableDay;
    }

    public static int getUniqueRequestCode(Long triggerTimeInMillis, MultipleAlarmConstants.FeatureType featureType) {
        return (int) ((triggerTimeInMillis % MAX_VALUE) + featureType.id());
    }

    // Return calendar instance of the given string date and time
    public static Calendar convertToCalendarDate(String dateStr) {
        SimpleDateFormat parser = new SimpleDateFormat(MultipleAlarmConstants.DATE_FORMAT);

        Calendar calendar = Calendar.getInstance();
        try {
            Date date = parser.parse(dateStr);
            calendar.setTime(date);
        } catch (ParseException e) {
            // Invalid date was entered
        }
        return calendar;
    }

    public static int convertTimeInMins(String timeStr) {
        String[] time = timeStr.split(":");
        int hours = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);
        return hours * 60 + min;
    }

    public static String selectDaysWithinDateRange(String selectedFromDate, String selectedToDate) {
        String selectedDays = "";
        Calendar fromDate = Utility.convertToCalendarDate(selectedFromDate);
        Calendar toDate = Utility.convertToCalendarDate(selectedToDate);
        int daysInWeekCount = 1;
        do {
            int fromDateDayOfWeek = fromDate.get(Calendar.DAY_OF_WEEK);

            selectedDays += MultipleAlarmConstants.DAYS_OF_WEEK[fromDateDayOfWeek - 1] + MultipleAlarmConstants.DAY_OF_WEEK_SEPERATOR;

            // Increment the day
            fromDate.add(Calendar.DAY_OF_YEAR, 1);
            daysInWeekCount++;
        } while (fromDate.getTimeInMillis() <= toDate.getTimeInMillis() && daysInWeekCount <= 7);

        selectedDays = selectedDays.substring(0, selectedDays.length() - 2);

        return selectedDays;
    }
}
