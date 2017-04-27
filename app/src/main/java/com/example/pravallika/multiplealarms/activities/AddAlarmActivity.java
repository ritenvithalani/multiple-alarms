package com.example.pravallika.multiplealarms.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.Alarm;
import com.example.pravallika.multiplealarms.database.AlarmDataSource;
import com.example.pravallika.multiplealarms.fragments.TimePickerFragment;
import com.example.pravallika.multiplealarms.helpers.Utility;

public class AddAlarmActivity extends AppCompatActivity {
    private static final String DEFAULT_ALARM_TEXT = "Set Time";

    boolean[] activeDays = new boolean[]{false, false, false, false, false, false, false};
    String[] days = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    Button sunday, monday, tuesday, wednesday, thursday, friday, saturday;
    LinearLayout setAlarm;
    private Long DEFAULT_ID = -1l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        Intent intent = getIntent();
        if (null != intent && intent.hasExtra("AddAlarmEntry")) {
            populateFormValues(intent);
        }

        initDays();
        initSetTime();

        Button button = (Button) findViewById(R.id.setAlarmButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extract user input
                Alarm currentAlarm = extractUserInput();
                //save currentAlarm to DB
                saveAlarm(currentAlarm);
                //set the actual currentAlarm

                finish();
            }
        });
    }

    private void populateFormValues(Intent intent) {
        Alarm alarm = (Alarm) intent.getSerializableExtra("AddAlarmEntry");
        TextView id = (TextView) findViewById(R.id.tv_alarm_id);
        EditText label = (EditText) findViewById(R.id.label);
        TextView time = (TextView) findViewById(R.id.set_time);

        id.setText(alarm.getId() + "");
        label.setText(alarm.getLabel());
        time.setText(alarm.getTime());

        // what if no days are selected?
    }

    private void saveAlarm(Alarm currentAlarm) {
        boolean wasSuccessful = false;
        AlarmDataSource dataSource = new AlarmDataSource(this);
        try {
            dataSource.openWritableDatabase();

            if (currentAlarm.getId() == -1) {
                Long newId = dataSource.insertAlarm(currentAlarm);
                wasSuccessful = newId > 0;
                currentAlarm.setId(newId);
            } else {
                wasSuccessful = dataSource.updateAlarm(currentAlarm);
            }
            dataSource.close();
        } catch (Exception e) {
            wasSuccessful = false;
            e.printStackTrace();
        }
    }

    private Alarm extractUserInput() {
        TextView alarmId = (TextView) findViewById(R.id.tv_alarm_id);
        TextView alarmTime = (TextView) findViewById(R.id.set_time);
        String selectedDays = extractActiveDays();
        TextView alarmLabel = (TextView) findViewById(R.id.label);

        Long id = !"".equals(alarmId.getText()) ? Long.parseLong(alarmId.getText().toString()) : DEFAULT_ID;
        String label = null != alarmLabel.getText() ? alarmLabel.getText().toString() : "";
        String time = DEFAULT_ALARM_TEXT.equals(alarmTime.getText().toString()) ? Utility.now() : alarmTime.getText().toString();

        Alarm alarm = new Alarm();
        alarm.setId(id);
        alarm.setTime(time);
        alarm.setLabel(label);
        alarm.setSelectedDays(selectedDays);
        alarm.setActive(Boolean.TRUE);

        return alarm;
    }

    private String extractActiveDays() {
        String selectedDays = "";
        for (int i = 0; i < activeDays.length; i++) {
            if (activeDays[i]) {
                selectedDays += days[i] + ", ";
            }
        }
        selectedDays = selectedDays.substring(0, selectedDays.length() - 2);

        return selectedDays;
    }

    private void initSetTime() {
        setAlarm = (LinearLayout) findViewById(R.id.setAlarm);

        setAlarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                Bundle data = new Bundle();
                data.putString("alarmTimePicker", "alarmTimePicker");
                newFragment.setArguments(data);
                newFragment.show(getSupportFragmentManager(), "alarmTimePicker");
            }
        });
    }

    private void initDays() {
        sunday();
        monday();
        tuesday();
        wednesday();
        thursday();
        friday();
        saturday();
    }

    private void sunday() {
        sunday = (Button) findViewById(R.id.sunday);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeDays[0]) {
                    sunday.setBackgroundResource(R.drawable.rectangle_inactive);
                    sunday.setTextColor(Color.DKGRAY);
                    activeDays[0] = false;
                } else {
                    sunday.setBackgroundResource(R.drawable.rectangle_active);
                    sunday.setTextColor(Color.WHITE);
                    activeDays[0] = true;
                }
            }
        });
    }

    private void monday() {
        monday = (Button) findViewById(R.id.monday);
        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeDays[1]) {
                    monday.setBackgroundResource(R.drawable.rectangle_inactive);
                    monday.setTextColor(Color.DKGRAY);
                    activeDays[1] = false;
                } else {
                    monday.setBackgroundResource(R.drawable.rectangle_active);
                    monday.setTextColor(Color.WHITE);
                    activeDays[1] = true;
                }
            }
        });
    }

    private void tuesday() {
        tuesday = (Button) findViewById(R.id.tuesday);
        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeDays[2]) {
                    tuesday.setBackgroundResource(R.drawable.rectangle_inactive);
                    tuesday.setTextColor(Color.DKGRAY);
                    activeDays[2] = false;
                } else {
                    tuesday.setBackgroundResource(R.drawable.rectangle_active);
                    tuesday.setTextColor(Color.WHITE);
                    activeDays[2] = true;
                }
            }
        });
    }

    private void wednesday() {
        wednesday = (Button) findViewById(R.id.wednesday);
        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeDays[3]) {
                    wednesday.setBackgroundResource(R.drawable.rectangle_inactive);
                    wednesday.setTextColor(Color.DKGRAY);
                    activeDays[3] = false;
                } else {
                    wednesday.setBackgroundResource(R.drawable.rectangle_active);
                    wednesday.setTextColor(Color.WHITE);
                    activeDays[3] = true;
                }
            }
        });

    }

    private void thursday() {
        thursday = (Button) findViewById(R.id.thursday);
        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeDays[4]) {
                    thursday.setBackgroundResource(R.drawable.rectangle_inactive);
                    thursday.setTextColor(Color.DKGRAY);
                    activeDays[4] = false;
                } else {
                    thursday.setBackgroundResource(R.drawable.rectangle_active);
                    thursday.setTextColor(Color.WHITE);
                    activeDays[4] = true;
                }
            }
        });

    }

    private void friday() {
        friday = (Button) findViewById(R.id.friday);
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeDays[5]) {
                    friday.setBackgroundResource(R.drawable.rectangle_inactive);
                    friday.setTextColor(Color.DKGRAY);
                    activeDays[5] = false;
                } else {
                    friday.setBackgroundResource(R.drawable.rectangle_active);
                    friday.setTextColor(Color.WHITE);
                    activeDays[5] = true;
                }
            }
        });

    }

    private void saturday() {
        saturday = (Button) findViewById(R.id.saturday);
        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeDays[6]) {
                    saturday.setBackgroundResource(R.drawable.rectangle_inactive);
                    saturday.setTextColor(Color.DKGRAY);
                    activeDays[6] = false;
                } else {
                    saturday.setBackgroundResource(R.drawable.rectangle_active);
                    saturday.setTextColor(Color.WHITE);
                    activeDays[6] = true;
                }
            }
        });

    }

}

