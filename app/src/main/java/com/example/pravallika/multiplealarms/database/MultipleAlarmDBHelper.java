package com.example.pravallika.multiplealarms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by RitenVithlani on 4/17/17.
 */
public class MultipleAlarmDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "multiplealarms.db";
    private static final int DATABASE_VERSION = 1;
    // Database creation sql statement
    private static final String CREATE_TABLE_SPL_DAYS_REMINDER =
            "create table " + MainContract.SpecialDaysReminderContract.TABLE_NAME
                    + "(" + MainContract.SpecialDaysReminderContract._ID + " integer primary key autoincrement, "
                    + MainContract.SpecialDaysReminderContract.COLUMN_NAME_LABEL + " text, "
                    + MainContract.SpecialDaysReminderContract.COLUMN_NAME_TITLE + " text not null, "
                    + MainContract.SpecialDaysReminderContract.COLUMN_NAME_DATE + " text not null, "
                    + MainContract.SpecialDaysReminderContract.COLUMN_NAME_TIME + " text not null, "
                    + MainContract.SpecialDaysReminderContract.COLUMN_NAME_ACTIVE + " integer);";
    private static final String CREATE_TABLE_EVENTS_REMINDER =
            "create table " + MainContract.EventsReminderContract.TABLE_NAME
                    + "(" + MainContract.EventsReminderContract._ID + " integer primary key autoincrement, "
                    + MainContract.EventsReminderContract.COLUMN_NAME_LABEL + " text, "
                    + MainContract.EventsReminderContract.COLUMN_NAME_DATE + " text not null, "
                    + MainContract.EventsReminderContract.COLUMN_NAME_TIME + " text not null, "
                    + MainContract.EventsReminderContract.COLUMN_NAME_LOCATION + " text, "
                    + MainContract.EventsReminderContract.COLUMN_NAME_ACTIVE + " integer);";
    private static final String CREATE_TABLE_ALARM =
            "create table " + MainContract.AlarmContract.TABLE_NAME
                    + "(" + MainContract.AlarmContract._ID + " integer primary key autoincrement, "
                    + MainContract.AlarmContract.COLUMN_NAME_LABEL + " text, "
                    + MainContract.AlarmContract.COLUMN_NAME_DAYS + " text not null, "
                    + MainContract.AlarmContract.COLUMN_NAME_TIME + " text not null, "
                    + MainContract.AlarmContract.COLUMN_NAME_ACTIVE + " integer);";
    private static final String CREATE_TABLE_MULTIPLE_ALARM =
            "create table " + MainContract.MultiAlarmContract.TABLE_NAME
                    + "(" + MainContract.MultiAlarmContract._ID + " integer primary key autoincrement, "
                    + MainContract.MultiAlarmContract.COLUMN_NAME_LABEL + " text, "
                    + MainContract.MultiAlarmContract.COLUMN_NAME_DAYS + " text, "
                    + MainContract.MultiAlarmContract.COLUMN_NAME_FROM_TIME + " text not null, "
                    + MainContract.MultiAlarmContract.COLUMN_NAME_TO_TIME + " text not null, "
                    + MainContract.MultiAlarmContract.COLUMN_NAME_FROM_DATE + " text, "
                    + MainContract.MultiAlarmContract.COLUMN_NAME_TO_DATE + " text, "
                    + MainContract.MultiAlarmContract.COLUMN_NAME_REPEAT + " text not null, "
                    + MainContract.MultiAlarmContract.COLUMN_NAME_ACTIVE + " integer);";

    private static MultipleAlarmDBHelper multipleAlarmDBHelper;

    private MultipleAlarmDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MultipleAlarmDBHelper getInstance(Context context) {
        if (null == multipleAlarmDBHelper) {
            multipleAlarmDBHelper = new MultipleAlarmDBHelper(context);
        }
        return multipleAlarmDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_SPL_DAYS_REMINDER);
        database.execSQL(CREATE_TABLE_EVENTS_REMINDER);
        database.execSQL(CREATE_TABLE_ALARM);
        database.execSQL(CREATE_TABLE_MULTIPLE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MultipleAlarmDBHelper.class.getName(),
                "Upgrading com.example.pravallika.multiplealarms.database from version " + oldVersion + " to " + newVersion);

        // Use alter table instead of drop table to add new column in the contact table to retain the existing data
        if (newVersion > oldVersion) {
            //db.execSQL("ALTER TABLE contact ADD COLUMN bff integer");
        }
    }

}
