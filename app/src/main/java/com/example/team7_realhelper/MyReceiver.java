package com.example.team7_realhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.team7_realhelper.Overlay.OverlayService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;

        String action = intent.getAction();
        if ("com.HelperApp_Prototype.ACTION_BUTTON_INFO".equals(action)) {
            String payload = intent.getStringExtra("payload");
            if (payload == null) {
                Log.e(TAG, "payload is null");
                return;
            }

            try {
                // JSON에서 필요한 정보를 꺼내 OverlayService로 전달
                JSONObject payloadJson = new JSONObject(payload);
                JSONArray buttons = payloadJson.optJSONArray("buttons");
                if (buttons == null || buttons.length() == 0) {
                    Log.e(TAG, "buttons 배열이 비었거나 없음");
                    return;
                }

                JSONObject firstButton = buttons.getJSONObject(0);
                int x = firstButton.optInt("x", 100);
                int y = firstButton.optInt("y", 100);
                int width = firstButton.optInt("width", 200);
                int height = firstButton.optInt("height", 50);

                Intent serviceIntent = new Intent(context, com.example.team7_realhelper.Overlay.OverlayService.class);
                serviceIntent.putExtra("command", "highlight_send");
                serviceIntent.putExtra("x", x);
                serviceIntent.putExtra("y", y);
                serviceIntent.putExtra("width", width);
                serviceIntent.putExtra("height", height);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent);
                } else {
                    context.startService(serviceIntent);
                }

            } catch (Exception e) {
                Log.e(TAG, "JSON 파싱 실패", e);
            }
        }
    }
}
