package com.example.pravallika.multiplealarms.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.activities.ReminderActivity;
import com.example.pravallika.multiplealarms.constants.MultipleAlarmConstants;

/**
 * Created by RitenVithlani on 4/24/17.
 */

public class NotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationMessage = intent.getStringExtra("notificationMessage");
        String notificationTitle = intent.getStringExtra("notificationTitle");
        int requestCode = intent.getIntExtra("requestCode", 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_alarm_white_24dp)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationMessage)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_HIGH);

        // When notification is clicked, user will be redirected using this intent
        Intent resultIntent = new Intent(context, ReminderActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ReminderActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // if request code is different then only new notification will be generated. If requestCode is same then new will overwride the old one
        mNotificationManager.notify(requestCode, mBuilder.build());

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(MultipleAlarmConstants.NOTIF_VIBRATE_IN_MILLIS);
    }
}
