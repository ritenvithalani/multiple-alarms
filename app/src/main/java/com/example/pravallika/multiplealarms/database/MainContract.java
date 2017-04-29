package com.example.pravallika.multiplealarms.database;

import android.provider.BaseColumns;

/**
 * Created by RitenVithlani on 4/17/17.
 */

public class MainContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private MainContract() {
    }


    public static class EventsReminderContract implements BaseColumns {
        public static final String TABLE_NAME = "events_reminder";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_LABEL = "label";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_ACTIVE = "active";
    }

    public static class SpecialDaysReminderContract implements BaseColumns {
        public static final String TABLE_NAME = "spl_days_reminder";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_LABEL = "label";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_ACTIVE = "active";
    }

    public static class AlarmContract implements BaseColumns {
        public static final String TABLE_NAME = "alarms";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_LABEL = "label";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_DAYS = "days";
        public static final String COLUMN_NAME_ACTIVE = "active";
    }

    public static class MultiAlarmContract implements BaseColumns {
        public static final String TABLE_NAME = "multiple_alarms";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_LABEL = "label";
        public static final String COLUMN_NAME_FROM_TIME = "fromtime";
        public static final String COLUMN_NAME_TO_TIME = "totime";
        public static final String COLUMN_NAME_FROM_DATE = "fromdate";
        public static final String COLUMN_NAME_TO_DATE = "todate";
        public static final String COLUMN_NAME_REPEAT = "repeat";
        public static final String COLUMN_NAME_DAYS = "days";
        public static final String COLUMN_NAME_ACTIVE = "active";
    }
}
