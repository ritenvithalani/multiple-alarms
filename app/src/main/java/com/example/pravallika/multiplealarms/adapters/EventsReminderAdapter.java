package com.example.pravallika.multiplealarms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.EventsReminder;

import java.util.List;

/**
 * Created by RitenVithlani on 2/20/17.
 */

public class EventsReminderAdapter extends ArrayAdapter<EventsReminder> {

    public EventsReminderAdapter(Context context, List<EventsReminder> eventsReminders) {
        super(context, 0, eventsReminders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(null == convertView) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_events_reminder, parent, false);
        }

        EventsReminder eventsReminder = getItem(position);

        TextView date = (TextView) listItemView.findViewById(R.id.tv_events_rem_date);
        TextView time = (TextView) listItemView.findViewById(R.id.tv_events_rem_time);
        TextView label = (TextView) listItemView.findViewById(R.id.tv_events_rem_label);
        TextView location = (TextView) listItemView.findViewById(R.id.tv_location);

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
        return listItemView;
    }
}
