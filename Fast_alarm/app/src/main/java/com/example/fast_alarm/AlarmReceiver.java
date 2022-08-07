package com.example.fast_alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private final int NOTIFICATION_ID = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = getNotificationBuilder(context);

        builder.setContentTitle("알람");
        builder.setContentText("일어날 시간입니다.");
        builder.setSmallIcon(R.drawable.ic_baseline_access_alarm_24);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }

    private final String CHANNEL_NAME = "Alarm Channel";
    private final String CHANNEL_DESCRIPTION = "Alarm 송출을 위한 채널";
    private final String CHANNEL_ID = "Channel Id";

    private NotificationCompat.Builder getNotificationBuilder(Context context) {
        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        return builder;
    }
}
