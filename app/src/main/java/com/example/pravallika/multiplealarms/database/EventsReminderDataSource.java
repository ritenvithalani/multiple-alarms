package com.example.pravallika.multiplealarms.database;

import android.content.ContentValues;
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

    public void openWritableDatabase() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void openReadableDatabase() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<EventsReminder> retrieveEventReminders() {
        String query = "SELECT * FROM " + MainContract.EventsReminderContract.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);

        List<EventsReminder> eventReminderList = new ArrayList<EventsReminder>();
        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(MainContract.EventsReminderContract._ID);
                int labelIndex = cursor.getColumnIndex(MainContract.EventsReminderContract.COLUMN_NAME_LABEL);
                int dateIndex = cursor.getColumnIndex(MainContract.EventsReminderContract.COLUMN_NAME_DATE);
                int timeIndex = cursor.getColumnIndex(MainContract.EventsReminderContract.COLUMN_NAME_TIME);
                int locationIndex = cursor.getColumnIndex(MainContract.EventsReminderContract.COLUMN_NAME_LOCATION);
                int activeIndex = cursor.getColumnIndex(MainContract.EventsReminderContract.COLUMN_NAME_ACTIVE);

                Long id = cursor.getLong(idIndex);
                String label = cursor.getString(labelIndex);
                String date = cursor.getString(dateIndex);
                String time = cursor.getString(timeIndex);
                String location = cursor.getString(locationIndex);
                Integer iActive = cursor.getInt(activeIndex);

                Boolean active = Boolean.TRUE;
                if (iActive == 0) active = Boolean.FALSE;

                EventsReminder eventsReminder = new EventsReminder();
                eventsReminder.setId(id);
                eventsReminder.setLabel(label);
                eventsReminder.setLocation(location);
                eventsReminder.setDate(date);
                eventsReminder.setTime(time);
                eventsReminder.setActive(active);

                eventReminderList.add(eventsReminder);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return eventReminderList;
    }

    public Long insertEventsReminder(EventsReminder eventsReminder) {
        Long insertedRowId = -1l;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put(MainContract.EventsReminderContract.COLUMN_NAME_LABEL, eventsReminder.getLabel());
            initialValues.put(MainContract.EventsReminderContract.COLUMN_NAME_LOCATION, eventsReminder.getLocation());
            initialValues.put(MainContract.EventsReminderContract.COLUMN_NAME_DATE, eventsReminder.getDate());
            initialValues.put(MainContract.EventsReminderContract.COLUMN_NAME_TIME, eventsReminder.getTime());
            initialValues.put(MainContract.EventsReminderContract.COLUMN_NAME_ACTIVE, eventsReminder.getActive());

            insertedRowId = database.insert(MainContract.EventsReminderContract.TABLE_NAME, null, initialValues);
        } catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return insertedRowId;
    }

    public boolean updateEventsReminder(EventsReminder eventsReminder) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) eventsReminder.getId();
            ContentValues updateValues = new ContentValues();

            updateValues.put(MainContract.EventsReminderContract.COLUMN_NAME_LABEL, eventsReminder.getLabel());
            updateValues.put(MainContract.EventsReminderContract.COLUMN_NAME_LOCATION, eventsReminder.getLocation());
            updateValues.put(MainContract.EventsReminderContract.COLUMN_NAME_DATE, eventsReminder.getDate());
            updateValues.put(MainContract.EventsReminderContract.COLUMN_NAME_TIME, eventsReminder.getTime());
            updateValues.put(MainContract.EventsReminderContract.COLUMN_NAME_ACTIVE, eventsReminder.getActive());

            didSucceed = database.update(MainContract.EventsReminderContract.TABLE_NAME, updateValues,
                    MainContract.EventsReminderContract._ID + "=" + rowId, null) > 0;
        } catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean deleteEventsReminder(Long id) {
        boolean didDelete = false;
        try {
            didDelete = database.delete(MainContract.EventsReminderContract.TABLE_NAME,
                    MainContract.EventsReminderContract._ID + "=" + id, null) > 0;
        } catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }
}
