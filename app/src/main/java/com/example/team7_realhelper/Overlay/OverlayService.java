package com.example.team7_realhelper.Overlay;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class OverlayService extends Service {

    private static final String CHANNEL_ID = "overlay_service_channel";

    private OverlayManager overlayManager;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Overlay Service")
                .setContentText("Overlay is running")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();

        startForeground(1, notification);

        // 오버레이 매니저 초기화 및 아이콘 보여주기
        overlayManager = new OverlayManager(this);
        overlayManager.showIcon();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Overlay Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 좌표 전달 받기
        int x = intent.getIntExtra("x", -1);
        int y = intent.getIntExtra("y", -1);

        // 좌표가 유효할 때만 강조 표시 실행
        if (x != -1 && y != -1) {
            overlayManager.showHighlightWithTooltip(x, y);
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayManager != null) {
            overlayManager.removeAll();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;  // 바인딩 서비스 아님
    }
}
