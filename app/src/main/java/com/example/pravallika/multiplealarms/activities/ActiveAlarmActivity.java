package com.example.pravallika.multiplealarms.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.helpers.AlarmHelper;
import com.example.pravallika.multiplealarms.receivers.AlarmReceiver;

/**
 * Created by RitenVithlani on 4/29/17.
 */

public class ActiveAlarmActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_active_alarm);

        String alarmLabel = getIntent().getStringExtra("alarmLabel");
        TextView tvAlarmLabel = (TextView) findViewById(R.id.tv_active_alarm_label);
        tvAlarmLabel.setText(null != alarmLabel ? alarmLabel : "");

        LinearLayout llDismissAlarm = (LinearLayout) findViewById(R.id.ll_dismiss_alarm);
        llDismissAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestCode = getIntent().getIntExtra("requestCode", 0);
                AlarmHelper.cancelAlarm(getBaseContext(), requestCode);

                redirectToConcernedActivity();
            }
        });

        LinearLayout llSnoozeAlarm = (LinearLayout) findViewById(R.id.ll_snooze_alarm);
        llSnoozeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestCode = getIntent().getIntExtra("requestCode", 0);
                String alarmLabel = getIntent().getStringExtra("alarmLabel");
                int featureId = getIntent().getIntExtra("featureId", 0);
                AlarmHelper.cancelAlarm(getBaseContext(), requestCode);

                Intent intent = new Intent(ActiveAlarmActivity.this, AlarmReceiver.class);
                intent.putExtra("isAlarmOn", true);
                intent.putExtra("alarmLabel", alarmLabel);
                intent.putExtra("featureId", featureId);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(ActiveAlarmActivity.this, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Log.i("ActiveAlarmActivity", "Alarm snooze clicked");
                AlarmManager alarmManager = (AlarmManager) ActiveAlarmActivity.this.getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    // 5min snooze
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + MultipleAlarmConstants.SNOOZE_TIME_IN_MILLIS, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + MultipleAlarmConstants.SNOOZE_TIME_IN_MILLIS, pendingIntent);
                }

                redirectToConcernedActivity();
            }
        });

    }

    private void redirectToConcernedActivity() {
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

    @Override
    public void onBackPressed() {
    }
}
