package com.example.pravallika.multiplealarms.listeners;

import android.content.Context;
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
import com.example.pravallika.multiplealarms.database.AlarmDataSource;
import com.example.pravallika.multiplealarms.database.EventsReminderDataSource;
import com.example.pravallika.multiplealarms.database.MultipleAlarmDataSource;
import com.example.pravallika.multiplealarms.database.SpecialDaysReminderDataSource;

import java.util.ArrayList;
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
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        selectedItems.clear();
    }
}