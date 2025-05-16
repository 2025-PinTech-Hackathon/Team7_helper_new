package com.example.team7_realhelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.team7_realhelper.Overlay.OverlayService;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable checkRunnable;
    private static final String TARGET_PACKAGE_NAME = "com.example.helperapp_hackathon_team7"; // 실제 targetapp 패키지명 입력

    private static final int REQUEST_OVERLAY_PERMISSION = 1000;
    private static final int REQUEST_USAGE_STATS_PERMISSION = 1001;

    private UsageStatsHelper usageStatsHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usageStatsHelper = new UsageStatsHelper(this);

        if (!Settings.canDrawOverlays(this)) {
            // 오버레이 권한 없으면 요청
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
        } else if (!usageStatsHelper.hasUsageStatsPermission()) {
            // 사용량 접근 권한 없으면 요청
            requestUsageStatsPermission();
        } else {
            startCheckingForegroundApp();
        }
    }

    private void requestUsageStatsPermission() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivityForResult(intent, REQUEST_USAGE_STATS_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                if (!usageStatsHelper.hasUsageStatsPermission()) {
                    requestUsageStatsPermission();
                } else {
                    startCheckingForegroundApp();


                }
            } else {
                Log.e("MainActivity", "오버레이 권한이 필요합니다.");
                // 필요하면 권한 없을 때 안내 다이얼로그 띄우기 or 종료 처리
            }
        } else if (requestCode == REQUEST_USAGE_STATS_PERMISSION) {
            if (usageStatsHelper.hasUsageStatsPermission()) {
                startCheckingForegroundApp();
            } else {
                Log.e("MainActivity", "사용량 접근 권한이 필요합니다.");
                // 권한 없을 때 처리
            }
        }
    }

    private void startCheckingForegroundApp() {
        checkRunnable = new Runnable() {
            @Override
            public void run() {
                String foregroundApp = usageStatsHelper.getForegroundAppPackageName();
                Log.d("MainActivity", "Foreground app: " + foregroundApp);

                if (TARGET_PACKAGE_NAME.equals(foregroundApp)) {
                    Log.d("MainActivity", "Target app detected! Starting OverlayService...");
                    Intent serviceIntent = new Intent(MainActivity.this, OverlayService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);


                        finish();
                    }
                } else {
                    // 타겟 앱이 아닐 경우 서비스 종료 (필요하면)
                    Intent serviceIntent = new Intent(MainActivity.this, OverlayService.class);
                    stopService(serviceIntent);
                }

                handler.postDelayed(this, 1000);  // 3초마다 체크
            }
        };

        handler.post(checkRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && checkRunnable != null) {
            handler.removeCallbacks(checkRunnable);
        }
        // 앱 종료 시 서비스도 같이 종료
        Intent serviceIntent = new Intent(MainActivity.this, OverlayService.class);
        stopService(serviceIntent);
    }
}
