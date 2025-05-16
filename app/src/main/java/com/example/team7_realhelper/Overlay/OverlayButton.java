package com.example.team7_realhelper.Overlay;


import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.WindowManager;
import android.widget.Button;

import java.nio.Buffer;

public class OverlayButton {
    private  final Context context;
    private final OverlayManager manager;
    private final WindowManager windowManager;
    private Button buttonView;
    private WindowManager.LayoutParams params;

    // 생성자
    public OverlayButton(Context context, OverlayManager manager) {
        this.context = context;
        this.manager = manager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void show(){
        buttonView = new Button(context);
        buttonView.setText("오버레이 버튼");

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        windowManager.addView(buttonView, params);

    }


    public void remove() {
        if (buttonView != null) {
            windowManager.removeView(buttonView);
            buttonView = null;
        }
    }
}
