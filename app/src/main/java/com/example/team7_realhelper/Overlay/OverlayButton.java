package com.example.team7_realhelper.Overlay;


import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;

import java.nio.Buffer;

public class OverlayButton {
    private  final Context context;
    private final OverlayManager manager;
    private final WindowManager windowManager;
    // 송금 버튼
    private Button sendBtn;
    private WindowManager.LayoutParams sendParams;

    // 큐알 결제 버튼
    private Button qrBtn;
    private WindowManager.LayoutParams qrParams;

    // 음성 버튼
    private Button voiceBtn;
    private WindowManager.LayoutParams voiceParams;

    // 생성자
    public OverlayButton(Context context, OverlayManager manager) {
        this.context = context;
        this.manager = manager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void show(){
        sendBtn = new Button(context);
        sendBtn.setText("송금");

        qrBtn=new Button(context);
        qrBtn.setText("큐알 결제");

        voiceBtn=new Button(context);
        voiceBtn.setText("음성");

        int baseX = 100; // 좌측 위치 (x좌표)
        int baseY = 100; // 첫 번째 버튼의 y좌표 시작 위치
        int buttonHeight = 100; // 버튼 높이 (LayoutParams 높이와 동일)

        sendParams = new WindowManager.LayoutParams(
                //WindowManager.LayoutParams.WRAP_CONTENT,
                //WindowManager.LayoutParams.WRAP_CONTENT,
                150,100,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        sendParams.gravity = Gravity.TOP | Gravity.LEFT;
        sendParams.x = baseX;
        sendParams.y = baseY;

        qrParams = new WindowManager.LayoutParams(
                150,100,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        qrParams.gravity = Gravity.TOP | Gravity.LEFT;
        qrParams.x = baseX;
        qrParams.y = baseY + buttonHeight; // 첫 번째 버튼 아래

        voiceParams = new WindowManager.LayoutParams(
                150,100,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        voiceParams.gravity = Gravity.TOP | Gravity.LEFT;
        voiceParams.x = baseX;
        voiceParams.y = baseY + buttonHeight * 2; // 두 번째 버튼 아래


        windowManager.addView(sendBtn, sendParams);
        windowManager.addView(qrBtn,qrParams);
        windowManager.addView(voiceBtn,voiceParams);
    }


    public void remove() {
        if (sendBtn != null) {
            windowManager.removeView(sendBtn);
            sendBtn = null;
        }
    }
}
