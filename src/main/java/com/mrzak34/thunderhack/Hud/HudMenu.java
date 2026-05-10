package com.mrzak34.thunderhack.Hud;

public enum HudMenu {
    HUD("HUD", 320, 10, false, true);

    public String name;
    public int posX, posY;
    public boolean mouseClicked;
    public boolean showHuds;

    HudMenu(String name, int posX, int posY, boolean mouseClicked, boolean showHuds) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.mouseClicked = mouseClicked;
        this.showHuds = showHuds;
    }
}
