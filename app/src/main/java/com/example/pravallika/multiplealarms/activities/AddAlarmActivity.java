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
import android.widget.ToggleButton;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.Alarm;
import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.database.AlarmDataSource;
import com.example.pravallika.multiplealarms.fragments.TimePickerFragment;
import com.example.pravallika.multiplealarms.helpers.AlarmHelper;
import com.example.pravallika.multiplealarms.utils.Utility;

import static com.example.pravallika.multiplealarms.R.id.set_time;

public class AddAlarmActivity extends AppCompatActivity {
    private static final String DEFAULT_ALARM_TEXT = "Set Time";
    String[] daysOfWeek = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
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
                setAlarm(currentAlarm);
                finish();
            }
        });
    }

    private void setAlarm(Alarm currentAlarm) {
        for (String dayOfWeek : currentAlarm.getSelectedDays().split(MultipleAlarmConstants.DAY_OF_WEEK_SEPERATOR)) {
            String alarmDate = Utility.getDateFromDayOfWeek(dayOfWeek, currentAlarm.getTime());
            Long triggerAtMillis = Utility.getDurationInMillis(alarmDate, currentAlarm.getTime());
            AlarmHelper.setAlarm(AddAlarmActivity.this, triggerAtMillis, currentAlarm.getLabel(), MultipleAlarmConstants.FeatureType.ALARM);
        }
    }

    private void populateFormValues(Intent intent) {
        Alarm alarm = (Alarm) intent.getSerializableExtra("AddAlarmEntry");
        TextView id = (TextView) findViewById(R.id.tv_alarm_id);
        EditText label = (EditText) findViewById(R.id.et_alarm_label);
        TextView time = (TextView) findViewById(set_time);

        id.setText(alarm.getId() + "");
        label.setText(alarm.getLabel());
        time.setText(alarm.getTime());

        highlightSelectedDays(alarm);
    }

    private void highlightSelectedDays(Alarm alarm) {
        String daysList = alarm.getSelectedDays();

        boolean[] activeMapDaysOfWeek = new boolean[]{false, false, false, false, false, false, false};
        for (int i = 0; i < daysOfWeek.length; i++) {
            if (daysList.contains(daysOfWeek[i])) {
                activeMapDaysOfWeek[i] = true;
            }
        }

        LinearLayout llAlarmDays = (LinearLayout) findViewById(R.id.ll_alarm_days);
        int count = llAlarmDays.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = llAlarmDays.getChildAt(i);
            if (view instanceof ToggleButton) {
                ToggleButton day = (ToggleButton) view;
                if (activeMapDaysOfWeek[i]) {
                    day.setChecked(true);
                    toggleButtonState(day);
                }
            }
        }
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
        TextView alarmTime = (TextView) findViewById(set_time);
        TextView alarmLabel = (TextView) findViewById(R.id.et_alarm_label);

        Long id = !"".equals(alarmId.getText()) ? Long.parseLong(alarmId.getText().toString()) : DEFAULT_ID;
        String label = null != alarmLabel.getText() ? alarmLabel.getText().toString() : "";
        String selectedTime = DEFAULT_ALARM_TEXT.equals(alarmTime.getText().toString()) ? Utility.now() : alarmTime.getText().toString();

        String selectedDays = extractActiveDays(selectedTime);

        Alarm alarm = new Alarm();
        alarm.setId(id);
        alarm.setTime(selectedTime);
        alarm.setLabel(label);
        alarm.setSelectedDays(selectedDays);
        alarm.setActive(Boolean.TRUE);

        return alarm;
    }

    private String extractActiveDays(String selectedTime) {
        String selectedDays = "";
        boolean isDaySelected = false;
        LinearLayout llAlarmDays = (LinearLayout) findViewById(R.id.ll_alarm_days);
        int count = llAlarmDays.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = llAlarmDays.getChildAt(i);
            if (view instanceof ToggleButton) {
                ToggleButton day = (ToggleButton) view;
                if (day.isChecked()) {
                    selectedDays += daysOfWeek[i] + MultipleAlarmConstants.DAY_OF_WEEK_SEPERATOR;
                    isDaySelected = true;
                }
            }
        }

        if (!isDaySelected) {
            selectedDays = Utility.getNextAvailableDayOfWeek(selectedTime);
        } else {
            selectedDays = selectedDays.substring(0, selectedDays.length() - 2);
        }
        return selectedDays;
    }

    private void initSetTime() {
        LinearLayout selectedTime = (LinearLayout) findViewById(R.id.alarm_time);

        selectedTime.setOnClickListener(new View.OnClickListener() {

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
        LinearLayout llAlarmDays = (LinearLayout) findViewById(R.id.ll_alarm_days);
        int count = llAlarmDays.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = llAlarmDays.getChildAt(i);
            if (view instanceof ToggleButton) {
                final ToggleButton day = (ToggleButton) view;
                day.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleButtonState(day);
                    }
                });
            }
        }
    }

    private void toggleButtonState(ToggleButton button) {
        if (button.isChecked()) {
            button.setBackgroundResource(R.drawable.rectangle_active);
            button.setTextColor(Color.WHITE);
        } else {
            button.setBackgroundResource(R.drawable.rectangle_inactive);
            button.setTextColor(Color.DKGRAY);
        }
    }
}

