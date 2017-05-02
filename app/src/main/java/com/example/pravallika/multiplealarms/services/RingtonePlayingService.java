package com.example.pravallika.multiplealarms.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
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
    boolean isAlarmOn, isNotificationNeeded, isAlarmDismissed;
    String notificationMessage;
    Uri notification;
    private MediaPlayer player;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notificationId;
    private RemoteViews remoteViews;
    //private Timer timer;

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

        if (null == player) {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            player = MediaPlayer.create(this, notification);
        }

        if (isAlarmOn && null != player && !player.isPlaying()) {
            player.setLooping(true);
            player.start();

            isAlarmOn = false;

            if (isNotificationNeeded) {
                //createNotification();
            }

            /*timer = new Timer();
            stopPlayerAfter(MultipleAlarmConstants.RINGTONE_DURATION_IN_MILLIS);*/

        } else if (!isAlarmOn && null != player && player.isPlaying()) {
            player.stop();
            player.reset();
            player.release();
            player = null;
            isAlarmOn = false;
        }
        return START_STICKY;
    }

    /*private void stopPlayerAfter(int timeout) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (null != player && player.isPlaying()) {
                    player.stop();
                    player.reset();
                    player.release();
                    player = null;
                }
            }
        }, timeout);
        *//*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != player && player.isPlaying()) {
                    player.stop();
                    player.reset();
                    player.release();
                    player = null;
                }
            }
        }, timeout);*//*
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != player) {
            player.stop();
            player.reset();
            player.release();
            player = null;
        }
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
