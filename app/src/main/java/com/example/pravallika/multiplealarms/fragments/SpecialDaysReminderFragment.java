package com.example.pravallika.multiplealarms.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.adapters.ReminderAdapter;
import com.example.pravallika.multiplealarms.beans.Reminder;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpecialDaysReminderFragment extends Fragment {


    public SpecialDaysReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);

        // Create a list of words
        List<Reminder> reminders = new ArrayList<Reminder>();
        // Fetch the values from the database
        reminders.add(new Reminder("Ram birthday", "Mar 6", "12:00 AM"));
        reminders.add(new Reminder("Parents Marriage day", "Sep 10", "8:00 AM"));

        ReminderAdapter reminderAdapter =
                new ReminderAdapter(getActivity(), reminders);

        ListView listView = (ListView) rootView.findViewById(R.id.reminder_list);
        listView.setAdapter(reminderAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Fragment editSpecialDaysReminderFragment = new SpecialDaysReminderFormFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.reminder_list, editSpecialDaysReminderFragment).
                        addToBackStack("editSpecialDaysReminderFragment").commit();*/
            }
        });

        return rootView;
    }

}
