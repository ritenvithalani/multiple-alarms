package com.example.pravallika.multiplealarms.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.adapters.ReminderAdapter;
import com.example.pravallika.multiplealarms.beans.Reminder;

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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);

        // Create a list of words
        List<Reminder> reminders = new ArrayList<Reminder>();
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM", "at Home"));
        reminders.add(new Reminder("Parents Marriage day", "Sep 10", "8:00 AM", "at Seattle"));

        ReminderAdapter reminderAdapter =
                new ReminderAdapter(getActivity(), reminders);

        ListView listView = (ListView) rootView.findViewById(R.id.reminder_list);

        listView.setAdapter(reminderAdapter);

        return rootView;
    }

}
