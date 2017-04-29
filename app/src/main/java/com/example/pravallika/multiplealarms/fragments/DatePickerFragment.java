package com.example.pravallika.multiplealarms.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;

import java.util.Calendar;

/**
 * Created by RitenVithlani on 2/21/17.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String[] months = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        String text = months[month] + " " + dayOfMonth + ", " + year;
        if(null!= this.getArguments() && "splDatePicker".equals(this.getArguments().getString("splDatePicker"))) {
            TextView txtView = (TextView) getActivity().findViewById(R.id.tv_spl_rem_set_date);
            txtView.setText(text);
        } else if(null!= this.getArguments() && "eventsDatePicker".equals(this.getArguments().getString("eventsDatePicker"))) {
            TextView txtView = (TextView) getActivity().findViewById(R.id.tv_events_set_date);
            txtView.setText(text);
        } else if (null != this.getArguments() && "maFromDatePicker".equals(this.getArguments().getString("maFromDatePicker"))) {
            TextView txtView = (TextView) getActivity().findViewById(R.id.tv_multiple_alarm_from_date);
            txtView.setText(text);
        } else if (null != this.getArguments() && "maToDatePicker".equals(this.getArguments().getString("maToDatePicker"))) {
            TextView txtView = (TextView) getActivity().findViewById(R.id.tv_multiple_alarm_to_date);
            txtView.setText(text);
        }
    }
}
