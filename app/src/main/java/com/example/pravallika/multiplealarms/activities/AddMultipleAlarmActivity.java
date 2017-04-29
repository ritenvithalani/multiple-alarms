package com.example.pravallika.multiplealarms.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.MultipleAlarm;
import com.example.pravallika.multiplealarms.database.MultipleAlarmDataSource;
import com.example.pravallika.multiplealarms.fragments.DatePickerFragment;
import com.example.pravallika.multiplealarms.fragments.TimePickerFragment;
import com.example.pravallika.multiplealarms.helpers.Utility;

import static com.example.pravallika.multiplealarms.R.id.et_multiple_alarms_repeat;
import static com.example.pravallika.multiplealarms.R.id.tv_multiple_alarm_from_date;
import static com.example.pravallika.multiplealarms.R.id.tv_multiple_alarm_from_time;
import static com.example.pravallika.multiplealarms.R.id.tv_multiple_alarm_to_date;
import static com.example.pravallika.multiplealarms.R.id.tv_multiple_alarm_to_time;

public class AddMultipleAlarmActivity extends AppCompatActivity {
    public static final String DAY_OF_WEEK_SEPERATOR = ", ";
    private static final String DEFAULT_TIME_TEXT = "Set Time";
    private static final String DEFAULT_DATE_TEXT = "Set Date";
    String[] daysOfWeek = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private Long DEFAULT_ID = -1l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_multiple_alarm);

        Intent intent = getIntent();
        if (null != intent && intent.hasExtra("AddMultipleAlarmEntry")) {
            populateFormValues(intent);
        }

        initDays();
        initSetFromTime();
        initSetToTime();
        initSetFromDate();
        initSetToDate();

        Button button = (Button) findViewById(R.id.btn_set_multiple_alarm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extract user input
                MultipleAlarm currentMultipleAlarm = extractUserInput();
                if (currentMultipleAlarm == null) return;
                //save currentMultipleAlarm to DB
                saveAlarm(currentMultipleAlarm);
                //set the actual currentMultipleAlarm
                setMultipleAlarm(currentMultipleAlarm);
                finish();
            }
        });
    }

    private void setMultipleAlarm(MultipleAlarm currentMultipleAlarm) {
        /*for (String dayOfWeek : currentMultipleAlarm.getSelectedDays().split(DAY_OF_WEEK_SEPERATOR)) {
            String alarmDate = Utility.getDateFromDayOfWeek(dayOfWeek, currentMultipleAlarm.getTime());
            Long triggerAtMillis = Utility.getDurationInMillis(alarmDate, currentMultipleAlarm.getTime());
            AlarmHelper.setAlarm(AddMultipleAlarmActivity.this, currentMultipleAlarm.getId(), triggerAtMillis, false, "");
        }*/
    }

    private void populateFormValues(Intent intent) {
        MultipleAlarm multipleAlarm = (MultipleAlarm) intent.getSerializableExtra("AddMultipleAlarmEntry");
        TextView id = (TextView) findViewById(R.id.tv_multiple_alarm_id);
        EditText label = (EditText) findViewById(R.id.et_multiple_alarm_label);
        TextView fromTime = (TextView) findViewById(tv_multiple_alarm_from_time);
        TextView toTime = (TextView) findViewById(tv_multiple_alarm_to_time);
        TextView fromDate = (TextView) findViewById(tv_multiple_alarm_from_date);
        TextView toDate = (TextView) findViewById(tv_multiple_alarm_to_date);
        EditText repeat = (EditText) findViewById(et_multiple_alarms_repeat);

        id.setText(multipleAlarm.getId() + "");
        label.setText(multipleAlarm.getLabel());
        fromTime.setText(multipleAlarm.getFromTime());
        toTime.setText(multipleAlarm.getToTime());
        fromDate.setText(multipleAlarm.getFromDate());
        toDate.setText(multipleAlarm.getToDate());
        repeat.setText(multipleAlarm.getRepeat());

        highlightSelectedDays(multipleAlarm);
    }

    private void highlightSelectedDays(MultipleAlarm multipleAlarm) {
        String daysList = multipleAlarm.getSelectedDays();

        boolean[] activeMapDaysOfWeek = new boolean[]{false, false, false, false, false, false, false};
        for (int i = 0; i < daysOfWeek.length; i++) {
            if (daysList.contains(daysOfWeek[i])) {
                activeMapDaysOfWeek[i] = true;
            }
        }

        LinearLayout llMultipleAlarmDays = (LinearLayout) findViewById(R.id.ll_multiple_alarm_days);
        int count = llMultipleAlarmDays.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = llMultipleAlarmDays.getChildAt(i);
            if (view instanceof ToggleButton) {
                ToggleButton day = (ToggleButton) view;
                if (activeMapDaysOfWeek[i]) {
                    day.setChecked(true);
                    toggleButtonState(day);
                }
            }
        }
    }

    private void saveAlarm(MultipleAlarm currentMultipleAlarm) {
        boolean wasSuccessful = false;
        MultipleAlarmDataSource dataSource = new MultipleAlarmDataSource(this);
        try {
            dataSource.openWritableDatabase();

            if (currentMultipleAlarm.getId() == -1) {
                Long newId = dataSource.insertMultipleAlarm(currentMultipleAlarm);
                wasSuccessful = newId > 0;
                currentMultipleAlarm.setId(newId);
            } else {
                wasSuccessful = dataSource.updateMultipleAlarm(currentMultipleAlarm);
            }
            dataSource.close();
        } catch (Exception e) {
            wasSuccessful = false;
            e.printStackTrace();
        }
    }

    private MultipleAlarm extractUserInput() {
        TextView tvId = (TextView) findViewById(R.id.tv_multiple_alarm_id);
        EditText tvLabel = (EditText) findViewById(R.id.et_multiple_alarm_label);
        TextView tvFromTime = (TextView) findViewById(tv_multiple_alarm_from_time);
        TextView tvToTime = (TextView) findViewById(tv_multiple_alarm_to_time);
        TextView tvFromDate = (TextView) findViewById(tv_multiple_alarm_from_date);
        TextView tvToDate = (TextView) findViewById(tv_multiple_alarm_to_date);
        EditText etRepeat = (EditText) findViewById(et_multiple_alarms_repeat);

        // Validate time range and repeat
        if (DEFAULT_TIME_TEXT.equals(tvFromTime.getText().toString()) ||
                DEFAULT_TIME_TEXT.equals(tvToTime.getText().toString())) {
            Toast.makeText(AddMultipleAlarmActivity.this, "Please select the time values for the alarm", Toast.LENGTH_LONG).show();
            return null;
        } else if (null == etRepeat.getText() || "".equals(etRepeat.getText().toString())
                || isInvalidRepeatValue(etRepeat.getText().toString())) {
            Toast.makeText(AddMultipleAlarmActivity.this, "Please enter a valid repeat value", Toast.LENGTH_LONG).show();
            return null;
        } else if (!DEFAULT_DATE_TEXT.equals(tvFromDate.getText().toString()) &&
                !DEFAULT_DATE_TEXT.equals(tvToDate.getText().toString()) &&
                Utility.compareDate(tvFromDate.getText().toString(), tvToDate.getText().toString())) {
            Toast.makeText(AddMultipleAlarmActivity.this, "To date cannot be greater than from date", Toast.LENGTH_LONG).show();
            return null;
        }

        Long id = !"".equals(tvId.getText()) ? Long.parseLong(tvId.getText().toString()) : DEFAULT_ID;
        String label = null != tvLabel.getText() ? tvLabel.getText().toString() : "";
        String selectedFromTime = tvFromTime.getText().toString();
        String selectedToTime = tvToTime.getText().toString();

        // Dates are not mandatory. So make sure both (from and to) dates are present. If one of the date is missing use todays date as a default value
        String selectedFromDate = Utility.today();
        String selectedToDate = Utility.today();
        if (!DEFAULT_DATE_TEXT.equals(tvFromTime.getText().toString()) &&
                !DEFAULT_DATE_TEXT.equals(tvToTime.getText().toString())) {
            selectedFromDate = tvFromDate.getText().toString();
            selectedToDate = tvToDate.getText().toString();
        }
        String repeat = etRepeat.getText().toString();
        String selectedDays = extractActiveDays();

        MultipleAlarm multipleAlarm = new MultipleAlarm();
        multipleAlarm.setId(id);
        multipleAlarm.setFromTime(selectedFromTime);
        multipleAlarm.setToTime(selectedToTime);
        multipleAlarm.setFromDate(selectedFromDate);
        multipleAlarm.setToDate(selectedToDate);
        multipleAlarm.setLabel(label);
        multipleAlarm.setRepeat(repeat);
        multipleAlarm.setSelectedDays(selectedDays);
        multipleAlarm.setActive(Boolean.TRUE);

        return multipleAlarm;
    }

    private boolean isInvalidRepeatValue(String repeatStr) {
        boolean isInvalid = true;
        try {
            int repeat = Integer.parseInt(repeatStr);
            if (repeat > 0 || repeat <= 1440) {
                isInvalid = false; // repeat is only valid if it is between (0, 1440]
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return isInvalid;
    }

    private String extractActiveDays() {
        String selectedDays = "";
        boolean isDaySelected = false;
        LinearLayout llMultipleAlarmDays = (LinearLayout) findViewById(R.id.ll_multiple_alarm_days);
        int count = llMultipleAlarmDays.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = llMultipleAlarmDays.getChildAt(i);
            if (view instanceof ToggleButton) {
                ToggleButton day = (ToggleButton) view;
                if (day.isChecked()) {
                    selectedDays += daysOfWeek[i] + DAY_OF_WEEK_SEPERATOR;
                    isDaySelected = true;
                }
            }
        }

        if (!isDaySelected) {
            selectedDays = TextUtils.join(DAY_OF_WEEK_SEPERATOR, daysOfWeek);
        } else {
            selectedDays = selectedDays.substring(0, selectedDays.length() - 2);
        }
        return selectedDays;
    }

    private void initSetFromTime() {
        TextView selectedFromTime = (TextView) findViewById(R.id.tv_multiple_alarm_from_time);

        selectedFromTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                Bundle data = new Bundle();
                data.putString("maFromTimePicker", "maFromTimePicker");
                newFragment.setArguments(data);
                newFragment.show(getSupportFragmentManager(), "maFromTimePicker");
            }
        });
    }

    private void initSetToTime() {
        TextView selectedToTime = (TextView) findViewById(R.id.tv_multiple_alarm_to_time);

        selectedToTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                Bundle data = new Bundle();
                data.putString("maToTimePicker", "maToTimePicker");
                newFragment.setArguments(data);
                newFragment.show(getSupportFragmentManager(), "maToTimePicker");
            }
        });
    }

    private void initSetFromDate() {
        TextView selectedFromDate = (TextView) findViewById(R.id.tv_multiple_alarm_from_date);
        selectedFromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle data = new Bundle();
                data.putString("maFromDatePicker", "maFromDatePicker");
                newFragment.setArguments(data);
                newFragment.show(getSupportFragmentManager(), "maFromDatePicker");
            }
        });
    }

    private void initSetToDate() {
        TextView selectedToDate = (TextView) findViewById(R.id.tv_multiple_alarm_to_date);
        selectedToDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle data = new Bundle();
                data.putString("maToDatePicker", "maToDatePicker");
                newFragment.setArguments(data);
                newFragment.show(getSupportFragmentManager(), "maToDatePicker");
            }
        });
    }

    private void initDays() {
        LinearLayout llMultipleAlarmDays = (LinearLayout) findViewById(R.id.ll_multiple_alarm_days);
        int count = llMultipleAlarmDays.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = llMultipleAlarmDays.getChildAt(i);
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

