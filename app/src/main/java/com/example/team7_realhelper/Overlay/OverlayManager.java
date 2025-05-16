package com.example.team7_realhelper.Overlay;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.team7_realhelper.R;


public class OverlayManager {
    private final Context context;
    private final OverlayIcon overlayIcon;
    private final OverlayButton overlayButton;
    private final WindowManager windowManager;

    private View highlightView;
    private TextView tooltipView;

    public OverlayManager(Context context) {
        this.context = context;
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.overlayIcon = new OverlayIcon(context, this);
        this.overlayButton = new OverlayButton(context, this);
    }

    public void showIcon() {
        overlayIcon.show();
    }

    public void showButton() {
        overlayButton.show();
    }

    public void removeButton() {
        overlayButton.remove();
    }

    public void removeAll() {
        overlayIcon.remove();
        overlayButton.remove();

        if (highlightView != null) {
            windowManager.removeView(highlightView);
            highlightView = null;
        }
        if (tooltipView != null) {
            windowManager.removeView(tooltipView);
            tooltipView = null;
        }
    }

    public int getIconX() {
        return overlayIcon.getX();
    }

    public int getIconY() {
        return overlayIcon.getY();
    }

    public  void setFirstClick(boolean bool){
        overlayIcon.firstClick=bool;
    }

    public void iconPositionChanged(int x, int y) {
        overlayButton.updatePosition(x, y + 150);
    }


    public void showHighlightWithTooltip(int x, int y) {
        highlightView = new View(context);
        highlightView.setBackgroundResource(R.drawable.highlight_border);

        // 화면 중앙 높이 계산
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        int highlightX = 40;
        int highlightY = screenHeight / 2 - 930;

        WindowManager.LayoutParams highlightParams = new WindowManager.LayoutParams(
                150, 150,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        highlightParams.gravity = Gravity.TOP | Gravity.LEFT;
        highlightParams.x = highlightX;
        highlightParams.y = highlightY;

        windowManager.addView(highlightView, highlightParams);

        tooltipView = new TextView(context);
        tooltipView.setText("여기를 눌러보세요");
        tooltipView.setTextColor(0xFFFFFFFF); // white
        tooltipView.setBackgroundResource(R.drawable.tooltip_bg);
        tooltipView.setPadding(24, 16, 24, 16);
        tooltipView.setTextSize(14f);

        WindowManager.LayoutParams tooltipParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        tooltipParams.gravity = Gravity.TOP | Gravity.LEFT;
        tooltipParams.x = highlightX;
        tooltipParams.y = highlightY - 100;

        windowManager.addView(tooltipView, tooltipParams);

        // 깜빡임 애니메이션
        ObjectAnimator blinkAnim = ObjectAnimator.ofFloat(highlightView, "alpha", 1f, 0f);
        blinkAnim.setDuration(700);
        blinkAnim.setRepeatMode(ValueAnimator.REVERSE);
        blinkAnim.setRepeatCount(ValueAnimator.INFINITE);
        blinkAnim.start();

        new Handler().postDelayed(() -> {
            if (highlightView != null) {
                blinkAnim.cancel();
                windowManager.removeView(highlightView);
                highlightView = null;
            }
            if (tooltipView != null) {
                windowManager.removeView(tooltipView);
                tooltipView = null;
            }
        }, 10000);
    }
}
