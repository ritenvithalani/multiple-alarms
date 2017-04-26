package com.example.pravallika.multiplealarms.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pravallika.multiplealarms.services.RingtonePlayingService;

/**
 * Created by RitenVithlani on 4/24/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        boolean isAlarmOn = intent.getBooleanExtra("isAlarmOn", false);
        boolean isNotificationNeeded = intent.getBooleanExtra("isNotificationNeeded", false);
        String notificationMessage = intent.getStringExtra("notificationMessage");

        serviceIntent.putExtra("isAlarmOn", isAlarmOn);
        serviceIntent.putExtra("isNotificationNeeded", isNotificationNeeded);
        serviceIntent.putExtra("notificationMessage", notificationMessage);

        context.startService(serviceIntent);
    }
}
