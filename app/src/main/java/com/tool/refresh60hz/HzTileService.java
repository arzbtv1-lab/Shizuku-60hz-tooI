package com.tool.refresh60hz;

import android.content.Context;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;
import java.io.DataOutputStream;

public class HzTileService extends TileService {

    @Override
    public void onClick() {
        super.onClick();
        Tile tile = getQsTile();
        boolean isCurrentActive = (tile.getState() == Tile.STATE_ACTIVE);

        if (!isCurrentActive) {
            // สั่งงานยิงคำสั่ง Shell ล็อกค่า Refresh Rate ที่ 60Hz ทั้ง min และ max
            boolean success = applyDisplay60Hz(getApplicationContext());
            if (success) {
                tile.setState(Tile.STATE_ACTIVE);
                tile.setLabel("60Hz (เปิด)");
                Toast.makeText(this, "ล็อกหน้าจอที่ 60Hz เรียบร้อย", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "ต้องอนุญาตสิทธิ์ WRITE_SECURE_SETTINGS ก่อน", Toast.LENGTH_LONG).show();
            }
        } else {
            // คืนค่ากลับโหมดอัตโนมัติ
            boolean success = resetDisplayRate(getApplicationContext());
            if (success) {
                tile.setState(Tile.STATE_INACTIVE);
                tile.setLabel("60Hz Mode");
                Toast.makeText(this, "คืนค่าความถี่หน้าจอป้องกันประหยัดแบตแล้ว", Toast.LENGTH_SHORT).show();
            }
        }
        tile.updateTile();
    }

    private boolean applyDisplay60Hz(Context ctx) {
        try {
            // เขียนลงใน Settings.System โดยตรง
            Settings.System.putFloat(ctx.getContentResolver(), "peak_refresh_rate", 60.0f);
            Settings.System.putFloat(ctx.getContentResolver(), "min_refresh_rate", 60.0f);
            return true;
        } catch (Exception e) {
            // หากไม่มีสิทธิ์ ให้พยายามรันผ่าน Shell Su / ADB
            return executeShell("settings put system peak_refresh_rate 60.0 && settings put system min_refresh_rate 60.0");
        }
    }

    private boolean resetDisplayRate(Context ctx) {
        try {
            Settings.System.putFloat(ctx.getContentResolver(), "peak_refresh_rate", 0.0f);
            Settings.System.putFloat(ctx.getContentResolver(), "min_refresh_rate", 0.0f);
            return true;
        } catch (Exception e) {
            return executeShell("settings put system peak_refresh_rate 0.0 && settings put system min_refresh_rate 0.0");
        }
    }

    private boolean executeShell(String cmd) {
        try {
            Process p = Runtime.getRuntime().exec("sh");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            p.waitFor();
            return p.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }
                  }
