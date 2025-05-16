package com.example.team7_realhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String payloadJson = intent.getStringExtra("payload");
        try {
            JSONObject payload = new JSONObject(payloadJson);
            String screenName = payload.getString("screen");
            JSONArray buttonArray = payload.getJSONArray("buttons");

            Log.d("AppB", "수신된 화면: " + screenName);
            for (int i = 0; i < buttonArray.length(); i++) {
                JSONObject btn = buttonArray.getJSONObject(i);
                String id = btn.getString("id");
                int x = btn.getInt("x");
                int y = btn.getInt("y");
                int width = btn.getInt("width");
                int height = btn.getInt("height");

                Log.d("AppB", screenName + " - 버튼[" + id + "] 위치: (" + x + ", " + y + "), 크기: " + width + "x" + height);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
