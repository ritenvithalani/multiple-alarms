package com.example.pravallika.multiplealarms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.pravallika.multiplealarms.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAlarm = (Button) findViewById(R.id.btn_alarm);
        Button btnMultipleAlarm = (Button) findViewById(R.id.btn_multiple_alarm);
        Button btnReminder = (Button) findViewById(R.id.btn_reminder);

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alarmIntent = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(alarmIntent);
            }
        });

        btnMultipleAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent multipleAlarmIntent = new Intent(MainActivity.this, MultipleAlarmActivity.class);
                startActivity(multipleAlarmIntent);
            }
        });

        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reminderIntent = new Intent(MainActivity.this, ReminderActivity.class);
                startActivity(reminderIntent);
            }
        });

    }
}
