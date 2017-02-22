package com.example.pravallika.multiplealarms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.Reminder;

import java.util.List;

/**
 * Created by RitenVithlani on 2/20/17.
 */

public class EventsReminderAdapter extends ArrayAdapter<Reminder> {

    public EventsReminderAdapter(Context context, List<Reminder> reminders) {
        super(context, 0, reminders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(null == convertView) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.events_reminder_item, parent, false);
        }

        Reminder reminder = getItem(position);

        TextView date = (TextView) listItemView.findViewById(R.id.tv_date);
        TextView time = (TextView) listItemView.findViewById(R.id.tv_time);
        TextView label = (TextView) listItemView.findViewById(R.id.tv_label);
        TextView location = (TextView) listItemView.findViewById(R.id.tv_location);

        if(null != reminder.getDate() && null!=date && !"".equals(reminder.getDate())) {
            date.setText(reminder.getDate());
        }
        else {
            date.setVisibility(View.GONE);
        }

        if(null != reminder.getTime() && null!= time && !"".equals(reminder.getTime())) {
            time.setText(reminder.getTime());
        }
        else {
            time.setVisibility(View.GONE);
        }

        if(null != reminder.getLabel() && null!=label && !"".equals(reminder.getLabel())) {
            label.setText(reminder.getLabel());
        }
        else {
            label.setVisibility(View.GONE);
        }

        if(null != reminder.getLocation() && null!=location && !"".equals(reminder.getLocation())) {
            location.setText(reminder.getLocation());
        }
        else {
            location.setVisibility(View.GONE);
        }
        return listItemView;
    }
}
