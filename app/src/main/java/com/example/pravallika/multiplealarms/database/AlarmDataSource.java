package com.example.pravallika.multiplealarms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pravallika.multiplealarms.beans.Alarm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RitenVithlani on 4/17/17.
 */
public class AlarmDataSource {

    private SQLiteDatabase database;
    private MultipleAlarmDBHelper dbHelper;

    public AlarmDataSource(Context context) {
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


    public List<Alarm> retrieveAlarms() {
        String query = "SELECT * FROM " + MainContract.AlarmContract.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);

        List<Alarm> alarmList = new ArrayList<Alarm>();
        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(MainContract.AlarmContract._ID);
                int labelIndex = cursor.getColumnIndex(MainContract.AlarmContract.COLUMN_NAME_LABEL);
                int daysIndex = cursor.getColumnIndex(MainContract.AlarmContract.COLUMN_NAME_DAYS);
                int timeIndex = cursor.getColumnIndex(MainContract.AlarmContract.COLUMN_NAME_TIME);
                int activeIndex = cursor.getColumnIndex(MainContract.AlarmContract.COLUMN_NAME_ACTIVE);

                Long id = cursor.getLong(idIndex);
                String label = cursor.getString(labelIndex);
                String days = cursor.getString(daysIndex);
                String time = cursor.getString(timeIndex);
                Integer iActive = cursor.getInt(activeIndex);

                Boolean active = Boolean.TRUE;
                if (iActive == 0) active = Boolean.FALSE;
                Alarm alarm = new Alarm();
                alarm.setId(id);
                alarm.setLabel(label);
                alarm.setSelectedDays(days);
                alarm.setTime(time);
                alarm.setActive(active);

                alarmList.add(alarm);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return alarmList;
    }

    public boolean updateAlarm(Alarm alarm) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) alarm.getId();
            ContentValues updateValues = new ContentValues();

            updateValues.put(MainContract.AlarmContract.COLUMN_NAME_LABEL, alarm.getLabel());
            updateValues.put(MainContract.AlarmContract.COLUMN_NAME_DAYS, alarm.getSelectedDays());
            updateValues.put(MainContract.AlarmContract.COLUMN_NAME_TIME, alarm.getTime());
            updateValues.put(MainContract.AlarmContract.COLUMN_NAME_ACTIVE, alarm.getActive());

            didSucceed = database.update(MainContract.AlarmContract.TABLE_NAME, updateValues,
                    MainContract.AlarmContract._ID + "=" + rowId, null) > 0;
        } catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean deleteAlarm(Long id) {
        boolean didDelete = false;
        try {
            didDelete = database.delete(MainContract.AlarmContract.TABLE_NAME,
                    MainContract.AlarmContract._ID + "=" + id, null) > 0;
        } catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }


    public Long insertAlarm(Alarm currentAlarm) {
        Long insertedRowId = -1l;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put(MainContract.AlarmContract.COLUMN_NAME_LABEL, currentAlarm.getLabel());
            initialValues.put(MainContract.AlarmContract.COLUMN_NAME_DAYS, currentAlarm.getSelectedDays());
            initialValues.put(MainContract.AlarmContract.COLUMN_NAME_TIME, currentAlarm.getTime());
            initialValues.put(MainContract.AlarmContract.COLUMN_NAME_ACTIVE, currentAlarm.getActive());

            insertedRowId = database.insert(MainContract.AlarmContract.TABLE_NAME, null, initialValues);
        } catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return insertedRowId;
    }
}
