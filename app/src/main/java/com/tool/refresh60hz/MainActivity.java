package com.tool.refresh60hz;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // สร้าง Layout แบบย่อด้วยโปรแกรม
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        tvStatus = new TextView(this);
        tvStatus.setText("สถานะสิทธิ์ระบบ: กำลังตรวจสอบ...");
        tvStatus.setTextSize(18f);

        Button btnApply60 = new Button(this);
        btnApply60.setText("เปิดใช้งาน 60Hz ตอนนี้");
        btnApply60.setOnClickListener(v -> {
            try {
                Settings.System.putFloat(getContentResolver(), "peak_refresh_rate", 60.0f);
                Settings.System.putFloat(getContentResolver(), "min_refresh_rate", 60.0f);
                Toast.makeText(this, "ตั้งค่า 60Hz เรียบร้อย!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "โปรดให้สิทธิ์ผ่าน ADB ก่อนใช้งาน", Toast.LENGTH_LONG).show();
            }
        });

        layout.addView(tvStatus);
        layout.addView(btnApply60);
        setContentView(layout);

        checkPermission();
    }

    private void checkPermission() {
        boolean hasPermission = checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == PackageManager.PERMISSION_GRANTED;
        if (hasPermission) {
            tvStatus.setText("สถานะสิทธิ์ระบบ: พร้อมใช้งาน (WRITE_SECURE_SETTINGS)");
        } else {
            tvStatus.setText("สถานะสิทธิ์ระบบ: ยังไม่ได้สิทธิ์\nรันคำสั่งนี้ใน ADB:\nadb shell pm grant " + getPackageName() + " android.permission.WRITE_SECURE_SETTINGS");
        }
    }
                  }
