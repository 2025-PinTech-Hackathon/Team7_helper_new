package com.example.team7_realhelper.Overlay;

import android.content.Context;

public class OverlayManager {
    private final Context context;
    private OverlayIcon overlayIcon;
    private OverlayButton overlayButton;

    public OverlayManager(Context context) {
        this.context = context;
        overlayIcon = new OverlayIcon(context, this);
        overlayButton = new OverlayButton(context, this);
    }

    public void showIcon() {
        overlayIcon.show();
    }

    // 버튼 오버레이 띄우기
    public void showButton() {
        //overlayIcon.remove();
        overlayButton.show();
    }

    public void removeButton() {
        overlayButton.remove();
    }

    public void removeAll() {
        overlayIcon.remove();
        overlayButton.remove();
    }

    public int getIconX(){
        return overlayIcon.getX();
    }

    public int getIconY(){
        return overlayIcon.getY();
    }
}
