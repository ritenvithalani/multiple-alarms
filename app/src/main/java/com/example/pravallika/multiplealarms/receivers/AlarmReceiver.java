package com.example.pravallika.multiplealarms.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pravallika.multiplealarms.activities.ActiveAlarmActivity;
import com.example.pravallika.multiplealarms.services.RingtonePlayingService;

/**
 * Created by RitenVithlani on 4/24/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        boolean isAlarmOn = intent.getBooleanExtra("isAlarmOn", false);
        String alarmLabel = intent.getStringExtra("alarmLabel");
        int requestCode = intent.getIntExtra("requestCode", 0);

        serviceIntent.putExtra("isAlarmOn", isAlarmOn);
        serviceIntent.putExtra("alarmLabel", alarmLabel);
        context.startService(serviceIntent);

        Intent activeAlarmIntent = new Intent(context, ActiveAlarmActivity.class);
        activeAlarmIntent.putExtra("requestCode", requestCode);
        activeAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activeAlarmIntent);
    }
}
