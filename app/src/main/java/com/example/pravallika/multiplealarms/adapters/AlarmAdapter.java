package com.example.pravallika.multiplealarms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.Alarm;
import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.database.AlarmDataSource;
import com.example.pravallika.multiplealarms.helpers.AlarmHelper;
import com.example.pravallika.multiplealarms.utils.Utility;

/**
 * Created by RitenVithlani on 4/26/17.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    // Total characters for Sun, Mon, Tue, ... Sat is 33
    public static final int COUNT_CHARS_ALL_DAYS = 33;
    Context context;

    public AlarmAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (null == convertView) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_alarm, parent, false);
        }

        final Alarm alarm = getItem(position);

        TextView id = (TextView) listItemView.findViewById(R.id.tv_list_alarm_id);
        TextView time = (TextView) listItemView.findViewById(R.id.tv_alarm_time);
        TextView label = (TextView) listItemView.findViewById(R.id.tv_alarm_label);
        TextView selectedDays = (TextView) listItemView.findViewById(R.id.tv_alarm_selected_days);
        Switch alarmToggleSwitch = (Switch) listItemView.findViewById(R.id.multiple_alarm_toggle_switch);

        id.setText(alarm.getId() + "");
        String formattedTime = Utility.formatMinute(alarm.getTime());
        time.setText(formattedTime);

        if (null != alarm.getLabel() && !"".equals(alarm.getLabel())) {
            label.setText(alarm.getLabel());
        } else {
            label.setVisibility(View.GONE);
        }

        if (null != alarm.getSelectedDays() && !"".equals(alarm.getSelectedDays())) {
            if (alarm.getSelectedDays().length() == COUNT_CHARS_ALL_DAYS) {
                selectedDays.setText("Everyday");
            } else {
                selectedDays.setText(alarm.getSelectedDays());
            }
        } else {
            selectedDays.setVisibility(View.GONE);
        }

        alarmToggleSwitch.setChecked(null != alarm.getActive() ? alarm.getActive() : Boolean.TRUE);

        alarmToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (String dayOfWeek : alarm.getSelectedDays().split(MultipleAlarmConstants.DAY_OF_WEEK_SEPERATOR)) {
                        String alarmDate = Utility.getDateFromDayOfWeek(dayOfWeek, alarm.getTime());
                        Long triggerAtMillis = Utility.getDurationInMillis(alarmDate, alarm.getTime());
                        AlarmHelper.setAlarm(context, triggerAtMillis, alarm.getLabel(), MultipleAlarmConstants.FeatureType.ALARM);
                    }
                } else {
                    for (String dayOfWeek : alarm.getSelectedDays().split(MultipleAlarmConstants.DAY_OF_WEEK_SEPERATOR)) {
                        String alarmDate = Utility.getDateFromDayOfWeek(dayOfWeek, alarm.getTime());
                        Long triggerAtMillis = Utility.getDurationInMillis(alarmDate, alarm.getTime());
                        int requestCode = Utility.getUniqueRequestCode(triggerAtMillis, MultipleAlarmConstants.FeatureType.ALARM);
                        AlarmHelper.cancelAlarm(context, requestCode);
                    }
                }

                alarm.setActive(isChecked);
                saveAlarm(alarm);
            }
        });

        return listItemView;
    }

    private void saveAlarm(Alarm currentAlarm) {
        boolean wasSuccessful = false;
        AlarmDataSource dataSource = new AlarmDataSource(context);
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
}
