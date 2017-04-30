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
import com.example.pravallika.multiplealarms.adapters.MultipleAlarmAdapter;
import com.example.pravallika.multiplealarms.beans.MultipleAlarm;
import com.example.pravallika.multiplealarms.database.MultipleAlarmDataSource;
import com.example.pravallika.multiplealarms.listeners.MultipleAlarmMultiChoiceModeListener;

import java.util.ArrayList;
import java.util.List;

public class MultipleAlarmActivity extends AppCompatActivity {

    MultipleAlarmAdapter multipleAlarmAdapter;
    ListView listView;
    List<MultipleAlarm> multipleAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_alarm);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeFab();
        initializeMultipleAlarmList();
    }

    private void initializeMultipleAlarmList() {
        multipleAlarmAdapter = new MultipleAlarmAdapter(this);
        TextView emptyMsg = (TextView) findViewById(R.id.tv_empty_multiple_alarm_msg);
        listView = (ListView) findViewById(R.id.multiple_alarm_list);
        listView.setAdapter(multipleAlarmAdapter);
        listView.setEmptyView(emptyMsg);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MultipleAlarmActivity.this, AddMultipleAlarmActivity.class);
                intent.putExtra("AddMultipleAlarmEntry", multipleAlarmAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    private void initializeFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_multiple_alarm_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultipleAlarmActivity.this, AddMultipleAlarmActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        multipleAlarmAdapter.clear();

        // Create a list of specialDaysReminder
        multipleAlarms = new ArrayList<MultipleAlarm>();
        MultipleAlarmDataSource multipleAlarmDataSource = new MultipleAlarmDataSource(this);
        try {
            multipleAlarmDataSource.openReadableDatabase();
            multipleAlarms = multipleAlarmDataSource.retrieveMultipleAlarms();
            multipleAlarmDataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MultipleAlarmActivity", "Error while fetching MultipleAlarm records");
        }

        multipleAlarmAdapter.addAll(multipleAlarms);
        multipleAlarmAdapter.notifyDataSetChanged();

        MultipleAlarmMultiChoiceModeListener multiModeListener = new MultipleAlarmMultiChoiceModeListener(this, multipleAlarms, multipleAlarmAdapter);
        listView.setMultiChoiceModeListener(multiModeListener);
    }
}
