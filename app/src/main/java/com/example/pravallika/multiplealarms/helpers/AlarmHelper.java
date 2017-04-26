package com.example.pravallika.multiplealarms.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.pravallika.multiplealarms.receivers.AlarmReceiver;

import static java.lang.Integer.MAX_VALUE;

/**
 * Created by RitenVithlani on 4/24/17.
 */

public class AlarmHelper {

    public static void cancelAlarm(Context context, long cancelId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int requestCode = AlarmHelper.getRequestCode(cancelId);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("isAlarmOn", false);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // To stop the alarm immediately if its ringing
        context.sendBroadcast(intent);

        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    public static void setAlarm(Context context, long alarmId, long triggerTimeInMillis, boolean isNotificationNeeded, String notificationMessage) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int requestCode = AlarmHelper.getRequestCode(alarmId);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("isAlarmOn", true);
        intent.putExtra("isNotificationNeeded", isNotificationNeeded);
        intent.putExtra("notificationMessage", notificationMessage);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);
    }

    public static int getRequestCode(Long id) {
        return (int) (id % MAX_VALUE);
    }
}
