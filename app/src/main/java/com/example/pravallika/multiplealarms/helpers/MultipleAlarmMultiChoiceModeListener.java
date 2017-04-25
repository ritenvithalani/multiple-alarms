package com.example.pravallika.multiplealarms.helpers;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.SpecialDaysReminder;
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
            SpecialDaysReminderDataSource dataSource = new SpecialDaysReminderDataSource(context);
            try {
                dataSource.openWritableDatabase();
                dataSource.deleteSplReminder(((SpecialDaysReminder) item).getId());
                dataSource.close();
            } catch (Exception e) {
                Toast.makeText(context, "Item could not be deleted", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        selectedItems.clear();
    }
}
