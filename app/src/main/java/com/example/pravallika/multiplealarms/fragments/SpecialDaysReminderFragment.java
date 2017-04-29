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
import com.example.pravallika.multiplealarms.activities.AddSpecialDaysReminderActivity;
import com.example.pravallika.multiplealarms.adapters.SpecialDaysReminderAdapter;
import com.example.pravallika.multiplealarms.beans.SpecialDaysReminder;
import com.example.pravallika.multiplealarms.database.SpecialDaysReminderDataSource;
import com.example.pravallika.multiplealarms.listeners.MultipleAlarmMultiChoiceModeListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpecialDaysReminderFragment extends Fragment {

    private ListView listView;
    private List<SpecialDaysReminder> specialDaysReminder;
    private SpecialDaysReminderAdapter specialDaysReminderAdapter;

    public SpecialDaysReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_spl_days_reminder, container, false);

        specialDaysReminderAdapter = new SpecialDaysReminderAdapter(getActivity());
        TextView emptyMsg = (TextView) rootView.findViewById(R.id.tv_empty_spl_reminder_msg);
        listView = (ListView) rootView.findViewById(R.id.reminder_list);
        listView.setAdapter(specialDaysReminderAdapter);
        listView.setEmptyView(emptyMsg);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AddSpecialDaysReminderActivity.class);
                intent.putExtra("SpecialDaysReminderEntry", specialDaysReminderAdapter.getItem(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        specialDaysReminderAdapter.clear();

        // Create a list of specialDaysReminder
        specialDaysReminder = new ArrayList<SpecialDaysReminder>();
        SpecialDaysReminderDataSource specialDaysReminderDataSource = new SpecialDaysReminderDataSource(getActivity());
        try {
            specialDaysReminderDataSource.openReadableDatabase();
            specialDaysReminder = specialDaysReminderDataSource.retrieveSpecialDaysReminders();
            specialDaysReminderDataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SplDaysReminderFrag", "Error while fetching SpecialDaysReminderContract records");
        }

        specialDaysReminderAdapter.addAll(specialDaysReminder);
        specialDaysReminderAdapter.notifyDataSetChanged();

        MultipleAlarmMultiChoiceModeListener multiModeListener = new MultipleAlarmMultiChoiceModeListener(getContext(), specialDaysReminder, specialDaysReminderAdapter);
        listView.setMultiChoiceModeListener(multiModeListener);
    }
}
