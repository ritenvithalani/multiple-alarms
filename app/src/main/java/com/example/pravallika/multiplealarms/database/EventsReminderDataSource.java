package com.example.pravallika.multiplealarms.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pravallika.multiplealarms.beans.EventsReminder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RitenVithlani on 4/17/17.
 */
public class EventsReminderDataSource {

    private SQLiteDatabase database;
    private MultipleAlarmDBHelper dbHelper;

    public EventsReminderDataSource(Context context) {
        dbHelper = MultipleAlarmDBHelper.getInstance(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<EventsReminder> retrieveEventReminders() {
        String query = "SELECT * FROM " + MultipleAlarmContract.EventsReminderContract.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);

        List<EventsReminder> eventReminderList = new ArrayList<EventsReminder>();
        if (cursor.moveToFirst()) {
            for (int i = 1; cursor.moveToNext(); i++) {
                int idIndex = cursor.getColumnIndex(MultipleAlarmContract.EventsReminderContract._ID);
                int labelIndex = cursor.getColumnIndex(MultipleAlarmContract.EventsReminderContract.COLUMN_NAME_LABEL);
                int dateIndex = cursor.getColumnIndex(MultipleAlarmContract.EventsReminderContract.COLUMN_NAME_DATE);
                int timeIndex = cursor.getColumnIndex(MultipleAlarmContract.EventsReminderContract.COLUMN_NAME_TIME);
                int locationIndex = cursor.getColumnIndex(MultipleAlarmContract.EventsReminderContract.COLUMN_NAME_LOCATION);
                int activeIndex = cursor.getColumnIndex(MultipleAlarmContract.EventsReminderContract.COLUMN_NAME_ACTIVE);

                Integer id = cursor.getInt(idIndex);
                String label = cursor.getString(labelIndex);
                String date = cursor.getString(dateIndex);
                String time = cursor.getString(timeIndex);
                String location = cursor.getString(locationIndex);
                Integer iActive = cursor.getInt(activeIndex);

                Boolean active = Boolean.TRUE;
                if (iActive == 0) active = Boolean.FALSE;
                EventsReminder eventsReminder = new EventsReminder(id, label, date, time, location, active);

                eventReminderList.add(eventsReminder);
                /*Log.i("Name for row " + i, cursor.getString(cursor.getColumnIndex("contactname")));
                Log.i("Bff for row " + i, String.valueOf(cursor.getInt(cursor.getColumnIndex("bff"))));*/

            }
        }
        cursor.close();

        return eventReminderList;
    }
}
