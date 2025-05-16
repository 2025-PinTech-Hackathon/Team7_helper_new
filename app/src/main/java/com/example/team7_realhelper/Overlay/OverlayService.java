package com.example.team7_realhelper.Overlay;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;


import androidx.annotation.Nullable;

import com.example.team7_realhelper.R;

// 백그라운드에서 실행되는 서비스
public class OverlayService extends Service {
    private WindowManager windowManager;  // 윈도우 관리하는 시스템 서비스 객체
    //private ImageView overlayIcon;   // 오버레이 띄울 아이콘
    private WindowManager.LayoutParams params;
    private OverlayIcon overlayIcon;

    private OverlayManager overlayManager;

    @Override
    public void onCreate() {
        super.onCreate();

        overlayManager = new OverlayManager(this);
        overlayManager.showIcon();

    }

/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 좌표 전달 받기
        int x = intent.getIntExtra("x", 300);
        int y = intent.getIntExtra("y", 700);

        overlayManager.showHighlightWithTooltip(x, y);

        return START_NOT_STICKY;
    }   */


    @Override
    public void onDestroy() {
        super.onDestroy();
        overlayManager.removeAll();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
