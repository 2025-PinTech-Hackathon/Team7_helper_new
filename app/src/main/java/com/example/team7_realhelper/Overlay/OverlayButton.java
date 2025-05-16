package com.example.team7_realhelper.Overlay;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;

import com.example.team7_realhelper.R;
import com.example.team7_realhelper.chatbot.ChatbotCallback;
import com.example.team7_realhelper.chatbot.ChatbotService;
import com.example.team7_realhelper.chatbot.VoiceListener;
import com.example.team7_realhelper.chatbot.VoiceService;

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

    //챗봇 연결 위한 클래스
    private VoiceService voiceService;
    //어떤 기능을 갈지 저장하는 string
    public String fuc = "";

    // 생성자
    public OverlayButton(Context context, OverlayManager manager) {
        this.context = context;
        this.manager = manager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        voiceService = new VoiceService(context, new VoiceListener() {
            @Override
            public void onSpeechResult(String result) {
                Log.d("VC","결과 도출");
                // 👉 여기서 DialogflowClient.sendTextRequest(...) 호출 가능

                ChatbotService.sendMessageToChatbot(context, result, new ChatbotCallback() {
                    @Override
                    public void onResponse(String response) {
                        fuc = response;
                        Log.d("VC", fuc);

                        if(fuc.equals("송금")){
                            Log.d("VC", "1");

                            remove();
                            manager.setFirstClick(true);

                            Intent intent = new Intent(context, OverlayService.class);
                            intent.putExtra("x1", 870);  // 1단계 좌표 (송금)
                            intent.putExtra("y1", 400);
                            intent.putExtra("width1", 150);
                            intent.putExtra("height1", 150);

                            intent.putExtra("x2", 480);  // 2단계 좌표 (계좌번호 입력)
                            intent.putExtra("y2", -640);
                            intent.putExtra("width2", 500);
                            intent.putExtra("height2", 120);

                            intent.putExtra("x3", 100);  // 3단계 좌표 (은행/증권사)
                            intent.putExtra("y3", 540);
                            intent.putExtra("width3", 500);
                            intent.putExtra("height3", 120);

                            intent.putExtra("x4", 60);  // 4단계 좌표 (카카오뱅크)
                            intent.putExtra("y4", 250);
                            intent.putExtra("width4", 220);
                            intent.putExtra("height4", 220);

                            intent.putExtra("x5", 100);  // 5단계 좌표 (확인)
                            intent.putExtra("y5", -575);
                            intent.putExtra("width5", 850);
                            intent.putExtra("height5", 150);

                            intent.putExtra("x6", 120);  // 6단계 좌표 (확인)
                            intent.putExtra("y6", -630);
                            intent.putExtra("width6", 850);
                            intent.putExtra("height6", 150);

                            intent.putExtra("x7", 430);  // 7단계 좌표 (보내기)
                            intent.putExtra("y7", -620);
                            intent.putExtra("width7", 600);
                            intent.putExtra("height7", 150);

                            intent.putExtra("x8", 430);  // 8단계 좌표 (확인)
                            intent.putExtra("y8", -620);
                            intent.putExtra("width8", 600);
                            intent.putExtra("height8", 150);

                            context.startService(intent);
                        }
                        else if(fuc.equals("QR결제")){
                            Log.d("VC", "2");

                            remove();
                            manager.setFirstClick(true);

                            Intent intent = new Intent(context, OverlayService.class);
                            intent.putExtra("x1", 85);
                            intent.putExtra("y1", 140);
                            intent.putExtra("width1", 120);
                            intent.putExtra("height1", 120);

                            context.startService(intent);
                        }
                        else if(fuc.equals("청구서")){
                            Log.d("VC", "3");
                        }
                        else{ //fuc == "X"
                            fuc = "error";
                            Log.d("VC", "4");

                        }
                    }

                    @Override
                    public void onError(String error) {
                        fuc = "error";

                    }
                });
            }

            @Override
            public void onSpeechError(String error) {
                Log.d("VC","에러 발생: " + error);

            }
        });
    }

    public void show(){
        sendBtn = new Button(context);
        qrBtn=new Button(context);
        voiceBtn=new Button(context);

        // 텍스트 설정
        sendBtn.setText("송금");
        qrBtn.setText("큐알 결제");
        voiceBtn.setText("음성");

        sendBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        qrBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        voiceBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        // 버튼 디자인 설정
        sendBtn.setBackgroundResource(R.drawable.custom_button);
        qrBtn.setBackgroundResource(R.drawable.custom_button);
        voiceBtn.setBackgroundResource(R.drawable.custom_button);

        sendBtn.setMinHeight(0);
        sendBtn.setMinWidth(0);
        sendBtn.setPadding(0,0,0,0);
        qrBtn.setMinHeight(0);
        qrBtn.setMinWidth(0);
        qrBtn.setPadding(0,0,0,0);
        voiceBtn.setMinHeight(0);
        voiceBtn.setMinWidth(0);
        voiceBtn.setPadding(0,0,0,0);

        int baseX = manager.getIconX()-40;  // 좌측 위치 (x좌표)
        int baseY = manager.getIconY()+150; // 첫 번째 버튼의 y좌표 시작 위치
        int buttonHeight = 80; // 버튼 높이 (LayoutParams 높이와 동일)
        int buttonWidth=250;

        sendParams = new WindowManager.LayoutParams(
                //WindowManager.LayoutParams.WRAP_CONTENT,
                //WindowManager.LayoutParams.WRAP_CONTENT,
                buttonWidth,buttonHeight,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        sendParams.gravity = Gravity.TOP | Gravity.LEFT;
        sendParams.x = baseX;
        sendParams.y = baseY;

        qrParams = new WindowManager.LayoutParams(
                buttonWidth,buttonHeight,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        qrParams.gravity = Gravity.TOP | Gravity.LEFT;
        qrParams.x = baseX;
        qrParams.y = baseY + buttonHeight; // 첫 번째 버튼 아래

        voiceParams = new WindowManager.LayoutParams(
                buttonWidth,buttonHeight,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        voiceParams.gravity = Gravity.TOP | Gravity.LEFT;
        voiceParams.x = baseX;
        voiceParams.y = baseY + buttonHeight * 2; // 두 번째 버튼 아래




        // 버튼 클릭 이벤트
        // 송금 버튼 클릭 시
        sendBtn.setOnClickListener(v -> {
            Log.d("BT", "송금");
            remove();
            manager.setFirstClick(true);

            Intent intent = new Intent(context, OverlayService.class);
            intent.putExtra("x1", 870);  // 1단계 좌표 (송금)
            intent.putExtra("y1", 400);
            intent.putExtra("width1", 150);
            intent.putExtra("height1", 150);

            intent.putExtra("x2", 480);  // 2단계 좌표 (계좌번호 입력)
            intent.putExtra("y2", -640);
            intent.putExtra("width2", 500);
            intent.putExtra("height2", 120);

            intent.putExtra("x3", 100);  // 3단계 좌표 (은행/증권사)
            intent.putExtra("y3", 540);
            intent.putExtra("width3", 500);
            intent.putExtra("height3", 120);

            intent.putExtra("x4", 60);  // 4단계 좌표 (카카오뱅크)
            intent.putExtra("y4", 250);
            intent.putExtra("width4", 220);
            intent.putExtra("height4", 220);

            intent.putExtra("x5", 100);  // 5단계 좌표 (확인)
            intent.putExtra("y5", -575);
            intent.putExtra("width5", 850);
            intent.putExtra("height5", 150);

            intent.putExtra("x6", 120);  // 6단계 좌표 (확인)
            intent.putExtra("y6", -630);
            intent.putExtra("width6", 850);
            intent.putExtra("height6", 150);

            intent.putExtra("x7", 430);  // 7단계 좌표 (보내기)
            intent.putExtra("y7", -620);
            intent.putExtra("width7", 600);
            intent.putExtra("height7", 150);

            intent.putExtra("x8", 430);  // 8단계 좌표 (확인)
            intent.putExtra("y8", -620);
            intent.putExtra("width8", 600);
            intent.putExtra("height8", 150);

            context.startService(intent);
        });

        // qr버튼 클릭 시
        qrBtn.setOnClickListener(v -> {
            Log.d("BT", "QR");
            remove();
            manager.setFirstClick(true);

            Intent intent = new Intent(context, OverlayService.class);
            intent.putExtra("x1", 85);
            intent.putExtra("y1", 140);
            intent.putExtra("width1", 120);
            intent.putExtra("height1", 120);

            context.startService(intent);

        });

        // 음성 버튼 클릭 시
        voiceBtn.setOnClickListener(v -> {
            voiceService.startListening();

            remove();
            manager.setFirstClick(true);

            Intent intent = new Intent(context, OverlayService.class);
            intent.putExtra("x", 80);  // 음성 버튼에 맞는 좌표로 수정
            intent.putExtra("y", 700); // 필요시 조정
            context.startService(intent);
        });


        windowManager.addView(sendBtn, sendParams);
        windowManager.addView(qrBtn,qrParams);
        windowManager.addView(voiceBtn,voiceParams);
    }

    public void updatePosition(int x,int y){
        int buttonHeight = 80;
        sendParams.x=x-40;
        sendParams.y=y;
        windowManager.updateViewLayout(sendBtn,sendParams);

        qrParams.x=x-40;
        qrParams.y=y+buttonHeight;
        windowManager.updateViewLayout(qrBtn,qrParams);

        voiceParams.x=x-40;
        voiceParams.y=y+buttonHeight*2;
        windowManager.updateViewLayout(voiceBtn,voiceParams);

    }


    public void remove() {
        if (sendBtn != null) {
            windowManager.removeView(sendBtn);
            sendBtn = null;
        }

        if (qrBtn != null) {
            windowManager.removeView(qrBtn);
            qrBtn = null;
        }

        if (voiceBtn != null) {
            windowManager.removeView(voiceBtn);
            voiceBtn = null;
        }
    }
}
