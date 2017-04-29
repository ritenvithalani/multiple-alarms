package com.example.pravallika.multiplealarms.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.activities.AddEventsReminderActivity;
import com.example.pravallika.multiplealarms.adapters.EventsReminderAdapter;
import com.example.pravallika.multiplealarms.beans.EventsReminder;
import com.example.pravallika.multiplealarms.database.EventsReminderDataSource;
import com.example.pravallika.multiplealarms.listeners.MultipleAlarmMultiChoiceModeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsReminderFragment extends Fragment {

    EventsReminderAdapter eventsReminderAdapter;
    ListView listView;
    List<EventsReminder> eventsReminders;

    public EventsReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_events_reminder, container, false);

        eventsReminderAdapter = new EventsReminderAdapter(getActivity());
        TextView emptyMsg = (TextView) rootView.findViewById(R.id.tv_empty_events_reminder_msg);
        listView = (ListView) rootView.findViewById(R.id.events_reminder_list);
        listView.setAdapter(eventsReminderAdapter);
        listView.setEmptyView(emptyMsg);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AddEventsReminderActivity.class);
                intent.putExtra("EventsReminderEntry", eventsReminderAdapter.getItem(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        eventsReminderAdapter.clear();

        // Create a list of eventsReminder
        eventsReminders = new ArrayList<EventsReminder>();
        EventsReminderDataSource eventsReminderDataSource = new EventsReminderDataSource(getActivity());
        try {
            eventsReminderDataSource.openReadableDatabase();
            eventsReminders = eventsReminderDataSource.retrieveEventReminders();
            eventsReminderDataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EventsReminderFrag", "Error while fetching EventsReminderContract records");
        }

        eventsReminderAdapter.addAll(eventsReminders);
        eventsReminderAdapter.notifyDataSetChanged();

        MultipleAlarmMultiChoiceModeListener multiModeListener = new MultipleAlarmMultiChoiceModeListener(getContext(), eventsReminders, eventsReminderAdapter);
        listView.setMultiChoiceModeListener(multiModeListener);
    }

}
