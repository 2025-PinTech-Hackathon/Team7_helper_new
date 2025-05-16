package com.example.team7_realhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.team7_realhelper.Overlay.OverlayManager;
import com.example.team7_realhelper.Overlay.OverlayService;

public class OverlayControlReceiver extends BroadcastReceiver {
    private OverlayManager overlayManager;

    public OverlayControlReceiver() {
    }


    public OverlayControlReceiver(OverlayManager overlayManager) {
        this.overlayManager = overlayManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Log.d("OverlayControlReceiver", "Broadcast received! Action: " + action);

        if ("com.example.ACTION_REMOVE_OVERLAY".equals(action)) {
            Log.d("OverlayControlReceiver", "ACTION_REMOVE_OVERLAY received");

            if (overlayManager != null) {
                //overlayManager.removeAll();
                Intent stopIntent = new Intent(context, OverlayService.class);
                boolean stopped = context.stopService(stopIntent);
                Log.d("OverlayControlReceiver", "OverlayManager.removeAll() called");
            } else {
                Log.e("OverlayControlReceiver", "overlayManager is null!");
            }
        }
    }
}


