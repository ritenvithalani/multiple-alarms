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
            "create table " + MultipleAlarmContract.SpecialDaysReminderContract.TABLE_NAME
                    + "(" + MultipleAlarmContract.SpecialDaysReminderContract._ID + " integer primary key autoincrement, "
                    + MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_LABEL + " text not null, "
                    + MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_TITLE + " text not null, "
                    + MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_DATE + " text not null, "
                    + MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_TIME + " text not null, "
                    + MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_ACTIVE + " boolean);";
    private static final String CREATE_TABLE_EVENTS_REMINDER =
            "create table " + MultipleAlarmContract.EventsReminderContract.TABLE_NAME
                    + "(" + MultipleAlarmContract.EventsReminderContract._ID + " integer primary key autoincrement, "
                    + MultipleAlarmContract.EventsReminderContract.COLUMN_NAME_LABEL + " text not null, "
                    + MultipleAlarmContract.EventsReminderContract.COLUMN_NAME_DATE + " text not null, "
                    + MultipleAlarmContract.EventsReminderContract.COLUMN_NAME_TIME + " text not null, "
                    + MultipleAlarmContract.EventsReminderContract.COLUMN_NAME_LOCATION + " text, "
                    + MultipleAlarmContract.EventsReminderContract.COLUMN_NAME_ACTIVE + " boolean);";
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
