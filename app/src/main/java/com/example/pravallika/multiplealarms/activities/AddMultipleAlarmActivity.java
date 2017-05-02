package com.example.pravallika.multiplealarms.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.MultipleAlarm;
import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.database.MultipleAlarmDataSource;
import com.example.pravallika.multiplealarms.fragments.DatePickerFragment;
import com.example.pravallika.multiplealarms.fragments.TimePickerFragment;
import com.example.pravallika.multiplealarms.helpers.AlarmHelper;
import com.example.pravallika.multiplealarms.receivers.AlarmReceiver;
import com.example.pravallika.multiplealarms.utils.Utility;

import java.util.Calendar;
import java.util.Date;

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
                //setMultipleAlarmTest(currentMultipleAlarm);
                finish();
            }
        });
    }

    private void setMultipleAlarmTest(MultipleAlarm currentMultipleAlarm) {
        for (int i = 1; i <= 10; i++) {

            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("isAlarmOn", true); // used to track the alarm state
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Log.i("i ", i + "");
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10000 * i), pendingIntent);
        }
    }

    private void setMultipleAlarm(MultipleAlarm currentMultipleAlarm) {
        Calendar fromDate = Utility.convertToCalendarDate(currentMultipleAlarm.getFromDate());
        Calendar toDate = Utility.convertToCalendarDate(currentMultipleAlarm.getToDate());
        boolean isValid = false;
        if (TextUtils.join(DAY_OF_WEEK_SEPERATOR, daysOfWeek).equals(currentMultipleAlarm.getSelectedDays())) {
            isValid = true;
        }

        do {
            int fromDateDayOfWeek = fromDate.get(Calendar.DAY_OF_WEEK);
            // isValid is true when user does not select any days of week. If no days of week is selected then create alarm for everyday within the date range
            // If user has selected the days of week then alarm should be triggered for that particular day in the given date range
            if (isValid || currentMultipleAlarm.getSelectedDays().contains(daysOfWeek[fromDateDayOfWeek - 1])) {

                // Add repeat interval to the from date such that it between start and end time
                int fromTimeInMins = Utility.convertTimeInMins(currentMultipleAlarm.getFromTime());
                int toTimeInMins = Utility.convertTimeInMins(currentMultipleAlarm.getToTime());
                int repeatInterval = Integer.parseInt(currentMultipleAlarm.getRepeat());

                fromDate.add(Calendar.MINUTE, fromTimeInMins);

                for (int time = fromTimeInMins; time < toTimeInMins; time = time + repeatInterval) {
                    if (time > Utility.convertTimeInMins(Utility.now())) {
                        fromDate.add(Calendar.MINUTE, repeatInterval);
                        Date d1 = fromDate.getTime();
                        Log.i("from date values", d1.toString());
                        AlarmHelper.setAlarm(AddMultipleAlarmActivity.this, fromDate.getTimeInMillis(), currentMultipleAlarm.getLabel(), MultipleAlarmConstants.FeatureType.MULTIPLE_ALARM);
                    }
                }
            }
            // Increment the day
            fromDate.add(Calendar.DAY_OF_YEAR, 1);
        } while (fromDate.getTimeInMillis() < toDate.getTimeInMillis());

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
        } else if (Utility.convertTimeInMins(tvFromTime.getText().toString()) >= Utility.convertTimeInMins(tvToTime.getText().toString())) {
            Toast.makeText(AddMultipleAlarmActivity.this, "To time cannot be greater than from time", Toast.LENGTH_LONG).show();
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
        } else if (!DEFAULT_DATE_TEXT.equals(tvFromDate.getText().toString()) &&
                !DEFAULT_DATE_TEXT.equals(tvToDate.getText().toString()) &&
                (Utility.compareDate(Utility.today(), tvFromDate.getText().toString()) ||
                        Utility.compareDate(Utility.today(), tvToDate.getText().toString()))) {
            Toast.makeText(AddMultipleAlarmActivity.this, "To and from date cannot be the past date", Toast.LENGTH_LONG).show();
            return null;
        }

        Long id = !"".equals(tvId.getText()) ? Long.parseLong(tvId.getText().toString()) : DEFAULT_ID;
        String label = null != tvLabel.getText() ? tvLabel.getText().toString() : "";
        String selectedFromTime = tvFromTime.getText().toString();
        String selectedToTime = tvToTime.getText().toString();

        // Dates are not mandatory. So make sure both (from and to) dates are present. If one of the date is missing use todays date as a default value
        String selectedFromDate = Utility.today();
        String selectedToDate = Utility.today();
        if (!DEFAULT_DATE_TEXT.equals(tvFromDate.getText().toString()) &&
                !DEFAULT_DATE_TEXT.equals(tvToDate.getText().toString())) {
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
            if (repeat > 0 && repeat <= 1440) {
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

