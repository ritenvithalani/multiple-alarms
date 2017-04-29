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
import com.example.pravallika.multiplealarms.beans.MultipleAlarm;
import com.example.pravallika.multiplealarms.utils.Utility;

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
            selectedDays.setText(multipleAlarm.getSelectedDays());
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
                /*if (isChecked) {
                    for (String dayOfWeek : multipleAlarm.getSelectedDays().split(DAY_OF_WEEK_SEPERATOR)) {
                        String alarmDate = Utility.getDateFromDayOfWeek(dayOfWeek, multipleAlarm.getTime());
                        Long triggerAtMillis = Utility.getDurationInMillis(alarmDate, multipleAlarm.getTime());
                        AlarmHelper.setAlarm(context, multipleAlarm.getId(), triggerAtMillis, false, "");
                    }
                } else {
                    AlarmHelper.cancelAlarm(context, multipleAlarm.getId());
                }*/
            }
        });

        return listItemView;
    }
}
