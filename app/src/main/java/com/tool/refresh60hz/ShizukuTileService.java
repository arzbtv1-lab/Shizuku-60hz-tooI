package com.tool.refresh60hz;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;
import rikka.shizuku.Shizuku;

public class ShizukuTileService extends TileService {

    @Override
    public void onClick() {
        super.onClick();
        Tile tile = getQsTile();

        try {
            if (Shizuku.pingBinder()) {
                if (Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    tile.setState(Tile.STATE_ACTIVE);
                    tile.setLabel("Shizuku (Active)");
                    Toast.makeText(this, "Shizuku พร้อมใช้งานแล้ว", Toast.LENGTH_SHORT).show();
                } else {
                    Shizuku.requestPermission(1001);
                    tile.setState(Tile.STATE_INACTIVE);
                    tile.setLabel("Shizuku (ขอสิทธิ์)");
                }
            } else {
                tile.setState(Tile.STATE_UNAVAILABLE);
                tile.setLabel("Shizuku (ยังไม่รัน)");
                Toast.makeText(this, "โปรดเปิดใช้งาน Shizuku Service ในเครื่องก่อน", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            tile.setState(Tile.STATE_UNAVAILABLE);
            Toast.makeText(this, "ไม่พบบริการ Shizuku", Toast.LENGTH_SHORT).show();
        }
        tile.updateTile();
    }
                      }
