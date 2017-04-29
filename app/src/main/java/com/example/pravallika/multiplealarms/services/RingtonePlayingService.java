package com.example.pravallika.multiplealarms.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.pravallika.multiplealarms.R;
import com.example.pravallika.multiplealarms.fragments.SpecialDaysReminderFragment;

/**
 * Created by RitenVithlani on 4/25/17.
 */

public class RingtonePlayingService extends Service {
    MediaPlayer player;
    boolean isAlarmOn, isNotificationNeeded;
    String notificationMessage;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notificationId;
    private RemoteViews remoteViews;
    private int DELAY_IN_MILLIS = 10 * 1000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isAlarmOn = intent.getBooleanExtra("isAlarmOn", false);
        isNotificationNeeded = intent.getBooleanExtra("isNotificationNeeded", false);
        notificationMessage = intent.getStringExtra("notificationMessage");

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        player = MediaPlayer.create(this, notification);

        if (isAlarmOn && !player.isPlaying()) {
            player.setLooping(true);
            player.start();

            isAlarmOn = false;

            if (isNotificationNeeded) {
                createNotification();
            }

            stopPlayerAfter(DELAY_IN_MILLIS);

        } else if (!isAlarmOn && player.isPlaying()) {
            player.stop();
            player.reset();

            isAlarmOn = false;
        }
        return START_NOT_STICKY;
    }

    private void stopPlayerAfter(int timeout) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (player.isPlaying()) {
                    player.stop();
                    player.reset();
                }
            }
        }, timeout);
    }

    private void createNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);

        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_spl_reminder);
        remoteViews.setImageViewResource(R.id.icon_spl_rem_notif, R.drawable.ic_action_access_alarm);
        remoteViews.setTextViewText(R.id.tv_spl_rem_notif_message, notificationMessage);

        notificationId = (int) System.currentTimeMillis();

        /*Intent buttonIntent = new Intent("spl_rem_notif_off_click");
        buttonIntent.putExtra("id", notificationId);
        PendingIntent buttonPendingEvent = PendingIntent.getBroadcast(getApplicationContext(), notificationId,buttonIntent,0);

        remoteViews.setOnClickPendingIntent(R.id.tv_spl_rem_notif_view, buttonPendingEvent);*/

        Intent notificationIntent = new Intent(getApplicationContext(), SpecialDaysReminderFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        builder.setSmallIcon(R.drawable.ic_action_access_alarm)
                .setAutoCancel(true)
                .setCustomBigContentView(remoteViews)
                .setContentIntent(pendingIntent);

        notificationManager.notify(notificationId, builder.build());
    }

}
