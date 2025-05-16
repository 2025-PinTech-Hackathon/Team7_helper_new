package com.example.team7_realhelper.Overlay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OverlayReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.HelperApp_Prototype.ACTION_BUTTON_CLICKED".equals(intent.getAction())) {
            int x = intent.getIntExtra("x", 0);
            int y = intent.getIntExtra("y", 0);
            String id = intent.getStringExtra("id");

            // OverlayManager에 전달
            //OverlayManager.getInstance(context).showOverlay(x, y, id);
        }
    }
}