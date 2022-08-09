package com.example.fast_fcmnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String CHANNEL_NAME = "Emoji Party";
    private String CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널";
    private String CHANNEL_ID = "Channel Id";

    private final Map<String, Integer> NotificationType = new HashMap<String, Integer>() {
        {
            put("NORMAL", 0);
            put("EXPANDABLE", 1);
            put("CUSTOM", 3);
        }
    };

    // 토큰이 갱신되면 새로운 토큰을 서버에 전달
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.d("newtoken", s);
    }


    // 데이터 메세지가 오면 반응하는 함수
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();

        String type = data.get("type");
        String title = data.get("title");
        String message = data.get("message");

        if (NotificationType.get(type) == null) return;
        int typeId = NotificationType.get(type);

        Notification notification = createNotification(type, title, message);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(typeId, notification);
    }

    private Notification createNotification(String type, String title, String message) {
        NotificationCompat.Builder builder = getNotificationBuilder();

        builder.setSmallIcon(R.drawable.ic_baseline_notifications_active_24);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        switch (type) {
            case "NORMAL":
                break;
            case "EXPANDABLE":
                NotificationCompat.BigTextStyle big = new NotificationCompat.BigTextStyle(builder);
                big.bigText("\uD83E\uDD29 \uD83E\uDD73 \uD83D\uDE0F \uD83D\uDE12 \uD83D\uDE1E \uD83D\uDE14 \uD83D\uDE1F \uD83D\uDE15 \uD83D\uDE41 ☹️ \uD83D\uDE23 \uD83D\uDE16 \uD83D\uDE2B \uD83D\uDE29 \uD83E\uDD7A \uD83D\uDE22 \uD83D\uDE2D \uD83D\uDE24 \uD83D\uDE20 \uD83D\uDE21 \uD83E\uDD2C \uD83E\uDD2F \uD83D\uDE33 \uD83E\uDD75 \uD83E\uDD76 \uD83D\uDE31 \uD83D\uDE28 \uD83D\uDE30 \uD83D\uDE25 \uD83D\uDE13 \uD83E\uDD17 \uD83E\uDD14 \uD83E\uDD2D \uD83E\uDD2B \uD83E\uDD25 \uD83D\uDE36 \uD83D\uDE10 \uD83D\uDE11 \uD83D\uDE2C \uD83D\uDE44 \uD83D\uDE2F \uD83D\uDE26 \uD83D\uDE27 \uD83D\uDE2E \uD83D\uDE32 \uD83E\uDD71 \uD83D\uDE34 \uD83E\uDD24 \uD83D\uDE2A \uD83D\uDE35 \uD83E\uDD10 \uD83E\uDD74 \uD83E\uDD22 \uD83E\uDD2E \uD83E\uDD27 \uD83D\uDE37");
                break;
            case "CUSTOM":
//                NotificationCompat.DecoratedCustomViewStyle custom = new NotificationCompat.DecoratedCustomViewStyle();
//                builder.setCustomContentView(RemoteViews(getPackageName(), R.layout.custom_notification));
//                builder.setStyle(custom);
                break;
        }

        return builder.build();
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        return builder;
    }
}
