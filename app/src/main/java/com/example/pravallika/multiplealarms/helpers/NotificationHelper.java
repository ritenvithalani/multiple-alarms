package com.example.pravallika.multiplealarms.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.receivers.NotificationReceiver;
import com.example.pravallika.multiplealarms.utils.Utility;

/**
 * Created by RitenVithlani on 4/24/17.
 */

public class NotificationHelper {

    public static void cancelNotification(Context context, long triggerTimeInMillis, MultipleAlarmConstants.FeatureType featureType) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int requestCode = Utility.getUniqueRequestCode(triggerTimeInMillis, featureType);

        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    public static void createNotification(Context context, long triggerTimeInMillis, String notificationMessage, MultipleAlarmConstants.FeatureType featureType) {
        // If you start pending intent on the time that has already elapsed, then it triggers immediately. Hence this condition
        if (triggerTimeInMillis > System.currentTimeMillis()) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            int requestCode = Utility.getUniqueRequestCode(triggerTimeInMillis, featureType);

            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("notificationMessage", notificationMessage);
            intent.putExtra("requestCode", requestCode);
            intent.putExtra("notificationTitle", featureType.title());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);
        }
    }
}
