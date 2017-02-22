package com.example.pravallika.multiplealarms.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.fragments.DatePickerFragment;
import com.example.pravallika.multiplealarms.fragments.TimePickerFragment;

public class SpecialDaysReminderFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_days_reminder_form);

        LinearLayout llSetDate = (LinearLayout) findViewById(R.id.ll_spl_rem_set_date);
        LinearLayout llSetTime = (LinearLayout) findViewById(R.id.ll_spl_rem_set_time);

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

        llSetTime.setOnClickListener(new View.OnClickListener(){

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
}
