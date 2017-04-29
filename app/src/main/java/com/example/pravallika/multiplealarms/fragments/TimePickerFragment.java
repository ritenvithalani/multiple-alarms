package com.example.pravallika.multiplealarms.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pravallika.multiplealarms.R;

import java.util.Calendar;

/**
 * Created by RitenVithlani on 2/21/17.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String min = (minute <= 9) ? "0" + minute : minute + "";
        String displayTime = hourOfDay + ":" + min;
        if(null!= this.getArguments() && "splTimePicker".equals(this.getArguments().getString("splTimePicker"))) {
            TextView txtView = (TextView) getActivity().findViewById(R.id.tv_spl_rem_set_time);
            txtView.setText(displayTime);
        } else if(null!= this.getArguments() && "eventsTimePicker".equals(this.getArguments().getString("eventsTimePicker"))) {
            TextView txtView = (TextView) getActivity().findViewById(R.id.tv_events_set_time);
            txtView.setText(displayTime);
        } else if (null != this.getArguments() && "alarmTimePicker".equals(this.getArguments().getString("alarmTimePicker"))) {
            TextView txtView = (TextView) getActivity().findViewById(R.id.set_time);
            txtView.setText(displayTime);
        } else if (null != this.getArguments() && "maFromTimePicker".equals(this.getArguments().getString("maFromTimePicker"))) {
            TextView txtView = (TextView) getActivity().findViewById(R.id.tv_multiple_alarm_from_time);
            txtView.setText(displayTime);
        } else if (null != this.getArguments() && "maToTimePicker".equals(this.getArguments().getString("maToTimePicker"))) {
            TextView txtView = (TextView) getActivity().findViewById(R.id.tv_multiple_alarm_to_time);
            txtView.setText(displayTime);
        }

    }
}
