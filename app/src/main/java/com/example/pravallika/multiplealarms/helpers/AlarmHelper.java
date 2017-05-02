package com.example.pravallika.multiplealarms.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;
import com.example.pravallika.multiplealarms.receivers.AlarmReceiver;
import com.example.pravallika.multiplealarms.utils.Utility;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.MAX_VALUE;

/**
 * Created by RitenVithlani on 4/24/17.
 */

public class AlarmHelper {

    public static void cancelAlarm(Context context, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("isAlarmOn", false);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // To stop the alarm immediately if its ringing
        context.sendBroadcast(intent);

        Log.e("request code cancel", requestCode + "");
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    public static void setAlarm(Context context, long triggerTimeInMillis, String label, MultipleAlarmConstants.FeatureType featureType) {
        int requestCode = Utility.getUniqueRequestCode(triggerTimeInMillis, featureType);
        Log.e("request code", requestCode + "");
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("isAlarmOn", true); // used to track the alarm state
        intent.putExtra("alarmLabel", label); // Can be used in the notification
        intent.putExtra("requestCode", requestCode); // Will be used to cancel the alarm later in the flow
        intent.putExtra("featureId", featureType.id()); // To determine which activity to call when user taps dismiss alarm in ActiveAlarmActivity

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(triggerTimeInMillis);
        Date d1 = c1.getTime();
        Log.i("Label: " + label + " Trigger time ", d1.toString());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeInMillis, pendingIntent);
        }
    }

    public static int getRequestCode(Long id) {
        return (int) (id % MAX_VALUE);
    }
}
