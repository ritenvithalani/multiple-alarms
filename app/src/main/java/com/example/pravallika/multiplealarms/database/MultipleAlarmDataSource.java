package com.example.pravallika.multiplealarms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pravallika.multiplealarms.beans.MultipleAlarm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RitenVithlani on 4/17/17.
 */
public class MultipleAlarmDataSource {

    private SQLiteDatabase database;
    private MultipleAlarmDBHelper dbHelper;

    public MultipleAlarmDataSource(Context context) {
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


    public List<MultipleAlarm> retrieveMultipleAlarms() {
        String query = "SELECT * FROM " + MainContract.MultiAlarmContract.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);

        List<MultipleAlarm> multipleAlarmList = new ArrayList<MultipleAlarm>();
        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(MainContract.MultiAlarmContract._ID);
                int labelIndex = cursor.getColumnIndex(MainContract.MultiAlarmContract.COLUMN_NAME_LABEL);
                int daysIndex = cursor.getColumnIndex(MainContract.MultiAlarmContract.COLUMN_NAME_DAYS);
                int fromTimeIndex = cursor.getColumnIndex(MainContract.MultiAlarmContract.COLUMN_NAME_FROM_TIME);
                int toTimeIndex = cursor.getColumnIndex(MainContract.MultiAlarmContract.COLUMN_NAME_TO_TIME);
                int fromDateIndex = cursor.getColumnIndex(MainContract.MultiAlarmContract.COLUMN_NAME_FROM_DATE);
                int toDateIndex = cursor.getColumnIndex(MainContract.MultiAlarmContract.COLUMN_NAME_TO_DATE);
                int repeatIndex = cursor.getColumnIndex(MainContract.MultiAlarmContract.COLUMN_NAME_REPEAT);
                int activeIndex = cursor.getColumnIndex(MainContract.MultiAlarmContract.COLUMN_NAME_ACTIVE);

                Long id = cursor.getLong(idIndex);
                String label = cursor.getString(labelIndex);
                String days = cursor.getString(daysIndex);
                String fromTime = cursor.getString(fromTimeIndex);
                String toTime = cursor.getString(toTimeIndex);
                String fromDate = cursor.getString(fromDateIndex);
                String toDate = cursor.getString(toDateIndex);
                String repeat = cursor.getString(repeatIndex);
                Integer iActive = cursor.getInt(activeIndex);

                Boolean active = Boolean.TRUE;
                if (iActive == 0) active = Boolean.FALSE;
                MultipleAlarm multipleAlarm = new MultipleAlarm();
                multipleAlarm.setId(id);
                multipleAlarm.setLabel(label);
                multipleAlarm.setSelectedDays(days);
                multipleAlarm.setFromTime(fromTime);
                multipleAlarm.setToTime(toTime);
                multipleAlarm.setFromDate(fromDate);
                multipleAlarm.setToDate(toDate);
                multipleAlarm.setRepeat(repeat);
                multipleAlarm.setActive(active);

                multipleAlarmList.add(multipleAlarm);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return multipleAlarmList;
    }

    public boolean updateMultipleAlarm(MultipleAlarm multipleAlarm) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) multipleAlarm.getId();
            ContentValues updateValues = new ContentValues();

            updateValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_LABEL, multipleAlarm.getLabel());
            updateValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_DAYS, multipleAlarm.getSelectedDays());
            updateValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_FROM_TIME, multipleAlarm.getFromTime());
            updateValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_TO_TIME, multipleAlarm.getToTime());
            updateValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_FROM_DATE, multipleAlarm.getFromDate());
            updateValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_TO_DATE, multipleAlarm.getToDate());
            updateValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_REPEAT, multipleAlarm.getRepeat());
            updateValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_ACTIVE, multipleAlarm.getActive());

            didSucceed = database.update(MainContract.MultiAlarmContract.TABLE_NAME, updateValues,
                    MainContract.MultiAlarmContract._ID + "=" + rowId, null) > 0;
        } catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean deleteMultipleAlarm(Long id) {
        boolean didDelete = false;
        try {
            didDelete = database.delete(MainContract.MultiAlarmContract.TABLE_NAME,
                    MainContract.MultiAlarmContract._ID + "=" + id, null) > 0;
        } catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }


    public Long insertMultipleAlarm(MultipleAlarm currentMultipleAlarm) {
        Long insertedRowId = -1l;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_LABEL, currentMultipleAlarm.getLabel());
            initialValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_DAYS, currentMultipleAlarm.getSelectedDays());
            initialValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_FROM_TIME, currentMultipleAlarm.getFromTime());
            initialValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_TO_TIME, currentMultipleAlarm.getToTime());
            initialValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_FROM_DATE, currentMultipleAlarm.getFromDate());
            initialValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_TO_DATE, currentMultipleAlarm.getToDate());
            initialValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_REPEAT, currentMultipleAlarm.getRepeat());
            initialValues.put(MainContract.MultiAlarmContract.COLUMN_NAME_ACTIVE, currentMultipleAlarm.getActive());

            insertedRowId = database.insert(MainContract.MultiAlarmContract.TABLE_NAME, null, initialValues);
        } catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return insertedRowId;
    }
}
