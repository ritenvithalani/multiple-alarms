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
import com.example.pravallika.multiplealarms.helpers.Utility;

/**
 * Created by RitenVithlani on 4/26/17.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {

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
        Switch alarmToggleSwitch = (Switch) listItemView.findViewById(R.id.alarm_toggle_switch);

        id.setText(alarm.getId() + "");
        String formattedTime = Utility.formatMinute(alarm.getTime());
        time.setText(formattedTime);

        if (null != alarm.getLabel() && !"".equals(alarm.getLabel())) {
            label.setText(alarm.getLabel());
        } else {
            label.setVisibility(View.GONE);
        }

        if (null != alarm.getSelectedDays() && !"".equals(alarm.getSelectedDays())) {
            selectedDays.setText(alarm.getSelectedDays());
        } else {
            selectedDays.setVisibility(View.GONE);
        }

        alarmToggleSwitch.setChecked(null != alarm.getActive() ? alarm.getActive() : Boolean.TRUE);

        alarmToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*if (isChecked) {
                    Long triggerAtMillis = Utility.getDurationInMillis(alarm.getDate(), alarm.getTime());
                    AlarmHelper.setAlarm(context, alarm.getId(), triggerAtMillis, true, alarm.getLabel());
                } else {
                    AlarmHelper.cancelAlarm(context, alarm.getId());
                }*/
            }
        });

        return listItemView;
    }
}
