package com.example.pravallika.multiplealarms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pravallika.multiplealarms.beans.SpecialDaysReminder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RitenVithlani on 4/17/17.
 */
public class SpecialDaysReminderDataSource {

    private SQLiteDatabase database;
    private MultipleAlarmDBHelper dbHelper;

    public SpecialDaysReminderDataSource(Context context) {
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


    public List<SpecialDaysReminder> retrieveSpecialDaysReminders() {
        String query = "SELECT * FROM " + MultipleAlarmContract.SpecialDaysReminderContract.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);

        List<SpecialDaysReminder> specialDaysReminderList = new ArrayList<SpecialDaysReminder>();
        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(MultipleAlarmContract.SpecialDaysReminderContract._ID);
                int labelIndex = cursor.getColumnIndex(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_LABEL);
                int titleIndex = cursor.getColumnIndex(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_TITLE);
                int dateIndex = cursor.getColumnIndex(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_DATE);
                int timeIndex = cursor.getColumnIndex(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_TIME);
                int activeIndex = cursor.getColumnIndex(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_ACTIVE);

                Long id = cursor.getLong(idIndex);
                String label = cursor.getString(labelIndex);
                String title = cursor.getString(titleIndex);
                String date = cursor.getString(dateIndex);
                String time = cursor.getString(timeIndex);
                Integer iActive = cursor.getInt(activeIndex);

                Boolean active = Boolean.TRUE;
                if (iActive == 0) active = Boolean.FALSE;
                SpecialDaysReminder specialDaysReminder = new SpecialDaysReminder();
                specialDaysReminder.setId(id);
                specialDaysReminder.setLabel(label);
                specialDaysReminder.setTitle(title);
                specialDaysReminder.setDate(date);
                specialDaysReminder.setTime(time);
                specialDaysReminder.setActive(active);

                specialDaysReminderList.add(specialDaysReminder);
                /*Log.i("Name for row " + i, cursor.getString(cursor.getColumnIndex("contactname")));
                Log.i("Bff for row " + i, String.valueOf(cursor.getInt(cursor.getColumnIndex("bff"))));*/

            } while (cursor.moveToNext());
        }
        cursor.close();

        return specialDaysReminderList;
    }

    public Long insertSplDaysReminder(SpecialDaysReminder specialDaysReminder) {
        Long insertedRowId = -1l;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_LABEL, specialDaysReminder.getLabel());
            initialValues.put(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_TITLE, specialDaysReminder.getTitle());
            initialValues.put(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_DATE, specialDaysReminder.getDate());
            initialValues.put(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_TIME, specialDaysReminder.getTime());
            initialValues.put(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_ACTIVE, specialDaysReminder.getActive());

            insertedRowId = database.insert(MultipleAlarmContract.SpecialDaysReminderContract.TABLE_NAME, null, initialValues);
        } catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return insertedRowId;
    }

    public boolean updateSplDaysReminder(SpecialDaysReminder specialDaysReminder) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) specialDaysReminder.getId();
            ContentValues updateValues = new ContentValues();

            updateValues.put(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_LABEL, specialDaysReminder.getLabel());
            updateValues.put(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_TITLE, specialDaysReminder.getTitle());
            updateValues.put(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_DATE, specialDaysReminder.getDate());
            updateValues.put(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_TIME, specialDaysReminder.getTime());
            updateValues.put(MultipleAlarmContract.SpecialDaysReminderContract.COLUMN_NAME_ACTIVE, specialDaysReminder.getActive());

            didSucceed = database.update(MultipleAlarmContract.SpecialDaysReminderContract.TABLE_NAME, updateValues,
                    MultipleAlarmContract.SpecialDaysReminderContract._ID + "=" + rowId, null) > 0;
        } catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean deleteSplReminder(Long id) {
        boolean didDelete = false;
        try {
            didDelete = database.delete(MultipleAlarmContract.SpecialDaysReminderContract.TABLE_NAME,
                    MultipleAlarmContract.SpecialDaysReminderContract._ID + "=" + id, null) > 0;
        } catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

    public Long getLastSplReminderId() {
        Long lastId = -1l;
        try {
            String query = "Select MAX(" + MultipleAlarmContract.SpecialDaysReminderContract._ID + ") " +
                    "from " + MultipleAlarmContract.SpecialDaysReminderContract.TABLE_NAME;
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getLong(0);
            cursor.close();
        } catch (Exception e) {
            lastId = -1l;
        }
        return lastId;
    }
}
