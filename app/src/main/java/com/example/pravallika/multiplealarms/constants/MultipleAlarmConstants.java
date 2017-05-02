package com.example.pravallika.multiplealarms.constants;

/**
 * Created by RitenVithlani on 4/29/17.
 */

public class MultipleAlarmConstants {

    public static final int NOTIF_VIBRATE_IN_MILLIS = 4000;
    public static final String DATE_TIME_FORMAT = "MMM dd, yyyy HH:mm";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATE_FORMAT = "MMM dd, yyyy";
    public static final String DAY_OF_WEEK_SEPERATOR = ", ";
    public static final String[] DAYS_OF_WEEK = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    public static final int SNOOZE_TIME_IN_MILLIS = 10 * 60 * 1000;
    public static final int RINGTONE_DURATION_IN_MILLIS = 60 * 1000;

    public enum FeatureType {
        ALARM(1, "Alarm"),
        MULTIPLE_ALARM(2, "Multiple Alarm"),
        SPECIAL_REMINDER(3, "Reminder!!!"),
        EVENT_REMINDER(4, "Event Reminder!!!");

        private final int id;
        private final String title;

        FeatureType(int id, String title) {
            this.id = id;
            this.title = title;
        }

        public int id() {
            return id;
        }

        public String title() {
            return title;
        }
    }
}
