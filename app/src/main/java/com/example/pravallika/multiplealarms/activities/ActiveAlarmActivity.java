package com.example.pravallika.multiplealarms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.helpers.AlarmHelper;

/**
 * Created by RitenVithlani on 4/29/17.
 */

public class ActiveAlarmActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.active_alarm_activity);

        String alarmLabel = getIntent().getStringExtra("alarmLabel");
        TextView tvAlarmLabel = (TextView) findViewById(R.id.tv_active_alarm_label);
        tvAlarmLabel.setText(null != alarmLabel ? alarmLabel : "");

        ImageView ivCancelAlarm = (ImageView) findViewById(R.id.iv_cancel_alarm);
        ivCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestCode = getIntent().getIntExtra("requestCode", 0);
                AlarmHelper.cancelAlarm(getBaseContext(), requestCode);

                //off switch --- not required

                // return to the alarm activity and remove the selected day for the alarm
                int featureId = getIntent().getIntExtra("featureId", 0);
                if (featureId == MultipleAlarmConstants.FeatureType.ALARM.id()) {
                    Intent intent = new Intent(ActiveAlarmActivity.this, AlarmActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (featureId == MultipleAlarmConstants.FeatureType.MULTIPLE_ALARM.id()) {
                    Intent intent = new Intent(ActiveAlarmActivity.this, MultipleAlarmActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ActiveAlarmActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }
}
