package com.example.pravallika.multiplealarms.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.pravallika.multiplealarms.activities.ActiveAlarmActivity;
import com.example.pravallika.multiplealarms.services.RingtonePlayingService;

/**
 * Created by RitenVithlani on 4/24/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    public static final String DISMISS = "Dismiss";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isAlarmOn = intent.getBooleanExtra("isAlarmOn", false);
        String alarmLabel = intent.getStringExtra("alarmLabel");
        int requestCode = intent.getIntExtra("requestCode", 0);
        int featureId = intent.getIntExtra("featureId", 0);

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("isAlarmOn", isAlarmOn);
        serviceIntent.putExtra("alarmLabel", alarmLabel);
        context.startService(serviceIntent);

        Log.e("Label: " + alarmLabel + " request code receiver: ", requestCode + "");

        if (isAlarmOn) {
            Intent activeAlarmIntent = new Intent(context, ActiveAlarmActivity.class);
            activeAlarmIntent.putExtra("requestCode", requestCode);
            activeAlarmIntent.putExtra("featureId", featureId);
            activeAlarmIntent.putExtra("alarmLabel", alarmLabel);
            activeAlarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activeAlarmIntent);
        }

        /*NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_action_access_alarm)
                        .setContentTitle("Alarm")
                        .setContentText(alarmLabel)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH);

        // When notification is clicked, user will be redirected using this intent
        Intent resultIntent = new Intent(context, ActiveAlarmActivity.class);
        resultIntent.putExtra("isAlarmOn", isAlarmOn);
        resultIntent.putExtra("requestCode", requestCode);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // if request code is different then only new notification will be generated. If requestCode is same then new will overwride the old one
        mNotificationManager.notify(requestCode, mBuilder.build());

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(MultipleAlarmConstants.NOTIF_VIBRATE_IN_MILLIS);*/
    }
}
