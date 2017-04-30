package com.example.pravallika.multiplealarms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.SpecialDaysReminder;
import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.database.SpecialDaysReminderDataSource;
import com.example.pravallika.multiplealarms.fragments.DatePickerFragment;
import com.example.pravallika.multiplealarms.fragments.TimePickerFragment;
import com.example.pravallika.multiplealarms.helpers.NotificationHelper;
import com.example.pravallika.multiplealarms.utils.Utility;

import java.util.Calendar;

public class AddSpecialDaysReminderActivity extends AppCompatActivity {

    private final String DEFAULT_SPL_REM_TIME_TEXT = "Set time";
    private final String DEFAULT_SPL_REM_DATE_TEXT = "Set date";
    private final Long DEFAULT_ID = -1l;

    Button btnSetSplReminder;
    LinearLayout llSetTime;
    LinearLayout llSetDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_days_reminder_form);

        Intent intent = getIntent();
        if (null != intent && intent.hasExtra("SpecialDaysReminderEntry")) {
            populateFormValues(intent);
        }
        initSetDate();
        initSetTime();
        initSetSplReminderButton();
    }

    private void populateFormValues(Intent intent) {
        SpecialDaysReminder specialDaysReminder = (SpecialDaysReminder) intent.getSerializableExtra("SpecialDaysReminderEntry");
        TextView id = (TextView) findViewById(R.id.tv_special_rem_id);
        EditText label = (EditText) findViewById(R.id.et_spl_rem_label);
        Spinner title = (Spinner) findViewById(R.id.sp_spl_rem_title);
        TextView date = (TextView) findViewById(R.id.tv_spl_rem_set_date);
        TextView time = (TextView) findViewById(R.id.tv_spl_rem_set_time);

        id.setText(specialDaysReminder.getId() + "");
        label.setText(specialDaysReminder.getLabel());

        String compareValue = specialDaysReminder.getTitle();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.special_days_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        title.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            title.setSelection(spinnerPosition);
        }

        date.setText(specialDaysReminder.getDate());
        time.setText(specialDaysReminder.getTime());
    }

    private void initSetTime() {
        llSetTime = (LinearLayout) findViewById(R.id.ll_spl_rem_set_time);
        llSetTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                Bundle data = new Bundle();
                data.putString("splTimePicker", "splTimePicker");
                newFragment.setArguments(data);
                newFragment.show(getSupportFragmentManager(), "splTimePicker");
            }
        });
    }

    private void initSetDate() {
        llSetDate = (LinearLayout) findViewById(R.id.ll_spl_rem_set_date);
        llSetDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle data = new Bundle();
                data.putString("splDatePicker", "splDatePicker");
                newFragment.setArguments(data);
                newFragment.show(getSupportFragmentManager(), "splDatePicker");
            }
        });
    }

    private void initSetSplReminderButton() {
        btnSetSplReminder = (Button) findViewById(R.id.btn_set_spl_reminder);
        btnSetSplReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecialDaysReminder currentSpecialDaysReminder = extractCurrentSplReminder();
                if (null == currentSpecialDaysReminder) {
                    return;
                }
                saveSplRemToDB(currentSpecialDaysReminder);
                setNotificationForReminder(currentSpecialDaysReminder);
                finish();
            }
        });
    }

    private void setNotificationForReminder(SpecialDaysReminder currentSpecialDaysReminder) {
        Long triggerAtMillis = Utility.getDurationInMillis(currentSpecialDaysReminder.getDate(), currentSpecialDaysReminder.getTime());
        NotificationHelper.createNotification(AddSpecialDaysReminderActivity.this, triggerAtMillis, currentSpecialDaysReminder.getLabel(), MultipleAlarmConstants.FeatureType.SPECIAL_REMINDER);
    }

    private void saveSplRemToDB(SpecialDaysReminder currentSpecialDaysReminder) {
        boolean wasSuccessful = false;
        SpecialDaysReminderDataSource dataSource = new SpecialDaysReminderDataSource(AddSpecialDaysReminderActivity.this);
        try {
            dataSource.openWritableDatabase();

            if (currentSpecialDaysReminder.getId() == -1) {
                Long newId = dataSource.insertSplDaysReminder(currentSpecialDaysReminder);
                wasSuccessful = newId > 0;
                currentSpecialDaysReminder.setId(newId);
            } else {
                wasSuccessful = dataSource.updateSplDaysReminder(currentSpecialDaysReminder);
            }
            dataSource.close();
        } catch (Exception e) {
            wasSuccessful = false;
            e.printStackTrace();
        }
    }

    private SpecialDaysReminder extractCurrentSplReminder() {
        TextView tvSplRemId = (TextView) findViewById(R.id.tv_special_rem_id);
        EditText etSplRemLabel = (EditText) findViewById(R.id.et_spl_rem_label);
        Spinner spSplRemTitle = (Spinner) findViewById(R.id.sp_spl_rem_title);
        TextView tvSplRemDate = (TextView) findViewById(R.id.tv_spl_rem_set_date);
        TextView tvSplRemTime = (TextView) findViewById(R.id.tv_spl_rem_set_time);

        String date = DEFAULT_SPL_REM_DATE_TEXT.equals(tvSplRemDate.getText().toString()) ? Utility.today() : tvSplRemDate.getText().toString();
        String time = DEFAULT_SPL_REM_TIME_TEXT.equals(tvSplRemTime.getText().toString()) ? Utility.now() : tvSplRemTime.getText().toString();

        Long triggerAtMillis = Utility.getDurationInMillis(date, time);
        if (triggerAtMillis < Calendar.getInstance().getTimeInMillis()) {
            Toast.makeText(this, "Selected time period has already elapsed. Please select a future time", Toast.LENGTH_LONG).show();
            return null;
        }

        Long id = !"".equals(tvSplRemId.getText()) ? Long.parseLong(tvSplRemId.getText().toString()) : DEFAULT_ID;
        String label = null != etSplRemLabel.getText() ? etSplRemLabel.getText().toString() : "";
        String title = spSplRemTitle.getSelectedItem().toString();

        SpecialDaysReminder specialDaysReminder = new SpecialDaysReminder();
        specialDaysReminder.setId(id);
        specialDaysReminder.setLabel(label);
        specialDaysReminder.setTitle(title);
        specialDaysReminder.setTime(time);
        specialDaysReminder.setDate(date);
        specialDaysReminder.setActive(Boolean.TRUE);

        return specialDaysReminder;
    }

}
