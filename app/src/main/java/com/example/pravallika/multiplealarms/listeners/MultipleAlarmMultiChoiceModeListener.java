package com.example.pravallika.multiplealarms.listeners;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.Alarm;
import com.example.pravallika.multiplealarms.beans.EventsReminder;
import com.example.pravallika.multiplealarms.beans.MultipleAlarm;
import com.example.pravallika.multiplealarms.beans.SpecialDaysReminder;
import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.database.AlarmDataSource;
import com.example.pravallika.multiplealarms.database.EventsReminderDataSource;
import com.example.pravallika.multiplealarms.database.MultipleAlarmDataSource;
import com.example.pravallika.multiplealarms.database.SpecialDaysReminderDataSource;
import com.example.pravallika.multiplealarms.helpers.AlarmHelper;
import com.example.pravallika.multiplealarms.helpers.NotificationHelper;
import com.example.pravallika.multiplealarms.utils.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by RitenVithlani on 4/17/17.
 * <p>
 * Generic class to work with menu action items
 */

public class MultipleAlarmMultiChoiceModeListener<ITEM_TYPE> implements AbsListView.MultiChoiceModeListener {

    List<ITEM_TYPE> selectedItems = new ArrayList<ITEM_TYPE>();
    List<ITEM_TYPE> items;
    ArrayAdapter<ITEM_TYPE> adapter;
    Context context;

    public MultipleAlarmMultiChoiceModeListener(Context context, List<ITEM_TYPE> items, ArrayAdapter<ITEM_TYPE> adapter) {
        this.context = context;
        this.items = items;
        this.adapter = adapter;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked) {
            selectedItems.add(items.get(position));
        } else {
            selectedItems.remove(items.get(position));
        }

        mode.setTitle(selectedItems.size() + " item(s) selected");
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.delete:
                for (ITEM_TYPE item : selectedItems) {
                    adapter.remove(item);
                    removeItem(item);
                }
                Toast.makeText(context, selectedItems.size() + " item(s) removed", Toast.LENGTH_LONG).show();
                selectedItems.clear();
                mode.finish();
                return true;

            default:
                return false;
        }
    }

    private void removeItem(ITEM_TYPE item) {
        if (item instanceof SpecialDaysReminder) {
            deleteSpecialDaysReminder((SpecialDaysReminder) item);
        } else if (item instanceof EventsReminder) {
            deleteEventsReminder((EventsReminder) item);
        } else if (item instanceof Alarm) {
            deleteAlarm((Alarm) item);
        } else if (item instanceof MultipleAlarm) {
            deleteMultipleAlarm((MultipleAlarm) item);
        }

    }

    private void deleteMultipleAlarm(MultipleAlarm item) {
        MultipleAlarmDataSource dataSource = new MultipleAlarmDataSource(context);
        try {
            dataSource.openWritableDatabase();
            dataSource.deleteMultipleAlarm(item.getId());
            dataSource.close();
        } catch (Exception e) {
            Toast.makeText(context, "Item could not be deleted", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        cancelMultipleAlarm(item);
    }

    private void cancelMultipleAlarm(MultipleAlarm multipleAlarm) {
        Calendar fromDate = Utility.convertToCalendarDate(multipleAlarm.getFromDate());
        Calendar toDate = Utility.convertToCalendarDate(multipleAlarm.getToDate());
        boolean isValid = false;
        if (TextUtils.join(MultipleAlarmConstants.DAY_OF_WEEK_SEPERATOR, MultipleAlarmConstants.DAYS_OF_WEEK).equals(multipleAlarm.getSelectedDays())) {
            isValid = true;
        }

        do {
            int fromDateDayOfWeek = fromDate.get(Calendar.DAY_OF_WEEK);
            // isValid is true when user does not select any days of week. If no days of week is selected then create alarm for everyday within the date range
            // If user has selected the days of week then alarm should be triggered for that particular day in the given date range
            if (isValid || multipleAlarm.getSelectedDays().contains(MultipleAlarmConstants.DAYS_OF_WEEK[fromDateDayOfWeek - 1])) {

                // Add repeat interval to the from date such that it between start and end time
                int fromTimeInMins = Utility.convertTimeInMins(multipleAlarm.getFromTime());
                int toTimeInMins = Utility.convertTimeInMins(multipleAlarm.getToTime());
                int repeatInterval = Integer.parseInt(multipleAlarm.getRepeat());

                fromDate.add(Calendar.MINUTE, fromTimeInMins);

                for (int time = fromTimeInMins; time < toTimeInMins; time = time + repeatInterval) {
                    fromDate.add(Calendar.MINUTE, repeatInterval);
                    if (time >= Utility.convertTimeInMins(Utility.now())) {
                        Date d1 = fromDate.getTime();
                        Log.i("from date values", d1.toString());
                        AlarmHelper.setAlarm(context, fromDate.getTimeInMillis(), multipleAlarm.getLabel(), MultipleAlarmConstants.FeatureType.MULTIPLE_ALARM);
                        int requestCode = Utility.getUniqueRequestCode(fromDate.getTimeInMillis(), MultipleAlarmConstants.FeatureType.MULTIPLE_ALARM);
                        AlarmHelper.cancelAlarm(context, requestCode);
                    }
                }
            }
            // Increment the day
            fromDate.add(Calendar.DAY_OF_YEAR, 1);
        } while (fromDate.getTimeInMillis() <= toDate.getTimeInMillis());

    }

    private void deleteAlarm(Alarm item) {
        AlarmDataSource dataSource = new AlarmDataSource(context);
        try {
            dataSource.openWritableDatabase();
            dataSource.deleteAlarm(item.getId());
            dataSource.close();
        } catch (Exception e) {
            Toast.makeText(context, "Item could not be deleted", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        for (String dayOfWeek : item.getSelectedDays().split(MultipleAlarmConstants.DAY_OF_WEEK_SEPERATOR)) {
            String alarmDate = Utility.getDateFromDayOfWeek(dayOfWeek, item.getTime());
            Long triggerAtMillis = Utility.getDurationInMillis(alarmDate, item.getTime());
            int requestCode = Utility.getUniqueRequestCode(triggerAtMillis, MultipleAlarmConstants.FeatureType.ALARM);
            AlarmHelper.cancelAlarm(context, requestCode);
        }
    }

    private void deleteEventsReminder(EventsReminder item) {
        EventsReminderDataSource dataSource = new EventsReminderDataSource(context);
        try {
            dataSource.openWritableDatabase();
            dataSource.deleteEventsReminder(item.getId());
            dataSource.close();
        } catch (Exception e) {
            Toast.makeText(context, "Item could not be deleted", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        Long triggerAtMillis = Utility.getDurationInMillis(item.getDate(), item.getTime());
        NotificationHelper.cancelNotification(context, triggerAtMillis, MultipleAlarmConstants.FeatureType.EVENT_REMINDER);
    }

    private void deleteSpecialDaysReminder(SpecialDaysReminder item) {
        SpecialDaysReminderDataSource dataSource = new SpecialDaysReminderDataSource(context);
        try {
            dataSource.openWritableDatabase();
            dataSource.deleteSplReminder(item.getId());
            dataSource.close();
        } catch (Exception e) {
            Toast.makeText(context, "Item could not be deleted", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        Long triggerAtMillis = Utility.getDurationInMillis(item.getDate(), item.getTime());
        NotificationHelper.cancelNotification(context, triggerAtMillis, MultipleAlarmConstants.FeatureType.SPECIAL_REMINDER);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        selectedItems.clear();
    }
}
