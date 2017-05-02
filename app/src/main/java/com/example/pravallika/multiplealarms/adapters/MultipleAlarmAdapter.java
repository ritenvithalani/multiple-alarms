package com.example.pravallika.multiplealarms.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.MultipleAlarm;
import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.database.MultipleAlarmDataSource;
import com.example.pravallika.multiplealarms.helpers.AlarmHelper;
import com.example.pravallika.multiplealarms.utils.Utility;

import java.util.Calendar;
import java.util.Date;

import static com.example.pravallika.multiplealarms.adapters.AlarmAdapter.COUNT_CHARS_ALL_DAYS;

/**
 * Created by RitenVithlani on 4/26/17.
 */

public class MultipleAlarmAdapter extends ArrayAdapter<MultipleAlarm> {

    Context context;

    public MultipleAlarmAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (null == convertView) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_multiple_alarm, parent, false);
        }

        final MultipleAlarm multipleAlarm = getItem(position);

        TextView id = (TextView) listItemView.findViewById(R.id.tv_list_multiple_alarm_id);
        TextView label = (TextView) listItemView.findViewById(R.id.tv_multiple_alarm_label);
        TextView fromDate = (TextView) listItemView.findViewById(R.id.tv_multiple_alarm_from_date);
        TextView toDate = (TextView) listItemView.findViewById(R.id.tv_multiple_alarm_to_date);
        TextView fromTime = (TextView) listItemView.findViewById(R.id.tv_multiple_alarm_from_time);
        TextView toTime = (TextView) listItemView.findViewById(R.id.tv_multiple_alarm_to_time);
        TextView repeat = (TextView) listItemView.findViewById(R.id.tv_multiple_alarm_repeat_time);
        TextView selectedDays = (TextView) listItemView.findViewById(R.id.tv_multiple_alarm_selected_days);
        Switch alarmToggleSwitch = (Switch) listItemView.findViewById(R.id.multiple_alarm_toggle_switch);

        id.setText(multipleAlarm.getId() + "");

        String formattedFromTime = Utility.formatMinute(multipleAlarm.getFromTime());
        String formattedToTime = Utility.formatMinute(multipleAlarm.getToTime());
        fromTime.setText(formattedFromTime);
        toTime.setText(formattedToTime);

        if (null != multipleAlarm.getLabel() && !"".equals(multipleAlarm.getLabel())) {
            label.setText(multipleAlarm.getLabel());
        } else {
            label.setVisibility(View.GONE);
        }

        if (null != multipleAlarm.getSelectedDays() && !"".equals(multipleAlarm.getSelectedDays())) {
            if (multipleAlarm.getSelectedDays().length() == COUNT_CHARS_ALL_DAYS) {
                selectedDays.setText("Everyday");
            } else {
                selectedDays.setText(multipleAlarm.getSelectedDays());
            }
        } else {
            selectedDays.setVisibility(View.GONE);
        }

        if (null != multipleAlarm.getFromDate() && null != multipleAlarm.getToDate()
                && !"".equals(multipleAlarm.getFromDate()) && !"".equals(multipleAlarm.getToDate())) {
            fromDate.setText(multipleAlarm.getFromDate());
            toDate.setText(multipleAlarm.getToDate());
        } else {
            fromDate.setVisibility(View.GONE);
            toDate.setVisibility(View.GONE);
        }

        if (null != multipleAlarm.getRepeat() && !"".equals(multipleAlarm.getRepeat())) {
            repeat.setText(multipleAlarm.getRepeat());
        } else {
            repeat.setVisibility(View.GONE);
        }

        alarmToggleSwitch.setChecked(null != multipleAlarm.getActive() ? multipleAlarm.getActive() : Boolean.TRUE);

        alarmToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setMultipleAlarm(multipleAlarm);
                } else {
                    cancelMultipleAlarm(multipleAlarm);
                }

                multipleAlarm.setActive(isChecked);
                saveAlarm(multipleAlarm);
            }
        });

        return listItemView;
    }

    private void cancelMultipleAlarm(MultipleAlarm multipleAlarm) {
        Calendar fromDate = Utility.convertToCalendarDate(multipleAlarm.getFromDate());
        Calendar toDate = Utility.convertToCalendarDate(multipleAlarm.getToDate());
        boolean isValid = false;
        if (TextUtils.join(MultipleAlarmConstants.DAY_OF_WEEK_SEPERATOR, MultipleAlarmConstants.DAYS_OF_WEEK).equals(multipleAlarm.getSelectedDays())) {
            isValid = true;
        }

        do {
            int fromDateDayOfWeek = fromDate.get(Calendar.DAY_OF_WEEK);
            // isValid is true when user does not select any days of week. If no days of week is selected then create alarm for everyday within the date range
            // If user has selected the days of week then alarm should be triggered for that particular day in the given date range
            if (isValid || multipleAlarm.getSelectedDays().contains(MultipleAlarmConstants.DAYS_OF_WEEK[fromDateDayOfWeek - 1])) {

                // Add repeat interval to the from date such that it between start and end time
                int fromTimeInMins = Utility.convertTimeInMins(multipleAlarm.getFromTime());
                int toTimeInMins = Utility.convertTimeInMins(multipleAlarm.getToTime());
                int repeatInterval = Integer.parseInt(multipleAlarm.getRepeat());

                fromDate.add(Calendar.MINUTE, fromTimeInMins);

                for (int time = fromTimeInMins; time < toTimeInMins; time = time + repeatInterval) {
                    fromDate.add(Calendar.MINUTE, repeatInterval);
                    if (time >= Utility.convertTimeInMins(Utility.now())) {
                        Date d1 = fromDate.getTime();
                        Log.i("from date values", d1.toString());
                        AlarmHelper.setAlarm(context, fromDate.getTimeInMillis(), multipleAlarm.getLabel(), MultipleAlarmConstants.FeatureType.MULTIPLE_ALARM);
                        int requestCode = Utility.getUniqueRequestCode(fromDate.getTimeInMillis(), MultipleAlarmConstants.FeatureType.MULTIPLE_ALARM);
                        AlarmHelper.cancelAlarm(context, requestCode);
                    }
                }
            }
            // Increment the day
            fromDate.add(Calendar.DAY_OF_YEAR, 1);
        } while (fromDate.getTimeInMillis() <= toDate.getTimeInMillis());

    }

    private void setMultipleAlarm(MultipleAlarm currentMultipleAlarm) {
        Calendar fromDate = Utility.convertToCalendarDate(currentMultipleAlarm.getFromDate());
        Calendar toDate = Utility.convertToCalendarDate(currentMultipleAlarm.getToDate());
        boolean isValid = false;
        if (TextUtils.join(MultipleAlarmConstants.DAY_OF_WEEK_SEPERATOR, MultipleAlarmConstants.DAYS_OF_WEEK).equals(currentMultipleAlarm.getSelectedDays())) {
            isValid = true;
        }

        do {
            int fromDateDayOfWeek = fromDate.get(Calendar.DAY_OF_WEEK);
            // isValid is true when user does not select any days of week. If no days of week is selected then create alarm for everyday within the date range
            // If user has selected the days of week then alarm should be triggered for that particular day in the given date range
            if (isValid || currentMultipleAlarm.getSelectedDays().contains(MultipleAlarmConstants.DAYS_OF_WEEK[fromDateDayOfWeek - 1])) {

                // Add repeat interval to the from date such that it between start and end time
                int fromTimeInMins = Utility.convertTimeInMins(currentMultipleAlarm.getFromTime());
                int toTimeInMins = Utility.convertTimeInMins(currentMultipleAlarm.getToTime());
                int repeatInterval = Integer.parseInt(currentMultipleAlarm.getRepeat());

                fromDate.add(Calendar.MINUTE, fromTimeInMins);

                for (int time = fromTimeInMins; time < toTimeInMins; time = time + repeatInterval) {
                    fromDate.add(Calendar.MINUTE, repeatInterval);
                    if (time >= Utility.convertTimeInMins(Utility.now())) {
                        Date d1 = fromDate.getTime();
                        Log.i("from date values", d1.toString());
                        AlarmHelper.setAlarm(context, fromDate.getTimeInMillis(), currentMultipleAlarm.getLabel(), MultipleAlarmConstants.FeatureType.MULTIPLE_ALARM);
                    }
                }
            }
            // Increment the day
            fromDate.add(Calendar.DAY_OF_YEAR, 1);
        } while (fromDate.getTimeInMillis() <= toDate.getTimeInMillis());

    }

    private void saveAlarm(MultipleAlarm currentMultipleAlarm) {
        boolean wasSuccessful = false;
        MultipleAlarmDataSource dataSource = new MultipleAlarmDataSource(context);
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
}
