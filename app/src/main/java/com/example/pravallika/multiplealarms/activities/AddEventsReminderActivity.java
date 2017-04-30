package com.example.pravallika.multiplealarms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.EventsReminder;
import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.database.EventsReminderDataSource;
import com.example.pravallika.multiplealarms.fragments.DatePickerFragment;
import com.example.pravallika.multiplealarms.fragments.TimePickerFragment;
import com.example.pravallika.multiplealarms.helpers.NotificationHelper;
import com.example.pravallika.multiplealarms.utils.Utility;

import java.util.Calendar;

public class AddEventsReminderActivity extends AppCompatActivity {

    private final String DEFAULT_EVENTS_REM_TIME_TEXT = "Set time";
    private final String DEFAULT_EVENTS_REM_DATE_TEXT = "Set date";
    private final Long DEFAULT_ID = -1l;

    Button btnSetEventsReminder;
    LinearLayout llSetTime;
    LinearLayout llSetDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_reminder_form);

        Intent intent = getIntent();
        if (null != intent && intent.hasExtra("EventsReminderEntry")) {
            populateFormValues(intent);
        }

        initSetDate();
        initSetTime();
        initSetEventsReminderButton();

    }

    private void initSetTime() {
        llSetTime = (LinearLayout) findViewById(R.id.ll_events_set_time);
        llSetTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                Bundle data = new Bundle();
                data.putString("eventsTimePicker", "eventsTimePicker");
                newFragment.setArguments(data);
                newFragment.show(getSupportFragmentManager(), "eventsTimePicker");
            }
        });
    }

    private void initSetDate() {
        llSetDate = (LinearLayout) (LinearLayout) findViewById(R.id.ll_events_set_date);
        llSetDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle data = new Bundle();
                data.putString("eventsDatePicker","eventsDatePicker");
                newFragment.setArguments(data);
                newFragment.show(getSupportFragmentManager(), "eventsDatePicker");
            }
        });
    }

    private void initSetEventsReminderButton() {
        btnSetEventsReminder = (Button) findViewById(R.id.btn_events_reminder);
        btnSetEventsReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventsReminder currentEventsReminder = extractCurrentSplReminder();
                if (null == currentEventsReminder) {
                    return;
                }

                saveEventsRemToDB(currentEventsReminder);
                setNotificationForReminder(currentEventsReminder);
                finish();
            }
        });
    }

    private void setNotificationForReminder(EventsReminder eventsReminder) {
        Long triggerAtMillis = Utility.getDurationInMillis(eventsReminder.getDate(), eventsReminder.getTime());
        NotificationHelper.createNotification(AddEventsReminderActivity.this, triggerAtMillis, eventsReminder.getLabel(), MultipleAlarmConstants.FeatureType.EVENT_REMINDER);
    }

    private EventsReminder extractCurrentSplReminder() {
        TextView tvEventsRemId = (TextView) findViewById(R.id.tv_events_rem_id);
        EditText etEventsRemLabel = (EditText) findViewById(R.id.et_events_label);
        EditText etEventsRemLocation = (EditText) findViewById(R.id.et_events_location);
        TextView tvEventsRemDate = (TextView) findViewById(R.id.tv_events_set_date);
        TextView tvEventsRemTime = (TextView) findViewById(R.id.tv_events_set_time);

        String date = DEFAULT_EVENTS_REM_DATE_TEXT.equals(tvEventsRemDate.getText().toString()) ? Utility.today() : tvEventsRemDate.getText().toString();
        String time = DEFAULT_EVENTS_REM_TIME_TEXT.equals(tvEventsRemTime.getText().toString()) ? Utility.now() : tvEventsRemTime.getText().toString();

        Long triggerAtMillis = Utility.getDurationInMillis(date, time);
        if (triggerAtMillis < Calendar.getInstance().getTimeInMillis()) {
            Toast.makeText(this, "Selected time period has already elapsed. Please select a future time", Toast.LENGTH_LONG).show();
            return null;
        }

        Long id = !"".equals(tvEventsRemId.getText()) ? Long.parseLong(tvEventsRemId.getText().toString()) : DEFAULT_ID;
        String label = null != etEventsRemLabel.getText() ? etEventsRemLabel.getText().toString() : "";
        String location = null != etEventsRemLocation.getText() ? etEventsRemLocation.getText().toString() : "";

        EventsReminder eventsReminder = new EventsReminder();
        eventsReminder.setId(id);
        eventsReminder.setLabel(label);
        eventsReminder.setLocation(location);
        eventsReminder.setTime(time);
        eventsReminder.setDate(date);
        eventsReminder.setActive(Boolean.TRUE);

        return eventsReminder;
    }

    private void saveEventsRemToDB(EventsReminder currentEventsReminder) {
        boolean wasSuccessful = false;
        EventsReminderDataSource dataSource = new EventsReminderDataSource(AddEventsReminderActivity.this);
        try {
            dataSource.openWritableDatabase();

            if (currentEventsReminder.getId() == -1) {
                Long newId = dataSource.insertEventsReminder(currentEventsReminder);
                wasSuccessful = newId > 0;
                currentEventsReminder.setId(newId);
            } else {
                wasSuccessful = dataSource.updateEventsReminder(currentEventsReminder);
            }
            dataSource.close();
        } catch (Exception e) {
            wasSuccessful = false;
            e.printStackTrace();
        }
    }

    private void populateFormValues(Intent intent) {
        EventsReminder eventsReminder = (EventsReminder) intent.getSerializableExtra("EventsReminderEntry");
        TextView id = (TextView) findViewById(R.id.tv_events_rem_id);
        EditText label = (EditText) findViewById(R.id.et_events_label);
        EditText location = (EditText) findViewById(R.id.et_events_location);
        TextView date = (TextView) findViewById(R.id.tv_events_set_date);
        TextView time = (TextView) findViewById(R.id.tv_events_set_time);

        id.setText(eventsReminder.getId() + "");
        label.setText(eventsReminder.getLabel());
        location.setText(eventsReminder.getLocation());
        date.setText(eventsReminder.getDate());
        time.setText(eventsReminder.getTime());
    }

}
