package com.example.team7_realhelper;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

// 백그라운드에서 실행되는 서비스
public class OverlayService extends Service {
    private WindowManager windowManager;  // 윈도우 관리하는 시스템 서비스 객체
    private ImageView overlayIcon;   // 오버레이 띄울 아이콘
    private WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        overlayIcon = new ImageView(this);
        overlayIcon.setImageResource(R.drawable.start_icon);  // 이미지 설정

        // 띄울 뷰의 크기, 타입 등등 정의
        params = new WindowManager.LayoutParams(
                150, 150,  // 크기
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,    // 오버레이가 포커스 받지 않도록
                PixelFormat.TRANSLUCENT);

        // 위치 지정
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 100;
        params.y = 100;

        // 드래그 리스너
        overlayIcon.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:   // 드래그 시작시 

                        // 시작 위치 저장
                        initialX=params.x;
                        initialY=params.y;
                        initialTouchX=event.getRawX();
                        initialTouchY=event.getRawY();
                        return true;


                    case MotionEvent.ACTION_MOVE:   // 이동하면
                        // 움직인 거리 계산
                        params.x=initialX+(int)(event.getRawX()-initialTouchX);
                        params.y=initialY+(int)(event.getRawY()-initialTouchY);
                        windowManager.updateViewLayout(overlayIcon,params);    // 위치 갱신
                        return true;
                }
                return false;
            }
        });


        // 시작 아이콘 화면에 띄움
        windowManager.addView(overlayIcon, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayIcon != null) {
            windowManager.removeView(overlayIcon);   // 띄워둔 뷰 제거
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
