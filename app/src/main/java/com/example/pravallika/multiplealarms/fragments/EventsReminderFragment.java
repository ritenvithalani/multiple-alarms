package com.example.pravallika.multiplealarms.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.activities.EventsReminderFormActivity;
import com.example.pravallika.multiplealarms.adapters.EventsReminderAdapter;
import com.example.pravallika.multiplealarms.beans.EventsReminder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsReminderFragment extends Fragment {


    public EventsReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_events_reminder, container, false);

        // Create a list of words
        List<EventsReminder> eventsReminders = new ArrayList<EventsReminder>();
        eventsReminders.add(new EventsReminder(1, "Ram birthday", "Mar 6", "12:00 AM", "at Home", Boolean.TRUE));
        eventsReminders.add(new EventsReminder(2, "Ram birthday", "Mar 6", "12:00 AM", "at Home", Boolean.TRUE));
        eventsReminders.add(new EventsReminder(3, "Ram birthday", "Mar 6", "12:00 AM", "at Home", Boolean.TRUE));
        eventsReminders.add(new EventsReminder(4, "Ram birthday", "Mar 6", "12:00 AM", "at Home", Boolean.TRUE));
        eventsReminders.add(new EventsReminder(5, "Ram birthday", "Mar 6", "12:00 AM", "at Home", Boolean.TRUE));
        eventsReminders.add(new EventsReminder(6, "Ram birthday", "Mar 6", "12:00 AM", "at Home", Boolean.TRUE));
        eventsReminders.add(new EventsReminder(7, "Parents Marriage day", "Sep 10", "8:00 AM", "at Seattle", Boolean.TRUE));

        EventsReminderAdapter eventsReminderAdapter =
                new EventsReminderAdapter(getActivity(), eventsReminders);

        ListView listView = (ListView) rootView.findViewById(R.id.events_reminder_list);

        listView.setAdapter(eventsReminderAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EventsReminderFormActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
