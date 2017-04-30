package com.example.pravallika.multiplealarms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.adapters.AlarmAdapter;
import com.example.pravallika.multiplealarms.beans.Alarm;
import com.example.pravallika.multiplealarms.database.AlarmDataSource;
import com.example.pravallika.multiplealarms.listeners.MultipleAlarmMultiChoiceModeListener;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends AppCompatActivity {

    AlarmAdapter alarmAdapter;
    ListView listView;
    List<Alarm> alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeFab();
        initializeAlarmList();

    }

    private void initializeAlarmList() {
        alarmAdapter = new AlarmAdapter(this);
        TextView emptyMsg = (TextView) findViewById(R.id.tv_empty_alarm_msg);
        listView = (ListView) findViewById(R.id.alarm_list);
        listView.setAdapter(alarmAdapter);
        listView.setEmptyView(emptyMsg);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AlarmActivity.this, AddAlarmActivity.class);
                intent.putExtra("AddAlarmEntry", alarmAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    private void initializeFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_alarm_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmActivity.this, AddAlarmActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        alarmAdapter.clear();

        // Create a list of specialDaysReminder
        alarms = new ArrayList<Alarm>();
        AlarmDataSource alarmDataSource = new AlarmDataSource(this);
        try {
            alarmDataSource.openReadableDatabase();
            alarms = alarmDataSource.retrieveAlarms();
            alarmDataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SplDaysReminderFrag", "Error while fetching SpecialDaysReminderContract records");
        }

        alarmAdapter.addAll(alarms);
        alarmAdapter.notifyDataSetChanged();

        MultipleAlarmMultiChoiceModeListener multiModeListener = new MultipleAlarmMultiChoiceModeListener(this, alarms, alarmAdapter);
        listView.setMultiChoiceModeListener(multiModeListener);
    }
}

