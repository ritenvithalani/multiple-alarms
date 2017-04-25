package com.example.pravallika.multiplealarms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.beans.SpecialDaysReminder;

/**
 * Created by RitenVithlani on 2/20/17.
 */

public class SpecialDaysReminderAdapter extends ArrayAdapter<SpecialDaysReminder> {

    public SpecialDaysReminderAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(null == convertView) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_spl_days_reminder, parent, false);
        }

        SpecialDaysReminder specialDaysReminder = getItem(position);

        TextView id = (TextView) listItemView.findViewById(R.id.tv_spl_rem_id);
        TextView date = (TextView) listItemView.findViewById(R.id.tv_spl_rem_date);
        TextView time = (TextView) listItemView.findViewById(R.id.tv_spl_rem_time);
        TextView label = (TextView) listItemView.findViewById(R.id.tv_spl_rem_label);
        Switch splRemToggleSwitch = (Switch) listItemView.findViewById(R.id.spl_rem_toggle_switch);

        id.setText(specialDaysReminder.getId() + "");

        if (null != specialDaysReminder.getDate() && null != date && !"".equals(specialDaysReminder.getDate())) {
            date.setText(specialDaysReminder.getDate());
        }
        else {
            date.setVisibility(View.GONE);
        }

        if (null != specialDaysReminder.getTime() && null != time && !"".equals(specialDaysReminder.getTime())) {
            time.setText(specialDaysReminder.getTime());
        }
        else {
            time.setVisibility(View.GONE);
        }

        if (null != specialDaysReminder.getLabel() && null != label && !"".equals(specialDaysReminder.getLabel())) {
            label.setText(specialDaysReminder.getLabel());
        }
        else {
            label.setVisibility(View.GONE);
        }

        splRemToggleSwitch.setChecked(null != specialDaysReminder.getActive() ? specialDaysReminder.getActive() : Boolean.TRUE);

        return listItemView;
    }
}
