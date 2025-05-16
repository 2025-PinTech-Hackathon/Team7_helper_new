package com.example.team7_realhelper.Overlay;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.team7_realhelper.R;

public class OverlayIcon {
    private final Context context;
    private final OverlayManager manager;
    private WindowManager windowManager;  // 윈도우 관리하는 시스템 서비스 객체
    private ImageView overlayIcon;     // 오버레이 띄울 아이콘
    private WindowManager.LayoutParams params;

    private OverlayButton overlayButton;

    public boolean firstClick=true;


    private long touchStartTime;
    private static final int CLICK_THRESHOLD = 200;

    // 생성자
    public OverlayIcon(Context context, OverlayManager manager) {
        this.context = context;
        this.manager = manager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }


    // 아이콘 오버레이띄우기
    public void show() {
        overlayIcon = new ImageView(context);
        overlayIcon.setImageResource(R.drawable.start_icon);

        // 띄울 뷰의 크기, 타입 등등 정의
        params = new WindowManager.LayoutParams(
                150, 150,    // 크기
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,     // 오버레이가 포커스 받지 않도록
                PixelFormat.TRANSLUCENT);

        // 위치 지정
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 100;
        params.y = 100;
        
        // 터치 리스너
        overlayIcon.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;
            //firstClick=true;   // 몇번째 클릭인지

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:      // 드래그 시작시
                        touchStartTime = System.currentTimeMillis();     // 시작 시간 저장

                        // 시작 위치 저장
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:    // 이동하면
                        // 움직인 거리 계산
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(overlayIcon, params);       // 위치 갱신


                        if(!firstClick){
                            manager.iconPositionChanged(params.x,params.y);   // 위치변경알림
                        }

                        return true;

                    case MotionEvent.ACTION_UP:    // 클릭 시 (눌렀다가 바로 뗌)
                        long touchDuration = System.currentTimeMillis() - touchStartTime;   // 터치 지속 시간
                        float dx = event.getRawX() - initialTouchX;
                        float dy = event.getRawY() - initialTouchY;

                        if (touchDuration < CLICK_THRESHOLD && Math.abs(dx) < 10 && Math.abs(dy) < 10) {    // 짧은 터치 시간 + 거의 안움직임
                            if(firstClick){
                                manager.showButton();   // 버튼 오버레이 요청
                                firstClick=!firstClick;
                            }else{
                                manager.removeButton();
                                firstClick=!firstClick;
                            }

                        }
                        return true;
                }
                return false;
            }
        });

        // 시작 아이콘 화면에 띄움
        windowManager.addView(overlayIcon, params);
    }

    // 띄워둔 뷰 제거
    public void remove() {
        if (overlayIcon != null) {
            windowManager.removeView(overlayIcon);
            overlayIcon = null;
        }
    }

    // 아이콘 좌표 얻어오기
    public int getX(){
        return params.x;
    }

    public int getY(){
        return params.y;
    }
}
