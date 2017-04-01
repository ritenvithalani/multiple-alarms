package com.example.pravallika.multiplealarms.fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pravallika.multiplealarms.R;

import java.util.Calendar;

/**
 * Created by Pravallika on 4/1/17.
 */

public class TimePickerFragment2 extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String displayTime = hourOfDay + ":" + minute;
        TextView txtView = (TextView) getActivity().findViewById(R.id.set_time);
        txtView.setText(displayTime);

    }
}
