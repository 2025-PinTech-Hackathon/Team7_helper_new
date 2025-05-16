package com.example.team7_realhelper.Overlay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.os.Handler;


import androidx.annotation.Nullable;

import com.example.team7_realhelper.OverlayControlReceiver;
import com.example.team7_realhelper.R;

// 백그라운드에서 실행되는 서비스
public class OverlayService extends Service {
    private WindowManager windowManager;  // 윈도우 관리하는 시스템 서비스 객체
    //private ImageView overlayIcon;   // 오버레이 띄울 아이콘
    private WindowManager.LayoutParams params;
    private OverlayIcon overlayIcon;

    private OverlayManager overlayManager;
    private OverlayControlReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();


        overlayManager = new OverlayManager(this);

        receiver = new OverlayControlReceiver(overlayManager);
        //IntentFilter filter = new IntentFilter("com.example.ACTION_REMOVE_OVERLAY");
        //registerReceiver(receiver, filter,Context.RECEIVER_NOT_EXPORTED);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, new IntentFilter("com.example.ACTION_REMOVE_OVERLAY"), Context.RECEIVER_EXPORTED);
        }*/

        registerReceiver(receiver, new IntentFilter("com.example.ACTION_REMOVE_OVERLAY"), Context.RECEIVER_EXPORTED);


        overlayManager.showIcon();

    }

    private void showHighlightsSequentially(int[] x, int[] y, int[] width, int[] height, int index) {
        if (index >= x.length || x[index] == -1 || y[index] == -1) return;

        overlayManager.showHighlightWithTooltip(x[index], y[index], width[index], height[index]);

        new Handler().postDelayed(() -> {
            showHighlightsSequentially(x, y, width, height, index + 1);
        }, 3000); // 3초 후 다음 단계 실행
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int[] x = new int[8];
        int[] y = new int[8];
        int[] width = new int[8];
        int[] height = new int[8];

        for (int i = 0; i < 8; i++) {
            x[i] = intent.getIntExtra("x" + (i + 1), -1);
            y[i] = intent.getIntExtra("y" + (i + 1), -1);
            width[i] = intent.getIntExtra("width" + (i + 1), 150);
            height[i] = intent.getIntExtra("height" + (i + 1), 150);
        }

        showHighlightsSequentially(x, y, width, height, 0);

        return START_NOT_STICKY;
    }

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
