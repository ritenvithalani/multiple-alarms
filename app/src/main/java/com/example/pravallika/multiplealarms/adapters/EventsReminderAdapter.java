package com.example.pravallika.multiplealarms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.EventsReminder;
import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.database.EventsReminderDataSource;
import com.example.pravallika.multiplealarms.helpers.NotificationHelper;
import com.example.pravallika.multiplealarms.utils.Utility;

/**
 * Created by RitenVithlani on 2/20/17.
 */

public class EventsReminderAdapter extends ArrayAdapter<EventsReminder> {

    Context context;

    public EventsReminderAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(null == convertView) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_events_reminder, parent, false);
        }

        final EventsReminder eventsReminder = getItem(position);

        TextView id = (TextView) listItemView.findViewById(R.id.tv_list_events_rem_id);
        TextView date = (TextView) listItemView.findViewById(R.id.tv_events_rem_date);
        TextView time = (TextView) listItemView.findViewById(R.id.tv_events_rem_time);
        TextView label = (TextView) listItemView.findViewById(R.id.tv_events_rem_label);
        TextView location = (TextView) listItemView.findViewById(R.id.tv_events_rem_location);
        Switch eventsRemToggleSwitch = (Switch) listItemView.findViewById(R.id.ts_events_reminder);

        id.setText(eventsReminder.getId() + "");

        if (null != eventsReminder.getDate() && null != date && !"".equals(eventsReminder.getDate())) {
            date.setText(eventsReminder.getDate());
        }
        else {
            date.setVisibility(View.GONE);
        }

        if (null != eventsReminder.getTime() && null != time && !"".equals(eventsReminder.getTime())) {
            time.setText(eventsReminder.getTime());
        }
        else {
            time.setVisibility(View.GONE);
        }

        if (null != eventsReminder.getLabel() && null != label && !"".equals(eventsReminder.getLabel())) {
            label.setText(eventsReminder.getLabel());
        }
        else {
            label.setVisibility(View.GONE);
        }

        if (null != eventsReminder.getLocation() && null != location && !"".equals(eventsReminder.getLocation())) {
            location.setText(eventsReminder.getLocation());
        }
        else {
            location.setVisibility(View.GONE);
        }

        eventsRemToggleSwitch.setChecked(null != eventsReminder.getActive() ? eventsReminder.getActive() : Boolean.TRUE);

        eventsRemToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Long triggerAtMillis = Utility.getDurationInMillis(eventsReminder.getDate(), eventsReminder.getTime());
                    NotificationHelper.createNotification(context, triggerAtMillis, eventsReminder.getLabel(), MultipleAlarmConstants.FeatureType.EVENT_REMINDER);
                } else {
                    Long triggerAtMillis = Utility.getDurationInMillis(eventsReminder.getDate(), eventsReminder.getTime());
                    NotificationHelper.cancelNotification(context, triggerAtMillis, MultipleAlarmConstants.FeatureType.EVENT_REMINDER);
                }

                eventsReminder.setActive(isChecked);
                saveEventsRemToDB(eventsReminder);
            }
        });

        return listItemView;
    }

    private void saveEventsRemToDB(EventsReminder currentEventsReminder) {
        boolean wasSuccessful = false;
        EventsReminderDataSource dataSource = new EventsReminderDataSource(context);
        try {
            dataSource.openWritableDatabase();

            if (currentEventsReminder.getId() == -1) {
                Long newId = dataSource.insertEventsReminder(currentEventsReminder);
                wasSuccessful = newId > 0;
                currentEventsReminder.setId(newId);
            } else {
                wasSuccessful = dataSource.updateEventsReminder(currentEventsReminder);
            }
            dataSource.close();
        } catch (Exception e) {
            wasSuccessful = false;
            e.printStackTrace();
        }
    }

}
