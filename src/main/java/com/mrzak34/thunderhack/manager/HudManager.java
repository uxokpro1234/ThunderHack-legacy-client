package com.mrzak34.thunderhack.manager;

import com.mrzak34.thunderhack.settings.Setting;

import java.util.ArrayList;
import java.util.List;

public class HudManager {
    public String name;
    public int posX;
    public int posY;
    public boolean toggle;
    public boolean showSettings;
    public List<Setting> settings = new ArrayList<Setting>();

    public HudManager(String name, int posX, int posY,boolean toggle, boolean showSettings) {
        this.name = name;
        this.posX = 2;
        this.posY = 2;
        this.toggle = false;
        this.showSettings = showSettings;
    }
}
